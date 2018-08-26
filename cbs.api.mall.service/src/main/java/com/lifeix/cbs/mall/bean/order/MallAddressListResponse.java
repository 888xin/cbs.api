/**
 * 
 */
package com.lifeix.cbs.mall.bean.order;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

/**
 * @author lifeix
 * 
 */
public class MallAddressListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = -9141018788536815353L;

    /**
     * 收货地址列表
     * 
     * */
    private List<MallAddressResponse> address_list;

    public List<MallAddressResponse> getAddress_list() {
	return address_list;
    }

    public void setAddress_list(List<MallAddressResponse> address_list) {
	this.address_list = address_list;
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
