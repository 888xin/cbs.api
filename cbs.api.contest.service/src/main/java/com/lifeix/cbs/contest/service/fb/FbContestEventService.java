package com.lifeix.cbs.contest.service.fb;

import com.lifeix.cbs.contest.bean.fb.ext.FbEventsListResponse;

public interface FbContestEventService {
    public FbEventsListResponse findEventsByContestId(Long contestId);
}
