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
import com.lifeix.cbs.contest.dao.bb.BbOddsSizeDao;
import com.lifeix.cbs.contest.dto.odds.OddsSize;
import com.lifeix.framework.memcache.MultiCacheResult;

@Service("bbSizeDao")
public class BbOddsSizeDaoImpl extends ContestDaoSupport implements BbOddsSizeDao {

    @Override
    public OddsSize selectById(long id) {
	return sqlSession.selectOne("BbOddsSizeMapper.selectById", id);
    }

    @Override
    public boolean update(OddsSize entity) {
	int res = sqlSession.update("BbOddsSizeMapper.update", entity);
	if (res > 0) {
	    // 更新比分和状态
	    deleteObjCache(entity.getContestId());
	    return true;
	}
	return false;
    }

    @Override
    public boolean closeOdds(Long contestId) {
	int res = sqlSession.update("BbOddsSizeMapper.closeOdds", contestId);
	if (res > 0) {
	    deleteObjCache(contestId);
	    return true;
	}
	return false;
    }

    @Override
    public boolean openOdds(Long contestId) {
	int res = sqlSession.update("BbOddsSizeMapper.openOdds", contestId);
	if (res > 0) {
	    deleteObjCache(contestId);
	    return true;
	}
	return false;
    }

    @Override
    public OddsSize selectByContest(Long contestId) {
	String cacheKey = getCacheId(contestId);

	OddsSize odds = memcacheService.get(cacheKey);

	if (odds == null) {
	    odds = sqlSession.selectOne("BbOddsSizeMapper.selectByContest", contestId);
	    if (odds != null) {
		memcacheService.set(cacheKey, odds);
	    }
	}
	return odds;
    }

    @Override
    public Map<Long, OddsSize> findOddsSizeMapByIds(List<Long> ids) {
	Map<Long, OddsSize> ret = new HashMap<Long, OddsSize>();
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
		    ret.put(Long.valueOf(revertCacheId(key)), (OddsSize) obj);
		} catch (Exception e) {
		    LOG.warn(String.format("BbOddsSizeDaoImpl get multi cache fail %s - %s", key, e.getMessage()));
		}
	    }
	}
	if (!noCached.isEmpty()) {
	    List<OddsSize> oddss = sqlSession.selectList("BbOddsSizeMapper.selectByIds", revertMultiCacheId(noCached));
	    for (OddsSize odds : oddss) {
		ret.put(odds.getContestId(), odds);
		memcacheService.set(getCacheId(odds.getContestId()), odds);
	    }
	}
	return ret;
    }

    @Override
    public List<OddsSize> findOdds(Long startId, int limit, Boolean isFive, Integer byOrder) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("startId", startId);
	params.put("belongFive", isFive);
	params.put("byOrder", byOrder);
	params.put("limit", limit);
	return sqlSession.selectList("BbOddsSizeMapper.findOdds", params);
    }

    @Override
    public boolean saveOdds(List<OddsSize> list) {
	if (list == null || list.size() == 0) {
	    return false;
	}
	int res = sqlSession.insert("BbOddsSizeMapper.saveOdds", list);
	if (res > 0) {
	    Set<Long> contestIdSet = new HashSet<Long>(list.size());
	    for (OddsSize odds : list) {
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
