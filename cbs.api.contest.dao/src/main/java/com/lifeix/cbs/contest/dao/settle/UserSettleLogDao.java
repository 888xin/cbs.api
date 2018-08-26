package com.lifeix.cbs.contest.dao.settle;

import java.util.Date;
import java.util.List;

import com.lifeix.cbs.contest.dto.settle.UserSettleLog;

public interface UserSettleLogDao {
    /**
     * 根据id查找记录
     * 
     * @param id
     * @return
     */
    public List<UserSettleLog> findUserSettleListById(Long id, Boolean isLongbi, int limit);

    /**
     * 添加结算日志
     * 
     * @param entity
     * @return
     */
    public boolean insert(UserSettleLog entity);

    public UserSettleLog findById(Long lastId);
    
    public List<UserSettleLog> findByIds(List<Long> ids);
    
    /**
     * 用户的结算日志
     * 
     * @param userId
     * @param pageNum
     * @param startTime
     * @param endTime
     * @param limit
     * @return
     */
    public List<UserSettleLog> getUserSettleLogs(Integer userId, Integer page, String startTime, String endTime,
	    Integer limit);

    /**
     * 获取用户结算记录数
     * 
     * @param userId
     * @param status
     * @param startTime
     * @param endTime
     * @return
     */
    public Integer getUserSettleLogCounts(Long userId, Integer status, Date startTime, Date endTime);

    /**
     * 用户单条的结算日志
     * 
     * @param userId
     * @param pageNum
     * @param startTime
     * @param endTime
     * @param limit
     * @return
     */
    public UserSettleLog getUserSettleLog(Long userId, Integer type, Long contestId, Integer playId, Integer support);

    /**
     * 根据Id来进行删除
     */
    public Integer deleteByContestId(Integer contestType, Long contestId);

}
