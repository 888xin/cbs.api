package com.lifeix.cbs.api.common.cache;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;

import com.lifeix.framework.memcache.MemcacheService;

public class MemcachedHelper {
	
	final static Logger LOG = LoggerFactory.getLogger(MemcachedHelper.class);

	private MemcacheService memcacheService;

	private MemcachedHelper() {
		memcacheService = (MemcacheService) ContextLoader.getCurrentWebApplicationContext().getBean("memcacheService");
	}

	public static MemcachedHelper getInstance() {
		return InstanceHolder.INSTANCE;
	}

	private static final class InstanceHolder {
		private static MemcachedHelper INSTANCE = new MemcachedHelper();
	}
	
	/**
	 * 添加自定义缓存
	 * @param key
	 * @param value
	 * @param itemType
	 * @return
	 */
	public boolean setCache(Object key, Object value, CbsMemcacheItemType itemType){
		return memcacheService.set(buildKey(itemType, key), value, itemType.getExpire());
	}
	
	/**
	 * 删除自定义缓存
	 * @param key
	 * @param itemType
	 */
	public void deleteCache(Object key, CbsMemcacheItemType itemType){
		memcacheService.delete(buildKey(itemType, key));
	}
	
	/**
	 * 获取自定义缓存
	 * @param key
	 * @return
	 */
	public <T> T getCache(Object key, CbsMemcacheItemType itemType) {
		try {
		    return memcacheService.get(buildKey(itemType, key));
		} catch (Exception e) {
			LOG.error("error get key:" + buildKey(itemType, key), e);
		    return null;
		}
	}
	
	/**
	 * 生产缓存key
	 * @param prefix
	 * @param itemType
	 * @param specificKey
	 * @return
	 */
	public static String buildKey(CbsMemcacheItemType itemType , Object key) {
		String cacheKey = KeyGenerator.generateSimpleKey(itemType.getKeyPrefix() + itemType.getPrimaryKey() + key.toString());
		if(cacheKey.length() >= 250){
			LOG.warn("the key is to long ===== "+key);
		}
		return cacheKey;
	}

}
