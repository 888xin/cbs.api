package com.lifeix.cbs.contest.dao.bb;

import java.util.List;

import com.lifeix.cbs.contest.dto.bb.BbContestExt;

public interface BbContestExtDao {

    public BbContestExt selectById(Long contestId);

    public boolean saveContestExt(List<BbContestExt> list);
}
