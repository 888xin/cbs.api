package com.lifeix.cbs.contest.impl.spark.settle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import lifeix.framwork.util.JsonUtils;

import org.apache.commons.lang3.time.FastDateFormat;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.l99.common.utils.TimeUtil;
import com.lifeix.cbs.achieve.bean.bo.SettleAchieveBO;
import com.lifeix.cbs.achieve.common.constant.BehaviorConstants;
import com.lifeix.cbs.achieve.service.AchieveService;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.exception.MsgCode.ContestMsg;
import com.lifeix.cbs.api.common.lock.RedisLockHelper;
import com.lifeix.cbs.api.common.util.BetConstants;
import com.lifeix.cbs.api.common.util.BetConstants.BetResultStatus;
import com.lifeix.cbs.api.common.util.BetConstants.BetStatus;
import com.lifeix.cbs.api.common.util.BetConstants.ContestContainOdds_BB;
import com.lifeix.cbs.api.common.util.CommerceMath;
import com.lifeix.cbs.api.common.util.ContestConstants;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestStatu_BB;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestType;
import com.lifeix.cbs.api.common.util.ContestConstants.TempletId;
import com.lifeix.cbs.api.common.util.LockConstants;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.common.util.PlayType;
import com.lifeix.cbs.api.common.util.RandomUtils;
import com.lifeix.cbs.api.service.coupon.CouponUserService;
import com.lifeix.cbs.api.service.money.MoneyService;
import com.lifeix.cbs.api.service.money.MoneyStatisticService;
import com.lifeix.cbs.contest.dao.bb.BbBetJcDao;
import com.lifeix.cbs.contest.dao.bb.BbBetOddevenDao;
import com.lifeix.cbs.contest.dao.bb.BbBetOpDao;
import com.lifeix.cbs.contest.dao.bb.BbBetSizeDao;
import com.lifeix.cbs.contest.dao.bb.BbContestDao;
import com.lifeix.cbs.contest.dao.bb.BbOddsJcDao;
import com.lifeix.cbs.contest.dao.bb.BbOddsOpDao;
import com.lifeix.cbs.contest.dao.bb.BbOddsSizeDao;
import com.lifeix.cbs.contest.dao.bb.BbTeamDao;
import com.lifeix.cbs.contest.dao.settle.UserSettleLogDao;
import com.lifeix.cbs.contest.dto.bb.BbContest;
import com.lifeix.cbs.contest.dto.bb.BbTeam;
import com.lifeix.cbs.contest.dto.bet.Bet;
import com.lifeix.cbs.contest.dto.bet.BetJc;
import com.lifeix.cbs.contest.dto.bet.BetOddeven;
import com.lifeix.cbs.contest.dto.bet.BetOp;
import com.lifeix.cbs.contest.dto.bet.BetSize;
import com.lifeix.cbs.contest.dto.settle.UserSettleLog;
import com.lifeix.cbs.contest.service.spark.settle.BbSettleDubbo;
import com.lifeix.cbs.contest.util.CbsSettleLog;
import com.lifeix.cbs.contest.util.WeixinNotifyTemplate;
import com.lifeix.cbs.contest.util.WeixinNotifyUtil;
import com.lifeix.cbs.contest.util.algorithm.AlgorithmFactory;
import com.lifeix.cbs.contest.util.algorithm.AlgorithmResult;
import com.lifeix.cbs.contest.util.algorithm.AlgorithmRole;
import com.lifeix.cbs.message.service.notify.NotifyService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.framework.kafka.producer.MessageSender;
import com.lifeix.user.beans.CustomResponse;

/**
 * 篮球赛事结算逻辑
 * 
 * @author lifeix-sz
 */
@Service("bbSettleService")
public class BbSettleDubboImpl implements BbSettleDubbo {

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
    private UserSettleLogDao userSettleLogDao;
    @Autowired
    private MoneyService moneyService;

    @Autowired
    NotifyService notifyService;

    @Autowired
    CouponUserService couponUserService;

    @Autowired
    private RedisLockHelper redisLock;

    @Autowired
    protected AchieveService achieveService;

    @Autowired
    protected MoneyStatisticService moneyStatisticService;

    @Resource
    private MessageSender kafkaMessageSender;

    /**
     * 赛事结算锁
     */
    private static Object lock = new Object();

    /**
     * 结算篮球赛事
     * 
     * @param contestId
     * @throws L99IllegalDataException
     * @throws L99IllegalParamsException
     * @throws JSONException
     */
    @Override
    public CustomResponse settleContest(Long contestId) throws L99IllegalParamsException, L99IllegalDataException,
	    JSONException {

	ParamemeterAssert.assertDataNotNull(contestId);

	BbContest contest = bbContestDao.selectById(contestId);
	// 比赛状态判断
	if (contest == null) {
	    throw new L99IllegalDataException(ContestMsg.CODE_CONTEST_NOT_EXIST, ContestMsg.KEY_CONTEST_NOT_EXIST);
	} else if (contest.getSettle() == contest.getOddsType()) {
	    throw new L99IllegalDataException(ContestMsg.CODE_CONTEST_SETTLED, ContestMsg.KEY_CONTEST_SETTLED);
	} else if (contest.getStatus() >= 0) {
	    throw new L99IllegalDataException(ContestMsg.CODE_CONTEST_NOT_END, ContestMsg.KEY_CONTEST_NOT_END);
	} else if (contest.getStatus() == ContestStatu_BB.PEDING || contest.getStatus() == ContestStatu_BB.INTERRUPT
	        || contest.getStatus() == ContestStatu_BB.PUTOFF) {
	    throw new L99IllegalDataException(ContestMsg.CODE_CONTEST_PEDDING, ContestMsg.KEY_CONTEST_PEDDING);
	} else if (contest.getStatus() == ContestStatu_BB.DONE || contest.getStatus() == ContestStatu_BB.CANCLE) { // 正常结算

	    CbsSettleLog.info(String.format("##bb[%d] settle start", contestId));

	    // 因为赛事更新与超时检查的定时器都会call结算的逻辑，为避免重复结算问题，此处加线程锁
	    synchronized (lock) {
		if (redisLock.getRedisLock(contestId, LockConstants.IDENTIFY_BB) != null) {
		    CbsSettleLog.info(String.format("!!bb[%d] settle failed,contest id pending.", contestId));
		    throw new L99IllegalDataException(ContestMsg.CODE_CONTEST_PEDDING, ContestMsg.KEY_CONTEST_PEDDING);
		}
		redisLock.setRedisLock(contestId, LockConstants.IDENTIFY_BB);
	    }

	    try {
		boolean cancleFlag = false;
		if (contest.getStatus() == ContestStatu_BB.CANCLE) { // 比赛取消做为走盘结算
		    cancleFlag = true;
		}
		String homeName = "undefined", awayName = "undefined";
		Integer homeCountryId = null, awayCountryId = null;

		// 球队信息
		List<Long> teamIds = new ArrayList<Long>();
		teamIds.add(contest.getHomeTeam());
		teamIds.add(contest.getAwayTeam());
		Map<Long, BbTeam> teams = bbTeamDao.findBbTeamMapByIds(teamIds);
		BbTeam homeBbTeam = teams.get(contest.getHomeTeam());
		if (homeBbTeam != null) {
		    homeName = homeBbTeam.getName();
		    homeCountryId = homeBbTeam.getCountryId();
		}

		BbTeam awayBbTeam = teams.get(contest.getAwayTeam());
		if (awayBbTeam != null) {
		    awayName = awayBbTeam.getName();
		    awayCountryId = awayBbTeam.getCountryId();
		}
		String contestName = String.format("%sVS%s", homeName, awayName);

		int op = 0, jc = 0, size = 0, oddeven = 0;

		// 结算胜平负
		if ((contest.getOddsType() & ContestContainOdds_BB.BB_SPF) > 0
		        && (contest.getSettle() & ContestContainOdds_BB.BB_SPF) == 0) {
		    try {
			settleContestOp(contest, contestName, cancleFlag, homeName, awayName, homeCountryId, awayCountryId);
			op = 1;
		    } catch (L99IllegalDataException e) {
			CbsSettleLog.error(String.format("!!bb[%d] settle op failed, %s", contestId, e.getMessage()), e);
		    }
		} else {
		    op = -1;
		}

		// 结算让球胜平负
		if ((contest.getOddsType() & ContestContainOdds_BB.BB_RQSPF) > 0
		        && (contest.getSettle() & ContestContainOdds_BB.BB_RQSPF) == 0) {
		    try {
			settleContestJc(contest, contestName, cancleFlag, homeName, awayName, homeCountryId, awayCountryId);
			jc = 1;
		    } catch (L99IllegalDataException e) {
			CbsSettleLog.error(String.format("!!bb[%d] settle jc failed, %s", contestId, e.getMessage()), e);
		    }
		} else {
		    jc = -1;
		}

		// 结算大小球
		if ((contest.getOddsType() & ContestContainOdds_BB.BB_SIZE) > 0
		        && (contest.getSettle() & ContestContainOdds_BB.BB_SIZE) == 0) {
		    try {
			settleContestSize(contest, contestName, cancleFlag, homeName, awayName, homeCountryId, awayCountryId);
			size = 1;
		    } catch (L99IllegalDataException e) {
			CbsSettleLog.error(String.format("!!bb[%d] settle size failed, %s", contestId, e.getMessage()), e);
		    }
		} else {
		    size = -1;
		}

		// 结算单双数
		if ((contest.getOddsType() & ContestContainOdds_BB.BB_ODDEVEN) > 0
		        && (contest.getSettle() & ContestContainOdds_BB.BB_ODDEVEN) == 0) {
		    try {
			settleContestOddeven(contest, contestName, cancleFlag, homeName, awayName, homeCountryId,
			        awayCountryId);
			oddeven = 1;
		    } catch (L99IllegalDataException e) {
			CbsSettleLog
			        .error(String.format("!!bb[%d] settle oddeven failed, %s", contestId, e.getMessage()), e);
		    }
		} else {
		    oddeven = -1;
		}

		CustomResponse response = new CustomResponse();
		response.put("op", op);
		response.put("jc", jc);
		response.put("size", size);
		response.put("oddeven", oddeven);
		CbsSettleLog.info(String.format("##bb[%d] settle success, op=%d | jc=%d | size=%d | oddeven=%d", contestId,
		        op, jc, size, oddeven));

		return response;
	    } finally {
		redisLock.delRedisLock(contestId, LockConstants.IDENTIFY_BB);
	    }

	} else {
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}

    }

    /**
     * 取消比赛的单个下注
     * 
     * @param playType
     * @param bId
     * @param reson
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     * @throws JSONException
     */
    @Override
    public void cancleBet(Integer playType, Long bId, String reson, String userName) throws L99IllegalParamsException,
	    L99IllegalDataException, JSONException {

	ParamemeterAssert.assertDataNotNull(playType, bId);

	Long contestId = null;
	AlgorithmResult ret = new AlgorithmResult();
	ret.setProfit(0);
	ret.setStatus(BetResultStatus.DRAW);
	Bet betBase = null;
	String playName = null;
	if (playType == PlayType.BB_SPF.getId()) {
	    BetOp bet = bbBetOpDao.selectById(bId);
	    if (bet == null || bet.getStatus().intValue() != BetResultStatus.INIT) {
		throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	    }
	    ret.setAmount(bet.getBet());
	    if (bet.getSupport().intValue() == BetStatus.HOME) {
		ret.setOdds(bet.getHomeRoi());
	    } else if (bet.getSupport().intValue() == BetStatus.AWAY) {
		ret.setOdds(bet.getAwayRoi());
	    } else if (bet.getSupport().intValue() == BetStatus.DRAW) {
		ret.setOdds(bet.getDrawRoi());
	    }
	    betBase = bet;
	    contestId = bet.getContestId();
	    playName = "胜负";
	} else if (playType == PlayType.BB_JC.getId()) {
	    BetJc bet = bbBetJcDao.selectById(bId);
	    if (bet == null || bet.getStatus().intValue() != BetResultStatus.INIT) {
		throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	    }
	    ret.setAmount(bet.getBet());
	    if (bet.getSupport().intValue() == BetStatus.HOME) {
		ret.setOdds(bet.getHomeRoi());
	    } else if (bet.getSupport().intValue() == BetStatus.AWAY) {
		ret.setOdds(bet.getAwayRoi());
	    } else if (bet.getSupport().intValue() == BetStatus.DRAW) {
		ret.setOdds(bet.getDrawRoi());
	    }
	    betBase = bet;
	    contestId = bet.getContestId();
	    playName = "让球胜负";
	} else if (playType == PlayType.BB_SIZE.getId()) {
	    BetSize bet = bbBetSizeDao.selectById(bId);
	    if (bet == null || bet.getStatus().intValue() != BetResultStatus.INIT) {
		throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	    }
	    ret.setAmount(bet.getBet());
	    if (bet.getSupport() == BetStatus.HOME) {
		ret.setOdds(bet.getBigRoi());
	    } else if (bet.getSupport() == BetStatus.AWAY) {
		ret.setOdds(bet.getTinyRoi());
	    }
	    betBase = bet;
	    contestId = bet.getContestId();
	    playName = "大小球";
	} else if (playType == PlayType.BB_ODDEVEN.getId()) {
	    BetOddeven bet = bbBetOddevenDao.selectById(bId);
	    if (bet == null || bet.getStatus().intValue() != BetResultStatus.INIT) {
		throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	    }
	    ret.setAmount(bet.getBet());
	    if (bet.getSupport() == BetStatus.HOME) {
		ret.setOdds(bet.getOddRoi());
	    } else if (bet.getSupport() == BetStatus.AWAY) {
		ret.setOdds(bet.getEvenRoi());
	    }
	    betBase = bet;
	    contestId = bet.getContestId();
	    playName = "单双数";
	} else {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}

	BbContest contest = bbContestDao.selectById(contestId);
	if (contest == null) {
	    throw new L99IllegalDataException(ContestMsg.CODE_CONTEST_NOT_EXIST, ContestMsg.KEY_CONTEST_NOT_EXIST);
	}
	// 球队信息
	String homeName = "undefined", awayName = "undefined";
	List<Long> teamIds = new ArrayList<Long>();
	teamIds.add(contest.getHomeTeam());
	teamIds.add(contest.getAwayTeam());
	Map<Long, BbTeam> teams = bbTeamDao.findBbTeamMapByIds(teamIds);
	BbTeam homeFbTeam = teams.get(contest.getHomeTeam());
	if (homeFbTeam != null) {
	    homeName = homeFbTeam.getName();
	}

	BbTeam awayFbTeam = teams.get(contest.getAwayTeam());
	if (awayFbTeam != null) {
	    awayName = awayFbTeam.getName();
	}
	String contestName = String.format("%sVS%s", homeName, awayName);
	settleUserBet(ret, contest, betBase, contestName, playType, playName);

    }

    @Override
    public void settleMissedBet(Integer playType, Long bId) throws L99IllegalParamsException, L99IllegalDataException,
	    JSONException {
	ParamemeterAssert.assertDataNotNull(playType, bId);

	Long contestId = null;
	Bet betBase = null;
	String playName = null;
	Double odds = 0.0D;
	if (playType == PlayType.BB_SPF.getId()) {
	    BetOp bet = bbBetOpDao.selectById(bId);
	    if (bet == null || bet.getStatus().intValue() == BetResultStatus.INIT) {
		throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	    }
	    if (bet.getSupport().intValue() == BetStatus.HOME) {
		odds = bet.getHomeRoi();
	    } else if (bet.getSupport().intValue() == BetStatus.AWAY) {
		odds = bet.getAwayRoi();
	    } else if (bet.getSupport().intValue() == BetStatus.DRAW) {
		odds = bet.getDrawRoi();
	    }
	    betBase = bet;
	    contestId = bet.getContestId();
	    playName = "胜负";
	} else if (playType == PlayType.BB_JC.getId()) {
	    BetJc bet = bbBetJcDao.selectById(bId);
	    if (bet == null || bet.getStatus().intValue() == BetResultStatus.INIT) {
		throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	    }
	    if (bet.getSupport().intValue() == BetStatus.HOME) {
		odds = bet.getHomeRoi();
	    } else if (bet.getSupport().intValue() == BetStatus.AWAY) {
		odds = bet.getAwayRoi();
	    } else if (bet.getSupport().intValue() == BetStatus.DRAW) {
		odds = bet.getDrawRoi();
	    }
	    betBase = bet;
	    contestId = bet.getContestId();
	    playName = "让球胜负";
	} else if (playType == PlayType.BB_SIZE.getId()) {
	    BetSize bet = bbBetSizeDao.selectById(bId);
	    if (bet == null || bet.getStatus().intValue() == BetResultStatus.INIT) {
		throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	    }
	    if (bet.getSupport() == BetStatus.HOME) {
		odds = bet.getBigRoi();
	    } else if (bet.getSupport() == BetStatus.AWAY) {
		odds = bet.getTinyRoi();
	    }
	    betBase = bet;
	    contestId = bet.getContestId();
	    playName = "大小球";
	} else if (playType == PlayType.BB_ODDEVEN.getId()) {
	    BetOddeven bet = bbBetOddevenDao.selectById(bId);
	    if (bet == null || bet.getStatus().intValue() == BetResultStatus.INIT) {
		throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	    }
	    if (bet.getSupport() == BetStatus.HOME) {
		odds = bet.getOddRoi();
	    } else if (bet.getSupport() == BetStatus.AWAY) {
		odds = bet.getEvenRoi();
	    }
	    betBase = bet;
	    contestId = bet.getContestId();
	    playName = "单双数";
	} else {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}

	BbContest contest = bbContestDao.selectById(contestId);
	if (contest == null) {
	    throw new L99IllegalDataException(ContestMsg.CODE_CONTEST_NOT_EXIST, ContestMsg.KEY_CONTEST_NOT_EXIST);
	}
	// 球队信息
	String homeName = "undefined", awayName = "undefined";
	List<Long> teamIds = new ArrayList<Long>();
	teamIds.add(contest.getHomeTeam());
	teamIds.add(contest.getAwayTeam());
	Map<Long, BbTeam> teams = bbTeamDao.findBbTeamMapByIds(teamIds);
	BbTeam homeFbTeam = teams.get(contest.getHomeTeam());
	if (homeFbTeam != null) {
	    homeName = homeFbTeam.getName();
	}

	BbTeam awayFbTeam = teams.get(contest.getAwayTeam());
	if (awayFbTeam != null) {
	    awayName = awayFbTeam.getName();
	}
	String contestName = String.format("%sVS%s", homeName, awayName);
	settleMoneyToUser(betBase, contest, odds, contestName, playType, playName);

    }

    /**
     * 结算胜平负下单
     * 
     * @param contest
     * @param contestName
     * @param cancleFlag
     *            比赛是否为取消 为true 认为是取消比赛
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     * @throws JSONException
     */
    private int settleContestOp(BbContest contest, String contestName, boolean cancleFlag, String homeName, String awayName,
	    Integer homeCountryId, Integer awayCountryId) throws L99IllegalParamsException, L99IllegalDataException,
	    JSONException {

	int count = 0;
	Long startId = null;
	List<BetOp> bets = null;
	AlgorithmRole role = AlgorithmFactory.createContextRole(PlayType.BB_SPF);
	FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
	do {
	    bets = bbBetOpDao.findBbBetOp(contest.getContestId(), null, true, startId, ContestConstants.SETTLE_LIMIT);
	    if (bets.size() > 0) {
		List<SettleAchieveBO> dataList = new ArrayList<SettleAchieveBO>(bets.size());
		for (BetOp bet : bets) {
		    startId = bet.getbId();
		    Long contestId = contest.getContestId();
		    if (bet.getStatus().intValue() != BetResultStatus.INIT) {
			CbsSettleLog.info(String.format("##bb[%d]-op-bet[%d] resettle", contestId, bet.getbId()));
			continue;
		    }
		    AlgorithmResult ret = null;
		    if (cancleFlag) {
			// 取消比赛按走盘结算
			ret = new AlgorithmResult();
			ret.setAmount(bet.getBet());
			if (bet.getSupport().intValue() == BetStatus.HOME) {
			    ret.setOdds(bet.getHomeRoi());
			} else if (bet.getSupport().intValue() == BetStatus.AWAY) {
			    ret.setOdds(bet.getAwayRoi());
			} else if (bet.getSupport().intValue() == BetStatus.DRAW) {
			    ret.setOdds(bet.getDrawRoi());
			}
			ret.setProfit(0);
			ret.setStatus(BetResultStatus.DRAW);
		    } else {
			// 按照玩法计算奖励
			ret = role.settleBet(bet.getBet(), bet.getSupport(), contest.getHomeScores(),
			        contest.getAwayScores(), bet.getHomeRoi(), bet.getAwayRoi());
		    }
		    settleUserBet(ret, contest, bet, contestName, PlayType.BB_SPF.getId(), "胜平负");
		    count++;

		    // 结算成就相关
		    try {
			SettleAchieveBO bo = new SettleAchieveBO();
			bo.setBehavior_type(BehaviorConstants.BehaviorType.SETTLE_TYPE);
			bo.setUser_id(bet.getUserId());
			bo.setAway_team(contest.getAwayTeam());
			bo.setAway_name(awayName);
			bo.setAway_country_id(awayCountryId);
			bo.setAway_score(contest.getAwayScores());
			bo.setBet_id(bet.getbId());
			Double betRoi = null;
			if (bet.getSupport() == BetStatus.HOME)
			    betRoi = bet.getHomeRoi();
			else if (bet.getSupport() == BetStatus.AWAY)
			    betRoi = bet.getAwayRoi();
			else if (bet.getSupport() == BetStatus.DRAW)
			    betRoi = bet.getDrawRoi();

			bo.setBet_roi(betRoi);
			bo.setBet_value(bet.getBet());
			bo.setContest_id(contest.getContestId());
			bo.setContest_type(ContestType.BASKETBALL);
			bo.setCreate_time(bet.getCreateTime() != null ? format.format(bet.getCreateTime()) : null);
			bo.setCupId(contest.getCupId());
			bo.setHome_team(contest.getHomeTeam());
			bo.setHome_name(homeName);
			bo.setHome_country_id(homeCountryId);
			bo.setHome_score(contest.getHomeScores());
			bo.setPlay_type(PlayType.BB_SPF.getId());
			bo.setStartTime(format.format(contest.getStartTime()));
			bo.setSupport(bet.getSupport());
			bo.setStatus(bet.getStatus());
			bo.setBack(bet.getBack());
			dataList.add(bo);
		    } catch (Throwable t) {
			CbsSettleLog.error(
			        "userId=" + bet.getUserId() + ", achieve settle data analysis failed: " + t.getMessage(), t);
		    }
		}
		// 结算行为批量发送
		if (dataList.size() > 0) {
		    try {
			achieveService.addAchieveData(JsonUtils.toJsonString(dataList), true);
		    } catch (Throwable t) {
			CbsSettleLog.error("add achieve settle data failed: " + t.getMessage(), t);
		    }
		}
	    }

	} while (bets != null && bets.size() > 0);

	// 结算后标记比赛
	contest.setSettle(contest.getSettle() | ContestContainOdds_BB.BB_SPF);
	boolean flag = bbContestDao.updateSettle(contest);
	if (!flag) {
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}

	// 关闭赔率记录
	bbOpDao.closeOdds(contest.getContestId());
	return count;
    }

    /**
     * 结算让球胜平负下单
     * 
     * @param contest
     * @param contestName
     * @param cancleFlag
     * @param homeName
     * @param awayName
     * @param homeCountryId
     * @param awayCountryId
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    private int settleContestJc(BbContest contest, String contestName, boolean cancleFlag, String homeName, String awayName,
	    Integer homeCountryId, Integer awayCountryId) throws L99IllegalParamsException, L99IllegalDataException,
	    JSONException {

	Long startId = null;
	List<BetJc> bets = null;
	int count = 0;
	AlgorithmRole role = AlgorithmFactory.createContextRole(PlayType.BB_JC);
	FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
	do {
	    bets = bbBetJcDao.findBbBetJc(contest.getContestId(), null, true, startId, ContestConstants.SETTLE_LIMIT);
	    if (bets.size() > 0) {
		List<SettleAchieveBO> dataList = new ArrayList<SettleAchieveBO>(bets.size());
		for (BetJc bet : bets) {
		    startId = bet.getbId();
		    Long contestId = contest.getContestId();
		    if (bet.getStatus().intValue() != BetResultStatus.INIT) {
			CbsSettleLog.info(String.format("##bb[%d]-jc-bet[%d] resettle", contestId, bet.getbId()));
			continue;
		    }
		    AlgorithmResult ret = null;
		    if (cancleFlag) {
			// 取消比赛按走盘结算
			ret = new AlgorithmResult();
			ret.setAmount(bet.getBet());
			if (bet.getSupport().intValue() == BetStatus.HOME) {
			    ret.setOdds(bet.getHomeRoi());
			} else if (bet.getSupport().intValue() == BetStatus.AWAY) {
			    ret.setOdds(bet.getAwayRoi());
			} else if (bet.getSupport().intValue() == BetStatus.DRAW) {
			    ret.setOdds(bet.getDrawRoi());
			}
			ret.setProfit(0);
			ret.setStatus(BetResultStatus.DRAW);
		    } else {
			// 按照玩法计算奖励
			ret = role.settleBet(bet.getBet(), bet.getSupport(), contest.getHomeScores(),
			        contest.getAwayScores(), bet.getHandicap(), bet.getHomeRoi(), bet.getAwayRoi());
		    }
		    settleUserBet(ret, contest, bet, contestName, PlayType.BB_JC.getId(), "让球胜平负");
		    count++;
		    // 结算成就相关
		    try {
			SettleAchieveBO bo = new SettleAchieveBO();
			bo.setBehavior_type(BehaviorConstants.BehaviorType.SETTLE_TYPE);
			bo.setUser_id(bet.getUserId());
			bo.setAway_team(contest.getAwayTeam());
			bo.setAway_name(awayName);
			bo.setAway_country_id(awayCountryId);
			bo.setAway_score(contest.getAwayScores());
			bo.setBet_id(bet.getbId());
			Double betRoi = null;
			if (bet.getSupport() == BetStatus.HOME)
			    betRoi = bet.getHomeRoi();
			else if (bet.getSupport() == BetStatus.AWAY)
			    betRoi = bet.getAwayRoi();
			bo.setBet_roi(betRoi);
			bo.setBet_value(bet.getBet());
			bo.setContest_id(contest.getContestId());
			bo.setContest_type(ContestType.BASKETBALL);
			bo.setCreate_time(bet.getCreateTime() != null ? format.format(bet.getCreateTime()) : null);
			bo.setCupId(contest.getCupId());
			bo.setHandicap(bet.getHandicap());
			bo.setHome_team(contest.getHomeTeam());
			bo.setHome_name(homeName);
			bo.setHome_country_id(homeCountryId);
			bo.setHome_score(contest.getHomeScores());
			bo.setPlay_type(PlayType.BB_JC.getId());
			bo.setStartTime(format.format(contest.getStartTime()));
			bo.setSupport(bet.getSupport());
			bo.setStatus(bet.getStatus());
			bo.setBack(bet.getBack());
			dataList.add(bo);
		    } catch (Throwable t) {
			CbsSettleLog.error(
			        "userId=" + bet.getUserId() + ", achieve settle data analysis failed: " + t.getMessage(), t);
		    }
		}
		// 结算行为批量发送
		if (dataList.size() > 0) {
		    try {
			achieveService.addAchieveData(JsonUtils.toJsonString(dataList), true);
		    } catch (Throwable t) {
			CbsSettleLog.error("add achieve settle data failed: " + t.getMessage(), t);
		    }
		}
	    }

	} while (bets != null && bets.size() > 0);

	// 结算后标记比赛
	contest.setSettle(contest.getSettle() | ContestContainOdds_BB.BB_RQSPF);
	boolean flag = bbContestDao.updateSettle(contest);
	if (!flag) {
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}

	// 关闭赔率记录
	bbJcDao.closeOdds(contest.getContestId());
	return count;
    }

    /**
     * 结算大小球下单 cancelFlag 比赛是否为取消 为true 认为是取消比赛
     */
    private int settleContestSize(BbContest contest, String contestName, boolean cancelFlag, String homeName,
	    String awayName, Integer homeCountryId, Integer awayCountryId) throws L99IllegalParamsException,
	    L99IllegalDataException, JSONException {

	Long startId = null;
	List<BetSize> bets;
	int count = 0;
	AlgorithmRole role = AlgorithmFactory.createContextRole(PlayType.BB_SIZE);
	FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
	do {
	    bets = bbBetSizeDao.findBbBetSize(contest.getContestId(), null, true, startId, ContestConstants.SETTLE_LIMIT);
	    if (bets.size() > 0) {
		List<SettleAchieveBO> dataList = new ArrayList<>(bets.size());
		for (BetSize bet : bets) {
		    startId = bet.getbId();
		    Long contestId = contest.getContestId();
		    if (bet.getStatus() != BetResultStatus.INIT) {
			CbsSettleLog.info(String.format("##bb[%d]-size-bet[%d] resettle", contestId, bet.getbId()));
			continue;
		    }
		    AlgorithmResult ret;
		    if (cancelFlag) {
			// 取消比赛按走盘结算
			ret = new AlgorithmResult();
			ret.setAmount(bet.getBet());
			if (bet.getSupport() == BetStatus.HOME) {
			    ret.setOdds(bet.getBigRoi());
			} else if (bet.getSupport() == BetStatus.AWAY) {
			    ret.setOdds(bet.getTinyRoi());
			}
			ret.setProfit(0);
			ret.setStatus(BetResultStatus.DRAW);
		    } else {
			// 按照玩法计算奖励
			ret = role.settleBet(bet.getBet(), bet.getSupport(), contest.getHomeScores(),
			        contest.getAwayScores(), bet.getHandicap(), bet.getBigRoi(), bet.getTinyRoi());
		    }
		    settleUserBet(ret, contest, bet, contestName, PlayType.BB_SIZE.getId(), "大小球");
		    count++;
		    // 结算成就相关
		    try {
			SettleAchieveBO bo = new SettleAchieveBO();
			bo.setBehavior_type(BehaviorConstants.BehaviorType.SETTLE_TYPE);
			bo.setUser_id(bet.getUserId());
			bo.setAway_team(contest.getAwayTeam());
			bo.setAway_name(awayName);
			bo.setAway_country_id(awayCountryId);
			bo.setAway_score(contest.getAwayScores());
			bo.setBet_id(bet.getbId());
			Double betRoi = null;
			if (bet.getSupport() == BetStatus.HOME)
			    betRoi = bet.getBigRoi();
			else if (bet.getSupport() == BetStatus.AWAY)
			    betRoi = bet.getTinyRoi();
			bo.setBet_roi(betRoi);
			bo.setBet_value(bet.getBet());
			bo.setContest_id(contest.getContestId());
			bo.setContest_type(ContestType.BASKETBALL);
			bo.setCreate_time(bet.getCreateTime() != null ? format.format(bet.getCreateTime()) : null);
			bo.setCupId(contest.getCupId());
			bo.setHandicap(bet.getHandicap());
			bo.setHome_team(contest.getHomeTeam());
			bo.setHome_name(homeName);
			bo.setHome_country_id(homeCountryId);
			bo.setHome_score(contest.getHomeScores());
			bo.setPlay_type(PlayType.BB_SIZE.getId());
			bo.setStartTime(format.format(contest.getStartTime()));
			bo.setSupport(bet.getSupport());
			bo.setStatus(bet.getStatus());
			bo.setBack(bet.getBack());
			dataList.add(bo);
		    } catch (Throwable t) {
			CbsSettleLog.error(
			        "userId=" + bet.getUserId() + ", achieve settle data analysis failed: " + t.getMessage(), t);
		    }
		}
		// 结算行为批量发送
		if (dataList.size() > 0) {
		    try {
			achieveService.addAchieveData(JsonUtils.toJsonString(dataList), true);
		    } catch (Throwable t) {
			CbsSettleLog.error("add achieve settle data failed: " + t.getMessage(), t);
		    }
		}
	    }

	} while (bets != null && bets.size() > 0);

	// 结算后标记比赛
	contest.setSettle(contest.getSettle() | ContestContainOdds_BB.BB_SIZE);
	boolean flag = bbContestDao.updateSettle(contest);
	if (!flag) {
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}

	// 关闭赔率记录
	bbOddsSizeDao.closeOdds(contest.getContestId());
	return count;
    }

    /**
     * 结算单双数 cancelFlag 比赛是否为取消 为true 认为是取消比赛
     */
    private int settleContestOddeven(BbContest contest, String contestName, boolean cancelFlag, String homeName,
	    String awayName, Integer homeCountryId, Integer awayCountryId) throws L99IllegalParamsException,
	    L99IllegalDataException, JSONException {

	Long startId = null;
	List<BetOddeven> bets;
	int count = 0;
	AlgorithmRole role = AlgorithmFactory.createContextRole(PlayType.BB_ODDEVEN);
	FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
	do {
	    bets = bbBetOddevenDao.findBbBetOddeven(contest.getContestId(), null, true, startId,
		    ContestConstants.SETTLE_LIMIT);
	    if (bets.size() > 0) {
		List<SettleAchieveBO> dataList = new ArrayList<>(bets.size());
		for (BetOddeven bet : bets) {
		    startId = bet.getbId();
		    Long contestId = contest.getContestId();
		    if (bet.getStatus() != BetResultStatus.INIT) {
			CbsSettleLog.info(String.format("##bb[%d]-oddeven-bet[%d] resettle", contestId, bet.getbId()));
			continue;
		    }
		    AlgorithmResult ret;
		    if (cancelFlag) {
			// 取消比赛按走盘结算
			ret = new AlgorithmResult();
			ret.setAmount(bet.getBet());
			if (bet.getSupport() == BetStatus.HOME) {
			    ret.setOdds(bet.getOddRoi());
			} else if (bet.getSupport() == BetStatus.AWAY) {
			    ret.setOdds(bet.getEvenRoi());
			}
			ret.setProfit(0);
			ret.setStatus(BetResultStatus.DRAW);
		    } else {
			// 按照玩法计算奖励
			ret = role.settleBet(bet.getBet(), bet.getSupport(), contest.getHomeScores(),
			        contest.getAwayScores());
		    }
		    settleUserBet(ret, contest, bet, contestName, PlayType.BB_ODDEVEN.getId(), "单双数");
		    count++;
		    // 结算成就相关
		    try {
			SettleAchieveBO bo = new SettleAchieveBO();
			bo.setBehavior_type(BehaviorConstants.BehaviorType.SETTLE_TYPE);
			bo.setUser_id(bet.getUserId());
			bo.setAway_team(contest.getAwayTeam());
			bo.setAway_name(awayName);
			bo.setAway_country_id(awayCountryId);
			bo.setAway_score(contest.getAwayScores());
			bo.setBet_id(bet.getbId());
			Double betRoi = null;
			if (bet.getSupport() == BetStatus.HOME)
			    betRoi = bet.getOddRoi();
			else if (bet.getSupport() == BetStatus.AWAY)
			    betRoi = bet.getEvenRoi();
			bo.setBet_roi(betRoi);
			bo.setBet_value(bet.getBet());
			bo.setContest_id(contest.getContestId());
			bo.setContest_type(ContestType.BASKETBALL);
			bo.setCreate_time(bet.getCreateTime() != null ? format.format(bet.getCreateTime()) : null);
			bo.setCupId(contest.getCupId());
			bo.setHome_team(contest.getHomeTeam());
			bo.setHome_name(homeName);
			bo.setHome_country_id(homeCountryId);
			bo.setHome_score(contest.getHomeScores());
			bo.setPlay_type(PlayType.BB_ODDEVEN.getId());
			bo.setStartTime(format.format(contest.getStartTime()));
			bo.setSupport(bet.getSupport());
			bo.setStatus(bet.getStatus());
			bo.setBack(bet.getBack());
			dataList.add(bo);
		    } catch (Throwable t) {
			CbsSettleLog.error(
			        "userId=" + bet.getUserId() + ", achieve settle data analysis failed: " + t.getMessage(), t);
		    }
		}
		// 结算行为批量发送
		if (dataList.size() > 0) {
		    try {
			achieveService.addAchieveData(JsonUtils.toJsonString(dataList), true);
		    } catch (Throwable t) {
			CbsSettleLog.error("add achieve settle data failed: " + t.getMessage(), t);
		    }
		}
	    }

	} while (bets != null && bets.size() > 0);

	// 结算后标记比赛
	contest.setSettle(contest.getSettle() | ContestContainOdds_BB.BB_ODDEVEN);
	boolean flag = bbContestDao.updateSettle(contest);
	if (!flag) {
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}

	// 关闭赔率记录
	// bbOddsSizeDao.closeOdds(contest.getContestId());
	return count;
    }

    /**
     * 个人结算下单方法
     * 
     * @param contest
     * @param bet
     * @param contestName
     * @param cancleFlag
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     * @throws JSONException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    private void settleUserBet(AlgorithmResult ret, BbContest contest, Bet bet, String contestName, int playId,
	    String betName) throws L99IllegalParamsException, L99IllegalDataException, JSONException {
	// 修改下单记录状态
	bet.setStatus(ret.getStatus());
	if (ret.getStatus() == BetResultStatus.LOSS) { // 输盘
	    double profit = ret.getProfit();
	    if (profit > 0 && bet.isLongbi() && bet.getCoupon() != null && bet.getCoupon() > 0) { // 混合下单输盘按比例退
		double longbiBet = CommerceMath.sub(bet.getBet(), bet.getCoupon());
		profit = CommerceMath.mul(profit, CommerceMath.div(longbiBet, bet.getBet()));
	    }
	    bet.setBack(profit);
	} else if (ret.getStatus() == BetResultStatus.WIN) { // 赢盘
	    bet.setBack(CommerceMath.add(ret.getAmount(), ret.getProfit()));
	} else { // 走盘
	    double amount = ret.getAmount();
	    if (bet.isLongbi() && bet.getCoupon() != null && bet.getCoupon() > 0) { // 混合下单走盘按比例退龙币
		double longbiBet = CommerceMath.sub(bet.getBet(), bet.getCoupon());
		amount = CommerceMath.div(CommerceMath.mul(amount, longbiBet), bet.getBet());
	    }
	    bet.setBack(amount);
	}

	boolean flag = false;
	if (playId == PlayType.BB_SPF.getId()) {
	    flag = bbBetOpDao.updateSettle(bet.getbId(), bet.getBack(), bet.getStatus());
	} else if (playId == PlayType.BB_JC.getId()) {
	    flag = bbBetJcDao.updateSettle(bet.getbId(), bet.getBack(), bet.getStatus());
	} else if (playId == PlayType.BB_SIZE.getId()) {
	    flag = bbBetSizeDao.updateSettle(bet.getbId(), bet.getBack(), bet.getStatus());
	} else if (playId == PlayType.BB_ODDEVEN.getId()) {
	    flag = bbBetOddevenDao.updateSettle(bet.getbId(), bet.getBack(), bet.getStatus());
	}

	if (!flag) {
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}

	settleMoneyToUser(bet, contest, ret.getOdds(), contestName, playId, betName);

    }

    /**
     * 返钱给用户 发消息提醒 并更新猜友圈
     * 
     * @param bet
     * @param contest
     * @param odds
     * @param contestName
     * @param playId
     * @param betName
     * @throws L99IllegalDataException
     * @throws L99IllegalParamsException
     * @throws JSONException
     */
    private void settleMoneyToUser(Bet bet, BbContest contest, Double odds, String contestName, int playId, String betName)
	    throws L99IllegalDataException, L99IllegalParamsException, JSONException {
	Long contestId = contest.getContestId();
	Long payLogId = null;
	// 添加龙币筹码
	if (bet.getBack() > 0) {
	    String desc = String.format("在『%s』的『%s』局中下单结算", contestName, betName);
	    if (bet.isLongbi()) {
		payLogId = moneyService.earnMoney(bet.getUserId(), bet.getBack(), desc, bet.getIpaddress(), playId, bet
		        .getbId().toString());
		// add by lhx on 16-04-13
		moneyStatisticService.insertUserConsumer(bet.getUserId() + "", bet.getBack());
	    } else { // 结算龙筹
		payLogId = couponUserService.settleCouponByBack(bet.getUserId(), bet.getBack(), desc);
	    }
	}

	// 结算记录
	UserSettleLog settleLog = new UserSettleLog();
	settleLog.setContestType(ContestType.BASKETBALL);
	settleLog.setUserId(bet.getUserId());
	settleLog.setContestId(contestId);
	settleLog.setPlayId(playId);
	settleLog.setSupport(bet.getSupport());
	settleLog.setOdds(odds);
	settleLog.setBet(bet.getBet());
	settleLog.setBack(bet.getBack());
	settleLog.setStatus(bet.getStatus());
	settleLog.setContentId(bet.getContentId());
	settleLog.setContestTime(contest.getStartTime());
	settleLog.setCreateTime(new Date());
	settleLog.setLongbi(bet.isLongbi());
	settleLog.setPayLogId(payLogId);

	boolean flag = userSettleLogDao.insert(settleLog);
	if (!flag) {
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}

	String homeName = "undefined", awayName = "undefined";
	BbTeam homeTeam = bbTeamDao.selectById(contest.getHomeTeam());
	homeName = homeTeam.getName();
	BbTeam awayTeam = bbTeamDao.selectById(contest.getAwayTeam());
	awayName = awayTeam.getName();
	JSONObject params = new JSONObject();
	params.put("start_time", TimeUtil.getUtcTimeForDate(contest.getStartTime()));
	params.put("home_name", homeName);
	params.put("away_name", awayName);
	params.put("play_type", playId);
	params.put("bet_odds", odds);

	// 下单金额
	String bet_val = "";
	if (bet.getCoupon() != null && bet.getCoupon() > 0) {
	    bet_val = String.format("%d龙筹", bet.getCoupon());
	}
	if (bet.isLongbi()) {
	    bet_val = String.format("%s%s龙币", bet_val, CommerceMath.sub(bet.getBet(), bet.getCoupon()));
	}
	params.put("bet_val", bet_val);
	// 下单结果
	String back_val = "";
	Double back = 0D;
	if (bet.getStatus() == BetResultStatus.LOSS) {
	    back_val = "失败。";
	    back = bet.getBack();
	} else if (bet.getStatus() == BetResultStatus.WIN) {
	    back = CommerceMath.sub(bet.getBack(), bet.getBet());
	    if (bet.isLongbi() && bet.getCoupon() != null && bet.getCoupon() > 0) { // 混合局龙筹算盈利
		back = CommerceMath.add(back, bet.getCoupon());
	    } else if (!bet.isLongbi()) {// 纯龙筹场向上取整
		back = CommerceMath.mul(bet.getBet(), odds);
		back = BetConstants.getCouponPriceByBack(back) * 1D - bet.getBet();
	    }
	    if (bet.isLongbi()) {
		back_val = String.format("胜利,净赢%s%s", back, "龙币");
	    } else {
		back_val = String.format("胜利,净赢%s%s", back.intValue(), "龙筹");
	    }

	} else if (bet.getStatus() == BetResultStatus.DRAW) {
	    back = bet.getBack();
	    if (bet.isLongbi()) {
		back_val = String.format("走盘,返回%s%s", back, "龙币");
	    } else {
		back_val = String.format("走盘,返回%s%s", back.intValue(), "龙筹");
	    }
	}
	params.put("back_val", back_val);
	params.put("back", back);

	// 发送结算通知
	notifyService.addNotify(TempletId.BET_RESULT, bet.getUserId(), 0L, params.toString(), bet.getContentId());

	// 微信下单发送公众号消息
	if ("WEIXIN".equals(bet.getClient())) {
	    try {

		String playName = PlayType.findPlayTypeByIdAndType(playId, ContestConstants.ContestType.BASKETBALL)
		        .getName();

		// 获取用户信息
		SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy年MM月dd日");
		params.put("create_time", formatDateTime.format(bet.getCreateTime()));
		params.put("start_time", formatDate.format(contest.getStartTime()));
		params.put("bId", "D" + RandomUtils.numLongEncoder(bet.getbId()));
		params.put("play_name", playName);
		params.put("user_id", bet.getUserId());

		params.put("contest_type", ContestConstants.ContestType.BASKETBALL);
		params.put("temp_id", WeixinNotifyTemplate.ORDER_OVER_TEMPID);

		kafkaMessageSender.sendText(params.toString(), WeixinNotifyUtil.KAFKA_VER);
		CbsSettleLog
		        .info(String.format("weixin kafka message %s,%s", WeixinNotifyUtil.KAFKA_VER, params.toString()));
	    } catch (Exception e) {
		CbsSettleLog.error("userId=" + bet.getUserId() + ", send weixin template fail: " + e.getMessage(), e);
	    }

	}

	CbsSettleLog.info(String.format("##bb[%d]-bet[%d][%s] back %s", contestId, bet.getbId(), betName, bet.getBack()));

    }

}
