package com.lifeix.cbs.contest.dao.bb.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.bb.BbTeamDao;
import com.lifeix.cbs.contest.dto.bb.BbTeam;
import com.lifeix.framework.memcache.MultiCacheResult;

@Service("bbTeamDao")
public class BbTeamDaoImpl extends ContestDaoSupport implements BbTeamDao {

    @Override
    public BbTeam selectById(long id) {
	String cacheKey = getCacheId(id);

	BbTeam team = memcacheService.get(cacheKey);

	if (team == null) {
	    team = sqlSession.selectOne("BbTeamMapper.selectById", id);
	    if (team != null) {
		memcacheService.set(cacheKey, team);
	    }
	}

	return team;
    }

    @Override
    public Boolean update(BbTeam team) {
	int res = sqlSession.update("BbTeamMapper.update", team);
	if (res > 0) {
	    // 更新比分和状态
	    deleteObjCache(team.getBtId());
	    return true;
	}
	return false;
    }

    @Override
    public List<BbTeam> searchTeam(String key) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("key", key);
	return sqlSession.selectList("BbTeamMapper.searchTeam", params);
    }

    @Override
    public Map<Long, BbTeam> findBbTeamMapByIds(List<Long> ids) {
	Map<Long, BbTeam> ret = new HashMap<Long, BbTeam>();
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
		    ret.put(Long.valueOf(revertCacheId(key)), (BbTeam) obj);
		} catch (Exception e) {
		    LOG.warn(String.format("BbTeamDaoImpl get multi cache fail %s - %s", key, e.getMessage()));
		}
	    }
	}
	if (!noCached.isEmpty()) {
	    List<BbTeam> teams = sqlSession.selectList("BbTeamMapper.selectByIds", revertMultiCacheId(noCached));
	    for (BbTeam team : teams) {
		ret.put(team.getBtId(), team);
		memcacheService.set(getCacheId(team.getBtId()), team);
	    }
	}
	return ret;
    }

    @Override
    public boolean saveTeam(List<BbTeam> list) {
	if (list == null | list.size() == 0)
	    return false;
	int res = sqlSession.insert("BbTeamMapper.saveTeam", list);
	return res > 0;
    }

    @Override
    public Map<Long, BbTeam> getAllTeam() {
	List<BbTeam> teamList = sqlSession.selectList("BbTeamMapper.getAllTeam");
	Map<Long, BbTeam> teamMap = new HashMap<Long, BbTeam>(teamList.size());
	for (BbTeam team : teamList) {
	    teamMap.put(team.getTargetId(), team);
	}
	return teamMap;
    }

    @Override
    public Map<Long, BbTeam> getTeamByTargetIds(List<Long> targetIds) {
	List<BbTeam> teamList = sqlSession.selectList("BbTeamMapper.getTeamByTargetIds", targetIds);
	Map<Long, BbTeam> teamMap = new HashMap<Long, BbTeam>(teamList.size());
	for (BbTeam team : teamList) {
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
