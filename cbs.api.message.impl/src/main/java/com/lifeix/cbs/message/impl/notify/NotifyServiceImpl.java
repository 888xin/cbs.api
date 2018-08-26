package com.lifeix.cbs.message.impl.notify;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.cbs.api.common.exception.MsgCode.MessageMsg;
import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.util.BetConstants.BetResultStatus;
import com.lifeix.cbs.api.common.util.ContentConstants.MsgType;
import com.lifeix.cbs.api.common.util.ContentConstants.NotifyAddType;
import com.lifeix.cbs.api.common.util.ContentConstants.PushType;
import com.lifeix.cbs.api.common.util.ContestConstants.ContentActionType;
import com.lifeix.cbs.api.common.util.ContestConstants.ContentType;
import com.lifeix.cbs.api.common.util.ContestConstants.TempletId;
import com.lifeix.cbs.api.common.util.MessageConstants.NotifyNumType;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.common.util.PlayType;
import com.lifeix.cbs.api.service.user.CbsUserService;
import com.lifeix.cbs.message.bean.notify.NotifyListResponse;
import com.lifeix.cbs.message.bean.notify.NotifyResponse;
import com.lifeix.cbs.message.dao.notify.NotifyDataDao;
import com.lifeix.cbs.message.dao.notify.NotifyTempletDao;
import com.lifeix.cbs.message.dao.placard.PlacardDataDao;
import com.lifeix.cbs.message.dao.placard.PlacardTempletDao;
import com.lifeix.cbs.message.dto.notify.NotifyData;
import com.lifeix.cbs.message.dto.notify.NotifyTemplet;
import com.lifeix.cbs.message.service.notify.NotifyService;
import com.lifeix.cbs.message.service.placard.PlacardTempletService;
import com.lifeix.cbs.message.service.push.PushService;
import com.lifeix.common.utils.TimeUtil;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.user.beans.CustomResponse;

@Service("notifyService")
public class NotifyServiceImpl extends ImplSupport implements NotifyService {
    @Autowired
    NotifyDataDao notifyDataDao;

    @Autowired
    NotifyTempletDao notifyTempletDao;
    @Autowired
    PlacardDataDao placardDataDao;
    @Autowired
    PlacardTempletDao placardTempletDao;

    @Autowired
    CbsUserService cbsUserService;
    @Autowired
    PushService pushService;

    @Autowired
    PlacardTempletService placardTempletService;

    // static List<Integer> contestType = new ArrayList<Integer>();
    static List<Integer> pkType = new ArrayList<Integer>();
    static List<Integer> followType = new ArrayList<Integer>();

    static List<Integer> msgType = new ArrayList<Integer>();

    static {
	// 通知消息
	msgType.add(2);
	msgType.add(3);
	msgType.add(5);
	msgType.add(6);
	msgType.add(9);
	msgType.add(-1);
	// 2,4,5,6,7
	// contestType.add(2);
	pkType.add(7);
	followType.add(4);
    }

    /**
     * 外部系统调用添加消息提醒
     * 
     * @param notifyData
     * @param batchFlag
     * @throws L99IllegalParamsException
     * @throws JSONException
     * @throws L99IllegalDataException
     */
    @Override
    public void handleNotifyData(String notifyData, Integer addType) throws L99IllegalParamsException, JSONException,
	    L99IllegalDataException {
	ParamemeterAssert.assertDataNotNull(notifyData);

	if (addType != null) {
	    if (NotifyAddType.BATCH_ADD == addType) {
		JSONArray array = new JSONArray(notifyData);
		for (int i = 0; i < array.length(); i++) {
		    try {
			JSONObject obj = array.optJSONObject(i);
			if (obj == null)
			    continue;
			Long achieveId = obj.optLong("achieve_id");
			achieveId = achieveId == 0 ? null : achieveId;
			Long templetId = obj.optLong("templet_id");
			Long userId = obj.optLong("user_id");
			Long targetId = obj.optLong("target_id");
			String params = obj.optString("params");
			addNotify(templetId, userId, targetId, params, achieveId);
		    } catch (Exception e) {
			LOG.error(e.getMessage(), e);
		    }
		}
	    } else if (NotifyAddType.POLYMERIZE_ADD == addType) {
		JSONArray array = new JSONArray(notifyData);
		for (int i = 0; i < array.length(); i++) {
		    try {
			JSONObject userObj = array.optJSONObject(i);
			if (userObj == null)
			    continue;
			Long userId = userObj.optLong("user_id");
			JSONArray notifies = userObj.optJSONArray("notifies");
			if (userId == null || notifies == null || notifies.length() == 0)
			    continue;
			boolean added = false;
			for (int j = 0; j < notifies.length(); j++) {
			    JSONObject notifyObj = notifies.optJSONObject(j);
			    if (notifyObj == null)
				continue;
			    Long templetId = notifyObj.optLong("templet_id");
			    if (templetId == null)
				continue;
			    String params = notifyObj.optString("params");
			    Long targetId = notifyObj.optLong("target_id");
			    NotifyTemplet templet = notifyTempletDao.findById(templetId);
			    if (templet == null)
				continue;
			    Long achieveId = null;
			    if (notifyObj.has("achieve_id")) {
				achieveId = notifyObj.getLong("achieve_id");
			    }
			    NotifyData nData = new NotifyData();
			    nData.setUserId(userId);
			    nData.setTargetId(targetId);
			    nData.setTempletId(templetId);
			    nData.setType(templet.getType());
			    nData.setTemplate(templet.getTemplate());
			    nData.setTemplateData(params);
			    nData.setCreateTime(new Date());
			    nData.setReadFlag(false);
			    nData.setSkipId(achieveId);
			    boolean flag = notifyDataDao.insert(nData);
			    if (flag)
				added = true;
			}
			if (added) {
			    notifyIMServerUnread(userId);
			}
		    } catch (Exception e) {
			LOG.error(e.getMessage(), e);
		    }
		}
	    }
	} else {
	    JSONObject obj = new JSONObject(notifyData);
	    Long templetId = obj.optLong("templet_id");
	    Long userId = obj.optLong("user_id");
	    Long targetId = obj.optLong("target_id");
	    String params = obj.optString("params");
	    addNotify(templetId, userId, targetId, params, null);
	}
    }

    /**
     * 单次添加消息提醒
     * 
     * @param templetId
     * @param userId
     * @param targetId
     * @param params
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    @Override
    public void addNotify(Long templetId, Long userId, Long targetId, String params, Long skipId)
	    throws L99IllegalParamsException, L99IllegalDataException {

	ParamemeterAssert.assertDataNotNull(templetId, userId);

	NotifyTemplet templet = notifyTempletDao.findById(templetId);
	if (templet == null) {
	    throw new L99IllegalDataException(MessageMsg.CODE_MESSAGE_TEMPLET_EMPTY, MessageMsg.KEY_MESSAGE_TEMPLET_EMPTY);
	}

	NotifyData notifyData = new NotifyData();

	notifyData.setUserId(userId);
	notifyData.setTargetId(targetId);
	notifyData.setTempletId(templetId);
	notifyData.setType(templet.getType());
	notifyData.setTemplate(templet.getTemplate());
	notifyData.setTemplateData(params);
	notifyData.setCreateTime(new Date());
	notifyData.setReadFlag(false);
	notifyData.setSkipId(skipId);

	boolean flag = notifyDataDao.insert(notifyData);
	if (!flag) {
	    throw new L99IllegalDataException(MessageMsg.CODE_MESSAGE_NOTIFY_FAIL, MessageMsg.KEY_MESSAGE_NOTIFY_FAIL);
	}

	notifyIMServerUnread(userId);
    }

    /**
     * 通知IM-SERVER用户未读消息数量变动
     * 
     * @param userId
     */
    private void notifyIMServerUnread(Long userId) {
	if (userId != null) {
	    try {
		CbsUserResponse user = cbsUserService.selectById(userId);
		if (user != null) {
		    JSONObject obj = new JSONObject();
		    obj.put("longid", user.getLong_no());

		    int num = notifyDataDao.getUnreadNotifyCount(userId, null);
		    int msgNum = notifyDataDao.getUnreadNotifyCount(userId, msgType);
		    int followNum = notifyDataDao.getUnreadNotifyCount(userId, followType);
		    int pkNum = notifyDataDao.getUnreadNotifyCount(userId, pkType);
		    // int conunt = msgNum + followNum + pkNum;
		    obj.put("user_id", userId);
		    obj.put("num", num);
		    obj.put("msg_num", msgNum);
		    obj.put("follow_num", followNum);
		    obj.put("pk_num", pkNum);
		    obj.put("push_type", PushType.SYSTEM_MSG);

		    pushService.pushCount(userId, num, obj.toString());
		}
	    } catch (Throwable t) {
		LOG.error("user(userId=" + userId + ") unread message notify IM-SERVER failed..." + t.getMessage(), t);
	    }
	}
    }

    /**
     * 获取未读消息数量
     * 
     * @param userId
     * @return
     */
    public int getUnreadNotify(Long userId, int type) {
	if (userId == null) {
	    return 0;
	}
	switch (type) {
	case NotifyNumType.SYSTEM:
	    return notifyDataDao.getUnreadNotifyCount(userId, msgType);
	case NotifyNumType.FOLLOW:
	    return notifyDataDao.getUnreadNotifyCount(userId, followType);
	case NotifyNumType.PLACARD:
	    try {
		return placardTempletService.unreadNum(userId);
	    } catch (L99IllegalParamsException e) {
		return 0;
	    }
	case NotifyNumType.PK:
	    return notifyDataDao.getUnreadNotifyCount(userId, pkType);
	case NotifyNumType.COMMUNITY:
	    return notifyDataDao.getUnreadNotifyCount(userId, msgType);
	default:
	    return 0;
	}
    }

    /**
     * 消息数量的集合
     * 
     * @param userId
     * @param type
     * @return
     * @throws L99IllegalParamsException
     */
    @Override
    public CustomResponse getUnreadCount(Long userId) throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(userId);

	CustomResponse resp = new CustomResponse();
	int msgNum = notifyDataDao.getUnreadNotifyCount(userId, msgType);
	int followNum = notifyDataDao.getUnreadNotifyCount(userId, followType);
	int pkNum = notifyDataDao.getUnreadNotifyCount(userId, pkType);
	int notify = notifyDataDao.getUnreadNotifyCount(userId, null);
	int placard = placardTempletService.unreadNum(userId);
	resp.put("notify", notify);
	resp.put("placard", placard);

	resp.put("msg_num", msgNum);
	resp.put("follow_num", followNum);
	resp.put("pk_num", pkNum);
	return resp;
    }

    /**
     * 分页获取消息列表(时间倒排序)
     * 
     * @param startId
     * @param endId
     * @param userId
     * @param type
     * @param limit
     * @return
     * @throws L99IllegalParamsException
     * @throws JSONException
     */
    @Override
    public NotifyListResponse getNotifies(Long page, Long endId, Long userId, Integer type, Integer limit, Boolean forWeb)
	    throws L99IllegalParamsException, JSONException {
	ParamemeterAssert.assertDataNotNull(userId);
	limit = reviseLimit(limit);
	if (page == null) {
	    page = 1L;
	}

	NotifyListResponse ListResp = new NotifyListResponse();
	List<Integer> types = null;
	if (type != null) {
	    if (MsgType.NOTITY == type) {
		types = msgType;
	    } else if (MsgType.PK == type) {
		types = pkType;
	    } else if (MsgType.FOLLOW == type) {
		types = followType;
	    }

	}

	List<NotifyData> notifyList = notifyDataDao.findUserNotifys(userId, page, endId, types, limit);
	if (notifyList.size() == 0) {
	    ListResp.setStartId(-1L);
	    ListResp.setLimit(limit);
	    return ListResp;
	}

	if (notifyList.size() > limit) {
	    notifyList = notifyList.subList(0, limit);
	    ListResp.setStartId(page + 1);
	} else {
	    ListResp.setStartId(-1L);
	}

	List<NotifyResponse> respList = new ArrayList<NotifyResponse>(notifyList.size());
	for (int i = 0; i < notifyList.size(); i++) {
	    NotifyResponse resp = new NotifyResponse();
	    NotifyData notify = notifyList.get(i);
	    resp.setNotify_id(notify.getNotifyId());
	    resp.setUser_id(notify.getUserId());
	    resp.setTarget_id(notify.getTargetId());
	    resp.setType(notify.getType());

	    if (Boolean.TRUE.equals(forWeb)) {
		// 解析替换消息体
		Map<String, String> map = resolveNotifyBody(notify);
		resp.setTemplate(map.get("notifyBody"));
		if (StringUtils.isNotEmpty(map.get("pId"))) {
		    resp.setRef_id(Long.parseLong(map.get("pId")));
		}

	    } else {
		resp.setTemplate_id(notify.getTempletId());
		String templateData = notify.getTemplateData();
		if (notify.getTempletId() == TempletId.CONTENT && !StringUtil.isBlank(templateData)) {// 解析替换表情
		    try {
			JSONObject dataObj = new JSONObject(templateData);
			String targetContent = dataObj.optString("target_content");
			if (!StringUtil.isBlank(targetContent)) {
			    dataObj.put("target_content", targetContent);
			    templateData = dataObj.toString();
			}
		    } catch (Exception e) {
			LOG.error("Notify template data handle failed..." + e.getMessage(), e);
		    }
		}
		resp.setTemplate_data(templateData);
	    }
	    resp.setCreate_time(TimeUtil.getUtcTimeForDate(notify.getCreateTime()));
	    resp.setRead_flag(notify.isReadFlag());
	    Long skipId = notify.getSkipId();
	    if (notify.getTempletId() == TempletId.BET_RESULT) {
		// 赛事竞猜
		if (skipId != null) {
		    resp.setSkip_path("cbs://singlezhanji?id=" + skipId);
		}
	    } else if (notify.getTempletId() == TempletId.ACHIEVE) {
		// 成就
		if (skipId != null) {
		    resp.setSkip_path("cbs://singleachieved?id=" + skipId);
		}
	    } else if (notify.getTempletId() == TempletId.YY_BET_RESULT) {
		// 成就
		if (skipId != null) {
		    resp.setSkip_path("cbs://singleyaya?id=" + skipId);
		}
	    } else if (notify.getTempletId() == TempletId.COUPON_INVALID) {
		// 龙筹失效提醒
		resp.setSkip_path("cbs://longchou?valid=1");
	    }
	    respList.add(resp);
	}
	ListResp.setNotifies(respList);
	ListResp.setLimit(limit);

	// 当前类型的所有未读消息置为已读
	notifyDataDao.updateUnreadNotify(userId, types);

	return ListResp;
    }

    /**
     * 设定用户未读消息为已读
     */
    @Override
    public void updateUnreadNotify(Long userId, Integer type) {
	List<Integer> types = null;
	if (type != null) {
	    if (MsgType.NOTITY == type) {
		types = msgType;
	    } else if (MsgType.PK == type) {
		types = pkType;
	    } else if (MsgType.FOLLOW == type) {
		types = followType;
	    }
	}
	// 当前类型的所有未读消息置为已读
	notifyDataDao.updateUnreadNotify(userId, types);
    }

    /**
     * 解析替换消息体
     * 
     * @param notify
     * @return
     * @throws JSONException
     */
    private Map<String, String> resolveNotifyBody(NotifyData notify) throws JSONException {
	if (notify == null || StringUtil.isBlank(notify.getTemplate()) || StringUtil.isBlank(notify.getTemplateData())) {
	    return null;
	}
	Map<String, String> map = new HashMap<String, String>();
	String notifyBody = notify.getTemplate();
	JSONObject params = new JSONObject(notify.getTemplateData());

	if (notify.getTempletId() == TempletId.ACHIEVE) {
	    String achieveTimeKey = "achieve_time";
	    // 转换时间
	    String achieveTime = params.optString(achieveTimeKey, null);
	    String achieveTimeDesc = transformTime(achieveTime);
	    notifyBody = notifyBody.replaceAll(String.format("\\{#%s}", achieveTimeKey), achieveTimeDesc);

	} else if (notify.getTempletId() == TempletId.BET_RESULT || notify.getTempletId() == TempletId.PK_BET_RESULT
	        || notify.getTempletId() == TempletId.YY_BET_RESULT) {
	    String startTimeKey = "start_time";
	    String playTypeKey = "play_type";
	    String resultTypeKey = "result_type";
	    String returnTypeKey = "return_type";
	    String backKey = "back";
	    String isLongbiKey = "is_longbi";

	    // 更新开始时间
	    String startTime = params.optString(startTimeKey, null);
	    String startTimeDesc = transformTime(startTime);
	    notifyBody = notifyBody.replaceAll(String.format("\\{#%s}", startTimeKey), startTimeDesc);

	    // 更新货币单位
	    String unit = transformUnit(params.optBoolean(isLongbiKey, false));
	    notifyBody = notifyBody.replaceAll(String.format("\\{#%s}", isLongbiKey), unit);

	    // 更新结果描述
	    String resultType = params.optString(resultTypeKey, null);
	    if (!StringUtil.isBlank(resultType)) {
		String[] resultData = transformResult(resultType);
		notifyBody = notifyBody.replaceAll(String.format("\\{#%s}", resultTypeKey), resultData[0]);
		notifyBody = notifyBody.replaceAll(String.format("\\{#%s}", returnTypeKey), resultData[1]);
	    }

	    // 更新金额
	    double back = params.optDouble(backKey, 0D);
	    notifyBody = notifyBody.replaceAll(String.format("\\{#%s}", backKey), String.valueOf(back));

	    // 足球和篮球赛事需要解析类型
	    if (notify.getTempletId() == TempletId.BET_RESULT || notify.getTempletId() == TempletId.PK_BET_RESULT) {
		int playType = params.optInt(playTypeKey, 0);
		String playDesc = transformPlayType(playType);
		notifyBody = notifyBody.replaceAll(String.format("\\{#%s}", playTypeKey), playDesc);
	    }

	} else if (notify.getTempletId() == TempletId.CONTENT) {
	    String actionTypeKey = "action_type";
	    String contentTypeKey = "content_type";
	    String createTimeKey = "create_time";

	    // 更新创建时间
	    String createTime = params.optString(createTimeKey, null);
	    String createTimeDesc = transformTime(createTime);
	    notifyBody = notifyBody.replaceAll(String.format("\\{#%s}", createTimeKey), createTimeDesc);

	    // 更新操作类型
	    String actionType = params.optString(actionTypeKey, null);
	    if (!StringUtil.isBlank(actionType)) {
		String actionTypeDesc = transformActionType(Integer.valueOf(actionType));
		notifyBody = notifyBody.replaceAll(String.format("\\{#%s}", actionTypeKey), actionTypeDesc);
	    }

	    // 更新类容类型
	    String contentType = params.optString(contentTypeKey, null);
	    if (!StringUtil.isBlank(contentType)) {
		String contentTypeDesc = transformContentType(Integer.valueOf(actionType));
		notifyBody = notifyBody.replaceAll(String.format("\\{#%s}", contentTypeKey), contentTypeDesc);
	    }
	} else if (notify.getTempletId() == TempletId.USER_RELATION) {
	    String createTimeKey = "create_time";

	    // 更新创建时间
	    String createTime = params.optString(createTimeKey, null);
	    String createTimeDesc = transformTime(createTime);
	    notifyBody = notifyBody.replaceAll(String.format("\\{#%s}", createTimeKey), createTimeDesc);
	} else if (notify.getTempletId() == TempletId.USER_PK || notify.getTempletId() == TempletId.USER_PK_ACCEPT
	        || notify.getTempletId() == TempletId.USER_AT) {
	    String createTimeKey = "create_time";

	    // 更新创建时间
	    String createTime = params.optString(createTimeKey, null);
	    String createTimeDesc = transformTime(createTime);
	    notifyBody = notifyBody.replaceAll(String.format("\\{#%s}", createTimeKey), createTimeDesc);

	} else if (notify.getTempletId() == TempletId.CONTINUE_LOGIN_REWARD) {
	    String loginTimesKey = "loginTimes";
	    String moneyKey = "money";
	    String loginTimes = params.optString(loginTimesKey);
	    if (!StringUtil.isBlank(loginTimes)) {
		notifyBody = notifyBody.replaceAll(new StringBuilder().append("\\{#").append(loginTimesKey).append("\\}")
		        .toString(), loginTimes);
	    }
	    String money = params.optString(moneyKey);
	    if (!StringUtil.isBlank(money)) {
		notifyBody = notifyBody.replaceAll(new StringBuilder().append("\\{#").append(moneyKey).append("\\}")
		        .toString(), money);
	    }
	} else if (notify.getTempletId() == TempletId.SYSTEM_ROIL_PRIZE) {
	    String moneyKey = "money";
	    String money = params.optString(moneyKey);
	    if (!StringUtil.isBlank(money)) {
		notifyBody = notifyBody.replaceAll(new StringBuilder().append("\\{#").append(moneyKey).append("\\}")
		        .toString(), money);
	    }
	} else if (notify.getTempletId() == TempletId.SYSTEM_ROIL_RECHARGE) {
	    String amountKey = "amount";
	    String amount = params.optString(amountKey);
	    if (!StringUtil.isBlank(amount)) {
		notifyBody = notifyBody.replaceAll(new StringBuilder().append("\\{#").append(amountKey).append("\\}")
		        .toString(), amount);
	    }
	} else if (notify.getTempletId() == TempletId.SYSTEM_FIRST_WIN_PRIZE) { // 首胜
	    String timekey = "time";
	    String time = params.optString(timekey);
	    if (!StringUtil.isBlank(time)) {
		notifyBody = notifyBody.replaceAll(new StringBuilder().append("\\{#").append(timekey).append("\\}")
		        .toString(), transformTime(time));
	    }
	}

	String pId = "pId";
	String contestId = "contest_id";
	String contestType = "contest_type";
	// 转换基本参数
	Iterator<?> it = params.keys();
	while (it.hasNext()) {
	    String key = (String) it.next();

	    if (pId.equalsIgnoreCase(key)) {
		map.put("pId", params.getString(key));
		continue;
	    }

	    if (contestType.equalsIgnoreCase(key)) {
		map.put("contest_type", params.getString(key));
		continue;
	    }

	    if (contestId.equalsIgnoreCase(key)) {
		map.put("contest_id", params.getString(key));
		continue;
	    }

	    String rep = new StringBuilder().append("\\{#").append(key).append("\\}").toString();
	    String value = params.getString(key);
	    notifyBody = notifyBody.replaceAll(rep, value);
	}

	map.put("notifyBody", notifyBody);
	return map;
    }

    /**
     * 修正limit的范围
     * 
     * @param limit
     * @return
     */
    private static int reviseLimit(int limit) {
	limit = Math.max(0, limit);
	limit = Math.min(limit, 100);
	return limit;
    }

    /**
     * 转换货币单位
     * 
     * @param isLongbi
     * @return
     */
    static String transformUnit(boolean isLongbi) {
	return isLongbi ? "龙币" : "龙筹";
    }

    /**
     * 转换赛事结果
     * 
     * @param resultType
     * @return
     */
    static String[] transformResult(String resultType) {
	String[] resultData = { "", "" };
	if (String.valueOf(BetResultStatus.WIN).equalsIgnoreCase(resultType)) {
	    resultData[0] = "胜利";
	    resultData[1] = "净赢";
	} else if (String.valueOf(BetResultStatus.LOSS).equalsIgnoreCase(resultType)) {
	    resultData[0] = "失败";
	    resultData[1] = "输";
	} else if (String.valueOf(BetResultStatus.DRAW).equalsIgnoreCase(resultType)
	        || String.valueOf(3).equalsIgnoreCase(resultType)) {
	    resultData[0] = "走盘";
	    resultData[1] = "返还";
	}
	return resultData;
    }

    /**
     * 转换时间
     * 
     * @param time
     * @return
     */
    static String transformTime(String time) {
	if (time == null) {
	    return null;
	}
	Calendar cal = Calendar.getInstance();
	Date date = TimeUtil.getDateByUtcFormattedDate(time);
	cal.setTime(date);
	int month = cal.get(Calendar.MONTH) + 1;
	int day = cal.get(Calendar.DAY_OF_MONTH);
	return String.format("%s月%s日", month, day);
    }

    /**
     * 转换类型
     * 
     * @param playType
     * @return
     */
    static String transformPlayType(int playType) {
	for (PlayType pt : PlayType.values()) {
	    if (playType == pt.getId()) {
		return pt.getName();
	    }
	}
	return "";
    }

    /**
     * 转换操作类型
     * 
     * @param actionType
     * @return
     */
    static String transformActionType(int actionType) {
	if (actionType == ContentActionType.REBLOG) {
	    return "转发";
	} else if (actionType == ContentActionType.COMMENT) {
	    return "回复";
	}
	return "";
    }

    /**
     * 转换类容类型
     * 
     * @param contentType
     * @return
     */
    static String transformContentType(int contentType) {
	if (contentType == ContentType.CONTENT) {
	    return "正文";
	} else if (contentType == ContentType.REBLOG) {
	    return "转发";
	} else if (contentType == ContentType.COMMENT) {
	    return "回复";
	}
	return "";
    }
}
