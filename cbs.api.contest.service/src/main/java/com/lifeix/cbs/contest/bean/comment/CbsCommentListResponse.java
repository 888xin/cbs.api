package com.lifeix.cbs.contest.bean.comment;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class CbsCommentListResponse extends ListResponse implements Response {
    /**
     * 
     */
    private static final long serialVersionUID = -3896697090114496875L;
    private List<CbsCommentResponse> comments;

    public List<CbsCommentResponse> getComments() {
	return comments;
    }

    public void setComments(List<CbsCommentResponse> comments) {
	this.comments = comments;
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
