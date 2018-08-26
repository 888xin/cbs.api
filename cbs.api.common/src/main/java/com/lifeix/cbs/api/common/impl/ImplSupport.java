package com.lifeix.cbs.api.common.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ImplSupport {

    /**
     * log object
     */
    protected static final Logger LOG = LoggerFactory.getLogger(ImplSupport.class);
    
    /**
     * dto缓存的id
     * 
     * @param id
     * @return
     */
    protected String getCacheId(Object id) {
	return String.format("%s:id:%s", this.getClass().getName(), id.toString());
    }

}
