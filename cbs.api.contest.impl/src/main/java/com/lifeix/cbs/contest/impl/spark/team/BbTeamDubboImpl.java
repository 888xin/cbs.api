package com.lifeix.cbs.contest.impl.spark.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.lifeix.cbs.contest.bean.fb.TeamResponse;
import com.lifeix.cbs.contest.dao.bb.BbTeamDao;
import com.lifeix.cbs.contest.dto.bb.BbTeam;
import com.lifeix.cbs.contest.service.spark.team.BbTeamDubbo;
import com.lifeix.cbs.contest.util.transform.ContestTransformUtil;

public class BbTeamDubboImpl implements BbTeamDubbo {

    @Autowired
    private BbTeamDao bbTeamDao;

    @Override
    public boolean saveTeam(List<TeamResponse> list) {
	List<BbTeam> teamList = new ArrayList<BbTeam>(list.size());
	for (TeamResponse resp : list) {
	    BbTeam team = new BbTeam();
	    team.setCountryId(resp.getCountry_id());
	    team.setCupId(resp.getCup_id());
	    team.setData(resp.getData());
	    team.setLogo(resp.getLogo());
	    team.setName(resp.getName());
	    team.setNameEN(resp.getName_en());
	    team.setNameFull(resp.getName_full());
	    team.setStatus(resp.getStatus());
	    team.setTargetId(resp.getTarget_id());
	    teamList.add(team);
	}
	return bbTeamDao.saveTeam(teamList);
    }

    @Override
    public Map<Long, TeamResponse> getAllTeam() {
	Map<Long, BbTeam> teamMap = bbTeamDao.getAllTeam();
	Set<Long> keySet = teamMap.keySet();
	Map<Long, TeamResponse> respMap = new HashMap<Long, TeamResponse>(teamMap.size());
	for (Long key : keySet) {
	    BbTeam team = teamMap.get(key);
	    TeamResponse resp = ContestTransformUtil.transformBbTeam(team, false);
	    respMap.put(key, resp);
	}
	return respMap;
    }

    @Override
    public Map<Long, TeamResponse> getTeamByTargetIds(List<Long> targetIds) {
	Map<Long, BbTeam> teamMap = bbTeamDao.getTeamByTargetIds(targetIds);
	Set<Long> keySet = teamMap.keySet();
	Map<Long, TeamResponse> respMap = new HashMap<Long, TeamResponse>(teamMap.size());
	for (Long key : keySet) {
	    BbTeam team = teamMap.get(key);
	    TeamResponse resp = ContestTransformUtil.transformBbTeam(team, false);
	    respMap.put(key, resp);
	}
	return respMap;
    }

}
