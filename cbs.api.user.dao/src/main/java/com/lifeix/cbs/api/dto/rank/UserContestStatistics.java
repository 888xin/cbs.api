package com.lifeix.cbs.api.dto.rank;

import java.io.Serializable;

/**
 * 用户下单分析总表（包含所有的下单统计情况）
 * 
 * @author pengkw
 *
 */
public class UserContestStatistics implements Serializable {

    private static final long serialVersionUID = 3199410397346401752L;

    /**
     * 用户id
     */
    private Long userId;

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
     * 最大连赢次数
     */
    private Integer maxConWinCount = 0;

    /**
     * 连输次数
     */
    private Integer conLossCount = 0;

    /**
     * 最大连输次数
     */
    private Integer maxConLossCount = 0;

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
    private Integer score;

    /**
     * 综合排名
     */
    private Integer rank;

    /**
     * 版本控制锁
     */
    private Integer lockerId = 0;

    /**
     * 版本控制锁
     */
    private Integer lastLockerId;

    /**
     * 用户参与下注统计
     */
    private String contestCount;

    public UserContestStatistics() {
	super();
    }

    public UserContestStatistics(Long userId, Integer betCount, Integer winCount, Integer drawCount, Integer lossCount,
	    Double winning, Integer conWinCount, Integer maxConWinCount, Integer conLossCount, Integer maxConLossCount,
	    Double winOdds, Double lossOdds, Double betGold, Double winGold, Double lossGold, Double roi, Integer score,
	    Integer rank, Integer lockerId, String contestCount) {
	super();
	this.userId = userId;
	this.betCount = betCount;
	this.winCount = winCount;
	this.drawCount = drawCount;
	this.lossCount = lossCount;
	this.winning = winning;
	this.conWinCount = conWinCount;
	this.maxConWinCount = maxConWinCount;
	this.conLossCount = conLossCount;
	this.maxConLossCount = maxConLossCount;
	this.winOdds = winOdds;
	this.lossOdds = lossOdds;
	this.betGold = betGold;
	this.winGold = winGold;
	this.lossGold = lossGold;
	this.roi = roi;
	this.score = score;
	this.rank = rank;
	this.lockerId = lockerId;
	this.contestCount = contestCount;
    }

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public Integer getBetCount() {
	return betCount;
    }

    public Integer getLastLockerId() {
	return lastLockerId;
    }

    public void setLastLockerId(Integer lastLockerId) {
	this.lastLockerId = lastLockerId;
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

    public Integer getMaxConWinCount() {
	return maxConWinCount;
    }

    public void setMaxConWinCount(Integer maxConWinCount) {
	this.maxConWinCount = maxConWinCount;
    }

    public Integer getMaxConLossCount() {
	return maxConLossCount;
    }

    public void setMaxConLossCount(Integer maxConLossCount) {
	this.maxConLossCount = maxConLossCount;
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

    public Integer getScore() {
	return score;
    }

    public void setScore(Integer score) {
	this.score = score;
    }

    public Integer getRank() {
	return rank;
    }

    public void setRank(Integer rank) {
	this.rank = rank;
    }

    public Integer getLockerId() {
	return lockerId;
    }

    public void setLockerId(Integer lockerId) {
	this.lockerId = lockerId;
    }

    public String getContestCount() {
	return contestCount;
    }

    public void setContestCount(String contestCount) {
	this.contestCount = contestCount;
    }

}
