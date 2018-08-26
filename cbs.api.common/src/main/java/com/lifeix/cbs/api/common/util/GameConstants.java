package com.lifeix.cbs.api.common.util;


public class GameConstants {
    
    /**
     * 时彩游戏
     * @author lifeix
     *
     */
    public static final class Shicai{
	
	public static final long goldPoint = 10;// 每一注的龙筹
	
    }
    
    
    /** 关于支付配置 **/
    public static class GoldPaymentLogType {
	/** 床点红包消费 */
	public static final String GOLD_BUY = "GOLD_BUY";
	/** 床点添加 */
	public static final String GOLD_ADD = "GOLD_ADD";
	/** 床点减少 */
	public static final String GOLD_REDUCE = "GOLD_REDUCE";
	/** 龙币换床点 */
	public static final String LONGBI_TO_GOLD = "LONGBI_TO_GOLD";
	/** 退还床币 */
	public static final String GOLD_BACKA = "GOLD_BACKA";

//	/** 置顶 */
//	public static final String BED_HELP_TOP = "BED_HELP_TOP";
//
//	/** 附近排行榜 */
//	public static final String BED_RECOMMEND = "BED_RECOMMEND";
//	/**
//	 * 床点购买礼物
//	 */
//	public static final String BED_BUY_PRESENT = "BED_BUY_PRESENT";
//	/**
//	 * 床点礼物提成
//	 */
//	public static final String BED_PRESENT_COMMISSION = "BED_PRESENT_COMMISSION";
//
//	/**
//	 * 猜拳游戏
//	 */
//	public static final String BED_GUESS_FINGER = "BED_GUESS_FINGER";

    }
    
}
