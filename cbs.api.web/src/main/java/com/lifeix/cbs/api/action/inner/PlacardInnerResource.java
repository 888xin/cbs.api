package com.lifeix.cbs.api.action.inner;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.message.bean.placard.PlacardTempletListResponse;
import com.lifeix.cbs.message.bean.placard.PlacardTempletResponse;
import com.lifeix.cbs.message.service.placard.PlacardTempletService;
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
 * Created by lhx on 15-10-19 下午6:14
 *
 * @Description
 */
@Controller
@Path("/inner/placard")
public class PlacardInnerResource extends BaseAction {

    protected static Logger LOG = LoggerFactory.getLogger(PlacardInnerResource.class);

    @Autowired
    private PlacardTempletService placardTempletService;

    /**
     * 更新系统公告
     *
     * @param templetId
     * @param title
     * @param content
     * @param endTime
     * @param disableFlag
     * @param format
     * @return
     * @throws org.json.JSONException
     */
    @POST
    @Path("/templet/edit")
    @Produces(MediaType.APPLICATION_JSON)
    public String editTemplet(@FormParam("templet_id") Long templetId, @FormParam("title") String title,
                              @FormParam("content") String content, @FormParam("end_time") String endTime,
                              @FormParam("disable_flag") Boolean disableFlag, @FormParam("link_type") @DefaultValue("0") Integer linkType,
                              @FormParam("link_data") String linkData, @FormParam("format") @DefaultValue("json") String format)
            throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            placardTempletService.editPlacardTemplet(templetId, title, content, endTime, disableFlag, linkType, linkData);
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
     * push 系统公告
     *
     * @param templetId
     * @param format
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/templet/push")
    @Produces(MediaType.APPLICATION_JSON)
    public String pushTemplet(@FormParam("templet_id") Long templetId,
                              @FormParam("format") @DefaultValue("json") String format) throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            placardTempletService.pushPlacardTemplet(templetId);
            ret.setCode(DataResponse.OK);
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
     * 系统公告列表
     *
     * @param disableFlag
     * @param nowPage
     * @param limit
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/templet/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String templetList(@QueryParam("disable_flag") Boolean disableFlag,
                              @QueryParam("now_page") @DefaultValue("1") Integer nowPage,
                              @QueryParam("limit") @DefaultValue("20") Integer limit) throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            PlacardTempletListResponse data = placardTempletService.findPlacardTemplets(disableFlag, nowPage, limit);
            ret.setCode(DataResponse.OK);
            ret.setData(data);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(DataResponse.NO);
            ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }


    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String list(@QueryParam("start_id") Long startId, @QueryParam("limit") @DefaultValue("20") Integer limit) throws JSONException {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            PlacardTempletListResponse data = placardTempletService.findPlacardsInner(startId, limit);
            ret.setCode(DataResponse.OK);
            ret.setData(data);
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
     * 系统公告
     *
     * @param format
     * @param templetId
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/templet/view")
    @Produces(MediaType.APPLICATION_JSON)
    public String templetView(@QueryParam("format") @DefaultValue("json") String format,
                              @QueryParam("templet_id") Long templetId) throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            PlacardTempletResponse data = placardTempletService.viewPlacardTemplet(templetId, false);
            ret.setCode(DataResponse.OK);
            ret.setData(data);
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
    @Path("/templet/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteTemplet(@FormParam("templet_id") Long templetId) throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            placardTempletService.deletePlacardTemplet(templetId);
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
}
