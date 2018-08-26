package com.lifeix.cbs.contest.bean.odds;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

/**
 * 欧赔赔率历史记录
 * 
 * @author Peter
 * 
 */
public class OddsOpHistResponse implements JsonSerializer<OddsOpHistResponse>, Response {

    private static final long serialVersionUID = -9218999044836612586L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 赛事ID
     */
    private Long contest_id;

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
     * 时间
     */
    private String create_time;

    /**
     * 公司Id
     */
    private Integer company;

    public OddsOpHistResponse() {
	super();
    }

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

    public String getCreate_time() {
	return create_time;
    }

    public void setCreate_time(String create_time) {
	this.create_time = create_time;
    }

    public Integer getCompany() {
	return company;
    }

    public void setCompany(Integer company) {
	this.company = company;
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(OddsOpHistResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

}
