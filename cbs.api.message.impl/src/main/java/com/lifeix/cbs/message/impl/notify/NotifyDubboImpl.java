package com.lifeix.cbs.message.impl.notify;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.message.service.notify.NotifyService;
import com.lifeix.cbs.message.service.spark.NotifyDubbo;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

@Service("notifyDubbo")
public class NotifyDubboImpl extends ImplSupport implements NotifyDubbo {
    
    @Autowired
    protected NotifyService notifyService;
    
    @Override
    public void handleNotifyData(String notifyData, Integer addType)
            throws L99IllegalParamsException, JSONException, L99IllegalDataException {
	notifyService.handleNotifyData(notifyData, addType);
    }
    
}
