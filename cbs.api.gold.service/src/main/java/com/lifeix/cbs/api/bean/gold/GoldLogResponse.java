package com.lifeix.cbs.api.bean.gold;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.user.beans.Response;

public class GoldLogResponse implements JsonSerializer<GoldLogResponse>, Response {

    private static final long serialVersionUID = 1919111141166356098L;

    private Long log_id;

    private Long user_id;

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
    private String ip_address;

    /**
     * 相关链接
     */
    private String link;

    /**
     * 日志时间
     */
    private String log_time;

    /**
     * 日志用户
     */
    private CbsUserResponse user;

    public Long getLog_id() {
	return log_id;
    }

    public void setLog_id(Long log_id) {
	this.log_id = log_id;
    }

    public Long getUser_id() {
	return user_id;
    }

    public void setUser_id(Long user_id) {
	this.user_id = user_id;
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

    public String getIp_address() {
	return ip_address;
    }

    public void setIp_address(String ip_address) {
	this.ip_address = ip_address;
    }

    public String getLink() {
	return link;
    }

    public void setLink(String link) {
	this.link = link;
    }

    public String getLog_time() {
	return log_time;
    }

    public void setLog_time(String log_time) {
	this.log_time = log_time;
    }

    public CbsUserResponse getUser() {
	return user;
    }

    public void setUser(CbsUserResponse user) {
	this.user = user;
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(GoldLogResponse src, Type typeOfSrc, JsonSerializationContext context) {

	return context.serialize(src);
    }

}
