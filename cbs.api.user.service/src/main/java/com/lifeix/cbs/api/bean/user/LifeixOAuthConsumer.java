package com.lifeix.cbs.api.bean.user;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.user.beans.Response;

/**
 * 立方网 Oauth consumer
 * 
 * @author peter
 * 
 */
public class LifeixOAuthConsumer implements Serializable, JsonSerializer<LifeixOAuthConsumer>, Response {

    private static final long serialVersionUID = -5380082430368721490L;

    private Long consumerId;

    /**
     * 用户ID
     */
    private Long accountId;

    /**
     * 请求token
     */
    private String requestToken;

    /**
     * 访问token
     */
    private String accessToken;

    /**
     * 访问密匙
     */
    private String tokenSecret;

    /**
     * 第三方类型
     */
    private String provider;

    /**
     * app类型 0 飞鸽 1 在哪
     */
    private int syncType;

    private Date createTime;

    private Date updateTime;

    /**
     * 第三方用户数据
     */
    private String data;

    /**
     * 同步选项
     */
    private String feedStr;

    /**
     * 第三方用户名
     */
    private String username;

    /**
     * 第三方用户头像
     */
    private String userhead;

    /**
     * 第三方主页链接
     */
    private String userlink;

    /**
     * token超时
     */
    private Integer expiresIn;

    /**
     * 第三方账号标识
     */
    private String usercode;

    /**
     * 失效标识
     */
    private String oauthInfo;

    private Set<String> feedTypes;

    /**
     * 性别，第三方获取用户信息时暂存
     */
    private int gender;

    public LifeixOAuthConsumer() {
	Date now = new Date();
	setCreateTime(now);
	setUpdateTime(now);
    }

    public LifeixOAuthConsumer(Long consumerId, Long accountId, String requestToken, String accessToken, String tokenSecret,
	    String provider, int syncType, Date createTime, Date updateTime, String data, String feedStr, String username,
	    String userhead, String userlink, Integer expiresIn, String usercode, String oauthInfo) {
	super();
	this.consumerId = consumerId;
	this.accountId = accountId;
	this.requestToken = requestToken;
	this.accessToken = accessToken;
	this.tokenSecret = tokenSecret;
	this.provider = provider;
	this.syncType = syncType;
	this.createTime = createTime;
	this.updateTime = updateTime;
	this.data = data;
	this.feedStr = feedStr;
	this.username = username;
	this.userhead = userhead;
	this.userlink = userlink;
	this.expiresIn = expiresIn;
	this.usercode = usercode;
	this.oauthInfo = oauthInfo;
    }

    public Long getConsumerId() {
	return consumerId;
    }

    public void setConsumerId(Long consumerId) {
	this.consumerId = consumerId;
    }

    public Long getAccountId() {
	return accountId;
    }

    public void setAccountId(Long accountId) {
	this.accountId = accountId;
    }

    public String getRequestToken() {
	return requestToken;
    }

    public void setRequestToken(String requestToken) {
	this.requestToken = requestToken;
    }

    public String getAccessToken() {
	return accessToken;
    }

    public void setAccessToken(String accessToken) {
	this.accessToken = accessToken;
    }

    public String getTokenSecret() {
	return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
	this.tokenSecret = tokenSecret;
    }

    public String getProvider() {
	return provider;
    }

    public void setProvider(String provider) {
	this.provider = provider;
    }

    public int getSyncType() {
	return syncType;
    }

    public void setSyncType(int syncType) {
	this.syncType = syncType;
    }

    public Date getCreateTime() {
	return createTime;
    }

    public void setCreateTime(Date createTime) {
	this.createTime = createTime;
    }

    public Date getUpdateTime() {
	return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
	this.updateTime = updateTime;
    }

    public String getData() {
	return data;
    }

    public void setData(String data) {
	this.data = data;
    }

    public String getFeedStr() {
	return feedStr;
    }

    public void setFeedStr(String feedStr) {
	this.feedStr = feedStr;
	if (feedStr != null) {
	    String[] feeds = feedStr.split(",");
	    if (feeds != null && feeds.length > 0) {
		feedTypes = new HashSet<String>();
		for (String f : feeds) {
		    if (StringUtils.isNotEmpty(f)) {
			feedTypes.add(f);
		    }
		}
	    }
	}
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public String getUserhead() {
	return userhead;
    }

    public void setUserhead(String userhead) {
	this.userhead = userhead;
    }

    public String getUserlink() {
	return userlink;
    }

    public void setUserlink(String userlink) {
	this.userlink = userlink;
    }

    public Integer getExpiresIn() {
	return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
	this.expiresIn = expiresIn;
    }

    public String getUsercode() {
	return usercode;
    }

    public void setUsercode(String usercode) {
	this.usercode = usercode;
    }

    public String getOauthInfo() {
	return oauthInfo;
    }

    public void setOauthInfo(String oauthInfo) {
	this.oauthInfo = oauthInfo;
    }

    public Set<String> getFeedTypes() {
	return feedTypes;
    }

    public int getGender() {
	return gender;
    }

    public void setGender(int gender) {
	this.gender = gender;
    }

    /**
     * 是否失效.
     */
    public boolean isExpire() {
	if (StringUtils.isEmpty(oauthInfo)) {
	    return false;
	}
	return this.oauthInfo.contains("\"expire\":true");
    }

    @Override
    public String getObjectName() {
	return "consumer";
    }

    @Override
    public JsonElement serialize(LifeixOAuthConsumer src, Type arg1, JsonSerializationContext context) {
	return context.serialize(src);
    }

}
