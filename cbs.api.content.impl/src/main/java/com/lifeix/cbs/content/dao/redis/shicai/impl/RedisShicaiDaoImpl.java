package com.lifeix.cbs.content.dao.redis.shicai.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Repository;






import com.lifeix.cbs.api.common.dao.DaoSupport;
import com.lifeix.cbs.content.bean.shicai.PrizeRecordResponse;
import com.lifeix.cbs.content.dao.redis.shicai.RedisShicaiDao;



/**
 * @author wenhuans 2015年11月4日 下午2:14:10
 * 
 */
@Repository("redisShicaiDao")
public class RedisShicaiDaoImpl extends DaoSupport implements RedisShicaiDao {

    @Resource(name = "srt")
    private StringRedisTemplate srt;

    // 进行中的scId
    private static final String SHICAI_ING_SCID = "shicai:scid:ing";

    // 上一期的scId
    private static final String SHICAI_LAST_SCID = "shicai:scid:last";

    // 奖池一区数额
    private static final String SHICAI_POOL_ONE_MONEY = "shicai:pool-one:money";

    // 奖池一区赌注注数
    private static final String SHICAI_POOL_ONE_COUNT = "shicai:pool-one:count";

    // 奖池二区数额
    private static final String SHICAI_POOL_TWO_MONEY = "shicai:pool-two:money";

    // 奖池二区赌注注数
    private static final String SHICAI_POOL_TWO_COUNT = "shicai:pool-two:count";

    // 参与本期游戏的play记录
    private static final String SHICAI_PLAYID = "shicai:playid";

    // 用户获奖记录
    private static final String SHICAI_EARN_RECORD = "shicai:earn:record";
    
    // 获取一等奖的人数
    private static final String SHICAI_PRIZE_FIRST_COUNT = "shicai:prize-first:count";

    // 获取二等奖的人数
    private static final String SHICAI_PRIZE_SECOND_COUNT = "shicai:prize-second:count";
    
    // 新活动标识
    private static final String SHICAI_NEW_RECORD = "shicai:new:record";
    
    @Override
    public void setIngOrLastScId(Long scId, Boolean ingScIdFlag) {
	String key = getCacheId(SHICAI_ING_SCID);
	if (!ingScIdFlag) {
	    key = getCacheId(SHICAI_LAST_SCID);
	}
	srt.opsForValue().set(key, String.valueOf(scId));
    }

    @Override
    public Long getIngOrLastScId(Boolean ingScIdFlag) {
	String key = getCacheId(SHICAI_ING_SCID);
	if (!ingScIdFlag) {
	    key = getCacheId(SHICAI_LAST_SCID);
	}
	String value = srt.opsForValue().get(key);
	if (StringUtils.isNotBlank(value)) {
	    return Long.valueOf(value);
	}
	return -1L;
    }

    @Override
    public void addPrizePool(Long scId, Long money, Long count, Boolean oneFlag) {
	String moneyKey = getCacheId(SHICAI_POOL_ONE_MONEY);
	String countKey = getCacheId(SHICAI_POOL_ONE_COUNT);
	if (!oneFlag) {
	    moneyKey = getCacheId(SHICAI_POOL_TWO_MONEY);
	    countKey = getCacheId(SHICAI_POOL_TWO_COUNT);
	}
	HashOperations<String, String, Object> hos = srt.opsForHash();
	hos.increment(moneyKey, String.valueOf(scId), money.longValue());
	hos.increment(countKey, String.valueOf(scId), count.longValue());
    }

    @Override
    public Double getPrizePoolMoney(Long scId, Boolean oneFlag) {
	String key = getCacheId(SHICAI_POOL_ONE_MONEY);
	if (!oneFlag) {
	    key = getCacheId(SHICAI_POOL_TWO_MONEY);
	}
	Object money = srt.opsForHash().get(key, String.valueOf(scId));
	if (money != null) {
	    return Double.valueOf(money.toString());
	}
	return 0D;
    }

    @Override
    public Long getPrizePoolCount(Long scId, Boolean oneFlag) {
	String key = getCacheId(SHICAI_POOL_ONE_COUNT);
	if (!oneFlag) {
	    key = getCacheId(SHICAI_POOL_TWO_COUNT);
	}
	Object money = srt.opsForHash().get(key, String.valueOf(scId));
	if (money != null) {
	    return Long.valueOf(money.toString());
	}
	return 0L;
    }

    @Override
    public void setBetPlayId(Long scId, Long playId) {
	String key = getCustomCache(SHICAI_PLAYID, scId);
	srt.opsForList().leftPush(key, String.valueOf(playId));
    }

    @Override
    public List<Long> getBetPlayId(Long scId) {
	List<Long> ids = new ArrayList<Long>();
	String key = getCustomCache(SHICAI_PLAYID, scId);
	List<String> playIds = srt.opsForList().range(key, 0, -1);
	if (playIds != null && playIds.size() > 0) {
	    for (String playId : playIds) {
		ids.add(Long.valueOf(playId));
	    }
	}
	return ids;
    }

    @Override
    public void setAccountEarnRecord(Long scId, Long accountId, Long bedPoint) {
	String key = getCustomCache(SHICAI_EARN_RECORD, scId);
	srt.opsForZSet().add(key, String.valueOf(accountId), bedPoint.doubleValue());
    }

    @Override
    public List<PrizeRecordResponse> getAccountEarnRecord(Long scId) {
 
	List<PrizeRecordResponse> prizeList = new ArrayList<PrizeRecordResponse>();

	String key = getCustomCache(SHICAI_EARN_RECORD, scId);
	Set<TypedTuple<String>> accIdSet = srt.opsForZSet().reverseRangeByScoreWithScores(key, 0, Double.POSITIVE_INFINITY,
	        0, 3);
	if (accIdSet != null && accIdSet.size() > 0) {
	    Iterator<TypedTuple<String>> it = accIdSet.iterator();
	    while (it.hasNext()) {
		TypedTuple<String> tuple = it.next();
		Long accId = Long.valueOf(tuple.getValue());
		Double score = tuple.getScore();
		if (accId != null && score != null) {
		    PrizeRecordResponse prize = new PrizeRecordResponse();
		    prize.setAccountId(accId);
		    prize.setBedpoint(score.longValue());
		    prizeList.add(prize);
		}
	    }
	}

	return prizeList;
    }

    @Override
    public void setFirstOrSecondPrizeCount(Long scId, Long count, Boolean firstPrizeFlag) {
	String key = getCacheId(SHICAI_PRIZE_FIRST_COUNT);
	if(!firstPrizeFlag){
	    key = getCacheId(SHICAI_PRIZE_SECOND_COUNT);
	}
	srt.opsForHash().increment(key, String.valueOf(scId), count);
    }

    @Override
    public Long getFirstOrSecondPrizeCount(Long scId, Boolean firstPrizeFlag) {
	String key = getCacheId(SHICAI_PRIZE_FIRST_COUNT);
	if(!firstPrizeFlag){
	    key = getCacheId(SHICAI_PRIZE_SECOND_COUNT);
	}
	Object object = srt.opsForHash().get(key, String.valueOf(scId));
	if(object!=null){
	    return Long.valueOf(String.valueOf(object));
	}
	return 0L;
    }

    @Override
    public void setNewRecord(Long accountId, Long scId) {
	String key = getCacheId(SHICAI_NEW_RECORD);
	srt.opsForHash().put(key, String.valueOf(accountId), String.valueOf(scId));
    }

    @Override
    public Long getNewRecord(Long accountId) {
	String key = getCacheId(SHICAI_NEW_RECORD);
	Object object = srt.opsForHash().get(key, String.valueOf(accountId));
	if(object!=null){
	    return Long.valueOf(String.valueOf(object));
	}
	return null;
    }

    @Override
    public void clearNewRecord(Long accountId) {
	String key = getCacheId(SHICAI_NEW_RECORD);
	srt.opsForHash().delete(key, String.valueOf(accountId));
    }

}
