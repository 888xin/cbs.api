package com.lifeix.cbs.contest.dao.fb.impl;

import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.api.common.util.ContestConstants;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestStatu;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestType;
import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.fb.FbContestDao;
import com.lifeix.cbs.contest.dto.fb.FbContest;
import com.lifeix.framework.memcache.MultiCacheResult;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("fbContestDao")
public class FbContestDaoImpl extends ContestDaoSupport implements FbContestDao {

    @Override
    public FbContest selectById(long id) {
	String cacheKey = getCacheId(id);

	FbContest contest = memcacheService.get(cacheKey);

	if (contest == null) {
	    contest = sqlSession.selectOne("FbContestMapper.selectById", id);
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
    public FbContest selectByRoomId(long roomId) {
	FbContest contest = sqlSession.selectOne("FbContestMapper.selectByRoomId", roomId);
	return contest;
    }

    @Override
    public boolean update(FbContest contest) {

	int res = sqlSession.update("FbContestMapper.update", contest);
	if (res > 0) {
	    // 更新比分和状态
	    updateChanges(contest);
	    return true;
	}
	return false;
    }

    @Override
    public boolean delete(FbContest contest) {

	int delete = sqlSession.delete("FbContestMapper.delete", contest.getContestId());
	if (delete == 1) {
	    deleteObjCache(contest.getContestId());
	    deleteListCache(contest);
	    return true;
	}
	return false;
    }

    @Override
    public Map<Long, FbContest> findFbContestMapByIds(List<Long> ids) {
	Map<Long, FbContest> ret = new HashMap<Long, FbContest>();
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
		    ret.put(Long.valueOf(revertCacheId(key)), (FbContest) obj);
		} catch (Exception e) {
		    LOG.warn(String.format("FbContestDaoImpl get multi cache fail %s - %s", key, e.getMessage()));
		}
	    }
	}
	if (!noCached.isEmpty()) {
	    List<FbContest> contests = sqlSession.selectList("FbContestMapper.selectByIds", revertMultiCacheId(noCached));
	    for (FbContest contest : contests) {
		ret.put(contest.getContestId(), contest);
		memcacheService.set(getCacheId(contest.getContestId()), contest);
	    }
	}
	return ret;
    }

    @Override
    public List<Long> findCupIdByRunOdds() {
	return sqlSession.selectList("FbContestMapper.findCupIdByRunOdds");
    }

    @Override
    public List<FbContest> findFbContestsByAbnormal(Long startId, Integer limit) {
	List<FbContest> contentMains = null;
	Map<String, Object> params = new HashMap<String, Object>();
	Calendar now = Calendar.getInstance();
	now.setTime(new Date());
	now.add(Calendar.HOUR_OF_DAY, -3);
	params.put("startTime", now.getTime());
	params.put("doneData", ContestStatu.DONE);
	params.put("cancleData", ContestStatu.CANCLE);
	if (startId != null) {
	    params.put("startId", startId);
	}
	params.put("limit", limit);
	contentMains = sqlSession.selectList("FbContestMapper.findFbContestsByAbnormal", params);
	return contentMains;
    }

    @Override
    public List<FbContest> findFbContestsForImportant(Date time) {
	DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
	String cacheKey = getCustomCache("findFbContestsForImportant", timeFormat.format(time));
	List<FbContest> contentMains = memcacheService.get(cacheKey);
	if (contentMains != null) {
	    return contentMains;
	}
	Date[] times = CbsTimeUtils.findBasicDayRange(time);
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("start", times[0]);
	params.put("end", times[1]);
	contentMains = sqlSession.selectList("FbContestMapper.findFbContestsForImportant", params);
	memcacheService.set(cacheKey, contentMains);
	return contentMains;
    }

    /**
     * 一级赛事全部入选 ，一级赛事不够20个用二级和三级赛事补充 ，一二三级赛事不够20个，4级赛事补充不能超过10个
     */
    @Override
    public List<FbContest> findFbContestsByRangeTime(Date time, boolean fullFlag) {

	List<FbContest> contentMains = null;
	DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
	String cacheKey = getCustomCache("findFbContestsByRangeTime", timeFormat.format(time));
	if (!fullFlag) {
	    // 重要赛事会被缓存
	    contentMains = memcacheService.get(cacheKey);
	}
	if (contentMains != null) {
	    return contentMains;
	}
	Date[] times = CbsTimeUtils.findDayRange(time, ContestType.FOOTBALL);
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("start", times[0]);
	params.put("end", times[1]);
	params.put("limit", fullFlag ? ContestConstants.SQL_RET_LIMIT : 50);
	contentMains = sqlSession.selectList("FbContestMapper.findFbContestsByRangeTime", params);
	if (fullFlag) { // 全书赛事直接返回
	    return contentMains;
	}
	List<FbContest> filterContest = new ArrayList<FbContest>();
	if (contentMains.size() > 1) { // 只有数据大于1才进行过滤
	    int count = 0; // 计数
	    int lowCount = 0;// 野鸡赛事计数
	    for (FbContest fbContest : contentMains) {
		if (lowCount > 2 || (fbContest.getLevel() == 4 && count > 0)) { // 篮球野鸡赛事不能超过3个
		    break;
		}
		if (fbContest.getLevel() == 4) {
		    lowCount++;
		}
		if (fbContest.getLevel() == 1 || (count < 15)) {
		    filterContest.add(fbContest);
		    count++;
		}
	    }
	    // 按时间重新排序
	    Collections.sort(filterContest, new Comparator<FbContest>() {
		@Override
		public int compare(FbContest o1, FbContest o2) {
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
    public List<FbContest> findTimeoutContest(Long contestId, int limit) {
	Calendar now = Calendar.getInstance();
	now.setTime(new Date());
	now.add(Calendar.HOUR_OF_DAY, -3);
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("contestId", contestId);
	params.put("startTime", now.getTime());
	params.put("limit", limit);
	return sqlSession.selectList("FbContestMapper.findTimeoutContest", params);
    }

    @Override
    public List<FbContest> findCancleContest(int limit) {
	Calendar now = Calendar.getInstance();
	now.setTime(new Date());
	now.add(Calendar.DATE, 20);
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("startTime", now.getTime());
	params.put("limit", limit);
	return sqlSession.selectList("FbContestMapper.findCancleContest", params);
    }

    @Override
    public boolean updateChanges(FbContest contest) {
	int res = sqlSession.update("FbContestMapper.updateChanges", contest);

	if (res > 0) {
	    deleteObjCache(contest.getContestId());
	    deleteListCache(contest);
	    return true;
	}
	return false;
    }

    @Override
    public boolean updateSettle(FbContest entity) {
	int res = sqlSession.update("FbContestMapper.updateSettle", entity);

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
	int res = sqlSession.update("FbContestMapper.updateRoom", params);

	if (res > 0) {
	    deleteObjCache(contestId);
	    return true;
	}
	return false;
    }

    /**
     * spark批量保存赛事
     * 
     * @param list
     * @return
     */
    @Override
    public boolean saveContest(List<FbContest> list) {
	if (list == null || list.size() == 0)
	    return false;
	int res = sqlSession.insert("FbContestMapper.saveContest", list);
	if (res > 0) {
	    for (FbContest contest : list) {
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

    /**
     * 获取正在进行的一级足球比赛的id列表
     */
    @Override
    public List<Long> findFbContestIngNum(String nowTime) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("startTime", nowTime);
	List<Long> ids = sqlSession.selectList("FbContestMapper.findFbContestsIngNumber", params);
	return ids;
    }

    @Override
    public boolean updateInner(FbContest entity) {

	int res = sqlSession.update("FbContestMapper.updateInner", entity);
	if (res > 0) {
	    deleteObjCache(entity.getContestId());
	    deleteListCache(entity);
	    return true;
	}
	return false;
    }

    @Override
    public FbContest findNextFbContestsByCup(Date time, List<Long> cupIds) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("time", time);
	if (cupIds != null) {
	    params.put("ids", cupIds);
	}
	return sqlSession.selectOne("FbContestMapper.findNextFbContestsByCup", params);
    }

    @Override
    public FbContest findPrevFbContestsByCup(Date time, List<Long> cupIds) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("time", time);
	if (cupIds != null) {
	    params.put("ids", cupIds);
	}
	return sqlSession.selectOne("FbContestMapper.findPrevFbContestsByCup", params);
    }

    @Override
    public boolean rollback(Long contestId) {
	int res = sqlSession.update("FbContestMapper.updateByRollback", contestId);
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
    public void deleteListCache(FbContest contest) {

	// 清除赛事列表缓存
	if (contest.getStartTime() != null) {
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    String time1 = df.format(contest.getStartTime());
	    // 体育头条赛事列表
	    String cacheKey1 = getCustomCache("findFbContestsForImportant", time1);
	    memcacheService.delete(cacheKey1);

	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(contest.getStartTime());
	    // 小于11点则往后推一天
	    if (calendar.get(Calendar.HOUR_OF_DAY) < 11) {
		calendar.add(Calendar.DAY_OF_YEAR, -1);
	    }
	    String time2 = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

	    // 大赢家赛事列表
	    String cacheKey2 = getCustomCache("findFbContestsByRangeTime", time2);
	    memcacheService.delete(cacheKey2);

	}

    }

    /**
     * 根据主客队获取比赛
     */
    @Override
    public FbContest selectByTeam(Long homeTeam, Long awayTeam, String time) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("homeTeam", homeTeam);
	params.put("awayTeam", awayTeam);
	params.put("time", time);
	return sqlSession.selectOne("FbContestMapper.selectByTeam", params);
    }

    /**
     * 更新比赛最终结果
     * 
     * @param list
     * @return
     */
    @Override
    public boolean updateFinalResults(List<FbContest> list) {
	if (list == null || list.isEmpty())
	    return false;
	boolean flag = sqlSession.update("FbContestMapper.updateFinalResults", list) > 0;
	if (flag) {
	    for (FbContest contest : list) {
		deleteObjCache(contest.getContestId());
		deleteListCache(contest);
	    }
	}
	return flag;
    }

    @Override
    public Map<Long, String> findFbContestCupByRangeTime(Date start, Date end) {
	Map<Long, String> ret = new HashMap<Long, String>();
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("start", start);
	params.put("end", end);
	List<FbContest> contentMains = sqlSession.selectList("FbContestMapper.findFbContestTypeByRangeTime", params);
	for (FbContest bean : contentMains) {
	    ret.put(bean.getCupId(), bean.getCupName());
	}
	return ret;
    }

    /**
     * 获取最近五天包含胜平负和让球胜平负的一级比赛来做赔率自检
     * 
     * @return
     */
    public List<FbContest> findFbContestsByCheck() {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(new Date());
	Date startTime = calendar.getTime();
	calendar.add(Calendar.DAY_OF_YEAR, 5);
	Date endTime = calendar.getTime();
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("start", startTime);
	params.put("end", endTime);
	params.put("limit", 200);
	return sqlSession.selectList("FbContestMapper.findFbContestsByCheck", params);
    }

	@Override
	public List<FbContest> findFbContestsBySame() {
		return sqlSession.selectList("FbContestMapper.findFbContestsBySame");
	}
}
