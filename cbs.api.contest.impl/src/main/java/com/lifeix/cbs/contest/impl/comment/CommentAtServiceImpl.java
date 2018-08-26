package com.lifeix.cbs.contest.impl.comment;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.l99.common.utils.TimeUtil;
import com.lifeix.cbs.api.common.util.CbsClientType;
import com.lifeix.cbs.api.common.util.ContestConstants.CommentAtStatus;
import com.lifeix.cbs.api.common.util.ContestConstants.TempletId;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.service.user.CbsUserService;
import com.lifeix.cbs.contest.dao.comment.CommentAtDao;
import com.lifeix.cbs.contest.dto.comment.CommentAt;
import com.lifeix.cbs.contest.impl.ImplSupport;
import com.lifeix.cbs.contest.service.comment.CommentAtService;
import com.lifeix.cbs.message.service.notify.NotifyService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;

@Service("commentAtService")
public class CommentAtServiceImpl extends ImplSupport implements CommentAtService {
    @Autowired
    CommentAtDao commentAtDao;

    @Autowired
    NotifyService notifyService;
    
    @Autowired
    CbsUserService cbsUserService;
    
    @Override
    public void postCommentAt(Integer contestType,Long contestId, Long sendUserId, Long acceptUserId, String content, String ipaddress,
	    String client) throws L99IllegalParamsException, L99NetworkException, L99IllegalDataException {
	ParamemeterAssert.assertDataNotNull(contestId,sendUserId,acceptUserId,content);
	CommentAt comment = new CommentAt();
	comment.setContent(content);
	comment.setContestType(contestType);
	comment.setContestId(contestId);
	comment.setSendUserId(sendUserId);
	comment.setAcceptUserId(acceptUserId);
	comment.setCreateTime(new Date());
	comment.setIpaddress(ipaddress);
	comment.setStatus(CommentAtStatus.NOT_DO);
	comment.setClient(CbsClientType.getClient(client).getName());
	
	commentAtDao.insertAndGetPrimaryKey(comment);
	
	JSONObject params = new JSONObject();
	try {
	    params.put("user_name", cbsUserService.getCbsUserByUserId(sendUserId, false).getName());
	    params.put("create_time", TimeUtil.getUtcTimeForDate(new Date()));
	    params.put("contest_id", contestId);
	    params.put("contest_type", contestType);
        } catch (JSONException e) {
            LOG.error("postCommentAt " + e.getMessage());
        }
	
	notifyService.addNotify(TempletId.USER_AT, acceptUserId, sendUserId, params.toString(), null);
    }

}
