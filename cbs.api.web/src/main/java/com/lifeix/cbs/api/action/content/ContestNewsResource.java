package com.lifeix.cbs.api.action.content;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.common.exception.MsgCode;
import com.lifeix.cbs.api.common.util.ContentConstants;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.content.bean.contest.ContestNewsListResponse;
import com.lifeix.cbs.content.bean.frontpage.FrontPageListResponse;
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
 * Created by lhx on 16-4-15 上午11:02
 *
 * @Description 赛事分析
 */
@Controller
@Path("/contest/news")
public class ContestNewsResource extends BaseAction {

    protected static Logger LOG = LoggerFactory.getLogger(ContestNewsResource.class);

    @Autowired
    private ContestNewsService contestNewsService ;

    /**
     * 获取赛事分析（新闻资讯）列表
     * @param contestId
     * @param ContestType
     * @return
     * @throws JSONException
     */
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String list(@QueryParam("contest_id") Long contestId,
                       @QueryParam("contest_type") @DefaultValue("0") Integer ContestType) throws JSONException {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            ContestNewsListResponse data = contestNewsService.findNews(contestId, ContestType);
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
