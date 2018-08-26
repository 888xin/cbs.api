package com.lifeix.cbs.api.impl.gold;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.l99.vo.pay.PayAccountLogListReponse;
import com.l99.vo.pay.PayAccountLogReponse;
import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.bean.money.MoneyStatisticListResponse;
import com.lifeix.cbs.api.bean.money.MoneyStatisticResponse;
import com.lifeix.cbs.api.bean.money.MoneyUserStatisticListResponse;
import com.lifeix.cbs.api.bean.money.MoneyUserStatisticResponse;
import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.api.common.util.GoldConstants;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.common.util.RedisConstants;
import com.lifeix.cbs.api.common.util.RedisConstants.GoldRedis;
import com.lifeix.cbs.api.dao.money.MoneyStatisticDao;
import com.lifeix.cbs.api.dto.money.MoneyStatistic;
import com.lifeix.cbs.api.impl.money.MoneyLogServiceImpl;
import com.lifeix.cbs.api.service.money.MoneyStatisticService;
import com.lifeix.cbs.api.service.user.CbsUserService;
import com.lifeix.cbs.api.transform.GoldTransformUtil;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.framework.redis.impl.RedisDataIdentify;
import com.lifeix.framework.redis.impl.RedisSortSetHandler;
import com.lifeix.framework.redis.impl.ResultSortData;
import com.lifeix.payment.dubbo.service.PayAccountLogService;

@Service("moneyStatisticService")
public class MoneyStatisticServiceImpl extends ImplSupport implements MoneyStatisticService {

    @Autowired
    private RedisSortSetHandler redisSortSetHandler;

    @Autowired
    private MoneyStatisticDao moneyStatisticDao;

    @Autowired
    private CbsUserService cbsUserService;

    @Autowired
    private PayAccountLogService payAccountLogService;

    /**
     * 创建指定日期的龙币支出与收入记录
     * 
     * @throws L99IllegalParamsException
     * @throws JSONException
     * @throws L99IllegalDataException
     */
    @Override
    public void buildMoneyDayStatistic(String time) throws L99IllegalParamsException, L99IllegalDataException, JSONException {

	ParamemeterAssert.assertDataNotNull(time);

	Date date = null;
	try {
	    date = new SimpleDateFormat("yyyy-MM-dd").parse(time);
	} catch (ParseException e) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}
	// 计算一天的开始与结束
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	calendar.set(Calendar.HOUR_OF_DAY, 0);
	calendar.set(Calendar.MINUTE, 0);
	calendar.set(Calendar.SECOND, 0);
	Date startTime = calendar.getTime();
	calendar.add(Calendar.DAY_OF_YEAR, 1);
	Date endTime = calendar.getTime();
	String timeKey = new SimpleDateFormat("yyMMdd").format(startTime);

	RedisDataIdentify addIt = new RedisDataIdentify(RedisConstants.MODEL_GOLD, GoldRedis.STATISTIC_lB_INCOME);
	addIt.setIdentifyId(timeKey);
	RedisDataIdentify minusIt = new RedisDataIdentify(RedisConstants.MODEL_GOLD, GoldRedis.STATISTIC_lB_OUTLAY);
	minusIt.setIdentifyId(timeKey);

	// 删除旧的redis记录
	redisSortSetHandler.del(addIt);
	redisSortSetHandler.del(minusIt);

	// 只查询大赢家的龙币日志
	String logTypes = MoneyLogServiceImpl.filterLogTypes(null);
	String moneyData = payAccountLogService.getLogs(null, logTypes, startTime, endTime, null, 10000);
	JSONObject moneyRet = new JSONObject(moneyData);
	if (moneyRet.getInt("code") != DataResponse.OK) {
	    throw new L99IllegalDataException(moneyRet.getString("code"), moneyRet.getString("msg"));
	}

	// 重新写入日志
	PayAccountLogListReponse payLogRes = new Gson().fromJson(moneyRet.getString("data"), PayAccountLogListReponse.class);
	for (PayAccountLogReponse plog : payLogRes.getLogs()) {
	    // float to duble
	    BigDecimal b = new BigDecimal(String.valueOf(plog.getUser_money()));
	    double money = b.doubleValue();
	    if (money < 0) {
		redisSortSetHandler.zIncrby(minusIt, String.valueOf(plog.getAccount_id()), Math.abs(money));
	    } else {
		redisSortSetHandler.zIncrby(addIt, String.valueOf(plog.getAccount_id()), money);
	    }
	}
    }

    @Override
    public MoneyStatisticResponse findByTime(Date time) throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(time);
	MoneyStatisticResponse resp = new MoneyStatisticResponse();
	MoneyStatistic moneyStatistic = moneyStatisticDao.findByTime(time);
	resp = GoldTransformUtil.transformMoneyStatistic(moneyStatistic);
	return resp;
    }

    @Override
    public MoneyStatisticListResponse findByBetweenTime(Date begin, Date end) throws L99IllegalParamsException {
	MoneyStatisticListResponse ret = new MoneyStatisticListResponse();
	ParamemeterAssert.assertDataNotNull(begin, end);
	if (begin.after(end)) { // 开始时间不能大于结束时间
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}
	SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
	List<Date> times = CbsTimeUtils.rangeDates(begin, end);
	int timeSize = times.size();
	double[] incomeArray = new double[timeSize];
	double[] outlayArray = new double[timeSize];
	List<String> timeRange = new ArrayList<String>();
	for (int i = 0; i < timeSize; i++) {
	    timeRange.add(timeFormat.format(times.get(i)));
	    MoneyStatistic moneyStatistic = moneyStatisticDao.findByTime(times.get(i));
	    if (moneyStatistic != null) {
		incomeArray[i] = moneyStatistic.getIncome();
		outlayArray[i] = moneyStatistic.getOutlay();
	    }
	}
	List<MoneyStatistic> moneyStatistics = moneyStatisticDao.findBetweenTime(begin, end);
	List<MoneyStatisticResponse> resp = new ArrayList<MoneyStatisticResponse>();
	if (moneyStatistics != null && moneyStatistics.size() > 0) {
	    for (MoneyStatistic moneyStatistic : moneyStatistics) {
		resp.add(GoldTransformUtil.transformMoneyStatistic(moneyStatistic));
	    }
	}
	ret.setMoney_statistic(resp);
	ret.setTime_range(timeRange);
	ret.setAll_income(incomeArray);
	ret.setAll_outlay(outlayArray);
	return ret;
    }

    @Override
    public void insertUserConsumer(String userId, double money) {
	String time = new SimpleDateFormat("yyMMdd").format(new Date());
	RedisDataIdentify indentify = null;
	if (money < 0) {
	    money = -money;
	    indentify = new RedisDataIdentify(RedisConstants.MODEL_GOLD, GoldRedis.STATISTIC_lB_OUTLAY);
	} else {
	    indentify = new RedisDataIdentify(RedisConstants.MODEL_GOLD, GoldRedis.STATISTIC_lB_INCOME);
	}
	indentify.setIdentifyId(time);
	redisSortSetHandler.zIncrby(indentify, userId, money);
    }

	@Override
	public void insertSystemConsumer(String userId, double money) {
		String time = new SimpleDateFormat("yyMMdd").format(new Date());
		RedisDataIdentify indentify ;
		if (money < 0) {
			money = -money;
			indentify = new RedisDataIdentify(RedisConstants.MODEL_GOLD, GoldRedis.STATISTIC_GOODS_OUTLAY);
		} else {
			indentify = new RedisDataIdentify(RedisConstants.MODEL_GOLD, GoldRedis.STATISTIC_RECHARGE_INCOME);
		}
		indentify.setIdentifyId(time);
		redisSortSetHandler.zIncrby(indentify, userId, money);
	}

	@Override
	public MoneyUserStatisticListResponse getUsersDayConsumer(String date, Integer type, Integer order) {
		MoneyUserStatisticListResponse moneyUserStatisticListResponse = new MoneyUserStatisticListResponse();
		List<MoneyUserStatisticResponse> list = new ArrayList<MoneyUserStatisticResponse>();
		// Set<ZSetOperations.TypedTuple<String>> addSet;
		// Set<ZSetOperations.TypedTuple<String>> minusSet;
		List<ResultSortData> addList = null;
		List<ResultSortData> minusList = null;
		if (StringUtils.isEmpty(date)) {
			date = new SimpleDateFormat("yyMMdd").format(new Date());
		}

		RedisDataIdentify addIt = new RedisDataIdentify(RedisConstants.MODEL_GOLD, GoldRedis.STATISTIC_lB_INCOME);
		addIt.setIdentifyId(date);
		RedisDataIdentify minusIt = new RedisDataIdentify(RedisConstants.MODEL_GOLD, GoldRedis.STATISTIC_lB_OUTLAY);
		minusIt.setIdentifyId(date);
		//充值和购买商品
		RedisDataIdentify addSystemIdentify = new RedisDataIdentify(RedisConstants.MODEL_GOLD, GoldRedis.STATISTIC_RECHARGE_INCOME);
		addSystemIdentify.setIdentifyId(date);
		RedisDataIdentify minusSystemIdentify = new RedisDataIdentify(RedisConstants.MODEL_GOLD, GoldRedis.STATISTIC_GOODS_OUTLAY);
		minusSystemIdentify.setIdentifyId(date);
		List<ResultSortData> addSystemList = redisSortSetHandler.reverseRangeWithScores(addSystemIdentify, 0L, Long.MAX_VALUE);
		List<ResultSortData> minusSystemList = redisSortSetHandler.reverseRangeWithScores(minusSystemIdentify, 0L, Long.MAX_VALUE);
		Map<Long, Double> addSystemMap = new HashMap<Long, Double>();
		Map<Long, Double> minusSystemMap = new HashMap<Long, Double>();
		for (ResultSortData resultSortData : addSystemList) {
			Long userId = Long.valueOf(resultSortData.getDataKey());
			addSystemMap.put(userId, resultSortData.getScore());
		}
		for (ResultSortData resultSortData : minusSystemList) {
			Long userId = Long.valueOf(resultSortData.getDataKey());
			minusSystemMap.put(userId, resultSortData.getScore());
		}
		if (type == null) {
			// 查询出指定日期的数据

			if (order == GoldConstants.MoneyStatisticOrder.ADD_DESC || order == GoldConstants.MoneyStatisticOrder.MINUS_DESC) {
				// 降序查询
				addList = redisSortSetHandler.reverseRangeWithScores(addIt, 0L, Long.MAX_VALUE);
				minusList = redisSortSetHandler.reverseRangeWithScores(minusIt, 0L, Long.MAX_VALUE);
			} else {
				// 升序查询
				addList = redisSortSetHandler.rangeWithScores(addIt, 0L, Long.MAX_VALUE);
				minusList = redisSortSetHandler.rangeWithScores(minusIt, 0L, Long.MAX_VALUE);
			}

			Map<Long, Double> map1 = new HashMap<Long, Double>();
			Map<Long, Double> map2 = new HashMap<Long, Double>();
			// 用户集合，用于批量查找用户信息
			Set<Long> userSet = new HashSet<Long>();
			for (ResultSortData rs : addList) {
				Long userId = Long.valueOf(rs.getDataKey());
				map1.put(userId, rs.getScore());
				userSet.add(userId);
			}
			for (ResultSortData rs : minusList) {
				Long userId = Long.valueOf(rs.getDataKey());
				map2.put(userId, rs.getScore());
				userSet.add(userId);
			}
			// 查找用户信息
			List<Long> userList = new ArrayList<Long>(userSet);
			Map<Long, CbsUserResponse> userMap = cbsUserService.findCsAccountMapByIds(userList);
			if (order == GoldConstants.MoneyStatisticOrder.ADD_DESC || order == GoldConstants.MoneyStatisticOrder.ADD_ASC) {
				// 收入为有序
				for (ResultSortData rs : addList) {
					Long userId = Long.valueOf(rs.getDataKey());
					MoneyUserStatisticResponse moneyUserStatisticResponse = new MoneyUserStatisticResponse();
					moneyUserStatisticResponse.setUser(userMap.get(userId));

					double income = rs.getScore();
					moneyUserStatisticResponse.setIncome(income);
					double internalIn = addSystemMap.get(userId) == null ? 0 : addSystemMap.get(userId);
					moneyUserStatisticResponse.setSystem_income(income - internalIn);
					Double value2 = map2.get(userId);
					if (value2 != null) {
						moneyUserStatisticResponse.setOutlay(value2);
						double internalOut = minusSystemMap.get(userId) == null ? 0 : minusSystemMap.get(userId);
						moneyUserStatisticResponse.setSystem_outlay(value2 - internalOut);
					}
					list.add(moneyUserStatisticResponse);
					userMap.remove(userId);
				}
				// 剩余的支出
				for (Long aLong : userMap.keySet()) {
					MoneyUserStatisticResponse moneyUserStatisticResponse = new MoneyUserStatisticResponse();
					moneyUserStatisticResponse.setUser(userMap.get(aLong));
					moneyUserStatisticResponse.setOutlay(map2.get(aLong));
					double internalOut = minusSystemMap.get(aLong) == null ? 0 : minusSystemMap.get(aLong);
					moneyUserStatisticResponse.setSystem_outlay(map2.get(aLong) - internalOut);
					list.add(moneyUserStatisticResponse);
				}
				// 当addSet.size() == 1，排序为asc时
				if (addList.size() == 1 && order == GoldConstants.MoneyStatisticOrder.ADD_ASC) {
					MoneyUserStatisticResponse moneyUserStatisticResponse = list.remove(0);
					list.add(moneyUserStatisticResponse);
				}
			} else {
				// 支出为有序
				for (ResultSortData rs : minusList) {
					Long userId = Long.valueOf(rs.getDataKey());
					MoneyUserStatisticResponse moneyUserStatisticResponse = new MoneyUserStatisticResponse();
					moneyUserStatisticResponse.setUser(userMap.get(userId));

					double outlay = rs.getScore();
					moneyUserStatisticResponse.setOutlay(outlay);
					double internalOut = minusSystemMap.get(userId) == null ? 0 : minusSystemMap.get(userId);
					moneyUserStatisticResponse.setSystem_outlay(outlay - internalOut);
					Double value1 = map1.get(userId);
					if (value1 != null) {
						moneyUserStatisticResponse.setIncome(value1);
						double internalIn = addSystemMap.get(userId) == null ? 0 : addSystemMap.get(userId);
						moneyUserStatisticResponse.setSystem_income(value1 - internalIn);
					}
					list.add(moneyUserStatisticResponse);
					userMap.remove(userId);
				}
				// 剩余的用户收入
				for (Long aLong : userMap.keySet()) {
					MoneyUserStatisticResponse moneyUserStatisticResponse = new MoneyUserStatisticResponse();
					moneyUserStatisticResponse.setUser(userMap.get(aLong));
					moneyUserStatisticResponse.setIncome(map1.get(aLong));
					double internalIn = addSystemMap.get(aLong) == null ? 0 : addSystemMap.get(aLong);
					moneyUserStatisticResponse.setSystem_income(map1.get(aLong) - internalIn);
					list.add(moneyUserStatisticResponse);
				}
				// 当minusSet.size() == 1，排序为asc时
				if (minusList.size() == 1 && order == GoldConstants.MoneyStatisticOrder.MINUS_ASC) {
					MoneyUserStatisticResponse moneyUserStatisticResponse = list.remove(0);
					list.add(moneyUserStatisticResponse);
				}
			}
		}
		moneyUserStatisticListResponse.setUsers_money(list);
		return moneyUserStatisticListResponse;
	}

}
