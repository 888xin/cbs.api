package com.lifeix.cbs.contest.impl.spark.contest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.contest.bean.fb.ContestResponse;
import com.lifeix.cbs.contest.bean.fb.ext.RecordResponse;
import com.lifeix.cbs.contest.dao.fb.FbContestRecordDao;
import com.lifeix.cbs.contest.dto.fb.FbContestRecord;
import com.lifeix.cbs.contest.service.spark.contest.FbContestRecordDubbo;
import com.lifeix.cbs.contest.util.transform.ContestTransformUtil;

public class FbContestRecordDubboImpl implements FbContestRecordDubbo {

    @Autowired
    private FbContestRecordDao fbContestRecordDao;

    @Override
    public boolean saveContestRecord(List<ContestResponse> respList) {
	if (respList == null || respList.size() == 0)
	    return false;
	List<FbContestRecord> list = new ArrayList<FbContestRecord>(respList.size());
	for (ContestResponse resp : respList) {
	    FbContestRecord contestRecord = new FbContestRecord();
	    contestRecord.setAwayScores(resp.getAway_scores());
	    contestRecord.setAwayTeam(resp.getAway_team());
	    contestRecord.setCupId(resp.getCup_id());
	    contestRecord.setCupName(resp.getCup_name());
	    contestRecord.setHomeScores(resp.getHome_scores());
	    contestRecord.setHomeTeam(resp.getHome_team());
	    contestRecord.setStartTime(CbsTimeUtils.getDateByUtcFormattedDate(resp.getStart_time()));
	    contestRecord.setTargetId(resp.getTarget_id());
	    list.add(contestRecord);
	}
	return fbContestRecordDao.saveContestRecord(list);
    }

    @Override
    public List<RecordResponse> selectAnalysisNeeded(Long maxId, Integer limit) {
	List<FbContestRecord> list = fbContestRecordDao.selectAnalysisNeeded(maxId, limit);
	List<RecordResponse> respList = new ArrayList<RecordResponse>();
	if (list != null && list.size() > 0) {
	    for (FbContestRecord record : list) {
		RecordResponse resp = ContestTransformUtil.transformFbContestRecord(record);
		if (resp != null)
		    respList.add(resp);
	    }
	}
	return respList;
    }

    @Override
    public boolean updateWinTypes(List<RecordResponse> respList) {
	if (respList == null || respList.size() == 0)
	    return false;
	List<FbContestRecord> list = new ArrayList<FbContestRecord>(respList.size());
	for (RecordResponse resp : respList) {
	    FbContestRecord contestRecord = new FbContestRecord();
	    contestRecord.setAwayScores(resp.getAway_scores());
	    contestRecord.setAwayTeam(resp.getAway_team());
	    contestRecord.setCupId(resp.getCup_id());
	    contestRecord.setCupName(resp.getCup_name());
	    contestRecord.setHomeScores(resp.getHome_scores());
	    contestRecord.setHomeTeam(resp.getHome_team());
	    contestRecord.setStartTime(CbsTimeUtils.getDateByUtcFormattedDate(resp.getStart_time()));
	    contestRecord.setTargetId(resp.getTarget_id());
	    contestRecord.setWinType(resp.getWin_type());
	    contestRecord.setJcWinType(resp.getJc_win_type());
	    list.add(contestRecord);
	}
	return fbContestRecordDao.updateWinTypes(list);
    }
}
