package com.lifeix.cbs.contest.dao.bb;

import java.util.List;

import com.lifeix.cbs.contest.dto.odds.OddsJcHist;

public interface BbOddsJcHistDao {

    public List<OddsJcHist> selectListByContestId(Long contestId, Long startId, Long endId, Integer limit);

    public boolean saveOddsHist(List<OddsJcHist> list);

}
