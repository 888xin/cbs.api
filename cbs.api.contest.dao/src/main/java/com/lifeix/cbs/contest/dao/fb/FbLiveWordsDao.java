package com.lifeix.cbs.contest.dao.fb;

import java.util.List;

import com.lifeix.cbs.contest.dto.fb.FbLiveWords;

public interface FbLiveWordsDao {

    public List<FbLiveWords> selectByContestId(Long contestId, Long endId);

    public boolean saveLiveWords(List<FbLiveWords> list);

}
