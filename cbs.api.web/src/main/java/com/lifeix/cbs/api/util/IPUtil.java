package com.lifeix.cbs.api.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.exception.service.L99IllegalOperateException;
import com.lifeix.user.beans.SetResponse;

/**
 * ip地址解析
 * 
 * @author lifeix
 * 
 */
public class IPUtil {

    protected static final Logger LOG = LoggerFactory.getLogger(IPUtil.class);

    /**
     * 管理IP段
     */
    private SetResponse<String> manageIps;

    static class SingletonHolder {
	private static final IPUtil INSTANCE = new IPUtil();
    }

    public static IPUtil getInstance() {
	return SingletonHolder.INSTANCE;
    }

    private IPUtil() {

    }

    /**
     * init manageIps
     * 
     * @param manageIps
     */
    public void setManageIps(SetResponse<String> manageIps) {
	this.manageIps = manageIps;
    }

    /**
     * 取得真实地址IP
     * 
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
	String ip = request.getHeader("x-forwarded-for");
	if (ip != null && ip.length() > 0 && !"unknown".equalsIgnoreCase(ip)) {
	    String[] ips = ip.split(",");
	    if (ips.length > 0) {
		ip = ips[0];
	    }
	}
	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	    ip = request.getHeader("Proxy-Client-IP");
	}
	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	    ip = request.getHeader("WL-Proxy-Client-IP");
	}
	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	    ip = request.getRemoteAddr();
	}
	return ip;
    }

    /**
     * filter manage ipadddress
     * 
     * @param ipaddress
     * @throws L99IllegalOperateException
     */
    public void filterManageIPs(String ipaddress) throws L99IllegalOperateException {
	if (ipaddress == null || manageIps == null) {
	    throw new L99IllegalOperateException(BasicMsg.CODE_IPBLOCK, BasicMsg.KEY_IPBLOCK);
	}
	LOG.debug("manage ipaddres operation - " + ipaddress);
	for (String ipReg : manageIps) {
	    Pattern p = Pattern.compile(ipReg);
	    Matcher m = p.matcher(ipaddress);
	    if (m.matches()) {
		return;
	    }
	}
	LOG.info(String.format("ip %s access inner api has block", ipaddress));
	throw new L99IllegalOperateException(BasicMsg.CODE_IPBLOCK, BasicMsg.KEY_IPBLOCK);
    }

}
