package com.lifeix.cbs.contest.bean.fb;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.cbs.contest.bean.fb.ext.StatisticsResponse;
import com.lifeix.user.beans.Response;
/**
 * 比赛技术统计
 * @author lifeix
 *
 */
public class FbStatisticsResponse implements JsonSerializer<FbStatisticsResponse>, Response {
    /**
     * 
     */
    private static final long serialVersionUID = -8498540440321980177L;

    /**
     * 主队实时技术统计
     */
    private StatisticsResponse h_statistics;

    /**
     * 客队实时技术统计
     */
    private StatisticsResponse a_statistics;
    
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

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(FbStatisticsResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

}
