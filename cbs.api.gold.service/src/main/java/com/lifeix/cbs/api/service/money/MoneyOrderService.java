package com.lifeix.cbs.api.service.money;

import org.json.JSONException;

import com.lifeix.cbs.api.bean.money.MoneyCardListResponse;
import com.lifeix.cbs.api.bean.money.MoneyCardResponse;
import com.lifeix.cbs.api.bean.money.MoneyOrderListResponse;
import com.lifeix.cbs.api.bean.money.MoneyOrderResponse;
import com.lifeix.cbs.api.bean.money.MoneyOrderTNResponse;
import com.lifeix.cbs.api.bean.money.MoneyOrderWapResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;

/**
 * 龙币日志接口
 * 
 */
public interface MoneyOrderService {

    /**
     * 删除充值卡
     * 
     * @return
     */
    public boolean deleteMoneyCard(Long id, Integer deleteFlag) throws L99IllegalParamsException, L99IllegalDataException,
	    JSONException;

    /**
     * 更新充值卡
     * 
     * @return
     */
    public boolean updateMoneyCard(Long id, String name, String detail, Double price, Double amount, Integer type,
	    Double handsel) throws L99IllegalParamsException, L99IllegalDataException, JSONException;

    /**
     * 新增充值卡
     * 
     * @return
     */
    public boolean insertMoneyCard(String name, String detail, Double price, Double amount, Integer type, Double handsel)
	    throws L99IllegalParamsException, L99IllegalDataException, JSONException;

    /**
     * 充值卡列表
     * 
     * @return
     */
    public MoneyCardListResponse findMoneyCardList(Integer deleteFlag) throws L99IllegalParamsException,
	    L99IllegalDataException, JSONException;

    /**
     * 查看单个充值卡记录
     * 
     * @return
     */
    public MoneyCardResponse findMoneyCard(Long id) throws L99IllegalParamsException, L99IllegalDataException, JSONException;

    /**
     * 龙币充值类型列表
     * 
     * @return
     */
    public MoneyCardListResponse findMoneyCards(Long userId) throws L99IllegalParamsException, L99IllegalDataException,
	    JSONException;

    /**
     * 创建订单
     * 
     * @param cardId
     * @param userId
     * @param targetId
     * @param money
     * @param paymentId
     * @param ipaddress
     * @return
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     * @throws JSONException
     */
    public MoneyOrderResponse buildOrder(Long cardId, Long userId, Long targetId, Double money, Integer paymentId,
	    String ipaddress) throws L99NetworkException, L99IllegalParamsException, L99IllegalDataException, JSONException;

    /**
     * 确认订单
     * 
     * @param userId
     * @param orderId
     * @param ipaddress
     * @return
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     * @throws JSONException
     */
    public MoneyOrderResponse confirmOrder(Long userId, Long orderId, String ipaddress) throws L99IllegalParamsException,
	    L99IllegalDataException, JSONException;

    /**
     * 确认网页订单
     * 
     * @param userId
     * @param orderId
     * @param ipaddress
     * @return
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     * @throws JSONException
     */
    public MoneyOrderWapResponse confirmWapOrder(Long userId, Long orderId, String ipaddress, String from)
	    throws L99IllegalParamsException, L99IllegalDataException, JSONException;

    /**
     * 检查订单
     * 
     * @param userId
     * @param orderId
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     * @throws JSONException
     */
    public void checkOrder(Long userId, Long orderId) throws L99IllegalParamsException, L99IllegalDataException,
	    JSONException;

    /**
     * 获取银联TN号
     * 
     * @param orderId
     * @return
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     * @throws JSONException
     */
    public MoneyOrderTNResponse orderTN(Long orderId) throws L99IllegalParamsException, L99IllegalDataException,
	    JSONException;

    /**
     * 查询指定时间和指定来源的已完成订单信息
     * 
     * @param startTime
     * @param endTime
     * @param source
     * @param startId
     * @param limit
     * @return
     * @throws L99IllegalParamsException
     * @throws JSONException
     * @throws L99IllegalDataException
     * @throws L99NetworkException
     */
    public MoneyOrderListResponse findOrdersBySource(Long startTime, Long endTime, String source, Long startId, int limit)
	    throws L99IllegalParamsException, L99IllegalDataException, JSONException, L99NetworkException;
}
