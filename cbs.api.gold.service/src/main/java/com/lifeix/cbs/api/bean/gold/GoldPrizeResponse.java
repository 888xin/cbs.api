package com.lifeix.cbs.api.bean.gold;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

public class GoldPrizeResponse implements JsonSerializer<GoldPrizeResponse>, Response {

    private static final long serialVersionUID = 1919111141166356098L;

    public GoldPrizeResponse() {
    }

    public GoldPrizeResponse(Integer denomination, Integer num) {
	super();
	this.denomination = denomination;
	this.num = num;
    }

    /**
     * 优惠券面额
     */
    private Integer denomination;

    /**
     * 数量
     */
    private Integer num;

    public Integer getDenomination() {
	return denomination;
    }

    public void setDenomination(Integer denomination) {
	this.denomination = denomination;
    }

    public Integer getNum() {
	return num;
    }

    public void setNum(Integer num) {
	this.num = num;
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(GoldPrizeResponse src, Type typeOfSrc, JsonSerializationContext context) {

	return context.serialize(src);
    }

}
