package com.lifeix.cbs.contest.bean.bet;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

import java.lang.reflect.Type;

/**
 * Created by lhx on 16-4-28 下午2:30
 *
 * @Description 大小球下单
 */
public class BetSizeResponse implements JsonSerializer<BetSizeResponse>, Response {

    private static final long serialVersionUID = 7594212243810274682L;
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
     * 盘口
     */
    private Double handicap;

    /**
     * 大球赔率
     */
    private Double big_roi;

    /**
     * 小球赔率
     */
    private Double tiny_roi;

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

    public Double getHandicap() {
        return handicap;
    }

    public void setHandicap(Double handicap) {
        this.handicap = handicap;
    }

    public Double getBig_roi() {
        return big_roi;
    }

    public void setBig_roi(Double big_roi) {
        this.big_roi = big_roi;
    }

    public Double getTiny_roi() {
        return tiny_roi;
    }

    public void setTiny_roi(Double tiny_roi) {
        this.tiny_roi = tiny_roi;
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
    public JsonElement serialize(BetSizeResponse betSizeResponse, Type type, JsonSerializationContext jsonSerializationContext) {
        return null;
    }

    @Override
    public String getObjectName() {
        return null;
    }
}
