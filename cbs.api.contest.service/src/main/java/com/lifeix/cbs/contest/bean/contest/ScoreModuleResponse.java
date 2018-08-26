package com.lifeix.cbs.contest.bean.contest;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

public class ScoreModuleResponse implements JsonSerializer<ScoreModuleResponse>, Response {

    private static final long serialVersionUID = -6426908778273919106L;

    private Long id;

    private Integer contest_type;

    private Integer module_value;

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(ScoreModuleResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Integer getContest_type() {
	return contest_type;
    }

    public void setContest_type(Integer contest_type) {
	this.contest_type = contest_type;
    }

    public Integer getModule_value() {
	return module_value;
    }

    public void setModule_value(Integer module_value) {
	this.module_value = module_value;
    }

}
