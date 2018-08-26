/**
 * 
 */
package com.lifeix.cbs.api.action.inner;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.ContentConstants.InformType;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.content.bean.inform.InformListResponse;
import com.lifeix.cbs.content.service.inform.InformService;
import com.lifeix.exception.service.L99IllegalDataException;

/**
 * 后台朋友圈评论举报管理
 * 
 * @author lifeix
 *
 */
@Controller
@Path("/inner/inform")
public class InformInnerResource extends BaseAction {

    protected static Logger LOG = LoggerFactory.getLogger(InformInnerResource.class);

    @Autowired
    private InformService informService;

    /**
     * 获取评论举报列表
     * 
     * @param page
     * @param limit
     * @param status
     *            处理状态
     * @param commentId
     *            评论id
     * @return
     */
    @GET
    @Path("/comment/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCommentInformList(@QueryParam("page") @DefaultValue("0") Integer page,
	    @QueryParam("limit") @DefaultValue("20") int limit, @QueryParam("status") Integer status) {
	DataResponse<InformListResponse> ret = new DataResponse<InformListResponse>();
	try {
	    start();
	    InformListResponse listResponse = informService.getInformList(page, limit, status, InformType.COMMENT);
	    ret.setData(listResponse);
	    ret.setCode(DataResponse.OK);
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
     * 更新评论举报处理状态
     * 
     * @param id
     *            主键
     * @param ids
     *            多个主键
     * @param status
     *            处理状态
     * @param disposeInfo
     *            处理理由
     * @return
     */
    @POST
    @Path("/comment/updatestatus")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateCommentInformStatus(@FormParam("id") Long id, @FormParam("status") Integer status,
	    @FormParam("dispose_info") String disposeInfo) {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    informService.updateInformStatus(id, status, disposeInfo, null, InformType.COMMENT);
	    ret.setCode(DataResponse.OK);
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
     * 获取吐槽举报列表
     * 
     * @param page
     * @param limit
     * @param status
     *            处理状态
     * @param contentId
     *            吐槽id
     * @return
     */
    @GET
    @Path("/content/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String getContentInformList(@QueryParam("page") @DefaultValue("0") Integer page,
	    @QueryParam("limit") @DefaultValue("20") int limit, @QueryParam("status") Integer status) {
	DataResponse<InformListResponse> ret = new DataResponse<InformListResponse>();
	try {
	    start();
	    InformListResponse listResponse = informService.getInformList(page, limit, status, InformType.CONTENT);
	    ret.setData(listResponse);
	    ret.setCode(DataResponse.OK);
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
     * 更新吐槽举报处理状态
     * 
     * @param id
     *            主键
     * @param ids
     *            多个主键
     * @param status
     *            处理状态
     * @param disposeInfo
     *            处理理由
     * @return
     */
    @POST
    @Path("/content/updatestatus")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateContentInformStatus(@FormParam("id") Long id, @FormParam("status") Integer status,
	    @FormParam("dispose_info") String disposeInfo) {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    informService.updateInformStatus(id, status, disposeInfo, null, InformType.CONTENT);
	    ret.setCode(DataResponse.OK);
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
     * 获取im举报列表
     * 
     * @param page
     * @param limit
     * @param status
     *            处理状态
     * @param imId
     *            imid
     * @return
     */
    @GET
    @Path("/im/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String getIMInformList(@QueryParam("page") @DefaultValue("0") Integer page,
	    @QueryParam("limit") @DefaultValue("20") int limit, @QueryParam("status") Integer status) {
	DataResponse<InformListResponse> ret = new DataResponse<InformListResponse>();
	try {
	    start();
	    InformListResponse listResponse = informService.getInformList(page, limit, status, InformType.IM);
	    ret.setData(listResponse);
	    ret.setCode(DataResponse.OK);
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
     * 更新im举报处理状态
     * 
     * @param id
     *            主键
     * @param status
     *            处理状态
     * @param disposeInfo
     *            处理理由
     * @return
     */
    @POST
    @Path("/im/updatestatus")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateIMInformStatus(@FormParam("id") Long id, @FormParam("status") Integer status,
	    @FormParam("dispose_info") String disposeInfo, @FormParam("last_time") Long lastTime) {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    informService.updateInformStatus(id, status, disposeInfo, lastTime, InformType.IM);
	    ret.setCode(DataResponse.OK);
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
     * 获取用户举报列表
     * 
     * @param page
     * @param limit
     * @param status
     *            处理状态
     * @param userId
     *            被举报用户id
     * @return
     */
    @GET
    @Path("/user/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUserInformList(@QueryParam("page") @DefaultValue("0") Integer page,
	    @QueryParam("limit") @DefaultValue("20") int limit, @QueryParam("status") Integer status) {
	DataResponse<InformListResponse> ret = new DataResponse<InformListResponse>();
	try {
	    start();
	    InformListResponse listResponse = informService.getInformList(page, limit, status, InformType.USER);
	    ret.setData(listResponse);
	    ret.setCode(DataResponse.OK);
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
     * 更新用户举报处理状态
     * 
     * @param id
     *            主键
     * @param status
     *            处理状态
     * @param disposeInfo
     *            处理理由
     * @return
     */
    @POST
    @Path("/user/updatestatus")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateUserInformStatus(@FormParam("id") Long id, @FormParam("status") Integer status,
	    @FormParam("dispose_info") String disposeInfo, @FormParam("last_time") Long lastTime) {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    informService.updateInformStatus(id, status, disposeInfo, lastTime, InformType.USER);
	    ret.setCode(DataResponse.OK);
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
     * 获取新闻评论举报列表
     * 
     * @param page
     * @param limit
     * @param status
     *            处理状态
     * @return
     */
    @GET
    @Path("/news/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String getNewsCommInformList(@QueryParam("page") @DefaultValue("0") Integer page,
	    @QueryParam("limit") @DefaultValue("20") int limit, @QueryParam("status") Integer status) {
	DataResponse<InformListResponse> ret = new DataResponse<InformListResponse>();
	try {
	    start();
	    InformListResponse listResponse = informService.getInformList(page, limit, status, InformType.NEWS_COMMENT);
	    ret.setData(listResponse);
	    ret.setCode(DataResponse.OK);
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
     * 更新新闻评论举报处理状态
     * 
     * @param id
     *            主键
     * @param status
     *            处理状态
     * @param disposeInfo
     *            处理理由
     * @return
     */
    @POST
    @Path("/news/updatestatus")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateNewsCommInformStatus(@FormParam("id") Long id, @FormParam("status") Integer status,
	    @FormParam("dispose_info") String disposeInfo) {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    informService.updateInformStatus(id, status, disposeInfo, null, InformType.NEWS_COMMENT);
	    ret.setCode(DataResponse.OK);
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
     * 获取新闻举报列表
     * 
     * @param page
     * @param limit
     * @param status
     *            处理状态
     * @return
     */
    @GET
    @Path("/news/main/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String getNewsInformList(@QueryParam("page") @DefaultValue("0") Integer page,
	    @QueryParam("limit") @DefaultValue("20") int limit, @QueryParam("status") Integer status) {
	DataResponse<InformListResponse> ret = new DataResponse<InformListResponse>();
	try {
	    start();
	    InformListResponse listResponse = informService.getInformList(page, limit, status, InformType.NEWS);
	    ret.setData(listResponse);
	    ret.setCode(DataResponse.OK);
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
     * 更新新闻举报处理状态
     * 
     * @param id
     *            主键
     * @param status
     *            处理状态
     * @param disposeInfo
     *            处理理由
     * @return
     */
    @POST
    @Path("/news/main/updatestatus")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateNewsInformStatus(@FormParam("id") Long id, @FormParam("status") Integer status,
	    @FormParam("dispose_info") String disposeInfo) {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    informService.updateInformStatus(id, status, disposeInfo, null, InformType.NEWS);
	    ret.setCode(DataResponse.OK);
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

}
