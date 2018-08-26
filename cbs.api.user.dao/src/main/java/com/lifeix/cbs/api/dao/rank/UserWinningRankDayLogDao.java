package com.lifeix.cbs.api.dao.rank;

import com.lifeix.cbs.api.dto.rank.UserWinningRankDayLog;

public interface UserWinningRankDayLogDao {
    /**
     * 查询某年某周的排名
     * 
     * @param year
     * @param week
     * @return
     */
    public UserWinningRankDayLog findRankByYearAndDay(Integer year, Integer day,Boolean isLongbi);

    public boolean insert(UserWinningRankDayLog rankLog);

    public boolean update(UserWinningRankDayLog rankLog);
}
