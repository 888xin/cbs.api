package com.lifeix.cbs.activity.dao.first;

import java.util.List;

import com.lifeix.cbs.activity.dto.first.ActivityFirstLog;

/**
 * Created by yis on 16-5-9 下午5:47
 *
 * @Description
 */
public interface ActivityFirstLogDao {

    /**
     * 插入记录
     */
    public int insert(ActivityFirstLog bean);

    /**
     * 根据userId查找
     * 
     * @param userId
     * @return
     */
    public List<ActivityFirstLog> find(Long userId);

    /**
     * 更新
     * 
     * @param bean
     * @return
     */
    public boolean update(ActivityFirstLog bean);
}
