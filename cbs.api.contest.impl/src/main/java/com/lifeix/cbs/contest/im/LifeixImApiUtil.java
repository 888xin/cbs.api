package com.lifeix.cbs.contest.im;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lifeix.cbs.api.common.util.ContestConstants.ContestType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;

/**
 * 新版IM工具类
 * 
 * @author lifeix
 * 
 */
public class LifeixImApiUtil {

    protected static final Logger LOG = LoggerFactory.getLogger(LifeixImApiUtil.class);

    static class SingletonHolder {
	private static final LifeixImApiUtil INSTANCE = new LifeixImApiUtil();
    }

    public static LifeixImApiUtil getInstance() {
	return SingletonHolder.INSTANCE;
    }

    private LifeixImApiUtil() {
	client = Client.create();
	client.setConnectTimeout(new Integer(3000));
	client.setReadTimeout(new Integer(3000));
    }

    /**
     * 服务初始化
     * 
     * @param uri
     */
    public void initData(final String uri) {
	if (uri == null || uri.isEmpty() || !uri.contains("http://")) {
	    throw new RuntimeException("uri is wrong, please check it");
	}
	int lastIndex = uri.length() - 1;
	if (uri.charAt(lastIndex) != '/') {
	    this.uri = uri + "/";
	} else {
	    this.uri = uri;
	}
    }

    private Client client;

    private String uri;

    // user list by ids
    private final static String URL_ROOM_CREATE = "createChatRoom";

    /**
     * 创建房间
     * 
     * @param contestType
     * @param contestId
     * @param name
     * @return
     */
    public Long createImRoom(int contestType, Long contestId, String name, Date startTime) {
	String roomId = null;
	if (contestType == ContestType.FOOTBALL) { // 足球
	    roomId = String.format("fb_%d", contestId);
	} else { // 篮球
	    roomId = String.format("bb_%d", contestId);
	}

	Form queryParam = new Form();
	queryParam.add("identifier", roomId);
	queryParam.add("name", name);
	queryParam.add("description", name);
	queryParam.add("sort", 0);
	queryParam.add("lab", 0);
	queryParam.add("tm", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime));

	WebResource resource = client.resource(uri + URL_ROOM_CREATE);
	String reponse = resource.type(MediaType.WILDCARD).accept(MediaType.APPLICATION_JSON_TYPE)
	        .post(String.class, queryParam);
	try {
	    JSONObject ret = new JSONObject(reponse);
	    if (ret.getInt("code") == 1000) {
		return ret.getLong("content");
	    }
	} catch (JSONException e) {
	}
	return -1L;
    }
}
