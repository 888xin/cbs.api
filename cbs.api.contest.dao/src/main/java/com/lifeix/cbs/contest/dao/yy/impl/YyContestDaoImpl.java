package com.lifeix.cbs.contest.dao.yy.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.yy.YyContestDao;
import com.lifeix.cbs.contest.dto.yy.YyContest;
import com.lifeix.cbs.contest.dto.yy.YyCup;
import com.lifeix.framework.memcache.MultiCacheResult;

@Service("yyContestDao")
public class YyContestDaoImpl extends ContestDaoSupport implements YyContestDao {

    @Override
    public YyContest selectById(long id) {
	String cacheKey = getCacheId(id);

	YyContest contest = memcacheService.get(cacheKey);
	if (contest == null) {
	    contest = sqlSession.selectOne("YyContestMapper.selectById", id);
	    if (contest != null) {
		memcacheService.set(cacheKey, contest);
	    }
	}
	return contest;
    }

    @Override
    public Map<Long, YyContest> findYyContestMapByIds(List<Long> ids) {
	Map<Long, YyContest> ret = new HashMap<Long, YyContest>();
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
		    ret.put(Long.valueOf(revertCacheId(key)), (YyContest) obj);
		} catch (Exception e) {
		    LOG.warn(String.format("YyContestDaoImpl get multi cache fail %s - %s", key, e.getMessage()));
		}
	    }
	}
	if (!noCached.isEmpty()) {
	    List<YyContest> contests = sqlSession.selectList("YyContestMapper.selectByIds", revertMultiCacheId(noCached));
	    for (YyContest contest : contests) {
		ret.put(contest.getId(), contest);
		memcacheService.set(getCacheId(contest.getId()), contest);
	    }
	}
	return ret;
    }

    @Override
    public boolean insert(YyContest contest) {
	int res = sqlSession.insert("YyContestMapper.insert", contest);
	return res > 0;
    }

    @Override
    public boolean update(YyContest contest) {

	int res = sqlSession.update("YyContestMapper.update", contest);
	if (res > 0) {
	    deleteObjCache(contest.getId());
	    return true;
	}
	return false;
    }

    @Override
    public boolean updateHide(Long contestId, boolean hideFlag) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("id", contestId);
	params.put("hideFlag", hideFlag);

	int delete = sqlSession.delete("YyContestMapper.updateHide", params);
	if (delete == 1) {
	    deleteObjCache(contestId);
	    return true;
	}
	return false;
    }

    @Override
    public List<YyContest> findBetYyContests(Long cupId, Long startId, int limit) {
	List<YyContest> contentMains = null;
	Map<String, Object> params = new HashMap<String, Object>();
	if (cupId != null) {
	    params.put("cupId", cupId);
	}
	if (startId != null) {
	    params.put("startId", startId);
	}
	params.put("limit", limit);
	contentMains = sqlSession.selectList("YyContestMapper.findBetYyContests", params);
	return contentMains;
    }

    @Override
    public List<YyCup> findBetYyCups() {
	String cacheKey = getCustomCache("findBetYyCups");
	List<YyCup> ret = memcacheService.get(cacheKey);
	if (ret != null) {
	    return ret;
	}
	List<YyContest> contents = sqlSession.selectList("YyContestMapper.findBetYyCups");
	ret = new ArrayList<>();
	for (YyContest bean : contents) {
	    YyCup cup = new YyCup();
	    cup.setCupId(bean.getCupId());
	    cup.setCupName(bean.getCupName());
	    ret.add(cup);
	}
	memcacheService.set(cacheKey, ret);
	return ret;
    }

    @Override
    public List<YyContest> findYyContests(Boolean hideFlag, Boolean type, Long cupId, Long startId, int limit) {
	List<YyContest> contentMains = null;
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("hideFlag", hideFlag);
	if (type != null) {
	    params.put("type", type);
	}
	if (cupId != null) {
	    params.put("cupId", cupId);
	}
	if (startId != null) {
	    params.put("startId", startId);
	}
	params.put("limit", limit);
	contentMains = sqlSession.selectList("YyContestMapper.findYyContests", params);
	return contentMains;
    }

    @Override
    public List<YyContest> findTimeoutContest(Long contestId, int limit) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("contestId", contestId);
	params.put("limit", limit);
	return sqlSession.selectList("YyContestMapper.findTimeoutContest", params);
    }

    @Override
    public Integer settleNum() {
	return sqlSession.selectOne("YyContestMapper.settleNum");
    }

    /**
     * 对象缓存
     */
    public void deleteObjCache(Long contestId) {

	if (contestId != null) {
	    // 单个对象缓存
	    String cacheKey = getCacheId(contestId);
	    memcacheService.delete(cacheKey);
	}

	// 清除可下注的押押分类缓存
	String cacheKey = getCustomCache("findBetYyCups");
	memcacheService.delete(cacheKey);

    }

}
