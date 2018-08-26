package com.lifeix.cbs.contest.impl.fb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lifeix.framwork.util.JsonUtils;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.exception.MsgCode.ContestMsg;
import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.api.common.util.ContestConstants;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestMemcached;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestStatu;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestType;
import com.lifeix.cbs.api.common.util.ContestConstants.TeamDiffer;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.contest.bean.contest.BetCountRatioResponse;
import com.lifeix.cbs.contest.bean.fb.ContestExtResponse;
import com.lifeix.cbs.contest.bean.fb.ContestListResponse;
import com.lifeix.cbs.contest.bean.fb.ContestResponse;
import com.lifeix.cbs.contest.bean.fb.FbFixedResponse;
import com.lifeix.cbs.contest.bean.fb.FbStatistcsEventResponse;
import com.lifeix.cbs.contest.bean.fb.TeamListResponse;
import com.lifeix.cbs.contest.bean.fb.TeamResponse;
import com.lifeix.cbs.contest.bean.fb.ext.EventResponse;
import com.lifeix.cbs.contest.bean.fb.ext.RecordResponse;
import com.lifeix.cbs.contest.dao.fb.FbContestDao;
import com.lifeix.cbs.contest.dao.fb.FbContestExtDao;
import com.lifeix.cbs.contest.dao.fb.FbContestRecordDao;
import com.lifeix.cbs.contest.dao.fb.FbStatisticsDao;
import com.lifeix.cbs.contest.dao.fb.FbTeamDao;
import com.lifeix.cbs.contest.dto.fb.FbContest;
import com.lifeix.cbs.contest.dto.fb.FbContestExt;
import com.lifeix.cbs.contest.dto.fb.FbContestRecord;
import com.lifeix.cbs.contest.dto.fb.FbStatistics;
import com.lifeix.cbs.contest.dto.fb.FbTeam;
import com.lifeix.cbs.contest.impl.ImplSupport;
import com.lifeix.cbs.contest.impl.redis.RedisBetCountHandler;
import com.lifeix.cbs.contest.service.fb.FbContestService;
import com.lifeix.cbs.contest.service.spark.cup.FbCupDubbo;
import com.lifeix.cbs.contest.service.spark.odds.FbOddsDubbo;
import com.lifeix.cbs.contest.util.ContestFilterUtil;
import com.lifeix.cbs.contest.util.transform.ContestTransformUtil;
import com.lifeix.common.utils.RegexUtil;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.framework.memcache.MemcacheService;
import com.lifeix.user.beans.CustomResponse;

@Service("fbContestService")
public class FbContestServiceImpl extends ImplSupport implements FbContestService {

    @Autowired
    private FbContestDao fbContestDao;

    @Autowired
    private FbTeamDao fbTeamDao;

    @Autowired
    private FbStatisticsDao fbStatisticsDao;

    @Autowired
    private FbContestExtDao fbContestExtDao;

    @Autowired
    private FbContestRecordDao fbContestRecordDao;

    @Autowired
    private RedisBetCountHandler redisBetCountHandler;

    @Autowired
    private FbCupDubbo fbCupDubbo;

    @Autowired
    private FbOddsDubbo fbOddsDubbo;

    @Autowired
    protected MemcacheService memcacheService;

    @Override
    public ContestResponse findFbContestsById(Long contestId, String client) throws L99IllegalDataException,
	    L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(contestId);

	FbContest contest = fbContestDao.selectById(contestId);
	if (contest == null) {
	    throw new L99IllegalDataException(ContestMsg.CODE_CONTEST_NOT_EXIST, ContestMsg.KEY_CONTEST_NOT_EXIST);
	}

	List<Long> teamIds = new ArrayList<Long>();
	teamIds.add(contest.getHomeTeam());
	teamIds.add(contest.getAwayTeam());

	Map<Long, FbTeam> teams = fbTeamDao.findFbTeamMapByIds(teamIds);
	// int betCount =
	// redisBetCountHandler.findContestCount(ContestType.FOOTBALL,
	// contestId);
	int[] betCount = redisBetCountHandler.findContestBetRatio(ContestType.FOOTBALL, contest.getContestId());
	ContestResponse response = ContestTransformUtil.transformFbContest(contest, teams.get(contest.getHomeTeam()),
	        teams.get(contest.getAwayTeam()), betCount, true);
	ContestFilterUtil.filterContestLogo(response, true, client);

	return response;
    }

    /**
     * 根据房间id获取赛事信息
     */
    @Override
    public ContestResponse findFbContestsByRoomId(Long roomId) throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(roomId);
	FbContest contest = fbContestDao.selectByRoomId(roomId);
	if (contest == null) {
	    return null;
	}
	List<Long> teamIds = new ArrayList<Long>();
	teamIds.add(contest.getHomeTeam());
	teamIds.add(contest.getAwayTeam());

	Map<Long, FbTeam> teams = fbTeamDao.findFbTeamMapByIds(teamIds);
	// int betCount =
	// redisBetCountHandler.findContestCount(ContestType.FOOTBALL,
	// contest.getContestId());
	int[] betCount = redisBetCountHandler.findContestBetRatio(ContestType.FOOTBALL, contest.getContestId());
	ContestResponse response = ContestTransformUtil.transformFbContest(contest, teams.get(contest.getHomeTeam()),
	        teams.get(contest.getAwayTeam()), betCount, true);
	ContestFilterUtil.filterContestLogo(response, true, null);

	return response;
    }

    @Override
    public void dropContest(Long contestId) throws L99IllegalParamsException, L99IllegalDataException {
	ParamemeterAssert.assertDataNotNull(contestId);

	FbContest contest = fbContestDao.selectById(contestId);
	if (contest == null) {
	    throw new L99IllegalDataException(ContestMsg.CODE_CONTEST_NOT_EXIST, ContestMsg.KEY_CONTEST_NOT_EXIST);
	}
	if (contest.getBetCount() > 0) {
	    throw new L99IllegalDataException(ContestMsg.CODE_CONTEST_CANT_DROP, ContestMsg.KEY_CONTEST_CANT_DROP);
	}
	boolean flag = fbContestDao.delete(contest);
	if (!flag) {
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}
    }

    @Override
    public ContestListResponse findAbnormalContests(Long startId, int limit) {
	limit = Math.min(limit, ContestConstants.SQL_RET_LIMIT);
	limit = Math.max(limit, 1);

	List<FbContest> contests = fbContestDao.findFbContestsByAbnormal(startId, limit);

	List<Long> teamIds = new ArrayList<Long>();
	List<String> contestIds = new ArrayList<String>();
	for (FbContest contest : contests) {
	    teamIds.add(contest.getHomeTeam());
	    teamIds.add(contest.getAwayTeam());
	    contestIds.add(contest.getContestId().toString());
	}

	Map<Long, FbTeam> teams = fbTeamDao.findFbTeamMapByIds(teamIds);
	// Map<Long, Integer> countMap =
	// redisBetCountHandler.findContestsCount(ContestType.FOOTBALL,
	// contestIds);
	List<ContestResponse> fb_contests = new ArrayList<ContestResponse>();
	for (FbContest contest : contests) {
	    // 赛事下单数量
	    // int betCount = countMap.get(contest.getContestId());
	    int[] betCount = redisBetCountHandler.findContestBetRatio(ContestType.FOOTBALL, contest.getContestId());
	    fb_contests.add(ContestTransformUtil.transformFbContest(contest, teams.get(contest.getHomeTeam()),
		    teams.get(contest.getAwayTeam()), betCount, false));
	}

	ContestListResponse reponse = new ContestListResponse();
	reponse.setContests(fb_contests);
	reponse.setNumber(contests.size());

	return reponse;
    }

    /**
     * 更改足球比赛信息
     */
    @Override
    public void updateContests(Long contestId, Integer homeScore, Integer awayScore, Integer status, Boolean isLock,
	    Integer level, String cupName, Integer homeCount, Integer awayCount) throws L99IllegalParamsException,
	    L99IllegalDataException {
	ParamemeterAssert.assertDataNotNull(contestId);

	FbContest contest = fbContestDao.selectById(contestId);

	// memcache更新标志位
	Boolean updateMemflag = false;

	if (contest != null) {
	    // 赛事等级变动
	    if (contest.getLevel() != level) {
		updateMemflag = true;
	    }

	    contest.setHomeScores(homeScore);
	    contest.setAwayScores(awayScore);
	    contest.setStatus(status);
	    contest.setLockFlag(isLock);
	    contest.setLevel(level);
	    contest.setCupName(cupName);
	    if (homeCount != null && awayCount != null) {
		BetCountRatioResponse betCountRatioResponse = new BetCountRatioResponse();
		betCountRatioResponse.setHome_count(homeCount);
		betCountRatioResponse.setAway_count(awayCount);
		String betRatio = JsonUtils.toJsonString(betCountRatioResponse);
		contest.setBetRatio(betRatio);
	    }
	    boolean flag = fbContestDao.updateInner(contest);
	    if (!flag) {
		throw new L99IllegalDataException(BasicMsg.CODE_OPERATE_FAIL, BasicMsg.KEY_OPERATE_FAIL);
	    }

	    if (updateMemflag) {
		// 30天赛事分类缓存
		memcacheService.delete(ContestMemcached.VALID_FB_CUP);

		// 检查赛事赔率
		fbOddsDubbo.checkWarnOdds(contestId);
	    }

	} else {
	    throw new L99IllegalDataException(ContestMsg.CODE_CONTEST_NOT_EXIST, ContestMsg.KEY_CONTEST_NOT_EXIST);
	}
    }

    /**
     * 封装返回赛况与技术统计
     */
    @Override
    public FbStatistcsEventResponse findStatisticsAndEventByContestId(Long contestId) throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(contestId);
	// 技术统计
	List<FbStatistics> list = fbStatisticsDao.selectByContestId(contestId);
	FbStatistcsEventResponse statistcsEventRespnse = new FbStatistcsEventResponse();
	for (FbStatistics statistics : list) {
	    if (statistics.getTeam() == TeamDiffer.HOME) {
		statistcsEventRespnse.setH_statistics(ContestTransformUtil.transformStatistics(statistics));
	    } else if (statistics.getTeam() == TeamDiffer.AWAY) {
		statistcsEventRespnse.setA_statistics(ContestTransformUtil.transformStatistics(statistics));
	    }
	}

	// 赛事

	FbContestExt contestExt = fbContestExtDao.selectById(contestId);
	ContestExtResponse contestRe = ContestTransformUtil.transformFbContestExt(contestExt);

	List<EventResponse> fbEvents = new ArrayList<EventResponse>();
	if (contestRe != null) {
	    fbEvents.addAll(contestRe.getEvents());
	}
	statistcsEventRespnse.setEvents(fbEvents);

	return statistcsEventRespnse;
    }

    /**
     * 足球赛事不变信息，交手记录、阵容
     */
    @Override
    public FbFixedResponse findFbFixedDataByContestId(Long contestId) throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(contestId);

	FbContest contest = fbContestDao.selectById(contestId);
	if (contest == null) {
	    return null;
	}

	// 赛事扩展信息
	FbContestExt contestExt = fbContestExtDao.selectById(contestId);
	ContestExtResponse contestExtResp = null;
	if (contestExt != null) {
	    contestExtResp = ContestTransformUtil.transformFbContestExt(contestExt);
	}

	// 赛事交手记录
	List<RecordResponse> recordResponse = new ArrayList<RecordResponse>();
	List<FbContestRecord> fbRecords = fbContestRecordDao.selectHeadToHeadRecord(contest.getHomeTeam(),
	        contest.getAwayTeam(), 10);
	for (FbContestRecord fbRecord : fbRecords) {
	    recordResponse.add(ContestTransformUtil.transformFbContestRecord(fbRecord));
	}

	// 球队的信息，包括阵容
	FbFixedResponse fbFixedResponse = new FbFixedResponse();
	if (contestExtResp != null) {
	    fbFixedResponse.setA_t_ext(contestExtResp.getA_t_ext());
	    fbFixedResponse.setH_t_ext(contestExtResp.getH_t_ext());
	}

	fbFixedResponse.setFbRecords(recordResponse);

	return fbFixedResponse;
    }

    /**
     * 大赢家赛事列表
     */
    @Override
    public ContestListResponse findFbContestsByTime(String time, boolean fullFlag, String client)
	    throws L99IllegalParamsException {

	ParamemeterAssert.assertDataNotNull(time);

	Date fTime = null;
	try {
	    fTime = new SimpleDateFormat("yyyy-MM-dd").parse(time);
	} catch (ParseException e) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}
	List<FbContest> contests = fbContestDao.findFbContestsByRangeTime(fTime, fullFlag);

	List<ContestResponse> contestReponses = buildContest(contests);

	ContestListResponse reponse = new ContestListResponse();
	reponse.setContests(contestReponses);
	reponse.setNumber(contests.size());
	ContestFilterUtil.filterContestListLogo(reponse, true, client);

	return reponse;
    }

    @Override
    public CustomResponse findFbScoreByContestIds(String contestIds) throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(contestIds);

	List<Long> ids = new ArrayList<Long>();
	for (String id : contestIds.split(",")) {
	    if (!RegexUtil.hasMatche(id, RegexUtil.DOMAIN_REG2)) {
		continue;
	    }
	    ids.add(Long.valueOf(id.trim()));
	}
	if (ids.size() == 0) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}

	Map<Long, FbContest> contestMap = fbContestDao.findFbContestMapByIds(ids);

	CustomResponse response = new CustomResponse();
	for (FbContest contest : contestMap.values()) {
	    String ret = String.format("%d_%d_%d", contest.getHomeScores(), contest.getAwayScores(), contest.getStatus());
	    response.put(contest.getContestId().toString(), ret);
	}

	return response;
    }

    /**
     * 获取赛事的最终比分(包含加时和点球)
     * 
     * @param contestIds
     * @return
     * @throws L99IllegalParamsException
     */
    @Override
    public CustomResponse findFbFinalScores(String contestIds) throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(contestIds);

	List<Long> ids = new ArrayList<Long>();
	for (String id : contestIds.split(",")) {
	    if (!RegexUtil.hasMatche(id, RegexUtil.DOMAIN_REG2)) {
		continue;
	    }
	    ids.add(Long.valueOf(id.trim()));
	}
	if (ids.size() == 0) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}

	Map<Long, FbContest> contestMap = fbContestDao.findFbContestMapByIds(ids);

	CustomResponse response = new CustomResponse();
	for (FbContest contest : contestMap.values()) {
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put("h", contest.getHomeScores());
	    params.put("a", contest.getAwayScores());
	    params.put("s", contest.getStatus());
	    try {
		if (StringUtils.isNotEmpty(contest.getFinalResult())) {
		    JSONObject finalObj = new JSONObject(contest.getFinalResult());
		    params.put("fh", finalObj.getInt("home_scores"));
		    params.put("fa", finalObj.getInt("away_scores"));
		    params.put("fs", finalObj.getInt("status"));
		    if (finalObj.has("score_type")) {
			params.put("ft", finalObj.getInt("score_type"));
		    } else {
			params.put("ft", ContestStatu.AWAITING_EXTRA_TIME);
		    }
		}
	    } catch (JSONException e) {
		LOG.error(e.getMessage(), e);
	    }
	    response.put(contest.getContestId().toString(), params);
	}

	return response;
    }

    @Override
    public TeamListResponse findFbTeams(String searchKey) throws L99IllegalParamsException {

	ParamemeterAssert.assertDataNotNull(searchKey);

	List<FbTeam> teams = null;
	if (RegexUtil.hasMatche(searchKey, "(\\d)+")) { // ID查询
	    teams = new ArrayList<FbTeam>();
	    FbTeam team = fbTeamDao.selectById(Long.valueOf(searchKey));
	    teams.add(team);
	} else {
	    teams = fbTeamDao.searchTeam(searchKey);
	}

	List<TeamResponse> fb_teams = new ArrayList<TeamResponse>();
	for (FbTeam team : teams) {
	    fb_teams.add(ContestTransformUtil.transformFbTeam(team, false));
	}

	TeamListResponse reponse = new TeamListResponse();
	reponse.setTeams(fb_teams);
	reponse.setNumber(fb_teams.size());

	return reponse;
    }

    @Override
    public void updateTeam(Long id, String name, String logo) throws L99IllegalParamsException, L99IllegalDataException {

	ParamemeterAssert.assertDataNotNull(id);

	ParamemeterAssert.assertDataOrNotNull(name, logo);

	FbTeam team = fbTeamDao.selectById(id);
	if (team == null) {
	    throw new L99IllegalDataException(ContestMsg.CODE_TEAM_NOT_EXIST, ContestMsg.KEY_TEAM_NOT_EXIST);
	}

	if (StringUtils.isNotEmpty(name)) {
	    team.setName(name);
	}

	if (StringUtils.isNotEmpty(logo)) {
	    team.setLogo(logo);
	}

	boolean flag = fbTeamDao.update(team);
	if (!flag) {
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}

    }

    /**
     * 转化赛事
     * 
     * @param contests
     * @return
     */
    private List<ContestResponse> buildContest(List<FbContest> contests) {

	List<ContestResponse> contestReponses = new ArrayList<ContestResponse>();
	if (contests == null || contests.size() == 0) {
	    return contestReponses;
	}

	List<Long> teamIds = new ArrayList<Long>();
	List<String> contestIds = new ArrayList<String>();
	for (FbContest contest : contests) {
	    teamIds.add(contest.getHomeTeam());
	    teamIds.add(contest.getAwayTeam());
	    contestIds.add(contest.getContestId().toString());
	}

	Map<Long, FbTeam> teams = fbTeamDao.findFbTeamMapByIds(teamIds);
	// Map<Long, Integer> countMap =
	// redisBetCountHandler.findContestsCount(ContestType.FOOTBALL,
	// contestIds);

	List<ContestResponse> fb_contests = new ArrayList<ContestResponse>();
	for (FbContest contest : contests) {
	    // 赛事下单数量
	    // int betCount = countMap.get(contest.getContestId());
	    int[] betCount = redisBetCountHandler.findContestBetRatio(ContestType.FOOTBALL, contest.getContestId());
	    ContestResponse resp = ContestTransformUtil.transformFbContest(contest, teams.get(contest.getHomeTeam()),
		    teams.get(contest.getAwayTeam()), betCount, true);
	    fb_contests.add(resp);
	}

	for (ContestResponse contest : fb_contests) {
	    // 中断、推迟的赛事不在前端展示
	    if (contest.getStatus() == ContestStatu.PEDING || contest.getStatus() == ContestStatu.CUT
		    || contest.getStatus() == ContestStatu.INTERRUPT || contest.getStatus() == ContestStatu.PUTOFF) {
		continue;
	    }

	    Date startTime = CbsTimeUtils.getDateByUtcFormattedDate(contest.getStart_time());
	    String remainTime = null;
	    if (startTime.before(new Date())
		    && (contest.getStatus() == ContestStatu.HALF_PREV || contest.getStatus() == ContestStatu.HALF_NEXT || contest
		            .getStatus() == ContestStatu.OVERTIME)) {
		remainTime = CbsTimeUtils.calRemainTime(contest.getStatus(), startTime);
	    }
	    contest.setRemain_time(remainTime);
	    contestReponses.add(contest);
	}
	return contestReponses;
    }

    /**
     * 根据主客队获取比赛
     */
    @Override
    public ContestResponse findContestsByTeam(Long homeTeam, Long awayTeam, String time) throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(homeTeam, awayTeam);
	FbContest contest = fbContestDao.selectByTeam(homeTeam, awayTeam, time);
	if (contest == null) {
	    return null;
	}
	List<Long> teamIds = new ArrayList<Long>();
	teamIds.add(contest.getHomeTeam());
	teamIds.add(contest.getAwayTeam());

	Map<Long, FbTeam> teams = fbTeamDao.findFbTeamMapByIds(teamIds);
	// int betCount =
	// redisBetCountHandler.findContestCount(ContestType.FOOTBALL,
	// contest.getContestId());
	int[] betCount = redisBetCountHandler.findContestBetRatio(ContestType.FOOTBALL, contest.getContestId());
	ContestResponse response = ContestTransformUtil.transformFbContest(contest, teams.get(contest.getHomeTeam()),
	        teams.get(contest.getAwayTeam()), betCount, true);
	ContestFilterUtil.filterContestLogo(response, true, null);

	return response;
    }
}
