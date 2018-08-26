package com.lifeix.cbs.content.dto.frontpage;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lhx on 15-11-27 下午3:00
 *
 * @Description
 */
public class FrontPage implements Serializable {

    private static final long serialVersionUID = -6691884949833623647L;

    /**
     * id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 类型 <0 广告区， >0 内容区
     */
    private Integer type;

    /**
     * 内容
     */
    private String content;

    /**
     * '@'用户组成的jsonarray
     */
    private String at;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 猜友圈ID
     */
    private Long contentId;

    private Integer status ;

    /**
     * 资讯内嵌赛事ID
     */
    private Long innnerContestId ;

    /**
     * 资讯内嵌赛事类型
     */
    private Integer innnercontestType ;

    public Long getInnnerContestId() {
        return innnerContestId;
    }

    public void setInnnerContestId(Long innnerContestId) {
        this.innnerContestId = innnerContestId;
    }

    public Integer getInnnercontestType() {
        return innnercontestType;
    }

    public void setInnnercontestType(Integer innnercontestType) {
        this.innnercontestType = innnercontestType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
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

    public String getAt() {
	return at;
    }

    public void setAt(String at) {
	this.at = at;
    }

    public Date getCreateTime() {
	return createTime;
    }

    public void setCreateTime(Date createTime) {
	this.createTime = createTime;
    }
}
