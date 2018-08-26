package com.lifeix.cbs.contest.bean.odds;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.cbs.contest.bean.fb.ContestResponse;
import com.lifeix.user.beans.Response;

/**
 * 大小球
 */
public class OddsSizeResponse implements JsonSerializer<OddsSizeResponse>, Response {

    private static final long serialVersionUID = 8797426757878880062L;

    /**
     * 主键
     */
    private Long odds_id;

    /**
     * 赛事ID
     */
    private Long contest_id;

    /**
     * 赛事信息
     */
    private ContestResponse contest;

    /**
     * 盘口
     */
    private Double handicap;

    /**
     * 大球赔率
     */
    private Double big_roi;

    /**
     * 小球赔率
     */
    private Double tiny_roi;

    /**
     * 初始盘口
     */
    private Double init_handicap;

    /**
     * 初始大球赔率
     */
    private Double init_big_roi;

    /**
     * 初始小球赔率
     */
    private Double init_tiny_roi;

    /**
     * 公司Id
     */
    private Integer company;

    /**
     * 对应比赛是否结束
     */
    private Boolean close_flag;

    /**
     * 赔率锁定
     */
    private Boolean lock_flag;

    /**
     * 是否属于重要联赛
     */
    private Boolean belong_five;

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(OddsSizeResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

    public Long getOdds_id() {
	return odds_id;
    }

    public void setOdds_id(Long odds_id) {
	this.odds_id = odds_id;
    }

    public Long getContest_id() {
	return contest_id;
    }

    public void setContest_id(Long contest_id) {
	this.contest_id = contest_id;
    }

    public ContestResponse getContest() {
	return contest;
    }

    public void setContest(ContestResponse contest) {
	this.contest = contest;
    }

    public Double getHandicap() {
	return handicap;
    }

    public void setHandicap(Double handicap) {
	this.handicap = handicap;
    }

    public Double getBig_roi() {
	return big_roi;
    }

    public void setBig_roi(Double big_roi) {
	this.big_roi = big_roi;
    }

    public Double getTiny_roi() {
	return tiny_roi;
    }

    public void setTiny_roi(Double tiny_roi) {
	this.tiny_roi = tiny_roi;
    }

    public Double getInit_handicap() {
	return init_handicap;
    }

    public void setInit_handicap(Double init_handicap) {
	this.init_handicap = init_handicap;
    }

    public Double getInit_big_roi() {
	return init_big_roi;
    }

    public void setInit_big_roi(Double init_big_roi) {
	this.init_big_roi = init_big_roi;
    }

    public Double getInit_tiny_roi() {
	return init_tiny_roi;
    }

    public void setInit_tiny_roi(Double init_tiny_roi) {
	this.init_tiny_roi = init_tiny_roi;
    }

    public Integer getCompany() {
	return company;
    }

    public void setCompany(Integer company) {
	this.company = company;
    }

    public Boolean getClose_flag() {
	return close_flag;
    }

    public void setClose_flag(Boolean close_flag) {
	this.close_flag = close_flag;
    }

    public Boolean getLock_flag() {
	return lock_flag;
    }

    public void setLock_flag(Boolean lock_flag) {
	this.lock_flag = lock_flag;
    }

    public Boolean getBelong_five() {
	return belong_five;
    }

    public void setBelong_five(Boolean belong_five) {
	this.belong_five = belong_five;
    }
}
