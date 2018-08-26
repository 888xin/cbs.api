package com.lifeix.cbs.api.bean.user;

import com.lifeix.user.beans.Response;

/**
 * 用户统计相关信息
 * 
 * @author huiy
 * 
 */
public class UserStatisticsResponse implements Response {
    private static final long serialVersionUID = 1L;

    /**
     * 用户信息
     */
    private CbsUserResponse user;

    /**
     * 用户id
     */
    private Long user_id;

    /**
     * 资金数
     */
    private Double fund = 0.0;

    /**
     * 总竞猜次数
     */
    private Integer bet_count = 0;

    /**
     * 赢次数
     */
    private Integer win_count = 0;

    /**
     * 走次数
     */
    private Integer draw_count = 0;

    private boolean isLongbi;
    /**
     * 输次数
     */
    private Integer loss_count = 0;

    /**
     * 胜率
     */
    private Double winning = 0.0;

    /**
     * 赢的平均赔率
     */
    private Double win_odds = 0.0;

    /**
     * 输的平均赔率
     */
    private Double loss_odds = 0.0;

    /**
     * 下单总金额
     */
    private Double bet_gold = 0.0;

    /**
     * 赢多少钱
     */
    private Double win_gold = 0.0;

    /**
     * 单天最终的输赢金币额 正负值
     */
    private Double final_gold = 0.0;

    /**
     * 输了多少钱
     */
    private Double loss_gold = 0.0;

    /**
     * 投资回报率
     */
    private Double roi = 0.0;

    /**
     * 排名
     */
    private Integer rank = 0;

    private String time;

    public UserStatisticsResponse() {
	super();
    }

    public CbsUserResponse getUser() {
	return user;
    }

    public void setUser(CbsUserResponse user) {
	this.user = user;
    }

    public Long getUser_id() {
	return user_id;
    }

    public void setUser_id(Long user_id) {
	this.user_id = user_id;
    }

    public String getTime() {
	return time;
    }

    public Double getFinal_gold() {
	return final_gold;
    }

    public void setFinal_gold(Double final_gold) {
	this.final_gold = final_gold;
    }

    public void setTime(String time) {
	this.time = time;
    }

    public Integer getBet_count() {
	return bet_count;
    }

    public boolean isLongbi() {
	return isLongbi;
    }

    public void setLongbi(boolean isLongbi) {
	this.isLongbi = isLongbi;
    }

    public void setBet_count(Integer bet_count) {
	this.bet_count = bet_count;
    }

    public Integer getWin_count() {
	return win_count;
    }

    public void setWin_count(Integer win_count) {
	this.win_count = win_count;
    }

    public Integer getDraw_count() {
	return draw_count;
    }

    public void setDraw_count(Integer draw_count) {
	this.draw_count = draw_count;
    }

    public Integer getLoss_count() {
	return loss_count;
    }

    public void setLoss_count(Integer loss_count) {
	this.loss_count = loss_count;
    }

    public Double getWinning() {
	return winning;
    }

    public void setWinning(Double winning) {
	this.winning = winning;
    }

    public Double getWin_odds() {
	return win_odds;
    }

    public void setWin_odds(Double win_odds) {
	this.win_odds = win_odds;
    }

    public Double getLoss_odds() {
	return loss_odds;
    }

    public void setLoss_odds(Double loss_odds) {
	this.loss_odds = loss_odds;
    }

    public Double getBet_gold() {
	return bet_gold;
    }

    public void setBet_gold(Double bet_gold) {
	this.bet_gold = bet_gold;
    }

    public Double getWin_gold() {
	return win_gold;
    }

    public void setWin_gold(Double win_gold) {
	this.win_gold = win_gold;
    }

    public Double getLoss_gold() {
	return loss_gold;
    }

    public void setLoss_gold(Double loss_gold) {
	this.loss_gold = loss_gold;
    }

    public Double getRoi() {
	return roi;
    }

    public void setRoi(Double roi) {
	this.roi = roi;
    }

    public Integer getRank() {
	return rank;
    }

    public void setRank(Integer rank) {
	this.rank = rank;
    }

    public Double getFund() {
	return fund;
    }

    public void setFund(Double fund) {
	this.fund = fund;
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
