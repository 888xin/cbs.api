package com.lifeix.cbs.contest.dao.fb;

import java.util.List;

import com.lifeix.cbs.contest.dto.odds.OddsSizeHist;

public interface FbOddsSizeHistDao {

    public List<OddsSizeHist> selectListByContestId(Long contestId, Long startId, Long endId, Integer limit);

    public boolean saveOddsHist(List<OddsSizeHist> list);

}
