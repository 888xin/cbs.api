package com.lifeix.cbs.contest.bean.fb.ext;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

/**
 * 裁判
 * 
 * @author Lifeix
 * 
 */
public class RefereeResponse implements JsonSerializer<RefereeResponse>, Response {

    private static final long serialVersionUID = 4649595300403033451L;

    /**
     * 名字
     */
    private String name;

    /**
     * 国籍
     */
    private String country;

    public RefereeResponse() {
	super();
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(RefereeResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getCountry() {
	return country;
    }

    public void setCountry(String country) {
	this.country = country;
    }

}
