/**
 * 
 */
package com.lifeix.cbs.contest.impl.redis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.util.RedisConstants;
import com.lifeix.cbs.api.common.util.RedisConstants.ContestRedis;
import com.lifeix.framework.redis.impl.RedisDataIdentify;
import com.lifeix.framework.redis.impl.RedisHashHandler;

/**
 * 押押下单人数统计
 * 
 * @author lifeix
 * 
 */
@Service("redisYyCountHandler")
public class RedisYyCountHandler extends ImplSupport {

    @Autowired
    private RedisHashHandler redisHashHandler;

    /**
     * 押押赛事下单统计
     * 
     * @param contestId
     * @param index
     */
    public void addYyCount(Long userId, Long contestId, Integer index) {

	// 针对赛事添加投票统计
	try {
	    RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, ContestRedis.CONTEST_YY_BET);
	    indentify.setIdentifyId(contestId.toString());
	    redisHashHandler.opsIncr(indentify, index.toString());
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}

	// 针对用户添加投票统计
	try {
	    RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, ContestRedis.CONTEST_YY_USER);
	    indentify.setIdentifyId(userId.toString());
	    redisHashHandler.opsIncr(indentify, contestId.toString());
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
    }

    /**
     * 针对赛事查询下单统计详情
     * 
     * @param contestId
     *            赛事id
     * @return
     */
    public Map<String, Object> viewYyCount(Long contestId) {
	Map<String, Object> bet = new HashMap<String, Object>();
	try {
	    RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, ContestRedis.CONTEST_YY_BET);
	    indentify.setIdentifyId(contestId.toString());
	    bet = redisHashHandler.opsHash(indentify);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
	return bet;
    }

    /**
     * 针对用户查询下单统计详情
     * 
     * @param userId
     * @return
     */
    public Map<String, Object> viewYyUser(Long userId) {
	Map<String, Object> bet = new HashMap<String, Object>();
	try {
	    RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, ContestRedis.CONTEST_YY_USER);
	    indentify.setIdentifyId(userId.toString());
	    bet = redisHashHandler.opsHash(indentify);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
	return bet;
    }

    /**
     * 清除押押赛事下单统计
     * 
     */
    public void cleanYyCount() {
	// 针对赛事查询下单统计详情
	RedisDataIdentify contestIndent = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, ContestRedis.CONTEST_YY_BET);
	contestIndent.setIdentifyId("*");
	// 所有匹配的key
	Set<String> contestSet = redisHashHandler.keys(contestIndent);
	// 登陆记录删除
	for (String s : contestSet) {
	    redisHashHandler.del(s);
	}

	// 针对用户查询下单统计详情
	RedisDataIdentify userIndent = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, ContestRedis.CONTEST_YY_USER);
	userIndent.setIdentifyId("*");
	// 所有匹配的key
	Set<String> userSet = redisHashHandler.keys(userIndent);
	// 登陆记录删除
	for (String s : userSet) {
	    redisHashHandler.del(s);
	}
    }

    public void setRedisHashHandler(RedisHashHandler redisHashHandler) {
	this.redisHashHandler = redisHashHandler;
    }

}
