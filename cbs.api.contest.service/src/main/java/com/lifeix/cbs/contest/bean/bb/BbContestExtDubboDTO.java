package com.lifeix.cbs.contest.bean.bb;

import java.io.Serializable;

public class BbContestExtDubboDTO implements Serializable {

    private static final long serialVersionUID = -7731041657003394079L;

    private Long contestId;

    private String lineups;

    private String referee;

    private String venue;

    public BbContestExtDubboDTO() {
	super();
    }

    public Long getContestId() {
	return contestId;
    }

    public void setContestId(Long contestId) {
	this.contestId = contestId;
    }

    public String getLineups() {
	return lineups;
    }

    public void setLineups(String lineups) {
	this.lineups = lineups;
    }

    public String getReferee() {
	return referee;
    }

    public void setReferee(String referee) {
	this.referee = referee;
    }

    public String getVenue() {
	return venue;
    }

    public void setVenue(String venue) {
	this.venue = venue;
    }

}
