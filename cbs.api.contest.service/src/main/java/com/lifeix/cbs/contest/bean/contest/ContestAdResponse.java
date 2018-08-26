package com.lifeix.cbs.contest.bean.contest;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

/**
 * 押押：赛事信息
 * 
 * @author peter
 * 
 */
public class ContestAdResponse implements JsonSerializer<ContestAdResponse>, Response {

    /**
     * 
     */
    private static final long serialVersionUID = 7443098467203237293L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 赛事主键
     */
    private Long contestId;

    /**
     * 赛事类型
     */
    private Integer contestType;

    /**
     * 标题
     */
    private String title;

    /**
     * 图片集
     */
    private String images;

    /**
     * 描述
     */
    private String text;

    /**
     * 隐藏标识
     */
    private Boolean hideFlag;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 跳转url
     */
    private String url_skip;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Long getContestId() {
	return contestId;
    }

    public void setContestId(Long contestId) {
	this.contestId = contestId;
    }

    public Integer getContestType() {
	return contestType;
    }

    public void setContestType(Integer contestType) {
	this.contestType = contestType;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getImages() {
	return images;
    }

    public void setImages(String images) {
	this.images = images;
    }

    public String getText() {
	return text;
    }

    public void setText(String text) {
	this.text = text;
    }

    public Boolean getHideFlag() {
	return hideFlag;
    }

    public void setHideFlag(Boolean hideFlag) {
	this.hideFlag = hideFlag;
    }

    public String getCreateTime() {
	return createTime;
    }

    public void setCreateTime(String createTime) {
	this.createTime = createTime;
    }

    public String getUpdateTime() {
	return updateTime;
    }

    public void setUpdateTime(String updateTime) {
	this.updateTime = updateTime;
    }

    public String getUrl_skip() {
	return url_skip;
    }

    public void setUrl_skip(String url_skip) {
	this.url_skip = url_skip;
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(ContestAdResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

}
