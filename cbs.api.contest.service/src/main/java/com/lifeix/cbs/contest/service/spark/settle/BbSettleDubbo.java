package com.lifeix.cbs.contest.service.spark.settle;

import org.json.JSONException;

import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.user.beans.CustomResponse;

public interface BbSettleDubbo {

    /**
     * 结算篮球赛事
     * 
     * @param contestId
     * @throws L99IllegalDataException
     * @throws L99IllegalParamsException
     * @throws JSONException
     */
    public CustomResponse settleContest(Long contestId) throws L99IllegalParamsException, L99IllegalDataException,
	    JSONException;

    /**
     * 取消比赛的单个下注
     * 
     * @param playType
     * @param bId
     * @param reson
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     * @throws JSONException
     */
    public void cancleBet(Integer playType, Long bId, String reson, String adminName) throws L99IllegalParamsException,
	    L99IllegalDataException, JSONException;

    /**
     * 结算因为跨系统产生的货币丢失问题
     * 
     * @param playType
     * @param bId
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     * @throws JSONException
     */
    public void settleMissedBet(Integer playType, Long bId) throws L99IllegalParamsException, L99IllegalDataException,
	    JSONException;
}
