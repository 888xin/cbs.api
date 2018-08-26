package com.lifeix.cbs.api.action.qiniu;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Controller;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.qiniu.LifeixImageApiUtil;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.exception.service.L99IllegalParamsException;

@Controller
@Path("/upload")
public class ImageAction extends BaseAction {

    /**
     * 获取七牛token
     * 
     * @param format
     * @param bucketName
     * @param mimeType
     * @param num
     * @return
     */
    @GET
    @Path("/token")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUploadToken(@QueryParam("format") @DefaultValue("json") String format,
	    @QueryParam("bucket_name") String bucketName, @QueryParam("mime_type") @DefaultValue("0") Integer mimeType,
	    @QueryParam("num") @DefaultValue("1") Integer num) {
	start();
	try {
	    return DataResponseFormat.response(LifeixImageApiUtil.getInstance().getToken(bucketName, num, mimeType));
	} catch (L99IllegalParamsException e) {
	    DataResponse<Object> ret = new DataResponse<Object>();
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	    return DataResponseFormat.response(ret);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    DataResponse<Object> ret = new DataResponse<Object>();
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	    return DataResponseFormat.response(ret);
	} finally {
	    end();
	}
    }

}
