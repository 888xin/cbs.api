package com.lifeix.cbs.api.impl.money;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.l99.util.pay.PayAccountLogTypeEnum;
import com.l99.vo.pay.PayAccountLogListReponse;
import com.l99.vo.pay.PayAccountLogReponse;
import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.bean.gold.GoldLogListResponse;
import com.lifeix.cbs.api.bean.gold.GoldLogResponse;
import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.cbs.api.common.exception.MsgCode;
import com.lifeix.cbs.api.common.exception.MsgCode.UserMsg;
import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.service.money.MoneyLogService;
import com.lifeix.cbs.api.service.money.MoneyService;
import com.lifeix.cbs.api.service.user.CbsUserService;
import com.lifeix.cbs.api.transform.GoldTransformUtil;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;
import com.lifeix.payment.dubbo.service.PayAccountLogService;

@Service("moneyLogService")
public class MoneyLogServiceImpl extends ImplSupport implements MoneyLogService {

    @Autowired
    private PayAccountLogService payAccountLogService;

    @Autowired
    private MoneyService moneyService;

    @Autowired
    private CbsUserService cbsUserService;

    public GoldLogListResponse findGoldLogList(Long userId, Long userNo, String logTypes, Date startTime, Date endTime,
	    boolean userFlag, Long startId, int limit) throws L99IllegalParamsException, L99IllegalDataException,
	    JSONException, L99NetworkException {

	limit = Math.min(limit, 100);
	limit = Math.max(limit, 1);

	logTypes = filterLogTypes(logTypes);

	if (userNo != null && userId == null) {
	    CbsUserResponse user = cbsUserService.getCbsUserByLongNo(userNo);
	    if (user != null) {
		userId = user.getUser_id();
	    } else {
		throw new L99IllegalParamsException(UserMsg.CODE_USER_ACCOUNT_NOT_FOUND,
		        MsgCode.UserMsg.KEY_USER_ACCOUNT_NOT_FOUND);
	    }
	}

	String moneyData = payAccountLogService.getLogs(userId, logTypes, startTime, endTime, startId, limit);
	JSONObject moneyRet = new JSONObject(moneyData);
	if (moneyRet.getInt("code") != DataResponse.OK) {
	    throw new L99IllegalDataException(moneyRet.getString("code"), moneyRet.getString("msg"));
	}

	GoldLogListResponse reponse = new GoldLogListResponse();
	PayAccountLogListReponse payLogRes = new Gson().fromJson(moneyRet.getString("data"), PayAccountLogListReponse.class);
	List<PayAccountLogReponse> payLogs = payLogRes.getLogs();
	List<GoldLogResponse> gold_logs = new ArrayList<GoldLogResponse>();
	if (payLogs == null || payLogs.isEmpty()) {
	    reponse.setGold_logs(gold_logs);
	    return reponse;
	}

	Map<Long, CbsUserResponse> users = null;
	if (userFlag) { // 查询用户标识
	    List<Long> userIds = new ArrayList<Long>();
	    for (PayAccountLogReponse payLog : payLogs) {
		userIds.add(payLog.getAccount_id());
	    }
	    users = cbsUserService.findCsAccountMapByIds(userIds);
	} else {
	    users = new HashMap<Long, CbsUserResponse>();
	}

	// 转换Log
	for (PayAccountLogReponse payLog : payLogs) {
	    gold_logs.add(GoldTransformUtil.transformPaymentLog(payLog, users.get(payLog.getAccount_id())));
	    startId = payLog.getLog_id();
	}

	if (gold_logs.size() == 0 || gold_logs.size() < limit) { // 没有数据或数据不够返回-1
	    startId = -1L;
	}

	reponse.setGold_logs(gold_logs);
	reponse.setStartId(startId);
	reponse.setLimit(limit);
	return reponse;

    }

    @Override
    public GoldLogListResponse findGoldLogById(Long logId) throws L99IllegalDataException, JSONException,
	    L99IllegalParamsException, L99NetworkException {
	String moneyData = payAccountLogService.findLogById(logId);
	JSONObject moneyRet = new JSONObject(moneyData);
	if (moneyRet.getInt("code") != DataResponse.OK) {
	    throw new L99IllegalDataException(moneyRet.getString("code"), moneyRet.getString("msg"));
	}

	GoldLogListResponse reponse = new GoldLogListResponse();
	List<GoldLogResponse> gold_logs = new ArrayList<GoldLogResponse>();
	reponse.setGold_logs(gold_logs);

	PayAccountLogReponse payLogRes = new Gson().fromJson(moneyRet.getString("data"), PayAccountLogReponse.class);
	CbsUserResponse user = cbsUserService.getCbsUserByUserId(payLogRes.getAccount_id(), false);
	GoldLogResponse log = GoldTransformUtil.transformPaymentLog(payLogRes, user);
	gold_logs.add(log);
	return reponse;
    }

    // 默认只查询大赢家的日志
    static Set<Long> typeSet = new HashSet<Long>();
    static {
	typeSet.add(PayAccountLogTypeEnum.CBS_RECHARGE);
	typeSet.add(PayAccountLogTypeEnum.CBS_SYSTEMREC);
	typeSet.add(PayAccountLogTypeEnum.CBS_PAYMENT);
	typeSet.add(PayAccountLogTypeEnum.CBS_SYSTEMDED);
	typeSet.add(PayAccountLogTypeEnum.CBS_EARN);
    }

    /**
     * 过滤龙币日志类型
     * 
     * @param logTypes
     * @return
     */
    public static String filterLogTypes(String logTypes) {
	if (logTypes == null) {
	    return StringUtils.join(typeSet, ",");
	}
	String[] typeArray = logTypes.split(",");
	Set<Long> newTypes = new HashSet<Long>();
	for (String typeStr : typeArray) {
	    if (StringUtils.isEmpty(typeStr)) {
		continue;
	    }
	    Long type = Long.valueOf(typeStr);
	    if (typeSet.contains(type)) {
		newTypes.add(type);
	    }
	}
	if (newTypes.size() == 0) {
	    return StringUtils.join(typeSet, ",");
	} else {
	    return StringUtils.join(newTypes, ",");
	}
    }

}
