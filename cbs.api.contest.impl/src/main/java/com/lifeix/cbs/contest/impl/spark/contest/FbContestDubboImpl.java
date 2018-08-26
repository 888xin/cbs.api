package com.lifeix.cbs.contest.impl.spark.contest;

import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestStatu;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestType;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.contest.bean.channel.ContestChannelResponse;
import com.lifeix.cbs.contest.bean.fb.*;
import com.lifeix.cbs.contest.bean.fb.ext.RecordResponse;
import com.lifeix.cbs.contest.dao.fb.*;
import com.lifeix.cbs.contest.dto.fb.*;
import com.lifeix.cbs.contest.dto.odds.OddsOp;
import com.lifeix.cbs.contest.im.LifeixImApiUtil;
import com.lifeix.cbs.contest.impl.redis.RedisBetCountHandler;
import com.lifeix.cbs.contest.service.spark.channel.ContestChannelDubbo;
import com.lifeix.cbs.contest.service.spark.contest.FbContestDubbo;
import com.lifeix.cbs.contest.util.ContestFilterUtil;
import com.lifeix.cbs.contest.util.transform.ContestTransformUtil;
import com.lifeix.exception.service.L99IllegalParamsException;
import lifeix.framwork.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("fbContestDubbo")
public class FbContestDubboImpl implements FbContestDubbo {

	protected static Logger LOG = LoggerFactory.getLogger(FbContestDubboImpl.class);

    @Autowired
    private FbContestDao fbContestDao;

    @Autowired
    private FbContestExtDao fbContestExtDao;

    @Autowired
    private FbTeamDao fbTeamDao;

    @Autowired
    private FbLiveWordsDao fbLiveWordsDao;

    @Autowired
    private FbContestRecordDao fbContestRecordDao;

    @Autowired
    private ContestChannelDubbo contestChannelDubbo;

    @Autowired
    private RedisBetCountHandler redisBetCountHandler;

    @Autowired
    private FbOddsOpDao fbOpDao;

    @Override
    public Map<Long, ContestResponse> findContestMapByIds(List<Long> ids) {
	Map<Long, FbContest> contestMap = fbContestDao.findFbContestMapByIds(ids);
	Map<Long, ContestResponse> respMap = new HashMap<Long, ContestResponse>();
	if (contestMap == null || contestMap.size() == 0)
	    return respMap;
	List<Long> teamIds = new ArrayList<Long>();
	Set<Long> keySet = contestMap.keySet();
	List<String> contestIds = new ArrayList<String>();
	for (Long key : keySet) {
	    FbContest contest = contestMap.get(key);
	    teamIds.add(contest.getHomeTeam());
	    teamIds.add(contest.getAwayTeam());
	    contestIds.add(contest.getContestId().toString());
	}
	Map<Long, FbTeam> teams = fbTeamDao.findFbTeamMapByIds(teamIds);
	// Map<Long, Integer> countMap =
	// redisBetCountHandler.findContestsCount(ContestType.FOOTBALL,
	// contestIds);
	for (Long key : keySet) {
	    FbContest contest = contestMap.get(key);
	    // 赛事下单数量
	    // int betCount = countMap.get(contest.getContestId());
	    int[] betCount = redisBetCountHandler.findContestBetRatio(ContestType.FOOTBALL, contest.getContestId());
	    ContestResponse cr = ContestTransformUtil.transformFbContest(contest, teams.get(contest.getHomeTeam()),
		    teams.get(contest.getAwayTeam()), betCount, true);
	    respMap.put(key, cr);
	}
	return respMap;
    }

    /**
     * 获取超时未结算的比赛列表(超过开场时间3小时)
     * 
     * @param limit
     * @return
     */
    @Override
    public List<ContestResponse> findTimeoutContest(Long contestId) {
	List<FbContest> timeouts = fbContestDao.findTimeoutContest(contestId, 20);
	List<ContestResponse> contestReponses = new ArrayList<ContestResponse>();
	for (FbContest contest : timeouts) {
	    contestReponses.add(ContestTransformUtil.transformFbContest(contest, null, null, new int[] { 0, 0 }, true));
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
	FbContest contest = new FbContest();
	contest.setContestId(contestId);
	contest.setHomeScores(homeScores);
	contest.setAwayScores(awayScores);
	contest.setStatus(status);
	return fbContestDao.updateChanges(contest);
    }

    /**
     * 批量保存赛事
     * 
     * @param list
     * @return
     */
    @Override
    public boolean saveContest(List<ContestResponse> list) {
	List<FbContest> contestList = new ArrayList<FbContest>(list.size());
	for (ContestResponse resp : list) {
	    FbContest contest = new FbContest();
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
	    contest.setOpenTime(CbsTimeUtils.getDateByUtcFormattedDate(resp.getOpen_time()));
	    contest.setStatus(resp.getStatus());
	    contest.setOddsType(resp.getOdds_type());
	    contest.setBelongFive(resp.isBelong_five());
	    contest.setLevel(resp.getLevel());
	    contest.setLongbi(resp.isLongbi());
	    contest.setExtFlag(resp.getExt_flag());
	    contestList.add(contest);
	}
	return fbContestDao.saveContest(contestList);
    }

    @Override
    public ContestListResponse findFbContestsForCbs(Date startTime, Date endTime) throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(startTime, endTime);

	List<ContestResponse> contestReponses = null;
	// 起始时间不能晚于结束时间
	if (startTime.after(endTime)) {
	    contestReponses = new ArrayList<ContestResponse>();
	} else {
	    List<Date> dates = CbsTimeUtils.rangeDates(startTime, endTime);
	    List<FbContest> contests = new ArrayList<FbContest>();
	    int tcount = 0;
	    for (Date date : dates) {
		if (tcount > 9) { // 最多只能返回9天的数据
		    break;
		}
		contests.addAll(fbContestDao.findFbContestsByRangeTime(date, false));
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
    public ContestListResponse findFbContestsV2ForCbs(String startTime, String endTime) throws L99IllegalParamsException {
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

	ContestListResponse response = findFbContestsForImportant(startDate, endDate, ifFuture, false);
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
    public ContestListResponse findFbContestsForImportant(Date startTime, Date endTime) throws L99IllegalParamsException {
	return findFbContestsForImportant(startTime, endTime, null, true);
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
    private ContestListResponse findFbContestsForImportant(Date startTime, Date endTime, Boolean ifFuture,
	    boolean recursionFlag) throws L99IllegalParamsException {

	ParamemeterAssert.assertDataNotNull(startTime, endTime);

	List<ContestResponse> contestReponses = null;
	// 起始时间不能晚于结束时间
	if (startTime.after(endTime)) {
	    contestReponses = new ArrayList<ContestResponse>();
	} else {
	    List<Date> dates = CbsTimeUtils.rangeDates(startTime, endTime);
	    List<FbContest> contests = new ArrayList<FbContest>();
	    int tcount = 0;
	    for (Date date : dates) {
		if (tcount > 9) { // 最多只能返回9天的数据
		    break;
		}
		contests.addAll(fbContestDao.findFbContestsForImportant(date));
		tcount++;
	    }

	    // ifFuture不为空继续查询
	    if (ifFuture != null) {
		// 递归锁只允许一次递归
		if (contests.size() == 0 && !recursionFlag) {
		    FbContest contest = null;
		    if (ifFuture) { // 未来数据
			contest = fbContestDao.findNextFbContestsByCup(endTime, null);
		    } else { // 历史数据
			contest = fbContestDao.findPrevFbContestsByCup(startTime, null);
		    }
		    if (contest != null) {
			if (ifFuture) { // 未来三天的数据
			    startTime = contest.getStartTime();
			    endTime = CbsTimeUtils.upgradeTime(startTime, Calendar.DAY_OF_YEAR, 3);
			} else { // 往前三天的数据
			    endTime = contest.getStartTime();
			    startTime = CbsTimeUtils.upgradeTime(endTime, Calendar.DAY_OF_YEAR, -3);
			}
			return findFbContestsForImportant(startTime, endTime, ifFuture, true);
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

    /**
     * 按照分类加载赛事列表
     * 
     * @param startTime
     * @param endTime
     * @param channelId
     * @param ifFuture
     *            标识赛事 true未来 | false历史
     * @return
     * @throws L99IllegalParamsException
     */
    @Override
    public ContestListResponse findFbContestsForImportantByChannelId(Date startTime, Date endTime, Long channelId,
	    boolean ifFuture, boolean recursionFlag) throws L99IllegalParamsException {

	ParamemeterAssert.assertDataNotNull(startTime, endTime);

	// 当channel为空时 返回所有赛事列表方法
	if (channelId == null) {
	    return findFbContestsForImportant(startTime, endTime, ifFuture, recursionFlag);
	}
	List<FbContest> contestRets = new ArrayList<FbContest>();

	// 起始时间不能晚于结束时间
	if (!startTime.after(endTime)) {

	    // 获取分类信息
	    ContestChannelResponse channel = contestChannelDubbo.viewChannel(channelId);

	    // channel是否符合足球赛事
	    if (channel != null && channel.getContest_type() == ContestType.FOOTBALL) {

		List<Long> cupIds = channel.getCup_id();

		// 根据时间获取赛事列表
		List<Date> dates = CbsTimeUtils.rangeDates(startTime, endTime);
		List<FbContest> contests = new ArrayList<FbContest>();
		int tcount = 0;
		for (Date date : dates) {
		    if (tcount > 9) { // 最多只能返回9天的数据
			break;
		    }
		    contests.addAll(fbContestDao.findFbContestsForImportant(date));
		    endTime = date;
		    tcount++;
		}

		// 根据channel的cupIds过滤赛事
		if (contests.size() > 0) {
		    if (cupIds != null && !cupIds.isEmpty()) {
			Set<Long> cupSets = new HashSet<Long>(cupIds);
			for (FbContest contest : contests) {
			    if (cupSets.contains(contest.getCupId())) {
				contestRets.add(contest);
			    }
			}
		    }
		}

		// 递归锁只允许一次递归
		if (contestRets.size() == 0 && !recursionFlag) {
		    FbContest contest = null;
		    if (ifFuture) { // 未来数据
			contest = fbContestDao.findNextFbContestsByCup(endTime, cupIds);
		    } else { // 历史数据
			contest = fbContestDao.findPrevFbContestsByCup(startTime, cupIds);
		    }
		    if (contest != null) {
			if (ifFuture) { // 未来3天的数据
			    startTime = contest.getStartTime();
			    endTime = CbsTimeUtils.upgradeTime(startTime, Calendar.DAY_OF_YEAR, 3);
			} else { // 往前三天的数据
			    endTime = contest.getStartTime();
			    startTime = CbsTimeUtils.upgradeTime(endTime, Calendar.DAY_OF_YEAR, -3);
			}
			return findFbContestsForImportantByChannelId(startTime, endTime, channelId, ifFuture, true);
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
     * 查赛事及扩展信息
     * 
     * @throws L99IllegalParamsException
     */
    @Override
    public ContestFullResponse findContestInfo(Long contestId) throws L99IllegalParamsException {

	ParamemeterAssert.assertDataNotNull(contestId);

	FbContest contest = fbContestDao.selectById(contestId);
	if (contest == null) {
	    return null;
	}

	List<Long> teamIds = new ArrayList<Long>();
	teamIds.add(contest.getHomeTeam());
	teamIds.add(contest.getAwayTeam());
	Map<Long, FbTeam> teams = fbTeamDao.findFbTeamMapByIds(teamIds);
	FbTeam homeTeam = teams.get(contest.getHomeTeam());
	FbTeam awayTeam = teams.get(contest.getAwayTeam());

	// 开赛前两天到赛后十五天才允许创建房间
	if (contest.getRoomId() == null && CbsTimeUtils.roomCreateRange(contest.getStartTime())) {
	    String homeName = homeTeam == null ? "" : homeTeam.getName();
	    String awayName = awayTeam == null ? "" : awayTeam.getName();
	    String roomName = String.format("%s VS %s ", homeName, awayName);
	    Long roomId = LifeixImApiUtil.getInstance().createImRoom(ContestType.FOOTBALL, contestId, roomName,
		    contest.getStartTime());
	    if (roomId > 0) {
		fbContestDao.updateRoom(contestId, roomId);
		contest.setRoomId(roomId);
	    }
	}

	// 赛事下单数量
	// int betCount =
	// redisBetCountHandler.findContestCount(ContestType.FOOTBALL,
	// contestId);
	int[] betCount = redisBetCountHandler.findContestBetRatio(ContestType.FOOTBALL, contest.getContestId());

	// 赛事基础信息
	ContestResponse contestResp = ContestTransformUtil.transformFbContest(contest, homeTeam, awayTeam, betCount, true);

	// 赛事扩展信息
	FbContestExt contestExt = fbContestExtDao.selectById(contestId);
	ContestExtResponse contestExtResp = null;
	if (contestExt != null) {
	    contestExtResp = ContestTransformUtil.transformFbContestExt(contestExt);
	}

	// 赛事文字直播
	List<FbLiveWordsResponse> live_wordsResp = new LinkedList<FbLiveWordsResponse>();
	List<FbLiveWords> fbLiveWords = fbLiveWordsDao.selectByContestId(contestId, null);
	for (FbLiveWords fbLiveWord : fbLiveWords) {
	    live_wordsResp.add(ContestTransformUtil.transformFbLiveWords(fbLiveWord));
	}

	// 赛事交手记录
	List<RecordResponse> recordResponse = new ArrayList<RecordResponse>();
	List<FbContestRecord> fbRecords = fbContestRecordDao.selectHeadToHeadRecord(contest.getHomeTeam(),
	        contest.getAwayTeam(), 10);
	for (FbContestRecord fbRecord : fbRecords) {
	    recordResponse.add(ContestTransformUtil.transformFbContestRecord(fbRecord));
	}

	ContestFullResponse response = new ContestFullResponse();
	// 赛事赔率获取
	OddsOp op = fbOpDao.selectByContest(contestId);
	if (op != null) {
	    response.setHome_roi(op.getHomeRoi());
	    response.setAway_roi(op.getAwayRoi());
	}
	response.setContest(contestResp);
	response.setContest_ext(contestExtResp);
	response.setLive_words(live_wordsResp);
	response.setRecords(recordResponse);
	return response;
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

	List<ContestResponse> fb_contests = new ArrayList<ContestResponse>();
	for (FbContest contest : contests) {
	    // 赛事下单数量
	    int[] betCount = redisBetCountHandler.findContestBetRatio(ContestType.FOOTBALL, contest.getContestId());
	    fb_contests.add(ContestTransformUtil.transformFbContest(contest, teams.get(contest.getHomeTeam()),
		    teams.get(contest.getAwayTeam()), betCount, true));
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

    @Override
    public List<Long> findFbContestIngNum() {
	// long nowTime = System.currentTimeMillis()/1000-5*60; //提前5分钟
	DateFormat dateTimeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String beginDate = dateTimeformat.format(new Date().getTime() - 300000);
	List<Long> ids = fbContestDao.findFbContestIngNum(beginDate);
	return ids;
    }

    @Override
    public boolean updateContestRoom(Long contestId, Long roomId) {
	return fbContestDao.updateRoom(contestId, roomId);
    }

    /**
     * 更新比赛最终结果
     * 
     * @param respList
     * @return
     */
    @Override
    public boolean updateFinalResults(List<ContestResponse> respList) {
	if (respList == null || respList.isEmpty())
	    return false;
	List<FbContest> list = new ArrayList<FbContest>(respList.size());
	for (ContestResponse resp : respList) {
	    FbContest contest = new FbContest();
	    contest.setContestId(resp.getContest_id());
	    if (resp.getFinal_result() != null) {
		contest.setFinalResult(JsonUtils.toJsonString(resp.getFinal_result()));
	    }
	    list.add(contest);
	}
	return fbContestDao.updateFinalResults(list);
    }


}
