package com.lifeix.cbs.message.service.notify;

import org.json.JSONException;

import com.lifeix.cbs.message.bean.notify.NotifyListResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.user.beans.CustomResponse;

public interface NotifyService {

    /**
     * 外部系统调用添加消息提醒
     * 
     * @param notifyData
     * @param batchFlag
     * @throws L99IllegalParamsException
     * @throws JSONException
     * @throws L99IllegalDataException
     */
    public void handleNotifyData(String notifyData, Integer addType) throws L99IllegalParamsException, JSONException,
	    L99IllegalDataException;

    /**
     * 单次添加消息提醒
     * 
     * @param templetId
     * @param userId
     * @param targetId
     * @param params
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    public void addNotify(Long templetId, Long userId, Long targetId, String params, Long skipId)
	    throws L99IllegalParamsException, L99IllegalDataException;

    /**
     * 消息数量的集合
     * 
     * @param userId
     * @param type
     * @return
     * @throws L99IllegalParamsException
     */
    public CustomResponse getUnreadCount(Long userId) throws L99IllegalParamsException;

    /**
     * 分页获取消息列表(时间倒排序)
     * 
     * @param startId
     * @param endId
     * @param userId
     * @param type
     * @param limit
     * @return
     * @throws L99IllegalParamsException
     * @throws JSONException
     */
    public NotifyListResponse getNotifies(Long startId, Long endId, Long userId, Integer type, Integer limit, Boolean forWeb)
	    throws L99IllegalParamsException, JSONException;

    /**
     * 获取未读消息数量
     * 
     * @param userId
     * @return
     */
    public int getUnreadNotify(Long userId, int type);

    /**
     * 设定未读消息为已读
     * 
     * @param userId
     */
    public void updateUnreadNotify(Long userId, Integer type);
}
