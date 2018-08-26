package com.lifeix.cbs.api.bean.user;

import com.lifeix.user.beans.Response;

/**
 * 推荐用户信息
 * 
 * @author lifeix
 * 
 */
public class CbsUserStarResponse implements Response {

    private static final long serialVersionUID = 2925157441814280659L;

    private Long id;

    /**
     * 主键
     */
    private Long user_id;

    /**
     * 排名
     */
    private Integer rank;

    /**
     * 赢率
     */
    private Double winning;

    /**
     * 展示计数
     */
    private int show_num;

    /**
     * 点击计数
     */
    private int hit_num;

    /**
     * 因子越高 概率越高
     */
    private int factor;

    /**
     * 隐藏标识
     */
    private boolean hide_flag;

    /**
     * 创建时间
     */
    private String create_time;

    /**
     * 用户信息
     */
    private CbsUserResponse user;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Long getUser_id() {
	return user_id;
    }

    public void setUser_id(Long user_id) {
	this.user_id = user_id;
    }

    public Integer getRank() {
	return rank;
    }

    public void setRank(Integer rank) {
	this.rank = rank;
    }

    public Double getWinning() {
	return winning;
    }

    public void setWinning(Double winning) {
	this.winning = winning;
    }

    public int getShow_num() {
	return show_num;
    }

    public void setShow_num(int show_num) {
	this.show_num = show_num;
    }

    public int getHit_num() {
	return hit_num;
    }

    public void setHit_num(int hit_num) {
	this.hit_num = hit_num;
    }

    public int getFactor() {
	return factor;
    }

    public void setFactor(int factor) {
	this.factor = factor;
    }

    public boolean isHide_flag() {
	return hide_flag;
    }

    public void setHide_flag(boolean hide_flag) {
	this.hide_flag = hide_flag;
    }

    public String getCreate_time() {
	return create_time;
    }

    public void setCreate_time(String create_time) {
	this.create_time = create_time;
    }

    public CbsUserResponse getUser() {
	return user;
    }

    public void setUser(CbsUserResponse user) {
	this.user = user;
    }

    @Override
    public String getObjectName() {
	return "user_star";
    }

}
