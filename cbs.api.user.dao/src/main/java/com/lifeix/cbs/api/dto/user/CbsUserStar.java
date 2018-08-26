package com.lifeix.cbs.api.dto.user;

import java.io.Serializable;
import java.util.Date;

/**
 * 猜比赛推荐用户
 * 
 * @author lifeix-sz
 * 
 */
public class CbsUserStar implements Serializable {

    private static final long serialVersionUID = -4777966421014582021L;

    private Long id;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 排名
     */
    private Integer rank;

    /**
     * 赢率
     */
    private Double winning;

    /**
     * 展示计数
     */
    private int showNum;

    /**
     * 点击计数
     */
    private int hitNum;

    /**
     * 因子越高 概率越高
     */
    private int factor;

    /**
     * 隐藏标识
     */
    private boolean hideFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    public Long getId() {
	return id;
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

    public Integer getRank() {
	return rank;
    }

    public void setRank(Integer rank) {
	this.rank = rank;
    }

    public Double getWinning() {
	return winning;
    }

    public void setWinning(Double winning) {
	this.winning = winning;
    }

    public int getShowNum() {
	return showNum;
    }

    public void setShowNum(int showNum) {
	this.showNum = showNum;
    }

    public int getHitNum() {
	return hitNum;
    }

    public void setHitNum(int hitNum) {
	this.hitNum = hitNum;
    }

    public boolean isHideFlag() {
	return hideFlag;
    }

    public void setHideFlag(boolean hideFlag) {
	this.hideFlag = hideFlag;
    }

    public Date getCreateTime() {
	return createTime;
    }

    public void setCreateTime(Date createTime) {
	this.createTime = createTime;
    }

    public int getFactor() {
	return factor;
    }

    public void setFactor(int factor) {
	this.factor = factor;
    }

}
