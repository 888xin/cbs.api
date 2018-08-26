package com.lifeix.cbs.contest.impl.spark.settle;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.l99.common.utils.TimeUtil;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.exception.MsgCode.ContestMsg;
import com.lifeix.cbs.api.common.lock.RedisLockHelper;
import com.lifeix.cbs.api.common.util.BetConstants;
import com.lifeix.cbs.api.common.util.BetConstants.BetResultStatus;
import com.lifeix.cbs.api.common.util.CommerceMath;
import com.lifeix.cbs.api.common.util.ContestConstants;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestStatu_YY;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestType;
import com.lifeix.cbs.api.common.util.ContestConstants.TempletId;
import com.lifeix.cbs.api.common.util.GoldConstants.MoneyMissedType;
import com.lifeix.cbs.api.common.util.LockConstants;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.common.util.RandomUtils;
import com.lifeix.cbs.api.service.coupon.CouponUserService;
import com.lifeix.cbs.api.service.money.MoneyService;
import com.lifeix.cbs.api.service.money.MoneyStatisticService;
import com.lifeix.cbs.contest.bean.yy.YyOptionResponse;
import com.lifeix.cbs.contest.dao.settle.UserSettleLogDao;
import com.lifeix.cbs.contest.dao.yy.YyBetDao;
import com.lifeix.cbs.contest.dao.yy.YyContestDao;
import com.lifeix.cbs.contest.dto.settle.UserSettleLog;
import com.lifeix.cbs.contest.dto.yy.YyBet;
import com.lifeix.cbs.contest.dto.yy.YyContest;
import com.lifeix.cbs.contest.service.spark.settle.YySettleDubbo;
import com.lifeix.cbs.contest.util.CbsSettleLog;
import com.lifeix.cbs.contest.util.WeixinNotifyTemplate;
import com.lifeix.cbs.contest.util.WeixinNotifyUtil;
import com.lifeix.cbs.contest.util.algorithm.AlgorithmResult;
import com.lifeix.cbs.contest.util.transform.YyContestTransformUtil;
import com.lifeix.cbs.message.service.notify.NotifyService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.framework.kafka.producer.MessageSender;
import com.lifeix.user.beans.CustomResponse;

/**
 * 押押赛事结算逻辑
 * 
 * @author lifeix-sz
 * 
 */
@Service("yySettleService")
public class YySettleDubboImpl implements YySettleDubbo {

    @Autowired
    private YyContestDao yyContestDao;

    @Autowired
    private YyBetDao yyBetDao;

    @Autowired
    private UserSettleLogDao userSettleLogDao;

    @Autowired
    private MoneyService moneyService;

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private RedisLockHelper redisLock;

    @Autowired
    CouponUserService couponUserService;

    @Autowired
    protected MoneyStatisticService moneyStatisticService;

    @Resource
    private MessageSender kafkaMessageSender;

    /**
     * 赛事结算锁
     */
    private static Object lock = new Object();

    /**
     * 结算押押赛事
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

	YyContest contest = yyContestDao.selectById(contestId);
	// 比赛状态判断
	if (contest == null) {
	    throw new L99IllegalDataException(ContestMsg.CODE_CONTEST_NOT_EXIST, ContestMsg.KEY_CONTEST_NOT_EXIST);
	} else if (contest.isSettle()) { // 已结算
	    throw new L99IllegalDataException(ContestMsg.CODE_CONTEST_SETTLED, ContestMsg.KEY_CONTEST_SETTLED);
	} else if (contest.getEndTime().compareTo(new Date()) > 0) { // 还未到结束时间
	    throw new L99IllegalDataException(ContestMsg.CODE_CONTEST_NOT_END, ContestMsg.KEY_CONTEST_NOT_END);
	} else if (contest.getWinner() == 0) { // 后台还未设置胜出方
	    throw new L99IllegalDataException(ContestMsg.CODE_CONTEST_PEDDING, ContestMsg.KEY_CONTEST_PEDDING);
	} else if (contest.getStatus() == ContestStatu_YY.DONE || contest.getStatus() == ContestStatu_YY.CANCLE) { // 正常结算

	    CbsSettleLog.info(String.format("##yy[%d] settle start", contestId));

	    // 因为赛事更新与超时检查的定时器都会call结算的逻辑，为避免重复结算问题，此处加线程锁
	    synchronized (lock) {
		if (redisLock.getRedisLock(contestId, LockConstants.IDENTIFY_YY) != null) {
		    CbsSettleLog.info(String.format("!!yy[%d] settle failed,contest id pending.", contestId));
		    throw new L99IllegalDataException(ContestMsg.CODE_CONTEST_PEDDING, ContestMsg.KEY_CONTEST_PEDDING);
		}
		redisLock.setRedisLock(contestId, LockConstants.IDENTIFY_YY);
	    }

	    try {

		List<YyOptionResponse> options = YyContestTransformUtil.transformYyOption(contest.getOptions(), null);

		boolean cancleFlag = false;
		if (contest.getStatus() == ContestStatu_YY.CANCLE) { // 比赛取消做为走盘结算
		    cancleFlag = true;
		}

		// 结算押押
		int count = 0;
		Long startId = null;
		List<YyBet> bets = null;
		do {
		    bets = yyBetDao.findYyBet(contest.getId(), null, true, startId, ContestConstants.SETTLE_LIMIT);
		    if (bets.size() > 0) {
			for (YyBet bet : bets) {
			    startId = bet.getId();
			    if (bet.getStatus().intValue() != BetResultStatus.INIT) {
				CbsSettleLog.info(String.format("##yy[%d]-yy-bet[%d] resettle", contestId, bet.getId()));
				continue;
			    }
			    AlgorithmResult ret = new AlgorithmResult();
			    ret.setAmount(bet.getBet()); // 本金
			    ret.setOdds(bet.getYyRoi()); // 赔率
			    if (cancleFlag) {
				// 取消比赛按走盘结算
				ret.setProfit(0);
				ret.setStatus(BetResultStatus.DRAW);
			    } else {
				if (contest.getWinner() == bet.getSupport()) { // 获胜
				    // 计算下单可获得奖励
				    ret.setProfit(CommerceMath.mul(bet.getBet(), CommerceMath.sub(bet.getYyRoi(), 1)));
				    ret.setStatus(BetResultStatus.WIN);
				} else { // 失败
				    ret.setProfit(0);
				    ret.setStatus(BetResultStatus.LOSS);
				}
			    }

			    YyOptionResponse option = options.get(bet.getSupport() - 1);
			    settleUserBet(ret, contest, bet, option.getName());
			    count++;
			}
		    }

		} while (bets != null && bets.size() > 0);

		// 结算后标记比赛
		contest.setSettle(true);
		boolean flag = yyContestDao.update(contest);
		if (!flag) {
		    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
		}

		CustomResponse response = new CustomResponse();
		response.put("yy", count);
		CbsSettleLog.info(String.format("##yy[%d] settle success, yy=%d", contestId, count));

		return response;
	    } finally {
		redisLock.delRedisLock(contestId, LockConstants.IDENTIFY_YY);
	    }
	} else {
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}

    }

    /**
     * 个人结算下单方法
     * 
     * @param ret
     * @param contest
     * @param bet
     * @param optionName
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     * @throws JSONException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    private void settleUserBet(AlgorithmResult ret, YyContest contest, YyBet bet, String optionName)
	    throws L99IllegalParamsException, L99IllegalDataException, JSONException {
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
	boolean flag = yyBetDao.updateSettle(bet.getId(), bet.getBack(), bet.getStatus());

	if (!flag) {
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}
	Long contestId = contest.getId();
	Long payLogId = null;
	if (bet.getBack() > 0) {
	    String desc = String.format("在『押押』的『%s』局中下单结算", contest.getTitle());
	    if (bet.isLongbi()) { // 结算龙币
		payLogId = moneyService.earnMoney(bet.getUserId(), bet.getBack(), desc, bet.getIpaddress(),
		        MoneyMissedType.YY_SETTLE, bet.getId().toString());
		// add by lhx on 16-04-13
		moneyStatisticService.insertUserConsumer(bet.getUserId() + "", bet.getBack());
	    } else { // 结算龙筹
		payLogId = couponUserService.settleCouponByBack(bet.getUserId(), bet.getBack(), desc);
	    }

	}

	// 结算记录
	UserSettleLog settleLog = new UserSettleLog();
	settleLog.setContestType(ContestType.YAYA);
	settleLog.setUserId(bet.getUserId());
	settleLog.setContestId(contestId);
	settleLog.setPlayId(0);
	settleLog.setSupport(bet.getSupport());
	settleLog.setOdds(ret.getOdds());
	settleLog.setBet(ret.getAmount());
	settleLog.setBack(bet.getBack());
	settleLog.setStatus(ret.getStatus());
	settleLog.setContentId(bet.getContentId());
	settleLog.setContestTime(contest.getStartTime());
	settleLog.setCreateTime(new Date());
	settleLog.setLongbi(bet.isLongbi());
	settleLog.setPayLogId(payLogId);
	flag = userSettleLogDao.insert(settleLog);
	if (!flag) {
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}
	JSONObject params = new JSONObject();
	params.put("start_time", TimeUtil.getUtcTimeForDate(contest.getStartTime()));
	params.put("title", contest.getTitle());
	params.put("name", optionName);
	params.put("bet_odds", ret.getOdds());

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
		back = CommerceMath.mul(bet.getBet(), ret.getOdds());
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
	notifyService.addNotify(TempletId.YY_BET_RESULT, bet.getUserId(), 0L, params.toString(), bet.getContentId());

	// 微信下单发送公众号消息
	if ("WEIXIN".equals(bet.getClient())) {
	    try {
		SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy年MM月dd日");
		params.put("create_time", formatDateTime.format(bet.getCreateTime()));
		params.put("start_time", formatDate.format(contest.getStartTime()));
		params.put("bId", "D" + RandomUtils.numLongEncoder(bet.getId()));
		params.put("user_id", bet.getUserId());

		params.put("contest_type", ContestConstants.ContestType.YAYA);
		params.put("temp_id", WeixinNotifyTemplate.ORDER_OVER_TEMPID);

		kafkaMessageSender.sendText(params.toString(), WeixinNotifyUtil.KAFKA_VER);
		CbsSettleLog
		        .info(String.format("weixin kafka message %s,%s", WeixinNotifyUtil.KAFKA_VER, params.toString()));
	    } catch (Exception e) {
		CbsSettleLog.error("userId=" + bet.getUserId() + ", send weixin template fail: " + e.getMessage(), e);
	    }

	}

	CbsSettleLog.info(String.format("##yy[%d]-bet[%d] back %s", contestId, bet.getId(), bet.getBack()));

    }
}
