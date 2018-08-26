package com.lifeix.cbs.contest.util.transform;

import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.contest.bean.bet.BetJcResponse;
import com.lifeix.cbs.contest.bean.bet.BetOddevenResponse;
import com.lifeix.cbs.contest.bean.bet.BetOpResponse;
import com.lifeix.cbs.contest.bean.bet.BetSizeResponse;
import com.lifeix.cbs.contest.bean.odds.OddsJcHistResponse;
import com.lifeix.cbs.contest.bean.odds.OddsJcResponse;
import com.lifeix.cbs.contest.bean.odds.OddsOpHistResponse;
import com.lifeix.cbs.contest.bean.odds.OddsOpResponse;
import com.lifeix.cbs.contest.bean.odds.OddsSizeHistResponse;
import com.lifeix.cbs.contest.bean.odds.OddsSizeResponse;
import com.lifeix.cbs.contest.bean.odds.OddsWarnResponse;
import com.lifeix.cbs.contest.dto.bet.BetJc;
import com.lifeix.cbs.contest.dto.bet.BetOddeven;
import com.lifeix.cbs.contest.dto.bet.BetOp;
import com.lifeix.cbs.contest.dto.bet.BetSize;
import com.lifeix.cbs.contest.dto.odds.OddsJc;
import com.lifeix.cbs.contest.dto.odds.OddsJcHist;
import com.lifeix.cbs.contest.dto.odds.OddsOp;
import com.lifeix.cbs.contest.dto.odds.OddsOpHist;
import com.lifeix.cbs.contest.dto.odds.OddsSize;
import com.lifeix.cbs.contest.dto.odds.OddsSizeHist;
import com.lifeix.cbs.contest.dto.odds.OddsWarn;

/**
 * 赔率DTO 转换 VO
 * 
 * @author lifeix-sz
 * 
 */
public class OddsTransformUtil {

    /**
     * op数据转换vo
     * 
     * @param op
     * @param simpleFlag
     *            true 简单数据
     * @return
     */
    public static OddsOpResponse transformOddsOp(OddsOp op) {
	OddsOpResponse resp = null;
	if (op != null) {
	    resp = new OddsOpResponse();
	    resp.setOdds_id(op.getOddsId());
	    resp.setContest_id(op.getContestId());
	    resp.setHome_roi(op.getHomeRoi());
	    if (op.getDrawRoi() == null) {
		resp.setDraw_roi(0.0);
	    } else {
		resp.setDraw_roi(op.getDrawRoi());
	    }
	    resp.setAway_roi(op.getAwayRoi());
	    resp.setInit_home_roi(op.getInitHomeRoi());
	    resp.setInit_draw_roi(op.getInitDrawRoi());
	    resp.setInit_away_roi(op.getInitAwayRoi());
	    resp.setCompany(op.getCompany());
	    resp.setClose_flag(op.getCloseFlag());
	    resp.setLock_flag(op.getLockFlag());
	}
	return resp;
    }

    /**
     * opHist数据转换vo
     * 
     * @param opHist
     * @param simpleFlag
     *            true 简单数据
     * @return
     */
    public static OddsOpHistResponse transformOddsOpHist(OddsOpHist opHist) {
	OddsOpHistResponse resp = null;
	if (opHist != null) {
	    resp = new OddsOpHistResponse();
	    resp.setId(opHist.getId());
	    resp.setContest_id(opHist.getContestId());
	    resp.setHome_roi(opHist.getHomeRoi());
	    resp.setDraw_roi(opHist.getDrawRoi());
	    resp.setAway_roi(opHist.getAwayRoi());
	    resp.setCreate_time(CbsTimeUtils.getUtcTimeForDate(opHist.getCreateTime()));
	    resp.setCompany(opHist.getCompany());
	}
	return resp;
    }

    /**
     * jc数据转换vo
     * 
     * @param jc
     * @return
     */
    public static OddsJcResponse transformOddsJc(OddsJc jc) {
	OddsJcResponse resp = null;
	if (jc != null) {
	    resp = new OddsJcResponse();
	    resp.setOdds_id(jc.getOddsId());
	    resp.setContest_id(jc.getContestId());
	    resp.setHandicap(jc.getHandicap());
	    resp.setHome_roi(jc.getHomeRoi());
	    resp.setDraw_roi(jc.getDrawRoi());
	    resp.setAway_roi(jc.getAwayRoi());
	    resp.setInit_handicap(jc.getInitHandicap());
	    resp.setInit_home_roi(jc.getInitHomeRoi());
	    resp.setInit_draw_roi(jc.getInitDrawRoi());
	    resp.setInit_away_roi(jc.getInitAwayRoi());
	    resp.setClose_flag(jc.getCloseFlag());
	    resp.setLock_flag(jc.getLockFlag());
	}
	return resp;
    }

    /**
     * jc数据历史转换
     * 
     * @param jcHist
     * @return
     */
    public static OddsJcHistResponse transformOddsJcHist(OddsJcHist jcHist) {
	OddsJcHistResponse resp = null;
	if (jcHist != null) {
	    resp = new OddsJcHistResponse();
	    resp.setId(jcHist.getId());
	    resp.setContest_id(jcHist.getContestId());
	    resp.setHandicap(jcHist.getHandicap());
	    resp.setHome_roi(jcHist.getHomeRoi());
	    resp.setDraw_roi(jcHist.getDrawRoi());
	    resp.setAway_roi(jcHist.getAwayRoi());
	    resp.setCreate_time(CbsTimeUtils.getUtcTimeForDate(jcHist.getCreateTime()));
	    resp.setCompany(jcHist.getCompany());
	}
	return resp;
    }

    /**
     * 大小球赔率转换vo
     * 
     * @param size
     * @return
     */
    public static OddsSizeResponse transformOddsSize(OddsSize size) {
	OddsSizeResponse resp = null;
	if (size != null) {
	    resp = new OddsSizeResponse();
	    resp.setOdds_id(size.getOddsId());
	    resp.setContest_id(size.getContestId());
	    resp.setHandicap(size.getHandicap());
	    resp.setBig_roi(size.getBigRoi());
	    resp.setTiny_roi(size.getTinyRoi());
	    resp.setInit_handicap(size.getInitHandicap());
	    resp.setInit_big_roi(size.getInitBigRoi());
	    resp.setInit_tiny_roi(size.getInitTinyRoi());
	    resp.setClose_flag(size.getCloseFlag());
	    resp.setLock_flag(size.getLockFlag());
	}
	return resp;
    }

    /**
     * 大小球赔率历史转换
     * 
     * @param sizeHist
     * @return
     */
    public static OddsSizeHistResponse transformOddsSizeHist(OddsSizeHist sizeHist) {
	OddsSizeHistResponse resp = null;
	if (sizeHist != null) {
	    resp = new OddsSizeHistResponse();
	    resp.setId(sizeHist.getId());
	    resp.setContest_id(sizeHist.getContestId());
	    resp.setHandicap(sizeHist.getHandicap());
	    resp.setBig_roi(sizeHist.getBigRoi());
	    resp.setTiny_roi(sizeHist.getTinyRoi());
	    resp.setCreate_time(CbsTimeUtils.getUtcTimeForDate(sizeHist.getCreateTime()));
	}
	return resp;
    }

    /**
     * op下单数据转换vo
     * 
     * @param op
     * @param simpleFlag
     *            true 简单数据
     * @return
     */
    public static BetOpResponse transformBetOp(BetOp op) {
	BetOpResponse resp = null;
	if (op != null) {
	    resp = new BetOpResponse();
	    resp.setB_id(op.getbId());
	    resp.setUser_id(op.getUserId());
	    resp.setContest_id(op.getContestId());
	    resp.setSupport(op.getSupport());
	    resp.setHome_roi(op.getHomeRoi());
	    if (op.getDrawRoi() == null) {
		resp.setDraw_roi(0d);
	    } else {
		resp.setDraw_roi(op.getDrawRoi());
	    }
	    resp.setAway_roi(op.getAwayRoi());
	    resp.setBet(op.getBet());
	    resp.setBack(op.getBack());
	    resp.setStatus(op.getStatus());
	    resp.setCompany(op.getCompany());
	    resp.setCreate_time(CbsTimeUtils.getUtcTimeForDate(op.getCreateTime()));
	}
	return resp;
    }

    /**
     * jc下单数据转换vo
     * 
     * @param jc
     * @return
     */
    public static BetJcResponse transformBetBd(BetJc jc) {
	BetJcResponse resp = null;
	if (jc != null) {
	    resp = new BetJcResponse();
	    resp.setB_id(jc.getbId());
	    resp.setUser_id(jc.getUserId());
	    resp.setContest_id(jc.getContestId());
	    resp.setSupport(jc.getSupport());
	    resp.setHandicap(jc.getHandicap());
	    resp.setHome_roi(jc.getHomeRoi());
	    resp.setDraw_roi(jc.getDrawRoi());
	    resp.setAway_roi(jc.getAwayRoi());
	    resp.setBet(jc.getBet());
	    resp.setBack(jc.getBack());
	    resp.setStatus(jc.getStatus());
	    resp.setCreate_time(CbsTimeUtils.getUtcTimeForDate(jc.getCreateTime()));
	}
	return resp;
    }

    /**
     * 大小球下单记录转换
     * 
     * @param betSize
     * @return
     */
    public static BetSizeResponse transformBetSize(BetSize betSize) {
	BetSizeResponse resp = null;
	if (betSize != null) {
	    resp = new BetSizeResponse();
	    resp.setB_id(betSize.getbId());
	    resp.setUser_id(betSize.getUserId());
	    resp.setContest_id(betSize.getContestId());
	    resp.setSupport(betSize.getSupport());
	    resp.setHandicap(betSize.getHandicap());
	    resp.setBig_roi(betSize.getBigRoi());
	    resp.setTiny_roi(betSize.getTinyRoi());
	    resp.setBet(betSize.getBet());
	    resp.setBack(betSize.getBack());
	    resp.setStatus(betSize.getStatus());
	    resp.setCreate_time(CbsTimeUtils.getUtcTimeForDate(betSize.getCreateTime()));
	}
	return resp;
    }

    /**
     * 单双数下单记录转换
     * 
     * @param betOddeven
     * @return
     */
    public static BetOddevenResponse transformBetOddeven(BetOddeven betOddeven) {
	BetOddevenResponse resp = null;
	if (betOddeven != null) {
	    resp = new BetOddevenResponse();
	    resp.setB_id(betOddeven.getbId());
	    resp.setUser_id(betOddeven.getUserId());
	    resp.setContest_id(betOddeven.getContestId());
	    resp.setSupport(betOddeven.getSupport());
	    resp.setOdd_roi(betOddeven.getOddRoi());
	    resp.setEven_roi(betOddeven.getEvenRoi());
	    resp.setBet(betOddeven.getBet());
	    resp.setBack(betOddeven.getBack());
	    resp.setStatus(betOddeven.getStatus());
	    resp.setCreate_time(CbsTimeUtils.getUtcTimeForDate(betOddeven.getCreateTime()));
	}
	return resp;
    }

    public static OddsWarnResponse transformOddsWarn(OddsWarn oddsWarn) {
	OddsWarnResponse resp = null;
	if (oddsWarn != null) {
	    resp = new OddsWarnResponse();
	    resp.setId(oddsWarn.getId());
	    resp.setHandicap(oddsWarn.getHandicap());
	    resp.setInitHandicap(oddsWarn.getInitHandicap());
	    resp.setInitHomeRoi(oddsWarn.getInitHomeRoi());
	    resp.setInitDrawRoi(oddsWarn.getInitDrawRoi());
	    resp.setInitAwayRoi(oddsWarn.getInitAwayRoi());
	    resp.setHomeRoi(oddsWarn.getHomeRoi());
	    resp.setDrawRoi(oddsWarn.getDrawRoi());
	    resp.setAwayRoi(oddsWarn.getAwayRoi());
	    resp.setOddsId(oddsWarn.getOddsId());
	    resp.setPlayType(oddsWarn.getPlayType());
	    resp.setStatus(oddsWarn.getStatus());
	    resp.setCompany(oddsWarn.getCompany());
	    resp.setCreateTime(CbsTimeUtils.getUtcTimeForDate(oddsWarn.getCreateTime()));
	}
	return resp;
    }

}
