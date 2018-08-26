package com.lifeix.cbs.contest.impl.spark.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.lifeix.cbs.contest.bean.fb.TeamResponse;
import com.lifeix.cbs.contest.dao.fb.FbTeamDao;
import com.lifeix.cbs.contest.dto.fb.FbTeam;
import com.lifeix.cbs.contest.service.spark.team.FbTeamDubbo;
import com.lifeix.cbs.contest.util.transform.ContestTransformUtil;

public class FbTeamDubboImpl implements FbTeamDubbo {

    @Autowired
    private FbTeamDao fbTeamDao;

    @Override
    public boolean saveTeam(List<TeamResponse> list) {
	List<FbTeam> teamList = new ArrayList<FbTeam>(list.size());
	for (TeamResponse resp : list) {
	    FbTeam team = new FbTeam();
	    team.setCountryId(resp.getCountry_id());
	    team.setCupId(resp.getCup_id());
	    team.setLogo(resp.getLogo());
	    team.setName(resp.getName());
	    team.setNameEN(resp.getName_en());
	    team.setStatus(resp.getStatus());
	    team.setTargetId(resp.getTarget_id());
	    teamList.add(team);
	}
	return fbTeamDao.saveTeam(teamList);
    }

    @Override
    public Map<Long, TeamResponse> getAllTeam() {
	Map<Long, FbTeam> teamMap = fbTeamDao.getAllTeam();
	Set<Long> keySet = teamMap.keySet();
	Map<Long, TeamResponse> respMap = new HashMap<Long, TeamResponse>(teamMap.size());
	for (Long key : keySet) {
	    FbTeam team = teamMap.get(key);
	    TeamResponse resp = ContestTransformUtil.transformFbTeam(team, false);
	    respMap.put(key, resp);
	}
	return respMap;
    }

    @Override
    public Map<Long, TeamResponse> getTeamByTargetIds(List<Long> targetIds) {
	Map<Long, FbTeam> teamMap = fbTeamDao.getTeamByTargetIds(targetIds);
	Set<Long> keySet = teamMap.keySet();
	Map<Long, TeamResponse> respMap = new HashMap<Long, TeamResponse>(teamMap.size());
	for (Long key : keySet) {
	    FbTeam team = teamMap.get(key);
	    TeamResponse resp = ContestTransformUtil.transformFbTeam(team, false);
	    respMap.put(key, resp);
	}
	return respMap;
    }
}
