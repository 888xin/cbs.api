package com.lifeix.cbs.contest.bean.bb.ext;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

/**
 * 裁判
 * 
 * @author Lifeix
 * 
 */
public class BbRefereeResponse implements JsonSerializer<BbRefereeResponse>, Response {

    private static final long serialVersionUID = -7806457402502567530L;

    /**
     * 名字
     */
    private String name;

    /**
     * 衣服号码
     */
    private Integer number;

    /**
     * 在赛事的中的判罚角色(主裁或副裁)
     */
    private Integer assignment;

    /**
     * 在联盟中的执行经验年限
     */
    private Integer experience;

    public BbRefereeResponse() {
	super();
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(BbRefereeResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Integer getNumber() {
	return number;
    }

    public void setNumber(Integer number) {
	this.number = number;
    }

    public Integer getAssignment() {
	return assignment;
    }

    public void setAssignment(Integer assignment) {
	this.assignment = assignment;
    }

    public Integer getExperience() {
	return experience;
    }

    public void setExperience(Integer experience) {
	this.experience = experience;
    }

}
