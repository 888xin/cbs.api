package com.lifeix.cbs.contest.impl.spark.contest;

import com.lifeix.cbs.api.bean.user.CbsUserWxResponse;
import com.lifeix.cbs.api.service.user.CbsUserWxService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.common.user.LifeixUserApiUtil;
import com.lifeix.cbs.contest.service.spark.contest.ContestBetDubbo;
import com.lifeix.cbs.contest.util.WeixinNotifyUtil;
import com.lifeix.user.beans.oauth.LifeixOAuthConsumer;

@Service("contestBetDubbo")
public class ContestBetDubboImpl implements ContestBetDubbo {

    protected static final Logger LOG = LoggerFactory.getLogger(ContestBetDubboImpl.class);

    @Autowired
    private CbsUserWxService cbsUserWxService;

    /**
     * 发送微信下单消息提醒
     *
     * @param paramStr
     * @return
     */
    public void wxNotify(String paramStr) {
        try {
            JSONObject obj = new JSONObject(paramStr);
            long userId = obj.getLong("user_id");
            // 获取用户信息
//            LifeixOAuthConsumer consumer = LifeixUserApiUtil.getInstance().findWxAccessToken(userId);
//            if (consumer != null) {
//                obj.put("open_Id", consumer.getTokenSecret());
//                obj.put("access_token", consumer.getAccessToken());
//
//                String tempId = obj.getString("temp_id");
//                // 发送消息提醒
//                WeixinNotifyUtil.getInstance().sendWxTempMessById(obj, tempId, obj.getInt("contest_type"));
//                LOG.info(String.format("weixin kafka receive message %s,%s", obj.toString(), tempId));
//            }
            //update by lhx on 16-08-18
            CbsUserWxResponse cbsUserWxResponse = cbsUserWxService.selectById(userId);
            if (cbsUserWxResponse != null) {
                obj.put("open_Id", cbsUserWxResponse.getOpen_id());
                String tempId = obj.getString("temp_id");
                // 发送消息提醒
                WeixinNotifyUtil.getInstance().sendWxTempMessById(obj, tempId, obj.getInt("contest_type"));
                LOG.info(String.format("weixin kafka receive message %s,%s", obj.toString(), tempId));
            }
        } catch (Exception e) {
            LOG.error(" bet weixin message failed: " + e.getMessage(), e);
        }
    }
}
