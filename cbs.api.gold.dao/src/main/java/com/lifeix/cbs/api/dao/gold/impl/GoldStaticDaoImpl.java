package com.lifeix.cbs.api.dao.gold.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lifeix.cbs.api.dao.ContentGoldDaoSupport;
import com.lifeix.cbs.api.dao.gold.GoldStatisticDao;
import com.lifeix.cbs.api.dto.gold.GoldStatistic;

@Repository("goldStatisticDao")
public class GoldStaticDaoImpl extends ContentGoldDaoSupport implements GoldStatisticDao {

    @Override
    public GoldStatistic findById(Long id) {
	GoldStatistic goldStatistic = sqlSession.selectOne("GoldStaticMapper.findById", id);
	return goldStatistic;
    }

    @Override
    public boolean insert(GoldStatistic goldStatistic) {
	int num = sqlSession.insert("GoldStaticMapper.insertAndGetPrimaryKey", goldStatistic);
	if (num > 0) {
	    return true;
	}
	return false;
    }

    @Override
    public boolean update(GoldStatistic goldCountStatistic) {
	int num = sqlSession.update("GoldStaticMapper.update", goldCountStatistic);
	if (num > 0) {
	    return true;
	} else {
	    return false;
	}
    }

    @Override
    public boolean delete(GoldStatistic goldCountStatistic) {
	int num = sqlSession.delete("GoldStaticMapper.delete", goldCountStatistic);
	if (num > 0) {
	    return true;
	} else {
	    return false;
	}
    }

    @Override
    public boolean batchInsert(List<GoldStatistic> counts) {
	int faild = 0;
	for (int i = 0; i < counts.size(); i++) {
	    boolean flag = insert(counts.get(i));
	    if (!flag) {
		faild++;
	    }
	}
	if (faild > 0) {
	    return false;
	}
	return true;
    }

    @Override
    public GoldStatistic findByTime(Date time) {

	Calendar calendar = Calendar.getInstance();
	calendar.setTime(time);
	// 获取年份
	Integer year = calendar.get(Calendar.YEAR);
	// 获取在一年中所在的天数
	Integer day = calendar.get(Calendar.DAY_OF_YEAR);
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("year", year);
	params.put("day", day);
	GoldStatistic goldStatistic = sqlSession.selectOne("GoldStaticMapper.findByTime", params);
	return goldStatistic;
    }

    @Override
    public List<GoldStatistic> findBetweenTime(Date begin, Date end) {
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
	List<GoldStatistic> goldStatistics = sqlSession.selectList("GoldStaticMapper.findBetweenTime", params);
	return goldStatistics;
    }

}
