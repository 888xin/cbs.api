package com.lifeix.cbs.api.service.gold;

import java.util.Date;

import com.lifeix.cbs.api.bean.gold.GoldStatisticListResponse;
import com.lifeix.cbs.api.bean.gold.GoldStatisticResponse;
import com.lifeix.exception.service.L99IllegalParamsException;

public interface GoldStatisticService {

    /**
     * 插入统计数据
     * 
     * @param income
     * @param outlay
     * @param inCounts
     * @param outCounts
     * @return
     * @throws L99IllegalParamsException 
     */
    public boolean insert(Double income, Double outlay, Long inCounts, Long outCounts, Integer year, Integer day) throws L99IllegalParamsException;
    
    /**
     * 获取当天的系统收入支出统计
     * @param time
     * @return
     * @throws L99IllegalParamsException 
     */
    public GoldStatisticResponse findByTime(Date time) throws L99IllegalParamsException;
    
    /**
     * 获取一段时间内的的系统收入支出统计
     * @param begin
     * @param end
     * @return
     * @throws L99IllegalParamsException 
     */
    public GoldStatisticListResponse findByBetweenTime(Date begin,Date end) throws L99IllegalParamsException;

}
