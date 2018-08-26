package com.lifeix.cbs.contest.dto.contest;

import java.io.Serializable;
import java.util.Date;

/**
 * 赛事广告
 * 
 * @author lifeix
 * 
 */
public class ContestAd implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -109388089713542072L;

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
    private Date createTime;

    /**
     * 开始时间
     */
    private Date updateTime;

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

    public Date getCreateTime() {
	return createTime;
    }

    public void setCreateTime(Date createTime) {
	this.createTime = createTime;
    }

    public Date getUpdateTime() {
	return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
	this.updateTime = updateTime;
    }

}
