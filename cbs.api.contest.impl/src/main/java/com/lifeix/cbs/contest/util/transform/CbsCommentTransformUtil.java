package com.lifeix.cbs.contest.util.transform;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;

import com.l99.common.utils.EmojiUtil;
import com.lifeix.cbs.contest.bean.comment.CbsCommentResponse;
import com.lifeix.cbs.contest.dto.comment.Comment;
import com.lifeix.common.utils.TimeUtil;
import com.lifeix.exception.service.L99NetworkException;
import com.lifeix.user.beans.account.AccountResponse;

public class CbsCommentTransformUtil {
    
    public static CbsCommentResponse transformComment(Comment comment ,HashMap<Long, AccountResponse> accountHashmap) throws L99NetworkException, JSONException{
	CbsCommentResponse resp = null;
	if(comment!=null){
	    resp = new CbsCommentResponse();
	}
	resp.setId(comment.getId());
	resp.setContest_id(comment.getContestId());
	resp.setUser_id(comment.getUserId());
	//从comment中获取userId，然后获取评论用户头像，昵称
	//FullAccountResponse acs = LifeixUserApiUtil.getInstance().findFullUser(comment.getUserId().toString(), "id", false);
	AccountResponse acs = accountHashmap.get(comment.getUserId());
	resp.setUser_avatar(acs.getPhotoPath());
	resp.setUser_name(acs.getName());
	resp.setContent(EmojiUtil.decode(comment.getContent()));
	if (StringUtils.isNotEmpty(comment.getImages())) {
		String[] images = comment.getImages().split(",");
		resp.setImages(images);	   
	}
	resp.setCreate_time(TimeUtil.getUtcTimeForDate(comment.getCreateTime()));
	resp.setIpaddress(comment.getIpaddress());
	resp.setClient(comment.getClient());

	return resp;
	
    }

}
