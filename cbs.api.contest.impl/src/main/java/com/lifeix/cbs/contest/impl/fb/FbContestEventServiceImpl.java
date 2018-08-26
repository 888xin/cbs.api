package com.lifeix.cbs.contest.impl.fb;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.contest.bean.fb.ContestExtResponse;
import com.lifeix.cbs.contest.bean.fb.ext.EventResponse;
import com.lifeix.cbs.contest.bean.fb.ext.FbEventsListResponse;
import com.lifeix.cbs.contest.dao.fb.FbContestExtDao;
import com.lifeix.cbs.contest.dto.fb.FbContestExt;
import com.lifeix.cbs.contest.impl.ImplSupport;
import com.lifeix.cbs.contest.service.fb.FbContestEventService;
import com.lifeix.cbs.contest.util.transform.ContestTransformUtil;
@Service("fbContestEventService")
public class FbContestEventServiceImpl extends ImplSupport implements FbContestEventService {
    @Autowired
    private FbContestExtDao fbContestExtDao;
    @Override
    public FbEventsListResponse findEventsByContestId(Long contestId) {
	FbContestExt contestExt = fbContestExtDao.selectById(contestId);
	ContestExtResponse contestRe = ContestTransformUtil.transformFbContestExt(contestExt);
	
	FbEventsListResponse fb= new FbEventsListResponse();
         if(contestRe==null){
	    fb.setEvents(new ArrayList<EventResponse>());
	}else{
		fb.setEvents(contestRe.getEvents());
	}
	return fb;
    }

}
