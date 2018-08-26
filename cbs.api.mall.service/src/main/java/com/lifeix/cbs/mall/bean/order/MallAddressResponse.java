/**
 * 
 */
package com.lifeix.cbs.mall.bean.order;

import com.lifeix.user.beans.Response;

/**
 * 订单收货地址
 * 
 * @author lifeix
 * 
 */
public class MallAddressResponse implements Response {

    private static final long serialVersionUID = -3121666671342090875L;

    /**
     * 地址Id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long user_id;

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
    private String create_time;

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

    public String getCreate_time() {
	return create_time;
    }

    public void setCreate_time(String create_time) {
	this.create_time = create_time;
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
