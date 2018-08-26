package com.lifeix.cbs.api.dto.market;

import java.io.Serializable;
import java.util.Date;

/**
 * 渠道日统计
 * 
 * @author yis
 * 
 */
public class CbsMarketDayStatistic implements Serializable {

    private static final long serialVersionUID = 2487274357217324924L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 渠道
     */
    private String marketCode;

    /**
     * 统计日期
     */
    private Date statisticDate;

    /**
     * 统计日期(开始)
     */
    private Date statisticDateBefore;

    /**
     * 统计日期(结束)
     */
    private Date statisticDateAfter;

    /**
     * 男性人数
     */
    private Integer maleNums;

    /**
     * 女性人数
     */
    private Integer femaleNums;

    /**
     * 总人数
     */
    private Integer totalNums;

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getMarketCode() {
	return marketCode;
    }

    public void setMarketCode(String marketCode) {
	this.marketCode = marketCode;
    }

    public Date getStatisticDate() {
	return statisticDate;
    }

    public void setStatisticDate(Date statisticDate) {
	this.statisticDate = statisticDate;
    }

    public Integer getMaleNums() {
	return maleNums;
    }

    public void setMaleNums(Integer maleNums) {
	this.maleNums = maleNums;
    }

    public Integer getFemaleNums() {
	return femaleNums;
    }

    public void setFemaleNums(Integer femaleNums) {
	this.femaleNums = femaleNums;
    }

    public Integer getTotalNums() {
	return totalNums;
    }

    public void setTotalNums(Integer totalNums) {
	this.totalNums = totalNums;
    }

    public Date getStatisticDateBefore() {
        return statisticDateBefore;
    }

    public void setStatisticDateBefore(Date statisticDateBefore) {
        this.statisticDateBefore = statisticDateBefore;
    }

    public Date getStatisticDateAfter() {
        return statisticDateAfter;
    }

    public void setStatisticDateAfter(Date statisticDateAfter) {
        this.statisticDateAfter = statisticDateAfter;
    }
}
