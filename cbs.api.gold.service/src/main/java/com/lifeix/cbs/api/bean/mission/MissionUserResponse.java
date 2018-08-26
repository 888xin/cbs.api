package com.lifeix.cbs.api.bean.mission;

import com.lifeix.cbs.api.bean.user.CbsUserResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lhx on 16-6-17 下午5:52
 *
 * @Description 用户任务
 */
public class MissionUserResponse implements Serializable {

    private static final long serialVersionUID = -4491941562638929733L;

    private Long userId;

    /**
     * 用户拥有的积分
     */
    private Integer amount;

    /**
     * 积分值
     */
    private Integer value;

    /**
     * 用户
     */
    private CbsUserResponse user ;

    /**
     * 任务完成情况
     */
    private List<MissionResponse> missions ;

    /**
     * 积分兑换龙筹
     */
    private List<PointResponse> point_list ;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public CbsUserResponse getUser() {
        return user;
    }

    public void setUser(CbsUserResponse user) {
        this.user = user;
    }

    public List<PointResponse> getPoint_list() {
        return point_list;
    }

    public void setPoint_list(List<PointResponse> point_list) {
        this.point_list = point_list;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public List<MissionResponse> getMissions() {
        return missions;
    }

    public void setMissions(List<MissionResponse> missions) {
        this.missions = missions;
    }
}
