package com.lifeix.cbs.contest.bean.bb.ext;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class BbPlayerListResponse extends ListResponse implements Response {

    /**
     * 
     */
    private static final long serialVersionUID = 1369908801860414211L;

    private List<BbPlayerResponse> players;

    @Override
    public String getObjectName() {
	return null;
    }

    public List<BbPlayerResponse> getPlayers() {
	return players;
    }

    public void setPlayers(List<BbPlayerResponse> players) {
	this.players = players;
    }

}
