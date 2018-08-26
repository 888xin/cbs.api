package com.lifeix.cbs.api.common.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InternationalResources {

    private static Logger LOG = LoggerFactory.getLogger(InternationalResources.class);

    private HashMap<String, String> cnMap;

    private HashMap<String, String> usMap;

    static class SingletonHolder {
	private static final InternationalResources INSTANCE = new InternationalResources();
    }

    public static InternationalResources getInstance() {
	return SingletonHolder.INSTANCE;
    }

    private InternationalResources() {
	cnMap = new HashMap<String, String>();
	usMap = new HashMap<String, String>();
	initConfig();
    }

    /**
     * init config
     */
    @SuppressWarnings("rawtypes")
    private void initConfig() {
	try {
	    String path = InternationalResources.class.getResource("/").getFile() + "globalMessages_zh_CN.properties";
	    Properties properties = new Properties();
	    path = URLDecoder.decode(path, "utf-8");
	    properties.load(new FileInputStream(path));
	    Enumeration enu = properties.propertyNames();
	    while (enu.hasMoreElements()) {
		String key = (String) enu.nextElement();
		String value = properties.getProperty(key);
		cnMap.put(key, value);
	    }
	    enu = null;
	    path = InternationalResources.class.getResource("/").getFile() + "globalMessages_en_US.properties";
	    path = URLDecoder.decode(path, "utf-8");
	    enu = properties.propertyNames();
	    while (enu.hasMoreElements()) {
		String key = (String) enu.nextElement();
		String value = properties.getProperty(key);
		usMap.put(key, value);
	    }
	} catch (FileNotFoundException e) {
	    LOG.error(e.getMessage(), e);
	} catch (IOException e) {
	    LOG.error(e.getMessage(), e);
	}
    }

    /**
     * load value from key
     * 
     * @param key
     * @param locale
     * @return
     */
    public String locale(String key, Locale locale) {
	String val = getLocalMessage(locale).get(key);
	return val == null ? key : val;
    }

    /**
     * default zh_CN
     * 
     * @param key
     * @return
     */
    public String locale(String key) {
	return locale(key, Locale.CHINESE);
    }

    /**
     * replace loacl map
     * 
     * @param locale
     * @return
     */
    private HashMap<String, String> getLocalMessage(Locale locale) {
	if (locale.equals(Locale.ENGLISH)) {
	    return usMap;
	} else {
	    return cnMap;
	}
    }

}
