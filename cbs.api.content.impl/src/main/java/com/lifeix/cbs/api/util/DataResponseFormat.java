/**
 * 
 */
package com.lifeix.cbs.api.util;

import lifeix.framwork.util.JsonUtils;

/**
 * web返回格式化
 * 
 * @author peter
 *
 */
public class DataResponseFormat {

    /**
     * Add a private constructor to hide the implicit public one.
     */
    private DataResponseFormat() {
    }

    /**
     * 对象序列化成JSON返回
     * 
     * @param ret
     * @return
     */
    public static String response(Object ret) {
	return JsonUtils.toJsonString(ret);
    }

}
