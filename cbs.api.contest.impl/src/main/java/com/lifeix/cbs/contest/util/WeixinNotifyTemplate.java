package com.lifeix.cbs.contest.util;

public class WeixinNotifyTemplate {

    /**
     * 订单完成提醒ID
     */
    public static final String ORDER_OVER_TEMPID = "esYUaDrrAEwz8gC2taiZ69eir2KZysa-emd14SIsamo";

    /**
     * 订单支付提醒ID
     */
    public static final String ORDER_PAY_TEMPID = "HbsUF1bW0XxZUJPevn8bUqZnyVoppjOBW4uNYFfjg-8";

    /**
     * 订单完成提醒模板
     */
    public static final String orderOverTemp = "{\"topcolor\":\"#000000\",\"data\":{\"remark\":{\"color\":\"#000000\",\"value\":\"竞猜结果：%s。 点击详情下载大赢家兑换豪礼 \"},\"keyword1\":{\"color\":\"#000000\",\"value\":\"%s\"},\"first\":{\"color\":\"#000000\",\"value\":\"订单详情：您在%s的比赛『%sVS%s』中选择『%s』玩法,下单%s.\"},\"keyword2\":{\"color\":\"#000000\",\"value\":\"%s\"}},\"template_id\":\"%s\",\"touser\":\"%s\",\"url\":\"%s\"}";

    public static final String yyOrderOverTemp = "{\"topcolor\":\"#000000\",\"data\":{\"remark\":{\"color\":\"#000000\",\"value\":\"竞猜结果：%s。 点击详情下载大赢家兑换豪礼 \"},\"keyword1\":{\"color\":\"#000000\",\"value\":\"%s\"},\"first\":{\"color\":\"#000000\",\"value\":\"订单详情：您在%s的押押『%s』中选择『%s』，下单%s.\"},\"keyword2\":{\"color\":\"#000000\",\"value\":\"%s\"}},\"template_id\":\"%s\",\"touser\":\"%s\",\"url\":\"%s\"}";

    /**
     * 订单下单提醒模板
     */
    public static final String orderPayTemp = "{\"template_id\":\"%s\",\"touser\":\"%s\",\"url\":\"%s\",\"topcolor\":\"#000000\",\"data\":{ \"first\":{\"color\":\"#000000\",\"value\":\"订单主题：参与%s的%s比赛『%sVS%s』中选择『%s』玩法.\"},\"keyword1\":{\"color\":\"#000000\",\"value\":\"%s\"},\"keyword2\":{\"color\":\"#000000\",\"value\":\"%s\"},\"remark\":{\"color\":\"#000000\",\"value\":\"订单详情：您选择『%s』，赔率『%s』\"}}}";

    public static final String yyOrderPayTemp = "{\"template_id\":\"%s\",\"touser\":\"%s\",\"url\":\"%s\",\"topcolor\":\"#000000\",\"data\":{\"first\":{\"color\":\"#000000\",\"value\":\"订单主题：参与%s的押押『%s』.\"},\"keyword1\":{\"color\":\"#000000\",\"value\":\"%s\"},\"keyword2\":{\"color\":\"#000000\",\"value\":\"%s\"},\"remark\":{\"color\":\"#000000\",\"value\":\"订单详情：您选择『%s』，赔率『%s』\"}}}";

}
