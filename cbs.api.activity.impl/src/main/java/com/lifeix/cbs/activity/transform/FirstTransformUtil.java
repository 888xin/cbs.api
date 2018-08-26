package com.lifeix.cbs.activity.transform;

import com.lifeix.cbs.activity.bean.first.ActivityFirstResponse;
import com.lifeix.cbs.activity.dto.first.ActivityFirstLog;
import com.lifeix.cbs.api.common.util.CbsTimeUtils;

/**
 * DTO 转换为 VO 工具类
 * 
 * @author jacky
 * 
 */
public class FirstTransformUtil {

    /**
     * 首充
     * 
     * @param gold
     * @return
     */
    public static ActivityFirstResponse transformFirstLog(ActivityFirstLog bean) {
	ActivityFirstResponse resp = null;
	if (bean != null) {
	    resp = new ActivityFirstResponse();
	    resp.setUserId(bean.getUserId());
	    resp.setAmount(bean.getAmount());
	    resp.setReward(bean.getReward());
	    resp.setRule(bean.getRule());
	    resp.setCreateTime(CbsTimeUtils.getUtcTimeForDate(bean.getCreateTime()));
	}
	return resp;
    }

}
