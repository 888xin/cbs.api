package com.lifeix.cbs.api.impl.money;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.l99.common.utils.RegexUtil;
import com.l99.util.pay.PaymentType;
import com.l99.vo.pay.PayAccountOrderListReponse;
import com.l99.vo.pay.PayAccountOrderReponse;
import com.l99.vo.pay.PayAccountOrderTNReponse;
import com.l99.vo.pay.RechargePayFormVo;
import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.activity.bean.first.ActivityFirstResponse;
import com.lifeix.cbs.activity.service.first.ActivityFirstLogService;
import com.lifeix.cbs.api.bean.gold.GoldResponse;
import com.lifeix.cbs.api.bean.money.MoneyCardListResponse;
import com.lifeix.cbs.api.bean.money.MoneyCardResponse;
import com.lifeix.cbs.api.bean.money.MoneyOrderListResponse;
import com.lifeix.cbs.api.bean.money.MoneyOrderResponse;
import com.lifeix.cbs.api.bean.money.MoneyOrderTNResponse;
import com.lifeix.cbs.api.bean.money.MoneyOrderWapResponse;
import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.user.LifeixUserApiUtil;
import com.lifeix.cbs.api.common.util.CommerceMath;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.dao.money.MoneyCardDao;
import com.lifeix.cbs.api.dao.money.MoneyOrderDao;
import com.lifeix.cbs.api.dto.money.MoneyCard;
import com.lifeix.cbs.api.dto.money.MoneyOrder;
import com.lifeix.cbs.api.service.money.MoneyOrderService;
import com.lifeix.cbs.api.service.money.MoneyService;
import com.lifeix.cbs.api.transform.GoldTransformUtil;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;
import com.lifeix.payment.dubbo.service.PayAccountOrderService;
import com.lifeix.user.beans.account.AccountResponse;
import com.lifeix.user.beans.oauth.LifeixOAuthConsumer;

@Service("moneyOrderService")
public class MoneyOrderServiceImpl extends ImplSupport implements MoneyOrderService {

    /**
     * 网页支付跳转地址
     */
    private final static String WAP_PAY_CALL = "http://caibisai.com/gold/order_success";

    @Autowired
    private PayAccountOrderService payAccountOrderService;

    @Autowired
    private ActivityFirstLogService activityFirstLogService;

    @Autowired
    private MoneyService moneyService;

    @Autowired
    private MoneyCardDao moneyCardDao;

    @Autowired
    private MoneyOrderDao moneyOrderDao;

    /**
     * 龙币充值类型列表
     * 
     * @return
     * @throws L99IllegalParamsException
     */
    @Override
    public MoneyCardListResponse findMoneyCards(Long userId) throws L99IllegalParamsException, L99IllegalDataException,
	    JSONException {

	ParamemeterAssert.assertDataNotNull(userId);
	List<MoneyCard> cards = moneyCardDao.findMoneyCards(0);
	List<MoneyCardResponse> money_cards = new ArrayList<MoneyCardResponse>();
	for (MoneyCard card : cards) {
	    money_cards.add(GoldTransformUtil.transformMoneyCard(card));
	}

	GoldResponse moneyResponse = moneyService.viewUserMoney(userId, null);
	double money = CommerceMath.sub(moneyResponse.getBalance(), moneyResponse.getFrozen());
	MoneyCardListResponse reponse = new MoneyCardListResponse();
	// 查询用户是否参与首充活动
	ActivityFirstResponse res = activityFirstLogService.check(userId, false);
	reponse.setActFlag(res.getActFlag());
	reponse.setActStatus(res.getStatus());
	reponse.setMoney_cards(money_cards);
	reponse.setMoney(money);
	reponse.setNumber(cards.size());
	return reponse;
    }

    /**
     * 创建订单
     * 
     * @param cardId
     *            充值卡Id
     * @param userId
     *            支付用户
     * @param targetId
     *            被充值用户
     * @param money
     *            充值金额
     * @param paymentId
     *            支付方式
     * @param ipaddress
     * @return
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     * @throws JSONException
     */
    @Override
    public MoneyOrderResponse buildOrder(Long cardId, Long userId, Long targetId, Double money, Integer paymentId,
	    String ipaddress) throws L99NetworkException, L99IllegalParamsException, L99IllegalDataException, JSONException {

	ParamemeterAssert.assertDataNotNull(userId, targetId, paymentId);

	MoneyCard card = null;
	Double obtain = null; // 获取的龙币数额
	if (cardId != null) { // 充值卡充值
	    card = moneyCardDao.findById(cardId);
	    ParamemeterAssert.assertDataNotNull(card);
	    money = card.getPrice();
	    obtain = card.getAmount();
	} else { // 任意金额充值
	    ParamemeterAssert.assertDataNotNull(money);
	    obtain = money;

	    // 因为mq回调需要 拼装一个临时对象
	    card = new MoneyCard();
	    card.setId(0L);
	    card.setPrice(money);
	    card.setAmount(money);
	    card.setType(0);
	    card.setHandsel(0D);
	}

	String openId = "";// 用户微信协议id

	if (paymentId == PaymentType.WEIXINTIYU_GZH) {// 体育公众号支付
	    LifeixOAuthConsumer consumer = LifeixUserApiUtil.getInstance().findWxAccessToken(userId);
	    openId = consumer.getTokenSecret();
	}

	LOG.info(String.format("*** add cbs money oreder 1 user %s = %s *** ", userId, money));
	String orderData = payAccountOrderService.build(userId, targetId, 0L, money.floatValue(), obtain.floatValue(), null,
	        paymentId, "CBS", ipaddress, openId);

	LOG.info(String.format("*** add cbs money oreder 2 user %s = %s | %s *** ", userId, money, orderData));

	JSONObject orderRet = new JSONObject(orderData);
	if (orderRet.getInt("code") != DataResponse.OK) {
	    throw new L99IllegalDataException(orderRet.getString("code"), orderRet.getString("msg"));
	}
	PayAccountOrderReponse order = new Gson().fromJson(orderRet.getString("data"), PayAccountOrderReponse.class);

	if (card != null) { // 插入一条赠送记录
	    MoneyOrder moneyOrder = new MoneyOrder();
	    moneyOrder.setOrderId(order.getOrder_id());
	    moneyOrder.setUserId(targetId);
	    moneyOrder.setCardId(card.getId());
	    moneyOrder.setPrice(card.getPrice());
	    moneyOrder.setAmount(card.getAmount());
	    moneyOrder.setType(card.getType());
	    moneyOrder.setHandsel(card.getHandsel());
	    moneyOrder.setStatus(0);
	    moneyOrder.setCreateTime(new Date());
	    boolean flag = moneyOrderDao.insert(moneyOrder);
	    LOG.info(String.format("*** add cbs money oreder 3 user %s = %s | %s *** ", userId, money, flag));
	}

	return GoldTransformUtil.transformOrder(order);
    }

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
    @Override
    public MoneyOrderResponse confirmOrder(Long userId, Long orderId, String ipaddress) throws L99IllegalParamsException,
	    L99IllegalDataException, JSONException {

	ParamemeterAssert.assertDataNotNull(userId, orderId);

	String orderData = payAccountOrderService.confirm(orderId, userId, ipaddress);

	JSONObject orderRet = new JSONObject(orderData);
	if (orderRet.getInt("code") != DataResponse.OK) {
	    throw new L99IllegalDataException(orderRet.getString("code"), orderRet.getString("msg"));
	}
	PayAccountOrderReponse order = new Gson().fromJson(orderRet.getString("data"), PayAccountOrderReponse.class);
	return GoldTransformUtil.transformOrder(order);
    }

    @Override
    public MoneyOrderWapResponse confirmWapOrder(Long userId, Long orderId, String ipaddress, String from)
	    throws L99IllegalParamsException, L99IllegalDataException, JSONException {
	ParamemeterAssert.assertDataNotNull(userId, orderId);
	// 判断返回地址
	String returnUrl = WAP_PAY_CALL;
	if (RegexUtil.hasMatche(from, RegexUtil.NETURL_REG)) {
	    returnUrl = from;
	}

	String orderData = payAccountOrderService.confirmWapOrder(orderId, userId, returnUrl, null, ipaddress);

	JSONObject orderRet = new JSONObject(orderData);
	if (orderRet.getInt("code") != DataResponse.OK) {
	    throw new L99IllegalDataException(orderRet.getString("code"), orderRet.getString("msg"));
	}
	RechargePayFormVo order = new Gson().fromJson(orderRet.getString("data"), RechargePayFormVo.class);

	MoneyOrderWapResponse res = GoldTransformUtil.transformWapOrder(order);
	res.setFrom_url(returnUrl);
	return res;
    }

    /**
     * 检查订单
     * 
     * @param userId
     * @param orderId
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     * @throws JSONException
     */
    @Override
    public void checkOrder(Long userId, Long orderId) throws L99IllegalParamsException, L99IllegalDataException,
	    JSONException {

	ParamemeterAssert.assertDataNotNull(userId, orderId);

	String orderData = payAccountOrderService.check(orderId, userId);

	LOG.info(String.format("start check money order %d", orderId));

	JSONObject orderRet = new JSONObject(orderData);
	if (orderRet.getInt("code") != DataResponse.OK) {
	    throw new L99IllegalDataException(orderRet.getString("code"), orderRet.getString("msg"));
	}

    }

    /**
     * 获取银联TN号
     * 
     * @param orderId
     * @return
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     * @throws JSONException
     */
    @Override
    public MoneyOrderTNResponse orderTN(Long orderId) throws L99IllegalParamsException, L99IllegalDataException,
	    JSONException {

	ParamemeterAssert.assertDataNotNull(orderId);

	String orderData = payAccountOrderService.getTN(orderId);

	JSONObject orderRet = new JSONObject(orderData);
	if (orderRet.getInt("code") != DataResponse.OK) {
	    throw new L99IllegalDataException(orderRet.getString("code"), orderRet.getString("msg"));
	}
	PayAccountOrderTNReponse orderTn = new Gson().fromJson(orderRet.getString("data"), PayAccountOrderTNReponse.class);
	return GoldTransformUtil.transformOrderTN(orderTn);
    }

    @Override
    public MoneyCardResponse findMoneyCard(Long id) throws L99IllegalParamsException, L99IllegalDataException, JSONException {
	ParamemeterAssert.assertDataNotNull(id);
	MoneyCard bean = moneyCardDao.findById(id);

	return GoldTransformUtil.transformMoneyCard(bean);
    }

    @Override
    public MoneyCardListResponse findMoneyCardList(Integer deleteFlag) throws L99IllegalParamsException,
	    L99IllegalDataException, JSONException {
	MoneyCardListResponse reponse = new MoneyCardListResponse();

	List<MoneyCard> cards = moneyCardDao.findMoneyCards(deleteFlag);
	List<MoneyCardResponse> moneyCards = new ArrayList<MoneyCardResponse>();
	for (MoneyCard card : cards) {
	    moneyCards.add(GoldTransformUtil.transformMoneyCard(card));
	}
	reponse.setMoney_cards(moneyCards);
	reponse.setNumber(cards.size());
	return reponse;
    }

    @Override
    public boolean updateMoneyCard(Long id, String name, String detail, Double price, Double amount, Integer type,
	    Double handsel) throws L99IllegalParamsException, L99IllegalDataException, JSONException {
	MoneyCard entity = new MoneyCard();
	entity.setId(id);
	entity.setName(name);
	entity.setDetail(detail);
	entity.setHandsel(handsel);
	entity.setAmount(amount);
	entity.setPrice(price);
	entity.setType(type);

	return moneyCardDao.update(entity);
    }

    @Override
    public boolean insertMoneyCard(String name, String detail, Double price, Double amount, Integer type, Double handsel)
	    throws L99IllegalParamsException, L99IllegalDataException, JSONException {

	MoneyCard entity = new MoneyCard();
	entity.setName(name);
	entity.setDetail(detail);
	entity.setHandsel(handsel);
	entity.setAmount(amount);
	entity.setPrice(price);
	entity.setType(type);
	entity.setCreateTime(new Date());
	return moneyCardDao.insert(entity);
    }

    @Override
    public boolean deleteMoneyCard(Long id, Integer deleteFlag) throws L99IllegalParamsException, L99IllegalDataException,
	    JSONException {
	MoneyCard entity = new MoneyCard();
	entity.setId(id);
	entity.setDeleteFlag(deleteFlag);
	return moneyCardDao.delete(entity);
    }

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
    @Override
    public MoneyOrderListResponse findOrdersBySource(Long startTime, Long endTime, String source, Long startId, int limit)
	    throws L99IllegalParamsException, L99IllegalDataException, JSONException, L99NetworkException {

	ParamemeterAssert.assertDataNotNull(startTime, endTime);

	// 查询订单信息
	String orderData = payAccountOrderService.findPayOrdersBySource(startTime, endTime, source, startId, limit);
	JSONObject orderRet = new JSONObject(orderData);
	if (orderRet.getInt("code") != DataResponse.OK) {
	    throw new L99IllegalDataException(orderRet.getString("code"), orderRet.getString("msg"));
	}
	PayAccountOrderListReponse orderResponse = new Gson().fromJson(orderRet.getString("data"),
	        PayAccountOrderListReponse.class);

	// 查询被充值用户信息
	List<PayAccountOrderReponse> orders = orderResponse.getOrders();
	List<Long> userIds = new ArrayList<Long>();
	for (PayAccountOrderReponse orderReponse : orders) {
	    userIds.add(orderReponse.getTarget_id());
	}
	Map<Long, AccountResponse> userMap = LifeixUserApiUtil.getInstance().findUserMapByIds(
	        StringUtils.join(userIds, ","), null, false);

	List<MoneyOrderResponse> orderList = new ArrayList<MoneyOrderResponse>();
	for (PayAccountOrderReponse orderReponse : orders) {
	    MoneyOrderResponse order = GoldTransformUtil.transformOrder(orderReponse);
	    if (order != null) {
		order.setAccount(userMap.get(order.getTarget_id()));
		orderList.add(order);
	    }
	}

	MoneyOrderListResponse response = new MoneyOrderListResponse();
	response.setOrders(orderList);
	response.setStartId(startId);
	response.setLimit(limit);
	response.setNumber(orderList.size());
	return response;
    }
}
