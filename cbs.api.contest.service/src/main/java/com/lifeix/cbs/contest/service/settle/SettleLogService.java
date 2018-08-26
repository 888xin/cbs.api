package com.lifeix.cbs.contest.service.settle;

import java.util.List;

import org.json.JSONException;

import com.lifeix.cbs.contest.bean.settle.UserSettleLogListResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;

public interface SettleLogService {
    /**
     * 后台查询用户指定日期的结算记录
     * 
     * @param userId
     * @param nowPage
     * @param startTime
     * @param endTime
     * @param limit
     * @return
     * @throws L99IllegalParamsException
     * @throws L99NetworkException 
     * @throws JSONException 
     */
    public UserSettleLogListResponse getUserSettleLogs(Long longNO, Integer nowPage, String startTime, String endTime,
	    Integer limit) throws L99IllegalParamsException, JSONException, L99NetworkException;

    /**
     * 后台查询用户指定日期的结算记录
     * 
     * @param userId
     * @param nowPage
     * @param startTime
     * @param endTime
     * @param limit
     * @return
     * @throws L99IllegalParamsException
     * @throws L99NetworkException 
     * @throws JSONException 
     * @throws L99IllegalDataException 
     */
    public UserSettleLogListResponse getUserSettleLog(Long userId,Integer type, Long contestId, Integer playId, Integer support) throws L99IllegalParamsException, JSONException, L99NetworkException, L99IllegalDataException;

    
    /**
     * 结算日志数据
     * 
     * @param id
     * @param limit
     * @return
     */
    public UserSettleLogListResponse findUserSettleListById(Long id,Boolean isLongbi, int limit);
    
    /**
     * 结算日志数据
     * 
     * @param id
     * @param limit
     * @return
     */
    public UserSettleLogListResponse findUserSettleListByIds(List<Long> id,Boolean isLongbi);

}
