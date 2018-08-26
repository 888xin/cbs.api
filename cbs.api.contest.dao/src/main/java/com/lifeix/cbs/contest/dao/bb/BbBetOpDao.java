package com.lifeix.cbs.contest.dao.bb;

import java.util.Date;
import java.util.List;

import com.lifeix.cbs.contest.dto.bet.BetOp;

public interface BbBetOpDao {

    public BetOp selectById(long id);

    public Long insertAndGetPrimaryKey(BetOp entity);

    /**
     * 查询单个玩法单方下单记录
     * 
     * @param contestId
     * @param userId
     * @param suport
     * @return
     */
    public BetOp selectByBet(Long contestId, Long userId, int support);

    /**
     * 查询下单记录
     * 
     * @param contestId
     * @param userId
     * @param settle
     *            未结算的下单
     * @param start
     * @param limit
     * @return
     */
    public List<BetOp> findBbBetOp(Long contestId, Long userId, boolean settle, Long startId, int limit);

    /**
     * 更新内容id
     * 
     * @param entity
     * @return
     */
    public boolean updateContentId(BetOp entity);

    /**
     * 更新结算
     * 
     * @param bId
     * @param back
     * @param status
     * @return
     */
    public boolean updateSettle(Long bId, Double back, Integer status);

    /**
     * 修改下注记录
     */
    public boolean updateInner(Long bId, Double back, Double homeRoi, Double awayRoi);

    /**
     * 根据id更新contentId
     * 
     * @param contentId
     * @param bId
     * @return
     */
    public boolean updateContentIdById(Long contentId, Integer bId);

    /**
     * 查询胜平负下单记录（后台用）
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
    public List<BetOp> findBbBetOpList(Long contestId, Long userId, boolean settle, Long startId, int limit, Date startTime,
	    Date endTime);

    /**
     * add by lhx on 16-03-02 根据Id来删除记录
     */
    public boolean deleteById(Long id);

    /**
     * add by lhx on 16-03-14 根据contestId来查询记录
     */
    public List<BetOp> findBbBetOpByContestId(Long contestId, Long startId, Integer limit);

    /**
     * 用户混合下单次数
     * 
     * @param contestId
     * @param userId
     * @return
     */
    public int countUserMixBet(Long contestId, Long userId);

    /**
     * 根据战绩查询下单记录
     * 
     * @param contentId
     * @return
     */
    public BetOp selectByContent(Long contentId);

}