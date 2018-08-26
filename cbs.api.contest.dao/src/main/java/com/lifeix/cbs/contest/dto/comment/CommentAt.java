package com.lifeix.cbs.contest.dto.comment;

import java.io.Serializable;
import java.util.Date;
/**
 * 评论对象
 * @author lifeix
 *
 */
public class CommentAt implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 753098973713953336L;
    /**
     * 评论id
     */
    private Long id;
    private Integer contestType;
    /**
     * 赛事id
     */
    private Long contestId;
    /**
     * 发布者id
     */
    private Long sendUserId;
    
    /**
     * at者id
     */
    private Long acceptUserId;
    /**
     * 评论内容
     */
    private String content;
    
    /**
     * 0未读，1已读
     */
    private Integer status;
    
    /**
     * 评论时间
     */
    private Date createTime;
    
    /**
     * ip地址
     */
    private String ipaddress;
    /**
     * 评论来源平台
     */
    private String client;
    
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
    public Long getSendUserId() {
        return sendUserId;
    }
    public void setSendUserId(Long sendUserId) {
        this.sendUserId = sendUserId;
    }
    public Long getAcceptUserId() {
        return acceptUserId;
    }
    public void setAcceptUserId(Long acceptUserId) {
        this.acceptUserId = acceptUserId;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Integer getStatus() {
        return status;
    }
    public Integer getContestType() {
        return contestType;
    }
    public void setContestType(Integer contestType) {
        this.contestType = contestType;
    }
    public void setStatus(Integer status) {
        this.status = status;
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
    public String getClient() {
        return client;
    }
    public void setClient(String client) {
        this.client = client;
    }

    
    
}
