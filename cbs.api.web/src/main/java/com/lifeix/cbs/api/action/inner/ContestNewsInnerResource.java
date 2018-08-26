package com.lifeix.cbs.api.action.inner;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.common.exception.MsgCode;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.content.bean.contest.ContestNewsListResponse;
import com.lifeix.cbs.content.service.contest.ContestNewsService;
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
 * Created by lhx on 16-4-15 上午11:06
 *
 * @Description
 */
@Controller
@Path("/inner/contest/news")
public class ContestNewsInnerResource extends BaseAction {

    protected static Logger LOG = LoggerFactory.getLogger(ContestNewsInnerResource.class);

    @Autowired
    private ContestNewsService contestNewsService;

    /**
     * 添加
     */
    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public String add(@FormParam("contest_id") Long contestId, @FormParam("contest_type") Integer contestType,
                      @FormParam("image") String image, @FormParam("title") String title, @FormParam("desc") String desc,
                      @FormParam("content_id") Long contentId) throws JSONException {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            contestNewsService.add(title, desc, image, contentId, contestId, contestType);
            ret.setCode(DataResponse.OK);
        } catch (L99IllegalDataException e) {
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

    @POST
    @Path("/edit")
    @Produces(MediaType.APPLICATION_JSON)
    public String edit(@FormParam("id") Long id, @FormParam("contest_id") Long contestId,
                       @FormParam("contest_type") Integer contestType,
                       @FormParam("status") Integer status,
                       @FormParam("image") String image, @FormParam("title") String title, @FormParam("desc") String desc,
                       @FormParam("content_id") Long contentId) throws JSONException {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            contestNewsService.edit(id, title, desc, image, contentId, contestId, contestType, status);
            ret.setCode(DataResponse.OK);
        } catch (L99IllegalDataException | L99IllegalParamsException e) {
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

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String list(@QueryParam("contest_id") Long contestId,
                       @QueryParam("contest_type") Integer ContestType,
                       @QueryParam("status") Integer status,
                       @QueryParam("limit") @DefaultValue("40") Integer limit, @QueryParam("start_id") Long startId,
                       @QueryParam("end_id") Long endId,@QueryParam("skip") Integer skip) throws JSONException {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            ContestNewsListResponse data = contestNewsService.findNewsInner(contestId, ContestType, status,
                    startId, endId, skip, limit);
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
}
