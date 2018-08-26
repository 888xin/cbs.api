package com.lifeix.cbs.contest.service.spark.contest;

import java.util.List;
import java.util.Map;

import com.lifeix.cbs.contest.bean.bb.ext.BbPlayerResponse;

public interface BbPlayerDubbo {

    public boolean savePlayers(List<BbPlayerResponse> respList);

    public Map<String, BbPlayerResponse> getAllPlayers();

    public Map<String, BbPlayerResponse> getPlayersByTargetIds(List<String> targetIds);

    public boolean updatePlayersTeamId(List<BbPlayerResponse> respList);

}
