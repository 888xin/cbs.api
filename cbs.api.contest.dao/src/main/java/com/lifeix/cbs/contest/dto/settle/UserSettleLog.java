package com.lifeix.cbs.contest.dto.settle;

import java.util.Date;

public class UserSettleLog {
	
	/**
	 * 主键id
	 */
	private Long id;
	
	/**
	 * 用户id
	 */
	private Long userId;
	
	/**
	 * 支付logid
	 */
	private Long payLogId;
	
	/**
	 * 比赛类型 0 足球 1 篮球
	 */
	private Integer contestType;
	
	/**
	 * 比赛id
	 */
	private Long contestId;
	
	/**
	 * 是否龙币
	 */
	private boolean isLongbi;
	/**
	 * 玩法类型
	 */
	private Integer playId;
	
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
	private Long contentId;
	
	/**
	 * 比赛结束时间
	 */
	private Date contestTime;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	public UserSettleLog() {
		super();
	}

	public UserSettleLog(Long id, Long userId, Integer contestType,
			Long contestId, Integer playId, Integer support, Double odds, Double bet,
			Double back, Integer status, Long contentId, Date contestTime,
			Date createTime) {
		super();
		this.id = id;
		this.userId = userId;
		this.contestType = contestType;
		this.contestId = contestId;
		this.playId = playId;
		this.support = support;
		this.odds = odds;
		this.bet = bet;
		this.back = back;
		this.status = status;
		this.contentId = contentId;
		this.contestTime = contestTime;
		this.createTime = createTime;
	}

	public Integer getContestType() {
		return contestType;
	}

	public void setContestType(Integer contestType) {
		this.contestType = contestType;
	}

	public Long getId() {
		return id;
	}

	public boolean isLongbi() {
	    return isLongbi;
	}

	public void setLongbi(boolean isLongbi) {
	    this.isLongbi = isLongbi;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getContestId() {
		return contestId;
	}

	public void setContestId(Long contestId) {
		this.contestId = contestId;
	}

	public Integer getPlayId() {
		return playId;
	}

	public void setPlayId(Integer playId) {
		this.playId = playId;
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

	public Date getContestTime() {
		return contestTime;
	}

	public void setContestTime(Date contestTime) {
		this.contestTime = contestTime;
	}
	
	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getPayLogId() {
	    return payLogId;
	}

	public void setPayLogId(Long payLogId) {
	    this.payLogId = payLogId;
	}
	
}
