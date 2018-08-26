package com.lifeix.cbs.contest.bean.fb.ext;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

public class RecordResponse implements JsonSerializer<RecordResponse>, Response {

    private static final long serialVersionUID = 8040067074642617701L;

    /**
     * 第三方赛事ID
     */
    private Long target_id;

    /**
     * 开始时间
     */
    private String start_time;

    /**
     * 主队
     */
    private Long home_team;

    /**
     * 客队
     */
    private Long away_team;

    /**
     * 主队得分
     */
    private Integer home_scores;

    /**
     * 客队得分
     */
    private Integer away_scores;

    /**
     * 联赛或杯赛ID
     */
    private Long cup_id;

    /**
     * 联赛或杯赛名称
     */
    private String cup_name;

    /**
     * 胜负种类
     */
    private Integer win_type;

    /**
     * 竞彩赢盘种类
     */
    private Integer jc_win_type;
    
    
    private String home_team_name;

    /**
     * 客队
     */
    private String away_team_name;

    public Long getTarget_id() {
	return target_id;
    }

    public void setTarget_id(Long target_id) {
	this.target_id = target_id;
    }

    public String getStart_time() {
	return start_time;
    }

    public void setStart_time(String start_time) {
	this.start_time = start_time;
    }

    public Long getHome_team() {
	return home_team;
    }

    public void setHome_team(Long home_team) {
	this.home_team = home_team;
    }

    public Long getAway_team() {
	return away_team;
    }

    public void setAway_team(Long away_team) {
	this.away_team = away_team;
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

    public Long getCup_id() {
	return cup_id;
    }

    public void setCup_id(Long cup_id) {
	this.cup_id = cup_id;
    }

    public String getCup_name() {
	return cup_name;
    }

    public void setCup_name(String cup_name) {
	this.cup_name = cup_name;
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(RecordResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

    public Integer getWin_type() {
	return win_type;
    }

    public void setWin_type(Integer win_type) {
	this.win_type = win_type;
    }

    public Integer getJc_win_type() {
	return jc_win_type;
    }

    public void setJc_win_type(Integer jc_win_type) {
	this.jc_win_type = jc_win_type;
    }

    public String getHome_team_name() {
        return home_team_name;
    }

    public void setHome_team_name(String home_team_name) {
        this.home_team_name = home_team_name;
    }

    public String getAway_team_name() {
        return away_team_name;
    }

    public void setAway_team_name(String away_team_name) {
        this.away_team_name = away_team_name;
    }

  
}
