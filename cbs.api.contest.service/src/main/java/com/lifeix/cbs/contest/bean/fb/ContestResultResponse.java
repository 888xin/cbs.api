package com.lifeix.cbs.contest.bean.fb;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

public class ContestResultResponse implements JsonSerializer<ContestResultResponse>, Response {

    private static final long serialVersionUID = -5643280276500435516L;

    /**
     * 主队比分
     */
    private int home_scores;

    /**
     * 客队比分
     */
    private int away_scores;

    /**
     * 比赛状态
     */
    private int status;

    private Integer score_type;

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(ContestResultResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

    public int getHome_scores() {
	return home_scores;
    }

    public void setHome_scores(int home_scores) {
	this.home_scores = home_scores;
    }

    public int getAway_scores() {
	return away_scores;
    }

    public void setAway_scores(int away_scores) {
	this.away_scores = away_scores;
    }

    public int getStatus() {
	return status;
    }

    public void setStatus(int status) {
	this.status = status;
    }

    public Integer getScore_type() {
	return score_type;
    }

    public void setScore_type(Integer score_type) {
	this.score_type = score_type;
    }

}
