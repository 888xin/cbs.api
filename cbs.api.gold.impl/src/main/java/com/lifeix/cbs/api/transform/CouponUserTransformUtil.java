package com.lifeix.cbs.api.transform;

import com.lifeix.cbs.api.bean.coupon.CouponUserResponse;
import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.api.dto.coupon.Coupon;
import com.lifeix.cbs.api.dto.coupon.CouponUser;

/**
 * DTO 转换为 VO 工具类
 * 
 * @author jacky
 * 
 */
public class CouponUserTransformUtil {

    /**
     * 转换龙筹券对象
     * 
     * @param u
     * @param c
     * @param isReceiver
     * @return
     */
    public static CouponUserResponse transformCouponUser(CouponUser u, Coupon c) {
	CouponUserResponse resp = null;
	if (u != null && c != null) {
	    resp = new CouponUserResponse();
	    resp.setId(u.getId());
	    resp.setCoupon_id(u.getId());
	    resp.setUsed(u.isUsed());
	    resp.setPrice(u.getPrice());
	    resp.setUser_id(u.getUserId());
	    resp.setProportion(c.getProportion());
	    resp.setStart_time(CbsTimeUtils.getUtcTimeForDate(u.getStartTime()));
	    resp.setEnd_time(CbsTimeUtils.getUtcTimeForDate(u.getEndTime()));
	    resp.setUpdate_time(CbsTimeUtils.getUtcTimeForDate(u.getUpdateTime()));
	    resp.setName(c.getName());
	    resp.setDescr(c.getDescr());
	    resp.setType(c.getType());
	    resp.setHour(c.getHour());
	    resp.setRange_key(c.getRangeKey());
	    resp.setRange_value(c.getRangeValue());
	    resp.setValid(c.isValid());
	}
	return resp;
    }

}
