package com.lifeix.cbs.contest.service.spark.statistic;

/**
 * Created by lhx on 2016/7/20 16:35
 *
 * @Description
 */
public interface SameStatisticDubbo {

    /**
     * 未来十五天的数据，用做排除相同球赛
     * @return
     */
    void findFbContestsBySame() ;

    /**
     * 获取相同球赛的数据
     */
    String[] getFbContestsBySame();

    /**
     * 获取数量
     */
    int[] getContestsBySameNum();

    /**
     * 未来十五天的数据，用做排除相同球赛
     * @return
     */
    void findBbContestsBySame() ;

    /**
     * 获取相同球赛的数据
     */
    String[] getBbContestsBySame();

}
