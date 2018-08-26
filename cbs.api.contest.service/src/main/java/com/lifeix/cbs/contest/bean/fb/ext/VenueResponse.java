package com.lifeix.cbs.contest.bean.fb.ext;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

/**
 * 比赛场地
 * 
 * @author Lifeix
 * 
 */
public class VenueResponse implements JsonSerializer<VenueResponse>, Response {

    private static final long serialVersionUID = 7509684753142965289L;

    /**
     * 城市
     */
    private String city;

    /**
     * 国家
     */
    private String country;

    /**
     * 体育馆名称
     */
    private String stadium;

    public VenueResponse() {
	super();
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(VenueResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

    public String getCity() {
	return city;
    }

    public void setCity(String city) {
	this.city = city;
    }

    public String getCountry() {
	return country;
    }

    public void setCountry(String country) {
	this.country = country;
    }

    public String getStadium() {
	return stadium;
    }

    public void setStadium(String stadium) {
	this.stadium = stadium;
    }

}
