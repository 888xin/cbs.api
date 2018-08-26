package com.lifeix.cbs.mall.service.order;

import org.json.JSONException;

import com.lifeix.cbs.mall.bean.order.MallOrderListResponse;
import com.lifeix.cbs.mall.bean.order.MallOrderResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;

public interface MallOrderService {

    /**
     * 用户订单列表
     * 
     * @param userId
     * @param innerFlag
     *            内部访问
     * @param userFlag
     *            是否包含用户数据
     * @param status
     * @param startId
     * @param limit
     * @return
     * @throws L99IllegalParamsException
     */
    public MallOrderListResponse findMallOrderList(Long userId, boolean innerFlag, boolean userFlag, Integer status,
	    Long startId, int limit) throws L99IllegalParamsException;

    /**
     * 生成商品订单
     * 
     * @param goodsId
     * @param userId
     * @param addressId
     * @param goodsNum
     * @param postage
     * @param goodsPro
     * @param userRemark
     * @throws L99IllegalDataException
     * @throws L99IllegalParamsException
     * @throws JSONException
     */
    public void buildMallOrder(Long goodsId, Long userId, String address, Integer goodsNum, Double postage, String goodsPro,
	    String userRemark, String ipaddrss) throws L99IllegalDataException, L99IllegalParamsException, JSONException;

    /**
     * 确认发货
     * 
     * @param orderId
     * @param expressType
     * @param expressNO
     * @return
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    public void sendMallOrder(Long orderId, Integer expressType, String expressNO) throws L99IllegalParamsException,
	    L99IllegalDataException;

    /**
     * 确认收货
     * 
     * @param orderId
     * @param userId
     * @throws L99IllegalDataException
     * @throws L99IllegalParamsException
     */
    public void confirmMallOrder(Long orderId, Long userId) throws L99IllegalDataException, L99IllegalParamsException;

    /**
     * 单个订单信息
     * 
     * @param orderId
     * @return
     * @throws L99IllegalParamsException
     * @throws JSONException
     * @throws L99NetworkException
     */
    public MallOrderResponse findOneOrder(Long orderId) throws L99IllegalParamsException, L99NetworkException, JSONException;

    /**
     * 取消订单
     * 
     * @param orderId
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     * @throws JSONException
     */
    public void cancelOrder(Long orderId, String ipaddress) throws L99IllegalParamsException, L99IllegalDataException, JSONException;

}
