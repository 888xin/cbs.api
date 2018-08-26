package com.lifeix.cbs.api.action.inner;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.common.exception.MsgCode;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.contest.bean.bunch.BunchBetListResponse;
import com.lifeix.cbs.contest.bean.bunch.BunchContestListResponse;
import com.lifeix.cbs.contest.bean.bunch.BunchContestResponse;
import com.lifeix.cbs.contest.bean.bunch.BunchPrizeResponse;
import com.lifeix.cbs.contest.service.bunch.BunchBetService;
import com.lifeix.cbs.contest.service.bunch.BunchContestService;
import com.lifeix.cbs.contest.service.spark.contest.BunchContestDubbo;
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
 * Created by lhx on 16-5-17 下午2:44
 *
 * @Description
 */
@Controller
@Path("/inner/bunch")
public class BunchInnerResource extends BaseAction {

    protected static Logger LOG = LoggerFactory.getLogger(BunchInnerResource.class);

    @Autowired
    private BunchContestService bunchContestService;

    @Autowired
    private BunchBetService bunchBetService;

    @Autowired
    private BunchContestDubbo bunchContestDubbo;

    /**
     * 添加串（管理后台用）
     *
     * @param name    名称
     * @param image   照片
     * @param options json字符串 格式：[{"index":0,"contest_id":17186,"contest_type":1,"play_type":6},{"index":1,"contest_id":17186,"contest_type":1,"play_type":7}]
     *                index从0开始
     * @param prizes  奖项 格式：[{"name":"5龙筹","price":5,"type":0,"winNum":2,"num":30},{"name":"10龙币","price":10,"type":1,"winNum":4,"num":10},{"name":"娃娃","price":0,"type":2,"winNum":5,"num":1}]
     */
    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public String add(@FormParam("name") String name, @FormParam("image") String image,
                      @FormParam("options") String options, @FormParam("cost") @DefaultValue("0") int cost,
                      @FormParam("longbi") @DefaultValue("false") boolean longbi,
                      @FormParam("prizes") String prizes) throws JSONException {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            bunchContestService.insert(name, image, options, prizes, cost, longbi);
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
     * 获得列表
     */
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String list(@QueryParam("status") Integer status, @QueryParam("start_id") Long startId, @QueryParam("limit") @DefaultValue("20") Integer limit) {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            BunchContestListResponse bunchContestListResponse = bunchContestService.getInnerList(status, startId, limit);
            ret.setCode(DataResponse.OK);
            ret.setData(bunchContestListResponse);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(Integer.valueOf(MsgCode.BasicMsg.CODE_BASIC_SERVCER));
            ret.setMsg(InternationalResources.getInstance().locale(MsgCode.BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }

    @GET
    @Path("/view")
    @Produces(MediaType.APPLICATION_JSON)
    public String view(@QueryParam("id") Long id) {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            BunchContestResponse bunchContestResponse = bunchContestService.viewInner(id);
            ret.setCode(DataResponse.OK);
            ret.setData(bunchContestResponse);
        } catch (L99IllegalParamsException e) {
            ret.setCode(Integer.valueOf(e.getErrorcode()));
            ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(Integer.valueOf(MsgCode.BasicMsg.CODE_BASIC_SERVCER));
            ret.setMsg(InternationalResources.getInstance().locale(MsgCode.BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }


    @GET
    @Path("/view/prize")
    @Produces(MediaType.APPLICATION_JSON)
    public String viewPrize(@QueryParam("id") Long id) {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            BunchContestResponse bunchContestResponse = bunchContestService.viewPrizeInner(id);
            ret.setCode(DataResponse.OK);
            ret.setData(bunchContestResponse);
        } catch (L99IllegalParamsException e) {
            ret.setCode(Integer.valueOf(e.getErrorcode()));
            ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(Integer.valueOf(MsgCode.BasicMsg.CODE_BASIC_SERVCER));
            ret.setMsg(InternationalResources.getInstance().locale(MsgCode.BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }


    @POST
    @Path("/update/prize")
    @Produces(MediaType.APPLICATION_JSON)
    public String updatePrize(@FormParam("prize") String prize) throws JSONException {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            bunchContestService.updatePrize(prize);
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

    /**
     * 更新串，字段值为null不更新，id必须（管理后台用）
     *
     * @param id
     * @param name
     * @param image
     * @param result
     * @param options
     * @param status
     * @return
     * @throws JSONException
     */
    @POST
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public String update(@FormParam("id") Long id, @FormParam("name") String name, @FormParam("image") String image,
                         @FormParam("result") String result, @FormParam("people") Integer people,
                         @FormParam("options") String options, @FormParam("cost") Integer cost, @FormParam("longbi") Boolean longbi,
                         @FormParam("status") Integer status) throws JSONException {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            bunchContestService.update(id, name, image, options, cost, longbi, result, status, people);
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
     * 更新赛事串的状态，由定时任务来运行，后台管理系统也可以操作
     */
    @POST
    @Path("/status/update")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateStatus(@FormParam("status") @DefaultValue("0") int status) throws JSONException {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            bunchContestDubbo.updateStatus(status);
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


    /**
     * 奖品照片
     */
    @POST
    @Path("/prize/image/add")
    @Produces(MediaType.APPLICATION_JSON)
    public String addPrizeImage(@FormParam("image") String image) throws JSONException {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            bunchContestService.addPrizeImage(image);
            ret.setCode(DataResponse.OK);
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
     * 删除奖品照片
     */
    @POST
    @Path("/prize/image/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public String deletePrizeImage(@FormParam("image") String image) throws JSONException {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            bunchContestService.deletePrizeImage(image);
            ret.setCode(DataResponse.OK);
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
     * 获取奖品照片
     */
    @GET
    @Path("/prize/image/get")
    @Produces(MediaType.APPLICATION_JSON)
    public String getPrizeImage() throws JSONException {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            BunchPrizeResponse bunchPrizeResponse = bunchContestService.getPrizeImage();
            ret.setCode(DataResponse.OK);
            ret.setData(bunchPrizeResponse);
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
     * 获取可能中奖的用户信息
     *
     * @param id
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/award/user/get")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUserAward(@QueryParam("id") Long id) throws JSONException {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            BunchContestResponse bunchContestResponse = bunchContestService.getUserAward(id);
            ret.setCode(DataResponse.OK);
            ret.setData(bunchContestResponse);
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
     * 给用户派奖
     */
    @POST
    @Path("/award/user/send")
    @Produces(MediaType.APPLICATION_JSON)
    public String sendUserAward(@FormParam("id") Long id, @FormParam("user_ids") String userIds) throws JSONException {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            bunchContestService.sendAward(id, userIds);
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
     * 查看该期参与情况
     */
    @GET
    @Path("/award/user/view")
    @Produces(MediaType.APPLICATION_JSON)
    public String viewUserAward(@QueryParam("id") Long id) throws JSONException {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            BunchBetListResponse bunchBetListResponse = bunchBetService.getAwardsList(id, null, 0, 10000);
            ret.setCode(DataResponse.OK);
            ret.setData(bunchBetListResponse);
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
