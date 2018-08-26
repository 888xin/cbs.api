package com.lifeix.cbs.api.bean.coupon;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.cbs.api.bean.gold.GoldLogResponse;
import com.lifeix.user.beans.Response;

public class CouponResponse implements JsonSerializer<GoldLogResponse>, Response {

    private static final long serialVersionUID = 4278960570807446516L;

    private Long id;
    /**
     * 龙筹券类型
     */
    private Integer type;
    /**
     * 有效时间
     */
    private Integer hour;
    /**
     * 命名
     */
    private String name;
    /**
     * 面额
     */
    private Integer price;
    /**
     * 范围key
     */
    private Integer range_key;
    /**
     * 范围value
     */
    private String range_value;
    /**
     * 总数
     * 
     */
    private Integer sum;
    /**
     * 已发数
     */
    private Integer num;
    /**
     * 
     */
    private Double proportion;
    /**
     * 描述
     */
    private String descr;
    /**
     * 使用有效
     */
    private boolean valid;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Integer getType() {
	return type;
    }

    public void setType(Integer type) {
	this.type = type;
    }

    public Integer getHour() {
	return hour;
    }

    public void setHour(Integer hour) {
	this.hour = hour;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Integer getRange_key() {
	return range_key;
    }

    public Integer getPrice() {
	return price;
    }

    public void setPrice(Integer price) {
	this.price = price;
    }

    public void setRange_key(Integer range_key) {
	this.range_key = range_key;
    }

    public String getRange_value() {
	return range_value;
    }

    public void setRange_value(String range_value) {
	this.range_value = range_value;
    }

    public Integer getSum() {
	return sum;
    }

    public void setSum(Integer sum) {
	this.sum = sum;
    }

    public Integer getNum() {
	return num;
    }

    public void setNum(Integer num) {
	this.num = num;
    }

    public Double getProportion() {
	return proportion;
    }

    public void setProportion(Double proportion) {
	this.proportion = proportion;
    }

    public String getDescr() {
	return descr;
    }

    public void setDescr(String descr) {
	this.descr = descr;
    }

    public boolean isValid() {
	return valid;
    }

    public void setValid(boolean valid) {
	this.valid = valid;
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(GoldLogResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return null;
    }
}
