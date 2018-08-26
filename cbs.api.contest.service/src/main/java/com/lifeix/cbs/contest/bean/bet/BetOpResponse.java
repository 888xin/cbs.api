package com.lifeix.cbs.contest.bean.bet;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

/**
 * 胜平负下单
 * 
 * @author lifeix-sz
 * 
 */
public class BetOpResponse implements JsonSerializer<BetOpResponse>, Response {

    private static final long serialVersionUID = -3355809768897761309L;

    /**
     * 主键
     */
    private Long b_id;

    /**
     * 用户id
     */
    private Long user_id;

    /**
     * 比赛id
     */
    private Long contest_id;

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

    public Long getContest_id() {
	return contest_id;
    }

    public void setContest_id(Long contest_id) {
	this.contest_id = contest_id;
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
    public JsonElement serialize(BetOpResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }
}
