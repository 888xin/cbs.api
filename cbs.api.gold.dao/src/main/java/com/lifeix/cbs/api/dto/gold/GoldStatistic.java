package com.lifeix.cbs.api.dto.gold;

import java.io.Serializable;
import java.util.Date;

/**
 * 后台收入支出统计
 * 
 * @author jacky
 *
 */
public class GoldStatistic implements Serializable {

    private static final long serialVersionUID = -2549797701692470339L;

    /**
     * id
     */
    private Long id;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 天数
     */
    private Integer day;

    /**
     * 系统当日总收入
     */
    private Double income;

    /**
     * 系统当日总支出
     */
    private Double outlay;

    /**
     * 合计
     */
    private Double total;

    /**
     * 当日收入记录数
     */
    private Long inCounts;

    /**
     * 当日支出记录数
     */
    private Long outCounts;

    /**
     * 日期
     */
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Double getIncome() {
        return income;
    }

    public void setIncome(Double income) {
        this.income = income;
    }

    public Double getOutlay() {
        return outlay;
    }

    public void setOutlay(Double outlay) {
        this.outlay = outlay;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Long getInCounts() {
        return inCounts;
    }

    public void setInCounts(Long inCounts) {
        this.inCounts = inCounts;
    }

    public Long getOutCounts() {
        return outCounts;
    }

    public void setOutCounts(Long outCounts) {
        this.outCounts = outCounts;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    
}