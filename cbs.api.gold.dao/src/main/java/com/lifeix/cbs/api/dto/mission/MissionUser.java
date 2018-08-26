package com.lifeix.cbs.api.dto.mission;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lhx on 16-6-17 下午5:52
 *
 * @Description 用户任务
 */
public class MissionUser implements Serializable {

    private static final long serialVersionUID = -5912403356117483386L;

    private Long id;

    private Long userId;

    /**
     * 首次任务值
     */
    private Integer value;

    /**
     * 用户拥有的积分
     */
    private Integer amount;

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

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
