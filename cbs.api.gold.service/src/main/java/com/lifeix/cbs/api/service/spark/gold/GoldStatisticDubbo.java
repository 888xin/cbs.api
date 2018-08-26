package com.lifeix.cbs.api.service.spark.gold;

import com.lifeix.exception.service.L99IllegalParamsException;

public interface GoldStatisticDubbo {

    /**
     * 插入统计数据
     * 
     * @param year
     * @param day
     * @return
     * @throws L99IllegalParamsException 
     */
    public boolean insert(Integer year, Integer day) throws L99IllegalParamsException;
    
}
