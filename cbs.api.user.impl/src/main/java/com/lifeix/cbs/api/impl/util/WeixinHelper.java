package com.lifeix.cbs.api.impl.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.lifeix.cbs.api.bean.gold.GoldResponse;
import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.user.LifeixUserApiUtil;
import com.lifeix.cbs.api.common.util.CbsUserConstants.UserWeixin;
import com.lifeix.cbs.api.common.util.CommerceMath;
import com.lifeix.cbs.api.service.money.MoneyService;
import com.lifeix.cbs.api.service.user.CbsUserService;
import com.lifeix.cbs.api.service.user.CbsUserWxService;
import com.lifeix.common.utils.StringUtil;
import com.lifeix.common.utils.oauth.OAuthProviderType;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;
import com.lifeix.user.beans.CustomResponse;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;

/**
 * 微信公众号服务类
 * 
 * @author lifeix
 * 
 */
public class WeixinHelper {

    private Client client;

    private CbsUserService cbsUserService;

    private MoneyService moneyService;

    private CbsUserWxService cbsUserWxService;

    private static class SingletonHolder {
	private static final WeixinHelper INSTANCE = new WeixinHelper();
    }

    public static WeixinHelper getInstance() {
	return SingletonHolder.INSTANCE;
    }

    private WeixinHelper() {
	client = Client.create();
	client.setConnectTimeout(new Integer(3000));
	client.setReadTimeout(new Integer(3000));

	// 初始化bean
	WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
	cbsUserService = (CbsUserService) context.getBean("cbsUserService");
	moneyService = (MoneyService) context.getBean("moneyService");
	cbsUserWxService = (CbsUserWxService) context.getBean("cbsUserWxService");
    }

    /**
     * 微信登录或注册
     * 
     * @param code
     * @param state
     * @return
     * @throws L99IllegalParamsException
     * @throws L99NetworkException
     * @throws L99IllegalDataException
     * @throws JSONException
     */
    public CustomResponse weixinLogin(String code, String state, String ipaddress) throws L99IllegalParamsException,
	    L99NetworkException, L99IllegalDataException, JSONException {

	// 参数map
	Map<String, String> map = new HashMap<String, String>();

	// 第一步：通过code换取网页授权access_token 和 用户唯一Id
	String url = String.format("%sappid=%s&secret=%s&code=%s&grant_type=authorization_code", UserWeixin.ACCESS_TOKEN,
	        UserWeixin.APPID, UserWeixin.SECRET, code);
	String json = weixinReq(url);

	map = getJsonMsg(json, "access_token", "refresh_token", "expires_in", "openid");
	String accessToken = map.get("access_token");
	String refreshToken = map.get("refresh_token");
	String openid = map.get("openid");
	Integer expiresIn = Integer.parseInt(map.get("expires_in"));

	// 第二步：拉取用户信息和unionid
	url = String.format("%saccess_token=%s&openid=%s&lang=zh_CN", UserWeixin.USERINFO, accessToken, openid);
	json = weixinReq(url);

	map = getJsonMsg(json, "nickname", "sex", "city", "headimgurl", "unionid");
	String sex = map.get("sex");
	String unionid = map.get("unionid");
	String nickname = map.get("nickname");
	String strEncode = getEncode(unionid, nickname);

	// 用户登录或者注册
	CbsUserResponse user = cbsUserService.loginByThird(OAuthProviderType.WEIXIN_LF.name(), unionid, nickname, "",
	        Integer.parseInt(sex), null, "key:roi_wx", null, null, strEncode, ipaddress);

	// 存储accessToken和refreshToken
	LifeixUserApiUtil.getInstance().refreshWxOauth(user.getUser_id(), unionid, accessToken, refreshToken, openid,
	        expiresIn);

	// 用户龙币
	GoldResponse money = moneyService.viewUserMoney(user.getUser_id(), ipaddress);

	CustomResponse response = new CustomResponse();
	response.put("user_id", user.getUser_id());
	response.put("user_head", user.getHead());
	response.put("money", CommerceMath.sub(money.getBalance(), money.getFrozen()));
	response.put("token", user.getToken());

	// 存儲用戶openid
	cbsUserWxService.saveUserWxInfo(user.getUser_id(), openid, UserWeixin.APPID, "H5");
	return response;
    }

    /**
     * 微信发送请求
     * 
     * @param start_time
     * @param end_time
     * @return
     * @throws JSONException
     */
    public String weixinReq(String url) throws JSONException {
	Form queryParam = new Form();
	WebResource resource = client.resource(url);
	String jsonStr = resource.queryParams(queryParam).get(String.class);
	return jsonStr;
    }

    /**
     * 解析返回值的对应的map
     * 
     * @param json
     * @param parms
     * @return
     * @throws JSONException
     * @throws L99IllegalDataException
     */
    public static Map<String, String> getJsonMsg(String json, String... parms) throws JSONException, L99IllegalDataException {
	Map<String, String> map = new HashMap<String, String>();
	JSONObject obj = new JSONObject(json);
	if (obj.has("errcode")) { // 出现异常
	    throw new L99IllegalDataException(BasicMsg.CODE_WX_ERROR, obj.getString("errmsg"));
	}
	for (String s : parms) {
	    map.put(s, obj.getString(s));
	}
	return map;
    }

    /**
     * 生成对称秘钥
     * 
     * @param unionid
     * @param username
     * @return
     */
    private String getEncode(String unionid, String username) {
	String str = "WEIXIN_LF";
	if (!StringUtils.isEmpty(unionid)) {
	    str = str + unionid;
	}

	if (!StringUtils.isEmpty(username)) {
	    str = str + username;
	}
	str = str + "g442VmSL";
	String strEncode = StringUtil.MD5Encode(str);
	return strEncode;
    }

}
