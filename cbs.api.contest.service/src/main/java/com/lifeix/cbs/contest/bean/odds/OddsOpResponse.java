package com.lifeix.cbs.contest.bean.odds;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.cbs.contest.bean.fb.ContestResponse;
import com.lifeix.user.beans.Response;

/**
 * 胜平负
 * 
 * @author lifeix-sz
 * 
 */
public class OddsOpResponse implements JsonSerializer<OddsOpResponse>, Response {

    private static final long serialVersionUID = -7306150593694741395L;

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
     * 主胜赔率
     */
    private Double home_roi;

    /**
     * 平局赔率
     */
    private Double draw_roi;

    /**
     * 客胜赔率
     */
    private Double away_roi;

    /**
     * 初始主胜赔率
     */
    private Double init_home_roi;

    /**
     * 初始平局赔率
     */
    private Double init_draw_roi;

    /**
     * 初始客胜赔率
     */
    private Double init_away_roi;

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

    public ContestResponse getContest() {
	return contest;
    }

    public void setContest(ContestResponse contest) {
	this.contest = contest;
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

    public Double getInit_home_roi() {
	return init_home_roi;
    }

    public void setInit_home_roi(Double init_home_roi) {
	this.init_home_roi = init_home_roi;
    }

    public Double getInit_draw_roi() {
	return init_draw_roi;
    }

    public void setInit_draw_roi(Double init_draw_roi) {
	this.init_draw_roi = init_draw_roi;
    }

    public Double getInit_away_roi() {
	return init_away_roi;
    }

    public void setInit_away_roi(Double init_away_roi) {
	this.init_away_roi = init_away_roi;
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

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(OddsOpResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

}
