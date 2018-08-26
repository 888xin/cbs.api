package com.lifeix.cbs.contest.dao.bb;

import java.util.List;

import com.lifeix.cbs.contest.dto.bb.BbContestRecord;

public interface BbContestRecordDao {

    public List<BbContestRecord> selectHeadToHeadRecord(Long homeTeam, Long awayTeam, Integer limit);

    public boolean saveContestRecord(List<BbContestRecord> list);

    public List<BbContestRecord> selectAnalysisNeeded(Long maxId, Integer limit);

    public boolean updateWinTypes(List<BbContestRecord> list);

    public List<BbContestRecord> selectTeamRecord(Long teamId, Integer limit);

}
