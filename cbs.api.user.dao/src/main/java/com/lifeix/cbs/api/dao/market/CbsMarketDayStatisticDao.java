package com.lifeix.cbs.api.dao.market;

import java.util.Date;
import java.util.List;

import com.lifeix.cbs.api.dto.market.CbsMarketDayStatistic;

public interface CbsMarketDayStatisticDao {

    /**
     * 获取所有统计数据
     * 
     * @param CbsMarketDayStatistic
     * @return
     */
    public List<CbsMarketDayStatistic> findList(String marketCode, Long startId, Date statisticDateBefore, Date statisticDateAfter, int limit);

    /**
     * 插入 - 自动生成主键
     * 
     * @param CbsMarketDayStatistic
     * @return
     */
    public Integer insert(CbsMarketDayStatistic bean);

}