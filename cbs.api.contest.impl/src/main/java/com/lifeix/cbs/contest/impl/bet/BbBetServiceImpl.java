package com.lifeix.cbs.contest.impl.bet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lifeix.cbs.achieve.bean.bo.BetAchieveBO;
import com.lifeix.cbs.achieve.common.constant.BehaviorConstants;
import com.lifeix.cbs.achieve.service.AchieveService;
import com.lifeix.cbs.api.bean.coupon.CouponUserResponse;
import com.lifeix.cbs.api.bean.gold.GoldResponse;
import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.exception.MsgCode.BetMsg;
import com.lifeix.cbs.api.common.exception.MsgCode.CouponMsg;
import com.lifeix.cbs.api.common.mission.Mission;
import com.lifeix.cbs.api.common.util.BetConstants;
import com.lifeix.cbs.api.common.util.BetConstants.BetResultStatus;
import com.lifeix.cbs.api.common.util.BetConstants.BetStatus;
import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.api.common.util.CommerceMath;
import com.lifeix.cbs.api.common.util.ContentConstants.PostType;
import com.lifeix.cbs.api.common.util.ContestConstants;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestStatu;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestType;
import com.lifeix.cbs.api.common.util.CouponConstants.CouponSystem;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.common.util.PlayType;
import com.lifeix.cbs.api.common.util.RandomUtils;
import com.lifeix.cbs.api.service.coupon.CouponUserService;
import com.lifeix.cbs.api.service.misson.MissionUserService;
import com.lifeix.cbs.api.service.money.MoneyService;
import com.lifeix.cbs.api.service.money.MoneyStatisticService;
import com.lifeix.cbs.api.service.user.CbsUserService;
import com.lifeix.cbs.contest.bean.bet.BetLogListResponse;
import com.lifeix.cbs.contest.bean.bet.BetLogResponse;
import com.lifeix.cbs.contest.bean.fb.ContestResponse;
import com.lifeix.cbs.contest.bean.odds.OddsDssResponse;
import com.lifeix.cbs.contest.dao.bb.BbBetJcDao;
import com.lifeix.cbs.contest.dao.bb.BbBetOddevenDao;
import com.lifeix.cbs.contest.dao.bb.BbBetOpDao;
import com.lifeix.cbs.contest.dao.bb.BbBetSizeDao;
import com.lifeix.cbs.contest.dao.bb.BbContestDao;
import com.lifeix.cbs.contest.dao.bb.BbOddsJcDao;
import com.lifeix.cbs.contest.dao.bb.BbOddsOpDao;
import com.lifeix.cbs.contest.dao.bb.BbOddsSizeDao;
import com.lifeix.cbs.contest.dao.bb.BbTeamDao;
import com.lifeix.cbs.contest.dao.circle.FriendCircleDao;
import com.lifeix.cbs.contest.dao.contest.RepairBetDao;
import com.lifeix.cbs.contest.dao.contest.RollbackDao;
import com.lifeix.cbs.contest.dto.bb.BbContest;
import com.lifeix.cbs.contest.dto.bb.BbTeam;
import com.lifeix.cbs.contest.dto.bet.BetJc;
import com.lifeix.cbs.contest.dto.bet.BetOddeven;
import com.lifeix.cbs.contest.dto.bet.BetOp;
import com.lifeix.cbs.contest.dto.bet.BetRepair;
import com.lifeix.cbs.contest.dto.bet.BetSize;
import com.lifeix.cbs.contest.dto.circle.FriendCircle;
import com.lifeix.cbs.contest.dto.odds.OddsJc;
import com.lifeix.cbs.contest.dto.odds.OddsOp;
import com.lifeix.cbs.contest.dto.odds.OddsSize;
import com.lifeix.cbs.contest.impl.ImplSupport;
import com.lifeix.cbs.contest.impl.redis.RedisBetCountHandler;
import com.lifeix.cbs.contest.service.bb.BbContestService;
import com.lifeix.cbs.contest.service.bet.BbBetService;
import com.lifeix.cbs.contest.service.cirle.FriendCircleService;
import com.lifeix.cbs.contest.util.WeixinNotifyTemplate;
import com.lifeix.cbs.contest.util.WeixinNotifyUtil;
import com.lifeix.cbs.contest.util.transform.BbOddsDssDateUtil;
import com.lifeix.cbs.message.service.notify.NotifyService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.framework.kafka.producer.MessageSender;
import com.lifeix.user.beans.CustomResponse;

/**
 * 下单逻辑
 * 
 * @author lifeix-sz
 */
@Service("bbBetService")
public class BbBetServiceImpl extends ImplSupport implements BbBetService {

    @Autowired
    private MoneyService moneyService;

    @Autowired
    private CouponUserService couponUserService;

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
    FriendCircleService friendCircleService;

    @Autowired
    private CbsUserService cbsUserService;

    @Autowired
    private BbContestService bbContestService;

    @Autowired
    private RedisBetCountHandler redisBetCountHandler;

    @Autowired
    protected AchieveService achieveService;

    @Autowired
    protected RollbackDao rollbackDao;

    @Autowired
    protected MoneyStatisticService moneyStatisticService;

    @Resource
    private MessageSender kafkaMessageSender;

    @Autowired
    private MissionUserService missionUserService;

    @Autowired
    private FriendCircleDao friendCircleDao;

    @Autowired
    private RepairBetDao repairBetDao;

    @Autowired
    private NotifyService notifyService;

    @Override
    public CustomResponse addBBBet(Long userId, Long contestId, boolean longbi, Integer playId, Integer support, Double r1,
	    Double r2, Double r3, Double bet, String content, String client, String ipaddress, Boolean permission,
	    Long couponUserId) throws L99IllegalParamsException, L99IllegalDataException, JSONException {

	// 参数验证
	ParamemeterAssert.assertDataNotNull(userId);
	ParamemeterAssert.assertDataNotNull(contestId);
	ParamemeterAssert.assertDataNotNull(playId);
	ParamemeterAssert.assertDataNotNull(support);
	ParamemeterAssert.assertDataNotNull(r1);
	ParamemeterAssert.assertDataNotNull(r3);

	// 校验玩法类型是否支持(篮球)
	if (!PlayType.verifyType(playId, ContestType.BASKETBALL)) {
	    throw new L99IllegalParamsException(BetMsg.CODE_BET_NOT_SUPPORT, BetMsg.KEY_BET_NOT_SUPPORT);
	}

	// 校验支持方是否合法
	if (!BetStatus.verifySupport(support, playId)) {
	    throw new L99IllegalParamsException(BetMsg.CODE_BET_NOT_SUPPORT, BetMsg.KEY_BET_NOT_SUPPORT);
	}

	if (BetStatus.DRAW == support) {
	    // 篮球不支持平局
	    throw new L99IllegalParamsException(BetMsg.CODE_BET_NOT_SUPPORT, BetMsg.KEY_BET_NOT_SUPPORT);
	}

	BbContest contest = bbContestDao.selectById(contestId);
	// 比赛不可下单
	if (contest == null) {
	    throw new L99IllegalParamsException(BetMsg.CODE_BET_CANT, BetMsg.KEY_BET_CANT);
	}
	// 比赛已经开始，不可下单
	if (contest.getStatus() != ContestStatu.NOTOPEN || (contest.getStartTime().compareTo(new Date()) < 0)) {
	    throw new L99IllegalParamsException(BetMsg.CODE_BET_CONTEST_BEGIN, BetMsg.KEY_BET_CONTEST_BEGIN);
	}
	// 纯龙筹场必须传递龙筹券
	if (!longbi) {
	    ParamemeterAssert.assertDataNotNull(couponUserId);
	} else { // 龙币场必须传递龙币
	    ParamemeterAssert.assertDataNotNull(bet);
	}

	// 计算下单可获得奖励
	double backRoi = 0.0;
	if (support == BetStatus.HOME) {
	    backRoi = r1;
	} else if (support == BetStatus.AWAY) {
	    backRoi = r3;
	} else if (support == BetStatus.DRAW) {
	    backRoi = r2;
	}

	// 每场比赛的玩法只允许一次混合下单
	if (longbi && couponUserId != null) {
	    int mixCount = 0;
	    if (PlayType.BB_SPF.getId() == playId.intValue()) {
		mixCount = bbBetOpDao.countUserMixBet(contestId, userId);
	    } else if (PlayType.BB_JC.getId() == playId.intValue()) {
		mixCount = bbBetJcDao.countUserMixBet(contestId, userId);
	    } else if (PlayType.BB_SIZE.getId() == playId) {
		mixCount = bbBetSizeDao.countUserMixBet(contestId, userId);
	    } else if (PlayType.BB_ODDEVEN.getId() == playId) {
		mixCount = bbBetOddevenDao.countUserMixBet(contestId, userId);
	    }
	    if (mixCount > 0) {
		throw new L99IllegalParamsException(BetMsg.CODE_BET_MIX_REPEAT, BetMsg.KEY_BET_MIX_REPEAT);
	    }

	    // 混合下单必须大于1.7赔率
	    if (backRoi < 1.7D) {
		throw new L99IllegalParamsException(BetMsg.CODE_BET_MIX_LIMIT, BetMsg.KEY_BET_MIX_LIMIT);
	    }
	}

	// 龙筹券面额
	double over = 0D;
	int couponPrice = 0;

	if (couponUserId != null) {
	    // 检查龙筹券是否有效
	    CouponUserResponse couponUser = couponUserService.findCouponDetail(couponUserId, true);

	    // 判断龙筹券是否是自己拥有
	    if (couponUser.getUser_id().longValue() != userId.longValue()) {
		throw new L99IllegalDataException(CouponMsg.CODE_COUPON_FAIL, CouponMsg.KEY_COUPON_FAIL);
	    }

	    couponPrice = couponUser.getPrice();

	    // 纯龙筹不允许下100
	    if (couponPrice == CouponSystem.PIRCE_100 && !longbi) {
		throw new L99IllegalDataException(CouponMsg.CODE_COUPON_100_NOT_RANGE, CouponMsg.KEY_COUPON_100_NOT_RANGE);
	    }

	}

	// 下单金额
	String bet_val = "";

	if (couponPrice > 0) {
	    bet_val = String.format("%s龙筹", couponPrice);
	}
	if (longbi) { // 龙筹 + 龙币场
	    GoldResponse gold = moneyService.viewUserMoney(userId, ipaddress);
	    over = CommerceMath.sub(gold.getBalance(), gold.getFrozen());

	    // 判断是否有足够的龙币
	    if (CommerceMath.sub(over, bet) < 0) {
		throw new L99IllegalDataException(BasicMsg.CODE_NOT_MONEY, BasicMsg.KEY_NOT_MONEY);
	    }
	    bet_val = String.format("%s%s龙币", bet_val, bet);
	    bet = CommerceMath.add(couponPrice, bet);
	} else { // 纯龙筹场
	    bet = couponPrice * 1.0D;
	}

	// 不允许下单超过1000
	if (bet > 1000) {
	    throw new L99IllegalDataException(BetMsg.CODE_BET_UP_LIMIT, BetMsg.KEY_BET_UP_LIMIT);
	}

	JSONObject paramObj = new JSONObject();
	// 获取比赛名称 用于记录日志
	String homeName = "undefined", awayName = "undefined";
	Map<Long, BbTeam> teams = bbTeamDao.findBbTeamMapByIds(Arrays.asList(contest.getHomeTeam(), contest.getAwayTeam()));
	BbTeam homeBbTeam = teams.get(contest.getHomeTeam());
	if (homeBbTeam != null) {
	    homeName = homeBbTeam.getName();
	    try {
		// 主队名
		paramObj.put("hn", homeName);
		// 主队logo
		paramObj.put("hl", homeBbTeam.getLogo());
	    } catch (JSONException e) {
		LOG.error(e.getMessage(), e);
	    }
	}
	BbTeam awayBbTeam = teams.get(contest.getAwayTeam());
	if (awayBbTeam != null) {
	    awayName = awayBbTeam.getName();
	    try {
		// 主队名
		paramObj.put("an", awayName);
		// 主队logo
		paramObj.put("al", awayBbTeam.getLogo());
	    } catch (JSONException e) {
		LOG.error(e.getMessage(), e);
	    }
	}
	String contestName = String.format("%sVS%s", homeName, awayName);

	Long cupId = contest.getCupId();
	JSONObject betRet = null;
	// 获取赔率，是否封盘
	// 在contestId,userId,support三个字段上建立唯一约束，每场比赛，每个用户，每个支持方只能下单一次
	if (PlayType.BB_SPF.getId() == playId.intValue()) {
	    betRet = betBBOp(userId, contestId, cupId, longbi, support, r1, r2, r3, bet, couponPrice, couponUserId,
		    contestName, ipaddress, client);
	} else if (PlayType.BB_JC.getId() == playId.intValue()) {
	    betRet = betBBJc(userId, contestId, cupId, longbi, support, r1, r2, r3, bet, couponPrice, couponUserId,
		    contestName, ipaddress, client);
	} else if (PlayType.BB_SIZE.getId() == playId) {
	    betRet = betBBSize(userId, contestId, cupId, longbi, support, r1, r3, bet, couponPrice, couponUserId,
		    contestName, ipaddress, client);
	} else if (PlayType.BB_ODDEVEN.getId() == playId) {
	    betRet = betBBOddeven(userId, contestId, cupId, longbi, support, r1, r3, bet, couponPrice, couponUserId,
		    contestName, ipaddress, client);
	}
	if (betRet == null) {
	    throw new L99IllegalDataException(BetMsg.CODE_BET_FAIL, BetMsg.KEY_BET_FAIL);
	}

	Long bId = null;
	CustomResponse response = new CustomResponse();
	Long contentId = null;

	// 下单发表竞猜
	try {
	    bId = betRet.getLong("bId");
	    response.put("bId", bId);
	    // 联赛名称
	    paramObj.put("cName", contest.getCupName());
	    // 联赛主色调
	    paramObj.put("color", contest.getColor());
	    // 下单的id
	    paramObj.put("bId", bId);
	    // 玩法
	    paramObj.put("playId", playId);
	    // 下单选择
	    paramObj.put("support", support);
	    // 比赛开始时间
	    paramObj.put("time", CbsTimeUtils.getUtcTimeForDate(contest.getStartTime()));
	    paramObj.put("r1", r1);
	    if (r2 != null && !Double.isNaN(r2)) {
		paramObj.put("r2", r2);
	    }
	    paramObj.put("r3", r3);
	    paramObj.put("isLongbi", longbi);
	    if (PlayType.BB_JC.getId() == playId) {
		paramObj.put("handicap", betRet.optDouble("handicap", 0.0));
	    } else if (PlayType.BB_SIZE.getId() == playId) {
		paramObj.put("handicap", betRet.optDouble("handicap", 0.0));
	    }

	    // 下单金额
	    paramObj.put("bet", bet);

	    // 接入猜友圈
	    contentId = friendCircleService.postContent(userId, PostType.GUESS, content, "", null, contestId,
		    ContestType.BASKETBALL, paramObj.toString(), client, permission, couponPrice);
	    response.put("contentId", contentId);
	    if (PlayType.BB_SPF.getId() == playId) {
		BetOp op = new BetOp();
		op.setbId(bId);
		op.setContentId(contentId);
		bbBetOpDao.updateContentId(op);
	    } else if (PlayType.BB_JC.getId() == playId) {
		BetJc jc = new BetJc();
		jc.setbId(bId);
		jc.setContentId(contentId);
		bbBetJcDao.updateContentId(jc);
	    } else if (PlayType.BB_SIZE.getId() == playId) {
		BetSize betSize = new BetSize();
		betSize.setbId(bId);
		betSize.setContentId(contentId);
		bbBetSizeDao.updateContentId(betSize);
	    } else if (PlayType.BB_ODDEVEN.getId() == playId) {
		BetOddeven betOddeven = new BetOddeven();
		betOddeven.setbId(bId);
		betOddeven.setContentId(contentId);
		bbBetOddevenDao.updateContentId(betOddeven);
	    }
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}

	// 下单预盈利
	double back = CommerceMath.mul(bet, backRoi);
	if (!longbi) { // 纯龙筹场向上取整
	    back = BetConstants.getCouponPriceByBack(back);
	}

	response.put("back", back);
	Double betRoi = null;
	Double handicap = null;
	String supportName = "";
	// 下单成就相关
	try {
	    BetAchieveBO bo = new BetAchieveBO();
	    bo.setBehavior_type(BehaviorConstants.BehaviorType.BET_TYPE);
	    bo.setUser_id(userId);
	    bo.setAway_team(contest.getAwayTeam());
	    bo.setAway_name(awayName);
	    bo.setBet_id(bId);

	    if (PlayType.BB_SPF.getId() == playId) {
		if (support == BetStatus.HOME) {
		    betRoi = r1;
		    supportName = "主胜";
		} else if (support == BetStatus.AWAY) {
		    betRoi = r3;
		    supportName = "客胜";
		}
	    } else if (PlayType.BB_JC.getId() == playId) {
		handicap = betRet.optDouble("handicap", 0d);
		if (support == BetStatus.HOME) {
		    betRoi = r1;
		    supportName = "主胜";
		} else if (support == BetStatus.AWAY) {
		    betRoi = r3;
		    supportName = "客胜";
		}

		if (handicap != 0) {
		    supportName = String.format("%s(%s)", supportName, handicap);
		}
	    } else if (PlayType.BB_SIZE.getId() == playId) {
		handicap = betRet.optDouble("handicap", 0d);
		if (support == BetStatus.HOME) {
		    betRoi = r1;
		    supportName = "大球";
		} else if (support == BetStatus.AWAY) {
		    betRoi = r3;
		    supportName = "小球";
		}

		if (handicap != 0) {
		    supportName = String.format("%s(%s)", supportName, handicap);
		}
	    } else if (PlayType.BB_ODDEVEN.getId() == playId) {
		if (support == BetStatus.HOME) {
		    betRoi = r1;
		    supportName = "单数";
		} else if (support == BetStatus.AWAY) {
		    betRoi = r3;
		    supportName = "双数";
		}
	    }
	    FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
	    bo.setBet_roi(betRoi);
	    bo.setBet_value(bet);
	    bo.setContest_id(contestId);
	    bo.setContest_type(ContestType.BASKETBALL);
	    bo.setCreate_time(format.format(new Date()));
	    bo.setCupId(cupId);
	    bo.setHandicap(handicap);
	    bo.setHome_team(contest.getHomeTeam());
	    bo.setHome_name(homeName);
	    bo.setMin_flag(Double.compare(bet, 1.0) == 0);
	    bo.setPlay_type(playId);
	    bo.setStartTime(format.format(contest.getStartTime()));
	    bo.setSupport(support);
	} catch (Throwable t) {
	    LOG.error("userId=" + userId + ", bet achieve data analysis failed: " + t.getMessage(), t);
	}

	// 微信下单发送消息提醒
	if ("WEIXIN".equals(client)) {
	    try {

		String playName = PlayType.findPlayTypeByIdAndType(playId, ContestConstants.ContestType.BASKETBALL)
		        .getName();

		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy年MM月dd日");
		JSONObject params = new JSONObject();
		params.put("start_time", formatDate.format(contest.getStartTime()));
		params.put("home_name", homeName);
		params.put("away_name", awayName);
		params.put("play_name", playName);
		params.put("bet_odds", betRoi);
		params.put("bId", "D" + RandomUtils.numLongEncoder(bId));
		params.put("bet_val", bet_val);

		params.put("user_id", userId);
		params.put("support_name", supportName);
		params.put("contest_type_name", "篮球");
		if (contentId != null) {
		    params.put("circle_id", contentId);
		}

		params.put("contest_type", ContestConstants.ContestType.BASKETBALL);
		params.put("temp_id", WeixinNotifyTemplate.ORDER_PAY_TEMPID);

		//LOG.info("weixin01:" + contentId);
		kafkaMessageSender.sendText(params.toString(), WeixinNotifyUtil.KAFKA_VER);
		LOG.info(String.format("weixin kafka message %s,%s", WeixinNotifyUtil.KAFKA_VER, params.toString()));
	    } catch (Exception e) {
		LOG.error("userId=" + userId + ", bet weixin message failed: " + e.getMessage(), e);
	    }
	    // kafkaMessageSender
	}

	// add by lhx on 16-07-22 每日下注（床上用户任务） start
	missionUserService.validate(userId, Mission.NYX_BET);
	// add by lhx on 16-07-22 每日下注（床上用户任务） end

	return response;
    }

    /**
     * 篮球 下单胜平负
     * 
     * @param userId
     * @param contestId
     * @param support
     * @param r1
     * @param r2
     * @param r3
     * @param bet
     * @param contestName
     * @param ipaddress
     * @return
     * @throws L99IllegalDataException
     */
    private JSONObject betBBOp(Long userId, Long contestId, Long cupId, boolean longbi, Integer support, Double r1,
	    Double r2, Double r3, Double bet, int couponPrice, Long couponUserId, String contestName, String ipaddress,
	    String client) throws L99IllegalDataException {

	OddsOp oddsOp = bbOpDao.selectByContest(contestId);
	// 已经封盘则不能下单
	if (oddsOp == null || oddsOp.getCloseFlag()) {
	    throw new L99IllegalDataException(BetMsg.CODE_BET_ODDS_CLOSE, BetMsg.KEY_BET_ODDS_CLOSE);
	}
	if (Double.compare(r1, oddsOp.getHomeRoi()) != 0 || Double.compare(r3, oddsOp.getAwayRoi()) != 0) {
	    throw new L99IllegalDataException(BetMsg.CODE_BET_ODDS_CHANGE, BetMsg.KEY_BET_ODDS_CHANGE);
	}
	BetOp betOp = bbBetOpDao.selectByBet(contestId, userId, support);
	if (betOp != null) {
	    // 已经下过注了
	    throw new L99IllegalDataException(BetMsg.CODE_BET_HAS, BetMsg.KEY_BET_HAS);
	}
	betOp = new BetOp();
	betOp.setUserId(userId);
	betOp.setContestId(contestId);
	betOp.setSupport(support);
	betOp.setCreateTime(new Date());
	betOp.setBet(bet);
	betOp.setCoupon(couponPrice);
	betOp.setBack(0.0);
	betOp.setStatus(BetResultStatus.INIT);
	betOp.setHomeRoi(oddsOp.getHomeRoi());
	betOp.setDrawRoi(oddsOp.getDrawRoi());
	betOp.setAwayRoi(oddsOp.getAwayRoi());
	betOp.setCompany(oddsOp.getCompany());
	betOp.setIpaddress(ipaddress);
	betOp.setLongbi(longbi);
	betOp.setClient(client);
	JSONObject ret = null;
	try {
	    // 先扣钱后下单
	    String supportName = null;
	    if (support == BetStatus.HOME) {
		supportName = "主胜";
	    } else if (support == BetStatus.AWAY) {
		supportName = "客胜";
	    } else if (support == BetStatus.DRAW) {
		supportName = "平局";
	    }
	    String desc = String.format("在『%s』的『%s』局中下单%s", contestName, "胜负", supportName);

	    // 消费龙筹券
	    if (couponUserId != null) {
		couponUserService.useCoupon(couponUserId, userId, longbi, CommerceMath.sub(bet, couponPrice),
		        ContestType.BASKETBALL, cupId, contestId, desc);
	    }

	    // 扣除龙币
	    if (longbi) {
		double moneyLongbi = CommerceMath.sub(bet, couponPrice);
		moneyService.consumeMoney(userId, CommerceMath.sub(bet, couponPrice), desc, ipaddress);
		// add by lhx on 16-04-13
		moneyStatisticService.insertUserConsumer(userId + "", -moneyLongbi);
	    }

	    Long bId = bbBetOpDao.insertAndGetPrimaryKey(betOp);
	    if (-1L == bId.longValue()) { // 下单出错
		desc = String.format("非常抱歉，在『%s』的『%s』局中下单%s失败,系统返回下单金额", contestName, "胜负", supportName);
		if (longbi) {
		    moneyService.systemRechargeMoney(userId, CommerceMath.sub(bet, couponPrice), desc, ipaddress);
		    // add by lhx on 16-04-13
		    moneyStatisticService.insertUserConsumer(userId + "", CommerceMath.sub(bet, couponPrice));
		}
		if (couponUserId != null) {
		    couponUserService.settleCouponByPrice(userId, couponPrice, CouponSystem.TIME_24, desc);
		}
		throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	    }

	    // 下单信息存入redis中做统计
	    redisBetCountHandler.addContestBet(ContestType.BASKETBALL, contestId, PlayType.BB_SPF.getId(), support);

	    ret = new JSONObject();
	    ret.put("bId", bId);
	} catch (L99IllegalDataException e) {
	    throw new L99IllegalDataException(e.getErrorcode(), e.getMessage());
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}
	return ret;
    }

    /**
     * 篮球 下单让球胜平负
     * 
     * @param userId
     * @param contestId
     * @param support
     * @param r1
     * @param r2
     * @param r3
     * @param bet
     * @param contestName
     * @param ipaddress
     * @return
     * @throws L99IllegalDataException
     */
    private JSONObject betBBJc(Long userId, Long contestId, Long cupId, boolean longbi, Integer support, Double r1,
	    Double r2, Double r3, Double bet, int couponPrice, Long couponUserId, String contestName, String ipaddress,
	    String client) throws L99IllegalDataException {

	OddsJc oddsJc = bbJcDao.selectByContest(contestId);
	// 已经封盘则不能下单
	if (oddsJc == null || oddsJc.getCloseFlag()) {
	    throw new L99IllegalDataException(BetMsg.CODE_BET_ODDS_CLOSE, BetMsg.KEY_BET_ODDS_CLOSE);
	}
	if (Double.compare(r1, oddsJc.getHomeRoi()) != 0 || Double.compare(r3, oddsJc.getAwayRoi()) != 0) {
	    throw new L99IllegalDataException(BetMsg.CODE_BET_ODDS_CHANGE, BetMsg.KEY_BET_ODDS_CHANGE);
	}
	BetJc betJc = bbBetJcDao.selectByBet(contestId, userId, support);
	if (betJc != null) {
	    // 已经下过注了
	    throw new L99IllegalDataException(BetMsg.CODE_BET_HAS, BetMsg.KEY_BET_HAS);
	}
	betJc = new BetJc();
	betJc.setUserId(userId);
	betJc.setContestId(contestId);
	betJc.setSupport(support);
	betJc.setCreateTime(new Date());
	betJc.setBet(bet);
	betJc.setCoupon(couponPrice);
	betJc.setBack(0.0);
	betJc.setStatus(BetResultStatus.INIT);
	betJc.setCompany(oddsJc.getCompany());
	betJc.setHandicap(oddsJc.getHandicap());
	betJc.setHomeRoi(oddsJc.getHomeRoi());
	betJc.setDrawRoi(oddsJc.getDrawRoi());
	betJc.setAwayRoi(oddsJc.getAwayRoi());
	betJc.setIpaddress(ipaddress);
	betJc.setLongbi(longbi);
	betJc.setClient(client);
	JSONObject ret = null;
	try {

	    // 先扣钱后下单
	    String supportName = null;
	    if (support == BetStatus.HOME) {
		supportName = "主胜";
	    } else if (support == BetStatus.AWAY) {
		supportName = "客胜";
	    } else if (support == BetStatus.DRAW) {
		supportName = "平局";
	    }
	    String desc = String.format("在『%s』的『%s』局中下单%s", contestName, "让球胜负", supportName);

	    // 消费龙筹券
	    if (couponUserId != null) {
		couponUserService.useCoupon(couponUserId, userId, longbi, CommerceMath.sub(bet, couponPrice),
		        ContestType.BASKETBALL, cupId, contestId, desc);
	    }

	    if (longbi) {
		double moneyLongbi = CommerceMath.sub(bet, couponPrice);
		moneyService.consumeMoney(userId, CommerceMath.sub(bet, couponPrice), desc, ipaddress);
		// add by lhx on 16-04-13
		moneyStatisticService.insertUserConsumer(userId + "", -moneyLongbi);
	    }

	    Long bId = bbBetJcDao.insertAndGetPrimaryKey(betJc);
	    if (-1L == bId.longValue()) { // 下单出错
		desc = String.format("非常抱歉，在『%s』的『%s』局中下单%s失败,系统返回下单金额", contestName, "让球胜负", supportName);
		if (longbi) {
		    moneyService.systemRechargeMoney(userId, CommerceMath.sub(bet, couponPrice), desc, ipaddress);
		    // add by lhx on 16-04-13
		    moneyStatisticService.insertUserConsumer(userId + "", CommerceMath.sub(bet, couponPrice));
		}
		if (couponUserId != null) {
		    couponUserService.settleCouponByPrice(userId, couponPrice, CouponSystem.TIME_24, desc);
		}
		throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	    }

	    // 下单信息存入redis中做统计
	    redisBetCountHandler.addContestBet(ContestType.BASKETBALL, contestId, PlayType.BB_JC.getId(), support);

	    ret = new JSONObject();
	    ret.put("bId", bId);
	    ret.put("handicap", oddsJc.getHandicap());

	} catch (L99IllegalDataException e) {
	    throw new L99IllegalDataException(e.getErrorcode(), e.getMessage());
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}
	return ret;
    }

    /**
     * 篮球 下单大小球
     */
    private JSONObject betBBSize(Long userId, Long contestId, Long cupId, boolean longbi, Integer support, Double r1,
	    Double r3, Double bet, int couponPrice, Long couponUserId, String contestName, String ipaddress, String client)
	    throws L99IllegalDataException {

	OddsSize oddsSize = bbOddsSizeDao.selectByContest(contestId);
	// 已经封盘则不能下单
	if (oddsSize == null || oddsSize.getCloseFlag()) {
	    throw new L99IllegalDataException(BetMsg.CODE_BET_ODDS_CLOSE, BetMsg.KEY_BET_ODDS_CLOSE);
	}
	if (Double.compare(r1, oddsSize.getBigRoi()) != 0 || Double.compare(r3, oddsSize.getTinyRoi()) != 0) {
	    throw new L99IllegalDataException(BetMsg.CODE_BET_ODDS_CHANGE, BetMsg.KEY_BET_ODDS_CHANGE);
	}
	BetSize betSize = bbBetSizeDao.selectByBet(contestId, userId, support);
	if (betSize != null) {
	    // 已经下过注了
	    throw new L99IllegalDataException(BetMsg.CODE_BET_HAS, BetMsg.KEY_BET_HAS);
	}
	betSize = new BetSize();
	betSize.setUserId(userId);
	betSize.setContestId(contestId);
	betSize.setSupport(support);
	betSize.setCreateTime(new Date());
	betSize.setBet(bet);
	betSize.setCoupon(couponPrice);
	betSize.setBack(0.0);
	betSize.setStatus(BetResultStatus.INIT);
	betSize.setCompany(oddsSize.getCompany());
	betSize.setHandicap(oddsSize.getHandicap());
	betSize.setBigRoi(oddsSize.getBigRoi());
	betSize.setTinyRoi(oddsSize.getTinyRoi());
	betSize.setIpaddress(ipaddress);
	betSize.setLongbi(longbi);
	betSize.setClient(client);
	JSONObject ret;
	try {

	    // 先扣钱后下单
	    String supportName = null;
	    if (support == BetStatus.HOME) {
		supportName = "大球";
	    } else if (support == BetStatus.AWAY) {
		supportName = "小球";
	    }
	    String desc = String.format("在『%s』的『%s』局中下单%s", contestName, "大小球", supportName);

	    // 消费龙筹券
	    if (couponUserId != null) {
		couponUserService.useCoupon(couponUserId, userId, longbi, CommerceMath.sub(bet, couponPrice),
		        ContestType.BASKETBALL, cupId, contestId, desc);
	    }

	    if (longbi) {
		double moneyLongbi = CommerceMath.sub(bet, couponPrice);
		moneyService.consumeMoney(userId, CommerceMath.sub(bet, couponPrice), desc, ipaddress);
		// add by lhx on 16-04-13
		moneyStatisticService.insertUserConsumer(userId + "", -moneyLongbi);
	    }

	    Long bId = bbBetSizeDao.insertAndGetPrimaryKey(betSize);
	    if (-1L == bId) { // 下单出错
		desc = String.format("非常抱歉，在『%s』的『%s』局中下单%s失败,系统返回下单金额", contestName, "大小球", supportName);
		if (longbi) {
		    moneyService.systemRechargeMoney(userId, CommerceMath.sub(bet, couponPrice), desc, ipaddress);
		    // add by lhx on 16-04-13
		    moneyStatisticService.insertUserConsumer(userId + "", CommerceMath.sub(bet, couponPrice));
		}
		if (couponUserId != null) {
		    couponUserService.settleCouponByPrice(userId, couponPrice, CouponSystem.TIME_24, desc);
		}
		throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	    }

	    // 下单信息存入redis中做统计
	    redisBetCountHandler.addContestBet(ContestType.BASKETBALL, contestId, PlayType.BB_SIZE.getId(), support);

	    ret = new JSONObject();
	    ret.put("bId", bId);
	    ret.put("handicap", oddsSize.getHandicap());

	} catch (L99IllegalDataException e) {
	    throw new L99IllegalDataException(e.getErrorcode(), e.getMessage());
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}
	return ret;
    }

    /**
     * 篮球 下单单双数
     */
    private JSONObject betBBOddeven(Long userId, Long contestId, Long cupId, boolean longbi, Integer support, Double r1,
	    Double r3, Double bet, int couponPrice, Long couponUserId, String contestName, String ipaddress, String client)
	    throws L99IllegalDataException {

	OddsDssResponse oddsDssResponse = BbOddsDssDateUtil.getInstance().getDssResponse();
	if (Double.compare(r1, oddsDssResponse.getOdd_roi()) != 0 || Double.compare(r3, oddsDssResponse.getEven_roi()) != 0) {
	    throw new L99IllegalDataException(BetMsg.CODE_BET_ODDS_CHANGE, BetMsg.KEY_BET_ODDS_CHANGE);
	}

	BetOddeven betOddeven = bbBetOddevenDao.selectByBet(contestId, userId, support);
	if (betOddeven != null) {
	    // 已经下过注了
	    throw new L99IllegalDataException(BetMsg.CODE_BET_HAS, BetMsg.KEY_BET_HAS);
	}
	betOddeven = new BetOddeven();
	betOddeven.setUserId(userId);
	betOddeven.setContestId(contestId);
	betOddeven.setSupport(support);
	betOddeven.setCreateTime(new Date());
	betOddeven.setBet(bet);
	betOddeven.setCoupon(couponPrice);
	betOddeven.setBack(0.0);
	betOddeven.setStatus(BetResultStatus.INIT);
	betOddeven.setOddRoi(oddsDssResponse.getOdd_roi());
	betOddeven.setEvenRoi(oddsDssResponse.getEven_roi());
	betOddeven.setIpaddress(ipaddress);
	betOddeven.setLongbi(longbi);
	betOddeven.setClient(client);
	JSONObject ret;
	try {

	    // 先扣钱后下单
	    String supportName = null;
	    if (support == BetStatus.HOME) {
		supportName = "单数";
	    } else if (support == BetStatus.AWAY) {
		supportName = "双数";
	    }
	    String desc = String.format("在『%s』的『%s』局中下单%s", contestName, "单双数", supportName);

	    // 消费龙筹券
	    if (couponUserId != null) {
		couponUserService.useCoupon(couponUserId, userId, longbi, CommerceMath.sub(bet, couponPrice),
		        ContestType.BASKETBALL, cupId, contestId, desc);
	    }

	    if (longbi) {
		double moneyLongbi = CommerceMath.sub(bet, couponPrice);
		moneyService.consumeMoney(userId, CommerceMath.sub(bet, couponPrice), desc, ipaddress);
		// add by lhx on 16-04-13
		moneyStatisticService.insertUserConsumer(userId + "", -moneyLongbi);
	    }

	    Long bId = bbBetOddevenDao.insertAndGetPrimaryKey(betOddeven);
	    if (-1L == bId) { // 下单出错
		desc = String.format("非常抱歉，在『%s』的『%s』局中下单%s失败,系统返回下单金额", contestName, "单双数", supportName);
		if (longbi) {
		    moneyService.systemRechargeMoney(userId, CommerceMath.sub(bet, couponPrice), desc, ipaddress);
		    // add by lhx
		    moneyStatisticService.insertUserConsumer(userId + "", CommerceMath.sub(bet, couponPrice));
		}
		if (couponUserId != null) {
		    couponUserService.settleCouponByPrice(userId, couponPrice, CouponSystem.TIME_24, desc);
		}
		throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	    }

	    // 下单信息存入redis中做统计
	    redisBetCountHandler.addContestBet(ContestType.BASKETBALL, contestId, PlayType.BB_ODDEVEN.getId(), support);

	    ret = new JSONObject();
	    ret.put("bId", bId);

	} catch (L99IllegalDataException e) {
	    throw new L99IllegalDataException(e.getErrorcode(), e.getMessage());
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}
	return ret;
    }

    /**
     * 获取可下单的最大值
     * 
     * @param total
     * @param longbi
     * @return
     */
    public static int getMaxBetNumber(Double total, boolean longbi) {
	// return (total.intValue() - 1) / 1000 * 10 + 10;
	return longbi ? 21 : 201;
    }

    /**
     * 获取篮球下单列表
     */
    @Override
    public BetLogListResponse getBbBetList(Long startId, Long contestId, Integer type, Long userId, Boolean settle,
	    Integer limit, String startTime, String endTime) throws L99IllegalDataException, L99IllegalParamsException {
	BetLogListResponse betLogListResponse = new BetLogListResponse();
	List<BetLogResponse> betLogResponseList = new ArrayList<>();
	BetLogResponse betLogResponse;
	Date startTimeDate = null;
	if (StringUtils.isNotEmpty(startTime)) {
	    startTimeDate = CbsTimeUtils.getDateByFormatDate(startTime);
	}
	Date endTimeDate = null;
	if (StringUtils.isNotEmpty(endTime)) {
	    endTimeDate = CbsTimeUtils.getDateByFormatDate(endTime);
	}
	if (PlayType.BB_SPF.getId() == type) {
	    // 胜平负
	    List<BetOp> betOpList = bbBetOpDao.findBbBetOpList(contestId, userId, settle, startId, limit, startTimeDate,
		    endTimeDate);
	    if (userId == null) {
		// 用户不指定
		List<Long> userIds = new ArrayList<>();
		for (BetOp betOp : betOpList) {
		    userIds.add(betOp.getUserId());
		}
		Map<Long, CbsUserResponse> userMap = cbsUserService.findCsAccountMapByIds(userIds);
		for (BetOp betOp : betOpList) {
		    betLogResponse = new BetLogResponse();
		    betLogResponse.setB_id(betOp.getbId());
		    ContestResponse contestResponse = bbContestService.findBbContestsById(betOp.getContestId(), null);
		    betLogResponse.setContest(contestResponse);
		    betLogResponse.setSupport(betOp.getSupport());
		    betLogResponse.setHome_roi(betOp.getHomeRoi());
		    betLogResponse.setDraw_roi(betOp.getDrawRoi());
		    betLogResponse.setAway_roi(betOp.getAwayRoi());
		    betLogResponse.setBet(betOp.getBet());
		    betLogResponse.setBack(betOp.getBack());
		    betLogResponse.setStatus(betOp.getStatus());
		    betLogResponse.setCompany(betOp.getCompany());
		    betLogResponse.setLongbi(betOp.isLongbi());
		    betLogResponse.setCoupon(betOp.getCoupon());
		    betLogResponse.setCreate_time(CbsTimeUtils.getUtcTimeForDate(betOp.getCreateTime()));
		    CbsUserResponse cbsUserResponse = userMap.get(betOp.getUserId());
		    betLogResponse.setUser_id(betOp.getUserId());
		    betLogResponse.setUser_name(cbsUserResponse.getName());
		    betLogResponse.setLong_no(cbsUserResponse.getLong_no());
		    betLogResponseList.add(betLogResponse);
		    startId = betLogResponse.getB_id();
		}

	    } else {
		// 用户指定
		CbsUserResponse cbsUserResponse = cbsUserService.selectById(userId);
		for (BetOp betOp : betOpList) {
		    betLogResponse = new BetLogResponse();
		    betLogResponse.setB_id(betOp.getbId());
		    ContestResponse contestResponse = bbContestService.findBbContestsById(betOp.getContestId(), null);
		    betLogResponse.setContest(contestResponse);
		    betLogResponse.setSupport(betOp.getSupport());
		    betLogResponse.setHome_roi(betOp.getHomeRoi());
		    betLogResponse.setDraw_roi(betOp.getDrawRoi());
		    betLogResponse.setAway_roi(betOp.getAwayRoi());
		    betLogResponse.setBet(betOp.getBet());
		    betLogResponse.setBack(betOp.getBack());
		    betLogResponse.setStatus(betOp.getStatus());
		    betLogResponse.setCompany(betOp.getCompany());
		    betLogResponse.setLongbi(betOp.isLongbi());
		    betLogResponse.setCoupon(betOp.getCoupon());
		    betLogResponse.setCreate_time(CbsTimeUtils.getUtcTimeForDate(betOp.getCreateTime()));
		    betLogResponse.setUser_id(userId);
		    betLogResponse.setUser_name(cbsUserResponse.getName());
		    betLogResponse.setLong_no(cbsUserResponse.getLong_no());
		    betLogResponseList.add(betLogResponse);
		    startId = betLogResponse.getB_id();
		}
	    }
	    if (betOpList.size() < limit) {
		startId = -1L;
	    }
	    betLogListResponse.setBets(betLogResponseList);
	} else if (PlayType.BB_JC.getId() == type) {
	    // 让球胜平负
	    // 胜平负
	    List<BetJc> betJcList = bbBetJcDao.findBbBetJcList(contestId, userId, settle, startId, limit, startTimeDate,
		    endTimeDate);
	    if (userId == null) {
		// 用户不指定
		List<Long> userIds = new ArrayList<>();
		for (BetJc betJc : betJcList) {
		    userIds.add(betJc.getUserId());
		}
		Map<Long, CbsUserResponse> userMap = cbsUserService.findCsAccountMapByIds(userIds);
		for (BetJc betJc : betJcList) {
		    betLogResponse = new BetLogResponse();
		    betLogResponse.setB_id(betJc.getbId());
		    ContestResponse contestResponse = bbContestService.findBbContestsById(betJc.getContestId(), null);
		    betLogResponse.setContest(contestResponse);
		    betLogResponse.setSupport(betJc.getSupport());
		    betLogResponse.setHome_roi(betJc.getHomeRoi());
		    betLogResponse.setDraw_roi(betJc.getDrawRoi());
		    betLogResponse.setAway_roi(betJc.getAwayRoi());
		    betLogResponse.setHandicap(betJc.getHandicap());
		    betLogResponse.setBet(betJc.getBet());
		    betLogResponse.setBack(betJc.getBack());
		    betLogResponse.setStatus(betJc.getStatus());
		    betLogResponse.setCompany(betJc.getCompany());
		    betLogResponse.setLongbi(betJc.isLongbi());
		    betLogResponse.setCoupon(betJc.getCoupon());
		    betLogResponse.setCreate_time(CbsTimeUtils.getUtcTimeForDate(betJc.getCreateTime()));
		    CbsUserResponse cbsUserResponse = userMap.get(betJc.getUserId());
		    betLogResponse.setUser_id(betJc.getUserId());
		    betLogResponse.setUser_name(cbsUserResponse.getName());
		    betLogResponse.setLong_no(cbsUserResponse.getLong_no());
		    betLogResponseList.add(betLogResponse);
		    startId = betLogResponse.getB_id();
		}

	    } else {
		// 用户指定
		CbsUserResponse cbsUserResponse = cbsUserService.selectById(userId);
		for (BetJc betJc : betJcList) {
		    betLogResponse = new BetLogResponse();
		    betLogResponse.setB_id(betJc.getbId());
		    ContestResponse contestResponse = bbContestService.findBbContestsById(betJc.getContestId(), null);
		    betLogResponse.setContest(contestResponse);
		    betLogResponse.setSupport(betJc.getSupport());
		    betLogResponse.setHome_roi(betJc.getHomeRoi());
		    betLogResponse.setDraw_roi(betJc.getDrawRoi());
		    betLogResponse.setAway_roi(betJc.getAwayRoi());
		    betLogResponse.setHandicap(betJc.getHandicap());
		    betLogResponse.setBet(betJc.getBet());
		    betLogResponse.setBack(betJc.getBack());
		    betLogResponse.setStatus(betJc.getStatus());
		    betLogResponse.setCompany(betJc.getCompany());
		    betLogResponse.setLongbi(betJc.isLongbi());
		    betLogResponse.setCoupon(betJc.getCoupon());
		    betLogResponse.setCreate_time(CbsTimeUtils.getUtcTimeForDate(betJc.getCreateTime()));
		    betLogResponse.setUser_id(userId);
		    betLogResponse.setUser_name(cbsUserResponse.getName());
		    betLogResponse.setLong_no(cbsUserResponse.getLong_no());
		    betLogResponseList.add(betLogResponse);
		    startId = betLogResponse.getB_id();
		}
	    }
	    if (betJcList.size() < limit) {
		startId = -1L;
	    }
	    betLogListResponse.setBets(betLogResponseList);
	} else if (PlayType.BB_SIZE.getId() == type) {
	    // 大小球
	    // 胜平负
	    List<BetSize> betSizeList = bbBetSizeDao.findBbBetSizeList(contestId, userId, settle, startId, limit,
		    startTimeDate, endTimeDate);
	    if (userId == null) {
		// 用户不指定
		List<Long> userIds = new ArrayList<>();
		for (BetSize betSize : betSizeList) {
		    userIds.add(betSize.getUserId());
		}
		Map<Long, CbsUserResponse> userMap = cbsUserService.findCsAccountMapByIds(userIds);
		for (BetSize betSize : betSizeList) {
		    betLogResponse = new BetLogResponse();
		    betLogResponse.setB_id(betSize.getbId());
		    ContestResponse contestResponse = bbContestService.findBbContestsById(betSize.getContestId(), null);
		    betLogResponse.setContest(contestResponse);
		    betLogResponse.setSupport(betSize.getSupport());
		    betLogResponse.setHome_roi(betSize.getBigRoi());
		    betLogResponse.setAway_roi(betSize.getTinyRoi());
		    betLogResponse.setHandicap(betSize.getHandicap());
		    betLogResponse.setBet(betSize.getBet());
		    betLogResponse.setBack(betSize.getBack());
		    betLogResponse.setStatus(betSize.getStatus());
		    betLogResponse.setCompany(betSize.getCompany());
		    betLogResponse.setLongbi(betSize.isLongbi());
		    betLogResponse.setCoupon(betSize.getCoupon());
		    betLogResponse.setCreate_time(CbsTimeUtils.getUtcTimeForDate(betSize.getCreateTime()));
		    CbsUserResponse cbsUserResponse = userMap.get(betSize.getUserId());
		    betLogResponse.setUser_id(betSize.getUserId());
		    betLogResponse.setUser_name(cbsUserResponse.getName());
		    betLogResponse.setLong_no(cbsUserResponse.getLong_no());
		    betLogResponseList.add(betLogResponse);
		    startId = betLogResponse.getB_id();
		}

	    } else {
		// 用户指定
		CbsUserResponse cbsUserResponse = cbsUserService.selectById(userId);
		for (BetSize betSize : betSizeList) {
		    betLogResponse = new BetLogResponse();
		    betLogResponse.setB_id(betSize.getbId());
		    ContestResponse contestResponse = bbContestService.findBbContestsById(betSize.getContestId(), null);
		    betLogResponse.setContest(contestResponse);
		    betLogResponse.setSupport(betSize.getSupport());
		    betLogResponse.setHome_roi(betSize.getBigRoi());
		    betLogResponse.setAway_roi(betSize.getTinyRoi());
		    betLogResponse.setHandicap(betSize.getHandicap());
		    betLogResponse.setBet(betSize.getBet());
		    betLogResponse.setBack(betSize.getBack());
		    betLogResponse.setStatus(betSize.getStatus());
		    betLogResponse.setCompany(betSize.getCompany());
		    betLogResponse.setLongbi(betSize.isLongbi());
		    betLogResponse.setCoupon(betSize.getCoupon());
		    betLogResponse.setCreate_time(CbsTimeUtils.getUtcTimeForDate(betSize.getCreateTime()));
		    betLogResponse.setUser_id(userId);
		    betLogResponse.setUser_name(cbsUserResponse.getName());
		    betLogResponse.setLong_no(cbsUserResponse.getLong_no());
		    betLogResponseList.add(betLogResponse);
		    startId = betLogResponse.getB_id();
		}
	    }
	    if (betSizeList.size() < limit) {
		startId = -1L;
	    }
	    betLogListResponse.setBets(betLogResponseList);
	} else if (PlayType.BB_ODDEVEN.getId() == type) {
	    // 大小球
	    // 胜平负
	    List<BetOddeven> betOddevenList = bbBetOddevenDao.findBbBetOddevenList(contestId, userId, settle, startId,
		    limit, startTimeDate, endTimeDate);
	    if (userId == null) {
		// 用户不指定
		List<Long> userIds = new ArrayList<>();
		for (BetOddeven betOddeven : betOddevenList) {
		    userIds.add(betOddeven.getUserId());
		}
		Map<Long, CbsUserResponse> userMap = cbsUserService.findCsAccountMapByIds(userIds);
		for (BetOddeven betOddeven : betOddevenList) {
		    betLogResponse = new BetLogResponse();
		    betLogResponse.setB_id(betOddeven.getbId());
		    ContestResponse contestResponse = bbContestService.findBbContestsById(betOddeven.getContestId(), null);
		    betLogResponse.setContest(contestResponse);
		    betLogResponse.setSupport(betOddeven.getSupport());
		    betLogResponse.setHome_roi(betOddeven.getEvenRoi());
		    betLogResponse.setAway_roi(betOddeven.getOddRoi());
		    betLogResponse.setBet(betOddeven.getBet());
		    betLogResponse.setBack(betOddeven.getBack());
		    betLogResponse.setStatus(betOddeven.getStatus());
		    betLogResponse.setCompany(betOddeven.getCompany());
		    betLogResponse.setLongbi(betOddeven.isLongbi());
		    betLogResponse.setCoupon(betOddeven.getCoupon());
		    betLogResponse.setCreate_time(CbsTimeUtils.getUtcTimeForDate(betOddeven.getCreateTime()));
		    CbsUserResponse cbsUserResponse = userMap.get(betOddeven.getUserId());
		    betLogResponse.setUser_id(betOddeven.getUserId());
		    betLogResponse.setUser_name(cbsUserResponse.getName());
		    betLogResponse.setLong_no(cbsUserResponse.getLong_no());
		    betLogResponseList.add(betLogResponse);
		    startId = betLogResponse.getB_id();
		}

	    } else {
		// 用户指定
		CbsUserResponse cbsUserResponse = cbsUserService.selectById(userId);
		for (BetOddeven betOddeven : betOddevenList) {
		    betLogResponse = new BetLogResponse();
		    betLogResponse.setB_id(betOddeven.getbId());
		    ContestResponse contestResponse = bbContestService.findBbContestsById(betOddeven.getContestId(), null);
		    betLogResponse.setContest(contestResponse);
		    betLogResponse.setSupport(betOddeven.getSupport());
		    betLogResponse.setHome_roi(betOddeven.getEvenRoi());
		    betLogResponse.setAway_roi(betOddeven.getOddRoi());
		    betLogResponse.setBet(betOddeven.getBet());
		    betLogResponse.setBack(betOddeven.getBack());
		    betLogResponse.setStatus(betOddeven.getStatus());
		    betLogResponse.setCompany(betOddeven.getCompany());
		    betLogResponse.setLongbi(betOddeven.isLongbi());
		    betLogResponse.setCoupon(betOddeven.getCoupon());
		    betLogResponse.setCreate_time(CbsTimeUtils.getUtcTimeForDate(betOddeven.getCreateTime()));
		    betLogResponse.setUser_id(userId);
		    betLogResponse.setUser_name(cbsUserResponse.getName());
		    betLogResponse.setLong_no(cbsUserResponse.getLong_no());
		    betLogResponseList.add(betLogResponse);
		    startId = betLogResponse.getB_id();
		}
	    }

	    if (betOddevenList.size() < limit) {
		startId = -1L;
	    }
	    betLogListResponse.setBets(betLogResponseList);
	}
	return betLogListResponse;
    }

    @Override
    @Transactional(rollbackFor = L99IllegalDataException.class)
    public BetLogResponse repair(Long bId, Integer playType, Double repairRoi, String reason) throws JSONException,
	    L99IllegalDataException, L99IllegalParamsException {
	BetLogResponse betLogResponse = new BetLogResponse();
	betLogResponse.setBack(0D);
	int support = 0;
	double betMoney = 0;
	double oldRoi = 0;
	double newBack = 0;
	boolean betUpdate = false;
	boolean updateFriendCircle = false;
	boolean settle = false;
	long userId = 0;
	String contestMsg = "";
	String dateMsg = "";
	if (playType == PlayType.BB_SPF.getId()) {
	    BetOp bet = bbBetOpDao.selectById(bId);
	    userId = bet.getUserId();
	    betMoney = bet.getBet();
	    newBack = betMoney * repairRoi;
	    if (bet.isLongbi()) {
		betLogResponse.setLongbi(true);
		// 只有龙币下注才做处理（包括混合下注）
		support = bet.getSupport();
		// 更新猜友圈和下注记录
		FriendCircle friendCircle = friendCircleDao.findById(bet.getContentId());
		JSONObject jsonObj = new JSONObject(friendCircle.getParams());
		JSONObject betCircle = jsonObj.getJSONObject("bet");
		if (support == BetStatus.HOME) {
		    oldRoi = betCircle.getDouble("r1");
		    betCircle.put("r1", repairRoi);
		    betUpdate = bbBetOpDao.updateInner(bId, newBack, repairRoi, null);
		} else if (support == BetStatus.AWAY) {
		    oldRoi = betCircle.getDouble("r3");
		    betCircle.put("r3", repairRoi);
		    betUpdate = bbBetOpDao.updateInner(bId, newBack, null, repairRoi);
		}
		if (bet.getStatus() != BetResultStatus.INIT) {
		    betCircle.put("back", newBack);
		    if (bet.getStatus() == BetResultStatus.WIN) {
			// 赢盘才会涉及到金钱
			settle = true;
		    }
		}

		jsonObj.put("bet", betCircle);
		String params = jsonObj.toString();
		FriendCircle friendCircle1 = new FriendCircle();
		friendCircle1.setParams(params);
		friendCircle1.setId(friendCircle.getId());

		updateFriendCircle = friendCircleDao.updateFriendCircle(friendCircle1);
		// 查询赛事信息
		ContestResponse contestResponse = bbContestService.findBbContestsById(bet.getContestId(), null);
		contestMsg = String.format("『%sVS%s』", contestResponse.getH_t().getName(), contestResponse.getA_t()
		        .getName());

		// 日期
		Date date = bet.getCreateTime();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		dateMsg = (calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日";
	    } else {
		return betLogResponse;
	    }

	} else if (playType == PlayType.BB_JC.getId()) {
	    BetJc bet = bbBetJcDao.selectById(bId);
	    userId = bet.getUserId();
	    betMoney = bet.getBet();
	    newBack = betMoney * repairRoi;
	    if (bet.isLongbi()) {
		betLogResponse.setLongbi(true);
		// 只有龙币下注才做处理（包括混合下注）
		support = bet.getSupport();
		// 更新猜友圈和下注记录
		FriendCircle friendCircle = friendCircleDao.findById(bet.getContentId());
		JSONObject jsonObj = new JSONObject(friendCircle.getParams());
		JSONObject betCircle = jsonObj.getJSONObject("bet");
		if (support == BetStatus.HOME) {
		    oldRoi = betCircle.getDouble("r1");
		    betCircle.put("r1", repairRoi);
		    betUpdate = bbBetJcDao.updateInner(bId, newBack, repairRoi, null);
		} else if (support == BetStatus.AWAY) {
		    oldRoi = betCircle.getDouble("r3");
		    betCircle.put("r3", repairRoi);
		    betUpdate = bbBetJcDao.updateInner(bId, newBack, null, repairRoi);
		}
		if (bet.getStatus() != BetResultStatus.INIT) {
		    betCircle.put("back", newBack);
		    if (bet.getStatus() == BetResultStatus.WIN) {
			// 赢盘才会涉及到金钱
			settle = true;
		    }
		}
		jsonObj.put("bet", betCircle);
		String params = jsonObj.toString();
		FriendCircle friendCircle1 = new FriendCircle();
		friendCircle1.setParams(params);
		friendCircle1.setId(friendCircle.getId());

		updateFriendCircle = friendCircleDao.updateFriendCircle(friendCircle1);
		// 查询赛事信息
		ContestResponse contestResponse = bbContestService.findBbContestsById(bet.getContestId(), null);
		contestMsg = String.format("『%sVS%s』", contestResponse.getH_t().getName(), contestResponse.getA_t()
		        .getName());

		// 日期
		Date date = bet.getCreateTime();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		dateMsg = (calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日";
	    } else {
		return betLogResponse;
	    }
	} else if (playType == PlayType.BB_SIZE.getId()) {
	    BetSize bet = bbBetSizeDao.selectById(bId);
	    userId = bet.getUserId();
	    betMoney = bet.getBet();
	    newBack = betMoney * repairRoi;
	    if (bet.isLongbi()) {
		betLogResponse.setLongbi(true);
		// 只有龙币下注才做处理（包括混合下注）
		support = bet.getSupport();
		// 更新猜友圈和下注记录
		FriendCircle friendCircle = friendCircleDao.findById(bet.getContentId());
		JSONObject jsonObj = new JSONObject(friendCircle.getParams());
		JSONObject betCircle = jsonObj.getJSONObject("bet");
		if (support == BetStatus.HOME) {
		    oldRoi = betCircle.getDouble("r1");
		    betCircle.put("r1", repairRoi);
		    betUpdate = bbBetSizeDao.updateInner(bId, newBack, repairRoi, null);
		} else if (support == BetStatus.AWAY) {
		    oldRoi = betCircle.getDouble("r3");
		    betCircle.put("r3", repairRoi);
		    betUpdate = bbBetSizeDao.updateInner(bId, newBack, null, repairRoi);
		}
		if (bet.getStatus() != BetResultStatus.INIT) {
		    betCircle.put("back", newBack);
		    if (bet.getStatus() == BetResultStatus.WIN) {
			// 赢盘才会涉及到金钱
			settle = true;
		    }
		}
		jsonObj.put("bet", betCircle);
		String params = jsonObj.toString();
		FriendCircle friendCircle1 = new FriendCircle();
		friendCircle1.setParams(params);
		friendCircle1.setId(friendCircle.getId());

		updateFriendCircle = friendCircleDao.updateFriendCircle(friendCircle1);

		// 查询赛事信息
		ContestResponse contestResponse = bbContestService.findBbContestsById(bet.getContestId(), null);
		contestMsg = String.format("『%sVS%s』", contestResponse.getH_t().getName(), contestResponse.getA_t()
		        .getName());

		// 日期
		Date date = bet.getCreateTime();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		dateMsg = (calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日";
	    } else {
		return betLogResponse;
	    }
	}
	if (betUpdate && updateFriendCircle) {
	    BetRepair betRepair = new BetRepair();
	    if (settle) {
		// 给用户返钱或扣钱
		double money = betMoney * oldRoi - newBack;
		if (money > 0) {
		    // 扣钱
		    moneyService.consumeMoney(userId, money, "在" + contestMsg + "出现错盘,系统自动修正", null);
		    betLogResponse.setBack(money);
		} else if (money < 0) {
		    // 加钱
		    moneyService.earnMoney(userId, -money, "在" + contestMsg + "出现错盘,系统自动修正", null, playType, bId.toString());
		    betLogResponse.setBack(-money);
		}
		betRepair.setChangeMoney(money);
	    }
	    // 记录信息
	    betRepair.setBetId(bId);
	    betRepair.setBet((int) betMoney);
	    betRepair.setSupport(support);
	    betRepair.setPlayType(playType);
	    betRepair.setOldRoi(oldRoi);
	    betRepair.setNewRoi(repairRoi);
	    betRepair.setReason(reason);
	    boolean flag = repairBetDao.insert(betRepair);
	    if (flag) {
		// 发送通知给用户
		// 初始化信鸽消息map
		// Map<String, Object> customContent = new HashMap<>();
		// customContent.put("scheme_link", "cbs://message/");
		String msg = "您在" + dateMsg + "下单的比赛" + contestMsg + ",由于出现错盘,下单赔率已被系统修改为" + repairRoi;
		// // 发送信鸽消息提醒
		// XingePushUtil.getInstance().pushSingleAccount(userId, "",
		// msg, customContent);
		// 发送消息提醒
		JSONObject params = new JSONObject();
		params.put("content", msg);
		notifyService.addNotify(ContestConstants.TempletId.SYSTEM_PROMPT, userId, userId, params.toString(), null);
	    }
	}
	return betLogResponse;
    }

}
