package com.lifeix.cbs.contest.dto.bb;

import java.io.Serializable;

public class BbTeamStatistics implements Serializable {

    private static final long serialVersionUID = -5255750988888007445L;

    /**
     * 主键
     */
    private Long sId;
    /**
     * 赛事ID
     */
    private Long contestId;
    /**
     * 队伍ID
     */
    private Long teamId;
    /**
     * 主客标志，1主队2客队
     */
    private Integer team;
    /**
     * 各节次得分
     */
    private String scores;
    /**
     * 三分球命中数
     */
    private Integer threePointMade;
    /**
     * 三分球投篮数
     */
    private Integer threePointAtt;
    /**
     * 两分球命中数
     */
    private Integer twoPointMade;
    /**
     * 两分球投篮数
     */
    private Integer twoPointAtt;
    /**
     * 罚球命中数
     */
    private Integer freeThrowMade;
    /**
     * 罚球投篮数
     */
    private Integer freeThrowAtt;
    /**
     * 前场篮板
     */
    private Integer offensiveRebounds;
    /**
     * 后场篮板
     */
    private Integer defensiveRebounds;
    /**
     * 助攻
     */
    private Integer assists;
    /**
     * 失误
     */
    private Integer turnovers;
    /**
     * 抢断
     */
    private Integer steals;
    /**
     * 盖帽
     */
    private Integer blocks;
    /**
     * 个人犯规
     */
    private Integer personalFouls;
    /**
     * 技术犯规
     */
    private Integer techFouls;

    public BbTeamStatistics() {
	super();
    }

    public BbTeamStatistics(Long sId, Long contestId, Long teamId, Integer team, String scores, Integer threePointMade,
	    Integer threePointAtt, Integer twoPointMade, Integer twoPointAtt, Integer freeThrowMade, Integer freeThrowAtt,
	    Integer offensiveRebounds, Integer defensiveRebounds, Integer assists, Integer turnovers, Integer steals,
	    Integer blocks, Integer personalFouls, Integer techFouls) {
	this();
	this.sId = sId;
	this.contestId = contestId;
	this.teamId = teamId;
	this.team = team;
	this.scores = scores;
	this.threePointMade = threePointMade;
	this.threePointAtt = threePointAtt;
	this.twoPointMade = twoPointMade;
	this.twoPointAtt = twoPointAtt;
	this.freeThrowMade = freeThrowMade;
	this.freeThrowAtt = freeThrowAtt;
	this.offensiveRebounds = offensiveRebounds;
	this.defensiveRebounds = defensiveRebounds;
	this.assists = assists;
	this.turnovers = turnovers;
	this.steals = steals;
	this.blocks = blocks;
	this.personalFouls = personalFouls;
	this.techFouls = techFouls;
    }

    public Long getsId() {
	return sId;
    }

    public void setsId(Long sId) {
	this.sId = sId;
    }

    public Long getContestId() {
	return contestId;
    }

    public void setContestId(Long contestId) {
	this.contestId = contestId;
    }

    public Long getTeamId() {
	return teamId;
    }

    public void setTeamId(Long teamId) {
	this.teamId = teamId;
    }

    public Integer getTeam() {
	return team;
    }

    public void setTeam(Integer team) {
	this.team = team;
    }

    public String getScores() {
	return scores;
    }

    public void setScores(String scores) {
	this.scores = scores;
    }

    public Integer getThreePointMade() {
	return threePointMade;
    }

    public void setThreePointMade(Integer threePointMade) {
	this.threePointMade = threePointMade;
    }

    public Integer getThreePointAtt() {
	return threePointAtt;
    }

    public void setThreePointAtt(Integer threePointAtt) {
	this.threePointAtt = threePointAtt;
    }

    public Integer getTwoPointMade() {
	return twoPointMade;
    }

    public void setTwoPointMade(Integer twoPointMade) {
	this.twoPointMade = twoPointMade;
    }

    public Integer getTwoPointAtt() {
	return twoPointAtt;
    }

    public void setTwoPointAtt(Integer twoPointAtt) {
	this.twoPointAtt = twoPointAtt;
    }

    public Integer getFreeThrowMade() {
	return freeThrowMade;
    }

    public void setFreeThrowMade(Integer freeThrowMade) {
	this.freeThrowMade = freeThrowMade;
    }

    public Integer getFreeThrowAtt() {
	return freeThrowAtt;
    }

    public void setFreeThrowAtt(Integer freeThrowAtt) {
	this.freeThrowAtt = freeThrowAtt;
    }

    public Integer getOffensiveRebounds() {
	return offensiveRebounds;
    }

    public void setOffensiveRebounds(Integer offensiveRebounds) {
	this.offensiveRebounds = offensiveRebounds;
    }

    public Integer getDefensiveRebounds() {
	return defensiveRebounds;
    }

    public void setDefensiveRebounds(Integer defensiveRebounds) {
	this.defensiveRebounds = defensiveRebounds;
    }

    public Integer getAssists() {
	return assists;
    }

    public void setAssists(Integer assists) {
	this.assists = assists;
    }

    public Integer getTurnovers() {
	return turnovers;
    }

    public void setTurnovers(Integer turnovers) {
	this.turnovers = turnovers;
    }

    public Integer getSteals() {
	return steals;
    }

    public void setSteals(Integer steals) {
	this.steals = steals;
    }

    public Integer getBlocks() {
	return blocks;
    }

    public void setBlocks(Integer blocks) {
	this.blocks = blocks;
    }

    public Integer getPersonalFouls() {
	return personalFouls;
    }

    public void setPersonalFouls(Integer personalFouls) {
	this.personalFouls = personalFouls;
    }

    public Integer getTechFouls() {
	return techFouls;
    }

    public void setTechFouls(Integer techFouls) {
	this.techFouls = techFouls;
    }

}
