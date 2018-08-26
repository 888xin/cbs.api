package com.lifeix.cbs.content.impl.comment;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.l99.common.utils.EmojiUtil;
import com.l99.pojo.comment.L99Comment;
import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.push.PushMessageUtil;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.util.LifeixCommentUtil;
import com.lifeix.cbs.content.impl.transform.CommentTransformUtil;
import com.lifeix.cbs.content.service.comment.HessionCommentService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.tiyu.content.bean.comment.CommentListResponse;
import com.lifeix.tiyu.content.bean.comment.CommentResponse;

@Service("hessionCommentService")
public class HessionCommentServiceImpl extends ImplSupport implements HessionCommentService {

    /**
     * 内容点赞
     * 
     * @param userId
     * @param dashboardId
     * @param client
     * @param lat
     * @param lng
     * @param ipAddress
     * @return
     */
    public DataResponse<Object> LikeContent(Long userId, Long dashboardId, String client, Double lat, Double lng,
	    String ipAddress) {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {

	    ParamemeterAssert.assertDataNotNull(userId);
	    ParamemeterAssert.assertDataNotNull(dashboardId);

	    try {
		LifeixCommentUtil.getInstance().likeByBoard(userId, dashboardId, client, lat, lng, ipAddress);
	    } catch (Exception e) {
		throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
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

    /**
     * 评论新闻
     * 
     * @param userId
     * @param dashboardId
     * @param content
     * @param image
     * @param ipAddress
     * @return
     */
    public DataResponse<CommentResponse> sendComment(Long userId, Long dashboardId, String content, String image,
	    String client, Double lat, Double lng, String ipAddress) {
	DataResponse<CommentResponse> ret = new DataResponse<CommentResponse>();
	try {

	    ParamemeterAssert.assertDataNotNull(userId);
	    ParamemeterAssert.assertDataNotNull(dashboardId);
	    ParamemeterAssert.assertDataOrNotNull(content, image);

	    CommentResponse response = null;
	    try {
		// if (content != null) {
		// content = EmojiUtil.encode(content);
		// }

		L99Comment comment = LifeixCommentUtil.getInstance().replyByBoardAndGetComment(userId, dashboardId, content,
		        image, client, lat, lng, ipAddress);
		response = CommentTransformUtil.transformComment(comment);
	    } catch (Exception e) {
		throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	    }

	    ret.setCode(DataResponse.OK);
	    ret.setData(response);
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
     * 回复评论
     * 
     * @param userId
     * @param commentId
     * @param content
     * @param image
     * @param ipAddress
     * @return
     */
    public DataResponse<CommentResponse> replyComment(Long userId, Long commentId, String content, String image,
	    String client, Double lat, Double lng, String ipAddress) {
	DataResponse<CommentResponse> ret = new DataResponse<CommentResponse>();
	try {

	    ParamemeterAssert.assertDataNotNull(userId);
	    ParamemeterAssert.assertDataNotNull(commentId);
	    ParamemeterAssert.assertDataOrNotNull(content, image);

	    CommentResponse response = null;
	    try {
		if (content != null) {
		    content = EmojiUtil.encode(content);
		}
		L99Comment comment = LifeixCommentUtil.getInstance().replyComment(userId, commentId, content, image, client,
		        lat, lng, ipAddress);

		if (comment == null) {
		    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
		}

		response = CommentTransformUtil.transformComment(comment);

		// 不是回复自己就添加push队列
		if (comment.getAccountId() != comment.getReplyAccount()) {
		    PushMessageUtil.getInstance().addPushCount(comment.getReplyAccount());
		}

	    } catch (Exception e) {
		throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	    }

	    ret.setCode(DataResponse.OK);
	    ret.setData(response);
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
     * 评论列表
     * 
     * @param dashboardId
     * @param startId
     * @param limit
     * @return
     */
    public DataResponse<CommentListResponse> findDashboardComments(Long dashboardId, Long startId, int limit) {
	DataResponse<CommentListResponse> ret = new DataResponse<CommentListResponse>();
	try {

	    ParamemeterAssert.assertDataNotNull(dashboardId);
	    CommentListResponse response = new CommentListResponse();
	    try {
		List<L99Comment> comments = LifeixCommentUtil.getInstance().findCommentByBoard(dashboardId, startId, limit);

		List<CommentResponse> commentResponse = new ArrayList<CommentResponse>();
		for (L99Comment comment : comments) {
		    if (comment.getStatus() != 12) { // 滤掉like记录
			commentResponse.add(CommentTransformUtil.transformComment(comment));
		    }
		}
		// startId为小的id，加载更多
		if (commentResponse != null && commentResponse.size() > 0) {
		    response.setStartId(commentResponse.get(commentResponse.size() - 1).getComment_id());
		} else {
		    response.setStartId(-1L);
		}
		// endId为大的id，下拉刷新
		if (commentResponse != null && commentResponse.size() > 0) {
		    response.setEndId(commentResponse.get(0).getComment_id());
		} else {
		    response.setEndId(-1L);
		}
		response.setComments(commentResponse);
	    } catch (Exception e) {
		throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	    }
	    ret.setCode(DataResponse.OK);
	    ret.setData(response);
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
     * 用户的评论列表
     * 
     * @param userId
     * @param startId
     * @param limit
     * @return
     */
    public DataResponse<CommentListResponse> findUserComments(Long userId, Long startId, int limit) {
	DataResponse<CommentListResponse> ret = new DataResponse<CommentListResponse>();
	try {

	    ParamemeterAssert.assertDataNotNull(userId);
	    CommentListResponse response = new CommentListResponse();

	    try {
		List<L99Comment> comments = LifeixCommentUtil.getInstance().findUserComment(userId, startId, limit);
		List<CommentResponse> commentResponse = new ArrayList<CommentResponse>();
		for (L99Comment comment : comments) {
		    commentResponse.add(CommentTransformUtil.transformComment(comment));
		}
		response.setComments(commentResponse);
	    } catch (Exception e) {
		throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	    }

	    ret.setCode(DataResponse.OK);
	    ret.setData(response);
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
     * 提到用户的评论列表
     * 
     * @param userId
     * @param startId
     * @param limit
     * @return
     */
    public DataResponse<CommentListResponse> findReplyUserComments(Long userId, Long startId, int limit) {
	DataResponse<CommentListResponse> ret = new DataResponse<CommentListResponse>();
	try {
	    ParamemeterAssert.assertDataNotNull(userId);
	    CommentListResponse response = new CommentListResponse();
	    try {
		List<L99Comment> comments = LifeixCommentUtil.getInstance().findReplyComment(userId, startId, limit);
		List<CommentResponse> commentResponse = new ArrayList<CommentResponse>();
		for (L99Comment comment : comments) {
		    commentResponse.add(CommentTransformUtil.transformComment(comment));
		}
		response.setComments(commentResponse);
	    } catch (Exception e) {
		throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	    }

	    // 用户查看消息后，将accountId清空
	    PushMessageUtil.getInstance().clearCount(userId);

	    ret.setCode(DataResponse.OK);
	    ret.setData(response);
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
     * 查询评论数量
     * 
     * @param dashboardType
     * @param dashboardData
     * @return
     */
    public DataResponse<Integer> findCommentNum(Integer dashboardType, Long dashboardData) {
	DataResponse<Integer> ret = new DataResponse<Integer>();
	try {

	    ParamemeterAssert.assertDataNotNull(dashboardType, dashboardData);

	    int noteNum = 0;
	    try {
		noteNum = LifeixCommentUtil.getInstance().getAllCommentNumByTargetId(dashboardType, dashboardData);
	    } catch (Exception e) {
		LOG.warn(String.format("comment client get note num fail (%d,%d) - %s", dashboardType, dashboardData,
		        e.getMessage()));
	    }

	    ret.setCode(DataResponse.OK);
	    ret.setData(noteNum);
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

    /**
     * 删除评论
     */
    @Override
    public DataResponse<Object> dropComment(Long id) {
	DataResponse<Object> ret = new DataResponse<Object>();
	try {

	    ParamemeterAssert.assertDataNotNull(id);

	    try {
		LifeixCommentUtil.getInstance().delComment(id);
	    } catch (Exception e) {
		LOG.warn(String.format("comment client del comment fail (%d) - %s", id, e.getMessage()));
	    }

	    ret.setCode(DataResponse.OK);
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

}
