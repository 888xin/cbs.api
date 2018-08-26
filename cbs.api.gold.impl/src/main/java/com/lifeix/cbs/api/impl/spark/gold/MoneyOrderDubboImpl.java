package com.lifeix.cbs.api.impl.spark.gold;

import com.lifeix.cbs.api.common.mission.Mission;
import com.lifeix.cbs.api.service.misson.MissionUserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.activity.service.first.ActivityFirstLogService;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.dao.money.MoneyOrderDao;
import com.lifeix.cbs.api.dto.money.MoneyOrder;
import com.lifeix.cbs.api.service.money.MoneyStatisticService;
import com.lifeix.cbs.api.service.spark.gold.MoneyOrderDubbo;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.payment.dubbo.service.PayAccountOrderService;

public class MoneyOrderDubboImpl implements MoneyOrderDubbo {

    protected static final Logger LOG = LoggerFactory.getLogger(MoneyOrderDubboImpl.class);

    @Autowired
    private PayAccountOrderService payAccountOrderService;

    @Autowired
    private MoneyOrderDao moneyOrderDao;

    @Autowired
    protected MoneyStatisticService moneyStatisticService;

    @Autowired
    private ActivityFirstLogService activityFirstLogService;

    @Autowired
    private MissionUserService missionUserService;

    @Override
    public void finshOrder(Long userId, Long orderId) throws L99IllegalParamsException, L99IllegalDataException,
	    JSONException {

	ParamemeterAssert.assertDataNotNull(userId, orderId);

	String orderData = payAccountOrderService.check(orderId, userId);

	LOG.info(String.format("start check money order %d", orderId));

	JSONObject orderRet = new JSONObject(orderData);
	if (orderRet.getInt("code") != DataResponse.OK) {
	    throw new L99IllegalDataException(orderRet.getString("code"), orderRet.getString("msg"));
	}

	// 确认订单的赠送记录
	MoneyOrder moneyOrder = moneyOrderDao.findById(orderId);
	if (moneyOrder != null && moneyOrder.getStatus() == 0) {
	    LOG.info(String.format("start exec money order %d - %d - %s", orderId, moneyOrder.getType(),
		    moneyOrder.getHandsel()));
	    if (moneyOrder.getType() == 0) { // 赠送龙筹
		try {
		    moneyOrder.setStatus(1);
		    boolean flag = moneyOrderDao.update(moneyOrder);
		    if (flag) {
			// TODO 赠送龙筹
			LOG.info(String.format("money order add gold sucess %d - %s", orderId, moneyOrder.getHandsel()));
		    }
		} catch (Exception e) {
		    LOG.info(String.format("money order failed %d - %s", orderId, e.getMessage()));
		}

	    }

	    // 写入龙币充值统计
	    moneyStatisticService.insertUserConsumer(userId + "", moneyOrder.getAmount());
	    moneyStatisticService.insertSystemConsumer(userId + "", moneyOrder.getAmount());

	    // 判断是否参与首充活动
	    activityFirstLogService.addFirstRecord(userId, moneyOrder.getPrice());

        //add by lhx on 16-06-23 首次充值任务 start
        missionUserService.validate(userId, Mission.FIRST_LONGBI);
        //add by lhx on 16-06-23 首次充值任务 end
	}

    }
}
