package com.lifeix.cbs.contest.service.spark.settle;

import org.json.JSONException;

import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.user.beans.CustomResponse;

public interface YySettleDubbo {

    /**
     * 结算押押赛事
     * 
     * @param contestId
     * @throws L99IllegalDataException
     * @throws L99IllegalParamsException
     * @throws JSONException
     */
    public CustomResponse settleContest(Long contestId) throws L99IllegalParamsException, L99IllegalDataException,
	    JSONException;
}
