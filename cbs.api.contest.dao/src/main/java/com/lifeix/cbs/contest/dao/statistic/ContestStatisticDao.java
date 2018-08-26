package com.lifeix.cbs.contest.dao.statistic;

/**
 * Created by lhx on 15-12-21 上午11:17
 *
 * @Description
 */
public interface ContestStatisticDao {
    /**
     * 有多少下单数
     */
    public int betsStatistic(String date);

    /**
     * 有多少人下单
     */
    public int peopleStatistic(String date);

    /**
     * 胜平负下单数
     */
    public int opStatistic(String date);

    /**
     * 让球胜平负下单数
     */
    public int jcStatistic(String date);

    /**
     * 足球下单数
     */
    public int fbStatistic(String date);

    /**
     * 篮球下单数
     */
    public int bbStatistic(String date);
}
