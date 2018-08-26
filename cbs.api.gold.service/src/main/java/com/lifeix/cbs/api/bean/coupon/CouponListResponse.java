package com.lifeix.cbs.api.bean.coupon;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

import java.util.List;

public class CouponListResponse extends ListResponse implements Response{

    private static final long serialVersionUID = 8768903024793066363L;

    private List<CouponResponse> conpons;

    public List<CouponResponse> getConpons() {
        return conpons;
    }

    public void setConpons(List<CouponResponse> conpons) {
        this.conpons = conpons;
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
