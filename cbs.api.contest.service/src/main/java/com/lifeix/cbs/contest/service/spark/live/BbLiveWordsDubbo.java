package com.lifeix.cbs.contest.service.spark.live;

import java.util.List;

import com.lifeix.cbs.contest.bean.bb.BbLiveWordsListResponse;
import com.lifeix.cbs.contest.bean.bb.BbLiveWordsResponse;
import com.lifeix.cbs.contest.bean.fb.FbLiveWordsListResponse;
import com.lifeix.exception.service.L99IllegalParamsException;

public interface BbLiveWordsDubbo {

    /**
     * 批量保存文字直播
     * 
     * @param list
     * @return
     */
    public boolean saveLiveWords(List<BbLiveWordsResponse> list);
    /**
     * 获取文字直播
     * @param contestId
     * @param endId
     * @return
     * @throws L99IllegalParamsException 
     */
    public BbLiveWordsListResponse findLiveWordsByContestId(Long contestId, Long endId, Boolean newFlag) throws L99IllegalParamsException;

}
