package com.lifeix.cbs.api.impl.spark;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import lifeix.framwork.util.JsonUtils;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.achieve.bean.bo.AchieveBOBase;
import com.lifeix.cbs.achieve.bean.bo.StatisticAchieveBO;
import com.lifeix.cbs.achieve.common.constant.BehaviorConstants.BehaviorType;
import com.lifeix.cbs.achieve.service.AchieveService;
import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.lock.RedisLockHelper;
import com.lifeix.cbs.api.common.util.BetConstants.BetResultStatus;
import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.api.common.util.CommerceMath;
import com.lifeix.cbs.api.common.util.ContestConstants.MeadalTaskType;
import com.lifeix.cbs.api.common.util.LockConstants;
import com.lifeix.cbs.api.common.util.RedisConstants;
import com.lifeix.cbs.api.common.util.RedisConstants.RankRedis;
import com.lifeix.cbs.api.dao.rank.MedalTaskDao;
import com.lifeix.cbs.api.dao.rank.UserContestStatisticsDao;
import com.lifeix.cbs.api.dao.rank.UserContestStatisticsWeekDao;
import com.lifeix.cbs.api.dao.user.CbsUserDao;
import com.lifeix.cbs.api.dto.rank.MedalTask;
import com.lifeix.cbs.api.dto.rank.UserContestStatistics;
import com.lifeix.cbs.api.dto.rank.UserContestStatisticsWeek;
import com.lifeix.cbs.api.service.relationship.RelationshipService;
import com.lifeix.cbs.api.service.spark.UserContestTaskDubbo;
import com.lifeix.cbs.contest.bean.settle.UserSettleLogListResponse;
import com.lifeix.cbs.contest.bean.settle.UserSettleLogResponse;
import com.lifeix.cbs.contest.service.cirle.FriendCircleService;
import com.lifeix.cbs.contest.service.settle.SettleLogService;
import com.lifeix.framework.redis.impl.RedisDataIdentify;
import com.lifeix.framework.redis.impl.RedisSortSetHandler;
import com.lifeix.user.beans.CustomResponse;

/**
 * 统计用户下注数据 并生成榜单数据任务
 * 
 * @author lifeix
 * 
 */
@Service("userContestTaskDubbo")
public class UserContestTaskDubboImpl implements UserContestTaskDubbo {

    /**
     * log object
     */
    protected static final Logger LOG = LoggerFactory.getLogger(ImplSupport.class);
    /**
     * 总榜获取日志数据的条数
     */
    private static final int SETTLE_LOG_LIMIT = 120;

    /**
     * 总榜需要30场才能上榜
     */
    private static final int RANK_BET_LIMIT = 30;

    /**
     * 总榜需要5场才能上榜
     */
    private static final int RANK_WEEK_LIMIT = 5;

    /**
     * 97%置信水平
     */
    private static final double Z = 2.144411; // 97% confidence

    @Autowired
    MedalTaskDao medalTaskDao;

    @Autowired
    SettleLogService settleLogService;

    @Autowired
    private UserContestStatisticsDao userContestStatisticsDao;

    @Autowired
    UserContestStatisticsWeekDao userContestStatisticsWeekDao;

    @Autowired
    FriendCircleService friendCircleService;

    @Autowired
    RedisSortSetHandler redisSortSetHandler;

    @Autowired
    RelationshipService relationshipService;

    @Autowired
    CbsUserDao cbsUserDao;

    @Autowired
    private RedisLockHelper redisLock;

    @Autowired
    protected AchieveService achieveService;

    /**
     * 更新猜友圈
     */
    @Override
    public CustomResponse completeFriendCircle() {

	CustomResponse ret = new CustomResponse();
	// 添加一个redis锁 防止并发
	if (redisLock.getRedisLock(LockConstants.KEY_CIRCLE, LockConstants.IDENTIFY_STATISTICS) != null) {
	    LOG.info("completeFriendCircle running!!!");
	    ret.put("error", "task running");
	    return ret;
	}
	redisLock.setRedisLock(LockConstants.KEY_CIRCLE, LockConstants.IDENTIFY_STATISTICS);
	int total = 0;
	int ok = 0;
	int no = 0;
	try {
	    // 先查询已经结算的最新settlelogId
	    MedalTask task = medalTaskDao.findById(MeadalTaskType.USER_CONTEST_CIRCLE);
	    if (task == null) {
		LOG.error("Can't find task " + MeadalTaskType.USER_CONTEST_CIRCLE + " in db.");
		ret.put("error", "no medal id");
		return ret;
	    }
	    Long lastLogId = task.getLastId();
	    LOG.info("completeFriendCircle find lastId = " + lastLogId);

	    UserSettleLogListResponse rep = null;

	    do {
		// 查询120条settle日志进行分析
		rep = settleLogService.findUserSettleListById(lastLogId, null, SETTLE_LOG_LIMIT);
		if (rep != null && rep.getLogs().size() > 0) {
		    List<UserSettleLogResponse> userSettles = rep.getLogs();
		    for (UserSettleLogResponse userSettle : userSettles) {
			total++;
			lastLogId = userSettle.getId();
			// 更新对应的竞猜内容中的下单结果及比分
			boolean flag = updateContent(userSettle);

			// 记录任务执行id
			if (flag) {
			    ok++;
			} else {
			    no++;
			}

			medalTaskDao.update(new MedalTask(MeadalTaskType.USER_CONTEST_CIRCLE, lastLogId, 1, null));
		    }
		}

	    } while (rep != null && rep.getLogs().size() > 0);

	} catch (Exception e) {
	    LOG.error("completeFriendCircle", e);
	} finally {
	    redisLock.delRedisLock(LockConstants.KEY_CIRCLE, LockConstants.IDENTIFY_STATISTICS);
	}
	ret.put("total", total);
	ret.put("ok", ok);
	ret.put("no", no);
	return ret;

    }

    /**
     * 用户总榜的竞猜数据统计 生成总榜
     */
    @Override
    public CustomResponse processUserContestStatisticsTask() {

	CustomResponse ret = new CustomResponse();
	// 添加一个redis锁 防止并发
	if (redisLock.getRedisLock(LockConstants.KEY_USER, LockConstants.IDENTIFY_STATISTICS) != null) {
	    LOG.info("UserContestStatisticsTask running!!!");
	    ret.put("error", "task running");
	    return ret;
	}
	redisLock.setRedisLock(LockConstants.KEY_USER, LockConstants.IDENTIFY_STATISTICS);
	int total = 0;
	int ok = 0;
	int no = 0;
	try {
	    // 先查询已经结算的最新settlelogId
	    MedalTask task = medalTaskDao.findById(MeadalTaskType.USER_CONTEST_STATISTICS);
	    if (task == null) {
		LOG.error("Can't find task " + MeadalTaskType.USER_CONTEST_STATISTICS + " in db.");
		ret.put("error", "no medal id");
		return ret;
	    }
	    Long lastLogId = task.getLastId();
	    LOG.info("UserContestStatisticsTask find lastId = " + lastLogId);

	    UserSettleLogListResponse rep = null;

	    // 回报率总榜
	    RedisDataIdentify roiIden = new RedisDataIdentify(RedisConstants.MODEL_USER, RankRedis.RANK_ROI_ALL);
	    // 胜率总榜
	    RedisDataIdentify winIden = new RedisDataIdentify(RedisConstants.MODEL_USER, RankRedis.RANK_WINNING_ALL);

	    do {
		// 查询120条settle日志进行分析
		rep = settleLogService.findUserSettleListById(lastLogId, null, SETTLE_LOG_LIMIT);
		if (rep != null && rep.getLogs().size() > 0) {

		    List<UserSettleLogResponse> userSettles = rep.getLogs();

		    // 成就数据集合
		    List<AchieveBOBase> dataList = new ArrayList<AchieveBOBase>(userSettles.size());
		    for (UserSettleLogResponse userSettle : userSettles) {
			total++;
			lastLogId = userSettle.getId();
			Long userId = userSettle.getUser_id();
			// 查询用户历史统计数据
			UserContestStatistics statistics = userContestStatisticsDao
			        .getUserContestStatisticsByNoCache(userId);
			boolean insert = false;
			if (statistics == null) {
			    statistics = new UserContestStatistics();
			    statistics.setUserId(userSettle.getUser_id());
			    insert = true;
			}
			// 下单总数+1
			statistics.setBetCount(statistics.getBetCount() + 1);
			// 下单总额
			statistics.setBetGold(CommerceMath.add(statistics.getBetGold(), userSettle.getBet()));
			int contestSettleStatus = userSettle.getStatus();
			if (contestSettleStatus == BetResultStatus.WIN) {
			    double odds = CommerceMath.add(
				    CommerceMath.mul(statistics.getWinOdds(), statistics.getWinCount()),
				    userSettle.getOdds());
			    // 赢次数+1
			    statistics.setWinCount(statistics.getWinCount() + 1);
			    // 连赢+1
			    int conWin = statistics.getConWinCount() + 1;
			    statistics.setConWinCount(conWin);
			    // 最高连赢记录
			    if (conWin > statistics.getMaxConWinCount()) {
				statistics.setMaxConWinCount(conWin);
			    }
			    // 连输清零
			    statistics.setConLossCount(0);
			    // 赢的平均赔率
			    statistics.setWinOdds(CommerceMath.div(odds, statistics.getWinCount()));
			    // 赢的总金额 +(返还给用户的钱 - 用户下单的钱)
			    statistics.setWinGold(CommerceMath.add(statistics.getWinGold(),
				    CommerceMath.sub(userSettle.getBack(), userSettle.getBet())));

			} else if (contestSettleStatus == BetResultStatus.DRAW) {
			    // 走次数+1
			    statistics.setDrawCount(statistics.getDrawCount() + 1);
			    // 走盘不影响连赢连输
			} else if (contestSettleStatus == BetResultStatus.LOSS) {
			    double odds = CommerceMath.add(
				    CommerceMath.mul(statistics.getLossOdds(), statistics.getLossCount()),
				    userSettle.getOdds());
			    // 输次数+1
			    statistics.setLossCount(statistics.getLossCount() + 1);
			    // 连胜清零
			    statistics.setConWinCount(0);
			    // 连输+1
			    int conLoss = statistics.getConLossCount() + 1;
			    statistics.setConLossCount(conLoss);
			    // 最高连输记录
			    if (conLoss > statistics.getMaxConLossCount()) {
				statistics.setMaxConLossCount(conLoss);
			    }
			    // 输的平均赔率
			    statistics.setLossOdds(CommerceMath.div(odds, statistics.getLossCount()));
			    // 增加输的金额 +(用户下单的钱 - 返还给用户的钱)
			    statistics.setLossGold(CommerceMath.add(statistics.getLossGold(),
				    CommerceMath.sub(userSettle.getBet(), userSettle.getBack())));
			}
			// 胜率
			// 计算公式： 赢次数/(总下单次数 - 走盘次数)
			int c = statistics.getBetCount() - statistics.getDrawCount();
			if (c == 0) {
			    statistics.setWinning(0.0);
			} else {
			    statistics.setWinning((double) statistics.getWinCount() / c);
			}
			// 投资回报率
			// 计算公式：(净赚 - 输)/总投入
			statistics
			        .setRoi(CommerceMath.div(
			                CommerceMath.sub(statistics.getWinGold(), statistics.getLossGold()),
			                statistics.getBetGold()));
			// 计算排名得分，总排名至少需要50场
			statistics.setScore(calculateRoiV2(50, statistics.getRoi(), statistics.getBetCount()));
			// 当前用户的排名，由于这个用户的积分变化导致其他用户的排名会有波动，此处只是更新了这个用户排名，并没有更新其他用户的排名

			// 增加已下注结算统计
			JSONObject contestCount = null;
			if (StringUtils.isEmpty(statistics.getContestCount())) {
			    contestCount = new JSONObject();
			    contestCount.put("0", 0);
			    contestCount.put("1", 0);
			    contestCount.put("10", 0);
			} else {
			    contestCount = new JSONObject(statistics.getContestCount());
			}

			// 取上次结算统计下注值
			int type = userSettle.getContest_type();
			String contestType = String.valueOf(type);
			int nums = contestCount.optInt(contestType, 0);

			contestCount.put(contestType, ++nums);
			statistics.setContestCount(contestCount.toString());

			// 写入数据
			boolean flag = false;
			if (insert) {
			    flag = userContestStatisticsDao.insert(statistics);
			} else {
			    Integer lastLockerId = statistics.getLockerId();
			    // 更改版本
			    statistics.setLockerId(getNextLockerId(lastLockerId));
			    statistics.setLastLockerId(lastLockerId);

			    flag = userContestStatisticsDao.updateWithLockerId(statistics);
			}
			if (!flag) {
			    // 总表更新失败，直接退出，等待下次重入
			    LOG.error("UserContestStatisticsTask update fail with UserSettleLog " + userSettle.getId());
			    no++;
			    break;
			} else {
			    ok++;
			}
			// 添加榜单排名
			if (statistics.getBetCount() >= RANK_BET_LIMIT) {
			    redisSortSetHandler.zAdd(roiIden, userId.toString(), statistics.getRoi());
			    redisSortSetHandler.zAdd(winIden, userId.toString(), statistics.getWinning());
			}

			// 记录任务执行id
			medalTaskDao.update(new MedalTask(MeadalTaskType.USER_CONTEST_STATISTICS, lastLogId, 1, null));

			// 统计成就相关
			if (type == 0 || type == 1) {
			    try {
				StatisticAchieveBO bo = new StatisticAchieveBO();
				bo.setBehavior_type(BehaviorType.STATISTIC_TYPE);
				bo.setUser_id(userSettle.getUser_id());
				bo.setBet_count(statistics.getBetCount());
				bo.setMax_con_win_count(statistics.getMaxConWinCount());
				bo.setMax_con_loss_count(statistics.getMaxConLossCount());
				bo.setRoi(statistics.getRoi());
				dataList.add(bo);
			    } catch (Throwable t) {
				LOG.error("userId=" + userSettle.getUser_id()
				        + ", achieve statistics data analysis failed: " + t.getMessage(), t);
			    }
			}
		    }
		    if (dataList.size() > 0) {
			try {
			    achieveService.addAchieveData(JsonUtils.toJsonString(dataList), true);
			} catch (Throwable t) {
			    LOG.error("add achieve statistics data failed: " + t.getMessage(), t);
			}
		    }
		}

	    } while (rep != null && rep.getLogs().size() > 0);

	} catch (Exception e) {
	    LOG.error("processUserContestStatisticsTask", e);
	} finally {
	    redisLock.delRedisLock(LockConstants.KEY_USER, LockConstants.IDENTIFY_STATISTICS);
	}
	ret.put("total", total);
	ret.put("ok", ok);
	ret.put("no", no);
	return ret;

    }

    /**
     * 用户周榜的竞猜数据统计 生成周榜
     */
    @Override
    public CustomResponse processUserContestStatisticsWeek() {
	return processUserContestStatisticsWeek(true);
    }

    /**
     * 用户周榜的竞猜数据统计 生成周榜 (cacheFlag 强制清除缓存)
     */
    @Override
    public CustomResponse processUserContestStatisticsWeek(boolean cacheFlag) {

	CustomResponse ret = new CustomResponse();

	// 添加一个redis锁 防止并发
	if (redisLock.getRedisLock(LockConstants.KEY_WEEK, LockConstants.IDENTIFY_STATISTICS) != null) {
	    LOG.info("UserContestStatisticsWeek running!!!");
	    ret.put("error", "task running");
	    return ret;
	}
	redisLock.setRedisLock(LockConstants.KEY_WEEK, LockConstants.IDENTIFY_STATISTICS);

	int total = 0;
	int ok = 0;
	int no = 0;
	try {

	    // 先查询已经结算的最新settlelogId
	    MedalTask task = medalTaskDao.findById(MeadalTaskType.USER_CONTEST_STATISTICS_WEEK);
	    if (task == null) {
		LOG.error("Can't find task " + MeadalTaskType.USER_CONTEST_STATISTICS_WEEK + " in db.");
		ret.put("error", "no medal id");
		return ret;
	    }
	    Long lastLogId = task.getLastId();
	    LOG.info("UserContestStatisticsWeek find lastId = " + lastLogId);

	    // 回报率周榜
	    RedisDataIdentify roiIden = new RedisDataIdentify(RedisConstants.MODEL_USER, RankRedis.RANK_ROI_WEEK);
	    // 胜率周榜
	    RedisDataIdentify winIden = new RedisDataIdentify(RedisConstants.MODEL_USER, RankRedis.RANK_WINNING_WEEK);

	    UserSettleLogListResponse rep = null;
	    do {
		// 查询120条settle日志进行分析
		rep = settleLogService.findUserSettleListById(lastLogId, null, SETTLE_LOG_LIMIT);
		if (rep != null && rep.getLogs().size() > 0) {
		    List<UserSettleLogResponse> userSettles = rep.getLogs();

		    for (UserSettleLogResponse userSettle : userSettles) {
			total++;
			lastLogId = userSettle.getId();
			Long userId = userSettle.getUser_id();
			// 时间已比赛开始时间为准 获取对应的year和week
			Date contestTime = CbsTimeUtils.getDateByUtcFormattedDate(userSettle.getContest_time());

			// 获取当前周的year和week，周时间为周一0点到周日23：59分
			Calendar cal = Calendar.getInstance();
			cal.setTime(contestTime);
			cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) - 24);
			Integer year = cal.get(Calendar.YEAR);
			Integer week = cal.get(Calendar.WEEK_OF_YEAR);
			if (week == 1 && cal.get(Calendar.MONTH) == 11) { // 防止最后一个月的时间是第二年的第一周
			    year++;
			}

			int contestSettleStatus = userSettle.getStatus();
			UserContestStatisticsWeek statistics = userContestStatisticsWeekDao.findById(userId, year, week,
			        cacheFlag);
			boolean insert = false;
			if (statistics == null) {
			    statistics = new UserContestStatisticsWeek();
			    statistics.setYear(year);
			    statistics.setWeek(week);
			    statistics.setUserId(userSettle.getUser_id());
			    insert = true;
			}
			// 总下单次数+1
			statistics.setBetCount(statistics.getBetCount() + 1);
			// 下单总额
			statistics.setBetGold(CommerceMath.add(statistics.getBetGold(), userSettle.getBet()));

			if (contestSettleStatus == BetResultStatus.WIN) {
			    double odds = CommerceMath.add(
				    CommerceMath.mul(statistics.getWinOdds(), statistics.getWinCount()),
				    userSettle.getOdds());
			    // 赢次数+1
			    statistics.setWinCount(statistics.getWinCount() + 1);
			    // 连赢+1
			    statistics.setConWinCount(statistics.getConWinCount() + 1);
			    // 连输清零
			    statistics.setConLossCount(0);
			    // 赢的平均赔率
			    statistics.setWinOdds(CommerceMath.div(odds, statistics.getWinCount()));
			    // 赢的总金额 +(返还给用户的钱 - 用户下单的前)
			    statistics.setWinGold(CommerceMath.add(statistics.getWinGold(),
				    CommerceMath.sub(userSettle.getBack(), userSettle.getBet())));

			} else if (contestSettleStatus == BetResultStatus.DRAW) {
			    // 平（走）次数+1
			    statistics.setDrawCount(statistics.getDrawCount() + 1);
			    // 走盘不影响连赢连输
			} else if (contestSettleStatus == BetResultStatus.LOSS) {
			    double odds = CommerceMath.add(
				    CommerceMath.mul(statistics.getLossOdds(), statistics.getLossCount()),
				    userSettle.getOdds());
			    // 输次数+1
			    statistics.setLossCount(statistics.getLossCount() + 1);
			    // 连胜清零
			    statistics.setConWinCount(0);
			    // 连输+1
			    statistics.setConLossCount(statistics.getConLossCount() + 1);
			    // 输的平均赔率
			    statistics.setLossOdds(CommerceMath.div(odds, statistics.getLossCount()));
			    // 增加输的金额
			    statistics.setLossGold(CommerceMath.add(statistics.getLossGold(),
				    CommerceMath.sub(userSettle.getBet(), userSettle.getBack())));

			}

			statistics.setProfit(CommerceMath.sub(statistics.getWinGold(), statistics.getLossGold()));

			// 胜率
			// 计算公式： 赢次数/(总下单次数 - 走盘次数)
			int c = statistics.getBetCount() - statistics.getDrawCount();
			if (c == 0) {
			    statistics.setWinning(0.0);
			} else {
			    statistics.setWinning((double) statistics.getWinCount() / c);
			}
			// 投资回报率
			// 计算公式：(净赚 - 输)/总投入
			double roi = CommerceMath.div(CommerceMath.sub(statistics.getWinGold(), statistics.getLossGold()),
			        statistics.getBetGold());
			statistics.setRoi(roi);
			// 计算排名得分
			statistics.setScore(calculate(statistics.getWinCount(), statistics.getBetCount(), Z));
			statistics.setLongbi(true);
			boolean flag = false;
			if (insert) {
			    flag = userContestStatisticsWeekDao.insert(statistics);
			} else {
			    Integer lastLockerId = statistics.getLockerId();
			    // 更改版本
			    statistics.setLockerId(getNextLockerId(lastLockerId));
			    statistics.setLastLockerId(lastLockerId);
			    flag = userContestStatisticsWeekDao.updateWithLockerId(statistics);
			}
			if (!flag) {
			    // 周表处理失败
			    no++;
			    LOG.error("Update UserContestStatisticsPeroidTask week fail with UserSettleLog " + lastLogId);
			} else {
			    ok++;
			}

			// 添加榜单排名
			if (statistics.getBetCount() >= RANK_WEEK_LIMIT) {
			    String weekKey = String.format("%d:%d", year, week);
			    roiIden.setIdentifyId(weekKey);
			    redisSortSetHandler.zAdd(roiIden, userId.toString(), statistics.getRoi());
			    winIden.setIdentifyId(weekKey);
			    redisSortSetHandler.zAdd(winIden, userId.toString(), statistics.getWinning());
			}

			// 更新上次任务id
			medalTaskDao.update(new MedalTask(MeadalTaskType.USER_CONTEST_STATISTICS_WEEK, lastLogId, 1, null));
		    }
		}
	    } while (rep != null && rep.getLogs().size() > 0);
	} catch (Exception e) {
	    LOG.error("processUserContestStatisticsPeriodWeekTask EXCEPTION", e);
	} finally {
	    if (total > 0) {
		LOG.info(String.format("processUserContestStatisticsWeek [total=%d][ok=%d][no=%d]", total, ok, no));
	    }
	    redisLock.delRedisLock(LockConstants.KEY_WEEK, LockConstants.IDENTIFY_STATISTICS);
	}
	ret.put("total", total);
	ret.put("ok", ok);
	ret.put("no", no);
	return ret;
    }

    @Override
    public CustomResponse clearWeekRankRedis(int type) {
	RedisDataIdentify iden = new RedisDataIdentify(RedisConstants.MODEL_USER, RankRedis.RANK_ROI_WEEK);
	if (type == 0) { // 回报率周榜
	    iden = new RedisDataIdentify(RedisConstants.MODEL_USER, RankRedis.RANK_ROI_WEEK);
	} else {// 胜率周榜
	    iden = new RedisDataIdentify(RedisConstants.MODEL_USER, RankRedis.RANK_WINNING_WEEK);
	}
	iden.setIdentifyId("*");
	// 所有匹配的key
	Set<String> set = redisSortSetHandler.keys(iden);
	// 登陆记录删除
	int totle = 0;
	for (String s : set) {
	    totle++;
	    redisSortSetHandler.del(s);
	}
	CustomResponse ret = new CustomResponse();
	ret.put("type", type);
	ret.put("totle", totle);
	return ret;
    }

    /**
     * 更新猜友圈数据
     * 
     * @param userSettle
     * @return
     */
    private boolean updateContent(UserSettleLogResponse userSettle) {

	boolean ret = false;
	// 更新我发表的竞猜中比赛结果
	if (userSettle.getContent_id() != null && userSettle.getContent_id().longValue() > 0L) {
	    JSONObject resultObj = new JSONObject();
	    try {
		resultObj.put("back", userSettle.getBack());
		resultObj.put("status", userSettle.getStatus());
		friendCircleService.editContentContest(userSettle.getContent_id(), resultObj.toString(), true);
		ret = true;
	    } catch (Exception e) {
		LOG.error(e.getMessage(), e);
	    }
	} else {
	    LOG.error("Can't get contentId from roi_user_settle_log" + userSettle.getId());
	    // 没有对应的contentId，无法更新，直接返回true容错
	    ret = true;
	}
	return ret;
    }

    /**
     * 总榜根据投资回报率和场次，计算用户积分
     * 
     * @param target
     *            最大50
     * @param roi
     * @param total
     * @return
     */
    private static int calculateRoiV2(int target, double roi, int total) {
	if (Double.compare(roi, 0) > 0) {
	    // roi>0时利用x/(x+1)
	    // x/(x+1)的取值范围(0,1)
	    roi = CommerceMath.div(roi, CommerceMath.add(roi, 1.0));
	} else {
	    // roi<0时利用x/(1-x)
	    // x/(1-x)的取值范围[-0.5,0]
	    roi = CommerceMath.div(roi, CommerceMath.sub(1.0, roi));
	}
	// 将roi的值控制在[0, 1.5), 乘2 最终控制在[0，3.0)
	roi = CommerceMath.mul(CommerceMath.add(roi, 0.5), 2);

	int res = 0;
	if (total < target) {
	    // [0, 3000) + [0, target] = [0, 3050)
	    res = (int) (CommerceMath.mul(roi, 1000 * (total / 10 + 1))) + getCountScore(total);
	} else {
	    // [0, 30000) + [0, 1000] = [0, 31000)
	    res = (int) (CommerceMath.mul(roi, 10000)) + getCountScore(total);
	}
	// 最终结果控制在[0, 31000)
	return res;
    }

    /**
     * 周榜胜率相关排名分数计算
     * 参考：http://www.evanmiller.org/how-not-to-sort-by-average-rating.html
     * 内部wiki：http://192.168.1.175/default.asp?W1169
     * 
     * @param win
     *            赢的场次
     * @param total
     *            参与竞猜的总场次
     * @param z
     *            某一置信水平下的值，固定值
     * @return
     */
    private static double calculate(int win, int total, double z) {
	if (total == 0) {
	    return 0;
	}
	double p = 1.0 * win / total;
	double d = z * z / total;
	double s = (p * (1 - p) + z * z / (4 * total)) / total;
	return (p + d / 2 - z * Math.sqrt(s)) / (1 + d);

    }

    /**
     * 根据竞猜场次计算附加分
     * 
     * @param total
     * @return
     */
    private static int getCountScore(int total) {
	// 900场以内按照场次增加
	if (total <= 900) {
	    return total;
	} else {
	    // 超过900场以后，增长速度变慢，增加的场次基本不起作用。由于是int型，组多增加32。
	    return 900 + (int) (Math.log(total - 900) / Math.log(2));
	}
    }

    /**
     * 控制版本在一定范围内变化
     * 
     * @param lockerId
     * @return
     */
    public static Integer getNextLockerId(Integer lockerId) {
	if (lockerId == null) {
	    return 0;
	}
	return (lockerId + 1) % 100;
    }

}
