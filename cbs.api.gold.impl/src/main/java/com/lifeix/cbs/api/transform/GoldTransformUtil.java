package com.lifeix.cbs.api.transform;

import com.l99.vo.pay.PayAccountLogReponse;
import com.l99.vo.pay.PayAccountOrderReponse;
import com.l99.vo.pay.PayAccountOrderTNReponse;
import com.l99.vo.pay.PayAccountReponse;
import com.l99.vo.pay.PaymentWeixinResponse;
import com.l99.vo.pay.RechargePayFormVo;
import com.lifeix.cbs.api.bean.gold.GoldLogResponse;
import com.lifeix.cbs.api.bean.gold.GoldResponse;
import com.lifeix.cbs.api.bean.gold.GoldStatisticResponse;
import com.lifeix.cbs.api.bean.money.MoneyCardResponse;
import com.lifeix.cbs.api.bean.money.MoneyMissedResponse;
import com.lifeix.cbs.api.bean.money.MoneyOrderResponse;
import com.lifeix.cbs.api.bean.money.MoneyOrderTNResponse;
import com.lifeix.cbs.api.bean.money.MoneyOrderWapResponse;
import com.lifeix.cbs.api.bean.money.MoneyOrderWeixinResponse;
import com.lifeix.cbs.api.bean.money.MoneyStatisticResponse;
import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.cbs.api.dto.gold.Gold;
import com.lifeix.cbs.api.dto.gold.GoldLog;
import com.lifeix.cbs.api.dto.gold.GoldStatistic;
import com.lifeix.cbs.api.dto.money.MoneyCard;
import com.lifeix.cbs.api.dto.money.MoneyMissed;
import com.lifeix.cbs.api.dto.money.MoneyStatistic;
import com.lifeix.common.utils.TimeUtil;

/**
 * DTO 转换为 VO 工具类
 * 
 * @author jacky
 * 
 */
public class GoldTransformUtil {

    /**
     * 龙筹对象
     * 
     * @param gold
     * @return
     */
    public static GoldResponse transformGold(Gold gold) {
	GoldResponse resp = null;
	if (gold != null) {
	    resp = new GoldResponse();
	    resp.setUser_id(gold.getUserId());
	    resp.setBalance(gold.getBalance());
	    resp.setFrozen(gold.getFrozen());
	    resp.setIncome(gold.getIncome());
	    resp.setOutlay(gold.getOutlay());
	    resp.setStatus(gold.getStatus());
	}
	return resp;
    }

    /**
     * 龙筹日志
     * 
     * @param goldLog
     * @return
     */
    public static GoldLogResponse transformGoldLog(GoldLog goldLog, CbsUserResponse user) {
	GoldLogResponse resp = null;
	if (goldLog != null) {
	    resp = new GoldLogResponse();
	    resp.setLog_id(goldLog.getLogId());
	    resp.setUser_id(goldLog.getUserId());
	    resp.setMoney(goldLog.getMoney());
	    resp.setContent(goldLog.getContent());
	    resp.setType(goldLog.getType());
	    resp.setIp_address(goldLog.getIpaddress());
	    resp.setLink(goldLog.getLink());
	    resp.setLog_time(TimeUtil.getUtcTimeForDate(goldLog.getLogTime()));
	    resp.setUser(user);
	}
	return resp;
    }

    /**
     * 龙筹统计
     * 
     * @param goldStatistic
     * @return
     */
    public static GoldStatisticResponse transformGoldStatistic(GoldStatistic goldStatistic) {
	GoldStatisticResponse resp = null;
	if (goldStatistic != null) {
	    resp = new GoldStatisticResponse();
	    resp.setId(goldStatistic.getId());
	    resp.setIncome(goldStatistic.getIncome());
	    resp.setOutlay(goldStatistic.getOutlay());
	    resp.setTotal(goldStatistic.getTotal());
	    resp.setIn_counts(goldStatistic.getInCounts());
	    resp.setOut_counts(goldStatistic.getOutCounts());
	    resp.setYear(goldStatistic.getYear());
	    resp.setDay(goldStatistic.getDay());
	    resp.setCreate_time(TimeUtil.getUtcTimeForDate(goldStatistic.getCreateTime()));
	}
	return resp;
    }

    /**
     * 龙币统计
     * 
     * @param goldStatistic
     * @return
     */
    public static MoneyStatisticResponse transformMoneyStatistic(MoneyStatistic moneyStatistic) {
	MoneyStatisticResponse resp = null;
	if (moneyStatistic != null) {
	    resp = new MoneyStatisticResponse();
	    resp.setId(moneyStatistic.getId());
	    resp.setIncome(moneyStatistic.getIncome());
	    resp.setOutlay(moneyStatistic.getOutlay());
	    resp.setTotal(moneyStatistic.getTotal());
	    resp.setIn_counts(moneyStatistic.getInCounts());
	    resp.setOut_counts(moneyStatistic.getOutCounts());
	    resp.setYear(moneyStatistic.getYear());
	    resp.setDay(moneyStatistic.getDay());
	    resp.setCreate_time(TimeUtil.getUtcTimeForDate(moneyStatistic.getCreateTime()));
	}
	return resp;
    }

    /**
     * 龙币对象
     * 
     * @param payment
     * @return
     */
    public static GoldResponse transformPayment(PayAccountReponse payment) {

	GoldResponse resp = null;
	if (payment != null) {
	    resp = new GoldResponse();
	    resp.setUser_id(payment.getAccount_id());
	    resp.setBalance(Double.parseDouble(String.valueOf(payment.getUser_money())));
	    resp.setFrozen(payment.getFrozen_money());
	    resp.setStatus(payment.getStatus());
	}
	return resp;
    }

    /**
     * 龙币日志对象
     * 
     * @param payment
     * @return
     */
    public static GoldLogResponse transformPaymentLog(PayAccountLogReponse paymentLog, CbsUserResponse user) {

	GoldLogResponse resp = null;
	if (paymentLog != null) {
	    resp = new GoldLogResponse();
	    resp.setLog_id(paymentLog.getLog_id());
	    resp.setUser_id(paymentLog.getAccount_id());
	    resp.setMoney(Double.parseDouble(String.valueOf(paymentLog.getUser_money())));
	    resp.setContent(paymentLog.getChange_desc());
	    resp.setType(paymentLog.getChange_type());
	    resp.setLog_time(TimeUtil.getUtcTimeForDate(TimeUtil.getDateByUnixTimeStamp(paymentLog.getChange_time())));
	    resp.setLink(paymentLog.getAbout_link());
	    resp.setIp_address(paymentLog.getOperat_ip());
	    resp.setUser(user);
	}
	return resp;
    }

    /**
     * 龙币订单对象
     * 
     * @param order
     * @return
     */
    public static MoneyOrderResponse transformOrder(PayAccountOrderReponse order) {

	MoneyOrderResponse resp = null;
	if (order != null) {
	    resp = new MoneyOrderResponse();
	    resp.setOrder_id(order.getOrder_id());
	    resp.setUser_id(order.getAccount_id());
	    resp.setTarget_id(order.getTarget_id());
	    resp.setCard_id(order.getCard_id());
	    resp.setAmount(order.getAmount());
	    resp.setObtain(order.getObtain());
	    resp.setOrder_no(order.getOrder_no());
	    resp.setAdd_time(order.getAdd_time());
	    resp.setPaid_time(order.getPaid_time());
	    resp.setOrder_statu(order.getOrder_statu());
	    resp.setPayment_id(order.getPayment_id());
	    resp.setPayment_type(order.getPayment_type());
	    resp.setAdmin_user(order.getAdmin_user());
	    resp.setAdmin_note(order.getAdmin_note());
	    resp.setUser_note(order.getUser_note());
	    resp.setReturned(order.isReturned());
	    resp.setReturned(order.isReturned());
	    resp.setIp_address(order.getIp_address());
	    resp.setNotify_id(order.getNotify_id());
	    resp.setPay_order(order.getPay_order());
	    resp.setSourceClient(order.getSourceClient());
	    PaymentWeixinResponse weixin = order.getWeixin();
	    if (weixin != null) {
		MoneyOrderWeixinResponse nWeixin = new MoneyOrderWeixinResponse();
		nWeixin.setNoncestr(weixin.getNoncestr());
		nWeixin.setPackagestr(weixin.getPackagestr());
		nWeixin.setPrepayid(weixin.getPrepayid());
		nWeixin.setSign(weixin.getSign());
		nWeixin.setTimestamp(weixin.getTimestamp());
		nWeixin.setAppId(weixin.getAppId());
		resp.setWeixin(nWeixin);
	    }
	}
	return resp;
    }

    /**
     * 龙币网页订单对象
     * 
     * @param order
     * @return
     */
    public static MoneyOrderWapResponse transformWapOrder(RechargePayFormVo order) {

	MoneyOrderWapResponse resp = null;
	if (order != null) {
	    resp = new MoneyOrderWapResponse();
	    resp.setPay_url(order.getPay_url());
	    resp.setPay_method(order.getPay_method());
	    resp.setPay_parameters(order.getPay_parameters());
	}
	return resp;
    }

    /**
     * 龙币订单流水号对象
     * 
     * @param order
     * @return
     */
    public static MoneyOrderTNResponse transformOrderTN(PayAccountOrderTNReponse orderTn) {

	MoneyOrderTNResponse resp = null;
	if (orderTn != null) {
	    resp = new MoneyOrderTNResponse();
	    resp.setOrder_id(orderTn.getOrder_id());
	    resp.setTn(orderTn.getTn());
	}
	return resp;
    }

    /**
     * 龙币充值类型
     * 
     * @param card
     * @return
     */
    public static MoneyCardResponse transformMoneyCard(MoneyCard card) {

	MoneyCardResponse resp = null;
	if (card != null) {
	    resp = new MoneyCardResponse();
	    resp.setId(card.getId());
	    resp.setName(card.getName());
	    resp.setDetail(card.getDetail());
	    resp.setPrice(card.getPrice());
	    resp.setAmount(card.getAmount());
	    resp.setType(card.getType());
	    resp.setHandsel(card.getHandsel());
	    resp.setCreate_time(TimeUtil.getUtcTimeForDate(card.getCreateTime()));
	}
	return resp;
    }

    /**
     * 龙币丢失记录
     * 
     * @param moneyMissed
     * @return
     */
    public static MoneyMissedResponse transformMoneyMissed(MoneyMissed moneyMissed) {

	MoneyMissedResponse resp = null;
	if (moneyMissed != null) {
	    resp = new MoneyMissedResponse();
	    resp.setId(moneyMissed.getId());
	    resp.setUserId(moneyMissed.getUserId());
	    resp.setMoney_type(moneyMissed.getMoneyType());
	    resp.setMoney_data(moneyMissed.getMoneyData());
	    resp.setAmount(moneyMissed.getAmount());
	    resp.setDetail(moneyMissed.getDetail());
	    resp.setStatus(moneyMissed.getStatus());
	    resp.setCreate_time(TimeUtil.getUtcTimeForDate(moneyMissed.getCreateTime()));
	}
	return resp;
    }
}
