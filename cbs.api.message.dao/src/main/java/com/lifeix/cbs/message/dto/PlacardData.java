package com.lifeix.cbs.message.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lhx on 15-10-19 上午10:51
 *
 * @Description 用户公告数据
 */
public class PlacardData implements Serializable {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 已读时间
     */
    private Date readTime;

    public PlacardData() {
        super();
    }

    public PlacardData(Long userId, Date createTime, Date readTime) {
        super();
        this.userId = userId;
        this.createTime = createTime;
        this.readTime = readTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }

}
