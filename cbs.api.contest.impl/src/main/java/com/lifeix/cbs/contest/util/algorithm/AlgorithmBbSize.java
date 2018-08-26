package com.lifeix.cbs.contest.util.algorithm;

import com.lifeix.cbs.api.common.util.BetConstants;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.cbs.api.common.util.CommerceMath;


/**
 * 足球大小球算法
 *
 */
public class AlgorithmBbSize implements AlgorithmIF {

	@Override
	public AlgorithmResult settleBet(double betMoney, int betStatus, int homeScore, int visitScore, Object... oddsParams) throws L99IllegalParamsException {
		int len = oddsParams.length;
		if(len != 3) {
			throw new L99IllegalParamsException("Odds params number error!");
		}
		
		double type = 0;
		double overlineDiscount = 1;
		double underlineDiscount = 1;
		
		try{
			type = Double.parseDouble(oddsParams[0].toString());
			overlineDiscount = Double.parseDouble(oddsParams[1].toString());
			underlineDiscount = Double.parseDouble(oddsParams[2].toString());
		}
		catch(Error e){
			throw new L99IllegalParamsException("Odds params type error!");
		}
		
		//计算时扣除本金
		overlineDiscount = CommerceMath.sub(overlineDiscount, 1);
		underlineDiscount = CommerceMath.sub(underlineDiscount, 1);
		if(overlineDiscount < 0 || underlineDiscount < 0){
			throw new L99IllegalParamsException("Odds params number error!");
		}
		
		boolean isBetOverlineWin = (betStatus == BetConstants.BetStatus.HOME);
        //总分
		int totalScore = homeScore + visitScore;
		
		double profit = BetAlgorithm.getGoalBasketballProfit(betMoney, totalScore, overlineDiscount, underlineDiscount, isBetOverlineWin, type);
		
		AlgorithmResult result = new AlgorithmResult();
		result.setOdds(betStatus == BetConstants.BetStatus.HOME ? overlineDiscount : underlineDiscount);
		// 返回时加上本金
		result.setOdds(CommerceMath.add(result.getOdds(), 1));
		result.setAmount(betMoney);
		if(profit > 0){
			result.setStatus(BetConstants.BetResultStatus.WIN);
			result.setProfit(profit);
		}
		else if(profit == 0){
			result.setStatus(BetConstants.BetResultStatus.DRAW);
			result.setProfit(0);
		}
		else{
			result.setStatus(BetConstants.BetResultStatus.LOSS);
			if(profit == CommerceMath.mul(betMoney, -1)){
				result.setProfit(0);
			}
			else{
				result.setProfit(CommerceMath.mul(profit, -1));
			}
		}
		
		
		return result;
	}

}
