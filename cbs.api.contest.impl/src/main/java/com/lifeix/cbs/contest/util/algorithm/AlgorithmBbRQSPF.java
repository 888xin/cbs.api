package com.lifeix.cbs.contest.util.algorithm;

import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.BetConstants.BetResultStatus;
import com.lifeix.cbs.api.common.util.BetConstants.BetStatus;
import com.lifeix.cbs.api.common.util.CommerceMath;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * 篮球胜平负算法
 * 
 */
public class AlgorithmBbRQSPF implements AlgorithmIF {

    @Override
    public AlgorithmResult settleBet(double betMoney, int betStatus, int homeScore, int visitScore, Object... oddsParams)
	    throws L99IllegalParamsException {

	int len = oddsParams.length;
	if (len != 3) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}

	double homeDiscount = 0;
	double visitDiscount = 0;
	double handicapeType = 0;

	try {
	    handicapeType = Double.parseDouble(oddsParams[0].toString());
	    homeDiscount = Double.parseDouble(oddsParams[1].toString());
	    visitDiscount = Double.parseDouble(oddsParams[2].toString());
	} catch (Error e) {
	    throw new L99IllegalParamsException("Odds params type error!");
	}

	// 计算时扣除本金
	homeDiscount = CommerceMath.sub(homeDiscount, 1);
	visitDiscount = CommerceMath.sub(visitDiscount, 1);
	if (homeDiscount < 0 || visitDiscount < 0) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}

	boolean isBetHomeWin = (betStatus == BetStatus.HOME ? true : false);

	double profit = BetAlgorithm.getAsianBasketballProfit(betMoney, homeScore, visitScore, homeDiscount, visitDiscount,
	        isBetHomeWin, handicapeType);

	AlgorithmResult result = new AlgorithmResult();
	result.setOdds(betStatus == BetStatus.HOME ? homeDiscount : visitDiscount);
	// 返回时加上本金
	result.setOdds(CommerceMath.add(result.getOdds(), 1));
	result.setAmount(betMoney);
	if (profit > 0) {
	    result.setStatus(BetResultStatus.WIN);
	    result.setProfit(profit);
	} else if (profit == 0) {
	    result.setStatus(BetResultStatus.DRAW);
	    result.setProfit(0);
	} else {
	    result.setStatus(BetResultStatus.LOSS);
	    if (profit == CommerceMath.mul(betMoney, -1)) {
		result.setProfit(0);
	    } else {
		result.setProfit(CommerceMath.mul(profit, -1));
	    }
	}

	return result;
    }

}
