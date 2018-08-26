package com.lifeix.cbs.api.bean.gold;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class GoldListResponse extends ListResponse implements Response {

  
    private static final long serialVersionUID = -4080733435245112593L;

    private List<GoldResponse> goldResponses;
    
    public List<GoldResponse> getGoldResponses() {
        return goldResponses;
    }

    public void setGoldResponses(List<GoldResponse> goldResponses) {
        this.goldResponses = goldResponses;
    }

    @Override
    public String getObjectName() {
	return null;
    }

   

}
