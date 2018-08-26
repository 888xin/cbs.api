package com.lifeix.cbs.contest.bean.fb;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

public class CupResponse implements JsonSerializer<CupResponse>, Response {

    /**
     * 
     */
    private static final long serialVersionUID = 8277982243269877789L;

    /**
     * 主键
     */
    private Long cup_id;

    /**
     * 第三方id
     */
    private Long target_id;

    /**
     * 联赛名称
     */
    private String name;

    /**
     * 名称全称
     */
    private String name_full;

    /**
     * 英文名称
     */
    private String name_en;

    /**
     * 颜色值
     */
    private String color;

    /**
     * 所属国家id
     */
    private Integer country_id;

    /**
     * 所属国家
     */
    private String country;

    /**
     * 层级
     */
    private Integer level;

    /**
     * 赛事种类，1联赛2杯赛
     */
    private Integer type;

    public Long getCup_id() {
	return cup_id;
    }

    public void setCup_id(Long cup_id) {
	this.cup_id = cup_id;
    }

    public Long getTarget_id() {
	return target_id;
    }

    public void setTarget_id(Long target_id) {
	this.target_id = target_id;
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

    public String getColor() {
	return color;
    }

    public void setColor(String color) {
	this.color = color;
    }

    public Integer getCountry_id() {
	return country_id;
    }

    public void setCountry_id(Integer country_id) {
	this.country_id = country_id;
    }

    public String getCountry() {
	return country;
    }

    public void setCountry(String country) {
	this.country = country;
    }

    public Integer getLevel() {
	return level;
    }

    public void setLevel(Integer level) {
	this.level = level;
    }

    public Integer getType() {
	return type;
    }

    public void setType(Integer type) {
	this.type = type;
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(CupResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

}
