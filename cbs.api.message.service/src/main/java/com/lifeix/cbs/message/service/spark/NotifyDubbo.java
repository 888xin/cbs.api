package com.lifeix.cbs.message.service.spark;

import org.json.JSONException;

import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

public interface NotifyDubbo {
    
    /**
     * 外部系统调用添加消息提醒
     * 
     * @param notifyData
     * @param batchFlag
     * @throws L99IllegalParamsException
     * @throws JSONException
     * @throws L99IllegalDataException
     */
    public void handleNotifyData(String notifyData, Integer addType)
            throws L99IllegalParamsException, JSONException, L99IllegalDataException;
	    
}
