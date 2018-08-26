package com.lifeix.cbs.contest.impl.spark.settle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestType;
import com.lifeix.cbs.api.common.util.ContestConstants.SettleStatus;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.contest.bean.fb.ContestListResponse;
import com.lifeix.cbs.contest.bean.fb.ContestResponse;
import com.lifeix.cbs.contest.bean.settle.CbsSettleResponse;
import com.lifeix.cbs.contest.dao.bb.BbContestDao;
import com.lifeix.cbs.contest.dao.bb.BbTeamDao;
import com.lifeix.cbs.contest.dao.circle.FriendCircleDao;
import com.lifeix.cbs.contest.dao.fb.FbContestDao;
import com.lifeix.cbs.contest.dao.fb.FbTeamDao;
import com.lifeix.cbs.contest.dao.settle.CbsSettleDao;
import com.lifeix.cbs.contest.dto.bb.BbContest;
import com.lifeix.cbs.contest.dto.bb.BbTeam;
import com.lifeix.cbs.contest.dto.fb.FbContest;
import com.lifeix.cbs.contest.dto.fb.FbTeam;
import com.lifeix.cbs.contest.dto.settle.CbsSettle;
import com.lifeix.cbs.contest.impl.redis.RedisBetCountHandler;
import com.lifeix.cbs.contest.service.spark.settle.SettleTaskDubbo;
import com.lifeix.cbs.contest.util.transform.ContestTransformUtil;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * 赛事任务添加逻辑
 * 
 * @author lifeix-sz
 * 
 */
public class SettleTaskDubboImpl implements SettleTaskDubbo {

    private static Logger LOG = LoggerFactory.getLogger(SettleTaskDubboImpl.class);

    @Autowired
    private CbsSettleDao cbsSettleDao;

    @Autowired
    private FbContestDao fbContestDao;

    @Autowired
    private FbTeamDao fbTeamDao;

    @Autowired
    private BbContestDao bbContestDao;

    @Autowired
    private BbTeamDao bbTeamDao;

    @Autowired
    FriendCircleDao friendCircleDao;

    @Autowired
    private RedisBetCountHandler redisBetCountHandler;

    @Override
    public void addSettleTask(Integer type, Long contestId, boolean immediate) {

	if (type == null || contestId == null) {
	    return;
	}
	CbsSettle settle = cbsSettleDao.findByContest(type, contestId);
	Date createTime = new Date();
	boolean flag = false;
	if (settle == null) {
	    settle = new CbsSettle();
	    // 结算时间延迟半个小时
	    settle.setType(type);
	    settle.setContestId(contestId);
	    settle.setCreateTime(createTime);
	    if (immediate || type.intValue() == ContestType.YAYA) { // 押押立即结算
		settle.setSettleTime(createTime);
	    } else {
		settle.setSettleTime(delayTime(createTime, 30));
	    }

	    settle.setCloseFlag(SettleStatus.INIT);
	    flag = cbsSettleDao.insert(settle);
	} else {
	    if (!immediate && settle.getCloseFlag() == SettleStatus.INIT) {
		return;
	    }
	    settle.setCreateTime(createTime);
	    settle.setSettleTime(createTime);
	    settle.setCloseFlag(SettleStatus.INIT);
	    flag = cbsSettleDao.update(settle);
	}
	if (!flag) {
	    LOG.warn(String.format("RoiSettleService add settle %d-%d fail", type, contestId));
	}

    }

    @Override
    public ContestListResponse settlesContest(Long settleId, Integer type, int limit) throws L99IllegalParamsException {

	ParamemeterAssert.assertDataNotNull(type);

	limit = Math.max(0, limit);
	limit = Math.min(limit, 100);

	List<CbsSettle> settles = cbsSettleDao.findCbsSettles(settleId, type, limit);

	// 结算列表
	List<ContestResponse> contests = new ArrayList<ContestResponse>();
	List<Long> ids = new ArrayList<Long>();
	for (CbsSettle settle : settles) {
	    ids.add(settle.getContestId());
	}
	if (type.intValue() == ContestType.FOOTBALL) {

	    // 足球赛事列表
	    Map<Long, FbContest> contestMap = fbContestDao.findFbContestMapByIds(ids);

	    List<Long> teamIds = new ArrayList<Long>();
	    List<String> contestIds = new ArrayList<String>();
	    for (FbContest contest : contestMap.values()) {
		teamIds.add(contest.getHomeTeam());
		teamIds.add(contest.getAwayTeam());
		contestIds.add(contest.getContestId().toString());
	    }

	    Map<Long, FbTeam> teams = fbTeamDao.findFbTeamMapByIds(teamIds);
	    // Map<Long, Integer> countMap =
	    // redisBetCountHandler.findContestsCount(ContestType.BASKETBALL,
	    // contestIds);

	    for (CbsSettle settle : settles) {
		FbContest contest = contestMap.get(settle.getContestId());
		// 赛事下单数量
		// int betCount = countMap.get(contest.getContestId());
		int[] betCount = redisBetCountHandler.findContestBetRatio(ContestType.FOOTBALL, contest.getContestId());
		ContestResponse cr = ContestTransformUtil.transformFbContest(contest, teams.get(contest.getHomeTeam()),
		        teams.get(contest.getAwayTeam()), betCount, true);
		cr.setCreate_time(CbsTimeUtils.getUtcTimeForDate(settle.getSettleTime()));
		cr.setSettle_statu(settle.getCloseFlag());
		cr.setTarget_id(settle.getSettleId());
		contests.add(cr);
	    }
	} else if (type.intValue() == ContestType.BASKETBALL) {

	    // 篮球赛事列表
	    Map<Long, BbContest> contestMap = bbContestDao.findBbContestMapByIds(ids);

	    List<Long> teamIds = new ArrayList<Long>();
	    List<String> contestIds = new ArrayList<String>();
	    for (BbContest contest : contestMap.values()) {
		teamIds.add(contest.getHomeTeam());
		teamIds.add(contest.getAwayTeam());
		contestIds.add(contest.getContestId().toString());
	    }

	    Map<Long, BbTeam> teams = bbTeamDao.findBbTeamMapByIds(teamIds);
	    // Map<Long, Integer> betCountMap =
	    // redisBetCountHandler.findContestsCount(ContestType.BASKETBALL,
	    // contestIds);
	    for (CbsSettle settle : settles) {
		BbContest contest = contestMap.get(settle.getContestId());
		// int betCount = betCountMap.get(contest.getContestId());
		int[] betCount = redisBetCountHandler.findContestBetRatio(ContestType.BASKETBALL, contest.getContestId());
		ContestResponse cr = ContestTransformUtil.transformBbContest(contest, teams.get(contest.getHomeTeam()),
		        teams.get(contest.getAwayTeam()), betCount, true);
		cr.setCreate_time(CbsTimeUtils.getUtcTimeForDate(settle.getSettleTime()));
		cr.setSettle_statu(settle.getCloseFlag());
		cr.setTarget_id(settle.getSettleId());
		contests.add(cr);
	    }
	}

	ContestListResponse reponse = new ContestListResponse();
	reponse.setContests(contests);
	reponse.setNumber(contests.size());
	return reponse;
    }

    @Override
    public List<CbsSettleResponse> findSettleTask(Long settleId, int limit) {

	List<CbsSettleResponse> responses = new ArrayList<CbsSettleResponse>();
	List<CbsSettle> settles = cbsSettleDao.findSettleTask(settleId, limit);
	for (CbsSettle settle : settles) {
	    responses.add(ContestTransformUtil.transformCbsSettle(settle));
	}
	return responses;
    }

    @Override
    public void closeContest(Long settleId, int status) {
	cbsSettleDao.closeContest(settleId, status);
    }

    /**
     * 将一个时间延后n分钟
     * 
     * @param time
     * @param hour
     * @return
     */
    private static Date delayTime(Date time, int minute) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(time);
	calendar.add(Calendar.MINUTE, minute);
	return calendar.getTime();
    }

    public void setCbsSettleDao(CbsSettleDao cbsSettleDao) {
	this.cbsSettleDao = cbsSettleDao;
    }

    public void setFbContestDao(FbContestDao fbContestDao) {
	this.fbContestDao = fbContestDao;
    }

    public void setFbTeamDao(FbTeamDao fbTeamDao) {
	this.fbTeamDao = fbTeamDao;
    }

    public void setBbContestDao(BbContestDao bbContestDao) {
	this.bbContestDao = bbContestDao;
    }

    public void setBbTeamDao(BbTeamDao bbTeamDao) {
	this.bbTeamDao = bbTeamDao;
    }

}
