package com.lifeix.cbs.contest.bean.bb;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.cbs.contest.bean.bb.ext.BbPlayerResponse;
import com.lifeix.cbs.contest.bean.bb.ext.BbRefereeResponse;
import com.lifeix.cbs.contest.bean.bb.ext.BbVenueResponse;
import com.lifeix.user.beans.Response;

public class BbContestExtResponse implements JsonSerializer<BbContestExtResponse>, Response {

    private static final long serialVersionUID = -9125103714720650192L;

    /**
     * 赛事ID
     */
    private Long contest_id;

    /**
     * 阵容
     */
    private List<BbPlayerResponse> home_lineups;

    /**
     * 阵容
     */
    private List<BbPlayerResponse> away_lineups;

    /**
     * 裁判
     */
    private List<BbRefereeResponse> referee;

    /**
     * 比赛场地
     */
    private BbVenueResponse venue;

    public BbContestExtResponse() {
	super();
    }

    @Override
    public String getObjectName() {
	return null;
    }

    @Override
    public JsonElement serialize(BbContestExtResponse src, Type typeOfSrc, JsonSerializationContext context) {
	return context.serialize(src);
    }

    public Long getContest_id() {
	return contest_id;
    }

    public void setContest_id(Long contest_id) {
	this.contest_id = contest_id;
    }

    public List<BbRefereeResponse> getReferee() {
	return referee;
    }

    public void setReferee(List<BbRefereeResponse> referee) {
	this.referee = referee;
    }

    public BbVenueResponse getVenue() {
	return venue;
    }

    public void setVenue(BbVenueResponse venue) {
	this.venue = venue;
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

}
