package com.lifeix.cbs.api.dao.rank;

import com.lifeix.cbs.api.dto.rank.UserWinningRankLog;

public interface UserWinningRankLogDao {
    /**
	 * 查询某年某周的排名
	 * @param year
	 * @param week
	 * @return
	 */
	public UserWinningRankLog findRankByYearAndWeek(Integer year, Integer week,Boolean isLongbi);

    public boolean insert(UserWinningRankLog rankLog);

    public boolean update(UserWinningRankLog rankLog);
}
