package com.lifeix.cbs.api.dao.gold;

import java.util.Date;
import java.util.List;

import com.lifeix.cbs.api.dao.BasicDao;
import com.lifeix.cbs.api.dto.gold.GoldStatistic;

public interface GoldStatisticDao extends BasicDao<GoldStatistic, Long> {

    /**
     * 批量插入统计数据
     * 
     * @param counts
     * @return
     */
    public boolean batchInsert(List<GoldStatistic> counts);

    /**
     * 查找某一天的统计数据
     * 
     * @param time
     * @return
     */

    public GoldStatistic findByTime(Date time);

    /**
     * 查找某段时间内的统计
     * 
     * @param begin
     * @param end
     * @return
     */
    public List<GoldStatistic> findBetweenTime(Date begin, Date end);
    
    

}
