package com.lifeix.cbs.api.dto.coupon;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户的龙筹券
 * 
 * @author lifeix
 * 
 */
public class CouponUser implements Serializable {

    private static final long serialVersionUID = -7611787422142497486L;

    private Long id;

    private Long userId;

    private Long couponId;
    /**
     * 龙筹券的使用比例
     */
    private Double proportion;
    /**
     * 是否使用
     */
    private boolean used;

    private Date startTime;

    private Date endTime;
    /**
     * 面额
     */
    private Integer price;

    private Date updateTime;

    /**
     * 龙筹券范围
     */
    private Integer rangeKey;

    /**
     * 是否发送消息
     */
    private boolean notifyFlag;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Long getUserId() {
	return userId;
    }

    public Double getProportion() {
	return proportion;
    }

    public void setProportion(Double proportion) {
	this.proportion = proportion;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public Long getCouponId() {
	return couponId;
    }

    public void setCouponId(Long couponId) {
	this.couponId = couponId;
    }

    public boolean isUsed() {
	return used;
    }

    public void setUsed(boolean used) {
	this.used = used;
    }

    public Date getStartTime() {
	return startTime;
    }

    public void setStartTime(Date startTime) {
	this.startTime = startTime;
    }

    public Date getEndTime() {
	return endTime;
    }

    public void setEndTime(Date endTime) {
	this.endTime = endTime;
    }

    public Date getUpdateTime() {
	return updateTime;
    }

    public Integer getPrice() {
	return price;
    }

    public void setPrice(Integer price) {
	this.price = price;
    }

    public void setUpdateTime(Date updateTime) {
	this.updateTime = updateTime;
    }

    public Integer getRangeKey() {
	return rangeKey;
    }

    public void setRangeKey(Integer rangeKey) {
	this.rangeKey = rangeKey;
    }

    public boolean isNotifyFlag() {
	return notifyFlag;
    }

    public void setNotifyFlag(boolean notifyFlag) {
	this.notifyFlag = notifyFlag;
    }

}
