package com.lifeix.cbs.api.service.spark;

import com.lifeix.user.beans.CustomResponse;

/**
 * 统计用户下注数据 并生成榜单数据任务
 * 
 * @author lifeix
 * 
 */
public interface UserContestTaskDubbo {

    /**
     * 更新猜友圈
     * 
     * @return
     */
    public CustomResponse completeFriendCircle();

    /**
     * 用户总榜的竞猜数据统计 生成总榜
     */
    public CustomResponse processUserContestStatisticsTask();

    /**
     * 用户周榜的竞猜数据统计 生成周榜
     */
    public CustomResponse processUserContestStatisticsWeek();

    /**
     * 用户周榜的竞猜数据统计 生成周榜 (强制清除缓存)
     */
    public CustomResponse processUserContestStatisticsWeek(boolean cacheFlag);

    /**
     * 清理周榜的redis
     * 
     * @param type
     */
    public CustomResponse clearWeekRankRedis(int type);

}
