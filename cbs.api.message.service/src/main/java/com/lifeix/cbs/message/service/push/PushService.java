package com.lifeix.cbs.message.service.push;

import java.util.List;

import com.lifeix.cbs.message.bean.message.PushCountResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

public interface PushService {
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
	public void pushCount(Long userId, Integer count, String params) throws L99IllegalParamsException, L99IllegalDataException;
	
	/**
	 * 获取消息队列
	 * @param target
	 * @param showData
	 * @return
	 */
	public List<PushCountResponse> findRoiPushs(Integer showData);
}
