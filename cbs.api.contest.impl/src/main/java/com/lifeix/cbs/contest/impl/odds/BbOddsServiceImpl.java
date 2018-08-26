package com.lifeix.cbs.contest.impl.odds;

import java.util.ArrayList;
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
import com.lifeix.cbs.api.common.util.BetConstants.ContestContainOdds_BB;
import com.lifeix.cbs.api.common.util.CommerceMath;
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
import com.lifeix.cbs.contest.dao.bb.BbBetJcDao;
import com.lifeix.cbs.contest.dao.bb.BbBetOddevenDao;
import com.lifeix.cbs.contest.dao.bb.BbBetOpDao;
import com.lifeix.cbs.contest.dao.bb.BbBetSizeDao;
import com.lifeix.cbs.contest.dao.bb.BbContestDao;
import com.lifeix.cbs.contest.dao.bb.BbOddsJcDao;
import com.lifeix.cbs.contest.dao.bb.BbOddsJcHistDao;
import com.lifeix.cbs.contest.dao.bb.BbOddsOpDao;
import com.lifeix.cbs.contest.dao.bb.BbOddsOpHistDao;
import com.lifeix.cbs.contest.dao.bb.BbOddsSizeDao;
import com.lifeix.cbs.contest.dao.bb.BbTeamDao;
import com.lifeix.cbs.contest.dto.bb.BbContest;
import com.lifeix.cbs.contest.dto.bb.BbTeam;
import com.lifeix.cbs.contest.dto.bet.BetJc;
import com.lifeix.cbs.contest.dto.bet.BetOddeven;
import com.lifeix.cbs.contest.dto.bet.BetOp;
import com.lifeix.cbs.contest.dto.bet.BetSize;
import com.lifeix.cbs.contest.dto.odds.OddsJc;
import com.lifeix.cbs.contest.dto.odds.OddsJcHist;
import com.lifeix.cbs.contest.dto.odds.OddsOp;
import com.lifeix.cbs.contest.dto.odds.OddsOpHist;
import com.lifeix.cbs.contest.dto.odds.OddsSize;
import com.lifeix.cbs.contest.impl.ImplSupport;
import com.lifeix.cbs.contest.impl.redis.RedisBetCountHandler;
import com.lifeix.cbs.contest.service.cirle.FriendCircleService;
import com.lifeix.cbs.contest.service.odds.BbOddsService;
import com.lifeix.cbs.contest.util.ContestFilterUtil;
import com.lifeix.cbs.contest.util.transform.BbOddsDssDateUtil;
import com.lifeix.cbs.contest.util.transform.ContestTransformUtil;
import com.lifeix.cbs.contest.util.transform.OddsTransformUtil;
import com.lifeix.cbs.message.service.placard.PlacardTempletService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.user.beans.CustomResponse;
import com.lifeix.user.beans.ListResponse;

/**
 * 篮球赔率逻辑
 * 
 * @author lifeix-sz
 */
@Service("bbOddsService")
public class BbOddsServiceImpl extends ImplSupport implements BbOddsService {

    @Autowired
    MoneyService moneyService;

    @Autowired
    CouponUserService couponUserService;

    @Autowired
    private BbContestDao bbContestDao;

    @Autowired
    private BbTeamDao bbTeamDao;

    @Autowired
    private BbOddsOpDao bbOpDao;

    @Autowired
    private BbOddsJcDao bbJcDao;

    @Autowired
    private BbOddsSizeDao bbOddsSizeDao;

    @Autowired
    private BbBetOpDao bbBetOpDao;

    @Autowired
    private BbBetJcDao bbBetJcDao;

    @Autowired
    private BbBetSizeDao bbBetSizeDao;

    @Autowired
    private BbBetOddevenDao bbBetOddevenDao;

    @Autowired
    private BbOddsJcHistDao bbOddsJcHistDao;

    @Autowired
    private BbOddsOpHistDao bbOddsOpHistDao;

    @Autowired
    private RedisBetCountHandler redisBetCountHandler;

    @Autowired
    private ContestNewsService contestNewsService;

    @Autowired
    private FriendCircleService friendCircleService;

    @Autowired
    private PlacardTempletService placardTempletService;

    @Override
    public OddsResponse findBbOddsResponse(Long contestId, Long userId, boolean hasContest, int oddsType, String client)
	    throws L99IllegalParamsException, L99IllegalDataException, JSONException {
	ParamemeterAssert.assertDataNotNull(contestId, oddsType);

	OddsResponse response = new OddsResponse();
	BbContest contest = bbContestDao.selectById(contestId);
	if (contest == null) {
	    throw new L99IllegalDataException(ContestMsg.CODE_CONTEST_NOT_EXIST, ContestMsg.KEY_CONTEST_NOT_EXIST);
	}

	// 赛事下单统计
	contest.setBetCount(redisBetCountHandler.findContestCount(ContestType.BASKETBALL, contestId));

	// 获取用户龙币和龙筹
	if (userId != null) {
	    GoldResponse gold = moneyService.viewUserMoney(userId, "");
	    response.setGold(CommerceMath.sub(gold.getBalance(), gold.getFrozen()));

	    CouponUserListResponse userCoupons = couponUserService.findUserBetCoupons(userId, contestId, contest.getCupId(),
		    ContestType.BASKETBALL);
	    response.setCoupons(userCoupons);
	}

	response.setStatus(contest.getStatus());

	// 赛事信息
	if (hasContest) {
	    List<Long> teamIds = new ArrayList<Long>();
	    teamIds.add(contest.getHomeTeam());
	    teamIds.add(contest.getAwayTeam());
	    Map<Long, BbTeam> teams = bbTeamDao.findBbTeamMapByIds(teamIds);
	    // 赛事下单数量
	    // int betCount =
	    // redisBetCountHandler.findContestCount(ContestType.BASKETBALL,
	    // contest.getContestId());
	    int[] betCount = redisBetCountHandler.findContestBetRatio(ContestType.BASKETBALL, contest.getContestId());
	    ContestResponse contest_res = ContestTransformUtil.transformBbContest(contest, teams.get(contest.getHomeTeam()),
		    teams.get(contest.getAwayTeam()), betCount, false);
	    ContestFilterUtil.filterContestLogo(contest_res, true, client);
	    response.setContest(contest_res);
	}

	// 胜平负
	if ((oddsType & ContestContainOdds_BB.BB_SPF) > 0 && (contest.getOddsType() & ContestContainOdds_BB.BB_SPF) > 0) {

	    OddsOp op = bbOpDao.selectByContest(contestId);
	    response.setOp(OddsTransformUtil.transformOddsOp(op));

	    if (userId != null) {
		List<BetOp> ops = bbBetOpDao.findBbBetOp(contestId, userId, false, null, 3);
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
	if ((oddsType & ContestContainOdds_BB.BB_RQSPF) > 0 && (contest.getOddsType() & ContestContainOdds_BB.BB_RQSPF) > 0) {

	    OddsJc jc = bbJcDao.selectByContest(contestId);
	    response.setJc(OddsTransformUtil.transformOddsJc(jc));

	    if (userId != null) {
		List<BetJc> jcs = bbBetJcDao.findBbBetJc(contestId, userId, false, null, 3);
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

	// 大小球胜平负
	if ((oddsType & ContestContainOdds_BB.BB_SIZE) > 0 && (contest.getOddsType() & ContestContainOdds_BB.BB_SIZE) > 0) {

	    OddsSize oddsSize = bbOddsSizeDao.selectByContest(contestId);
	    response.setSize(OddsTransformUtil.transformOddsSize(oddsSize));

	    if (userId != null) {
		List<BetSize> betSizeList = bbBetSizeDao.findBbBetSize(contestId, userId, false, null, 2);
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
	if ((oddsType & ContestContainOdds_BB.BB_ODDEVEN) > 0
	        && (contest.getOddsType() & ContestContainOdds_BB.BB_ODDEVEN) > 0) {

	    OddsDssResponse oddsDssResponse = BbOddsDssDateUtil.getInstance().getDssResponse();

	    response.setOddeven(oddsDssResponse);

	    if (userId != null) {
		List<BetOddeven> betOddevenList = bbBetOddevenDao.findBbBetOddeven(contestId, userId, false, null, 2);
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
	Integer[] op_count = { 0, 0 };// 主，客
	Integer[] jc_count = { 0, 0 };// 主，客
	Integer[] size_count = { 0, 0 };// 大，小
	Integer[] oddeven_count = { 0, 0 };// 单，双
	Map<String, Object> betCount = redisBetCountHandler.getContestBet(ContestType.BASKETBALL, contestId);
	Object ophCount = betCount.get(String.format("%d,%d", PlayType.BB_SPF.getId(), BetStatus.HOME));// 主胜人数
	if (ophCount != null) {
	    op_count[0] = Integer.valueOf(ophCount.toString());
	}
	Object opaCount = betCount.get(String.format("%d,%d", PlayType.BB_SPF.getId(), BetStatus.AWAY));// 客胜人数
	if (opaCount != null) {
	    op_count[1] = Integer.valueOf(opaCount.toString());
	}
	Object jchCount = betCount.get(String.format("%d,%d", PlayType.BB_JC.getId(), BetStatus.HOME));// 主胜人数
	if (jchCount != null) {
	    jc_count[0] = Integer.valueOf(jchCount.toString());
	}
	Object jcaCount = betCount.get(String.format("%d,%d", PlayType.BB_JC.getId(), BetStatus.AWAY));// 客胜人数
	if (jcaCount != null) {
	    jc_count[1] = Integer.valueOf(jcaCount.toString());
	}
	Object sizehCount = betCount.get(String.format("%d,%d", PlayType.BB_SIZE.getId(), BetStatus.HOME));// 大球人数
	if (sizehCount != null) {
	    size_count[0] = Integer.valueOf(sizehCount.toString());
	}
	Object sizeaCount = betCount.get(String.format("%d,%d", PlayType.BB_SIZE.getId(), BetStatus.AWAY));// 小球人数
	if (sizeaCount != null) {
	    size_count[1] = Integer.valueOf(sizeaCount.toString());
	}
	Object oddevenhCount = betCount.get(String.format("%d,%d", PlayType.BB_ODDEVEN.getId(), BetStatus.HOME));// 单数人数
	if (oddevenhCount != null) {
	    oddeven_count[0] = Integer.valueOf(oddevenhCount.toString());
	}
	Object oddevenaCount = betCount.get(String.format("%d,%d", PlayType.BB_ODDEVEN.getId(), BetStatus.AWAY));// 双数人数
	if (oddevenaCount != null) {
	    oddeven_count[1] = Integer.valueOf(oddevenaCount.toString());
	}
	response.setOp_count(op_count);
	response.setJc_count(jc_count);
	response.setSize_count(size_count);
	response.setOddeven_count(oddeven_count);

	// add by lhx on 16-04-18
	// 该球赛是否有赛事分析
	boolean hasNews = contestNewsService.hasNews(contestId, ContestType.BASKETBALL);
	response.setHas_news(hasNews);

	// add by lhx on 16-05-10
	// 该球赛是否有下单理由
	boolean hasReason = friendCircleService.isHasReason(contestId, ContestType.BASKETBALL);
	response.setHas_reason(hasReason);

	// 查询是否关联广告
	String placard = placardTempletService.findPlacardRelation(contest.getCupId(), contestId, ContestType.BASKETBALL);
	response.setPlacardId(placard);

	return response;
    }

    /**
     * 查找篮球胜平负赔率列表
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
	List<OddsOp> ops = bbOpDao.findOdds(startId, limit, isFive, byOrder);
	List<String> contestIdArray = new ArrayList<String>();
	for (OddsOp op : ops) {
	    contestIds.add(op.getContestId());
	    contestIdArray.add(op.getContestId().toString());
	}
	Map<Long, BbContest> contestMap = bbContestDao.findBbContestMapByIds(contestIds);
	for (BbContest contest : contestMap.values()) {
	    teamIds.add(contest.getHomeTeam());
	    teamIds.add(contest.getAwayTeam());
	}
	Map<Long, BbTeam> teamMap = bbTeamDao.findBbTeamMapByIds(teamIds);
	// Map<Long, Integer> countMap =
	// redisBetCountHandler.findContestsCount(ContestType.BASKETBALL,
	// contestIdArray);
	for (OddsOp op : ops) {
	    OddsOpResponse odds = new OddsOpResponse();
	    BbContest contest = contestMap.get(op.getContestId());
	    if (contest == null) {
		LOG.info(String.format("bb findManageOpOdds[%d] has error contest[%d]", op.getOddsId(), op.getContestId()));
		continue;
	    }
	    odds.setOdds_id(op.getOddsId());
	    // int betCount = countMap.get(op.getContestId());
	    int[] betCount = redisBetCountHandler.findContestBetRatio(ContestType.BASKETBALL, contest.getContestId());
	    odds.setContest(ContestTransformUtil.transformBbContest(contest, teamMap.get(contest.getHomeTeam()),
		    teamMap.get(contest.getAwayTeam()), betCount, true));
	    odds.setInit_home_roi(op.getInitHomeRoi());
	    odds.setInit_away_roi(op.getInitAwayRoi());
	    odds.setHome_roi(op.getHomeRoi());
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
     * 查找篮球让球胜平负赔率列表
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
	List<OddsJc> jcs = bbJcDao.findOdds(startId, limit, isFive, byOrder);
	List<String> contestIdArray = new ArrayList<String>();
	for (OddsJc jc : jcs) {
	    contestIds.add(jc.getContestId());
	    contestIdArray.add(jc.getContestId().toString());
	}
	Map<Long, BbContest> contestMap = bbContestDao.findBbContestMapByIds(contestIds);
	for (BbContest contest : contestMap.values()) {
	    teamIds.add(contest.getHomeTeam());
	    teamIds.add(contest.getAwayTeam());
	}
	Map<Long, BbTeam> teamMap = bbTeamDao.findBbTeamMapByIds(teamIds);
	// Map<Long, Integer> countMap =
	// redisBetCountHandler.findContestsCount(ContestType.BASKETBALL,
	// contestIdArray);
	for (OddsJc jc : jcs) {
	    OddsJcResponse odds = new OddsJcResponse();
	    BbContest contest = contestMap.get(jc.getContestId());
	    if (contest == null) {
		LOG.info(String.format("bb findManageJcOdds[%d] has error contest[%d]", jc.getOddsId(), jc.getContestId()));
		continue;
	    }
	    odds.setOdds_id(jc.getOddsId());
	    // int betCount = countMap.get(jc.getContestId());
	    int[] betCount = redisBetCountHandler.findContestBetRatio(ContestType.BASKETBALL, contest.getContestId());
	    odds.setContest(ContestTransformUtil.transformBbContest(contest, teamMap.get(contest.getHomeTeam()),
		    teamMap.get(contest.getAwayTeam()), betCount, true));
	    odds.setInit_handicap(jc.getInitHandicap());
	    odds.setInit_home_roi(jc.getInitHomeRoi());
	    odds.setInit_away_roi(jc.getInitAwayRoi());
	    odds.setHome_roi(jc.getHomeRoi());
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
	oddsOp.setAwayRoi(oddsOpResponse.getAway_roi());
	oddsOp.setInitHomeRoi(oddsOpResponse.getInit_home_roi());
	oddsOp.setInitAwayRoi(oddsOpResponse.getInit_away_roi());
	oddsOp.setCloseFlag(oddsOpResponse.getClose_flag());
	oddsOp.setCompany(oddsOpResponse.getCompany());
	boolean flag = bbOpDao.update(oddsOp);
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
	oddsJc.setAwayRoi(oddsJcResponse.getAway_roi());
	oddsJc.setInitHomeRoi(oddsJcResponse.getInit_home_roi());
	oddsJc.setInitAwayRoi(oddsJcResponse.getInit_away_roi());
	oddsJc.setHandicap(oddsJcResponse.getHandicap());
	oddsJc.setInitHandicap(oddsJcResponse.getInit_handicap());
	oddsJc.setCloseFlag(oddsJcResponse.getClose_flag());
	oddsJc.setCompany(oddsJcResponse.getCompany());
	boolean flag = bbJcDao.update(oddsJc);
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
	boolean flag = bbOddsSizeDao.update(oddsSize);
	if (!flag) {
	    throw new L99IllegalDataException(MsgCode.BasicMsg.CODE_OPERATE_FAIL, MsgCode.BasicMsg.KEY_OPERATE_FAIL);
	}
    }

    /**
     * 获取篮球赛事赔率历史
     * 
     * @param contestId
     * @param oddsType
     * @param startId
     * @param limit
     * @return
     */
    @Override
    public ListResponse findOddsHistory(Long contestId, Integer oddsType, Long startId, Integer limit)
	    throws L99IllegalParamsException {

	ParamemeterAssert.assertDataNotNull(contestId, oddsType);

	if (oddsType == PlayType.BB_SPF.getId()) {
	    List<OddsOpHistResponse> oddsOpHists = new ArrayList<>();
	    List<OddsOpHist> oddsList = bbOddsOpHistDao.selectListByContestId(contestId, startId, null, limit);
	    for (OddsOpHist oddsOpHist : oddsList) {
		oddsOpHists.add(OddsTransformUtil.transformOddsOpHist(oddsOpHist));
		startId = oddsOpHist.getId();
	    }
	    OddsOpHistListResponse opHistResp = new OddsOpHistListResponse();
	    opHistResp.setOdds_op_hists(oddsOpHists);
	    opHistResp.setStartId(startId);
	    return opHistResp;
	} else if (oddsType == PlayType.BB_JC.getId()) {
	    List<OddsJcHistResponse> oddsJcHists = new ArrayList<>();
	    List<OddsJcHist> oddsList = bbOddsJcHistDao.selectListByContestId(contestId, startId, null, limit);
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
     * 重新所有篮球比赛的下单支持统计
     */
    @Override
    public CustomResponse resetBetCount() {
	int opa = 0, jca = 0;
	// 清除所有旧的篮球统计
	redisBetCountHandler.delContestSupport(ContestType.BASKETBALL);
	Long startId = null;
	List<BetOp> betOps = null;
	LOG.info("start count bb bet");
	do {
	    betOps = bbBetOpDao.findBbBetOpByContestId(null, startId, 100);
	    for (BetOp betOp : betOps) {
		opa++;
		startId = betOp.getbId();
		redisBetCountHandler.addContestBet(ContestType.BASKETBALL, betOp.getContestId(), PlayType.BB_SPF.getId(),
		        betOp.getSupport());
	    }
	} while (betOps != null && betOps.size() > 0);

	LOG.info("done count op bb bet - " + opa);
	startId = null;
	List<BetJc> betJcs = null;
	do {
	    betJcs = bbBetJcDao.findBbBetJcByContestId(null, startId, 100);
	    for (BetJc betJc : betJcs) {
		jca++;
		startId = betJc.getbId();
		redisBetCountHandler.addContestBet(ContestType.BASKETBALL, betJc.getContestId(), PlayType.BB_JC.getId(),
		        betJc.getSupport());
	    }
	} while (betJcs != null && betJcs.size() > 0);
	LOG.info("done count jc bb bet - " + jca);

	CustomResponse data = new CustomResponse();
	data.put("opa", opa);
	data.put("jca", jca);
	return data;
    }

}
