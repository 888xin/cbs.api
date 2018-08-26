package com.lifeix.cbs.api.service.money;

import java.util.Date;

import org.json.JSONException;

import com.lifeix.cbs.api.bean.gold.GoldLogListResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;

/**
 * 龙币日志接口
 * 
 */
public interface MoneyLogService {

    /**
     * 查询龙币日志流水
     * 
     * @param userId
     * @param logTypes
     * @param startTime
     * @param endTime
     * @param startId
     * @param limit
     * @return
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     * @throws L99NetworkException 
     * @throws JSONException 
     */
    public GoldLogListResponse findGoldLogList(Long userId, Long userNo, String logTypes, Date startTime, Date endTime, boolean userFlag, Long startId,
	    int limit) throws L99IllegalParamsException, L99IllegalDataException, JSONException, L99NetworkException;
    
    /**
     * 根据id查询
     * @param logId
     * @return
     * @throws L99IllegalDataException
     * @throws JSONException
     * @throws L99IllegalParamsException
     * @throws L99NetworkException
     */
    public GoldLogListResponse findGoldLogById(Long logId) throws L99IllegalDataException, JSONException, L99IllegalParamsException, L99NetworkException;
}
