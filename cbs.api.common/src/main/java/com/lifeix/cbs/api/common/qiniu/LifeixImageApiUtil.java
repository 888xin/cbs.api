package com.lifeix.cbs.api.common.qiniu;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.commons.beans.DataResponse;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.image.center.service.MetadataService;
import com.lifeix.image.center.service.TokenService;
import com.lifeix.image.center.vo.TokenResult;

/**
 * 获取token和写入
 * 
 * @author lifeix
 * 
 */
public class LifeixImageApiUtil {

    private LifeixImageApiUtil() {
	WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
	metadataService = (MetadataService) context.getBean("metadataService");
	tokenService = (TokenService) context.getBean("tokenService");
    }

    private static class InstanceHolder {

	private InstanceHolder() {
	}

	private static final LifeixImageApiUtil INSTANCE = new LifeixImageApiUtil();
    }

    public static LifeixImageApiUtil getInstance() {
	return InstanceHolder.INSTANCE;
    }

    private MetadataService metadataService;

    private TokenService tokenService;

    /**
     * 获取上传token
     * 
     * @param bucketName
     * @param num
     * @param mimeType
     * @return
     * @throws L99IllegalParamsException
     */
    public DataResponse<TokenResult> getToken(String bucketName, Integer num, Integer mimeType)
	    throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(bucketName, num, mimeType);
	return tokenService.getUploadToken(bucketName, mimeType, num);
    }

    /**
     * 将文件名写入redis
     * 
     * @param bucketName
     * @param imageFileName
     * @return
     */
    public String putUploadRedis(String bucketName, String fileName) {
	return metadataService.putUploadRedis(bucketName, fileName);
    }

}
