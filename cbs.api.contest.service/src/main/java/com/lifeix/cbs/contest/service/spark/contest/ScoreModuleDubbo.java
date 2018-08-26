package com.lifeix.cbs.contest.service.spark.contest;

import com.lifeix.cbs.contest.bean.contest.ScoreModuleResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

public interface ScoreModuleDubbo {

    public void updateModuleValue(Integer contestType, Integer moduleValue)
            throws L99IllegalParamsException, L99IllegalDataException;

    public ScoreModuleResponse selectByContestType(Integer contestType)
            throws L99IllegalDataException, L99IllegalParamsException;

}
