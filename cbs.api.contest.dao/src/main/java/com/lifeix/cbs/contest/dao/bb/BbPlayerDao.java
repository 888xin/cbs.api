package com.lifeix.cbs.contest.dao.bb;

import java.util.List;
import java.util.Map;

import com.lifeix.cbs.contest.dto.bb.BbPlayer;

public interface BbPlayerDao {

    public List<BbPlayer> selectMore(Long startId, Integer limit);

    public boolean update(BbPlayer player);

    public boolean savePlayers(List<BbPlayer> list);

    public Map<String, BbPlayer> selectByTargetIds(List<String> targetIds);

    public BbPlayer selectByTargetId(String targetId);

    public BbPlayer selectById(Long id);

    public Map<String, BbPlayer> getAllPlayers();

    public List<BbPlayer> selectByName(String name);

    public boolean updateTeamId(List<BbPlayer> list);
    
    public List<BbPlayer> selectByTeamId(Long teamId);

}
