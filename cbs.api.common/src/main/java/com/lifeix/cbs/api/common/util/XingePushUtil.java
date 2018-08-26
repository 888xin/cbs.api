package com.lifeix.cbs.api.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tencent.xinge.ClickAction;
import com.tencent.xinge.Message;
import com.tencent.xinge.MessageIOS;
import com.tencent.xinge.Style;
import com.tencent.xinge.XingeApp;

/**
 * Created by lhx on 15-10-21 下午3:53
 * 
 * @Description
 */
public class XingePushUtil {

    protected static Logger LOG = LoggerFactory.getLogger(XingePushUtil.class);

    private static final Long ANDROID_ACCESS_ID = 2100154244L;
    private static final String ANDROID_SECRET_KEY = "6a9d9cb0e3a7baeb8a3a645b00c8f5ad";

    private static final Long IOS_ACCESS_ID = 2200154245L;
    private static final String IOS_SECRET_KEY = "11bee70e1e90d5998b92e6e72fdfee1e";

    private XingeApp androidXinge;

    private XingeApp iosXinge;

    static class SingletonHolder {
	private static final XingePushUtil INSTANCE = new XingePushUtil();
    }

    public static XingePushUtil getInstance() {
	return SingletonHolder.INSTANCE;
    }

    private XingePushUtil() {
	androidXinge = new XingeApp(ANDROID_ACCESS_ID, ANDROID_SECRET_KEY);
	iosXinge = new XingeApp(IOS_ACCESS_ID, IOS_SECRET_KEY);
    }

    // 下发所有设备
    public JSONObject pushAllDevice(String activity, String content, Map<String, Object> customContent) {
	JSONObject androidRet = null;
	try {
	    // Android 信鸽 Push
	    Message pushMessage = new Message();
	    pushMessage.setTitle("大赢家");
	    pushMessage.setContent(content);
	    pushMessage.setCustom(customContent);
	    // TYPE_NOTIFICATION:通知； TYPE_MESSAGE:透传消息。必填 注意：TYPE_MESSAGE类型消息
	    // 默认在终端是不展示的
	    pushMessage.setType(Message.TYPE_MESSAGE);
	    Style style = new Style(0);
	    pushMessage.setStyle(style);
	    if (activity != null) {
		ClickAction action = new ClickAction();
		action.setActivity(activity);
		pushMessage.setAction(action);
	    }
	    androidRet = androidXinge.pushAllDevice(XingeApp.DEVICE_ALL, pushMessage);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
	return androidRet;
    }

    // 单个设备下发透传消息
    public JSONObject PushSingleDeviceMessage(Message message, String token) {
	message.setType(Message.TYPE_MESSAGE);
	return androidXinge.pushSingleDevice(token, message);
    }

    // 发给单个用户（根据账号ID）
    public JSONObject pushSingleAccount(long toAccountId, String activity, String content, Map<String, Object> customContent) {
	JSONObject androidRet = null;
	try {
	    // Android 信鸽 Push
	    Message pushMessage = new Message();
	    pushMessage.setTitle("大赢家");
	    pushMessage.setContent(content);
	    pushMessage.setCustom(customContent);
	    // TYPE_NOTIFICATION:通知； TYPE_MESSAGE:透传消息。必填 注意：TYPE_MESSAGE类型消息
	    // 默认在终端是不展示的
	    pushMessage.setType(Message.TYPE_MESSAGE);
	    Style style = new Style(0);
	    pushMessage.setStyle(style);
	    if (activity != null) {
		ClickAction action = new ClickAction();
		action.setActivity(activity);
		pushMessage.setAction(action);
	    }
	    androidRet = androidXinge.pushSingleAccount(0, String.valueOf(toAccountId), pushMessage);

	    // ios 信鸽push
	    MessageIOS iosMessage = new MessageIOS();
	    iosMessage.setAlert(content);
	    iosMessage.setBadge(1);
	    iosMessage.setCustom(customContent);
	    JSONObject iosRet = iosXinge.pushSingleAccount(0, String.valueOf(toAccountId), iosMessage, XingeApp.IOSENV_DEV);
	    JSONObject iosRet2 = iosXinge
		    .pushSingleAccount(0, String.valueOf(toAccountId), iosMessage, XingeApp.IOSENV_PROD);
	    System.out.println(toAccountId + "--" + iosRet + "--" + iosRet2 + "--" + androidRet);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
	return androidRet;
    }

    /**
     * 发送多个账号
     * 
     * @return
     */
    public JSONObject pushAccounts(List<String> toAccountIds, String activity, String content,
	    Map<String, Object> customContent) {
	JSONObject multipushResult = null;
	try {
	    // Android 信鸽 Push
	    Message pushMessage = new Message();
	    pushMessage.setTitle("大赢家");
	    pushMessage.setContent(content);
	    pushMessage.setCustom(customContent);
	    // TYPE_NOTIFICATION:通知； TYPE_MESSAGE:透传消息。必填 注意：TYPE_MESSAGE类型消息
	    // 默认在终端是不展示的
	    pushMessage.setType(Message.TYPE_MESSAGE);
	    Style style = new Style(0);
	    pushMessage.setStyle(style);
	    if (activity != null) {
		ClickAction action = new ClickAction();
		action.setActivity(activity);
		pushMessage.setAction(action);
	    }
	    JSONObject androidRet = androidXinge.createMultipush(pushMessage);
	    if (androidRet != null && androidRet.optInt("ret_code") == 0) {
		JSONObject result = androidRet.optJSONObject("result");
		int pushId = result.optInt("push_id");
		multipushResult = androidXinge.pushAccountListMultiple(pushId, toAccountIds);
		if (multipushResult != null && multipushResult.optInt("ret_code") != 0) {
		    LOG.error("Android pushAccountListMultiple error:" + multipushResult.toString());
		}
	    } else {
		LOG.error("android createMultipush return:" + androidRet);
	    }
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
	return multipushResult;
    }

    public static void main(String[] args) {
	// JSONObject ret =
	// XingePushUtil.getInstance().pushAllDevice(大赢家","测试文本");
	// System.out.println(ret);

	// String activity = "";
	// String content = "测试";
	// Map<String, Object> customContent = new HashMap<String, Object>();
	// customContent.put("user_id", 10000023L);
	// customContent.put("message_type", 2);
	// customContent.put("push_type", 1);
	// JSONObject ret =
	// XingePushUtil.getInstance().pushSingleAccount(10000023L, activity,
	// content, customContent);
	// System.out.println(ret);

	// customContent.put("msg_num", 3);
	// customContent.put("push_type", PushType.SYSTEM_MSG);
	// JSONObject ret = XingePushUtil.getInstance().pushAllDevice(activity,
	// content, customContent);

	// Message message = new Message();
	// message.setTitle("大赢家");
	// message.setContent("测试文本");
	// Map<String, Object> customContent = new HashMap<String, Object>();
	// customContent.put("user_id",10000023L);
	// customContent.put("message_type",2);
	// customContent.put("push_type",1);
	// customContent.put("link","chuangshang://dashboard?dashboard_id=123456");
	// message.setCustom(customContent);
	// JSONObject ret =
	// XingePushUtil.getInstance().PushSingleDeviceMessage(message,"261a01353930335d49baa2d443aebe42128c7dd1");
	// System.out.println(ret);
	// test();

	String activity = "";
	String content = "测试";
	Map<String, Object> customContent = new HashMap<String, Object>();
//	customContent.put("scheme_link", "cbs://caicai?contestId=9&contestType=0");
//	customContent.put("scheme_link", "cbs://caicai?contestId=9&contestType=1");
//	customContent.put("scheme_link", "cbs://news?contentId=1");
//	customContent.put("scheme_link", "cbs://recharge/");
//	customContent.put("scheme_link", "cbs://yaya?contestId=1");
	 customContent.put("scheme_link", "cbs://bigwinner?type=0");
	JSONObject ret = XingePushUtil.getInstance().pushSingleAccount(10000266L, activity, content, customContent);
	System.out.println(ret);
    }

    public static void test() {

	String test = "1.跳转资讯单篇,cbs://news?contentId=12384,2.跳转足球比赛,cbs://caicai?contestId=153800&contestType=0,2.跳转篮球比赛,cbs://caicai?contestId=17485&contestType=1,3.跳转押押某场比赛,cbs://yaya?contestId=7013,4.跳转公告,cbs://placard?templetId=123456,5.跳转吐槽,cbs://tucao?circleId=123456&userId=8217170,6.跳转下注理由,cbs://reason?contentId=123456&userId=8217170,7.跳转网页,http://www.baidu.com,8.跳转个人空间,cbs://userspace?userId=8217170,9.跳转到关注列表,cbs://following?userId=1238383,10.跳转到粉丝列表,cbs://fans?userId=1243543,11.跳转到消息页面,cbs://message/,12.跳转到社区,cbs://community/,13.跳转到猜友圈,cbs://circle/,14.跳转到排行榜,cbs://rank/,15.跳转到生肖抢红包,cbs://luckymoney/,16.跳转到QQ群,cbs://qqgroup/,17.跳转到活动页面,cbs://activity/,18.跳转到商城页面,cbs://mall/,19.跳转到商城全部专题,cbs://mallcategory/,20.跳转到专题详情,cbs://mallcategorydetail?categoryId=12355&categoryName=哈哈哈哈哈&categoryImage=abc/jkldggg.jpg,21.跳转到单个商品详情页面,cbs://gooddetail?goodId=435221,22.跳转到订单详情,cbs://orderlist/,23.跳转h5（无需鉴权）,cbs://h5?url=www.baidu.com,24.跳转龙筹列表(valid:0跳转过期龙筹1跳转有效龙筹),cbs://longchou?valid=0,25.跳转龙币列表,cbs://longbi/,26.跳转充值页面,cbs://recharge/,28.跳转查找用户页面,cbs://search/,29.跳转成就页面,cbs://achievement/,30.跳转猜友圈历史消息,cbs://circlehistorymsg/,31.跳转猜友圈最新消息,cbs://circlenewmsg/,32.跳转到我的押押列表页面,cbs://myyaya/,33.跳转到战绩列表页面,cbs://zhanji/,34.跳到大赢家页面下的押押、足球、篮球(type押押10足球0篮球1),cbs://bigwinner?type=1";

	String[] argsStrings = test.split(",");

	List<Map<String, String>> list = new ArrayList<Map<String, String>>();

	Map<String, String> map;

	System.out.println(argsStrings.length);
	for (int i = 0; i <= argsStrings.length - 1;) {

	    map = new HashMap<String, String>();
	    map.put("content", argsStrings[i]);
	    ++i;
	    map.put("url", argsStrings[i]);
	    ++i;
	    list.add(map);
	}
	sendMessage(list);
    }

    public static void sendMessage(List<Map<String, String>> list) {

	Iterator<Map<String, String>> iterator = list.iterator();
	Map<String, String> map;
	while (iterator.hasNext()) {
	    map = iterator.next();
	    String activity = "";
	    String content = map.get("content");
	    Map<String, Object> customContent = new HashMap<String, Object>();
	    customContent.put("scheme_link", map.get("url"));
	    JSONObject ret = XingePushUtil.getInstance().pushSingleAccount(10000266L, activity, content, customContent);
	    System.out.println(ret);
	}

    }

}
