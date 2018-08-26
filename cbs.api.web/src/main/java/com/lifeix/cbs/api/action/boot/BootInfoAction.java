package com.lifeix.cbs.api.action.boot;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.content.bean.boot.BootInfoListResponse;
import com.lifeix.cbs.content.bean.boot.BootInfoResponse;
import com.lifeix.cbs.content.service.boot.BootInfoService;


/**
 * 开机信息接口
 * 
 * @author lifeix-sz
 * 
 */
@Controller
@Path("/boot")
public class BootInfoAction extends BaseAction {

    @Autowired
    private BootInfoService bootInfoService;



    /**
     * 当前开机信息
     * 
     * @return
     */
    @GET
    @Path("/curr")
    @Produces(MediaType.APPLICATION_JSON)
    public String curr() {
	start();
	DataResponse<BootInfoResponse> ret = new DataResponse<BootInfoResponse>();
	
	try {
	    BootInfoResponse response = bootInfoService.currBootInfo();
	    ret.setCode(DataResponse.OK);
	    ret.setData(response);
	    return DataResponseFormat.response(ret);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	    return DataResponseFormat.response(ret);
	}finally {
	    end();
	}
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String list(@QueryParam("start_id") Long startId, @QueryParam("limit") @DefaultValue("20") Integer limit) {
	start();
	DataResponse<BootInfoListResponse> ret =  new DataResponse<BootInfoListResponse>();
	try {
	    BootInfoListResponse response = bootInfoService.findBootInfolList(startId, limit);
	    ret.setCode(DataResponse.OK);
	    ret.setData(response);
	    return DataResponseFormat.response(ret);
	}catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	    return DataResponseFormat.response(ret);
	} finally {
	    end();
	}
    }
    
    
    @GET
    @Path("/view")
    @Produces(MediaType.APPLICATION_JSON)
    public String view(@QueryParam("id") Long id) {
	start();
	DataResponse<BootInfoResponse> ret = new DataResponse<BootInfoResponse>();
	
	try {
	    BootInfoResponse response = bootInfoService.findOneById(id);
	    ret.setCode(DataResponse.OK);
	    ret.setData(response);
	    return DataResponseFormat.response(ret);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	    return DataResponseFormat.response(ret);
	}finally {
	    end();
	}
    }


    public void setBootInfoService(BootInfoService bootInfoService) {
	this.bootInfoService = bootInfoService;
    }

}
