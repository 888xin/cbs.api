package com.lifeix.cbs.content.dto.contest;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lhx on 16-4-14 下午5:41
 *
 * @Description
 */
public class ContestNews implements Serializable {

    private static final long serialVersionUID = 1887678705155129058L;
    private Long id ;
    /**
     * 内容
     */
    private String content ;

    private Date createTime;

    private Integer status ;

    /**
     * 资讯id
     */
    private Long contentId ;

    /**
     * 赛事类型: 1---足球, 2---篮球
     */
    private Integer contestType;

    private Long contestId ;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public Integer getContestType() {
        return contestType;
    }

    public void setContestType(Integer contestType) {
        this.contestType = contestType;
    }

    public Long getContestId() {
        return contestId;
    }

    public void setContestId(Long contestId) {
        this.contestId = contestId;
    }
}
