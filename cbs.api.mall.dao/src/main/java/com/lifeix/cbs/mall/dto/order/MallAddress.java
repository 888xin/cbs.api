/**
 * 
 */
package com.lifeix.cbs.mall.dto.order;

import java.io.Serializable;
import java.util.Date;

/**
 * 商城用户收货地址
 * 
 * @author lifeix
 * 
 */
public class MallAddress implements Serializable {

    private static final long serialVersionUID = -5530494272887238077L;

    /**
     * 地址Id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 收货人姓名
     */
    private String name;

    /**
     * 收货人联系方式
     */
    private String mobile;

    /**
     * 收货人地址
     */
    private String address;

    /**
     * 创建时间
     */
    private Date createTime;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getMobile() {
	return mobile;
    }

    public void setMobile(String mobile) {
	this.mobile = mobile;
    }

    public String getAddress() {
	return address;
    }

    public void setAddress(String address) {
	this.address = address;
    }

    public Date getCreateTime() {
	return createTime;
    }

    public void setCreateTime(Date createTime) {
	this.createTime = createTime;
    }

}
