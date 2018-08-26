package com.lifeix.cbs.api.action.content;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.bean.coupon.CouponUserListResponse;
import com.lifeix.cbs.api.bean.coupon.CouponUserResponse;
import com.lifeix.cbs.api.bean.gold.GoldResponse;
import com.lifeix.cbs.api.common.exception.MsgCode;
import com.lifeix.cbs.api.common.util.ContentConstants;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.service.coupon.CouponUserService;
import com.lifeix.cbs.api.service.money.MoneyService;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.content.bean.frontpage.FrontCardListResponse;
import com.lifeix.cbs.content.bean.frontpage.FrontPageListResponse;
import com.lifeix.cbs.content.service.frontpage.FrontCardService;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhx on 15-11-30 下午2:54
 *
 * @Description
 */
@Controller
@Path("/frontpage")
public class FrontPageResource extends BaseAction {

    protected static Logger LOG = LoggerFactory.getLogger(FrontPageResource.class);

    @Autowired
    private FrontPageService frontPageService;

    @Autowired
    private FrontCardService frontCardService;

    /**
     * 获取用户是否有龙筹券来发表头版
     */
    @GET
    @Path("/cards")
    @Produces(MediaType.APPLICATION_JSON)
    public String cardList() throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            FrontCardListResponse frontCardListResponse = frontCardService.findList(getSessionAccount(request));
            ret.setCode(DataResponse.OK);
            ret.setData(frontCardListResponse);
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
     * @param type -100为头版广告区 100为头版内容区 1为用户的吐槽 2为下单理由 3为官方消息 4为推荐咨询 5为推荐比赛
     *             6为推荐网页 7为新闻（包括资讯/推荐比赛/推荐网页）
     */
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String list(@QueryParam("limit") @DefaultValue("29") Integer limit, @QueryParam("start_id") Long startId,
                       @QueryParam("type") @DefaultValue("100") Integer type) throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            FrontPageListResponse data = new FrontPageListResponse();
            if (type > 0) {
                data = frontPageService.getFrontPagesContent(getSessionAccount(request),startId, limit, type);
            } else if (type == ContentConstants.FrontPage.TYPE_AD_ALL) {
                data = frontPageService.getFrontPagesAd(type);
            }
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

}
