package com.lifeix.cbs.contest.service.spark.live;

import java.util.List;

import com.lifeix.cbs.contest.bean.fb.FbLiveWordsListResponse;
import com.lifeix.cbs.contest.bean.fb.FbLiveWordsResponse;
import com.lifeix.exception.service.L99IllegalParamsException;

public interface FbLiveWordsDubbo {

    /**
     * 批量保存文字直播
     * 
     * @param list
     * @return
     */
    public boolean saveLiveWords(List<FbLiveWordsResponse> list);

    /**
     * 根据赛事id获取文字直播(endId为空则返回全部，不为空则返回大于endId的值)
     * 
     * @param contestId
     * @return
     * @throws L99IllegalParamsException
     */
    public FbLiveWordsListResponse findLiveWordsByContestId(Long contestId, Long endId) throws L99IllegalParamsException;

}
