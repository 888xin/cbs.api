package com.lifeix.cbs.api.action.user;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.alibaba.dubbo.rpc.RpcException;
import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.bean.user.CbsUserListResponse;
import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.cbs.api.bean.user.UserSpaceResponse;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.user.LifeixUserApiUtil;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.common.util.Md5Encrypt;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.impl.util.WeixinHelper;
import com.lifeix.cbs.api.service.gold.GoldLogService;
import com.lifeix.cbs.api.service.money.MoneyService;
import com.lifeix.cbs.api.service.user.CbsUserService;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.api.util.IPUtil;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;
import com.lifeix.user.api.action.account.AccountAuthAction;
import com.lifeix.user.beans.CustomResponse;
import com.lifeix.user.beans.account.FullAccountResponse;

/**
 * 用户接口
 * 
 * @author lifeix-sz
 * 
 */
@Controller
@Path("/user")
public class CbsUserAction extends BaseAction {

    /**
     * 客户端token后缀
     */
    public static final String PLAINTEXT = "lifei-cbs-123!@#";

    @Autowired
    private CbsUserService cbsUserService;

    @Autowired
    AccountAuthAction accountAuthAction;

    @Autowired
    MoneyService moneyService;

    @Autowired
    private GoldLogService goldLogService;

    /**
     * 第一步 用户输入手机或邮箱获取验证码(或为重新发送验证码)
     * 
     * @param format
     * @param authType
     * @param authKey
     * @param country
     *            国家代码
     * @return
     */
    @POST
    @Path("/code")
    @Produces(MediaType.APPLICATION_JSON)
    public String code(@FormParam("auth_type") @DefaultValue("10") int authType, @FormParam("auth_key") String authKey,
	    @FormParam("country") String country) {
	start();
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    ParamemeterAssert.assertDataNotNull(authKey);
	    LifeixUserApiUtil.getInstance().sendCode(authType, authKey, country);
	    ret.setCode(DataResponse.OK);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (L99NetworkException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(e.getMessage());
	} catch (RpcException e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.CODE_USER_DUBBO, request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 注册第二部 用户输入验证码进行校验
     * 
     * @param format
     * @param authType
     * @param authKey
     * @param authCode
     * @param country
     *            国家代码
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/code/check")
    @Produces(MediaType.APPLICATION_JSON)
    public String checkcode(@FormParam("auth_type") @DefaultValue("10") int authType, @FormParam("auth_key") String authKey,
	    @FormParam("auth_code") String authCode, @FormParam("country") String country) throws JSONException {
	start();
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    ParamemeterAssert.assertDataNotNull(authKey, authCode);
	    LifeixUserApiUtil.getInstance().checkCode(authType, authKey, authCode, country, IPUtil.getIpAddr(request));
	    ret.setCode(DataResponse.OK);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (L99NetworkException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(e.getMessage());
	} catch (RpcException e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.CODE_USER_DUBBO, request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 第三步 注册生成账号
     * 
     * @param format
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
     * @param randCode
     *            随机码(如果需要)
     * @param machineCode
     *            机器码
     * @param client
     *            客户端标识
     * @param market
     *            市场标识
     * @param source
     *            应用源
     * @param country
     *            国家code
     * @return
     */
    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    public String register(@FormParam("auth_type") @DefaultValue("10") int authType, @FormParam("auth_key") String authKey,
	    @FormParam("account_type") @DefaultValue("0") int accountType, @FormParam("name") String name,
	    @FormParam("password") String password, @FormParam("gender") @DefaultValue("1") int gender,
	    @FormParam("rand_code") String randCode, @FormParam("machine_code") String machineCode,
	    @FormParam("client") @DefaultValue("undefined") String client, @FormParam("market") String market,
	    @FormParam("source") @DefaultValue("dovebox") String source, @FormParam("country") String country) {
	start();
	DataResponse<CbsUserResponse> ret = new DataResponse<CbsUserResponse>();
	ret.setCode(DataResponse.OK);
	try {
	    CbsUserResponse data = cbsUserService.register(authType, authKey, accountType, name, password, gender,
		    machineCode, randCode, client, market, country, IPUtil.getIpAddr(request));
	    String auth = accountAuthAction.createToken(data.getUser_id(), false, "");

	    if (auth == null) {
		throw new L99NetworkException(BasicMsg.CODE_USERSERVER, BasicMsg.KEY_USERSERVER);
	    }
	    JSONObject json = new JSONObject(auth);
	    if (json.getInt("code") == 200) {
		JSONObject rett = new JSONObject(json.get("data").toString());
		data.setToken(rett.getString("token"));
	    } else {
		throw new L99NetworkException(json.optString("code"), json.optString("msg"));
	    }
	    ret.setData(data);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (L99NetworkException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(e.getMessage());
	} catch (RpcException e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.CODE_USER_DUBBO, request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 第三方登录或注册
     * 
     * @param oauthType
     * @param usercode
     * @param username
     * @param userhead
     * @param gender
     * @param machineCode
     * @param market
     * @param client
     * @param sign
     * @param country
     * @return
     */
    @POST
    @Path("/third")
    @Produces(MediaType.APPLICATION_JSON)
    public String third(@FormParam("oauth_type") String oauthType, @FormParam("usercode") String usercode,
	    @FormParam("username") String username, @FormParam("userhead") String userhead,
	    @FormParam("gender") @DefaultValue("1") int gender, @FormParam("machine_code") String machineCode,
	    @FormParam("market") String market, @FormParam("client") @DefaultValue("undefined") String client,
	    @FormParam("sign") String sign, @FormParam("country") String country, @FormParam("open_id") String openId) {
	start();
	DataResponse<CbsUserResponse> ret = new DataResponse<CbsUserResponse>();
	ret.setCode(DataResponse.OK);
	try {
	    CbsUserResponse data = cbsUserService.loginByThird(oauthType, usercode, username, userhead, gender, machineCode,
		    client, market, country, sign, IPUtil.getIpAddr(request), openId);

	    LOG.info(String.format("user [%d] == openid [%s]", data.getUser_id(), openId));
	    ret.setData(data);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (L99NetworkException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(e.getMessage());
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}

	return DataResponseFormat.response(ret);
    }

    /**
     * 微信公众号登录
     * 
     * @param code
     * @param state
     * @param ipaddress
     * @return
     */
    @GET
    @Path("/wxmp/login")
    @Produces(MediaType.APPLICATION_JSON)
    public String wxmpLogin(@QueryParam("code") String code, @QueryParam("state") String state,
	    @QueryParam("ipaddress") String ipaddress) {
	start();
	DataResponse<CustomResponse> ret = new DataResponse<CustomResponse>();
	ret.setCode(DataResponse.OK);
	try {
	    CustomResponse rep = WeixinHelper.getInstance().weixinLogin(code, state, ipaddress);
	    ret.setData(rep);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (L99IllegalDataException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (L99NetworkException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(e.getMessage());
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 第三方 用户输入手机或邮箱获取验证码(或为重新发送验证码)
     * 
     * @param format
     * @param authType
     * @param authKey
     * @param country
     *            国家代码
     * @return
     */
    @POST
    @Path("/third/code")
    @Produces(MediaType.APPLICATION_JSON)
    public String reCode(@FormParam("auth_type") @DefaultValue("20") int authType, @FormParam("auth_key") String authKey,
	    @FormParam("country") String country, @FormParam("source") String source) {
	start();
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    ParamemeterAssert.assertDataNotNull(authKey);
	    LifeixUserApiUtil.getInstance().sendAuthenticationMobileCode(authType, authKey, country, source);
	    ret.setCode(DataResponse.OK);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (L99NetworkException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(e.getMessage());
	} catch (RpcException e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.CODE_USER_DUBBO, request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 第三方 用户输入验证码进行校验
     * 
     * @param format
     * @param authType
     * @param authKey
     * @param authCode
     * @param country
     *            国家代码
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/third/code/check")
    @Produces(MediaType.APPLICATION_JSON)
    public String reCheckcode(@FormParam("auth_type") @DefaultValue("10") int authType,
	    @FormParam("account_id") Long accountId, @FormParam("auth_key") String authKey,
	    @FormParam("auth_code") String authCode, @FormParam("country") String country) throws JSONException {
	start();
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    ParamemeterAssert.assertDataNotNull(authKey, authCode);
	    LifeixUserApiUtil.getInstance().bindAuthenticationByMobileCode(accountId, authType, authKey, authCode, country);
	    ret.setCode(DataResponse.OK);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (L99NetworkException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(e.getMessage());
	} catch (RpcException e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.CODE_USER_DUBBO, request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * login
     * 
     * @param request
     * @param format
     * @param type
     *            //登录类型 0 默认值 | 1 龙号 | 2 手机号 | 3 邮箱
     * @param username
     * @param password
     * @param country
     *            // 国家代码，只有登录类型为手机号的时候需要
     * @param encryptFlag
     * @param devToken
     * @param version
     * @param client
     * @param machineCode
     * @return
     */
    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public String login(@Context HttpServletRequest request, @FormParam("type") @DefaultValue("0") Integer type,
	    @FormParam("username") String username, @FormParam("password") String password,
	    @FormParam("country") String country, @FormParam("encrypt_flag") @DefaultValue("false") boolean encryptFlag,
	    @FormParam("dev_token") String devToken, @FormParam("version") String version,
	    @FormParam("client") String client, @FormParam("machine_code") String machineCode) {
	start();
	DataResponse<CbsUserResponse> ret = new DataResponse<CbsUserResponse>();
	ret.setCode(DataResponse.OK);
	try {
	    String timezone = Calendar.getInstance(request.getLocale()).getTimeZone().getID();

	    CbsUserResponse data = cbsUserService.login(username, password, type, country, version, machineCode,
		    encryptFlag, IPUtil.getIpAddr(request), request.getHeader("User-Agent"), timezone, client, devToken);
	    ret.setData(data);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(e.getMessage());
	} catch (L99NetworkException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(e.getMessage());
	} catch (RpcException e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.CODE_USER_DUBBO, request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 更新用户信息
     * 
     * @param type
     * @param name
     * @param avatarId
     * @param userPath
     * @param gender
     * @param aboutme
     * @param back
     * @return
     */
    @POST
    @Path("/edit")
    @Produces(MediaType.APPLICATION_JSON)
    public String edit(@FormParam("type") Integer type, @FormParam("name") String name,
	    @FormParam("avatar_id") Long avatarId, @FormParam("user_path") String userPath,
	    @FormParam("gender") @DefaultValue("1") int gender, @FormParam("aboutme") String aboutme,
	    @FormParam("back") String back) {
	start();
	try {
	    DataResponse<Object> ret = cbsUserService.updateUserInfo(getSessionAccount(request), type, name, avatarId,
		    userPath, gender, aboutme, back);
	    if (ret.getCode() != DataResponse.OK) { // 错误国际化翻译
		ret.setMsg(InternationalResources.getInstance().locale(ret.getMsg(), request.getLocale()));
	    }
	    return DataResponseFormat.response(ret);
	} finally {
	    end();
	}
    }

    @POST
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@FormParam("long_no") Long longNO) {
	start();
	DataResponse<CbsUserResponse> ret = new DataResponse<CbsUserResponse>();
	ret.setCode(DataResponse.OK);
	try {
	    ret.setData(cbsUserService.getCbsUserByLongNo(longNO));
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(e.getMessage());
	} catch (RpcException e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.CODE_USER_DUBBO, request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}

	return DataResponseFormat.response(ret);
    }

    /**
     * 修改密码
     * 
     * @param newPassword
     * @param oldPassword
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/updatePassword")
    @Produces(MediaType.APPLICATION_JSON)
    public String updatePassword(@FormParam("new_password") String newPassword, @FormParam("old_password") String oldPassword)
	    throws JSONException {
	start();
	DataResponse<Map<String, Object>> ret = new DataResponse<Map<String, Object>>();
	try {
	    ParamemeterAssert.assertDataNotNull(newPassword, oldPassword);
	    FullAccountResponse rep = LifeixUserApiUtil.getInstance().updatePassword(getSessionAccount(request),
		    newPassword, oldPassword);
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("password", rep.getPassword());
	    ret.setCode(DataResponse.OK);
	    ret.setData(map);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (L99NetworkException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(e.getMessage());
	} catch (RpcException e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.CODE_USER_DUBBO, request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * app端找回密码 第一步发送验证码
     * 
     * @param type
     * @param key
     * @param country
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/reset/findMobilePassword")
    @Produces(MediaType.APPLICATION_JSON)
    public String findMobilePassword(@FormParam("type") Integer type, @FormParam("key") String key,
	    @FormParam("country") String country) throws JSONException {
	start();
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    ParamemeterAssert.assertDataNotNull(key);
	    Long accountId = LifeixUserApiUtil.getInstance().findMobilePassword(type, key, country);
	    ret.setCode(DataResponse.OK);
	    if (accountId == null) {
		ret.setCode(DataResponse.NO);
		ret.setMsg("该手机下不存在账号");
	    } else {
		CustomResponse c = new CustomResponse();
		c.put("user_id", accountId);
		ret.setData(c);
	    }

	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (L99NetworkException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(e.getMessage());
	} catch (RpcException e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.CODE_USER_DUBBO, request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * isPasswordCodeValidate
     * 
     * @param accountId
     * @param code
     * @return
     */
    @POST
    @Path("/reset/resetPassword")
    @Produces(MediaType.APPLICATION_JSON)
    public String resetPassword(@FormParam("account_id") Long accountId, @FormParam("code") String code,
	    @FormParam("new_password") String newPassword) {
	start();
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    ParamemeterAssert.assertDataNotNull(code);
	    LifeixUserApiUtil.getInstance().resetPassword(accountId, newPassword, code);
	    ret.setCode(DataResponse.OK);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (L99NetworkException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(e.getMessage());
	} catch (RpcException e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.CODE_USER_DUBBO, request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * buxuyao?
     * 
     * @param accountId
     * @param code
     * @return
     */
    @POST
    @Path("/reset/isPasswordCodeValidate")
    @Produces(MediaType.APPLICATION_JSON)
    public String isPasswordCodeValidate(@FormParam("account_id") Long accountId, @FormParam("code") String code) {
	start();
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    ParamemeterAssert.assertDataNotNull(code);
	    LifeixUserApiUtil.getInstance().isPasswordCodeValidate(accountId, code);
	    ret.setCode(DataResponse.OK);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (L99NetworkException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(e.getMessage());
	} catch (RpcException e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.CODE_USER_DUBBO, request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 查看用户空间
     * 
     * @param format
     * @param userId
     * @param longNO
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/space")
    @Produces(MediaType.APPLICATION_JSON)
    public String space(@QueryParam("format") @DefaultValue("json") String format, @QueryParam("user_id") Long userId,
	    @QueryParam("long_no") Long longNO) throws JSONException {
	DataResponse<UserSpaceResponse> ret = new DataResponse<UserSpaceResponse>();
	ret.setCode(DataResponse.OK);
	start();
	try {
	    UserSpaceResponse user = cbsUserService.findUserSpace(getSessionAccount(request), userId, longNO);
	    ret.setData(user);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (L99IllegalDataException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    ret.setCode(DataResponse.NO);
	    ret.setMsg("未知异常");
	    LOG.error(e.getMessage(), e);
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 搜索用户
     * 
     * @param startId
     * @param searchKey
     * @param limit
     * @return
     */
    @GET
    @Path("/find")
    @Produces(MediaType.APPLICATION_JSON)
    public String find(@QueryParam("start_id") Integer startId, @QueryParam("search_key") String searchKey,
	    @QueryParam("limit") @DefaultValue("10") Integer limit) {
	start();
	DataResponse<CbsUserListResponse> ret = new DataResponse<CbsUserListResponse>();
	ret.setCode(DataResponse.OK);
	try {
	    CbsUserListResponse users = cbsUserService.select(getSessionAccount(request), searchKey, startId, limit);
	    users.setLimit(limit);
	    List<CbsUserResponse> list = users.getUsers();
	    if (list != null && !list.isEmpty()) {
		if (list.size() < limit) {
		    users.setStartId(-1L);
		} else {
		    users.setStartId(list.get(list.size() - 1).getUser_id());
		}

	    } else {
		users.setStartId(-1L);
	    }

	    ret.setData(users);
	} catch (L99NetworkException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * processUserContestStatisticsTask
     * 
     * @param format
     * @param userId
     * @param longNO
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/continueLogin")
    @Produces(MediaType.APPLICATION_JSON)
    public String continueLogin(@QueryParam("user_id") Long userId, @QueryParam("login_token") String loginToken,
	    @QueryParam("machine_code") String machineCode, @QueryParam("client") String client) throws JSONException {
	DataResponse<CustomResponse> ret = new DataResponse<CustomResponse>();
	start();
	try {
	    CustomResponse data = cbsUserService.continueLoginGold(userId, loginToken, machineCode, client,
		    IPUtil.getIpAddr(request));
	    ret.setCode(DataResponse.OK);
	    ret.setData(data);
	} catch (Exception e) {
	    ret.setCode(DataResponse.NO);
	    ret.setMsg("连续登录奖励出错");
	    LOG.error(e.getMessage(), e);
	}
	return DataResponseFormat.response(ret);

    }

    /**
     * 用户通讯录
     */

    @POST
    @Path("/contacts")
    @Produces(MediaType.APPLICATION_JSON)
    public String contacts(@FormParam("mobile_phones") String mobilePhones) {
	start();
	DataResponse<CbsUserListResponse> ret = new DataResponse<CbsUserListResponse>();
	ret.setCode(DataResponse.OK);
	try {
	    CbsUserListResponse users = cbsUserService.contacts(getSessionAccount(request), mobilePhones);
	    ret.setData(users);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} catch (L99NetworkException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 根据用户auth获取用户信息
     * 
     * @param auth
     * @param ipaddress
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/auth")
    @Produces(MediaType.APPLICATION_JSON)
    public String auth(@QueryParam("auth") String auth, @QueryParam("token") String token,
	    @QueryParam("ipaddress") String ipaddress) throws JSONException {
	DataResponse<CustomResponse> ret = new DataResponse<CustomResponse>();
	start();
	try {
	    CustomResponse data = cbsUserService.authUser(auth, token, ipaddress);
	    ret.setCode(DataResponse.OK);
	    ret.setData(data);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (L99IllegalDataException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (L99NetworkException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	    LOG.error(e.getMessage(), e);
	}
	return DataResponseFormat.response(ret);

    }

    @POST
    @Path("/getByUserId")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUser(@FormParam("user_id") Long userId) {
	start();
	DataResponse<CbsUserResponse> ret = new DataResponse<CbsUserResponse>();
	ret.setCode(DataResponse.OK);
	try {
	    CbsUserResponse user = cbsUserService.getCbsUserByUserId(userId, false);
	    String auth = accountAuthAction.createToken(userId, false, "");

	    if (auth == null) {
		throw new L99NetworkException(BasicMsg.CODE_USERSERVER, BasicMsg.KEY_USERSERVER);
	    }
	    JSONObject json = new JSONObject(auth);
	    if (json.getInt("code") == 200) {
		JSONObject rett = new JSONObject(json.get("data").toString());
		user.setToken(rett.getString("token"));
	    } else {
		throw new L99NetworkException(json.optString("code"), json.optString("msg"));
	    }

	    ret.setData(user);

	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(e.getMessage());
	} catch (RpcException e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.CODE_USER_DUBBO, request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}

	return DataResponseFormat.response(ret);
    }

    @POST
    @Path("/replace/token")
    @Produces(MediaType.APPLICATION_JSON)
    public String replaceToken(@FormParam("user_id") Long userId, @FormParam("pre_token") String preToken,
	    @FormParam("md5_flag") @DefaultValue("true") Boolean md5Flag) {
	start();
	DataResponse<Map<String, String>> ret = new DataResponse<Map<String, String>>();
	ret.setCode(DataResponse.OK);
	try {
	    Map<String, String> map = new HashMap<String, String>();

	    if (!md5Flag) {
		preToken = Md5Encrypt.md5(PLAINTEXT + preToken, "utf-8");
	    }

	    String auth = accountAuthAction.createToken(userId, true, preToken);

	    if (auth == null) {
		throw new L99NetworkException(BasicMsg.CODE_USERSERVER, BasicMsg.KEY_USERSERVER);
	    }
	    JSONObject json = new JSONObject(auth);
	    if (json.getInt("code") == 200) {
		JSONObject rett = new JSONObject(json.get("data").toString());
		map.put("token", rett.getString("token"));
	    } else {
		throw new L99NetworkException(json.optString("code"), json.optString("msg"));
	    }

	    ret.setData(map);
	} catch (L99NetworkException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}

	return DataResponseFormat.response(ret);
    }

    /**
     * 获取用户龙币余额
     * 
     * @return
     */
    @GET
    @Path("/money/view")
    @Produces(MediaType.APPLICATION_JSON)
    public String moneyView() {
	start();
	DataResponse<CustomResponse> ret = new DataResponse<CustomResponse>();
	try {
	    CustomResponse data = cbsUserService.viewUserMoney(getSessionAccount(request));
	    ret.setCode(DataResponse.OK);
	    ret.setData(data);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (L99IllegalDataException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}

	return DataResponseFormat.response(ret);
    }
}
