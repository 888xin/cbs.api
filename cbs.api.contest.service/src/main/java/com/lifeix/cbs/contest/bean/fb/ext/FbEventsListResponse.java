package com.lifeix.cbs.contest.bean.fb.ext;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class FbEventsListResponse extends ListResponse implements Response {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1051286594617989770L;
    private List<EventResponse> events;

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

}
