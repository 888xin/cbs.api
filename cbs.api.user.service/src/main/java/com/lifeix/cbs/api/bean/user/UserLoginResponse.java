package com.lifeix.cbs.api.bean.user;

import com.lifeix.cbs.achieve.bean.achieve.AchieveResponse;
import com.lifeix.user.beans.Response;

/**
 * Created by lhx on 16-4-19 上午10:08
 * 
 * @Description
 */
public class UserLoginResponse implements Response {

    private static final long serialVersionUID = -698664140927597686L;

    /**
     * 用户
     */
    private CbsUserResponse user;

    public long user_id;

    /**
     * 本月登陆的天数
     */
    public long day = 1;

    /**
     * 本日是否点击确认
     */
    public boolean receive;

    /**
     * 可以获得筹码的第几天
     */
    public String gold_days;

    /**
     * 第几天对应的筹码
     */
    public String amounts;

    /**
     * 押押结算下注次数
     */
    public Integer yyBetCount;
    /**
     * 篮球结算下注次数
     */
    public Integer bbBetCount;
    /**
     * 足球结算下注次数
     */
    public Integer fbBetCount;

    /**
     * 显示比赛类型
     */
    public Integer displayType;

    /**
     * 首充活动状态
     */
    private Integer actStatus;

    /**
     * 活动状态
     */
    private Boolean actFlag;

    /**
     * 剩余抽奖机会
     */
    private Integer actTimes;

    /**
     * 用户最后一次获取的成就信息
     */
    private AchieveResponse achieve;

    public CbsUserResponse getUser() {
	return user;
    }

    public void setUser(CbsUserResponse user) {
	this.user = user;
    }

    public String getGold_days() {
	return gold_days;
    }

    public void setGold_days(String gold_days) {
	this.gold_days = gold_days;
    }

    public String getAmounts() {
	return amounts;
    }

    public void setAmounts(String amounts) {
	this.amounts = amounts;
    }

    public boolean isReceive() {
	return receive;
    }

    public void setReceive(boolean receive) {
	this.receive = receive;
    }

    public long getDay() {
	return day;
    }

    public void setDay(long day) {
	this.day = day;
    }

    public long getUser_id() {
	return user_id;
    }

    public void setUser_id(long user_id) {
	this.user_id = user_id;
    }

    public String getObjectName() {
	return null;
    }

    public Integer getYyBetCount() {
	return yyBetCount;
    }

    public void setYyBetCount(Integer yyBetCount) {
	this.yyBetCount = yyBetCount;
    }

    public Integer getBbBetCount() {
	return bbBetCount;
    }

    public void setBbBetCount(Integer bbBetCount) {
	this.bbBetCount = bbBetCount;
    }

    public Integer getFbBetCount() {
	return fbBetCount;
    }

    public void setFbBetCount(Integer fbBetCount) {
	this.fbBetCount = fbBetCount;
    }

    public Integer getDisplayType() {
	return displayType;
    }

    public void setDisplayType(Integer displayType) {
	this.displayType = displayType;
    }

    public Integer getActStatus() {
	return actStatus;
    }

    public void setActStatus(Integer actStatus) {
	this.actStatus = actStatus;
    }

    public Boolean getActFlag() {
	return actFlag;
    }

    public void setActFlag(Boolean actFlag) {
	this.actFlag = actFlag;
    }

    public Integer getActTimes() {
	return actTimes;
    }

    public void setActTimes(Integer actTimes) {
	this.actTimes = actTimes;
    }

    public AchieveResponse getAchieve() {
	return achieve;
    }

    public void setAchieve(AchieveResponse achieve) {
	this.achieve = achieve;
    }

}
