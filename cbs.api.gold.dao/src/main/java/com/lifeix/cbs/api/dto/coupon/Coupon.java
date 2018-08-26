package com.lifeix.cbs.api.dto.coupon;

import java.io.Serializable;

public class Coupon implements Serializable {

    private static final long serialVersionUID = -8369053068177351650L;

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
     * 龙筹券名称
     */
    private String name;
    /**
     * 面额
     */
    private Integer price;
    /**
     * 范围key
     */
    private Integer rangeKey;
    /**
     * 范围value
     */
    private String rangeValue;
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
     * 是否有效
     */
    private boolean valid;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
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

    public Integer getType() {
	return type;
    }

    public void setType(Integer type) {
	this.type = type;
    }

    public String getRangeValue() {
	return rangeValue;
    }

    public Integer getRangeKey() {
	return rangeKey;
    }

    public void setRangeKey(Integer rangeKey) {
	this.rangeKey = rangeKey;
    }

    public void setRangeValue(String rangeValue) {
	this.rangeValue = rangeValue;
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

    public Integer getPrice() {
	return price;
    }

    public void setPrice(Integer price) {
	this.price = price;
    }

    public boolean isValid() {
	return valid;
    }

    public void setValid(boolean valid) {
	this.valid = valid;
    }

}
