package com.lifeix.cbs.contest.impl.spark.contest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.contest.bean.fb.ContestResponse;
import com.lifeix.cbs.contest.bean.fb.ext.RecordResponse;
import com.lifeix.cbs.contest.dao.bb.BbContestRecordDao;
import com.lifeix.cbs.contest.dto.bb.BbContestRecord;
import com.lifeix.cbs.contest.service.spark.contest.BbContestRecordDubbo;
import com.lifeix.cbs.contest.util.transform.ContestTransformUtil;

public class BbContestRecordDubboImpl implements BbContestRecordDubbo {

    @Autowired
    private BbContestRecordDao bbContestRecordDao;

    @Override
    public boolean saveContestRecord(List<ContestResponse> respList) {
	if (respList == null || respList.size() == 0)
	    return false;
	List<BbContestRecord> list = new ArrayList<BbContestRecord>(respList.size());
	for (ContestResponse resp : respList) {
	    BbContestRecord contestRecord = new BbContestRecord();
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
	return bbContestRecordDao.saveContestRecord(list);
    }

    @Override
    public List<RecordResponse> selectAnalysisNeeded(Long maxId, Integer limit) {
	List<BbContestRecord> list = bbContestRecordDao.selectAnalysisNeeded(maxId, limit);
	List<RecordResponse> respList = new ArrayList<RecordResponse>();
	if (list != null && list.size() > 0) {
	    for (BbContestRecord record : list) {
		RecordResponse resp = ContestTransformUtil.transformBbContestRecord(record);
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
	List<BbContestRecord> list = new ArrayList<BbContestRecord>(respList.size());
	for (RecordResponse resp : respList) {
	    BbContestRecord contestRecord = new BbContestRecord();
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
	return bbContestRecordDao.updateWinTypes(list);
    }
}
