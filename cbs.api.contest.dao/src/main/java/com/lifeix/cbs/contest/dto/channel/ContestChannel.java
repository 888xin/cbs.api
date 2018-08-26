package com.lifeix.cbs.contest.dto.channel;

import java.io.Serializable;
import java.util.Date;

public class ContestChannel implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = -2026718072943130055L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;
    /**
     * cupId列表，逗号隔开
     */
    private String data;

    /**
     * 排序
     */
    private int sort;

    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 分类下的赛事类型（0足球|1篮球）
     */
    private Integer contestType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getContestType() {
        return contestType;
    }

    public void setContestType(Integer contestType) {
        this.contestType = contestType;
    }
}
