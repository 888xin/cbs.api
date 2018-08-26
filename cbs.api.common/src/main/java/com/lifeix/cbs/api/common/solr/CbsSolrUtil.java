package com.lifeix.cbs.api.common.solr;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;

/**
 * 操作cbs solr工具类
 * 
 * @author jacky
 * 
 */
public class CbsSolrUtil {

    protected final static Logger LOG = LoggerFactory.getLogger(CbsSolrUtil.class);
    private Client client;
    private String uri;

    private final static String CBS_USERS_UPDATE = "cbs-users/update";

    private static final String CBS_USER_SEARCH = "cbs-users/select";
    private static final String CBS_USER_FRIEND_CIRCLE = "cbs-friend-circle/select";

    static class SingletonHolder {
	/**
	 * Add a private constructor to hide the implicit public one.
	 */
	private SingletonHolder() {
	}

	private static final CbsSolrUtil INSTANCE = new CbsSolrUtil();
    }

    public static CbsSolrUtil getInstance() {
	return SingletonHolder.INSTANCE;
    }

    private CbsSolrUtil() {
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
	    throw new RuntimeException("Uri format wrong,eg. http://host:8080/lifeix-tiyu-search/");
	}
	int lastIndex = uri.length() - 1;
	if (uri.charAt(lastIndex) != '/') {
	    this.uri = uri + "/";
	} else {
	    this.uri = uri;
	}
    }

    /**
     * 搜索用户
     * 
     * @param mutilSeachKey
     * @param startId
     * @param limit
     * @return
     */
    public String select(String mutilSeachKey, Integer startId, Integer limit) {
	Form queryParam = new Form();
	queryParam.add("wt", "json");
	queryParam.add("q", mutilSeachKey);
	if (startId != null) {
	    queryParam.add("start", startId);
	}
	queryParam.add("indent", "true");
	queryParam.add("sort", "cbs_score desc,user_id desc");
	queryParam.add("rows", limit);
	WebResource resource = client.resource(uri + CBS_USER_SEARCH);
	return resource.queryParams(queryParam).get(String.class);
    }

    /**
     * 搜索用户
     * 
     * @param mutilSeachKey
     * @param startId
     * @param limit
     * @return
     */
    public String selectInnerCircles(Long userId, String content, Long startId, Long endId, Integer limit, Integer skip,
	    boolean isCount) {
	Form queryParam = new Form();
	queryParam.add("wt", "json");
	if (userId != null) {
	    queryParam.add("q", userId);
	}

	if (StringUtils.isNotEmpty(content)) {
	    queryParam.add("q", content);
	}

	if (StringUtils.isEmpty(content) && userId == null) {
	    queryParam.add("q", "*:*");
	}

	if (startId != null && !isCount) {
	    queryParam.add("fq", String.format("friend_circle_id:{%s TO *}", startId));
	    queryParam.add("sort", "friend_circle_id asc");
	}

	if (endId != null && !isCount) {
	    queryParam.add("fq", String.format("friend_circle_id:{* TO %s}", endId));
	    queryParam.add("sort", "friend_circle_id desc");
	}
	if (startId == null && endId == null) {
	    queryParam.add("sort", "friend_circle_id desc");
	}
	if (skip != null && skip.intValue() > 0) {
	    queryParam.add("start", skip);
	}
	queryParam.add("indent", "true");
	queryParam.add("rows", limit);
	WebResource resource = client.resource(uri + CBS_USER_FRIEND_CIRCLE);
	return resource.queryParams(queryParam).get(String.class);
    }

    /**
     * 
     * @param userId
     * @param userNo
     * @param namePinyin
     * @param userName
     * @param userPath
     * @param gender
     * @param status
     * @param local
     * @param aboutme
     * @param back
     * @param createTime
     * @param mobilePhone
     */
    public void updateIndex(Long userId, Long userNo, String namePinyin, String userName, String userPath, boolean gender,
	    Integer status, String local, String aboutme, String back, String mobilePhone, Date createTime) {
	try {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	    WebResource resource = client.resource(uri + CBS_USERS_UPDATE);
	    JSONObject params = new JSONObject();
	    JSONObject addParams = new JSONObject();
	    JSONObject doc = new JSONObject();
	    doc.put("id", userId);
	    doc.put("user_id", userId);
	    doc.put("name_pinyin", namePinyin);
	    doc.put("user_no", userNo);
	    doc.put("user_name", userName);
	    doc.put("user_path", userPath);
	    doc.put("gender", gender);
	    doc.put("mobile_phone", mobilePhone);
	    doc.put("status", status);
	    doc.put("aboutme", aboutme);
	    doc.put("create_time", sdf.format(createTime));
	    addParams.put("doc", doc);
	    addParams.put("boost", 1);
	    addParams.put("overwrite", true);
	    addParams.put("commitWithin", 2000);
	    params.put("add", addParams);
	    ClientResponse result = resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON_TYPE)
		    .post(ClientResponse.class, params.toString());
	    String strResult = result.getEntity(String.class);
	    JSONObject resultObj = new JSONObject(strResult);
	    JSONObject response = resultObj.getJSONObject("responseHeader");
	    int retStatus = response.getInt("status");
	    if (retStatus == 0) {
		LOG.info("insert user solr index successfully");
	    } else {
		LOG.warn("insert user solr index faily");
	    }
	} catch (Exception e) {
	    LOG.error(String.format("inser user solr index failed - %s", e.getMessage()), e);
	}

    }

    public static String stringFilter(String str) {
	return filterMatch(str, "([a-zA-Z\\d\u4E00-\u9FA5])");

    }

    /**
     * 过滤搜索特殊字符
     * 
     * @param context
     * @param reg
     * @return
     */
    public static String filterMatch(String context, String reg) {
	if (context == null || context.equals(""))
	    return context;
	Pattern p = Pattern.compile(reg);
	Matcher m = p.matcher(context);
	StringBuffer buffer = new StringBuffer();
	while (m.find()) {
	    buffer.append(m.group(1));
	}
	return buffer.toString();
    }

}
