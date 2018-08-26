package com.lifeix.cbs.contest.dto.fb;

import java.io.Serializable;

public class FbStatistics implements Serializable {

    private static final long serialVersionUID = -8393990644584963438L;

    /**
     * 主键
     */
    private Long sId;

    /**
     * 赛事ID
     */
    private Long contestId;

    /**
     * 雷达赛事ID
     */
    private Long targetId;

    /**
     * 队伍ID
     */
    private Long teamId;

    /**
     * 主客标志，1主队2客队
     */
    private Integer team;

    /**
     * 控球百分比
     */
    private Integer ballPossession;

    /**
     * 射正球门次数
     */
    private Integer shotsOnGoal;

    /**
     * 射偏球门次数
     */
    private Integer shotsOffGoal;

    /**
     * 任意球次数
     */
    private Integer freeKicks;

    /**
     * 角球次数
     */
    private Integer cornerKicks;

    /**
     * 越位次数
     */
    private Integer offsides;

    /**
     * 掷界外球次数
     */
    private Integer throwIns;

    /**
     * 门将扑救次数
     */
    private Integer goalkeeperSaves;

    /**
     * 球门球次数
     */
    private Integer goalKicks;

    /**
     * 犯规次数
     */
    private Integer fouls;

    public FbStatistics() {
	super();
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

    public Long getTargetId() {
	return targetId;
    }

    public void setTargetId(Long targetId) {
	this.targetId = targetId;
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

    public Integer getBallPossession() {
	return ballPossession;
    }

    public void setBallPossession(Integer ballPossession) {
	this.ballPossession = ballPossession;
    }

    public Integer getShotsOnGoal() {
	return shotsOnGoal;
    }

    public void setShotsOnGoal(Integer shotsOnGoal) {
	this.shotsOnGoal = shotsOnGoal;
    }

    public Integer getShotsOffGoal() {
	return shotsOffGoal;
    }

    public void setShotsOffGoal(Integer shotsOffGoal) {
	this.shotsOffGoal = shotsOffGoal;
    }

    public Integer getFreeKicks() {
	return freeKicks;
    }

    public void setFreeKicks(Integer freeKicks) {
	this.freeKicks = freeKicks;
    }

    public Integer getCornerKicks() {
	return cornerKicks;
    }

    public void setCornerKicks(Integer cornerKicks) {
	this.cornerKicks = cornerKicks;
    }

    public Integer getOffsides() {
	return offsides;
    }

    public void setOffsides(Integer offsides) {
	this.offsides = offsides;
    }

    public Integer getThrowIns() {
	return throwIns;
    }

    public void setThrowIns(Integer throwIns) {
	this.throwIns = throwIns;
    }

    public Integer getGoalkeeperSaves() {
	return goalkeeperSaves;
    }

    public void setGoalkeeperSaves(Integer goalkeeperSaves) {
	this.goalkeeperSaves = goalkeeperSaves;
    }

    public Integer getGoalKicks() {
	return goalKicks;
    }

    public void setGoalKicks(Integer goalKicks) {
	this.goalKicks = goalKicks;
    }

    public Integer getFouls() {
	return fouls;
    }

    public void setFouls(Integer fouls) {
	this.fouls = fouls;
    }

}
