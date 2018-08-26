package com.lifeix.cbs.contest.bean.yy;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.cbs.api.bean.coupon.CouponUserListResponse;
import com.lifeix.user.beans.Response;

/**
 * 押押赛事和下单信息
 * 
 * @author Peter
 * 
 */
public class YyOddsResponse implements JsonSerializer<YyOddsResponse>, Response {

    private static final long serialVersionUID = 810585290479416582L;

    /**
     * 用户龙币或龙筹
     */
    private double gold;

    /**
     * 龙筹券列表
     */
    private CouponUserListResponse coupons;

    /**
     * 赛事信息
     */
    private YyContestResponse contest;

    /**
     * 下单信息
     */
    private List<YyBetResponse> bets;

    /**
     * 可领取的龙筹公告Id
     */
    private String placardId;

    public double getGold() {
	return gold;
    }

    public void setGold(double gold) {
	this.gold = gold;
    }

    public YyContestResponse getContest() {
	return contest;
    }

    public void setContest(YyContestResponse contest) {
	this.contest = contest;
    }

    public List<YyBetResponse> getBets() {
	return bets;
    }

    public void setBets(List<YyBetResponse> bets) {
	this.bets = bets;
    }

    public CouponUserListResponse getCoupons() {
	return coupons;
    }

    public void setCoupons(CouponUserListResponse coupons) {
	this.coupons = coupons;
    }

    public String getPlacardId() {
	return placardId;
    }

    public void setPlacardId(String placardId) {
	this.placardId = placardId;
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(YyOddsResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }
}
