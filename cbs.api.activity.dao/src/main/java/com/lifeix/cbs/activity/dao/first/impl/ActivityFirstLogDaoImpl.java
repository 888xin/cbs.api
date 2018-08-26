package com.lifeix.cbs.activity.dao.first.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lifeix.cbs.activity.dao.ActivityDaoSupport;
import com.lifeix.cbs.activity.dao.first.ActivityFirstLogDao;
import com.lifeix.cbs.activity.dto.first.ActivityFirstLog;

/**
 * Created by yis on 16-5-9 下午5:47
 *
 * @Description
 */
@Repository("activityFirstLogDao")
public class ActivityFirstLogDaoImpl extends ActivityDaoSupport implements ActivityFirstLogDao {

    @Override
    public int insert(ActivityFirstLog bean) {
	return sqlSession.insert("ActivityFirstLogMapper.insert", bean);
    }

    @Override
    public List<ActivityFirstLog> find(Long userId) {
	ActivityFirstLog queryBean = new ActivityFirstLog();
	queryBean.setUserId(userId);
	return sqlSession.selectList("ActivityFirstLogMapper.findList", queryBean);
    }

    @Override
    public boolean update(ActivityFirstLog bean) {
	int num = sqlSession.update("ActivityFirstLogMapper.update", bean);
	if (num > 0) {
	    return true;
	} else {
	    return false;
	}
    }

}
