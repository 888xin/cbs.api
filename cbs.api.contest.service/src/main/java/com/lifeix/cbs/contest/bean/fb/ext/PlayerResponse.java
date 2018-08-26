package com.lifeix.cbs.contest.bean.fb.ext;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

/**
 * 球员
 * 
 * @author Lifeix
 * 
 */
public class PlayerResponse implements JsonSerializer<PlayerResponse>, Response {

    private static final long serialVersionUID = 4336411466180271014L;

    /**
     * 场上位置
     */
    private Integer pos;

    /**
     * 球员名字
     */
    private String name;

    /**
     * 球衣号码
     */
    private Integer shirt_number;

    /**
     * 是否是替补
     */
    private boolean substitute;

    /**
     * 主客标志，1主队2客队
     */
    private Integer team;

    public PlayerResponse() {
	super();
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(PlayerResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
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

    public Integer getShirt_number() {
	return shirt_number;
    }

    public void setShirt_number(Integer shirt_number) {
	this.shirt_number = shirt_number;
    }

    public boolean isSubstitute() {
	return substitute;
    }

    public void setSubstitute(boolean substitute) {
	this.substitute = substitute;
    }

    public Integer getTeam() {
	return team;
    }

    public void setTeam(Integer team) {
	this.team = team;
    }

}
