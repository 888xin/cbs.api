package com.lifeix.cbs.contest.impl.yy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.bean.coupon.CouponUserResponse;
import com.lifeix.cbs.api.bean.gold.GoldResponse;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.exception.MsgCode.BetMsg;
import com.lifeix.cbs.api.common.exception.MsgCode.CouponMsg;
import com.lifeix.cbs.api.common.mission.Mission;
import com.lifeix.cbs.api.common.util.BetConstants;
import com.lifeix.cbs.api.common.util.BetConstants.BetResultStatus;
import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.api.common.util.CommerceMath;
import com.lifeix.cbs.api.common.util.ContentConstants.PostType;
import com.lifeix.cbs.api.common.util.ContestConstants;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestStatu_YY;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestType;
import com.lifeix.cbs.api.common.util.CouponConstants.CouponSystem;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.common.util.RandomUtils;
import com.lifeix.cbs.api.service.coupon.CouponUserService;
import com.lifeix.cbs.api.service.misson.MissionUserService;
import com.lifeix.cbs.api.service.money.MoneyService;
import com.lifeix.cbs.api.service.money.MoneyStatisticService;
import com.lifeix.cbs.api.service.user.CbsUserService;
import com.lifeix.cbs.contest.bean.yy.YyOptionResponse;
import com.lifeix.cbs.contest.dao.yy.YyBetDao;
import com.lifeix.cbs.contest.dao.yy.YyContestDao;
import com.lifeix.cbs.contest.dto.yy.YyBet;
import com.lifeix.cbs.contest.dto.yy.YyContest;
import com.lifeix.cbs.contest.impl.ImplSupport;
import com.lifeix.cbs.contest.impl.redis.RedisYyCountHandler;
import com.lifeix.cbs.contest.service.cirle.FriendCircleService;
import com.lifeix.cbs.contest.service.fb.FbContestService;
import com.lifeix.cbs.contest.service.yy.YyBetService;
import com.lifeix.cbs.contest.util.WeixinNotifyTemplate;
import com.lifeix.cbs.contest.util.WeixinNotifyUtil;
import com.lifeix.cbs.contest.util.transform.YyContestTransformUtil;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.framework.kafka.producer.MessageSender;
import com.lifeix.user.beans.CustomResponse;

/**
 * 押押下单逻辑
 * 
 * @author lifeix-sz
 * 
 */
@Service("yyBetService")
public class YyBetServiceImpl extends ImplSupport implements YyBetService {

    @Autowired
    private CouponUserService couponUserService;

    @Autowired
    private MoneyService moneyService;

    @Autowired
    private YyContestDao yyContestDao;

    @Autowired
    private YyBetDao yyBetDao;

    @Autowired
    private FriendCircleService friendCircleService;

    @Autowired
    private CbsUserService cbsUserService;

    @Autowired
    private FbContestService fbContestService;

    @Autowired
    private RedisYyCountHandler redisYyCountHandler;

    @Autowired
    protected MoneyStatisticService moneyStatisticService;

    @Resource
    private MessageSender kafkaMessageSender;

    @Autowired
    private MissionUserService missionUserService;

    @Override
    public CustomResponse addYyBet(Long userId, Long contestId, boolean longbi, Integer support, Double roi, Double bet,
	    String content, Long couponUserId, String client, String ipaddress, String from)
	    throws L99IllegalParamsException, L99IllegalDataException, JSONException {

	// 参数验证
	ParamemeterAssert.assertDataNotNull(userId);
	ParamemeterAssert.assertDataNotNull(contestId);
	ParamemeterAssert.assertDataNotNull(support);
	ParamemeterAssert.assertDataNotNull(roi);

	YyContest contest = yyContestDao.selectById(contestId);
	// 比赛不可下单
	if (contest == null) {
	    throw new L99IllegalParamsException(BetMsg.CODE_BET_CANT, BetMsg.KEY_BET_CANT);
	}
	// 比赛已经开始，不可下单
	if (contest.getStatus() != ContestStatu_YY.NOTOPEN || (contest.getEndTime().compareTo(new Date()) < 0)) {
	    throw new L99IllegalParamsException(BetMsg.CODE_BET_CONTEST_BEGIN, BetMsg.KEY_BET_CONTEST_BEGIN);
	}
	// 纯龙筹场必须传递龙筹券
	if (!longbi) {
	    ParamemeterAssert.assertDataNotNull(couponUserId);
	} else { // 龙币场必须传递龙币
	    ParamemeterAssert.assertDataNotNull(bet);
	}

	// 每场比赛的玩法只允许一次混合下单
	if (longbi && couponUserId != null) {
	    int mixCount = yyBetDao.countUserMixBet(contestId, userId);
	    if (mixCount > 0) {
		throw new L99IllegalParamsException(BetMsg.CODE_BET_MIX_REPEAT, BetMsg.KEY_BET_MIX_REPEAT);
	    }

	    // 混合下单必须大于1.7赔率
	    if (roi < 1.7D) {
		throw new L99IllegalParamsException(BetMsg.CODE_BET_MIX_LIMIT, BetMsg.KEY_BET_MIX_LIMIT);
	    }
	}

	List<YyOptionResponse> options = YyContestTransformUtil.transformYyOption(contest.getOptions(), null);
	if (support < 1 || support > options.size()) {
	    throw new L99IllegalParamsException(BetMsg.CODE_BET_NOT_SUPPORT, BetMsg.KEY_BET_NOT_SUPPORT);
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

	// 押押不允许下单超过100
	if (bet > 100) {
	    throw new L99IllegalDataException(BetMsg.CODE_BET_YY_LIMIT, BetMsg.KEY_BET_YY_LIMIT);
	}

	// 获取押押选项
	YyOptionResponse option = options.get(support - 1);

	// 下单
	JSONObject betRet = betYy(userId, contestId, longbi, support, roi, bet, couponPrice, couponUserId,
	        contest.getTitle(), ipaddress, option, client);
	if (betRet == null) {
	    throw new L99IllegalDataException(BetMsg.CODE_BET_FAIL, BetMsg.KEY_BET_FAIL);
	}

	CustomResponse response = new CustomResponse();

	Long bId = betRet.getLong("bId");
	Long contentId = null;
	// 下单发表竞猜
	try {

	    JSONObject paramObj = new JSONObject();
	    paramObj.put("title", contest.getTitle());
	    paramObj.put("opi", option.getIndex());
	    paramObj.put("opn", option.getName());
	    // 下单的id
	    paramObj.put("bId", bId);
	    // 下单选择
	    paramObj.put("support", support);
	    // 比赛开始时间
	    paramObj.put("time", CbsTimeUtils.getUtcTimeForDate(contest.getStartTime()));
	    paramObj.put("roi", roi);
	    paramObj.put("isLongbi", longbi);
	    // 下单金额
	    paramObj.put("bet", bet);
	    // 接入猜友圈
	    contentId = friendCircleService.postContent(userId, PostType.GUESS, content, "", null, contestId,
		    ContestType.YAYA, paramObj.toString(), client, false, couponPrice);

	    YyBet yyBet = new YyBet();
	    yyBet.setId(bId);
	    yyBet.setContentId(contentId);
	    yyBetDao.updateContentId(yyBet);

	    response.put("bId", bId);
	    response.put("contentId", contentId);

	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}

	// 计算下单可获得奖励
	double back = CommerceMath.mul(bet, roi);
	if (!longbi) { // 纯龙筹场向上取整
	    back = BetConstants.getCouponPriceByBack(back);
	}

	response.put("back", back);

	// 微信下单发送消息提醒
	if ("WEIXIN".equals(client)) {
	    try {

		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy年MM月dd日");
		JSONObject params = new JSONObject();
		params.put("bId", "D" + RandomUtils.numLongEncoder(bId));
		params.put("start_time", formatDate.format(contest.getStartTime()));
		params.put("title", contest.getTitle());
		params.put("name", option.getName());
		params.put("bet_odds", roi);
		params.put("bet_val", bet_val);
		params.put("user_id", userId);
		if (contentId != null) {
		    params.put("circle_id", contentId);
		}

		params.put("contest_type", ContestConstants.ContestType.YAYA);
		params.put("temp_id", WeixinNotifyTemplate.ORDER_PAY_TEMPID);

			//LOG.info("weixin01:"+contentId);
		kafkaMessageSender.sendText(params.toString(), WeixinNotifyUtil.KAFKA_VER);
		LOG.info(String.format("weixin kafka message %s,%s", WeixinNotifyUtil.KAFKA_VER, params.toString()));
	    } catch (Exception e) {
		LOG.error("userId=" + userId + ", bet weixin message failed: " + e.getMessage(), e);
	    }
	}

	// 来源与其他app的用户来源导入到大赢家
	if (from != null) {
	    cbsUserService.checkCbsUser(userId, from);
	}

	// add by lhx on 16-07-22 每日下注（床上用户任务） start
	missionUserService.validate(userId, Mission.NYX_BET);
	// add by lhx on 16-07-22 每日下注（床上用户任务） end

	return response;
    }

    /**
     * 押押下单
     * 
     * @param userId
     * @param contestId
     * @param longbi
     * @param support
     * @param roi
     * @param bet
     * @param contestName
     * @param ipaddress
     * @param option
     * @return
     * @throws L99IllegalDataException
     */
    private JSONObject betYy(Long userId, Long contestId, boolean longbi, Integer support, Double roi, Double bet,
	    int couponPrice, Long couponUserId, String contestName, String ipaddress, YyOptionResponse option, String client)
	    throws L99IllegalDataException {

	// 已经封盘则不能下单
	if (Double.compare(roi, option.getRoi()) != 0) {
	    throw new L99IllegalDataException(BetMsg.CODE_BET_ODDS_CHANGE, BetMsg.KEY_BET_ODDS_CHANGE);
	}
	YyBet yyBet = yyBetDao.selectByBet(contestId, userId, support);
	if (yyBet != null) {
	    // 已经下过注了
	    throw new L99IllegalDataException(BetMsg.CODE_BET_HAS, BetMsg.KEY_BET_HAS);
	}
	yyBet = new YyBet();
	yyBet.setUserId(userId);
	yyBet.setContestId(contestId);
	yyBet.setSupport(support);
	yyBet.setCreateTime(new Date());
	yyBet.setBet(bet);
	yyBet.setCoupon(couponPrice);
	yyBet.setBack(0.0);
	yyBet.setStatus(BetResultStatus.INIT);
	yyBet.setYyRoi(roi);
	yyBet.setIpaddress(ipaddress);
	yyBet.setLongbi(longbi);
	yyBet.setCoupon(couponPrice);
	yyBet.setClient(client);
	JSONObject ret = null;
	try {

	    // 扣钱后下单
	    String desc = String.format("在『%s』中下单『%s』", contestName, option.getName());

	    // 消费龙筹券
	    if (couponUserId != null) {
		couponUserService.useCoupon(couponUserId, userId, longbi, CommerceMath.sub(bet, couponPrice),
		        ContestType.YAYA, null, contestId, desc);
	    }

	    // 扣除龙币
	    if (longbi) {
		double moneyLongbi = CommerceMath.sub(bet, couponPrice);
		moneyService.consumeMoney(userId, CommerceMath.sub(bet, couponPrice), desc, ipaddress);
		// add by lhx on 16-04-13
		moneyStatisticService.insertUserConsumer(userId + "", -moneyLongbi);
	    }

	    Long id = yyBetDao.insertAndGetPrimaryKey(yyBet);
	    if (-1L == id.longValue()) {
		desc = String.format("非常抱歉，在『%s』中下单『%s』失败,系统返回下单金额", contestName, option.getName());
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
	    redisYyCountHandler.addYyCount(userId, contestId, support);

	    ret = new JSONObject();

	    ret.put("bId", id);
	} catch (L99IllegalDataException e) {
	    throw new L99IllegalDataException(e.getErrorcode(), e.getMessage());
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}
	return ret;
    }

    /**
     * 重新所有押押比赛的下单统计
     */
    @Override
    public CustomResponse resetBetCount() {
	int yya = 0;
	// 清除所有旧的押押统计
	redisYyCountHandler.cleanYyCount();
	Long startId = null;
	List<YyBet> betYys = null;
	LOG.info("start count yy bet");
	do {
	    betYys = yyBetDao.findUserYyBet(null, startId, 100);
	    for (YyBet betYy : betYys) {
		yya++;
		startId = betYy.getId();
		redisYyCountHandler.addYyCount(betYy.getUserId(), betYy.getContestId(), betYy.getSupport());
	    }
	} while (betYys != null && betYys.size() > 0);

	LOG.info("done count yy bet - " + yya);

	CustomResponse data = new CustomResponse();
	data.put("yya", yya);
	return data;
    }

}
