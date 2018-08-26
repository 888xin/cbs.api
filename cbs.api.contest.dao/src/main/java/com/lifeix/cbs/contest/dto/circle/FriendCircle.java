package com.lifeix.cbs.contest.dto.circle;

import java.io.Serializable;
import java.util.Date;

public class FriendCircle implements Serializable {

    private static final long serialVersionUID = 2394706330791242931L;
    /**
     * 主键id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 源内容id
     */
    private Long sourceId = 0L;

    /**
     * 源作者id
     */
    private Long sourceUserId = 0L;

    /**
     * 类型（心情|推荐）
     */
    private Integer type;

    /**
     * 内容
     */
    private String content;

    /**
     * 扩展字段
     */
    private String params;

    /**
     * 比赛id
     */
    private Long contestId;

    /**
     * 比赛类型
     */
    private Integer contestType;

    /**
     * 客户端标识
     */
    private String client;

    /**
     * 删除标志位
     */
    private Boolean deleteFlag;

    /**
     * 发表时间
     */
    private Date createTime;

    /**
     * 是否包含爆料内容(有音频，图片，文字三者其一即为true)
     */
    private Boolean hasContent;

    /**
     * 龙筹券
     */
    private Integer coupon;

    /**
     * 战绩是否同步结算
     */
    private Boolean settle;

    public Integer getCoupon() {
	return coupon;
    }

    public void setCoupon(Integer coupon) {
	this.coupon = coupon;
    }

    public Long getUserId() {
	return userId;
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public Long getSourceId() {
	return sourceId;
    }

    public void setSourceId(Long sourceId) {
	this.sourceId = sourceId;
    }

    public Long getSourceUserId() {
	return sourceUserId;
    }

    public void setSourceUserId(Long sourceUserId) {
	this.sourceUserId = sourceUserId;
    }

    public Integer getType() {
	return type;
    }

    public void setType(Integer type) {
	this.type = type;
    }

    public String getContent() {
	return content;
    }

    public void setContent(String content) {
	this.content = content;
    }

    public String getParams() {
	return params;
    }

    public void setParams(String params) {
	this.params = params;
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

    public String getClient() {
	return client;
    }

    public void setClient(String client) {
	this.client = client;
    }

    public Boolean getDeleteFlag() {
	return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
	this.deleteFlag = deleteFlag;
    }

    public Date getCreateTime() {
	return createTime;
    }

    public void setCreateTime(Date createTime) {
	this.createTime = createTime;
    }

    public Boolean getHasContent() {
	return this.hasContent;
    }

    public void setHasContent(Boolean hasContent) {
	this.hasContent = hasContent;
    }

    public Boolean getSettle() {
	return settle;
    }

    public void setSettle(Boolean settle) {
	this.settle = settle;
    }

}
