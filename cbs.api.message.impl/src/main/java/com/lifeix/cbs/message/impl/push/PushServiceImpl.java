package com.lifeix.cbs.message.impl.push;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.message.bean.message.PushCountResponse;
import com.lifeix.cbs.message.dao.push.CbsPushCountDao;
import com.lifeix.cbs.message.dto.push.CbsPushCount;
import com.lifeix.cbs.message.service.push.PushService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * 消息Push队列
 * @author Peter
 *
 */
@Service("pushService")
public class PushServiceImpl extends ImplSupport implements  PushService{
    @Autowired
    CbsPushCountDao cbsPushCountDao;
	
	
	/**
	 * 更新统计
	 * @param pushType
	 * @param pushData
	 * @param target
	 * @param msg
	 * @param updateFlag
	 * @throws L99IllegalParamsException
	 * @throws L99IllegalDataException
	 */
	public void pushCount(Long userId, Integer count, String params) throws L99IllegalParamsException, L99IllegalDataException{
		
		ParamemeterAssert.assertDataNotNull(userId, count, params);
		
		
		CbsPushCount pushCount = new CbsPushCount();

		pushCount.setUserId(userId);
		pushCount.setCount(count);
		pushCount.setParams(params);
		pushCount.setCreateTime(new Date());
		pushCount.setSent(false);
		
		boolean flag = cbsPushCountDao.replace(pushCount);

		if(!flag){
			throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
		}
	}


	@Override
        public List<PushCountResponse> findRoiPushs(Integer showData) {
	    List<PushCountResponse> resps = new ArrayList<PushCountResponse>();
	    List<CbsPushCount> counts = cbsPushCountDao.findRoiPushs(showData);
	    if(counts==null || counts.isEmpty()){
		return resps;
	    }
	    for(CbsPushCount c : counts){
		PushCountResponse resp = new PushCountResponse(c.getPushId(), c.getUserId(), c.getCount(), c.getParams(),
			    c.getCreateTime(), c.getSentTime(), c.getSent());
		resps.add(resp);
	    }
	   return resps;
        }
}
