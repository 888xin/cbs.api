/**
 * 
 */
package com.lifeix.cbs.mall.dto.order;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单物流信息
 * 
 * @author lifeix
 *
 */
public class MallExpress implements Serializable {

    private static final long serialVersionUID = -283721718023326101L;
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
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

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

    public Date getCreateTime() {
	return createTime;
    }

    public void setCreateTime(Date createTime) {
	this.createTime = createTime;
    }

    public Date getUpdateTime() {
	return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
	this.updateTime = updateTime;
    }

}
