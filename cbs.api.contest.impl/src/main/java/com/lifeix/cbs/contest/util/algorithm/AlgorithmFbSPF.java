package com.lifeix.cbs.contest.util.algorithm;

import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.BetConstants.BetResultStatus;
import com.lifeix.cbs.api.common.util.BetConstants.BetStatus;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * 足球胜平负算法
 * 
 */
public class AlgorithmFbSPF implements AlgorithmIF {

    @Override
    public AlgorithmResult settleBet(double betMoney, int betStatus, int homeScore, int visitScore, Object... oddsParams)
	    throws L99IllegalParamsException {
	int len = oddsParams.length;
	if (len != 3) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}

	double winOdds = 0;
	double sameScoreOdds = 0;
	double loseOdds = 0;

	try {
	    winOdds = Double.parseDouble(oddsParams[0].toString());
	    sameScoreOdds = Double.parseDouble(oddsParams[1].toString());
	    loseOdds = Double.parseDouble(oddsParams[2].toString());

	} catch (Error e) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}

	int betType = 1;
	double odds = sameScoreOdds;

	if (betStatus == BetStatus.HOME) {
	    betType = 3;
	    odds = winOdds;
	} else if (betStatus == BetStatus.AWAY) {
	    betType = 0;
	    odds = loseOdds;
	}

	double profit = BetAlgorithm.get310SoccerProfit(betMoney, homeScore, visitScore, winOdds, sameScoreOdds, loseOdds,
	        betType);

	AlgorithmResult result = new AlgorithmResult();
	result.setOdds(odds);
	result.setAmount(betMoney);
	if (profit > 0) {
	    result.setStatus(BetResultStatus.WIN);
	    result.setProfit(profit);
	} else if (profit == 0) {
	    result.setStatus(BetResultStatus.DRAW);
	    result.setProfit(0);
	} else {
	    result.setStatus(BetResultStatus.LOSS);
	    result.setProfit(0);
	}

	return result;
    }

}
