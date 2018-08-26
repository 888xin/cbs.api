package com.lifeix.cbs.contest.service.spark.odds;

import java.util.List;

import com.lifeix.cbs.contest.bean.odds.OddsJcHistResponse;
import com.lifeix.cbs.contest.bean.odds.OddsOpHistResponse;
import com.lifeix.cbs.contest.bean.odds.OddsSizeHistResponse;

public interface BbOddsHistDubbo {

    public boolean saveJcOddsHist(List<OddsJcHistResponse> list);

    public boolean saveOpOddsHist(List<OddsOpHistResponse> list);

    public boolean saveSizeOddsHist(List<OddsSizeHistResponse> list);
}
