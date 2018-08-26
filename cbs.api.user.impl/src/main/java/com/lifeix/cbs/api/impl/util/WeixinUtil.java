package com.lifeix.cbs.api.impl.util;

import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lifeix.framwork.util.JsonUtils;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.context.ContextLoader;
import org.xml.sax.InputSource;

import com.lifeix.cbs.api.bean.gold.GoldResponse;
import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.cbs.api.bean.user.WeixinUserDataResponse;
import com.lifeix.cbs.api.bean.user.WeixinUserPushResponse;
import com.lifeix.cbs.api.common.user.LifeixUserApiUtil;
import com.lifeix.cbs.api.common.util.CbsUserConstants.UserWeixin;
import com.lifeix.cbs.api.common.util.CommerceMath;
import com.lifeix.cbs.api.service.money.MoneyService;
import com.lifeix.cbs.api.service.user.CbsUserService;
import com.lifeix.common.utils.StringUtil;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;
import com.lifeix.user.beans.CustomResponse;
import com.lifeix.user.beans.oauth.LifeixOAuthConsumer;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;

public class WeixinUtil {
    private static Client client;

    private static class SingletonHolder {
	private static final WeixinUtil INSTANCE = new WeixinUtil();
    }

    public static WeixinUtil getInstance() {
	return SingletonHolder.INSTANCE;
    }

    private WeixinUtil() {
	client = Client.create();
	client.setConnectTimeout(new Integer(3000));
	client.setReadTimeout(new Integer(3000));
    }

    /**
     * 微信发送请求
     * 
     * @param start_time
     * @param end_time
     * @return
     * @throws JSONException
     */
    public static String weixinReq(String url) throws JSONException {
	Form queryParam = new Form();
	WebResource resource = client.resource(url);
	String jsonStr = resource.queryParams(queryParam).get(String.class);
	System.out.println(jsonStr);
	return jsonStr;
    }

    public CustomResponse weixinCallBack(String xml, String code, String state) throws JSONException, DocumentException,
	    NumberFormatException, L99IllegalParamsException, L99NetworkException, L99IllegalDataException {
	String appId = UserWeixin.APPID;
	String secret = UserWeixin.SECRET;
	String accessToken = "";
	String refreshToken = "";
	String uid = "";
	Integer expiresIn = null;

	// 解析微信回调xml
	Map<String, String> map = getXmlMsg(xml, "ToUserName", "ToUserName", "MsgId");
	String openID = map.get("ToUserName");
	String fromUserName = map.get("FromUserName");

	// 第二步：通过code换取网页授权access_token 第一步在微信内嵌界面url调用
	String url2 = UserWeixin.ACCESS_TOKEN + "appid=" + appId + "&secret=" + secret + "&code=" + code
	        + "&grant_type=authorization_code";
	String json2 = weixinReq(url2);

	map = getJsonMsg(json2, "access_token", "refresh_token", "expires_in", "openid");
	refreshToken = map.get("refresh_token");
	openID = map.get("openid");
	expiresIn = Integer.parseInt(map.get("expires_in"));
	// 第三步：刷新access_token（如果需要）
	String url3 = UserWeixin.REFRESH_TOKEN + "appid=" + appId + "&grant_type=refresh_token&refresh_token="
	        + refreshToken;
	String json3 = weixinReq(url3);

	map = getJsonMsg(json3, "access_token");
	accessToken = map.get("access_token");

	if (StringUtils.isEmpty(accessToken)) {
	    map = getJsonMsg(json3, "errcode");
	}
	// 第四步：拉取用户信息(需scope为 snsapi_userinfo)
	String url4 = UserWeixin.USERINFO + "access_token=" + accessToken + "&openid=OPENID&lang=zh_CN";
	String json4 = weixinReq(url4);

	map = getJsonMsg(json4, "nickname", "sex", "city", "headimgurl", "unionid");
	// 保存用户TODO
	String sex = map.get("sex");
	String str = "WEIXIN_LF";
	uid = map.get("unionid");
	String strEncode = getEncode(uid, map, str);
	CbsUserService cbsUserService = (CbsUserService) ContextLoader.getCurrentWebApplicationContext().getBean(
	        "cbsUserService");

	CbsUserResponse user = cbsUserService.loginByThird("WEIXIN_LF", uid, map.get("nickname"), "", Integer.parseInt(sex),
	        "", "", "", "", strEncode, "");
	LifeixOAuthConsumer consumer = LifeixUserApiUtil.getInstance().findWxAccessToken(user.getUser_id());
	if (consumer == null) {
	    LifeixUserApiUtil.getInstance().refreshWxOauth(user.getUser_id(), uid, accessToken, refreshToken, openID,
		    expiresIn);
	}

	Long userId = user.getUser_id();

	MoneyService moneyService = (MoneyService) ContextLoader.getCurrentWebApplicationContext().getBean("moneyService");

	// 用户龙币
	GoldResponse money = moneyService.viewUserMoney(userId, "");

	CustomResponse response = new CustomResponse();
	response.put("user_id", userId);
	response.put("money", CommerceMath.sub(money.getBalance(), money.getFrozen()));
	return response;
    }

    private String getEncode(String uid, Map<String, String> map, String str) {
	if (!StringUtils.isEmpty(uid)) {
	    str = str + uid;
	}

	if (!StringUtils.isEmpty(map.get("nickname"))) {
	    str = str + map.get("nickname");
	}
	str = str + "g442VmSL";
	String strEncode = StringUtil.MD5Encode(str);
	return strEncode;
    }

    public String notifyWeixin(Long userId) throws L99NetworkException, JSONException {
	String openId = "touser14343";
	// 查询openId
	LifeixOAuthConsumer consumer = LifeixUserApiUtil.getInstance().findWxAccessToken(userId);
	if (consumer == null) {

	}
	openId = consumer.getTokenSecret();
	// https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN
	// http请求方式: POST
	WeixinUserPushResponse r = new WeixinUserPushResponse();
	r.setTemplate_id("tempid111");// TODO 待定
	r.setTouser(openId);
	r.setUrl("http://abidu.com"); // TODO 待定
	WeixinUserDataResponse d = new WeixinUserDataResponse();
	d.setFirst(d.new Data("v1", "c1"));
	d.setKeynote1(d.new Data("v2", "c2"));
	d.setRemark(d.new Data("r2", "r2"));
	r.setData(d);
	return JsonUtils.toJsonString(r);
    }

    public static Map<String, String> getJsonMsg(String json, String... parms) throws JSONException {
	Map<String, String> map = new HashMap<String, String>();
	// json =
	// "{\"access_token\":\"ACCESS_TOKEN\",  \"expires_in\":7200,  \"refresh_token\":\"REFRESH_TOKEN\", \"openid\":\"OPENID\",  \"scope\":\"SCOPE\"}";
	JSONObject obj = new JSONObject(json);
	for (String s : parms) {
	    System.out.println(obj.getString(s));
	    map.put(s, obj.getString(s));

	}

	return map;
    }

    public static Map<String, String> getXmlMsg(String xml, String... parms) throws JSONException, DocumentException {
	xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml>" + "<ToUserName>SDFFDF</ToUserName>"
	        + "<FromUserName>EERE</FromUserName>" + "<CreateTime>1348831860</CreateTime>" + "<MsgType>TEXT</MsgType>"
	        + "<Content>CFSDFDS</Content>" + "<MsgId>1234567890123456</MsgId>" + "</xml>";
	SAXReader reader = new SAXReader();
	StringReader sr = new StringReader(xml);
	InputSource in = new InputSource(sr);
	Document doc = reader.read(in);
	Element root = doc.getRootElement();
	Map<String, String> map = new HashMap<String, String>();
	for (String p : parms) {
	    String ToUserName = root.element(p).getText();
	    map.put(p, ToUserName);
	    System.out.println(ToUserName);
	}

	return map;

    }

    /**
     * 在微信回调开发者后开发者返回的xml串
     * 
     * 被动回复用户消息
     * 
     * 当用户发送消息给公众号时（或某些特定的用户操作引发的事件推送时），会产生一个POST请求，开发者可以在响应包（Get）中返回特定XML结构
     * ，来对该消息进行响应
     * （现支持回复文本、图片、图文、语音、视频、音乐）。严格来说，发送被动响应消息其实并不是一种接口，而是对微信服务器发过来消息的一次回复
     * 
     * @param start_time
     * @param end_time
     * @return
     * @throws JSONException
     */
    public String setToUserMsg(String openID, String FromUserName) throws JSONException {
	String xml = "";
	String MsgType = "text";
	Date CreateTime = new Date();
	String Content = "dddd";

	xml = "<xml>" + "<ToUserName>" + openID + "</ToUserName>" + "<FromUserName>" + FromUserName + "</FromUserName>"
	        + "<CreateTime>" + CreateTime.getTime() + "</CreateTime>" + "<MsgType>" + MsgType + "</MsgType>"
	        + "<Content>" + Content + "</Content>" + "</xml>";

	return xml;

    }

    public static void main(String[] args) {
	try {
	    SingletonHolder.INSTANCE
		    .weixinReq("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd78f00d036377692&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect");
	    getJsonMsg("", "access_token");
	    getXmlMsg("", "FromUserName");
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (DocumentException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

}
