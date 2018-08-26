package com.lifeix.cbs.message.service.spark;

import java.util.List;

import com.lifeix.cbs.message.bean.message.PushCountResponse;

public interface PushMessageTaskDubbo {
	/**
	 * 获取消息队列
	 * @param target
	 * @param showData
	 * @return
	 */
	public List<PushCountResponse> findRoiPushs(Integer showData);
}
