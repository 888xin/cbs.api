package com.lifeix.cbs.api.bean.money;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

/**
 * 龙币网页订单
 * 
 * @author lifeix
 * 
 */
public class MoneyOrderWapResponse implements JsonSerializer<MoneyOrderWapResponse>, Response {

    private static final long serialVersionUID = -6538012261111500822L;

    // 表单提交地址
    private String pay_url;

    // 表单提交方法
    private String pay_method;

    // 表单提交参数
    private Map<String, String> pay_parameters;

    // 充值成功返回url
    private String from_url;

    public String getPay_url() {
	return pay_url;
    }

    public void setPay_url(String pay_url) {
	this.pay_url = pay_url;
    }

    public String getPay_method() {
	return pay_method;
    }

    public void setPay_method(String pay_method) {
	this.pay_method = pay_method;
    }

    public Map<String, String> getPay_parameters() {
	return pay_parameters;
    }

    public void setPay_parameters(Map<String, String> pay_parameters) {
	this.pay_parameters = pay_parameters;
    }

    public String getFrom_url() {
	return from_url;
    }

    public void setFrom_url(String from_url) {
	this.from_url = from_url;
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(MoneyOrderWapResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

}
