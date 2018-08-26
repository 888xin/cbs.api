package com.lifeix.cbs.contest.util;

import java.util.List;

import org.json.JSONException;

import com.lifeix.cbs.api.common.util.BetConstants;
import com.lifeix.cbs.api.common.util.CommerceMath;
import com.lifeix.cbs.api.common.util.CouponConstants;
import com.lifeix.cbs.api.service.coupon.CouponUserService;
import com.lifeix.cbs.api.service.money.MoneyService;
import com.lifeix.cbs.contest.dto.bet.BetJc;
import com.lifeix.cbs.contest.dto.bet.BetOp;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * Created by lhx on 16-3-17 上午11:34
 *
 * @Description
 */
public class ContestRollbackUtil {

    public static double[] betOpRollback(List<BetOp> opList, CouponUserService couponUserService, MoneyService moneyService) throws L99IllegalParamsException, L99IllegalDataException, JSONException {
        //0加龙币 1扣龙币 2加龙筹
        double[] total = new double[]{0, 0, 0};
        if (opList.size() > 0) {
            //返钱或者扣钱
            for (BetOp betOp : opList) {
                if (betOp.getStatus() == BetConstants.BetResultStatus.WIN) {
                    //赢 扣钱，主要扣龙币，龙筹券下单，暂时不扣
                    Double back = betOp.getBack();
                    Double bet = betOp.getBet();
                    Long userId = betOp.getUserId();
                    boolean longbi = betOp.isLongbi();
                    Integer coupon = betOp.getCoupon();
                    if (back != null && back > 0) {
                        if (longbi) {
                            //龙币下单（包括龙筹券+龙币）
                            if (coupon != null) {
                                //有使用龙筹券
                                //返回龙筹券（因不知使用什么类型的龙筹券，统一返回系统通用券）
                                couponUserService.settleCouponByPrice(userId, coupon, CouponConstants.CouponSystem.TIME_24, "赛事错误回滚返龙筹券");
                                total[2] += coupon;
                                //扣去赢得的龙币
                                double money = back - bet + coupon;
                                moneyService.consumeMoney(userId, money, "赛事错误回滚扣钱", null);
                                total[1] = CommerceMath.add(total[1], bet);
                            } else {
                                //纯龙币下单
                                double money = back - bet;
                                moneyService.consumeMoney(userId, money, "赛事错误回滚扣钱", null);
                                total[1] = CommerceMath.add(total[1], bet);
                            }
                        }
                    }
                } else if (betOp.getStatus() == BetConstants.BetResultStatus.LOSS) {
                    //输钱，返回给用户
                    Double bet = betOp.getBet();
                    Long userId = betOp.getUserId();
                    boolean longbi = betOp.isLongbi();
                    Integer coupon = betOp.getCoupon();
                    if (bet != null && bet > 0) {
                        if (longbi) {
                            //龙币下单（包括龙筹券+龙币）
                            if (coupon != null) {
                                //有使用龙筹券
                                //返回龙筹券（因不知使用什么类型的龙筹券，统一返回系统通用券）
                                couponUserService.settleCouponByPrice(userId, coupon, CouponConstants.CouponSystem.TIME_24, "赛事错误回滚返龙筹券");
                                total[2] += coupon;
                                //加输掉的龙币
                                double money = bet - coupon;
                                moneyService.consumeMoney(userId, money, "赛事错误回滚加钱", null);
                                total[0] = CommerceMath.add(total[0], money);
                            } else {
                                //纯龙币下单
                                moneyService.consumeMoney(userId, bet, "赛事错误回滚加钱", null);
                                total[0] = CommerceMath.add(total[0], bet);
                            }
                        } else {
                            //龙筹券下单
                            if (coupon != null) {
                                //加龙筹券
                                couponUserService.settleCouponByPrice(userId, coupon, CouponConstants.CouponSystem.TIME_24, "赛事错误回滚返龙筹券");
                                total[2] += coupon;
                            }
                        }
                    }
                }
            }
        }
        return total;
    }

    public static double[] betJcRollback(List<BetJc> jcList, CouponUserService couponUserService, MoneyService moneyService) throws L99IllegalParamsException, L99IllegalDataException, JSONException {
        //0加龙币 1扣龙币 2加龙筹
        double[] total = new double[]{0, 0, 0};
        if (jcList.size() > 0) {
            for (BetJc betJc : jcList) {
                if (betJc.getStatus() == BetConstants.BetResultStatus.WIN) {
                    //赢 扣钱，主要扣龙币，龙筹券下单，暂时不扣
                    Double back = betJc.getBack();
                    Double bet = betJc.getBet();
                    Long userId = betJc.getUserId();
                    boolean longbi = betJc.isLongbi();
                    Integer coupon = betJc.getCoupon();
                    if (back != null) {
                        if (longbi) {
                            //龙币下单（包括龙筹券+龙币）
                            if (coupon != null) {
                                //有使用龙筹券
                                //返回龙筹券（因不知使用什么类型的龙筹券，统一返回系统通用券）
                                couponUserService.settleCouponByPrice(userId, coupon, CouponConstants.CouponSystem.TIME_24, "赛事错误回滚返龙筹券");
                                total[2] += coupon;
                                //扣去赢得的龙币
                                double money = back - bet + coupon;
                                moneyService.consumeMoney(userId, money, "赛事错误回滚扣钱", null);
                                total[1] = CommerceMath.add(total[1], money);
                            } else {
                                //纯龙币下单
                                double money = back - bet;
                                moneyService.consumeMoney(userId, money, "赛事错误回滚扣钱", null);
                                total[1] = CommerceMath.add(total[1], money);
                            }
                        }
                    }
                } else if (betJc.getStatus() == BetConstants.BetResultStatus.LOSS) {
                    //输钱，返回给用户
                    Double bet = betJc.getBet();
                    Long userId = betJc.getUserId();
                    boolean longbi = betJc.isLongbi();
                    Integer coupon = betJc.getCoupon();
                    if (bet != null) {
                        if (longbi) {
                            //龙币下单（包括龙筹券+龙币）
                            if (coupon != null) {
                                //有使用龙筹券
                                //返回龙筹券（因不知使用什么类型的龙筹券，统一返回系统通用券）
                                couponUserService.settleCouponByPrice(userId, coupon, CouponConstants.CouponSystem.TIME_24, "赛事错误回滚返龙筹券");
                                total[2] += coupon;
                                //加输掉的龙币
                                double money = bet - coupon;
                                moneyService.consumeMoney(userId, money, "赛事错误回滚加钱", null);
                                total[0] = CommerceMath.add(total[0], money);
                            } else {
                                //纯龙币下单
                                moneyService.consumeMoney(userId, bet, "赛事错误回滚加钱", null);
                                total[0] = CommerceMath.add(total[0], bet);
                            }
                        } else {
                            //龙筹券下单
                            if (coupon != null) {
                                //加龙筹券
                                couponUserService.settleCouponByPrice(userId, coupon, CouponConstants.CouponSystem.TIME_24, "赛事错误回滚返龙筹券");
                                total[2] += coupon;
                            }
                        }
                    }
                }
            }
        }
        return total;
    }
}
