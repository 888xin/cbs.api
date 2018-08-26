package com.lifeix.cbs.api.service.spark.coupon;

public interface CouponUserDubbo {

    /**
     * 龙筹券过期发送消息提醒
     * 
     * @return
     */
    public void sendMessage();

    /**
     * 龙筹券过期发送信鸽消息
     * 
     * @return
     */
    public void sendPigeonMessage();

}
