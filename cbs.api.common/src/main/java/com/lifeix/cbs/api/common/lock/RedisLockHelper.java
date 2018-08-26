package com.lifeix.cbs.api.common.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

/**
 * redis共享锁工具类
 * 
 * @author lifeix
 * 
 */
@Service("redisLock")
public class RedisLockHelper {

    private static final Logger LOG = LoggerFactory.getLogger(RedisLockHelper.class);

    static final String REDIS_LOCK_PREFIX = "cbs:lock:";

    // 锁的超时时间59分钟
    private static final long EXPIRE_TIME = 59 * 60L;

    @Autowired
    @Qualifier("srt")
    private StringRedisTemplate srt;

    public String getRedisLock(Object key, String identify) {
	String ret = null;
	try {
	    final RedisSerializer<String> serializer = srt.getStringSerializer();
	    final byte[] redisKey = serializer.serialize(buildKey(key, identify));
	    ret = srt.execute(new RedisCallback<String>() {
		public String doInRedis(RedisConnection connection) throws DataAccessException {
		    if (connection.exists(redisKey)) {
			return new String(connection.get(redisKey));
		    }
		    return null;
		}
	    });
	} catch (Exception e) {
	    LOG.error(String.format("get redis lock fail - %s", e.getMessage()));
	}
	return ret;
    }

    public void setRedisLock(Object key, String identify) {
	try {
	    final RedisSerializer<String> serializer = srt.getStringSerializer();
	    final byte[] redisKey = serializer.serialize(buildKey(key, identify));
	    final byte[] redisLock = serializer.serialize("1");
	    srt.execute(new RedisCallback<Integer>() {
		public Integer doInRedis(RedisConnection connection) throws DataAccessException {
		    connection.set(redisKey, redisLock);
		    connection.expire(redisKey, EXPIRE_TIME);
		    return 1;
		}
	    });
	} catch (Exception e) {
	    LOG.error(String.format("set redis lock fail - %s", e.getMessage()));
	}
    }

    public void delRedisLock(Object key, String identify) {
	try {
	    final RedisSerializer<String> serializer = srt.getStringSerializer();
	    final byte[] redisKey = serializer.serialize(buildKey(key, identify));
	    srt.execute(new RedisCallback<Long>() {
		public Long doInRedis(RedisConnection connection) throws DataAccessException {
		    return connection.del(redisKey);
		}
	    });
	} catch (Exception e) {
	    LOG.error(String.format("del redis lock fail - %s", e.getMessage()));
	}
    }

    private String buildKey(Object key, String identify) {
	return new StringBuilder().append(REDIS_LOCK_PREFIX).append(identify).append(":").append(key).toString();
    }
}
