package com.lifeix.cbs.api.bean.market;

import com.lifeix.user.beans.Response;

/**
 * 渠道统计
 * 
 * @author lifeix
 * 
 */
public class CbsMarketStatisticResponse implements Response {

    private static final long serialVersionUID = -2536806949667106487L;

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
    private String statisticDate;

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

    /**
     * 渠道
     */
    private String marketCodeOri;

    @Override
    public String getObjectName() {
	return "CbsMarketStatistic";
    }

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

    public String getStatisticDate() {
	return statisticDate;
    }

    public void setStatisticDate(String statisticDate) {
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

    public String getMarketCodeOri() {
        return marketCodeOri;
    }

    public void setMarketCodeOri(String marketCodeOri) {
        this.marketCodeOri = marketCodeOri;
    }
}
