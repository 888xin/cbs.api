package com.lifeix.cbs.contest.dao.fb;

import java.util.List;

import com.lifeix.cbs.contest.dto.odds.OddsJcHist;

public interface FbOddsJcHistDao {

    public List<OddsJcHist> selectListByContestId(Long contestId, Long startId, Long endId, Integer limit);

    public boolean saveOddsHist(List<OddsJcHist> list);

}
