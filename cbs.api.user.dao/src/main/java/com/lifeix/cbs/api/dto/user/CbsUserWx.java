package com.lifeix.cbs.api.dto.user;

import java.io.Serializable;
import java.util.Date;

/**
 * 猜比赛基础用户信息
 * 
 * 
 */
public class CbsUserWx implements Serializable {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 微信协议id
     */
    private String openId;

    /**
     * 微信appid
     */
    private String appId;

    /**
     * 来源：APP、H5
     */
    private String source;

    /**
     * 创建时间
     */
    private Date createTime;

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public String getOpenId() {
	return openId;
    }

    public void setOpenId(String openId) {
	this.openId = openId;
    }

    public String getAppId() {
	return appId;
    }

    public void setAppId(String appId) {
	this.appId = appId;
    }

    public String getSource() {
	return source;
    }

    public void setSource(String source) {
	this.source = source;
    }

    public Date getCreateTime() {
	return createTime;
    }

    public void setCreateTime(Date createTime) {
	this.createTime = createTime;
    }

}
