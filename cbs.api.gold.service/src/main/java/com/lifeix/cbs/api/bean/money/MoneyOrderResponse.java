package com.lifeix.cbs.api.bean.money;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;
import com.lifeix.user.beans.account.AccountResponse;

/**
 * 龙币订单
 * 
 * @author lifeix
 * 
 */
public class MoneyOrderResponse implements JsonSerializer<MoneyOrderResponse>, Response {

    private static final long serialVersionUID = 3212982624679314499L;

    /**
     * 充值id
     */
    private Long order_id;

    /**
     * 充值用户
     */
    private Long user_id;

    /**
     * 被充值用户
     */
    private Long target_id;

    /**
     * 充值卡类型
     */
    private Long card_id;

    /**
     * 花费金额
     */
    private Float amount;

    /**
     * 获得龙币
     */
    private Float obtain;

    /**
     * 订单唯一序列号
     */
    private String order_no;

    /**
     * 添加时间
     */
    private Long add_time;

    /**
     * 支付时间
     */
    private Long paid_time;

    /**
     * 0 等待确认 1 等待完成 2 交易完成 10 超时关闭 11 用户取消
     */
    private int order_statu;

    /**
     * @link PaymentType 支付方式
     */
    private Integer payment_id;

    /**
     * @link PaymentType 支付方式
     */
    private String payment_type;

    /**
     * 操作管理员
     */
    private String admin_user;

    /**
     * 管理员备注
     */
    private String admin_note;

    /**
     * 用户备注
     */
    private String user_note;

    /**
     * 提款or充值
     */
    private boolean returned;

    /**
     * IP地址
     */
    private String ip_address;

    /**
     * 第三支付流水号
     */
    private String notify_id;

    private Long pay_order;

    /**
     * 客户端来源
     */
    private String sourceClient;

    /**
     * 微信订单信息
     */
    private MoneyOrderWeixinResponse weixin;

    /**
     * 被充值用户信息
     */
    private AccountResponse account;

    public Long getOrder_id() {
	return order_id;
    }

    public void setOrder_id(Long order_id) {
	this.order_id = order_id;
    }

    public Long getUser_id() {
	return user_id;
    }

    public void setUser_id(Long user_id) {
	this.user_id = user_id;
    }

    public Long getTarget_id() {
	return target_id;
    }

    public void setTarget_id(Long target_id) {
	this.target_id = target_id;
    }

    public Long getCard_id() {
	return card_id;
    }

    public void setCard_id(Long card_id) {
	this.card_id = card_id;
    }

    public Float getAmount() {
	return amount;
    }

    public void setAmount(Float amount) {
	this.amount = amount;
    }

    public Float getObtain() {
	return obtain;
    }

    public void setObtain(Float obtain) {
	this.obtain = obtain;
    }

    public String getOrder_no() {
	return order_no;
    }

    public void setOrder_no(String order_no) {
	this.order_no = order_no;
    }

    public Long getAdd_time() {
	return add_time;
    }

    public void setAdd_time(Long add_time) {
	this.add_time = add_time;
    }

    public Long getPaid_time() {
	return paid_time;
    }

    public void setPaid_time(Long paid_time) {
	this.paid_time = paid_time;
    }

    public int getOrder_statu() {
	return order_statu;
    }

    public void setOrder_statu(int order_statu) {
	this.order_statu = order_statu;
    }

    public Integer getPayment_id() {
	return payment_id;
    }

    public void setPayment_id(Integer payment_id) {
	this.payment_id = payment_id;
    }

    public String getPayment_type() {
	return payment_type;
    }

    public String getAdmin_user() {
	return admin_user;
    }

    public void setAdmin_user(String admin_user) {
	this.admin_user = admin_user;
    }

    public String getAdmin_note() {
	return admin_note;
    }

    public void setAdmin_note(String admin_note) {
	this.admin_note = admin_note;
    }

    public String getUser_note() {
	return user_note;
    }

    public void setUser_note(String user_note) {
	this.user_note = user_note;
    }

    public boolean isReturned() {
	return returned;
    }

    public void setReturned(boolean returned) {
	this.returned = returned;
    }

    public String getIp_address() {
	return ip_address;
    }

    public void setIp_address(String ip_address) {
	this.ip_address = ip_address;
    }

    public String getNotify_id() {
	return notify_id;
    }

    public void setNotify_id(String notify_id) {
	this.notify_id = notify_id;
    }

    public final Long getPay_order() {
	return pay_order;
    }

    public final void setPayment_type(String payment_type) {
	this.payment_type = payment_type;
    }

    public final void setPay_order(Long pay_order) {
	this.pay_order = pay_order;
    }

    public MoneyOrderWeixinResponse getWeixin() {
	return weixin;
    }

    public void setWeixin(MoneyOrderWeixinResponse weixin) {
	this.weixin = weixin;
    }

    @Override
    public String getObjectName() {
	return null;
    }

    public String getSourceClient() {
	return sourceClient;
    }

    public void setSourceClient(String sourceClient) {
	this.sourceClient = sourceClient;
    }

    public AccountResponse getAccount() {
	return account;
    }

    public void setAccount(AccountResponse account) {
	this.account = account;
    }

    @Override
    public JsonElement serialize(MoneyOrderResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

}
