package com.lifeix.cbs.api.service.money;

import org.json.JSONException;

import com.lifeix.cbs.api.bean.gold.GoldResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * 龙币接口
 * 
 */
public interface MoneyService {

    /**
     * 龙币账户
     * 
     * @param userId
     * @param ipaddress
     * @return
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     * @throws JSONException
     */
    public GoldResponse viewUserMoney(Long userId, String ipaddress) throws L99IllegalParamsException,
	    L99IllegalDataException, JSONException;

    /**
     * 消费龙币
     * 
     * @param userId
     * @param amount
     * @param desc
     * @param ipaddress
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     * @throws JSONException
     */
    public Long consumeMoney(Long userId, Double amount, String desc, String ipaddress) throws L99IllegalParamsException,
	    L99IllegalDataException, JSONException;

    /**
     * 赚取龙币
     * 
     * @param userId
     * @param amount
     * @param desc
     * @param ipaddress
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     * @throws JSONException
     */
    public Long earnMoney(Long userId, Double amount, String desc, String ipaddress, Integer moneyType, String moneyData)
	    throws L99IllegalParamsException, L99IllegalDataException, JSONException;

    /**
     * 后台充值龙币
     * 
     * @param userId
     * @param amount
     * @param admin
     * @param ipaddress
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    public void systemRechargeMoney(Long userId, Double amount, String admin, String ipaddress)
	    throws L99IllegalParamsException, L99IllegalDataException;

    /**
     * 后台扣除龙币
     * 
     * @param userId
     * @param amount
     * @param admin
     * @param ipaddress
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    public void systemDeductionMoney(Long userId, Double amount, String admin, String ipaddress)
	    throws L99IllegalParamsException, L99IllegalDataException;
}
