package com.lifeix.cbs.api.impl.rank;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.bean.user.UserGraphListResponse;
import com.lifeix.cbs.api.bean.user.UserStatisticsListResponse;
import com.lifeix.cbs.api.bean.user.UserStatisticsResponse;
import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestMemcached;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.common.util.RedisConstants;
import com.lifeix.cbs.api.common.util.RedisConstants.RankRedis;
import com.lifeix.cbs.api.dao.rank.UserContestStatisticsDao;
import com.lifeix.cbs.api.dao.rank.UserContestStatisticsWeekDao;
import com.lifeix.cbs.api.dao.user.CbsUserDao;
import com.lifeix.cbs.api.dto.rank.UserContestStatistics;
import com.lifeix.cbs.api.dto.rank.UserContestStatisticsWeek;
import com.lifeix.cbs.api.dto.user.CbsUser;
import com.lifeix.cbs.api.impl.util.AccountTransformUtil;
import com.lifeix.cbs.api.service.spark.CbsRankDubbo;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.framework.memcache.MemcacheService;
import com.lifeix.framework.redis.impl.RedisDataIdentify;
import com.lifeix.framework.redis.impl.RedisSortSetHandler;
import com.lifeix.framework.redis.impl.ResultSortData;
import com.lifeix.user.beans.CustomResponse;

@Service("cbsRankDubbo")
public class CbsRankServiceImpl implements CbsRankDubbo {

    private final static Logger LOG = LoggerFactory.getLogger(CbsRankServiceImpl.class);

    @Autowired
    private RedisSortSetHandler redisSortSetHandler;

    @Autowired
    private UserContestStatisticsDao userContestStatisticDao;

    @Autowired
    private UserContestStatisticsWeekDao userContestStatisticsWeekDao;

    @Autowired
    private CbsUserDao cbsUserDao;

    @Autowired
    private MemcacheService memcacheService;

    /**
     * 胜率总榜
     * 
     * @param nowPage
     * @param limit
     * @param userId
     * @return
     */
    @Override
    public UserStatisticsListResponse getWinningTop(Integer nowPage, int limit, Long userId) {
	return getRankTop(ContestMemcached.RANK_WIN_ALL, RankRedis.RANK_WINNING_ALL, nowPage, limit, userId);
    }

    /**
     * 胜率周榜
     * 
     * @param nowPage
     * @param limit
     * @param userId
     * @return
     */
    @Override
    public UserStatisticsListResponse getWinningWeek(Integer year, Integer week, Integer nowPage, int limit, Long userId) {

	// 默认当前周
	String cacheKey = null;
	if (year == null || week == null) {
	    Calendar cal = Calendar.getInstance();
	    cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) - 24);
	    cal.add(Calendar.DAY_OF_WEEK, -1);
	    year = cal.get(Calendar.YEAR);
	    week = cal.get(Calendar.WEEK_OF_YEAR);
	    cacheKey = ContestMemcached.RANK_WIN_WEEK;
	}

	return getRankWeek(cacheKey, RankRedis.RANK_WINNING_WEEK, year, week, nowPage, limit, userId);
    }

    /**
     * 回报率总榜
     * 
     * @param nowPage
     * @param limit
     * @param userId
     * @return
     */
    @Override
    public UserStatisticsListResponse getRoiTop(Integer nowPage, int limit, Long userId) {
	return getRankTop(ContestMemcached.RANK_ROI_ALL, RankRedis.RANK_ROI_ALL, nowPage, limit, userId);
    }

    /**
     * 回报率周榜
     * 
     * @param nowPage
     * @param limit
     * @param userId
     * @return
     */
    @Override
    public UserStatisticsListResponse getRoiWeek(Integer year, Integer week, Integer nowPage, int limit, Long userId) {

	// 默认当前周
	String cacheKey = null;
	if (year == null || week == null) {
	    Calendar cal = Calendar.getInstance();
	    cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) - 24);
	    cal.add(Calendar.DAY_OF_WEEK, -1);
	    year = cal.get(Calendar.YEAR);
	    week = cal.get(Calendar.WEEK_OF_YEAR);
	    cacheKey = ContestMemcached.RANK_ROI_WEEK;
	}

	return getRankWeek(cacheKey, RankRedis.RANK_ROI_WEEK, year, week, nowPage, limit, userId);
    }

    /**
     * 
     * 用户数据曲线图(按周)
     * 
     * @param userId
     * @param startTime
     * @param endTime
     * @param limit
     * @return
     * @throws L99IllegalParamsException
     */
    public UserGraphListResponse userBetGraph(Long userId, String startTime, String endTime, Integer limit)
	    throws L99IllegalParamsException {

	ParamemeterAssert.assertDataNotNull(userId, startTime);

	List<UserStatisticsResponse> ranks = new ArrayList<UserStatisticsResponse>();
	Date start = CbsTimeUtils.formatDateB(startTime);
	Date end = CbsTimeUtils.formatDateB(endTime);

	// start year and week
	Calendar cal = Calendar.getInstance();
	cal.setTime(start);
	cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) - 24);
	Integer startYear = cal.get(Calendar.YEAR);
	Integer startWeek = cal.get(Calendar.WEEK_OF_YEAR);
	if (startWeek == 1 && cal.get(Calendar.MONTH) == 11) { // 防止最后一个月的时间是第二年的第一周
	    startYear++;
	}

	Calendar calSta = Calendar.getInstance();
	cal.setTime(end);
	cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) - 24);
	Integer endYear = cal.get(Calendar.YEAR);
	Integer endWeek = cal.get(Calendar.WEEK_OF_YEAR);
	if (endWeek == 1 && cal.get(Calendar.MONTH) == 11) { // 防止最后一个月的时间是第二年的第一周
	    endYear++;
	}

	List<UserContestStatisticsWeek> weeks = userContestStatisticsWeekDao.findByUser(userId, startYear, startWeek,
	        endYear, endWeek, limit);

	Integer tmpD = null;
	Integer tmpY = null;
	int last = 0;
	boolean isDo = false;
	for (UserContestStatisticsWeek weekStatistics : weeks) {
	    Integer y = weekStatistics.getYear();
	    Integer w = weekStatistics.getWeek();
	    // 填充第一条数据前的日期数据
	    if (tmpD == null) {
		tmpD = w;
		tmpY = y;
		if (startYear < y || (startYear.intValue() == y.intValue() && startWeek < w)) {
		    while (hasWeek(startYear, startWeek, y, w)) {
			UserStatisticsResponse blankDay = fillBlankWeek(y, startWeek);
			blankDay.setLongbi(true);
			ranks.add(blankDay);
			startWeek++;
			if (startWeek >= 52) {
			    startYear++;
			    startWeek = 1;
			}
			last++;
			if (last >= limit) {
			    break;
			}
		    }
		}
	    } else {
		int j = w - tmpD;
		if (tmpY.intValue() != y.intValue() && !isDo) {
		    int maxDay = calSta.getActualMaximum(Calendar.DAY_OF_YEAR);
		    j = maxDay - tmpD + w + 1;
		    isDo = true;
		}

		if (j > 1) {
		    for (int t = 1; t < j; t++) {
			tmpD++;
			UserStatisticsResponse blankDay = fillBlankWeek(y, tmpD);
			blankDay.setLongbi(true);
			ranks.add(blankDay);
			last++;
			if (last >= limit) {
			    break;
			}
		    }
		}

	    }

	    UserStatisticsResponse usr = AccountTransformUtil.transformUserContestStatisticsWeek(weekStatistics, null, true);
	    usr.setLongbi(true);
	    String time = getWeekTimeFormat(y, w);
	    usr.setTime(time);
	    ranks.add(usr);

	    tmpD = w;
	    tmpY = y;
	    last++;

	    if (last >= limit) {
		break;
	    }
	}

	if (tmpY == null || tmpD == null) {
	    tmpY = startYear;
	    tmpD = startWeek;
	}

	// 剩余时间
	while (hasWeek(tmpY, tmpD, endYear, endWeek)) {
	    tmpD++;
	    UserStatisticsResponse blankDay = fillBlankWeek(tmpY, tmpD);
	    blankDay.setLongbi(true);
	    ranks.add(blankDay);
	    last++;
	    if (last >= limit) {
		break;
	    }
	}

	CbsUser user = cbsUserDao.selectById(userId);
	UserGraphListResponse resp = new UserGraphListResponse();
	resp.setRanks(ranks);
	resp.setUser(AccountTransformUtil.transformUser(user, true));
	return resp;
    }

    /**
     * 重置赢率和回报率总榜
     * 
     * @param type
     */
    @Override
    public CustomResponse resetTop(Integer type) {

	Long roi = 0L;
	Long winning = 0L;
	if (type == null || (type & 1) > 0) {
	    // 回报率榜
	    RedisDataIdentify iden = new RedisDataIdentify(RedisConstants.MODEL_USER, RankRedis.RANK_ROI_ALL);
	    // 先清空
	    redisSortSetHandler.del(iden);
	    Map<String, Double> map = userContestStatisticDao.findUserContestStatisticsRoiMap();
	    // 放入新的数据
	    if (map.size() > 0) {
		roi = redisSortSetHandler.zAdd(iden, map);// 存入redis
	    }
	}

	if (type == null || (type & 1) > 0) {
	    // 胜率榜
	    RedisDataIdentify iden = new RedisDataIdentify(RedisConstants.MODEL_USER, RankRedis.RANK_WINNING_ALL);
	    // 先清空
	    redisSortSetHandler.del(iden);
	    Map<String, Double> map = userContestStatisticDao.findUserContestStatisticsWinningMap();
	    if (map.size() > 0) {
		winning = redisSortSetHandler.zAdd(iden, map);// 存入redis
	    }
	}
	CustomResponse resp = new CustomResponse();
	resp.put("roi", roi);
	resp.put("winning", winning);
	return resp;
    }

    /**
     * 获取总数据
     * 
     * @param rankKey
     *            榜单类型
     * @param ident
     *            如果为周榜，传递每周标识
     * @param nowPage
     *            分页
     * @param limit
     *            返回数量
     * @param userId
     *            当前用户
     * @return
     */
    private UserStatisticsListResponse getRankTop(String cacheKey, String rankKey, Integer nowPage, int limit, Long userId) {
	if (nowPage == null) {
	    nowPage = 1;
	}
	int startId = (nowPage - 1) * limit;
	// 判断是否是首页缓存
	boolean cacheFlag = nowPage == 1;
	List<UserStatisticsResponse> ranks = null;
	if (cacheFlag) {// 首页缓存
	    ranks = memcacheService.get(cacheKey);
	}
	RedisDataIdentify iden = new RedisDataIdentify(RedisConstants.MODEL_USER, rankKey);
	if (ranks == null) {
	    List<ResultSortData> data = redisSortSetHandler.reverseRangeWithScores(iden, startId, limit + 1);
	    ranks = new ArrayList<UserStatisticsResponse>();
	    if (nowPage > 0) {
		// 判断是否有加载更多
		if (data.size() > limit) {
		    data = data.subList(0, limit);
		    nowPage = nowPage + 1;
		    if (nowPage > 5) { // 最多返回5页
			nowPage = -1;
		    }
		} else {
		    nowPage = -1;
		}

		List<Long> userIds = new ArrayList<Long>();
		for (ResultSortData d : data) {
		    userIds.add(Long.parseLong(d.getDataKey()));
		}

		if (userIds != null && !userIds.isEmpty()) {
		    // 查询统计
		    Map<Long, UserContestStatistics> userStatisticsMap = userContestStatisticDao
			    .findUserContestStatisticsByIds(userIds);

		    // 查询用户
		    Map<Long, CbsUser> userMap = cbsUserDao.findCsAccountMapByIds(userIds);

		    // 排名
		    int rank = startId + 1;
		    for (Long uId : userIds) {
			UserContestStatistics us = userStatisticsMap.get(uId);
			UserStatisticsResponse usr = AccountTransformUtil.transformUserStatistics(us, rank, 0.0D, false);
			if (usr == null) {
			    continue;
			}
			CbsUser user = userMap.get(uId);
			usr.setUser(AccountTransformUtil.transformUser(user, true));
			ranks.add(usr);
			rank++;
		    }
		} else {
		    nowPage = -1;
		}

		if (cacheFlag) {// 首页缓存
		    memcacheService.set(cacheKey, ranks, ContestMemcached.RANK_EXPIRED);
		}
	    }

	} else {
	    int size = ranks.size();
	    if (size == limit) {
		nowPage++;
	    } else {
		nowPage = -1;
	    }
	}
	UserStatisticsListResponse resp = new UserStatisticsListResponse();
	resp.setRanks(ranks);
	resp.setLimit(limit);
	resp.setStartId(Long.valueOf(nowPage));

	if (userId != null) {
	    CbsUser user = cbsUserDao.selectById(userId);
	    if (user != null) {
		resp.setUser_id(userId);
		resp.setHead(user.getUserPath());
		resp.setName(user.getUserName());
		Long userRank = redisSortSetHandler.zRevRank(iden, userId);
		if (userRank == null || userRank >= 1000) {
		    userRank = -1L;
		} else {
		    userRank = userRank + 1;
		}
		resp.setRank(userRank);
	    }
	}

	return resp;
    }

    /**
     * 周榜数据
     * 
     * @param rankKey
     * @param ident
     * @param nowPage
     * @param limit
     * @param userId
     * @return
     */
    private UserStatisticsListResponse getRankWeek(String cacheKey, String rankKey, int year, int week, Integer nowPage,
	    int limit, Long userId) {
	if (nowPage == null) {
	    nowPage = 1;
	}

	// 判断是否是首页缓存
	boolean cacheFlag = (cacheKey != null && nowPage == 1);

	List<UserStatisticsResponse> ranks = null;
	if (cacheFlag) {// 首页缓存
	    ranks = memcacheService.get(cacheKey);
	}

	RedisDataIdentify iden = new RedisDataIdentify(RedisConstants.MODEL_USER, rankKey);
	iden.setIdentifyId(String.format("%d:%d", year, week));

	if (ranks == null) {
	    ranks = new ArrayList<UserStatisticsResponse>();
	    if (nowPage > 0) {
		int startId = (nowPage - 1) * limit;

		List<ResultSortData> data = redisSortSetHandler.reverseRangeWithScores(iden, startId, limit + 1);
		// 判断是否有加载更多
		if (data.size() > limit) {
		    data = data.subList(0, limit);
		    nowPage = nowPage + 1;
		    if (nowPage > 5) { // 最多返回5页
			nowPage = -1;
		    }
		} else {
		    nowPage = -1;
		}

		List<Long> userIds = new ArrayList<Long>();
		for (ResultSortData d : data) {
		    userIds.add(Long.parseLong(d.getDataKey()));
		}

		if (userIds != null && !userIds.isEmpty()) {
		    // 查询统计
		    Map<Long, UserContestStatisticsWeek> userStatisticsMap = userContestStatisticsWeekDao.findByIds(userIds,
			    year, week);

		    // 查询用户
		    Map<Long, CbsUser> userMap = cbsUserDao.findCsAccountMapByIds(userIds);

		    // 排名
		    int rank = startId + 1;
		    for (Long uId : userIds) {
			UserContestStatisticsWeek us = userStatisticsMap.get(uId);
			UserStatisticsResponse usr = AccountTransformUtil
			        .transformUserContestStatisticsWeek(us, rank, false);
			if (usr == null) {
			    continue;
			}
			CbsUser user = userMap.get(uId);
			usr.setUser(AccountTransformUtil.transformUser(user, true));
			ranks.add(usr);
			rank++;
		    }
		} else {
		    nowPage = -1;
		}

		if (cacheFlag) {// 首页缓存
		    memcacheService.set(cacheKey, ranks, ContestMemcached.RANK_EXPIRED);
		}

	    }
	} else {
	    int size = ranks.size();
	    if (size == limit) {
		nowPage++;
	    } else {
		nowPage = -1;
	    }
	}

	UserStatisticsListResponse resp = new UserStatisticsListResponse();
	resp.setRanks(ranks);
	resp.setLimit(limit);
	resp.setStartId(Long.valueOf(nowPage));

	if (userId != null) {
	    CbsUser user = cbsUserDao.selectById(userId);
	    if (user != null) {
		resp.setUser_id(userId);
		resp.setHead(user.getUserPath());
		resp.setName(user.getUserName());
		Long userRank = redisSortSetHandler.zRevRank(iden, userId);
		if (userRank == null || userRank >= 1000) {
		    userRank = -1L;
		} else {
		    userRank = userRank + 1;
		}
		resp.setRank(userRank);
	    }
	}

	return resp;
    }

    private String getWeekTimeFormat(Integer y, Integer w) {
	if (y == null || w == null) {
	    return null;
	}
	Calendar cal = Calendar.getInstance();
	cal.set(Calendar.YEAR, y);
	cal.set(Calendar.WEEK_OF_YEAR, w);
	Date date = cal.getTime();
	return CbsTimeUtils.getTimeForDate(date);
    }

    private boolean hasWeek(int startYear, int startWeek, int y, int w) {
	return (startYear != y) || (startWeek != w);
    }

    /**
     * 填充空白周
     * 
     * @param y
     * @param w
     * @return
     */
    private static UserStatisticsResponse fillBlankWeek(Integer y, Integer w) {
	UserStatisticsResponse weekStatistics = new UserStatisticsResponse();
	Calendar cal = Calendar.getInstance();
	cal.set(Calendar.YEAR, y);
	cal.set(Calendar.WEEK_OF_YEAR, w);
	Date date = cal.getTime();
	String time = CbsTimeUtils.getTimeForDate(date);
	weekStatistics.setTime(time);
	weekStatistics.setBet_count(0);
	weekStatistics.setWinning(0.0);
	weekStatistics.setWin_gold(0.0);
	weekStatistics.setFinal_gold(0.0);
	return weekStatistics;
    }
}
