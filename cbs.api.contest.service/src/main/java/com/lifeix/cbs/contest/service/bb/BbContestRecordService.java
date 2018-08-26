package com.lifeix.cbs.contest.service.bb;

import com.lifeix.cbs.contest.bean.fb.ext.AboutRecordListResponse;
import com.lifeix.cbs.contest.bean.fb.ext.RecordListResponse;
import com.lifeix.exception.service.L99IllegalParamsException;

public interface BbContestRecordService {

    /**
     * 查篮球球队近期交手记录盘面分析
     * 
     * @param teamId
     * @param limit
     * @return
     * @throws L99IllegalParamsException
     */
    public RecordListResponse selectTeamRecord(Long teamId, Integer limit) throws L99IllegalParamsException;
    
    /**
     * 查询关于主客的各自和关联的比赛记录
     * @param homeTeamId
     * @param awayTeamId
     * @param limit
     * @return
     * @throws L99IllegalParamsException 
     */
    public AboutRecordListResponse selectAboutRecord(Long homeTeamId, Long awayTeamId, Integer limit) throws L99IllegalParamsException;

}
