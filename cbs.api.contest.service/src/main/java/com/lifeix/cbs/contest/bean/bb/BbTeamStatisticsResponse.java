package com.lifeix.cbs.contest.bean.bb;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.cbs.contest.bean.bb.ext.BbScoreResponse;
import com.lifeix.user.beans.Response;

/**
 * 篮球球队实时技术统计
 * 
 * @author Lifeix
 * 
 */
public class BbTeamStatisticsResponse implements JsonSerializer<BbTeamStatisticsResponse>, Response {

    private static final long serialVersionUID = 3470917323542155596L;

    /**
     * 主键
     */
    private Long s_id;
    /**
     * 赛事ID
     */
    private Long contest_id;
    /**
     * 队伍ID
     */
    private Long team_id;
    /**
     * 主客标志，1主队2客队
     */
    private Integer team;
    /**
     * 各节次得分
     */
    private List<BbScoreResponse> seg_scores;
    /**
     * 各节次得分
     */
    private String scores;
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

    public BbTeamStatisticsResponse() {
	super();
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(BbTeamStatisticsResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

    public Long getS_id() {
	return s_id;
    }

    public void setS_id(Long s_id) {
	this.s_id = s_id;
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

    public List<BbScoreResponse> getSeg_scores() {
	return seg_scores;
    }

    public void setSeg_scores(List<BbScoreResponse> seg_scores) {
	this.seg_scores = seg_scores;
    }
}
