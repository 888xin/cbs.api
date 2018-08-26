package com.lifeix.cbs.contest.dto.settle;

import java.util.Date;

/**
 * .
 * 
 * 赛事结算任务表
 */
public class CbsSettle {

    /**
     * 结算ID
     */
    private Long settleId;

    /**
     * 赛事类型
     */
    private Integer type;

    /**
     * 赛事ID
     */
    private Long contestId;

    /**
     * 0 初始化 1 成功 2 失败
     */
    private Integer closeFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 预结算时间
     */
    private Date settleTime;

    public Long getSettleId() {
	return settleId;
    }

    public void setSettleId(Long settleId) {
	this.settleId = settleId;
    }

    public Integer getType() {
	return type;
    }

    public void setType(Integer type) {
	this.type = type;
    }

    public Long getContestId() {
	return contestId;
    }

    public void setContestId(Long contestId) {
	this.contestId = contestId;
    }

    public Integer getCloseFlag() {
	return closeFlag;
    }

    public void setCloseFlag(Integer closeFlag) {
	this.closeFlag = closeFlag;
    }

    public Date getCreateTime() {
	return createTime;
    }

    public void setCreateTime(Date createTime) {
	this.createTime = createTime;
    }

    public Date getSettleTime() {
	return settleTime;
    }

    public void setSettleTime(Date settleTime) {
	this.settleTime = settleTime;
    }

}
