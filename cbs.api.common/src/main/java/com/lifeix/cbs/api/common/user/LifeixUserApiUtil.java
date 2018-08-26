package com.lifeix.cbs.api.common.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.qiniu.LifeixImageApiUtil;
import com.lifeix.common.utils.StringUtil;
import com.lifeix.exception.service.L99NetworkException;
import com.lifeix.user.api.action.account.AccountBasicAction;
import com.lifeix.user.api.action.account.AccountCommonAction;
import com.lifeix.user.api.action.account.AccountV2BasicAction;
import com.lifeix.user.api.action.avatar.AvatarAction;
import com.lifeix.user.api.action.log.AccountLogAction;
import com.lifeix.user.api.action.third.AccountThirdAction;
import com.lifeix.user.beans.account.AccountResponse;
import com.lifeix.user.beans.account.FullAccountResponse;
import com.lifeix.user.beans.log.AccountLogResponse;
import com.lifeix.user.beans.oauth.LifeixOAuthConsumer;
import com.lifeix.user.beans.profile.AccountProfile;

public class LifeixUserApiUtil {

    private LifeixUserApiUtil() {
	WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
	accountCommonAction = (AccountCommonAction) context.getBean("accountCommonAction");
	accountBasicAction = (AccountBasicAction) context.getBean("accountBasicAction");
	accountV2BasicAction = (AccountV2BasicAction) context.getBean("accountV2BasicAction");
	avatarAction = (AvatarAction) context.getBean("avatarAction");
	accountThirdAction = (AccountThirdAction) context.getBean("accountThirdAction");
	avatar = (String) context.getBean("avatar");
	accountLogAction = (AccountLogAction) context.getBean("accountLogAction");
    }

    private static class InstanceHolder {
	/**
	 * Add a private constructor to hide the implicit public one.
	 */
	private InstanceHolder() {
	}

	private static final LifeixUserApiUtil INSTANCE = new LifeixUserApiUtil();
    }

    public static LifeixUserApiUtil getInstance() {
	return InstanceHolder.INSTANCE;
    }

    private AccountCommonAction accountCommonAction;

    private AccountBasicAction accountBasicAction;

    private AccountV2BasicAction accountV2BasicAction;

    private AvatarAction avatarAction;

    private AccountThirdAction accountThirdAction;

    private AccountLogAction accountLogAction;

    private String avatar;

    public AccountResponse findUser(Long userId) throws JSONException, L99NetworkException {
	return findUser(userId.toString(), "id", false);
    }

    /**
     * 用户信息
     * 
     * @param key
     * @param type
     * @param blockFilter
     * @return
     * @throws JSONException
     * @throws L99NetworkException
     */
    public AccountResponse findUser(String key, String type, Boolean blockFilter) throws JSONException, L99NetworkException {
	String resp = accountCommonAction.findAccountById(key, type, blockFilter);
	if (resp == null) {
	    throw new L99NetworkException(BasicMsg.CODE_USERSERVER, BasicMsg.KEY_USERSERVER);
	}
	JSONObject ret = new JSONObject(resp);
	if (ret.getInt("code") == 200) {
	    return new Gson().fromJson(ret.get("data").toString(), AccountResponse.class);
	} else {
	    throw new L99NetworkException(ret.optString("code"), ret.optString("msg"));
	}
    }

    /**
     * 用户信息
     * 
     * @param key
     * @param type
     * @param blockFilter
     * @return
     * @throws JSONException
     * @throws L99NetworkException
     */
    public FullAccountResponse findFullUser(String key, String type, Boolean blockFilter) throws JSONException,
	    L99NetworkException {
	String resp = accountCommonAction.findAccountById(key, type, blockFilter);
	if (resp == null) {
	    throw new L99NetworkException(BasicMsg.CODE_USERSERVER, BasicMsg.KEY_USERSERVER);
	}
	JSONObject ret = new JSONObject(resp);
	if (ret.getInt("code") == 200) {
	    return new Gson().fromJson(ret.get("data").toString(), FullAccountResponse.class);
	} else {
	    throw new L99NetworkException(ret.optString("code"), ret.optString("msg"));
	}
    }

    /**
     * 用户扩展信息
     * 
     * @param accountId
     * @return
     * @throws JSONException
     * @throws L99NetworkException
     */
    public AccountProfile findUserProfile(Long accountId) throws JSONException, L99NetworkException {
	String resp = accountCommonAction.findAccountProfileById(accountId);
	if (resp == null) {
	    throw new L99NetworkException(BasicMsg.CODE_USERSERVER, BasicMsg.KEY_USERSERVER);
	}
	JSONObject ret = new JSONObject(resp);
	if (ret.getInt("code") == 200) {
	    return new Gson().fromJson(ret.get("data").toString(), AccountProfile.class);
	} else {
	    throw new L99NetworkException(ret.optString("code"), ret.optString("msg"));
	}

    }

    /**
     * 根据IDs查询用户
     * 
     * @param ids
     * @param type
     * @return
     * @throws JSONException
     * @throws L99NetworkException
     */
    public List<AccountResponse> findUserByIds(String ids, String type, Boolean blockFilter) throws JSONException,
	    L99NetworkException {
	String resp = accountCommonAction.findAccountByIds(ids, type, blockFilter);
	if (resp == null) {
	    throw new L99NetworkException(BasicMsg.CODE_USERSERVER, BasicMsg.KEY_USERSERVER);
	}
	JSONObject ret = new JSONObject(resp);
	if (ret.getInt("code") == 200) {
	    return new Gson().fromJson(ret.get("data").toString(), new TypeToken<ArrayList<AccountResponse>>() {
	    }.getType());
	} else {
	    throw new L99NetworkException(ret.optString("code"), ret.optString("msg"));
	}
    }

    /**
     * 根据IDs查询用户
     * 
     * @param ids
     * @param type
     * @return
     * @throws JSONException
     * @throws L99NetworkException
     */
    public Map<Long, AccountResponse> findUserMapByIds(String ids, String type, Boolean blockFilter) throws JSONException,
	    L99NetworkException {
	List<AccountResponse> userList = findUserByIds(ids, type, blockFilter);
	Map<Long, AccountResponse> userMap = new HashMap<Long, AccountResponse>();
	for (AccountResponse accountResponse : userList) {
	    userMap.put(accountResponse.getAccountId(), accountResponse);
	}
	return userMap;
    }

    /**
     * 校验用户信息
     * 
     * @param authorization
     * @param ipaddress
     * @return
     * @throws JSONException
     * @throws L99NetworkException
     */
    public AccountResponse authorizationUser(String authorization, String ipaddress) throws JSONException,
	    L99NetworkException {
	String resp = accountBasicAction.findAccountByAuthorization(authorization, ipaddress);
	if (resp == null) {
	    throw new L99NetworkException(BasicMsg.CODE_USERSERVER, BasicMsg.KEY_USERSERVER);
	}
	JSONObject ret = new JSONObject(resp);
	if (ret.getInt("code") == 200) {
	    return new Gson().fromJson(ret.get("data").toString(), AccountResponse.class);
	} else {
	    throw new L99NetworkException(ret.optString("code"), ret.optString("msg"));
	}
    }

    /**
     * 注册 第一步 用户输入手机或邮箱获取验证码(或为重新发送验证码)
     * 
     * @param authType
     * @param authKey
     * @param country
     * @throws JSONException
     * @throws L99NetworkException
     */
    public void sendCode(Integer authType, String authKey, String country) throws JSONException, L99NetworkException {
	String resp = accountV2BasicAction.sendCode(authType, authKey, country, "tiyu");
	if (resp == null) {
	    throw new L99NetworkException(BasicMsg.CODE_USERSERVER, BasicMsg.KEY_USERSERVER);
	}
	JSONObject ret = new JSONObject(resp);
	if (ret.getInt("code") == 200) {
	    return;
	} else {
	    throw new L99NetworkException(ret.optString("code"), ret.optString("msg"));
	}
    }

    /**
     * 注册 第二步 用户输入验证码进行校验
     * 
     * @param authType
     * @param authKey
     * @param authCode
     * @param country
     * @param ipaddress
     * @throws JSONException
     * @throws L99NetworkException
     */
    public void checkCode(Integer authType, String authKey, String authCode, String country, String ipaddress)
	    throws JSONException, L99NetworkException {
	String resp = accountV2BasicAction.checkcode(authType, authKey, authCode, country, ipaddress);
	if (resp == null) {
	    throw new L99NetworkException(BasicMsg.CODE_USERSERVER, BasicMsg.KEY_USERSERVER);
	}
	JSONObject ret = new JSONObject(resp);
	if (ret.getInt("code") == 200) {
	    return;
	} else {
	    throw new L99NetworkException(ret.optString("code"), ret.optString("msg"));
	}
    }

    /**
     * 注册 第三步 注册生成账号
     * 
     * @param authType
     *            验证类型 10 邮箱 20 手机
     * @param authKey
     *            邮箱 or 手机
     * @param accountType
     *            用户类型 0 user 100 商家 101 机构
     * @param name
     *            用户姓名
     * @param password
     *            用户密码
     * @param gender
     *            性别
     * @param ipaddress
     *            注册IP地址
     * @param randCode
     *            随机码(如果需要)
     * @param machineCode
     *            机器码
     * @param client
     *            客户端标识
     * @param market
     *            市场标识
     * @param country
     *            国家code
     * @return
     */
    public FullAccountResponse register(Integer authType, String authKey, Integer accountType, String name, String password,
	    Integer gender, String photoPath, String randCode, String machineCode, String client, String market,
	    String country, String ipaddress, boolean ignoreName) throws JSONException, L99NetworkException {
	String resp = accountV2BasicAction.register(authType, authKey, accountType, name, password, gender, photoPath,
	        ipaddress, randCode, machineCode, client, market, "tiyu", country);
	if (resp == null) {
	    throw new L99NetworkException(BasicMsg.CODE_USERSERVER, BasicMsg.KEY_USERSERVER);
	}
	JSONObject ret = new JSONObject(resp);
	if (ret.getInt("code") == 200) {
	    return new Gson().fromJson(ret.get("data").toString(), FullAccountResponse.class);
	} else if (ignoreName && ret.getInt("code") == 11022) { // 名字包含关键字
	    return register(authType, authKey, accountType, "游客" + StringUtil.createRandom(false, 4), password, gender,
		    photoPath, randCode, machineCode, client, market, country, ipaddress, false);
	} else {
	    throw new L99NetworkException(ret.optString("code"), ret.optString("msg"));
	}
    }

    /**
     * 登录接口
     * 
     * @param type
     * @param name
     * @param password
     * @param country
     * @param encryptFlag
     * @param devToken
     * @param machineCode
     * @param requestClient
     * @param version
     * @param ipaddress
     * @return
     * @throws JSONException
     * @throws L99NetworkException
     */
    public FullAccountResponse login(Integer type, String name, String password, String country, Boolean encryptFlag,
	    String devToken, String machineCode, String requestClient, String version, String ipaddress)
	    throws JSONException, L99NetworkException {
	String resp = accountBasicAction.login(type, name, password, country, encryptFlag, devToken, ipaddress,
	        requestClient, machineCode, null, null);
	if (resp == null) {
	    throw new L99NetworkException(BasicMsg.CODE_USERSERVER, BasicMsg.KEY_USERSERVER);
	}
	JSONObject ret = new JSONObject(resp);
	if (ret.getInt("code") == 200) {
	    return new Gson().fromJson(ret.get("data").toString(), FullAccountResponse.class);
	} else {
	    throw new L99NetworkException(ret.optString("code"), ret.optString("msg"));
	}
    }

    /**
     * 修改姓名
     * 
     * @param accountId
     * @param name
     * @throws JSONException
     * @throws L99NetworkException
     */
    public void updateName(Long accountId, String name) throws JSONException, L99NetworkException {
	String resp = accountCommonAction.updateInfo(accountId, name, 1);
	if (resp == null) {
	    throw new L99NetworkException(BasicMsg.CODE_USERSERVER, BasicMsg.KEY_USERSERVER);
	}
	JSONObject ret = new JSONObject(resp);
	if (ret.getInt("code") == 200) {
	    return;
	} else {
	    throw new L99NetworkException(ret.optString("code"), ret.optString("msg"));
	}
    }

    /**
     * 修改性别
     * 
     * @param accountId
     * @param gender
     * @throws JSONException
     * @throws L99NetworkException
     */
    public void updateGender(Long accountId, Integer gender) throws JSONException, L99NetworkException {
	String resp = accountCommonAction.updateInfo(accountId, gender.toString(), 2);
	if (resp == null) {
	    throw new L99NetworkException(BasicMsg.CODE_USERSERVER, BasicMsg.KEY_USERSERVER);
	}
	JSONObject ret = new JSONObject(resp);
	if (ret.getInt("code") == 200) {
	    return;
	} else {
	    throw new L99NetworkException(ret.optString("code"), ret.optString("msg"));
	}
    }

    /**
     * 修改头像
     * 
     * @param accountId
     * @param avatarId
     * @param userPath
     * @throws JSONException
     * @throws L99NetworkException
     */
    public void updateUserPath(Long accountId, Long avatarId, String userPath) throws JSONException, L99NetworkException {
	String resp = avatarAction.updateMainAvatar(accountId, avatarId, userPath, false);
	if (resp == null) {
	    throw new L99NetworkException(BasicMsg.CODE_USERSERVER, BasicMsg.KEY_USERSERVER);
	}
	JSONObject ret = new JSONObject(resp);
	if (ret.getInt("code") == 200) {
	    // 将头像保存到redis
	    LifeixImageApiUtil.getInstance().putUploadRedis(avatar, userPath);
	    return;
	} else {
	    if (ret.getInt("code") == 11021) {
		throw new L99NetworkException(ret.optString("code"), "用户头像不存在！");
	    } else {
		throw new L99NetworkException(ret.optString("code"), ret.optString("msg"));
	    }

	}
    }

    /**
     * 第三方登录注册
     * 
     * @param oauthType
     * @param usercode
     * @param username
     * @param userhead
     * @param gender
     * @param machineCode
     * @param client
     * @param market
     * @param country
     * @param ipaddress
     * @return
     * @throws JSONException
     * @throws L99NetworkException
     */
    public FullAccountResponse loginBySdk(String oauthType, String usercode, String username, String userhead,
	    Integer gender, String machineCode, String client, String market, String country, String ipaddress)
	    throws JSONException, L99NetworkException {
	String resp = accountThirdAction.loginBySdk(oauthType, usercode, username, userhead, gender, ipaddress);
	if (resp == null) {
	    throw new L99NetworkException(BasicMsg.CODE_USERSERVER, BasicMsg.KEY_USERSERVER);
	}
	JSONObject ret = new JSONObject(resp);
	FullAccountResponse account = null;
	if (ret.getInt("code") == 200) {
	    account = new Gson().fromJson(ret.get("data").toString(), FullAccountResponse.class);
	    account.setOnline(false); // 正常登录
	} else if (ret.getInt("code") == 11020) {
	    JSONObject msgObj = new JSONObject(ret.getString("msg"));
	    String rand_code = msgObj.optString("rand_code");
	    Integer auth_type = msgObj.optInt("auth_type");
	    String auth_key = msgObj.optString("auth_key");
	    String user_name = msgObj.optString("user_name");
	    String user_head = msgObj.optString("user_head");
	    Integer gender2 = msgObj.optInt("gender", 1);

	    // 微信的性别是错误的 需要调整
	    if (auth_type == 240 || auth_type == 241) {
		if (gender2 == 2) {
		    gender2 = 0;
		} else if (gender2 == 0) {
		    gender2 = 2;
		}
	    }

	    String password = createRandom(true, 1) + createRandom(false, 7);
	    account = register(auth_type, auth_key, 0, user_name, password, gender2, user_head, rand_code, machineCode,
		    client, market, country, ipaddress, true);
	    account.setOnline(true); // 注册
	} else {
	    throw new L99NetworkException(ret.optString("code"), ret.optString("msg"));
	}
	return account;
    }

    /**
     * 发送验证码
     * 
     * @param authType
     * @param authKey
     * @param country
     * @param source
     * @throws JSONException
     * @throws L99NetworkException
     */
    public void sendAuthenticationMobileCode(Integer authType, String authKey, String country, String source)
	    throws JSONException, L99NetworkException {
	String resp = accountBasicAction.sendAuthenticationMobileCode(authType, authKey, country, source);
	if (resp == null) {
	    throw new L99NetworkException(BasicMsg.CODE_USERSERVER, BasicMsg.KEY_USERSERVER);
	}
	JSONObject ret = new JSONObject(resp);
	if (ret.getInt("code") != 200) {
	    throw new L99NetworkException(ret.optString("code"), ret.optString("msg"));
	}
    }

    /**
     * 绑定验证
     * 
     * @param accountId
     * @param authType
     * @param authKey
     * @param randCode
     * @param country
     * @throws L99NetworkException
     * @throws JSONException
     */
    public void bindAuthenticationByMobileCode(Long accountId, Integer authType, String authKey, String randCode,
	    String country) throws L99NetworkException, JSONException {
	String resp = accountBasicAction.bindAuthenticationByMobileCode(accountId, authType, authKey, randCode, country);
	if (resp == null) {
	    throw new L99NetworkException(BasicMsg.CODE_USERSERVER, BasicMsg.KEY_USERSERVER);
	}
	JSONObject ret = new JSONObject(resp);
	if (ret.getInt("code") != 200) {
	    throw new L99NetworkException(ret.optString("code"), ret.optString("msg"));
	}
    }

    /**
     * 修改密码
     * 
     * @param accountId
     * @param newPassword
     * @param oldPassword
     * @throws L99NetworkException
     * @throws JSONException
     */
    public FullAccountResponse updatePassword(Long accountId, String newPassword, String oldPassword)
	    throws L99NetworkException, JSONException {
	String resp = accountBasicAction.updatePassword(accountId, newPassword, oldPassword);
	if (resp == null) {
	    throw new L99NetworkException(BasicMsg.CODE_USERSERVER, BasicMsg.KEY_USERSERVER);
	}

	JSONObject ret = new JSONObject(resp);
	if (ret.getInt("code") != 200) {
	    throw new L99NetworkException(ret.optString("code"), ret.optString("msg"));
	} else if (ret.getInt("code") == 200) {
	    return new Gson().fromJson(ret.get("data").toString(), FullAccountResponse.class);
	}
	return null;
    }

    /**
     * app端找回密码 第一步发送验证码
     * 
     * @param type
     * @param key
     * @param country
     * @return
     * @throws L99NetworkException
     * @throws JSONException
     */
    public Long findMobilePassword(Integer type, String key, String country) throws L99NetworkException, JSONException {
	String resp = accountBasicAction.findMobilePassword(type, key, country);
	if (resp == null) {
	    throw new L99NetworkException(BasicMsg.CODE_USERSERVER, BasicMsg.KEY_USERSERVER);
	}

	JSONObject ret = new JSONObject(resp);
	if (ret.getInt("code") != 200) {
	    throw new L99NetworkException(ret.optString("code"), ret.optString("msg"));
	} else {
	    JSONObject rett = new JSONObject(ret.get("data").toString());
	    return rett.getLong("account_id");
	}
    }

    /**
     * 校验验证码是否有效
     * 
     * @param accountId
     * @param code
     * @return
     * @throws L99NetworkException
     * @throws JSONException
     */
    public void isPasswordCodeValidate(Long accountId, String code) throws L99NetworkException, JSONException {

	String resp = accountBasicAction.isPasswordCodeValidate(accountId, code);
	if (resp == null) {
	    throw new L99NetworkException(BasicMsg.CODE_USERSERVER, BasicMsg.KEY_USERSERVER);
	}

	JSONObject ret = new JSONObject(resp);
	if (ret.getInt("code") != 200) {
	    throw new L99NetworkException(ret.optString("code"), ret.optString("msg"));
	}

    }

    /**
     * 根据验证码重设密码
     * 
     * @param accountId
     * @param newPassword
     * @param code
     * @return
     * @throws L99NetworkException
     * @throws JSONException
     */
    public void resetPassword(Long accountId, String newPassword, String code) throws L99NetworkException, JSONException {

	String resp = accountBasicAction.resetPassword(accountId, newPassword, code);
	if (resp == null) {
	    throw new L99NetworkException(BasicMsg.CODE_USERSERVER, BasicMsg.KEY_USERSERVER);
	}

	JSONObject ret = new JSONObject(resp);
	if (ret.getInt("code") != 200) {
	    throw new L99NetworkException(ret.optString("code"), ret.optString("msg"));
	}
    }

    /**
     * 返回用户微信的认证信息
     * 
     * @param userId
     * @return
     */
    public LifeixOAuthConsumer findWxAccessToken(Long userId) throws L99NetworkException, JSONException {

	String resp = accountThirdAction.findWxAccessToken(userId);
	if (resp == null) {
	    throw new L99NetworkException(BasicMsg.CODE_USERSERVER, BasicMsg.KEY_USERSERVER);
	}

	JSONObject ret = new JSONObject(resp);
	if (ret.getInt("code") != 200) {
	    throw new L99NetworkException(ret.optString("code"), ret.optString("msg"));
	} else if (ret.getInt("code") == 200) {
	    if (ret.has("data")) {
		return new Gson().fromJson(ret.get("data").toString(), LifeixOAuthConsumer.class);
	    }
	}
	return null;
    }

    /**
     * 返回用户微信的认证信息
     * 
     * @param userId
     * @return
     */
    public List<LifeixOAuthConsumer> findWxAccessTokens(String userId, String prividers) throws L99NetworkException,
	    JSONException {

	String resp = accountThirdAction.findConsumerByUserIdsAndTypes(userId, prividers);
	if (resp == null) {
	    throw new L99NetworkException(BasicMsg.CODE_USERSERVER, BasicMsg.KEY_USERSERVER);
	}

	JSONObject ret = new JSONObject(resp);
	if (ret.getInt("code") != 200) {
	    throw new L99NetworkException(ret.optString("code"), ret.optString("msg"));
	} else if (ret.getInt("code") == 200) {
	    if (ret.has("data")) {
		return new Gson().fromJson(ret.get("data").toString(), new TypeToken<ArrayList<LifeixOAuthConsumer>>() {
		}.getType());
	    }
	}
	return null;
    }

    /**
     * 更新用户微信的认证信息
     * 
     * @param accountId
     * @param usercode
     * @param accessToken
     * @param refreshToken
     * @param openId
     * @param expiresIn
     * @return
     */
    public void refreshWxOauth(Long accountId, String usercode, String accessToken, String refreshToken, String openId,
	    Integer expiresIn) throws L99NetworkException, JSONException {

	String resp = accountThirdAction.refreshWxOauth(accountId, usercode, accessToken, refreshToken, openId, expiresIn);
	if (resp == null) {
	    throw new L99NetworkException(BasicMsg.CODE_USERSERVER, BasicMsg.KEY_USERSERVER);
	}

	JSONObject ret = new JSONObject(resp);
	if (ret.getInt("code") != 200) {
	    throw new L99NetworkException(ret.optString("code"), ret.optString("msg"));
	}
    }

    /**
     * 创建指定数量的随机字符串
     * 
     * @param letterFlag
     *            是否是数字
     * @param length
     * @return
     */
    public static String createRandom(boolean letterFlag, int length) {
	String retStr = "";
	String strTable = letterFlag ? "abcdefghijkmnpqrstuvwxyz" : "1234567890abcdefghijkmnpqrstuvwxyz";
	int len = strTable.length();
	retStr = "";
	for (int i = 0; i < length; i++) {
	    double dblR = Math.random() * len;
	    int intR = (int) Math.floor(dblR);
	    // char c = strTable.charAt(intR);
	    /*
	     * if (('0' <= c) && (c <= '9')) { count++; }
	     */
	    retStr += strTable.charAt(intR);
	}

	return retStr;
    }

    /**
     * 根据时间查询注册渠道日志
     * 
     * @param date
     * @param startId
     * @param limit
     * @return
     * @throws JSONException
     * @throws L99NetworkException
     */
    public List<AccountLogResponse> findRegisterMarket(String date, Long startId, Integer limit) throws JSONException,
	    L99NetworkException {
	String resp = accountLogAction.findRegisterMarket(date, "roi", startId, limit);
	if (resp == null) {
	    throw new L99NetworkException(BasicMsg.CODE_USERSERVER, BasicMsg.KEY_USERSERVER);
	}
	JSONObject ret = new JSONObject(resp);
	if (ret.getInt("code") == 200) {
	    return new Gson().fromJson(ret.get("data").toString(), new TypeToken<ArrayList<AccountLogResponse>>() {
	    }.getType());
	} else {
	    throw new L99NetworkException(ret.optString("code"), ret.optString("msg"));
	}
    }

    public static void main(String[] args) {
	String s = createRandom(false, 1);
	System.out.println(s);
    }

    public void setAccountCommonAction(AccountCommonAction accountCommonAction) {
	this.accountCommonAction = accountCommonAction;
    }

    public void setAccountBasicAction(AccountBasicAction accountBasicAction) {
	this.accountBasicAction = accountBasicAction;
    }

    public void setAvatarAction(AvatarAction avatarAction) {
	this.avatarAction = avatarAction;
    }

    public void setAccountV2BasicAction(AccountV2BasicAction accountV2BasicAction) {
	this.accountV2BasicAction = accountV2BasicAction;
    }

    public void setAccountThirdAction(AccountThirdAction accountThirdAction) {
	this.accountThirdAction = accountThirdAction;
    }

    public void setAccountLogAction(AccountLogAction accountLogAction) {
	this.accountLogAction = accountLogAction;
    }

}
