package com.lifeix.cbs.api.transform;

import com.lifeix.cbs.api.bean.coupon.CouponResponse;
import com.lifeix.cbs.api.dto.coupon.Coupon;

/**
 * Created by lhx on 16-2-25 下午2:55
 * 
 * @Description
 */
public class CouponTransformUtil {

    public static CouponResponse transformGold(Coupon coupon) {
	CouponResponse resp = null;
	if (coupon != null) {
	    resp = new CouponResponse();
	    resp.setId(coupon.getId());
	    resp.setType(coupon.getType());
	    resp.setHour(coupon.getHour());
	    resp.setName(coupon.getName());
	    resp.setPrice(coupon.getPrice());
	    resp.setRange_key(coupon.getRangeKey());
	    resp.setRange_value(coupon.getRangeValue());
	    resp.setSum(coupon.getSum());
	    resp.setNum(coupon.getNum());
	    resp.setProportion(coupon.getProportion());
	    resp.setDescr(coupon.getDescr());
	    resp.setValid(coupon.isValid());
	}
	return resp;
    }
}
