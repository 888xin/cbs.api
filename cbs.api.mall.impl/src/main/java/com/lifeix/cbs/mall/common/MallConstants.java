package com.lifeix.cbs.mall.common;

public class MallConstants {

    /**
     * 商品状态
     */
    public static final class GoodsStatus {

	/**
	 * 下架
	 */
	public static final int OFF = 0;

	/**
	 * 上架
	 */
	public static final int ON = 1;

	/**
	 * 删除
	 */
	public static final int DEL = 2;

    }

    /**
     * 商品类型
     * 
     * @author lifeix
     * 
     */
    public static final class GoodsType {

	/**
	 * 实物商品
	 */
	public static final int REAL = 0;

	/**
	 * 手机充值卡
	 */
	public static final int MOBILE_CARD = 1;

    }

    /**
     * 订单状态
     * 
     * @author lifeix
     * 
     */
    public static final class OrderStatus {

	/**
	 * 未付款
	 */
	public static final int INIT = 0;

	/**
	 * 未发货(已付款)
	 */
	public static final int PAY = 1;

	/**
	 * 未确认(已发货)
	 */
	public static final int SEND = 2;

	/**
	 * 已完成
	 */
	public static final int DONE = 3;

	/**
	 * 取消
	 */
	public static final int CAN = 10;

    }

    /**
     * 商品锁
     * 
     * @author lifeix
     * 
     */
    public static final class GoodsLock {
	/**
	 * 商品库存锁
	 */
	public static final String STOCK_LOCK = "stock:lock:";

	/**
	 * 用户购买锁
	 */
	public static final String USER_LOCK = "user:lock:";

	/**
	 * 锁的超时时间3分钟
	 */
	public static final int EXPIRE = 3 * 60;
    }

}
