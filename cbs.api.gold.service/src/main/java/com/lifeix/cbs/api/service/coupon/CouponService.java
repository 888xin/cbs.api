package com.lifeix.cbs.api.service.coupon;

import com.lifeix.cbs.api.bean.coupon.CouponListResponse;
import com.lifeix.cbs.api.bean.coupon.CouponResponse;
import com.lifeix.exception.service.L99IllegalParamsException;

public interface CouponService {

    /**
     * 查询龙筹券
     * 
     * @param id
     * @return
     */
    public CouponResponse findById(Long id);

    /**
     * 新增或修改龙筹券
     */
    public void saveOrUpdate(Long id, Integer hour, String name, Integer price, Integer rangeKey, String rangeValue,
	    Integer sum, Integer num, Double proportion, String descr, Boolean isvalid) throws L99IllegalParamsException;

    /**
     * 获得龙筹卷列表
     */
    public CouponListResponse selectCoupons(Integer type, Boolean valid, Long startId, int limit);

    /**
     * 改变龙筹券的状态（激活与失效）
     */
    public void toggerValid(Long id, Boolean isvalid) throws L99IllegalParamsException;

}
