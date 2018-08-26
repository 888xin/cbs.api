package com.lifeix.cbs.contest.util.algorithm;

import com.lifeix.cbs.api.common.util.BetConstants;
import com.lifeix.cbs.api.common.util.CommerceMath;


/**
 * 足球单双数算法
 *
 */
public class AlgorithmFbDss implements AlgorithmIF {
	
	public static final double ODD_DISCOUNT = 0.9;
	public static final double EVEN_DISCOUNT = 0.85;

	@Override
	public AlgorithmResult settleBet(double betMoney, int betStatus, int homeScore, int visitScore, Object... oddsParams) {

		double oddDiscount =  ODD_DISCOUNT;
		double evenDiscount =  EVEN_DISCOUNT;
		
		boolean isBetOddWin = (betStatus == BetConstants.BetStatus.HOME);
		int totalScore = homeScore + visitScore;
		double profit = BetAlgorithm.getGoalOddEvenSoccerProfit(betMoney, totalScore, oddDiscount, evenDiscount, isBetOddWin);
		
		AlgorithmResult result = new AlgorithmResult();
		result.setOdds(betStatus == BetConstants.BetStatus.HOME ? ODD_DISCOUNT : EVEN_DISCOUNT);
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
			result.setProfit(0);
		}
		
		return result;
	}

}
