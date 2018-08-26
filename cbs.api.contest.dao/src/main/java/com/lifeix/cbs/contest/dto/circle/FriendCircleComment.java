package com.lifeix.cbs.contest.dto.circle;

import java.io.Serializable;
import java.util.Date;

/**
 * 朋友圈评论实体
 * 
 * @author jacky
 *
 */
public class FriendCircleComment implements Serializable {

    private static final long serialVersionUID = 667208964264582706L;

    private Long id;

    /**
     * 新闻Id
     */
    private Long contentId;

    /**
     * 发表猜友圈用户id
     */
    private Long circleUserId;

    /**
     * 评论者id
     */
    private Long commUserId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论图片
     */
    private String image;

    /**
     * 被回复用户id
     */
    private Long reUserId;

    /**
     * 被回复内容
     */
    private String reContent;

    /**
     * 被回复图片
     */
    private String reImage;

    /**
     * 评论时间
     */
    private Date createTime;

    /**
     * ip地址
     */
    private String ipaddress;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 来源
     */
    private String source;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Long getContentId() {
	return contentId;
    }

    public void setContentId(Long contentId) {
	this.contentId = contentId;
    }

    public String getContent() {
	return content;
    }

    public void setContent(String content) {
	this.content = content;
    }

    public String getImage() {
	return image;
    }

    public void setImage(String image) {
	this.image = image;
    }

    public Long getCircleUserId() {
	return circleUserId;
    }

    public void setCircleUserId(Long circleUserId) {
	this.circleUserId = circleUserId;
    }

    public Long getCommUserId() {
	return commUserId;
    }

    public void setCommUserId(Long commUserId) {
	this.commUserId = commUserId;
    }

    public Long getReUserId() {
	return reUserId;
    }

    public void setReUserId(Long reUserId) {
	this.reUserId = reUserId;
    }

    public String getReContent() {
	return reContent;
    }

    public void setReContent(String reContent) {
	this.reContent = reContent;
    }

    public String getReImage() {
	return reImage;
    }

    public void setReImage(String reImage) {
	this.reImage = reImage;
    }

    public Date getCreateTime() {
	return createTime;
    }

    public void setCreateTime(Date createTime) {
	this.createTime = createTime;
    }

    public String getIpaddress() {
	return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
	this.ipaddress = ipaddress;
    }

    public Integer getStatus() {
	return status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    public String getSource() {
	return source;
    }

    public void setSource(String source) {
	this.source = source;
    }

}
