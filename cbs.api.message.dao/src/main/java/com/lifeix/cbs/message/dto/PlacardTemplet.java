package com.lifeix.cbs.message.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lhx on 15-10-19 上午10:50
 * 
 * @Description 全站公告
 */
public class PlacardTemplet implements Serializable {

    private static final long serialVersionUID = -591106942700537544L;

    private Long templetId;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 关闭标识
     */
    private Boolean disableFlag;

    /**
     * 公告统计
     */
    private Integer placardCount;

    /**
     * 链接类型
     * 
     * 1.网页 2.内容 3.足球赛事 4.篮球赛事 5.锦标赛 6.送龙筹券
     */
    private Integer linkType;

    /**
     * 链接数据
     */
    private String linkData;

    public PlacardTemplet() {
	super();
    }

    public PlacardTemplet(Long templetId, String title, String content, Date createTime, Date endTime, Boolean disableFlag,
	    Integer placardCount, Integer linkType, String linkData) {
	super();
	this.templetId = templetId;
	this.title = title;
	this.content = content;
	this.createTime = createTime;
	this.endTime = endTime;
	this.disableFlag = disableFlag;
	this.placardCount = placardCount;
	this.linkType = linkType;
	this.linkData = linkData;
    }

    public Integer getLinkType() {
	return linkType;
    }

    public void setLinkType(Integer linkType) {
	this.linkType = linkType;
    }

    public String getLinkData() {
	return linkData;
    }

    public void setLinkData(String linkData) {
	this.linkData = linkData;
    }

    public Long getTempletId() {
	return templetId;
    }

    public void setTempletId(Long templetId) {
	this.templetId = templetId;
    }

    public Date getCreateTime() {
	return createTime;
    }

    public void setCreateTime(Date createTime) {
	this.createTime = createTime;
    }

    public Date getEndTime() {
	return endTime;
    }

    public void setEndTime(Date endTime) {
	this.endTime = endTime;
    }

    public Boolean getDisableFlag() {
	return disableFlag;
    }

    public void setDisableFlag(Boolean disableFlag) {
	this.disableFlag = disableFlag;
    }

    public Integer getPlacardCount() {
	return placardCount;
    }

    public void setPlacardCount(Integer placardCount) {
	this.placardCount = placardCount;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getContent() {
	return content;
    }

    public void setContent(String content) {
	this.content = content;
    }

}
