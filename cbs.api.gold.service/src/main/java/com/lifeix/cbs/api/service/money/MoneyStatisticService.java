package com.lifeix.cbs.api.service.money;

import java.util.Date;

import org.json.JSONException;

import com.lifeix.cbs.api.bean.money.MoneyStatisticListResponse;
import com.lifeix.cbs.api.bean.money.MoneyStatisticResponse;
import com.lifeix.cbs.api.bean.money.MoneyUserStatisticListResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

public interface MoneyStatisticService {

    /**
     * 获取当天的系统收入支出统计
     *
     * @param time
     * @return
     * @throws L99IllegalParamsException
     */
    MoneyStatisticResponse findByTime(Date time) throws L99IllegalParamsException;

    /**
     * 获取一段时间内的的系统收入支出统计
     *
     * @param begin
     * @param end
     * @return
     * @throws L99IllegalParamsException
     */
    MoneyStatisticListResponse findByBetweenTime(Date begin, Date end) throws L99IllegalParamsException;

    /**
     * 保存用户的收支记录
     *
     * @param userId
     * @param money
     */
    void insertUserConsumer(String userId, double money);

    /**
     * 用户充值和购买商品的收支记录
     */
    void insertSystemConsumer(String userId, double money);

    /**
     * 获得一天的用户收支记录
     *
     * @param date  yyMMdd
     * @param type  0收入 1支出 null全部
     * @param order 0 add desc 1 add asc 2 minus desc 3 minus asc
     * @return
     */
    MoneyUserStatisticListResponse getUsersDayConsumer(String date, Integer type, Integer order);

    /**
     * 创建指定日期的龙币支出与收入记录
     *
     * @param time
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     * @throws JSONException
     */
    void buildMoneyDayStatistic(String time) throws L99IllegalParamsException, L99IllegalDataException, JSONException;
}
