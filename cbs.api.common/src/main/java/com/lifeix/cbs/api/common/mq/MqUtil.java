package com.lifeix.cbs.api.common.mq;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lifeix.framework.message.producer.LifeixRabbitMQProducer;

public class MqUtil {

    private final static Logger LOG = LoggerFactory.getLogger(MqUtil.class);

    @Autowired
    private LifeixRabbitMQProducer cbsMqProducer;

    static ExecutorService fixedThreadPool = null;

    static {
	LOG.info("初始化处理异步执行的线程池...");
	fixedThreadPool = Executors.newFixedThreadPool(80);
    }

    public void sendCbsRegisterMessage(final Integer authType, final String authKey, final Long userId) {
	fixedThreadPool.execute(new Runnable() {
	    @Override
	    public void run() {
		try {
		    Map<String, Object> map = new HashMap<String, Object>();
		    map.put("msgType", MqMsgType.USER_REGISTER);
		    JSONObject params = new JSONObject();
		    params.put("authType", authType);
		    params.put("authKey", authKey);
		    params.put("userId", userId);
		    map.put("params", params.toString());
		    cbsMqProducer.sendMessage(map, false);
		    LOG.info("sendRegisterMessage --> 异步消息用户注册通知：{}", map);
		} catch (Exception e) {
		    LOG.error("sendRegisterMessage --> 异步消息用户注册通知异常：{}", e);
		}
	    }
	});
    }

    public static void main(String[] args) throws JSONException {
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("msgType", MqMsgType.USER_REGISTER);
	JSONObject params = new JSONObject();
	params.put("authType", "1");
	params.put("authKey", "22");
	params.put("userId", "333");
	map.put("params", params.toString());
    }

}
