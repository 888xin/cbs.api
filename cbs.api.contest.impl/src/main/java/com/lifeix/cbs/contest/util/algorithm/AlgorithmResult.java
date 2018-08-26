package com.lifeix.cbs.contest.util.algorithm;

/**
 * 算法结果
 * 
 * @author roi
 * 
 */
public class AlgorithmResult {

    /**
     * 下单单ID
     */
    private Long oddsId;

    /**
     * 下单额
     */
    private double amount;

    /**
     * 下单赔率
     */
    private double odds;

    /**
     * 利润
     */
    private double profit;

    /**
     * 结果 赢 1 输 2 走盘 -1
     */
    private int status;

    public Long getOddsId() {
	return oddsId;
    }

    public void setOddsId(Long oddsId) {
	this.oddsId = oddsId;
    }

    public double getAmount() {
	return amount;
    }

    public void setAmount(double amount) {
	this.amount = amount;
    }

    public double getProfit() {
	return profit;
    }

    public void setProfit(double profit) {
	this.profit = profit;
    }

    public int getStatus() {
	return status;
    }

    public void setStatus(int status) {
	this.status = status;
    }

    public double getOdds() {
	return odds;
    }

    public void setOdds(double odds) {
	this.odds = odds;
    }

}