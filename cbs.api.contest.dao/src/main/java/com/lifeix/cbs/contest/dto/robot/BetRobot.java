package com.lifeix.cbs.contest.dto.robot;

import java.io.Serializable;
import java.util.Date;

/**
 * 下单机器人
 * 
 * @author Peter
 * 
 */
public class BetRobot implements Serializable {

    private static final long serialVersionUID = 8669701670480057119L;

    private Long userId;

    /**
     * 用户信息
     */
    private String userInfo;

    /**
     * 足球玩法
     */
    private int fbOdds;

    /**
     * 篮球玩法
     */
    private int bbOdds;

    /**
     * 玩法配置
     */
    private String setting;

    /**
     * 最近下单记录
     */
    private String history;

    /**
     * 可下单时间(小时)
     */
    private String betTime;

    /**
     * 是否参与小游戏
     */
    private boolean gameFlag;

    /**
     * 小游戏时间
     */
    private Date gameTime;

    /**
     * 字段待用
     */
    private boolean pkFlag;

    private Date createTime;

    private Date updateTime;

    private boolean closeFlag;

    /**
     * 唤醒次数
     */
    private int callCount;

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public String getSetting() {
	return setting;
    }

    public void setSetting(String setting) {
	this.setting = setting;
    }

    public String getHistory() {
	return history;
    }

    public void setHistory(String history) {
	this.history = history;
    }

    public Date getCreateTime() {
	return createTime;
    }

    public void setCreateTime(Date createTime) {
	this.createTime = createTime;
    }

    public Date getUpdateTime() {
	return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
	this.updateTime = updateTime;
    }

    public boolean isCloseFlag() {
	return closeFlag;
    }

    public void setCloseFlag(boolean closeFlag) {
	this.closeFlag = closeFlag;
    }

    public boolean isGameFlag() {
	return gameFlag;
    }

    public void setGameFlag(boolean gameFlag) {
	this.gameFlag = gameFlag;
    }

    public boolean isPkFlag() {
	return pkFlag;
    }

    public void setPkFlag(boolean pkFlag) {
	this.pkFlag = pkFlag;
    }

    public int getFbOdds() {
	return fbOdds;
    }

    public void setFbOdds(int fbOdds) {
	this.fbOdds = fbOdds;
    }

    public int getBbOdds() {
	return bbOdds;
    }

    public void setBbOdds(int bbOdds) {
	this.bbOdds = bbOdds;
    }

    public String getUserInfo() {
	return userInfo;
    }

    public void setUserInfo(String userInfo) {
	this.userInfo = userInfo;
    }

    public int getCallCount() {
	return callCount;
    }

    public void setCallCount(int callCount) {
	this.callCount = callCount;
    }

    public String getBetTime() {
	return betTime;
    }

    public void setBetTime(String betTime) {
	this.betTime = betTime;
    }

    public Date getGameTime() {
	return gameTime;
    }

    public void setGameTime(Date gameTime) {
	this.gameTime = gameTime;
    }

}
