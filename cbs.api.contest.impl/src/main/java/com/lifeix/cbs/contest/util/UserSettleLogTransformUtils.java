package com.lifeix.cbs.contest.util;

import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.api.common.util.CommerceMath;
import com.lifeix.cbs.contest.bean.settle.UserSettleLogResponse;
import com.lifeix.cbs.contest.dto.settle.UserSettleLog;

public class UserSettleLogTransformUtils {

    public static UserSettleLogResponse transformUserSettleLog(UserSettleLog l) {
	if(l == null){
	    return null;
	}
	UserSettleLogResponse log = new UserSettleLogResponse();
	log.setBack(l.getBack());
	log.setBet(l.getBet());
	log.setContent_id(l.getContentId());
	log.setContest_id(l.getContestId());
	log.setContest_time(CbsTimeUtils.getUtcTimeForDate(l.getContestTime()));
	log.setContest_type(l.getContestType());
	log.setCreateTime(CbsTimeUtils.getUtcTimeForDate(l.getCreateTime()));
	log.setId(l.getId());
	log.setOdds(l.getOdds());
	log.setPlay_id(l.getPlayId());
	log.setStatus(l.getStatus());
	log.setSupport(l.getSupport());
	log.setUser_id(l.getUserId());
	log.setWin_gold(CommerceMath.sub(l.getBack(), l.getBet()));
	log.setLongbi(l.isLongbi());
	log.setPay_log_id(l.getPayLogId());
	return log;
    }

}
