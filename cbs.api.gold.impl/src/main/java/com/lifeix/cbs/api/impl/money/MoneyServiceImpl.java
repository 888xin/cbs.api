package com.lifeix.cbs.api.impl.money;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.rpc.RpcException;
import com.google.gson.Gson;
import com.l99.util.pay.PayAccountLogTypeEnum;
import com.l99.vo.pay.PayAccountReponse;
import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.bean.gold.GoldResponse;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.service.money.MoneyMissedService;
import com.lifeix.cbs.api.service.money.MoneyService;
import com.lifeix.cbs.api.transform.GoldTransformUtil;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.payment.dubbo.service.PayAccountService;

@Service("moneyService")
public class MoneyServiceImpl extends ImplSupport implements MoneyService {

    protected final static Logger LOG = LoggerFactory.getLogger(MoneyServiceImpl.class);

    @Autowired
    private PayAccountService payAccountService;

    @Autowired
    private MoneyMissedService moneyMissedService;

    @Override
    public GoldResponse viewUserMoney(Long userId, String ipaddress) throws L99IllegalParamsException,
	    L99IllegalDataException, JSONException {

	ParamemeterAssert.assertDataNotNull(userId);
	try {
	    String moneyData = payAccountService.view(userId, ipaddress);
	    JSONObject moneyRet = new JSONObject(moneyData);
	    if (moneyRet.getInt("code") != DataResponse.OK) {
		throw new L99IllegalDataException(moneyRet.getString("code"), moneyRet.getString("msg"));
	    }
	    PayAccountReponse money = new Gson().fromJson(moneyRet.getString("data"), PayAccountReponse.class);
	    return GoldTransformUtil.transformPayment(money);
	} catch (RpcException e) {
	    throw new L99IllegalDataException(BasicMsg.CODE_MONEYSERVER, BasicMsg.KEY_MONEYSERVER);
	}

    }

    @Override
    public Long consumeMoney(Long userId, Double amount, String desc, String ipaddress) throws L99IllegalParamsException,
	    L99IllegalDataException, JSONException {

	ParamemeterAssert.assertDataNotNull(userId, amount);
	ParamemeterAssert.assertPrice(amount);

	// 记录消费日志
	try {
	    JSONObject params = new JSONObject();
	    params.put("thing", desc);

	    String execData = payAccountService.consume(userId, amount.floatValue(), PayAccountLogTypeEnum.CBS_PAYMENT,
		    params.toString(), null, ipaddress);

	    JSONObject execRet = new JSONObject(execData);
	    if (execRet.getInt("code") != DataResponse.OK) {
		throw new L99IllegalDataException(execRet.getString("code"), execRet.getString("msg"));
	    }
	    Long logId = null;
	    if (execRet.has("data")) {
		JSONObject dataRet = execRet.getJSONObject("data");
		if (dataRet.has("log_id")) {
		    logId = dataRet.getLong("log_id");
		}
	    }

	    return logId;

	} catch (RpcException e) {
	    LOG.error(String.format("## rpc consume money[%s]-[%s]-[%s] failed", userId, amount, desc));
	    throw new L99IllegalDataException(BasicMsg.CODE_MONEYSERVER, BasicMsg.KEY_MONEYSERVER);
	}

    }

    @Override
    public Long earnMoney(Long userId, Double amount, String desc, String ipaddress, Integer moneyType, String moneyData)
	    throws L99IllegalParamsException, L99IllegalDataException, JSONException {
	ParamemeterAssert.assertDataNotNull(userId, amount);
	ParamemeterAssert.assertPrice(amount);

	try {
	    String execData = payAccountService.earn(userId, amount.floatValue(), PayAccountLogTypeEnum.CBS_EARN, desc,
		    null, ipaddress);

	    JSONObject execRet = new JSONObject(execData);
	    if (execRet.getInt("code") != DataResponse.OK) {
		throw new L99IllegalDataException(execRet.getString("code"), execRet.getString("msg"));
	    }
	    Long logId = null;
	    if (execRet.has("data")) {
		JSONObject dataRet = execRet.getJSONObject("data");
		if (dataRet.has("log_id")) {
		    logId = dataRet.getLong("log_id");
		}
	    }
	    return logId;
	} catch (RpcException e) {

	    // 因为是先操作后加钱 如果加钱失败会让用户产生损，记录到一张表中方便后台查询
	    moneyMissedService.saveMoneyMissed(userId, moneyType, moneyData, amount, desc);

	    LOG.error(String.format("## rpc earn money[%s]-[%s]-[%s] failed", userId, amount, desc));
	    throw new L99IllegalDataException(BasicMsg.CODE_MONEYSERVER, BasicMsg.KEY_MONEYSERVER);
	}

    }

    @Override
    public void systemRechargeMoney(Long userId, Double amount, String admin, String ipaddress)
	    throws L99IllegalParamsException, L99IllegalDataException {

	ParamemeterAssert.assertDataNotNull(userId, amount, admin);
	ParamemeterAssert.assertPrice(amount);

	try {
	    String desc = "后台充值" + amount + "龙币";
	    payAccountService.earn(userId, amount.floatValue(), PayAccountLogTypeEnum.CBS_SYSTEMREC, desc, admin, ipaddress);
	} catch (RpcException e) {
	    throw new L99IllegalDataException(BasicMsg.CODE_MONEYSERVER, BasicMsg.KEY_MONEYSERVER);
	}

    }

    @Override
    public void systemDeductionMoney(Long userId, Double amount, String admin, String ipaddress)
	    throws L99IllegalParamsException, L99IllegalDataException {

	ParamemeterAssert.assertDataNotNull(userId, amount, admin);
	ParamemeterAssert.assertPrice(amount);

	try {
	    String params = String.format("{\"thing\":\"后台扣除%s龙币\"}", amount);
	    payAccountService.consume(userId, amount.floatValue(), PayAccountLogTypeEnum.CBS_SYSTEMDED, params, admin,
		    ipaddress);
	} catch (RpcException e) {
	    throw new L99IllegalDataException(BasicMsg.CODE_MONEYSERVER, BasicMsg.KEY_MONEYSERVER);
	}

    }
}
