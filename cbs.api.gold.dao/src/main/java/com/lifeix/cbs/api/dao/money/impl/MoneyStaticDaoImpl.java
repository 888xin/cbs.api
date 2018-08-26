package com.lifeix.cbs.api.dao.money.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lifeix.cbs.api.dao.ContentGoldDaoSupport;
import com.lifeix.cbs.api.dao.money.MoneyStatisticDao;
import com.lifeix.cbs.api.dto.money.MoneyStatistic;

@Repository("moneyStatisticDao")
public class MoneyStaticDaoImpl extends ContentGoldDaoSupport implements MoneyStatisticDao {

    @Override
    public MoneyStatistic findById(Long id) {
	MoneyStatistic moneyStatistic = sqlSession.selectOne("MoneyStaticMapper.findById", id);
	return moneyStatistic;
    }

    @Override
    public boolean insert(MoneyStatistic moneyStatistic) {
	int num = sqlSession.insert("MoneyStaticMapper.insertAndGetPrimaryKey", moneyStatistic);
	if (num > 0) {
	    return true;
	}
	return false;
    }

    @Override
    public boolean update(MoneyStatistic moneyStatistic) {
	int num = sqlSession.update("MoneyStaticMapper.update", moneyStatistic);
	if (num > 0) {
	    return true;
	} else {
	    return false;
	}
    }

    @Override
    public boolean delete(MoneyStatistic moneyStatistic) {
	int num = sqlSession.delete("MoneyStaticMapper.delete", moneyStatistic);
	if (num > 0) {
	    return true;
	} else {
	    return false;
	}
    }

    @Override
    public MoneyStatistic findByTime(Date time) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(time);
	// 获取年份
	Integer year = calendar.get(Calendar.YEAR);
	// 获取在一年中所在的天数
	Integer day = calendar.get(Calendar.DAY_OF_YEAR);
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("year", year);
	params.put("day", day);
	MoneyStatistic moneyStatistic = sqlSession.selectOne("MoneyStaticMapper.findByTime", params);
	return moneyStatistic;
    }

    @Override
    public List<MoneyStatistic> findBetweenTime(Date begin, Date end) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(begin);
	Integer beginYear = calendar.get(Calendar.YEAR);
	Integer beginDay = calendar.get(Calendar.DAY_OF_YEAR);
	calendar.setTime(end);
	Integer endYear = calendar.get(Calendar.YEAR);
	Integer endDay = calendar.get(Calendar.DAY_OF_YEAR);
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("beginYear", beginYear);
	params.put("beginDay", beginDay);
	params.put("endYear", endYear);
	params.put("endDay", endDay);
	List<MoneyStatistic> moneyStatistics = sqlSession.selectList("MoneyStaticMapper.findBetweenTime", params);
	return moneyStatistics;
    }

    

}
