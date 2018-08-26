package com.lifeix.cbs.contest.dao.fb;

import java.util.List;

import com.lifeix.cbs.contest.dto.fb.FbContestExt;

public interface FbContestExtDao {

    public FbContestExt selectById(Long contestId);

    public boolean saveContestExt(List<FbContestExt> list);
}
