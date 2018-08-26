package com.lifeix.cbs.api.dao.user;

import java.util.List;

import com.lifeix.cbs.api.dto.user.CbsUserRankLog;

public interface UserRankLogDao {
    /**
     * 根据id查找记录
     * 
     * @param id
     * @return
     */
    public List<CbsUserRankLog> findAll();

    public boolean update(CbsUserRankLog rankLog);

    public boolean insert(CbsUserRankLog numRankLog);


}
