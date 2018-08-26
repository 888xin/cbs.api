package com.lifeix.cbs.api.impl.gold;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.bean.gold.GoldStatisticListResponse;
import com.lifeix.cbs.api.bean.gold.GoldStatisticResponse;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.dao.gold.GoldStatisticDao;
import com.lifeix.cbs.api.dto.gold.GoldStatistic;
import com.lifeix.cbs.api.service.gold.GoldStatisticService;
import com.lifeix.cbs.api.transform.GoldTransformUtil;
import com.lifeix.exception.service.L99IllegalParamsException;

@Service("goldStatisticService")
public class GoldStatisticServiceImpl extends ImplSupport implements GoldStatisticService {

    @Autowired
    private GoldStatisticDao goldStatisticDao;

    @Override
    public boolean insert(Double income, Double outlay, Long inCounts, Long outCounts, Integer year, Integer day)
	    throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(year, day);
	GoldStatistic goldStatistic = new GoldStatistic();
	goldStatistic.setIncome(income);
	goldStatistic.setOutlay(outlay);
	goldStatistic.setTotal(income + outlay);
	goldStatistic.setInCounts(inCounts);
	goldStatistic.setOutCounts(outCounts);
	goldStatistic.setYear(year);
	goldStatistic.setDay(day);
	goldStatistic.setCreateTime(new Date(System.currentTimeMillis()));
	return goldStatisticDao.insert(goldStatistic);
    }

    @Override
    public GoldStatisticResponse findByTime(Date time) throws L99IllegalParamsException {
	GoldStatisticResponse resp = new GoldStatisticResponse();
	ParamemeterAssert.assertDataNotNull(time);
	GoldStatistic goldStatistic = goldStatisticDao.findByTime(time);
	resp = GoldTransformUtil.transformGoldStatistic(goldStatistic);
	return resp;
    }

    @Override
    public GoldStatisticListResponse findByBetweenTime(Date begin, Date end) throws L99IllegalParamsException {
	GoldStatisticListResponse ret = new GoldStatisticListResponse();
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
	    GoldStatistic goldStatistic = goldStatisticDao.findByTime(times.get(i));
	    if (goldStatistic != null) {
		incomeArray[i] = goldStatistic.getIncome();
		outlayArray[i] = goldStatistic.getOutlay();
	    }

	}
	List<GoldStatistic> goldStatistics = goldStatisticDao.findBetweenTime(begin, end);
	List<GoldStatisticResponse> resp = new ArrayList<GoldStatisticResponse>();
	if (goldStatistics != null && goldStatistics.size() > 0) {
	    for (GoldStatistic goldStatistic : goldStatistics) {
		resp.add(GoldTransformUtil.transformGoldStatistic(goldStatistic));
	    }
	}
	ret.setGold_statistic(resp);
	ret.setTime_range(timeRange);
	ret.setAll_income(incomeArray);
	ret.setAll_outlay(outlayArray);
	return ret;
    }
}
