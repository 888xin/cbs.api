package com.lifeix.cbs.contest.bean.yy;

import java.util.List;

import com.lifeix.cbs.contest.bean.contest.ContestAdResponse;
import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class YyContestListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = -5112670523603558436L;

    /**
     * yy list
     */
    private List<YyContestResponse> contests;

    /**
     * yy cup list
     */
    private List<YyCupResponse> cups;

    /**
     * yy list
     */
    private List<ContestAdResponse> ads;

    /**
     * 系统时间
     */
    private Long system_time;

    /**
     * 可用龙币
     */
    private Double money;

    /**
     * 未读数量
     */
    private Integer notify_num;

    public List<YyContestResponse> getContests() {
	return contests;
    }

    public void setContests(List<YyContestResponse> contests) {
	this.contests = contests;
    }

    public List<YyCupResponse> getCups() {
	return cups;
    }

    public void setCups(List<YyCupResponse> cups) {
	this.cups = cups;
    }

    public List<ContestAdResponse> getAds() {
	return ads;
    }

    public void setAds(List<ContestAdResponse> ads) {
	this.ads = ads;
    }

    public Long getSystem_time() {
	return system_time;
    }

    public void setSystem_time(Long system_time) {
	this.system_time = system_time;
    }

    public Double getMoney() {
	return money;
    }

    public void setMoney(Double money) {
	this.money = money;
    }

    public Integer getNotify_num() {
	return notify_num;
    }

    public void setNotify_num(Integer notify_num) {
	this.notify_num = notify_num;
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
