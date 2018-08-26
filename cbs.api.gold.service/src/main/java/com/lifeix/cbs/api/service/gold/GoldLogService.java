package com.lifeix.cbs.api.service.gold;

import java.util.Date;

import org.json.JSONException;

import com.lifeix.cbs.api.bean.gold.GoldLogListResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;

public interface GoldLogService {

    /**
     * 日志列表
     * 
     * @param userId
     * @param startId
     * @param limit
     * @return
     * @throws L99IllegalParamsException
     * @throws JSONException
     * @throws L99IllegalDataException
     */
    public GoldLogListResponse findGoldLogList(Long userId, Long startId, int limit) throws L99IllegalParamsException,
	    L99IllegalDataException, JSONException;

    /**
     * 根据时间查询日志列表
     * 
     * @param userId
     * @param startId
     * @param limit
     * @param time
     * @return
     * @throws L99IllegalParamsException
     * @throws JSONException
     * @throws L99IllegalDataException
     */
    public GoldLogListResponse findGoldLogBytime(Long userId, Long startId, int limit, Date createTime, Date endTime)
	    throws L99IllegalParamsException, L99IllegalDataException, JSONException;

    /**
     * 查询系统充值记录
     * 
     * @param userId
     * @param roleId
     * @param startId
     * @param limit
     * @param beginTime
     * @param endTime
     * @return
     */
    public GoldLogListResponse findSystemGoldLogList(Long userId, Long roleId, Long startId, Integer limit, Date beginTime,
	    Date endTime);

    /**
     * 查看系统详细收入支出日志
     * 
     * @param logId
     * @param LongNo
     * @param startDate
     * @param endDate
     * @param startId
     * @param limit
     * @param types
     * @return
     * @throws L99NetworkException
     * @throws JSONException
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    public GoldLogListResponse findSystemLogsDetail(Long logId, Long LongNo, Date startDate, Date endDate, Long startId,
	    Integer limit, Integer[] types) throws L99IllegalParamsException, JSONException, L99NetworkException,
	    L99IllegalDataException;

    /**
     * 转换用户旧版龙筹余额
     * 
     * @param userId
     */
    public void replaceGold(Long userId);
}
