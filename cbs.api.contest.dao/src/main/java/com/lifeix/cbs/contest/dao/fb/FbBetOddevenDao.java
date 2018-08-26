package com.lifeix.cbs.contest.dao.fb;

import java.util.Date;
import java.util.List;

import com.lifeix.cbs.contest.dto.bet.BetOddeven;

/**
 * Created by lhx on 16-5-4 下午3:52
 * 
 * @Description
 */
public interface FbBetOddevenDao {

    /**
     * 根据id来查询
     */
    public BetOddeven selectById(long id);

    /**
     * 写入记录并返回主键
     */
    public Long insertAndGetPrimaryKey(BetOddeven BetOddeven);

    /**
     * 查询下单记录
     */
    public List<BetOddeven> findFbBetOddeven(Long contestId, Long userId, boolean settle, Long startId, Integer limit);

    /**
     * 更新内容id
     */
    public boolean updateContentId(BetOddeven BetOddeven);

    /**
     * 根据id更新contentId
     */
    public boolean updateContentIdById(Long contentId, Integer bId);

    /**
     * 查询单个玩法单方下单记录
     */
    public BetOddeven selectByBet(Long contestId, Long userId, int support);

    /**
     * 更新结算
     */
    public boolean updateSettle(Long bId, Double back, Integer status);

    /**
     * 查询胜平负下单记录（后台用）
     */
    public List<BetOddeven> findFbBetOddevenList(Long contestId, Long userId, boolean settle, Long startId, int limit,
	    Date startTime, Date endTime);

    /**
     * add by lhx on 16-03-02 根据Id来删除记录
     */
    public boolean deleteById(Long id);

    /**
     * 根据contestId来查询记录
     */
    public List<BetOddeven> findFbBetOddevenByContestId(Long contestId, Long startId, Integer limit);

    /**
     * 用户混合下单次数
     */
    public int countUserMixBet(Long contestId, Long userId);

    /**
     * 根据战绩查询下单记录
     */
    public BetOddeven selectByContent(Long contentId);
}
