package com.lifeix.cbs.api.common.util;

import com.lifeix.cbs.api.common.util.CouponConstants.CouponSystem;

public class BetConstants {

    /**
     * 下单状态类型
     */
    public static final class BetStatus {

	/**
	 * 主赢 | 大球 | 单数
	 */
	public static final int HOME = 0;

	/**
	 * 客赢 | 小球 | 双数
	 */
	public static final int AWAY = 1;

	/**
	 * 平局
	 */
	public static final int DRAW = 2;

	public static boolean verifySupport(int status, int playId) {
	    if (HOME == status) {
		return true;
	    } else if (AWAY == status) {
		return true;
	    } else if (DRAW == status) {
		// 只有足球胜平负和让球胜平负才有第三种
		if (playId == PlayType.FB_SPF.getId() || playId == PlayType.FB_RQSPF.getId()) {
		    return true;
		} else {
		    return false;
		}
	    }
	    return false;
	}
    }

    /**
     * 下单结果状态类型
     */
    public static final class BetResultStatus {
	/**
	 * 初始下单状态
	 */
	public static final int INIT = 0;

	/**
	 * 赢
	 */
	public static final int WIN = 1;

	/**
	 * 输
	 */
	public static final int LOSS = 2;

	/**
	 * 走盘
	 */
	public static final int DRAW = -1;

    }

    /**
     * 赛事关联赔率
     */
    public final static class ContestContainOdds {

	/**
	 * 胜平负
	 */
	public static final int FB_SPF = 1;

	/**
	 * 让球胜平负
	 */
	public static final int FB_RQSPF = 2;

	/**
	 * 亚盘
	 */
	public static final int FB_YP = 4;

	/**
	 * 大小球
	 */
	public static final int FB_SIZE = 8;

	/**
	 * 单双数
	 */
	public static final int FB_ODDEVEN = 16;
    }

    /**
     * 赛事关联赔率
     */
    public final static class ContestContainOdds_BB {

	/**
	 * 胜负
	 */
	public static final int BB_SPF = 1;

	/**
	 * 让球胜平负，竞彩
	 */
	public static final int BB_RQSPF = 2;

	/**
	 * 亚盘
	 */
	public static final int BB_YP = 4;

	/**
	 * 大小球
	 */
	public static final int BB_SIZE = 8;

	/**
	 * 单双数
	 */
	public static final int BB_ODDEVEN = 16;
    }

    /**
     * 龙筹向上取整策略
     * 
     * @param back
     * @return
     */
    public static int getCouponPriceByBack(double back) {
	if (back > CouponSystem.PIRCE_100) {
	    return CouponSystem.PIRCE_100;
	}
	int[] price = new int[] { CouponSystem.PIRCE_5, CouponSystem.PIRCE_10, CouponSystem.PIRCE_20, CouponSystem.PIRCE_50,
	        CouponSystem.PIRCE_100 };
	for (int i = 1; i < price.length; i++) {
	    if (back > price[i - 1] && back <= price[i]) {
		return price[i];
	    }
	}
	return CouponSystem.PIRCE_5;
    }
}
