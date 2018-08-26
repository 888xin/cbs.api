package com.lifeix.cbs.contest.dao.yy;

import java.util.Date;
import java.util.List;

import com.lifeix.cbs.contest.dto.yy.YyBet;

public interface YyBetDao {

    public YyBet selectById(long id);

    public Long insertAndGetPrimaryKey(YyBet entity);

    /**
     * 查询单个玩法单方下单记录
     * 
     * @param contestId
     * @param userId
     * @param suport
     * @return
     */
    public YyBet selectByBet(Long contestId, Long userId, int support);

    /**
     * 查询下单记录
     * 
     * @param contestId
     * @param userId
     * @param start
     * @param limit
     * @return
     */
    public List<YyBet> findYyBet(Long contestId, Long userId, boolean settle, Long startId, int limit);

    /**
     * 查询用户的下单记录
     * 
     * @param userId
     * @param startId
     * @param limit
     * @return
     */
    public List<YyBet> findUserYyBet(Long userId, Long startId, int limit);

    /**
     * 更新内容id
     * 
     * @param entity
     * @return
     */
    public boolean updateContentId(YyBet entity);

    /**
     * 更新结算
     * 
     * @param id
     * @param back
     * @param status
     * @return
     */
    public boolean updateSettle(Long id, Double back, Integer status);

    /**
     * 根据id更新contentId
     * 
     * @param contentId
     * @param id
     * @return
     */
    public boolean updateContentIdById(Long contentId, Integer id);

    /**
     * 查询下单记录（后台用）
     * 
     * @param contestId
     * @param userId
     * @param settle
     * @param startId
     * @param limit
     * @param startTime
     * @param endTime
     * @return
     */
    public List<YyBet> findYyBetList(Long contestId, Long userId, boolean settle, Long startId, int limit, Date startTime,
	    Date endTime);

    /**
     * 用户混合下单次数
     * 
     * @param contestId
     * @param userId
     * @return
     */
    public int countUserMixBet(Long contestId, Long userId);

}