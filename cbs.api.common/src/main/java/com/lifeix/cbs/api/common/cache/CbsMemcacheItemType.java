package com.lifeix.cbs.api.common.cache;

/**
 * 自定义缓存枚举
 * 
 * @author Peter
 * 
 */
public enum CbsMemcacheItemType {

    AUTHEN_LOGIN("authen_login", "account_id", 7200, "自定义登录缓存");

    private final String keyPrefix;

    private final String primaryKey;

    private final int expire;

    private final String description;

    public String getDescription() {
	return description;
    }

    CbsMemcacheItemType(String keyPrefix, String primaryKey, int expire, String decription) {
	this.keyPrefix = keyPrefix;
	this.primaryKey = primaryKey;
	this.expire = expire;
	this.description = decription;
    }

    public String getKeyPrefix() {
	return keyPrefix;
    }

    public String getPrimaryKey() {
	return primaryKey;
    }

    public int getExpire() {
	return expire;
    }

}
