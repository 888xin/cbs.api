package com.lifeix.cbs.api.impl.spark.coupon;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lifeix.cbs.api.common.util.ContestConstants.TempletId;
import com.lifeix.cbs.api.common.util.XingePushUtil;
import com.lifeix.cbs.api.dao.coupon.CouponUserDao;
import com.lifeix.cbs.api.dto.coupon.CouponUser;
import com.lifeix.cbs.api.impl.money.MoneyServiceImpl;
import com.lifeix.cbs.api.impl.redis.RedisNotifyHandler;
import com.lifeix.cbs.api.service.spark.coupon.CouponUserDubbo;
import com.lifeix.cbs.message.service.notify.NotifyService;

public class CouponUserDubboImpl implements CouponUserDubbo {

    protected final static Logger LOG = LoggerFactory.getLogger(MoneyServiceImpl.class);

    @Autowired
    private CouponUserDao couponUserDao;

    @Autowired
    NotifyService notifyService;

    @Autowired
    private RedisNotifyHandler redisNotifyHandler;

    private Date getDelayDateTime(int hours) {
	Calendar calendar = Calendar.getInstance();
	calendar.add(Calendar.HOUR_OF_DAY, hours);
	Date date = calendar.getTime();
	return date;
    }

    @Override
    public void sendMessage() {

	// 0.初始化变量
	Integer limit = 1000;
	List<Long> idList = null;
	Long startId = null;
	int size = 0;
	List<CouponUser> list = null;
	Long userId = null;

	JSONObject params = null;

	// 1.查询出6个小时内失效的龙筹券
	// 过期时间 = 当前时间+6个小时
	Date date = getDelayDateTime(6);

	while (true) {
	    list = couponUserDao.findMessage(false, date, startId, limit);
	    idList = new ArrayList<Long>();
	    // 2.发送通知消息
	    for (CouponUser bean : list) {
		// 发送通知消息提醒用户龙筹券快要过期
		userId = bean.getUserId();
		try {
		    params = new JSONObject();
		    params.put("amount", bean.getPrice());
		    notifyService.addNotify(TempletId.COUPON_INVALID, userId, userId, params.toString(), null);
		} catch (Exception e) {
		    LOG.error(String.format("send coupon invalid message fail:userId %d - %s", userId, e.getMessage()), e);
		}
		idList.add(bean.getId());
	    }
	    size = list.size();

	    // 3 将所有龙筹券notifyFlag更新为1-已发送过期提醒
	    if (size > 0) {
		couponUserDao.updateNotifyFlagByIds(idList, true);
	    }

	    if (size < 1000) {
		break;
	    }
	    startId = list.get(size - 1).getId();
	}

    }

    @Override
    public void sendPigeonMessage() {
	// 0.初始化变量
	Integer limit = 1000;
	Integer count = 0;
	Integer userCount = 0;
	Long startId = null;
	Long userId;

	String msg = "";
	String activity = "";
	List<CouponUser> list = null;
	Map<Long, Integer> map = new HashMap<Long, Integer>();

	// 获取当天日期
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String time = sdf.format(new Date());

	// 初始化信鸽消息map
	Map<String, Object> customContent = new HashMap<String, Object>();
	customContent.put("scheme_link", "cbs://longchou?valid=1");

	int size = 0;

	Date date = getDelayDateTime(6);
	while (true) {
	    // 1.查询最近6小时将要过期的龙筹券
	    list = couponUserDao.findPigeonMess(false, date, startId, limit);
	    // 2.统计用户6小时内将要到期的龙筹券数量
	    for (CouponUser bean : list) {
		userId = bean.getUserId();
		count = map.get(userId) == null ? 0 : map.get(userId);
		map.put(userId, ++count);
	    }

	    size = list.size();

	    if (size < 1000) {
		break;
	    }
	    startId = list.get(size - 1).getId();
	}

	Iterator<Long> iterator = map.keySet().iterator();
	// 3.发送信鸽消息
	while (iterator.hasNext()) {
	    try {
		userId = iterator.next();
		count = map.get(userId);
		// 查看用户今天是否发送过信鸽提醒
		userCount = redisNotifyHandler.getUserNotifyNum(time, userId);
		// 若用户存在过期券&&没发送过信鸽信息
		if (count >= 1 && userCount == 0) {
		    // 您有count个筹码马上就要过期了,快去下单吧！
		    msg = String.format("您有%d个筹码马上就要过期了,快去下单吧！", count);
		    // 发送信鸽消息提醒
		    XingePushUtil.getInstance().pushSingleAccount(userId, activity, msg, customContent);

		    // redis插入信鸽信息记录
		    redisNotifyHandler.addUserNotifyNum(time, userId, 1);
		}
	    } catch (Exception e) {
		LOG.error(String.format("send pigeon message fail - %s", e.getMessage()), e);
	    }
	}
    }
}
