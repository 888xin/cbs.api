package com.lifeix.cbs.contest.bean.bb.ext;

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
public class BbScoreResponse implements JsonSerializer<BbScoreResponse>, Response {

    private static final long serialVersionUID = -5515794000597807365L;

    /**
     * 节次种类
     */
    private Integer period_type;

    /**
     * 节次数
     */
    private Integer period_number;

    /**
     * 得分
     */
    private Integer score;

    public BbScoreResponse() {
	super();
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(BbScoreResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

    public Integer getPeriod_type() {
	return period_type;
    }

    public void setPeriod_type(Integer period_type) {
	this.period_type = period_type;
    }

    public Integer getPeriod_number() {
	return period_number;
    }

    public void setPeriod_number(Integer period_number) {
	this.period_number = period_number;
    }

    public Integer getScore() {
	return score;
    }

    public void setScore(Integer score) {
	this.score = score;
    }

}
