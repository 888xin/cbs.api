package com.lifeix.cbs.api.impl.spark.gold;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.dao.gold.GoldLogDao;
import com.lifeix.cbs.api.dao.gold.GoldStatisticDao;
import com.lifeix.cbs.api.dto.gold.GoldLog;
import com.lifeix.cbs.api.dto.gold.GoldStatistic;
import com.lifeix.cbs.api.service.spark.gold.GoldStatisticDubbo;
import com.lifeix.exception.service.L99IllegalParamsException;

public class GoldStatisticDubboImpl implements GoldStatisticDubbo {

    @Autowired
    private GoldStatisticDao goldStatisticDao;

    @Autowired
    private GoldLogDao goldLogDao;

    @Override
    public boolean insert(Integer year, Integer day) throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(year, day);

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
	Date time = calendar.getTime();
	List<GoldLog> incomeLogs = goldLogDao.findSystemIncomeLog(time, null, null);
	List<GoldLog> outlayLogs = goldLogDao.findSystemOutlayLog(time, null, null);
	if (incomeLogs != null && incomeLogs.size() > 0) {
	    // 总的收入记录数
	    inCounts = incomeLogs.size();
	    for (GoldLog log : incomeLogs) {
		income += log.getMoney();
	    }
	}
	if (outlayLogs != null && outlayLogs.size() > 0) {
	    // 总的支出记录数
	    outCounts = outlayLogs.size();
	    for (GoldLog log : outlayLogs) {
		outlay += log.getMoney();
	    }
	}
	GoldStatistic goldStatistic = new GoldStatistic();
	goldStatistic.setIncome(Math.abs(income));
	goldStatistic.setOutlay(outlay);
	// total为income 减去 outlay
	goldStatistic.setTotal(Math.abs(income) - Math.abs(outlay));
	goldStatistic.setInCounts(inCounts);
	goldStatistic.setOutCounts(outCounts);
	goldStatistic.setYear(year);
	goldStatistic.setDay(day);
	goldStatistic.setCreateTime(new Date(System.currentTimeMillis()));
	return goldStatisticDao.insert(goldStatistic);
    }

}
