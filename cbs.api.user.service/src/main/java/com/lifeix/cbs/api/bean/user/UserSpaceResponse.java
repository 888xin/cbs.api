package com.lifeix.cbs.api.bean.user;

import com.lifeix.cbs.achieve.bean.achieve.UserAchieveResponse;
import com.lifeix.user.beans.CustomResponse;
import com.lifeix.user.beans.Response;

public class UserSpaceResponse implements Response {
    private static final long serialVersionUID = 1L;

    /**
     * 用户信息
     */
    private CbsUserResponse user;

    /**
     * 统计信息
     */
    private UserContestStatisticsResponse statistics;

    /**
     * 我关注的人
     */
    private int following_num;

    /**
     * 关注我的人
     */
    private int followed_num;

    /**
     * 关系类型 0 无关系 2 关注关系 4 相互关注关系
     */
    private Integer relationship_type;

    /**
     * 消息数量
     */
    CustomResponse msg;

    /**
     * 用户最近成就
     */
    private UserAchieveResponse achieve;

    /**
     * 用户拥有的积分
     */
    private Integer amount;

    public UserSpaceResponse() {
	super();
    }

    public CbsUserResponse getUser() {
	return user;
    }

    public void setUser(CbsUserResponse user) {
	this.user = user;
    }

    public UserContestStatisticsResponse getStatistics() {
	return statistics;
    }

    public void setStatistics(UserContestStatisticsResponse statistics) {
	this.statistics = statistics;
    }

    public int getFollowing_num() {
	return following_num;
    }

    public void setFollowing_num(int following_num) {
	this.following_num = following_num;
    }

    public Integer getRelationship_type() {
	return relationship_type;
    }

    public void setRelationship_type(Integer relationship_type) {
	this.relationship_type = relationship_type;
    }

    public CustomResponse getMsg() {
	return msg;
    }

    public void setMsg(CustomResponse msg) {
	this.msg = msg;
    }

    public int getFollowed_num() {
	return followed_num;
    }

    public void setFollowed_num(int followed_num) {
	this.followed_num = followed_num;
    }

    public UserAchieveResponse getAchieve() {
	return achieve;
    }

    public void setAchieve(UserAchieveResponse achieve) {
	this.achieve = achieve;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
