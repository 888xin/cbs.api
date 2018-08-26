package com.lifeix.cbs.contest.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lifeix.cbs.api.common.util.CbsUserConstants.UserWeixin;
import com.lifeix.cbs.api.common.util.MyX509TrustManager;
import com.lifeix.cbs.api.common.util.WxToken;

public class WeixinNotifyUtil {
    protected static final Logger LOG = LoggerFactory.getLogger(WeixinNotifyUtil.class);

    private static WxToken token = null;// 微信token

    private static final int INVALID_TOKEN_CODE = 40001;// 失效token错误码
    private static final int OK = 0;// 正确码
	private static String cacheToken = null;

    private String cbsWebUri;

    public static String KAFKA_VER = "1.0-notify";

    private static class SingletonHolder {
	private static final WeixinNotifyUtil INSTANCE = new WeixinNotifyUtil();
    }

    public static WeixinNotifyUtil getInstance() {
	return SingletonHolder.INSTANCE;
    }

    private WeixinNotifyUtil() {
    }

    public void setCbsWebUri(String cbsWebUri) {
	this.cbsWebUri = cbsWebUri;
    }

    /**
     * 发送http请求
     * 
     * @param requestUrl
     * @param requestMethod
     * @param outputStr
     * @return
     */
    public String httpRequest(String requestUrl, String requestMethod, String outputStr) {
	StringBuffer buffer = new StringBuffer();
	try {
	    // 创建SSLContext对象，并使用我们指定的信任管理器初始化
	    TrustManager[] tm = { new MyX509TrustManager() };
	    SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
	    sslContext.init(null, tm, new java.security.SecureRandom());
	    // 从上述SSLContext对象中得到SSLSocketFactory对象
	    SSLSocketFactory ssf = sslContext.getSocketFactory();

	    URL url = new URL(requestUrl);
	    HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
	    httpUrlConn.setSSLSocketFactory(ssf);

	    httpUrlConn.setDoOutput(true);
	    httpUrlConn.setDoInput(true);
	    httpUrlConn.setUseCaches(false);
	    // 设置请求方式（GET/POST）
	    httpUrlConn.setRequestMethod(requestMethod);

	    if ("GET".equalsIgnoreCase(requestMethod))
		httpUrlConn.connect();

	    // 当有数据需要提交时
	    if (null != outputStr) {
		OutputStream outputStream = httpUrlConn.getOutputStream();
		// 注意编码格式，防止中文乱码
		outputStream.write(outputStr.getBytes("UTF-8"));
		outputStream.close();
	    }

	    // 将返回的输入流转换成字符串
	    InputStream inputStream = httpUrlConn.getInputStream();
	    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
	    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

	    String str = null;
	    while ((str = bufferedReader.readLine()) != null) {
		buffer.append(str);
	    }
	    bufferedReader.close();
	    inputStreamReader.close();
	    // 释放资源
	    inputStream.close();
	    inputStream = null;
	    httpUrlConn.disconnect();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return buffer.toString();
    }

    /**
     * 获取微信访问凭证
     * 
     * @return
     */
    public WxToken getWxToken() {
	if (token == null) {
	    token = getToken();
	} else {
	    if (token.getCurTimeMilles() + token.getExpiresIn() - 200 > System.currentTimeMillis()) {
		token = getToken();
	    }
	}
	return token;
    }

    /**
     * 调用微信接口获取接口访问凭证
     * 
     * @return
     */
    private WxToken getToken() {

	String requestUrl = UserWeixin.TOKEN_URL.replace("APPID", UserWeixin.APPID).replace("APPSECRET", UserWeixin.SECRET);
	// 发起GET请求获取凭证
	String result = httpRequest(requestUrl, "GET", null);

	if (null != result) {
	    try {
		JSONObject obj = new JSONObject(result);
		token = new WxToken();
		token.setAccessToken(obj.getString("access_token"));
		token.setExpiresIn(obj.getInt("expires_in"));
		token.setCurTimeMilles(System.currentTimeMillis());
	    } catch (JSONException e) {
		token = null;
		e.printStackTrace();
	    }
	}
	return token;
    }

    /**
     * 获取消息模板
     * 
     * @param templateId
     * @return
     */
    public String getTemplate(String templateId, int type) {
	if (WeixinNotifyTemplate.ORDER_OVER_TEMPID.equals(templateId) && type == 10) {// 押押
		                                                                      // 订单完成提醒
	    return WeixinNotifyTemplate.yyOrderOverTemp;
	} else if (WeixinNotifyTemplate.ORDER_OVER_TEMPID.equals(templateId) && (type == 0 || type == 1)) {// 足球、篮球
	    // 订单完成提醒
	    return WeixinNotifyTemplate.orderOverTemp;
	} else if (WeixinNotifyTemplate.ORDER_PAY_TEMPID.equals(templateId) && (type == 0 || type == 1)) { // 足球、篮球
	    // 订单支付提醒
	    return WeixinNotifyTemplate.orderPayTemp;
	} else if (WeixinNotifyTemplate.ORDER_PAY_TEMPID.equals(templateId) && type == 10) {// 押押
	    // 订单支付提醒
	    return WeixinNotifyTemplate.yyOrderPayTemp;
	} else {
	    return "";
	}
    }

    /**
     * 发送消息模板
     * 
     * @param accessToken
     * @param message
     * @return
     */
	@Deprecated
    public String sendWxTempMess(String accessToken, String message) {
	// 调用微信接口发送消息模板
	String result = sendTempMess(accessToken, message);
	JSONObject obj;
	try {
	    obj = new JSONObject(result);
	    // invalid credential, access_token is invalid or not latest hint
	    if (INVALID_TOKEN_CODE == obj.getInt("errcode")) {
		getWxToken();
		result = sendTempMess(token.getAccessToken(), message);
	    }
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return result;
    }


	public String sendWxTempMess(String message) {
		// 调用微信接口发送消息模板
		if (cacheToken == null){
			cacheToken = getToken().getAccessToken();
		}
		String result = sendTempMess(cacheToken, message);
		JSONObject obj;
		try {
			obj = new JSONObject(result);
			if (OK != obj.getInt("errcode")) {
				cacheToken = getToken().getAccessToken();
				result = sendTempMess(cacheToken, message);
			}
		} catch (JSONException e) {
			LOG.error(e.getMessage(), e);
		}
		return result;
	}

    /**
     * 调用微信接口发送消息模板
     * 
     * @param accessToken
     * @param message
     * @return
     */
	private String sendTempMess(String accessToken, String message) {
		// 获取请求地址
		String reqUrl = UserWeixin.SEND_TEMP_MESS.replace("ACCESS_TOKEN", accessToken);
		// 发送请求
		String result = httpRequest(reqUrl, "POST", message);
		//LOG.info("weixin03:" + accessToken);
		return result;
	}

    /**
     * 根据模板ID发送对应消息模板
     * 
     * @param params
     * @param templateId
     * @return
     */
    public String sendWxTempMessById(JSONObject params, String templateId, int type) {

	String sendResult = "";
	String message = "";
	try {

	    String template = getTemplate(templateId, type);
	    if (StringUtils.isEmpty(cbsWebUri)) {
		cbsWebUri = "http://www.caibisai.com/";
	    }
	    if (WeixinNotifyTemplate.ORDER_OVER_TEMPID.equals(templateId) && (type == 0 || type == 1)) { // 足球篮球
		message = String.format(template, params.getString("back_val"), params.getString("create_time"),
		        params.getString("start_time"), params.getString("home_name"), params.getString("away_name"),
		        params.getString("play_name"), params.getString("bet_val"), params.getString("bId"), templateId,
		        params.getString("open_Id"), "http://a.app.qq.com/o/simple.jsp?pkgname=com.l99.lotto");
	    } else if (WeixinNotifyTemplate.ORDER_OVER_TEMPID.equals(templateId) && type == 10) { // 押押
		message = String.format(template, params.getString("back_val"), params.getString("create_time"),
		        params.getString("start_time"), params.getString("title"), params.getString("name"),
		        params.getString("bet_val"), params.getString("bId"), templateId, params.getString("open_Id"),
		        "http://a.app.qq.com/o/simple.jsp?pkgname=com.l99.lotto");
	    } else if (WeixinNotifyTemplate.ORDER_PAY_TEMPID.equals(templateId) && type == 10) {
		String requestUrl;
		if (params.has("circle_id")) {
		    requestUrl = cbsWebUri + "yy/yy_share?id=" + params.getLong("circle_id");
		} else {
		    requestUrl = "http://a.app.qq.com/o/simple.jsp?pkgname=com.l99.lotto";
		}

		message = String.format(template, templateId, params.getString("open_Id"), requestUrl,
		        params.getString("start_time"), params.getString("title"), params.getString("bId"),
		        params.getString("bet_val"), params.getString("name"), params.getDouble("bet_odds"));
	    } else if (WeixinNotifyTemplate.ORDER_PAY_TEMPID.equals(templateId) && (type == 0 || type == 1)) {
		String requestUrl;
		if (params.has("circle_id")) {
		    requestUrl = cbsWebUri + "circle/my_share?id=" + params.getLong("circle_id");
		} else if (params.has("user_id")) {
		    requestUrl = cbsWebUri + "user/user_info?userId=" + params.getString("user_id");
		} else {
		    requestUrl = "http://a.app.qq.com/o/simple.jsp?pkgname=com.l99.lotto";
		}
		message = String.format(template, templateId, params.getString("open_Id"), requestUrl,
		        params.getString("start_time"), params.getString("contest_type_name"),
		        params.getString("home_name"), params.getString("away_name"), params.getString("play_name"),
		        params.getString("bId"), params.getString("bet_val"), params.getString("support_name"),
		        params.getDouble("bet_odds"));
	    }
		//LOG.info("weixin02:"+params.getString("circle_id"));
	    sendResult = sendWxTempMess(message);
	    LOG.info(String.format("weixin message result: %s", sendResult));
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return sendResult;
    }

    public static void main(String[] args) {

	// String openId = "o3lVhwlGd5Np9UUTO2g6owysUkaY";

	// String test =
	// "{\"remark\":\"下单成功\",\"bet_odds\":1.91,\"away_name\":\"森林狼\",\"play_name\":\"让球胜平负\",\"back\":11.37,\"start_time\":\"Sat Apr 02 09:00:00 +0800 2016\",\"home_name\":\"爵士\",\"bet_val\":\"5龙筹2.0龙币\"}";
	// try {
	// JSONObject params = new JSONObject(test);
	// SimpleDateFormat formatDateTime = new
	// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// SimpleDateFormat formatDate = new SimpleDateFormat("yyyy年MM月dd日");
	//
	// params.put("create_time", formatDateTime.format(new Date()));
	// params.put("start_time", formatDate.format(new Date()));
	// params.put("bId", "D111111111");
	// params.put("open_Id", openId);
	// params.put("access_token", "12222222222222222");
	// params.put("user_id", "10000266");
	// params.put("support_name", "主胜");
	// params.put("contest_type", "篮球");
	// String sendResult =
	// WeixinNotifyUtil.getInstance().sendWxTempMessById(params,
	// ORDER_PAY_TEMPID, 0);
	// System.out.println(sendResult);
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }

	// String test =
	// "{\"start_time\":\"Fri Apr 01 00:00:00 +0800 2016\",\"name\":\"不会\",\"bet_val\":\"5龙筹20.0龙币\",\"remark\":\"下单成功\",\"back\":20,\"bet_odds\":1.91,\"title\":\"US weekly 等媒体于美国时间3月31日爆料：邓文迪与普京拍拖，猜截止到4月5日，这个新闻会得到证实吗?\"}";
	// try {
	// JSONObject params = new JSONObject(test);
	// SimpleDateFormat formatDateTime = new
	// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// SimpleDateFormat formatDate = new SimpleDateFormat("yyyy年MM月dd日");
	//
	// params.put("create_time", formatDateTime.format(new Date()));
	// params.put("start_time", formatDate.format(new Date()));
	// params.put("bId", "D111111111");
	// params.put("open_Id", openId);
	// params.put("access_token", "12222222222222222");
	// params.put("user_id", "10000266");
	// params.put("remark", "下单成功");
	// String sendResult =
	// WeixinNotifyUtil.getInstance().sendWxTempMessById(params,
	// WeixinNotifyTemplate.ORDER_PAY_TEMPID, 10);
	// System.out.println(sendResult);
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }

    }
}
