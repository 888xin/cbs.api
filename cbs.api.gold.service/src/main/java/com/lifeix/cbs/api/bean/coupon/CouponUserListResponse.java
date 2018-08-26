package com.lifeix.cbs.api.bean.coupon;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

/**
 * 龙筹券列表
 * 
 * @author lifeix
 * 
 */
public class CouponUserListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = -4041270812214225792L;

    private List<CouponUserResponse> coupons;

    public List<CouponUserResponse> getCoupons() {
	return coupons;
    }

    public void setCoupons(List<CouponUserResponse> coupons) {
	this.coupons = coupons;
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
