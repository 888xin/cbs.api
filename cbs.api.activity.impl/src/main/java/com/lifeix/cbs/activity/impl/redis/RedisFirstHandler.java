package com.lifeix.cbs.activity.impl.redis;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.util.RedisConstants;
import com.lifeix.cbs.api.common.util.RedisConstants.ActivityRedis;
import com.lifeix.framework.redis.impl.RedisDataIdentify;
import com.lifeix.framework.redis.impl.RedisListHandler;
import com.lifeix.framework.redis.impl.RedisSortSetHandler;

/**
 * 首充活动
 * 
 * @author lifeix
 */
@Service("redisfirstHandler")
public class RedisFirstHandler extends ImplSupport {

    @Autowired
    private RedisSortSetHandler redisSortSetHandler;

    @Autowired
    private RedisListHandler redisListHandler;

    private static final int REWARD_LOG_NUM = 10;

    /**
     * 查询中奖日志
     * 
     * @param rewardLog
     *            中奖消息
     */
    public List<String> getRewardLogList() {
	List<String> list = new ArrayList<String>();
	try {
	    RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_ACTIVITY,
		    ActivityRedis.FIRST_REWARD_LOG);
	    List<byte[]> datas = redisListHandler.sMembersByte(indentify, 0, -1);

	    for (byte[] log : datas) {
		list.add(new String(log));
	    }
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
	return list;
    }

    /**
     * 插入中奖日志 ：保留最近10条记录
     * 
     * @param rewardLog
     *            中奖消息
     */
    public void addFirstRewardLog(String rewardLog) {
	try {
	    RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_ACTIVITY,
		    ActivityRedis.FIRST_REWARD_LOG);
	    Long index = redisListHandler.insertRedisData(indentify, redisListHandler.serialize(String.valueOf(rewardLog)));
	    if (index > REWARD_LOG_NUM) {
		redisListHandler.lPopList(indentify, 1);
	    }
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
    }

    /**
     * 插入首充记录
     * 
     * @param userId
     *            用户ID
     * 
     */
    public void addFirstRecord(Long userId) {
	try {
	    RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_ACTIVITY, ActivityRedis.FIRST_RECORDS);
	    redisSortSetHandler.zAdd(indentify, userId.toString(), 1);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
    }

    /**
     * 首充状态累加
     * 
     * @param userId
     *            用户ID
     */
    public void incrFirstRecord(Long userId) {
	try {
	    RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_ACTIVITY, ActivityRedis.FIRST_RECORDS);
	    redisSortSetHandler.zIncrby(indentify, userId.toString(), 1);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
    }

    public void seRedisSortSetHandler(RedisSortSetHandler redisSortSetHandler) {
	this.redisSortSetHandler = redisSortSetHandler;
    }

    /**
     * 获取首充状态
     * 
     * @param userId
     *            用户ID
     * @return status 首充状态：0-未参与、1-充值、2-抽奖
     */
    public int getFirstStatus(Long userId) {
	int status = 0;
	try {
	    RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_ACTIVITY, ActivityRedis.FIRST_RECORDS);
	    Double retVal = redisSortSetHandler.zScore(indentify, userId);
	    if (retVal != null) {
		status = retVal.intValue();
	    }
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
	return status;
    }
}
