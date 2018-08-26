package com.lifeix.cbs.api.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.lifeix.cbs.api.common.util.BetConstants;

/**
 * 系统龙筹券快速缓存
 * 
 * @author lifeix
 * 
 */
public class CouponSystemHelper {

    /**
     * 根据时间和面额为Key，快速找到对应龙筹券Id
     */
    private Map<String, Long> couponMaps = new HashMap<String, Long>();

    /**
     * 根据对应龙筹券Id快速找到对应价格
     */
    private Map<Long, Integer> priceMaps = new HashMap<Long, Integer>();

    private CouponSystemHelper() {
	couponMaps.put(String.format("%d-%d", 5, 6), 1L);
	couponMaps.put(String.format("%d-%d", 5, 12), 2L);
	couponMaps.put(String.format("%d-%d", 5, 24), 3L);
	couponMaps.put(String.format("%d-%d", 5, 48), 4L);
	couponMaps.put(String.format("%d-%d", 5, 72), 5L);

	couponMaps.put(String.format("%d-%d", 10, 6), 6L);
	couponMaps.put(String.format("%d-%d", 10, 12), 7L);
	couponMaps.put(String.format("%d-%d", 10, 24), 8L);
	couponMaps.put(String.format("%d-%d", 10, 48), 9L);
	couponMaps.put(String.format("%d-%d", 10, 72), 10L);

	couponMaps.put(String.format("%d-%d", 20, 6), 11L);
	couponMaps.put(String.format("%d-%d", 20, 12), 12L);
	couponMaps.put(String.format("%d-%d", 20, 24), 13L);
	couponMaps.put(String.format("%d-%d", 20, 48), 14L);
	couponMaps.put(String.format("%d-%d", 20, 72), 15L);

	couponMaps.put(String.format("%d-%d", 50, 6), 16L);
	couponMaps.put(String.format("%d-%d", 50, 12), 17L);
	couponMaps.put(String.format("%d-%d", 50, 24), 18L);
	couponMaps.put(String.format("%d-%d", 50, 48), 19L);
	couponMaps.put(String.format("%d-%d", 50, 72), 20L);

	couponMaps.put(String.format("%d-%d", 100, 6), 21L);
	couponMaps.put(String.format("%d-%d", 100, 12), 22L);
	couponMaps.put(String.format("%d-%d", 100, 24), 23L);
	couponMaps.put(String.format("%d-%d", 100, 48), 24L);
	couponMaps.put(String.format("%d-%d", 100, 72), 25L);

	priceMaps.put(1L, 5);
	priceMaps.put(2L, 5);
	priceMaps.put(3L, 5);
	priceMaps.put(4L, 5);
	priceMaps.put(5L, 5);
	priceMaps.put(6L, 10);
	priceMaps.put(7L, 10);
	priceMaps.put(8L, 10);
	priceMaps.put(9L, 10);
	priceMaps.put(10L, 10);
	priceMaps.put(11L, 20);
	priceMaps.put(12L, 20);
	priceMaps.put(13L, 20);
	priceMaps.put(14L, 20);
	priceMaps.put(15L, 20);
	priceMaps.put(16L, 50);
	priceMaps.put(17L, 50);
	priceMaps.put(18L, 50);
	priceMaps.put(19L, 50);
	priceMaps.put(20L, 50);
	priceMaps.put(21L, 100);
	priceMaps.put(22L, 100);
	priceMaps.put(23L, 100);
	priceMaps.put(24L, 100);
	priceMaps.put(25L, 100);
    }

    private static class InstanceHolder {
	private InstanceHolder() {
	}

	private static final CouponSystemHelper INSTANCE = new CouponSystemHelper();
    }

    public static CouponSystemHelper getInstance() {
	return InstanceHolder.INSTANCE;
    }

    /**
     * 根据面值和时间返回对应龙筹Id
     * 
     * @param price
     * @param hour
     * @return
     */
    public Long getCoupon(int price, int hour) {
	return couponMaps.get(String.format("%d-%d", price, hour));
    }

    /**
     * 根据龙筹Id返回对应面值
     * 
     * @param couponId
     * @return
     */
    public Integer getPrice(Long couponId) {
	return priceMaps.get(couponId);
    }

    /**
     * 根据数值计算可以获得的龙筹券
     */
    public static Map<Long, Integer> getCouponBack(Double back, int hour) {
	int price = BetConstants.getCouponPriceByBack(back);
	Map<Long, Integer> ret = new HashMap<Long, Integer>();
	CouponSystemHelper cs = CouponSystemHelper.getInstance();
	ret.put(cs.getCoupon(price, hour), 1);
	return ret;
    }

    /**
     * 动态替换日志参数
     */
    @SuppressWarnings("unchecked")
    public static String replaceLogParams(String content, String params) throws JSONException {
	JSONObject param = new JSONObject(params);
	for (Iterator<String> iterator = param.keys(); iterator.hasNext();) {
	    String key = iterator.next().toString();
	    Pattern pat = Pattern.compile("\\{#" + key + "\\}");
	    Matcher mat = pat.matcher(content);
	    content = mat.replaceAll(param.getString(key));
	}
	return content;
    }
}
