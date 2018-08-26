package com.lifeix.cbs.contest.dao.fb;

import java.util.List;

import com.lifeix.cbs.contest.dto.odds.OddsOpHist;

public interface FbOddsOpHistDao {

    public List<OddsOpHist> selectListByContestId(Long contestId, Long startId, Long endId, Integer limit);

    public boolean saveOddsHist(List<OddsOpHist> list);

}
