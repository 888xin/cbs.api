package com.lifeix.cbs.api.dao.user;

import java.util.List;

import com.lifeix.cbs.api.dto.user.CbsUserWx;

public interface CbsUserWxDao {

    /**
     * 查询用户列表
     * 
     * @param userIds
     * @return
     */
    public List<CbsUserWx> selectByUsers(List<Long> userIds);

    /**
     * 根据主键查询记录
     * 
     * @param id
     * @return
     */
    public CbsUserWx selectById(long userId);

    /**
     * 插入 - 自动生成主键
     * 
     * @param user
     * @return
     */
    public void insertAndGetPrimaryKey(CbsUserWx user);

    /**
     * 更新 - update方式
     * 
     * @param user
     * @return
     */
    public Boolean update(CbsUserWx user);

}