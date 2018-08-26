package com.lifeix.cbs.api.dao.gold.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.api.dao.ContentGoldDaoSupport;
import com.lifeix.cbs.api.dao.gold.GoldLogDao;
import com.lifeix.cbs.api.dto.gold.GoldLog;
import com.lifeix.cbs.api.service.user.CbsUserService;

@Repository("goldLogDao")
public class GoldLogDaoImpl extends ContentGoldDaoSupport implements GoldLogDao {

    @Override
    public GoldLog findById(Long id) {
	GoldLog goldLog = sqlSession.selectOne("GoldLogMapper.findById", id);
	return goldLog;
    }

    @Override
    public boolean insert(GoldLog goldLog) {
	int num = sqlSession.insert("GoldLogMapper.insertAndGetPrimaryKey", goldLog);
	if (num > 0) {
	    return true;
	}
	return false;
    }

    @Override
    public Long insertAndGetKey(GoldLog goldLog) {
	int num = sqlSession.insert("GoldLogMapper.insertAndGetPrimaryKey", goldLog);
	if (num > 0) {
	    return goldLog.getLogId();
	}
	return -1L;
    }

    @Override
    public boolean update(GoldLog goldLog) {
	int num = sqlSession.update("GoldLogMapper.update", goldLog);
	if (num > 0) {
	    return true;
	} else {
	    return false;
	}
    }

    @Override
    public boolean delete(GoldLog goldLog) {
	int num = sqlSession.delete("GoldLogMapper.delete", goldLog);
	if (num > 0) {
	    return true;
	} else {
	    return false;
	}
    }

    @Override
    public List<GoldLog> findGoldLogs(Long userId, Long startId, Integer limit) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("userId", userId);
	params.put("startId", startId);
	if (limit == null) {
	    // 默认显示20条记录
	    limit = 20;
	}
	params.put("limit", limit);
	List<GoldLog> goldLogs = sqlSession.selectList("GoldLogMapper.findGoldLogs", params);
	return goldLogs;
    }

    @Override
    public List<GoldLog> countGold(Date createTime, Date endTime) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("createTime", createTime);
	params.put("endTime", endTime);
	List<GoldLog> godLogs = sqlSession.selectList("GoldLogMapper.countGold", params);
	return godLogs;
    }

    @Override
    public List<GoldLog> findGoldLogs(Long userId, Long startId, Integer limit, Date createTime, Date endTime) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("userId", userId);
	if (startId != null) {
	    params.put("startId", startId);
	} else {
	    params.put("startId", 0);
	}
	if (limit == null) {
	    // 默认显示20条记录
	    limit = 20;
	}
	params.put("limit", limit);
	params.put("createTime", createTime);
	params.put("endTime", endTime);
	List<GoldLog> goldLogs = sqlSession.selectList("GoldLogMapper.findGoldLogs", params);
	return goldLogs;
    }

    @Override
    public List<GoldLog> findSystemGoldLogs(Long userId, Long roleId, Long startId, Integer limit, Date beginTime,
	    Date endTime) {
	Map<String, Object> params = new HashMap<String, Object>();
	if (userId != null) {
	    params.put("userId", userId);
	}
	if (startId != null) {
	    params.put("startId", startId);
	}
	if (roleId != null) {
	    params.put("roleId", roleId);
	}
	if (limit != null) {
	    params.put("limit", limit);
	} else {
	    params.put("limit", 20);
	}
	if (beginTime != null) {
	    params.put("beginTime", beginTime);
	}
	if (endTime != null) {
	    params.put("endTime", endTime);
	}
	List<GoldLog> goldLogs = sqlSession.selectList("GoldLogMapper.findSystemGoldLogs", params);
	return goldLogs;
    }

    @Override
    public List<GoldLog> findSystemIncomeLog(Date time, Long startId, Integer limit) {
	Date start = CbsTimeUtils.dayOfStart(time);
	Date end = CbsTimeUtils.dayOfEnd(time);
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("start", start);
	params.put("end", end);
	if (startId != null) {
	    params.put("startId", startId);
	}
	params.put("limit", limit);
	List<GoldLog> goldLogs = sqlSession.selectList("GoldLogMapper.findSystemIncomeLog", params);
	return goldLogs;
    }

    @Override
    public List<GoldLog> findSystemOutlayLog(Date time, Long startId, Integer limit) {
	Date start = CbsTimeUtils.dayOfStart(time);
	Date end = CbsTimeUtils.dayOfEnd(time);
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("start", start);
	params.put("end", end);
	if (startId != null) {
	    params.put("startId", startId);
	}
	params.put("limit", limit);
	List<GoldLog> goldLogs = sqlSession.selectList("GoldLogMapper.findSystemOutlayLog", params);
	return goldLogs;
    }

    @Override
    public List<GoldLog> findSystemLogDetail(Long logId, Long userId, Date startDate,Date endDate, Long startId, Integer limit, Integer[] type) {
	Map<String, Object> params = new HashMap<String, Object>();
	if (logId == null) {
	    //Date start = CbsTimeUtils.dayOfStart(time);
	    //Date end = CbsTimeUtils.dayOfEnd(time);
	    params.put("start", startDate);
	    params.put("end", endDate);
	}

	if (logId != null) {
	    params.put("logId", logId);
	}
	if (userId != null) {
	    params.put("userId", userId);
	}
	if (startId != null) {
	    params.put("startId", startId);
	}
	if (type != null) {
	    params.put("typeArray", type);
	}
	params.put("limit", limit);
	List<GoldLog> goldLogs = sqlSession.selectList("GoldLogMapper.findSystemLogDetail", params);
	return goldLogs;
    }

}
