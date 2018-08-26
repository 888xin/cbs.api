package com.lifeix.cbs.contest.impl.comment;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.achieve.bean.bo.ContentAchieveBO;
import com.lifeix.cbs.achieve.common.constant.BehaviorConstants;
import com.lifeix.cbs.achieve.service.AchieveService;
import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.exception.MsgCode.UserMsg;
import com.lifeix.cbs.api.common.mission.Mission;
import com.lifeix.cbs.api.common.user.LifeixUserApiUtil;
import com.lifeix.cbs.api.common.util.CbsClientType;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestType;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.service.misson.MissionUserService;
import com.lifeix.cbs.api.service.user.CbsUserService;
import com.lifeix.cbs.contest.bean.comment.CbsCommentListResponse;
import com.lifeix.cbs.contest.bean.comment.CbsCommentResponse;
import com.lifeix.cbs.contest.dao.bb.BbContestDao;
import com.lifeix.cbs.contest.dao.comment.CommentDao;
import com.lifeix.cbs.contest.dao.fb.FbContestDao;
import com.lifeix.cbs.contest.dto.comment.Comment;
import com.lifeix.cbs.contest.impl.ImplSupport;
import com.lifeix.cbs.contest.service.comment.CommentAtService;
import com.lifeix.cbs.contest.service.comment.CommentService;
import com.lifeix.cbs.contest.util.transform.CbsCommentTransformUtil;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;
import com.lifeix.user.beans.account.AccountResponse;

@Service("commentService")
public class CommentServiceImpl extends ImplSupport implements CommentService {

    @Autowired
    private CommentAtService commentAtService;

    @Autowired
    private CbsUserService cbsUserService;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private FbContestDao fbContestDao;

    @Autowired
    private BbContestDao bbContestDao;

    @Autowired
    protected AchieveService achieveService;

    @Autowired
    protected MissionUserService missionUserService;

    @Override
    public CbsCommentListResponse findComments(Long contestId, Integer contestType, Long startId, Long endId, int limit)
	    throws L99IllegalParamsException, L99NetworkException, JSONException {

	ParamemeterAssert.assertDataNotNull(contestId);

	List<Comment> comments = commentDao.findComments(contestId, contestType, startId, endId, limit);
	List<CbsCommentResponse> commentResponses = new LinkedList<CbsCommentResponse>();
	// 重新排序，id从小到大，升序

	Collections.sort(comments, new Comparator<Comment>() {
	    /*
	     * int compare(Student o1, Student o2) 返回一个基本类型的整型， 返回负数表示：o1 小于o2，
	     * 返回0 表示：o1和o2相等， 返回正数表示：o1大于o2。
	     */
	    public int compare(Comment com1, Comment com2) {

		// 按照学生的年龄进行升序排列
		if (com1.getId() > com2.getId()) {
		    return 1;
		}
		if (com1.getId() == com2.getId()) {
		    return 0;
		}
		return -1;
	    }
	});
	// 先收集comments中的ids
	// List<Long> ids = new LinkedList<Long>();
	String ids = null;
	for (Comment comm : comments) {
	    // ids.add(comm.getUserId());
	    if (ids == null) {
		ids = comm.getUserId() + "";
	    } else {
		ids = ids + "," + comm.getUserId();

	    }
	}
	if (ids != null) {
	    List<AccountResponse> accounts = LifeixUserApiUtil.getInstance().findUserByIds(ids, "id", false);
	    // 存放在map里面，
	    HashMap<Long, AccountResponse> accountHashmap = new HashMap<Long, AccountResponse>();
	    for (AccountResponse ars : accounts) {
		accountHashmap.put(ars.getAccountId(), ars);
	    }

	    for (Comment comm : comments) {
		CbsCommentResponse ccr = CbsCommentTransformUtil.transformComment(comm, accountHashmap);
		commentResponses.add(ccr);
	    }
	}
	CbsCommentListResponse reponse = new CbsCommentListResponse();
	reponse.setComments(commentResponses);

	// reponse.setEndId(endId);
	// reponse.setStartId(startId);
	return reponse;
    }

    @Override
    public Long postComment(Long contestId, Integer contestType, Long userId, Long acceptUserId, String content,
	    String images, String ipaddress, String client, Long cirCommentId, boolean missionValidate)
	    throws L99IllegalParamsException {

	if (contestType == null) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_BASIC_VERSIONLOW, BasicMsg.KEY_BASIC_VERSIONLOW);
	}

	ParamemeterAssert.assertDataNotNull(contestId, userId, content);

	int contentLength = 252;
	// 处理content超长252个字节的情况保留252个，然后加“...”
	if (content.length() > contentLength) {
	    content = content.substring(0, contentLength) + "...";
	}

	if (contestType == ContestType.FOOTBALL) {
	    if (fbContestDao.selectById(contestId) == null) {
		throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	    }
	} else if (contestType == ContestType.BASKETBALL) {
	    if (bbContestDao.selectById(contestId) == null) {
		throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	    }
	} else {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}

	// 检查用户是否不存在或被拉入黑名单
	try {
	    CbsUserResponse user = cbsUserService.getCbsUserByUserId(userId, false);
	    if (user == null) {
		throw new L99IllegalParamsException(UserMsg.CODE_USER_ACCOUNT_NOT_FOUND, UserMsg.KEY_USER_ACCOUNT_NOT_FOUND);
	    } else if (user.getStatus() == 1) {
		throw new L99IllegalParamsException(UserMsg.CODE_USER_ACCOUNT_BLOCK, UserMsg.KEY_USER_ACCOUNT_BLOCK);
	    }
	} catch (Exception e) {
	    throw new L99IllegalParamsException(UserMsg.CODE_USER_ACCOUNT_NOT_FOUND, UserMsg.KEY_USER_ACCOUNT_NOT_FOUND);
	}

	Comment comment = new Comment();
	comment.setContestId(contestId);
	comment.setUserId(userId);
	comment.setContent(content);
	comment.setImages(images);
	comment.setIpaddress(ipaddress);
	comment.setClient(CbsClientType.getClient(client).getName());
	comment.setCreateTime(new Date());
	comment.setCirId(cirCommentId);
	comment.setContestType(contestType);
	try {
	    if (acceptUserId != null) {
		commentAtService.postCommentAt(contestType, contestId, userId, acceptUserId, content.substring(0, 10),
		        ipaddress, client);
	    }
	} catch (Exception e) {
	    LOG.error("commentAtService postComment", e);
	}

	// 赛事评论成就
	try {
	    ContentAchieveBO bo = new ContentAchieveBO();
	    bo.setBehavior_type(BehaviorConstants.BehaviorType.CONTENT_TYPE);
	    bo.setUser_id(userId);
	    bo.setComment_flag(true);
	    achieveService.addAchieveData(bo.generateData(), false);
	} catch (Throwable t) {
	    LOG.error("userId=" + userId + ", content achieve data analysis failed: " + t.getMessage(), t);
	}

	// add by lhx on 16-06-27 每日评论赛事 start
	if (missionValidate) {
	    missionUserService.validate(userId, Mission.COMMENT_UNDER_CONTEST);
	}
	// add by lhx on 16-06-27 每日评论赛事 end

	return commentDao.insertAndGetPrimaryKey(comment);
    }

    @Override
    public Integer findCommentNum(Long contestId) {
	return null;
    }

    @Override
    public void deleteByCirId(Long cirCommentId, Long contestId, Integer contestType) {
	commentDao.deleteBycirId(cirCommentId, contestId, contestType);
    }

}
