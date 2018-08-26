package com.lifeix.cbs.contest.bean.odds;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.cbs.api.bean.coupon.CouponUserListResponse;
import com.lifeix.cbs.contest.bean.bet.BetJcListResponse;
import com.lifeix.cbs.contest.bean.bet.BetOddevenListResponse;
import com.lifeix.cbs.contest.bean.bet.BetOpListResponse;
import com.lifeix.cbs.contest.bean.bet.BetSizeListResponse;
import com.lifeix.cbs.contest.bean.fb.ContestResponse;
import com.lifeix.user.beans.Response;

/**
 * 赛事赔率信息
 * 
 * @author Peter
 * 
 */
public class OddsResponse implements JsonSerializer<OddsResponse>, Response {

    private static final long serialVersionUID = -2700455997160368089L;

    /**
     * 用户龙币
     */
    private double gold;

    /**
     * 赛事当前状态
     */
    private int status;

    /**
     * 赛事信息
     */
    private ContestResponse contest;

    /**
     * 胜平负信息
     */
    private OddsOpResponse op;

    /**
     * 胜平负下单信息
     */
    private BetOpListResponse op_bets;

    /**
     * 让球胜平负信息
     */
    private OddsJcResponse jc;

    /**
     * 让球胜平负下单信息
     */
    private BetJcListResponse jc_bets;

    /**
     * 大小球信息
     */
    private OddsSizeResponse size;

    /**
     * 大小球下单信息
     */
    private BetSizeListResponse size_bets;

    /**
     * 单双数信息
     */
    private OddsDssResponse oddeven;

    /**
     * 单双数下单信息
     */
    private BetOddevenListResponse oddeven_bets;

    /**
     * 胜平负统计信息
     */
    private Integer[] op_count;

    /**
     * 让球胜平负统计信息
     */
    private Integer[] jc_count;

    /**
     * 大小球统计信息
     */
    private Integer[] size_count;

    /**
     * 单双数统计信息
     */
    private Integer[] oddeven_count;

    /**
     * 龙筹券列表
     */
    private CouponUserListResponse coupons;

    /**
     * 是否有赛事分析
     */
    private boolean has_news;

    /**
     * 是否有下单理由
     */
    private boolean has_reason;

    /**
     * 关联公告ID
     */
    private String placardId;

    public boolean isHas_reason() {
	return has_reason;
    }

    public void setHas_reason(boolean has_reason) {
	this.has_reason = has_reason;
    }

    public BetOddevenListResponse getOddeven_bets() {
	return oddeven_bets;
    }

    public void setOddeven_bets(BetOddevenListResponse oddeven_bets) {
	this.oddeven_bets = oddeven_bets;
    }

    public OddsDssResponse getOddeven() {
	return oddeven;
    }

    public void setOddeven(OddsDssResponse oddeven) {
	this.oddeven = oddeven;
    }

    public Integer[] getOddeven_count() {
	return oddeven_count;
    }

    public void setOddeven_count(Integer[] oddeven_count) {
	this.oddeven_count = oddeven_count;
    }

    public OddsSizeResponse getSize() {
	return size;
    }

    public void setSize(OddsSizeResponse size) {
	this.size = size;
    }

    public BetSizeListResponse getSize_bets() {
	return size_bets;
    }

    public void setSize_bets(BetSizeListResponse size_bets) {
	this.size_bets = size_bets;
    }

    public Integer[] getSize_count() {
	return size_count;
    }

    public void setSize_count(Integer[] size_count) {
	this.size_count = size_count;
    }

    public boolean isHas_news() {
	return has_news;
    }

    public void setHas_news(boolean has_news) {
	this.has_news = has_news;
    }

    public double getGold() {
	return gold;
    }

    public void setGold(double gold) {
	this.gold = gold;
    }

    public int getStatus() {
	return status;
    }

    public void setStatus(int status) {
	this.status = status;
    }

    public ContestResponse getContest() {
	return contest;
    }

    public void setContest(ContestResponse contest) {
	this.contest = contest;
    }

    public OddsOpResponse getOp() {
	return op;
    }

    public void setOp(OddsOpResponse op) {
	this.op = op;
    }

    public BetOpListResponse getOp_bets() {
	return op_bets;
    }

    public void setOp_bets(BetOpListResponse op_bets) {
	this.op_bets = op_bets;
    }

    public CouponUserListResponse getCoupons() {
	return coupons;
    }

    public void setCoupons(CouponUserListResponse coupons) {
	this.coupons = coupons;
    }

    public OddsJcResponse getJc() {
	return jc;
    }

    public void setJc(OddsJcResponse jc) {
	this.jc = jc;
    }

    public BetJcListResponse getJc_bets() {
	return jc_bets;
    }

    public void setJc_bets(BetJcListResponse jc_bets) {
	this.jc_bets = jc_bets;
    }

    public Integer[] getOp_count() {
	return op_count;
    }

    public void setOp_count(Integer[] op_count) {
	this.op_count = op_count;
    }

    public Integer[] getJc_count() {
	return jc_count;
    }

    public void setJc_count(Integer[] jc_count) {
	this.jc_count = jc_count;
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
    public JsonElement serialize(OddsResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }
}
