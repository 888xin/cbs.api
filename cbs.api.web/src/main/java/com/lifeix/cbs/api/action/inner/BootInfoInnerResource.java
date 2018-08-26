package com.lifeix.cbs.api.action.inner;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.content.service.boot.BootInfoService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.user.beans.ListResponse;

/**
 * 开机信息管理接口
 * 
 * @author lifeix-sz
 * 
 */
@Controller
@Path("/inner/boot")
public class BootInfoInnerResource extends BaseAction {

    @Autowired
    private BootInfoService bootInfoService;

    /**
     * 添加开机信息
     * 
     * @param infoKey
     * @param enableFlag
     * @return
     */
    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public String add(@FormParam("info_key") String infoKey,
	    @FormParam("enable_flag") @DefaultValue("true") Boolean enableFlag,
	    @FormParam("type") @DefaultValue("1") Integer type, @FormParam("adv_time") @DefaultValue("0") Integer advTime,
	    @FormParam("data_link") String data_link) {
	start();
	DataResponse<Object> ret = new DataResponse<Object>();

	try {
	    bootInfoService.addBootInfo(infoKey, enableFlag, type, advTime, data_link);

	    ret.setCode(DataResponse.OK);

	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (L99IllegalDataException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 禁用开机信息
     * 
     * @param id
     * @return
     */
    @POST
    @Path("/disable")
    @Produces(MediaType.APPLICATION_JSON)
    public String disable(@FormParam("id") Long id) {
	start();
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    boolean result = bootInfoService.disableBootInfo(id);
	    if (result) {
		ret.setCode(DataResponse.OK);
	    } else {
		ret.setCode(DataResponse.NO);
		ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	    }
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);

    }
    
    @POST
    @Path("/able")
    @Produces(MediaType.APPLICATION_JSON)
    public String able(@FormParam("id") Long id) {
	start();
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    boolean result = bootInfoService.ableBootInfo(id);
	    if (result) {
		ret.setCode(DataResponse.OK);
	    } else {
		ret.setCode(DataResponse.NO);
		ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	    }
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);

    }

    /**
     * 删除分类
     * 
     * @param id
     * @return
     */
    @POST
    @Path("/del")
    @Produces(MediaType.APPLICATION_JSON)
    public String drop(@FormParam("id") Long id) {
	start();
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    boolean result = bootInfoService.delBootInfo(id);
	    if (result) {
		ret.setCode(DataResponse.OK);
	    } else {
		ret.setCode(DataResponse.NO);
		ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	    }
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);

    }
    
    
    @POST
    @Path("/edit")
    @Produces(MediaType.APPLICATION_JSON)
    public String add( @FormParam("id") Long id,@FormParam("info_key") String infoKey,
	    @FormParam("type") Integer type, @FormParam("adv_time")  Integer advTime,
	    @FormParam("data_link") String data_link) {
	start();
	DataResponse<Object> ret = new DataResponse<Object>();

	try {
	    bootInfoService.editBootInfo(id, infoKey, type, advTime, data_link);

	    ret.setCode(DataResponse.OK);

	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	}catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(DataResponse.NO);
	    ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    public void setBootInfoService(BootInfoService bootInfoService) {
	this.bootInfoService = bootInfoService;
    }

}
