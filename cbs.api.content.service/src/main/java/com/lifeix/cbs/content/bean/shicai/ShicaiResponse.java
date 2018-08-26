package com.lifeix.cbs.content.bean.shicai;

import java.util.Map;

import com.lifeix.user.beans.Response;



/**
 * @author wenhuans
 * 2015年11月4日 上午10:08:44
 * 
 */
public class ShicaiResponse implements Response{

    /**
     * 
     */
    private static final long serialVersionUID = 1450531814088822623L;

    /**
     * 当前正在进行的期数
     */
    private Long sc_id;
    
    /**
     * 剩余时间
     */
    private Map<String, Long> left_time;
    
    /**
     * 总共押的赌注
     */
    private Long total_bet;
    
    /**
     * 上期结果
     */
    private Map<String, Integer> last_result;
    
    /**
     * 奖池一区相关数值
     */
    private PrizePoolResponse pool_one;
    
    /**
     * 奖池二区相关数值
     */
    private PrizePoolResponse pool_two;
    
    private Integer new_count;
    
    public Long getSc_id() {
        return sc_id;
    }

    public void setSc_id(Long sc_id) {
        this.sc_id = sc_id;
    }

    public Map<String, Integer> getLast_result() {
        return last_result;
    }

    public void setLast_result(Map<String, Integer> last_result) {
        this.last_result = last_result;
    }

    public PrizePoolResponse getPool_one() {
        return pool_one;
    }

    public void setPool_one(PrizePoolResponse pool_one) {
        this.pool_one = pool_one;
    }

    public PrizePoolResponse getPool_two() {
        return pool_two;
    }

    public void setPool_two(PrizePoolResponse pool_two) {
        this.pool_two = pool_two;
    }

    public Long getTotal_bet() {
        return total_bet;
    }

    public void setTotal_bet(Long total_bet) {
        this.total_bet = total_bet;
    }

    public Map<String, Long> getLeft_time() {
        return left_time;
    }

    public void setLeft_time(Map<String, Long> left_time) {
        this.left_time = left_time;
    }

    public Integer getNew_count() {
        return new_count;
    }

    public void setNew_count(Integer new_count) {
        this.new_count = new_count;
    }

    @Override
    public String getObjectName() {
	return null;
    }


}

