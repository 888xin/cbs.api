package com.lifeix.cbs.api.common.push;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.tiyu.message.service.push.PushCountService;

public class PushMessageUtil {

    private PushMessageUtil() {
	WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
	pushCountService = (PushCountService) context.getBean("pushCountService");
    }

    private static class InstanceHolder {
	/**
	 * Add a private constructor to hide the implicit public one.
	 */
	private InstanceHolder() {
	}

	private static final PushMessageUtil INSTANCE = new PushMessageUtil();
    }

    public static PushMessageUtil getInstance() {
	return InstanceHolder.INSTANCE;
    }

    private PushCountService pushCountService;

    /**
     * 添加push消息
     * 
     * @param userId
     * @return
     */
    public boolean addPushCount(Long userId) {
	DataResponse<Object> ret = pushCountService.addPushCount(userId);
	return ret.getCode() == DataResponse.OK;
    }

    /**
     * 清零count数量
     * 
     * @param userId
     * @return
     */
    public boolean clearCount(Long userId) {
	DataResponse<Object> ret = pushCountService.clearCount(userId);
	return ret.getCode() == DataResponse.OK;

    }

    /**
     * 更新发送消息状态
     * 
     * @param userId
     * @return
     */
    public boolean updateSent(Long userId) {
	DataResponse<Object> ret = pushCountService.updateSent(userId);
	return ret.getCode() == DataResponse.OK;
    }

    public void setPushCountService(PushCountService pushCountService) {
	this.pushCountService = pushCountService;
    }

}
