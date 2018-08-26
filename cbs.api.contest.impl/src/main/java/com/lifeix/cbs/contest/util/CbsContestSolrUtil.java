package com.lifeix.cbs.contest.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.contest.bean.fb.ContestListResponse;
import com.lifeix.cbs.contest.bean.fb.ContestResponse;
import com.lifeix.cbs.contest.bean.fb.TeamResponse;
import com.lifeix.common.utils.RegexUtil;
import com.lifeix.exception.service.L99NetworkException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;

public class CbsContestSolrUtil {
    protected final static Logger LOG = LoggerFactory.getLogger(CbsContestSolrUtil.class);
    private Client client;
    private String uri;

    private final static String CBS_FB_CONTEST_SEARCH = "cbs-fb-contest/select";
    private final static String CBS_BB_CONTEST_SEARCH = "cbs-bb-contest/select";

    static class SingletonHolder {
	/**
	 * Add a private constructor to hide the implicit public one.
	 */
	private SingletonHolder() {
	}

	private static final CbsContestSolrUtil INSTANCE = new CbsContestSolrUtil();
    }

    public static CbsContestSolrUtil getInstance() {
	return SingletonHolder.INSTANCE;
    }

    private CbsContestSolrUtil() {
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
     * 篮球赛事搜索
     * 
     * @param searchKey
     * @param startTime
     * @param limit
     * @return
     * @throws JSONException
     * @throws L99NetworkException
     * @throws ParseException
     */
    public ContestListResponse searchBbContests(String searchKey, String startTime, Integer limit) throws JSONException,
	    L99NetworkException, ParseException {
	try {
	    SimpleDateFormat sdf_solr = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	    SimpleDateFormat sdf_utc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String searchTime = null;
	    if (startTime != null && !StringUtils.isEmpty(startTime)) {// utc时间转solr时间
		Date searchDate = sdf_utc.parse(startTime);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(searchDate);
		calendar.add(Calendar.HOUR_OF_DAY, -8);
		searchTime = sdf_solr.format(calendar.getTime());
	    }
	    Form queryParam = new Form();
	    queryParam.add("wt", "json");
	    String mutilSeachKey = getBbContestMutilSearchKey(searchKey);
	    queryParam.add("q", mutilSeachKey);
	    if (searchTime != null) {
		queryParam.add("fq", String.format("start_time:{ * TO " + searchTime + " }"));
	    }
	    queryParam.add("indent", "true");
	    queryParam.add("sort", "start_time desc");
	    if (limit != null) {
		queryParam.add("rows", limit.toString());
	    }
	    WebResource resource = client.resource(uri + CBS_BB_CONTEST_SEARCH);
	    String jsonStr = resource.queryParams(queryParam).get(String.class);
	    JSONObject ret = new JSONObject(jsonStr);
	    int status = ret.getJSONObject("responseHeader").getInt("status");
	    if (status == 0) {
		return formatBbContestSearchData(ret.getJSONObject("response"));
	    } else {
		JSONObject errorRet = ret.getJSONObject("error");
		throw new L99NetworkException(errorRet.optString("code"), errorRet.optString("msg"));
	    }
	} catch (ClientHandlerException e) {
	    throw new L99NetworkException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}
    }

    /**
     * 足球赛事搜索
     * 
     * @param searchKey
     * @param startTime
     * @param limit
     * @return
     * @throws JSONException
     * @throws L99NetworkException
     * @throws ParseException
     */
    public ContestListResponse searchFbContests(String searchKey, String startTime, Integer limit) throws JSONException,
	    L99NetworkException, ParseException {
	try {

	    SimpleDateFormat sdf_solr = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	    SimpleDateFormat sdf_utc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String searchTime = null;
	    if (startTime != null && !StringUtils.isEmpty(startTime)) {// utc时间转solr时间
		Date searchDate = sdf_utc.parse(startTime);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(searchDate);
		calendar.add(Calendar.HOUR_OF_DAY, -8);
		searchTime = sdf_solr.format(calendar.getTime());
	    }
	    Form queryParam = new Form();
	    queryParam.add("wt", "json");
	    String mutilSeachKey = getFbContestMutilSearchKey(searchKey);
	    queryParam.add("q", mutilSeachKey);
	    if (searchTime != null) {
		queryParam.add("fq", String.format("start_time:{ * TO " + searchTime + " }"));
	    }
	    queryParam.add("indent", "true");
	    queryParam.add("sort", "start_time desc");
	    if (limit != null) {
		queryParam.add("rows", limit.toString());
	    }
	    WebResource resource = client.resource(uri + CBS_FB_CONTEST_SEARCH);
	    String jsonStr = resource.queryParams(queryParam).get(String.class);
	    JSONObject ret = new JSONObject(jsonStr);
	    int status = ret.getJSONObject("responseHeader").getInt("status");
	    if (status == 0) {
		return formatFbContestSearchData(ret.getJSONObject("response"));
	    } else {
		JSONObject errorRet = ret.getJSONObject("error");
		throw new L99NetworkException(errorRet.optString("code"), errorRet.optString("msg"));
	    }
	} catch (ClientHandlerException e) {
	    throw new L99NetworkException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}
    }

    /**
     * 获取篮球搜索关键字
     * 
     * @param input
     * @return
     */
    private String getBbContestMutilSearchKey(String input) {
	if (StringUtils.isEmpty(input)) {
	    return "*:*";
	}

	if (RegexUtil.hasMatche(input, "(\\d)+")) { // ID查询
	    return "home_team:" + input + " OR away_team:" + input;
	}

	String[] searchs = input.split(" ");
	Set<String> filtSet = new HashSet<String>();
	for (int i = 0; i < searchs.length; i++) {
	    if (!StringUtils.isEmpty(stringFilter(searchs[i]))) {
		filtSet.add(stringFilter(searchs[i]));
	    }
	}
	String[] strFilt = new String[filtSet.size()];
	String[] resultFilt = filtSet.toArray(strFilt);
	StringBuilder sb = new StringBuilder();
	if (resultFilt.length == 0) {
	    sb.append("cup_name:" + "*" + " OR bb_home_name:" + "*" + " OR bb_away_name:" + "*");
	} else if (resultFilt.length == 1) {
	    sb.append("cup_name:" + resultFilt[0] + " OR bb_home_name:" + resultFilt[0] + " OR bb_away_name:"
		    + resultFilt[0]);
	} else {
	    for (int i = 0; i < resultFilt.length; i++) {
		sb.append(String.format("( cup_name:%s OR bb_home_name:%s OR bb_away_name:%s )", resultFilt[i],
		        resultFilt[i], resultFilt[i]));
		if (i <= resultFilt.length - 2) {
		    sb.append(" AND ");
		}
	    }
	}
	return sb.toString();

    }

    /**
     * 获取足球搜索关键字
     * 
     * @param input
     * @return
     */
    private String getFbContestMutilSearchKey(String input) {
	if (StringUtils.isEmpty(input)) {
	    return "*:*";
	}

	if (RegexUtil.hasMatche(input, "(\\d)+")) { // ID查询
	    return "home_team:" + input + " OR away_team:" + input;
	}

	String[] searchs = input.split(" ");
	Set<String> filtSet = new HashSet<String>();
	for (int i = 0; i < searchs.length; i++) {
	    if (!StringUtils.isEmpty(stringFilter(searchs[i]))) {
		filtSet.add(stringFilter(searchs[i]));
	    }
	}
	String[] strFilt = new String[filtSet.size()];
	String[] resultFilt = filtSet.toArray(strFilt);
	StringBuilder sb = new StringBuilder();
	if (resultFilt.length == 0) {
	    sb.append("cup_name:" + "*" + " OR fb_home_name:" + "*" + " OR fb_away_name:" + "*");
	} else if (resultFilt.length == 1) {
	    sb.append("cup_name:" + resultFilt[0] + " OR fb_home_name:" + resultFilt[0] + " OR fb_away_name:"
		    + resultFilt[0]);
	} else {
	    for (int i = 0; i < resultFilt.length; i++) {
		sb.append(String.format("( cup_name:%s OR fb_home_name:%s OR fb_away_name:%s )", resultFilt[i],
		        resultFilt[i], resultFilt[i]));
		if (i <= resultFilt.length - 2) {
		    sb.append(" AND ");
		}
	    }
	}
	return sb.toString();

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

    /**
     * 返回足球赛事列表
     * 
     * @param response
     * @return
     */
    private ContestListResponse formatFbContestSearchData(JSONObject response) {
	ContestListResponse ret = new ContestListResponse();
	List<ContestResponse> contests = new ArrayList<ContestResponse>();
	try {
	    if (response == null) {
		return ret;
	    }
	    int number = response.getInt("numFound");
	    JSONArray contestAarray = response.getJSONArray("docs");
	    for (int i = 0; i < contestAarray.length(); i++) {
		ContestResponse contest = new ContestResponse();
		TeamResponse h_t = new TeamResponse();
		TeamResponse a_t = new TeamResponse();
		JSONObject contestObj = contestAarray.getJSONObject(i);
		contest.setContest_id(contestObj.getLong("contest_id"));
		contest.setContest_type(0);

		if (contestObj.has("belong_five")) {
		    contest.setBelong_five(contestObj.getBoolean("belong_five"));
		}
		if (contestObj.has("bet_count")) {
		    contest.setBet_count(contestObj.getInt("bet_count"));
		}
		if (contestObj.has("color")) {
		    contest.setColor(contestObj.getString("color"));
		}
		if (contestObj.has("create_time")) {
		    contest.setCreate_time(CbsTimeUtils.getUtcTimeForDate(formateUtilDate(contestObj
			    .getString("create_time"))));
		}
		if (contestObj.has("start_time")) {
		    contest.setStart_time(CbsTimeUtils.getUtcTimeForDate(formateUtilDate(contestObj.getString("start_time"))));
		}
		if (contestObj.has("cup_id")) {
		    contest.setCup_id(contestObj.getLong("cup_id"));
		}
		if (contestObj.has("cup_name")) {
		    contest.setCup_name(contestObj.getString("cup_name"));
		}
		if (contestObj.has("home_rank")) {
		    contest.setHome_rank(contestObj.getString("home_rank"));
		}
		if (contestObj.has("away_rank")) {
		    contest.setAway_rank(contestObj.getString("away_rank"));
		}
		if (contestObj.has("home_team")) {
		    contest.setHome_team(contestObj.getLong("home_team"));
		}
		if (contestObj.has("away_team")) {
		    contest.setAway_team(contestObj.getLong("away_team"));
		}
		if (contestObj.has("home_scores")) {
		    contest.setHome_scores(contestObj.getInt("home_scores"));
		}
		if (contestObj.has("away_scores")) {
		    contest.setAway_scores(contestObj.getInt("away_scores"));
		}
		if (contestObj.has("LEVEL")) {
		    contest.setLevel(contestObj.getInt("LEVEL"));
		}
		if (contestObj.has("lock_flag")) {
		    contest.setLock_flag(contestObj.getBoolean("lock_flag"));
		}
		if (contestObj.has("is_longbi")) {
		    contest.setLongbi(contestObj.getBoolean("is_longbi"));
		}
		if (contestObj.has("odds_type")) {
		    contest.setOdds_type(contestObj.getInt("odds_type"));
		}
		if (contestObj.has("settle")) {
		    contest.setSettle(contestObj.getInt("settle"));
		}
		if (contestObj.has("status")) {
		    contest.setStatus(contestObj.getInt("status"));
		}
		if (contestObj.has("target_id")) {
		    contest.setTarget_id(contestObj.getLong("target_id"));
		}

		if (contestObj.has("fb_home_name")) {
		    h_t.setName(contestObj.getString("fb_home_name"));
		}
		if (contestObj.has("fb_home_logo")) {
		    h_t.setLogo(contestObj.getString("fb_home_logo"));
		}
		if (contestObj.has("fb_away_name")) {
		    a_t.setName(contestObj.getString("fb_away_name"));
		}
		if (contestObj.has("fb_away_logo")) {
		    a_t.setLogo(contestObj.getString("fb_away_logo"));
		}
		contest.setH_t(h_t);
		contest.setA_t(a_t);
		contests.add(contest);
	    }
	    ret.setContests(contests);
	    ret.setNumber(number);
	} catch (JSONException e) {
	    LOG.error(e.getMessage(), e);
	} catch (ParseException e) {
	    LOG.error(e.getMessage(), e);
	}
	return ret;
    }

    /**
     * 返回篮球赛事列表
     * 
     * @param response
     * @return
     */
    private ContestListResponse formatBbContestSearchData(JSONObject response) {
	ContestListResponse ret = new ContestListResponse();
	List<ContestResponse> contests = new ArrayList<ContestResponse>();
	try {
	    if (response == null) {
		return ret;
	    }
	    int number = response.getInt("numFound");
	    JSONArray contestAarray = response.getJSONArray("docs");
	    for (int i = 0; i < contestAarray.length(); i++) {
		ContestResponse contest = new ContestResponse();
		TeamResponse h_t = new TeamResponse();
		TeamResponse a_t = new TeamResponse();
		JSONObject contestObj = contestAarray.getJSONObject(i);
		contest.setContest_id(contestObj.getLong("contest_id"));
		contest.setContest_type(1);
		if (contestObj.has("belong_five")) {
		    contest.setBelong_five(contestObj.getBoolean("belong_five"));
		}
		if (contestObj.has("bet_count")) {
		    contest.setBet_count(contestObj.getInt("bet_count"));
		}
		if (contestObj.has("color")) {
		    contest.setColor(contestObj.getString("color"));
		}
		if (contestObj.has("create_time")) {
		    contest.setCreate_time(CbsTimeUtils.getUtcTimeForDate(formateUtilDate(contestObj
			    .getString("create_time"))));
		}
		if (contestObj.has("start_time")) {
		    contest.setStart_time(CbsTimeUtils.getUtcTimeForDate(formateUtilDate(contestObj.getString("start_time"))));
		}
		if (contestObj.has("cup_id")) {
		    contest.setCup_id(contestObj.getLong("cup_id"));
		}
		if (contestObj.has("cup_name")) {
		    contest.setCup_name(contestObj.getString("cup_name"));
		}
		if (contestObj.has("home_team")) {
		    contest.setHome_team(contestObj.getLong("home_team"));
		}
		if (contestObj.has("away_team")) {
		    contest.setAway_team(contestObj.getLong("away_team"));
		}
		if (contestObj.has("home_scores")) {
		    contest.setHome_scores(contestObj.getInt("home_scores"));
		}
		if (contestObj.has("away_scores")) {
		    contest.setAway_scores(contestObj.getInt("away_scores"));
		}
		if (contestObj.has("LEVEL")) {
		    contest.setLevel(contestObj.getInt("LEVEL"));
		}
		if (contestObj.has("lock_flag")) {
		    contest.setLock_flag(contestObj.getBoolean("lock_flag"));
		}
		if (contestObj.has("is_longbi")) {
		    contest.setLongbi(contestObj.getBoolean("is_longbi"));
		}
		if (contestObj.has("odds_type")) {
		    contest.setOdds_type(contestObj.getInt("odds_type"));
		}
		if (contestObj.has("settle")) {
		    contest.setSettle(contestObj.getInt("settle"));
		}
		if (contestObj.has("status")) {
		    contest.setStatus(contestObj.getInt("status"));
		}
		if (contestObj.has("target_id")) {
		    contest.setTarget_id(contestObj.getLong("target_id"));
		}

		if (contestObj.has("bb_home_name")) {
		    h_t.setName(contestObj.getString("bb_home_name"));
		}
		if (contestObj.has("bb_home_logo")) {
		    h_t.setLogo(contestObj.getString("bb_home_logo"));
		}
		if (contestObj.has("bb_away_name")) {
		    a_t.setName(contestObj.getString("bb_away_name"));
		}
		if (contestObj.has("bb_away_logo")) {
		    a_t.setLogo(contestObj.getString("bb_away_logo"));
		}
		contest.setH_t(h_t);
		contest.setA_t(a_t);
		contests.add(contest);
	    }
	    ret.setContests(contests);
	    ret.setNumber(number);
	} catch (JSONException e) {
	    LOG.error(e.getMessage(), e);
	} catch (ParseException e) {
	    LOG.error(e.getMessage(), e);
	}
	return ret;
    }

    /**
     * 将字符串类型的solr日期格式，转换成UTC时区的日期对象
     * 
     * @param solrTime
     * @return
     * @throws ParseException
     */
    private Date formateUtilDate(String solrTime) throws ParseException {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	Date solrDate = sdf.parse(solrTime);
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(solrDate);
	calendar.add(Calendar.HOUR_OF_DAY, 8);
	return calendar.getTime();
    }

}
