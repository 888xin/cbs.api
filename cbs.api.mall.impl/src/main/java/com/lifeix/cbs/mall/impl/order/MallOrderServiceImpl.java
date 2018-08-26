package com.lifeix.cbs.mall.impl.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.bean.gold.GoldResponse;
import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.exception.MsgCode.GoodsMsg;
import com.lifeix.cbs.api.common.exception.MsgCode.OrderMsg;
import com.lifeix.cbs.api.common.exception.MsgCode.UserMsg;
import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.util.CommerceMath;
import com.lifeix.cbs.api.common.util.ContentConstants;
import com.lifeix.cbs.api.common.util.GoldConstants.MoneyMissedType;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.service.money.MoneyService;
import com.lifeix.cbs.api.service.money.MoneyStatisticService;
import com.lifeix.cbs.api.service.user.CbsUserService;
import com.lifeix.cbs.content.service.frontpage.FrontPageService;
import com.lifeix.cbs.mall.bean.goods.MallGoodsResponse;
import com.lifeix.cbs.mall.bean.order.MallOrderListResponse;
import com.lifeix.cbs.mall.bean.order.MallOrderResponse;
import com.lifeix.cbs.mall.common.ExpressConstants;
import com.lifeix.cbs.mall.common.MallConstants.GoodsLock;
import com.lifeix.cbs.mall.common.MallConstants.GoodsStatus;
import com.lifeix.cbs.mall.common.MallConstants.GoodsType;
import com.lifeix.cbs.mall.common.MallConstants.OrderStatus;
import com.lifeix.cbs.mall.dao.goods.MallGoodsDao;
import com.lifeix.cbs.mall.dao.order.MallExpressDao;
import com.lifeix.cbs.mall.dao.order.MallOrderDao;
import com.lifeix.cbs.mall.dto.goods.MallGoods;
import com.lifeix.cbs.mall.dto.order.MallExpress;
import com.lifeix.cbs.mall.dto.order.MallOrder;
import com.lifeix.cbs.mall.impl.transform.MallGoodsTransformUtil;
import com.lifeix.cbs.mall.impl.transform.MallOrderTransformUtil;
import com.lifeix.cbs.mall.service.order.MallOrderService;
import com.lifeix.common.utils.RegexUtil;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;
import com.lifeix.framework.memcache.MemcacheService;

/**
 * @author huiy
 * @date 2015.8.5
 */
@Service("mallOrderService")
public class MallOrderServiceImpl extends ImplSupport implements MallOrderService {

    private static final Logger LOG = LoggerFactory.getLogger(MallOrderServiceImpl.class);

    @Autowired
    private MallOrderDao mallOrderDao;

    @Autowired
    private MallGoodsDao mallGoodsDao;

    @Autowired
    private MallExpressDao mallExpressDao;

    @Autowired
    private MoneyService moneyService;

    @Autowired
    private CbsUserService cbsUserService;

    @Autowired
    private MemcacheService memcacheService;

    @Autowired
    private FrontPageService frontPageService;

    @Autowired
    private MoneyStatisticService moneyStatisticService;

    /**
     * 用户订单列表
     */
    public MallOrderListResponse findMallOrderList(Long userId, boolean innerFlag, boolean userFlag, Integer status,
	    Long startId, int limit) throws L99IllegalParamsException {

	ParamemeterAssert.assertDataNotNull(status);

	if (!innerFlag) {
	    ParamemeterAssert.assertDataNotNull(userId);
	}

	limit = Math.min(limit, 100);
	limit = Math.max(limit, 1);
	List<Integer> statusList = null;
	if (status != -1) { // 查看全部类型订单
	    statusList = new ArrayList<Integer>();
	    statusList.add(status);
	    if (status == OrderStatus.DONE) {
		statusList.add(OrderStatus.CAN);
	    }
	}

	List<MallOrder> mallOrders = mallOrderDao.findMallOrders(userId, statusList, startId, limit);

	List<Long> goodsIds = new ArrayList<Long>();
	List<Long> userIds = new ArrayList<Long>();
	for (MallOrder mallOrder : mallOrders) {
	    goodsIds.add(mallOrder.getGoodsId());
	    userIds.add(mallOrder.getUserId());
	}
	Map<Long, MallGoods> goodsMap = mallGoodsDao.findByIds(goodsIds);
	Map<Long, CbsUserResponse> userMap = null;

	if (userFlag) {
	    userMap = cbsUserService.findCsAccountMapByIds(userIds);
	}

	List<MallOrderResponse> mallOrderRespons = new ArrayList<MallOrderResponse>();
	for (MallOrder mallOrder : mallOrders) {
	    MallGoodsResponse goods = MallGoodsTransformUtil.toMallGoodsResponse(goodsMap.get(mallOrder.getGoodsId()), true);
	    MallOrderResponse order = MallOrderTransformUtil.transformMallOrder(mallOrder, goods);
	    if (userFlag) {
		order.setUser(userMap.get(mallOrder.getUserId()));
	    }
	    mallOrderRespons.add(order);
	}

	if (mallOrderRespons.size() == 0 || mallOrderRespons.size() < limit) { // 没有数据或数据不够返回-1
	    startId = -1L;
	}

	MallOrderListResponse reponse = new MallOrderListResponse();
	reponse.setOrders(mallOrderRespons);
	reponse.setNumber(mallOrderRespons.size());
	reponse.setStartId(startId);

	return reponse;

    }

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
    @Override
    public void buildMallOrder(Long goodsId, Long userId, String address, Integer goodsNum, Double postage, String goodsPro,
	    String userRemark, String ipaddrss) throws L99IllegalDataException, L99IllegalParamsException, JSONException {

	ParamemeterAssert.assertDataNotNull(goodsId, userId, address);

	// 商品库存锁逻辑
	String goodsKey = GoodsLock.STOCK_LOCK + goodsId;
	Integer goodsStock = memcacheService.get(goodsKey);
	if (goodsStock != null && goodsStock < goodsNum) {
	    // 商品库存不足
	    throw new L99IllegalDataException(GoodsMsg.CODE_GOODS_SELLOUT, GoodsMsg.GOODS_SELLOUT);
	}

	// 添加用户购买锁逻辑
	String userKey = GoodsLock.USER_LOCK + userId;
	Long userGoods = memcacheService.get(userKey);
	if (userGoods != null) {
	    // 重复下单
	    throw new L99IllegalDataException(OrderMsg.CODE_ORDER_EXIST, OrderMsg.KEY_ORDER_EXIST);
	}
	MallGoods mallGoods = mallGoodsDao.findById(goodsId);
	if (mallGoods == null) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	} else if (mallGoods.getStatus() == GoodsStatus.DEL || mallGoods.getStatus() == GoodsStatus.OFF) {
	    // 商品已下架
	    throw new L99IllegalDataException(GoodsMsg.CODE_GOODS_NO_FOUND, GoodsMsg.GOODS_NO_FOUND);
	} else if (mallGoods.getStock() < goodsNum) {
	    // 商品库存不足
	    throw new L99IllegalDataException(GoodsMsg.CODE_GOODS_SELLOUT, GoodsMsg.GOODS_SELLOUT);
	}

	// 手机充值卡验证手机号
	if (mallGoods.getType() == GoodsType.MOBILE_CARD) {
	    if (!RegexUtil.hasMatche(address, RegexUtil.MOBILE_ALL_REG)) {
		throw new L99IllegalDataException(UserMsg.CODE_USER_MOBILEERROR, UserMsg.KEY_USER_MOBILEERROR);
	    }
	}

	// 计算总价
	double amount = CommerceMath.mul(mallGoods.getPrice(), goodsNum);
	amount = CommerceMath.add(amount, mallGoods.getPostage());

	// 判断龙币余额
	GoldResponse goldResponse = moneyService.viewUserMoney(userId, null);
	if (goldResponse.getBalance() < amount) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_NOT_MONEY, BasicMsg.KEY_NOT_MONEY);
	}

	try {

	    // 添加用户购买锁
	    memcacheService.set(userKey, goodsId, GoodsLock.EXPIRE);

	    // 添加库存锁
	    memcacheService.set(goodsKey, mallGoods.getStock() - goodsNum, GoodsLock.EXPIRE);

	    String desc = String.format("商城兑换『%s』x %d 成功", mallGoods.getName(), goodsNum);
	    Long logId = moneyService.consumeMoney(userId, amount, desc, ipaddrss);
	    // add by lhx on 16-04-13
	    moneyStatisticService.insertUserConsumer(userId + "", -amount);
	    moneyStatisticService.insertSystemConsumer(userId + "", -amount);

	    MallOrder mallOrder = new MallOrder();
	    mallOrder.setUserId(userId);
	    mallOrder.setGoodsId(goodsId);
	    mallOrder.setGoodsPrice(mallGoods.getPrice());
	    mallOrder.setGoodsNum(goodsNum);
	    mallOrder.setPostage(mallGoods.getPostage());
	    mallOrder.setAmount(amount);
	    mallOrder.setOrderAddress(address);
	    mallOrder.setStatus(OrderStatus.PAY);
	    mallOrder.setGoodsPro(goodsPro);
	    mallOrder.setUserRemark(userRemark);
	    mallOrder.setCreateTime(new Date());
	    mallOrder.setLogId(logId);
	    boolean flag = mallOrderDao.insert(mallOrder);
	    if (!flag) {
		throw new L99IllegalDataException(String.valueOf(BasicMsg.CODE_BASIC_SERVCER), BasicMsg.KEY_BASIC_SERVCER);
	    }

	    // 修改商品库存
	    mallGoods.setStock(mallGoods.getStock() - goodsNum);
	    mallGoods.setSales(mallGoods.getSales() + goodsNum);
	    flag = mallGoodsDao.update(mallGoods);
	    if (!flag) {
		LOG.error(String.format("build order to update goods %s failed !!!", goodsId));
	    }

	    // add by lhx on 15-12-01 start
	    String info = String.format("刚刚购买成功了%d件%s奖品", goodsNum, mallGoods.getName());
	    frontPageService.addFrontPage(null, null, null, userId, info, null, null,
		    ContentConstants.FrontPage.TYPE_CONTENT_MESSAGE, null, null, null, null);
	    // add by lhx on 15-12-01 end

	} finally {
	    // 解除用户购买锁
	    memcacheService.delete(userKey);
	}

    }

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
    @Override
    public void sendMallOrder(Long orderId, Integer expressType, String expressNO) throws L99IllegalParamsException,
	    L99IllegalDataException {

	ParamemeterAssert.assertDataNotNull(orderId);

	MallOrder mallOrder = mallOrderDao.selectById(orderId);
	if (mallOrder == null) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	} else if (mallOrder.getStatus() == OrderStatus.DONE) {
	    // 订单已完成
	    throw new L99IllegalDataException(OrderMsg.CODE_ORDER_IS_DONE, OrderMsg.KEY_ORDER_IS_DONE);
	} else if (mallOrder.getStatus() == OrderStatus.CAN) {
	    // 订单已取消
	    throw new L99IllegalDataException(OrderMsg.CODE_ORDER_IS_CANCEL, OrderMsg.KEY_ORDER_IS_CANCEL);
	} else if (mallOrder.getStatus() == OrderStatus.INIT) {
	    // 订单未支付
	    throw new L99IllegalDataException(OrderMsg.CODE_ORDER_NOT_PAY, OrderMsg.KEY_ORDER_NOT_PAY);
	} else if (mallOrder.getStatus() == OrderStatus.SEND) { // 订单已被确认
	    return;
	}

	// 存储快递信息
	if (expressType != null && !StringUtils.isEmpty(expressNO)) {

	    if (ExpressConstants.shipperCodeMap.get(expressType) == null) {
		// 快递类型错误
		throw new L99IllegalParamsException(OrderMsg.CODE_ORDER_ERROR_TYPE, OrderMsg.KEY_ORDER_ERROR_TYPE);
	    }

	    MallExpress mallExpress = mallExpressDao.findById(orderId);
	    if (mallExpress == null) {
		mallExpress = new MallExpress();
		mallExpress.setOrderId(orderId);
		mallExpress.setUserId(mallOrder.getUserId());
		mallExpress.setExpressType(expressType);
		mallExpress.setExpressNO(expressNO);
		mallExpress.setState(ExpressConstants.State.START);
		mallExpress.setCreateTime(new Date());
		boolean flag = mallExpressDao.insert(mallExpress);
		if (!flag) {
		    throw new L99IllegalDataException(String.valueOf(BasicMsg.CODE_BASIC_SERVCER),
			    BasicMsg.KEY_BASIC_SERVCER);
		}
	    } else {
		mallExpress.setUserId(mallOrder.getUserId());
		mallExpress.setExpressType(expressType);
		mallExpress.setExpressNO(expressNO);
		mallExpress.setExpressInfo(null);
		mallExpress.setState(ExpressConstants.State.START);
		boolean flag = mallExpressDao.update(mallExpress);
		if (!flag) {
		    throw new L99IllegalDataException(String.valueOf(BasicMsg.CODE_BASIC_SERVCER),
			    BasicMsg.KEY_BASIC_SERVCER);
		}
	    }
	}

	mallOrder.setStatus(OrderStatus.SEND);
	mallOrder.setShopTime(new Date());
	boolean flag = mallOrderDao.update(mallOrder);
	if (!flag) {
	    throw new L99IllegalDataException(String.valueOf(BasicMsg.CODE_BASIC_SERVCER), BasicMsg.KEY_BASIC_SERVCER);
	}

    }

    /**
     * 确认收货
     * 
     * @param orderId
     * @param userId
     * @throws L99IllegalDataException
     * @throws L99IllegalParamsException
     */
    @Override
    public void confirmMallOrder(Long orderId, Long userId) throws L99IllegalDataException, L99IllegalParamsException {

	ParamemeterAssert.assertDataNotNull(orderId, userId);

	MallOrder mallOrder = mallOrderDao.selectById(orderId);
	if (mallOrder == null) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	} else if (mallOrder.getStatus() == OrderStatus.DONE) {
	    return;
	} else if (mallOrder.getStatus() == OrderStatus.DONE) {
	    // 订单已完成
	    throw new L99IllegalDataException(OrderMsg.CODE_ORDER_IS_DONE, OrderMsg.KEY_ORDER_IS_DONE);
	} else if (mallOrder.getStatus() == OrderStatus.CAN) {
	    // 订单已取消
	    throw new L99IllegalDataException(OrderMsg.CODE_ORDER_IS_CANCEL, OrderMsg.KEY_ORDER_IS_CANCEL);
	} else if (mallOrder.getStatus() == OrderStatus.INIT) {
	    // 订单未支付
	    throw new L99IllegalDataException(OrderMsg.CODE_ORDER_NOT_PAY, OrderMsg.KEY_ORDER_NOT_PAY);
	} else if (!mallOrder.getUserId().equals(userId)) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}
	mallOrder.setStatus(OrderStatus.DONE);
	mallOrder.setDoneTime(new Date());
	boolean flag = mallOrderDao.update(mallOrder);
	if (!flag) {
	    throw new L99IllegalDataException(String.valueOf(BasicMsg.CODE_BASIC_SERVCER), BasicMsg.KEY_BASIC_SERVCER);
	}
    }

    /**
     * 单个订单信息
     * 
     * @param orderId
     * @return
     * @throws L99IllegalParamsException
     * @throws JSONException
     * @throws L99NetworkException
     */
    @Override
    public MallOrderResponse findOneOrder(Long orderId) throws L99IllegalParamsException, L99NetworkException, JSONException {
	ParamemeterAssert.assertDataNotNull(orderId);
	MallOrder mallOrder = mallOrderDao.selectById(orderId);
	if (mallOrder == null) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}
	MallGoods mallGoods = mallGoodsDao.findById(mallOrder.getGoodsId());
	CbsUserResponse user = cbsUserService.getCbsUserByUserId(mallOrder.getUserId(), false);
	MallGoodsResponse goods = MallGoodsTransformUtil.toMallGoodsResponse(mallGoods, true);
	MallOrderResponse order = MallOrderTransformUtil.transformMallOrder(mallOrder, goods);
	order.setUser(user);
	return order;
    }

    /**
     * 取消订单
     * 
     * @param orderId
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     * @throws JSONException
     */
    @Override
    public void cancelOrder(Long orderId, String ipaddress) throws L99IllegalParamsException, L99IllegalDataException,
	    JSONException {
	ParamemeterAssert.assertDataNotNull(orderId);
	MallOrder mallOrder = mallOrderDao.selectById(orderId);
	if (mallOrder == null) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}
	mallOrder.setStatus(OrderStatus.CAN);
	mallOrder.setDoneTime(new Date());
	boolean flag = mallOrderDao.update(mallOrder);
	if (!flag) {
	    throw new L99IllegalDataException(String.valueOf(BasicMsg.CODE_BASIC_SERVCER), BasicMsg.KEY_BASIC_SERVCER);
	}
	String desc = "后台取消订单，返还" + mallOrder.getAmount() + "龙币";
	moneyService.earnMoney(mallOrder.getUserId(), mallOrder.getAmount(), desc, ipaddress, MoneyMissedType.MALL_CANCLE,
	        orderId.toString());
	moneyStatisticService.insertUserConsumer(mallOrder.getUserId() + "", mallOrder.getAmount());
	moneyStatisticService.insertSystemConsumer(mallOrder.getUserId() + "", mallOrder.getAmount());
	// 库存修改
	MallGoods mallGoods = mallGoodsDao.findById(mallOrder.getGoodsId());
	Integer goodsNum = mallOrder.getGoodsNum();
	// 商品库存锁逻辑
	String goodsKey = GoodsLock.STOCK_LOCK + mallOrder.getGoodsId();
	// 添加库存锁
	memcacheService.set(goodsKey, mallGoods.getStock() + goodsNum, GoodsLock.EXPIRE);
	mallGoods.setStock(mallGoods.getStock() + goodsNum);
	mallGoods.setSales(mallGoods.getSales() - goodsNum);
	flag = mallGoodsDao.update(mallGoods);
	if (!flag) {
	    LOG.error(String.format("build order to update goods %s failed !!!", mallOrder.getGoodsId()));
	}
    }

}
