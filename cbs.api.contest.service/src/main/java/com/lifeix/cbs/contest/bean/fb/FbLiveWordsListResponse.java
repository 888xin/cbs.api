package com.lifeix.cbs.contest.bean.fb;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class FbLiveWordsListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = 3118161620809592219L;

    private List<FbLiveWordsResponse> live_words;

    @Override
    public String getObjectName() {
	return null;
    }

    public List<FbLiveWordsResponse> getLive_words() {
	return live_words;
    }

    public void setLive_words(List<FbLiveWordsResponse> live_words) {
	this.live_words = live_words;
    }

}
