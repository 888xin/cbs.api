package com.lifeix.cbs.api.dao.coupon;

import java.util.List;
import java.util.Map;

import com.lifeix.cbs.api.dao.BasicDao;
import com.lifeix.cbs.api.dto.coupon.Coupon;

public interface CouponDao extends BasicDao<Coupon, Long> {
    /**
     * 批量查找龙筹券
     */
    public Map<Long, Coupon> findMapByIds(List<Long> ids);

    /**
     * 获取指定条件的龙筹券
     */
    public List<Coupon> selectCoupons(Integer type, Boolean valid, Long startId, int limit);

    /**
     * 龙筹券递增库存
     * 
     * @param couponId
     * @return
     */
    public boolean incrementCoupon(Long couponId);

}
