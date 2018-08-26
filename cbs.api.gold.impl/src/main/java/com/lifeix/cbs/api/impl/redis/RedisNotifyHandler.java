package com.lifeix.cbs.api.impl.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.util.RedisConstants;
import com.lifeix.cbs.api.common.util.RedisConstants.GoldRedis;
import com.lifeix.framework.redis.impl.RedisDataIdentify;
import com.lifeix.framework.redis.impl.RedisHashHandler;

/**
 * 龙筹过期消息提醒
 * 
 * @author lifeix
 */
@Service("redisNotifyHandler")
public class RedisNotifyHandler extends ImplSupport {

    protected static final Logger LOG = LoggerFactory.getLogger(RedisNotifyHandler.class);

    @Autowired
    private RedisHashHandler redisHashHandler;

    /**
     * 增加用户信鸽通知数
     */
    public void addUserNotifyNum(String date, Long userId, Integer num) {
	try {
	    RedisDataIdentify identify = new RedisDataIdentify(RedisConstants.MODEL_GOLD,
		    GoldRedis.COUPON_USER_PIGEON_MESSAGE);
	    identify.setIdentifyId(date);
	    redisHashHandler.hIncrBy(identify, userId.toString().getBytes(), num);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
    }

    /**
     * 获取用户信鸽通知数
     */
    public Integer getUserNotifyNum(String date, Long userId) {
	Integer result = 0;
	try {
	    RedisDataIdentify identify = new RedisDataIdentify(RedisConstants.MODEL_GOLD,
		    GoldRedis.COUPON_USER_PIGEON_MESSAGE);
	    identify.setIdentifyId(date);
	    byte[] count = redisHashHandler.hget(identify, userId.toString().getBytes());
	    if (count != null && count.length > 0) {
		result = Integer.parseInt(new String(count));
	    }
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
	return result;
    }
}
