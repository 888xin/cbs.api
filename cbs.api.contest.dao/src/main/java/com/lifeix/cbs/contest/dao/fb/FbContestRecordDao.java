package com.lifeix.cbs.contest.dao.fb;

import java.util.List;

import com.lifeix.cbs.contest.dto.fb.FbContestRecord;

public interface FbContestRecordDao {

    public List<FbContestRecord> selectHeadToHeadRecord(Long homeTeam, Long awayTeam, Integer limit);

    public boolean saveContestRecord(List<FbContestRecord> list);

    public List<FbContestRecord> selectAnalysisNeeded(Long maxId, Integer limit);

    public boolean updateWinTypes(List<FbContestRecord> list);

    public List<FbContestRecord> selectTeamRecord(Long teamId, Integer limit);

}
