package com.lifeix.cbs.contest.dao.fb;

import java.util.List;

import com.lifeix.cbs.contest.dto.fb.FbStatistics;

public interface FbStatisticsDao {

    public List<FbStatistics> selectByContestId(Long contestId);

    public boolean saveStatistics(List<FbStatistics> list);

}
