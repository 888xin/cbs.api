package com.lifeix.cbs.contest.bean.yy;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.cbs.api.bean.user.CbsUserWxResponse;
import com.lifeix.user.beans.Response;

/**
 * 押押：押押下单信息
 * 
 * @author peter
 * 
 */
public class YyBetResponse implements JsonSerializer<YyBetResponse>, Response {

    private static final long serialVersionUID = 8068561630690709449L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 比赛id
     */
    private Long contest_id;

    /**
     * 用户id
     */
    private Long user_id;

    /**
     * 支持方
     */
    private Integer support;

    /**
     * 赔率
     */
    private Double yy_roi;

    /**
     * 下单金额
     */
    private Double bet;

    /**
     * 返还金额（包含本金）
     */
    private Double back;

    /**
     * 下单结果（初始|赢|输|走）
     */
    private Integer status;

    /**
     * 下单时间
     */
    private String create_time;

    /**
     * 内容id
     */
    private Long content_id;

    /**
     * ip地址
     */
    private String ipaddress;

    /**
     * 龙币标识
     */
    private boolean is_longbi;

    /**
     * 龙筹数量
     */
    private Integer coupon;

    /**
     * 押押赛事信息
     */
    private YyContestResponse contest;

    /**
     * 用户信息
     */
    private CbsUserResponse user;

    private CbsUserWxResponse userWx;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Long getContest_id() {
	return contest_id;
    }

    public void setContest_id(Long contest_id) {
	this.contest_id = contest_id;
    }

    public Long getUser_id() {
	return user_id;
    }

    public void setUser_id(Long user_id) {
	this.user_id = user_id;
    }

    public Integer getSupport() {
	return support;
    }

    public void setSupport(Integer support) {
	this.support = support;
    }

    public Double getYy_roi() {
	return yy_roi;
    }

    public void setYy_roi(Double yy_roi) {
	this.yy_roi = yy_roi;
    }

    public Double getBet() {
	return bet;
    }

    public void setBet(Double bet) {
	this.bet = bet;
    }

    public Double getBack() {
	return back;
    }

    public void setBack(Double back) {
	this.back = back;
    }

    public Integer getStatus() {
	return status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    public String getCreate_time() {
	return create_time;
    }

    public void setCreate_time(String create_time) {
	this.create_time = create_time;
    }

    public Long getContent_id() {
	return content_id;
    }

    public void setContent_id(Long content_id) {
	this.content_id = content_id;
    }

    public String getIpaddress() {
	return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
	this.ipaddress = ipaddress;
    }

    public boolean isIs_longbi() {
	return is_longbi;
    }

    public void setIs_longbi(boolean is_longbi) {
	this.is_longbi = is_longbi;
    }

    public Integer getCoupon() {
	return coupon;
    }

    public void setCoupon(Integer coupon) {
	this.coupon = coupon;
    }

    public YyContestResponse getContest() {
	return contest;
    }

    public void setContest(YyContestResponse contest) {
	this.contest = contest;
    }

    public CbsUserResponse getUser() {
	return user;
    }

    public void setUser(CbsUserResponse user) {
	this.user = user;
    }

    public CbsUserWxResponse getUserWx() {
	return userWx;
    }

    public void setUserWx(CbsUserWxResponse userWx) {
	this.userWx = userWx;
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(YyBetResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

}
