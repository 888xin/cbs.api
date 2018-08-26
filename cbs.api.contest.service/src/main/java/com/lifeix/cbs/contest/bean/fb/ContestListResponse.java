package com.lifeix.cbs.contest.bean.fb;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class ContestListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = 1889822579759291841L;

    /**
     * contest list
     */
    private List<ContestResponse> contests;

    /**
     * 加载未来数据的时间起点
     */
    private String next_time;

    /**
     * 记载历史数据的时间起点
     */
    private String prev_time;

    public List<ContestResponse> getContests() {
	return contests;
    }

    public void setContests(List<ContestResponse> contests) {
	this.contests = contests;
    }

    public String getNext_time() {
	return next_time;
    }

    public void setNext_time(String next_time) {
	this.next_time = next_time;
    }

    public String getPrev_time() {
	return prev_time;
    }

    public void setPrev_time(String prev_time) {
	this.prev_time = prev_time;
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
