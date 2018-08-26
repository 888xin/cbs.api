package com.lifeix.cbs.api.bean.money;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

public class MoneyCardResponse implements JsonSerializer<MoneyCardResponse>, Response {

    private static final long serialVersionUID = 1915305322869578958L;

    /**
     * id
     */
    private long id;

    /**
     * 充值名称
     */
    private String name;

    /**
     * 充值描述
     */
    private String detail;

    /**
     * 价格
     */
    private Double price;

    /**
     * 金额
     */
    private Double amount;

    /**
     * 赠送类型 0 龙筹 1 龙币
     */
    private Integer type;

    /**
     * 赠送金额
     */
    private Double handsel;

    /**
     * 生成时间
     */
    private String create_time;

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDetail() {
	return detail;
    }

    public void setDetail(String detail) {
	this.detail = detail;
    }

    public Double getPrice() {
	return price;
    }

    public void setPrice(Double price) {
	this.price = price;
    }

    public Double getAmount() {
	return amount;
    }

    public void setAmount(Double amount) {
	this.amount = amount;
    }

    public Integer getType() {
	return type;
    }

    public void setType(Integer type) {
	this.type = type;
    }

    public Double getHandsel() {
	return handsel;
    }

    public void setHandsel(Double handsel) {
	this.handsel = handsel;
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
    public JsonElement serialize(MoneyCardResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

}
