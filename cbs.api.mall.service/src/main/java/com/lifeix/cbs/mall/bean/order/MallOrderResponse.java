/**
 * 
 */
package com.lifeix.cbs.mall.bean.order;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.cbs.mall.bean.goods.MallGoodsResponse;
import com.lifeix.user.beans.Response;

/**
 * 商品订单vo
 * 
 * @author lifeix
 * 
 */
public class MallOrderResponse implements JsonSerializer<MallOrderResponse>, Response {

    private static final long serialVersionUID = -1935501623763342629L;

    /**
     * 订单Id
     */
    private Long id;

    /**
     * 用户Id
     */
    private Long user_id;

    /**
     * 商品Id
     */
    private Long goods_id;

    /**
     * 商品单价
     */
    private Double goods_price;

    /**
     * 商品数量
     */
    private Integer goods_num;

    /**
     * 邮费
     */
    private Double postage;

    /**
     * 订单总价
     */
    private Double amount;

    /**
     * 收货地址
     */
    private String order_address;

    /**
     * 订单状态
     */
    private Integer status;

    /**
     * 订单属性
     */
    private String goods_pro;

    /**
     * 用户备注
     */
    private String user_remark;

    /**
     * 取消原因
     */
    private String cancel_reason;

    /**
     * 下单时间
     */
    private String create_time;

    /**
     * 发货时间
     */
    private String shop_time;

    /**
     * 完成时间
     */
    private String done_time;

    /**
     * 支付方式
     */
    private Long log_id;

    /**
     * 商品信息
     */
    private MallGoodsResponse goods;

    /**
     * 用户信息
     */
    private CbsUserResponse user;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Long getUser_id() {
	return user_id;
    }

    public void setUser_id(Long user_id) {
	this.user_id = user_id;
    }

    public Long getGoods_id() {
	return goods_id;
    }

    public void setGoods_id(Long goods_id) {
	this.goods_id = goods_id;
    }

    public Double getGoods_price() {
	return goods_price;
    }

    public void setGoods_price(Double goods_price) {
	this.goods_price = goods_price;
    }

    public Integer getGoods_num() {
	return goods_num;
    }

    public void setGoods_num(Integer goods_num) {
	this.goods_num = goods_num;
    }

    public Double getPostage() {
	return postage;
    }

    public void setPostage(Double postage) {
	this.postage = postage;
    }

    public Double getAmount() {
	return amount;
    }

    public void setAmount(Double amount) {
	this.amount = amount;
    }

    public String getOrder_address() {
	return order_address;
    }

    public void setOrder_address(String order_address) {
	this.order_address = order_address;
    }

    public Integer getStatus() {
	return status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    public String getGoods_pro() {
	return goods_pro;
    }

    public void setGoods_pro(String goods_pro) {
	this.goods_pro = goods_pro;
    }

    public String getUser_remark() {
	return user_remark;
    }

    public void setUser_remark(String user_remark) {
	this.user_remark = user_remark;
    }

    public String getCancel_reason() {
	return cancel_reason;
    }

    public void setCancel_reason(String cancel_reason) {
	this.cancel_reason = cancel_reason;
    }

    public String getCreate_time() {
	return create_time;
    }

    public void setCreate_time(String create_time) {
	this.create_time = create_time;
    }

    public String getShop_time() {
	return shop_time;
    }

    public void setShop_time(String shop_time) {
	this.shop_time = shop_time;
    }

    public String getDone_time() {
	return done_time;
    }

    public void setDone_time(String done_time) {
	this.done_time = done_time;
    }

    public Long getLog_id() {
	return log_id;
    }

    public void setLog_id(Long log_id) {
	this.log_id = log_id;
    }

    public CbsUserResponse getUser() {
	return user;
    }

    public void setUser(CbsUserResponse user) {
	this.user = user;
    }

    public MallGoodsResponse getGoods() {
	return goods;
    }

    public void setGoods(MallGoodsResponse goods) {
	this.goods = goods;
    }

    @Override
    public JsonElement serialize(MallOrderResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return null;
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
