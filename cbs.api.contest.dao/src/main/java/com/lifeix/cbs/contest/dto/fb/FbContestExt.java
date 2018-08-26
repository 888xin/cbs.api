package com.lifeix.cbs.contest.dto.fb;

import java.io.Serializable;

public class FbContestExt implements Serializable {

    private static final long serialVersionUID = -8991527363577124824L;

    private Long contestId;

    private Long targetId;

    private String homeTeamExt;

    private String awayTeamExt;

    private String lineups;

    private String scores;

    private Integer status;

    private String goals;

    private String penalties;

    private String cards;

    private String substitutions;

    private String referee;

    private String venue;

    public FbContestExt() {
	super();
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

    public String getHomeTeamExt() {
	return homeTeamExt;
    }

    public void setHomeTeamExt(String homeTeamExt) {
	this.homeTeamExt = homeTeamExt;
    }

    public String getAwayTeamExt() {
	return awayTeamExt;
    }

    public void setAwayTeamExt(String awayTeamExt) {
	this.awayTeamExt = awayTeamExt;
    }

    public String getScores() {
	return scores;
    }

    public void setScores(String scores) {
	this.scores = scores;
    }

    public Integer getStatus() {
	return status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    public String getGoals() {
	return goals;
    }

    public void setGoals(String goals) {
	this.goals = goals;
    }

    public String getPenalties() {
	return penalties;
    }

    public void setPenalties(String penalties) {
	this.penalties = penalties;
    }

    public String getCards() {
	return cards;
    }

    public void setCards(String cards) {
	this.cards = cards;
    }

    public String getSubstitutions() {
	return substitutions;
    }

    public void setSubstitutions(String substitutions) {
	this.substitutions = substitutions;
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

    public String getLineups() {
	return lineups;
    }

    public void setLineups(String lineups) {
	this.lineups = lineups;
    }

}
