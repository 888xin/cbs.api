package com.lifeix.cbs.contest.impl.spark.live;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.contest.bean.fb.FbLiveWordsListResponse;
import com.lifeix.cbs.contest.bean.fb.FbLiveWordsResponse;
import com.lifeix.cbs.contest.dao.fb.FbLiveWordsDao;
import com.lifeix.cbs.contest.dto.fb.FbLiveWords;
import com.lifeix.cbs.contest.service.spark.live.FbLiveWordsDubbo;
import com.lifeix.cbs.contest.util.transform.ContestTransformUtil;
import com.lifeix.exception.service.L99IllegalParamsException;


@Service("fbLiveWordsDubbo")
public class FbLiveWordsDubboImpl implements FbLiveWordsDubbo {

    @Autowired
    private FbLiveWordsDao fbLiveWordsDao;

    @Override
    public boolean saveLiveWords(List<FbLiveWordsResponse> list) {
	List<FbLiveWords> liveWordsList = new ArrayList<FbLiveWords>(list.size());
	for (FbLiveWordsResponse resp : list) {
	    FbLiveWords liveWords = new FbLiveWords();
	    liveWords.setContestId(resp.getContest_id());
	    liveWords.setDisabled(resp.getDisabled());
	    liveWords.setInjuryTime(resp.getInjury_time());
	    liveWords.setPhraseId(resp.getPhrase_id());
	    liveWords.setTargetId(resp.getTarget_id());
	    liveWords.setTargetPhraseId(resp.getTarget_phrase_id());
	    liveWords.setTextContent(resp.getText_content());
	    liveWords.setTime(resp.getTime());
	    liveWords.setType(resp.getType());
	    liveWordsList.add(liveWords);
	}
	return fbLiveWordsDao.saveLiveWords(liveWordsList);
    }

    @Override
    public FbLiveWordsListResponse findLiveWordsByContestId(Long contestId, Long endId) throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(contestId);
	List<FbLiveWords> list = fbLiveWordsDao.selectByContestId(contestId, endId);
	FbLiveWordsListResponse listResponse = new FbLiveWordsListResponse();
	if (list == null || list.size() == 0)
	    return listResponse;
	List<FbLiveWordsResponse> respList = new ArrayList<FbLiveWordsResponse>(list.size());
	for (FbLiveWords fbLiveWords : list) {
	    FbLiveWordsResponse resp = ContestTransformUtil.transformFbLiveWords(fbLiveWords);
	    if (resp != null)
		respList.add(resp);
	}
	listResponse.setEndId(list.get(0).getPhraseId());
	listResponse.setLive_words(respList);
	return listResponse;
    }

}
