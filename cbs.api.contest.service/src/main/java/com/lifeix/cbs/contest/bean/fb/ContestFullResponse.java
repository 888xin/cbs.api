package com.lifeix.cbs.contest.bean.fb;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.cbs.contest.bean.bb.BbContestExtResponse;
import com.lifeix.cbs.contest.bean.bb.BbLiveWordsResponse;
import com.lifeix.cbs.contest.bean.fb.ext.RecordResponse;
import com.lifeix.user.beans.Response;

/**
 * 赛事完整信息
 * 
 * @author peter
 * 
 */
public class ContestFullResponse implements JsonSerializer<ContestFullResponse>, Response {

    private static final long serialVersionUID = 9170288907768351491L;

    /**
     * 赛事基础信息
     */
    private ContestResponse contest;

    /**
     * 足球赛事扩展信息
     */
    private ContestExtResponse contest_ext;
    
    /**
     * 篮球赛事扩展信息
     */
    private BbContestExtResponse bb_contest_ext;

    /**
     * 足球赛事文字直播
     */
    private List<FbLiveWordsResponse> live_words;
    
    /**
     * 篮球赛事文字直播
     */
    private List<BbLiveWordsResponse> bb_live_words;

    /**
     * 赛事交手记录
     */
    private List<RecordResponse> records;
    /**
     * 主胜赔率
     */
    private Double home_roi;

    /**
     * 客胜赔率
     */
    private Double away_roi;

    public ContestResponse getContest() {
	return contest;
    }

    public void setContest(ContestResponse contest) {
	this.contest = contest;
    }

    public ContestExtResponse getContest_ext() {
	return contest_ext;
    }

    public void setContest_ext(ContestExtResponse contest_ext) {
	this.contest_ext = contest_ext;
    }

    public List<FbLiveWordsResponse> getLive_words() {
	return live_words;
    }

    public void setLive_words(List<FbLiveWordsResponse> live_words) {
	this.live_words = live_words;
    }

    public List<RecordResponse> getRecords() {
	return records;
    }

    public void setRecords(List<RecordResponse> records) {
	this.records = records;
    }

    public BbContestExtResponse getBb_contest_ext() {
        return bb_contest_ext;
    }

    public void setBb_contest_ext(BbContestExtResponse bb_contest_ext) {
        this.bb_contest_ext = bb_contest_ext;
    }


    public List<BbLiveWordsResponse> getBb_live_words() {
        return bb_live_words;
    }

    public void setBb_live_words(List<BbLiveWordsResponse> bb_live_words) {
        this.bb_live_words = bb_live_words;
    } 
    
    public Double getHome_roi() {
        return home_roi;
    }

    public void setHome_roi(Double home_roi) {
        this.home_roi = home_roi;
    }

    public Double getAway_roi() {
        return away_roi;
    }

    public void setAway_roi(Double away_roi) {
        this.away_roi = away_roi;
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(ContestFullResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

}
