package com.lifeix.cbs.contest.impl.bb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lifeix.framwork.util.JsonUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.exception.MsgCode.ContestMsg;
import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.api.common.util.ContestConstants;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestMemcached;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestStatu;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestStatu_BB;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestType;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.contest.bean.bb.BbChangeDataResponse;
import com.lifeix.cbs.contest.bean.bb.BbContestExtResponse;
import com.lifeix.cbs.contest.bean.bb.BbFixedResponse;
import com.lifeix.cbs.contest.bean.bb.BbPlayerStatisticsResponse;
import com.lifeix.cbs.contest.bean.bb.BbTeamStatisticsResponse;
import com.lifeix.cbs.contest.bean.bb.ext.BbPlayerListResponse;
import com.lifeix.cbs.contest.bean.bb.ext.BbPlayerResponse;
import com.lifeix.cbs.contest.bean.contest.BetCountRatioResponse;
import com.lifeix.cbs.contest.bean.fb.ContestListResponse;
import com.lifeix.cbs.contest.bean.fb.ContestResponse;
import com.lifeix.cbs.contest.bean.fb.TeamListResponse;
import com.lifeix.cbs.contest.bean.fb.TeamResponse;
import com.lifeix.cbs.contest.bean.fb.ext.RecordResponse;
import com.lifeix.cbs.contest.dao.bb.BbContestDao;
import com.lifeix.cbs.contest.dao.bb.BbContestExtDao;
import com.lifeix.cbs.contest.dao.bb.BbContestRecordDao;
import com.lifeix.cbs.contest.dao.bb.BbPlayerDao;
import com.lifeix.cbs.contest.dao.bb.BbPlayerStatisticsDao;
import com.lifeix.cbs.contest.dao.bb.BbTeamDao;
import com.lifeix.cbs.contest.dao.bb.BbTeamStatisticsDao;
import com.lifeix.cbs.contest.dto.bb.BbContest;
import com.lifeix.cbs.contest.dto.bb.BbContestExt;
import com.lifeix.cbs.contest.dto.bb.BbContestRecord;
import com.lifeix.cbs.contest.dto.bb.BbPlayer;
import com.lifeix.cbs.contest.dto.bb.BbPlayerStatistics;
import com.lifeix.cbs.contest.dto.bb.BbTeam;
import com.lifeix.cbs.contest.dto.bb.BbTeamStatistics;
import com.lifeix.cbs.contest.impl.ImplSupport;
import com.lifeix.cbs.contest.impl.redis.RedisBetCountHandler;
import com.lifeix.cbs.contest.service.bb.BbContestService;
import com.lifeix.cbs.contest.service.spark.cup.BbCupDubbo;
import com.lifeix.cbs.contest.service.spark.odds.BbOddsDubbo;
import com.lifeix.cbs.contest.util.ContestFilterUtil;
import com.lifeix.cbs.contest.util.transform.ContestTransformUtil;
import com.lifeix.common.utils.RegexUtil;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.framework.memcache.MemcacheService;
import com.lifeix.user.beans.CustomResponse;

@Service("bbContestService")
public class BbContestServiceImpl extends ImplSupport implements BbContestService {

    @Autowired
    private BbContestDao bbContestDao;

    @Autowired
    private BbTeamDao bbTeamDao;

    @Autowired
    private BbPlayerDao bbPlayerDao;

    @Autowired
    private BbPlayerStatisticsDao bbPlayerStatisticsDao; // 个人技术统计

    @Autowired
    private BbTeamStatisticsDao bbTeamStatisticsDao; // 球队技术统计，包括每节比赛比分

    @Autowired
    private BbContestExtDao BbContestExtDao;

    @Autowired
    private BbContestExtDao bbContestExtDao;

    @Autowired
    private BbContestRecordDao bbContestRecordDao;

    @Autowired
    private RedisBetCountHandler redisBetCountHandler;

    @Autowired
    private BbCupDubbo bbCupDubbo;

    @Autowired
    private BbOddsDubbo bbOddsDubbo;

    @Autowired
    protected MemcacheService memcacheService;

    // 主客队
    private static final int HOME_TIME = 1;
    private static final int AWAY_TIME = 2;

    @Override
    public ContestListResponse findBbContestsByTime(String time, boolean fullFlag, String client)
	    throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(time);

	Date fTime = null;
	try {
	    fTime = new SimpleDateFormat("yyyy-MM-dd").parse(time);
	} catch (ParseException e) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}
	List<BbContest> contests = bbContestDao.findBbContestsByRangeTime(fTime, fullFlag);

	List<Long> teamIds = new ArrayList<Long>();
	List<String> contestIds = new ArrayList<String>();
	for (BbContest contest : contests) {
	    teamIds.add(contest.getHomeTeam());
	    teamIds.add(contest.getAwayTeam());
	    contestIds.add(contest.getContestId().toString());
	}

	Map<Long, BbTeam> teams = bbTeamDao.findBbTeamMapByIds(teamIds);
	// Map<Long, Integer> countMap =
	// redisBetCountHandler.findContestsCount(ContestType.BASKETBALL,
	// contestIds);

	List<ContestResponse> fb_contests = new ArrayList<ContestResponse>();
	for (BbContest contest : contests) {
	    // 赛事下单数量
	    // int betCount = countMap.get(contest.getContestId());
	    int[] betCount = redisBetCountHandler.findContestBetRatio(ContestType.BASKETBALL, contest.getContestId());
	    ContestResponse resp = ContestTransformUtil.transformBbContest(contest, teams.get(contest.getHomeTeam()),
		    teams.get(contest.getAwayTeam()), betCount, true);
	    fb_contests.add(resp);
	}

	List<ContestResponse> contestReponses = new ArrayList<ContestResponse>();
	for (ContestResponse contest : fb_contests) {
	    // 中断、推迟的赛事不在前端展示
	    if (contest.getStatus() == ContestStatu_BB.PEDING || contest.getStatus() == ContestStatu_BB.INTERRUPT
		    || contest.getStatus() == ContestStatu_BB.PUTOFF) {
		continue;
	    }
	    Date startTime = CbsTimeUtils.getDateByUtcFormattedDate(contest.getStart_time());
	    String remainTime = null;
	    if (startTime.before(new Date())
		    && (contest.getStatus() == ContestStatu.HALF_PREV || contest.getStatus() == ContestStatu.HALF_NEXT || contest
		            .getStatus() == ContestStatu.OVERTIME)) {
		remainTime = calRemainTime(contest.getStatus(), startTime);
	    }
	    contest.setRemain_time(remainTime);
	    contestReponses.add(contest);
	}

	ContestListResponse reponse = new ContestListResponse();
	reponse.setContests(contestReponses);
	reponse.setNumber(contests.size());
	ContestFilterUtil.filterContestListLogo(reponse, true, client);

	return reponse;
    }

    @Override
    public ContestResponse findBbContestsById(Long contestId, String client) throws L99IllegalParamsException,
	    L99IllegalDataException {
	ParamemeterAssert.assertDataNotNull(contestId);

	BbContest contest = bbContestDao.selectById(contestId);
	if (contest == null) {
	    throw new L99IllegalDataException(ContestMsg.CODE_CONTEST_NOT_EXIST, ContestMsg.KEY_CONTEST_NOT_EXIST);
	}

	List<Long> teamIds = new ArrayList<Long>();
	teamIds.add(contest.getHomeTeam());
	teamIds.add(contest.getAwayTeam());

	Map<Long, BbTeam> teams = bbTeamDao.findBbTeamMapByIds(teamIds);

	// 赛事下单数量
	// int betCount =
	// redisBetCountHandler.findContestCount(ContestType.BASKETBALL,
	// contestId);
	int[] betCount = redisBetCountHandler.findContestBetRatio(ContestType.BASKETBALL, contest.getContestId());
	ContestResponse response = ContestTransformUtil.transformBbContest(contest, teams.get(contest.getHomeTeam()),
	        teams.get(contest.getAwayTeam()), betCount, true);
	ContestFilterUtil.filterContestLogo(response, true, client);

	return response;
    }

    /***
     * 根据房间id获取赛事
     */
    @Override
    public ContestResponse findBbContestsByRoomId(Long roomId) throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(roomId);
	BbContest contest = bbContestDao.selectByRoomId(roomId);
	if (contest == null) {
	    return null;
	}
	List<Long> teamIds = new ArrayList<Long>();
	teamIds.add(contest.getHomeTeam());
	teamIds.add(contest.getAwayTeam());

	Map<Long, BbTeam> teams = bbTeamDao.findBbTeamMapByIds(teamIds);

	// 赛事下单数量
	// int betCount =
	// redisBetCountHandler.findContestCount(ContestType.BASKETBALL,
	// contest.getContestId());
	int[] betCount = redisBetCountHandler.findContestBetRatio(ContestType.BASKETBALL, contest.getContestId());
	ContestResponse response = ContestTransformUtil.transformBbContest(contest, teams.get(contest.getHomeTeam()),
	        teams.get(contest.getAwayTeam()), betCount, true);
	ContestFilterUtil.filterContestLogo(response, true, null);

	return response;
    }

    @Override
    public CustomResponse findBbScoreByContestIds(String contestIds) throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(contestIds);

	List<Long> ids = new ArrayList<Long>();
	for (String id : contestIds.split(",")) {
	    if (!RegexUtil.hasMatche(id.trim(), RegexUtil.DOMAIN_REG2)) {
		continue;
	    }
	    ids.add(Long.valueOf(id.trim()));
	}
	if (ids.size() == 0) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}

	Map<Long, BbContest> contestMap = bbContestDao.findBbContestMapByIds(ids);

	CustomResponse response = new CustomResponse();
	for (BbContest contest : contestMap.values()) {
	    String ret = String.format("%d_%d_%d", contest.getHomeScores(), contest.getAwayScores(), contest.getStatus());
	    response.put(contest.getContestId().toString(), ret);
	}

	return response;
    }

    @Override
    public void dropContest(Long contestId) throws L99IllegalParamsException, L99IllegalDataException {
	ParamemeterAssert.assertDataNotNull(contestId);

	BbContest contest = bbContestDao.selectById(contestId);
	if (contest == null) {
	    throw new L99IllegalDataException(ContestMsg.CODE_CONTEST_NOT_EXIST, ContestMsg.KEY_CONTEST_NOT_EXIST);
	}
	if (contest.getBetCount() > 0) {
	    throw new L99IllegalDataException(ContestMsg.CODE_CONTEST_CANT_DROP, ContestMsg.KEY_CONTEST_CANT_DROP);
	}
	boolean flag = bbContestDao.delete(contest);
	if (!flag) {
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}
    }

    @Override
    public ContestListResponse findAbnormalContests(Long startId, int limit) {
	limit = Math.min(limit, ContestConstants.SQL_RET_LIMIT);
	limit = Math.max(limit, 1);

	List<BbContest> contests = bbContestDao.findBbContestsByAbnormal(startId, limit);

	List<Long> teamIds = new ArrayList<Long>();
	List<String> contestIds = new ArrayList<String>();
	for (BbContest contest : contests) {
	    teamIds.add(contest.getHomeTeam());
	    teamIds.add(contest.getAwayTeam());
	    contestIds.add(contest.getContestId().toString());
	}

	Map<Long, BbTeam> teams = bbTeamDao.findBbTeamMapByIds(teamIds);
	// Map<Long, Integer> countMap =
	// redisBetCountHandler.findContestsCount(ContestType.BASKETBALL,
	// contestIds);

	List<ContestResponse> fb_contests = new ArrayList<ContestResponse>();
	for (BbContest contest : contests) {
	    // 赛事下单数量
	    // int betCount = countMap.get(contest.getContestId());
	    int[] betCount = redisBetCountHandler.findContestBetRatio(ContestType.BASKETBALL, contest.getContestId());
	    fb_contests.add(ContestTransformUtil.transformBbContest(contest, teams.get(contest.getHomeTeam()),
		    teams.get(contest.getAwayTeam()), betCount, false));
	}

	ContestListResponse reponse = new ContestListResponse();
	reponse.setContests(fb_contests);
	reponse.setNumber(contests.size());

	return reponse;
    }

    /**
     * 更改篮球信息
     * 
     * @param contestId
     * @param homeScore
     * @param awayScore
     * @param status
     * @param isLock
     * @return
     * @throws L99IllegalParamsException
     */
    @Override
    public void updateContests(Long contestId, Integer homeScore, Integer awayScore, Integer status, Boolean isLock,
	    Integer level, String cupName, Integer homeCount, Integer awayCount) throws L99IllegalParamsException,
	    L99IllegalDataException {
	ParamemeterAssert.assertDataNotNull(contestId);
	BbContest contest = bbContestDao.selectById(contestId);

	// memcache更新标志位
	Boolean updateMemflag = false;

	if (contest != null) {

	    // 若赛事等级变动则需要更新memcache 近30天赛事缓存
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
	    boolean flag = bbContestDao.updateInner(contest);
	    if (!flag) {
		throw new L99IllegalDataException(BasicMsg.CODE_OPERATE_FAIL, BasicMsg.KEY_OPERATE_FAIL);
	    }

	    // 更新memcache
	    if (updateMemflag) {
		// 近30天赛事分类缓存
		memcacheService.delete(ContestMemcached.VALID_BB_CUP);

		// 检查赛事赔率
		bbOddsDubbo.checkWarnOdds(contestId);
	    }
	} else {
	    throw new L99IllegalDataException(ContestMsg.CODE_CONTEST_NOT_EXIST, ContestMsg.KEY_CONTEST_NOT_EXIST);
	}
    }

    @Override
    public TeamListResponse findBbTeams(String searchKey) throws L99IllegalParamsException {

	ParamemeterAssert.assertDataNotNull(searchKey);

	List<BbTeam> teams = null;
	if (RegexUtil.hasMatche(searchKey, "(\\d)+")) { // ID查询
	    teams = new ArrayList<BbTeam>();
	    BbTeam team = bbTeamDao.selectById(Long.valueOf(searchKey));
	    teams.add(team);
	} else {
	    teams = bbTeamDao.searchTeam(searchKey);
	}

	List<TeamResponse> fb_teams = new ArrayList<TeamResponse>();
	for (BbTeam team : teams) {
	    fb_teams.add(ContestTransformUtil.transformBbTeam(team, false));
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

	BbTeam team = bbTeamDao.selectById(id);
	if (team == null) {
	    throw new L99IllegalDataException(ContestMsg.CODE_TEAM_NOT_EXIST, ContestMsg.KEY_TEAM_NOT_EXIST);
	}

	if (StringUtils.isNotEmpty(name)) {
	    team.setName(name);
	}

	if (StringUtils.isNotEmpty(logo)) {
	    team.setLogo(logo);
	}
	boolean flag = bbTeamDao.update(team);
	if (!flag) {
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}
    }

    /**
     * 查球员列表
     * 
     * @param startId
     * @param limit
     * @return
     */
    @Override
    public BbPlayerListResponse findBbPlayers(Long startId, int limit) {
	limit = Math.max(1, limit);
	limit = Math.min(20, limit);
	limit++;

	BbPlayerListResponse listResponse = new BbPlayerListResponse();
	List<BbPlayer> playerList = bbPlayerDao.selectMore(startId, limit);
	if (playerList == null || playerList.isEmpty())
	    return listResponse;

	if (playerList.size() == limit) {
	    playerList = playerList.subList(0, limit - 1);
	    startId = playerList.get(limit - 2).getId();
	} else {
	    startId = null;
	}

	List<Long> teamIds = new ArrayList<Long>();
	for (BbPlayer player : playerList) {
	    teamIds.add(player.getTeamId());
	}
	Map<Long, BbTeam> teams = bbTeamDao.findBbTeamMapByIds(teamIds);

	List<BbPlayerResponse> respList = new ArrayList<BbPlayerResponse>(playerList.size());
	for (int i = 0; i < playerList.size(); i++) {
	    BbPlayer player = playerList.get(i);
	    BbTeam team = teams.get(player.getTeamId());
	    TeamResponse tr = null;
	    if (team != null) {
		tr = ContestTransformUtil.transformBbTeam(team, true);
	    }
	    BbPlayerResponse resp = ContestTransformUtil.transformBbPlayer(player, tr);
	    respList.add(resp);
	}
	listResponse.setStartId(startId);
	listResponse.setLimit(limit - 1);
	listResponse.setPlayers(respList);
	return listResponse;
    }

    /**
     * 更新球员信息
     * 
     * @param id
     * @param name
     * @param firstName
     * @param lastName
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    @Override
    public void updatePlayer(Long id, String name, String firstName, String lastName) throws L99IllegalParamsException,
	    L99IllegalDataException {
	ParamemeterAssert.assertDataNotNull(id, name);
	BbPlayer player = bbPlayerDao.selectById(id);
	if (player == null) {
	    throw new L99IllegalDataException(ContestMsg.CODE_PLAYER_NOT_EXIST, ContestMsg.KEY_PLAYER_NOT_EXIST);
	}
	player.setName(name);
	if (StringUtils.isNotBlank(firstName)) {
	    player.setFirstName(firstName);
	}
	if (StringUtils.isNotBlank(lastName)) {
	    player.setLastName(lastName);
	}
	boolean flag = bbPlayerDao.update(player);
	if (!flag) {
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}
    }

    /**
     * 按球员名字或球队搜索
     * 
     * @param name
     * @param teamId
     * @return
     * @throws L99IllegalParamsException
     */
    @Override
    public BbPlayerListResponse searchPlayer(String name, Long teamId) throws L99IllegalParamsException {
	BbPlayerListResponse listResponse = new BbPlayerListResponse();
	List<BbPlayer> playerList = new ArrayList<BbPlayer>();
	if (!StringUtils.isEmpty(name)) {
	    playerList = bbPlayerDao.selectByName(name);
	} else if (teamId != null) {
	    playerList = bbPlayerDao.selectByTeamId(teamId);
	}
	if (playerList == null || playerList.isEmpty())
	    return listResponse;
	List<Long> teamIds = new ArrayList<Long>();
	for (BbPlayer player : playerList) {
	    teamIds.add(player.getTeamId());
	}
	Map<Long, BbTeam> teams = bbTeamDao.findBbTeamMapByIds(teamIds);
	List<BbPlayerResponse> respList = new ArrayList<BbPlayerResponse>(playerList.size());
	for (int i = 0; i < playerList.size(); i++) {
	    BbPlayer player = playerList.get(i);
	    BbTeam team = teams.get(player.getTeamId());
	    TeamResponse tr = null;
	    if (team != null) {
		tr = ContestTransformUtil.transformBbTeam(team, true);
	    }
	    BbPlayerResponse resp = ContestTransformUtil.transformBbPlayer(player, tr);
	    respList.add(resp);
	}
	listResponse.setPlayers(respList);
	return listResponse;
    }

    /**
     * 开场时间在当前之前 并且 赛事状态为上半场、下半场、加时时求剩余时间
     * 
     * @param status
     * @param startTime
     * @return
     */
    private static String calRemainTime(int status, Date startTime) {
	long interval = new Date().getTime() - startTime.getTime();
	int hour = (int) (interval / 1000 / 3600);
	int minute = (int) (interval / 1000 % 3600 / 60) + hour * 60;
	if (status == ContestStatu.HALF_NEXT) {
	    minute = (minute - 15) < 45 ? 45 : (minute - 15);
	}
	int second = (int) (interval / 1000 % 60);
	String remainTime = (String.format("%s:%s", (minute < 10 ? "0" + minute : ("" + minute)), (second < 10 ? "0"
	        + second : "" + second)));
	if (status == ContestStatu.HALF_PREV && minute > 45) {
	    return "45+";
	}
	if (minute > 90) {
	    return "90+";
	}
	return remainTime;
    }

    /**
     * 封装返回 每节比分赛况、球队技术统计、球员技术统计 等经常变化的数据
     */
    @Override
    public BbChangeDataResponse findBbChangeDataResponseByContestId(Long contestId, Boolean lineupFlag)
	    throws L99IllegalParamsException {

	ParamemeterAssert.assertDataNotNull(contestId);

	BbChangeDataResponse bbChangeDataResponse = new BbChangeDataResponse();
	List<BbPlayerStatistics> playerStatistics = bbPlayerStatisticsDao.selectByContestId(contestId);
	List<BbPlayerStatisticsResponse> homePlayerStatistics = new ArrayList<BbPlayerStatisticsResponse>();
	List<BbPlayerStatisticsResponse> awayPlayerStatistics = new ArrayList<BbPlayerStatisticsResponse>();
	for (BbPlayerStatistics statistic : playerStatistics) {
	    BbPlayerStatisticsResponse playStatisticResponse = ContestTransformUtil.transformBbPlayerStatistics(statistic);
	    // 主队
	    if (playStatisticResponse.getTeam() == HOME_TIME) {
		homePlayerStatistics.add(playStatisticResponse);
	    } else if (playStatisticResponse.getTeam() == AWAY_TIME) {
		awayPlayerStatistics.add(playStatisticResponse);
	    }

	}
	bbChangeDataResponse.setA_player_statistics(awayPlayerStatistics);
	bbChangeDataResponse.setH_player_statistics(homePlayerStatistics);

	List<BbTeamStatistics> teamStatistics = bbTeamStatisticsDao.selectByContestId(contestId);

	for (BbTeamStatistics bbTeamStatistic : teamStatistics) {
	    BbTeamStatisticsResponse bbTeamStatisticsResponse = ContestTransformUtil
		    .transformBbTeamStatistics(bbTeamStatistic);
	    if (bbTeamStatisticsResponse.getTeam() == HOME_TIME) {
		bbChangeDataResponse.setH_team_statistics(bbTeamStatisticsResponse);
	    } else if (bbTeamStatisticsResponse.getTeam() == AWAY_TIME) {
		bbChangeDataResponse.setA_team_statistics(bbTeamStatisticsResponse);
	    }
	}
	// 返回阵容
	if (lineupFlag) {
	    BbContestExt bbContestExt = BbContestExtDao.selectById(contestId);
	    if (bbContestExt != null) {
		BbContestExtResponse bbContestExtResponse = ContestTransformUtil.transformBbContestExt(bbContestExt);
		// 补充球员信息
		List<String> playerTargetIds = new ArrayList<String>();
		List<BbPlayerResponse> homeLineups = bbContestExtResponse.getHome_lineups();
		if (homeLineups != null && homeLineups.size() > 0) {
		    for (BbPlayerResponse bbPlayerResp : homeLineups) {
			playerTargetIds.add(bbPlayerResp.getPlayer_id());
		    }
		}
		List<BbPlayerResponse> awayLineups = bbContestExtResponse.getAway_lineups();
		if (awayLineups != null && awayLineups.size() > 0) {
		    for (BbPlayerResponse bbPlayerResp : awayLineups) {
			playerTargetIds.add(bbPlayerResp.getPlayer_id());
		    }
		}
		Map<String, BbPlayer> bbPlayerMap = bbPlayerDao.selectByTargetIds(playerTargetIds);
		if (homeLineups != null && homeLineups.size() > 0) {
		    for (BbPlayerResponse bbPlayerResp : homeLineups) {
			BbPlayer bbPlayer = bbPlayerMap.get(bbPlayerResp.getPlayer_id());
			if (bbPlayer != null) {
			    if (StringUtils.isNotBlank(bbPlayer.getName())) {
				bbPlayerResp.setName(bbPlayer.getName());
			    }
			    if (StringUtils.isNotBlank(bbPlayer.getFirstName())) {
				bbPlayerResp.setFirst_name(bbPlayer.getFirstName());
			    }
			    if (StringUtils.isNotBlank(bbPlayer.getLastName())) {
				bbPlayerResp.setLast_name(bbPlayer.getLastName());
			    }
			}
		    }
		}
		if (awayLineups != null && awayLineups.size() > 0) {
		    for (BbPlayerResponse bbPlayerResp : awayLineups) {
			BbPlayer bbPlayer = bbPlayerMap.get(bbPlayerResp.getPlayer_id());
			if (bbPlayer != null) {
			    if (StringUtils.isNotBlank(bbPlayer.getName())) {
				bbPlayerResp.setName(bbPlayer.getName());
			    }
			    if (StringUtils.isNotBlank(bbPlayer.getFirstName())) {
				bbPlayerResp.setFirst_name(bbPlayer.getFirstName());
			    }
			    if (StringUtils.isNotBlank(bbPlayer.getLastName())) {
				bbPlayerResp.setLast_name(bbPlayer.getLastName());
			    }
			}
		    }
		}
		bbChangeDataResponse.setAway_lineups(awayLineups);
		bbChangeDataResponse.setHome_lineups(homeLineups);
	    }
	}
	return bbChangeDataResponse;
    }

    @Override
    public BbFixedResponse findBbFixedDataByContestId(Long contestId) throws L99IllegalParamsException {
	BbContest contest = bbContestDao.selectById(contestId);
	if (contest == null) {
	    return null;
	}

	// 赛事扩展信息
	BbContestExt contestExt = bbContestExtDao.selectById(contestId);
	BbContestExtResponse bbContestExtResp = null;
	BbFixedResponse bbFixedResponse = new BbFixedResponse();
	if (contestExt != null) {
	    bbContestExtResp = ContestTransformUtil.transformBbContestExt(contestExt);
	    // 补充球员信息
	    List<String> playerTargetIds = new ArrayList<String>();
	    List<BbPlayerResponse> homeLineups = bbContestExtResp.getHome_lineups();
	    if (homeLineups != null && homeLineups.size() > 0) {
		for (BbPlayerResponse bbPlayerResp : homeLineups) {
		    playerTargetIds.add(bbPlayerResp.getPlayer_id());
		}
	    }
	    List<BbPlayerResponse> awayLineups = bbContestExtResp.getAway_lineups();
	    if (awayLineups != null && awayLineups.size() > 0) {
		for (BbPlayerResponse bbPlayerResp : awayLineups) {
		    playerTargetIds.add(bbPlayerResp.getPlayer_id());
		}
	    }
	    Map<String, BbPlayer> bbPlayerMap = bbPlayerDao.selectByTargetIds(playerTargetIds);
	    if (homeLineups != null && homeLineups.size() > 0) {
		for (BbPlayerResponse bbPlayerResp : homeLineups) {
		    BbPlayer bbPlayer = bbPlayerMap.get(bbPlayerResp.getPlayer_id());
		    if (bbPlayer != null) {
			if (StringUtils.isNotBlank(bbPlayer.getName())) {
			    bbPlayerResp.setName(bbPlayer.getName());
			}
			if (StringUtils.isNotBlank(bbPlayer.getFirstName())) {
			    bbPlayerResp.setFirst_name(bbPlayer.getFirstName());
			}
			if (StringUtils.isNotBlank(bbPlayer.getLastName())) {
			    bbPlayerResp.setLast_name(bbPlayer.getLastName());
			}
		    }
		}
	    }
	    if (awayLineups != null && awayLineups.size() > 0) {
		for (BbPlayerResponse bbPlayerResp : awayLineups) {
		    BbPlayer bbPlayer = bbPlayerMap.get(bbPlayerResp.getPlayer_id());
		    if (bbPlayer != null) {
			if (StringUtils.isNotBlank(bbPlayer.getName())) {
			    bbPlayerResp.setName(bbPlayer.getName());
			}
			if (StringUtils.isNotBlank(bbPlayer.getFirstName())) {
			    bbPlayerResp.setFirst_name(bbPlayer.getFirstName());
			}
			if (StringUtils.isNotBlank(bbPlayer.getLastName())) {
			    bbPlayerResp.setLast_name(bbPlayer.getLastName());
			}
		    }
		}
	    }
	    bbFixedResponse.setAway_lineups(awayLineups);
	    bbFixedResponse.setHome_lineups(homeLineups);

	}

	// 赛事交手记录
	List<RecordResponse> recordResponse = new ArrayList<RecordResponse>();
	List<BbContestRecord> bbRecords = bbContestRecordDao.selectHeadToHeadRecord(contest.getHomeTeam(),
	        contest.getAwayTeam(), 5);
	for (BbContestRecord bbRecord : bbRecords) {
	    recordResponse.add(ContestTransformUtil.transformBbContestRecord(bbRecord));
	}
	bbFixedResponse.setRecords(recordResponse);
	return bbFixedResponse;
    }

    @Override
    public ContestResponse findBbContestsByTeam(Long homeTeam, Long awayTeam, String time) throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(homeTeam, awayTeam);
	BbContest contest = bbContestDao.findBbContestsByTeam(homeTeam, awayTeam, time);
	if (contest == null) {
	    return null;
	}
	List<Long> teamIds = new ArrayList<Long>();
	teamIds.add(contest.getHomeTeam());
	teamIds.add(contest.getAwayTeam());

	Map<Long, BbTeam> teams = bbTeamDao.findBbTeamMapByIds(teamIds);

	// 赛事下单数量
	// int betCount =
	// redisBetCountHandler.findContestCount(ContestType.BASKETBALL,
	// contest.getContestId());
	int[] betCount = redisBetCountHandler.findContestBetRatio(ContestType.BASKETBALL, contest.getContestId());
	ContestResponse response = ContestTransformUtil.transformBbContest(contest, teams.get(contest.getHomeTeam()),
	        teams.get(contest.getAwayTeam()), betCount, true);
	ContestFilterUtil.filterContestLogo(response, true, null);

	return response;
    }

}
