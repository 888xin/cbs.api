package com.lifeix.cbs.contest.dao.bb;

import java.util.List;

import com.lifeix.cbs.contest.dto.bb.BbPlayerStatistics;

public interface BbPlayerStatisticsDao {

    public List<BbPlayerStatistics> selectByContestId(Long contestId);

    public boolean saveStatistics(List<BbPlayerStatistics> list);

}
