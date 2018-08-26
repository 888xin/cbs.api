package com.lifeix.cbs.api.bean.user;

import com.lifeix.user.beans.Response;

public class UserContestStatisticsResponse implements Response {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Long user_id;

    /**
     * 龙币
     */
    private Double gold = 0.00;

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

    /**
     * 输次数
     */
    private Integer loss_count = 0;

    /**
     * 胜率
     */
    private Double winning = 0.00;

    /**
     * 连赢次数
     */
    private Integer con_win_count = 0;

    /**
     * 最高连赢次数
     */
    private Integer max_con_win_count = 0;

    /**
     * 连黑次数
     */
    private Integer con_loss_count = 0;

    /**
     * 最高连黑次数
     */
    private Integer max_con_loss_count = 0;

    /**
     * 赢的平均赔率
     */
    private Double win_odds = 0.00;

    /**
     * 输的平均赔率
     */
    private Double loss_odds = 0.00;

    /**
     * 投资回报率
     */
    private Double roi = 0.00;

    private Integer rank = 0;

    /**
     * 用户参与下注统计
     */
    private String contest_count;

    public UserContestStatisticsResponse() {
	super();
    }

    public Long getUser_id() {
	return user_id;
    }

    public void setUser_id(Long user_id) {
	this.user_id = user_id;
    }

    public Double getGold() {
	return gold;
    }

    public void setGold(Double gold) {
	this.gold = gold;
    }

    public Integer getBet_count() {
	return bet_count;
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

    public Integer getCon_win_count() {
	return con_win_count;
    }

    public void setCon_win_count(Integer con_win_count) {
	this.con_win_count = con_win_count;
    }

    public Integer getMax_con_win_count() {
	return max_con_win_count;
    }

    public void setMax_con_win_count(Integer max_con_win_count) {
	this.max_con_win_count = max_con_win_count;
    }

    public Integer getCon_loss_count() {
	return con_loss_count;
    }

    public void setCon_loss_count(Integer con_loss_count) {
	this.con_loss_count = con_loss_count;
    }

    public Integer getMax_con_loss_count() {
	return max_con_loss_count;
    }

    public void setMax_con_loss_count(Integer max_con_loss_count) {
	this.max_con_loss_count = max_con_loss_count;
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

    @Override
    public String getObjectName() {
	return null;
    }

    public String getContest_count() {
	return contest_count;
    }

    public void setContest_count(String contest_count) {
	this.contest_count = contest_count;
    }

}
