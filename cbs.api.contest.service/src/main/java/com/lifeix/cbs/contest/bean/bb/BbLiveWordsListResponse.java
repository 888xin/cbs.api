package com.lifeix.cbs.contest.bean.bb;

import java.util.List;
import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class BbLiveWordsListResponse extends ListResponse implements Response {

    /**
     * 
     */
    private static final long serialVersionUID = 5354508135512623800L;
    private List<BbLiveWordsResponse> live_words;

    public List<BbLiveWordsResponse> getLive_words() {
	return live_words;
    }

    public void setLive_words(List<BbLiveWordsResponse> live_words) {
	this.live_words = live_words;
    }

    @Override
    public String getObjectName() {
	// TODO Auto-generated method stub
	return null;
    }

}
