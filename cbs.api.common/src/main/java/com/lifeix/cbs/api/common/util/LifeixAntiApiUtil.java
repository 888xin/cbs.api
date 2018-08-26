package com.lifeix.cbs.api.common.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.dubbo.rpc.RpcException;
import com.lifeix.anti.sensitiveword.bean.CommentSensitiveWordResponse;
import com.lifeix.anti.sensitiveword.bean.ContentSensitiveWordResponse;
import com.lifeix.anti.sensitiveword.bean.UserSensitiveWordResponse;
import com.lifeix.anti.sensitiveword.service.AntiCommentCheckService;
import com.lifeix.anti.sensitiveword.service.AntiContentCheckService;
import com.lifeix.anti.sensitiveword.service.AntiUserSensitiveWordService;

public class LifeixAntiApiUtil {

    protected static final Logger LOG = LoggerFactory.getLogger(LifeixAntiApiUtil.class);

    private LifeixAntiApiUtil() {
	WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
	antiComment = (AntiCommentCheckService) context.getBean("antiComment");
    }

    private static class InstanceHolder {

	/**
	 * Add a private constructor to hide the implicit public one.
	 */
	private InstanceHolder() {
	}

	private static final LifeixAntiApiUtil INSTANCE = new LifeixAntiApiUtil();
    }

    public static LifeixAntiApiUtil getInstance() {
	return InstanceHolder.INSTANCE;
    }

    private AntiUserSensitiveWordService antiUser;

    private AntiCommentCheckService antiComment;

    private AntiContentCheckService antiContent;

    /**
     * 过滤评论
     * 
     * @param content
     * @return
     */
    public String filterComment(String content) {
	if (StringUtils.isEmpty(content)) {
	    return null;
	}
	try {
	    CommentSensitiveWordResponse result = antiComment.commentCheckInfo(3, content);
	    if (result.isResult()) {
		return result.getSensitiveWord();
	    }
	} catch (RpcException e) {
	    LOG.error(String.format("anti commemt fail - %s", e.getMessage()));
	}

	return null;
    }

    public String filterUser(String content) {
	if (StringUtils.isEmpty(content)) {
	    return null;
	}
	try {
	    UserSensitiveWordResponse result = antiUser.checkUserRegisterInfo(3, content);
	    if (result.isResult()) {
		return result.getSensitiveWord();
	    }
	} catch (RpcException e) {
	    LOG.error(String.format("anti user fail - %s", e.getMessage()));
	}
	return null;
    }

    public String filterContent(String content) {
	if (StringUtils.isEmpty(content)) {
	    return null;
	}
	try {
	    ContentSensitiveWordResponse result = antiContent.contentCheckInfo(3, content);
	    if (result.isResult()) {
		return result.getSensitiveWord();
	    }
	} catch (RpcException e) {
	    LOG.error(String.format("anti conntent fail - %s", e.getMessage()));
	}
	return null;
    }
}
