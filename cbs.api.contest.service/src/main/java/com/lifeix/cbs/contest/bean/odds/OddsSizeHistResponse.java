package com.lifeix.cbs.contest.bean.odds;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

/**
 * 大小球赔率历史记录
 */
public class OddsSizeHistResponse implements JsonSerializer<OddsSizeHistResponse>, Response {

    private static final long serialVersionUID = -42555638029452909L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 赛事ID
     */
    private Long contest_id;

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
     * 时间
     */
    private String create_time;

    /**
     * 公司Id
     */
    private Integer company;

    public OddsSizeHistResponse() {
	super();
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(OddsSizeHistResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
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

}
