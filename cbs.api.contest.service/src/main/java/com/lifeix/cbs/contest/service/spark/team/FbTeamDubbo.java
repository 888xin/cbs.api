package com.lifeix.cbs.contest.service.spark.team;

import java.util.List;
import java.util.Map;

import com.lifeix.cbs.contest.bean.fb.TeamResponse;

public interface FbTeamDubbo {

    public boolean saveTeam(List<TeamResponse> list);

    public Map<Long, TeamResponse> getAllTeam();

    public Map<Long, TeamResponse> getTeamByTargetIds(List<Long> targetIds);

}
