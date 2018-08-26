package com.lifeix.cbs.api.service.money;

import org.json.JSONException;

import com.lifeix.cbs.api.bean.money.MoneyMissedListResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.user.beans.CustomResponse;

/**
 * 龙币丢失操作记录接口
 * 
 */
public interface MoneyMissedService {

    /**
     * 保存丢失的龙币操作记录
     * 
     * @param userId
     * @param moneyType
     * @param moneyData
     * @param amount
     * @param detail
     */
    public void saveMoneyMissed(Long userId, Integer moneyType, String moneyData, Double amount, String detail);

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
    public void editMoneyMissed(Long id, Boolean repairFlag) throws L99IllegalParamsException, L99IllegalDataException,
	    JSONException;

    /**
     * 查询龙币丢失的操作记录列表
     * 
     * @param status
     * @param startId
     * @param limit
     * @return
     */
    public MoneyMissedListResponse findMoneyMisseds(Integer status, Long startId, int limit);

    /**
     * 统计未处理的龙币丢失记录
     * 
     * @return
     */
    public CustomResponse countMoneyMisseds();
}
