package com.lifeix.cbs.api.action;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lifeix.cbs.api.util.IPUtil;

public class BaseAction {

    protected static Logger LOG = LoggerFactory.getLogger(BaseAction.class);

    final static long WARNING_THRESHOLD = 1000L;
    /**
     * request
     */
    @Context
    protected HttpServletRequest request;

    long time = 0;

    protected void start() {
	time = System.currentTimeMillis();
    }

    /**
     * 将api返回超过1s的方法打印警告日志
     */
    protected void end() {
	long interval = System.currentTimeMillis() - time;

	StackTraceElement stack[] = (new Throwable()).getStackTrace();
	if (stack.length > 1) {
	    String ipAddres = IPUtil.getIpAddr(request);
	    if (interval > WARNING_THRESHOLD) {
		LOG.warn(String.format("%s - %s.%s() - time opert %d ms", ipAddres, stack[1].getClassName(),
		        stack[1].getMethodName(), System.currentTimeMillis() - time));
	    } else {
		LOG.info(String.format("%s - %s.%s() - time opert %d ms", ipAddres, stack[1].getClassName(),
		        stack[1].getMethodName(), System.currentTimeMillis() - time));
	    }
	}

    }

    /**
     * 取得当前请求的userId
     * 
     * @param request
     * @return
     */
    public Long getSessionAccount(HttpServletRequest request) {
	Object sessionAccount = request.getAttribute("account_id");
	if (sessionAccount == null) {
	    return null;
	}
	return Long.valueOf(sessionAccount.toString());
    }

}
