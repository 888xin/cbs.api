package com.lifeix.cbs.contest.service.spark.cup;

import java.util.List;
import java.util.Map;

import com.lifeix.cbs.contest.bean.fb.CupListResponse;
import com.lifeix.cbs.contest.bean.fb.CupResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

public interface FbCupDubbo {

    public boolean saveCup(List<CupResponse> list);

    public Map<Long, CupResponse> getAllCup();

    public Map<Long, CupResponse> getCupByTargetIds(List<Long> targetIds);

    /**
     * 获取指定等级的联赛 add by lhx on 16-03-01
     */
    public CupListResponse getLevelCup(int level) throws L99IllegalParamsException;

    /**
     * 更新联赛名
     * 
     * @param id
     * @param name
     * @throws L99IllegalDataException
     * @throws L99IllegalParamsException
     */
    public void updateCupName(Long id, String name) throws L99IllegalDataException, L99IllegalParamsException;

    /**
     * 获取指定时间内赛事类型
     * 
     * @return
     */
    public CupListResponse getCupByContestDate();

    /**
     * 更新当前三十天内容的足球联赛列表缓存
     */
    public Map<Long, String> refresValidFbCup();

}
