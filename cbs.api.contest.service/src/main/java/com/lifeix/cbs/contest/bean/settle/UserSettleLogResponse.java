package com.lifeix.cbs.contest.bean.settle;

import com.lifeix.cbs.contest.bean.fb.ContestResponse;
import com.lifeix.user.beans.Response;

public class UserSettleLogResponse implements Response {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long user_id;
    /**
     * 龙号
     */
    private Long long_no;

    /**
     * name
     */
    private String name;
    /**
     * 龙号
     */
    private Long pay_log_id;
    /**
     * 比赛类型 0 足球 1 篮球
     */
    private Integer contest_type;

    /**
     * 比赛id
     */
    private Long contest_id;
    /**
     * 是否龙币
     */
    private boolean longbi;
    /**
     * 玩法类型
     */
    private Integer play_id;

    /**
     * 下单选择
     */
    private Integer support;

    /**
     * 赔率
     */
    private Double odds;

    /**
     * 下单金额
     */
    private Double bet;

    /**
     * 赢多少钱
     */
    private Double win_gold = 0.0;

    /**
     * 返还金额
     */
    private Double back;

    /**
     * 下单结果（赢|输|走）
     */
    private Integer status;

    /**
     * 内容id
     */
    private Long content_id;

    /**
     * 比赛结束时间
     */
    private String contest_time;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 赛事数据
     */
    private ContestResponse contest;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Long getUser_id() {
	return user_id;
    }

    public void setUser_id(Long user_id) {
	this.user_id = user_id;
    }

    public Integer getContest_type() {
	return contest_type;
    }

    public Double getWin_gold() {
        return win_gold;
    }

    public Long getPay_log_id() {
        return pay_log_id;
    }

    public void setPay_log_id(Long pay_log_id) {
        this.pay_log_id = pay_log_id;
    }

    public void setWin_gold(Double win_gold) {
        this.win_gold = win_gold;
    }

    public ContestResponse getContest() {
	return contest;
    }

    public void setContest(ContestResponse contest) {
	this.contest = contest;
    }

    public void setContest_type(Integer contest_type) {
	this.contest_type = contest_type;
    }

    public boolean isLongbi() {
	return longbi;
    }

    public void setLongbi(boolean isLongbi) {
	this.longbi = isLongbi;
    }

    public Long getContest_id() {
	return contest_id;
    }

    public void setContest_id(Long contest_id) {
	this.contest_id = contest_id;
    }

    public Integer getPlay_id() {
	return play_id;
    }

    public void setPlay_id(Integer play_id) {
	this.play_id = play_id;
    }

    public Integer getSupport() {
	return support;
    }

    public void setSupport(Integer support) {
	this.support = support;
    }

    public Double getOdds() {
	return odds;
    }

    public void setOdds(Double odds) {
	this.odds = odds;
    }

    public Double getBet() {
	return bet;
    }

    public void setBet(Double bet) {
	this.bet = bet;
    }

    public Double getBack() {
	return back;
    }

    public void setBack(Double back) {
	this.back = back;
    }

    public Integer getStatus() {
	return status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    public Long getContent_id() {
	return content_id;
    }

    public void setContent_id(Long content_id) {
	this.content_id = content_id;
    }

    public String getContest_time() {
	return contest_time;
    }

    public void setContest_time(String contest_time) {
	this.contest_time = contest_time;
    }

    public String getCreateTime() {
	return createTime;
    }

    public void setCreateTime(String createTime) {
	this.createTime = createTime;
    }

    public Long getLong_no() {
        return long_no;
    }

    public void setLong_no(Long long_no) {
        this.long_no = long_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
