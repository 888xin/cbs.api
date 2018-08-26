package com.lifeix.cbs.api.service.coupon;

import java.util.List;

import com.lifeix.cbs.api.bean.coupon.CouponUserListResponse;
import com.lifeix.cbs.api.bean.coupon.CouponUserResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.user.beans.CustomResponse;

public interface CouponUserService {

    /**
     * 发放或领取龙筹券
     * 
     * @param couponId
     * @param userId
     * @param innerFlag
     * @throws L99IllegalDataException
     * @throws L99IllegalParamsException
     */
    public void grantCoupon(Long couponId, Long userId, boolean innerFlag) throws L99IllegalDataException,
	    L99IllegalParamsException;

    /**
     * 用户是否已经领过该龙筹券以及剩余的数量
     */
    public CouponUserResponse checkCoupon(Long couponId, Long userId) throws L99IllegalParamsException;

    /**
     * 根据面值和时间发放龙筹券
     * 
     * @param userId
     * @param price
     * @param hour
     * @param desc
     * @return
     */
    public void settleCouponByPrice(Long userId, int price, int hour, String desc) throws L99IllegalParamsException,
	    L99IllegalDataException;

    /**
     * 结算赛事龙筹券
     * 
     * @param userId
     * @param back
     * @param desc
     * @return
     */
    public Long settleCouponByBack(Long userId, Double back, String desc);

    /**
     * 获取龙筹券详情
     * 
     * @param id
     * @param assertFlag
     *            是否判断龙筹券有效性
     * @return
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    public CouponUserResponse findCouponDetail(Long id, boolean assertFlag) throws L99IllegalParamsException,
	    L99IllegalDataException;

    /**
     * 使用龙筹券
     * 
     * @param couponUserId
     * @param userId
     * @param longbi
     * @param bet
     *            下单的龙币金额
     * @param contestType
     * @param cupId
     * @param contestId
     * @param desc
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    public void useCoupon(Long couponUserId, Long userId, boolean longbi, Double bet, Integer contestType, Long cupId,
	    Long contestId, String desc) throws L99IllegalParamsException, L99IllegalDataException;

    /**
     * 获取用户可下单的龙筹券列表
     * 
     * @param userId
     * @param contestId
     * @param cupId
     * @param contestType
     * @return
     */
    public CouponUserListResponse findUserBetCoupons(Long userId, Long contestId, Long cupId, Integer contestType);

    /**
     * 获取用户指定面额可下单的龙筹券列表
     * 
     * @param userId
     * @param contestId
     * @param cupId
     * @param contestType
     * @param filterPrice
     * @return
     */
    public CouponUserListResponse findUserBetCoupons(Long userId, Long contestId, Long cupId, Integer contestType,
	    List<Integer> filterPrice);

    /**
     * 用户获得龙筹券列表
     * 
     * @param userId
     * @param isUsed
     * @return
     */
    public CouponUserListResponse findUserCouponList(Long userId, Boolean isUsed, Long startId, Integer limit);

    /**
     * 向指定用户发送龙筹券
     * 
     * @param couponId
     * @param userIds
     * @return
     * @throws L99IllegalParamsException
     */
    public CustomResponse sendCouponToUser(Long couponId, String[] userIds, Integer num) throws L99IllegalParamsException,
	    L99IllegalDataException;

    /**
     * 向指定用户发送num张couponId龙筹券
     * 
     * @param couponId
     *            龙筹券id
     * @param userId
     *            用户id
     * @param num
     *            数量
     * @return
     * @throws L99IllegalParamsException
     */
    public void sendCouponToUser(Long couponId, Long userId, int num, String desc) throws L99IllegalParamsException,
	    L99IllegalDataException;

    /**
     * 获取用户可用于下单游戏的券
     */
    public List<Object[]> findUserZodiacGameBetCoupons(Long userId, List<Integer> price);

    /**
     * 批量使用龙筹券
     */
    public int useManyCoupons(String couponUserIds, Long userId, String desc) throws L99IllegalParamsException,
	    L99IllegalDataException;

}
