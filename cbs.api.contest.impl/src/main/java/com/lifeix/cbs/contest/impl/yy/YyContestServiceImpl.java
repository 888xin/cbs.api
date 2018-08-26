package com.lifeix.cbs.contest.impl.yy;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.bean.coupon.CouponUserListResponse;
import com.lifeix.cbs.api.bean.gold.GoldResponse;
import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.cbs.api.bean.user.CbsUserWxResponse;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.exception.MsgCode.BetMsg;
import com.lifeix.cbs.api.common.exception.MsgCode.ContestMsg;
import com.lifeix.cbs.api.common.util.CommerceMath;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestStatu_YY;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestType;
import com.lifeix.cbs.api.common.util.MessageConstants.NotifyNumType;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.common.util.RedisConstants;
import com.lifeix.cbs.api.service.coupon.CouponUserService;
import com.lifeix.cbs.api.service.money.MoneyService;
import com.lifeix.cbs.api.service.user.CbsUserService;
import com.lifeix.cbs.api.service.user.CbsUserWxService;
import com.lifeix.cbs.contest.bean.contest.ContestAdListResponse;
import com.lifeix.cbs.contest.bean.yy.YyBetListResponse;
import com.lifeix.cbs.contest.bean.yy.YyBetResponse;
import com.lifeix.cbs.contest.bean.yy.YyContestListResponse;
import com.lifeix.cbs.contest.bean.yy.YyContestResponse;
import com.lifeix.cbs.contest.bean.yy.YyCupListResponse;
import com.lifeix.cbs.contest.bean.yy.YyCupResponse;
import com.lifeix.cbs.contest.bean.yy.YyOddsResponse;
import com.lifeix.cbs.contest.bean.yy.YyOptionResponse;
import com.lifeix.cbs.contest.dao.yy.YyBetDao;
import com.lifeix.cbs.contest.dao.yy.YyContestDao;
import com.lifeix.cbs.contest.dao.yy.YyCupDao;
import com.lifeix.cbs.contest.dto.yy.YyBet;
import com.lifeix.cbs.contest.dto.yy.YyContest;
import com.lifeix.cbs.contest.dto.yy.YyCup;
import com.lifeix.cbs.contest.impl.ImplSupport;
import com.lifeix.cbs.contest.impl.redis.RedisYyCountHandler;
import com.lifeix.cbs.contest.service.contest.ContestAdService;
import com.lifeix.cbs.contest.service.yy.YyContestService;
import com.lifeix.cbs.contest.util.transform.YyContestTransformUtil;
import com.lifeix.cbs.message.service.notify.NotifyService;
import com.lifeix.cbs.message.service.placard.PlacardTempletService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;
import com.lifeix.framework.redis.impl.RedisDataIdentify;
import com.lifeix.framework.redis.impl.RedisHashHandler;
import com.lifeix.framework.redis.impl.RedisListHandler;

@Service("yyContestService")
public class YyContestServiceImpl extends ImplSupport implements YyContestService {

    /**
     * 奥运会开始时间
     */
    private final long OLYMPIC_TIME = 1470438000000L;

    // 精选数量
    private final int LIMIT = 20;

    @Autowired
    private YyContestDao yyContestDao;

    @Autowired
    private YyBetDao yyBetDao;

    @Autowired
    private YyCupDao yyCupDao;

    @Autowired
    private MoneyService moneyService;

    @Autowired
    CouponUserService couponUserService;

    @Autowired
    private CbsUserService cbsUserService;

    @Autowired
    private RedisYyCountHandler redisYyCountHandler;

    @Autowired
    private RedisHashHandler redisHashHandler;

    @Autowired
    private RedisListHandler redisListHandler;

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private ContestAdService contestAdService;

    @Autowired
    private CbsUserWxService cbsUserWxService;

    @Autowired
    private PlacardTempletService placardTempletService;

    @Override
    public YyCupListResponse findYyCups() {
	List<YyCup> cups = yyCupDao.findYyCups();

	List<YyCupResponse> yy_cups = new ArrayList<YyCupResponse>();
	for (YyCup cup : cups) {
	    yy_cups.add(YyContestTransformUtil.transformYyCup(cup));
	}

	YyCupListResponse reponse = new YyCupListResponse();
	reponse.setCups(yy_cups);
	reponse.setNumber(yy_cups.size());

	return reponse;
    }

    @Override
    public void insertYyCups(String cupName) throws L99IllegalParamsException, L99IllegalDataException {

	ParamemeterAssert.assertDataNotNull(cupName);

	YyCup cup = yyCupDao.selectByName(cupName);
	if (cup != null) {
	    throw new L99IllegalDataException(ContestMsg.CODE_TYPE_EXIST, ContestMsg.KEY_TYPE_EXIST);
	} else {
	    YyCup cup1 = new YyCup();
	    cup1.setCupName(cupName);
	    cup1.setCreateTime(new Date());
	    boolean flag = yyCupDao.insert(cup1);
	    if (!flag) {
		throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	    }
	}
    }

    @Override
    public YyContestResponse viewYyContest(Long id) throws L99IllegalParamsException, L99IllegalDataException {

	ParamemeterAssert.assertDataNotNull(id);

	YyContest contest = yyContestDao.selectById(id);

	if (contest == null) {
	    throw new L99IllegalDataException(ContestMsg.CODE_CONTEST_NOT_EXIST, ContestMsg.KEY_CONTEST_NOT_EXIST);
	}

	// 填充下单统计
	Map<String, Object> betCount = redisYyCountHandler.viewYyCount(contest.getId());
	return YyContestTransformUtil.transformYyContest(contest, betCount, false);
    }

    @Override
    public YyOddsResponse oddsYyContest(Long id, Long userId, String from) throws L99IllegalParamsException,
	    L99IllegalDataException, JSONException {

	ParamemeterAssert.assertDataNotNull(id);

	YyOddsResponse response = new YyOddsResponse();

	YyContest contest = yyContestDao.selectById(id);
	if (contest == null) {
	    throw new L99IllegalDataException(ContestMsg.CODE_CONTEST_NOT_EXIST, ContestMsg.KEY_CONTEST_NOT_EXIST);
	}
	// 获取用户龙筹或者龙币
	if (userId != null) {

	    // 来源与其他app的用户来源导入到大赢家
	    if (from != null) {
		cbsUserService.checkCbsUser(userId, from);
	    }

	    GoldResponse gold = moneyService.viewUserMoney(userId, "");
	    response.setGold(CommerceMath.sub(gold.getBalance(), gold.getFrozen()));

	    CouponUserListResponse userCoupons = couponUserService.findUserBetCoupons(userId, id, null, ContestType.YAYA);
	    response.setCoupons(userCoupons);
	}

	Map<String, Object> betCount = redisYyCountHandler.viewYyCount(id);
	YyContestResponse contestResponse = YyContestTransformUtil.transformYyContest(contest, betCount, false);
	response.setContest(contestResponse);

	// 下单信息
	if (userId != null) {
	    List<YyBet> bets = yyBetDao.findYyBet(id, userId, false, null, 10);
	    if (bets.size() > 0) {
		List<YyBetResponse> yy_bets = new ArrayList<YyBetResponse>();
		for (YyBet bet : bets) {
		    yy_bets.add(YyContestTransformUtil.transformYyBet(bet));
		}

		response.setBets(yy_bets);
	    }
	}

	// 查询是否关联广告
	String placard = placardTempletService.findPlacardRelation(contest.getCupId(), id, ContestType.YAYA);
	response.setPlacardId(placard);

	return response;
    }

    @Override
    public YyContestListResponse findBetYyContests(Boolean longbi, Long cupId, Long userId, Long startId, int limit)
	    throws L99IllegalParamsException {

	YyContestListResponse reponse = new YyContestListResponse();
	ContestAdListResponse adListRes = null;
	// 首页数据加载可下注的分类和奥运会剩余时间
	if (startId == null) {

	    List<YyCup> cupData = yyContestDao.findBetYyCups();
	    List<YyCupResponse> cups = new ArrayList<>();
	    cups.add(YyContestTransformUtil.transformYyCup(0L, "精选"));
	    for (YyCup yyCup : cupData) {
		cups.add(YyContestTransformUtil.transformYyCup(yyCup));
	    }
	    reponse.setCups(cups);
	    reponse.setSystem_time(0L);

	    if (userId != null && longbi) {
		// 返回用户龙币数额
		try {
		    GoldResponse money = moneyService.viewUserMoney(userId, null);
		    reponse.setMoney(CommerceMath.sub(money.getBalance(), money.getFrozen()));
		} catch (L99IllegalDataException | JSONException e) {
		    LOG.error(String.format("get user %s money failed - %s", userId, e.getMessage()));
		}

		// 未读消息数量
		reponse.setNotify_num(notifyService.getUnreadNotify(userId, NotifyNumType.SYSTEM));
	    }

	}

	List<YyContest> contests = new ArrayList<>();
	if (cupId != null && cupId == 0) {
	    RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST,
		    RedisConstants.ContestRedis.CONTEST_YY_GOOD);
	    List<byte[]> list = redisListHandler.sMembersByte(indentify);
	    List<Long> idList = new ArrayList<>();
	    if (list.size() > 0) {
		for (byte[] bytes : list) {
		    idList.add(Long.valueOf(new String(bytes)));
		}
		// 根据ID倒排
		Collections.sort(idList, new Comparator<Long>() {
		    @Override
		    public int compare(Long o1, Long o2) {
			if (o1 > o2) {
			    return -1;
			}
			return 1;
		    }
		});
		Map<Long, YyContest> contestMap = yyContestDao.findYyContestMapByIds(idList);
		for (Long aLong : contestMap.keySet()) {
		    YyContest yyContest = contestMap.get(aLong);
		    if (yyContest.getEndTime().getTime() < new Date().getTime()) {
			// 从redis里面移除
			editGoodYy(-aLong);
		    } else {
			// 符合条件，添加
			contests.add(yyContest);
		    }
		}
	    }

	    // 查询2个有效押押的广告 若小于2则不显示
	    adListRes = contestAdService.findContestsAd(ContestType.YAYA, 2);

	} else {
	    contests = yyContestDao.findBetYyContests(cupId, startId, limit);
	}

	Map<String, Object> userBetMap = null;
	if (userId != null) {
	    userBetMap = redisYyCountHandler.viewYyUser(userId);
	} else {
	    userBetMap = new HashMap<String, Object>();
	}
	List<YyContestResponse> yy_contests = new ArrayList<YyContestResponse>();
	for (YyContest contest : contests) {
	    Map<String, Object> betCount = redisYyCountHandler.viewYyCount(contest.getId());
	    YyContestResponse yyCr = YyContestTransformUtil.transformYyContest(contest, betCount, true);
	    // 是否下过单
	    Object count = userBetMap.get(contest.getId().toString());
	    if (count != null) {
		int countVal = Integer.valueOf(count.toString());
		yyCr.setUser_bet(countVal);
	    }
	    yy_contests.add(yyCr);
	    startId = contest.getId();
	}

	if (yy_contests.size() == 0 || yy_contests.size() < limit) { // 没有数据或数据不够返回-1
	    startId = -1L;
	}

	if (adListRes != null) {
	    reponse.setAds(adListRes.getAds());
	}

	reponse.setContests(yy_contests);
	reponse.setNumber(contests.size());
	reponse.setStartId(startId);

	return reponse;
    }

    @Override
    public YyBetListResponse findUserYyContests(Long userId, Long startId, int limit) throws L99IllegalParamsException,
	    L99IllegalDataException {

	ParamemeterAssert.assertDataNotNull(userId);

	List<YyBet> bets = yyBetDao.findUserYyBet(userId, startId, limit);

	List<YyBetResponse> yy_bets = new ArrayList<YyBetResponse>();

	List<Long> contestIds = new ArrayList<Long>();
	for (YyBet bet : bets) {
	    contestIds.add(bet.getContestId());
	}
	Map<Long, YyContest> contestMap = yyContestDao.findYyContestMapByIds(contestIds);

	for (YyBet bet : bets) {
	    YyBetResponse betRes = YyContestTransformUtil.transformYyBet(bet);
	    if (betRes != null) {
		YyContest contest = contestMap.get(bet.getContestId());
		betRes.setContest(YyContestTransformUtil.transformYyContest(contest, null, true));
		startId = betRes.getId();
		yy_bets.add(betRes);
	    }
	}

	if (yy_bets.size() == 0 || yy_bets.size() < limit) { // 没有数据或数据不够返回-1
	    startId = -1L;
	}

	YyBetListResponse response = new YyBetListResponse();
	response.setBets(yy_bets);
	response.setStartId(startId);
	response.setNumber(yy_bets.size());

	return response;
    }

    /**
     * 押押赛事下单记录
     * 
     * @param contestId
     * @param startId
     * @param limit
     * @return
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     * @throws JSONException
     * @throws L99NetworkException
     */
    @Override
    public YyBetListResponse findContestBets(Long contestId, Long startId, int limit) throws L99IllegalParamsException,
	    L99IllegalDataException, L99NetworkException, JSONException {

	ParamemeterAssert.assertDataNotNull(contestId);

	List<YyBet> bets = yyBetDao.findYyBet(contestId, null, false, startId, limit);

	List<YyBetResponse> yy_bets = new ArrayList<YyBetResponse>();

	List<Long> userIds = new ArrayList<Long>();
	for (YyBet bet : bets) {
	    userIds.add(bet.getUserId());
	}
	Map<Long, CbsUserResponse> userMap = cbsUserService.findCsAccountMapByIds(userIds);

	Map<Long, CbsUserWxResponse> userTokenMap = new HashMap<Long, CbsUserWxResponse>();
	if (userIds.size() > 0) {
	    List<CbsUserWxResponse> userWxList = cbsUserWxService.selectById(userIds);
	    if (userWxList != null) {
		for (CbsUserWxResponse bean : userWxList) {
		    userTokenMap.put(bean.getUser_id(), bean);
		}
	    }
	}

	for (YyBet bet : bets) {
	    YyBetResponse betRes = YyContestTransformUtil.transformYyBet(bet);
	    if (betRes != null) {
		betRes.setUser(userMap.get(bet.getUserId()));
		betRes.setUserWx(userTokenMap.get(bet.getUserId()));
		startId = betRes.getId();
		yy_bets.add(betRes);
	    }
	}

	if (yy_bets.size() == 0 || yy_bets.size() < limit) { // 没有数据或数据不够返回-1
	    startId = -1L;
	}

	YyBetListResponse response = new YyBetListResponse();
	response.setBets(yy_bets);
	response.setStartId(startId);
	response.setNumber(yy_bets.size());

	return response;
    }

    @Override
    public YyContestListResponse findYyContests(Boolean hideFlag, Boolean type, Long cupId, Long startId, int limit)
	    throws L99IllegalParamsException {

	// 是否属于精选 start
	RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST,
	        RedisConstants.ContestRedis.CONTEST_YY_GOOD);
	List<byte[]> list = redisListHandler.sMembersByte(indentify);
	Set<Long> idSet = new HashSet<>();
	if (list.size() > 0) {
	    for (byte[] bytes : list) {
		idSet.add(Long.valueOf(new String(bytes)));
	    }
	}
	int size = idSet.size();
	// 是否属于精选 end

	List<YyContest> contests = yyContestDao.findYyContests(hideFlag, type, cupId, startId, limit);

	List<YyContestResponse> yy_contests = new ArrayList<>();
	for (YyContest contest : contests) {
	    Map<String, Object> betCount = redisYyCountHandler.viewYyCount(contest.getId());
	    YyContestResponse yyContestResponse = YyContestTransformUtil.transformYyContest(contest, betCount, true);
	    if (size > 0) {
		if (idSet.contains(contest.getId())) {
		    yyContestResponse.setGood(true);
		} else {
		    yyContestResponse.setGood(false);
		}
	    } else {
		yyContestResponse.setGood(false);
	    }
	    yy_contests.add(yyContestResponse);
	    startId = contest.getId();
	}

	YyContestListResponse reponse = new YyContestListResponse();
	reponse.setContests(yy_contests);
	reponse.setNumber(contests.size());
	reponse.setStartId(startId);

	return reponse;
    }

    @Override
    public void insertYyContests(String title, String images, String text, String options, Long cupId, String startTime,
	    String endTime, boolean activityFlag, int showType, String listImage) throws L99IllegalParamsException,
	    L99IllegalDataException {

	ParamemeterAssert.assertDataNotNull(title, text, options, cupId, startTime, endTime);

	DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Date startDate = null, endDate = null;
	try {
	    startDate = timeFormat.parse(startTime);
	    endDate = timeFormat.parse(endTime);
	} catch (ParseException e) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}

	if (startDate.after(endDate)) { // 开始时间不能晚于结束时间
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}

	YyCup cup = yyCupDao.selectById(cupId);
	if (cup == null) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}

	// 判断押押参数
	int initCount = 0;
	try {
	    JSONArray optionArray = new JSONArray(options);
	    if (optionArray.length() < 1) {
		throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	    }
	    for (int i = 0; i < optionArray.length(); i++) {
		JSONObject optionObj = optionArray.getJSONObject(i);
		if (optionObj.has("c")) {
		    initCount += optionObj.getInt("c");
		}
	    }
	} catch (JSONException e) {
	    LOG.error(e.getMessage(), e);
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}

	YyContest contest = new YyContest();
	contest.setTitle(title);
	contest.setImages(images);
	contest.setText(text);
	contest.setOptions(options);
	contest.setCupId(cupId);
	contest.setCupName(cup.getCupName());
	contest.setCreateTime(new Date());
	contest.setStartTime(startDate);
	contest.setEndTime(endDate);
	contest.setStatus(0);
	contest.setWinner(0);
	contest.setSettle(false);
	contest.setLongbi(false);
	contest.setInitCount(initCount);
	contest.setShowType(showType);
	contest.setActivityFlag(activityFlag);
	contest.setListImage(listImage);
	boolean flag = yyContestDao.insert(contest);
	if (!flag) {
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}

    }

    @Override
    public void updateYyContests(Long id, String title, String images, String text, String options, Long cupId,
	    String startTime, String endTime, boolean activityFlag, Boolean hideFlag, int showType, String listImage)
	    throws L99IllegalParamsException, L99IllegalDataException {

	ParamemeterAssert.assertDataNotNull(id);
	YyContest contest = yyContestDao.selectById(id);
	if (contest == null) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}
	boolean flag;
	if (contest.getHideFlag().booleanValue() != hideFlag.booleanValue()) { // 当隐藏标识发生改变时不修改
	    flag = yyContestDao.updateHide(id, hideFlag);
	} else {
	    // 修改押押赛事
	    ParamemeterAssert.assertDataNotNull(title, text, options, cupId, startTime, endTime);

	    DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date startDate = null, endDate = null;
	    try {
		startDate = timeFormat.parse(startTime);
		endDate = timeFormat.parse(endTime);
	    } catch (ParseException e) {
		throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	    }
	    if (startDate.after(endDate)) { // 开始时间不能晚于结束时间
		throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	    }
	    YyCup cup = yyCupDao.selectById(cupId);
	    if (cup == null) {
		throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	    }

	    // 判断押押参数
	    int initCount = 0;
	    try {
		JSONArray optionArray = new JSONArray(options);
		if (optionArray.length() < 1) {
		    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
		}
		for (int i = 0; i < optionArray.length(); i++) {
		    JSONObject optionObj = optionArray.getJSONObject(i);
		    if (optionObj.has("c")) {
			initCount += optionObj.getInt("c");
		    }
		}
	    } catch (JSONException e) {
		LOG.error(e.getMessage(), e);
		throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	    }

	    contest.setTitle(title);
	    contest.setImages(images);
	    contest.setText(text);
	    contest.setOptions(options);
	    contest.setCupId(cupId);
	    contest.setCupName(cup.getCupName());
	    contest.setStartTime(startDate);
	    contest.setEndTime(endDate);
	    contest.setLongbi(false);
	    contest.setInitCount(initCount);
	    contest.setShowType(showType);
	    contest.setActivityFlag(activityFlag);
	    contest.setListImage(listImage);
	    flag = yyContestDao.update(contest);
	}
	if (!flag) {
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}
    }

    /**
     * 后台填入押押赛事结果
     * 
     * @param id
     *            赛事Id
     * @param winner
     *            赛事选项结果
     * @param status
     *            赛事状态 正常 or 走盘
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    @Override
    public void endYyContest(Long id, Integer winner, int status) throws L99IllegalParamsException, L99IllegalDataException {

	ParamemeterAssert.assertDataNotNull(id, winner, status);

	YyContest contest = yyContestDao.selectById(id);

	if (contest == null) {
	    throw new L99IllegalDataException(ContestMsg.CODE_CONTEST_NOT_EXIST, ContestMsg.KEY_CONTEST_NOT_EXIST);
	} else if (contest.isSettle()) {
	    throw new L99IllegalDataException(ContestMsg.CODE_CONTEST_SETTLED, ContestMsg.KEY_CONTEST_SETTLED);
	} else if (status != ContestStatu_YY.DONE && status != ContestStatu_YY.CANCLE) { // 结果参数异常
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	} else if (contest.getEndTime().compareTo(new Date()) > 0) { // 还未到结束时间
	    throw new L99IllegalDataException(ContestMsg.CODE_CONTEST_NOT_END, ContestMsg.KEY_CONTEST_NOT_END);
	}

	List<YyOptionResponse> options = YyContestTransformUtil.transformYyOption(contest.getOptions(), null);
	if (winner < 1 || winner > options.size()) {
	    throw new L99IllegalParamsException(BetMsg.CODE_BET_NOT_SUPPORT, BetMsg.KEY_BET_NOT_SUPPORT);
	}

	contest.setWinner(winner);
	contest.setStatus(status);

	boolean flag = yyContestDao.update(contest);
	if (!flag) {
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}

    }

    @Override
    public int YyShouldSettle() {
	return yyContestDao.settleNum();
    }

    @Override
    public void addOptionImage(String name, String path) {
	RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST,
	        RedisConstants.ContestRedis.CONTEST_YY_OPTION_IMAGE);
	redisHashHandler.hset(indentify, name.getBytes(), path.getBytes());
    }

    @Override
    public void deleteOptionImage(String name) {
	RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST,
	        RedisConstants.ContestRedis.CONTEST_YY_OPTION_IMAGE);
	redisHashHandler.hdel(indentify, name.getBytes());
    }

    @Override
    public YyContestResponse getOptionImage() {
	RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST,
	        RedisConstants.ContestRedis.CONTEST_YY_OPTION_IMAGE);
	Map<byte[], byte[]> map = redisHashHandler.hGetAll(indentify);
	YyContestResponse yyContestResponse = new YyContestResponse();
	if (map.size() > 0) {
	    StringBuilder stringBuilder = new StringBuilder();
	    for (byte[] bytes : map.keySet()) {
		String key = new String(bytes);
		String value = new String(map.get(bytes));
		stringBuilder.append(key).append(":").append(value).append(",");
	    }
	    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
	    yyContestResponse.setImages(stringBuilder.toString());
	}
	return yyContestResponse;
    }

    @Override
    public void editGoodYy(Long id) {
	RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST,
	        RedisConstants.ContestRedis.CONTEST_YY_GOOD);
	if (id > 0) {
	    // 添加
	    Long count = redisListHandler.countListData(indentify);
	    if (count > LIMIT) {
		// 移除多余的id
		redisListHandler.lPopList(indentify, (int) (count - LIMIT));
	    }
	    redisListHandler.insertRedisData(indentify, id.toString().getBytes());
	} else {
	    // 删除
	    id = -id;
	    redisListHandler.delRedisData(indentify, LIMIT, id.toString().getBytes());
	}
    }

}
