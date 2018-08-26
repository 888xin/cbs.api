package com.lifeix.cbs.contest.bean.bet;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

import java.lang.reflect.Type;

/**
 * Created by lhx on 16-5-5 下午3:34
 *
 * @Description
 */
public class BetOddevenResponse implements JsonSerializer<BetOddevenResponse>, Response {

    private static final long serialVersionUID = 3094830149470067656L;
    /**
     * 主键
     */
    private Long b_id;

    /**
     * 用户id
     */
    private Long user_id;

    /**
     * 比赛id
     */
    private Long contest_id;

    /**
     * 支持方
     */
    private Integer support;

    /**
     * 单数赔率
     */
    private Double odd_roi;

    /**
     * 双数赔率
     */
    private Double even_roi;

    /**
     * 下单金额
     */
    private Double bet;

    /**
     * 返还金额（包含本金）
     */
    private Double back;

    /**
     * 下单结果（初始|赢|输|走）
     */
    private Integer status;

    /**
     * 下单时间
     */
    private String create_time;

    public Long getB_id() {
        return b_id;
    }

    public void setB_id(Long b_id) {
        this.b_id = b_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getContest_id() {
        return contest_id;
    }

    public void setContest_id(Long contest_id) {
        this.contest_id = contest_id;
    }

    public Integer getSupport() {
        return support;
    }

    public void setSupport(Integer support) {
        this.support = support;
    }

    public Double getOdd_roi() {
        return odd_roi;
    }

    public void setOdd_roi(Double odd_roi) {
        this.odd_roi = odd_roi;
    }

    public Double getEven_roi() {
        return even_roi;
    }

    public void setEven_roi(Double even_roi) {
        this.even_roi = even_roi;
    }

    public Double getBet() {
        return bet;
    }

    public void setBet(Double bet) {
        this.bet = bet;
    }

    public Double getBack() {
        return back;
    }

    public void setBack(Double back) {
        this.back = back;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    @Override
    public JsonElement serialize(BetOddevenResponse betOddevenResponse, Type type, JsonSerializationContext jsonSerializationContext) {
        return null;
    }

    @Override
    public String getObjectName() {
        return null;
    }
}
