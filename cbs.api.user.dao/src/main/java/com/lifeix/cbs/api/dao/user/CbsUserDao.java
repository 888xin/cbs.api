package com.lifeix.cbs.api.dao.user;

import java.util.List;
import java.util.Map;

import com.lifeix.cbs.api.dto.user.CbsUser;

public interface CbsUserDao {

    /**
     * 根据主键查询记录
     * 
     * @param id
     * @return
     */
    public CbsUser selectById(long id);

    /**
     * 插入 - 自动生成主键
     * 
     * @param user
     * @return
     */
    public Long insertAndGetPrimaryKey(CbsUser user);

    /**
     * 更新 - update方式
     * 
     * @param user
     * @return
     */
    public Boolean update(CbsUser user);

    /**
     * 根据主键删除
     * 
     * @param id
     * @return
     */
    public Boolean deleteById(long id);

    /**
     * 根据主键删除
     * 
     * @param id
     * @return
     */
    public Map<Long, CbsUser> selectByMobile(List<String> mobiles);

    /**
     * 
     * @param accountIds
     * @return
     */
    public Map<Long, CbsUser> findCsAccountMapByIds(List<Long> accountIds);

    public CbsUser getCbsUserByLongNo(Long longNO);

}