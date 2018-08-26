package com.lifeix.cbs.contest.util.transform;

import java.util.Map;

import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.contest.bean.circle.FriendCircleCommResponse;
import com.lifeix.cbs.contest.bean.circle.FriendCircleResponse;
import com.lifeix.cbs.contest.dto.circle.FriendCircleComment;

/**
 * DTO 转 VO 工具类
 * 
 * @author jacky
 * 
 */
public class FriCirCommentTransformUtil {

    /**
     * 转换猜友圈评论
     * 
     * @param comment
     * @param commUserResponse
     * @param reUserResponse
     * @return
     */
    public static FriendCircleCommResponse transformFriendCircleComment(FriendCircleComment comment,
	    CbsUserResponse commUserResponse, CbsUserResponse reUserResponse) {
	if (comment == null || commUserResponse == null) {
	    return null;
	}
	FriendCircleCommResponse resp = new FriendCircleCommResponse();
	resp.setId(comment.getId());
	resp.setContent_id(comment.getContentId());
	resp.setComm_userid(comment.getCommUserId());
	resp.setUser_avatar(commUserResponse.getHead());
	resp.setUser_name(commUserResponse.getName());
	resp.setContent(comment.getContent());
	resp.setImage(comment.getImage());
	if (reUserResponse != null) {
	    resp.setRe_user(reUserResponse.getName());
	    resp.setRe_userid(reUserResponse.getUser_id());
	    resp.setRe_user_avatar(reUserResponse.getHead());
	}
	resp.setRe_content(comment.getReContent());
	resp.setRe_image(comment.getReImage());
	resp.setCreate_time(CbsTimeUtils.getContentTimeDiff(CbsTimeUtils.getUtcTimeForDate(comment.getCreateTime())));
	resp.setIpaddress(hideIpaddress(comment.getIpaddress()));
	resp.setSource(comment.getSource());
	resp.setStatus(comment.getStatus());
	return resp;
    }

    /**
     * 转换猜友圈评论(只转换主题和回复)
     * 
     * @param comment
     * @param commUserResponse
     * @param reUserResponse
     * @return
     */
    public static FriendCircleCommResponse transformFriendCircleComment(FriendCircleComment comment,
	    CbsUserResponse commUserResponse, String theme, String theme_img, Map<Long, FriendCircleResponse> resultMap) {
	if (comment == null || commUserResponse == null) {
	    return null;
	}
	FriendCircleCommResponse resp = new FriendCircleCommResponse();
	resp.setId(comment.getId());
	resp.setContent_id(comment.getContentId());
	resp.setCircle_userid(comment.getCircleUserId());
	resp.setUser_avatar(commUserResponse.getHead());
	resp.setUser_name(commUserResponse.getName());
	resp.setContent(comment.getContent());
	resp.setImage(comment.getImage());
	resp.setComm_userid(comment.getCommUserId());
	resp.setRe_userid(comment.getReUserId());
	resp.setCreate_time(CbsTimeUtils.getContentTimeDiff(CbsTimeUtils.getUtcTimeForDate(comment.getCreateTime())));
	resp.setTheme(theme);
	resp.setTheme_img(theme_img);
	if (resultMap != null) {
	    resp.setType(resultMap.get(comment.getContentId()).getFriendType());
	}
	return resp;
    }

    public static String hideIpaddress(String ipaddress) {
	if (ipaddress == null) {
	    return null;
	}
	String[] is = ipaddress.split(".");
	if (is.length < 3) {
	    return null;
	}

	return String.format("%s.%s.%s.*", is[0], is[1], is[2]);
    }

}
