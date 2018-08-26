package com.lifeix.cbs.content.bean.game;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.user.beans.Response;

public class ZodiacPlayResponse implements JsonSerializer<ZodiacPlayResponse>, Response {

    private static final long serialVersionUID = 1L;

    /**
     * 期号
     */
    private Integer game_id;
    /**
     * 开奖结果
     */
    private String lottery;

    /**
     * 
     * 用户信息
     */
    private CbsUserResponse user;
    /**
     * 下单哪些生肖
     */
    private String bet;

    /**
     * 下单金额
     */
    private Integer bet_sum;

    /**
     * 得到多少金额，没中奖为0
     */
    private Integer back_sum;

    /**
     * 是否龙币
     */
    private boolean longbi;
    /**
     * 状态
     */
    private int status;

    public Integer getBet_sum() {
	return bet_sum;
    }

    public void setBet_sum(Integer bet_sum) {
	this.bet_sum = bet_sum;
    }

    public Integer getBack_sum() {
	return back_sum;
    }

    public void setBack_sum(Integer back_sum) {
	this.back_sum = back_sum;
    }

    public Integer getGame_id() {
	return game_id;
    }

    public void setGame_id(Integer game_id) {
	this.game_id = game_id;
    }

    public String getLottery() {
	return lottery;
    }

    public void setLottery(String lottery) {
	this.lottery = lottery;
    }

    public CbsUserResponse getUser() {
	return user;
    }

    public void setUser(CbsUserResponse user) {
	this.user = user;
    }

    public String getBet() {
	return bet;
    }

    public void setBet(String bet) {
	this.bet = bet;
    }

    public boolean isLongbi() {
	return longbi;
    }

    public void setLongbi(boolean longbi) {
	this.longbi = longbi;
    }

    public int getStatus() {
	return status;
    }

    public void setStatus(int status) {
	this.status = status;
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(ZodiacPlayResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return null;
    }

}
