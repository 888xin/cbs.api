package com.lifeix.cbs.contest.dao.settle;

import java.util.List;

import com.lifeix.cbs.contest.dto.settle.CbsSettle;

public interface CbsSettleDao {

    public Boolean insert(CbsSettle entity);

    public Boolean update(CbsSettle entity);

    /**
     * 关闭结算
     * 
     * @param settleId
     * @return
     */
    public boolean closeContest(Long settleId, int status);

    /**
     * 获取结算任务
     * 
     * @param settleId
     * @param limit
     * @return
     */
    public List<CbsSettle> findSettleTask(Long settleId, int limit);

    /**
     * 获取结算列表
     * 
     * @param settleId
     * @param type
     * @param limit
     * @return
     */
    public List<CbsSettle> findCbsSettles(Long settleId, Integer type, int limit);

    /**
     * 根据赛事ID查询赛事结算任务
     * 
     * @param type
     * @param contestId
     * @return
     */
    public CbsSettle findByContest(Integer type, Long contestId);

}