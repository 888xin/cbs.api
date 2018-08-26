package com.lifeix.cbs.contest.impl.settle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestType;
import com.lifeix.cbs.api.service.user.CbsUserService;
import com.lifeix.cbs.contest.bean.fb.ContestResponse;
import com.lifeix.cbs.contest.bean.settle.UserSettleLogListResponse;
import com.lifeix.cbs.contest.bean.settle.UserSettleLogResponse;
import com.lifeix.cbs.contest.dao.settle.UserSettleLogDao;
import com.lifeix.cbs.contest.dto.settle.UserSettleLog;
import com.lifeix.cbs.contest.impl.ImplSupport;
import com.lifeix.cbs.contest.service.bb.BbContestService;
import com.lifeix.cbs.contest.service.fb.FbContestService;
import com.lifeix.cbs.contest.service.settle.SettleLogService;
import com.lifeix.cbs.contest.util.UserSettleLogTransformUtils;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;

@Service("settleLogService")
public class SettleLogServiceImpl extends ImplSupport implements SettleLogService {

    @Autowired
    BbContestService bbContestService;
    @Autowired
    FbContestService fbContestService;
    @Autowired
    UserSettleLogDao userSettleLogDao;
    @Autowired
    CbsUserService cbsUserService;

    @Override
    public UserSettleLogListResponse getUserSettleLogs(Long longNO, Integer nowPage, String startTime, String endTime,
	    Integer limit) throws L99IllegalParamsException, JSONException, L99NetworkException {

	//ParamemeterAssert.assertDataNotNull(longNO);
	UserSettleLogListResponse rep = new UserSettleLogListResponse();
	CbsUserResponse user = cbsUserService.getCbsUserByLongNo(longNO);
	Integer userId = null;
	if (user != null && user.getUser_id() != null) {
	    userId = user.getUser_id().intValue();
	}
	
	if (nowPage == null) { // 默认第一页
	    nowPage = 1;
	}

	if (startTime == null) { // 默认当天
	    startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}

	if (endTime == null) {
	    endTime = startTime;
	}

	List<UserSettleLogResponse> logs = new ArrayList<UserSettleLogResponse>();

	List<UserSettleLog> userSettleLogs = userSettleLogDao.getUserSettleLogs(userId, nowPage, startTime, endTime,
	        limit + 1);
	if (userSettleLogs == null || userSettleLogs.isEmpty()) {
	    return rep;
	}
	if (userSettleLogs.size() > limit) {
	    userSettleLogs = userSettleLogs.subList(0, limit);
	    rep.setNow_page(nowPage + 1);
	} else {
	    rep.setNow_page(-1);
	}
	rep.setLimit(limit);
	for (UserSettleLog l : userSettleLogs) {
	    try {
		UserSettleLogResponse log = UserSettleLogTransformUtils.transformUserSettleLog(l);
		CbsUserResponse logUser = cbsUserService.getCbsUserByUserId(l.getUserId(), false);
		log.setName(logUser.getName());
		log.setLong_no(logUser.getLong_no());
		Long contestId = log.getContest_id();
		Integer type = log.getContest_type();
		ContestResponse contest = null;
		if (ContestType.BASKETBALL == type) {
		    contest = bbContestService.findBbContestsById(contestId, null);

		} else if (ContestType.FOOTBALL == type) {
		    contest = fbContestService.findFbContestsById(contestId, null);
		}
		log.setContest(contest);
		logs.add(log);
	    } catch (L99IllegalDataException e) {
		LOG.info(String.format("getUserSettleLogs failed - %s", e.getMessage()));
	    }

	}
	rep.setLogs(logs);
	return rep;

    }

    @Override
    public UserSettleLogListResponse getUserSettleLog(Long userId, Integer type, Long contestId, Integer playId,
            Integer support) throws L99IllegalParamsException, JSONException, L99NetworkException, L99IllegalDataException {
	UserSettleLogListResponse rep = new UserSettleLogListResponse();
	UserSettleLog log = userSettleLogDao.getUserSettleLog(userId, type, contestId, playId, support);
	List<UserSettleLogResponse> logs = new ArrayList<UserSettleLogResponse>();
	rep.setLogs(logs);
	if(log == null){
	    return rep;
	}
	UserSettleLogResponse logRep = UserSettleLogTransformUtils.transformUserSettleLog(log);
	
	ContestResponse contest = null;
	if (ContestType.BASKETBALL == type) {
	    contest = bbContestService.findBbContestsById(contestId, null);

	} else if (ContestType.FOOTBALL == type) {
	    contest = fbContestService.findFbContestsById(contestId, null);
	}
	logRep.setContest(contest);
	
	CbsUserResponse logUser = cbsUserService.getCbsUserByUserId(log.getUserId(), false);
	logRep.setName(logUser.getName());
	logRep.setLong_no(logUser.getLong_no());
	logs.add(logRep);
	
	
	return rep;
    }
    
    @Override
    public UserSettleLogListResponse findUserSettleListById(Long id,Boolean isLongbi, int limit) {
	UserSettleLogListResponse resp = new UserSettleLogListResponse();
	List<UserSettleLog> logs = userSettleLogDao.findUserSettleListById(id,isLongbi, limit);
	List<UserSettleLogResponse> list = new ArrayList<UserSettleLogResponse>();
	resp.setLogs(list);
	if (logs == null || logs.isEmpty()) {
	    return resp;
	}
	
	for (UserSettleLog l : logs) {
	    list.add(UserSettleLogTransformUtils.transformUserSettleLog(l));
	}
	return resp;
    }

    @Override
    public UserSettleLogListResponse findUserSettleListByIds(List<Long> ids, Boolean isLongbi) {
	return null;
    }

}
