package com.lifeix.cbs.contest.util.algorithm;

import com.lifeix.cbs.api.common.util.CommerceMath;

public class BetAlgorithm {

    // 亚盘算法 ，足球
    /*
     * 
     * handicapeType 如果是负数代表了客场让球，正数代表主场让球
     */

    public static double getAsianSoccerProfit(double betMoney, // 下单额
	    int homeTeamScore, // 主队比分
	    int visitTeamScore, // 客队比分
	    double homeDiscount, // 主队水位
	    double visitDiscount, // 客队水位
	    boolean isBetHomeWin, // 是否下单主队赢
	    double handicapeType // 0，0.25,0.5,0.75,1,1.25,1.5,1.75,2,2.25,2.5,2.75
    ) {
	// 输球按水位为1计算，赢球才算水位

	boolean isReturn = false; // 走盘

	double winRate = 1;

	if (handicapeType < 0) {
	    // 主场让球

	    int dScore = homeTeamScore - visitTeamScore; // 净胜球

	    double difScore = CommerceMath.sub(dScore, CommerceMath.mul(handicapeType, -1));
	    if (difScore == 0) {
		isReturn = true;
	    } else if (difScore == 0.25) {
		winRate = 0.5;
	    } else if (difScore > 0.25) {
		winRate = 1;
	    } else if (difScore == -0.25) {
		winRate = -0.5;
	    } else if (difScore < -0.25) {
		winRate = -1;
	    }

	    if (isReturn)
		return 0;

	    if (winRate > 0) {
		// 主场赢了 并且 投了主场
		if (isBetHomeWin) {
		    return CommerceMath.mul(CommerceMath.mul(betMoney, winRate), homeDiscount);
		} else {
		    return CommerceMath.mul(CommerceMath.mul(betMoney, winRate), -1);
		}
	    } else {
		if (isBetHomeWin) {
		    return CommerceMath.mul(betMoney, winRate);
		} else {
		    return CommerceMath.mul(CommerceMath.mul(betMoney, winRate), CommerceMath.mul(-1, visitDiscount));
		}
	    }
	} else {
	    int dScore = visitTeamScore - homeTeamScore; // 净胜球

	    double difScore = CommerceMath.sub(dScore, handicapeType);
	    if (difScore == 0) {
		isReturn = true;
	    } else if (difScore == 0.25) {
		winRate = 0.5;
	    } else if (difScore > 0.25) {
		winRate = 1;
	    } else if (difScore == -0.25) {
		winRate = -0.5;
	    } else if (difScore < -0.25) {
		winRate = -1;
	    }

	    if (isReturn)
		return 0;

	    if (winRate > 0) {

		if (isBetHomeWin) {
		    return CommerceMath.mul(CommerceMath.mul(betMoney, winRate), -1);
		} else {
		    return CommerceMath.mul(CommerceMath.mul(betMoney, winRate), visitDiscount);
		}
	    } else {
		if (isBetHomeWin) {
		    return CommerceMath.mul(CommerceMath.mul(betMoney, winRate), CommerceMath.mul(-1, homeDiscount));
		} else {
		    return CommerceMath.mul(betMoney, winRate);
		}
	    }
	}
    }

    // 让球310 ，足球
    public static double getHandicap310SoccerProfit(double betMoney, // 下单额
	    int homeTeamScore, // 主队比分
	    int visitTeamScore, // 客队比分
	    double winOdds, // 胜 赔率
	    double sameScoreOdds, // 平 赔率
	    double loseOdds, // 负 赔率
	    int betType, // 胜 3， 平 1， 负 0
	    int handicapNum // 让球数目
    ) {
	int difScore = homeTeamScore - visitTeamScore + handicapNum;

	if (betType == 3) {
	    if (difScore > 0) {
		return CommerceMath.mul(betMoney, CommerceMath.sub(winOdds, 1));
	    } else {
		return CommerceMath.mul(betMoney, -1);
	    }
	} else if (betType == 1) {
	    if (difScore == 0) {
		return CommerceMath.mul(betMoney, CommerceMath.sub(sameScoreOdds, 1));
	    } else
		return CommerceMath.mul(betMoney, -1);

	} else if (betType == 0) {
	    if (difScore < 0) {
		return CommerceMath.mul(betMoney, CommerceMath.sub(loseOdds, 1));
	    } else
		return CommerceMath.mul(betMoney, -1);
	}

	return 0;
    }

    // 310 , 足球
    public static double get310SoccerProfit(double betMoney, // 下单额
	    int homeTeamScore, // 主队比分
	    int visitTeamScore, // 客队比分
	    double winOdds, // 胜 赔率
	    double sameScoreOdds, // 平 赔率
	    double loseOdds, // 负 赔率
	    int betType // 胜 3， 平 1， 负 0
    ) {
	int difScore = homeTeamScore - visitTeamScore;

	if (betType == 3) {
	    if (difScore > 0) {
		return CommerceMath.mul(betMoney, CommerceMath.sub(winOdds, 1));
	    } else {
		return CommerceMath.mul(betMoney, -1);
	    }
	} else if (betType == 1) {
	    if (difScore == 0) {
		return CommerceMath.mul(betMoney, CommerceMath.sub(sameScoreOdds, 1));
	    } else
		return CommerceMath.mul(betMoney, -1);

	} else if (betType == 0) {
	    if (difScore < 0) {
		return CommerceMath.mul(betMoney, CommerceMath.sub(loseOdds, 1));
	    } else
		return CommerceMath.mul(betMoney, -1);
	}
	return 0;
    }

    // 大小球 , 足球
    public static double getGoalSoccerProfit(double betMoney, // 下单额
	    int totalScore, // 实际总进球数
	    double overlineDiscount, // 上盘水位
	    double underlineDiscount, // 下盘水位
	    boolean isBetOverlineWin, // 是否下单上盘赢
	    double type // 让球类型 0，0.25,0.5,0.75,1,1.25,1.5,1.75,2,2.25,2.5,2.75
    ) {
	boolean isReturn = false; // 走盘
	double winRate = 1;

	double difScore = CommerceMath.sub(totalScore, type);

	if (difScore == 0)
	    isReturn = true;
	else if (difScore == 0.25) {
	    winRate = 0.5;
	} else if (difScore > 0.25) {
	    winRate = 1;
	} else if (difScore == -0.25) {
	    winRate = -0.5;
	} else if (difScore < -0.25) {
	    winRate = -1;
	}

	if (isReturn)
	    return 0;

	if (winRate > 0) {
	    if (isBetOverlineWin) {
		return CommerceMath.mul(CommerceMath.mul(betMoney, winRate), overlineDiscount);
	    } else {
		return CommerceMath.mul(CommerceMath.mul(betMoney, winRate), -1);
	    }
	} else {
	    if (isBetOverlineWin) {
		return CommerceMath.mul(betMoney, winRate);
	    } else {
		return CommerceMath.mul(CommerceMath.mul(betMoney, winRate), CommerceMath.mul(-1, underlineDiscount));
	    }
	}
    }

    // 单双数， 足球 ， 上下盘单双
    public static double getGoalOddEvenSoccerProfit(double betMoney, // 下单额
	    int totalScore, // 实际总进球数
	    double oddDiscount, // 奇数水位
	    double evenDiscount, // 偶数水位
	    boolean isBetOddWin // 是否下单奇数盘赢
    ) {
	// 这里仅仅提供单双判断

	boolean isOdd = totalScore % 2 == 0 ? false : true;

	if (isBetOddWin) {
	    if (isOdd)
		return CommerceMath.mul(betMoney, oddDiscount);
	    else
		return CommerceMath.mul(betMoney, -1);
	} else {
	    if (isOdd)
		return CommerceMath.mul(betMoney, -1);
	    else
		return CommerceMath.mul(betMoney, evenDiscount);
	}
    }

    // 亚盘算法 ，篮球

    /*
     * 
     * handicapeType 如果是负数代表了客场让球，正数代表主场让球
     */

    public static double getAsianBasketballProfit(double betMoney, // 下单额
	    int homeTeamScore, // 主队比分
	    int visitTeamScore, // 客队比分
	    double homeDiscount, // 主队水位
	    double visitDiscount, // 客队水位
	    boolean isBetHomeWin, // 是否下单主队赢
	    double handicapeType // 0，0.5,1,1.5,,2,,2.5,3
    ) {
	// 输球按水位为1计算，赢球才算水位
	boolean isReturn = false; // 走盘

	double winRate = 1;

	if (handicapeType < 0) {
	    int dScore = homeTeamScore - visitTeamScore;

	    double difScore = CommerceMath.sub(dScore, CommerceMath.mul(handicapeType, -1));

	    if (difScore >= 0.5) {
		winRate = 1;
	    } else if (difScore <= -0.5) {
		winRate = -1;
	    } else if (difScore == 0) {
		isReturn = true;
	    }

	    if (isReturn)
		return 0;

	    // 主场赢了 并且 投了主场
	    if (winRate > 0) {
		if (isBetHomeWin) {
		    return CommerceMath.mul(CommerceMath.mul(betMoney, winRate), homeDiscount);
		} else {
		    return CommerceMath.mul(CommerceMath.mul(betMoney, winRate), -1);
		}
	    } else {
		if (isBetHomeWin) {
		    return CommerceMath.mul(betMoney, winRate);
		} else {
		    return CommerceMath.mul(CommerceMath.mul(betMoney, winRate), CommerceMath.mul(-1, visitDiscount));
		}
	    }

	} else {
	    int dScore = visitTeamScore - homeTeamScore;

	    double difScore = CommerceMath.sub(dScore, handicapeType);

	    if (difScore >= 0.5) {
		winRate = 1;
	    } else if (difScore <= -0.5) {
		winRate = -1;
	    } else if (difScore == 0) {
		isReturn = true;
	    }

	    if (isReturn)
		return 0;

	    if (winRate > 0) {
		if (isBetHomeWin) {
		    return CommerceMath.mul(CommerceMath.mul(betMoney, winRate), -1);
		} else {
		    return CommerceMath.mul(CommerceMath.mul(betMoney, winRate), visitDiscount);
		}
	    } else {
		if (isBetHomeWin) {
		    return CommerceMath.mul(CommerceMath.mul(betMoney, winRate), CommerceMath.mul(-1, homeDiscount));
		} else {
		    return CommerceMath.mul(betMoney, winRate);
		}
	    }

	}
    }

    /** 篮球让球暂时不做 **/
    // 让球30 ，篮球
    /*
     * public static double getHandicap30BasketballProfit( double betMoney, //
     * 下单额 int homeTeamScore, // 主队比分 int visitTeamScore, // 客队比分 double
     * winOdds, // 胜 赔率 double loseOdds, // 负 赔率 int betType, // 胜 3， 负 0 int
     * handicapNum // 让球数目 ) { int difScore = homeTeamScore - visitTeamScore -
     * handicapNum;
     * 
     * if(betType == 3){ if(difScore > 0){ return
     * CommerceMath.mul(betMoney,CommerceMath.sub(winOdds,1)); } else{ return
     * CommerceMath.mul(betMoney,-1); } } else if(betType == 0){ if(difScore <
     * 0){ return CommerceMath.mul(betMoney,CommerceMath.sub(loseOdds,1)); }
     * else return CommerceMath.mul(betMoney,-1); }
     * 
     * return 0; }
     */

    // , 篮球
    public static double get30BasketballProfit(double betMoney, // 下单额
	    int homeTeamScore, // 主队比分
	    int visitTeamScore, // 客队比分
	    double winOdds, // 胜 赔率
	    double loseOdds, // 负 赔率
	    int betType // 胜 3， 负 0
    ) {
	int difScore = homeTeamScore - visitTeamScore;

	if (betType == 3) {
	    if (difScore > 0) {
		return CommerceMath.mul(betMoney, CommerceMath.sub(winOdds, 1));
	    } else {
		return CommerceMath.mul(betMoney, -1);
	    }
	} else if (betType == 0) {
	    if (difScore < 0) {
		return CommerceMath.mul(betMoney, CommerceMath.sub(loseOdds, 1));
	    } else
		return CommerceMath.mul(betMoney, -1);
	}
	return 0;
    }

    // 大小球 , 篮球
    public static double getGoalBasketballProfit(double betMoney, // 下单额
	    int totalScore, // 实际总进球数
	    double overlineDiscount, // 上盘水位
	    double underlineDiscount, // 下盘水位
	    boolean isBetOverlineWin, // 是否下单上盘赢
	    double type // 让球类型 0，0.5,1,1.5,2,2.5
    ) {
	double winRate = 1;
	boolean isReturn = false; // 走盘
	double difScore = CommerceMath.sub(totalScore, type);

	if (difScore >= 0.5) {
	    winRate = 1;
	} else if (difScore <= -0.5) {
	    winRate = -1;
	} else if (difScore == 0) {
	    isReturn = true;
	}

	if (isReturn)
	    return 0;

	if (winRate > 0) {
	    if (isBetOverlineWin) {
		return CommerceMath.mul(CommerceMath.mul(betMoney, winRate), overlineDiscount);
	    } else {
		return CommerceMath.mul(CommerceMath.mul(betMoney, winRate), -1);
	    }
	} else {
	    if (isBetOverlineWin) {
		return CommerceMath.mul(betMoney, winRate);
	    } else {
		return CommerceMath.mul(CommerceMath.mul(betMoney, winRate), CommerceMath.mul(-1, underlineDiscount));
	    }
	}
    }

    // 单双数， 篮球 ， 上下盘单双
    public static double getGoalOddEvenBasketballProfit(double betMoney, // 下单额
	    int totalScore, // 实际总进球数
	    double oddDiscount, // 奇数水位
	    double evenDiscount, // 偶数水位
	    boolean isBetOddWin // 是否下单奇数盘赢
    ) {
	// 这里仅仅提供单双判断

	boolean isOdd = totalScore % 2 == 0 ? false : true;

	if (isBetOddWin) {
	    if (isOdd)
		return CommerceMath.mul(betMoney, oddDiscount);
	    else
		return CommerceMath.mul(betMoney, -1);
	} else {
	    if (isOdd)
		return CommerceMath.mul(betMoney, -1);
	    else
		return CommerceMath.mul(betMoney, evenDiscount);
	}
    }

}
