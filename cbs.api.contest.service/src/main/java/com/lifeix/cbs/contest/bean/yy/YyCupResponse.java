package com.lifeix.cbs.contest.bean.yy;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

/**
 * 押押：赛事分类信息
 * 
 * @author peter
 * 
 */
public class YyCupResponse implements JsonSerializer<YyCupResponse>, Response {

    private static final long serialVersionUID = 5501473368693417811L;

    /**
     * 分类Id
     */
    private Long cup_id;

    /**
     * 分类名称
     */
    private String cup_name;

    /**
     * 分类统计
     */
    private Integer count;

    /**
     * 创建时间
     */
    private String create_time;

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

    public Integer getCount() {
	return count;
    }

    public void setCount(Integer count) {
	this.count = count;
    }

    public String getCreate_time() {
	return create_time;
    }

    public void setCreate_time(String create_time) {
	this.create_time = create_time;
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(YyCupResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

}
