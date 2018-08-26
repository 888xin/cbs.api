package com.lifeix.cbs.contest.bean.settle;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

/**
 * 结算任务
 * 
 * @author peter
 * 
 */
public class CbsSettleResponse implements JsonSerializer<CbsSettleResponse>, Response {

    private static final long serialVersionUID = -7716108711226445656L;

    /**
     * 结算ID
     */
    private Long settle_id;

    /**
     * 赛事类型
     */
    private Integer type;

    /**
     * 赛事ID
     */
    private Long contest_id;

    /**
     * 是否已结算
     */
    private Integer close_flag;

    /**
     * 创建时间
     */
    private String create_time;

    /**
     * 预结算时间
     */
    private String settle_time;

    public Long getSettle_id() {
	return settle_id;
    }

    public void setSettle_id(Long settle_id) {
	this.settle_id = settle_id;
    }

    public Integer getType() {
	return type;
    }

    public void setType(Integer type) {
	this.type = type;
    }

    public Long getContest_id() {
	return contest_id;
    }

    public void setContest_id(Long contest_id) {
	this.contest_id = contest_id;
    }

    public Integer getClose_flag() {
	return close_flag;
    }

    public void setClose_flag(Integer close_flag) {
	this.close_flag = close_flag;
    }

    public String getCreate_time() {
	return create_time;
    }

    public void setCreate_time(String create_time) {
	this.create_time = create_time;
    }

    public String getSettle_time() {
	return settle_time;
    }

    public void setSettle_time(String settle_time) {
	this.settle_time = settle_time;
    }

    @Override
    public JsonElement serialize(CbsSettleResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
