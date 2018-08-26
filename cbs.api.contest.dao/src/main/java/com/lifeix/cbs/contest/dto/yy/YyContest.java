package com.lifeix.cbs.contest.dto.yy;

import java.io.Serializable;
import java.util.Date;

/**
 * 押押：赛事信息
 * 
 * @author lifeix
 * 
 */
public class YyContest implements Serializable {

    private static final long serialVersionUID = 7461570007079109865L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 图片集
     */
    private String images;

    /**
     * 描述
     */
    private String text;

    /**
     * 选项
     */
    private String options;

    /**
     * 分类Id
     */
    private Long cupId;

    /**
     * 分类名称
     */
    private String cupName;

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
    private Date endTime;

    /**
     * 比赛状态
     */
    private int status;

    /**
     * 胜出方
     */
    private int winner;

    /**
     * 结算状态
     */
    private boolean settle;

    /**
     * 是否龙币
     */
    private boolean isLongbi;

    /**
     * 龙筹金额
     */
    private int coupon;

    /**
     * 隐藏标识
     */
    private Boolean hideFlag;

    /**
     * 初始统计
     */
    private int initCount;

    /**
     * 展示类型
     */
    private int showType;

    /**
     * 展示标识
     */
    private Boolean activityFlag;

    /**
     * 列表图片
     */
    private String listImage ;

    public String getListImage() {
        return listImage;
    }

    public void setListImage(String listImage) {
        this.listImage = listImage;
    }

    public Boolean getActivityFlag() {
        return activityFlag;
    }

    public void setActivityFlag(Boolean activityFlag) {
        this.activityFlag = activityFlag;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getImages() {
	return images;
    }

    public void setImages(String images) {
	this.images = images;
    }

    public String getText() {
	return text;
    }

    public void setText(String text) {
	this.text = text;
    }

    public String getOptions() {
	return options;
    }

    public void setOptions(String options) {
	this.options = options;
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

    public Date getEndTime() {
	return endTime;
    }

    public void setEndTime(Date endTime) {
	this.endTime = endTime;
    }

    public int getStatus() {
	return status;
    }

    public void setStatus(int status) {
	this.status = status;
    }

    public int getWinner() {
	return winner;
    }

    public void setWinner(int winner) {
	this.winner = winner;
    }

    public boolean isSettle() {
	return settle;
    }

    public void setSettle(boolean settle) {
	this.settle = settle;
    }

    public boolean isLongbi() {
	return isLongbi;
    }

    public void setLongbi(boolean isLongbi) {
	this.isLongbi = isLongbi;
    }

    public Boolean getHideFlag() {
	return hideFlag;
    }

    public void setHideFlag(Boolean hideFlag) {
	this.hideFlag = hideFlag;
    }

    public int getCoupon() {
	return coupon;
    }

    public void setCoupon(int coupon) {
	this.coupon = coupon;
    }

    public int getInitCount() {
	return initCount;
    }

    public void setInitCount(int initCount) {
	this.initCount = initCount;
    }

}
