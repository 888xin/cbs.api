package com.lifeix.cbs.contest.bean.bb;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

public class BbLiveWordsResponse implements JsonSerializer<BbLiveWordsResponse>, Response {

    private static final long serialVersionUID = -1742262813588355872L;

    /**
     * 主键
     */
    private Long phrase_id;

    /**
     * 对应赛事ID
     */
    private Long contest_id;

    /**
     * 文字内容
     */
    private String text_content;

    /**
     * 事件发生时主队得分
     */
    private Integer home_scores;

    /**
     * 事件发生时客队得分
     */
    private Integer away_scores;

    /**
     * 事件发生的节次种类，分为常规时间和加时
     */
    private Integer period_type;

    /**
     * 事件发生的节次数
     */
    private Integer period_number;

    /**
     * 事件发生的时钟
     */
    private String clock;

    /**
     * 事件发生的场地坐标
     */
    private String location;

    /**
     * 事件所属球队
     */
    private Integer team;

    /**
     * 事件类型
     */
    private Integer event_type;

    /**
     * 事件是否无效
     */
    private Boolean disabled;

    /**
     * 事件顺序序号
     */
    private Integer sequence;

    public BbLiveWordsResponse() {
	super();
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(BbLiveWordsResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

    public Long getPhrase_id() {
	return phrase_id;
    }

    public void setPhrase_id(Long phrase_id) {
	this.phrase_id = phrase_id;
    }

    public Long getContest_id() {
	return contest_id;
    }

    public void setContest_id(Long contest_id) {
	this.contest_id = contest_id;
    }

    public String getText_content() {
	return text_content;
    }

    public void setText_content(String text_content) {
	this.text_content = text_content;
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

    public String getClock() {
	return clock;
    }

    public void setClock(String clock) {
	this.clock = clock;
    }

    public String getLocation() {
	return location;
    }

    public void setLocation(String location) {
	this.location = location;
    }

    public Integer getTeam() {
	return team;
    }

    public void setTeam(Integer team) {
	this.team = team;
    }

    public Integer getEvent_type() {
	return event_type;
    }

    public void setEvent_type(Integer event_type) {
	this.event_type = event_type;
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

    public Boolean getDisabled() {
	return disabled;
    }

    public void setDisabled(Boolean disabled) {
	this.disabled = disabled;
    }

    public Integer getSequence() {
	return sequence;
    }

    public void setSequence(Integer sequence) {
	this.sequence = sequence;
    }

}
