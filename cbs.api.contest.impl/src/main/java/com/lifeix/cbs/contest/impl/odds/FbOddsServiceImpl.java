package com.lifeix.cbs.contest.impl.odds;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.bean.coupon.CouponUserListResponse;
import com.lifeix.cbs.api.bean.gold.GoldResponse;
import com.lifeix.cbs.api.common.exception.MsgCode;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.exception.MsgCode.ContestMsg;
import com.lifeix.cbs.api.common.util.BetConstants.BetStatus;
import com.lifeix.cbs.api.common.util.BetConstants.ContestContainOdds;
import com.lifeix.cbs.api.common.util.CommerceMath;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestStatu;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestType;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.common.util.PlayType;
import com.lifeix.cbs.api.service.coupon.CouponUserService;
import com.lifeix.cbs.api.service.money.MoneyService;
import com.lifeix.cbs.content.service.contest.ContestNewsService;
import com.lifeix.cbs.contest.bean.bet.BetJcListResponse;
import com.lifeix.cbs.contest.bean.bet.BetJcResponse;
import com.lifeix.cbs.contest.bean.bet.BetOddevenListResponse;
import com.lifeix.cbs.contest.bean.bet.BetOddevenResponse;
import com.lifeix.cbs.contest.bean.bet.BetOpListResponse;
import com.lifeix.cbs.contest.bean.bet.BetOpResponse;
import com.lifeix.cbs.contest.bean.bet.BetSizeListResponse;
import com.lifeix.cbs.contest.bean.bet.BetSizeResponse;
import com.lifeix.cbs.contest.bean.fb.ContestResponse;
import com.lifeix.cbs.contest.bean.odds.OddsDssResponse;
import com.lifeix.cbs.contest.bean.odds.OddsJcHistListResponse;
import com.lifeix.cbs.contest.bean.odds.OddsJcHistResponse;
import com.lifeix.cbs.contest.bean.odds.OddsJcListResponse;
import com.lifeix.cbs.contest.bean.odds.OddsJcResponse;
import com.lifeix.cbs.contest.bean.odds.OddsOpHistListResponse;
import com.lifeix.cbs.contest.bean.odds.OddsOpHistResponse;
import com.lifeix.cbs.contest.bean.odds.OddsOpListResponse;
import com.lifeix.cbs.contest.bean.odds.OddsOpResponse;
import com.lifeix.cbs.contest.bean.odds.OddsResponse;
import com.lifeix.cbs.contest.bean.odds.OddsSizeResponse;
import com.lifeix.cbs.contest.dao.fb.FbBetJcDao;
import com.lifeix.cbs.contest.dao.fb.FbBetOddevenDao;
import com.lifeix.cbs.contest.dao.fb.FbBetOpDao;
import com.lifeix.cbs.contest.dao.fb.FbBetSizeDao;
import com.lifeix.cbs.contest.dao.fb.FbContestDao;
import com.lifeix.cbs.contest.dao.fb.FbOddsJcDao;
import com.lifeix.cbs.contest.dao.fb.FbOddsJcHistDao;
import com.lifeix.cbs.contest.dao.fb.FbOddsOpDao;
import com.lifeix.cbs.contest.dao.fb.FbOddsOpHistDao;
import com.lifeix.cbs.contest.dao.fb.FbOddsSizeDao;
import com.lifeix.cbs.contest.dao.fb.FbTeamDao;
import com.lifeix.cbs.contest.dto.bet.BetJc;
import com.lifeix.cbs.contest.dto.bet.BetOddeven;
import com.lifeix.cbs.contest.dto.bet.BetOp;
import com.lifeix.cbs.contest.dto.bet.BetSize;
import com.lifeix.cbs.contest.dto.fb.FbContest;
import com.lifeix.cbs.contest.dto.fb.FbTeam;
import com.lifeix.cbs.contest.dto.odds.OddsJc;
import com.lifeix.cbs.contest.dto.odds.OddsJcHist;
import com.lifeix.cbs.contest.dto.odds.OddsOp;
import com.lifeix.cbs.contest.dto.odds.OddsOpHist;
import com.lifeix.cbs.contest.dto.odds.OddsSize;
import com.lifeix.cbs.contest.impl.ImplSupport;
import com.lifeix.cbs.contest.impl.redis.RedisBetCountHandler;
import com.lifeix.cbs.contest.service.cirle.FriendCircleService;
import com.lifeix.cbs.contest.service.odds.FbOddsService;
import com.lifeix.cbs.contest.util.ContestFilterUtil;
import com.lifeix.cbs.contest.util.transform.ContestTransformUtil;
import com.lifeix.cbs.contest.util.transform.FbOddsDssDateUtil;
import com.lifeix.cbs.contest.util.transform.OddsTransformUtil;
import com.lifeix.cbs.message.service.placard.PlacardTempletService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.user.beans.CustomResponse;
import com.lifeix.user.beans.ListResponse;

/**
 * 足球赔率逻辑
 * 
 * @author lifeix-sz
 */
@Service("fbOddsService")
public class FbOddsServiceImpl extends ImplSupport implements FbOddsService {

    @Autowired
    MoneyService moneyService;

    @Autowired
    CouponUserService couponUserService;

    @Autowired
    private FbContestDao fbContestDao;

    @Autowired
    private FbTeamDao fbTeamDao;

    @Autowired
    private FbOddsOpDao fbOpDao;

    @Autowired
    private FbOddsJcDao fbJcDao;

    @Autowired
    private FbOddsSizeDao fbOddsSizeDao;

    @Autowired
    private FbBetOpDao fbBetOpDao;

    @Autowired
    private FbBetJcDao fbBetJcDao;

    @Autowired
    private FbBetSizeDao fbBetSizeDao;

    @Autowired
    private FbBetOddevenDao fbBetOddevenDao;

    @Autowired
    private RedisBetCountHandler redisBetCountHandler;

    @Autowired
    private ContestNewsService contestNewsService;

    @Autowired
    private FbOddsJcHistDao fbOddsJcHistDao;

    @Autowired
    private FbOddsOpHistDao fbOddsOpHistDao;

    @Autowired
    private FriendCircleService friendCircleService;

    @Autowired
    private PlacardTempletService placardTempletService;

    @Override
    public OddsResponse findFbOddsResponse(Long contestId, Long userId, boolean hasContest, int oddsType, String client)
	    throws L99IllegalParamsException, L99IllegalDataException, JSONException {
	ParamemeterAssert.assertDataNotNull(contestId, oddsType);

	OddsResponse response = new OddsResponse();

	FbContest contest = fbContestDao.selectById(contestId);
	if (contest == null) {
	    throw new L99IllegalDataException(ContestMsg.CODE_CONTEST_NOT_EXIST, ContestMsg.KEY_CONTEST_NOT_EXIST);
	}

	// 赛事下单统计
	contest.setBetCount(redisBetCountHandler.findContestCount(ContestType.FOOTBALL, contestId));

	// 获取用户龙币 和 龙筹
	if (userId != null) {
	    GoldResponse gold = moneyService.viewUserMoney(userId, "");
	    response.setGold(CommerceMath.sub(gold.getBalance(), gold.getFrozen()));

	    CouponUserListResponse userCoupons = couponUserService.findUserBetCoupons(userId, contestId, contest.getCupId(),
		    ContestType.FOOTBALL);
	    response.setCoupons(userCoupons);
	}

	response.setStatus(contest.getStatus());

	// 赛事信息
	if (hasContest) {
	    List<Long> teamIds = new ArrayList<Long>();
	    teamIds.add(contest.getHomeTeam());
	    teamIds.add(contest.getAwayTeam());
	    Map<Long, FbTeam> teams = fbTeamDao.findFbTeamMapByIds(teamIds);

	    String remainTime = null;
	    if (contest.getStartTime().before(new Date())
		    && (contest.getStatus() == ContestStatu.HALF_PREV || contest.getStatus() == ContestStatu.HALF_NEXT || contest
		            .getStatus() == ContestStatu.OVERTIME)) {
		remainTime = calRemainTime(contest.getStatus(), contest.getStartTime());
	    }
	    // int betCount =
	    // redisBetCountHandler.findContestCount(ContestType.FOOTBALL,
	    // contest.getContestId());
	    int[] betCount = redisBetCountHandler.findContestBetRatio(ContestType.FOOTBALL, contest.getContestId());
	    ContestResponse contest_res = ContestTransformUtil.transformFbContest(contest, teams.get(contest.getHomeTeam()),
		    teams.get(contest.getAwayTeam()), betCount, false);
	    ContestFilterUtil.filterContestLogo(contest_res, true, client);
	    contest_res.setRemain_time(remainTime);

	    response.setContest(contest_res);
	}

	// 胜平负
	if ((oddsType & ContestContainOdds.FB_SPF) > 0 && (contest.getOddsType() & ContestContainOdds.FB_SPF) > 0) {

	    OddsOp op = fbOpDao.selectByContest(contestId);
	    response.setOp(OddsTransformUtil.transformOddsOp(op));

	    if (userId != null) {
		List<BetOp> ops = fbBetOpDao.findFbBetOp(contestId, userId, false, null, 3);
		if (ops.size() > 0) {
		    List<BetOpResponse> op_bets = new ArrayList<BetOpResponse>();
		    for (BetOp betOp : ops) {
			op_bets.add(OddsTransformUtil.transformBetOp(betOp));
		    }
		    BetOpListResponse betOps = new BetOpListResponse();
		    betOps.setOp_bets(op_bets);

		    response.setOp_bets(betOps);
		}
	    }
	}

	// 让球胜平负
	if ((oddsType & ContestContainOdds.FB_RQSPF) > 0 && (contest.getOddsType() & ContestContainOdds.FB_RQSPF) > 0) {

	    OddsJc jc = fbJcDao.selectByContest(contestId);
	    response.setJc(OddsTransformUtil.transformOddsJc(jc));

	    if (userId != null) {
		List<BetJc> jcs = fbBetJcDao.findFbBetJc(contestId, userId, false, null, 3);
		if (jcs.size() > 0) {
		    List<BetJcResponse> jc_bets = new ArrayList<BetJcResponse>();
		    for (BetJc betJc : jcs) {
			jc_bets.add(OddsTransformUtil.transformBetBd(betJc));
		    }
		    BetJcListResponse betJcs = new BetJcListResponse();
		    betJcs.setJc_bets(jc_bets);

		    response.setJc_bets(betJcs);
		}
	    }
	}

	// 大小球
	if ((oddsType & ContestContainOdds.FB_SIZE) > 0 && (contest.getOddsType() & ContestContainOdds.FB_SIZE) > 0) {

	    OddsSize oddsSize = fbOddsSizeDao.selectByContest(contestId);
	    response.setSize(OddsTransformUtil.transformOddsSize(oddsSize));

	    if (userId != null) {
		List<BetSize> betSizeList = fbBetSizeDao.findFbBetSize(contestId, userId, false, null, 2);
		if (betSizeList.size() > 0) {
		    List<BetSizeResponse> size_bets = new ArrayList<>();
		    for (BetSize betSize : betSizeList) {
			size_bets.add(OddsTransformUtil.transformBetSize(betSize));
		    }
		    BetSizeListResponse betSizeListResponse = new BetSizeListResponse();
		    betSizeListResponse.setSize_bets(size_bets);

		    response.setSize_bets(betSizeListResponse);
		}
	    }
	}

	// 单双数
	if ((oddsType & ContestContainOdds.FB_ODDEVEN) > 0 && (contest.getOddsType() & ContestContainOdds.FB_ODDEVEN) > 0) {

	    OddsDssResponse oddsDssResponse = FbOddsDssDateUtil.getInstance().getDssResponse();

	    response.setOddeven(oddsDssResponse);

	    if (userId != null) {
		List<BetOddeven> betOddevenList = fbBetOddevenDao.findFbBetOddeven(contestId, userId, false, null, 2);
		if (betOddevenList.size() > 0) {
		    List<BetOddevenResponse> oddeven_bets = new ArrayList<>();
		    for (BetOddeven betOddeven : betOddevenList) {
			oddeven_bets.add(OddsTransformUtil.transformBetOddeven(betOddeven));
		    }
		    BetOddevenListResponse betOddevenListResponse = new BetOddevenListResponse();
		    betOddevenListResponse.setOddeven_bets(oddeven_bets);

		    response.setOddeven_bets(betOddevenListResponse);
		}
	    }
	}

	// 获取下单人数信息
	Integer[] op_count = { 0, 0, 0 };// 主，平，客
	Integer[] jc_count = { 0, 0, 0 };// 主，平，客
	Integer[] size_count = { 0, 0 };// 大，小
	Integer[] oddeven_count = { 0, 0 };// 单，双
	Map<String, Object> betCount = redisBetCountHandler.getContestBet(ContestType.FOOTBALL, contestId);
	Object ophCount = betCount.get(String.format("%d,%d", PlayType.FB_SPF.getId(), BetStatus.HOME));// 主胜人数
	if (ophCount != null) {
	    op_count[0] = Integer.valueOf(ophCount.toString());
	}
	Object opdCount = betCount.get(String.format("%d,%d", PlayType.FB_SPF.getId(), BetStatus.DRAW));// 平局人数
	if (opdCount != null) {
	    op_count[1] = Integer.valueOf(opdCount.toString());
	}
	Object opaCount = betCount.get(String.format("%d,%d", PlayType.FB_SPF.getId(), BetStatus.AWAY));// 客胜人数
	if (opaCount != null) {
	    op_count[2] = Integer.valueOf(opaCount.toString());
	}
	Object jchCount = betCount.get(String.format("%d,%d", PlayType.FB_RQSPF.getId(), BetStatus.HOME));// 主胜人数
	if (jchCount != null) {
	    jc_count[0] = Integer.valueOf(jchCount.toString());
	}
	Object jcdCount = betCount.get(String.format("%d,%d", PlayType.FB_RQSPF.getId(), BetStatus.DRAW));// 平局人数
	if (jcdCount != null) {
	    jc_count[1] = Integer.valueOf(jcdCount.toString());
	}
	Object jcaCount = betCount.get(String.format("%d,%d", PlayType.FB_RQSPF.getId(), BetStatus.AWAY));// 客胜人数
	if (jcaCount != null) {
	    jc_count[2] = Integer.valueOf(jcaCount.toString());
	}
	Object sizehCount = betCount.get(String.format("%d,%d", PlayType.FB_SIZE.getId(), BetStatus.HOME));// 主胜人数
	if (sizehCount != null) {
	    size_count[0] = Integer.valueOf(sizehCount.toString());
	}
	Object sizeaCount = betCount.get(String.format("%d,%d", PlayType.FB_SIZE.getId(), BetStatus.AWAY));// 客胜人数
	if (sizeaCount != null) {
	    size_count[1] = Integer.valueOf(sizeaCount.toString());
	}
	Object oddevenhCount = betCount.get(String.format("%d,%d", PlayType.FB_ODDEVEN.getId(), BetStatus.HOME));// 单数人数
	if (oddevenhCount != null) {
	    oddeven_count[0] = Integer.valueOf(oddevenhCount.toString());
	}
	Object oddevenaCount = betCount.get(String.format("%d,%d", PlayType.FB_ODDEVEN.getId(), BetStatus.AWAY));// 双数人数
	if (oddevenaCount != null) {
	    oddeven_count[1] = Integer.valueOf(oddevenaCount.toString());
	}
	response.setOp_count(op_count);
	response.setJc_count(jc_count);
	response.setSize_count(size_count);
	response.setOddeven_count(oddeven_count);

	// add by lhx on 16-04-18
	// 该球赛是否有赛事分析
	boolean hasNews = contestNewsService.hasNews(contestId, ContestType.FOOTBALL);
	response.setHas_news(hasNews);

	// add by lhx on 16-05-10
	// 该球赛是否有下单理由
	boolean hasReason = friendCircleService.isHasReason(contestId, ContestType.FOOTBALL);
	response.setHas_reason(hasReason);

	// 查询赛事是否关联公告领取龙筹
	String placard = placardTempletService.findPlacardRelation(contest.getCupId(), contestId, ContestType.FOOTBALL);
	response.setPlacardId(placard);
	return response;
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
     * 查找足球胜平负赔率
     * 
     * @param startId
     * @param limit
     * @return
     */
    @Override
    public OddsOpListResponse findManageOpOdds(Long startId, Integer limit, Boolean isFive, Integer byOrder) {
	List<Long> contestIds = new ArrayList<Long>();
	List<Long> teamIds = new ArrayList<Long>();
	OddsOpListResponse oddsOpListResponse = new OddsOpListResponse();
	List<OddsOpResponse> oddsOpResponseList = new ArrayList<OddsOpResponse>();
	List<OddsOp> ops = fbOpDao.findOdds(startId, limit, isFive, byOrder);
	List<String> contestIdArray = new ArrayList<String>();
	for (OddsOp op : ops) {
	    contestIds.add(op.getContestId());
	    contestIdArray.add(op.getContestId().toString());
	}
	Map<Long, FbContest> contestMap = fbContestDao.findFbContestMapByIds(contestIds);
	for (FbContest contest : contestMap.values()) {
	    teamIds.add(contest.getHomeTeam());
	    teamIds.add(contest.getAwayTeam());
	}
	Map<Long, FbTeam> teamMap = fbTeamDao.findFbTeamMapByIds(teamIds);
	// Map<Long, Integer> countMap =
	// redisBetCountHandler.findContestsCount(ContestType.FOOTBALL,
	// contestIdArray);
	for (OddsOp op : ops) {
	    OddsOpResponse odds = new OddsOpResponse();
	    FbContest contest = contestMap.get(op.getContestId());
	    if (contest == null) {
		LOG.info(String.format("fb findManageOpOdds[%d] has error contest[%d]", op.getOddsId(), op.getContestId()));
		continue;
	    }
	    odds.setOdds_id(op.getOddsId());
	    // int betCount = countMap.get(op.getContestId());
	    int[] betCount = redisBetCountHandler.findContestBetRatio(ContestType.FOOTBALL, contest.getContestId());
	    odds.setContest(ContestTransformUtil.transformFbContest(contest, teamMap.get(contest.getHomeTeam()),
		    teamMap.get(contest.getAwayTeam()), betCount, true));
	    odds.setInit_home_roi(op.getInitHomeRoi());
	    odds.setInit_draw_roi(op.getInitDrawRoi());
	    odds.setInit_away_roi(op.getInitAwayRoi());
	    odds.setHome_roi(op.getHomeRoi());
	    odds.setDraw_roi(op.getDrawRoi());
	    odds.setAway_roi(op.getAwayRoi());
	    odds.setCompany(op.getCompany());
	    odds.setClose_flag(op.getCloseFlag());
	    odds.setLock_flag(op.getLockFlag());
	    oddsOpResponseList.add(odds);
	}
	oddsOpListResponse.setOdds_ops(oddsOpResponseList);
	return oddsOpListResponse;
    }

    /**
     * 查找足球让球胜平负赔率
     * 
     * @param startId
     * @param limit
     * @return
     */
    @Override
    public OddsJcListResponse findManageJcOdds(Long startId, Integer limit, Boolean isFive, Integer byOrder) {
	List<Long> contestIds = new ArrayList<Long>();
	List<Long> teamIds = new ArrayList<Long>();
	OddsJcListResponse oddsJcListResponse = new OddsJcListResponse();
	List<OddsJcResponse> oddsJcResponseList = new ArrayList<OddsJcResponse>();
	List<OddsJc> jcs = fbJcDao.findOdds(startId, limit, isFive, byOrder);
	List<String> contestIdArray = new ArrayList<String>();
	for (OddsJc jc : jcs) {
	    contestIds.add(jc.getContestId());
	    contestIdArray.add(jc.getContestId().toString());
	}
	Map<Long, FbContest> contestMap = fbContestDao.findFbContestMapByIds(contestIds);
	for (FbContest contest : contestMap.values()) {
	    teamIds.add(contest.getHomeTeam());
	    teamIds.add(contest.getAwayTeam());
	}
	Map<Long, FbTeam> teamMap = fbTeamDao.findFbTeamMapByIds(teamIds);
	// Map<Long, Integer> countMap =
	// redisBetCountHandler.findContestsCount(ContestType.FOOTBALL,
	// contestIdArray);
	for (OddsJc jc : jcs) {

	    OddsJcResponse odds = new OddsJcResponse();
	    FbContest contest = contestMap.get(jc.getContestId());
	    if (contest == null) {
		LOG.info(String.format("fb findManageJcOdds[%d] has error contest[%d]", jc.getOddsId(), jc.getContestId()));
		continue;
	    }
	    odds.setOdds_id(jc.getOddsId());
	    // int betCount = countMap.get(jc.getContestId());
	    int[] betCount = redisBetCountHandler.findContestBetRatio(ContestType.FOOTBALL, contest.getContestId());
	    odds.setContest(ContestTransformUtil.transformFbContest(contest, teamMap.get(contest.getHomeTeam()),
		    teamMap.get(contest.getAwayTeam()), betCount, true));
	    odds.setInit_handicap(jc.getInitHandicap());
	    odds.setInit_home_roi(jc.getInitHomeRoi());
	    odds.setInit_draw_roi(jc.getInitDrawRoi());
	    odds.setInit_away_roi(jc.getInitAwayRoi());
	    odds.setHome_roi(jc.getHomeRoi());
	    odds.setDraw_roi(jc.getDrawRoi());
	    odds.setAway_roi(jc.getAwayRoi());
	    odds.setCompany(jc.getCompany());
	    odds.setClose_flag(jc.getCloseFlag());
	    odds.setLock_flag(jc.getLockFlag());
	    odds.setHandicap(jc.getHandicap());
	    oddsJcResponseList.add(odds);
	}
	oddsJcListResponse.setOdds_jcs(oddsJcResponseList);
	return oddsJcListResponse;
    }

    /**
     * 更新赔率（胜平负）
     * 
     * @param oddsOpResponse
     * @return
     * @throws L99IllegalParamsException
     */
    @Override
    public void updateOpOdds(OddsOpResponse oddsOpResponse) throws L99IllegalParamsException, L99IllegalDataException {
	Long oddsId = oddsOpResponse.getOdds_id();
	Long contestId = oddsOpResponse.getContest_id();
	ParamemeterAssert.assertDataNotNull(oddsId, contestId);
	OddsOp oddsOp = new OddsOp();
	oddsOp.setOddsId(oddsId);
	oddsOp.setContestId(contestId);
	oddsOp.setHomeRoi(oddsOpResponse.getHome_roi());
	oddsOp.setDrawRoi(oddsOpResponse.getDraw_roi());
	oddsOp.setAwayRoi(oddsOpResponse.getAway_roi());
	oddsOp.setInitHomeRoi(oddsOpResponse.getInit_home_roi());
	oddsOp.setInitDrawRoi(oddsOpResponse.getInit_draw_roi());
	oddsOp.setInitAwayRoi(oddsOpResponse.getInit_away_roi());
	oddsOp.setCloseFlag(oddsOpResponse.getClose_flag());
	oddsOp.setCompany(oddsOpResponse.getCompany());
	boolean flag = fbOpDao.update(oddsOp);
	if (!flag) {
	    throw new L99IllegalDataException(MsgCode.BasicMsg.CODE_OPERATE_FAIL, MsgCode.BasicMsg.KEY_OPERATE_FAIL);
	}
    }

    /**
     * 更新赔率（让球胜平负）
     * 
     * @param oddsJcResponse
     * @return
     * @throws L99IllegalParamsException
     */
    @Override
    public void updateJcOdds(OddsJcResponse oddsJcResponse) throws L99IllegalParamsException, L99IllegalDataException {
	Long oddsId = oddsJcResponse.getOdds_id();
	Long contestId = oddsJcResponse.getContest_id();
	ParamemeterAssert.assertDataNotNull(oddsId, contestId);
	OddsJc oddsJc = new OddsJc();
	oddsJc.setOddsId(oddsId);
	oddsJc.setContestId(contestId);
	oddsJc.setHomeRoi(oddsJcResponse.getHome_roi());
	oddsJc.setDrawRoi(oddsJcResponse.getDraw_roi());
	oddsJc.setAwayRoi(oddsJcResponse.getAway_roi());
	oddsJc.setInitHomeRoi(oddsJcResponse.getInit_home_roi());
	oddsJc.setInitDrawRoi(oddsJcResponse.getInit_draw_roi());
	oddsJc.setInitAwayRoi(oddsJcResponse.getInit_away_roi());
	oddsJc.setHandicap(oddsJcResponse.getHandicap());
	oddsJc.setInitHandicap(oddsJcResponse.getInit_handicap());
	oddsJc.setCloseFlag(oddsJcResponse.getClose_flag());
	oddsJc.setCompany(oddsJcResponse.getCompany());
	boolean flag = fbJcDao.update(oddsJc);
	if (!flag) {
	    throw new L99IllegalDataException(MsgCode.BasicMsg.CODE_OPERATE_FAIL, MsgCode.BasicMsg.KEY_OPERATE_FAIL);
	}
    }

    @Override
    public void updateSizeOdds(OddsSizeResponse oddsSizeResponse) throws L99IllegalParamsException, L99IllegalDataException {
	Long oddsId = oddsSizeResponse.getOdds_id();
	ParamemeterAssert.assertDataNotNull(oddsId);
	OddsSize oddsSize = new OddsSize();
	oddsSize.setOddsId(oddsId);
	oddsSize.setContestId(oddsSizeResponse.getContest_id());
	oddsSize.setBigRoi(oddsSizeResponse.getBig_roi());
	oddsSize.setTinyRoi(oddsSizeResponse.getTiny_roi());
	oddsSize.setHandicap(oddsSizeResponse.getHandicap());
	boolean flag = fbOddsSizeDao.update(oddsSize);
	if (!flag) {
	    throw new L99IllegalDataException(MsgCode.BasicMsg.CODE_OPERATE_FAIL, MsgCode.BasicMsg.KEY_OPERATE_FAIL);
	}
    }

    /**
     * 获取足球赛事赔率历史
     */
    @Override
    public ListResponse findOddsHistory(Long contestId, Integer oddsType, Long startId, Integer limit)
	    throws L99IllegalParamsException {

	ParamemeterAssert.assertDataNotNull(contestId, oddsType);

	if (oddsType == PlayType.FB_SPF.getId()) {
	    List<OddsOpHistResponse> oddsOpHists = new ArrayList<>();
	    List<OddsOpHist> oddsList = fbOddsOpHistDao.selectListByContestId(contestId, startId, null, limit);
	    for (OddsOpHist oddsOpHist : oddsList) {
		oddsOpHists.add(OddsTransformUtil.transformOddsOpHist(oddsOpHist));
		startId = oddsOpHist.getId();
	    }
	    OddsOpHistListResponse opHistResp = new OddsOpHistListResponse();
	    opHistResp.setOdds_op_hists(oddsOpHists);
	    opHistResp.setStartId(startId);
	    return opHistResp;
	} else if (oddsType == PlayType.FB_RQSPF.getId()) {
	    List<OddsJcHistResponse> oddsJcHists = new ArrayList<>();
	    List<OddsJcHist> oddsList = fbOddsJcHistDao.selectListByContestId(contestId, startId, null, limit);
	    for (OddsJcHist oddsJcHist : oddsList) {
		oddsJcHists.add(OddsTransformUtil.transformOddsJcHist(oddsJcHist));
		startId = oddsJcHist.getId();
	    }
	    OddsJcHistListResponse jcHistResp = new OddsJcHistListResponse();
	    jcHistResp.setOdds_jc_hists(oddsJcHists);
	    jcHistResp.setStartId(startId);
	    return jcHistResp;
	} else {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}
    }

    /**
     * 重新所有足球比赛的下单统计
     */
    @Override
    public CustomResponse resetBetCount() {
	int opa = 0, jca = 0;
	// 清除所有旧的足球统计
	redisBetCountHandler.delContestSupport(ContestType.FOOTBALL);
	Long startId = null;
	List<BetOp> betOps = null;
	LOG.info("start count fb bet");
	do {
	    betOps = fbBetOpDao.findFbBetOpByContestId(null, startId, 100);
	    for (BetOp betOp : betOps) {
		opa++;
		startId = betOp.getbId();
		redisBetCountHandler.addContestBet(ContestType.FOOTBALL, betOp.getContestId(), PlayType.FB_SPF.getId(),
		        betOp.getSupport());
	    }
	} while (betOps != null && betOps.size() > 0);

	LOG.info("done count op fb bet - " + opa);
	startId = null;
	List<BetJc> betJcs = null;
	do {
	    betJcs = fbBetJcDao.findFbBetJcByContestId(null, startId, 100);
	    for (BetJc betJc : betJcs) {
		jca++;
		startId = betJc.getbId();
		redisBetCountHandler.addContestBet(ContestType.FOOTBALL, betJc.getContestId(), PlayType.FB_RQSPF.getId(),
		        betJc.getSupport());
	    }
	} while (betJcs != null && betJcs.size() > 0);
	LOG.info("done count jc fb bet - " + jca);

	CustomResponse data = new CustomResponse();
	data.put("opa", opa);
	data.put("jca", jca);
	return data;
    }

    /**
     * 重置赛事下单统计
     * 
     * @param contestType
     * @return
     * @throws L99IllegalParamsException
     */
    @Override
    public CustomResponse resetContestCount(Integer contestType) throws L99IllegalParamsException {

	ParamemeterAssert.assertDataNotNull(contestType);

	if (contestType != ContestType.FOOTBALL && contestType != ContestType.BASKETBALL) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}

	LOG.info(String.format("start reset %s - count ", contestType));
	long time = System.currentTimeMillis();

	int ret = redisBetCountHandler.resetContestCount(contestType);

	time = System.currentTimeMillis() - time;
	LOG.info(String.format("done reset %s - count, speed %d ms ", contestType, time));

	CustomResponse data = new CustomResponse();
	data.put("ret", ret);
	data.put("time", time);
	return data;
    }

}
