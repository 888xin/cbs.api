package com.lifeix.cbs.api.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.common.cache.CbsMemcacheItemType;
import com.lifeix.cbs.api.common.cache.MemcachedHelper;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.exception.MsgCode.UserTokenMsg;
import com.lifeix.cbs.api.common.user.LifeixUserApiUtil;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.common.utils.CipherUtil;
import com.lifeix.exception.service.L99IllegalOperateException;
import com.lifeix.exception.service.L99NetworkException;
import com.lifeix.user.api.action.account.AccountAuthAction;
import com.lifeix.user.beans.account.AccountResponse;

public class BasicAuthenticationFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(BasicAuthenticationFilter.class);

    private static Set<String> PASSURLS = new HashSet<String>();

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
	HttpServletRequest request = (HttpServletRequest) servletRequest;
	HttpServletResponse response = (HttpServletResponse) servletResponse;
	response.setHeader("Access-Control-Allow-Origin", "*");
	response.setCharacterEncoding("UTF-8");
	response.setHeader("Cache-Control", "no-cache");
	String ipaddress = IPUtil.getIpAddr(request);
	String requestURI = request.getRequestURI();
	try {
	    // 管理接口进行IP验证 只允许内部服务器访问
	    if (requestURI.indexOf("/inner/") >= 0) {
		IPUtil.getInstance().filterManageIPs(ipaddress);
		chain.doFilter(servletRequest, servletResponse);
		return;
	    }
	} catch (L99IllegalOperateException e) {
	    DataResponse<String> ret = new DataResponse<String>();
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	    response.getWriter().print(DataResponseFormat.response(ret));
	    return;
	}

	String authorization = request.getHeader("authorization");
	String tokenHead = request.getHeader("token");
	if (StringUtils.isBlank(authorization) && StringUtils.isBlank(tokenHead)
	        && PASSURLS.contains(request.getRequestURI())) {
	    chain.doFilter(servletRequest, servletResponse);
	    return;
	}
	if (StringUtils.isBlank(authorization) && StringUtils.isBlank(tokenHead)) {
	    DataResponse<Object> ret = new DataResponse<Object>();
	    ret.setCode(Integer.valueOf(BasicMsg.CODE_AUTHORIZATIONFAIL));
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_AUTHORIZATIONFAIL, request.getLocale()));
	    response.getWriter().print(DataResponseFormat.response(ret));
	    return;
	}

	try {
	    Long accountId = null;
	    if (StringUtils.isNotBlank(authorization)) {
		// 查询是否缓存过登录
		Map<String, String> map = resolveAuthorization(authorization);
		accountId = MemcachedHelper.getInstance().getCache(map.get("username"), CbsMemcacheItemType.AUTHEN_LOGIN);

		if (accountId == null) {
		    // 返回null则表示没有缓存或失效 需重新登录
		    AccountResponse account = LifeixUserApiUtil.getInstance().authorizationUser(authorization, ipaddress);

		    accountId = account.getAccountId();

		    // 设置登录缓存 有效时间2个小时
		    MemcachedHelper.getInstance().setCache(map.get("username"), accountId, CbsMemcacheItemType.AUTHEN_LOGIN);
		}
	    } else if (StringUtils.isNotBlank(tokenHead)) {
		AccountAuthAction accountAuthAction = (AccountAuthAction) ContextLoader.getCurrentWebApplicationContext()
		        .getBean("accountAuthAction");
		String[] arr = tokenHead.split(",");
		String token = arr[0];
		accountId = accountAuthAction.getUserIdByToken(token);

		// 是需要验证的接口并且token无效
		if (accountId == null && !PASSURLS.contains(request.getRequestURI())) {
		    accountId = Long.parseLong(arr[1]);

		    String currentToken = accountAuthAction.getValidToken(accountId);
		    if (token.equals(currentToken)) {
			DataResponse<Object> ret = new DataResponse<Object>();
			ret.setCode(Integer.valueOf(UserTokenMsg.CODE_TOKEN_NOT_EXSIST));
			ret.setMsg(InternationalResources.getInstance().locale(UserTokenMsg.KEY_TOKEN_NOT_EXSIST,
			        request.getLocale()));
			response.getWriter().print(DataResponseFormat.response(ret));
			return;
		    } else {
			DataResponse<Object> ret = new DataResponse<Object>();
			ret.setCode(Integer.valueOf(UserTokenMsg.CODE_TOKEN_ERROR));
			ret.setMsg(InternationalResources.getInstance().locale(UserTokenMsg.KEY_TOKEN_ERROR,
			        request.getLocale()));
			response.getWriter().print(DataResponseFormat.response(ret));
			return;
		    }

		}
	    }

	    request.setAttribute("account_id", accountId);
	} catch (L99NetworkException e) {
	    DataResponse<Object> ret = new DataResponse<Object>();
	    ret.setCode(Integer.valueOf(BasicMsg.CODE_AUTHORIZATIONFAIL));
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_AUTHORIZATIONFAIL, request.getLocale()));
	    response.getWriter().print(DataResponseFormat.response(ret));
	    return;
	} catch (L99IllegalOperateException e) {
	    DataResponse<Object> ret = new DataResponse<Object>();
	    ret.setCode(Integer.valueOf(BasicMsg.CODE_AUTHORIZATIONFAIL));
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_AUTHORIZATIONFAIL, request.getLocale()));
	    response.getWriter().print(DataResponseFormat.response(ret));
	    return;
	} catch (Exception e) {
	    String client = request.getHeader("client");
	    LOG.info(String.format("client:%s | token:%s | auth:%s", client, tokenHead, authorization));
	    LOG.error(e.getMessage(), e);
	    DataResponse<Object> ret = new DataResponse<Object>();
	    ret.setCode(Integer.valueOf(BasicMsg.CODE_AUTHORIZATIONFAIL));
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_AUTHORIZATIONFAIL, request.getLocale()));
	    response.getWriter().print(DataResponseFormat.response(ret));
	    return;
	}

	chain.doFilter(servletRequest, servletResponse);
	return;
    }

    /**
     * 解析出龙号
     * 
     * @return
     * @throws L99IllegalOperateException
     */
    private Map<String, String> resolveAuthorization(String authorization) throws L99IllegalOperateException {
	if (authorization == null) {
	    return null;
	}
	Map<String, String> map = new HashMap<String, String>();

	CipherUtil cipherUtil = CipherUtil.getInstance();
	String data = cipherUtil.base64Decode(authorization.split(" ")[1]);
	String[] keys = data.split(":");
	String username = cipherUtil.aesDecode(keys[0]);
	String code = cipherUtil.aesDecode(keys[1]);
	if (code == null) {
	    throw new L99IllegalOperateException(BasicMsg.CODE_AUTHORIZATIONFAIL, BasicMsg.KEY_AUTHORIZATIONFAIL);
	}
	String[] codes = code.split(":");
	if (codes.length < 2) {
	    throw new L99IllegalOperateException(BasicMsg.CODE_AUTHORIZATIONFAIL, BasicMsg.KEY_AUTHORIZATIONFAIL);
	}
	String password = codes[0];
	map.put("username", username);
	map.put("password", password);
	return map;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
	LOG.debug("init no basic authentication url");

	String contextPath = filterConfig.getServletContext().getContextPath();
	// 不需要通过验证
	PASSURLS.add(contextPath + "/");
	PASSURLS.add(contextPath + "/cbs/user/get");
	PASSURLS.add(contextPath + "/cbs/user/code");
	PASSURLS.add(contextPath + "/cbs/user/code/check");
	PASSURLS.add(contextPath + "/cbs/user/third");
	PASSURLS.add(contextPath + "/cbs/user/login");
	PASSURLS.add(contextPath + "/cbs/user/edit");
	PASSURLS.add(contextPath + "/cbs/user/find");
	PASSURLS.add(contextPath + "/cbs/user/register");
	PASSURLS.add(contextPath + "/cbs/user/auth");
	PASSURLS.add(contextPath + "/cbs/user/replace/token");
	PASSURLS.add(contextPath + "/cbs/user/wxmp/login");

	// 咨询
	PASSURLS.add(contextPath + "/cbs/content/list");
	PASSURLS.add(contextPath + "/cbs/content/view");

	// 体育头条评论
	PASSURLS.add(contextPath + "/cbs/content/comment/add");
	PASSURLS.add(contextPath + "/cbs/content/comment/reply");
	PASSURLS.add(contextPath + "/cbs/content/comment/like");
	PASSURLS.add(contextPath + "/cbs/content/comment/list");
	PASSURLS.add(contextPath + "/cbs/content/comment/user");
	PASSURLS.add(contextPath + "/cbs/content/comment/remind");
	PASSURLS.add(contextPath + "/cbs/content/comment/num");

	// 个人空间
	PASSURLS.add(contextPath + "/cbs/user/space");
	PASSURLS.add(contextPath + "/cbs/user/continueLogin");
	PASSURLS.add(contextPath + "/cbs/rank/winning/top");
	PASSURLS.add(contextPath + "/cbs/gold/log/list");
	PASSURLS.add(contextPath + "/cbs/money/log/list");
	PASSURLS.add(contextPath + "/cbs/login/continue");

	PASSURLS.add(contextPath + "/cbs/relationship/view");

	// 修改用户密码
	PASSURLS.add(contextPath + "/cbs/user/reset/findMobilePassword");
	PASSURLS.add(contextPath + "/cbs/user/reset/resetPassword");
	PASSURLS.add(contextPath + "/cbs/user/reset/isPasswordCodeValidate");

	PASSURLS.add(contextPath + "/cbs/user/third/code");
	PASSURLS.add(contextPath + "/cbs/user/third/code/check");

	// 足球赛事
	PASSURLS.add(contextPath + "/cbs/fb/contest/time");
	PASSURLS.add(contextPath + "/cbs/fb/contest/score");
	PASSURLS.add(contextPath + "/cbs/fb/contest/score/final");
	PASSURLS.add(contextPath + "/cbs/fb/odds/contest");
	PASSURLS.add(contextPath + "/cbs/fb/contest/info");
	PASSURLS.add(contextPath + "/cbs/fb/contest/fixed");
	PASSURLS.add(contextPath + "/cbs/fb/contest/team");
	PASSURLS.add(contextPath + "/cbs/fb/contest/bycupid");

	// 新的足球列表，取多天比赛
	PASSURLS.add(contextPath + "/cbs/fb/contest/list");

	// 足球文字直播
	PASSURLS.add(contextPath + "/cbs/fb/contest/livewords");
	// 篮球文字直播
	PASSURLS.add(contextPath + "/cbs/bb/contest/livewords");
	// 篮球变动大的信息
	PASSURLS.add(contextPath + "/cbs/bb/contest/change");
	// 技术统计
	PASSURLS.add(contextPath + "/cbs/fb/contest/statistics");
	// 赛事赛况
	PASSURLS.add(contextPath + "/cbs/fb/contest/events");
	PASSURLS.add(contextPath + "/cbs/fb/contest/statisticsandevents");

	// 真正进行比赛数
	PASSURLS.add(contextPath + "/cbs/fb/contest/fbing");

	PASSURLS.add(contextPath + "/cbs/fb/contest/search");

	// h5页面调用接口,返回单篇赛事
	PASSURLS.add(contextPath + "/cbs/bb/contest/bbone");
	PASSURLS.add(contextPath + "/cbs/fb/contest/fbone");

	PASSURLS.add(contextPath + "/cbs/bb/contest/bbing");

	// 篮球赛事
	PASSURLS.add(contextPath + "/cbs/bb/contest/time");
	PASSURLS.add(contextPath + "/cbs/bb/contest/score");
	PASSURLS.add(contextPath + "/cbs/bb/odds/contest");
	PASSURLS.add(contextPath + "/cbs/bb/contest/search");
	PASSURLS.add(contextPath + "/cbs/bb/contest/fixed");

	// 根据房间id找到篮球赛事
	PASSURLS.add(contextPath + "/cbs/bb/contest/room/contest");
	PASSURLS.add(contextPath + "/cbs/fb/contest/room/contest");

	// 新的篮球赛事列表，获取多天比赛
	PASSURLS.add(contextPath + "/cbs/bb/contest/list");
	// 根据主客队获取赛事
	PASSURLS.add(contextPath + "/cbs/bb/contest/team");

	// 排行榜
	PASSURLS.add(contextPath + "/cbs/rank/user/graph");
	PASSURLS.add(contextPath + "/cbs/rank/top");
	PASSURLS.add(contextPath + "/cbs/rank/winning/top");
	PASSURLS.add(contextPath + "/cbs/rank/winning/week");
	PASSURLS.add(contextPath + "/cbs/rank/roi/top");
	PASSURLS.add(contextPath + "/cbs/rank/roi/week");
	PASSURLS.add(contextPath + "/cbs/rank/setting");

	// 猜友圈
	PASSURLS.add(contextPath + "/cbs/friend/circle/time");
	PASSURLS.add(contextPath + "/cbs/friend/circle/view");
	PASSURLS.add(contextPath + "/cbs/friend/circle/reason");
	// 我的下单记录
	PASSURLS.add(contextPath + "/cbs/friend/circle/my");
	// 猜友圈评论
	PASSURLS.add(contextPath + "/cbs/friend/circle/comment/add");
	PASSURLS.add(contextPath + "/cbs/friend/circle/comment/list/detail");
	PASSURLS.add(contextPath + "/cbs/friend/circle/comment/list/unread");
	PASSURLS.add(contextPath + "/cbs/friend/circle/comment/list/history");
	PASSURLS.add(contextPath + "/cbs/friend/circle/comment/single/counts");
	PASSURLS.add(contextPath + "/cbs/friend/circle/comment/list");
	PASSURLS.add(contextPath + "/cbs/friend/circle/comment/shield");
	PASSURLS.add(contextPath + "/cbs/friend/circle/comment/delete");

	// 赛事评论
	PASSURLS.add(contextPath + "/cbs/comment/list");
	PASSURLS.add(contextPath + "/cbs/comment/send");

	// 商品
	PASSURLS.add(contextPath + "/cbs/goods/view");
	PASSURLS.add(contextPath + "/cbs/goods/list");
	PASSURLS.add(contextPath + "/cbs/goods/head/view");
	PASSURLS.add(contextPath + "/cbs/mall/order/add");
	PASSURLS.add(contextPath + "/cbs/mall/order/express");

	// 商品导航
	PASSURLS.add(contextPath + "/cbs/mall/recommend/list");

	// 头版
	PASSURLS.add(contextPath + "/cbs/frontpage/list");

	// 举报
	PASSURLS.add(contextPath + "/cbs/inform/comment/add");
	PASSURLS.add(contextPath + "/cbs/inform/content/add");
	PASSURLS.add(contextPath + "/cbs/inform/im/add");
	PASSURLS.add(contextPath + "/cbs/inform/user/add");
	PASSURLS.add(contextPath + "/cbs/inform/news/comment/add");
	PASSURLS.add(contextPath + "/cbs/inform/news/add");

	// 生肖游戏
	PASSURLS.add(contextPath + "/cbs/zodiac/open");
	PASSURLS.add(contextPath + "/cbs/zodiac/change");
	PASSURLS.add(contextPath + "/cbs/zodiac/get/number");
	PASSURLS.add(contextPath + "/cbs/zodiac/get/win");
	PASSURLS.add(contextPath + "/cbs/zodiac/get/past");

	// pk
	PASSURLS.add(contextPath + "/cbs/pk/outer/accept");

	// 押押
	PASSURLS.add(contextPath + "/cbs/yy/contest/cups");
	PASSURLS.add(contextPath + "/cbs/yy/contest/list");
	PASSURLS.add(contextPath + "/cbs/yy/contest/odds");
	PASSURLS.add(contextPath + "/cbs/yy/contest/history");
	PASSURLS.add(contextPath + "/cbs/yy/contest/bet/view");

	// 交手记录盘面分析
	PASSURLS.add(contextPath + "/cbs/fb/contest/record");
	PASSURLS.add(contextPath + "/cbs/bb/contest/record");

	// 商品接口
	PASSURLS.add(contextPath + "/cbs/mall/category/all");
	PASSURLS.add(contextPath + "/cbs/mall/category/recommend");
	PASSURLS.add(contextPath + "/cbs/mall/goods/view");
	PASSURLS.add(contextPath + "/cbs/mall/goods/category");
	PASSURLS.add(contextPath + "/cbs/mall/recommend/list");
	PASSURLS.add(contextPath + "/cbs/mall/goods/index");

	// 活动
	PASSURLS.add(contextPath + "/cbs/activity/first/rewardlog");
	PASSURLS.add(contextPath + "/cbs/activity/first/lottery");
	PASSURLS.add(contextPath + "/cbs/activity/first/check");

	PASSURLS.add(contextPath + "/cbs/activity/first/check");

	// 充值卡列表
	PASSURLS.add(contextPath + "/cbs/money/order/cardlist");
	// 广告位
	PASSURLS.add(contextPath + "/cbs/boot/list");
	PASSURLS.add(contextPath + "/cbs/boot/curr");
	PASSURLS.add(contextPath + "/cbs/boot/view");

	// 渠道登录
	PASSURLS.add(contextPath + "/cbs/market/login");
	// 渠道列表
	PASSURLS.add(contextPath + "/cbs/market/list");

	// 活动串
	PASSURLS.add(contextPath + "/cbs/bunch/list");
	PASSURLS.add(contextPath + "/cbs/bunch/awards");
	PASSURLS.add(contextPath + "/cbs/bunch/view");

	// 联赛列表
	PASSURLS.add(contextPath + "/cbs/fb/cup/list");
	PASSURLS.add(contextPath + "/cbs/bb/cup/list");

	PASSURLS.add(contextPath + "/cbs/achieve/view/one");

	// 根据主队客队id查找相关对阵
	PASSURLS.add(contextPath + "/cbs/bb/contest/about/record");
	PASSURLS.add(contextPath + "/cbs/fb/contest/about/record");

	// 获取三十天内的足球联赛列表缓存
	PASSURLS.add(contextPath + "/cbs/fb/cup/valid");
	PASSURLS.add(contextPath + "/cbs/bb/cup/valid");

	// 赛事广告
	PASSURLS.add(contextPath + "/cbs/all/contest/ad/list");
	LOG.debug("dove init url, has " + PASSURLS.size() + " url.");
    }

}
