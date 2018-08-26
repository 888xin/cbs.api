package com.lifeix.cbs.content.bean.shicai;
/**
 * @author wenhuans
 * 2015年11月4日 下午5:46:03
 * 
 */
public class PrizePoolResponse {

    /**
     * 第一个数字
     */
    private Integer first_num;
    
    /**
     * 第二个数字
     */
    private Integer second_num;
    
    /**
     * 用户赌注
     */
    private Long bet;
    
    /**
     * 第一个数字对应的注数
     */
    private Long first_count;
    
    /**
     * 第二个数字对应的注数
     */
    private Long second_count;
    
    /**
     * 用户总注数
     */
    private Long total_count;
    
    /**
     * 该奖池中的总赌注
     */
    private Long total_money;

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

    public Long getFirst_count() {
        return first_count;
    }

    public void setFirst_count(Long first_count) {
        this.first_count = first_count;
    }

    public Long getSecond_count() {
        return second_count;
    }

    public void setSecond_count(Long second_count) {
        this.second_count = second_count;
    }

    public Long getTotal_count() {
        return total_count;
    }

    public void setTotal_count(Long total_count) {
        this.total_count = total_count;
    }

    public Long getTotal_money() {
        return total_money;
    }

    public void setTotal_money(Long total_money) {
        this.total_money = total_money;
    }

    public Long getBet() {
        return bet;
    }

    public void setBet(Long bet) {
        this.bet = bet;
    }

}

