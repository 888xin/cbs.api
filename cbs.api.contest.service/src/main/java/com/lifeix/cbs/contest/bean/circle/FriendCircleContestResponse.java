package com.lifeix.cbs.contest.bean.circle;

import java.io.Serializable;
import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.user.beans.Response;

/**
 * 竞猜中比赛信息
 * 
 * @author pengkw
 * 
 */
public class FriendCircleContestResponse implements JsonSerializer<FriendCircleContestResponse>, Response, Serializable {

    private static final long serialVersionUID = 6061395276382689532L;

    /**
     * 比赛id
     */
    private Long contestId;

    /**
     * 比赛类型 0 足球 | 1 篮球
     */
    private Integer type;

    /**
     * 联赛名
     */
    private String cn;

    /**
     * 联赛主色调
     */
    private String color;

    /**
     * 主队名
     */
    private String hn;

    /**
     * 主队logo
     */
    private String hl;

    /**
     * 主队分数
     */
    private Integer hs;

    /**
     * 客队名
     */
    private String an;

    /**
     * 客队logo
     */
    private String al;
    /**
     * 是否龙币
     */
    private boolean isLongbi;

    /**
     * 客队分数
     */
    private Integer as;

    /**
     * 比赛时间
     */
    private String time;

    /**
     * 玩法名
     */
    private String play;

    /**
     * 玩法id
     */
    private int playId;

    /**
     * 下单选择id
     */
    private int supportId;
    /**
     * 下单选择
     */
    private String support;

    /**
     * 盘口
     */
    private Double handicap;

    /**
     * 当前盘口
     */
    private Double curHandicap;

    /**
     * 赔率
     */
    private Double odds;

    /**
     * 当前赔率
     */
    private Double curOdds;

    /**
     * 下单金额
     */
    private Double bet;

    /**
     * 比赛状态 0比赛结果还不知道 | 1 胜 | 2 输 | 3 走盘
     */
    private Integer status;

    /**
     * 返回多少钱
     */
    private Double back;

    /**
     * 押押标题
     */
    private String title;

    /**
     * 押押下单描述
     */
    private String option;

    /**
     * 获胜选项
     */
    private Integer winner;

    /**
     * 获胜选项描述
     */
    private String winner_name;

    /**
     * 跟投标志位 true：盘口或赔率已改变 ;false：盘口或赔率未改变
     */
    private boolean followFlag;

    /**
     * 下单标志位 true:已投 ;false:未投
     */
    private boolean betFlag;

    public Long getContestId() {
	return contestId;
    }

    public void setContestId(Long contestId) {
	this.contestId = contestId;
    }

    public Integer getType() {
	return type;
    }

    public void setType(Integer type) {
	this.type = type;
    }

    public String getCn() {
	return cn;
    }

    public void setCn(String cn) {
	this.cn = cn;
    }

    public boolean isLongbi() {
	return isLongbi;
    }

    public void setLongbi(boolean isLongbi) {
	this.isLongbi = isLongbi;
    }

    public String getColor() {
	return color;
    }

    public void setColor(String color) {
	this.color = color;
    }

    public String getHn() {
	return hn;
    }

    public void setHn(String hn) {
	this.hn = hn;
    }

    public String getHl() {
	return hl;
    }

    public void setHl(String hl) {
	this.hl = hl;
    }

    public String getAn() {
	return an;
    }

    public void setAn(String an) {
	this.an = an;
    }

    public String getAl() {
	return al;
    }

    public void setAl(String al) {
	this.al = al;
    }

    public Integer getHs() {
	return hs;
    }

    public void setHs(Integer hs) {
	this.hs = hs;
    }

    public Integer getAs() {
	return as;
    }

    public void setAs(Integer as) {
	this.as = as;
    }

    public String getTime() {
	return time;
    }

    public void setTime(String time) {
	this.time = time;
    }

    public String getFormat_time() {
	return CbsTimeUtils.getSimpleFormatUTCString(time);
    }

    public String getPlay() {
	return play;
    }

    public void setPlay(String play) {
	this.play = play;
    }

    public int getPlayId() {
	return playId;
    }

    public void setPlayId(int playId) {
	this.playId = playId;
    }

    public int getSupportId() {
	return supportId;
    }

    public void setSupportId(int supportId) {
	this.supportId = supportId;
    }

    public String getSupport() {
	return support;
    }

    public void setSupport(String support) {
	this.support = support;
    }

    public Double getHandicap() {
	return handicap;
    }

    public void setHandicap(Double handicap) {
	this.handicap = handicap;
    }

    public Double getCurHandicap() {
	return curHandicap;
    }

    public void setCurHandicap(Double curHandicap) {
	this.curHandicap = curHandicap;
    }

    public Double getOdds() {
	return odds;
    }

    public void setOdds(Double odds) {
	this.odds = odds;
    }

    public Double getCurOdds() {
	return curOdds;
    }

    public void setCurOdds(Double curOdds) {
	this.curOdds = curOdds;
    }

    public Double getBet() {
	return bet;
    }

    public void setBet(Double bet) {
	this.bet = bet;
    }

    public Integer getStatus() {
	return status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    public Double getBack() {
	return back;
    }

    public void setBack(Double back) {
	this.back = back;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getOption() {
	return option;
    }

    public void setOption(String option) {
	this.option = option;
    }

    public Integer getWinner() {
	return winner;
    }

    public void setWinner(Integer winner) {
	this.winner = winner;
    }

    public String getWinner_name() {
	return winner_name;
    }

    public void setWinner_name(String winner_name) {
	this.winner_name = winner_name;
    }

    public boolean isFollowFlag() {
	return followFlag;
    }

    public void setFollowFlag(boolean followFlag) {
	this.followFlag = followFlag;
    }

    public boolean isBetFlag() {
	return betFlag;
    }

    public void setBetFlag(boolean betFlag) {
	this.betFlag = betFlag;
    }

    @Override
    public String getObjectName() {
	return "contest";
    }

    @Override
    public JsonElement serialize(FriendCircleContestResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

}
