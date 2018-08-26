package com.lifeix.cbs.contest.service.contest;

/**
 * Created by lhx on 2016/8/1 14:48
 *
 * @Description 赛事统计
 */
public interface ContestStatisticsService {

    /**
     * 未来十五天的数据，用做排除相同球赛
     * @return
     */
    void findContestsBySame(int type) ;

    /**
     * 获取相同球赛的数据
     */
    String[] getContestsBySame(int type);

    /**
     * 获取数量
     */
    int[] getContestsBySameNum();

    /**
     * 刷新
     */
    void refreshContestsSame(int type);

    /**
     * 未来七天下注金额大于一定值的数据
     * */
    void findMuchBetMoney(int type) ;

    /**
     * 获取超额下注记录
     */
    String[] getMuchBetInfo();

    /**
     * 刷新
     */
    void refreshMuchBet();
}
