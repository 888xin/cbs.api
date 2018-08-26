package com.lifeix.cbs.contest.bean.bet;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.cbs.contest.bean.fb.ContestResponse;
import com.lifeix.user.beans.Response;

import java.lang.reflect.Type;

/**
 * 胜平负下单
 * 
 * @author lifeix-sz
 * 
 */
public class BetLogResponse implements JsonSerializer<BetLogResponse>, Response {

    private static final long serialVersionUID = 5382070628749758992L;

    /**
     * 主键
     */
    private Long b_id;

    /**
     * 用户id
     */
    private Long user_id;

    /**
     * 用户名
     */
    private String user_name;

    /**
     * 用户龙号
     */
    private Long long_no;

    /**
     * 赛事信息
     */
    private ContestResponse contest;

    /**
     * 支持方
     */
    private Integer support;

    /**
     * 主胜赔率
     */
    private Double home_roi;

    /**
     * 平赔率
     */
    private Double draw_roi;

    /**
     * 客胜赔率
     */
    private Double away_roi;

    /**
     * 盘口
     */
    private Double handicap;

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
     * 提供赔率数据的公司
     */
    private Integer company;

    /**
     * 下单时间
     */
    private String create_time;

    /**
     * 是否龙币下单
     */
    private boolean longbi;

    /**
     * 龙筹券
     */
    private Integer coupon;

    public Integer getCoupon() {
        return coupon;
    }

    public void setCoupon(Integer coupon) {
        this.coupon = coupon;
    }

    public boolean isLongbi() {
        return longbi;
    }

    public void setLongbi(boolean longbi) {
        this.longbi = longbi;
    }

    public Double getHandicap() {
        return handicap;
    }

    public void setHandicap(Double handicap) {
        this.handicap = handicap;
    }

    public Long getB_id() {
        return b_id;
    }

    public void setB_id(Long b_id) {
        this.b_id = b_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public Long getLong_no() {
        return long_no;
    }

    public void setLong_no(Long long_no) {
        this.long_no = long_no;
    }

    public ContestResponse getContest() {
        return contest;
    }

    public void setContest(ContestResponse contest) {
        this.contest = contest;
    }

    public Integer getSupport() {
        return support;
    }

    public void setSupport(Integer support) {
        this.support = support;
    }

    public Double getHome_roi() {
        return home_roi;
    }

    public void setHome_roi(Double home_roi) {
        this.home_roi = home_roi;
    }

    public Double getDraw_roi() {
        return draw_roi;
    }

    public void setDraw_roi(Double draw_roi) {
        this.draw_roi = draw_roi;
    }

    public Double getAway_roi() {
        return away_roi;
    }

    public void setAway_roi(Double away_roi) {
        this.away_roi = away_roi;
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

    public Integer getCompany() {
        return company;
    }

    public void setCompany(Integer company) {
        this.company = company;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(BetLogResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }
}
