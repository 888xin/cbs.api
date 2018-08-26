package com.lifeix.cbs.api.bean.money;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

public class MoneyMissedResponse implements JsonSerializer<MoneyMissedResponse>, Response {

    private static final long serialVersionUID = -9005681495514071083L;

    /**
     * id
     */
    private Long id;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 操作类型
     */
    private Integer money_type;

    /**
     * 操作的数据
     */
    private String money_data;

    /**
     * 金额
     */
    private Double amount;

    /**
     * 充值描述
     */
    private String detail;

    /**
     * 状态 0 未处理 1 已处理 2 放弃处理
     */
    private Integer status;

    /**
     * 日期
     */
    private String create_time;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public Integer getMoney_type() {
	return money_type;
    }

    public void setMoney_type(Integer money_type) {
	this.money_type = money_type;
    }

    public String getMoney_data() {
	return money_data;
    }

    public void setMoney_data(String money_data) {
	this.money_data = money_data;
    }

    public Double getAmount() {
	return amount;
    }

    public void setAmount(Double amount) {
	this.amount = amount;
    }

    public String getDetail() {
	return detail;
    }

    public void setDetail(String detail) {
	this.detail = detail;
    }

    public Integer getStatus() {
	return status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    public String getCreate_time() {
	return create_time;
    }

    public void setCreate_time(String create_time) {
	this.create_time = create_time;
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(MoneyMissedResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

}
