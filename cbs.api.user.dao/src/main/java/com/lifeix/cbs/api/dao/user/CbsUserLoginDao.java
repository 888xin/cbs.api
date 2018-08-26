package com.lifeix.cbs.api.dao.user;

import com.lifeix.cbs.api.dto.user.CbsUserLogin;

public interface CbsUserLoginDao {

    /**
     * 根据主键查询记录
     * 
     * @param userId
     * @return
     */
    public CbsUserLogin selectById(long userId);

    /**
     * 插入
     * 
     * @param cbsUserLogin
     * @return
     */
    public boolean insert(CbsUserLogin cbsUserLogin);

    /**
     * 更新 - update方式
     * 
     * @param cbsUserLogin
     * @return
     */
    public Boolean update(CbsUserLogin cbsUserLogin);

}