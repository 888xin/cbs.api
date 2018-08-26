package com.lifeix.cbs.contest.bean.fb.ext;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

/**
 * 比分
 * 
 * @author Lifeix
 * 
 */
public class ScoreResponse implements JsonSerializer<ScoreResponse>, Response {

    private static final long serialVersionUID = 955970452934855633L;

    /**
     * 比分种类
     */
    private Integer type;

    /**
     * 主队比分
     */
    private Integer home_score;

    /**
     * 客队比分
     */
    private Integer away_score;

    public ScoreResponse() {
	super();
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(ScoreResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

    public Integer getType() {
	return type;
    }

    public void setType(Integer type) {
	this.type = type;
    }

    public Integer getHome_score() {
	return home_score;
    }

    public void setHome_score(Integer home_score) {
	this.home_score = home_score;
    }

    public Integer getAway_score() {
	return away_score;
    }

    public void setAway_score(Integer away_score) {
	this.away_score = away_score;
    }

}
