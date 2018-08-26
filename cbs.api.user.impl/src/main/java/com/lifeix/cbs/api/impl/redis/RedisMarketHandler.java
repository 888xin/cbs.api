package com.lifeix.cbs.api.impl.redis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.util.RedisConstants;
import com.lifeix.cbs.api.common.util.RedisConstants.UserRedis;
import com.lifeix.framework.redis.impl.RedisDataIdentify;
import com.lifeix.framework.redis.impl.RedisHashHandler;
import com.lifeix.framework.redis.impl.RedisSetsHandler;
import com.lifeix.framework.redis.impl.RedisStringHandler;

/**
 * 渠道
 * 
 * @author yis
 */
@Service("redisMarketHandler")
public class RedisMarketHandler extends ImplSupport {

    protected static final Logger LOG = LoggerFactory.getLogger(RedisMarketHandler.class);

    @Autowired
    private RedisHashHandler redisHashHandler;

    @Autowired
    private RedisSetsHandler redisSetsHandler;

    @Autowired
    private RedisStringHandler redisStringHandler;

    static RedisDataIdentify marketId = new RedisDataIdentify(RedisConstants.MODEL_USER, UserRedis.MARKET_MARKET);

    static RedisDataIdentify statisticId = new RedisDataIdentify(RedisConstants.MODEL_USER,
	    UserRedis.MARKET_MARKET_STATISTIC);

    // 登录超时时间2小时
    private static final long EXPIRE_TIME = 3 * 60 * 60L;

    /**
     * 获取渠道登录记录
     */
    public String getMarketLoginInfo(String market) {
	try {
	    RedisDataIdentify marketLoginId = new RedisDataIdentify(RedisConstants.MODEL_USER, UserRedis.MARKET_MARKET_LOGIN);
	    marketLoginId.setIdentifyId(String.format("%s", market));
	    byte[] result = redisStringHandler.get(marketLoginId);
	    String ret = result.length > 0 ? new String(result) : "not found";
	    return ret;
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    return "error";
	}
    }

    /**
     * 增加渠道登录记录
     */
    public void addMarketLogin(String market, String value) {
	try {
	    RedisDataIdentify marketLoginId = new RedisDataIdentify(RedisConstants.MODEL_USER, UserRedis.MARKET_MARKET_LOGIN);
	    marketLoginId.setIdentifyId(String.format("%s", market));
	    marketLoginId.setExpireTime(EXPIRE_TIME);
	    redisStringHandler.set(marketLoginId, value.getBytes());
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
    }

    /**
     * 删除渠道
     */
    public void delMarket() {
	try {
	    redisSetsHandler.del(marketId);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
    }

    /**
     * 增加渠道
     */
    public void addMarket(String market) {
	try {
	    redisSetsHandler.sAdd(marketId, market);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
    }

    /**
     * 查询渠道列表
     */
    public Set<byte[]> getMarketList() {
	Set<byte[]> datas = null;
	try {
	    datas = redisSetsHandler.sMembersByte(marketId);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
	return datas;
    }

    /**
     * 删除渠道统计
     */
    public void delMarketStatistic(String market) {
	try {
	    statisticId.setIdentifyId(String.format("%s", market));
	    redisHashHandler.del(statisticId);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
    }

    /**
     * 增加渠道统计
     */
    public void addMarketStatistic(String market, String sex, long key) {
	try {
	    statisticId.setIdentifyId(String.format("%s", market));
	    redisHashHandler.hIncrBy(statisticId, sex.getBytes(), key);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
    }

    /**
     * 获取渠道统计
     */
    public Map<String, Integer> getMarketStatistic(String market) {
	Map<String, Integer> retMap = new HashMap<String, Integer>();
	try {
	    statisticId.setIdentifyId(String.format("%s", market));
	    Map<byte[], byte[]> map = redisHashHandler.hGetAll(statisticId);
	    String key;
	    for (byte[] o : map.keySet()) {
		key = new String(o);
		retMap.put(key, Integer.parseInt(new String(map.get(o))));
	    }

	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
	return retMap;
    }

}
