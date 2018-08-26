package com.lifeix.cbs.contest.bean.fb;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

public class FbLiveWordsResponse implements JsonSerializer<FbLiveWordsResponse>, Response {

    private static final long serialVersionUID = 4765319498729812951L;

    /**
     * 主键
     */
    private Long phrase_id;

    /**
     * 赛事ID
     */
    private Long contest_id;

    /**
     * 第三方赛事ID
     */
    private Long target_id;

    /**
     * 第三方赛事文字ID
     */
    private Long target_phrase_id;

    /**
     * 文字内容
     */
    private String text_content;

    /**
     * 发生时间
     */
    private Integer time;

    /**
     * 发生在伤停补时时间
     */
    private Integer injury_time;

    /**
     * 是否无效
     */
    private Boolean disabled;

    /**
     * 事件类型
     */
    private Integer type;

    public FbLiveWordsResponse() {
	super();
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(FbLiveWordsResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

    public Long getPhrase_id() {
	return phrase_id;
    }

    public void setPhrase_id(Long phrase_id) {
	this.phrase_id = phrase_id;
    }

    public Long getContest_id() {
	return contest_id;
    }

    public void setContest_id(Long contest_id) {
	this.contest_id = contest_id;
    }

    public Long getTarget_id() {
	return target_id;
    }

    public void setTarget_id(Long target_id) {
	this.target_id = target_id;
    }

    public Long getTarget_phrase_id() {
	return target_phrase_id;
    }

    public void setTarget_phrase_id(Long target_phrase_id) {
	this.target_phrase_id = target_phrase_id;
    }

    public String getText_content() {
	return text_content;
    }

    public void setText_content(String text_content) {
	this.text_content = text_content;
    }

    public Integer getTime() {
	return time;
    }

    public void setTime(Integer time) {
	this.time = time;
    }

    public Boolean getDisabled() {
	return disabled;
    }

    public void setDisabled(Boolean disabled) {
	this.disabled = disabled;
    }

    public Integer getType() {
	return type;
    }

    public void setType(Integer type) {
	this.type = type;
    }

    public Integer getInjury_time() {
	return injury_time;
    }

    public void setInjury_time(Integer injury_time) {
	this.injury_time = injury_time;
    }

}
