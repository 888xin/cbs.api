package com.lifeix.cbs.api.impl.spark;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.bean.user.UserContestStatisticsListResponse;
import com.lifeix.cbs.api.bean.user.UserContestStatisticsResponse;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.common.util.RedisConstants;
import com.lifeix.cbs.api.common.util.RedisConstants.RankRedis;
import com.lifeix.cbs.api.dao.rank.UserContestStatisticsDao;
import com.lifeix.cbs.api.dto.rank.UserContestStatistics;
import com.lifeix.cbs.api.impl.util.AccountTransformUtil;
import com.lifeix.cbs.api.service.spark.UserContestStatisticsDubbo;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.framework.redis.impl.RedisDataIdentify;
import com.lifeix.framework.redis.impl.RedisSortSetHandler;

@Service("userContestStatisticsDubbo")
public class UserContestStatisticsDubboImpl extends ImplSupport implements UserContestStatisticsDubbo {

    @Autowired
    private UserContestStatisticsDao userContestStatisticsDao;

    @Autowired
    private RedisSortSetHandler redisSortSetHandler;

    @Override
    public UserContestStatisticsResponse getUserContestStatistics(Long userId) throws L99IllegalParamsException {

	ParamemeterAssert.assertDataNotNull(userId);
	UserContestStatistics statistics = userContestStatisticsDao.getUserContestStatistics(userId);

	if (statistics != null) {
	    // 获取排名
	    RedisDataIdentify iden = new RedisDataIdentify(RedisConstants.MODEL_USER, RankRedis.RANK_ROI_ALL);
	    Long zrank = redisSortSetHandler.zRevRank(iden, userId.toString());
	    statistics.setRank(zrank == null ? 0 : zrank.intValue() + 1);
	    if (statistics.getRank().intValue() == 0) {
		statistics.setRank(9999);
	    }
	}

	// 获取龙筹
	Double gold = 0.00D;

	return AccountTransformUtil.transformUserContestStatistics(statistics, gold);

    }

    @Override
    public UserContestStatisticsListResponse findMoreUserStatistics(String userIds) throws L99IllegalParamsException {

	ParamemeterAssert.assertDataNotNull(userIds);

	String[] idsArray = userIds.split(",");
	List<Long> ids = new ArrayList<Long>();
	for (String id : idsArray) {
	    if (StringUtils.isEmpty(id)) {
		continue;
	    }
	    ids.add(Long.valueOf(id));
	}
	if (ids.size() == 0) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}

	Map<Long, UserContestStatistics> statisticMap = userContestStatisticsDao.findUserContestStatisticsByIds(ids);

	UserContestStatisticsListResponse response = new UserContestStatisticsListResponse();
	List<UserContestStatisticsResponse> statistics = new ArrayList<UserContestStatisticsResponse>();
	for (Long userId : ids) {
	    UserContestStatistics statistic = statisticMap.get(userId);
	    if (statistic != null) {

		// 获取排名
		RedisDataIdentify iden = new RedisDataIdentify(RedisConstants.MODEL_USER, RankRedis.RANK_ROI_ALL);
		Long zrank = redisSortSetHandler.zRevRank(iden, userId.toString());
		statistic.setRank(zrank == null ? 0 : zrank.intValue() + 1);
		if (statistic.getRank().intValue() == 0) {
		    statistic.setRank(9999);
		}
		statistics.add(AccountTransformUtil.transformUserContestStatistics(statistic, 0.0D));
	    }
	}
	response.setStatistics(statistics);
	return response;
    }

}
