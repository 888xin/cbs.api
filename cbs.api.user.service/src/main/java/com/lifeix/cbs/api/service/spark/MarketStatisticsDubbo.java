package com.lifeix.cbs.api.service.spark;

import java.text.ParseException;

import org.json.JSONException;

import com.lifeix.exception.service.L99NetworkException;

/**
 * @author yis
 * 
 */
public interface MarketStatisticsDubbo {

    /**
     * 渠道日统计数据入库
     * 
     * @param date
     * @throws JSONException
     * @throws L99NetworkException
     */
    public void marketDayStatistic(String date) throws L99NetworkException, JSONException, ParseException;

}
