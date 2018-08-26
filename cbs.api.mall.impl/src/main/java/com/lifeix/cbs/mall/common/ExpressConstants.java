/**
 * 
 */
package com.lifeix.cbs.mall.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 物流常量
 * 
 * @author lifeix
 *
 */
public class ExpressConstants {

    /**
     * 快递公司编码与数据库快递类型对应集合
     */
    public static Map<Integer, String> shipperCodeMap = new HashMap<Integer, String>();

    static {
	shipperCodeMap.put(1, "SF");// 顺丰
	shipperCodeMap.put(2, "YTO");// 圆通
	shipperCodeMap.put(3, "STO");// 申通
	shipperCodeMap.put(4, "YD");// 韵达
	shipperCodeMap.put(5, "ZTO");// 中通
	shipperCodeMap.put(6, "EMS");// EMS
	shipperCodeMap.put(7, "DBL");// 德邦
	shipperCodeMap.put(8, "FEDEX");// FEDEX
    }

    /**
     * 物流状态
     * 
     * @author lifeix
     *
     */
    public final static class State {
	/**
	 * 无物流信息
	 */
	public static final int START = 1;
	/**
	 * 在途中
	 */
	public static final int TZ = 2;
	/**
	 * 已签收
	 */
	public static final int QX = 3;
	/**
	 * 问题件
	 */
	public static final int WT = 4;
    }

}
