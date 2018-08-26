package com.lifeix.cbs.api.impl.coupon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lifeix.cbs.api.bean.coupon.CouponUserListResponse;
import com.lifeix.cbs.api.bean.coupon.CouponUserResponse;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.exception.MsgCode.CouponMsg;
import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.api.common.util.CommerceMath;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestType;
import com.lifeix.cbs.api.common.util.CouponConstants;
import com.lifeix.cbs.api.common.util.CouponConstants.CouponLimit;
import com.lifeix.cbs.api.common.util.CouponConstants.CouponSystem;
import com.lifeix.cbs.api.common.util.CouponConstants.CouponType;
import com.lifeix.cbs.api.common.util.CouponConstants.RangeKey;
import com.lifeix.cbs.api.common.util.GoldLogEnum;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.common.util.RedisConstants;
import com.lifeix.cbs.api.common.util.RedisConstants.UserRedis;
import com.lifeix.cbs.api.dao.coupon.CouponDao;
import com.lifeix.cbs.api.dao.coupon.CouponUserDao;
import com.lifeix.cbs.api.dao.gold.GoldLogDao;
import com.lifeix.cbs.api.dto.coupon.Coupon;
import com.lifeix.cbs.api.dto.coupon.CouponUser;
import com.lifeix.cbs.api.dto.gold.GoldLog;
import com.lifeix.cbs.api.service.coupon.CouponUserService;
import com.lifeix.cbs.api.transform.CouponUserTransformUtil;
import com.lifeix.cbs.api.util.CouponSystemHelper;
import com.lifeix.cbs.message.service.notify.NotifyService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.framework.memcache.MemcacheService;
import com.lifeix.framework.redis.impl.RedisDataIdentify;
import com.lifeix.framework.redis.impl.RedisSortSetHandler;
import com.lifeix.user.beans.CustomResponse;

@Service("couponUserService")
public class CouponUserServiceImpl extends ImplSupport implements CouponUserService {

    public static final String COUPON_USER_KEY = "COUPON_USER_KEY";

    @Autowired
    private CouponUserDao couponUserDao;

    @Autowired
    private CouponDao couponDao;

    @Autowired
    private GoldLogDao goldLogDao;

    @Autowired
    protected MemcacheService memcacheService;

    @Autowired
    private RedisSortSetHandler redisSortSetHandler;

    @Autowired
    private NotifyService notifyService;

    /**
     * 发放或领取龙筹券
     */
    @Override
    public void grantCoupon(Long couponId, Long userId, boolean innerFlag) throws L99IllegalDataException,
	    L99IllegalParamsException {

	ParamemeterAssert.assertDataNotNull(couponId, userId);

	Coupon coupon = couponDao.findById(couponId);
	ParamemeterAssert.assertDataNotNull(coupon);
	// 非内部调用不能获取系统龙筹券
	if (!innerFlag && coupon.getType() == CouponType.COUPON_SYSTEM) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}

	grantCoupon(coupon, userId);
    }

    @Override
    public CouponUserResponse checkCoupon(Long couponId, Long userId) throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(couponId, userId);
	Coupon coupon = couponDao.findById(couponId);
	ParamemeterAssert.assertDataNotNull(coupon);
	if (coupon.getType() == CouponType.COUPON_SYSTEM) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}
	CouponUserResponse couponUserResponse = new CouponUserResponse();
	RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_USER, UserRedis.COUPON_USER_KEY);
	indentify.setIdentifyId(userId.toString());
	// 每个活动龙筹券用户最多领取一次
	Long score = redisSortSetHandler.zRevRank(indentify, coupon.getId());
	if (score != null) {
	    couponUserResponse.setHas(true);
	    return couponUserResponse;
	}
	// 库存
	int remainder = coupon.getSum() - coupon.getNum();
	couponUserResponse.setHas(false);
	couponUserResponse.setRemainder(remainder);
	return couponUserResponse;
    }

    /**
     * 根据面值和时间发放龙筹券
     */
    @Override
    public void settleCouponByPrice(Long userId, int price, int hour, String desc) throws L99IllegalParamsException,
	    L99IllegalDataException {
	Long couponId = CouponSystemHelper.getInstance().getCoupon(price, hour);
	if (couponId == null) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}
	grantCoupon(couponId, userId, true);
	try {
	    GoldLogEnum logEnum = GoldLogEnum.EARN;
	    GoldLog log = new GoldLog();
	    log.setUserId(userId);
	    log.setMoney(price);
	    JSONObject params = new JSONObject();
	    params.put("thing", desc);
	    log.setContent(CouponSystemHelper.replaceLogParams(logEnum.getTemplate(), params.toString()));
	    log.setType(logEnum.getType());
	    log.setIpaddress(null);
	    log.setLink(null);
	    log.setLogTime(new Date());
	    goldLogDao.insertAndGetKey(log);
	} catch (JSONException e) {
	    LOG.error(String.format("%d - settle coupon fail - %s", userId, e.getMessage()), e);
	}
    }

    /**
     * 根据back结算龙筹券
     */
    @Override
    public Long settleCouponByBack(Long userId, Double back, String desc) {
	// 如果结算金额为0 就不再写入日志
	if (back <= 0) {
	    return 0L;
	}

	int logMoney = 0;

	// 计算可获取的龙筹
	Map<Long, Integer> coupons = CouponSystemHelper.getCouponBack(back, CouponSystem.TIME_24);
	for (Iterator<Long> iterator = coupons.keySet().iterator(); iterator.hasNext();) {
	    Long couponId = iterator.next();
	    Integer num = coupons.get(couponId);
	    for (int i = 0; i < num; i++) {
		// 发放龙筹券
		try {
		    grantCoupon(couponId, userId, true);
		    logMoney = logMoney + CouponSystemHelper.getInstance().getPrice(couponId);
		} catch (Exception e) {
		    LOG.error(String.format("grant coupon %d to %d failed - %s", couponId, userId, e.getMessage()));
		}
	    }
	}
	Long logId = -1L;
	if (logMoney > 0) {
	    try {
		GoldLogEnum logEnum = GoldLogEnum.EARN;
		GoldLog log = new GoldLog();
		log.setUserId(userId);
		log.setMoney(logMoney);
		JSONObject params = new JSONObject();
		params.put("thing", desc);
		log.setContent(CouponSystemHelper.replaceLogParams(logEnum.getTemplate(), params.toString()));
		log.setType(logEnum.getType());
		log.setIpaddress(null);
		log.setLink(null);
		log.setLogTime(new Date());
		logId = goldLogDao.insertAndGetKey(log);
	    } catch (JSONException e) {
		LOG.error(String.format("%d - settle coupon fail - %s", userId, e.getMessage()), e);
	    }
	}
	return logId;
    }

    /**
     * 使用龙筹券
     */
    @Override
    public void useCoupon(Long couponUserId, Long userId, boolean longbi, Double bet, Integer contestType, Long cupId,
	    Long contestId, String desc) throws L99IllegalParamsException, L99IllegalDataException {

	ParamemeterAssert.assertDataNotNull(userId, couponUserId);

	CouponUser couponUser = couponUserDao.findById(couponUserId);

	// 检查龙筹券是否有效
	if (couponUser == null) {
	    throw new L99IllegalDataException(CouponMsg.CODE_COUPON_FAIL, CouponMsg.KEY_COUPON_FAIL);
	} else if (couponUser.isUsed()) { // 是否被使用
	    throw new L99IllegalDataException(CouponMsg.CODE_COUPON_USED, CouponMsg.KEY_COUPON_USED);
	} else if (CbsTimeUtils.upgradeTime(couponUser.getEndTime(), Calendar.MINUTE, 30).before(new Date())) { // 是否过期
	    throw new L99IllegalDataException(CouponMsg.CODE_COUPON_PASS, CouponMsg.KEY_COUPON_PASS);
	} else if (couponUser.getUserId().longValue() != userId.longValue()) { // 是否拥有
	    throw new L99IllegalDataException(CouponMsg.CODE_COUPON_FAIL, CouponMsg.KEY_COUPON_FAIL);
	}

	Coupon coupon = couponDao.findById(couponUser.getCouponId());
	ParamemeterAssert.assertDataNotNull(coupon);

	// 判断龙币是否符合龙筹券要求
	if (longbi) {
	    double minMoney = CommerceMath.mul(coupon.getPrice(), coupon.getProportion());
	    if (CommerceMath.sub(bet, minMoney) < 0) {
		// 龙筹券所需龙币不足
		throw new L99IllegalDataException(CouponMsg.CODE_COUPON_PROPORTION, CouponMsg.KEY_COUPON_PROPORTION);
	    }
	}

	// 龙筹券的使用范围判断
	if (coupon.getRangeKey() != RangeKey.COUPON_DEFAULT) {
	    ParamemeterAssert.assertDataNotNull(contestType, coupon.getRangeValue());
	    List<String> rangeValue = Arrays.asList(coupon.getRangeValue().split(","));
	    // 先判断是否满足指定赛事
	    if (coupon.getRangeKey() == RangeKey.FB_COUPON_CONTEST) { // 足球赛事券
		ParamemeterAssert.assertDataNotNull(contestId);
		if (contestType != ContestType.FOOTBALL || !rangeValue.contains(String.valueOf(contestId))) {
		    throw new L99IllegalDataException(CouponMsg.CODE_COUPON_FAIL_NOT_RANGE,
			    CouponMsg.KEY_COUPON_FAIL_NOT_RANGE);
		}
	    } else if (coupon.getRangeKey() == RangeKey.FB_COUPON_CUP) { // 足球联赛券
		ParamemeterAssert.assertDataNotNull(cupId);
		if (contestType != ContestType.FOOTBALL || !rangeValue.contains(String.valueOf(cupId))) {
		    throw new L99IllegalDataException(CouponMsg.CODE_COUPON_FAIL_NOT_RANGE,
			    CouponMsg.KEY_COUPON_FAIL_NOT_RANGE);
		}
	    } else if (coupon.getRangeKey() == RangeKey.BB_COUPON_CONTEST) { // 篮球赛事券
		ParamemeterAssert.assertDataNotNull(contestId);
		if (contestType != ContestType.BASKETBALL || !rangeValue.contains(String.valueOf(contestId))) {
		    throw new L99IllegalDataException(CouponMsg.CODE_COUPON_FAIL_NOT_RANGE,
			    CouponMsg.KEY_COUPON_FAIL_NOT_RANGE);
		}
	    } else if (coupon.getRangeKey() == RangeKey.BB_COUPON_CUP) { // 篮球联赛券
		ParamemeterAssert.assertDataNotNull(cupId);
		if (contestType != ContestType.BASKETBALL || !rangeValue.contains(String.valueOf(cupId))) {
		    throw new L99IllegalDataException(CouponMsg.CODE_COUPON_FAIL_NOT_RANGE,
			    CouponMsg.KEY_COUPON_FAIL_NOT_RANGE);
		}
	    } else if (coupon.getRangeKey() == RangeKey.COUPON_YY) { // 押押赛事券
		ParamemeterAssert.assertDataNotNull(contestId);
		if (contestType != ContestType.YAYA || !rangeValue.contains(String.valueOf(contestId))) {
		    throw new L99IllegalDataException(CouponMsg.CODE_COUPON_FAIL_NOT_RANGE,
			    CouponMsg.KEY_COUPON_FAIL_NOT_RANGE);
		}
	    }
	}
	couponUser.setUsed(true);
	couponUser.setUpdateTime(new Date());
	boolean flag = couponUserDao.update(couponUser);
	if (!flag) {
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}
	try {// 写日志
	    GoldLogEnum logEnum = GoldLogEnum.PAYMENT;
	    GoldLog log = new GoldLog();
	    log.setUserId(userId);
	    log.setMoney(couponUser.getPrice());
	    JSONObject params = new JSONObject();
	    params.put("thing", desc);
	    log.setContent(CouponSystemHelper.replaceLogParams(logEnum.getTemplate(), params.toString()));
	    log.setType(logEnum.getType());
	    log.setIpaddress(null);
	    log.setLink(null);
	    log.setLogTime(new Date());
	    goldLogDao.insertAndGetKey(log);
	} catch (JSONException e) {
	    LOG.error(String.format("%d - use coupon fail - %s", userId, e.getMessage()), e);
	}
    }

    /**
     * 获得龙筹券详情
     */
    @Override
    public CouponUserResponse findCouponDetail(Long id, boolean assertFlag) throws L99IllegalParamsException,
	    L99IllegalDataException {
	ParamemeterAssert.assertDataNotNull(id);
	CouponUser couponUser = couponUserDao.findById(id);

	ParamemeterAssert.assertDataNotNull(couponUser);
	// 检查龙筹券是否有效
	if (assertFlag) {
	    if (couponUser.isUsed()) { // 是否被使用
		throw new L99IllegalDataException(CouponMsg.CODE_COUPON_USED, CouponMsg.KEY_COUPON_USED);
	    } else if (couponUser.getEndTime().before(new Date())) { // 是否过期
		throw new L99IllegalDataException(CouponMsg.CODE_COUPON_PASS, CouponMsg.KEY_COUPON_PASS);
	    }
	}

	Coupon coupon = couponDao.findById(couponUser.getCouponId());
	ParamemeterAssert.assertDataNotNull(coupon);

	return CouponUserTransformUtil.transformCouponUser(couponUser, coupon);
    }

    /**
     * 获取用户可下单的龙筹券列表
     */
    public CouponUserListResponse findUserBetCoupons(Long userId, Long contestId, Long cupId, Integer contestType) {
	return findUserBetCoupons(userId, contestId, cupId, contestType, null);
    }

    /**
     * 获取用户指定面额可下单的龙筹券列表
     */
    @Override
    public CouponUserListResponse findUserBetCoupons(Long userId, Long contestId, Long cupId, Integer contestType,
	    List<Integer> filterPrice) {

	CouponUserListResponse response = new CouponUserListResponse();

	List<CouponUserResponse> coupons = new ArrayList<CouponUserResponse>();

	// 产品约定 性能考虑 只需查用户最近的100张可用龙筹券
	List<CouponUser> couponUsers = couponUserDao.findUserActiveCoupon(userId, filterPrice, null,
	        CouponLimit.COUPON_AVTIVE);
	if (couponUsers.size() > 0) {
	    // 先填充代金券详情信息
	    List<Long> couponIds = new ArrayList<Long>();
	    for (CouponUser couponUser : couponUsers) {
		couponIds.add(couponUser.getCouponId());
	    }
	    Map<Long, Coupon> couponMap = couponDao.findMapByIds(couponIds);
	    // 排序集合
	    SortByTimeRangkeyPrice sortByTimeRangkeyPrice = new SortByTimeRangkeyPrice();
	    SortedSet<CouponUser> set = new TreeSet<CouponUser>(sortByTimeRangkeyPrice);
	    // 是否指定面额
	    boolean fixedPriceFlag = filterPrice != null && filterPrice.size() > 0;
	    // 过滤不符合赛事的龙筹券
	    for (CouponUser couponUser : couponUsers) {
		Coupon coupon = couponMap.get(couponUser.getCouponId());
		if (coupon == null) { // 龙筹券未找到 异常数据
		    LOG.warn(String.format("[%d] find coupon [%d] not found", couponUser.getId(), couponUser.getCouponId()));
		    continue;
		}
		if (fixedPriceFlag && !filterPrice.contains(coupon.getPrice())) {
		    // 如果有固定面额限制且价值不符合面额，直接跳过本次循环
		    continue;
		}
		int rangKey = coupon.getRangeKey();
		couponUser.setRangeKey(rangKey);
		if (rangKey == RangeKey.COUPON_DEFAULT) { // 通用券
		    set.add(couponUser);
		} else if (contestType == ContestType.FOOTBALL) { // 足球赛事
		    if (rangKey == RangeKey.FB_COUPON_CUP || rangKey == RangeKey.FB_COUPON_CONTEST) {
			set.add(couponUser);
		    }
		} else if (contestType == ContestType.BASKETBALL) { // 篮球赛事
		    if (rangKey == RangeKey.BB_COUPON_CUP || rangKey == RangeKey.BB_COUPON_CONTEST) {
			set.add(couponUser);
		    }
		} else if (contestType == ContestType.YAYA) { // 押押赛事
		    if (rangKey == RangeKey.COUPON_YY) {
			set.add(couponUser);
		    }
		}
	    }

	    if (set.size() > 0) {
		// 上一次的价格
		int lastPrice = 0;

		for (CouponUser couponUser : set) {
		    Long coupontId = couponUser.getCouponId();
		    Coupon coupon = couponMap.get(coupontId);
		    List<String> rangeValue;
		    if (coupon != null) {
			int price = couponUser.getPrice();
			// 与上一次的价格做比较，如果相同，表明已经存在该面额的券，跳过该次循环，否则再进一步进行判断
			if (price != lastPrice) {
			    if (coupon.getRangeKey() != CouponConstants.RangeKey.COUPON_DEFAULT) {
				rangeValue = Arrays.asList(coupon.getRangeValue().split(","));
				if (rangeValue.contains(String.valueOf(contestId))) { // 先判断是否满足指定赛事
				    lastPrice = price;
				    coupons.add(CouponUserTransformUtil.transformCouponUser(couponUser, coupon));
				} else if (rangeValue.contains(String.valueOf(cupId))) { // 在判断是否满足杯赛
				    lastPrice = price;
				    coupons.add(CouponUserTransformUtil.transformCouponUser(couponUser, coupon));
				}
			    } else {
				lastPrice = price;
				coupons.add(CouponUserTransformUtil.transformCouponUser(couponUser, coupon));
			    }
			}
		    }
		}
	    }
	}

	response.setCoupons(coupons);

	return response;
    }

    /**
     * 向指定用户发放龙筹券
     */
    @Override
    public CustomResponse sendCouponToUser(Long couponId, String[] userIds, Integer num) throws L99IllegalParamsException,
	    L99IllegalDataException {

	ParamemeterAssert.assertDataNotNull(couponId, userIds);

	Coupon coupon = couponDao.findById(couponId);

	ParamemeterAssert.assertDataNotNull(coupon);

	CustomResponse response = new CustomResponse();

	if (coupon.getType() == CouponType.COUPON_PROCEEDINGS) {
	    List<String> sucessArray = new ArrayList<String>();
	    List<String> failedArray = new ArrayList<String>();

	    for (String userId : userIds) {
		try {
		    Long accountId = Long.valueOf(userId);
		    grantCoupon(coupon, accountId);
		    // 活动龙筹券只能拥有一张
		    // num = (num == null) ? 1 : num ;
		    // for (int i = 0; i < num; i++) {
		    // grantCoupon(coupon, accountId);
		    // }
		    sucessArray.add(userId);
		    // 记录日志
		    GoldLogEnum logEnum = GoldLogEnum.SYSTEM;
		    GoldLog log = new GoldLog();
		    log.setUserId(accountId);
		    log.setMoney(coupon.getPrice() * num);
		    JSONObject params = new JSONObject();
		    params.put("thing", "系统后台发送");
		    log.setContent(CouponSystemHelper.replaceLogParams(logEnum.getTemplate(), params.toString()));
		    log.setType(logEnum.getType());
		    log.setIpaddress(null);
		    log.setLink(null);
		    log.setLogTime(new Date());
		    goldLogDao.insertAndGetKey(log);
		} catch (Exception e) {
		    failedArray.add(userId);
		    LOG.error(String.format("%s - give coupon fail - %s", userId, e.getMessage()), e);
		}
	    }
	    response.put("sucess", sucessArray);
	    response.put("failed", failedArray);
	} else if (coupon.getType() == CouponType.COUPON_SYSTEM) {
	    for (String userId : userIds) {
		if (num != null) {
		    this.sendCouponToUser(couponId, Long.valueOf(userId), num, "系统后台发送");
		} else {
		    this.sendCouponToUser(couponId, Long.valueOf(userId), 1, "系统后台发送");
		}
	    }
	}

	return response;
    }

    @Override
    public void sendCouponToUser(Long couponId, Long userId, int num, String desc) throws L99IllegalParamsException,
	    L99IllegalDataException {
	ParamemeterAssert.assertDataNotNull(couponId, userId);

	Coupon coupon = couponDao.findById(couponId);
	ParamemeterAssert.assertDataNotNull(coupon);

	if (!coupon.isValid()) {
	    throw new L99IllegalDataException(CouponMsg.CODE_COUPON_PASS, CouponMsg.KEY_COUPON_PASS);
	}

	Integer hour = coupon.getHour();
	Date now = new Date();
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(now);
	calendar.add(Calendar.HOUR_OF_DAY, hour);

	CouponUser user;
	List<CouponUser> list = new ArrayList<CouponUser>(num);
	Random random = new Random();
	for (int i = 0; i < num; i++) {
	    user = new CouponUser();
	    user.setCouponId(coupon.getId());
	    user.setUserId(userId);
	    user.setPrice(coupon.getPrice());
	    user.setStartTime(now);
	    // 防止结束时间相同导致后面取出来时遇到排序不起作用而导致券的数量减少
	    calendar.add(Calendar.SECOND, random.nextInt(600));
	    user.setEndTime(calendar.getTime());
	    user.setUsed(false);
	    user.setProportion(coupon.getProportion());
	    user.setUpdateTime(now);
	    list.add(user);
	}

	boolean flag = couponUserDao.insertByBatch(list);
	if (!flag) {
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	} else {
	    try {
		GoldLogEnum logEnum = GoldLogEnum.EARN;
		if ("系统后台发送".equals(desc)) {
		    logEnum = GoldLogEnum.SYSTEM;
		}
		GoldLog log = new GoldLog();
		log.setUserId(userId);
		log.setMoney(coupon.getPrice() * num);
		JSONObject params = new JSONObject();
		params.put("thing", desc);
		log.setContent(CouponSystemHelper.replaceLogParams(logEnum.getTemplate(), params.toString()));
		log.setType(logEnum.getType());
		log.setIpaddress(null);
		log.setLink(null);
		log.setLogTime(new Date());
		goldLogDao.insertAndGetKey(log);
	    } catch (JSONException e) {
		LOG.error(String.format("%d - give coupon fail - %s", userId, e.getMessage()), e);
	    }
	}
    }

    /**
     * 用户龙筹券列表
     */
    @Override
    public CouponUserListResponse findUserCouponList(Long userId, Boolean isUsed, Long startId, Integer limit) {

	limit = Math.min(limit, 100);
	limit = Math.max(limit, 1);

	List<CouponUser> couponUsers = null;

	if (isUsed) { // 可用的龙筹券
	    couponUsers = couponUserDao.findUserActiveCoupon(userId, null, startId, limit);
	} else {
	    couponUsers = couponUserDao.findUserExpiredCoupons(userId, startId, limit);
	}

	CouponUserListResponse response = new CouponUserListResponse();
	if (couponUsers.size() > 0) {
	    List<CouponUserResponse> coupons = new ArrayList<CouponUserResponse>();
	    List<Long> couponIds = new ArrayList<Long>();
	    for (CouponUser couponUser : couponUsers) {
		couponIds.add(couponUser.getCouponId());
	    }
	    Map<Long, Coupon> couponMap = couponDao.findMapByIds(couponIds);
	    for (CouponUser couponUser : couponUsers) {
		coupons.add(CouponUserTransformUtil.transformCouponUser(couponUser, couponMap.get(couponUser.getCouponId())));
		startId = couponUser.getId();
	    }

	    if (coupons.size() == 0 || coupons.size() < limit) { // 没有数据或数据不够返回-1
		startId = -1L;
	    }

	    response.setCoupons(coupons);
	    response.setStartId(startId);
	}

	response.setLimit(limit);
	return response;
    }

    /**
     * 发送龙筹券
     */
    private void grantCoupon(Coupon coupon, Long userId) throws L99IllegalDataException {

	// 龙筹券是否有效
	if (!coupon.isValid()) {
	    throw new L99IllegalDataException(CouponMsg.CODE_COUPON_PASS, CouponMsg.KEY_COUPON_PASS);
	}

	RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_USER, UserRedis.COUPON_USER_KEY);
	indentify.setIdentifyId(userId.toString());
	if (coupon.getType() == CouponType.COUPON_PROCEEDINGS) {

	    // 活动龙筹券是否有库存
	    if (coupon.getSum() <= coupon.getNum()) {
		throw new L99IllegalDataException(CouponMsg.CODE_COUPON_NUM, CouponMsg.KEY_COUPON_NUM);
	    }

	    // 每个活动龙筹券用户最多领取一次
	    Long score = redisSortSetHandler.zRevRank(indentify, coupon.getId());
	    if (score != null) {
		throw new L99IllegalDataException(CouponMsg.CODE_COUPON_RECEIVED, CouponMsg.KEY_COUPON_RECEIVED);
	    }
	}

	Integer hour = coupon.getHour();
	Date now = new Date();
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(now);
	calendar.add(Calendar.HOUR_OF_DAY, hour);

	CouponUser user = new CouponUser();
	user.setCouponId(coupon.getId());
	user.setUserId(userId);
	user.setPrice(coupon.getPrice());
	user.setStartTime(now);
	user.setEndTime(calendar.getTime());
	user.setUsed(false);
	user.setProportion(coupon.getProportion());
	user.setUpdateTime(now);
	boolean flag = couponUserDao.insert(user);
	if (!flag) {
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}

	if (coupon.getType() == CouponType.COUPON_PROCEEDINGS) {

	    // 每个活动龙筹券用户最多领取一次
	    redisSortSetHandler.zAdd(indentify, coupon.getId().toString(), System.currentTimeMillis());

	    // 活动龙筹券修改库存计数
	    couponDao.incrementCoupon(coupon.getId());
	}

    }

    // 重写排序规则
    private class SortByTimeRangkeyPrice implements Comparator<Object> {
	// 先按价格排，后按使用类型排，最后按失效时间排（这个可以任意调整）
	// |价格|类型|失效时间 把三者拼接成一个long整形的数字
	@Override
	public int compare(Object o1, Object o2) {
	    CouponUser th1 = (CouponUser) o1;
	    CouponUser th2 = (CouponUser) o2;
	    // 失效时间
	    long now = new Date().getTime();
	    Long inter1 = (th1.getEndTime().getTime() - now) / 1000;
	    String time1 = String.valueOf(inter1);
	    Long inter2 = (th2.getEndTime().getTime() - now) / 1000;
	    String time2 = String.valueOf(inter2);

	    String res1 = fillTime(time1);
	    String res2 = fillTime(time2);

	    String seed1 = th1.getPrice() + "" + th1.getRangeKey() + res1;
	    String seed2 = th2.getPrice() + "" + th2.getRangeKey() + res2;

	    return Long.valueOf(seed1).compareTo(Long.valueOf(seed2));
	}

	private String fillTime(String time) {
	    String[] times = { "0", "00", "000", "0000", "00000", "000000" };
	    int length = 7 - time.length();
	    return times[length - 1] + time;

	}
    }

    @Override
    public List<Object[]> findUserZodiacGameBetCoupons(Long userId, List<Integer> filterPrice) {
	// 要返回的结果集合
	List<Object[]> lists = new ArrayList<Object[]>();
	int priceSize = filterPrice.size();
	// 产品约定 性能考虑 只需查用户最近的100张可用龙筹券
	List<CouponUser> couponUsers = couponUserDao.findUserActiveCoupon(userId, filterPrice, null,
	        CouponLimit.COUPON_AVTIVE);
	if (couponUsers.size() > 0) {
	    // 先填充代金券详情信息
	    List<Long> couponIds = new ArrayList<Long>();
	    for (CouponUser couponUser : couponUsers) {
		couponIds.add(couponUser.getCouponId());
	    }
	    Map<Long, Coupon> couponMap = couponDao.findMapByIds(couponIds);
	    // 排序集合
	    SortByTimeRangkeyPrice sortByTimeRangkeyPrice = new SortByTimeRangkeyPrice();
	    SortedSet<CouponUser> set = new TreeSet<CouponUser>(sortByTimeRangkeyPrice);
	    // 过滤，只取默认龙筹券
	    for (CouponUser couponUser : couponUsers) {
		Coupon coupon = couponMap.get(couponUser.getCouponId());
		if (coupon == null) { // 龙筹券未找到 异常数据
		    LOG.warn(String.format("[%d] find coupon [%d] not found", couponUser.getId(), couponUser.getCouponId()));
		    continue;
		}
		int rangKey = coupon.getRangeKey();
		couponUser.setRangeKey(rangKey);
		// 通用券
		if (rangKey == RangeKey.COUPON_DEFAULT) {
		    // 放入排序集合中
		    set.add(couponUser);
		}
	    }

	    // 上一次的价格
	    Integer lastPrice = filterPrice.get(0);
	    List<Long> couponUserIdList = new ArrayList<Long>();
	    // ids集合暂时存到map
	    Map<Integer, Object[]> map = new HashMap<Integer, Object[]>();

	    for (CouponUser couponUser : set) {
		Long coupontUserId = couponUser.getId();
		int price = couponUser.getPrice();
		// 与上一次的价格做比较，如果相同，表明已经存在该面额的券，否则再进一步进行判断
		if (price == lastPrice) {
		    couponUserIdList.add(coupontUserId);
		} else {
		    map.put(lastPrice, couponUserIdList.toArray());
		    couponUserIdList.clear();
		    couponUserIdList.add(coupontUserId);
		    lastPrice = price;
		}
	    }
	    map.put(lastPrice, couponUserIdList.toArray());
	    for (Integer integer : filterPrice) {
		Object[] tmp = map.get(integer);
		if (tmp == null) {
		    lists.add(new Object[] {});
		} else {
		    lists.add(tmp);
		}
	    }
	} else {
	    // 初始化 都塞入空值
	    for (int i = 0; i < priceSize; i++) {
		lists.add(new Object[] {});
	    }
	}
	return lists;
    }

    @Override
    @Transactional(rollbackFor = L99IllegalDataException.class)
    public int useManyCoupons(String couponUserIds, Long userId, String desc) throws L99IllegalParamsException,
	    L99IllegalDataException {

	ParamemeterAssert.assertDataNotNull(couponUserIds, userId);

	String[] idArray = couponUserIds.split(",");
	List<Long> ids = new ArrayList<Long>();
	for (String id : idArray) {
	    if (StringUtils.isNotEmpty(id)) {
		ids.add(Long.valueOf(id));
	    }
	}
	if (ids.size() <= 0) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}

	List<CouponUser> couponUsers = couponUserDao.findCouponUsersByIds(ids);
	if (couponUsers.size() <= 0) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}

	int logMoney = 0;
	for (CouponUser couponUser : couponUsers) {
	    // 检查龙筹券是否有效
	    if (couponUser.isUsed()) { // 是否被使用
		throw new L99IllegalDataException(CouponMsg.CODE_COUPON_USED, CouponMsg.KEY_COUPON_USED);
	    } else if (CbsTimeUtils.upgradeTime(couponUser.getEndTime(), Calendar.MINUTE, 30).before(new Date())) { // 是否过期
		throw new L99IllegalDataException(CouponMsg.CODE_COUPON_PASS, CouponMsg.KEY_COUPON_PASS);
	    } else if (couponUser.getUserId().longValue() != userId.longValue()) { // 是否拥有
		throw new L99IllegalDataException(CouponMsg.CODE_COUPON_FAIL, CouponMsg.KEY_COUPON_FAIL);
	    }
	    couponUser.setUsed(true);
	    couponUser.setUpdateTime(new Date());
	    boolean flag = couponUserDao.update(couponUser);
	    if (!flag) {
		throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	    }
	    logMoney += couponUser.getPrice();
	}

	try {// 写日志
	    GoldLogEnum logEnum = GoldLogEnum.PAYMENT;
	    GoldLog log = new GoldLog();
	    log.setUserId(userId);
	    log.setMoney(logMoney);
	    JSONObject params = new JSONObject();
	    params.put("thing", desc);
	    log.setContent(CouponSystemHelper.replaceLogParams(logEnum.getTemplate(), params.toString()));
	    log.setType(logEnum.getType());
	    log.setIpaddress(null);
	    log.setLink(null);
	    log.setLogTime(new Date());
	    goldLogDao.insertAndGetKey(log);
	} catch (JSONException e) {
	    LOG.error(String.format("%d - use coupon fail - %s", userId, e.getMessage()), e);
	}
	return logMoney;

    }

}
