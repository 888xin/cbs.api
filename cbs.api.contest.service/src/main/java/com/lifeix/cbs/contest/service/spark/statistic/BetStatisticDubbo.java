package com.lifeix.cbs.contest.service.spark.statistic;

import com.lifeix.cbs.contest.bean.statistic.BetStatisticResponse;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * Created by lhx on 15-12-21 下午4:49
 *
 * @Description
 */
public interface BetStatisticDubbo {

    /**
     * 统计下单数
     */
    public void betsStatistic(String date);

    /**
     * 统计有多少人下单
     */
    public void peopleStatistic(String date);

    /**
     * 统计胜平负下单数
     */
    public void opStatistic(String date);

    /**
     * 统计让球胜平负下单数
     */
    public void jcStatistic(String date);

    /**
     * 统计足球下单数
     */
    public void fbStatistic(String date);

    /**
     * 统计篮球下单数
     */
    public void bbStatistic(String date);

    /**
     * 获得所有类型的下单数
     */
    public BetStatisticResponse getStatistic(String startDate, Integer limit) throws L99IllegalParamsException;
}
