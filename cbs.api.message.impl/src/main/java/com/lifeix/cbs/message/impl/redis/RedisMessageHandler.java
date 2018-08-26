package com.lifeix.cbs.message.impl.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.util.RedisConstants;
import com.lifeix.cbs.api.common.util.RedisConstants.MessageRedis;
import com.lifeix.framework.redis.impl.RedisDataIdentify;
import com.lifeix.framework.redis.impl.RedisStringHandler;

/**
 * 渠道
 * 
 * @author yis
 */
@Service("redisMessageHandler")
public class RedisMessageHandler extends ImplSupport {

    protected static final Logger LOG = LoggerFactory.getLogger(RedisMessageHandler.class);

    @Autowired
    private RedisStringHandler redisStringHandler;

    /**
     * 增加赛事 -公告关联
     */
    public void addRelation(String key, String value, Long validTime, int type) {
	try {
	    RedisDataIdentify identify = new RedisDataIdentify(RedisConstants.MODEL_MESSAGE,
		    MessageRedis.MESSAGE_PLACARD_RELATION);
	    identify.setIdentifyId(String.format("%s:%s", type, key));
	    identify.setExpireTime(validTime);
	    redisStringHandler.set(identify, value.getBytes());
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
    }

    /**
     * 删除赛事 -公告关联
     */
    public void delRelation(String key, int type) {
	try {
	    RedisDataIdentify identify = new RedisDataIdentify(RedisConstants.MODEL_MESSAGE,
		    MessageRedis.MESSAGE_PLACARD_RELATION);
	    identify.setIdentifyId(String.format("%s:%s", type, key));
	    redisStringHandler.del(identify);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
    }

    /**
     * 获取赛事 -公告关联
     */
    public String getRelation(String key, int type) {
	String ret = "";
	try {
	    RedisDataIdentify identify = new RedisDataIdentify(RedisConstants.MODEL_MESSAGE,
		    MessageRedis.MESSAGE_PLACARD_RELATION);
	    identify.setIdentifyId(String.format("%s:%s", type, key));
	    byte[] bytes = redisStringHandler.get(identify);
	    if (bytes != null && bytes.length > 0) {
		ret = new String(bytes);
	    }
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
	return ret;
    }
}
