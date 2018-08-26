package com.lifeix.cbs.contest.dto.bb;

import java.io.Serializable;
import java.util.Date;

public class BbContestRecord implements Serializable {

    private static final long serialVersionUID = -3976370996642930298L;

    /**
     * 第三方赛事ID
     */
    private Long targetId;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 主队
     */
    private Long homeTeam;

    /**
     * 客队
     */
    private Long awayTeam;

    /**
     * 主队得分
     */
    private Integer homeScores;

    /**
     * 客队得分
     */
    private Integer awayScores;

    /**
     * 联赛或杯赛ID
     */
    private Long cupId;

    /**
     * 联赛或杯赛名称
     */
    private String cupName;

    /**
     * 胜负种类
     */
    private Integer winType;

    /**
     * 竞彩赢盘种类
     */
    private Integer jcWinType;

    public BbContestRecord() {
	super();
    }

    public Long getTargetId() {
	return targetId;
    }

    public void setTargetId(Long targetId) {
	this.targetId = targetId;
    }

    public Date getStartTime() {
	return startTime;
    }

    public void setStartTime(Date startTime) {
	this.startTime = startTime;
    }

    public Long getHomeTeam() {
	return homeTeam;
    }

    public void setHomeTeam(Long homeTeam) {
	this.homeTeam = homeTeam;
    }

    public Long getAwayTeam() {
	return awayTeam;
    }

    public void setAwayTeam(Long awayTeam) {
	this.awayTeam = awayTeam;
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

    public Long getCupId() {
	return cupId;
    }

    public void setCupId(Long cupId) {
	this.cupId = cupId;
    }

    public String getCupName() {
	return cupName;
    }

    public void setCupName(String cupName) {
	this.cupName = cupName;
    }

    public Integer getWinType() {
	return winType;
    }

    public void setWinType(Integer winType) {
	this.winType = winType;
    }

    public Integer getJcWinType() {
	return jcWinType;
    }

    public void setJcWinType(Integer jcWinType) {
	this.jcWinType = jcWinType;
    }

}
