package com.lifeix.cbs.contest.dto.bunch;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lhx on 16-5-16 下午5:16
 *
 * @Description
 */
public class BunchContest implements Serializable {

    private static final long serialVersionUID = 941623083036057449L;

    private Long id;

    private String name ;

    private String image ;
    /**
     * 比赛串（json数组 数据有“序号、赛事ID、赛事类型、玩法”）
     */
    private String options;
    /**
     * 参加的代价（0表示不用支付）
     */
    private Integer cost;
    /**
     * 是否龙币支付
     */
    private Boolean longbi;
    /**
     * 状态（0未开始可下注 1比赛进行中或未结算，停止下注 2已有全部结果 -1已进行结算）
     */
    private Integer status;
    /**
     * 第一场比赛开始的时间
     */
    private Date startTime ;
    /**
     * 最后一场比赛的结束时间（估计）
     */
    private Date endTime;
    /**
     * 创建时间
     */
    private Date createTime ;
    /**
     * 开奖结果（json数组 数据有“序号、赛事ID、赛事类型、赢方”）
     */
    private String result ;

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Boolean getLongbi() {
        return longbi;
    }

    public void setLongbi(Boolean longbi) {
        this.longbi = longbi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
