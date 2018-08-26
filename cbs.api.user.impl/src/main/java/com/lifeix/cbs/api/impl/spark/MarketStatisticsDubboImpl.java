package com.lifeix.cbs.api.impl.spark;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.user.LifeixUserApiUtil;
import com.lifeix.cbs.api.dao.market.CbsMarketDayStatisticDao;
import com.lifeix.cbs.api.dto.market.CbsMarketDayStatistic;
import com.lifeix.cbs.api.impl.market.CbsMarketServiceImpl;
import com.lifeix.cbs.api.impl.redis.RedisMarketHandler;
import com.lifeix.cbs.api.service.spark.MarketStatisticsDubbo;
import com.lifeix.common.utils.StringUtil;
import com.lifeix.exception.service.L99NetworkException;
import com.lifeix.user.beans.log.AccountLogResponse;

@Service("marketStatisticsDubbo")
public class MarketStatisticsDubboImpl extends ImplSupport implements MarketStatisticsDubbo {
    protected static final Logger LOG = LoggerFactory.getLogger(CbsMarketServiceImpl.class);

    @Autowired
    private RedisMarketHandler redisMarketHandler;

    @Autowired
    private CbsMarketDayStatisticDao cbsMarketDayStatisticDao;

    @Override
    public void marketDayStatistic(String date) throws L99NetworkException, JSONException, ParseException {

	// ###################1.初始化###################
	Integer limit = 1000;
	Long startId = 0L;
	CbsMarketDayStatistic ms = null;
	Map<String, Integer> map = null;

	// 重置临时统计数据结构
	Set<byte[]> delMarketSet = redisMarketHandler.getMarketList();
	Iterator<byte[]> it = delMarketSet.iterator();

	while (it.hasNext()) {
	    String market = new String(it.next());
	    redisMarketHandler.delMarketStatistic(market);
	}
	redisMarketHandler.delMarket();

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	if (StringUtils.isEmpty(date)) {
	    Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.DATE, -1);
	    date = sdf.format(cal.getTime());
	}

	Date statisticDate = sdf.parse(date);

	// ###################2.redis统计数据###################
	// 查询前1000数据
	List<AccountLogResponse> list = LifeixUserApiUtil.getInstance().findRegisterMarket(date, startId, limit);

	while (list.size() > 0) {
	    for (AccountLogResponse bean : list) {
		// 将当前用户的渠道插入到渠道集合中
		redisMarketHandler.addMarket(bean.getMarket());
		// 统计当前用户性别
		redisMarketHandler.addMarketStatistic(bean.getMarket(), bean.getLoginTimes() == 0 ? "0" : "1", 1);
	    }
	    if (list.size() < 1000) {
		break;
	    }

	    startId = list.get(limit - 1).getAccountId();
	    // 查询前一天未处理数据
	    list = LifeixUserApiUtil.getInstance().findRegisterMarket(date, startId, limit);
	}
	list = null;

	// ################### 3>数据入库###################
	Set<byte[]> marketSet = redisMarketHandler.getMarketList();
	Iterator<byte[]> iterator = marketSet.iterator();

	while (iterator.hasNext()) {
	    String market = new String(iterator.next());
	    // 获取渠道数据
	    map = redisMarketHandler.getMarketStatistic(market);
	    // 填充统计数据
	    ms = new CbsMarketDayStatistic();
	    ms.setMarketCode(StringUtil.MD5Encode(market));

	    ms.setMaleNums(map.get("1") == null ? 0 : map.get("1"));// 男性
	    ms.setFemaleNums(map.get("0") == null ? 0 : map.get("0"));// 女性
	    ms.setTotalNums(ms.getMaleNums() + ms.getFemaleNums());
	    ms.setStatisticDate(statisticDate);
	    // 插入统计数据
	    cbsMarketDayStatisticDao.insert(ms);
	}
    }

}
