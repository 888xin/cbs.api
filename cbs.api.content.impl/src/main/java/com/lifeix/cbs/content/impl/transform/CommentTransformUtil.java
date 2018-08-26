package com.lifeix.cbs.content.impl.transform;

import org.apache.commons.lang.StringUtils;

import com.l99.common.utils.RegexUtil;
import com.l99.common.utils.ResolveCommentObject;
import com.l99.common.utils.StringUtil;
import com.l99.dto.account.Account;
import com.l99.pojo.comment.L99Comment;
import com.lifeix.common.utils.TimeUtil;
import com.lifeix.tiyu.content.bean.comment.CommentResponse;
import com.lifeix.user.beans.account.AccountResponse;

/**
 * 评论 DTO 转 VO
 * 
 * @author peter
 * 
 */
public class CommentTransformUtil {

    /**
     * 旧版默认头像
     */
    public final static String DAFAULT_OLD = "sharehead.jpg";

    /**
     * 默认头像
     */
    public final static String DAFAULT_AVATAR = "000/cbs-default.png";

    /**
     * Add a private constructor to hide the implicit public one.
     */
    private CommentTransformUtil() {
    }

    /**
     * 转换评论
     * 
     * @param comment
     * @return
     */
    public static CommentResponse transformComment(L99Comment comment) {
	if (comment == null) {
	    return null;
	}
	CommentResponse resp = new CommentResponse();
	resp.setComment_id(comment.getCommentId());
	resp.setRoot_id(comment.getRootId());
	resp.setAccount_id(comment.getAccountId());
	resp.setCreate_time(TimeUtil.getUtcTimeForDate(comment.getCreateTime()));
	resp.setParent_id(comment.getParentId());
	resp.setNum(comment.getNum());
	resp.setType(comment.getType());
	resp.setReply_id(comment.getReplyId());
	resp.setIpaddress(comment.getIpAddress());
	resp.setPos(comment.getPos());
	resp.setApp_url(comment.getAppUrl());
	resp.setReply_account(comment.getReplyAccount());
	resp.setStatus(comment.getStatus());
	resp.setSource(comment.getSource());
	resp.setBoard_id(comment.getBoardId());
	resp.setLng(comment.getLng());
	resp.setLat(comment.getLat());
	resp.setCon_type(comment.getConType());
	resp.setCurrent_board_id(comment.getCurrBoardId());
	ResolveCommentObject commentObject = StringUtil.resolveComment(comment.getContent(), comment.getReplyContent(),
	        false);
	resp.setImage(commentObject.getCommentImage());
	if (commentObject.getCommentText()!=null&&StringUtil.decodeDesc(commentObject.getCommentText()) != null) {
	    resp.setContent(StringUtil.decodeDesc(commentObject.getCommentText().replace("&quot;", "“")));
	} else {
	    resp.setContent(null);
	}
	if(commentObject.getCommentText()!=null){
	    resp.setContent(StringUtil.decodeDesc(commentObject.getCommentText().replace("&quot;", "“")));
	}else{
	    resp.setContent(null);
	}
	resp.setCited_image(commentObject.getCitedImage());
	if (commentObject.getCitedText()!=null&&StringUtil.decodeDesc(commentObject.getCitedText()) != null) {
	    resp.setCited_content(StringUtil.decodeDesc(commentObject.getCitedText().replace("&quot;", "“")));
	} else {
	    resp.setCited_content(null);
	}

	resp.setMeta(commentObject.getMeta());
	resp.setRe_meta(commentObject.getRe_meta());
	if (comment.getAccount() != null) {
	    resp.setAccount(transformAccount(comment.getAccount()));
	}
	if (comment.getRepliesAccount() != null) {
	    resp.setReplies_account(transformAccount(comment.getRepliesAccount()));
	} else if (resp.getCited_content() != null) { // 外站用户名
	    String rename = RegexUtil.filterMatch(resp.getCited_content(), "@(.*?)://.*");
	    if (StringUtils.isNotEmpty(rename)) {
		resp.setReplies_account(buildOutsiteUser(rename, comment.getFrom()));
		String[] contents = resp.getCited_content().split("@(.*?)://");
		if (contents.length > 1) { // 被回复内容可能会被过滤，考虑为空时的判断
		    if (resp.getCited_content().split("@(.*?)://")[1] != null) {
			resp.setCited_content(resp.getCited_content().split("@(.*?)://")[1].replace("&quot;", "“"));
		    } else {
			resp.setCited_content(null);
		    }
		}
	    }
	}

	return resp;
    }

    /**
     * 转换基础用户信息
     * 
     * @param account
     * @return
     */
    public static AccountResponse transformAccount(Account account) {
	if (account == null) {
	    return null;
	}
	AccountResponse resp = new AccountResponse();

	if (account.getAccountId() != null) {
	    resp.setAccountId(account.getAccountId());
	    resp.setLongNO(account.getLongNO());
	    resp.setOnline(account.isOnline());
	    resp.setAccountType(account.getAccountType());
	    resp.setRelationship(account.getRelationship());
	}
	resp.setName(account.getName());
	if (StringUtils.isNotEmpty(account.getPhotoPath())) {
	    resp.setPhotoPath(account.getPhotoPath());
	} else {
	    resp.setPhotoPath(DAFAULT_AVATAR);
	}
	resp.setNamePinyin(account.getNamePinyin());

	return resp;
    }

    /**
     * 转换基础用户信息
     * 
     * @param account
     * @return
     */
    public static AccountResponse transformAccount(AccountResponse account) {
	if (account == null) {
	    return null;
	}
	AccountResponse resp = new AccountResponse();

	if (account.getAccountId() != null) {
	    resp.setAccountId(account.getAccountId());
	    resp.setLongNO(account.getLongNO());
	    resp.setOnline(account.isOnline());
	    resp.setAccountType(account.getAccountType());
	    resp.setRelationship(account.getRelationship());
	}
	resp.setName(account.getName());
	if (StringUtils.isNotEmpty(account.getPhotoPath())) {
	    resp.setPhotoPath(account.getPhotoPath());
	} else {
	    resp.setPhotoPath(DAFAULT_AVATAR);
	}
	resp.setNamePinyin(account.getNamePinyin());

	return resp;
    }

    /**
     * 包装一个外站用户
     * 
     * @param name
     * @return
     */
    public static AccountResponse buildOutsiteUser(String name, String source) {
	if (name == null) {
	    return null;
	}
	AccountResponse resp = new AccountResponse();

	resp.setName(name);
	resp.setPhotoPath(source == null ? DAFAULT_AVATAR : String.format("%s.png", source));

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
