package com.lifeix.cbs.contest.dao.contest;

import java.util.List;

import com.lifeix.cbs.contest.dto.contest.ContestAd;

public interface ContestAdDao {

    /**
     * 查看赛事廣告
     * 
     * @param id
     * @return
     */
    public ContestAd selectById(long id);

    /**
     * 插入賽事廣告
     * 
     * @param ContestAd
     * @return
     */
    public boolean insert(ContestAd bean);

    /**
     * 修改赛事廣告
     * 
     * @param ContestAd
     * @return
     */
    public boolean update(ContestAd bean);

    /**
     * 隐藏或显示赛事廣告
     * 
     * @param id
     * @param hideFlag
     * @return
     */
    public boolean updateHide(Long id, boolean hideFlag);

    /**
     * 查詢赛事廣告列表
     * 
     * @param startId
     * @param limit
     * @return
     */
    public List<ContestAd> findContestAds(Integer contestType, Boolean hideFlag, Long startId, int limit);

}