package com.lifeix.cbs.contest.dao.bb;

import java.util.List;

import com.lifeix.cbs.contest.dto.bb.BbTeamStatistics;

public interface BbTeamStatisticsDao {

    public List<BbTeamStatistics> selectByContestId(Long contestId);

    public boolean saveStatistics(List<BbTeamStatistics> list);

}
