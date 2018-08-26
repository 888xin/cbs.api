package com.lifeix.cbs.contest.bean.bb.ext;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.cbs.contest.bean.fb.TeamResponse;
import com.lifeix.user.beans.Response;

/**
 * 球员
 * 
 * @author Lifeix
 * 
 */
public class BbPlayerResponse implements JsonSerializer<BbPlayerResponse>, Response {

    private static final long serialVersionUID = 7483563366005469462L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 雷达球员ID
     */
    private String player_id;

    /**
     * 场上位置
     */
    private Integer pos;

    /**
     * 球员名字
     */
    private String name;

    /**
     * 名
     */
    private String first_name;

    /**
     * 姓
     */
    private String last_name;

    /**
     * 球衣号码
     */
    private Integer shirt_number;

    /**
     * 主客标志，1主队2客队
     */
    private Integer team;

    /**
     * 状态(首发，替补上场，未上场，未激活)
     */
    private Integer status;

    /**
     * 当前效力球队ID
     */
    private Long team_id;

    /**
     * 当前效力的球队信息
     */
    private TeamResponse team_info;

    public BbPlayerResponse() {
	super();
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(BbPlayerResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getPlayer_id() {
	return player_id;
    }

    public void setPlayer_id(String player_id) {
	this.player_id = player_id;
    }

    public Integer getPos() {
	return pos;
    }

    public void setPos(Integer pos) {
	this.pos = pos;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getFirst_name() {
	return first_name;
    }

    public void setFirst_name(String first_name) {
	this.first_name = first_name;
    }

    public String getLast_name() {
	return last_name;
    }

    public void setLast_name(String last_name) {
	this.last_name = last_name;
    }

    public Integer getShirt_number() {
	return shirt_number;
    }

    public void setShirt_number(Integer shirt_number) {
	this.shirt_number = shirt_number;
    }

    public Integer getTeam() {
	return team;
    }

    public void setTeam(Integer team) {
	this.team = team;
    }

    public Integer getStatus() {
	return status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    public TeamResponse getTeam_info() {
	return team_info;
    }

    public void setTeam_info(TeamResponse team_info) {
	this.team_info = team_info;
    }

    public Long getTeam_id() {
	return team_id;
    }

    public void setTeam_id(Long team_id) {
	this.team_id = team_id;
    }

}
