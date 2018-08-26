package com.lifeix.cbs.api.service.spark.gold;

import org.json.JSONException;

import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

public interface MoneyOrderDubbo {

    /**
     * 完成龙币充值赠送
     * 
     * @param userId
     * @param orderId
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     * @throws JSONException 
     */
    public void finshOrder(Long userId, Long orderId) throws L99IllegalParamsException, L99IllegalDataException, JSONException;
}
