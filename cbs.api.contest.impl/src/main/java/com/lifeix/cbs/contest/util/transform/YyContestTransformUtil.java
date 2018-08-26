package com.lifeix.cbs.contest.util.transform;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.contest.bean.yy.YyBetResponse;
import com.lifeix.cbs.contest.bean.yy.YyContestResponse;
import com.lifeix.cbs.contest.bean.yy.YyCupResponse;
import com.lifeix.cbs.contest.bean.yy.YyOptionResponse;
import com.lifeix.cbs.contest.dto.yy.YyBet;
import com.lifeix.cbs.contest.dto.yy.YyContest;
import com.lifeix.cbs.contest.dto.yy.YyCup;

/**
 * 押押DTO 转换 VO
 * 
 * @author lifeix-sz
 * 
 */
public class YyContestTransformUtil {

    protected static Logger LOG = LoggerFactory.getLogger(YyContestTransformUtil.class);

    /**
     * 转换押押赛事对象
     * 
     * @param contest
     * @return
     */
    public static YyContestResponse transformYyContest(YyContest contest, Map<String, Object> betCount, boolean isList) {
	YyContestResponse resp = null;
	if (contest != null) {
	    resp = new YyContestResponse();
	    resp.setId(contest.getId());
	    resp.setTitle(contest.getTitle());
		//列表图单独设
		if (isList && StringUtils.isNotBlank(contest.getListImage())){
			resp.setImages(contest.getListImage());
		} else {
			resp.setImages(contest.getImages());
		}
	    resp.setList_image(contest.getListImage());
	    resp.setText(contest.getText());
	    resp.setCup_id(contest.getCupId());
	    resp.setCup_name(contest.getCupName());
	    resp.setCreate_time(CbsTimeUtils.getUtcTimeForDate(contest.getCreateTime()));
	    resp.setStart_time(CbsTimeUtils.getUtcTimeForDate(contest.getStartTime()));
	    resp.setEnd_time(CbsTimeUtils.getUtcTimeForDate(contest.getEndTime()));
	    resp.setStatus(contest.getStatus());
	    resp.setWinner(contest.getWinner());
	    resp.setSettle(contest.isSettle());
	    resp.setIs_longbi(contest.isLongbi());
	    resp.setHide_flag(contest.getHideFlag());
	    resp.setInit_count(contest.getInitCount());
	    resp.setShow_type(contest.getShowType());
	    resp.setActivity_flag(contest.getActivityFlag());
	    if (StringUtils.isNotEmpty(contest.getOptions())) {
		List<YyOptionResponse> option_data = new ArrayList<YyOptionResponse>();
		int total = contest.getInitCount();
		try {
		    JSONArray optionArray = new JSONArray(contest.getOptions());
		    for (int i = 0; i < optionArray.length(); i++) {
			JSONObject optionObj = optionArray.getJSONObject(i);
			YyOptionResponse option = new YyOptionResponse();
			int optionCount = 0;
			if (optionObj.has("c")) {
			    optionCount = optionObj.getInt("c");
			}
			Integer index = optionObj.getInt("i");
			option.setIndex(index);
			option.setName(optionObj.getString("n"));
			option.setRoi(optionObj.getDouble("r"));
			if (betCount != null) {
			    Object count = betCount.get(index.toString());// 下单人数
			    if (count != null) {
				int countVal = Integer.valueOf(count.toString());
				optionCount += countVal;
				total += countVal;
			    }
			}
			option.setCount(optionCount);
			// add by lhx on 16-07-08 start
			String image = optionObj.optString("p");
			if (StringUtils.isNotBlank(image)) {
			    option.setImage(image);
			}
			// end
			option_data.add(option);
		    }
		} catch (JSONException e) {
		    LOG.error(e.getMessage(), e);
		}
		resp.setOption_data(option_data);
		resp.setTotal(total);
	    }

	}
	return resp;
    }

    /**
     * 转换押押选项对象
     * 
     * @param options
     * @param betCount
     * @return
     */
    public static List<YyOptionResponse> transformYyOption(String options, Map<String, Object> betCount) {
	List<YyOptionResponse> option_data = new ArrayList<YyOptionResponse>();
	if (StringUtils.isNotEmpty(options)) {
	    try {
		JSONArray optionArray = new JSONArray(options);
		for (int i = 0; i < optionArray.length(); i++) {
		    JSONObject optionObj = optionArray.getJSONObject(i);
		    YyOptionResponse option = new YyOptionResponse();
		    Integer index = optionObj.getInt("i");
		    option.setIndex(index);
		    option.setName(optionObj.getString("n"));
		    option.setRoi(optionObj.getDouble("r"));
		    if (betCount != null) {
			Object count = betCount.get(index.toString());// 下单人数
			if (count != null) {
			    option.setCount(Integer.valueOf(count.toString()));
			}
		    }
		    option_data.add(option);
		}
	    } catch (JSONException e) {
		e.printStackTrace();
	    }
	}
	return option_data;
    }

    /**
     * 转换押押分类
     * 
     * @param cup
     * @return
     */
    public static YyCupResponse transformYyCup(YyCup cup) {
	YyCupResponse resp = null;
	if (cup != null) {
	    resp = new YyCupResponse();
	    resp.setCup_id(cup.getCupId());
	    resp.setCup_name(cup.getCupName());
	    if (cup.getCount() != null) {
		resp.setCount(cup.getCount());
	    }
	    if (cup.getCreateTime() != null) {
		resp.setCreate_time(CbsTimeUtils.getUtcTimeForDate(cup.getCreateTime()));
	    }
	}
	return resp;
    }

    public static YyCupResponse transformYyCup(Long cupId, String cupName) {
	YyCupResponse resp = new YyCupResponse();
	resp.setCup_id(cupId);
	resp.setCup_name(cupName);
	return resp;
    }

    /**
     * 押押下单数据转换vo
     * 
     * @param bet
     * @return
     */
    public static YyBetResponse transformYyBet(YyBet bet) {
	YyBetResponse resp = null;
	if (bet != null) {
	    resp = new YyBetResponse();
	    resp.setId(bet.getId());
	    resp.setUser_id(bet.getUserId());
	    resp.setContest_id(bet.getContestId());
	    resp.setSupport(bet.getSupport());
	    resp.setYy_roi(bet.getYyRoi());
	    resp.setBet(bet.getBet());
	    resp.setBack(bet.getBack());
	    resp.setStatus(bet.getStatus());
	    resp.setCreate_time(CbsTimeUtils.getUtcTimeForDate(bet.getCreateTime()));
	    resp.setContent_id(bet.getContentId());
	    resp.setIs_longbi(bet.isLongbi());
	    resp.setCoupon(bet.getCoupon());
	}
	return resp;
    }
}
