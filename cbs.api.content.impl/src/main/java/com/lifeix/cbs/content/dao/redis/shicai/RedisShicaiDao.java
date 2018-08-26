package com.lifeix.cbs.content.dao.redis.shicai;

import java.util.List;

import com.lifeix.cbs.content.bean.shicai.PrizeRecordResponse;




/**
 * @author wenhuans
 * 2015年11月4日 下午2:13:47
 * 
 */
public interface RedisShicaiDao {

    /**
     * 记录本期或上一期的scId
     * @author wenhuans
     * 2015年11月4日下午2:15:39
     *
     */
    public void setIngOrLastScId(Long scId, Boolean ingScIdFlag);
    
    /**
     * 获取本期或上一期的scId
     * @author wenhuans
     * 2015年11月4日下午2:17:39
     *
     */
    public Long getIngOrLastScId(Boolean ingScIdFlag);
    
    /**
     * 增加奖池的数额、赌注注数
     * @author wenhuans
     * 2015年11月5日上午9:34:18
     *
     */
    public void addPrizePool(Long scId, Long money, Long count, Boolean oneFlag);
    
    /**
     * 获取奖池一区、二区数额
     * @author wenhuans
     * 2015年11月4日下午4:07:02
     *
     */
    public Double getPrizePoolMoney(Long scId, Boolean oneFlag);
    
    /**
     * 获取奖池一区、二区赌注注数
     * @author wenhuans
     * 2015年11月4日下午4:23:54
     *
     */
    public Long getPrizePoolCount(Long scId, Boolean oneFlag);
    
    /**
     * 设置本期游戏有哪些play记录
     * @author wenhuans
     * 2015年11月5日上午11:54:00
     *
     */
    public void setBetPlayId(Long scId, Long playId);
    
    /**
     * 获取本期游戏有哪些play记录
     * @author wenhuans
     * 2015年11月5日上午11:56:25
     *
     */
    public List<Long> getBetPlayId(Long scId);
    
    /**
     * 用户获奖记录
     * @author wenhuans
     * 2015年11月5日下午4:45:15
     *
     */
    public void setAccountEarnRecord(Long scId, Long accountId, Long bedPoint);
    
    /**
     * 前3名用户获奖信息
     * @author wenhuans
     * 2015年11月5日下午6:37:18
     *
     */
    public List<PrizeRecordResponse> getAccountEarnRecord(Long scId);
    
    /**
     * 设置一等奖二等奖获奖注数
     * @author wenhuans
     * 2015年11月9日下午3:58:57
     *
     */
    public void setFirstOrSecondPrizeCount(Long scId, Long count, Boolean firstPrizeFlag);
    
    /**
     * 获取一等奖二等奖获奖注数
     * @author wenhuans
     * 2015年11月9日下午4:00:53
     *
     */
    public Long getFirstOrSecondPrizeCount(Long scId, Boolean firstPrizeFlag);
    
    /**
     * 新的一局new标识
     * @author wenhuans
     * 2015年11月13日下午3:49:19
     *
     */
    public void setNewRecord(Long accountId, Long scId);
    
    /**
     * 新的一局new标识
     * @author wenhuans
     * 2015年11月13日下午3:49:19
     *
     */
    public Long getNewRecord(Long accountId);
    
    /**
     * 清楚new标识
     * @author wenhuans
     * 2015年11月13日下午3:59:32
     *
     */
    public void clearNewRecord(Long accountId);
    
}

