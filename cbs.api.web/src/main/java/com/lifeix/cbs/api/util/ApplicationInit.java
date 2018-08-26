package com.lifeix.cbs.api.util;

import com.lifeix.cbs.api.common.solr.CbsSolrUtil;
import com.lifeix.cbs.contest.im.LifeixImApiUtil;
import com.lifeix.cbs.contest.util.CbsContestSolrUtil;
import com.lifeix.cbs.contest.util.WeixinNotifyUtil;
import com.lifeix.cbs.mall.impl.util.KdniaoSearchUtil;
import com.lifeix.user.beans.SetResponse;

/**
 * 全局变量初始化
 * 
 * @author lifeix-sz
 * 
 */
public class ApplicationInit {

    /**
     * 管理IP段
     */
    private String manageIPs;

    /**
     * solr服务
     */
    private String solrUri;

    /**
     * IM服务地址
     */
    private String imUri;

    /**
     * 大赢家web项目地址
     */
    private String cbsWebUri;

    private String eBusinessID;

    private String appKey;

    public void init() {

	// 后台IP地址初始化
	SetResponse<String> ipSet = new SetResponse<String>();
	String[] ips = manageIPs.split(",");
	for (String ip : ips) {
	    ipSet.add(ip.replace(".", "\\.").replace("*", "(\\d+)"));
	}
	IPUtil.getInstance().setManageIps(ipSet);

	// 搜索服务初始化
	CbsSolrUtil.getInstance().initData(solrUri);
	CbsContestSolrUtil.getInstance().initData(solrUri);

	// IM 服务初始化
	LifeixImApiUtil.getInstance().initData(imUri);

	// 快递鸟参数初始化
	KdniaoSearchUtil.getInstance().initData(eBusinessID, appKey);

	// 设置大赢家web项目地址
	WeixinNotifyUtil.getInstance().setCbsWebUri(cbsWebUri);

    }

    public void setManageIPs(String manageIPs) {
	this.manageIPs = manageIPs;
    }

    public void setSolrUri(String solrUri) {
	this.solrUri = solrUri;
    }

    public String getImUri() {
	return imUri;
    }

    public void setImUri(String imUri) {
	this.imUri = imUri;
    }

    public void seteBusinessID(String eBusinessID) {
	this.eBusinessID = eBusinessID;
    }

    public void setAppKey(String appKey) {
	this.appKey = appKey;
    }

    public void setCbsWebUri(String cbsWebUri) {
	this.cbsWebUri = cbsWebUri;
    }

}
