package com.lifeix.cbs.contest.impl.bb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.contest.bean.fb.ext.AboutRecordListResponse;
import com.lifeix.cbs.contest.bean.fb.ext.RecordListResponse;
import com.lifeix.cbs.contest.bean.fb.ext.RecordResponse;
import com.lifeix.cbs.contest.dao.bb.BbContestRecordDao;
import com.lifeix.cbs.contest.dao.bb.BbTeamDao;
import com.lifeix.cbs.contest.dao.fb.FbTeamDao;
import com.lifeix.cbs.contest.dto.bb.BbContestRecord;
import com.lifeix.cbs.contest.dto.bb.BbTeam;
import com.lifeix.cbs.contest.dto.fb.FbContestRecord;
import com.lifeix.cbs.contest.dto.fb.FbTeam;
import com.lifeix.cbs.contest.impl.ImplSupport;
import com.lifeix.cbs.contest.service.bb.BbContestRecordService;
import com.lifeix.cbs.contest.util.transform.ContestTransformUtil;
import com.lifeix.exception.service.L99IllegalParamsException;

@Service("bbContestRecordService")
public class BbContestRecordServiceImpl extends ImplSupport implements BbContestRecordService {

    @Autowired
    private BbContestRecordDao bbContestRecordDao;

    @Autowired
    private BbTeamDao bbTeamDao;

    @Override
    public RecordListResponse selectTeamRecord(Long teamId, Integer limit) throws L99IllegalParamsException {

	ParamemeterAssert.assertDataNotNull(teamId, limit);

	RecordListResponse listResponse = new RecordListResponse();
	List<BbContestRecord> list = bbContestRecordDao.selectTeamRecord(teamId, limit);
	if (list == null || list.size() == 0)
	    return listResponse;
	List<RecordResponse> respList = new ArrayList<RecordResponse>(list.size());
	for (BbContestRecord record : list) {
	    RecordResponse resp = ContestTransformUtil.transformBbContestRecord(record);
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
	List<BbContestRecord> homeAwayRecords = bbContestRecordDao.selectHeadToHeadRecord(homeTeamId, awayTeamId, limit);
	List<BbContestRecord> homeRecords = bbContestRecordDao.selectTeamRecord(homeTeamId, limit);
	List<BbContestRecord> awayRecords = bbContestRecordDao.selectTeamRecord(awayTeamId, limit);

	List<Long> teamIds = new ArrayList<Long>();
	for (BbContestRecord br : homeRecords) {
	    teamIds.add(br.getHomeTeam());
	    teamIds.add(br.getAwayTeam());
	}

	for (BbContestRecord br : awayRecords) {
	    teamIds.add(br.getHomeTeam());
	    teamIds.add(br.getAwayTeam());
	}

	Map<Long, BbTeam> teams = bbTeamDao.findBbTeamMapByIds(teamIds);

	List<RecordResponse> homeRespList = new ArrayList<RecordResponse>(homeRecords.size());

	for (BbContestRecord record : homeRecords) {
	    RecordResponse resp = ContestTransformUtil.transformBbContestRecord(record, teams.get(record.getHomeTeam()),
		    teams.get(record.getAwayTeam()));
	    if (resp != null)
		homeRespList.add(resp);
	}

	List<RecordResponse> homeAwayRespList = new ArrayList<RecordResponse>(homeAwayRecords.size());
	for (BbContestRecord record : homeAwayRecords) {
	    RecordResponse resp = ContestTransformUtil.transformBbContestRecord(record, teams.get(record.getHomeTeam()),
		    teams.get(record.getAwayTeam()));
	    if (resp != null)
		homeAwayRespList.add(resp);
	}
	List<RecordResponse> awayRespList = new ArrayList<RecordResponse>(awayRecords.size());
	for (BbContestRecord record : awayRecords) {
	    RecordResponse resp = ContestTransformUtil.transformBbContestRecord(record, teams.get(record.getHomeTeam()),
		    teams.get(record.getAwayTeam()));
	    if (resp != null)
		awayRespList.add(resp);
	}
	listResponse.setHome_records(homeRespList);
	listResponse.setAway_records(awayRespList);
	listResponse.setHome_away_records(homeAwayRespList);
	return listResponse;

    }

}
