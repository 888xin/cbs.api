package com.lifeix.cbs.contest.service.spark.contest;

public interface ContestBetDubbo {

    /**
     * 发送微信下单消息提醒
     * 
     * @param limit
     * @return
     */
    public void wxNotify(String paramStr);

}
