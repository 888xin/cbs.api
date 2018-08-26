package com.lifeix.cbs.contest.dao.contest;



import com.lifeix.cbs.contest.dto.contest.Contest;

import java.util.List;

public interface ContestStatisticsDao {

    /**
     * 未来十五天的数据，用做排除相同球赛
     */
    List<Contest> findContestsBySame(int type);

    /**
     * 根据状态查找比赛
     */
    List<Contest> findContestsByStatus(int type, int status);

    /**
     * 根据ID查找赛事
     */
    Contest findContestsById(long contestId, int type);

    /**
     * 查找超额下注
     */
    Double findBetMoney(int playType, long contestId, int support);

}