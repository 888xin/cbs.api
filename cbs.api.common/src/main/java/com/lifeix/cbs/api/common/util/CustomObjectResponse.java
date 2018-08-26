package com.lifeix.cbs.api.common.util;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

public class CustomObjectResponse implements JsonSerializer<CustomObjectResponse>, Response{
    private static final long serialVersionUID = 8879811967804672413L;

	/**
	 * id 
	 */
	private Long id;
	
	/**
	 * 名称
	 */
	private String name;
	
	public CustomObjectResponse() {
		super();
	}
	
	public CustomObjectResponse(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public JsonElement serialize(CustomObjectResponse src, Type typeOfSrc,
			JsonSerializationContext context) {
		return context.serialize(src);
	}

	@Override
	public String getObjectName() {
		return null;
	}

}
