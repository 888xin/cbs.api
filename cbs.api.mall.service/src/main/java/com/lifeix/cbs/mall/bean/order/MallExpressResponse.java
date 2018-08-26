/**
 * 
 */
package com.lifeix.cbs.mall.bean.order;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

/**
 * 订单物流信息
 * 
 * @author lifeix
 *
 */
public class MallExpressResponse implements JsonSerializer<MallExpressResponse>, Response {

    /**
     * 
     */
    private static final long serialVersionUID = -4372003863733817458L;
    /**
     * 订单ID
     */
    private Long orderId;
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 快递类型
     */
    private Integer expressType;
    /**
     * 快递单号
     */
    private String expressNO;
    /**
     * 物流状态（2-在途中,3-签收,4-问题件）
     */
    private Integer state;
    /**
     * 快递信息
     */
    private String expressInfo;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新时间
     */
    private String updateTime;

    public Long getOrderId() {
	return orderId;
    }

    public void setOrderId(Long orderId) {
	this.orderId = orderId;
    }

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public Integer getExpressType() {
	return expressType;
    }

    public void setExpressType(Integer expressType) {
	this.expressType = expressType;
    }

    public String getExpressNO() {
	return expressNO;
    }

    public void setExpressNO(String expressNO) {
	this.expressNO = expressNO;
    }

    public Integer getState() {
	return state;
    }

    public void setState(Integer state) {
	this.state = state;
    }

    public String getExpressInfo() {
	return expressInfo;
    }

    public void setExpressInfo(String expressInfo) {
	this.expressInfo = expressInfo;
    }

    public String getCreateTime() {
	return createTime;
    }

    public void setCreateTime(String createTime) {
	this.createTime = createTime;
    }

    public String getUpdateTime() {
	return updateTime;
    }

    public void setUpdateTime(String updateTime) {
	this.updateTime = updateTime;
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(MallExpressResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

}
