package com.lifeix.cbs.contest.dao.bb;

import java.util.Date;
import java.util.List;

import com.lifeix.cbs.contest.dto.bet.BetSize;

/**
 * Created by lhx on 16-4-27 上午9:54
 * 
 * @Description
 */
public interface BbBetSizeDao {

    /**
     * 根据id来查询
     */
    public BetSize selectById(long id);

    /**
     * 写入记录并返回主键
     */
    public Long insertAndGetPrimaryKey(BetSize betSize);

    /**
     * 查询下单记录
     */
    public List<BetSize> findBbBetSize(Long contestId, Long userId, boolean settle, Long startId, Integer limit);

    /**
     * 更新内容id
     */
    public boolean updateContentId(BetSize betSize);

    /**
     * 根据id更新contentId
     */
    public boolean updateContentIdById(Long contentId, Integer bId);

    /**
     * 查询单个玩法单方下单记录
     */
    public BetSize selectByBet(Long contestId, Long userId, int support);

    /**
     * 更新结算
     */
    public boolean updateSettle(Long bId, Double back, Integer status);

    /**
     * 修改下注记录
     */
    public boolean updateInner(Long bId, Double back, Double bigRoi, Double tinyRoi);

    /**
     * 查询胜平负下单记录（后台用）
     */
    public List<BetSize> findBbBetSizeList(Long contestId, Long userId, boolean settle, Long startId, int limit,
	    Date startTime, Date endTime);

    /**
     * add by lhx on 16-03-02 根据Id来删除记录
     */
    public boolean deleteById(Long id);

    /**
     * add by lhx on 16-03-14 根据contestId来查询记录
     */
    public List<BetSize> findBbBetSizeByContestId(Long contestId, Long startId, Integer limit);

    /**
     * 用户混合下单次数
     */
    public int countUserMixBet(Long contestId, Long userId);

    /**
     * 根据战绩查询下单记录
     */
    public BetSize selectByContent(Long contentId);
}
