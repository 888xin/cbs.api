package com.lifeix.cbs.contest.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 订单信息收集日志
 * 
 * @author peter
 * 
 */
public class CbsSettleLog {

    protected final static Logger LOG = LoggerFactory.getLogger(CbsSettleLog.class);

    public static void debug(String message) {
	LOG.debug(message);
    }

    public static void info(String message) {
	LOG.info(message);
    }

    public static void warn(String message) {
	LOG.warn(message);
    }

    public static void error(String message) {
	LOG.error(message);
    }

    public static void error(String message, Throwable e) {
	LOG.error(message, e);
    }
}
