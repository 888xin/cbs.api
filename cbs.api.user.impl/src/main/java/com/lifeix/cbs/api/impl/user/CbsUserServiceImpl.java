/**
 * 
 */
package com.lifeix.cbs.api.impl.user;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.achieve.bean.achieve.UserAchieveResponse;
import com.lifeix.cbs.achieve.bean.bo.UserAchieveBO;
import com.lifeix.cbs.achieve.common.constant.BehaviorConstants;
import com.lifeix.cbs.achieve.service.AchieveService;
import com.lifeix.cbs.api.bean.gold.GoldPrizeListResponse;
import com.lifeix.cbs.api.bean.gold.GoldPrizeResponse;
import com.lifeix.cbs.api.bean.gold.GoldResponse;
import com.lifeix.cbs.api.bean.mission.MissionUserResponse;
import com.lifeix.cbs.api.bean.user.CbsUserListResponse;
import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.cbs.api.bean.user.UserContestStatisticsResponse;
import com.lifeix.cbs.api.bean.user.UserSpaceResponse;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.exception.MsgCode.UserMsg;
import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.mission.Mission;
import com.lifeix.cbs.api.common.mq.MqUtil;
import com.lifeix.cbs.api.common.solr.CbsSolrUtil;
import com.lifeix.cbs.api.common.user.LifeixUserApiUtil;
import com.lifeix.cbs.api.common.util.CbsClientType;
import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.api.common.util.CbsUserConstants.UserFindType;
import com.lifeix.cbs.api.common.util.CbsUserConstants.UserRegSource;
import com.lifeix.cbs.api.common.util.CbsUserConstants.UserWeixin;
import com.lifeix.cbs.api.common.util.CommerceMath;
import com.lifeix.cbs.api.common.util.ContestConstants;
import com.lifeix.cbs.api.common.util.CouponConstants.CouponSystem;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.common.util.RedisConstants;
import com.lifeix.cbs.api.common.util.RedisConstants.UserRedis;
import com.lifeix.cbs.api.dao.rank.UserContestStatisticsDao;
import com.lifeix.cbs.api.dao.user.CbsUserDao;
import com.lifeix.cbs.api.dao.user.CbsUserLoginDao;
import com.lifeix.cbs.api.dto.rank.UserContestStatistics;
import com.lifeix.cbs.api.dto.user.CbsUser;
import com.lifeix.cbs.api.dto.user.CbsUserLogin;
import com.lifeix.cbs.api.impl.util.AccountTransformUtil;
import com.lifeix.cbs.api.service.coupon.CouponUserService;
import com.lifeix.cbs.api.service.gold.GoldLogService;
import com.lifeix.cbs.api.service.misson.MissionUserService;
import com.lifeix.cbs.api.service.money.MoneyService;
import com.lifeix.cbs.api.service.spark.UserContestStatisticsDubbo;
import com.lifeix.cbs.api.service.user.CbsUserService;
import com.lifeix.cbs.api.service.user.CbsUserWxService;
import com.lifeix.cbs.api.util.UserConstants;
import com.lifeix.cbs.contest.service.bb.BbContestService;
import com.lifeix.cbs.contest.service.fb.FbContestService;
import com.lifeix.cbs.message.service.notify.NotifyService;
import com.lifeix.common.utils.RegexUtil;
import com.lifeix.common.utils.SpellUtil;
import com.lifeix.common.utils.StringUtil;
import com.lifeix.community.message.service.MessageService;
import com.lifeix.exception.L99ExceptionBase;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;
import com.lifeix.framework.memcache.MemcacheService;
import com.lifeix.framework.redis.impl.RedisDataIdentify;
import com.lifeix.framework.redis.impl.RedisSortSetHandler;
import com.lifeix.nyx.relationship.constant.RelationshipConstants;
import com.lifeix.nyx.relationship.service.CbsRelationshipService;
import com.lifeix.user.api.action.account.AccountAuthAction;
import com.lifeix.user.beans.CustomResponse;
import com.lifeix.user.beans.account.AccountResponse;
import com.lifeix.user.beans.account.FullAccountResponse;
import com.lifeix.user.beans.profile.AccountProfile;
import com.sun.jersey.api.client.ClientHandlerException;

/**
 * @author jacky
 * 
 */
@Service("cbsUserService")
public class CbsUserServiceImpl extends ImplSupport implements CbsUserService {

    public static final int SOLR_LIMIT = 200;
    public static final Long USER_EXPIRT_REDIS = 60 * 60 * 24 * 100L;

    @Autowired
    private CbsUserLoginDao cbsUserLoginDao;

    @Autowired
    AccountAuthAction accountAuthAction;

    @Autowired
    MessageService messageService;

    @Autowired
    private MoneyService moneyService;

    @Autowired
    private CbsUserDao cbsUserDao;

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private UserContestStatisticsDubbo userContestStatisticsDubbo;

    @Autowired
    private CouponUserService couponUserService;

    @Autowired
    private UserContestStatisticsDao userContestStatisticsDao;

    @Autowired
    CbsRelationshipService cbsRelationshipService;

    @Autowired
    RedisSortSetHandler redisSortSetHandler;

    @Autowired
    BbContestService bbContestService;

    @Autowired
    FbContestService fbContestService;

    @Autowired
    protected MemcacheService memcacheService;

    @Autowired
    MqUtil mqUtil;

    @Autowired
    protected AchieveService achieveService;

    @Autowired
    protected GoldLogService goldLogService;

    @Autowired
    private MissionUserService missionUserService;

    @Autowired
    private CbsUserWxService cbsUserWxService;

    @Override
    public CbsUserResponse getCbsUserByLongNo(Long longNO) throws L99IllegalParamsException, JSONException,
	    L99NetworkException {
	if (longNO == null) {
	    return null;
	}
	CbsUser user = cbsUserDao.getCbsUserByLongNo(longNO);
	if (user == null) {
	    throw new L99IllegalParamsException(UserMsg.CODE_USER_ACCOUNT_NOT_FOUND, UserMsg.KEY_USER_ACCOUNT_NOT_FOUND);
	}
	FullAccountResponse rep = LifeixUserApiUtil.getInstance().findFullUser(user.getUserId().toString(), null, true);
	CbsUserResponse userRes = AccountTransformUtil.transformUser(user, true);
	userRes.setMobilePhone(rep.getMobilePhone());
	userRes.setAvatarsId(rep.getAvatarsId());
	UserContestStatistics userStatisticsResponse = userContestStatisticsDao.getUserContestStatistics(user.getUserId());
	userRes.setUserStatisticsResponse(AccountTransformUtil.transformUserContestStatistics(userStatisticsResponse, 0));

	return userRes;
    }

    @Override
    public CbsUserResponse getCbsUserByUserId(Long userId, boolean isFullUser, boolean isBlock)
	    throws L99IllegalParamsException, JSONException, L99NetworkException, L99IllegalDataException {
	CbsUserResponse data = getCbsUserByUserId(userId, isFullUser);
	if (isBlock && data != null && data.getStatus().intValue() == 1) {
	    throw new L99IllegalDataException(UserMsg.CODE_USER_ACCOUNT_BLOCK, UserMsg.KEY_USER_ACCOUNT_BLOCK);
	}
	return data;
    }

    @Override
    public CbsUserResponse getCbsUserByUserId(Long userId, boolean isFullUser) throws L99IllegalParamsException,
	    JSONException, L99NetworkException {
	CbsUserResponse data = new CbsUserResponse();
	CbsUser user = cbsUserDao.selectById(userId);
	if (user != null) {
	    data.setAboutme(user.getAboutme());
	    data.setBackground(user.getBack());
	    data.setGender(user.getGender());
	    data.setLocation(user.getLocal());
	    data.setStatus(user.getStatus());
	    data.setUser_id(user.getUserId());
	    data.setName(user.getUserName());
	    data.setLong_no(user.getUserNo());
	    data.setHead(user.getUserPath());

	    if (isFullUser) {
		FullAccountResponse acs = LifeixUserApiUtil.getInstance().findFullUser(user.getUserId().toString(), "id",
		        false);
		if (acs != null) {
		    AccountTransformUtil.transformCbsUserResponse(acs, data, false);
		}
	    }
	    UserContestStatistics userStatisticsResponse = userContestStatisticsDao.getUserContestStatistics(user
		    .getUserId());
	    data.setUserStatisticsResponse(AccountTransformUtil.transformUserContestStatistics(userStatisticsResponse, 0));

	} else {
	    throw new L99IllegalParamsException(UserMsg.CODE_USER_ACCOUNT_NOT_FOUND, UserMsg.KEY_USER_ACCOUNT_NOT_FOUND);
	}

	return data;

    }

    @Override
    public CbsUserResponse getSimpleCbsUserByUserId(Long userId) {
	CbsUserResponse data = new CbsUserResponse();
	CbsUser user = cbsUserDao.selectById(userId);
	data.setGender(user.getGender());
	data.setStatus(user.getStatus());
	data.setUser_id(user.getUserId());
	data.setName(user.getUserName());
	data.setLong_no(user.getUserNo());
	data.setHead(user.getUserPath());
	return data;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.lifeix.tiyu.user.service.basic.TiyuUserService#register(int,
     * java.lang.String, int, java.lang.String, java.lang.String, int,
     * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public CbsUserResponse register(int authType, String authKey, int accountType, String name, String password, int gender,
	    String machineCode, String randCode, String client, String market, String country, String ipaddress)
	    throws L99IllegalParamsException, L99NetworkException, JSONException {
	String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

	ParamemeterAssert.assertDataNotNull(authKey);
	ParamemeterAssert.assertDataNotNull(name);
	ParamemeterAssert.assertDataNotNull(password);

	// 注册用户中心账号
	FullAccountResponse response = LifeixUserApiUtil.getInstance().register(authType, authKey, accountType, name,
	        password, gender, null, randCode, machineCode, client, market, country, ipaddress, false);

	// 初始化体育头条信息
	CbsUserResponse cbsUser = registerCbsUser(response, gender, null, null);

	if (cbsUser != null) {
	    mqUtil.sendCbsRegisterMessage(authType, authKey, cbsUser.getUser_id());
	}

	/**
	 * 注册统计客户端
	 */
	RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_USER, UserRedis.USER_REG_TYPE);
	indentify.setKey(time);
	redisSortSetHandler.zIncrby(indentify, CbsClientType.getClient(client).getName(), 1);

	/**
	 * 注册统计为手机端
	 */
	indentify = new RedisDataIdentify(RedisConstants.MODEL_USER, UserRedis.USER_REG_SOURCE);
	indentify.setKey(time);
	redisSortSetHandler.zIncrby(indentify, UserRegSource.MOBILE, 1);

	/**
	 * 注册统计性别
	 */
	indentify = new RedisDataIdentify(RedisConstants.MODEL_USER, UserRedis.USER_REG_GENDER);
	indentify.setKey(time);
	redisSortSetHandler.zIncrby(indentify, gender + "", 1);

	return cbsUser;
    }

    /*
     * （非 Javadoc）
     * 
     * @see
     * com.lifeix.tiyu.user.service.subscribe.UserSubscribeService#login(java
     * .lang.String, java.lang.String, int, java.lang.String, java.lang.String,
     * java.lang.String, boolean, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public CbsUserResponse login(String username, String password, int type, String country, String version,
	    String machineCode, boolean encryptFlag, String ipaddress, String accountBrowser, String timezone,
	    String sourceClient, String devToken) throws L99IllegalParamsException, L99NetworkException, JSONException {

	ParamemeterAssert.assertDataNotNull(username);
	ParamemeterAssert.assertDataNotNull(password);

	FullAccountResponse response = LifeixUserApiUtil.getInstance().login(type, username, password, country, encryptFlag,
	        devToken, machineCode, sourceClient, version, ipaddress);

	// 初始cbs信息
	CbsUserResponse userResp = registerCbsUser(response, null, null, null);
	String auth = accountAuthAction.createToken(response.getAccountId(), false, "");

	if (auth == null) {
	    throw new L99NetworkException(BasicMsg.CODE_USERSERVER, BasicMsg.KEY_USERSERVER);
	}
	JSONObject json = new JSONObject(auth);
	if (json.getInt("code") == 200) {
	    JSONObject rett = new JSONObject(json.get("data").toString());
	    userResp.setToken(rett.getString("token"));
	} else {
	    throw new L99NetworkException(json.optString("code"), json.optString("msg"));
	}

	if (userResp != null) {
	    // 连续登录成就通知
	    try {
		UserAchieveBO bo = new UserAchieveBO();
		bo.setBehavior_type(BehaviorConstants.BehaviorType.USER_TYPE);
		bo.setUser_id(userResp.getUser_id());
		bo.setLogin_flag(true);
		achieveService.addAchieveData(bo.generateData(), false);
	    } catch (Throwable t) {
		LOG.error("login achieve data analysis failed: " + t.getMessage(), t);
	    }

	    // 转换旧版龙筹
	    goldLogService.replaceGold(userResp.getUser_id());
	}

	return userResp;
    }

    /*
     * （非 Javadoc）
     * 
     * @see
     * com.lifeix.tiyu.user.service.subscribe.UserSubscribeService#loginByThird
     * (java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * int, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public CbsUserResponse loginByThird(String oauthType, String usercode, String username, String userhead, int gender,
	    String machineCode, String client, String market, String country, String sign, String ipaddress)
	    throws L99IllegalParamsException, L99NetworkException, JSONException {
	String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	String str = oauthType;
	if (!StringUtils.isEmpty(usercode)) {
	    str = str + usercode;
	}

	if (!StringUtils.isEmpty(username)) {
	    str = str + username;
	}
	str = str + "g442VmSL";

	String strEncode = StringUtil.MD5Encode(str);

	if (!StringUtils.equals(strEncode, sign)) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}
	// 检查用户中心账号
	FullAccountResponse response = LifeixUserApiUtil.getInstance().loginBySdk(oauthType, usercode, username, userhead,
	        gender, machineCode, client, market, country, ipaddress);

	// 初始化大赢家信息
	CbsUserResponse userResp = registerCbsUser(response, gender, null, null);
	String auth = accountAuthAction.createToken(response.getAccountId(), false, "third");

	if (auth == null) {
	    throw new L99NetworkException(BasicMsg.CODE_USERSERVER, BasicMsg.KEY_USERSERVER);
	}
	JSONObject json = new JSONObject(auth);
	if (json.getInt("code") == 200) {
	    JSONObject rett = new JSONObject(json.get("data").toString());
	    userResp.setToken(rett.getString("token"));
	} else {
	    throw new L99NetworkException(json.optString("code"), json.optString("msg"));
	}

	if (userResp != null) {
	    // 连续登录成就通知
	    try {
		UserAchieveBO bo = new UserAchieveBO();
		bo.setBehavior_type(BehaviorConstants.BehaviorType.USER_TYPE);
		bo.setUser_id(userResp.getUser_id());
		bo.setLogin_flag(true);
		achieveService.addAchieveData(bo.generateData(), false);
	    } catch (Throwable t) {
		LOG.error("login achieve data analysis failed: " + t.getMessage(), t);
	    }
	}

	/**
	 * 注册统计客户端
	 */
	RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_USER, UserRedis.USER_REG_TYPE);
	indentify.setKey(time);
	redisSortSetHandler.zIncrby(indentify, CbsClientType.getClient(client).getName(), 1);

	/**
	 * 注册统计为微信端
	 */
	indentify = new RedisDataIdentify(RedisConstants.MODEL_USER, UserRedis.USER_REG_SOURCE);
	indentify.setKey(time);
	redisSortSetHandler.zIncrby(indentify, UserRegSource.WEIXIN, 1);

	/**
	 * 注册统计性别
	 */
	indentify = new RedisDataIdentify(RedisConstants.MODEL_USER, UserRedis.USER_REG_GENDER);
	indentify.setKey(time);
	redisSortSetHandler.zIncrby(indentify, gender + "", 1);

	return userResp;

    }

    @Override
    public CbsUserResponse loginByThird(String oauthType, String usercode, String username, String userhead, int gender,
	    String machineCode, String client, String market, String country, String sign, String ipaddress, String openId)
	    throws L99IllegalParamsException, L99NetworkException, JSONException {
	CbsUserResponse userResp = loginByThird(oauthType, usercode, username, userhead, gender, machineCode, client,
	        market, country, sign, ipaddress);
	// 存儲用戶openid
	if (StringUtils.isNotEmpty(openId))
	    cbsUserWxService.saveUserWxInfo(userResp.getUser_id(), openId, UserWeixin.APPID_APP, "APP");
	return userResp;
    }

    /*
     * （非 Javadoc）
     * 
     * @see com.lifeix.tiyu.user.service.subscribe.UserSubscribeService#
     * updateUserInfo (java.lang.Long, java.lang.Integer, java.lang.String,
     * java.lang.Long, java.lang.String, java.lang.Integer, java.lang.String,
     * java.lang.String)
     */
    @Override
    public DataResponse<Object> updateUserInfo(Long accountId, Integer type, String name, Long avatarId, String userPath,
	    Integer gender, String aboutme, String back) {

	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    ParamemeterAssert.assertDataNotNull(type);
	    if (type > 0) {
		CbsUser user = cbsUserDao.selectById(accountId);
		if (user == null) {
		    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
		}
		boolean achieveFlag = false;
		if ((type & 1) > 0) {
		    ParamemeterAssert.assertDataNotNull(name);
		    LifeixUserApiUtil.getInstance().updateName(accountId, name);
		    user.setUserName(name);
		    user.setNamePinyin(SpellUtil.getFullSpell(name));
		}
		if ((type & 2) > 0) {
		    ParamemeterAssert.assertDataNotNull(gender);
		    LifeixUserApiUtil.getInstance().updateGender(accountId, gender);
		    user.setGender(gender);
		}
		if ((type & 4) > 0) {
		    ParamemeterAssert.assertDataNotNull(avatarId, userPath);
		    LifeixUserApiUtil.getInstance().updateUserPath(accountId, avatarId, userPath);
		    user.setUserPath(userPath);
		    achieveFlag = true;
		}
		if ((type & 8) > 0) {
		    ParamemeterAssert.assertDataNotNull(aboutme);
		    user.setAboutme(aboutme);
		}
		if ((type & 16) > 0) {
		    ParamemeterAssert.assertDataNotNull(back);
		    user.setBack(back);
		    achieveFlag = true;
		}

		boolean flag = cbsUserDao.update(user);

		if (!flag) {
		    try {
			throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
		    } catch (L99IllegalDataException e) {
			ret.setCode(Integer.valueOf(e.getErrorcode()));
			ret.setMsg(e.getMessage());
		    }

		}
		if (achieveFlag) {
		    try {
			UserAchieveBO bo = new UserAchieveBO();
			bo.setBehavior_type(BehaviorConstants.BehaviorType.USER_TYPE);
			bo.setUser_id(user.getUserId());
			bo.setLogin_flag(true);
			if ((type & 4) > 0) {
			    bo.setAvatar_flag(true);
			    // add by lhx on 16-06-23 上传头像任务 start
			    missionUserService.validate(user.getUserId(), Mission.FIRST_AVATAR);
			    // add by lhx on 16-06-23 上传头像任务 end
			}
			if ((type & 16) > 0)
			    bo.setCover_flag(true);
			achieveService.addAchieveData(bo.generateData(), false);
		    } catch (Throwable t) {
			LOG.error("user info achieve data analysis failed: " + t.getMessage(), t);
		    }
		}

		/*
	         * // 同步到订阅信息 syncUserInfo(user);
	         */

		// 更新注册用户索引
		boolean gen = user.getGender() == 1 ? true : false;
		CbsSolrUtil.getInstance().updateIndex(user.getUserId(), user.getUserNo(), user.getNamePinyin(),
		        user.getUserName(), user.getUserPath(), gen, user.getStatus(), user.getLocal(), user.getAboutme(),
		        user.getBack(), user.getMobilePhone(), user.getCreateTime());

	    }
	    ret.setCode(DataResponse.OK);
	} catch (

	L99IllegalParamsException e)

	{
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(e.getMessage());
	} catch (

	L99IllegalDataException e)

	{
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(e.getMessage());
	} catch (

	L99NetworkException e)

	{
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(e.getMessage());
	} catch (

	JSONException e)

	{
	    ret.setMsg(e.getMessage());
	}

	return ret;

    }

    @Override
    public UserSpaceResponse findUserSpace(Long visitor, Long userId, Long longNO) throws JSONException, L99ExceptionBase {
	if (userId == null && longNO == null) {
	    throw new L99IllegalDataException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}

	// 查看用户信息
	CbsUser user = null;
	String userFindType = UserFindType.ID;
	String key = null;
	if (userId != null) {
	    user = cbsUserDao.selectById(userId);
	    key = userId.toString();
	} else if (longNO != null) {
	    user = cbsUserDao.getCbsUserByLongNo(longNO);
	    userFindType = UserFindType.LONGNO;
	    key = longNO.toString();
	}

	CbsUserResponse userResponse = null;
	if (user == null) {
	    try {
		// 查询一下是否是主站用户
		FullAccountResponse account = LifeixUserApiUtil.getInstance().findFullUser(key, userFindType, false);

		// 初始cbs信息
		userResponse = registerCbsUser(account, null, null, null);
	    } catch (Exception e) {
		throw new L99IllegalParamsException(UserMsg.CODE_USER_ACCOUNT_NOT_FOUND, UserMsg.KEY_USER_ACCOUNT_NOT_FOUND);
	    }
	} else {
	    userResponse = AccountTransformUtil.transformUser(user, false);
	}
	if (userResponse.getStatus() == UserConstants.BLOCK) {
	    throw new L99IllegalDataException(UserMsg.CODE_USER_ACCOUNT_BLOCK, UserMsg.KEY_USER_ACCOUNT_BLOCK);
	}

	userId = userResponse.getUser_id();

	UserSpaceResponse resp = new UserSpaceResponse();

	resp.setUser(userResponse);
	// 查询用户的统计信息
	UserContestStatisticsResponse statistics = null;
	try {
	    statistics = userContestStatisticsDubbo.getUserContestStatistics(userId);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    statistics = new UserContestStatisticsResponse();
	}
	resp.setStatistics(statistics);
	int followNum = 0;
	int followingNum = 0;
	try {
	    Map<String, Long> relationMap = cbsRelationshipService.getAllStatistisc(userId);
	    followNum = relationMap.get(RelationshipConstants.ACCOUNT_ATTENTION_ME) != null ? relationMap.get(
		    RelationshipConstants.ACCOUNT_ATTENTION_ME).intValue() : 0;
	    followingNum = relationMap.get(RelationshipConstants.ACCOUNT_ME_ATTENTION) != null ? relationMap.get(
		    RelationshipConstants.ACCOUNT_ME_ATTENTION).intValue() : 0;

	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
	// 查询关注数量和粉丝
	resp.setFollowed_num(followNum);// 关注我的人
	resp.setFollowing_num(followingNum);// 我关注的人

	if (followNum > 19) {
	    // 粉丝达到了20
	    missionUserService.validate(userId, Mission.FAN_20);
	}
	if (followingNum > 19) {
	    // 关注数达到了20
	    missionUserService.validate(userId, Mission.CONCERN_20);
	}

	resp.setRelationship_type(0);

	if (visitor != null) { // 当前访问处于登录

	    if (visitor.intValue() == userId.intValue()) { // 用户看自己
		// 看自己空间增加消息数量
		resp.setMsg(notifyService.getUnreadCount(userId));
		CustomResponse msg = notifyService.getUnreadCount(userId);
		msg.put("community_num", messageService.countUnreadMessages(userId));
		resp.setMsg(msg);

		// 查询用户龙币
		GoldResponse money = moneyService.viewUserMoney(userId, "");
		resp.getUser().setAvailableMoney(money == null ? null : money.getBalance());
	    } else { // 用户看别人
		List<Long> targetIds = new ArrayList<Long>();
		targetIds.add(userId);
		// 用户关注关系
		Map<Long, Integer> map = cbsRelationshipService.getAttentionType(visitor, targetIds);
		resp.setRelationship_type(map.get(userId));
	    }
	}

	// 查询用户最近5次成就
	UserAchieveResponse achieveResponse = null;
	try {
	    achieveResponse = achieveService.getUserLastLogs(userId, 5);
	} catch (Exception e) {
	    LOG.error(String.format("get user %d recent achieve faild - %s", userId, e.getMessage()), e);
	}
	resp.setAchieve(achieveResponse);
	// 查询用户积分
	MissionUserResponse missionUserResponse = missionUserService.getUserInfoSimple(userId);
	resp.setAmount(missionUserResponse.getAmount());

	return resp;

    }

    @Override
    public Map<Long, CbsUserResponse> findCsAccountMapByIds(List<Long> userIds) {
	Map<Long, CbsUserResponse> repMap = new HashMap<Long, CbsUserResponse>();
	Map<Long, CbsUser> userMap = cbsUserDao.findCsAccountMapByIds(userIds);
	if (userMap == null || userMap.isEmpty()) {
	    return repMap;
	}
	for (Map.Entry<Long, CbsUser> en : userMap.entrySet()) {
	    repMap.put(en.getKey(), AccountTransformUtil.transformUser(en.getValue(), true));
	}
	return repMap;
    }

    /*
     * （非 Javadoc）
     * 
     * @see
     * com.lifeix.tiyu.user.service.subscribe.UserSubscribeService#sendCode(int,
     * java.lang.String, java.lang.String)
     */
    // @Override
    public DataResponse<Object> sendCode(int authType, String authKey, String country) {
	DataResponse<Object> ret = new DataResponse<Object>();

	// 注册用户中心账号
	try {
	    ParamemeterAssert.assertDataNotNull(authKey);
	    LifeixUserApiUtil.getInstance().sendCode(authType, authKey, country);
	    ret.setCode(DataResponse.OK);
	} catch (L99NetworkException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(e.getMessage());

	} catch (JSONException e) {
	    ret.setMsg(e.getMessage());
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(e.getMessage());
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(Integer.valueOf(BasicMsg.CODE_BASIC_SERVCER));
	    ret.setMsg(BasicMsg.KEY_BASIC_SERVCER);
	}

	return ret;
    }

    /**
     * 连续登录奖励逻辑
     * 
     * @param userId
     * @param loginToken
     * @param machineCode
     * @param client
     * @param ipaddress
     */
    @Override
    public CustomResponse continueLoginGold(Long userId, String loginToken, String machineCode, String client,
	    String ipaddress) {
	CustomResponse ret = new CustomResponse();
	if (userId == null) {
	    return ret;
	}
	try {
	    CbsUserLogin cbsUserLogin = cbsUserLoginDao.selectById(userId);
	    int loginTimes = 1;
	    if (cbsUserLogin == null) { // 第一次初始化数据
		cbsUserLogin = new CbsUserLogin();
		cbsUserLogin.setUserId(userId);
		cbsUserLogin.setClient(client);
		cbsUserLogin.setContinuLogin(loginTimes);
		cbsUserLogin.setIpaddress(ipaddress);
		cbsUserLogin.setLoginTime(new Date());
		cbsUserLogin.setLoginToken(loginToken);
		cbsUserLogin.setMachineCode(machineCode);
		boolean flag = cbsUserLoginDao.insert(cbsUserLogin);
		if (!flag) {
		    return ret;
		}
	    } else {
		boolean nowFlag = false; // 同一天登录
		loginTimes = cbsUserLogin.getContinuLogin();
		Date lastLoginTime = cbsUserLogin.getLoginTime();
		if (CbsTimeUtils.inYerstoday(lastLoginTime)) { // 判读上次登录是否是昨天
		    loginTimes++;
		} else if (!CbsTimeUtils.inNow(lastLoginTime)) { // 判读上次登录是否是今天
		    loginTimes = 1;
		} else {
		    nowFlag = true;
		}
		cbsUserLogin.setClient(client);
		cbsUserLogin.setContinuLogin(loginTimes);
		cbsUserLogin.setIpaddress(ipaddress);
		cbsUserLogin.setLoginTime(new Date());
		cbsUserLogin.setLoginToken(loginToken);
		cbsUserLogin.setMachineCode(machineCode);
		boolean flag = cbsUserLoginDao.update(cbsUserLogin);
		if (!flag) {
		    return ret;
		}
		if (nowFlag) { // 同一天登录不获取奖励
		    return ret;
		}
	    }

	    // 连续登录奖励
	    int money = 0;
	    int hour = CouponSystem.TIME_12;
	    String desc = String.format("获得连续登录第%d天奖励", loginTimes);
	    // couponUserService.settleCouponByPrice(userId, money, hour, desc);
	    // 旧的登录送钱接口删除

	    // 添加用户连续登录获取龙筹消息
	    JSONObject json = new JSONObject();
	    json.put("loginTimes", loginTimes);
	    json.put("money", money);

	    ret.put("loginTimes", loginTimes);
	    ret.put("award", money);

	} catch (Exception e) {
	    LOG.error(String.format("user %d contine login faild - %s", userId, e.getMessage()), e);
	}
	return ret;
    }

    @Override
    public CbsUserResponse selectById(Long accountId) {
	CbsUserResponse rep = new CbsUserResponse();
	CbsUser user = cbsUserDao.selectById(accountId);
	if (user == null) {
	    return rep;
	}
	return AccountTransformUtil.transformUser(user, true);
    }

    @Override
    public CbsUserListResponse select(Long userId, String searchKey, Integer startId, Integer limit)
	    throws L99NetworkException, JSONException {

	try {
	    if (RegexUtil.hasMatche(searchKey, RegexUtil.LONGNO_REG)) { // 龙号搜索
		CbsUserListResponse response = new CbsUserListResponse();
		List<CbsUserResponse> users = new ArrayList<>();

		CbsUser user = cbsUserDao.getCbsUserByLongNo(Long.valueOf(searchKey));
		if (user != null) {
		    CbsUserResponse cu = AccountTransformUtil.transformUser(user, true);
		    boolean followFlag = cbsRelationshipService.isAttention(userId, user.getUserId());
		    cu.setRelationship_type(followFlag ? 1 : 0);
		    users.add(cu);
		    response.setUsers(users);
		    return response;
		}

	    }

	    // 名称搜索
	    String mutilSeachKey = getMutilSeachKey(searchKey);
	    String jsonStr = CbsSolrUtil.getInstance().select(mutilSeachKey, startId, limit);
	    JSONObject ret = new JSONObject(jsonStr);
	    int status = ret.getJSONObject("responseHeader").getInt("status");
	    if (status == 0) {
		return formatSearchData(userId, ret.getJSONObject("response"), false);
	    } else {
		JSONObject errorRet = ret.getJSONObject("error");
		throw new L99NetworkException(errorRet.optString("code"), errorRet.optString("msg"));
	    }
	} catch (ClientHandlerException e) {
	    throw new L99NetworkException(BasicMsg.CODE_USERSERVER, BasicMsg.KEY_USERSERVER);
	}

    }

    private CbsUserListResponse formatSearchData(Long userId, JSONObject response, boolean isMobile) throws JSONException {
	CbsUserListResponse resp = new CbsUserListResponse();
	JSONArray contentMainArray = response.getJSONArray("docs");
	List<CbsUserResponse> users = new ArrayList<CbsUserResponse>();
	List<Long> targetIds = new ArrayList<Long>();
	for (int i = 0; i < contentMainArray.length(); i++) {
	    JSONObject contentMainObj = contentMainArray.getJSONObject(i);
	    CbsUserResponse user = new CbsUserResponse();
	    Long user_id = contentMainObj.getLong("user_id");
	    Long user_no = contentMainObj.getLong("user_no");
	    int status = contentMainObj.getInt("status");

	    int gender = contentMainObj.getBoolean("gender") ? 1 : 0;
	    String user_name = contentMainObj.getString("user_name");
	    String user_path = contentMainObj.getString("user_path");
	    try {
		String name_pinyin = contentMainObj.getString("name_pinyin");
		user.setNamePinyin(name_pinyin);
	    } catch (Exception e) {
	    }

	    try {
		String mobile_phone = contentMainObj.getString("mobile_phone");
		if (!StringUtils.isEmpty(mobile_phone) && isMobile) {
		    user.setMobilePhone(mobile_phone);
		}
	    } catch (Exception e) {
	    }

	    user.setUser_id(user_id);
	    user.setLong_no(user_no);
	    user.setStatus(status);
	    user.setGender(gender);
	    user.setName(user_name);
	    user.setHead(user_path);

	    UserContestStatistics userStatisticsResponse = userContestStatisticsDao.getUserContestStatistics(user_id);
	    user.setUserStatisticsResponse(AccountTransformUtil.transformUserContestStatistics(userStatisticsResponse, 0));

	    users.add(user);
	    targetIds.add(user_id);
	}
	if (!targetIds.isEmpty() && userId != null) {
	    Map<Long, Integer> map = cbsRelationshipService.getAttentionType(userId, targetIds);
	    for (CbsUserResponse u : users) {
		u.setRelationship_type(map.get(u.getUser_id()));
	    }
	}

	resp.setUsers(users);
	return resp;
    }

    /**
     * 关键字搜索
     * 
     * @param key
     * @return
     */
    private String getMutilSeachKey(String key) {
	StringBuffer sb = new StringBuffer();
	if (RegexUtil.hasMatche(key, RegexUtil.MOBILE_ALL_REG)) { // 手机号搜索
	    sb.append(String.format("(name_pinyin:*%s* OR user_name:*%s* OR mobile_phone:%s)", key, key, key));
	} else {
	    String[] searchs = key.split(" ");
	    boolean firstFlag = true;
	    for (String sk : searchs) {
		if (!firstFlag) {
		    sb.append(" AND ");
		}
		sb.append(String.format("(name_pinyin:*%s* OR user_name:*%s*)", sk, sk));
		firstFlag = false;
	    }
	}
	return sb.toString();
    }

    @Override
    public CbsUserListResponse contacts(Long accountId, String mobilePhones) throws L99IllegalParamsException,
	    L99NetworkException, JSONException {
	CbsUserListResponse rep = new CbsUserListResponse();
	ParamemeterAssert.assertDataNotNull(accountId);
	if (StringUtils.isEmpty(mobilePhones)) {
	    return rep;
	}
	String[] mpa = mobilePhones.split(",");
	int lenght = mpa.length;
	int k = lenght % SOLR_LIMIT;
	int i = lenght / SOLR_LIMIT + (k == 0 ? 0 : 1);
	List<String> list = Arrays.asList(mpa);
	for (int j = 0; j < i; j++) {
	    List<String> tmpList = new ArrayList<String>();
	    if (j == i - 1) {
		tmpList = list.subList(j * SOLR_LIMIT, list.size());

	    } else {
		tmpList = list.subList(j * SOLR_LIMIT, (j + 1) * SOLR_LIMIT);
	    }

	    StringBuffer sb = new StringBuffer();
	    for (String s : tmpList) {
		sb.append("mobile_phone:" + s);
		sb.append(" OR ");
	    }
	    String jsonStr = CbsSolrUtil.getInstance().select(sb.substring(0, sb.length() - 3), null, SOLR_LIMIT);
	    JSONObject ret = new JSONObject(jsonStr);
	    int status = ret.getJSONObject("responseHeader").getInt("status");
	    if (status == 0) {
		CbsUserListResponse repSolr = formatSearchData(accountId, ret.getJSONObject("response"), true);
		List<CbsUserResponse> users = rep.getUsers();
		if (users == null || users.isEmpty()) {
		    rep.setUsers(repSolr.getUsers());
		} else {
		    users.addAll(repSolr.getUsers());
		}

	    } else {
		JSONObject errorRet = ret.getJSONObject("error");
		throw new L99NetworkException(errorRet.optString("code"), errorRet.optString("msg"));
	    }
	}
	return rep;

    }

    @Override
    public CustomResponse continueLoginTimes(Long userId) throws L99IllegalParamsException {
	CustomResponse resp = new CustomResponse();
	ParamemeterAssert.assertDataNotNull(userId);
	CbsUserLogin cbsUserLogin = cbsUserLoginDao.selectById(userId);
	boolean getAward = false;
	if (cbsUserLogin == null) {
	    resp.put("login_times", 0);
	    resp.put("get_award", getAward);
	} else {
	    int loginTimes = cbsUserLogin.getContinuLogin();
	    Date loginDate = cbsUserLogin.getLoginTime();
	    if (!CbsTimeUtils.inYerstoday(loginDate) && !CbsTimeUtils.inNow(loginDate)) { // 不是昨天，也不是今天登录
		loginTimes = 0;
	    }
	    if (CbsTimeUtils.inNow(loginDate)) {
		getAward = true;
	    }
	    resp.put("login_times", loginTimes);
	    resp.put("get_award", getAward);

	}

	GoldPrizeResponse denomination_five = new GoldPrizeResponse(5, 1);

	GoldPrizeListResponse day1 = new GoldPrizeListResponse();
	List<GoldPrizeResponse> day1_list = new ArrayList<GoldPrizeResponse>();
	day1_list.add(denomination_five);
	day1.setGold_prizes(day1_list);

	GoldPrizeListResponse day2 = new GoldPrizeListResponse();
	List<GoldPrizeResponse> day2_list = new ArrayList<GoldPrizeResponse>();
	day2_list.add(denomination_five);
	day2.setGold_prizes(day2_list);

	GoldPrizeListResponse day3 = new GoldPrizeListResponse();
	List<GoldPrizeResponse> day3_list = new ArrayList<GoldPrizeResponse>();
	day3_list.add(denomination_five);
	day3.setGold_prizes(day3_list);

	GoldPrizeListResponse day4 = new GoldPrizeListResponse();
	List<GoldPrizeResponse> day4_list = new ArrayList<GoldPrizeResponse>();
	day4_list.add(denomination_five);
	day4.setGold_prizes(day4_list);

	GoldPrizeListResponse day5 = new GoldPrizeListResponse();
	List<GoldPrizeResponse> day5_list = new ArrayList<GoldPrizeResponse>();
	// day5_list.add(denomination_fifth);
	day5_list.add(denomination_five);
	day5.setGold_prizes(day5_list);

	GoldPrizeListResponse day6 = new GoldPrizeListResponse();
	List<GoldPrizeResponse> day6_list = new ArrayList<GoldPrizeResponse>();
	day6_list.add(denomination_five);
	day6.setGold_prizes(day6_list);

	GoldPrizeListResponse day7 = new GoldPrizeListResponse();
	List<GoldPrizeResponse> day7_list = new ArrayList<GoldPrizeResponse>();
	day7_list.add(denomination_five);
	day7.setGold_prizes(day7_list);

	List<GoldPrizeListResponse> prizes = new ArrayList<GoldPrizeListResponse>();
	prizes.add(day1);
	prizes.add(day2);
	prizes.add(day3);
	prizes.add(day4);
	prizes.add(day5);
	prizes.add(day6);
	prizes.add(day7);

	// Long[] prizes = { 10L, 10L, 15L, 15L, 20L, 30L, 40L };
	resp.put("prizes", prizes);
	return resp;
    }

    @Override
    public CustomResponse authUser(String auth, String token, String ipaddress) throws L99IllegalParamsException,
	    JSONException, L99NetworkException, L99IllegalDataException {

	Long userId = null;
	if (auth != null) {
	    AccountResponse account = LifeixUserApiUtil.getInstance().authorizationUser(auth, ipaddress);
	    userId = account.getAccountId();
	} else if (token != null) {
	    userId = accountAuthAction.getUserIdByToken(token);
	}

	if (userId == null) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}

	// 用户龙币
	GoldResponse money = moneyService.viewUserMoney(userId, ipaddress);

	CustomResponse response = new CustomResponse();
	response.put("user_id", userId);
	response.put("money", CommerceMath.sub(money.getBalance(), money.getFrozen()));

	return response;
    }

    @Override
    public CustomResponse viewUserMoney(Long userId) throws L99IllegalParamsException, L99IllegalDataException,
	    JSONException {

	ParamemeterAssert.assertDataNotNull(userId);

	// 用户龙币
	GoldResponse money = moneyService.viewUserMoney(userId, null);

	CustomResponse response = new CustomResponse();
	response.put("user_id", userId);
	response.put("money", CommerceMath.sub(money.getBalance(), money.getFrozen()));

	return response;
    }

    /**
     * 检查是否存在大赢家用户 不存在则导入
     * 
     * @param userId
     * @param from
     */
    @Override
    public void checkCbsUser(Long userId, String from) {
	if (userId == null) {
	    return;
	}
	CbsUser user = cbsUserDao.selectById(userId);
	if (user == null) {
	    try {
		AccountResponse account = LifeixUserApiUtil.getInstance().findUser(userId);
		user = new CbsUser();
		user.setUserId(account.getAccountId());
		user.setUserNo(account.getLongNO());
		user.setUserName(account.getName());
		user.setUserPath(account.getPhotoPath());
		user.setStatus(account.getStatus());
		user.setNamePinyin(account.getNamePinyin());
		user.setMobilePhone(account.getMobilePhone());
		registerCbsUser(user);
		LOG.info(String.format("check cbs user %s from %s success", userId, from));
	    } catch (L99NetworkException | JSONException e) {
		LOG.error(String.format("check cbs user %s from %s failed - %s", userId, from, e.getMessage()));
	    }
	}
    }

    /**
     * 转换用户信息
     * 
     * @param account
     * @param gender
     * @param location
     * @param aboutme
     * @return
     */
    private CbsUserResponse registerCbsUser(FullAccountResponse account, Integer gender, String location, String aboutme) {

	if (account == null) {
	    return null;
	}
	CbsUser user = cbsUserDao.selectById(account.getAccountId());
	CbsUserResponse userResp = null;
	if (user == null) {

	    user = new CbsUser();
	    user.setUserId(account.getAccountId());
	    user.setUserNo(account.getLongNO());
	    user.setUserName(account.getName());
	    user.setUserPath(account.getPhotoPath());
	    user.setGender(gender);
	    user.setStatus(account.getStatus());
	    user.setLocal(location);
	    user.setAboutme(aboutme);
	    user.setNamePinyin(account.getNamePinyin());
	    user.setMobilePhone(account.getMobilePhone());
	    userResp = registerCbsUser(user);
	} else {
	    userResp = AccountTransformUtil.transformUser(user, true);
	}

	AccountTransformUtil.transformCbsUserResponse(account, userResp, false);

	return userResp;
    }

    /**
     * 初始化大赢家用户
     * 
     * @param newUser
     * @return
     */
    private CbsUserResponse registerCbsUser(CbsUser newUser) {

	if (newUser == null) {
	    return null;
	}

	if (newUser.getGender() == null && newUser.getAboutme() == null) { // 初始化扩展信息
	    try {
		AccountProfile profile = LifeixUserApiUtil.getInstance().findUserProfile(newUser.getUserId());
		newUser.setGender(profile.getGender());
		newUser.setAboutme(profile.getAboutMe());
	    } catch (Exception e) {
		LOG.warn(String.format("get user %d profile info fal %s", newUser.getUserId(), e.getMessage()));
	    }
	}
	newUser.setCreateTime(new Date());
	newUser.setBack(null);
	cbsUserDao.insertAndGetPrimaryKey(newUser);

	try {
	    // 添加用户注册赠送龙筹消息
	    couponUserService.settleCouponByPrice(newUser.getUserId(), CouponSystem.PIRCE_5, CouponSystem.TIME_72,
		    "用户注册赠送5龙筹");
	    couponUserService.settleCouponByPrice(newUser.getUserId(), CouponSystem.PIRCE_5, CouponSystem.TIME_72,
		    "用户注册赠送5龙筹");
	    couponUserService.settleCouponByPrice(newUser.getUserId(), CouponSystem.PIRCE_10, CouponSystem.TIME_72,
		    "用户注册赠送10龙筹");
	    JSONObject json = new JSONObject();
	    json.put("money", 20);
	    notifyService.addNotify(ContestConstants.TempletId.SYSTEM_ROIL_PRIZE, newUser.getUserId(), newUser.getUserId(),
		    json.toString(), null);
	} catch (L99IllegalParamsException e) {
	    LOG.info(String.format("init user gold failed - %s", e.getMessage()));
	} catch (L99IllegalDataException e) {
	    LOG.info(String.format("init user gold failed - %s", e.getMessage()));
	} catch (Exception e) {
	    LOG.error(String.format("init user gold failed - %s", e.getMessage()), e);
	}

	// Hello成就
	try {
	    UserAchieveBO bo = new UserAchieveBO();
	    bo.setBehavior_type(BehaviorConstants.BehaviorType.USER_TYPE);
	    bo.setUser_id(newUser.getUserId());
	    bo.setRegister_flag(true);
	    bo.setLogin_flag(true);
	    achieveService.addAchieveData(bo.generateData(), false);
	} catch (Throwable t) {
	    LOG.error("register achieve data analysis failed: " + t.getMessage(), t);
	}

	CbsUserResponse userResp = AccountTransformUtil.transformUser(newUser, true);

	// 更新注册用户索引
	boolean gen = newUser.getGender() == 1 ? true : false;
	CbsSolrUtil.getInstance().updateIndex(newUser.getUserId(), newUser.getUserNo(), newUser.getNamePinyin(),
	        newUser.getUserName(), newUser.getUserPath(), gen, newUser.getStatus(), newUser.getLocal(),
	        newUser.getAboutme(), newUser.getBack(), newUser.getMobilePhone(), newUser.getCreateTime());

	return userResp;
    }
}
