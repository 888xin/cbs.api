package com.lifeix.cbs.api.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l99.operations.xconfig.XConfLoader;
import com.lifeix.metrics.healthcheck.HealthCheckMethod;
import com.lifeix.metrics.healthcheck.LifeixHealthCheckListener;

/**
 * startup listener
 * 
 * @author lifeix
 * 
 */
public class InitListener extends LifeixHealthCheckListener {

    private final static Logger LOG = LoggerFactory.getLogger(InitListener.class);

    @Override
    public void contextDestroyed(ServletContextEvent contextEvent) {
	super.contextDestroyed(contextEvent);

	LOG.info("-----------------------------------------------------");
	LOG.info("start shutdown for cbs-content.");

	LOG.info("cbs-content has been shut down.");
	LOG.info("-----------------------------------------------------");

    }

    @Override
    public void contextInitialized(ServletContextEvent contextEvent) {
	// 从配置服务器读取配置
	XConfLoader.load();

	super.contextInitialized(contextEvent);

	try {
	    LOG.info("-----------------------------------------------------");
	    LOG.info("start init cbs-content in listener...");
	    Map<String, Object> config = new TreeMap<String, Object>();

	    registerHealthCheck();

	    printConfig(config, "cbs-content");

	    LOG.info("end init in listener");
	} catch (Exception ex) {
	    LOG.error(ex.getMessage(), ex);
	    throw new RuntimeException(ex.getMessage());
	}

    }

    /**
     * 打印出配置信息
     * 
     * @param config
     */
    private static void printConfig(Map<String, Object> config, String name) {
	StringBuffer configInfo = new StringBuffer();
	configInfo.append("\n");
	configInfo.append("*********************************************************** ");
	configInfo.append(name);
	configInfo.append(" config ");
	for (int i = 0; i < 97 - name.length(); i++) {
	    configInfo.append("*");
	}
	configInfo.append("\n");
	for (Iterator<String> iterator = config.keySet().iterator(); iterator.hasNext();) {
	    String key = iterator.next();
	    if (configInfo.length() > 200) {
		configInfo.append("\n");
	    }
	    int startLength = configInfo.length();
	    configInfo.append("*    ");
	    configInfo.append(key);
	    configInfo.append("=");
	    if (key.indexOf("password") > -1) {
		configInfo.append("******");
	    } else {
		configInfo.append(config.get(key).toString());
	    }
	    int length = 164 - (configInfo.length() - startLength);
	    if (length > 0) {
		for (int i = 0; i < length; i++) {
		    configInfo.append(" ");
		}
	    }
	    configInfo.append("*");
	}
	configInfo.append("\n");
	configInfo
	        .append("*********************************************************************************************************************************************************************");
	LOG.info(configInfo.toString());
	config.clear();
	config = null;
    }

    /**
     * 注册健康监测
     */
    @Override
    public List<HealthCheckMethod> assertHealthCheck() {
	List<HealthCheckMethod> healthChecks = new ArrayList<HealthCheckMethod>();

	return healthChecks;
    }

}
