package com.lifeix.cbs.contest.dao.bb.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.api.common.util.ContestConstants;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestStatu_BB;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestType;
import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.bb.BbContestDao;
import com.lifeix.cbs.contest.dto.bb.BbContest;
import com.lifeix.framework.memcache.MultiCacheResult;

@Service("bbContestDao")
public class BbContestDaoImpl extends ContestDaoSupport implements BbContestDao {

    @Override
    public BbContest selectById(long id) {
	String cacheKey = getCacheId(id);

	BbContest contest = memcacheService.get(cacheKey);

	if (contest == null) {
	    contest = sqlSession.selectOne("BbContestMapper.selectById", id);
	    if (contest != null) {
		memcacheService.set(cacheKey, contest);
	    }
	}

	return contest;
    }

    /**
     * 根据roomId获取赛事
     */
    @Override
    public BbContest selectByRoomId(long roomId) {
	BbContest contest = sqlSession.selectOne("BbContestMapper.selectByRoomId", roomId);
	return contest;
    }

    @Override
    public boolean update(BbContest contest) {

	int res = sqlSession.update("BbContestMapper.update", contest);
	if (res > 0) {
	    // 更新比分和状态
	    updateChanges(contest);

	    return true;
	}
	return false;
    }

    @Override
    public boolean delete(BbContest contest) {

	int delete = sqlSession.delete("BbContestMapper.delete", contest.getContestId());
	if (delete == 1) {
	    deleteObjCache(contest.getContestId());
	    deleteListCache(contest);
	    return true;
	}
	return false;
    }

    @Override
    public Map<Long, BbContest> findBbContestMapByIds(List<Long> ids) {
	Map<Long, BbContest> ret = new HashMap<Long, BbContest>();
	if (ids == null || ids.isEmpty()) {
	    return ret;
	}
	MultiCacheResult cacheResult = memcacheService.getMulti(getMultiCacheId(ids));
	Collection<String> noCached = cacheResult.getMissIds();
	Map<String, Object> cacheDatas = cacheResult.getCacheData();
	Iterator<String> it = cacheDatas.keySet().iterator();
	while (it.hasNext()) {
	    String key = it.next();
	    Object obj = cacheDatas.get(key);
	    if (obj != null) {
		try {
		    ret.put(Long.valueOf(revertCacheId(key)), (BbContest) obj);
		} catch (Exception e) {
		    LOG.warn(String.format("BbContestDaoImpl get multi cache fail %s - %s", key, e.getMessage()));
		}
	    }
	}
	if (!noCached.isEmpty()) {
	    List<BbContest> contests = sqlSession.selectList("BbContestMapper.selectByIds", revertMultiCacheId(noCached));
	    for (BbContest contest : contests) {
		ret.put(contest.getContestId(), contest);
		memcacheService.set(getCacheId(contest.getContestId()), contest);
	    }
	}
	return ret;
    }

    @Override
    public List<Long> findCupIdByRunOdds() {
	return sqlSession.selectList("BbContestMapper.findCupIdByRunOdds");
    }

    @Override
    public List<BbContest> findBbContestsByAbnormal(Long startId, Integer limit) {
	List<BbContest> contentMains = null;
	Map<String, Object> params = new HashMap<String, Object>();
	Calendar now = Calendar.getInstance();
	now.setTime(new Date());
	now.add(Calendar.HOUR_OF_DAY, -3);
	params.put("startTime", now.getTime());
	params.put("doneData", ContestStatu_BB.DONE);
	params.put("cancleData", ContestStatu_BB.CANCLE);
	if (startId != null) {
	    params.put("startId", startId);
	}
	params.put("limit", limit);
	contentMains = sqlSession.selectList("BbContestMapper.findBbContestsByAbnormal", params);
	return contentMains;
    }

    @Override
    public List<BbContest> findBbContestsForImportant(Date time) {
	DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
	String cacheKey = getCustomCache("findBbContestsForImportant", timeFormat.format(time));
	List<BbContest> contentMains = memcacheService.get(cacheKey);
	if (contentMains != null) {
	    return contentMains;
	}
	Date[] times = CbsTimeUtils.findBasicDayRange(time);
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("start", times[0]);
	params.put("end", times[1]);
	contentMains = sqlSession.selectList("BbContestMapper.findBbContestsForImportant", params);
	memcacheService.set(cacheKey, contentMains);
	return contentMains;
    }

    @Override
    public List<BbContest> findBbContestsByRangeTime(Date time, boolean fullFlag) {

	List<BbContest> contentMains = null;
	DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
	String cacheKey = getCustomCache("findBbContestsByRangeTime", timeFormat.format(time));
	if (!fullFlag) {
	    // 重要赛事会被缓存
	    contentMains = memcacheService.get(cacheKey);
	}
	if (contentMains != null) {
	    return contentMains;
	}
	Date[] times = CbsTimeUtils.findDayRange(time, ContestType.BASKETBALL);
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("start", times[0]);
	params.put("end", times[1]);
	params.put("limit", fullFlag ? ContestConstants.SQL_RET_LIMIT : 50);
	contentMains = sqlSession.selectList("BbContestMapper.findBbContestsByRangeTime", params);
	if (fullFlag) { // 全书赛事直接返回
	    return contentMains;
	}
	List<BbContest> filterContest = new ArrayList<BbContest>();
	if (contentMains.size() > 1) { // 只有数据大于1才进行过滤
	    int count = 0; // 计数
	    int lowCount = 0;// 野鸡赛事计数
	    for (BbContest bbContest : contentMains) {
		if (lowCount > 2 || (bbContest.getLevel() == 4 && count > 0)) { // 篮球野鸡赛事不能超过3个
		    break;
		}
		if (bbContest.getLevel() == 4) {
		    lowCount++;
		}
		if (bbContest.getLevel() == 1 || (count < 15)) {
		    filterContest.add(bbContest);
		    count++;
		}
	    }
	    // 按时间重新排序
	    Collections.sort(filterContest, new Comparator<BbContest>() {
		@Override
		public int compare(BbContest o1, BbContest o2) {
		    return o1.getStartTime().compareTo(o2.getStartTime());
		}
	    });
	} else {
	    filterContest.addAll(contentMains);
	}
	memcacheService.set(cacheKey, filterContest);
	return filterContest;
    }

    @Override
    public boolean updateChanges(BbContest contest) {
	int res = sqlSession.update("BbContestMapper.updateChanges", contest);

	if (res > 0) {
	    deleteObjCache(contest.getContestId());
	    deleteListCache(contest);
	    return true;
	}
	return false;
    }

    @Override
    public boolean updateSettle(BbContest entity) {
	int res = sqlSession.update("BbContestMapper.updateSettle", entity);

	if (res > 0) {
	    deleteObjCache(entity.getContestId());
	    deleteListCache(entity);
	    return true;
	}
	return false;
    }

    @Override
    public boolean updateRoom(Long contestId, Long roomId) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("contestId", contestId);
	params.put("roomId", roomId);
	int res = sqlSession.update("BbContestMapper.updateRoom", params);
	if (res > 0) {
	    deleteObjCache(contestId);
	    return true;
	}
	return false;
    }

    @Override
    public List<BbContest> findTimeoutContest(Long contestId, int limit) {
	Calendar now = Calendar.getInstance();
	now.setTime(new Date());
	now.add(Calendar.HOUR_OF_DAY, -3);
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("contestId", contestId);
	params.put("startTime", now.getTime());
	params.put("limit", limit);
	return sqlSession.selectList("BbContestMapper.findTimeoutContest", params);
    }

    /**
     * spark批量保存赛事
     * 
     * @param list
     * @return
     */
    @Override
    public boolean saveContest(List<BbContest> list) {
	if (list == null || list.size() == 0)
	    return false;
	int res = sqlSession.insert("BbContestMapper.saveContest", list);
	if (res > 0) {
	    for (BbContest contest : list) {
		deleteObjCache(contest.getContestId());
		deleteListCache(contest);
	    }
	}
	return res > 0;
    }

    /**
     * 对象缓存
     * 
     * @param contest
     * @param insertFlag
     */
    public void deleteObjCache(Long contestId) {

	// 单个对象缓存
	String cacheKey = getCacheId(contestId);
	memcacheService.delete(cacheKey);

    }

    @Override
    public List<Long> findBbContestIngNum(String nowTime) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("startTime", nowTime);
	List<Long> ids = sqlSession.selectList("BbContestMapper.findBbContestsIngNumber", params);
	return ids;
    }

    @Override
    public boolean updateInner(BbContest entity) {

	int res = sqlSession.update("BbContestMapper.updateInner", entity);
	if (res > 0) {
	    deleteObjCache(entity.getContestId());
	    deleteListCache(entity);
	    return true;
	}
	return false;
    }

    @Override
    public BbContest findBbContestsByTeam(Long homeTeam, Long awayTeam, String time) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("homeTeam", homeTeam);
	params.put("awayTeam", awayTeam);
	params.put("time", time);
	return sqlSession.selectOne("BbContestMapper.findBbContestsByTeam", params);

    }

    @Override
    public BbContest findNextBbContestsByCup(Date time, List<Long> cupIds) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("time", time);
	if (cupIds != null) {
	    params.put("ids", cupIds);
	}
	return sqlSession.selectOne("BbContestMapper.findNextBbContestsByCup", params);
    }

    @Override
    public BbContest findPrevBbContestsByCup(Date time, List<Long> cupIds) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("time", time);
	if (cupIds != null) {
	    params.put("ids", cupIds);
	}
	return sqlSession.selectOne("BbContestMapper.findPrevBbContestsByCup", params);
    }

    @Override
    public boolean rollback(Long contestId) {
	int res = sqlSession.update("BbContestMapper.updateByRollback", contestId);
	if (res > 0) {
	    deleteObjCache(contestId);
	    return true;
	}
	return false;
    }

    /**
     * 列表缓存
     * 
     * @param contest
     */
    public void deleteListCache(BbContest contest) {

	// 清除赛事列表缓存
	if (contest.getStartTime() != null) {

	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    String time1 = df.format(contest.getStartTime());
	    // 体育头条赛事列表
	    String cacheKey1 = getCustomCache("findBbContestsForImportant", time1);
	    memcacheService.delete(cacheKey1);

	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(contest.getStartTime());
	    // 小于11点则往后推一天
	    if (calendar.get(Calendar.HOUR_OF_DAY) < 12) {
		calendar.add(Calendar.DAY_OF_YEAR, -1);
	    }
	    String time2 = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

	    // 大赢家赛事列表
	    String cacheKey2 = getCustomCache("findBbContestsByRangeTime", time2);
	    memcacheService.delete(cacheKey2);

	}

    }

    @Override
    public Map<Long, String> findBbContestCupByRangeTime(Date start, Date end) {
	Map<Long, String> ret = new HashMap<Long, String>();

	Map<String, Object> params = new HashMap<String, Object>();
	params.put("start", start);
	params.put("end", end);
	List<BbContest> contentMains = sqlSession.selectList("BbContestMapper.findBbContestTypeByRangeTime", params);
	for (BbContest bean : contentMains) {
	    ret.put(bean.getCupId(), bean.getCupName());
	}
	return ret;
    }

    /**
     * 获取最近五天包含胜平负和让球胜平负的一级比赛来做赔率自检
     * 
     * @return
     */
    public List<BbContest> findBbContestsByCheck() {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(new Date());
	Date startTime = calendar.getTime();
	calendar.add(Calendar.DAY_OF_YEAR, 5);
	Date endTime = calendar.getTime();
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("start", startTime);
	params.put("end", endTime);
	params.put("limit", 200);
	return sqlSession.selectList("BbContestMapper.findBbContestsByCheck", params);
    }

	@Override
	public List<BbContest> findBbContestsBySame() {
		return sqlSession.selectList("BbContestMapper.findBbContestsBySame");
	}
}
