package com.lifeix.cbs.contest.impl.fb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.contest.bean.fb.ext.AboutRecordListResponse;
import com.lifeix.cbs.contest.bean.fb.ext.RecordListResponse;
import com.lifeix.cbs.contest.bean.fb.ext.RecordResponse;
import com.lifeix.cbs.contest.dao.fb.FbContestRecordDao;
import com.lifeix.cbs.contest.dao.fb.FbTeamDao;
import com.lifeix.cbs.contest.dto.bb.BbContestRecord;
import com.lifeix.cbs.contest.dto.fb.FbContest;
import com.lifeix.cbs.contest.dto.fb.FbContestRecord;
import com.lifeix.cbs.contest.dto.fb.FbTeam;
import com.lifeix.cbs.contest.impl.ImplSupport;
import com.lifeix.cbs.contest.service.fb.FbContestRecordService;
import com.lifeix.cbs.contest.util.transform.ContestTransformUtil;
import com.lifeix.exception.service.L99IllegalParamsException;

@Service("fbContestRecordService")
public class FbContestRecordServiceImpl extends ImplSupport implements FbContestRecordService {

    @Autowired
    private FbContestRecordDao fbContestRecordDao;
    
    @Autowired
    private FbTeamDao fbTeamDao;

    @Override
    public RecordListResponse selectTeamRecord(Long teamId, Integer limit) throws L99IllegalParamsException {

	ParamemeterAssert.assertDataNotNull(teamId, limit);

	RecordListResponse listResponse = new RecordListResponse();
	List<FbContestRecord> list = fbContestRecordDao.selectTeamRecord(teamId, limit);
	if (list == null || list.size() == 0)
	    return listResponse;
	List<RecordResponse> respList = new ArrayList<RecordResponse>(list.size());
	for (FbContestRecord record : list) {
	    RecordResponse resp = ContestTransformUtil.transformFbContestRecord(record);
	    if (resp != null)
		respList.add(resp);
	}
	listResponse.setRecords(respList);
	return listResponse;
    }

    @Override
    public AboutRecordListResponse selectAboutRecord(Long homeTeamId, Long awayTeamId, Integer limit)
	    throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(homeTeamId, awayTeamId, limit);
	AboutRecordListResponse listResponse = new AboutRecordListResponse();
	List<FbContestRecord> homeAwayRecords = fbContestRecordDao.selectHeadToHeadRecord(homeTeamId, awayTeamId, limit);
	List<FbContestRecord> homeRecords = fbContestRecordDao.selectTeamRecord(homeTeamId, limit);
	List<FbContestRecord> awayRecords = fbContestRecordDao.selectTeamRecord(awayTeamId, limit);
	
	
	
	List<Long> teamIds = new ArrayList<Long>();
	for (FbContestRecord fr : homeRecords) {
	    teamIds.add(fr.getHomeTeam());
	    teamIds.add(fr.getAwayTeam());
	}
	
	for (FbContestRecord fr : awayRecords) {
	    teamIds.add(fr.getHomeTeam());
	    teamIds.add(fr.getAwayTeam());
	}
	
	Map<Long, FbTeam> teams = fbTeamDao.findFbTeamMapByIds(teamIds);
	
	
	List<RecordResponse> homeRespList = new ArrayList<RecordResponse>(homeRecords.size());
	for (FbContestRecord record : homeRecords) {
	    FbTeam fbHomeTeam = teams.get(record.getHomeTeam());
	    FbTeam fbAwayTeam = teams.get(record.getAwayTeam());
	    homeRespList.add(ContestTransformUtil.transformFbContestRecord(record,fbHomeTeam,fbAwayTeam));
	}

	List<RecordResponse> homeAwayRespList = new ArrayList<RecordResponse>(homeAwayRecords.size());
	for (FbContestRecord record : homeAwayRecords) {
	    RecordResponse resp = ContestTransformUtil.transformFbContestRecord(record,teams.get(record.getHomeTeam()),teams.get(record.getAwayTeam()));
	    if (resp != null)
		homeAwayRespList.add(resp);
	}
	List<RecordResponse> awayRespList = new ArrayList<RecordResponse>(awayRecords.size());
	for (FbContestRecord record : awayRecords) {
	    RecordResponse resp =  ContestTransformUtil.transformFbContestRecord(record,teams.get(record.getHomeTeam()),teams.get(record.getAwayTeam()));
	    if (resp != null)
		awayRespList.add(resp);
	}
	listResponse.setHome_records(homeRespList);
	listResponse.setAway_records(awayRespList);
	listResponse.setHome_away_records(homeAwayRespList);
	return listResponse;

    }

}
