package com.lifeix.cbs.contest.dto.fb;

import java.io.Serializable;
import java.util.Date;

/**
 * 足球：赛事信息
 * 
 * @author Peter
 * 
 */
public class FbContest implements Serializable {

    private static final long serialVersionUID = -4748388702492277592L;

    /**
     * 主键
     */
    private Long contestId;

    /**
     * 第三方id
     */
    private Long targetId;

    /**
     * 联赛id
     */
    private Long cupId;

    /**
     * 名称
     */
    private String cupName;

    /**
     * 名称
     */
    private String color;

    /**
     * 主场球队id
     */
    private Long homeTeam;

    /**
     * 主场分数
     */
    private int homeScores;

    /**
     * 主队排名
     */
    private String homeRank;

    /**
     * 客场球队id
     */
    private Long awayTeam;

    /**
     * 客场分数
     */
    private int awayScores;

    /**
     * 客队排名
     */
    private String awayRank;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 开场时间
     */
    private Date openTime;

    /**
     * 比赛状态
     */
    private int status;

    /**
     * 是否龙币
     */
    private boolean isLongbi;
    /**
     * 结算状态，默认为0--未结算
     */
    private int settle;

    /**
     * 赔率玩法，1 2 4 8 16
     */
    private int oddsType;

    /**
     * 重要比赛标识
     */
    private Boolean belongFive;

    /**
     * 联赛层级
     */
    private int level = 0;

    /**
     * 下单统计
     */
    private int betCount = 0;

    /**
     * 比分锁定
     */
    private Boolean lockFlag;

    /**
     * im房间Id
     */
    private Long roomId;

    /**
     * 赛事相关数据持有标志位
     */
    private Integer extFlag;

    /**
     * 比赛最终结果
     */
    private String finalResult;
    /**
     * 下注比例人数
     */
    private String betRatio;

    public String getBetRatio() {
        return betRatio;
    }

    public void setBetRatio(String betRatio) {
        this.betRatio = betRatio;
    }

    public String getHomeRank() {
	return homeRank;
    }

    public void setHomeRank(String homeRank) {
	this.homeRank = homeRank;
    }

    public String getAwayRank() {
	return awayRank;
    }

    public void setAwayRank(String awayRank) {
	this.awayRank = awayRank;
    }

    public int getOddsType() {
	return oddsType;
    }

    public void setOddsType(int oddsType) {
	this.oddsType = oddsType;
    }

    public int getSettle() {
	return settle;
    }

    public void setSettle(int settle) {
	this.settle = settle;
    }

    public int getLevel() {
	return level;
    }

    public void setLevel(int level) {
	this.level = level;
    }

    public Long getContestId() {
	return contestId;
    }

    public void setContestId(Long contestId) {
	this.contestId = contestId;
    }

    public Long getTargetId() {
	return targetId;
    }

    public void setTargetId(Long targetId) {
	this.targetId = targetId;
    }

    public Long getCupId() {
	return cupId;
    }

    public void setCupId(Long cupId) {
	this.cupId = cupId;
    }

    public String getCupName() {
	return cupName;
    }

    public void setCupName(String cupName) {
	this.cupName = cupName;
    }

    public Long getHomeTeam() {
	return homeTeam;
    }

    public void setHomeTeam(Long homeTeam) {
	this.homeTeam = homeTeam;
    }

    public int getHomeScores() {
	return homeScores;
    }

    public void setHomeScores(int homeScores) {
	this.homeScores = homeScores;
    }

    public Long getAwayTeam() {
	return awayTeam;
    }

    public void setAwayTeam(Long awayTeam) {
	this.awayTeam = awayTeam;
    }

    public int getAwayScores() {
	return awayScores;
    }

    public void setAwayScores(int awayScores) {
	this.awayScores = awayScores;
    }

    public Date getCreateTime() {
	return createTime;
    }

    public void setCreateTime(Date createTime) {
	this.createTime = createTime;
    }

    public Date getStartTime() {
	return startTime;
    }

    public void setStartTime(Date startTime) {
	this.startTime = startTime;
    }

    public Date getOpenTime() {
	return openTime;
    }

    public void setOpenTime(Date openTime) {
	this.openTime = openTime;
    }

    public int getStatus() {
	return status;
    }

    public void setStatus(int status) {
	this.status = status;
    }

    public Boolean getBelongFive() {
	return belongFive;
    }

    public void setBelongFive(Boolean belongFive) {
	this.belongFive = belongFive;
    }

    public String getColor() {
	return color;
    }

    public void setColor(String color) {
	this.color = color;
    }

    public int getBetCount() {
	return betCount;
    }

    public void setBetCount(int betCount) {
	this.betCount = betCount;
    }

    public Boolean getLockFlag() {
	return lockFlag;
    }

    public void setLockFlag(Boolean lockFlag) {
	this.lockFlag = lockFlag;
    }

    public boolean isLongbi() {
	return isLongbi;
    }

    public void setLongbi(boolean isLongbi) {
	this.isLongbi = isLongbi;
    }

    public Long getRoomId() {
	return roomId;
    }

    public void setRoomId(Long roomId) {
	this.roomId = roomId;
    }

    public Integer getExtFlag() {
	return extFlag;
    }

    public void setExtFlag(Integer extFlag) {
	this.extFlag = extFlag;
    }

    public String getFinalResult() {
	return finalResult;
    }

    public void setFinalResult(String finalResult) {
	this.finalResult = finalResult;
    }

}
