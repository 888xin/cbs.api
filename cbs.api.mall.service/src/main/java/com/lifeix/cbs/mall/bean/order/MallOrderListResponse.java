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
public class MallOrderListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = 8454173728406821446L;

    /**
     * 订单列表
     * 
     * */
    private List<MallOrderResponse> orders;

    public List<MallOrderResponse> getOrders() {
	return orders;
    }

    public void setOrders(List<MallOrderResponse> orders) {
	this.orders = orders;
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
