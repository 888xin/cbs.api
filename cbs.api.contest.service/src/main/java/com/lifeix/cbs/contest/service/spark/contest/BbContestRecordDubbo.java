package com.lifeix.cbs.contest.service.spark.contest;

import java.util.List;

import com.lifeix.cbs.contest.bean.fb.ContestResponse;
import com.lifeix.cbs.contest.bean.fb.ext.RecordResponse;

public interface BbContestRecordDubbo {

    /**
     * 批量保存交手记录
     * 
     * @param respList
     * @return
     */
    public boolean saveContestRecord(List<ContestResponse> respList);

    /**
     * 分析胜负或者赢盘种类
     * 
     * @param maxId
     * @param limit
     * @return
     */
    public List<RecordResponse> selectAnalysisNeeded(Long maxId, Integer limit);

    /**
     * 批量更新胜负或赢盘情况
     * 
     * @param respList
     * @return
     */
    public boolean updateWinTypes(List<RecordResponse> respList);
}
