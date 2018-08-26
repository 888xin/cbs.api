package com.lifeix.cbs.contest.impl.circle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lifeix.cbs.api.common.mission.Mission;
import com.lifeix.cbs.api.service.misson.MissionUserService;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.exception.MsgCode.CircleCommMsg;
import com.lifeix.cbs.api.common.exception.MsgCode.ContentMsg;
import com.lifeix.cbs.api.common.exception.MsgCode.UserMsg;
import com.lifeix.cbs.api.common.util.LifeixAntiApiUtil;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.service.user.CbsUserService;
import com.lifeix.cbs.contest.bean.circle.FriendCircleCommListResponse;
import com.lifeix.cbs.contest.bean.circle.FriendCircleCommResponse;
import com.lifeix.cbs.contest.bean.circle.FriendCircleContestResponse;
import com.lifeix.cbs.contest.bean.circle.FriendCircleResponse;
import com.lifeix.cbs.contest.dao.circle.FriendCircleDao;
import com.lifeix.cbs.contest.dao.circlecomment.FriendCircleCommDao;
import com.lifeix.cbs.contest.dto.circle.FriendCircle;
import com.lifeix.cbs.contest.dto.circle.FriendCircleComment;
import com.lifeix.cbs.contest.impl.ImplSupport;
import com.lifeix.cbs.contest.impl.redis.RedisCommentListHandler;
import com.lifeix.cbs.contest.service.cirle.FriendCircleCommService;
import com.lifeix.cbs.contest.service.cirle.FriendCircleService;
import com.lifeix.cbs.contest.service.comment.CommentService;
import com.lifeix.cbs.contest.util.transform.FriCirCommentTransformUtil;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;
import com.lifeix.user.beans.CustomResponse;

@Service("friendCircleCommService")
public class FriendCircleCommServiceImpl extends ImplSupport implements FriendCircleCommService {

    @Autowired
    public FriendCircleCommDao friendCircleCommDao;

    @Autowired
    public FriendCircleService friendCircleService;

    @Autowired
    public RedisCommentListHandler redisCommentListHandler;

    @Autowired
    public CbsUserService cbsUserService;

    @Autowired
    public FriendCircleDao friendCircleDao;

    @Autowired
    public CommentService commentService;

    @Autowired
    private MissionUserService missionUserService;

    /**
     * 添加评论
     */
    @Override
    public DataResponse<Object> addComment(Long contentId, Long circleUserId, Long commUserId, String content, String image,
                                           Long reUserId, String reContent, String reImage, String source, String ipaddress) {

        DataResponse<Object> ret = new DataResponse<Object>();

        try {
            ParamemeterAssert.assertDataNotNull(contentId);
            ParamemeterAssert.assertDataNotNull(circleUserId);
            ParamemeterAssert.assertDataNotNull(commUserId);
            ParamemeterAssert.assertDataNotNull(content);

            // 检查用户是否不存在或被拉入黑名单
            try {
                CbsUserResponse user = cbsUserService.getCbsUserByUserId(commUserId, false);
                if (user == null) {
                    throw new L99IllegalParamsException(UserMsg.CODE_USER_ACCOUNT_NOT_FOUND,
                            UserMsg.KEY_USER_ACCOUNT_NOT_FOUND);
                } else if (user.getStatus() == 1) {
                    throw new L99IllegalParamsException(UserMsg.CODE_USER_ACCOUNT_BLOCK, UserMsg.KEY_USER_ACCOUNT_BLOCK);
                }
            } catch (Exception e) {
                throw new L99IllegalParamsException(UserMsg.CODE_USER_ACCOUNT_NOT_FOUND, UserMsg.KEY_USER_ACCOUNT_NOT_FOUND);
            }

            // 过滤敏感词
            String keyword = LifeixAntiApiUtil.getInstance().filterComment(content);
            if (keyword != null) {
                throw new L99IllegalParamsException(ContentMsg.CODE_CONTENT_SENSITIVE, ContentMsg.KEY_CONTENT_SENSITIVE);
            }

            FriendCircleComment comment = new FriendCircleComment();
            comment.setContentId(contentId);
            comment.setCircleUserId(circleUserId);
            comment.setCommUserId(commUserId);
            if (content.length() > 512) {
                content = content.substring(0, 512);
            }
            comment.setContent(content);
            comment.setImage(image);
            comment.setReUserId(reUserId);
            comment.setReContent(reContent);
            comment.setReImage(reImage);
            comment.setCreateTime(new Date());
            comment.setSource(source);
            comment.setIpaddress(ipaddress);
            Long id = null;
            List<Long> ids = friendCircleCommDao.selectIdsByContentId(contentId);
            if (commUserId.longValue() == circleUserId.longValue()) { // 发表评论的人是自己
                comment.setStatus(1);
                // 获取评论主键id
                id = friendCircleCommDao.insertAndGetPrimaryKey(comment);
                redisCommentListHandler.resetSingleComment(circleUserId, ids);// 清空该猜友圈的未读评论
                friendCircleCommDao.updateReadComment(null, contentId); // 将所有未读状态变为已读状态
                if (reUserId != null) {// 二级评论
                    redisCommentListHandler.addUnreadComment(reUserId, id);// 添加被评论者的未读评论记录
                }
            } else { // 发表评论的人是别人
                comment.setStatus(0);
                id = friendCircleCommDao.insertAndGetPrimaryKey(comment);
                redisCommentListHandler.resetSingleComment(commUserId, ids);// 评论者的关于该篇猜友圈的未读评论清空
                redisCommentListHandler.addUnreadComment(circleUserId, id);// 添加发表猜友圈用户的未读评论记录
                if (reUserId != null) {// 二级评论
                    if (!circleUserId.equals(reUserId)) { // 猜友圈发表者和被评论人不是同一个人
                        redisCommentListHandler.addUnreadComment(reUserId, id);// 添加被评论者的未读评论记录


                    }
                }
                // add by lhx on 16-06-23 每日评论下单理由 start
                missionUserService.validate(commUserId, Mission.COMMENT_UNDER_RESON);
                // add by lhx on 16-06-23 每日评论下单理由 end
            }

            if (id < 1) {
                throw new L99IllegalDataException(CircleCommMsg.CODE_POST_COMMEND_FAILED,
                        CircleCommMsg.KEY_POST_COMMEND_FAILED);
            }

            CbsUserResponse commUserResponse = cbsUserService.getCbsUserByUserId(comment.getCommUserId(), false);
            CbsUserResponse reUserResponse = null;
            if (comment.getReUserId() != null) {
                reUserResponse = cbsUserService.getCbsUserByUserId(comment.getReUserId(), false);
            }
            FriendCircleCommResponse resp = FriCirCommentTransformUtil.transformFriendCircleComment(comment,
                    commUserResponse, reUserResponse);

            // 添加猜友圈评论到赛事房间
            FriendCircleResponse fcr = friendCircleService.getSingleContentResponse(contentId);
            FriendCircleContestResponse friendCircleContestResponse = fcr.getContest();
            if (friendCircleContestResponse != null && friendCircleContestResponse.getContestId() != null) {
                commentService.postComment(fcr.getContest().getContestId(), fcr.getContest().getType(), commUserId, null,
                        content, image, ipaddress, null, id, false);
            }

            comment.setId(id);
            ret.setCode(DataResponse.OK);
            ret.setData(resp);
        } catch (L99IllegalParamsException e) {
            ret.setCode(Integer.valueOf(e.getErrorcode()));
            ret.setMsg(e.getMessage());
        } catch (L99IllegalDataException e) {
            ret.setCode(Integer.valueOf(e.getErrorcode()));
            ret.setMsg(e.getMessage());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(Integer.valueOf(BasicMsg.CODE_BASIC_SERVCER));
            ret.setMsg(BasicMsg.KEY_BASIC_SERVCER);
        }
        return ret;

    }

    /**
     * 删除评论
     */
    @Override
    public DataResponse<Object> delComment(Long id) {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    ParamemeterAssert.assertDataNotNull(id);
	    FriendCircleComment fcc = friendCircleCommDao.selectById(id);
	    Boolean flag = friendCircleCommDao.deleteById(id);
	    if (!flag) {
		throw new L99IllegalDataException(BasicMsg.CODE_OPERATE_FAIL, BasicMsg.KEY_OPERATE_FAIL);
	    }

	    // 关联删除赛事房间评论
	    Long contentId = fcc.getContentId();
	    FriendCircleResponse fcr = friendCircleService.getSingleContentResponse(contentId);
	    FriendCircleContestResponse friendCircleContestResponse = fcr.getContest();
	    if (friendCircleContestResponse != null && friendCircleContestResponse.getContestId() != null) {
		commentService.deleteByCirId(id, fcr.getContest().getContestId(), fcr.getContest().getType());
	    }

	    ret.setCode(DataResponse.OK);
	} catch (L99IllegalDataException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(e.getMessage());
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(Integer.valueOf(BasicMsg.CODE_BASIC_SERVCER));
	    ret.setMsg(BasicMsg.KEY_BASIC_SERVCER);
	}
	return ret;
    }

    /**
     * 获取单条猜友圈评论列表详细
     */
    @Override
    public DataResponse<FriendCircleCommListResponse> findCommentDetail(Long contentId, Long userId, Long startId, int limit) {
	DataResponse<FriendCircleCommListResponse> ret = new DataResponse<FriendCircleCommListResponse>();
	try {
	    ParamemeterAssert.assertDataNotNull(contentId);
	    FriendCircleCommListResponse response = new FriendCircleCommListResponse();
	    try {
		List<FriendCircleComment> comments = friendCircleCommDao.selectByContent(contentId, startId, limit);
		List<Long> ids = friendCircleCommDao.selectIdsByContentId(contentId);
		redisCommentListHandler.resetSingleComment(userId, ids);// 清空该条猜友圈下的所有未读评论
		friendCircleCommDao.updateReadComment(null, contentId);// 将该篇猜友圈未读状态变为已读状态
		List<FriendCircleCommResponse> commentResponse = new ArrayList<FriendCircleCommResponse>();
		for (FriendCircleComment comment : comments) {
		    CbsUserResponse commUserResponse = cbsUserService.getCbsUserByUserId(comment.getCommUserId(), false);
		    CbsUserResponse reUserResponse = null;
		    if (comment.getReUserId() != null) {
			reUserResponse = cbsUserService.getCbsUserByUserId(comment.getReUserId(), false);
		    }
		    commentResponse.add(FriCirCommentTransformUtil.transformFriendCircleComment(comment, commUserResponse,
			    reUserResponse));
		}

		// 获取最大id
		Long maxId = null;
		for (FriendCircleCommResponse resp : commentResponse) {
		    if (maxId == null) {
			maxId = resp.getId();
		    } else if (resp.getId() > maxId) {
			maxId = resp.getId();
		    }
		}
		// 判断是否还有数据，设置startId
		if (friendCircleCommDao.selectByContent(contentId, maxId, limit).size() > 0) {
		    response.setStartId(maxId);
		} else {
		    response.setStartId(0L);
		}

		Long circleUserId = friendCircleService.getSingleContentResponse(contentId).getUser().getUser_id();

		Long counts = (Long) getSingleCommentCounts(contentId, circleUserId).getData();

		response.setComment_counts(counts);
		response.setComments(commentResponse);

	    } catch (Exception e) {
		LOG.error(e.getMessage(), e);
		throw new L99IllegalDataException(CircleCommMsg.CODE_COMMEND_LIST_FAILED,
		        CircleCommMsg.KEY_COMMEND_LIST_FAILED);
	    }

	    ret.setCode(DataResponse.OK);
	    ret.setData(response);
	} catch (L99IllegalDataException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(e.getMessage());
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(Integer.valueOf(BasicMsg.CODE_BASIC_SERVCER));
	    ret.setMsg(BasicMsg.KEY_BASIC_SERVCER);
	}
	return ret;
    }

    /**
     * 获取未读评论列表（只显示回复和主题）
     */
    @Override
    public DataResponse<FriendCircleCommListResponse> findUnreadCommentList(Long userId, Integer pageNum, Integer limit) {
	DataResponse<FriendCircleCommListResponse> ret = new DataResponse<FriendCircleCommListResponse>();
	try {
	    ParamemeterAssert.assertDataNotNull(userId);
	    FriendCircleCommListResponse response = new FriendCircleCommListResponse();
	    try {
		List<Long> commendIds = redisCommentListHandler.getUnreadCommendIds(userId, pageNum, limit);
		if (redisCommentListHandler.hasUnreadCommendIds(userId, limit)) {
		    pageNum++;
		} else {
		    pageNum = -1;
		}
		List<FriendCircleComment> comments = new ArrayList<FriendCircleComment>();
		if (!commendIds.isEmpty()) {
		    comments = friendCircleCommDao.selectByIds(commendIds);
		}
		// redisCommentListHandler.resetCommentsRange(userId, limit); //
		// 清空当前页评论
		friendCircleCommDao.updateReadComment(userId, null);// 将所有未读状态变为已读状态
		List<FriendCircleCommResponse> commentResponse = new ArrayList<FriendCircleCommResponse>();
		if (!comments.isEmpty()) {
		    List<Long> contentIds = new ArrayList<Long>();
		    for (FriendCircleComment comment : comments) {
			if (!contentIds.contains(comment.getContentId())) {
			    contentIds.add(comment.getContentId());
			}
		    }
		    // 获取猜友圈评论类型映射
		    Map<Long, FriendCircleResponse> resultMap = friendCircleService.getMapContentResponse(contentIds);

		    for (FriendCircleComment comment : comments) {
			CbsUserResponse commUserResponse = cbsUserService.getCbsUserByUserId(comment.getCommUserId(), false);
			FriendCircle friendCircle = friendCircleDao.findById(comment.getContentId());
			String params = friendCircle.getParams();
			String theme_img = null;
			if (!StringUtils.isEmpty(params)) {
			    JSONObject obj = new JSONObject(params);
			    if (obj.has("images")) {
				String photoIdStr = obj.getString("images");
				String[] images = photoIdStr.split(",");
				// 取出第一个图片
				theme_img = images[0];
			    }
			}
			commentResponse.add(FriCirCommentTransformUtil.transformFriendCircleComment(comment,
			        commUserResponse, friendCircle.getContent(), theme_img, resultMap));
		    }
		}

		response.setPage_num(pageNum);
		response.setComments(commentResponse);
	    } catch (Exception e) {
		LOG.error(e.getMessage(), e);
		throw new L99IllegalDataException(CircleCommMsg.CODE_UNREAD_LIST_FAILED,
		        CircleCommMsg.KEY_UNREAD_LIST_FAILED);
	    }
	    ret.setCode(DataResponse.OK);
	    ret.setData(response);
	} catch (L99IllegalDataException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(e.getMessage());
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(Integer.valueOf(BasicMsg.CODE_BASIC_SERVCER));
	    ret.setMsg(BasicMsg.KEY_BASIC_SERVCER);
	}
	return ret;
    }

    /**
     * 获取历史评论列表（只显示回复和主题）
     */
    @Override
    public DataResponse<FriendCircleCommListResponse> findHistoryCommentList(Long userId, Long startId, Long endId,
                                                                             Integer limit) {
        DataResponse<FriendCircleCommListResponse> ret = new DataResponse<FriendCircleCommListResponse>();
        try {
            ParamemeterAssert.assertDataNotNull(userId);
            FriendCircleCommListResponse response = new FriendCircleCommListResponse();
            List<FriendCircleCommResponse> commentResponse = new ArrayList<FriendCircleCommResponse>();
            try {
                List<FriendCircleComment> comments = friendCircleCommDao.getUserComment(userId, startId, endId, limit);

                if (!comments.isEmpty()) {
                    List<Long> contentIds = new ArrayList<Long>();
                    for (FriendCircleComment comment : comments) {
                        if (!contentIds.contains(comment.getContentId())) {
                            contentIds.add(comment.getContentId());
                        }
                    }
                    // 获取猜友圈评论类型映射
                    Map<Long, FriendCircleResponse> resultMap = friendCircleService.getMapContentResponse(contentIds);

                    for (FriendCircleComment comment : comments) {
                        CbsUserResponse commUserResponse = cbsUserService.getCbsUserByUserId(comment.getCommUserId(), false);
                        FriendCircle friendCircle = friendCircleDao.findById(comment.getContentId());
                        if (friendCircle != null) {
                            String params = friendCircle.getParams();
                            String theme_img = null;
                            if (!StringUtils.isEmpty(params)) {
                                JSONObject obj = new JSONObject(params);
                                if (obj.has("images")) {
                                    String photoIdStr = obj.getString("images");
                                    String[] images = photoIdStr.split(",");
                                    // 取出第一个图片
                                    theme_img = images[0];
                                }
                            }
                            commentResponse.add(FriCirCommentTransformUtil.transformFriendCircleComment(comment,
                                    commUserResponse, friendCircle.getContent(), theme_img, resultMap));
                        }
                    }
                }
                // 获取最小id
                Long minId = null;
                for (FriendCircleCommResponse resp : commentResponse) {
                    if (minId == null) {
                        minId = resp.getId();
                    } else if (resp.getId() < minId) {
                        minId = resp.getId();
                    }
                }
                // 判断是否还有数据，设置endId
                if (friendCircleCommDao.getUserComment(userId, null, minId, limit).size() > 0) {
                    response.setEndId(minId);
                } else {
                    response.setEndId(0L);
                }
                response.setComments(commentResponse);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
                throw new L99IllegalDataException(CircleCommMsg.CODE_HISTORY_LIST_FAILED,
                        CircleCommMsg.KEY_HISTORY_LIST_FAILED);
            }
            ret.setCode(DataResponse.OK);
            ret.setData(response);
        } catch (L99IllegalDataException e) {
            ret.setCode(Integer.valueOf(e.getErrorcode()));
            ret.setMsg(e.getMessage());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(Integer.valueOf(BasicMsg.CODE_BASIC_SERVCER));
            ret.setMsg(BasicMsg.KEY_BASIC_SERVCER);
        }
        return ret;
    }

    /**
     * 后台屏蔽评论
     * 
     * @param ids
     * @return
     */
    @Override
    public DataResponse<Object> updateShield(List<Long> ids) {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    ParamemeterAssert.assertDataNotNull(ids);
	    Boolean flag = friendCircleCommDao.updateShield(ids);
	    if (!flag) {
		throw new L99IllegalDataException(BasicMsg.CODE_OPERATE_FAIL, BasicMsg.KEY_OPERATE_FAIL);
	    }
	    ret.setCode(DataResponse.OK);
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(e.getMessage());
	} catch (L99IllegalDataException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(e.getMessage());
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(Integer.valueOf(BasicMsg.CODE_BASIC_SERVCER));
	    ret.setMsg(BasicMsg.KEY_BASIC_SERVCER);
	}
	return ret;
    }

    @Override
    public DataResponse<Object> getUnreadCounts(Long userId, Integer api_version) {

	DataResponse<Object> ret = new DataResponse<Object>();
	try {

	    ParamemeterAssert.assertDataNotNull(userId);

	    Long counts = redisCommentListHandler.getUnreadCommentCounts(userId);
	    CustomResponse resp = new CustomResponse();
	    if (api_version != null && api_version == 2) {
		resp.put("counts", counts);
		ret.setData(resp);
	    } else {
		ret.setData(counts);
	    }
	} catch (L99IllegalParamsException e) {
	    ret.setCode(Integer.valueOf(e.getErrorcode()));
	    ret.setMsg(e.getMessage());
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(Integer.valueOf(BasicMsg.CODE_BASIC_SERVCER));
	    ret.setMsg(BasicMsg.KEY_BASIC_SERVCER);
	}
	return ret;
    }

    @Override
    public DataResponse<Object> getSingleCommentCounts(Long contentId, Long userId) {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {
	    Long counts = friendCircleCommDao.getCounts(contentId, userId);
	    ret.setData(counts);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(Integer.valueOf(BasicMsg.CODE_BASIC_SERVCER));
	    ret.setMsg(BasicMsg.KEY_BASIC_SERVCER);
	}
	return ret;
    }

    @Override
    public DataResponse<FriendCircleCommListResponse> findCommentList(Long userId, Long endId, Integer limit) {
	DataResponse<FriendCircleCommListResponse> ret = new DataResponse<FriendCircleCommListResponse>();
	try {
	    List<FriendCircleComment> comments = friendCircleCommDao.getCommentList(userId, endId, limit);
	    FriendCircleCommListResponse listResponse = new FriendCircleCommListResponse();
	    List<FriendCircleCommResponse> responses = new ArrayList<FriendCircleCommResponse>();
	    if (!comments.isEmpty()) {

		for (FriendCircleComment comment : comments) {
		    CbsUserResponse commUserResponse = cbsUserService.getCbsUserByUserId(comment.getCommUserId(), false);
		    CbsUserResponse reUserResponse = null;
		    if (comment.getReUserId() != null) {
			reUserResponse = cbsUserService.getCbsUserByUserId(comment.getReUserId(), false);
		    }
		    responses.add(FriCirCommentTransformUtil.transformFriendCircleComment(comment, commUserResponse,
			    reUserResponse));
		}
	    }
	    listResponse.setComments(responses);

	    ret.setData(listResponse);
	    ret.setCode(DataResponse.OK);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    ret.setCode(Integer.valueOf(BasicMsg.CODE_BASIC_SERVCER));
	    ret.setMsg(BasicMsg.KEY_BASIC_SERVCER);
	}
	return ret;
    }

    public RedisCommentListHandler getRedisCommentListHandler() {
	return redisCommentListHandler;
    }

    public void setRedisCommentListHandler(RedisCommentListHandler redisCommentListHandler) {
	this.redisCommentListHandler = redisCommentListHandler;
    }

    /**
     * 获取单条评论
     * 
     * @throws JSONException
     * @throws L99IllegalParamsException
     * @throws L99NetworkException
     */
    @Override
    public FriendCircleCommResponse getSingleComment(Long id) throws JSONException, L99IllegalDataException,
	    L99NetworkException, L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(id);

	FriendCircleComment comment = friendCircleCommDao.selectById(id);
	FriendCircleCommResponse commResponse = null;
	if (comment != null) { // 评论是否已删除
	    CbsUserResponse commUserResponse = cbsUserService.getCbsUserByUserId(comment.getCommUserId(), false);
	    FriendCircle friendCircle = friendCircleDao.findById(comment.getContentId());
	    String params = friendCircle.getParams();
	    String theme_img = null;
	    if (!StringUtils.isEmpty(params)) {
		JSONObject obj = new JSONObject(params);
		if (obj.has("images")) {
		    String photoIdStr = obj.getString("images");
		    String[] images = photoIdStr.split(",");
		    // 取出第一个图片
		    theme_img = images[0];
		}
	    }
	    commResponse = FriCirCommentTransformUtil.transformFriendCircleComment(comment, commUserResponse,
		    friendCircle.getContent(), theme_img, null);
	}
	return commResponse;
    }

}
