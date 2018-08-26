package com.lifeix.cbs.contest.dto.bet;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lhx on 16-7-1 上午10:11
 *
 * @Description
 */
public class BetRepair implements Serializable {

    private static final long serialVersionUID = -608629626105050857L;

    private Long id ;

    private Long betId ;
    /**
     * 玩法
     */
    private Integer playType ;
    /**
     * 支持方
     */
    private Integer support ;
    /**
     * 下注额
     */
    private Integer bet ;
    /**
     * 修改前的赔率
     */
    private Double oldRoi ;
    /**
     * 修改后的赔率
     */
    private Double newRoi ;
    /**
     * 理由
     */
    private String reason ;
    /**
     * 改变的金额
     */
    private Double changeMoney ;

    private Date createTime ;

    public Double getChangeMoney() {
        return changeMoney;
    }

    public void setChangeMoney(Double changeMoney) {
        this.changeMoney = changeMoney;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBetId() {
        return betId;
    }

    public void setBetId(Long betId) {
        this.betId = betId;
    }

    public Integer getPlayType() {
        return playType;
    }

    public void setPlayType(Integer playType) {
        this.playType = playType;
    }

    public Integer getSupport() {
        return support;
    }

    public void setSupport(Integer support) {
        this.support = support;
    }

    public Integer getBet() {
        return bet;
    }

    public void setBet(Integer bet) {
        this.bet = bet;
    }

    public Double getOldRoi() {
        return oldRoi;
    }

    public void setOldRoi(Double oldRoi) {
        this.oldRoi = oldRoi;
    }

    public Double getNewRoi() {
        return newRoi;
    }

    public void setNewRoi(Double newRoi) {
        this.newRoi = newRoi;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
