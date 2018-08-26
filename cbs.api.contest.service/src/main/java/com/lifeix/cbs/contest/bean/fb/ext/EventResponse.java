package com.lifeix.cbs.contest.bean.fb.ext;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

/**
 * 事件
 * 
 * @author Lifeix
 * 
 */
public class EventResponse implements JsonSerializer<EventResponse>, Response {

    private static final long serialVersionUID = 4833979384971671427L;

    /**
     * 第三方事件id
     */
    private Long target_id;

    /**
     * 时间
     */
    private Integer time;

    /**
     * 事件属于哪个球队
     */
    private Integer team;

    /**
     * 事件种类
     */
    private Integer type;

    /**
     * 事件触发者(进球，吃牌，换人，点球大战罚球)
     */
    private PlayerResponse player;

    /**
     * 进球第一助攻
     */
    private PlayerResponse assist1;

    /**
     * 进球第二助攻
     */
    private PlayerResponse assist2;

    /**
     * 罚点球顺序
     */
    private Integer sequence;

    /**
     * 当前主队得分
     */
    private Integer home_scores;

    /**
     * 当前客队得分
     */
    private Integer away_scores;

    public EventResponse() {
	super();
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(EventResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

    public Long getTarget_id() {
	return target_id;
    }

    public void setTarget_id(Long target_id) {
	this.target_id = target_id;
    }

    public Integer getTime() {
	return time;
    }

    public void setTime(Integer time) {
	this.time = time;
    }

    public Integer getTeam() {
	return team;
    }

    public void setTeam(Integer team) {
	this.team = team;
    }

    public Integer getType() {
	return type;
    }

    public void setType(Integer type) {
	this.type = type;
    }

    public PlayerResponse getPlayer() {
	return player;
    }

    public void setPlayer(PlayerResponse player) {
	this.player = player;
    }

    public PlayerResponse getAssist1() {
	return assist1;
    }

    public void setAssist1(PlayerResponse assist1) {
	this.assist1 = assist1;
    }

    public PlayerResponse getAssist2() {
	return assist2;
    }

    public void setAssist2(PlayerResponse assist2) {
	this.assist2 = assist2;
    }

    public Integer getSequence() {
	return sequence;
    }

    public void setSequence(Integer sequence) {
	this.sequence = sequence;
    }

    public Integer getHome_scores() {
	return home_scores;
    }

    public void setHome_scores(Integer home_scores) {
	this.home_scores = home_scores;
    }

    public Integer getAway_scores() {
	return away_scores;
    }

    public void setAway_scores(Integer away_scores) {
	this.away_scores = away_scores;
    }

}
