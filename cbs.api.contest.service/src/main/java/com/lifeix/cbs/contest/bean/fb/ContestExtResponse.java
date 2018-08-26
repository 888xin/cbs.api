package com.lifeix.cbs.contest.bean.fb;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.cbs.contest.bean.fb.ext.EventResponse;
import com.lifeix.cbs.contest.bean.fb.ext.RefereeResponse;
import com.lifeix.cbs.contest.bean.fb.ext.ScoreResponse;
import com.lifeix.cbs.contest.bean.fb.ext.TeamExtResponse;
import com.lifeix.cbs.contest.bean.fb.ext.VenueResponse;
import com.lifeix.user.beans.Response;

public class ContestExtResponse implements JsonSerializer<ContestExtResponse>, Response {

    private static final long serialVersionUID = -6896089978355453945L;

    /**
     * 赛事ID
     */
    private Long contestId;

    /**
     * 第三方赛事ID
     */
    private Long targetId;

    /**
     * 主队扩展信息
     */
    private TeamExtResponse h_t_ext;

    /**
     * 客队扩展信息
     */
    private TeamExtResponse a_t_ext;

    /**
     * 赛事状态
     */
    private Integer status;

    /**
     * 比赛各阶段比分
     */
    private List<ScoreResponse> scores;

    /**
     * 事件
     */
    private List<EventResponse> events;

    /**
     * 裁判
     */
    private RefereeResponse referee;

    /**
     * 比赛场地
     */
    private VenueResponse venue;

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(ContestExtResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

    public List<EventResponse> getEvents() {
	return events;
    }

    public void setEvents(List<EventResponse> events) {
	this.events = events;
    }

    public RefereeResponse getReferee() {
	return referee;
    }

    public void setReferee(RefereeResponse referee) {
	this.referee = referee;
    }

    public VenueResponse getVenue() {
	return venue;
    }

    public void setVenue(VenueResponse venue) {
	this.venue = venue;
    }

    public List<ScoreResponse> getScores() {
	return scores;
    }

    public void setScores(List<ScoreResponse> scores) {
	this.scores = scores;
    }

    public Integer getStatus() {
	return status;
    }

    public void setStatus(Integer status) {
	this.status = status;
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

    public Long getContestId() {
	return contestId;
    }

    public void setContestId(Long contestId) {
	this.contestId = contestId;
    }

    public Long getTargetId() {
	return targetId;
    }

    public void setTargetId(Long targetId) {
	this.targetId = targetId;
    }

}
