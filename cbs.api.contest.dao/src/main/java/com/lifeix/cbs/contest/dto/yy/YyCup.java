package com.lifeix.cbs.contest.dto.yy;

import java.io.Serializable;
import java.util.Date;

/**
 * 押押：赛事分类
 * 
 * @author lifeix
 * 
 */
public class YyCup implements Serializable {

    private static final long serialVersionUID = 1315416817763439491L;

    /**
     * 分类Id
     */
    private Long cupId;

    /**
     * 分类名称
     */
    private String cupName;

    /**
     * 分类统计
     */
    private Integer count;

    /**
     * 创建时间
     */
    private Date createTime;

    public Long getCupId() {
	return cupId;
    }

    public void setCupId(Long cupId) {
	this.cupId = cupId;
    }

    public String getCupName() {
	return cupName;
    }

    public void setCupName(String cupName) {
	this.cupName = cupName;
    }

    public Integer getCount() {
	return count;
    }

    public void setCount(Integer count) {
	this.count = count;
    }

    public Date getCreateTime() {
	return createTime;
    }

    public void setCreateTime(Date createTime) {
	this.createTime = createTime;
    }

}
