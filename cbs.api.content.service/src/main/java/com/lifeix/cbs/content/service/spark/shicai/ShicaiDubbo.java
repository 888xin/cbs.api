package com.lifeix.cbs.content.service.spark.shicai;

import org.json.JSONException;


/**
 * @author wenhuans 2015年11月4日 上午10:07:38
 * 
 */
public interface ShicaiDubbo {

    /**
     * 定时器每十分钟开启一局游戏
     * 
     * @author wenhuans 2015年11月5日上午10:54:41
     * @throws JSONException 
     * 
     */
    public void toStart() throws JSONException;
}
