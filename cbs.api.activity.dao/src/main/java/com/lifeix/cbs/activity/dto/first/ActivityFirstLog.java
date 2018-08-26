package com.lifeix.cbs.activity.dto.first;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户首充记录
 * 
 * @author lifeix
 * 
 */
public class ActivityFirstLog implements Serializable {

    private static final long serialVersionUID = -3232652225418471504L;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 金额
     */
    private Double amount;

    /**
     * 规则
     */
    private String rule;

    /**
     * 赠送金额
     */
    private Integer reward;

    /**
     * 日期
     */
    private Date createTime;

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public Double getAmount() {
	return amount;
    }

    public void setAmount(Double amount) {
	this.amount = amount;
    }

    public String getRule() {
	return rule;
    }

    public void setRule(String rule) {
	this.rule = rule;
    }

    public Integer getReward() {
	return reward;
    }

    public void setReward(Integer reward) {
	this.reward = reward;
    }

    public Date getCreateTime() {
	return createTime;
    }

    public void setCreateTime(Date createTime) {
	this.createTime = createTime;
    }
}