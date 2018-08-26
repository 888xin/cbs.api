package com.lifeix.cbs.contest.util.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * 下单算法调用类
 * 
 * @author peter-dell
 * 
 */
public class AlgorithmRole {

    final static Logger LOG = LoggerFactory.getLogger(AlgorithmRole.class);

    private AlgorithmIF algorithm;

    public AlgorithmRole(AlgorithmIF algorithm) {
	this.algorithm = algorithm;
    }

    /**
     * 结算下单
     * 
     * @param betMoney
     *            下单金额
     * @param betStatus
     *            下单类型 0 (主赢|大球|单数) 1 (客赢|小球|双数) 2 平局
     * @param homeScore
     *            主队分数
     * @param visitScore
     *            客队分数
     * @param oddsParams
     *            赔率参数
     * @return
     * @throws L99IllegalParamsException
     */
    public AlgorithmResult settleBet(double betMoney, int betStatus, int homeScore, int visitScore, Object... oddsParams)
	    throws L99IllegalParamsException {
	return algorithm.settleBet(betMoney, betStatus, homeScore, visitScore, oddsParams);
    }

}
