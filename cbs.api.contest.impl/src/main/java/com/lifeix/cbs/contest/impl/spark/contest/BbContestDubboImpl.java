package com.lifeix.cbs.contest.impl.spark.contest;

import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestStatu_BB;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestType;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.contest.bean.bb.BbContestExtResponse;
import com.lifeix.cbs.contest.bean.bb.BbLiveWordsResponse;
import com.lifeix.cbs.contest.bean.bb.ext.BbPlayerResponse;
import com.lifeix.cbs.contest.bean.channel.ContestChannelResponse;
import com.lifeix.cbs.contest.bean.fb.ContestFullResponse;
import com.lifeix.cbs.contest.bean.fb.ContestListResponse;
import com.lifeix.cbs.contest.bean.fb.ContestResponse;
import com.lifeix.cbs.contest.bean.fb.ext.RecordResponse;
import com.lifeix.cbs.contest.dao.bb.*;
import com.lifeix.cbs.contest.dto.bb.*;
import com.lifeix.cbs.contest.dto.odds.OddsOp;
import com.lifeix.cbs.contest.im.LifeixImApiUtil;
import com.lifeix.cbs.contest.impl.redis.RedisBetCountHandler;
import com.lifeix.cbs.contest.service.spark.channel.ContestChannelDubbo;
import com.lifeix.cbs.contest.service.spark.contest.BbContestDubbo;
import com.lifeix.cbs.contest.util.ContestFilterUtil;
import com.lifeix.cbs.contest.util.transform.ContestTransformUtil;
import com.lifeix.exception.service.L99IllegalParamsException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("bbContestDubbo")
public class BbContestDubboImpl implements BbContestDubbo {

	protected static Logger LOG = LoggerFactory.getLogger(BbContestDubboImpl.class);

    @Autowired
    private BbTeamDao bbTeamDao;

    @Autowired
    private BbContestDao bbContestDao;

    @Autowired
    private BbContestExtDao bbContestExtDao;

    @Autowired
    private BbContestRecordDao bbContestRecordDao;

    @Autowired
    private BbLiveWordsDao bbLiveWordsDao;

    @Autowired
    private BbPlayerDao bbPlayerDao;

    @Autowired
    private ContestChannelDubbo contestChannelDubbo;

    @Autowired
    private RedisBetCountHandler redisBetCountHandler;

    @Autowired
    private BbOddsOpDao bbOpDao;

    @Override
    public Map<Long, ContestResponse> findContestMapByIds(List<Long> ids) {
	Map<Long, BbContest> contestMap = bbContestDao.findBbContestMapByIds(ids);
	Map<Long, ContestResponse> respMap = new HashMap<Long, ContestResponse>();
	if (contestMap == null || contestMap.size() == 0)
	    return respMap;
	List<Long> teamIds = new ArrayList<Long>();
	Set<Long> keySet = contestMap.keySet();
	List<String> contestIds = new ArrayList<String>();
	for (Long key : keySet) {
	    BbContest contest = contestMap.get(key);
	    teamIds.add(contest.getHomeTeam());
	    teamIds.add(contest.getAwayTeam());
	    contestIds.add(contest.getContestId().toString());
	}
	Map<Long, BbTeam> teams = bbTeamDao.findBbTeamMapByIds(teamIds);
	// Map<Long, Integer> countMap =
	// redisBetCountHandler.findContestsCount(ContestType.BASKETBALL,
	// contestIds);
	for (Long key : keySet) {
	    BbContest contest = contestMap.get(key);
	    // int betCount = countMap.get(contest.getContestId());
	    int[] betCount = redisBetCountHandler.findContestBetRatio(ContestType.BASKETBALL, contest.getContestId());
	    ContestResponse cr = ContestTransformUtil.transformBbContest(contest, teams.get(contest.getHomeTeam()),
		    teams.get(contest.getAwayTeam()), betCount, true);
	    respMap.put(key, cr);
	}
	return respMap;
    }

    /**
     * 获取超时未结算的比赛列表(超过开场时间3小时)
     * 
     * @return
     */
    @Override
    public List<ContestResponse> findTimeoutContest(Long contestId) {
	List<BbContest> timeouts = bbContestDao.findTimeoutContest(contestId, 20);
	List<ContestResponse> contestReponses = new ArrayList<ContestResponse>();
	for (BbContest contest : timeouts) {
	    contestReponses.add(ContestTransformUtil.transformBbContest(contest, null, null, new int[] { 0, 0 }, true));
	}
	return contestReponses;
    }

    /**
     * 修改赛事比分和状态
     * 
     * @param homeScores
     * @param awayScores
     * @param status
     * @return
     */
    @Override
    public boolean updateChanges(Long contestId, int homeScores, int awayScores, int status) {
	BbContest contest = new BbContest();
	contest.setContestId(contestId);
	contest.setHomeScores(homeScores);
	contest.setAwayScores(awayScores);
	contest.setStatus(status);
	return bbContestDao.updateChanges(contest);
    }

    /**
     * 批量保存赛事
     * 
     * @param list
     * @return
     */
    @Override
    public boolean saveContest(List<ContestResponse> list) {
	List<BbContest> contestList = new ArrayList<BbContest>(list.size());
	for (ContestResponse resp : list) {
	    BbContest contest = new BbContest();
	    contest.setContestId(resp.getContest_id());
	    contest.setTargetId(resp.getTarget_id());
	    contest.setCupId(resp.getCup_id());
	    contest.setCupName(resp.getCup_name());
	    contest.setColor(resp.getColor());
	    contest.setHomeTeam(resp.getHome_team());
	    contest.setHomeScores(resp.getHome_scores());
	    contest.setHomeRank(resp.getHome_rank());
	    contest.setAwayTeam(resp.getAway_team());
	    contest.setAwayScores(resp.getAway_scores());
	    contest.setAwayRank(resp.getAway_rank());
	    contest.setStartTime(CbsTimeUtils.getDateByUtcFormattedDate(resp.getStart_time()));
	    contest.setStatus(resp.getStatus());
	    contest.setOddsType(resp.getOdds_type());
	    contest.setBelongFive(resp.isBelong_five());
	    contest.setLevel(resp.getLevel());
	    contest.setLongbi(resp.isLongbi());
	    contest.setExtFlag(resp.getExt_flag());
	    contestList.add(contest);
	}
	return bbContestDao.saveContest(contestList);
    }

    /**
     * 篮球赛事列表
     */
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

	List<ContestResponse> contestReponses = buildContest(contests);

	ContestListResponse reponse = new ContestListResponse();
	reponse.setContests(contestReponses);
	reponse.setNumber(contests.size());
	ContestFilterUtil.filterContestListLogo(reponse, true, client);
	return reponse;
    }

    /**
     * 大赢家赛事列表 返回重要赛事
     */
    @Override
    public ContestListResponse findBbContestsForCbs(Date startTime, Date endTime) throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(startTime, endTime);

	List<ContestResponse> contestReponses = null;
	// 起始时间不能晚于结束时间
	if (startTime.after(endTime)) {
	    contestReponses = new ArrayList<ContestResponse>();
	} else {
	    List<Date> dates = CbsTimeUtils.rangeDates(startTime, endTime);
	    List<BbContest> contests = new ArrayList<BbContest>();
	    int tcount = 0;
	    for (Date date : dates) {
		if (tcount > 9) { // 最多只能返回9天的数据
		    break;
		}
		contests.addAll(bbContestDao.findBbContestsForImportant(date));
		tcount++;
	    }
	    contestReponses = buildContest(contests);
	}

	ContestListResponse reponse = new ContestListResponse();
	reponse.setContests(contestReponses);
	reponse.setNumber(contestReponses.size());
	ContestFilterUtil.filterContestListLogo(reponse, false, null);

	return reponse;
    }

    @Override
    public ContestListResponse findBbContestsV2ForCbs(String startTime, String endTime) throws L99IllegalParamsException {
	Date startDate = null, endDate = null;
	DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	Boolean ifFuture = null;
	if (startTime == null && endTime == null) {// 第一次访问加载三天的数据
	    Calendar calendar = Calendar.getInstance();
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.add(Calendar.DAY_OF_YEAR, -1);
	    startDate = calendar.getTime();
	    calendar.add(Calendar.DAY_OF_YEAR, 2);
	    endDate = calendar.getTime();
	    ifFuture = true;
	} else if (startTime != null && endTime != null) { // 指定时间范围的数据
	    try {
		startDate = format.parse(startTime);
		endDate = format.parse(endTime);
	    } catch (ParseException e) {
		throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	    }
	} else if (startTime != null) { // 向下加载
	    try {
		startDate = format.parse(startTime);
		endDate = (Date) startDate.clone();
		ifFuture = true;
	    } catch (ParseException e) {
		throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	    }
	} else if (endTime != null) { // 向上加载
	    try {
		endDate = format.parse(endTime);
		startDate = (Date) endDate.clone();
		ifFuture = false;
	    } catch (ParseException e) {
		throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	    }
	}

	ContestListResponse response = findBbContestsForImportant(startDate, endDate, ifFuture, false);
	List<ContestResponse> contests = response.getContests();

	// 计算赛事列表加载的时间起点
	Date prevDate = null;
	Date nextDate = null;
	if (contests.size() > 0) {
	    prevDate = CbsTimeUtils.getDateByUtcFormattedDate(contests.get(0).getStart_time());
	    if (prevDate.after(startDate)) {
		prevDate = startDate;
	    }
	    nextDate = CbsTimeUtils.getDateByUtcFormattedDate(contests.get(contests.size() - 1).getStart_time());
	    if (nextDate.before(endDate)) {
		nextDate = endDate;
	    }
	} else {
	    prevDate = startDate;
	    nextDate = endDate;
	}
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(prevDate);
	calendar.add(Calendar.DAY_OF_YEAR, -1);
	prevDate = calendar.getTime();
	calendar.setTime(nextDate);
	calendar.add(Calendar.DAY_OF_YEAR, 1);
	nextDate = calendar.getTime();

	response.setPrev_time(format.format(prevDate));
	response.setNext_time(format.format(nextDate));

	return response;
    }

    /**
     * 体育头条赛事列表，只返回一级赛事
     */
    @Override
    public ContestListResponse findBbContestsForImportant(Date startTime, Date endTime) throws L99IllegalParamsException {
	return findBbContestsForImportant(startTime, endTime, null, true);
    }

    /**
     * 加载全部赛事
     * 
     * @param startTime
     * @param endTime
     * @param ifFuture
     * @param recursionFlag
     * @return
     * @throws L99IllegalParamsException
     */
    private ContestListResponse findBbContestsForImportant(Date startTime, Date endTime, Boolean ifFuture,
	    boolean recursionFlag) throws L99IllegalParamsException {

	ParamemeterAssert.assertDataNotNull(startTime, endTime);

	List<ContestResponse> contestReponses = null;
	// 起始时间不能晚于结束时间
	if (startTime.after(endTime)) {
	    contestReponses = new ArrayList<ContestResponse>();
	} else {
	    List<Date> dates = CbsTimeUtils.rangeDates(startTime, endTime);
	    List<BbContest> contests = new ArrayList<BbContest>();
	    int tcount = 0;
	    for (Date date : dates) {
		if (tcount > 9) { // 最多只能返回9天的数据
		    break;
		}
		contests.addAll(bbContestDao.findBbContestsForImportant(date));
		tcount++;
	    }

	    // ifFuture不为空继续查询
	    if (ifFuture != null) {
		// 递归锁只允许一次递归
		if (contests.size() == 0 && !recursionFlag) {
		    BbContest contest = null;
		    if (ifFuture) { // 未来数据
			contest = bbContestDao.findNextBbContestsByCup(endTime, null);
		    } else { // 历史数据
			contest = bbContestDao.findPrevBbContestsByCup(startTime, null);
		    }
		    if (contest != null) {
			if (ifFuture) { // 未来三天的数据
			    startTime = contest.getStartTime();
			    endTime = CbsTimeUtils.upgradeTime(startTime, Calendar.DAY_OF_YEAR, 3);
			} else { // 往前三天的数据
			    endTime = contest.getStartTime();
			    startTime = CbsTimeUtils.upgradeTime(endTime, Calendar.DAY_OF_YEAR, -3);
			}
			return findBbContestsForImportant(startTime, endTime, ifFuture, true);
		    }
		}
	    }
	    contestReponses = buildContest(contests);

	}

	ContestListResponse reponse = new ContestListResponse();
	reponse.setContests(contestReponses);
	reponse.setNumber(contestReponses.size());
	ContestFilterUtil.filterContestListLogo(reponse, false, null);

	return reponse;
    }

    @Override
    public ContestListResponse findBbContestsForImportantByChannelId(Date startTime, Date endTime, Long channelId,
	    boolean ifFuture, boolean recursionFlag) throws L99IllegalParamsException {

	ParamemeterAssert.assertDataNotNull(startTime, endTime);

	// 当channel为空时 返回所有赛事列表方法
	if (channelId == null) {
	    return findBbContestsForImportant(startTime, endTime, ifFuture, recursionFlag);
	}
	List<BbContest> contestRets = new ArrayList<BbContest>();

	// 起始时间不能晚于结束时间
	if (!startTime.after(endTime)) {

	    // 获取分类信息
	    ContestChannelResponse channel = contestChannelDubbo.viewChannel(channelId);

	    // channel是否符合篮球赛事
	    if (channel != null && channel.getContest_type() == ContestType.BASKETBALL) {
		List<Long> cupIds = channel.getCup_id();

		// 根据时间获取赛事列表
		List<Date> dates = CbsTimeUtils.rangeDates(startTime, endTime);
		List<BbContest> contests = new ArrayList<BbContest>();
		int tcount = 0;
		for (Date date : dates) {
		    if (tcount > 9) { // 最多只能返回9天的数据
			break;
		    }
		    contests.addAll(bbContestDao.findBbContestsForImportant(date));
		    endTime = date;
		    tcount++;
		}

		// 根据channel的cupIds过滤赛事
		if (contests.size() > 0) {
		    if (cupIds != null && !cupIds.isEmpty()) {
			Set<Long> cupSets = new HashSet<Long>(cupIds);
			for (BbContest contest : contests) {
			    if (cupSets.contains(contest.getCupId())) {
				contestRets.add(contest);
			    }
			}
		    }
		}

		// 递归锁只允许一次递归
		if (contestRets.size() == 0 && !recursionFlag) {
		    BbContest contest = null;
		    if (ifFuture) { // 未来数据
			contest = bbContestDao.findNextBbContestsByCup(endTime, cupIds);
		    } else { // 历史数据
			contest = bbContestDao.findPrevBbContestsByCup(startTime, cupIds);
		    }
		    if (contest != null) {
			if (ifFuture) { // 未来三天的数据
			    startTime = contest.getStartTime();
			    endTime = CbsTimeUtils.upgradeTime(startTime, Calendar.DAY_OF_YEAR, 3);
			} else { // 往前三天的数据
			    endTime = contest.getStartTime();
			    startTime = CbsTimeUtils.upgradeTime(endTime, Calendar.DAY_OF_YEAR, -3);
			}
			return findBbContestsForImportantByChannelId(startTime, endTime, channelId, ifFuture, true);
		    }
		}
	    }
	}

	List<ContestResponse> contestReponses = buildContest(contestRets);

	ContestListResponse reponse = new ContestListResponse();
	reponse.setContests(contestReponses);
	reponse.setNumber(contestReponses.size());
	ContestFilterUtil.filterContestListLogo(reponse, false, null);

	return reponse;
    }

    /**
     * 转换篮球赛事列表
     * 
     * @param contests
     * @return
     */
    private List<ContestResponse> buildContest(List<BbContest> contests) {
	List<ContestResponse> bb_contests = new ArrayList<ContestResponse>();

	if (contests == null || contests.size() == 0) {
	    return bb_contests;
	}

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

	for (BbContest contest : contests) {

	    // 中断、推迟的赛事不在前端展示
	    if (contest.getStatus() == ContestStatu_BB.PEDING || contest.getStatus() == ContestStatu_BB.INTERRUPT
		    || contest.getStatus() == ContestStatu_BB.PUTOFF) {
		continue;
	    }

	    // 赛事下单数量
	    // int betCount = countMap.get(contest.getContestId());
	    int[] betCount = redisBetCountHandler.findContestBetRatio(ContestType.BASKETBALL, contest.getContestId());
	    bb_contests.add(ContestTransformUtil.transformBbContest(contest, teams.get(contest.getHomeTeam()),
		    teams.get(contest.getAwayTeam()), betCount, true));
	}
	return bb_contests;
    }

    /**
     * 单篇赛事
     * 
     * @param contestId
     * @return
     * @throws L99IllegalParamsException
     */
    @Override
    public ContestFullResponse findBbContestInfo(Long contestId, Boolean newFlag) throws L99IllegalParamsException {

	ParamemeterAssert.assertDataNotNull(contestId);
	BbContest contest = bbContestDao.selectById(contestId);
	if (contest == null) {
	    return null;
	}

	List<Long> teamIds = new ArrayList<Long>();
	teamIds.add(contest.getHomeTeam());
	teamIds.add(contest.getAwayTeam());
	Map<Long, BbTeam> teams = bbTeamDao.findBbTeamMapByIds(teamIds);
	BbTeam homeTeam = teams.get(contest.getHomeTeam());
	BbTeam awayTeam = teams.get(contest.getAwayTeam());

	// 开赛前两天到赛后十五天才允许创建房间
	if (contest.getRoomId() == null && CbsTimeUtils.roomCreateRange(contest.getStartTime())) {
	    String homeName = homeTeam == null ? "" : homeTeam.getName();
	    String awayName = awayTeam == null ? "" : awayTeam.getName();
	    String roomName = String.format("%s VS %s ", homeName, awayName);
	    Long roomId = LifeixImApiUtil.getInstance().createImRoom(ContestType.BASKETBALL, contestId, roomName,
		    contest.getStartTime());
	    if (roomId > 0) {
		bbContestDao.updateRoom(contestId, roomId);
		contest.setRoomId(roomId);
	    }
	}

	// 赛事下单数量
	// int betCount =
	// redisBetCountHandler.findContestCount(ContestType.BASKETBALL,
	// contest.getContestId());
	int[] betCount = redisBetCountHandler.findContestBetRatio(ContestType.BASKETBALL, contest.getContestId());
	// 赛事基础信息
	ContestResponse contestResp = ContestTransformUtil.transformBbContest(contest, homeTeam, awayTeam, betCount, true);

	// 赛事扩展信息
	BbContestExt contestExt = bbContestExtDao.selectById(contestId);
	BbContestExtResponse bbContestExtResp = null;
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

	}
	// 赛事文字直播
	List<BbLiveWordsResponse> live_wordsResp = new LinkedList<BbLiveWordsResponse>();
	List<BbLiveWords> bbLiveWords = null;
	if (Boolean.TRUE.equals(newFlag)) {
	    bbLiveWords = bbLiveWordsDao.selectByContestIdNew(contestId, null);
	} else {
	    bbLiveWords = bbLiveWordsDao.selectByContestId(contestId, null);
	}
	if (bbLiveWords != null && bbLiveWords.size() > 0) {
	    for (BbLiveWords bbLiveWord : bbLiveWords) {
		live_wordsResp.add(ContestTransformUtil.transformBbLiveWords(bbLiveWord));
	    }
	}

	// 赛事交手记录
	List<RecordResponse> recordResponse = new ArrayList<RecordResponse>();
	List<BbContestRecord> bbRecords = bbContestRecordDao.selectHeadToHeadRecord(contest.getHomeTeam(),
	        contest.getAwayTeam(), 5);
	for (BbContestRecord bbRecord : bbRecords) {
	    recordResponse.add(ContestTransformUtil.transformBbContestRecord(bbRecord));
	}
	ContestFullResponse response = new ContestFullResponse();
	// 赛事赔率获取
	OddsOp op = bbOpDao.selectByContest(contestId);
	if (op != null) {
	    response.setHome_roi(op.getHomeRoi());
	    response.setAway_roi(op.getAwayRoi());
	}

	response.setContest(contestResp);
	response.setBb_contest_ext(bbContestExtResp);
	response.setBb_live_words(live_wordsResp);
	response.setRecords(recordResponse);
	return response;
    }

    @Override
    public List<Long> findBbContestIngNum() {
	DateFormat dateTimeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String beginDate = dateTimeformat.format(new Date().getTime() - 300000);
	List<Long> ids = bbContestDao.findBbContestIngNum(beginDate);
	return ids;

    }

    @Override
    public boolean updateContestRoom(Long contestId, Long roomId) {
	return bbContestDao.updateRoom(contestId, roomId);
    }


}
