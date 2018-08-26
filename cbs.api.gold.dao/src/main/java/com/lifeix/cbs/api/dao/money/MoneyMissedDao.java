package com.lifeix.cbs.api.dao.money;

import java.util.List;

import com.lifeix.cbs.api.dao.BasicDao;
import com.lifeix.cbs.api.dto.money.MoneyMissed;

public interface MoneyMissedDao extends BasicDao<MoneyMissed, Long> {

    /**
     * 丢失的龙币操作记录列表
     * 
     * @param status
     * @param startId
     * @param limit
     * @return
     */
    public List<MoneyMissed> findMoneyMisseds(Integer status, Long startId, int limit);

    /**
     * 统计未处理的龙币丢失记录
     * 
     * @return
     */
    public Integer countMoneyMisseds();
}
