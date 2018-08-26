package com.lifeix.cbs.api.common.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DaoSupport {

    protected final static Logger LOG = LoggerFactory.getLogger(DaoSupport.class);

    /**
     * dto缓存的id
     * 
     * @param id
     * @return
     */
    protected String getCacheId(Object id) {
	return String.format("%s:id:%s", this.getClass().getName(), id.toString());
    }

    /**
     * 自定义缓存的id
     * 
     * @param prefix
     * @param key
     * @return
     */
    protected String getCustomCache(String prefix, Object... objs) {
	if (objs == null) {
	    return String.format("%s:%s", this.getClass().getName(), prefix);
	} else {
	    StringBuilder keyBuf = new StringBuilder();
	    for (Object obj : objs) {
		if (keyBuf.length() > 0) {
		    keyBuf.append("-");
		}
		if (obj == null) {
		    keyBuf.append("null");
		} else if (obj instanceof Long) {
		    keyBuf.append(((Long) obj).toString());
		} else if (obj instanceof Integer) {
		    keyBuf.append(((Integer) obj).toString());
		} else if (obj instanceof Double) {
		    keyBuf.append(((Double) obj).toString());
		} else if (obj instanceof Float) {
		    keyBuf.append(((Float) obj).toString());
		} else if (obj instanceof Boolean) {
		    keyBuf.append(((Boolean) obj).toString());
		} else if (obj instanceof String) {
		    keyBuf.append(((String) obj).toString());
		} else {
		    keyBuf.append(obj.toString());
		}
	    }
	    return String.format("%s:%s:%s", this.getClass().getName(), prefix, keyBuf.toString().trim());
	}
    }

    /**
     * dto缓存的批量id列表
     * 
     * @param <T>
     * @param ids
     * @return
     */
    protected <T> List<String> getMultiCacheId(Collection<T> ids) {
	List<String> cacheIds = new ArrayList<String>();
	for (Object id : ids) {
	    cacheIds.add(getCacheId(id));
	}
	return cacheIds;
    }

    /**
     * 从cacheId还原成id
     * 
     * @param cacheIds
     * @return
     */
    protected String revertCacheId(String cacheId) {
	String[] splitKeys = cacheId.split(":id:");
	if (splitKeys.length == 2) {
	    return splitKeys[1];
	}
	return cacheId;
    }

    /**
     * 从批量cacheId还原成id列表
     * 
     * @param <T>
     * @param ids
     * @return
     */
    public List<String> revertMultiCacheId(Collection<String> cacheIds) {
	List<String> ids = new ArrayList<String>();
	for (String cacheId : cacheIds) {
	    ids.add(revertCacheId(cacheId));
	}
	return ids;
    }
}
