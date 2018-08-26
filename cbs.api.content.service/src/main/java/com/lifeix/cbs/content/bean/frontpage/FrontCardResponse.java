package com.lifeix.cbs.content.bean.frontpage;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

public class FrontCardResponse implements JsonSerializer<FrontPageContentResponse>, Response {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;
    
    private String card_name;
    private String card_detail;
    private Double price;
    private Integer card_type;
    private Date create_time;
    
    public Long getId() {
        return id;
    }
    public String getCard_name() {
        return card_name;
    }
    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }
    public String getCard_detail() {
        return card_detail;
    }
    public void setCard_detail(String card_detail) {
        this.card_detail = card_detail;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public Integer getCard_type() {
        return card_type;
    }
    public void setCard_type(Integer card_type) {
        this.card_type = card_type;
    }
    public Date getCreate_time() {
        return create_time;
    }
    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Override
    public String getObjectName() {
	// TODO Auto-generated method stub
	return null;
    }
    @Override
    public JsonElement serialize(FrontPageContentResponse src, Type typeOfSrc, JsonSerializationContext context) {
	// TODO Auto-generated method stub
	return null;
    }
    
}
