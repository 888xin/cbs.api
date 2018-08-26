package com.lifeix.cbs.contest.dao.yy;

import java.util.List;
import java.util.Map;

import com.lifeix.cbs.contest.dto.yy.YyContest;
import com.lifeix.cbs.contest.dto.yy.YyCup;

public interface YyContestDao {

    public YyContest selectById(long id);

    /**
     * 批量查找列表
     * 
     * @param ids
     * @return
     */
    public Map<Long, YyContest> findYyContestMapByIds(List<Long> ids);

    public boolean insert(YyContest contest);

    /**
     * 修改押押赛事
     * 
     * @param contest
     * @return
     */
    public boolean update(YyContest contest);

    /**
     * 隐藏或显示押押赛事
     * 
     * @param contestId
     * @param hideFlag
     * @return
     */
    public boolean updateHide(Long contestId, boolean hideFlag);

    /**
     * 押押可下单赛事列表
     * 
     * @param cupId
     * @param startId
     * @param limit
     * @return
     */
    public List<YyContest> findBetYyContests(Long cupId, Long startId, int limit);

    /**
     * 押押可下注的分类
     * 
     * @return
     */
    public List<YyCup> findBetYyCups();

    /**
     * 押押后台赛事列表
     * 
     * @param hideFlag
     *            是否隐藏
     * @param type
     *            null 全部 true 未结束 false 一结束
     * @param startId
     * @param limit
     * @return
     */
    public List<YyContest> findYyContests(Boolean hideFlag, Boolean type, Long cupId, Long startId, int limit);

    /**
     * 查询结束的押押赛事(超过结束时间并已有结果)
     * 
     * @param contestId
     * @param limit
     * @return
     */
    List<YyContest> findTimeoutContest(Long contestId, int limit);

    /**
     * 可以结算的押押数量
     */
    public Integer settleNum();
}