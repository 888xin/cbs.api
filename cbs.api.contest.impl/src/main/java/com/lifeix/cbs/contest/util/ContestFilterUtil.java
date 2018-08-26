package com.lifeix.cbs.contest.util;

import java.util.List;

import com.lifeix.cbs.contest.bean.fb.ContestListResponse;
import com.lifeix.cbs.contest.bean.fb.ContestResponse;
import com.lifeix.cbs.contest.bean.fb.TeamResponse;
import com.lifeix.cbs.contest.dto.odds.OddsJc;
import com.lifeix.cbs.contest.dto.odds.OddsOp;

public class ContestFilterUtil {

    public static final String DEFAULT_HOME_LOGO = "cbs_1.png";

    public static final String DEFAULT_AWAY_LOGO = "cbs_2.png";

    /**
     * 赛事列表过滤
     * 
     * @param listResponse
     * @param filterFlag
     * @param client
     * @return
     */
    public static ContestListResponse filterContestListLogo(ContestListResponse listResponse, boolean filterFlag,
	    String client) {
	filterFlag = false;
	if (filterFlag && "key:roi_iphone".equals(client)) {
	    List<ContestResponse> respList = listResponse.getContests();
	    if (respList != null && respList.size() > 0) {
		for (ContestResponse resp : respList) {
		    TeamResponse ht = resp.getH_t();
		    TeamResponse at = resp.getA_t();
		    ht.setLogo(DEFAULT_HOME_LOGO);
		    at.setLogo(DEFAULT_AWAY_LOGO);
		}
	    }
	    return listResponse;
	} else {
	    return listResponse;
	}
    }

    /**
     * 单个赛事过滤
     * 
     * @param resp
     * @param filterFlag
     * @param client
     * @return
     */
    public static ContestResponse filterContestLogo(ContestResponse resp, boolean filterFlag, String client) {
	filterFlag = false;
	if (filterFlag && "key:roi_iphone".equals(client)) {
	    TeamResponse ht = resp.getH_t();
	    TeamResponse at = resp.getA_t();
	    ht.setLogo(DEFAULT_HOME_LOGO);
	    at.setLogo(DEFAULT_AWAY_LOGO);
	    return resp;
	} else {
	    return resp;
	}
    }

    /**
     * 检查赔率是否异常
     * 
     * @param oddsOp
     * @param oddsJc
     * @return
     */
    public static int checkOdds(OddsOp oddsOp, OddsJc oddsJc) {

	int warnType = 0;
	if (oddsJc.getHandicap() > 0) { // 主队受让
	    if (oddsJc.getHomeRoi() > oddsOp.getHomeRoi()) {// 主队赔率不可能提高
		warnType = 1; // 让球盘赔率异常
	    } else if (oddsJc.getAwayRoi() < oddsOp.getAwayRoi()) {// 客队赔率不可能降低
		warnType = 2; // 胜平负赔率异常
	    }
	} else {// 主队让球
	    if (oddsJc.getHomeRoi() < oddsOp.getHomeRoi()) {// 主队赔率不可能降低
		warnType = 2; // 胜平负赔率异常
	    } else if (oddsJc.getAwayRoi() > oddsOp.getAwayRoi()) {// 客队赔率不可能提高
		warnType = 1; // 让球盘赔率异常
	    }
	}

	return warnType;
    }
}
