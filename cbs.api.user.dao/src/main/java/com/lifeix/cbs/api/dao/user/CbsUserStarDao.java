package com.lifeix.cbs.api.dao.user;

import java.util.List;
import java.util.Map;

import com.lifeix.cbs.api.dto.user.CbsUserStar;

public interface CbsUserStarDao {

    /**
     * 根据主键查询记录
     */
    public CbsUserStar selectByUser(long userId);

    /**
     * 插入 - 自动生成主键
     */
    public Boolean insert(CbsUserStar user);

    /**
     * 更新 - update方式
     */
    public Boolean update(CbsUserStar user);

    /**
     * 根据用户Id删除推荐
     */
    public Boolean deleteByUser(Long userId);

    /**
     * 获取所有推荐列表
     */
    public List<CbsUserStar> findAllStars(Boolean hideFlag, Long startId, int limit);

    /**
     * 查找所有推荐用户的id
     */
    public List<Long> findAllStarsIds();

    /**
     * 根据ids来查询
     */
    public Map<Long, CbsUserStar> findByIds(List<Long> ids);

    /**
     * 获取推荐用户列表
     */
    public List<CbsUserStar> findStarUsers(int limit);

}