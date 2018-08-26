package com.lifeix.cbs.contest.bean.circle;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

/**
 * 评论列表
 * 
 * @author jacky
 *
 */
public class FriendCircleCommListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = 5556325484889857163L;

    private List<FriendCircleCommResponse> comments;

    private Long comment_counts;

    public List<FriendCircleCommResponse> getComments() {
	return comments;
    }

    public void setComments(List<FriendCircleCommResponse> comments) {
	this.comments = comments;
    }

    @Override
    public String getObjectName() {
	return "comments";
    }

    public Long getComment_counts() {
	return comment_counts;
    }

    public void setComment_counts(Long comment_counts) {
	this.comment_counts = comment_counts;
    }

}
