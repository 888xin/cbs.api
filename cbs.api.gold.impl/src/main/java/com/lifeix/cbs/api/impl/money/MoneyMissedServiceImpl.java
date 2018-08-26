package com.lifeix.cbs.api.impl.money;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.bean.money.MoneyMissedListResponse;
import com.lifeix.cbs.api.bean.money.MoneyMissedResponse;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestType;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.common.util.PlayType;
import com.lifeix.cbs.api.dao.money.MoneyMissedDao;
import com.lifeix.cbs.api.dto.money.MoneyMissed;
import com.lifeix.cbs.api.service.money.MoneyMissedService;
import com.lifeix.cbs.api.transform.GoldTransformUtil;
import com.lifeix.cbs.contest.service.spark.settle.BbSettleDubbo;
import com.lifeix.cbs.contest.service.spark.settle.FbSettleDubbo;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.user.beans.CustomResponse;

@Service("moneyMissedService")
public class MoneyMissedServiceImpl extends ImplSupport implements MoneyMissedService {

    @Autowired
    private MoneyMissedDao moneyMissedDao;

    @Autowired
    private FbSettleDubbo fbSettleService;

    @Autowired
    private BbSettleDubbo bbSettleService;

    /**
     * 保存丢失的龙币操作记录
     * 
     * @param userId
     * @param moneyType
     * @param moneyData
     * @param amount
     * @param detail
     * @param ipaddress
     */
    @Override
    public void saveMoneyMissed(Long userId, Integer moneyType, String moneyData, Double amount, String detail) {

	MoneyMissed moneyMissed = new MoneyMissed();
	moneyMissed.setUserId(userId);
	moneyMissed.setMoneyType(moneyType);
	moneyMissed.setMoneyData(moneyData);
	moneyMissed.setAmount(amount);
	moneyMissed.setDetail(detail);
	moneyMissed.setStatus(0);
	moneyMissed.setCreateTime(new Date());

	moneyMissedDao.insert(moneyMissed);

    }

    /**
     * 修改记录状态
     * 
     * @param id
     * @param repairFlag
     *            true 修复 false 放弃
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     * @throws JSONException
     */
    @Override
    public void editMoneyMissed(Long id, Boolean repairFlag) throws L99IllegalParamsException, L99IllegalDataException,
	    JSONException {

	ParamemeterAssert.assertDataNotNull(id, repairFlag);

	MoneyMissed moneyMissed = moneyMissedDao.findById(id);
	if (moneyMissed == null || moneyMissed.getStatus().intValue() != 0) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}
	// 修复处理
	if (repairFlag) {
	    int playType = moneyMissed.getMoneyType();
	    if (PlayType.verifyType(playType, ContestType.FOOTBALL)) {
		fbSettleService.settleMissedBet(playType, Long.valueOf(moneyMissed.getMoneyData()));
	    } else if (PlayType.verifyType(playType, ContestType.BASKETBALL)) {
		bbSettleService.settleMissedBet(playType, Long.valueOf(moneyMissed.getMoneyData()));
	    }
	}
	moneyMissed.setStatus(repairFlag ? 1 : 2);
	boolean flag = moneyMissedDao.update(moneyMissed);
	if (!flag) {
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}
    }

    /**
     * 查询龙币丢失的操作记录列表
     * 
     * @param status
     * @param startId
     * @param limit
     * @return
     */
    @Override
    public MoneyMissedListResponse findMoneyMisseds(Integer status, Long startId, int limit) {

	List<MoneyMissed> moneyMisseds = moneyMissedDao.findMoneyMisseds(status, startId, limit);

	List<MoneyMissedResponse> money_misseds = new ArrayList<MoneyMissedResponse>();
	for (MoneyMissed moneyMissed : moneyMisseds) {
	    money_misseds.add(GoldTransformUtil.transformMoneyMissed(moneyMissed));
	    startId = moneyMissed.getId();
	}

	if (money_misseds.size() == 0 || money_misseds.size() < limit) { // 没有数据或数据不够返回-1
	    startId = -1L;
	}

	MoneyMissedListResponse reponse = new MoneyMissedListResponse();
	reponse.setMoney_misseds(money_misseds);
	reponse.setNumber(money_misseds.size());
	reponse.setStartId(startId);

	return reponse;
    }

    /**
     * 统计未处理的龙币丢失记录
     * 
     * @return
     */
    public CustomResponse countMoneyMisseds() {
	CustomResponse response = new CustomResponse();
	response.put("num", moneyMissedDao.countMoneyMisseds());
	return response;
    }
}
