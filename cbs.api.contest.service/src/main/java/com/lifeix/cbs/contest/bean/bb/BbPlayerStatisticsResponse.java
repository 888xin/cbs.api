package com.lifeix.cbs.contest.bean.bb;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

/**
 * 篮球球队实时技术统计
 * 
 * @author Lifeix
 * 
 */
public class BbPlayerStatisticsResponse implements JsonSerializer<BbPlayerStatisticsResponse>, Response {

    private static final long serialVersionUID = 2204201685368930614L;

    /**
     * 主键
     */
    private Long p_id;
    /**
     * 赛事ID
     */
    private Long contest_id;
    /**
     * 队伍ID
     */
    private Long team_id;
    /**
     * 雷达球员ID
     */
    private String player_id;
    /**
     * 主客标志，1主队2客队
     */
    private Integer team;
    /**
     * 上场时间，单位秒
     */
    private Integer play_time;
    /**
     * 各节次得分
     */
    private Integer points;
    /**
     * 三分球命中数
     */
    private Integer three_point_made;
    /**
     * 三分球投篮数
     */
    private Integer three_point_att;
    /**
     * 两分球命中数
     */
    private Integer two_point_made;
    /**
     * 两分球投篮数
     */
    private Integer two_point_att;
    /**
     * 罚球命中数
     */
    private Integer free_throw_made;
    /**
     * 罚球投篮数
     */
    private Integer free_throw_att;
    /**
     * 前场篮板
     */
    private Integer offensive_rebounds;
    /**
     * 后场篮板
     */
    private Integer defensive_rebounds;
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
    private Integer personal_fouls;
    /**
     * 技术犯规
     */
    private Integer tech_fouls;
    /**
     * 是否无效
     */
    private Boolean disabled;

    public Boolean getDisabled() {
	return disabled;
    }

    public void setDisabled(Boolean disabled) {
	this.disabled = disabled;
    }

    public BbPlayerStatisticsResponse() {
	super();
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(BbPlayerStatisticsResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

    public Long getP_id() {
	return p_id;
    }

    public void setP_id(Long p_id) {
	this.p_id = p_id;
    }

    public Long getContest_id() {
	return contest_id;
    }

    public void setContest_id(Long contest_id) {
	this.contest_id = contest_id;
    }

    public Long getTeam_id() {
	return team_id;
    }

    public void setTeam_id(Long team_id) {
	this.team_id = team_id;
    }

    public String getPlayer_id() {
	return player_id;
    }

    public void setPlayer_id(String player_id) {
	this.player_id = player_id;
    }

    public Integer getTeam() {
	return team;
    }

    public void setTeam(Integer team) {
	this.team = team;
    }

    public Integer getPlay_time() {
	return play_time;
    }

    public void setPlay_time(Integer play_time) {
	this.play_time = play_time;
    }

    public Integer getPoints() {
	return points;
    }

    public void setPoints(Integer points) {
	this.points = points;
    }

    public Integer getThree_point_made() {
	return three_point_made;
    }

    public void setThree_point_made(Integer three_point_made) {
	this.three_point_made = three_point_made;
    }

    public Integer getThree_point_att() {
	return three_point_att;
    }

    public void setThree_point_att(Integer three_point_att) {
	this.three_point_att = three_point_att;
    }

    public Integer getTwo_point_made() {
	return two_point_made;
    }

    public void setTwo_point_made(Integer two_point_made) {
	this.two_point_made = two_point_made;
    }

    public Integer getTwo_point_att() {
	return two_point_att;
    }

    public void setTwo_point_att(Integer two_point_att) {
	this.two_point_att = two_point_att;
    }

    public Integer getFree_throw_made() {
	return free_throw_made;
    }

    public void setFree_throw_made(Integer free_throw_made) {
	this.free_throw_made = free_throw_made;
    }

    public Integer getFree_throw_att() {
	return free_throw_att;
    }

    public void setFree_throw_att(Integer free_throw_att) {
	this.free_throw_att = free_throw_att;
    }

    public Integer getOffensive_rebounds() {
	return offensive_rebounds;
    }

    public void setOffensive_rebounds(Integer offensive_rebounds) {
	this.offensive_rebounds = offensive_rebounds;
    }

    public Integer getDefensive_rebounds() {
	return defensive_rebounds;
    }

    public void setDefensive_rebounds(Integer defensive_rebounds) {
	this.defensive_rebounds = defensive_rebounds;
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

    public Integer getPersonal_fouls() {
	return personal_fouls;
    }

    public void setPersonal_fouls(Integer personal_fouls) {
	this.personal_fouls = personal_fouls;
    }

    public Integer getTech_fouls() {
	return tech_fouls;
    }

    public void setTech_fouls(Integer tech_fouls) {
	this.tech_fouls = tech_fouls;
    }
}
