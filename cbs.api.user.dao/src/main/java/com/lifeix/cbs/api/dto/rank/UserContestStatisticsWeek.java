package com.lifeix.cbs.api.dto.rank;

import java.io.Serializable;

/**
 * 用户统计周表（用户一周内的下单数据统计）
 * 
 * @author pengkw
 * 
 */
public class UserContestStatisticsWeek implements Serializable {

    private static final long serialVersionUID = -4406590899514326803L;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 一年中的第几周
     */
    private Integer week;

    /**
     * 下单次数
     */
    private Integer betCount = 0;

    /**
     * 赢次数
     */
    private Integer winCount = 0;

    /**
     * 平（走）次数
     */
    private Integer drawCount = 0;

    /**
     * 输次数
     */
    private Integer lossCount = 0;

    /**
     * 胜率
     */
    private Double winning = 0.00;

    /**
     * 连赢次数
     */
    private Integer conWinCount = 0;

    /**
     * 连输次数
     */
    private Integer conLossCount = 0;

    /**
     * 赢平均赔率
     */
    private Double winOdds = 0.00;

    /**
     * 输平均赔率
     */
    private Double lossOdds = 0.00;

    /**
     * 下单投入总金额
     */
    private Double betGold = 0.00;

    /**
     * 盈利金额
     */
    private Double winGold = 0.00;

    /**
     * 盈利
     */
    private Double profit = 0.00;

    /**
     * 输掉金额
     */
    private Double lossGold = 0.00;

    /**
     * 投资回报率
     */
    private Double roi = 0.00;

    /**
     * 排名得分
     */
    private Double score = 0.00;

    /**
     * 版本控制锁
     */
    private Integer lockerId = 0;

    private Integer lastLockerId;

    /**
     * 是否龙币
     */
    private boolean isLongbi;

    /**
     * 分表
     */
    private String table = "cbs_user_contest_statistics_week";

    public UserContestStatisticsWeek() {
	super();
    }

    public UserContestStatisticsWeek(Long userId, Integer year, Integer week, Integer betCount, Integer winCount,
	    Integer drawCount, Integer lossCount, Double winning, Integer conWinCount, Integer conLossCount, Double winOdds,
	    Double lossOdds, Double betGold, Double profit, Double winGold, Double lossGold, Double roi, Double score,
	    Integer lockerId) {
	super();
	this.userId = userId;
	this.year = year;
	this.week = week;
	this.betCount = betCount;
	this.winCount = winCount;
	this.drawCount = drawCount;
	this.lossCount = lossCount;
	this.profit = profit;
	this.winning = winning;
	this.conWinCount = conWinCount;
	this.conLossCount = conLossCount;
	this.winOdds = winOdds;
	this.lossOdds = lossOdds;
	this.betGold = betGold;
	this.winGold = winGold;
	this.lossGold = lossGold;
	this.roi = roi;
	this.score = score;
	this.lockerId = lockerId;
    }

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public Integer getYear() {
	return year;
    }

    public Double getProfit() {
	return profit;
    }

    public void setProfit(Double profit) {
	this.profit = profit;
    }

    public void setYear(Integer year) {
	this.year = year;
    }

    public boolean isLongbi() {
	return isLongbi;
    }

    public void setLongbi(boolean isLongbi) {
	this.isLongbi = isLongbi;
    }

    public String getTable() {
	return table;
    }

    public void setTable(String table) {
	this.table = table;
    }

    public Integer getWeek() {
	return week;
    }

    public void setWeek(Integer week) {
	this.week = week;
    }

    public Integer getLastLockerId() {
	return lastLockerId;
    }

    public void setLastLockerId(Integer lastLockerId) {
	this.lastLockerId = lastLockerId;
    }

    public Integer getBetCount() {
	return betCount;
    }

    public void setBetCount(Integer betCount) {
	this.betCount = betCount;
    }

    public Integer getWinCount() {
	return winCount;
    }

    public void setWinCount(Integer winCount) {
	this.winCount = winCount;
    }

    public Integer getDrawCount() {
	return drawCount;
    }

    public void setDrawCount(Integer drawCount) {
	this.drawCount = drawCount;
    }

    public Integer getLossCount() {
	return lossCount;
    }

    public void setLossCount(Integer lossCount) {
	this.lossCount = lossCount;
    }

    public Double getWinning() {
	return winning;
    }

    public void setWinning(Double winning) {
	this.winning = winning;
    }

    public Integer getConWinCount() {
	return conWinCount;
    }

    public void setConWinCount(Integer conWinCount) {
	this.conWinCount = conWinCount;
    }

    public Integer getConLossCount() {
	return conLossCount;
    }

    public void setConLossCount(Integer conLossCount) {
	this.conLossCount = conLossCount;
    }

    public Double getWinOdds() {
	return winOdds;
    }

    public void setWinOdds(Double winOdds) {
	this.winOdds = winOdds;
    }

    public Double getLossOdds() {
	return lossOdds;
    }

    public void setLossOdds(Double lossOdds) {
	this.lossOdds = lossOdds;
    }

    public Double getBetGold() {
	return betGold;
    }

    public void setBetGold(Double betGold) {
	this.betGold = betGold;
    }

    public Double getWinGold() {
	return winGold;
    }

    public void setWinGold(Double winGold) {
	this.winGold = winGold;
    }

    public Double getLossGold() {
	return lossGold;
    }

    public void setLossGold(Double lossGold) {
	this.lossGold = lossGold;
    }

    public Double getRoi() {
	return roi;
    }

    public void setRoi(Double roi) {
	this.roi = roi;
    }

    public Double getScore() {
	return score;
    }

    public void setScore(Double score) {
	this.score = score;
    }

    public Integer getLockerId() {
	return lockerId;
    }

    public void setLockerId(Integer lockerId) {
	this.lockerId = lockerId;
    }

}
