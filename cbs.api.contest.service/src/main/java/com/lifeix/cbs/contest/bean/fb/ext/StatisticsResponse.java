package com.lifeix.cbs.contest.bean.fb.ext;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

/**
 * 实时技术统计
 * 
 * @author Lifeix
 * 
 */
public class StatisticsResponse implements JsonSerializer<StatisticsResponse>, Response {

    private static final long serialVersionUID = -7970634812694927336L;

    /**
     * 主键
     */
    private Long s_id;

    /**
     * 赛事ID
     */
    private Long contest_Id;

    /**
     * 第三方赛事ID
     */
    private Long target_id;

    /**
     * 队伍ID
     */
    private Long t_id;

    /**
     * 主客标志，1主队2客队
     */
    private Integer team;

    /**
     * 控球时间百分比
     */
    private Integer ball_possession;

    /**
     * 射正球门次数
     */
    private Integer shots_on_goal;

    /**
     * 射偏球门次数
     */
    private Integer shots_off_goal;

    /**
     * 任意球次数
     */
    private Integer free_kicks;

    /**
     * 角球次数
     */
    private Integer corner_kicks;

    /**
     * 越位次数
     */
    private Integer offsides;

    /**
     * 掷界外球次数
     */
    private Integer throw_ins;

    /**
     * 门将扑救次数
     */
    private Integer goalkeeper_saves;

    /**
     * 球门球次数
     */
    private Integer goal_kicks;

    /**
     * 犯规次数
     */
    private Integer fouls;

    public StatisticsResponse() {
	super();
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(StatisticsResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

    public Integer getBall_possession() {
	return ball_possession;
    }

    public void setBall_possession(Integer ball_possession) {
	this.ball_possession = ball_possession;
    }

    public Integer getShots_on_goal() {
	return shots_on_goal;
    }

    public void setShots_on_goal(Integer shots_on_goal) {
	this.shots_on_goal = shots_on_goal;
    }

    public Integer getShots_off_goal() {
	return shots_off_goal;
    }

    public void setShots_off_goal(Integer shots_off_goal) {
	this.shots_off_goal = shots_off_goal;
    }

    public Integer getFree_kicks() {
	return free_kicks;
    }

    public void setFree_kicks(Integer free_kicks) {
	this.free_kicks = free_kicks;
    }

    public Integer getCorner_kicks() {
	return corner_kicks;
    }

    public void setCorner_kicks(Integer corner_kicks) {
	this.corner_kicks = corner_kicks;
    }

    public Integer getThrow_ins() {
	return throw_ins;
    }

    public void setThrow_ins(Integer throw_ins) {
	this.throw_ins = throw_ins;
    }

    public Integer getGoalkeeper_saves() {
	return goalkeeper_saves;
    }

    public void setGoalkeeper_saves(Integer goalkeeper_saves) {
	this.goalkeeper_saves = goalkeeper_saves;
    }

    public Integer getGoal_kicks() {
	return goal_kicks;
    }

    public void setGoal_kicks(Integer goal_kicks) {
	this.goal_kicks = goal_kicks;
    }

    public Integer getFouls() {
	return fouls;
    }

    public void setFouls(Integer fouls) {
	this.fouls = fouls;
    }

    public Integer getOffsides() {
	return offsides;
    }

    public void setOffsides(Integer offsides) {
	this.offsides = offsides;
    }

    public Long getS_id() {
	return s_id;
    }

    public void setS_id(Long s_id) {
	this.s_id = s_id;
    }

    public Long getContest_Id() {
	return contest_Id;
    }

    public void setContest_Id(Long contest_Id) {
	this.contest_Id = contest_Id;
    }

    public Long getTarget_id() {
	return target_id;
    }

    public void setTarget_id(Long target_id) {
	this.target_id = target_id;
    }

    public Long getT_id() {
	return t_id;
    }

    public void setT_id(Long t_id) {
	this.t_id = t_id;
    }

    public Integer getTeam() {
	return team;
    }

    public void setTeam(Integer team) {
	this.team = team;
    }

}
