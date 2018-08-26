package com.lifeix.cbs.api.common.util;

/**
 * 常量定义
 * 
 * @author jacky
 * 
 */
public class GoldConstants {

    /**
     * 支付方式
     */
    public final static class PaymentType {

	/**
	 * 支付宝 - 即时到账
	 */
	public static final int ALIPAY = 1;

	/**
	 * 支付宝 - 手机即时到账
	 */
	public static final int ALIPAY_WAP = 11;

	/**
	 * 支付宝 - 快捷支付
	 */
	public static final int ALIPAY_FAST = 12;

	/**
	 * 快钱
	 */
	public static final int KUAIBIll = 2;

	/**
	 * 手机支付
	 */
	public static final int MOBILE = 3;

	/**
	 * 银联支付
	 */
	public static final int UNIONPAY = 4;

	/**
	 * 储值卡-中国银行
	 */
	public static final int STORED_BOC = 101;

	/**
	 * 储值卡-工商银行
	 */
	public static final int STORED_ICBC = 102;

	/**
	 * 储值卡-农业银行
	 */
	public static final int STORED_ABC = 103;

	/**
	 * 储值卡-建设银行
	 */
	public static final int STORED_CCB = 104;

	/**
	 * 储值卡-招商银行
	 */
	public static final int STORED_CMB = 105;

	/**
	 * 储值卡-交通银行
	 */
	public static final int STORED_COMM = 106;

	/**
	 * 储值卡-广发银行
	 */
	public static final int STORED_CGB = 107;

	/**
	 * 储值卡-浦发银行
	 */
	public static final int STORED_SPDB = 108;

	/**
	 * 储值卡-兴业银行
	 */
	public static final int STORED_CIB = 109;

	/**
	 * 信用卡-中国银行
	 */
	public static final int CREDIT_BOC = 201;

	/**
	 * 信用卡-工商银行
	 */
	public static final int CREDIT_ICBC = 202;

	/**
	 * 信用卡-农业银行
	 */
	public static final int CREDIT_ABC = 203;

	/**
	 * 信用卡-建设银行
	 */
	public static final int CREDIT_CCB = 204;

	/**
	 * 信用卡-招商银行
	 */
	public static final int CREDIT_CMB = 205;

	/**
	 * 信用卡-交通银行
	 */
	public static final int CREDIT_COMM = 206;

	/**
	 * 信用卡-广发银行
	 */
	public static final int CREDIT_CGB = 207;

	/**
	 * 信用卡-浦发银行
	 */
	public static final int CREDIT_SPDB = 208;

	/**
	 * 信用卡-兴业银行
	 */
	public static final int CREDIT_CIB = 209;

    }

    /*  *//**
     * 任务类型
     */
    /*
     * public static final class MeadalTaskType {
     *//**
     * 用户下单结算统计数据任务（全部统计）
     */
    /*
     * public static final Long USER_CONTEST_STATISTICS = 1L;
     *//**
     * 用户下单结算统计周表、月表任务
     */
    /*
     * public static final Long USER_CONTEST_STATISTICS_PEROID = 2L; }
     */

    /**
     * 数据上限
     * 
     */
    public final static class RoiMax {

	/**
	 * 联赛上限
	 */
	public static final int MAX_CUP = 2000;

	/**
	 * 球队上限
	 */
	public static final int MAX_TEAM = 25000;

	/**
	 * 活动赛事上限
	 */
	public static final int MAX_CONTEST = 700;

	/**
	 * 赔率上限
	 */
	public static final int MAX_ODDS = 2000;

    }

    /**
     * 用户统计相关的常量
     */
    public final static class Statistics {

	/**
	 * 投资回报率的排名人数
	 */
	public static final int MAX_ROI_RANK = 100;

	/**
	 * 投资回报率月排名人数
	 */
	public static final int MAX_ROI_MONTH_RANK = 50;

	/**
	 * 胜率排名的人数
	 */
	public static final int MAX_WINNING_RANK = 10;

	/**
	 * 全站用户排名数据库记录id
	 */
	public static final int ALL_USER_RANK = 1;

	/**
	 * 每个积分对应的用户数数据库记录id
	 */
	public static final int USER_RANK_NUM = 2;
    }

    /**
     * 篮球比赛类型
     */
    public static final class BbKind {

	public static final String CHANGGUISAI = "常规赛";

	public static final String JIQIANSAI = "季前赛";

	public static final String JIHOUSAI = "季后赛";
    }

    /**
     * 数据版本
     * 
     * @author Peter
     * 
     */
    public static final class ContestOddsTypes {

	/**
	 * 足球球迷版 胜平负(1) + 北单(2)
	 */
	public static final int FB_NORMAL = 3;

	/**
	 * 足球专业版 胜平负(1) + 北单(2) + 亚盘(4) + 大小球(8) + 单双数(16) = 31
	 */
	public static final int FB_PROFESSION = 31;

	/**
	 * 篮球球迷版 胜负(1) + 竞彩(2)
	 */
	public static final int BB_NORMAL = 3;

	/**
	 * 篮球专业版 胜负(1) + 竞彩(2) + 亚盘(4) + 大小球(8) + 单双数(16) = 31
	 */
	public static final int BB_PROFESSION = 31;

    }

    /**
     * 商城订单状态
     * 
     * @author Peter
     * 
     */
    public static class OrderMallStatus {

	/** 买家未付款。 */
	public static final Integer NOT_PAID = 0;
    }

    /**
     * 订单优惠状态枚举
     */
    public static class OrdersDownStatus {

	// 未验证
	public static final int DOWN_INIT = 0;

	// 验证
	public static final int DOWN_DONE = 1;

	// 取消订单
	public static final int DOWN_CANCLE = -1;
    }

    /**
     * 用户龙币收支类型
     */
    public static class MoneyStatisticType {

	// 收入
	public static final int ADD = 0;

	// 支出
	public static final int MINUS = 1;
    }

    /**
     * 用户龙币收支排序
     */
    public static class MoneyStatisticOrder {

	// 收入降序
	public static final int ADD_DESC = 0;
	// 收入升序
	public static final int ADD_ASC = 1;
	// 支出降序
	public static final int MINUS_DESC = 2;
	// 支出升序
	public static final int MINUS_ASC = 3;
    }

    /**
     * 龙币丢失操作类型
     */
    public static class MoneyMissedType {

	// 足球胜平负结算
	public static final int FB_SPF_SETTLE = 1;

	// 足球让球胜平负结算
	public static final int FB_JC_SETTLE = 2;

	// 足球大小球结算
	public static final int FB_SIZE_SETTLE = 4;

	// 足球单双数结算
	public static final int FB_ODDEVEN_SETTLE = 5;

	// 篮球胜负结算
	public static final int BB_SPF_SETTLE = 6;

	// 篮球让球胜负结算
	public static final int BB_JC_SETTLE = 7;

	// 篮球大小球结算
	public static final int BB_SIZE_SETTLE = 9;

	// 篮球单双数结算
	public static final int BB_ODDEVEN_SETTLE = 10;

	// 押押结算
	public static final int YY_SETTLE = 11;

	// 活动串结算
	public static final int BUNCH_SETTLE = 12;

	// 商城订单取消
	public static final int MALL_CANCLE = 21;
    }

}
