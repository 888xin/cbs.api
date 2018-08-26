package com.lifeix.cbs.activity.impl.first;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.activity.bean.first.ActivityFirstResponse;
import com.lifeix.cbs.activity.dao.first.ActivityFirstLogDao;
import com.lifeix.cbs.activity.dto.first.ActivityFirstLog;
import com.lifeix.cbs.activity.impl.redis.RedisFirstHandler;
import com.lifeix.cbs.activity.service.first.ActivityFirstLogService;
import com.lifeix.cbs.activity.transform.FirstTransformUtil;
import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.cbs.api.common.exception.MsgCode.ActivityMsg;
import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.lock.RedisLockHelper;
import com.lifeix.cbs.api.common.util.ActivityConstants;
import com.lifeix.cbs.api.common.util.CbsActivityFirstUtils;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.common.util.LockConstants;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.service.coupon.CouponUserService;
import com.lifeix.cbs.api.service.user.CbsUserService;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * Created by lhx on 16-4-15 上午10:00
 * 
 * @Description
 */
@Service("activityFirstLogService")
public class ActivityFirstLogServiceImpl extends ImplSupport implements ActivityFirstLogService {

    @Autowired
    private ActivityFirstLogDao activityFirstLogDao;

    @Autowired
    private RedisFirstHandler redisFirstHandler;

    @Autowired
    CouponUserService couponUserService;

    @Autowired
    CbsUserService cbsUserService;

    @Autowired
    private RedisLockHelper redisLock;

    /**
     * 抽奖锁
     */
    private static Object lock = new Object();

    @Override
    public ActivityFirstResponse check(Long userId, Boolean flag) {
	int status = redisFirstHandler.getFirstStatus(userId);
	ActivityFirstResponse response = new ActivityFirstResponse();

	Integer times = 0;
	Boolean actFlag = getFirstFlag();
	// 活动处于开启状态且当前记录为已充值
	if (actFlag && status == 1) {
	    times = 1;
	}
	if (flag) {
	    List<ActivityFirstLog> list = activityFirstLogDao.find(userId);
	    if (list.size() > 0) {
		response = FirstTransformUtil.transformFirstLog(list.get(0));
	    }
	}
	response.setRule("unknow");
	response.setStatus(status);
	response.setTimes(times);
	response.setActFlag(actFlag);
	return response;
    }

    @Override
    public ActivityFirstResponse check(Long userId) {
	return check(userId, true);
    }

    @Override
    public ActivityFirstResponse addFirstLog(Long userId) throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(userId);

	Boolean actFlag = getFirstFlag();
	// 判断当前活动是否处于开启中
	if (!actFlag) {
	    throw new L99IllegalParamsException(ActivityMsg.CODE_ACTIVITY_END, InternationalResources.getInstance().locale(
		    ActivityMsg.KEY_ACTIVITY_END));
	}

	// 判断当前用户是否可抽奖
	synchronized (lock) {
	    if (redisLock.getRedisLock(userId, LockConstants.ACTIVITY_LOTTERY) != null) {
		throw new L99IllegalParamsException(ActivityMsg.CODE_FIRST_NO_CHANCE, InternationalResources.getInstance()
		        .locale(ActivityMsg.KEY_FIRST_NO_CHANCE));
	    }
	    redisLock.setRedisLock(userId, LockConstants.ACTIVITY_LOTTERY);
	}
	try {
	    int status = redisFirstHandler.getFirstStatus(userId);

	    // 不存在参与首充活动的充值成功记录
	    if (status == ActivityConstants.FirstStatus.NOT_FOUND) {
		throw new L99IllegalParamsException(ActivityMsg.CODE_FIRST_NOT_FOUND, InternationalResources.getInstance()
		        .locale(ActivityMsg.KEY_FIRST_NOT_FOUND));
	    }

	    // 已参与首充活动不能再参加
	    if (status == ActivityConstants.FirstStatus.NO_CHANCE) {
		throw new L99IllegalParamsException(ActivityMsg.CODE_FIRST_NO_CHANCE, InternationalResources.getInstance()
		        .locale(ActivityMsg.KEY_FIRST_NO_CHANCE));
	    }
	    // 更新活动记录（状态置为已抽奖2）
	    redisFirstHandler.incrFirstRecord(userId);

	    List<ActivityFirstLog> list = activityFirstLogDao.find(userId);

	    if (list.size() <= 0) {
		throw new L99IllegalParamsException(ActivityMsg.CODE_FIRST_NOT_FOUND, ActivityMsg.KEY_FIRST_NOT_FOUND);
	    }
	    ActivityFirstLog activityFirstLog = list.get(0);
	    // 生成中奖金额及中奖券
	    Map<String, Object> result = CbsActivityFirstUtils.lottery(activityFirstLog.getAmount());
	    // 更新首充日志记录中奖金额及规则
	    activityFirstLog.setRule(result.get(CbsActivityFirstUtils.RULE).toString());
	    activityFirstLog.setReward((Integer) result.get(CbsActivityFirstUtils.REWARD));
	    activityFirstLogDao.update(activityFirstLog);

	    // 送对应龙筹券
	    CbsUserResponse user = cbsUserService.selectById(userId);
	    String name = user.getName().trim();
	    if (name.length() > 1) {
		name = name.charAt(0) + "**";
	    }

	    String couponLog = String.format("参加首充抽奖获得%d奖励", activityFirstLog.getReward());
	    couponUserService.settleCouponByBack(activityFirstLog.getUserId(), Double.valueOf(activityFirstLog.getReward()),
		    couponLog);
	    // 生成喇叭中奖消息（redis：保留最近10条记录）
	    String rewardRemark = String.format("恭喜<span>%s:</span>获得%d元", name, activityFirstLog.getReward());
	    redisFirstHandler.addFirstRewardLog(rewardRemark);
	    ActivityFirstResponse response = FirstTransformUtil.transformFirstLog(activityFirstLog);
	    response.setRule("unknow");
	    response.setRewardRemark(rewardRemark);
	    response.setStatus(ActivityConstants.FirstStatus.NO_CHANCE);
	    response.setTimes(0);
	    response.setActFlag(actFlag);
	    return response;
	} finally {
	    redisLock.delRedisLock(userId, LockConstants.ACTIVITY_LOTTERY);
	}
    }

    @Override
    public void addFirstRecord(Long userId, Double price) throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(userId, price);
	// 判断活动是否继续
	if (getFirstFlag()) {
	    // 判断当前充值金额是否在活动充值列表中
	    List<Double> list = getFirstMoneyOrders();
	    if (list.indexOf(price) >= 0) {
		int status = redisFirstHandler.getFirstStatus(userId);
		if (status == ActivityConstants.FirstStatus.NOT_FOUND) {
		    // redis中插入一条参与首充活动的充值记录
		    redisFirstHandler.addFirstRecord(userId);
		    // 插入一条首充日志到数据库
		    ActivityFirstLog bean = new ActivityFirstLog();
		    bean.setUserId(userId);
		    bean.setAmount(price);
		    bean.setCreateTime(new Date());
		    activityFirstLogDao.insert(bean);
		}
	    }

	}
    }

    // 获取活动充值列表
    private List<Double> getFirstMoneyOrders() {
	List<Double> list = new ArrayList<Double>();
	list.add(20D);
	list.add(50D);
	list.add(100D);
	list.add(500D);
	list.add(1000D);
	return list;
    }

    // 获取活动标志 true：开启 false：关闭
    private boolean getFirstFlag() {
	return true;
    }

    @Override
    public List<String> getRewardLogList() {
	return redisFirstHandler.getRewardLogList();
    }

}
