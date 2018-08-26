package com.lifeix.cbs.contest.bean.fb.ext;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

/**
 * 球队扩展信息
 * 
 * @author Lifeix
 * 
 */
public class TeamExtResponse implements JsonSerializer<TeamExtResponse>, Response {

    private static final long serialVersionUID = 5983084119996841383L;

    /**
     * 球衣颜色
     */
    private TeamColorResponse team_color;

    /**
     * 门将球衣颜色
     */
    private TeamColorResponse goalkeeper_team_color;

    /**
     * 排兵阵型
     */
    private String formation;

    /**
     * 阵容
     */
    private List<PlayerResponse> lineups;
    
     

    public TeamExtResponse() {
	super();
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(TeamExtResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

    public TeamColorResponse getTeam_color() {
	return team_color;
    }

    public void setTeam_color(TeamColorResponse team_color) {
	this.team_color = team_color;
    }

    public TeamColorResponse getGoalkeeper_team_color() {
	return goalkeeper_team_color;
    }

    public void setGoalkeeper_team_color(TeamColorResponse goalkeeper_team_color) {
	this.goalkeeper_team_color = goalkeeper_team_color;
    }

    public String getFormation() {
	return formation;
    }

    public void setFormation(String formation) {
	this.formation = formation;
    }

    public List<PlayerResponse> getLineups() {
	return lineups;
    }

    public void setLineups(List<PlayerResponse> lineups) {
	this.lineups = lineups;
    }

}
