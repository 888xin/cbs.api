package com.lifeix.cbs.contest.dao.bb.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.bb.BbOddsJcDao;
import com.lifeix.cbs.contest.dto.odds.OddsJc;
import com.lifeix.framework.memcache.MultiCacheResult;

@Service("bbJcDao")
public class BbOddsJcDaoImpl extends ContestDaoSupport implements BbOddsJcDao {

    @Override
    public OddsJc selectById(long id) {
	return sqlSession.selectOne("BbOddsJcMapper.selectById", id);
    }

    @Override
    public boolean update(OddsJc entity) {
	int res = sqlSession.update("BbOddsJcMapper.update", entity);
	if (res > 0) {
	    // 更新比分和状态
	    deleteObjCache(entity.getContestId());
	    return true;
	}
	return false;
    }

    @Override
    public boolean closeOdds(Long contestId) {
	int res = sqlSession.update("BbOddsJcMapper.closeOdds", contestId);
	if (res > 0) {
	    deleteObjCache(contestId);
	    return true;
	}
	return false;
    }

    @Override
    public boolean openOdds(Long contestId) {
        int res = sqlSession.update("BbOddsJcMapper.openOdds", contestId);
        if (res > 0) {
            deleteObjCache(contestId);
            return true;
        }
        return false;
    }

    @Override
    public OddsJc selectByContest(Long contestId) {
	String cacheKey = getCacheId(contestId);

	OddsJc odds = memcacheService.get(cacheKey);

	if (odds == null) {
	    odds = sqlSession.selectOne("BbOddsJcMapper.selectByContest", contestId);
	    if (odds != null) {
		memcacheService.set(cacheKey, odds);
	    }
	}
	return odds;
    }

    @Override
    public Map<Long, OddsJc> findOddsJcMapByIds(List<Long> ids) {
	Map<Long, OddsJc> ret = new HashMap<Long, OddsJc>();
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
		    ret.put(Long.valueOf(revertCacheId(key)), (OddsJc) obj);
		} catch (Exception e) {
		    LOG.warn(String.format("BbOddsJcDaoImpl get multi cache fail %s - %s", key, e.getMessage()));
		}
	    }
	}
	if (!noCached.isEmpty()) {
	    List<OddsJc> oddss = sqlSession.selectList("BbOddsJcMapper.selectByIds", revertMultiCacheId(noCached));
	    for (OddsJc odds : oddss) {
		ret.put(odds.getContestId(), odds);
		memcacheService.set(getCacheId(odds.getContestId()), odds);
	    }
	}
	return ret;
    }

    @Override
    public List<OddsJc> findOdds(Long startId, int limit, Boolean isFive, Integer byOrder) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("startId", startId);
	params.put("belongFive", isFive);
	params.put("byOrder", byOrder);
	params.put("limit", limit);
	return sqlSession.selectList("BbOddsJcMapper.findOdds", params);
    }

    @Override
    public boolean saveOdds(List<OddsJc> list) {
	if (list == null || list.size() == 0) {
	    return false;
	}
	int res = sqlSession.insert("BbOddsJcMapper.saveOdds", list);
	if (res > 0) {
	    Set<Long> contestIdSet = new HashSet<Long>(list.size());
	    for (OddsJc odds : list) {
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
