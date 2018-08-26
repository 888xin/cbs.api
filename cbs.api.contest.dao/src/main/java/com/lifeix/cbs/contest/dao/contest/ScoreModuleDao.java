package com.lifeix.cbs.contest.dao.contest;

import com.lifeix.cbs.contest.dto.contest.ScoreModule;

public interface ScoreModuleDao {

    public ScoreModule selectByContestType(Integer contestType);

    public boolean updateModuleValue(ScoreModule sm);

}
