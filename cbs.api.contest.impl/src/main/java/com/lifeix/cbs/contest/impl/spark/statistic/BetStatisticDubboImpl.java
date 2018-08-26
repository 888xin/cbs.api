package com.lifeix.cbs.contest.impl.spark.statistic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.common.exception.MsgCode;
import com.lifeix.cbs.api.common.util.RedisConstants;
import com.lifeix.cbs.api.common.util.RedisConstants.ContestRedis;
import com.lifeix.cbs.contest.bean.statistic.BetStatisticResponse;
import com.lifeix.cbs.contest.dao.statistic.ContestStatisticDao;
import com.lifeix.cbs.contest.service.spark.statistic.BetStatisticDubbo;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.framework.redis.impl.RedisDataIdentify;
import com.lifeix.framework.redis.impl.RedisHashHandler;

/**
 * Created by lhx on 15-12-21 下午4:49
 * 
 * @Description
 */
@Service("betStatisticDubbo")
public class BetStatisticDubboImpl implements BetStatisticDubbo {

    protected static Logger LOG = LoggerFactory.getLogger(BetStatisticDubboImpl.class);

    @Autowired
    private RedisHashHandler redisHashHandler;

    @Autowired
    private ContestStatisticDao contestStatisticDao;

    @Override
    public void betsStatistic(String date) {
	date = getDate(date);
	int num = contestStatisticDao.betsStatistic(date);
	try {
	    RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, ContestRedis.STATISTIC_BETS);
	    byte[] dataKey = redisHashHandler.serialize(date);
	    byte[] value = redisHashHandler.serialize(num);
	    redisHashHandler.hset(indentify, dataKey, value);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
    }

    @Override
    public void peopleStatistic(String date) {
	date = getDate(date);
	int num = contestStatisticDao.peopleStatistic(date);
	try {
	    RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, ContestRedis.STATISTIC_PEOTLE);
	    byte[] dataKey = redisHashHandler.serialize(date);
	    byte[] value = redisHashHandler.serialize(num);
	    redisHashHandler.hset(indentify, dataKey, value);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
    }

    @Override
    public void opStatistic(String date) {
	date = getDate(date);
	int num = contestStatisticDao.opStatistic(date);
	try {
	    RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, ContestRedis.STATISTIC_OP);
	    byte[] dataKey = redisHashHandler.serialize(date);
	    byte[] value = redisHashHandler.serialize(num);
	    redisHashHandler.hset(indentify, dataKey, value);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
    }

    @Override
    public void jcStatistic(String date) {
	date = getDate(date);
	int num = contestStatisticDao.jcStatistic(date);
	try {
	    RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, ContestRedis.STATISTIC_JC);
	    byte[] dataKey = redisHashHandler.serialize(date);
	    byte[] value = redisHashHandler.serialize(num);
	    redisHashHandler.hset(indentify, dataKey, value);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
    }

    @Override
    public void fbStatistic(String date) {
	date = getDate(date);
	int num = contestStatisticDao.fbStatistic(date);
	try {
	    RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, ContestRedis.STATISTIC_FB);
	    byte[] dataKey = redisHashHandler.serialize(date);
	    byte[] value = redisHashHandler.serialize(num);
	    redisHashHandler.hset(indentify, dataKey, value);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
    }

    @Override
    public void bbStatistic(String date) {
	date = getDate(date);
	int num = contestStatisticDao.bbStatistic(date);
	try {
	    RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, ContestRedis.STATISTIC_BB);
	    byte[] dataKey = redisHashHandler.serialize(date);
	    byte[] value = redisHashHandler.serialize(num);
	    redisHashHandler.hset(indentify, dataKey, value);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
    }

    @Override
    public BetStatisticResponse getStatistic(String startDate, Integer limit) throws L99IllegalParamsException {
	BetStatisticResponse statisticResponse = new BetStatisticResponse();
	startDate = getDate(startDate);
	Calendar calendar = Calendar.getInstance();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd");
	// 日期
	String[] dateStrs = new String[limit];
	// 数量
	int[] numBets = new int[limit];
	int[] numPelple = new int[limit];
	int[] numOp = new int[limit];
	int[] numJc = new int[limit];
	int[] numFb = new int[limit];
	int[] numBb = new int[limit];
	Date start;
	try {
	    start = sdf.parse(startDate);
	} catch (ParseException e) {
	    LOG.error(e.getMessage(), e);
	    throw new L99IllegalParamsException(MsgCode.BasicMsg.CODE_PARAMEMETER, MsgCode.BasicMsg.KEY_PARAMEMETER);
	}
	calendar.setTime(start);
	calendar.add(Calendar.DAY_OF_YEAR, 1);

	RedisDataIdentify betIt = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, ContestRedis.STATISTIC_BETS);
	RedisDataIdentify peopleIt = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, ContestRedis.STATISTIC_PEOTLE);
	RedisDataIdentify jcIt = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, ContestRedis.STATISTIC_JC);
	RedisDataIdentify opIt = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, ContestRedis.STATISTIC_OP);
	RedisDataIdentify fbIt = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, ContestRedis.STATISTIC_FB);
	RedisDataIdentify bbIt = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, ContestRedis.STATISTIC_BB);

	for (int i = 0; i < limit; i++) {
	    calendar.add(Calendar.DAY_OF_YEAR, -1);
	    byte[] dataKey = redisHashHandler.serialize(sdf.format(calendar.getTime()));
	    // 获取日期数组（倒序）
	    dateStrs[limit - i - 1] = sdf2.format(calendar.getTime());
	    // 获取下单数数组
	    byte[] valueBets = redisHashHandler.hget(betIt, dataKey);
	    if (valueBets == null || valueBets.length < 1) {
		numBets[limit - i - 1] = 0;
	    } else {
		numBets[limit - i - 1] = Integer.valueOf(new String(valueBets));
	    }
	    // 获取下单人数数组
	    byte[] valuePeople = redisHashHandler.hget(peopleIt, dataKey);
	    if (valuePeople == null) {
		numPelple[limit - i - 1] = 0;
	    } else {
		numPelple[limit - i - 1] = Integer.valueOf(new String(valuePeople));
	    }
	    // 获取下单胜平负数组
	    byte[] valueOp = redisHashHandler.hget(opIt, dataKey);
	    if (valueOp == null) {
		numOp[limit - i - 1] = 0;
	    } else {
		numOp[limit - i - 1] = Integer.valueOf(new String(valueOp));
	    }
	    // 获取下单让球胜平负数组
	    byte[] valueJc = redisHashHandler.hget(jcIt, dataKey);
	    if (valueJc == null) {
		numJc[limit - i - 1] = 0;
	    } else {
		numJc[limit - i - 1] = Integer.valueOf(new String(valueJc));
	    }
	    // 获取下单足球数组
	    byte[] valueFb = redisHashHandler.hget(fbIt, dataKey);
	    if (valueFb == null) {
		numFb[limit - i - 1] = 0;
	    } else {
		numFb[limit - i - 1] = Integer.valueOf(new String(valueFb));
	    }
	    // 获取下单两球数组
	    byte[] valueBb = redisHashHandler.hget(bbIt, dataKey);
	    if (valueBb == null) {
		numBb[limit - i - 1] = 0;
	    } else {
		numBb[limit - i - 1] = Integer.valueOf(new String(valueBb));
	    }
	}
	statisticResponse.setCategories(dateStrs);
	statisticResponse.setBets(numBets);
	statisticResponse.setPeople(numPelple);
	statisticResponse.setOp(numOp);
	statisticResponse.setJc(numJc);
	statisticResponse.setFb(numFb);
	statisticResponse.setBb(numBb);
	return statisticResponse;
    }

    private String getDate(String date) {
	if (StringUtils.isBlank(date)) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.add(Calendar.DAY_OF_YEAR, -1);
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    return sdf.format(calendar.getTime());
	} else {
	    return date;
	}
    }
}
