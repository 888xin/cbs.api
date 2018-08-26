package com.lifeix.cbs.contest.bean.circle;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class FriendCircleListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = 6103839352059001803L;

    private List<FriendCircleResponse> contents;
    
    private List<FriendCircleResponse> docs;
    private Integer numFound = 0;
    /**
     * 消息数量
     */
    private Integer msgNum = 0;

    public List<FriendCircleResponse> getContents() {
	return contents;
    }

    public void setContents(List<FriendCircleResponse> contents) {
	this.contents = contents;
    }

    public Integer getMsgNum() {
	return msgNum;
    }

    public void setMsgNum(Integer msgNum) {
	this.msgNum = msgNum;
    }

    public List<FriendCircleResponse> getDocs() {
        return docs;
    }

    public Integer getNumFound() {
        return numFound;
    }

    public void setNumFound(Integer numFound) {
        this.numFound = numFound;
    }

    public void setDocs(List<FriendCircleResponse> docs) {
        this.docs = docs;
    }

    @Override
    public String getObjectName() {
	return null;
    }
}
