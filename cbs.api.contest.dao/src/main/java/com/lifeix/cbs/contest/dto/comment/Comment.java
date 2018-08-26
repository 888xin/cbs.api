package com.lifeix.cbs.contest.dto.comment;

import java.io.Serializable;
import java.util.Date;
/**
 * 评论对象
 * @author lifeix
 *
 */
public class Comment implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 753098973713953336L;
    /**
     * 评论id
     */
    private Long id;
    /**
     * 赛事id
     */
    private Long contestId;
    /**
     * 赛事类型（0足球|1篮球）
     */
    private Integer contestType;
    /**
     * 发布者id
     */
    private Long userId;
    /**
     * 评论内容
     */
    private String content;
    
    /**
     * 评论图片集，逗号隔开
     */
    private String images;
    
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
    /**
     * 猜友圈评论id
     */
    private Long cirId;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
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

    public Long getCirId() {
        return cirId;
    }

    public void setCirId(Long cirId) {
        this.cirId = cirId;
    }

    public Integer getContestType() {
        return contestType;
    }

    public void setContestType(Integer contestType) {
        this.contestType = contestType;
    }
    
    
}
