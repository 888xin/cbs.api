package com.lifeix.cbs.api.bean.money;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

/**
 * 龙币订单微信参数
 * 
 * @author lifeix
 * 
 */
public class MoneyOrderWeixinResponse implements JsonSerializer<MoneyOrderWeixinResponse>, Response {

    private static final long serialVersionUID = 2138512304877256015L;

    private String appId;

    /**
     * 预支付交易会话标识，微信支付返回
     */
    private String prepayid;

    /**
     * 随机字符串
     */
    private String noncestr;

    /**
     * 扩展字段
     */
    private String packagestr;

    /**
     * 时间戳
     */
    private String timestamp;

    /**
     * 微信签名，微信支付返回
     */
    private String sign;

    public String getAppId() {
	return appId;
    }

    public void setAppId(String appId) {
	this.appId = appId;
    }

    public String getPrepayid() {
	return prepayid;
    }

    public void setPrepayid(String prepayid) {
	this.prepayid = prepayid;
    }

    public String getNoncestr() {
	return noncestr;
    }

    public void setNoncestr(String noncestr) {
	this.noncestr = noncestr;
    }

    public String getPackagestr() {
	return packagestr;
    }

    public void setPackagestr(String packagestr) {
	this.packagestr = packagestr;
    }

    public String getTimestamp() {
	return timestamp;
    }

    public void setTimestamp(String timestamp) {
	this.timestamp = timestamp;
    }

    public String getSign() {
	return sign;
    }

    public void setSign(String sign) {
	this.sign = sign;
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(MoneyOrderWeixinResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

}
