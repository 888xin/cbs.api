package com.lifeix.cbs.contest.util.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * 下单算法方法抽象策略角色
 */
public interface AlgorithmIF {

    Logger LOG = LoggerFactory.getLogger(AlgorithmIF.class);

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
     */
    AlgorithmResult settleBet(double betMoney, int betStatus, int homeScore, int visitScore, Object... oddsParams)
	    throws L99IllegalParamsException;

}
