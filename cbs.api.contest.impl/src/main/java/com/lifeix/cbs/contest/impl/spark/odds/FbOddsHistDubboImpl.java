package com.lifeix.cbs.contest.impl.spark.odds;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lifeix.cbs.contest.bean.odds.OddsJcHistResponse;
import com.lifeix.cbs.contest.bean.odds.OddsOpHistResponse;
import com.lifeix.cbs.contest.bean.odds.OddsSizeHistResponse;
import com.lifeix.cbs.contest.dao.fb.FbOddsJcHistDao;
import com.lifeix.cbs.contest.dao.fb.FbOddsOpHistDao;
import com.lifeix.cbs.contest.dao.fb.FbOddsSizeHistDao;
import com.lifeix.cbs.contest.dto.odds.OddsJcHist;
import com.lifeix.cbs.contest.dto.odds.OddsOpHist;
import com.lifeix.cbs.contest.dto.odds.OddsSizeHist;
import com.lifeix.cbs.contest.service.spark.odds.FbOddsHistDubbo;

public class FbOddsHistDubboImpl implements FbOddsHistDubbo {

    @Autowired
    private FbOddsJcHistDao fbOddsJcHistDao;

    @Autowired
    private FbOddsOpHistDao fbOddsOpHistDao;

    @Autowired
    private FbOddsSizeHistDao fbOddsSizeHistDao;

    @Override
    public boolean saveJcOddsHist(List<OddsJcHistResponse> list) {
	List<OddsJcHist> oddsHistList = new ArrayList<OddsJcHist>(list.size());
	for (OddsJcHistResponse resp : list) {
	    OddsJcHist oddsHist = new OddsJcHist();
	    oddsHist.setAwayRoi(resp.getAway_roi());
	    oddsHist.setCompany(resp.getCompany());
	    oddsHist.setContestId(resp.getContest_id());
	    oddsHist.setDrawRoi(resp.getDraw_roi());
	    oddsHist.setHandicap(resp.getHandicap());
	    oddsHist.setHomeRoi(resp.getHome_roi());
	    oddsHistList.add(oddsHist);
	}
	return fbOddsJcHistDao.saveOddsHist(oddsHistList);
    }

    @Override
    public boolean saveOpOddsHist(List<OddsOpHistResponse> list) {
	List<OddsOpHist> oddsHistList = new ArrayList<OddsOpHist>(list.size());
	for (OddsOpHistResponse resp : list) {
	    OddsOpHist oddsHist = new OddsOpHist();
	    oddsHist.setAwayRoi(resp.getAway_roi());
	    oddsHist.setCompany(resp.getCompany());
	    oddsHist.setContestId(resp.getContest_id());
	    oddsHist.setDrawRoi(resp.getDraw_roi());
	    oddsHist.setHomeRoi(resp.getHome_roi());
	    oddsHistList.add(oddsHist);
	}
	return fbOddsOpHistDao.saveOddsHist(oddsHistList);
    }

    @Override
    public boolean saveSizeOddsHist(List<OddsSizeHistResponse> list) {
	List<OddsSizeHist> oddsHistList = new ArrayList<OddsSizeHist>(list.size());
	for (OddsSizeHistResponse resp : list) {
	    OddsSizeHist oddsHist = new OddsSizeHist();
	    oddsHist.setBigRoi(resp.getBig_roi());
	    oddsHist.setCompany(resp.getCompany());
	    oddsHist.setContestId(resp.getContest_id());
	    oddsHist.setHandicap(resp.getHandicap());
	    oddsHist.setTinyRoi(resp.getTiny_roi());
	    oddsHistList.add(oddsHist);
	}
	return fbOddsSizeHistDao.saveOddsHist(oddsHistList);
    }

}
