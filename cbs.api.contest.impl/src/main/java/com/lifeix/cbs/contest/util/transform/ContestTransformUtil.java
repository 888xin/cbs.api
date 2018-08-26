package com.lifeix.cbs.contest.util.transform;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import lifeix.framwork.util.JsonUtils;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.api.common.util.CommerceMath;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestStatu;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestStatu_BB;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestType;
import com.lifeix.cbs.api.common.util.ContestConstants.TeamDiffer;
import com.lifeix.cbs.contest.bean.bb.BbContestExtResponse;
import com.lifeix.cbs.contest.bean.bb.BbLiveWordsResponse;
import com.lifeix.cbs.contest.bean.bb.BbPlayerStatisticsResponse;
import com.lifeix.cbs.contest.bean.bb.BbTeamStatisticsResponse;
import com.lifeix.cbs.contest.bean.bb.ext.BbPlayerResponse;
import com.lifeix.cbs.contest.bean.bb.ext.BbRefereeResponse;
import com.lifeix.cbs.contest.bean.bb.ext.BbScoreResponse;
import com.lifeix.cbs.contest.bean.bb.ext.BbVenueResponse;
import com.lifeix.cbs.contest.bean.contest.BetCountRatioResponse;
import com.lifeix.cbs.contest.bean.contest.ContestAdResponse;
import com.lifeix.cbs.contest.bean.contest.ScoreModuleResponse;
import com.lifeix.cbs.contest.bean.fb.ContestExtResponse;
import com.lifeix.cbs.contest.bean.fb.ContestResponse;
import com.lifeix.cbs.contest.bean.fb.ContestResultResponse;
import com.lifeix.cbs.contest.bean.fb.CupResponse;
import com.lifeix.cbs.contest.bean.fb.FbLiveWordsResponse;
import com.lifeix.cbs.contest.bean.fb.TeamResponse;
import com.lifeix.cbs.contest.bean.fb.ext.EventResponse;
import com.lifeix.cbs.contest.bean.fb.ext.PlayerResponse;
import com.lifeix.cbs.contest.bean.fb.ext.RecordResponse;
import com.lifeix.cbs.contest.bean.fb.ext.RefereeResponse;
import com.lifeix.cbs.contest.bean.fb.ext.ScoreResponse;
import com.lifeix.cbs.contest.bean.fb.ext.StatisticsResponse;
import com.lifeix.cbs.contest.bean.fb.ext.TeamExtResponse;
import com.lifeix.cbs.contest.bean.fb.ext.VenueResponse;
import com.lifeix.cbs.contest.bean.robot.BetRobotResponse;
import com.lifeix.cbs.contest.bean.settle.CbsSettleResponse;
import com.lifeix.cbs.contest.dto.bb.BbContest;
import com.lifeix.cbs.contest.dto.bb.BbContestExt;
import com.lifeix.cbs.contest.dto.bb.BbContestRecord;
import com.lifeix.cbs.contest.dto.bb.BbCup;
import com.lifeix.cbs.contest.dto.bb.BbLiveWords;
import com.lifeix.cbs.contest.dto.bb.BbPlayer;
import com.lifeix.cbs.contest.dto.bb.BbPlayerStatistics;
import com.lifeix.cbs.contest.dto.bb.BbTeam;
import com.lifeix.cbs.contest.dto.bb.BbTeamStatistics;
import com.lifeix.cbs.contest.dto.contest.ContestAd;
import com.lifeix.cbs.contest.dto.contest.ScoreModule;
import com.lifeix.cbs.contest.dto.fb.FbContest;
import com.lifeix.cbs.contest.dto.fb.FbContestExt;
import com.lifeix.cbs.contest.dto.fb.FbContestRecord;
import com.lifeix.cbs.contest.dto.fb.FbCup;
import com.lifeix.cbs.contest.dto.fb.FbLiveWords;
import com.lifeix.cbs.contest.dto.fb.FbStatistics;
import com.lifeix.cbs.contest.dto.fb.FbTeam;
import com.lifeix.cbs.contest.dto.robot.BetRobot;
import com.lifeix.cbs.contest.dto.settle.CbsSettle;

/**
 * 赛事DTO 转换 VO
 * 
 * @author lifeix-sz
 * 
 */
public class ContestTransformUtil {

    /**
     * 足球:比赛内容dto转换vo
     * 
     * @return
     */
    public static ContestResponse transformFbContest(FbContest contest, FbTeam homeTeam, FbTeam awayTeam, int[] betCount,
	    boolean simpleFlag) {
	ContestResponse resp = null;
	if (contest != null) {
	    resp = new ContestResponse();
	    resp.setContest_id(contest.getContestId());
	    resp.setCup_id(contest.getCupId());
	    resp.setCup_name(contest.getCupName());
	    resp.setColor(contest.getColor());
	    resp.setHome_team(contest.getHomeTeam());
	    resp.setHome_scores(contest.getHomeScores());
	    resp.setHome_rank(contest.getHomeRank());
	    resp.setAway_team(contest.getAwayTeam());
	    resp.setAway_scores(contest.getAwayScores());
	    resp.setAway_rank(contest.getAwayRank());
	    resp.setTarget_id(contest.getTargetId());
	    resp.setLevel(contest.getLevel());
	    resp.setSettle(contest.getSettle());
	    resp.setOdds_type(contest.getOddsType());
	    resp.setBelong_five(contest.getBelongFive());
	    resp.setH_t(transformFbTeam(homeTeam, true));
	    resp.setA_t(transformFbTeam(awayTeam, true));
	    if (contest.getStatus() == ContestStatu.NOTOPEN && contest.getStartTime().before(new Date())) {
		// 赛事未开始但是比赛开始时间已超过当前系统时间 暂定为上半场
		resp.setStatus(ContestStatu.HALF_PREV);
	    } else {
		resp.setStatus(contest.getStatus());
	    }

	    resp.setContest_type(ContestType.FOOTBALL);

	    String startTime = CbsTimeUtils.getUtcTimeForDate(contest.getStartTime());
	    resp.setStart_time(startTime);
	    resp.setLock_flag(contest.getLockFlag());
	    if (contest.getExtFlag() != null) {
		resp.setExt_flag(contest.getExtFlag());
	    }
	    resp.setLongbi(contest.isLongbi());
	    resp.setRoom_id(contest.getRoomId());
	    // resp.setInit_count(contest.getBetCount());
	    // add by lhx on 16-06-03 主客队下注人数（管理后台添加）
	    String betRatio = contest.getBetRatio();
	    if (StringUtils.isNotBlank(betRatio)) {
		BetCountRatioResponse betCountRatioResponse = new Gson().fromJson(betRatio, BetCountRatioResponse.class);
		resp.setHome_count(betCountRatioResponse.getHome_count());
		resp.setAway_count(betCountRatioResponse.getAway_count());
		resp.setInit_count(betCountRatioResponse.getHome_count() + betCountRatioResponse.getAway_count());
	    }

	    // add by lhx on 16-06-16
	    // 该球赛主客队支持率
	    resp.setHome_count(resp.getHome_count() + betCount[0]);
	    resp.setAway_count(resp.getAway_count() + betCount[1]);
	    resp.setBet_count(resp.getHome_count() + resp.getAway_count());

	    double homeCount = resp.getHome_count();
	    double awayCount = resp.getAway_count();
	    double total = homeCount + awayCount;
	    if (total > 0) {
		// 支持率 四舍五入 保留小数点两位
		double homeratio = 100 * (homeCount / total);
		BigDecimal bd = new BigDecimal(homeratio);
		homeratio = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		resp.setHome_ratio(homeratio);
		resp.setAway_ratio(CommerceMath.sub(100, homeratio));
	    }

	    if (!simpleFlag) {
		resp.setCreate_time(CbsTimeUtils.getUtcTimeForDate(contest.getCreateTime()));
		resp.setOpen_time(CbsTimeUtils.getUtcTimeForDate(contest.getOpenTime()));
	    }
	    if (StringUtils.isNotBlank(contest.getFinalResult())) {
		resp.setFinal_result(JsonUtils.jsonString2Bean(contest.getFinalResult(), ContestResultResponse.class));
	    }
	}
	return resp;
    }

    /**
     * 足球:球队dto转换vo
     * 
     * @param team
     * @param simpleFlag
     *            true 简单数据
     * @return
     */
    public static TeamResponse transformFbTeam(FbTeam team, boolean simpleFlag) {
	TeamResponse resp = null;
	if (team != null) {
	    resp = new TeamResponse();
	    resp.setT_id(team.getFtId());
	    resp.setName(team.getName());
	    resp.setLogo(team.getLogo());
	    if (!simpleFlag) {
		resp.setTarget_id(team.getTargetId());
		resp.setCup_id(team.getCupId());
		resp.setName_en(team.getNameEN());
		resp.setStatus(team.getStatus());
		resp.setCountry_id(team.getCountryId());
	    }
	}
	return resp;
    }

    /**
     * 篮球： 比赛内容dto转换vo
     * 
     * @return
     */
    public static ContestResponse transformBbContest(BbContest contest, BbTeam homeTeam, BbTeam awayTeam, int[] betCount,
	    boolean simpleFlag) {
	ContestResponse resp = null;
	if (contest != null) {
	    resp = new ContestResponse();
	    resp.setContest_id(contest.getContestId());
	    resp.setCup_id(contest.getCupId());
	    resp.setCup_name(contest.getCupName());
	    resp.setColor(contest.getColor());
	    resp.setHome_team(contest.getHomeTeam());
	    resp.setHome_scores(contest.getHomeScores());
	    resp.setHome_rank(contest.getHomeRank());
	    resp.setAway_team(contest.getAwayTeam());
	    resp.setAway_scores(contest.getAwayScores());
	    resp.setAway_rank(contest.getAwayRank());
	    resp.setTarget_id(contest.getTargetId());
	    resp.setLevel(contest.getLevel());
	    resp.setSettle(contest.getSettle());
	    resp.setOdds_type(contest.getOddsType());
	    resp.setBelong_five(contest.getBelongFive());
	    resp.setContest_type(ContestType.BASKETBALL);
	    resp.setH_t(transformBbTeam(homeTeam, true));
	    resp.setA_t(transformBbTeam(awayTeam, true));
	    if (contest.getStatus() == ContestStatu_BB.NOTOPEN && contest.getStartTime().before(new Date())) {
		// 赛事未开始但是比赛开始时间已超过当前系统时间 暂定为第一节
		resp.setStatus(ContestStatu_BB.ONE);
	    } else {
		resp.setStatus(contest.getStatus());
	    }
	    resp.setStatus(contest.getStatus());
	    resp.setRemain_time(contest.getRemainTime());
	    resp.setStart_time(CbsTimeUtils.getUtcTimeForDate(contest.getStartTime()));
	    resp.setLock_flag(contest.getLockFlag());
	    resp.setLongbi(contest.isLongbi());
	    if (contest.getExtFlag() != null) {
		resp.setExt_flag(contest.getExtFlag());
	    }
	    resp.setRoom_id(contest.getRoomId());
	    // resp.setInit_count(contest.getBetCount());
	    // add by lhx on 16-06-03 主客队下注人数（管理后台添加）
	    String betRatio = contest.getBetRatio();
	    if (StringUtils.isNotBlank(betRatio)) {
		BetCountRatioResponse betCountRatioResponse = new Gson().fromJson(betRatio, BetCountRatioResponse.class);
		resp.setHome_count(betCountRatioResponse.getHome_count());
		resp.setAway_count(betCountRatioResponse.getAway_count());
		resp.setInit_count(betCountRatioResponse.getHome_count() + betCountRatioResponse.getAway_count());
	    }

	    // add by lhx on 16-06-16
	    // 该球赛主客队支持率
	    resp.setHome_count(resp.getHome_count() + betCount[0]);
	    resp.setAway_count(resp.getAway_count() + betCount[1]);
	    resp.setBet_count(resp.getHome_count() + resp.getAway_count());

	    double homeCount = resp.getHome_count();
	    double awayCount = resp.getAway_count();
	    double total = homeCount + awayCount;
	    if (total > 0) {
		// 支持率 四舍五入 保留小数点两位
		double homeratio = 100 * (homeCount / total);
		BigDecimal bd = new BigDecimal(homeratio);
		homeratio = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		resp.setHome_ratio(homeratio);
		resp.setAway_ratio(CommerceMath.sub(100, homeratio));
	    }

	    if (!simpleFlag) {
		resp.setCreate_time(CbsTimeUtils.getUtcTimeForDate(contest.getCreateTime()));
	    }
	}
	return resp;
    }

    /**
     * 篮球：球队dto转换vo
     * 
     * @param team
     * @param simpleFlag
     *            true 简单数据
     * @return
     */
    public static TeamResponse transformBbTeam(BbTeam team, boolean simpleFlag) {
	TeamResponse resp = null;
	if (team != null) {
	    resp = new TeamResponse();
	    resp.setT_id(team.getBtId());
	    resp.setName(team.getName());
	    resp.setLogo(team.getLogo());
	    if (!simpleFlag) {
		resp.setTarget_id(team.getTargetId());
		resp.setCup_id(team.getCupId());
		resp.setName_en(team.getNameEN());
		resp.setStatus(team.getStatus());
		resp.setCountry_id(team.getCountryId());
	    }
	}
	return resp;
    }

    /**
     * 转换结算任务
     * 
     * @param settle
     * @return
     */
    public static CbsSettleResponse transformCbsSettle(CbsSettle settle) {
	CbsSettleResponse resp = null;
	if (settle != null) {
	    resp = new CbsSettleResponse();
	    resp.setSettle_id(settle.getSettleId());
	    resp.setType(settle.getType());
	    resp.setContest_id(settle.getContestId());
	    resp.setClose_flag(settle.getCloseFlag());
	    resp.setCreate_time(CbsTimeUtils.getUtcTimeForDate(settle.getCreateTime()));
	    resp.setSettle_time(CbsTimeUtils.getUtcTimeForDate(settle.getSettleTime()));
	}
	return resp;
    }

    /**
     * 转换足球联赛
     * 
     * @param cup
     * @return
     */
    public static CupResponse transformFbCup(FbCup cup) {
	CupResponse resp = null;
	if (cup != null) {
	    resp = new CupResponse();
	    resp.setColor(cup.getColor());
	    resp.setCountry(cup.getCountry());
	    resp.setCountry_id(cup.getCountryId());
	    resp.setCup_id(cup.getCupId());
	    resp.setLevel(cup.getLevel());
	    resp.setName(cup.getName());
	    resp.setName_en(cup.getNameEN());
	    resp.setName_full(cup.getNameFull());
	    resp.setTarget_id(cup.getTargetId());
	    resp.setType(cup.getType());
	}
	return resp;
    }

    /**
     * 转换篮球联赛
     * 
     * @param cup
     * @return
     */
    public static CupResponse transformBbCup(BbCup cup) {
	CupResponse resp = null;
	if (cup != null) {
	    resp = new CupResponse();
	    resp.setColor(cup.getColor());
	    resp.setCountry(cup.getCountry());
	    resp.setCountry_id(cup.getCountryId());
	    resp.setCup_id(cup.getCupId());
	    resp.setLevel(cup.getLevel());
	    resp.setName(cup.getName());
	    resp.setName_en(cup.getNameEN());
	    resp.setName_full(cup.getNameFull());
	    resp.setTarget_id(cup.getTargetId());
	    resp.setType(cup.getType());
	}
	return resp;
    }

    /**
     * 转换机器人
     * 
     * @param robot
     * @return
     */
    public static BetRobotResponse transformRobot(BetRobot robot) {
	BetRobotResponse resp = null;
	if (robot != null) {
	    resp = new BetRobotResponse();
	    resp.setUser_id(robot.getUserId());
	    resp.setUser_info(robot.getUserInfo());
	    resp.setFb_odds(robot.getFbOdds());
	    resp.setBb_odds(robot.getBbOdds());
	    resp.setSetting(robot.getSetting());
	    resp.setHistory(robot.getHistory());
	    resp.setBet_time(robot.getBetTime());
	    resp.setGame_flag(robot.isGameFlag());
	    resp.setGame_time(CbsTimeUtils.getUtcTimeForDate(robot.getGameTime()));
	    resp.setPk_flag(robot.isPkFlag());
	    resp.setCreate_time(CbsTimeUtils.getUtcTimeForDate(robot.getCreateTime()));
	    resp.setUpdate_time(CbsTimeUtils.getUtcTimeForDate(robot.getUpdateTime()));
	    resp.setClose_flag(robot.isCloseFlag());
	    resp.setCall_count(robot.getCallCount());
	}
	return resp;
    }

    /**
     * 足球转换文字直播
     * 
     * @param fbLiveWords
     * @return
     */
    public static FbLiveWordsResponse transformFbLiveWords(FbLiveWords fbLiveWords) {
	FbLiveWordsResponse resp = null;
	if (fbLiveWords != null) {
	    resp = new FbLiveWordsResponse();
	    resp.setInjury_time(fbLiveWords.getInjuryTime());
	    resp.setText_content(fbLiveWords.getTextContent());
	    resp.setTime(fbLiveWords.getTime());
	    resp.setType(fbLiveWords.getType());
	    resp.setPhrase_id(fbLiveWords.getPhraseId());
	}
	return resp;
    }

    /**
     * 篮球转换文字直播
     * 
     * @return
     */
    public static BbLiveWordsResponse transformBbLiveWords(BbLiveWords bbLiveWords) {
	BbLiveWordsResponse resp = null;
	if (bbLiveWords != null) {
	    resp = new BbLiveWordsResponse();
	    resp.setAway_scores(bbLiveWords.getAwayScores());
	    resp.setClock(bbLiveWords.getClock());
	    resp.setContest_id(bbLiveWords.getContestId());
	    resp.setEvent_type(bbLiveWords.getEventType());
	    resp.setHome_scores(bbLiveWords.getHomeScores());
	    resp.setLocation(bbLiveWords.getLocation());
	    resp.setPeriod_number(bbLiveWords.getPeriodNumber());
	    resp.setPeriod_type(bbLiveWords.getPeriodType());
	    resp.setPhrase_id(bbLiveWords.getPhraseId());
	    resp.setTeam(bbLiveWords.getTeam());
	    resp.setText_content(bbLiveWords.getTextContent());
	    resp.setSequence(bbLiveWords.getSequence());
	}
	return resp;
    }

    /**
     * 转换足球赛事扩展信息
     * 
     * @param contestExt
     * @return
     */
    public static ContestExtResponse transformFbContestExt(FbContestExt contestExt) {
	ContestExtResponse resp = null;
	if (contestExt != null) {
	    try {
		resp = new ContestExtResponse();
		resp.setStatus(contestExt.getStatus());
		if (StringUtils.isNotBlank(contestExt.getHomeTeamExt())) {
		    resp.setH_t_ext(transformContestExtTeamExt(contestExt.getHomeTeamExt()));
		}
		if (StringUtils.isNotBlank(contestExt.getAwayTeamExt())) {
		    resp.setA_t_ext(transformContestExtTeamExt(contestExt.getAwayTeamExt()));
		}
		if (StringUtils.isNotBlank(contestExt.getLineups())) {
		    List<PlayerResponse> playerList = transformContestExtLineups(contestExt.getLineups());
		    if (playerList != null && playerList.size() > 0) {
			List<PlayerResponse> homeLineups = new ArrayList<PlayerResponse>();
			List<PlayerResponse> awayLineups = new ArrayList<PlayerResponse>();
			for (PlayerResponse playerResp : playerList) {
			    if (playerResp.getTeam() == TeamDiffer.HOME) {
				homeLineups.add(playerResp);
			    } else if (playerResp.getTeam() == TeamDiffer.AWAY) {
				awayLineups.add(playerResp);
			    }
			}
			if (homeLineups.size() > 0) {
			    TeamExtResponse teamExtResp = resp.getH_t_ext();
			    if (teamExtResp == null) {
				teamExtResp = new TeamExtResponse();
				resp.setH_t_ext(teamExtResp);
			    }
			    teamExtResp.setLineups(homeLineups);
			}
			if (awayLineups.size() > 0) {
			    TeamExtResponse teamExtResp = resp.getA_t_ext();
			    if (teamExtResp == null) {
				teamExtResp = new TeamExtResponse();
				resp.setA_t_ext(teamExtResp);
			    }
			    teamExtResp.setLineups(awayLineups);
			}
		    }
		}
		if (StringUtils.isNotBlank(contestExt.getScores())) {
		    resp.setScores(transformContestExtScores(contestExt.getScores()));
		}
		if (StringUtils.isNotBlank(contestExt.getReferee())) {
		    resp.setReferee(transformContestExtReferee(contestExt.getReferee()));
		}
		if (StringUtils.isNotBlank(contestExt.getVenue())) {
		    resp.setVenue(transformContestExtVenue(contestExt.getVenue()));
		}
		List<EventResponse> eventList = new ArrayList<EventResponse>();
		List<EventResponse> specialEventList = new ArrayList<EventResponse>();
		List<EventResponse> normalEventList = new ArrayList<EventResponse>();
		if (StringUtils.isNotBlank(contestExt.getGoals())) {
		    List<EventResponse> goalsList = transformContestExtEvents(contestExt.getGoals());
		    if (goalsList != null && goalsList.size() > 0) {
			for (EventResponse eventResp : goalsList) {
			    if (eventResp.getTime() == 0) {
				specialEventList.add(eventResp);
			    } else {
				normalEventList.add(eventResp);
			    }
			}
		    }
		}
		if (StringUtils.isNotBlank(contestExt.getCards())) {
		    List<EventResponse> cardsList = transformContestExtEvents(contestExt.getCards());
		    if (cardsList != null && cardsList.size() > 0) {
			for (EventResponse eventResp : cardsList) {
			    if (eventResp.getTime() == 0) {
				specialEventList.add(eventResp);
			    } else {
				normalEventList.add(eventResp);
			    }
			}
		    }
		}
		if (StringUtils.isNotBlank(contestExt.getSubstitutions())) {
		    List<EventResponse> substitutionsList = transformContestExtEvents(contestExt.getSubstitutions());
		    if (substitutionsList != null && substitutionsList.size() > 0) {
			for (EventResponse eventResp : substitutionsList) {
			    if (eventResp.getTime() == 0) {
				specialEventList.add(eventResp);
			    } else {
				normalEventList.add(eventResp);
			    }
			}
		    }
		}
		Collections.sort(specialEventList, new Comparator<EventResponse>() {
		    @Override
		    public int compare(EventResponse o1, EventResponse o2) {
			if (o1.getTarget_id().equals(o2.getTarget_id()))
			    return o1.getType() - o2.getType();
			return o1.getTarget_id().compareTo(o2.getTarget_id());
		    }
		});
		eventList.addAll(specialEventList);
		Collections.sort(normalEventList, new Comparator<EventResponse>() {
		    @Override
		    public int compare(EventResponse o1, EventResponse o2) {
			if (o1.getTarget_id().equals(o2.getTarget_id()))
			    return o1.getType() - o2.getType();
			return o1.getTarget_id().compareTo(o2.getTarget_id());
		    }
		});
		eventList.addAll(normalEventList);
		if (StringUtils.isNotBlank(contestExt.getPenalties())) {
		    List<EventResponse> penaltiesList = transformContestExtEvents(contestExt.getPenalties());
		    if (penaltiesList != null && penaltiesList.size() > 0) {
			Collections.sort(penaltiesList, new Comparator<EventResponse>() {
			    @Override
			    public int compare(EventResponse o1, EventResponse o2) {
				return o1.getTarget_id().compareTo(o2.getTarget_id());
			    }
			});
			eventList.addAll(penaltiesList);
		    }
		}
		resp.setEvents(eventList);
	    } catch (Exception e) {
		e.printStackTrace();
		return null;
	    }
	}
	return resp;
    }

    /**
     * 转换球队扩展信息
     * 
     * @param teamExt
     * @return
     */
    public static TeamExtResponse transformContestExtTeamExt(String teamExt) {
	TeamExtResponse resp = null;
	if (StringUtils.isNotBlank(teamExt)) {
	    resp = JsonUtils.jsonString2Bean(teamExt, TeamExtResponse.class);
	}
	return resp;
    }

    /**
     * 转换阵容信息
     * 
     * @param lineups
     * @return
     */
    public static List<PlayerResponse> transformContestExtLineups(String lineups) {
	List<PlayerResponse> resp = null;
	if (StringUtils.isNotBlank(lineups)) {
	    resp = JsonUtils.jsonString2Bean(lineups, new TypeReference<List<PlayerResponse>>() {
	    });
	}
	return resp;
    }

    /**
     * 转换比分信息
     * 
     * @param scores
     * @return
     */
    public static List<ScoreResponse> transformContestExtScores(String scores) {
	List<ScoreResponse> resp = null;
	if (StringUtils.isNotBlank(scores)) {
	    resp = JsonUtils.jsonString2Bean(scores, new TypeReference<List<ScoreResponse>>() {
	    });
	}
	return resp;
    }

    /**
     * 转换比赛时间信息
     * 
     * @param events
     * @return
     */
    public static List<EventResponse> transformContestExtEvents(String events) {
	List<EventResponse> resp = null;
	if (StringUtils.isNotBlank(events)) {
	    resp = JsonUtils.jsonString2Bean(events, new TypeReference<List<EventResponse>>() {
	    });
	}
	return resp;
    }

    /**
     * 转换裁判信息
     * 
     * @param referee
     * @return
     */
    public static RefereeResponse transformContestExtReferee(String referee) {
	RefereeResponse resp = null;
	if (StringUtils.isNotBlank(referee)) {
	    resp = JsonUtils.jsonString2Bean(referee, RefereeResponse.class);
	}
	return resp;
    }

    /**
     * 转换比赛场地信息
     * 
     * @param venue
     * @return
     */
    public static VenueResponse transformContestExtVenue(String venue) {
	VenueResponse resp = null;
	if (StringUtils.isNotBlank(venue)) {
	    resp = JsonUtils.jsonString2Bean(venue, VenueResponse.class);
	}
	return resp;
    }

    /**
     * 转换足球实时技术统计信息
     * 
     * @param statistics
     * @return
     */
    public static StatisticsResponse transformStatistics(FbStatistics statistics) {
	StatisticsResponse resp = null;
	if (statistics != null) {
	    resp = new StatisticsResponse();
	    resp.setBall_possession(statistics.getBallPossession());
	    resp.setCorner_kicks(statistics.getCornerKicks());
	    resp.setFouls(statistics.getFouls());
	    resp.setFree_kicks(statistics.getFreeKicks());
	    resp.setGoal_kicks(statistics.getGoalKicks());
	    resp.setGoalkeeper_saves(statistics.getGoalkeeperSaves());
	    resp.setOffsides(statistics.getOffsides());
	    resp.setShots_off_goal(statistics.getShotsOffGoal());
	    resp.setShots_on_goal(statistics.getShotsOnGoal());
	    resp.setThrow_ins(statistics.getThrowIns());
	}
	return resp;
    }

    /**
     * 转换篮球交手记录
     * 
     * @param contestRecord
     * @return
     */
    public static RecordResponse transformBbContestRecord(BbContestRecord contestRecord) {
	RecordResponse resp = null;
	if (contestRecord != null) {
	    resp = new RecordResponse();
	    resp.setAway_scores(contestRecord.getAwayScores());
	    resp.setAway_team(contestRecord.getAwayTeam());
	    resp.setCup_id(contestRecord.getCupId());
	    resp.setCup_name(contestRecord.getCupName());
	    resp.setHome_scores(contestRecord.getHomeScores());
	    resp.setHome_team(contestRecord.getHomeTeam());
	    resp.setStart_time(CbsTimeUtils.getUtcTimeForDate(contestRecord.getStartTime()));
	    resp.setTarget_id(contestRecord.getTargetId());
	    resp.setWin_type(contestRecord.getWinType());
	    resp.setJc_win_type(contestRecord.getJcWinType());
	}
	return resp;
    }

    /**
     * 转换足球交手记录
     * 
     * @param contestRecord
     * @return
     */
    public static RecordResponse transformFbContestRecord(FbContestRecord contestRecord) {
	RecordResponse resp = null;
	if (contestRecord != null) {
	    resp = new RecordResponse();
	    resp.setAway_scores(contestRecord.getAwayScores());
	    resp.setAway_team(contestRecord.getAwayTeam());
	    resp.setCup_id(contestRecord.getCupId());
	    resp.setCup_name(contestRecord.getCupName());
	    resp.setHome_scores(contestRecord.getHomeScores());
	    resp.setHome_team(contestRecord.getHomeTeam());
	    resp.setStart_time(CbsTimeUtils.getUtcTimeForDate(contestRecord.getStartTime()));
	    resp.setTarget_id(contestRecord.getTargetId());
	    resp.setWin_type(contestRecord.getWinType());
	    resp.setJc_win_type(contestRecord.getJcWinType());
	}
	return resp;
    }

    /**
     * 带球队名返回记录
     * 
     * @param contestRecord
     * @param bbHomeTeam
     * @param bbAwayTeam
     * @return
     */
    public static RecordResponse transformBbContestRecord(BbContestRecord contestRecord, BbTeam bbHomeTeam, BbTeam bbAwayTeam) {
	RecordResponse resp = null;
	if (contestRecord != null) {
	    resp = new RecordResponse();
	    resp.setAway_scores(contestRecord.getAwayScores());
	    resp.setAway_team(contestRecord.getAwayTeam());
	    resp.setCup_id(contestRecord.getCupId());
	    resp.setCup_name(contestRecord.getCupName());
	    resp.setHome_scores(contestRecord.getHomeScores());
	    resp.setHome_team(contestRecord.getHomeTeam());
	    resp.setStart_time(CbsTimeUtils.getUtcTimeForDate(contestRecord.getStartTime()));
	    resp.setTarget_id(contestRecord.getTargetId());
	    resp.setWin_type(contestRecord.getWinType());
	    resp.setJc_win_type(contestRecord.getJcWinType());
	    resp.setAway_team_name(bbAwayTeam.getName());
	    resp.setHome_team_name(bbHomeTeam.getName());
	}
	return resp;
    }

    /**
     * 带球队名返回
     * 
     * @param contestRecord
     * @param fbTeam
     * @return
     */
    public static RecordResponse transformFbContestRecord(FbContestRecord contestRecord, FbTeam fbHomeTeam, FbTeam fbAwayTeam) {
	RecordResponse resp = null;
	if (contestRecord != null) {
	    resp = new RecordResponse();
	    resp.setAway_scores(contestRecord.getAwayScores());
	    resp.setAway_team(contestRecord.getAwayTeam());
	    resp.setCup_id(contestRecord.getCupId());
	    resp.setCup_name(contestRecord.getCupName());
	    resp.setHome_scores(contestRecord.getHomeScores());
	    resp.setHome_team(contestRecord.getHomeTeam());
	    resp.setStart_time(CbsTimeUtils.getUtcTimeForDate(contestRecord.getStartTime()));
	    resp.setTarget_id(contestRecord.getTargetId());
	    resp.setWin_type(contestRecord.getWinType());
	    resp.setJc_win_type(contestRecord.getJcWinType());
	    resp.setAway_team_name(fbAwayTeam.getName());
	    resp.setHome_team_name(fbHomeTeam.getName());
	}
	return resp;
    }

    /**
     * 转换篮球分节比分
     * 
     * @param scores
     * @return
     */
    public static List<BbScoreResponse> transformBbSegmentScore(String scores) {
	List<BbScoreResponse> resp = null;
	if (StringUtils.isNotBlank(scores)) {
	    resp = JsonUtils.jsonString2Bean(scores, new TypeReference<List<BbScoreResponse>>() {
	    });
	}
	return resp;
    }

    /**
     * 转换单个篮球球员信息
     * 
     * @param player
     * @return
     */
    public static BbPlayerResponse transformBbPlayer(BbPlayer player, TeamResponse teamResp) {
	BbPlayerResponse resp = null;
	if (player != null) {
	    resp = new BbPlayerResponse();
	    resp.setFirst_name(player.getFirstName());
	    resp.setLast_name(player.getLastName());
	    resp.setName(player.getName());
	    resp.setPlayer_id(player.getTargetId());
	    resp.setId(player.getId());
	    resp.setTeam_id(player.getTeamId());
	    resp.setTeam_info(teamResp);
	}
	return resp;
    }

    /**
     * 转换篮球阵容信息
     * 
     * @param lineups
     * @return
     */
    public static List<BbPlayerResponse> transformBbLineups(String lineups) {
	List<BbPlayerResponse> resp = null;
	if (StringUtils.isNotBlank(lineups)) {
	    resp = JsonUtils.jsonString2Bean(lineups, new TypeReference<List<BbPlayerResponse>>() {
	    });
	}
	return resp;
    }

    /**
     * 转换篮球裁判信息
     * 
     * @param referee
     * @return
     */
    public static List<BbRefereeResponse> transformBbReferee(String referee) {
	List<BbRefereeResponse> resp = null;
	if (StringUtils.isNotBlank(referee)) {
	    resp = JsonUtils.jsonString2Bean(referee, new TypeReference<List<BbRefereeResponse>>() {
	    });
	}
	return resp;
    }

    /**
     * 转换篮球比赛场地信息
     * 
     * @param venue
     * @return
     */
    public static BbVenueResponse transformBbVenue(String venue) {
	BbVenueResponse resp = null;
	if (StringUtils.isNotBlank(venue)) {
	    resp = JsonUtils.jsonString2Bean(venue, BbVenueResponse.class);
	}
	return resp;
    }

    /**
     * 转换篮球赛事扩展信息
     * 
     * @param contestExt
     * @return
     */
    public static BbContestExtResponse transformBbContestExt(BbContestExt contestExt) {
	BbContestExtResponse resp = null;
	if (contestExt != null) {
	    try {
		resp = new BbContestExtResponse();
		if (StringUtils.isNotBlank(contestExt.getLineups())) {
		    List<BbPlayerResponse> playerList = transformBbLineups(contestExt.getLineups());
		    if (playerList != null && playerList.size() > 0) {
			List<BbPlayerResponse> homeLineups = new ArrayList<BbPlayerResponse>();
			List<BbPlayerResponse> awayLineups = new ArrayList<BbPlayerResponse>();
			for (BbPlayerResponse playerResp : playerList) {
			    if (playerResp.getTeam() == TeamDiffer.HOME) {
				homeLineups.add(playerResp);
			    } else if (playerResp.getTeam() == TeamDiffer.AWAY) {
				awayLineups.add(playerResp);
			    }
			}
			resp.setHome_lineups(homeLineups);
			resp.setAway_lineups(awayLineups);
		    }
		}
		if (StringUtils.isNotBlank(contestExt.getReferee())) {
		    resp.setReferee(transformBbReferee(contestExt.getReferee()));
		}
		if (StringUtils.isNotBlank(contestExt.getVenue())) {
		    resp.setVenue(transformBbVenue(contestExt.getVenue()));
		}
	    } catch (Exception e) {
		e.printStackTrace();
		return null;
	    }
	}
	return resp;
    }

    /**
     * 转换篮球球队技术统计
     * 
     * @param statistics
     * @return
     */
    public static BbTeamStatisticsResponse transformBbTeamStatistics(BbTeamStatistics statistics) {
	BbTeamStatisticsResponse resp = null;
	if (statistics != null) {
	    resp = new BbTeamStatisticsResponse();
	    resp.setAssists(statistics.getAssists());
	    resp.setBlocks(statistics.getBlocks());
	    resp.setContest_id(statistics.getContestId());
	    resp.setDefensive_rebounds(statistics.getDefensiveRebounds());
	    resp.setFree_throw_att(statistics.getFreeThrowAtt());
	    resp.setFree_throw_made(statistics.getFreeThrowMade());
	    resp.setOffensive_rebounds(statistics.getOffensiveRebounds());
	    resp.setPersonal_fouls(statistics.getPersonalFouls());
	    if (StringUtils.isNotBlank(statistics.getScores())) {
		resp.setSeg_scores(transformBbSegmentScore(statistics.getScores()));
	    }
	    resp.setSteals(statistics.getSteals());
	    resp.setTech_fouls(statistics.getTechFouls());
	    resp.setThree_point_att(statistics.getThreePointAtt());
	    resp.setThree_point_made(statistics.getThreePointMade());
	    resp.setTurnovers(statistics.getTurnovers());
	    resp.setTwo_point_att(statistics.getTwoPointAtt());
	    resp.setTwo_point_made(statistics.getTwoPointMade());
	    resp.setTeam(statistics.getTeam());
	}
	return resp;
    }

    /**
     * 转换篮球球员技术统计
     * 
     * @param statistics
     * @return
     */
    public static BbPlayerStatisticsResponse transformBbPlayerStatistics(BbPlayerStatistics statistics) {
	BbPlayerStatisticsResponse resp = null;
	if (statistics != null) {
	    resp = new BbPlayerStatisticsResponse();
	    resp.setAssists(statistics.getAssists());
	    resp.setBlocks(statistics.getBlocks());
	    resp.setContest_id(statistics.getContestId());
	    resp.setDefensive_rebounds(statistics.getDefensiveRebounds());
	    resp.setFree_throw_att(statistics.getFreeThrowAtt());
	    resp.setFree_throw_made(statistics.getFreeThrowMade());
	    resp.setOffensive_rebounds(statistics.getOffensiveRebounds());
	    resp.setPersonal_fouls(statistics.getPersonalFouls());
	    resp.setPlay_time(statistics.getPlayTime());
	    resp.setPlayer_id(statistics.getPlayerId());
	    resp.setPoints(statistics.getPoints());
	    resp.setSteals(statistics.getSteals());
	    resp.setTech_fouls(statistics.getTechFouls());
	    resp.setThree_point_att(statistics.getThreePointAtt());
	    resp.setThree_point_made(statistics.getThreePointMade());
	    resp.setTurnovers(statistics.getTurnovers());
	    resp.setTwo_point_att(statistics.getTwoPointAtt());
	    resp.setTwo_point_made(statistics.getTwoPointMade());
	    resp.setTeam(statistics.getTeam());
	}
	return resp;
    }

    public static ScoreModuleResponse transformScoreModule(ScoreModule sm) {
	ScoreModuleResponse resp = null;
	if (sm != null) {
	    resp = new ScoreModuleResponse();
	    resp.setContest_type(sm.getContestType());
	    resp.setId(sm.getId());
	    resp.setModule_value(sm.getModuleValue());
	}
	return resp;
    }

    /**
     * 转换赛事广告
     * 
     * @param ContestAd
     * @return
     */
    public static ContestAdResponse transformContestAd(ContestAd bean) {
	ContestAdResponse resp = null;
	if (bean != null) {
	    resp = new ContestAdResponse();
	    resp.setContestId(bean.getContestId());
	    resp.setContestType(bean.getContestType());
	    resp.setCreateTime(CbsTimeUtils.getUtcTimeForDate(bean.getCreateTime()));
	    resp.setHideFlag(bean.getHideFlag());
	    resp.setId(bean.getId());
	    resp.setImages(bean.getImages());
	    resp.setText(bean.getText());
	    resp.setTitle(bean.getTitle());
	    resp.setUpdateTime(CbsTimeUtils.getUtcTimeForDate(bean.getUpdateTime()));

	    if (bean.getContestType() == ContestType.YAYA) {
		resp.setUrl_skip("cbs://yaya?contestId=" + bean.getContestId());
	    } else {
		resp.setUrl_skip("cbs://caicai?contestId=" + bean.getContestId() + "&contestType=" + bean.getContestType());
	    }
	}
	return resp;
    }
}
