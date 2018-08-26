package com.lifeix.cbs.contest.bean.bb;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.cbs.contest.bean.bb.ext.BbPlayerResponse;
import com.lifeix.cbs.contest.bean.fb.ext.RecordResponse;
import com.lifeix.user.beans.Response;

public class BbFixedResponse implements JsonSerializer<BbFixedResponse>, Response {

    /**
     * 
     */
    private static final long serialVersionUID = 4948843884108322223L;
    /**
     * 交手记录
     */
    private List<RecordResponse> records;
    
    /**
     * 主队阵容
     */
    private List<BbPlayerResponse> home_lineups;

    /**
     * 客队阵容
     */
    private List<BbPlayerResponse> away_lineups;
    
    public List<RecordResponse> getRecords() {
        return records;
    }

    public void setRecords(List<RecordResponse> records) {
        this.records = records;
    }

    public List<BbPlayerResponse> getHome_lineups() {
        return home_lineups;
    }

    public void setHome_lineups(List<BbPlayerResponse> home_lineups) {
        this.home_lineups = home_lineups;
    }

    public List<BbPlayerResponse> getAway_lineups() {
        return away_lineups;
    }

    public void setAway_lineups(List<BbPlayerResponse> away_lineups) {
        this.away_lineups = away_lineups;
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(BbFixedResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

}
