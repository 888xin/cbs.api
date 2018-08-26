package com.lifeix.cbs.contest.dao.bb.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.bb.BbPlayerDao;
import com.lifeix.cbs.contest.dto.bb.BbPlayer;
import com.lifeix.framework.memcache.MultiCacheResult;

@Service("bbPlayerDao")
public class BbPlayerDaoImpl extends ContestDaoSupport implements BbPlayerDao {

    @Override
    public BbPlayer selectById(Long id) {
	return sqlSession.selectOne("BbPlayerMapper.selectById", id);
    }

    @Override
    public List<BbPlayer> selectByName(String name) {
	return sqlSession.selectList("BbPlayerMapper.selectByName", name);
    }

    @Override
    public BbPlayer selectByTargetId(String targetId) {
	String cacheKey = getCacheId(targetId);
	BbPlayer player = memcacheService.get(cacheKey);
	if (player == null) {
	    player = sqlSession.selectOne("BbPlayerMapper.selectByTargetId", targetId);
	}
	return player;
    }

    @Override
    public List<BbPlayer> selectMore(Long startId, Integer limit) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("startId", startId);
	params.put("num", limit);
	return sqlSession.selectList("BbPlayerMapper.selectMore", params);
    }

    @Override
    public boolean update(BbPlayer player) {
	boolean flag = sqlSession.update("BbPlayerMapper.update", player) > 0;
	if (flag) {
	    deleteCache(player.getTargetId());
	}
	return flag;
    }

    @Override
    public boolean updateTeamId(List<BbPlayer> list) {
	if (list == null || list.isEmpty())
	    return false;
	boolean flag = sqlSession.update("BbPlayerMapper.updateTeamId", list) > 0;
	if (flag) {
	    for (BbPlayer player : list) {
		deleteCache(player.getTargetId());
	    }
	}
	return flag;
    }

    @Override
    public boolean savePlayers(List<BbPlayer> list) {
	if (list == null || list.size() == 0)
	    return false;
	boolean flag = sqlSession.insert("BbPlayerMapper.savePlayers", list) > 0;
	if (flag) {
	    for (BbPlayer player : list) {
		deleteCache(player.getTargetId());
	    }
	}
	return flag;
    }

    @Override
    public Map<String, BbPlayer> selectByTargetIds(List<String> targetIds) {
	Map<String, BbPlayer> ret = new HashMap<String, BbPlayer>();
	if (targetIds == null || targetIds.isEmpty()) {
	    return ret;
	}
	MultiCacheResult cacheResult = memcacheService.getMulti(getMultiCacheId(targetIds));
	Collection<String> noCached = cacheResult.getMissIds();
	Map<String, Object> cacheDatas = cacheResult.getCacheData();
	Iterator<String> it = cacheDatas.keySet().iterator();
	while (it.hasNext()) {
	    String key = it.next();
	    Object obj = cacheDatas.get(key);
	    if (obj != null) {
		try {
		    ret.put(revertCacheId(key), (BbPlayer) obj);
		} catch (Exception e) {
		    LOG.warn(String.format("BbPlayerDaoImpl get multi cache fail %s - %s", key, e.getMessage()));
		}
	    }
	}
	if (!noCached.isEmpty()) {
	    List<BbPlayer> players = sqlSession.selectList("BbPlayerMapper.selectByTargetIds", revertMultiCacheId(noCached));
	    for (BbPlayer player : players) {
		ret.put(player.getTargetId(), player);
		memcacheService.set(getCacheId(player.getTargetId()), player);
	    }
	}
	return ret;
    }

    @Override
    public Map<String, BbPlayer> getAllPlayers() {
	List<BbPlayer> playerList = sqlSession.selectList("BbPlayerMapper.getAllPlayers");
	Map<String, BbPlayer> playerMap = new HashMap<String, BbPlayer>(playerList.size());
	for (BbPlayer player : playerList) {
	    playerMap.put(player.getTargetId(), player);
	}
	return playerMap;
    }

    private void deleteCache(String targetId) {
	String cacheKey = getCacheId(targetId);
	memcacheService.delete(cacheKey);
    }

    @Override
    public List<BbPlayer> selectByTeamId(Long teamId) {
	return sqlSession.selectList("BbPlayerMapper.selectByTeamId", teamId);
    }
}
