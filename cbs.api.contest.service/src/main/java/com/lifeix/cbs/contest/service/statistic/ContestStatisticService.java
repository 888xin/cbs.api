package com.lifeix.cbs.contest.service.statistic;

/**
 * Created by lhx on 15-12-21 上午11:05
 *
 * @Description
 */
public interface ContestStatisticService {

    /**
     * 有多少下单数
     */
    public void betsStatistic(String date);

    /**
     * 有多少人下单
     */
    public void peopleStatistic(String date);

    /**
     * 胜平负下单数
     */
    public void opStatistic(String date);

    /**
     * 让球胜平负下单数
     */
    public void jcStatistic(String date);

    /**
     * 足球下单数
     */
    public void fbStatistic(String date);

    /**
     * 篮球下单数
     */
    public void bbStatistic(String date);
}
