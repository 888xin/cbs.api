package com.lifeix.cbs.content.bean.shicai;
/**
 * 开奖记录、下单记录
 * @author wenhuans
 * 2015年11月6日 上午9:10:36
 * 
 */
public class ShicaiRecordResponse {

    /**
     * 期数
     */
    private Long sc_id;
    
    /**
     * 系统开奖结果/用户下单第一个数
     */
    private Integer first_num; 
    
    /**
     * 系统开奖结果/用户下单第二个数
     */
    private Integer second_num;
    
    /**
     * 奖池数额
     */
    private Long bed_point;
    
    /**
     * 一等奖单注
     */
    private Long first_prize;
    
    /**
     * 二等奖单注
     */
    private Long second_prize;
    
    /**
     * 用户下单的和值第一个数字
     */
    private Integer s_first_num; 
    
    /**
     * 用户下单的和值第一个数字赌注数额
     */
    private Long s_first_bet;
    
    /**
     * 用户下单的和值第二个数
     */
    private Integer s_second_num;
    
    /**
     * 用户下单的和值第二个数字赌注数额
     */
    private Long s_second_bet;
    
    /**
     * 用户第一个和值获奖情况
     */
    private String s_first_desc;
    
    /**
     * 用户第二个和值获奖情况
     */
    private String s_second_desc;
    
    /**
     * 用户下单的第一、二个数字的获奖情况/开奖记录描述
     */
    private String desc;

    public Long getSc_id() {
        return sc_id;
    }

    public void setSc_id(Long sc_id) {
        this.sc_id = sc_id;
    }

    public Integer getFirst_num() {
        return first_num;
    }

    public void setFirst_num(Integer first_num) {
        this.first_num = first_num;
    }

    public Integer getSecond_num() {
        return second_num;
    }

    public void setSecond_num(Integer second_num) {
        this.second_num = second_num;
    }

    public Long getBed_point() {
        return bed_point;
    }

    public void setBed_point(Long bed_point) {
        this.bed_point = bed_point;
    }

    public Long getFirst_prize() {
        return first_prize;
    }

    public void setFirst_prize(Long first_prize) {
        this.first_prize = first_prize;
    }

    public Long getSecond_prize() {
        return second_prize;
    }

    public void setSecond_prize(Long second_prize) {
        this.second_prize = second_prize;
    }

    public Integer getS_first_num() {
        return s_first_num;
    }

    public void setS_first_num(Integer s_first_num) {
        this.s_first_num = s_first_num;
    }

    public Integer getS_second_num() {
        return s_second_num;
    }

    public void setS_second_num(Integer s_second_num) {
        this.s_second_num = s_second_num;
    }

    public String getS_first_desc() {
        return s_first_desc;
    }

    public void setS_first_desc(String s_first_desc) {
        this.s_first_desc = s_first_desc;
    }

    public String getS_second_desc() {
        return s_second_desc;
    }

    public void setS_second_desc(String s_second_desc) {
        this.s_second_desc = s_second_desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Long getS_first_bet() {
        return s_first_bet;
    }

    public void setS_first_bet(Long s_first_bet) {
        this.s_first_bet = s_first_bet;
    }

    public Long getS_second_bet() {
        return s_second_bet;
    }

    public void setS_second_bet(Long s_second_bet) {
        this.s_second_bet = s_second_bet;
    }
    
}

