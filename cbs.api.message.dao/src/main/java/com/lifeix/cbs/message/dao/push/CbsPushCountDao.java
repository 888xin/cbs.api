package com.lifeix.cbs.message.dao.push;

import java.util.List;

import com.lifeix.cbs.message.dto.push.CbsPushCount;

public interface CbsPushCountDao {

    boolean replace(CbsPushCount pushCount);

    /**
     * 获取消息队列
     * 
     * @param target
     * @param showData
     * @return
     */
    public List<CbsPushCount> findRoiPushs(Integer showData);
    
    public boolean update(CbsPushCount count);
}
