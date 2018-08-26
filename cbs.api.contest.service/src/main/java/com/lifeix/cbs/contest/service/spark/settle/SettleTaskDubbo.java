package com.lifeix.cbs.contest.service.spark.settle;

import java.util.List;

import com.lifeix.cbs.contest.bean.fb.ContestListResponse;
import com.lifeix.cbs.contest.bean.settle.CbsSettleResponse;
import com.lifeix.exception.service.L99IllegalParamsException;

public interface SettleTaskDubbo {

    /**
     * 添加结算任务
     * 
     * @param type
     * @param contestId
     * @param immediate
     */
    public void addSettleTask(Integer type, Long contestId, boolean immediate);

    /**
     * 获取结算比赛
     * 
     * @param settleId
     * @param limit
     * @return
     * @throws L99IllegalParamsException
     */
    public ContestListResponse settlesContest(Long settleId, Integer type, int limit) throws L99IllegalParamsException;

    /**
     * 获取结算任务
     * 
     * @param settleId
     * @param limit
     * @return
     */
    public List<CbsSettleResponse> findSettleTask(Long settleId, int limit);

    /**
     * 标识结算已完成
     * 
     * @param settleId
     */
    public void closeContest(Long settleId, int statu);
}
