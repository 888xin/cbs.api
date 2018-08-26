package com.lifeix.cbs.content.dto.game;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lhx on 15-12-11 下午3:01
 *
 * @Description
 */
public class ZodiacAnimalBet implements Serializable {
    private Integer id ;
    private Integer gameId;//期号id',
    private Long userId ;//用户id',
    private String bet;//下单内容',
    private Integer betSum ;//下单金额',
    private Integer backSum;//中奖金额',
    private Boolean isLongbi ;
    private Integer status ; //处理状态（0未处理，1中奖  2未中奖）
    private Date createTime ; //创建时间
    private Date updateTime ;

    public Integer getBetSum() {
        return betSum;
    }

    public void setBetSum(Integer betSum) {
        this.betSum = betSum;
    }

    public Integer getBackSum() {
        return backSum;
    }

    public void setBackSum(Integer backSum) {
        this.backSum = backSum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBet() {
        return bet;
    }

    public void setBet(String bet) {
        this.bet = bet;
    }

    public Boolean getIsLongbi() {
        return isLongbi;
    }

    public void setIsLongbi(Boolean isLongbi) {
        this.isLongbi = isLongbi;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
