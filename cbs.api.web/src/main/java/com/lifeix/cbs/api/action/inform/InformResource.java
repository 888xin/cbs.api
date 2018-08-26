package com.lifeix.cbs.api.action.inform;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
import com.lifeix.cbs.content.service.inform.InformService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

@Controller
@Path("/inform")
public class InformResource extends BaseAction {

    protected static Logger LOG = LoggerFactory.getLogger(InformResource.class);

    @Autowired
    private InformService informService;

    /**
     * 举报评论
     * 
     * @param commentId
     *            评论id
     * @param informType
     *            举报理由类型
     * @param informReason
     *            举报理由
     * @return
     */
    @POST
    @Path("/comment/add")
    @Produces(MediaType.APPLICATION_JSON)
    public String commentInformAdd(@FormParam("comment_id") Long commentId, @FormParam("inform_type") Integer informType,
	    @FormParam("inform_reason") String informReason) {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    informService.addOrUpdateInform(commentId, null, null, getSessionAccount(request), informType, informReason,
		    null, InformType.COMMENT);
	    ret.setCode(DataResponse.OK);
	} catch (L99IllegalDataException | L99IllegalParamsException e) {
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
     * 举报吐槽
     * 
     * @param contentId
     *            吐槽id
     * @param informType
     *            举报理由类型
     * @param informReason
     *            举报理由
     * @return
     */
    @POST
    @Path("/content/add")
    @Produces(MediaType.APPLICATION_JSON)
    public String contentInformAdd(@FormParam("content_id") Long contentId, @FormParam("inform_type") Integer informType,
	    @FormParam("inform_reason") String informReason) {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    informService.addOrUpdateInform(contentId, null, null, getSessionAccount(request), informType, informReason,
		    null, InformType.CONTENT);
	    ret.setCode(DataResponse.OK);
	} catch (L99IllegalDataException | L99IllegalParamsException e) {
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
     * 
     * 举报IM
     * 
     * @param imId
     *            imid
     * @param userId
     *            被举报者id
     * @param contain
     *            被举报内容
     * @param image
     *            被举报图片
     * @param informType
     *            举报理由类型
     * @param informReason
     *            举报理由
     * @return
     */
    @POST
    @Path("/im/add")
    @Produces(MediaType.APPLICATION_JSON)
    public String imInformAdd(@FormParam("im_id") Long imId, @FormParam("user_id") Long userId,
	    @FormParam("contain") String contain, @FormParam("image") String image,
	    @FormParam("inform_type") Integer informType, @FormParam("inform_reason") String informReason) {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    informService.addOrUpdateInform(imId, contain, image, getSessionAccount(request), informType, informReason,
		    userId, InformType.IM);
	    ret.setCode(DataResponse.OK);
	} catch (L99IllegalDataException | L99IllegalParamsException e) {
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
     * 用户举报
     * 
     * @param userId
     *            被举报者id
     * @param informType
     *            举报理由类型
     * @param informReason
     *            举报理由
     * @return
     */
    @POST
    @Path("/user/add")
    @Produces(MediaType.APPLICATION_JSON)
    public String userInformAdd(@FormParam("user_id") Long userId, @FormParam("inform_type") Integer informType,
	    @FormParam("inform_reason") String informReason) {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    Long id = informService.addOrUpdateInform(userId, null, null, getSessionAccount(request), informType,
		    informReason, null, InformType.USER);
	    ret.setCode(DataResponse.OK);
	    ret.setData(id);
	} catch (L99IllegalDataException | L99IllegalParamsException e) {
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
     * 举报新闻评论
     * 
     * @param commentId
     *            评论id
     * @param userName
     *            被举报用户名
     * @param contain
     *            评论内容
     * @param informType
     *            举报类型
     * @return
     */
    @POST
    @Path("/news/comment/add")
    @Produces(MediaType.APPLICATION_JSON)
    public String newsCommInformAdd(@FormParam("comment_id") Long commentId, @FormParam("user_name") String userName,
	    @FormParam("contain") String contain, @FormParam("inform_type") @DefaultValue("0") Integer informType) {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    informService.addOrUpdateInform(commentId, contain, null, getSessionAccount(request), informType, userName,
		    null, InformType.NEWS_COMMENT);
	    ret.setCode(DataResponse.OK);
	} catch (L99IllegalDataException | L99IllegalParamsException e) {
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
     * 举报新闻
     * 
     * @param newsId
     * @param informType
     * @param informReason
     * @return
     */
    @POST
    @Path("/news/add")
    @Produces(MediaType.APPLICATION_JSON)
    public String newsInformAdd(@FormParam("news_id") Long newsId, @FormParam("inform_type") Integer informType,
	    @FormParam("inform_reason") String informReason) {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    start();
	    Long id = informService.addOrUpdateInform(newsId, null, null, getSessionAccount(request), informType,
		    informReason, null, InformType.NEWS);
	    ret.setCode(DataResponse.OK);
	    ret.setData(id);
	} catch (L99IllegalDataException | L99IllegalParamsException e) {
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
