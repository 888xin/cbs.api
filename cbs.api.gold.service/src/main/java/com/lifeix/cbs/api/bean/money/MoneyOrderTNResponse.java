package com.lifeix.cbs.api.bean.money;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

/**
 * payment order vo object
 * 
 * @author peter-dell
 * 
 */
public class MoneyOrderTNResponse implements JsonSerializer<MoneyOrderTNResponse>, Response {

    private static final long serialVersionUID = 7636181301924597873L;

    /**
     * 充值id
     */
    private Long order_id;

    private String tn;

    public Long getOrder_id() {
	return order_id;
    }

    public void setOrder_id(Long order_id) {
	this.order_id = order_id;
    }

    public String getTn() {
	return tn;
    }

    public void setTn(String tn) {
	this.tn = tn;
    }

    @Override
    public JsonElement serialize(MoneyOrderTNResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

    @Override
    public String getObjectName() {
	return null;
    }
}
