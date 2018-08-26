package com.lifeix.cbs.api.dao.coupon;

import java.util.Date;
import java.util.List;

import com.lifeix.cbs.api.dao.BasicDao;
import com.lifeix.cbs.api.dto.coupon.CouponUser;

public interface CouponUserDao extends BasicDao<CouponUser, Long> {

    /**
     * 查找即将到期的龙筹券
     * 
     * @param notifyFlag
     * @param endTime
     * @param startId
     * @param limit
     * @return
     */
    public List<CouponUser> findMessage(Boolean notifyFlag, Date endTime, Long startId, int limit);

    /**
     * 分组查询用户后一天过期龙筹券
     * 
     * @param used
     * @param endTime
     * @return
     */
    public List<CouponUser> findPigeonMess(Boolean used, Date endTime, Long startId, int limit);

    /**
     * 批量更新用户发送消息标志位
     * 
     * @param ids
     * @param notifyFlag
     * @return
     */
    public boolean updateNotifyFlagByIds(List<Long> ids, Boolean notifyFlag);

    /**
     * 批量查找用户的龙筹券记录
     */
    public List<CouponUser> findCouponUsersByIds(List<Long> ids);

    /**
     * 查找用户可用的龙筹券(未使用并未过期)
     * 
     * @param userId
     * @param filterPrice
     *            指定价格
     * @param startId
     * @param limit
     * @return
     */
    public List<CouponUser> findUserActiveCoupon(Long userId, List<Integer> filterPrice, Long startId, int limit);

    /**
     * 查找用户过期的龙筹券
     * 
     * @param userId
     * @param startId
     * @param limit
     * @return
     */
    public List<CouponUser> findUserExpiredCoupons(Long userId, Long startId, int limit);

    /**
     * 对同一用户发多个同一的龙筹券
     * 
     * @param list
     * @return
     */
    public boolean insertByBatch(List<CouponUser> list);

}
