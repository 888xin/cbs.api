package com.lifeix.cbs.message.impl.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.util.ContentConstants.PushType;
import com.lifeix.cbs.api.common.util.XingePushUtil;
import com.lifeix.cbs.message.bean.message.PushCountResponse;
import com.lifeix.cbs.message.dao.push.CbsPushCountDao;
import com.lifeix.cbs.message.dto.push.CbsPushCount;
import com.lifeix.cbs.message.service.push.PushService;
import com.lifeix.cbs.message.service.spark.PushMessageTaskDubbo;

@Service("pushMessageTaskDubbo")
public class PushMessageTaskDubboImpl extends ImplSupport implements PushMessageTaskDubbo {
    @Autowired
    PushService pushService;
    @Autowired
    CbsPushCountDao cbsPushCountDao;

    @Override
    public List<PushCountResponse> findRoiPushs(Integer showData) {
	long s = System.currentTimeMillis();
	List<PushCountResponse> resps = new ArrayList<PushCountResponse>();
	resps = pushService.findRoiPushs(showData);
	if (resps == null || resps.isEmpty()) {
	    return resps;
	}

	for (PushCountResponse count : resps) {

	    try {
		String activity = "";
		
		Map<String, Object> customContent = new HashMap<String, Object>();
		customContent.put("user_id", count.getUserId());
		customContent.put("msg_num", count.getCount());
		JSONObject params = new JSONObject(count.getParams());
		if(params.has("msg_num")){
		    customContent.put("msg_num", params.getInt("msg_num"));
		}
		
		if(params.has("pk_num") && params.getInt("pk_num")>0){
		    customContent.put("pk_num", params.getInt("pk_num"));
		}
		
		if(params.has("follow_num")){
		    customContent.put("follow_num", params.getInt("follow_num"));
		}
		customContent.put("push_type", PushType.SYSTEM_MSG);
		String content = "您有" +count.getCount()+"条新消息";
		XingePushUtil.getInstance().pushSingleAccount(count.getUserId(), activity, content, customContent);
	    } catch (Exception e) {
		LOG.error(e.getMessage(), e);
	    }

	    
	    CbsPushCount c = new CbsPushCount(count.getPushId(), count.getUserId(), count.getCount(), count.getParams(),
		    count.getCreateTime(), count.getSentTime(), count.getSent());
	    c.setSentTime(new Date());
	    c.setSent(true);
	    cbsPushCountDao.update(c);
	}
	if (resps.size() > 0) {
	    LOG.info(String.format("load counts push size %d - speend %d ms", resps.size(), System.currentTimeMillis() - s));
	}

	return resps;
    }
}
