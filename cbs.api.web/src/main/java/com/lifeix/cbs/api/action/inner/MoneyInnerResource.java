package com.lifeix.cbs.api.action.inner;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.bean.gold.GoldLogListResponse;
import com.lifeix.cbs.api.bean.gold.GoldResponse;
import com.lifeix.cbs.api.bean.money.MoneyCardResponse;
import com.lifeix.cbs.api.bean.money.MoneyMissedListResponse;
import com.lifeix.cbs.api.bean.money.MoneyOrderListResponse;
import com.lifeix.cbs.api.bean.money.MoneyStatisticListResponse;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.service.money.MoneyLogService;
import com.lifeix.cbs.api.service.money.MoneyMissedService;
import com.lifeix.cbs.api.service.money.MoneyOrderService;
import com.lifeix.cbs.api.service.money.MoneyService;
import com.lifeix.cbs.api.service.money.MoneyStatisticService;
import com.lifeix.cbs.api.service.user.CbsUserService;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.api.util.IPUtil;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;
import com.lifeix.user.beans.CustomResponse;

@Controller
@Path("/inner/money")
public class MoneyInnerResource extends BaseAction {

    @Autowired
    private MoneyLogService moneyLogService;

    @Autowired
    private MoneyService moneyService;

    @Autowired
    private CbsUserService cbsUserService;

    @Autowired
    private MoneyStatisticService moneyStatisticService;

    @Autowired
    private MoneyOrderService moneyOrderService;

    @Autowired
    private MoneyMissedService moneyMissedService;

    /**
     * 后台充值卡记录删除
     * 
     * @param id
     * @return
     */

    @POST
    @Path("/card/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteCard(@FormParam("id") Long id, @FormParam("deleteFlag") Integer deleteFlag) {
	start();
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    boolean flag = moneyOrderService.deleteMoneyCard(id, deleteFlag);
	    ret.setData(flag);
	    ret.setCode(DataResponse.OK);
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
     * 后台充值卡记录插入
     * 
     * @param name
     * @param detail
     * @param price
     * @param amount
     * @param type
     * @param handsel
     * @return
     */

    @POST
    @Path("/card/insert")
    @Produces(MediaType.APPLICATION_JSON)
    public String insertCard(@FormParam("name") String name, @FormParam("detail") String detail,
	    @FormParam("price") Double price, @FormParam("amount") Double amount, @FormParam("type") Integer type,
	    @FormParam("handsel") Double handsel) {
	start();
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    boolean flag = moneyOrderService.insertMoneyCard(name, detail, price, amount, type, handsel);
	    ret.setData(flag);
	    ret.setCode(DataResponse.OK);
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
     * 后台充值卡记录更新
     * 
     * @param id
     * @param name
     * @param detail
     * @param price
     * @param amount
     * @param type
     * @param handsel
     * @return
     */

    @POST
    @Path("/card/update")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateCard(@FormParam("id") Long id, @FormParam("name") String name, @FormParam("detail") String detail,
	    @FormParam("price") Double price, @FormParam("amount") Double amount, @FormParam("type") Integer type,
	    @FormParam("handsel") Double handsel) {
	start();
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    boolean flag = moneyOrderService.updateMoneyCard(id, name, detail, price, amount, type, handsel);
	    ret.setData(flag);
	    ret.setCode(DataResponse.OK);
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
     * 后台充值卡记录查看
     * 
     * @param id
     * @return
     */

    @POST
    @Path("/card/view")
    @Produces(MediaType.APPLICATION_JSON)
    public String cardList(@FormParam("id") Long id) {
	start();
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    MoneyCardResponse bean = moneyOrderService.findMoneyCard(id);
	    ret.setData(bean);
	    ret.setCode(DataResponse.OK);
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
     * 后台龙币充值接口
     * 
     * @param userId
     * @param amount
     * @param aboutLink
     * @return
     */

    @POST
    @Path("/manage/recharge")
    @Produces(MediaType.APPLICATION_JSON)
    public String recharge(@FormParam("user_id") Long userId, @FormParam("amount") Double amount,
	    @FormParam("admin") String admin) {
	start();
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    moneyService.systemRechargeMoney(userId, amount, admin, IPUtil.getIpAddr(request));
	    // add by lhx on 16-04-13
	    moneyStatisticService.insertUserConsumer(userId + "", amount);
	    moneyStatisticService.insertSystemConsumer(userId + "", amount);
	    ret.setCode(DataResponse.OK);
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
     * 后台龙币扣除接口
     * 
     * @param userId
     * @param amount
     * @param aboutLink
     * @return
     */

    @POST
    @Path("/manage/deduction")
    @Produces(MediaType.APPLICATION_JSON)
    public String deduction(@FormParam("user_id") Long userId, @FormParam("amount") Double amount,
	    @FormParam("admin") String admin) {
	start();
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    moneyService.systemDeductionMoney(userId, amount, admin, IPUtil.getIpAddr(request));
	    // add by lhx on 16-04-13
	    moneyStatisticService.insertUserConsumer(userId + "", -amount);
	    moneyStatisticService.insertSystemConsumer(userId + "", -amount);
	    ret.setCode(DataResponse.OK);
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
     * 查询系统龙币日志详情
     * 
     * @param time
     * @param startId
     * @param startTime
     * @param endTime
     * @param limit
     * @param types
     * @return
     */
    @GET
    @Path("/log/system/detail")
    @Produces(MediaType.APPLICATION_JSON)
    public String systemLogsDetail(@QueryParam("log_id") Long logId, @QueryParam("long_no") Long longNo,
	    @QueryParam("start_id") Long startId, @QueryParam("start_time") String startTime,
	    @QueryParam("end_time") String endTime, @QueryParam("limit") @DefaultValue("20") Integer limit,
	    @QueryParam("type") String types) {
	start();
	DataResponse<GoldLogListResponse> ret = new DataResponse<GoldLogListResponse>();
	try {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date startDate = null;
	    Date endDate = null;
	    if (startTime != null && endTime != null) {
		startDate = sdf.parse(startTime);
		endDate = sdf.parse(endTime);
	    }

	    GoldLogListResponse resp = null;
	    if (logId != null) { // 只通过logId查询
		resp = moneyLogService.findGoldLogById(logId);
	    } else { // 通过龙号查询
		Long userId = null;
		resp = moneyLogService.findGoldLogList(userId, longNo, types, startDate, endDate, true, startId, limit);
	    }

	    ret.setData(resp);
	    ret.setCode(DataResponse.OK);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (L99IllegalDataException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(10001);
	    ret.setMsg(InternationalResources.getInstance().locale("error.basic.serviceerror", request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 统计日志
     * 
     * @param startTime
     * @param endTime
     * @return
     */
    @POST
    @Path("/log/statistic")
    @Produces(MediaType.APPLICATION_JSON)
    public String statistic(@FormParam("start_time") String startTime, @FormParam("end_time") String endTime) {
	start();
	DataResponse<MoneyStatisticListResponse> ret = new DataResponse<MoneyStatisticListResponse>();
	try {

	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    Date start = null;
	    Date end = null;
	    if (startTime != null) {
		start = sdf.parse(startTime);
	    }
	    if (endTime != null) {
		end = sdf.parse(endTime);
	    }
	    MoneyStatisticListResponse resp = moneyStatisticService.findByBetweenTime(start, end);
	    ret.setData(resp);
	    ret.setCode(DataResponse.OK);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(10001);
	    ret.setMsg(InternationalResources.getInstance().locale("error.basic.serviceerror", request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 查询指定时间和指定来源的已完成订单信息
     * 
     * @param startTime
     * @param endTime
     * @param source
     * @param startId
     * @param limit
     * @return
     */
    @GET
    @Path("/order/time")
    @Produces(MediaType.APPLICATION_JSON)
    public String orderTime(@QueryParam("start_time") Long startTime, @QueryParam("end_time") Long endTime,
	    @QueryParam("source") String source, @QueryParam("start_id") Long startId,
	    @QueryParam("limit") @DefaultValue("20") Integer limit) {
	start();
	DataResponse<MoneyOrderListResponse> ret = new DataResponse<MoneyOrderListResponse>();
	try {
	    MoneyOrderListResponse resp = moneyOrderService.findOrdersBySource(startTime, endTime, source, startId, limit);
	    ret.setData(resp);
	    ret.setCode(DataResponse.OK);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (L99IllegalDataException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (L99NetworkException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(10001);
	    ret.setMsg(InternationalResources.getInstance().locale("error.basic.serviceerror", request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 用户龙币余额
     * 
     * @param userId
     * @return
     */
    @GET
    @Path("/view")
    @Produces(MediaType.APPLICATION_JSON)
    public String view(@QueryParam("user_id") Long userId) {
	start();
	DataResponse<GoldResponse> ret = new DataResponse<GoldResponse>();
	try {
	    GoldResponse resp = moneyService.viewUserMoney(userId, null);
	    ret.setData(resp);
	    ret.setCode(DataResponse.OK);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (L99IllegalDataException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(10001);
	    ret.setMsg(InternationalResources.getInstance().locale("error.basic.serviceerror", request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 丢失的龙币操作记录
     * 
     * @param status
     * @param startId
     * @param limit
     * @return
     */
    @GET
    @Path("/missed/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String missedList(@QueryParam("status") Integer status, @QueryParam("start_id") Long startId,
	    @QueryParam("limit") @DefaultValue("20") Integer limit) {
	start();
	DataResponse<MoneyMissedListResponse> ret = new DataResponse<MoneyMissedListResponse>();
	try {
	    MoneyMissedListResponse resp = moneyMissedService.findMoneyMisseds(status, startId, limit);
	    ret.setData(resp);
	    ret.setCode(DataResponse.OK);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(10001);
	    ret.setMsg(InternationalResources.getInstance().locale("error.basic.serviceerror", request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 未处理丢失龙币记录统计
     * 
     * @return
     */
    @GET
    @Path("/missed/count")
    @Produces(MediaType.APPLICATION_JSON)
    public String missedCount() {
	start();
	DataResponse<CustomResponse> ret = new DataResponse<CustomResponse>();
	try {
	    CustomResponse resp = moneyMissedService.countMoneyMisseds();
	    ret.setData(resp);
	    ret.setCode(DataResponse.OK);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(10001);
	    ret.setMsg(InternationalResources.getInstance().locale("error.basic.serviceerror", request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }

    /**
     * 处理龙币丢失操作记录
     * 
     * @param status
     * @param repairFlag
     *            true 修复 false 放弃
     * @param limit
     * @return
     */
    @POST
    @Path("/missed/edit")
    @Produces(MediaType.APPLICATION_JSON)
    public String missedEdit(@FormParam("id") Long id, @FormParam("repair_flag") Boolean repairFlag) {
	start();
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    moneyMissedService.editMoneyMissed(id, repairFlag);
	    ret.setCode(DataResponse.OK);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (L99IllegalDataException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(InternationalResources.getInstance().locale(e.getMessage(), request.getLocale()));
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(10001);
	    ret.setMsg(InternationalResources.getInstance().locale("error.basic.serviceerror", request.getLocale()));
	} finally {
	    end();
	}
	return DataResponseFormat.response(ret);
    }
}
