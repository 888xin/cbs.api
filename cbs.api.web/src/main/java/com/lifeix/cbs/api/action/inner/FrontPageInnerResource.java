package com.lifeix.cbs.api.action.inner;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.common.exception.MsgCode;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.content.bean.frontpage.FrontPageListResponse;
import com.lifeix.cbs.content.bean.frontpage.FrontPageResponse;
import com.lifeix.cbs.content.service.frontpage.FrontPageService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by lhx on 15-11-30 下午2:16
 *
 * @Description
 */
@Controller
@Path("/inner/frontpage")
public class FrontPageInnerResource extends BaseAction {

    protected static Logger LOG = LoggerFactory.getLogger(FrontPageInnerResource.class);

    @Autowired
    private FrontPageService frontPageService;

    /**
     * 添加
     */
    @POST
    @Path("/edit")
    @Produces(MediaType.APPLICATION_JSON)
    public String edit(@FormParam("contest_id") Long contestId, @FormParam("contest_type") Integer contestType,
                       @FormParam("innner_contest_id") Long innnerContestId, @FormParam("innner_contest_type") Integer innnerContestType,
                       @FormParam("fid") Long fId, @FormParam("link") String link, @FormParam("path") String path,
                       @FormParam("title") String title, @FormParam("status") Integer status, @FormParam("desc") String desc,
                       @FormParam("content_id") Long contentId, @FormParam("type") @DefaultValue("100") Integer type,
                       @FormParam("oper") @DefaultValue("10") Integer oper) throws JSONException {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            if (fId == null) {
                // 添加操作
                frontPageService.addFrontPage(contestId, null, contestType, null, desc, path, link, type, contentId, title, innnerContestId, innnerContestType);
            } else {
                // 修改状态
                frontPageService.editFrontPagesInner(fId, status, type, contestId, contestType, desc, path, link, contentId, title, innnerContestId, innnerContestType, oper);
            }
            ret.setCode(DataResponse.OK);
        } catch (L99IllegalParamsException | L99IllegalDataException e) {
            ret.setCode(Integer.valueOf(e.getErrorcode()));
            ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(DataResponse.NO);
            ret.setMsg(InternationalResources.getInstance().locale(MsgCode.BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }

    /**
     * 获取列表
     *
     * @param status 0初始 1审核通过 2审核不通过（删除）
     * @param type   头版广告区<0 头版内容区>0，具体参考ContentConstants.FrontPage
     */
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String list(@QueryParam("limit") @DefaultValue("40") Integer limit, @QueryParam("start_id") Long startId,
                       @QueryParam("end_id") Long endId, @QueryParam("status") Integer status,
                       @QueryParam("type") @DefaultValue("100") Integer type, @QueryParam("skip") Integer skip) throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            FrontPageListResponse data = frontPageService.findFrontPagesInner(startId, endId, limit, type, status, skip);
            ret.setCode(DataResponse.OK);
            ret.setData(data);
        } catch (L99IllegalParamsException | L99IllegalDataException e) {
            ret.setCode(Integer.valueOf(e.getErrorcode()));
            ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(DataResponse.NO);
            ret.setMsg(InternationalResources.getInstance().locale(MsgCode.BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }

    /**
     * 对redis队列进行操作
     *
     * @param fId    头版id
     * @param status 操作状态 7为移出 8为插入 9为置顶
     */
    @POST
    @Path("/queue")
    @Produces(MediaType.APPLICATION_JSON)
    public String queue(@FormParam("fid") Long fId, @FormParam("status") Integer status,
                        @FormParam("type") @DefaultValue("100") Integer type) throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            frontPageService.operateQueue(fId, status, type);
            ret.setCode(DataResponse.OK);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(DataResponse.NO);
            ret.setMsg(InternationalResources.getInstance().locale(MsgCode.BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }

    @GET
    @Path("/view")
    @Produces(MediaType.APPLICATION_JSON)
    public String view(@QueryParam("fid") Long fId, @QueryParam("type") @DefaultValue("100") Integer type)
            throws JSONException {
        DataResponse<FrontPageResponse> ret = new DataResponse<FrontPageResponse>();
        try {
            start();
            FrontPageResponse data = frontPageService.findFrontPage(fId, type);
            ret.setCode(DataResponse.OK);
            ret.setData(data);
        } catch (L99IllegalParamsException e) {
            ret.setCode(Integer.valueOf(e.getErrorcode()));
            ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(DataResponse.NO);
            ret.setMsg(InternationalResources.getInstance().locale(MsgCode.BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }


    /**
     * 根据猜友圈的记录，后台推荐到头版
     */
    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public String add( @FormParam("circle_id") Long circleId, @FormParam("type") @DefaultValue("2") Integer type) throws JSONException {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            // 添加操作
            frontPageService.addFrontPageFromCircle(circleId, type);
            ret.setCode(DataResponse.OK);
        } catch (L99IllegalParamsException | L99IllegalDataException e) {
            ret.setCode(Integer.valueOf(e.getErrorcode()));
            ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(DataResponse.NO);
            ret.setMsg(InternationalResources.getInstance().locale(MsgCode.BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }
}
