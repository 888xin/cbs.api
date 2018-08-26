package com.lifeix.cbs.api.common.util;

/**
 * 龙筹券常量
 * 
 * @author lifeix
 * 
 */
public final class CouponConstants {

    // 龙筹券使用范围（数值越高，等级最低）
    public final static class RangeKey {

	// 默认龙筹券
	public static final int COUPON_DEFAULT = 90;

	// 足球联赛龙筹券
	public static final int FB_COUPON_CUP = 20;

	// 篮球联赛龙筹券
	public static final int BB_COUPON_CUP = 21;

	// 足球赛事龙筹券
	public static final int FB_COUPON_CONTEST = 10;

	// 篮球赛事龙筹券
	public static final int BB_COUPON_CONTEST = 11;

	// 押押龙筹券
	public static final int COUPON_YY = 30;

    }

    // 龙筹券类型
    public final static class CouponType {

	// 系统
	public static final int COUPON_SYSTEM = 0;

	// 活动
	public static final int COUPON_PROCEEDINGS = 1;

    }

    // 系统龙筹枚举
    public final static class CouponSystem {

	// 6个小时
	public static final int TIME_6 = 6;

	// 12个小时
	public static final int TIME_12 = 12;

	// 24个小时
	public static final int TIME_24 = 24;

	// 48个小时
	public static final int TIME_48 = 48;

	// 72个小时
	public static final int TIME_72 = 72;

	// 5元面值
	public static final int PIRCE_5 = 5;

	// 10元面值
	public static final int PIRCE_10 = 10;

	// 20元面值
	public static final int PIRCE_20 = 20;

	// 50元面值
	public static final int PIRCE_50 = 50;

	// 100元面值
	public static final int PIRCE_100 = 100;

	// 1000元面值
	public static final int PIRCE_1000 = 1000;

    }

    // 龙筹券上限枚举
    public final static class CouponLimit {

	// 用户可用的龙筹券
	public static final int COUPON_AVTIVE = 100;

    }
}
