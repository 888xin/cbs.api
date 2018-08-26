package com.lifeix.cbs.api.dto.gold;

import java.io.Serializable;
import java.util.Date;

/**
 * 龙币筹码日志
 * 
 * @author jacky
 *
 */
public class GoldLog implements Serializable {

    private static final long serialVersionUID = 4979625026429497135L;

    private Long logId;

    private Long userId;

    /**
     * 涉及金额
     */
    private double money;

    /**
     * 描述
     */
    private String content;

    /**
     * 类型
     */
    private int type;

    /**
     * ip地址
     */
    private String ipaddress;

    /**
     * 相关链接
     */
    private String link;

    /**
     * 日志时间
     */
    private Date logTime;

    public GoldLog() {
	super();
    }

    public GoldLog(Long logId, Long userId, double money, String content, int type, String ipaddress, String link,
	    Date logTime) {
	super();
	this.logId = logId;
	this.userId = userId;
	this.money = money;
	this.content = content;
	this.type = type;
	this.ipaddress = ipaddress;
	this.link = link;
	this.logTime = logTime;
    }

    public Long getLogId() {
	return logId;
    }

    public void setLogId(Long logId) {
	this.logId = logId;
    }

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public double getMoney() {
	return money;
    }

    public void setMoney(double money) {
	this.money = money;
    }

    public String getContent() {
	return content;
    }

    public void setContent(String content) {
	this.content = content;
    }

    public int getType() {
	return type;
    }

    public void setType(int type) {
	this.type = type;
    }

    public String getIpaddress() {
	return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
	this.ipaddress = ipaddress;
    }

    public String getLink() {
	return link;
    }

    public void setLink(String link) {
	this.link = link;
    }

    public Date getLogTime() {
	return logTime;
    }

    public void setLogTime(Date logTime) {
	this.logTime = logTime;
    }

    @Override
    public String toString() {
	return "GoldLog [logId=" + logId + ", userId=" + userId + ", money=" + money + ", content=" + content + ", type="
	        + type + ", ipaddress=" + ipaddress + ", link=" + link + ", logTime=" + logTime + "]";
    }

}
