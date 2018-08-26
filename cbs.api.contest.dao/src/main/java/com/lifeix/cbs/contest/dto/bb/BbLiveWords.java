package com.lifeix.cbs.contest.dto.bb;

import java.io.Serializable;

public class BbLiveWords implements Serializable {

    private static final long serialVersionUID = 3039418380272455023L;

    /**
     * 主键
     */
    private Long phraseId;

    /**
     * 对应赛事ID
     */
    private Long contestId;

    /**
     * 文字内容
     */
    private String textContent;

    /**
     * 事件发生时主队得分
     */
    private Integer homeScores;

    /**
     * 事件发生时客队得分
     */
    private Integer awayScores;

    /**
     * 事件发生的节次种类，分为常规时间和加时
     */
    private Integer periodType;

    /**
     * 事件发生的节次数
     */
    private Integer periodNumber;

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
    private Integer eventType;

    /**
     * 事件是否无效
     */
    private Boolean disabled;

    /**
     * 事件顺序序号
     */
    private Integer sequence;

    public BbLiveWords() {
	super();
    }

    public Long getPhraseId() {
	return phraseId;
    }

    public void setPhraseId(Long phraseId) {
	this.phraseId = phraseId;
    }

    public Long getContestId() {
	return contestId;
    }

    public void setContestId(Long contestId) {
	this.contestId = contestId;
    }

    public String getTextContent() {
	return textContent;
    }

    public void setTextContent(String textContent) {
	this.textContent = textContent;
    }

    public Integer getPeriodType() {
	return periodType;
    }

    public void setPeriodType(Integer periodType) {
	this.periodType = periodType;
    }

    public Integer getPeriodNumber() {
	return periodNumber;
    }

    public void setPeriodNumber(Integer periodNumber) {
	this.periodNumber = periodNumber;
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

    public Integer getEventType() {
	return eventType;
    }

    public void setEventType(Integer eventType) {
	this.eventType = eventType;
    }

    public Integer getHomeScores() {
	return homeScores;
    }

    public void setHomeScores(Integer homeScores) {
	this.homeScores = homeScores;
    }

    public Integer getAwayScores() {
	return awayScores;
    }

    public void setAwayScores(Integer awayScores) {
	this.awayScores = awayScores;
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
