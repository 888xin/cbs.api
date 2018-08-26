package com.lifeix.cbs.contest.dto.bunch;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lhx on 16-5-16 下午5:36
 *
 * @Description
 */
public class BunchBet implements Serializable {

    private static final long serialVersionUID = 7381877201973447065L;

    private Long id ;

    /**
     * 用户Id
     */
    private Long userId ;
    /**
     * 比赛串id
     */
    private Long bunchId ;
    /**
     * 状态（0未结算 -1已结算 1结果已出）
     */
    private Integer status ;
    /**
     * 该注赢的场数
     */
    private Integer winNum ;
    /**
     * 下注选项（cbs_contest_bunch表里options对应的序号1：支持方1，options对应的序号2：支持方2）
     */
    private String support ;
    /**
     * 创建时间
     */
    private Date createTime;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getWinNum() {
        return winNum;
    }

    public void setWinNum(Integer winNum) {
        this.winNum = winNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBunchId() {
        return bunchId;
    }

    public void setBunchId(Long bunchId) {
        this.bunchId = bunchId;
    }

    public String getSupport() {
        return support;
    }

    public void setSupport(String support) {
        this.support = support;
    }

}
