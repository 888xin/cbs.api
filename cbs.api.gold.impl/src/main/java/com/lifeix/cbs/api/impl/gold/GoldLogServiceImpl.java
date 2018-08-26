package com.lifeix.cbs.api.impl.gold;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lifeix.cbs.api.bean.gold.GoldLogListResponse;
import com.lifeix.cbs.api.bean.gold.GoldLogResponse;
import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.cbs.api.common.exception.MsgCode;
import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.util.ContestConstants.TempletId;
import com.lifeix.cbs.api.common.util.CouponConstants.CouponSystem;
import com.lifeix.cbs.api.common.util.GoldLogEnum;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.common.util.RedisConstants;
import com.lifeix.cbs.api.common.util.RedisConstants.UserRedis;
import com.lifeix.cbs.api.dao.gold.GoldDao;
import com.lifeix.cbs.api.dao.gold.GoldLogDao;
import com.lifeix.cbs.api.dto.gold.Gold;
import com.lifeix.cbs.api.dto.gold.GoldLog;
import com.lifeix.cbs.api.service.coupon.CouponUserService;
import com.lifeix.cbs.api.service.gold.GoldLogService;
import com.lifeix.cbs.api.service.user.CbsUserService;
import com.lifeix.cbs.api.transform.GoldTransformUtil;
import com.lifeix.cbs.api.util.CouponSystemHelper;
import com.lifeix.cbs.message.service.notify.NotifyService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;
import com.lifeix.framework.redis.impl.RedisDataIdentify;
import com.lifeix.framework.redis.impl.RedisSortSetHandler;

@Service("goldLogService")
public class GoldLogServiceImpl extends ImplSupport implements GoldLogService {

    @Autowired
    private GoldLogDao goldLogDao;

    @Autowired
    private GoldDao goldDao;

    @Autowired
    private CbsUserService cbsUserService;

    @Autowired
    private CouponUserService couponUserService;

    @Autowired
    private RedisSortSetHandler redisSortSetHandler;

    @Autowired
    private NotifyService notifyService;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public GoldLogListResponse findGoldLogList(Long userId, Long startId, int limit) throws L99IllegalParamsException,
	    L99IllegalDataException, JSONException {

	ParamemeterAssert.assertDataNotNull(userId);
	limit = Math.min(limit, 100);
	limit = Math.max(limit, 1);

	List<GoldLog> logs = goldLogDao.findGoldLogs(userId, startId, limit);
	Long minId = null;
	// 获取最小的logId
	for (GoldLog goldLog : logs) {
	    if (minId == null) {
		minId = goldLog.getLogId();
	    } else if (goldLog.getLogId() < minId) {
		minId = goldLog.getLogId();
	    }
	}

	List<GoldLogResponse> gold_logs = new ArrayList<GoldLogResponse>();
	for (GoldLog log : logs) {
	    gold_logs.add(GoldTransformUtil.transformGoldLog(log, null));
	}

	GoldLogListResponse reponse = new GoldLogListResponse();

	reponse.setGold_logs(gold_logs);
	// 判断是否还有数据，设置startId
	if (goldLogDao.findGoldLogs(userId, minId, limit).size() > 0) {
	    reponse.setStartId(minId);
	} else {
	    reponse.setStartId(0L);
	}
	reponse.setLimit(limit);
	return reponse;

    }

    @Override
    public GoldLogListResponse findGoldLogBytime(Long userId, Long startId, int limit, Date createTime, Date endTime)
	    throws L99IllegalParamsException, L99IllegalDataException, JSONException {
	ParamemeterAssert.assertDataNotNull(userId);
	limit = Math.min(limit, 100);
	limit = Math.max(limit, 1);

	List<GoldLog> logs = goldLogDao.findGoldLogs(userId, startId, limit, createTime, endTime);
	Long maxId = 0L;
	// 获取最大的logId
	for (GoldLog goldLog : logs) {
	    if (goldLog.getLogId() > maxId) {
		maxId = goldLog.getLogId();
	    }
	}

	List<GoldLogResponse> gold_logs = new ArrayList<GoldLogResponse>();
	for (GoldLog log : logs) {
	    gold_logs.add(GoldTransformUtil.transformGoldLog(log, null));
	}

	GoldLogListResponse reponse = new GoldLogListResponse();

	reponse.setGold_logs(gold_logs);
	// 判断是否还有数据，设置startId
	if (goldLogDao.findGoldLogs(userId, maxId, limit).size() > 0) {
	    reponse.setStartId(maxId);
	} else {
	    reponse.setStartId(0L);
	}
	reponse.setLimit(limit);
	return reponse;

    }

    @Override
    public GoldLogListResponse findSystemGoldLogList(Long userId, Long roleId, Long startId, Integer limit, Date beginTime,
	    Date endTime) {
	limit = Math.min(limit, 100);
	limit = Math.max(limit, 1);

	List<GoldLog> logs = goldLogDao.findSystemGoldLogs(userId, roleId, startId, limit, beginTime, endTime);
	Long minId = null;
	// 获取最小的logId
	for (GoldLog goldLog : logs) {
	    if (minId == null) {
		minId = goldLog.getLogId();
	    } else if (goldLog.getLogId() < minId) {
		minId = goldLog.getLogId();
	    }
	}
	List<GoldLogResponse> gold_logs = new ArrayList<GoldLogResponse>();
	List<Long> userIds = new ArrayList<Long>();
	for (GoldLog log : logs) {
	    userIds.add(log.getUserId());
	}
	Map<Long, CbsUserResponse> users = cbsUserService.findCsAccountMapByIds(userIds);
	for (GoldLog log : logs) {
	    gold_logs.add(GoldTransformUtil.transformGoldLog(log, users.get(log.getUserId())));
	}
	GoldLogListResponse reponse = new GoldLogListResponse();
	reponse.setGold_logs(gold_logs);
	// 判断是否还有数据，设置startId
	if (goldLogDao.findSystemGoldLogs(userId, roleId, minId, limit, beginTime, endTime).size() > 0) {
	    reponse.setStartId(minId);
	} else {
	    reponse.setStartId(0L);
	}
	reponse.setLimit(limit);
	return reponse;
    }

    @Override
    public GoldLogListResponse findSystemLogsDetail(Long logId, Long longNo, Date startDate, Date endDate, Long startId,
	    Integer limit, Integer[] types) throws L99IllegalParamsException, JSONException, L99NetworkException,
	    L99IllegalDataException {
	GoldLogListResponse response = new GoldLogListResponse();
	List<GoldLogResponse> gold_logs = new ArrayList<GoldLogResponse>();
	Long userId = null;
	if (longNo != null) {
	    CbsUserResponse user = cbsUserService.getCbsUserByLongNo(longNo);
	    if (user != null) {
		userId = user.getUser_id();
	    } else {
		throw new L99IllegalDataException(MsgCode.UserMsg.CODE_USER_ACCOUNT_NOT_FOUND,
		        MsgCode.UserMsg.KEY_USER_ACCOUNT_NOT_FOUND);
	    }
	}
	List<GoldLog> logs = goldLogDao.findSystemLogDetail(logId, userId, startDate, endDate, startId, limit, types);

	List<Long> userIds = new ArrayList<Long>();
	for (GoldLog log : logs) {
	    userIds.add(log.getUserId());
	}

	Map<Long, CbsUserResponse> users = cbsUserService.findCsAccountMapByIds(userIds);
	for (GoldLog log : logs) {
	    gold_logs.add(GoldTransformUtil.transformGoldLog(log, users.get(log.getUserId())));
	}

	response.setGold_logs(gold_logs);
	return response;
    }

    /**
     * 转换用户旧版龙筹余额
     * 
     * @param userId
     */
    @Override
    public void replaceGold(Long userId) {
	RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_USER, UserRedis.COUPON_SEND);
	Double send = redisSortSetHandler.zScore(indentify, userId);
	if (send == null || send != 1) {// redis标记为未返还或不存在
	    boolean update = true;
	    Gold gold = goldDao.findById(userId);
	    if (gold != null && gold.getStatus() == 0) {// 数据库中标记为未返还
		double balance = gold.getBalance() / 100;
		Map<Long, Integer> coupons = CouponSystemHelper.getCouponBack(balance, CouponSystem.TIME_72);// 计算可获得龙筹卷
		int logMoney = 0;
		for (Iterator<Long> iterator = coupons.keySet().iterator(); iterator.hasNext();) {
		    Long couponId = (Long) iterator.next();
		    Integer num = coupons.get(couponId);
		    for (int i = 0; i < num; i++) {
			// 发放龙筹券
			try {
			    couponUserService.grantCoupon(couponId, gold.getUserId(), true);
			    logMoney = logMoney + CouponSystemHelper.getInstance().getPrice(couponId);
			} catch (Exception e) {
			    LOG.error(String.format("grant coupon %d to %d failed - %s", couponId, gold.getUserId(),
				    e.getMessage()));
			}
		    }
		}
		if (logMoney > 0) {
		    try {
			GoldLogEnum logEnum = GoldLogEnum.SYSTEM;
			GoldLog log = new GoldLog();
			log.setUserId(gold.getUserId());
			log.setMoney(logMoney);
			JSONObject params = new JSONObject();
			final String thing = "《大赢家》4.0版本上线，您的龙筹余额已按1:100转换为新版龙筹。";
			params.put("thing", thing);
			log.setContent(CouponSystemHelper.replaceLogParams(logEnum.getTemplate(), params.toString()));
			log.setType(logEnum.getType());
			log.setIpaddress(null);
			log.setLink(null);
			log.setLogTime(new Date());
			goldLogDao.insertAndGetKey(log);
			// 发送结算通知
			params = new JSONObject();
			params.put("num", logMoney);
			notifyService.addNotify(TempletId.UPGRADE_4, gold.getUserId(), 0L, params.toString(), null);

		    } catch (Exception e) {
			LOG.error(String.format("%d - calCouponsAndSend fail - %s", gold.getUserId(), e.getMessage()), e);
		    }
		}
		gold.setStatus(1);
		update = goldDao.update(gold);// 更改数据库状态

		LOG.info(String.format("user %d replace gold %s to %d success  !!!!", gold.getUserId(), gold.getBalance(),
		        logMoney));
	    }
	    if (update) {
		redisSortSetHandler.zAddUserCouponSend(indentify, userId.toString(), 1);// 更改redis状态
	    }
	}
    }

}
