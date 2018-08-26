package com.lifeix.cbs.content.bean.frontpage;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

import java.lang.reflect.Type;

/**
 * Created by lhx on 15-11-30 下午1:58
 *
 * @Description
 */
public class FrontPageBetResponse implements JsonSerializer<FrontPageBetResponse>, Response {


    /**
     * 是否龙币
     */
    private boolean isLongbi;

    /**
     * 玩法名
     */
    private String play;

    /**
     * 下单选择
     */
    private String support;

    /**
     * 盘口
     */
    private Double handicap;

    /**
     * 赔率
     */
    private Double odds;

    /**
     * 下单金额
     */
    private Double bet;

    /**
     * 比赛状态 0比赛结果还不知道 | 1 胜 | 2 输 | 3 走盘
     */
    private Integer status;

    /**
     * 返回多少钱
     */
    private Double back;

    /**
     * 使用龙筹券的名额
     */
    private Integer coupon ;

    public Integer getCoupon() {
        return coupon;
    }

    public void setCoupon(Integer coupon) {
        this.coupon = coupon;
    }

    public boolean isLongbi() {
        return isLongbi;
    }

    public void setLongbi(boolean isLongbi) {
        this.isLongbi = isLongbi;
    }

    public String getPlay() {
        return play;
    }

    public void setPlay(String play) {
        this.play = play;
    }

    public String getSupport() {
        return support;
    }

    public void setSupport(String support) {
        this.support = support;
    }

    public Double getHandicap() {
        return handicap;
    }

    public void setHandicap(Double handicap) {
        this.handicap = handicap;
    }

    public Double getOdds() {
        return odds;
    }

    public void setOdds(Double odds) {
        this.odds = odds;
    }

    public Double getBet() {
        return bet;
    }

    public void setBet(Double bet) {
        this.bet = bet;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getBack() {
        return back;
    }

    public void setBack(Double back) {
        this.back = back;
    }

    @Override
    public JsonElement serialize(FrontPageBetResponse frontPageResponse, Type type, JsonSerializationContext jsonSerializationContext) {
        return null;
    }

    @Override
    public String getObjectName() {
        return null;
    }
}
