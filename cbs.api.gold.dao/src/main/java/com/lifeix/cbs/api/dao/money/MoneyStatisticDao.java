package com.lifeix.cbs.api.dao.money;

import java.util.Date;
import java.util.List;

import com.lifeix.cbs.api.dao.BasicDao;
import com.lifeix.cbs.api.dto.money.MoneyStatistic;

public interface MoneyStatisticDao extends BasicDao<MoneyStatistic, Long> {

    /**
     * 查找某一天的统计数据
     * 
     * @param time
     * @return
     */

    public MoneyStatistic findByTime(Date time);

    /**
     * 查找某段时间内的统计
     * 
     * @param begin
     * @param end
     * @return
     */
    public List<MoneyStatistic> findBetweenTime(Date begin, Date end);
    
    

}
