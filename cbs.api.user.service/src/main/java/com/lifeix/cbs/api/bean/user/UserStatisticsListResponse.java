package com.lifeix.cbs.api.bean.user;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class UserStatisticsListResponse extends ListResponse implements Response {
    private static final long serialVersionUID = -8500601696903328374L;

    private List<UserStatisticsResponse> ranks;

    private Integer year;

    private Integer month;

    private Integer week;

    private Integer day;

    /**
     * 当前用户id
     */
    private Long user_id;

    /**
     * 当前用户排名
     */
    private Long rank;

    /**
     * 当前用户头像
     */
    private String head;

    /**
     * 当前用户名
     */
    private String name;

    public UserStatisticsListResponse() {
	super();
    }

    public List<UserStatisticsResponse> getRanks() {
	return ranks;
    }

    public void setRanks(List<UserStatisticsResponse> ranks) {
	this.ranks = ranks;
    }

    public Integer getYear() {
	return year;
    }

    public void setYear(Integer year) {
	this.year = year;
    }

    public Integer getMonth() {
	return month;
    }

    public void setMonth(Integer month) {
	this.month = month;
    }

    public Integer getWeek() {
	return week;
    }

    public void setWeek(Integer week) {
	this.week = week;
    }

    public Integer getDay() {
	return day;
    }

    public void setDay(Integer day) {
	this.day = day;
    }

    public Long getUser_id() {
	return user_id;
    }

    public void setUser_id(Long user_id) {
	this.user_id = user_id;
    }

    public Long getRank() {
	return rank;
    }

    public void setRank(Long rank) {
	this.rank = rank;
    }

    public String getHead() {
	return head;
    }

    public void setHead(String head) {
	this.head = head;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    @Override
    public String getObjectName() {
	return null;
    }
}
