package com.lifeix.cbs.contest.impl.spark.live;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.contest.bean.bb.BbLiveWordsListResponse;
import com.lifeix.cbs.contest.bean.bb.BbLiveWordsResponse;
import com.lifeix.cbs.contest.dao.bb.BbLiveWordsDao;
import com.lifeix.cbs.contest.dto.bb.BbLiveWords;
import com.lifeix.cbs.contest.service.spark.live.BbLiveWordsDubbo;
import com.lifeix.cbs.contest.util.transform.ContestTransformUtil;
import com.lifeix.exception.service.L99IllegalParamsException;

@Service("bbLiveWordsDubbo")
public class BbLiveWordsDubboImpl implements BbLiveWordsDubbo {

    @Autowired
    private BbLiveWordsDao bbLiveWordsDao;

    @Override
    public boolean saveLiveWords(List<BbLiveWordsResponse> list) {
	List<BbLiveWords> liveWordsList = new ArrayList<BbLiveWords>(list.size());
	for (BbLiveWordsResponse resp : list) {
	    BbLiveWords liveWords = new BbLiveWords();
	    liveWords.setAwayScores(resp.getAway_scores());
	    liveWords.setClock(resp.getClock());
	    liveWords.setContestId(resp.getContest_id());
	    liveWords.setEventType(resp.getEvent_type());
	    liveWords.setHomeScores(resp.getHome_scores());
	    liveWords.setLocation(resp.getLocation());
	    liveWords.setPeriodNumber(resp.getPeriod_number());
	    liveWords.setPeriodType(resp.getPeriod_type());
	    liveWords.setPhraseId(resp.getPhrase_id());
	    liveWords.setTeam(resp.getTeam());
	    liveWords.setTextContent(resp.getText_content());
	    liveWords.setDisabled(resp.getDisabled());
	    liveWords.setSequence(resp.getSequence());
	    liveWordsList.add(liveWords);
	}
	return bbLiveWordsDao.saveLiveWords(liveWordsList);
    }

    @Override
    public BbLiveWordsListResponse findLiveWordsByContestId(Long contestId, Long endId, Boolean newFlag)
	    throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(contestId);
	List<BbLiveWords> list = null;
	if (Boolean.TRUE.equals(newFlag)) {
	    list = bbLiveWordsDao.selectByContestIdNew(contestId, endId);
	} else {
	    list = bbLiveWordsDao.selectByContestId(contestId, endId);
	}
	BbLiveWordsListResponse listResponse = new BbLiveWordsListResponse();
	if (list == null || list.size() == 0)
	    return listResponse;
	List<BbLiveWordsResponse> respList = new ArrayList<BbLiveWordsResponse>(list.size());
	for (BbLiveWords bbLiveWords : list) {
	    BbLiveWordsResponse resp = ContestTransformUtil.transformBbLiveWords(bbLiveWords);
	    if (resp != null)
		respList.add(resp);
	}
	if (Boolean.TRUE.equals(newFlag)) {
	    listResponse.setEndId(list.get(0).getSequence().longValue());
	} else {
	    listResponse.setEndId(list.get(0).getPhraseId());
	}
	listResponse.setLive_words(respList);
	return listResponse;
    }

}
