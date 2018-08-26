package com.lifeix.cbs.content.service;

import org.json.JSONException;

import com.lifeix.cbs.content.bean.shicai.ShicaiRecordListResponse;
import com.lifeix.cbs.content.bean.shicai.ShicaiResponse;
import com.lifeix.exception.service.L99IllegalOperateException;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * @author wenhuans 2015年11月4日 上午10:07:38
 * 
 */
public interface ShicaiService {

    /**
     * 获取列表
     * 
     * @author wenhuans 2015年11月4日上午10:09:46
     * @throws L99IllegalParamsException
     * @throws L99IllegalOperateException
     * @throws JSONException 
     * 
     */
    public ShicaiResponse getShicaiList(Long accountId) throws L99IllegalParamsException, L99IllegalOperateException, JSONException;

    /**
     * 用户下单
     * 
     * @author wenhuans 2015年11月4日下午5:41:34
     * @throws L99IllegalParamsException
     * @throws L99IllegalOperateException
     * @throws JSONException 
     * 
     */
    public void toBet(Long accountId, Long scId, String poolOne, String poolTwo, String ip)
	    throws L99IllegalParamsException, L99IllegalOperateException, JSONException;

    /**
     * 开奖记录、下单记录
     * 
     * @param type
     *            (默认0开奖记录，1下单记录)
     * @author wenhuans 2015年11月6日上午9:34:24
     * @throws L99IllegalParamsException
     * @throws JSONException 
     * 
     */
    public ShicaiRecordListResponse getRecordList(Long accountId, Integer type, Long startId, Long endId, Integer limit)
	    throws L99IllegalParamsException, JSONException;
}
