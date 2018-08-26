package com.lifeix.cbs.content.bean.game;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.user.beans.Response;

import java.lang.reflect.Type;
import java.util.List;

public class ZodiacAnimalResponse  implements JsonSerializer<ZodiacAnimalResponse>, Response {
    private static final long serialVersionUID = 1L;
    /**
     * 上一期期号
     */
    private Integer pre_id;
    /**
     * 上一期结果
     */
    private String pre_lottery;
    
    /**
     * 用户信息
     */
    private CbsUserResponse user;
    
    /**
     * 本期期号
     */
    private Integer id;

    /**
     * 本期开奖结果
     */
    private String lottery;
    
    /**
     * 开始时间
     */
    private String start_time;

    /**
     * 开始时间
     */
    private String end_time;
    /**
     * 剩余多少秒
     */
    private Integer second;
    
    /**
     * 旺气生肖轮播广告（中奖最多）
     */
    private Object[] quality_ad;
    
    /**
     * 潜力生肖轮播广告(未中奖)
     */
    private Object[] potential_ad;
    
    /**
     * 中奖轮播广告
     */
    private Object[] winning_ad;

    /**
     * 最近中奖轮播广告
     */
    private Object[] recent_ad;


    /**
     * 用户龙筹
     */
    private Double gold;

    /**
     * 用户龙币
     */
    private Double money;

    private Integer[] bet_num;

    /**
     * 代金券
     */
    private List<Object[]> user_coupon ;

    public List<Object[]> getUser_coupon() {
        return user_coupon;
    }

    public void setUser_coupon(List<Object[]> user_coupon) {
        this.user_coupon = user_coupon;
    }

    public Object[] getRecent_ad() {
        return recent_ad;
    }

    public void setRecent_ad(Object[] recent_ad) {
        this.recent_ad = recent_ad;
    }

    public Integer[] getBet_num() {
        return bet_num;
    }

    public void setBet_num(Integer[] bet_num) {
        this.bet_num = bet_num;
    }

    public String getLottery() {
        return lottery;
    }

    public void setLottery(String lottery) {
        this.lottery = lottery;
    }

    public Integer getPre_id() {
        return pre_id;
    }

    public void setPre_id(Integer pre_id) {
        this.pre_id = pre_id;
    }

    public String getPre_lottery() {
        return pre_lottery;
    }

    public void setPre_lottery(String pre_lottery) {
        this.pre_lottery = pre_lottery;
    }

    public CbsUserResponse getUser() {
        return user;
    }

    public void setUser(CbsUserResponse user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getSecond() {
        return second;
    }

    public void setSecond(Integer second) {
        this.second = second;
    }

    public Object[] getQuality_ad() {
        return quality_ad;
    }

    public void setQuality_ad(Object[] quality_ad) {
        this.quality_ad = quality_ad;
    }

    public Object[] getPotential_ad() {
        return potential_ad;
    }

    public void setPotential_ad(Object[] potential_ad) {
        this.potential_ad = potential_ad;
    }

    public Object[] getWinning_ad() {
        return winning_ad;
    }

    public void setWinning_ad(Object[] winning_ad) {
        this.winning_ad = winning_ad;
    }

    public Double getGold() {
        return gold;
    }

    public void setGold(Double gold) {
        this.gold = gold;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(ZodiacAnimalResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }
    
    
}
