package com.lifeix.cbs.contest.dao.fb.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.fb.FbOddsOpDao;
import com.lifeix.cbs.contest.dto.odds.OddsOp;
import com.lifeix.framework.memcache.MultiCacheResult;

@Service("fbOpDao")
public class FbOddsOpDaoImpl extends ContestDaoSupport implements FbOddsOpDao {

    @Override
    public OddsOp selectById(long id) {
	return sqlSession.selectOne("FbOddsOpMapper.selectById", id);
    }

    @Override
    public boolean update(OddsOp entity) {
	int res = sqlSession.update("FbOddsOpMapper.update", entity);
	if (res > 0) {
	    // 更新比分和状态
	    deleteObjCache(entity.getContestId());
	    return true;
	}
	return false;
    }

    @Override
    public boolean closeOdds(Long contestId) {
	int res = sqlSession.update("FbOddsOpMapper.closeOdds", contestId);
	if (res > 0) {
	    deleteObjCache(contestId);
	    return true;
	}
	return false;
    }

    @Override
    public boolean openOdds(Long contestId) {
        int res = sqlSession.update("FbOddsOpMapper.openOdds", contestId);
        if (res > 0) {
            deleteObjCache(contestId);
            return true;
        }
        return false;
    }

    @Override
    public OddsOp selectByContest(Long contestId) {
	String cacheKey = getCacheId(contestId);

	OddsOp odds = memcacheService.get(cacheKey);

	if (odds == null) {
	    odds = sqlSession.selectOne("FbOddsOpMapper.selectByContest", contestId);
	    if (odds != null) {
		memcacheService.set(cacheKey, odds);
	    }
	}
	return odds;
    }

    @Override
    public Map<Long, OddsOp> findOddsOpMapByIds(List<Long> ids) {
	Map<Long, OddsOp> ret = new HashMap<Long, OddsOp>();
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
		    ret.put(Long.valueOf(revertCacheId(key)), (OddsOp) obj);
		} catch (Exception e) {
		    LOG.warn(String.format("FbOddsOpDaoImpl get multi cache fail %s - %s", key, e.getMessage()));
		}
	    }
	}
	if (!noCached.isEmpty()) {
	    List<OddsOp> oddss = sqlSession.selectList("FbOddsOpMapper.selectByIds", revertMultiCacheId(noCached));
	    for (OddsOp odds : oddss) {
		ret.put(odds.getContestId(), odds);
		memcacheService.set(getCacheId(odds.getContestId()), odds);
	    }
	}
	return ret;
    }

    @Override
    public List<OddsOp> findOdds(Long startId, int limit, Boolean isFive, Integer byOrder) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("startId", startId);
	params.put("belongFive", isFive);
	params.put("limit", limit);
	params.put("byOrder", byOrder);
	return sqlSession.selectList("FbOddsOpMapper.findOdds", params);
    }

    @Override
    public boolean saveOdds(List<OddsOp> list) {
	if (list == null || list.size() == 0) {
	    return false;
	}
	int res = sqlSession.insert("FbOddsOpMapper.saveOdds", list);
	if (res > 0) {
	    Set<Long> contestIdSet = new HashSet<Long>(list.size());
	    for (OddsOp odds : list) {
		contestIdSet.add(odds.getContestId());
	    }
	    for (Long contestId : contestIdSet) {
		deleteObjCache(contestId);
	    }
	}
	return res > 0;
    }

    /**
     * 对象缓存
     * 
     * @param contestId
     */
    public void deleteObjCache(Long contestId) {

	// 单个对象缓存
	String cacheKey = getCacheId(contestId);
	memcacheService.delete(cacheKey);

    }

}
