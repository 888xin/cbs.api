package com.lifeix.cbs.api.common.util;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

public class ContentBeanUtils {
    private static WebApplicationContext content;
   static {
       content = ContextLoader.getCurrentWebApplicationContext();
    }
   
    public static Object getCurrentBean(String beanName) {
	return content.getBean(beanName);
    }
}
