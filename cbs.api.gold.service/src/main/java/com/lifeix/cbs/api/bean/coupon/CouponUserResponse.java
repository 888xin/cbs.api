package com.lifeix.cbs.api.bean.coupon;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

public class CouponUserResponse implements JsonSerializer<CouponUserResponse>, Response {

    private static final long serialVersionUID = -592949953181793024L;

    private Long id;

    private Long user_id;

    private Long coupon_id;

    /**
     * 龙筹券的使用比例
     */
    private Double proportion;
    /**
     * 是否使用
     */
    private boolean used;
    /**
     * 龙筹券使用范围
     */
    private Integer range_key;
    /**
     * 龙筹券使用值
     */
    private String range_value;
    /**
     * 开始时间
     */
    private String start_time;
    /**
     * 结束时间
     */
    private String end_time;
    /**
     * 面额
     */
    private Integer price;
    /**
     * 更新时间
     */
    private String update_time;
    /**
     * 龙筹券类型
     */
    private Integer type;
    /**
     * 有效时间
     */
    private Integer hour;
    /**
     * 龙筹券名称
     */
    private String name;

    /**
     * 描述
     */
    private String descr;
    /**
     * 是否有效
     */
    private boolean valid;

    /**
     * add by lhx on 16-03-15
     * 活动券的剩余数量
     */
    private Integer remainder ;

    /**
     * add by lhx on 16-03-15
     * 活动券是否领过
     */
    private Boolean has ;

    public Integer getRemainder() {
        return remainder;
    }

    public void setRemainder(Integer remainder) {
        this.remainder = remainder;
    }

    public Boolean getHas() {
        return has;
    }

    public void setHas(Boolean has) {
        this.has = has;
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Long getUser_id() {
	return user_id;
    }

    public void setUser_id(Long user_id) {
	this.user_id = user_id;
    }

    public Long getCoupon_id() {
	return coupon_id;
    }

    public void setCoupon_id(Long coupon_id) {
	this.coupon_id = coupon_id;
    }

    public Double getProportion() {
	return proportion;
    }

    public void setProportion(Double proportion) {
	this.proportion = proportion;
    }

    public boolean isUsed() {
	return used;
    }

    public void setUsed(boolean used) {
	this.used = used;
    }

    public Integer getRange_key() {
	return range_key;
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

    public String getStart_time() {
	return start_time;
    }

    public void setStart_time(String start_time) {
	this.start_time = start_time;
    }

    public String getEnd_time() {
	return end_time;
    }

    public void setEnd_time(String end_time) {
	this.end_time = end_time;
    }

    public Integer getPrice() {
	return price;
    }

    public void setPrice(Integer price) {
	this.price = price;
    }

    public String getUpdate_time() {
	return update_time;
    }

    public void setUpdate_time(String update_time) {
	this.update_time = update_time;
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
    public JsonElement serialize(CouponUserResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

}
