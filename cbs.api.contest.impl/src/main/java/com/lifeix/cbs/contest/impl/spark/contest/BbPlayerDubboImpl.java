package com.lifeix.cbs.contest.impl.spark.contest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.lifeix.cbs.contest.bean.bb.ext.BbPlayerResponse;
import com.lifeix.cbs.contest.dao.bb.BbPlayerDao;
import com.lifeix.cbs.contest.dto.bb.BbPlayer;
import com.lifeix.cbs.contest.service.spark.contest.BbPlayerDubbo;
import com.lifeix.cbs.contest.util.transform.ContestTransformUtil;

public class BbPlayerDubboImpl implements BbPlayerDubbo {

    @Autowired
    private BbPlayerDao bbPlayerDao;

    @Override
    public boolean savePlayers(List<BbPlayerResponse> respList) {
	if (respList == null || respList.isEmpty())
	    return false;
	List<BbPlayer> list = new ArrayList<BbPlayer>(respList.size());
	for (BbPlayerResponse resp : respList) {
	    BbPlayer player = new BbPlayer();
	    player.setFirstName(resp.getFirst_name());
	    player.setLastName(resp.getLast_name());
	    player.setName(resp.getName());
	    player.setTargetId(resp.getPlayer_id());
	    player.setTeamId(resp.getTeam_id());
	    list.add(player);
	}
	return bbPlayerDao.savePlayers(list);
    }

    @Override
    public boolean updatePlayersTeamId(List<BbPlayerResponse> respList) {
	if (respList == null || respList.isEmpty())
	    return false;
	List<String> targetIds = new ArrayList<String>(respList.size());
	for (BbPlayerResponse resp : respList) {
	    targetIds.add(resp.getPlayer_id());
	}
	Map<String, BbPlayer> playerMap = bbPlayerDao.selectByTargetIds(targetIds);
	List<BbPlayer> playerList = new ArrayList<BbPlayer>(playerMap.size());
	for (BbPlayerResponse resp : respList) {
	    BbPlayer player = playerMap.get(resp.getPlayer_id());
	    if (player != null) {
		player.setTeamId(resp.getTeam_id());
		playerList.add(player);
	    }
	}
	return bbPlayerDao.updateTeamId(playerList);
    }

    @Override
    public Map<String, BbPlayerResponse> getAllPlayers() {
	Map<String, BbPlayer> playerMap = bbPlayerDao.getAllPlayers();
	Set<String> keySet = playerMap.keySet();
	Map<String, BbPlayerResponse> respMap = new HashMap<String, BbPlayerResponse>(playerMap.size());
	for (String key : keySet) {
	    BbPlayer player = playerMap.get(key);
	    BbPlayerResponse resp = ContestTransformUtil.transformBbPlayer(player, null);
	    respMap.put(key, resp);
	}
	return respMap;
    }

    @Override
    public Map<String, BbPlayerResponse> getPlayersByTargetIds(List<String> targetIds) {
	Map<String, BbPlayer> playerMap = bbPlayerDao.selectByTargetIds(targetIds);
	Set<String> keySet = playerMap.keySet();
	Map<String, BbPlayerResponse> respMap = new HashMap<String, BbPlayerResponse>(playerMap.size());
	for (String key : keySet) {
	    BbPlayer player = playerMap.get(key);
	    BbPlayerResponse resp = ContestTransformUtil.transformBbPlayer(player, null);
	    respMap.put(key, resp);
	}
	return respMap;
    }
}
