package com.lifeix.cbs.contest.bean.bunch;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by lhx on 16-5-17 上午9:28
 *
 * @Description 赛事串
 */
public class BunchContestResponse implements JsonSerializer<BunchContestResponse>, Response {

    private static final long serialVersionUID = 1427561297095989634L;

    private Long id;

    private String name ;

    private String image ;

    /**
     * 比赛串（json数组 数据有“序号、赛事ID、赛事类型、玩法”）
     */
    private List<BunchOptionsResponse> options;

    /**
     * 用户已经下的注
     */
    private Integer[] user_support;

    /**
     * 参加的代价（0表示不用支付）
     */
    private Integer cost;
    /**
     * 是否龙币支付
     */
    private Boolean longbi;
    /**
     * 状态（0未开始可下注 1比赛进行中或未结算，停止下注 -1已进行结算）
     */
    private Integer status;
    /**
     * 第一场比赛开始的时间
     */
    private String start_time ;
    /**
     * 最后一场比赛的结束时间（估计）
     */
    private String end_time;
    /**
     * 创建时间
     */
    private String create_time ;
    /**
     * 开奖结果（json数组 数据有“序号、赛事ID、赛事类型、赢方”）
     */
    private String result ;
    /**
     * 奖品
     */
    private List<BunchPrizeResponse> prize;

    /**
     * 用户拥有的龙币
     */
    private Double gold ;
    /**
     * 可用的筹码
     */
    private Long coupon_id ;

    /**
     * 参与人数
     */
    private Integer bet_num ;

    /**
     * 得奖状态
     */
    private Integer reward_status ;

    public Integer getReward_status() {
        return reward_status;
    }

    public void setReward_status(Integer reward_status) {
        this.reward_status = reward_status;
    }

    public Integer[] getUser_support() {
        return user_support;
    }

    public void setUser_support(Integer[] user_support) {
        this.user_support = user_support;
    }

    public Integer getBet_num() {
        return bet_num;
    }

    public void setBet_num(Integer bet_num) {
        this.bet_num = bet_num;
    }

    public Double getGold() {
        return gold;
    }

    public void setGold(Double gold) {
        this.gold = gold;
    }

    public Long getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(Long coupon_id) {
        this.coupon_id = coupon_id;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Boolean getLongbi() {
        return longbi;
    }

    public void setLongbi(Boolean longbi) {
        this.longbi = longbi;
    }

    public List<BunchPrizeResponse> getPrize() {
        return prize;
    }

    public void setPrize(List<BunchPrizeResponse> prize) {
        this.prize = prize;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<BunchOptionsResponse> getOptions() {
        return options;
    }

    public void setOptions(List<BunchOptionsResponse> options) {
        this.options = options;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public JsonElement serialize(BunchContestResponse bunchContestResponse, Type type, JsonSerializationContext jsonSerializationContext) {
        return null;
    }

    @Override
    public String getObjectName() {
        return null;
    }
}
