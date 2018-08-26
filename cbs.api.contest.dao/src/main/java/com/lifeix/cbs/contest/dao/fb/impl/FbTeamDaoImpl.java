package com.lifeix.cbs.contest.dao.fb.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.fb.FbTeamDao;
import com.lifeix.cbs.contest.dto.fb.FbTeam;
import com.lifeix.framework.memcache.MultiCacheResult;

@Service("fbTeamDao")
public class FbTeamDaoImpl extends ContestDaoSupport implements FbTeamDao {

    @Override
    public FbTeam selectById(long id) {
	String cacheKey = getCacheId(id);

	FbTeam team = memcacheService.get(cacheKey);

	if (team == null) {
	    team = sqlSession.selectOne("FbTeamMapper.selectById", id);
	    if (team != null) {
		memcacheService.set(cacheKey, team);
	    }
	}

	return team;
    }

    @Override
    public Boolean update(FbTeam team) {
	int res = sqlSession.update("FbTeamMapper.update", team);
	if (res > 0) {
	    // 更新比分和状态
	    deleteObjCache(team.getFtId());
	    return true;
	}
	return false;
    }

    @Override
    public List<FbTeam> searchTeam(String key) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("key", key);
	return sqlSession.selectList("FbTeamMapper.searchTeam", params);
    }

    @Override
    public Map<Long, FbTeam> findFbTeamMapByIds(List<Long> ids) {
	Map<Long, FbTeam> ret = new HashMap<Long, FbTeam>();
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
		    ret.put(Long.valueOf(revertCacheId(key)), (FbTeam) obj);
		} catch (Exception e) {
		    LOG.warn(String.format("FbTeamDaoImpl get multi cache fail %s - %s", key, e.getMessage()));
		}
	    }
	}
	if (!noCached.isEmpty()) {
	    List<FbTeam> teams = sqlSession.selectList("FbTeamMapper.selectByIds", revertMultiCacheId(noCached));
	    for (FbTeam team : teams) {
		ret.put(team.getFtId(), team);
		memcacheService.set(getCacheId(team.getFtId()), team);
	    }
	}
	return ret;
    }

    @Override
    public boolean saveTeam(List<FbTeam> list) {
	if (list == null | list.size() == 0)
	    return false;
	int res = sqlSession.insert("FbTeamMapper.saveTeam", list);
	return res > 0;
    }

    @Override
    public Map<Long, FbTeam> getAllTeam() {
	List<FbTeam> teamList = sqlSession.selectList("FbTeamMapper.getAllTeam");
	Map<Long, FbTeam> teamMap = new HashMap<Long, FbTeam>(teamList.size());
	for (FbTeam team : teamList) {
	    teamMap.put(team.getTargetId(), team);
	}
	return teamMap;
    }

    @Override
    public Map<Long, FbTeam> getTeamByTargetIds(List<Long> targetIds) {
	List<FbTeam> teamList = sqlSession.selectList("FbTeamMapper.getTeamByTargetIds", targetIds);
	Map<Long, FbTeam> teamMap = new HashMap<Long, FbTeam>(teamList.size());
	for (FbTeam team : teamList) {
	    teamMap.put(team.getTargetId(), team);
	}
	return teamMap;
    }

    /**
     * 对象缓存
     * 
     * @param contest
     * @param insertFlag
     */
    public void deleteObjCache(Long teamId) {

	// 单个对象缓存
	String cacheKey = getCacheId(teamId);
	memcacheService.delete(cacheKey);

    }

}
