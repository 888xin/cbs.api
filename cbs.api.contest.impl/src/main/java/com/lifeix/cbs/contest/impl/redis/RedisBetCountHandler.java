/**
 * 
 */
package com.lifeix.cbs.contest.impl.redis;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.util.BetConstants.BetStatus;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestType;
import com.lifeix.cbs.api.common.util.PlayType;
import com.lifeix.cbs.api.common.util.RedisConstants;
import com.lifeix.cbs.api.common.util.RedisConstants.ContestRedis;
import com.lifeix.common.utils.RegexUtil;
import com.lifeix.framework.redis.impl.RedisDataIdentify;
import com.lifeix.framework.redis.impl.RedisHashHandler;

/**
 * 赛事下单和支持统计
 * 
 * @author lifeix
 */
@Service("redisBetCountHandler")
public class RedisBetCountHandler extends ImplSupport {

    @Autowired
    private RedisHashHandler redisHashHandler;

    /**
     * 添加下单支持统计
     * 
     * @param contestType
     *            赛事类型
     * @param contestId
     *            赛事id
     * @param betType
     *            玩法（1/6：胜平负，2/7：让球胜平负）{@link=PlayType}
     * @param support
     *            竞猜（0：主队，1：客队，2：平局 {@link=BetStatus}
     */
    public void addContestBet(int contestType, Long contestId, Integer betType, Integer support) {
	try {
	    RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, ContestRedis.CONTEST_BET);
	    indentify.setIdentifyId(String.format("%d-%d", contestType, contestId));
	    redisHashHandler.opsIncr(indentify, String.format("%d,%d", betType, support));

	    // 添加赛下单统计
	    incrContestCount(contestType, contestId, 1);

	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
    }

    /**
     * 获取赛事下单支持信息
     * 
     * @param contestType
     *            赛事类型
     * @param contestId
     *            赛事id
     * @return
     */
    public Map<String, Object> getContestBet(int contestType, Long contestId) {
	Map<String, Object> bet = new HashMap<String, Object>();
	try {
	    RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, ContestRedis.CONTEST_BET);
	    indentify.setIdentifyId(String.format("%d-%d", contestType, contestId));
	    bet = redisHashHandler.opsHash(indentify);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
	return bet;
    }

    /**
     * 返回胜平负和让球胜平负的下注比率
     * 
     * @param contestType
     * @param contestId
     * @return
     */
    public int[] findContestBetRatio(int contestType, Long contestId) {
	int[] bet_count = { 0, 0 };// 主，客
	Map<String, Object> betCount = getContestBet(contestType, contestId);
	Object ophCount = null;
	Object opaCount = null;
	Object jchCount = null;
	Object jcaCount = null;
	if (contestType == ContestType.FOOTBALL) {
	    ophCount = betCount.get(String.format("%d,%d", PlayType.FB_SPF.getId(), BetStatus.HOME));// 主胜人数
	    opaCount = betCount.get(String.format("%d,%d", PlayType.FB_SPF.getId(), BetStatus.AWAY));// 客胜人数
	    jchCount = betCount.get(String.format("%d,%d", PlayType.FB_RQSPF.getId(), BetStatus.HOME));// 主胜人数
	    jcaCount = betCount.get(String.format("%d,%d", PlayType.FB_RQSPF.getId(), BetStatus.AWAY));// 客胜人数
	} else if (contestType == ContestType.BASKETBALL) {
	    ophCount = betCount.get(String.format("%d,%d", PlayType.BB_SPF.getId(), BetStatus.HOME));// 主胜人数
	    opaCount = betCount.get(String.format("%d,%d", PlayType.BB_SPF.getId(), BetStatus.AWAY));// 客胜人数
	    jchCount = betCount.get(String.format("%d,%d", PlayType.BB_JC.getId(), BetStatus.HOME));// 主胜人数
	    jcaCount = betCount.get(String.format("%d,%d", PlayType.BB_JC.getId(), BetStatus.AWAY));// 客胜人数
	}
	if (ophCount != null) {
	    bet_count[0] = Integer.valueOf(ophCount.toString());
	}
	if (opaCount != null) {
	    bet_count[1] = Integer.valueOf(opaCount.toString());
	}
	if (jchCount != null) {
	    bet_count[0] += Integer.valueOf(jchCount.toString());
	}
	if (jcaCount != null) {
	    bet_count[1] += Integer.valueOf(jcaCount.toString());
	}
	return bet_count;
    }

    /**
     * 清除指定赛事下单支持统计的记录
     * 
     * @param contestType
     *            赛事类型
     */
    public void delContestSupport(int contestType) {
	RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, ContestRedis.CONTEST_BET);
	indentify.setIdentifyId(String.format("%d-*", contestType));
	// 所有匹配的key
	Set<String> set = redisHashHandler.keys(indentify);
	// 登陆记录删除
	for (String s : set) {
	    redisHashHandler.del(s);
	}
    }

    /**
     * 重置赛事统计
     * 
     * @param contestType
     */
    public int resetContestCount(Integer contestType) {
	int ret = 0;
	RedisDataIdentify countIndent = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, ContestRedis.CONTEST_COUNT);
	countIndent.setIdentifyId(contestType.toString());
	redisHashHandler.del(countIndent);
	RedisDataIdentify supportIdent = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, ContestRedis.CONTEST_BET);
	supportIdent.setIdentifyId(String.format("%d-*", contestType));
	String reg = String.format("%s:%s:%d-([\\d]+)", RedisConstants.MODEL_CONTEST, ContestRedis.CONTEST_BET, contestType);
	Set<String> keys = redisHashHandler.keys(supportIdent);
	for (String key : keys) {
	    ret++;
	    String contestId = RegexUtil.filterMatch(key, reg);
	    Map<String, Object> supportMap = redisHashHandler.opsHash(key);
	    Collection<Object> supports = supportMap.values();
	    int count = 0;
	    for (Object support : supports) {
		if (support != null) {
		    count += Integer.valueOf(support.toString());
		}
	    }
	    redisHashHandler.hset(countIndent, redisHashHandler.serialize(contestId), redisHashHandler.serialize(count));
	}
	return ret;
    }

    /**
     * 赛事下单统计累加
     * 
     * @param contestType
     * @param contestId
     * @param count
     */
    public void incrContestCount(Integer contestType, Long contestId, Integer count) {
	try {
	    RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, ContestRedis.CONTEST_COUNT);
	    indentify.setIdentifyId(contestType.toString());
	    redisHashHandler.hIncrBy(indentify, redisHashHandler.serialize(contestId), count);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
    }

    /**
     * 查询赛事下单统计
     * 
     * @param contestType
     * @param contestId
     * @return
     */
    public int findContestCount(Integer contestType, Long contestId) {
	int ret = 0;
	try {
	    if (contestId == null) {
		return ret;
	    }
	    RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, ContestRedis.CONTEST_COUNT);
	    indentify.setIdentifyId(contestType.toString());
	    byte[] data = redisHashHandler.hget(indentify, redisHashHandler.serialize(contestId));
	    if (data != null && data.length > 0) {
		ret = Integer.valueOf(new String(data));
	    }
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
	return ret;
    }

    /**
     * 批量查询赛事下单统计
     * 
     * @param contestType
     * @param contestIds
     * @return
     */
    public Map<Long, Integer> findContestsCount(Integer contestType, List<String> contestIds) {
	Map<Long, Integer> ret = new HashMap<>();
	try {
	    if (contestIds == null || contestIds.size() == 0) {
		return ret;
	    }
	    RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, ContestRedis.CONTEST_COUNT);
	    indentify.setIdentifyId(contestType.toString());
	    List<byte[]> datas = redisHashHandler.hMget(indentify, contestIds);
	    for (int i = 0; i < contestIds.size(); i++) {
		int count = 0;
		byte[] data = datas.get(i);
		if (data != null && data.length > 0) {
		    count = Integer.valueOf(new String(data));
		}
		ret.put(Long.valueOf(contestIds.get(i)), count);
	    }
	} catch (Exception e) {
	    LOG.error(
		    String.format("%d-[%s] hget fail - %s", contestType, StringUtils.join(contestIds, ","), e.getMessage()),
		    e);
	}
	return ret;
    }

    public void setRedisHashHandler(RedisHashHandler redisHashHandler) {
	this.redisHashHandler = redisHashHandler;
    }

}
