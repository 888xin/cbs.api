package com.lifeix.cbs.contest.bean.fb;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.cbs.contest.bean.fb.ext.EventResponse;
import com.lifeix.cbs.contest.bean.fb.ext.StatisticsResponse;
import com.lifeix.user.beans.Response;

public class FbStatistcsEventResponse implements JsonSerializer<FbStatistcsEventResponse>, Response {
    
    /**
     * 
     */
    private static final long serialVersionUID = -4250965148067449085L;    
    /**
     * 主队实时技术统计
     */
    private StatisticsResponse h_statistics;

    /**
     * 客队实时技术统计
     */
    private StatisticsResponse a_statistics;
    
    private List<EventResponse> events;


    public StatisticsResponse getH_statistics() {
        return h_statistics;
    }

    public void setH_statistics(StatisticsResponse h_statistics) {
        this.h_statistics = h_statistics;
    }

    public StatisticsResponse getA_statistics() {
        return a_statistics;
    }

    public void setA_statistics(StatisticsResponse a_statistics) {
        this.a_statistics = a_statistics;
    }

    public List<EventResponse> getEvents() {
        return events;
    }

    public void setEvents(List<EventResponse> events) {
        this.events = events;
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(FbStatistcsEventResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

}
