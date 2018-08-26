/**
 * 
 */
package com.lifeix.cbs.api.util;

import lifeix.framwork.util.JsonUtils;

/**
 * web返回格式化
 * @author peter
 *
 */
public  class DataResponseFormat {

    /**
     * 对象序列化成JSON返回
     * @param ret
     * @return
     */
    public static String response(Object ret) {
    	return JsonUtils.toJsonString(ret);
    }
    
}
