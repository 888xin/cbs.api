package com.lifeix.cbs.contest.service.spark.contest;

import java.util.List;

import com.lifeix.cbs.contest.bean.yy.YyContestResponse;

public interface YyContestDubbo {

    /**
     * 获取超时未结算的比赛列表(超过结束时间)
     * 
     * @param limit
     * @return
     */
    public List<YyContestResponse> findTimeoutContest(Long contestId);

}
