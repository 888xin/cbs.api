package com.lifeix.cbs.api.action.inner;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.contest.bean.yy.YyContestListResponse;
import com.lifeix.cbs.contest.bean.yy.YyContestResponse;
import com.lifeix.cbs.contest.service.spark.settle.YySettleDubbo;
import com.lifeix.cbs.contest.service.yy.YyContestService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

@Controller
@Path("/inner/yy")
public class YyInnerResource extends BaseAction {

    @Autowired
    private YyContestService yyContestService;

    @Autowired
    private YySettleDubbo yySettleService;

    /**
     * 押押赛事列表
     *
     * @param hideFlag
     * @param type
     * @param startId
     * @param limit
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/contest/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String listContest(@QueryParam("hide_flag") @DefaultValue("false") Boolean hideFlag,
                              @QueryParam("type") Boolean type, @QueryParam("cup_id") Long cupId, @QueryParam("start_id") Long startId,
                              @QueryParam("limit") @DefaultValue("20") Integer limit) throws JSONException {
        DataResponse<YyContestListResponse> ret = new DataResponse<YyContestListResponse>();
        try {
            start();
            YyContestListResponse response = yyContestService.findYyContests(hideFlag, type, cupId, startId, limit);
            ret.setCode(DataResponse.OK);
            ret.setData(response);
        } catch (L99IllegalParamsException e) {
            ret.setCode(Integer.valueOf(e.getErrorcode()));
            ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(Integer.valueOf(BasicMsg.CODE_BASIC_SERVCER));
            ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }

    /**
     * 单个押押赛事
     *
     * @param id
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/contest/view")
    @Produces(MediaType.APPLICATION_JSON)
    public String viewContest(@QueryParam("id") Long id) throws JSONException {
        DataResponse<YyContestResponse> ret = new DataResponse<YyContestResponse>();
        try {
            start();
            YyContestResponse response = yyContestService.viewYyContest(id);
            ret.setCode(DataResponse.OK);
            ret.setData(response);
        } catch (L99IllegalParamsException | L99IllegalDataException e) {
            ret.setCode(Integer.valueOf(e.getErrorcode()));
            ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(Integer.valueOf(BasicMsg.CODE_BASIC_SERVCER));
            ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }

    /**
     * 添加押押赛事
     *
     * @param title
     * @param images
     * @param text
     * @param options
     * @param cupId
     * @param startTime
     * @param activityFlag
     * @param endTime
     * @return
     */
    @POST
    @Path("/contest/add")
    @Produces(MediaType.APPLICATION_JSON)
    public String addContest(@FormParam("title") String title, @FormParam("images") String images,
                             @FormParam("text") String text, @FormParam("options") String options, @FormParam("cup_id") Long cupId,
                             @FormParam("start_time") String startTime, @FormParam("end_time") String endTime,
                             @FormParam("show_type") Integer showType, @FormParam("activity_flag") @DefaultValue("false") Boolean activityFlag,
                             @FormParam("list_image") String listImage) {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            yyContestService.insertYyContests(title, images, text, options, cupId, startTime, endTime, activityFlag, showType, listImage);
            ret.setCode(DataResponse.OK);
        } catch (L99IllegalParamsException | L99IllegalDataException e) {
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
     * 修改押押赛事
     *
     * @param contestId
     * @param title
     * @param images
     * @param text
     * @param options
     * @param cupId
     * @param startTime
     * @param activityFlag
     * @param endTime
     * @return
     */
    @POST
    @Path("/contest/edit")
    @Produces(MediaType.APPLICATION_JSON)
    public String editContest(@FormParam("contest_id") Long contestId, @FormParam("title") String title,
                              @FormParam("images") String images, @FormParam("text") String text, @FormParam("options") String options,
                              @FormParam("cup_id") Long cupId, @FormParam("start_time") String startTime,
                              @FormParam("end_time") String endTime, @FormParam("activity_flag") @DefaultValue("false") Boolean activityFlag,
                              @FormParam("show_type") Integer showType, @FormParam("hide_flag") @DefaultValue("false") Boolean hideFlag,
                              @FormParam("list_image") String listImage) {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            yyContestService.updateYyContests(contestId, title, images, text, options, cupId, startTime, endTime, activityFlag,
                    hideFlag, showType, listImage);
            ret.setCode(DataResponse.OK);
        } catch (L99IllegalParamsException | L99IllegalDataException e) {
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
     * 后台填入押押赛事结果
     *
     * @param contestId
     * @param support
     * @param status
     * @return
     */
    @POST
    @Path("/contest/end")
    @Produces(MediaType.APPLICATION_JSON)
    public String endContest(@FormParam("contest_id") Long contestId, @FormParam("support") Integer support,
                             @FormParam("status") Integer status) {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            yyContestService.endYyContest(contestId, support, status);
            ret.setCode(DataResponse.OK);
        } catch (L99IllegalParamsException | L99IllegalDataException e) {
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
     * 结算押押赛事
     *
     * @param contestId
     * @return
     */
    @POST
    @Path("/contest/settle")
    @Produces(MediaType.APPLICATION_JSON)
    public String settleContest(@FormParam("contest_id") Long contestId) {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            yySettleService.settleContest(contestId);
            ret.setCode(DataResponse.OK);
        } catch (L99IllegalParamsException | L99IllegalDataException e) {
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
     * 添加押押分类
     */
    @POST
    @Path("/cup/add")
    @Produces(MediaType.APPLICATION_JSON)
    public String addCup(@FormParam("cup_name") String cupName) {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            yyContestService.insertYyCups(cupName);
            ret.setCode(DataResponse.OK);
        } catch (L99IllegalParamsException | L99IllegalDataException e) {
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
     * 查询应结算押押数量
     */
    @GET
    @Path("/contest/unsettle")
    @Produces(MediaType.APPLICATION_JSON)
    public String viewContestUnsettle() throws JSONException {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            int num = yyContestService.YyShouldSettle();
            YyContestListResponse response = new YyContestListResponse();
            response.setNumber(num);
            ret.setCode(DataResponse.OK);
            ret.setData(response);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(Integer.valueOf(BasicMsg.CODE_BASIC_SERVCER));
            ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }

    /**
     * 添加选项照片
     */
    @POST
    @Path("/option/image/add")
    @Produces(MediaType.APPLICATION_JSON)
    public String addOptionImage(@FormParam("name") String name, @FormParam("path") String path) {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            yyContestService.addOptionImage(name, path);
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
     * 删除选项照片
     */
    @POST
    @Path("/option/image/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteOptionImage(@FormParam("name") String name) throws JSONException {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            yyContestService.deleteOptionImage(name);
            ret.setCode(DataResponse.OK);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(Integer.valueOf(BasicMsg.CODE_BASIC_SERVCER));
            ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }

    /**
     * 获取全部选项照片
     */
    @GET
    @Path("/option/image/get")
    @Produces(MediaType.APPLICATION_JSON)
    public String getOptionImage() throws JSONException {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            YyContestResponse yyContestResponse = yyContestService.getOptionImage();
            ret.setCode(DataResponse.OK);
            ret.setData(yyContestResponse);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(Integer.valueOf(BasicMsg.CODE_BASIC_SERVCER));
            ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }


    /**
     * 操作精选押押
     */
    @POST
    @Path("/good/edit")
    @Produces(MediaType.APPLICATION_JSON)
    public String editGood(@FormParam("id") Long id) throws JSONException {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            yyContestService.editGoodYy(id);
            ret.setCode(DataResponse.OK);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(Integer.valueOf(BasicMsg.CODE_BASIC_SERVCER));
            ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }
}
