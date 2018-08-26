package com.lifeix.cbs.contest.bean.fb.ext;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

/**
 * 球衣颜色
 * 
 * @author Lifeix
 * 
 */
public class TeamColorResponse implements JsonSerializer<TeamColorResponse>, Response {

    private static final long serialVersionUID = -2047666261915674990L;

    /**
     * 水平条纹颜色
     */
    private String h_stripes;

    /**
     * 主颜色
     */
    private String main;

    /**
     * 号码颜色
     */
    private String number;

    /**
     * 袖子颜色
     */
    private String sleeve;

    /**
     * 分隔线颜色
     */
    private String split;

    /**
     * 方格颜色
     */
    private String squares;

    /**
     * 垂直条纹颜色
     */
    private String v_stripes;

    public TeamColorResponse() {
	super();
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(TeamColorResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

    public String getH_stripes() {
	return h_stripes;
    }

    public void setH_stripes(String h_stripes) {
	this.h_stripes = h_stripes;
    }

    public String getMain() {
	return main;
    }

    public void setMain(String main) {
	this.main = main;
    }

    public String getNumber() {
	return number;
    }

    public void setNumber(String number) {
	this.number = number;
    }

    public String getSleeve() {
	return sleeve;
    }

    public void setSleeve(String sleeve) {
	this.sleeve = sleeve;
    }

    public String getSplit() {
	return split;
    }

    public void setSplit(String split) {
	this.split = split;
    }

    public String getSquares() {
	return squares;
    }

    public void setSquares(String squares) {
	this.squares = squares;
    }

    public String getV_stripes() {
	return v_stripes;
    }

    public void setV_stripes(String v_stripes) {
	this.v_stripes = v_stripes;
    }
}
