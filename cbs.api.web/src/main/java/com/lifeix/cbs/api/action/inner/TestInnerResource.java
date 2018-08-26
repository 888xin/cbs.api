package com.lifeix.cbs.api.action.inner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.lifeix.cbs.api.common.util.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.cbs.contest.bean.channel.ContestChannelResponse;
import com.lifeix.cbs.contest.bean.fb.ContestListResponse;
import com.lifeix.cbs.contest.service.spark.channel.ContestChannelDubbo;
import com.lifeix.cbs.contest.service.spark.contest.BbContestDubbo;
import com.lifeix.cbs.contest.service.spark.contest.FbContestDubbo;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * 测试专用接口 Created by lhx on 15-10-26 上午11:04
 *
 * @Description
 */
@Controller
@Path("/inner/test")
public class TestInnerResource extends BaseAction {

    protected static Logger LOG = LoggerFactory.getLogger(TestInnerResource.class);

    @Autowired
    private FbContestDubbo fbContestDubbo;

    @Autowired
    private BbContestDubbo bbContestDubbo;

    @Autowired
    private ContestChannelDubbo contestChannelDubbo;

    @GET
    @Path("/fb/contest/channel")
    @Produces(MediaType.APPLICATION_JSON)
    public String channelFbContest(@QueryParam("channel_id") Long channelId, @QueryParam("next_flag") boolean ifFuture,
                                   @QueryParam("start_time") String startTime, @QueryParam("end_time") String endTime) throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            // 把string转化为date
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            ContestListResponse respen = fbContestDubbo.findFbContestsForImportantByChannelId(df.parse(startTime),
                    df.parse(endTime), channelId, ifFuture, false);

            ret.setCode(DataResponse.OK);
            ret.setData(respen);
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

    @GET
    @Path("/bb/contest/channel")
    @Produces(MediaType.APPLICATION_JSON)
    public String channelBbContest(@QueryParam("channel_id") Long channelId, @QueryParam("next_flag") Boolean ifFuture,
                                   @QueryParam("start_time") String startTime, @QueryParam("end_time") String endTime) throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();

            Date startDate = null;
            Date endDate = null;

            // 不传递时间默认起始时间往前取4个小时， 结束时间取后一天
            if (startTime == null || endTime == null) {
                Date nowDate = new Date();
                startDate = CbsTimeUtils.upgradeTime(nowDate, Calendar.HOUR_OF_DAY, -4);
                endDate = CbsTimeUtils.upgradeTime(nowDate, Calendar.DAY_OF_YEAR, 1);
            } else {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                startDate = df.parse(startTime);
                endDate = df.parse(endTime);
            }
            ContestListResponse respen = bbContestDubbo.findBbContestsForImportantByChannelId(startDate, endDate, channelId,
                    ifFuture, false);
            ret.setCode(DataResponse.OK);
            ret.setData(respen);
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

    @GET
    @Path("/channel/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String getChannelList() throws JSONException {
        DataResponse<Object> ret = new DataResponse<Object>();
        try {
            start();
            List<ContestChannelResponse> channels = contestChannelDubbo.getAll();
            ret.setCode(DataResponse.OK);
            ret.setData(channels);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(DataResponse.NO);
            ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }

//    @GET
//    @Path("/weixin")
//    @Produces(MediaType.APPLICATION_JSON)
//    public String weixin(@QueryParam("token") String token, @QueryParam("message") String message) throws JSONException {
//        DataResponse<Object> ret = new DataResponse<>();
//        try {
//            start();
//            String result = sendTempMess(token, message);
//            ret.setData(result);
//        } catch (Exception e) {
//            LOG.error(e.getMessage(), e);
//            ret.setCode(DataResponse.NO);
//            ret.setMsg(InternationalResources.getInstance().locale(BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
//        } finally {
//            end();
//        }
//        return DataResponseFormat.response(ret);
//    }
//
//    public static void main(String[] args) {
//        //String message = "{\"template_id\":\"HbsUF1bW0XxZUJPevn8bUqZnyVoppjOBW4uNYFfjg-8\",\"touser\":\"o3lVhwoOJUh-o5_38SRQlXgpwyLw\",\"url\":\"http://weixintiyu.l99.com/cbs-web/circle/my_share?id=530\",\"topcolor\":\"#000000\",\"data\":{ \"first\":{\"color\":\"#000000\",\"value\":\"订单主题：参与2016年08月18日的篮球比赛『西班牙VS法国』中选择『胜负』玩法.\"},\"keyword1\":{\"color\":\"#000000\",\"value\":\"D27199890134\"},\"keyword2\":{\"color\":\"#000000\",\"value\":\"50.0龙币\"},\"remark\":{\"color\":\"#000000\",\"value\":\"订单详情：您选择『主胜』，赔率『1.52』\"}}}";
//        String message = "{\"template_id\":\"HbsUF1bW0XxZUJPevn8bUqZnyVoppjOBW4uNYFfjg-8\",\"touser\":\"o3lVhwlWbOYOMU6nkCnJc3fnzGgE\",\"url\":\"http://weixintiyu.l99.com/cbs-web/circle/my_share?id=530\",\"topcolor\":\"#000000\",\"data\":{ \"first\":{\"color\":\"#000000\",\"value\":\"订单主题：参与2016年08月18日的篮球比赛『西班牙VS法国』中选择『胜负』玩法.\"},\"keyword1\":{\"color\":\"#000000\",\"value\":\"D27199890134\"},\"keyword2\":{\"color\":\"#000000\",\"value\":\"50.0龙币\"},\"remark\":{\"color\":\"#000000\",\"value\":\"订单详情：您选择『主胜』，赔率『1.52』\"}}}";
//        WxToken token1 = new TestInnerResource().getToken();
//        String result = new TestInnerResource().sendTempMess(token1.getAccessToken(), message);
//        System.out.println(result);
//    }
//
//    private String sendTempMess(String accessToken, String message) {
//        // 获取请求地址
//        String reqUrl = CbsUserConstants.UserWeixin.SEND_TEMP_MESS.replace("ACCESS_TOKEN", accessToken);
//        // 发送请求
//        String result = httpRequest(reqUrl, "POST", message);
//
//        return result;
//    }
//
//    public String httpRequest(String requestUrl, String requestMethod, String outputStr) {
//        StringBuffer buffer = new StringBuffer();
//        try {
//            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
//            TrustManager[] tm = { new MyX509TrustManager() };
//            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
//            sslContext.init(null, tm, new java.security.SecureRandom());
//            // 从上述SSLContext对象中得到SSLSocketFactory对象
//            SSLSocketFactory ssf = sslContext.getSocketFactory();
//
//            URL url = new URL(requestUrl);
//            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
//            httpUrlConn.setSSLSocketFactory(ssf);
//
//            httpUrlConn.setDoOutput(true);
//            httpUrlConn.setDoInput(true);
//            httpUrlConn.setUseCaches(false);
//            // 设置请求方式（GET/POST）
//            httpUrlConn.setRequestMethod(requestMethod);
//
//            if ("GET".equalsIgnoreCase(requestMethod))
//                httpUrlConn.connect();
//
//            // 当有数据需要提交时
//            if (null != outputStr) {
//                OutputStream outputStream = httpUrlConn.getOutputStream();
//                // 注意编码格式，防止中文乱码
//                outputStream.write(outputStr.getBytes("UTF-8"));
//                outputStream.close();
//            }
//
//            // 将返回的输入流转换成字符串
//            InputStream inputStream = httpUrlConn.getInputStream();
//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//            String str = null;
//            while ((str = bufferedReader.readLine()) != null) {
//                buffer.append(str);
//            }
//            bufferedReader.close();
//            inputStreamReader.close();
//            // 释放资源
//            inputStream.close();
//            httpUrlConn.disconnect();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return buffer.toString();
//    }
//
//    private WxToken getToken() {
//
//        String requestUrl = CbsUserConstants.UserWeixin.TOKEN_URL.replace("APPID", CbsUserConstants.UserWeixin.APPID).replace("APPSECRET", CbsUserConstants.UserWeixin.SECRET);
//        // 发起GET请求获取凭证
//        String result = httpRequest(requestUrl, "GET", null);
//        WxToken token = null ;
//        if (null != result) {
//            try {
//                JSONObject obj = new JSONObject(result);
//                token = new WxToken();
//                token.setAccessToken(obj.getString("access_token"));
//                token.setExpiresIn(obj.getInt("expires_in"));
//                token.setCurTimeMilles(System.currentTimeMillis());
//            } catch (JSONException e) {
//                token = null;
//                e.printStackTrace();
//            }
//        }
//        return token;
//    }

}
