package com.lifeix.cbs.api.impl.spark.gold;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lifeix.cbs.api.bean.gold.GoldLogListResponse;
import com.lifeix.cbs.api.bean.gold.GoldLogResponse;
import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.api.dao.money.MoneyStatisticDao;
import com.lifeix.cbs.api.dto.money.MoneyStatistic;
import com.lifeix.cbs.api.service.money.MoneyLogService;
import com.lifeix.cbs.api.service.spark.gold.MoneyStatisticDubbo;

public class MoneyStatisticDubboImpl implements MoneyStatisticDubbo {

    protected static final Logger LOG = LoggerFactory.getLogger(MoneyStatisticDubboImpl.class);

    @Autowired
    private MoneyLogService moneyLogService;

    @Autowired
    private MoneyStatisticDao moneyStatisticDao;

    @Override
    public boolean insert(Integer year, Integer day) {
	boolean flag = true;
	try {
	    double income = 0d;
	    double outlay = 0d;
	    long inCounts = 0L;
	    long outCounts = 0L;
	    Calendar calendar = Calendar.getInstance();
	    calendar.set(Calendar.YEAR, year);
	    calendar.set(Calendar.DAY_OF_YEAR, day);
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    Date date = calendar.getTime();
	    Date startTime = CbsTimeUtils.dayOfStart(date);
	    Date endTime = CbsTimeUtils.dayOfEnd(date);
	    
	    Long id = null;
	    List<GoldLogResponse> goldLogs = null;
	    do {
		GoldLogListResponse goldLogListResponse = moneyLogService.findGoldLogList(null, null, null, startTime,
		        endTime, false, id, 200);
		goldLogs = goldLogListResponse.getGold_logs();
		for (GoldLogResponse goldLog : goldLogs) {
		    id = goldLog.getLog_id();
		    double money = goldLog.getMoney();
		    if (money > 0) { // 系统支出
			outlay += money;
			outCounts++;
		    } else {// 系统收入
			income += money;
			inCounts++;
		    }
		}
	    } while (goldLogs != null && goldLogs.size() > 0);

	    // 获取当天日志，超过2000条，不统计
	    // GoldLogListResponse goldLogListResponse =
	    // moneyLogService.findGoldLogList(null, null, null, startTime,
	    // endTime, false, null,
	    // 2000);
	    // List<GoldLogResponse> goldLogs =
	    // goldLogListResponse.getGold_logs();
	    // for (GoldLogResponse goldLog : goldLogs) {
	    // double money = goldLog.getMoney();
	    // if (money > 0) { // 系统支出
	    // outlay += money;
	    // outCounts++;
	    // } else {// 系统收入
	    // income += money;
	    // inCounts++;
	    // }
	    // }
	    
	    MoneyStatistic moneyStatistic = new MoneyStatistic();
	    moneyStatistic.setIncome(Math.abs(income));
	    moneyStatistic.setOutlay(outlay);
	    // total为income 减去 outlay
	    moneyStatistic.setTotal(Math.abs(income) - Math.abs(outlay));
	    moneyStatistic.setInCounts(inCounts);
	    moneyStatistic.setOutCounts(outCounts);
	    moneyStatistic.setYear(year);
	    moneyStatistic.setDay(day);
	    moneyStatistic.setCreateTime(new Date(System.currentTimeMillis()));
	    flag = moneyStatisticDao.insert(moneyStatistic);
	} catch (Exception e) {
	    flag = false;
	    LOG.error(e.getMessage(), e);
	}
	return flag;
    }

}
