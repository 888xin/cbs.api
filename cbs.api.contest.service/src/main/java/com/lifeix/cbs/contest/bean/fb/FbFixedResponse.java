package com.lifeix.cbs.contest.bean.fb;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.cbs.contest.bean.fb.ext.RecordResponse;
import com.lifeix.cbs.contest.bean.fb.ext.TeamExtResponse;
import com.lifeix.user.beans.Response;

public class FbFixedResponse implements JsonSerializer<FbFixedResponse>, Response {
    
    /**
     * 
     */
    private static final long serialVersionUID = -2069261067327450807L;

    /**
     * 交手记录
     */
    private List<RecordResponse>  fbRecords;
    
    /**
     * 主队扩展信息
     */
    private TeamExtResponse h_t_ext;

    /**
     * 客队扩展信息
     */
    private TeamExtResponse a_t_ext;

    public List<RecordResponse> getFbRecords() {
        return fbRecords;
    }

    public void setFbRecords(List<RecordResponse> fbRecords) {
        this.fbRecords = fbRecords;
    }

    public TeamExtResponse getH_t_ext() {
        return h_t_ext;
    }

    public void setH_t_ext(TeamExtResponse h_t_ext) {
        this.h_t_ext = h_t_ext;
    }

    public TeamExtResponse getA_t_ext() {
        return a_t_ext;
    }

    public void setA_t_ext(TeamExtResponse a_t_ext) {
        this.a_t_ext = a_t_ext;
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(FbFixedResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

}
