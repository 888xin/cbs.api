package com.lifeix.cbs.activity.bean.first;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

/**
 * 首充活動
 * 
 * @author lifeix
 *
 */
public class ActivityFirstResponse implements JsonSerializer<ActivityFirstResponse>, Response {

    private static final long serialVersionUID = -1275970292053272258L;

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
     * 赠送金额备注
     */
    private String rewardRemark;

    /**
     * 日期
     */
    private String createTime;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 活动状态
     */
    private Boolean actFlag;

    /**
     * 剩余抽奖机会
     */
    private Integer times;

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

    public String getRewardRemark() {
	return rewardRemark;
    }

    public void setRewardRemark(String rewardRemark) {
	this.rewardRemark = rewardRemark;
    }

    public String getCreateTime() {
	return createTime;
    }

    public void setCreateTime(String createTime) {
	this.createTime = createTime;
    }

    public Integer getStatus() {
	return status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    public Integer getTimes() {
	return times;
    }

    public void setTimes(Integer times) {
	this.times = times;
    }

    public Boolean getActFlag() {
	return actFlag;
    }

    public void setActFlag(Boolean actFlag) {
	this.actFlag = actFlag;
    }

    @Override
    public JsonElement serialize(ActivityFirstResponse activityFirstResponse, Type type,
	    JsonSerializationContext jsonSerializationContext) {
	return null;
    }

    @Override
    public String getObjectName() {
	return null;
    }
}
