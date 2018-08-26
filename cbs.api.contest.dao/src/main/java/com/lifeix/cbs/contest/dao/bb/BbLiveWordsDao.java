package com.lifeix.cbs.contest.dao.bb;

import java.util.List;

import com.lifeix.cbs.contest.dto.bb.BbLiveWords;

public interface BbLiveWordsDao {

    public List<BbLiveWords> selectByContestId(Long contestId, Long endId);

    public boolean saveLiveWords(List<BbLiveWords> list);

    public List<BbLiveWords> selectByContestIdNew(Long contestId, Long endId);

}
