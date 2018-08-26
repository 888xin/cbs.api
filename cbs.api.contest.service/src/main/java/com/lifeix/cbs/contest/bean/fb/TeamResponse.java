package com.lifeix.cbs.contest.bean.fb;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

/**
 * 球队信息
 * 
 * @author peter
 * 
 */
public class TeamResponse implements JsonSerializer<TeamResponse>, Response {

    private static final long serialVersionUID = -4151342360630162099L;

    /**
     * 主键
     */
    private Long t_id;

    /**
     * 第三方id
     */
    private Long target_id;

    /**
     * 联赛id
     */
    private Long cup_id;

    /**
     * 名称
     */
    private String name;

    /**
     * 中文全称
     */
    private String name_full;

    /**
     * 英文名称
     */
    private String name_en;

    /**
     * 图标
     */
    private String logo;

    /**
     * 球队资料
     */
    private String data;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 所属国家id
     */
    private Integer country_id;

    public TeamResponse() {
	super();
    }

    public Long getT_id() {
	return t_id;
    }

    public void setT_id(Long t_id) {
	this.t_id = t_id;
    }

    public Long getTarget_id() {
	return target_id;
    }

    public void setTarget_id(Long target_id) {
	this.target_id = target_id;
    }

    public Long getCup_id() {
	return cup_id;
    }

    public void setCup_id(Long cup_id) {
	this.cup_id = cup_id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getName_full() {
	return name_full;
    }

    public void setName_full(String name_full) {
	this.name_full = name_full;
    }

    public String getName_en() {
	return name_en;
    }

    public void setName_en(String name_en) {
	this.name_en = name_en;
    }

    public String getLogo() {
	return logo;
    }

    public void setLogo(String logo) {
	this.logo = logo;
    }

    public String getData() {
	return data;
    }

    public void setData(String data) {
	this.data = data;
    }

    public Integer getStatus() {
	return status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    public Integer getCountry_id() {
	return country_id;
    }

    public void setCountry_id(Integer country_id) {
	this.country_id = country_id;
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(TeamResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

}
