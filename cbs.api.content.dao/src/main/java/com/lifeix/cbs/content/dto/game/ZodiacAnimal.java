package com.lifeix.cbs.content.dto.game;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lhx on 15-12-11 上午11:37
 *
 * @Description
 */
public class ZodiacAnimal implements Serializable {

    private Integer id;//时彩id
    private String lottery;//开奖结果
    private Integer status;//处理状态（0未处理，1已处理）
    private Date startTime; //开始时间
    private Date endTime; //截止时间
    private Date updateTime; //更新时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLottery() {
        return lottery;
    }

    public void setLottery(String lottery) {
        this.lottery = lottery;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
