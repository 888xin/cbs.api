/**
 * 
 */
package com.lifeix.cbs.content.impl.inform;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.user.LifeixUserApiUtil;
import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.common.util.ContentConstants.InformStatus;
import com.lifeix.cbs.api.common.util.ContentConstants.InformType;
import com.lifeix.cbs.api.service.user.CbsUserService;
import com.lifeix.cbs.api.util.LifeixCommentUtil;
import com.lifeix.cbs.api.util.LifeixTiyuApiDubboUtil;
import com.lifeix.cbs.content.bean.inform.InformListResponse;
import com.lifeix.cbs.content.bean.inform.InformResponse;
import com.lifeix.cbs.content.dao.inform.InformDao;
import com.lifeix.cbs.content.dto.inform.Inform;
import com.lifeix.cbs.content.service.inform.InformService;
import com.lifeix.cbs.contest.bean.circle.FriendCircleCommResponse;
import com.lifeix.cbs.contest.bean.circle.FriendCircleResponse;
import com.lifeix.cbs.contest.service.cirle.FriendCircleCommService;
import com.lifeix.cbs.contest.service.cirle.FriendCircleService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;
import com.lifeix.tiyu.content.bean.main.cbs.CbsContentResponse;
import com.lifeix.user.beans.account.AccountResponse;

/**
 * @author lifeix
 *
 */
@Service("informService")
public class InformServiceImpl extends ImplSupport implements InformService {

    @Autowired
    private CbsUserService cbsUserService;

    @Autowired
    private FriendCircleService friendCircleService;

    @Autowired
    private FriendCircleCommService commentService;

    @Autowired
    private InformDao informDao;

    /**
     * 新增或更新举报
     * 
     * @param containId
     * @param contain
     * @param image
     * @param informerId
     * @param informType
     * @param informReason
     * @param userId
     * @param type
     * @return
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     * @throws L99NetworkException
     * @throws JSONException
     */
    @Override
    public Long addOrUpdateInform(Long containId, String contain, String image, Long informerId, Integer informType,
	    String informReason, Long userId, Integer type) throws L99IllegalParamsException, L99IllegalDataException,
	    L99NetworkException, JSONException {
	Long id = null;
	ParamemeterAssert.assertDataNotNull(informType, type);
	Inform inform = informDao.selectByContainId(containId, type, InformStatus.PENDING);
	if (inform != null) {// 不为第一次举报
	    id = inform.getId();
	    Integer total = inform.getTotal() + 1;// 被举报次数+1
	    boolean isOk = informDao.updateInform(id, total, informType, informReason);
	    if (!isOk) {
		throw new L99IllegalDataException(BasicMsg.CODE_OPERATE_FAIL, BasicMsg.KEY_OPERATE_FAIL);
	    }
	    return id;
	} else {// 第一次举报
	    inform = new Inform();
	    inform.setType(type);
	    inform.setContainId(containId);
	    if (informerId == null) {// 匿名举报
		informerId = 0L;
	    }
	    inform.setInformerId(informerId);
	    inform.setInformType(informType);
	    inform.setInformReason(informReason);// 新闻评论举报为被举报用户名
	    inform.setStatus(InformStatus.PENDING);
	    inform.setTotal(1);
	    switch (type) {
	    case InformType.COMMENT:
		FriendCircleCommResponse commResponse = commentService.getSingleComment(containId);
		inform.setContain(commResponse.getContent());
		inform.setImage(commResponse.getImage());
		inform.setUserId(commResponse.getComm_userid());
		break;
	    case InformType.CONTENT:
		FriendCircleResponse contentResponse = friendCircleService.getSingleContentResponse(containId);
		inform.setContain(contentResponse.getContent());
		if (contentResponse.getImages() != null) {
		    inform.setImage(contentResponse.getImages()[0]);
		} else {
		    inform.setImage(null);
		}
		inform.setUserId(contentResponse.getUser().getUser_id());
		break;
	    case InformType.IM:
		inform.setContain(contain);
		inform.setImage(image);
		inform.setUserId(userId);
		break;
	    case InformType.USER:
		try {
		    AccountResponse userResponse = LifeixUserApiUtil.getInstance().findUser(containId);
		    inform.setContain(userResponse.getName());
		} catch (L99NetworkException e) {
		    inform.setContain("用户已被删除或屏蔽");
		}
		inform.setUserId(containId);
		inform.setImage(null);
		break;
	    case InformType.NEWS_COMMENT:
		inform.setContain(contain);
		inform.setImage(null);
		inform.setUserId(0L);// 新闻评论用户非系统用户
		break;
	    case InformType.NEWS:
		CbsContentResponse resp = LifeixTiyuApiDubboUtil.getInstance()
		        .findContentById(containId, null, false, false);
		if (resp != null) {
		    inform.setContain(resp.getTitle());
		    if (resp.getImage() != null) {
			inform.setImage(resp.getImage());
		    } else {
			inform.setImage("无");
		    }
		    inform.setInformReason(resp.getEditor());// 新闻举报理由为编辑
		    inform.setUserId(resp.getUser_id());
		}
		break;
	    }
	    id = informDao.insertInform(inform);
	    if (id < 0) {
		throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	    }
	}
	return id;
    }

    /**
     * 后台获取举报列表
     * 
     * @param page
     * @param limit
     * @param status
     * @param type
     * @return
     * @throws JSONException
     */
    @Override
    public InformListResponse getInformList(Integer page, int limit, Integer status, Integer type) throws JSONException {
	InformListResponse list = new InformListResponse();
	List<InformResponse> informResponseList = new ArrayList<InformResponse>();
	InformResponse informResponse = null;
	int pagecount = 0;
	if (page != 0 && page != null) {
	    pagecount = page * limit;
	}
	List<Inform> informList = informDao.selectList(pagecount, limit, status, type);
	if (informList.size() > 0) {
	    for (Inform inform : informList) {
		informResponse = transform(inform, pagecount);
		informResponseList.add(informResponse);
	    }
	}
	page++;
	list.setPage_num(page);
	list.setInforms(informResponseList);
	return list;
    }

    /**
     * 处理举报
     * 
     * @param id
     * @param status
     * @param disposeInfo
     * @param lastTime
     * @param type
     * @throws L99IllegalDataException
     */
    @Override
    public void updateInformStatus(Long id, Integer status, String disposeInfo, Long lastTime, Integer type)
	    throws L99IllegalDataException {
	Inform inform = informDao.selectById(id);
	Date removeTime = null;
	if (lastTime != null) {
	    Date date = new Date();
	    if (lastTime > 0) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, Integer.valueOf(String.valueOf(lastTime)));
		removeTime = cal.getTime();
	    } else if (lastTime == 0) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, 1000);// 一千年，等于永久
		removeTime = cal.getTime();
	    } else {
		removeTime = null;
	    }
	}
	boolean isOk = informDao.updateStatusById(id, status, disposeInfo, removeTime);
	if (!isOk) {
	    throw new L99IllegalDataException(BasicMsg.CODE_OPERATE_FAIL, BasicMsg.KEY_OPERATE_FAIL);
	}
	if (status == InformStatus.SHEILDED) {// 若为屏蔽处理
	    if (type == InformType.NEWS_COMMENT) {// 新闻评论
		try {
		    LifeixCommentUtil.getInstance().delComment(inform.getContainId());
		} catch (Exception e) {
		    LOG.warn(String.format("comment client del comment fail (%d) - %s", id, e.getMessage()));
		}
	    } else if (type == InformType.NEWS) {// 新闻
		try {
		    LifeixTiyuApiDubboUtil.getInstance().dropContent(inform.getContainId());
		} catch (Exception e) {
		    LOG.warn(String.format("tiyu api drop content fail (%d) - %s", id, e.getMessage()));
		}
	    }

	}
    }

    private InformResponse transform(Inform inform, Integer type) throws JSONException {
	Date nowDate = new Date();
	InformResponse resp = new InformResponse();
	resp.setId(inform.getId());
	resp.setContain(inform.getContain());
	resp.setType(inform.getType());
	resp.setContain_id(inform.getContainId());
	resp.setUser_id(inform.getUserId());
	resp.setInformer_id(inform.getInformerId());
	resp.setInform_type(inform.getInformType());
	resp.setInform_reason(inform.getInformReason());
	resp.setStatus(inform.getStatus());
	resp.setDispose_info(inform.getDisposeInfo());
	resp.setTotal(inform.getTotal());
	resp.setUpdate_time(CbsTimeUtils.getUtcTimeForDate(inform.getUpdateTime()));
	if (inform.getInformerId() != 0) {// 是否为匿名举报
	    AccountResponse informer = new AccountResponse();
	    try {
		informer = LifeixUserApiUtil.getInstance().findUser(inform.getInformerId());
		resp.setInformer_name(informer.getName());
	    } catch (L99NetworkException e) {
		resp.setInformer_name("用户已被屏蔽或删除");
	    }
	} else {
	    resp.setInformer_name("匿名举报");
	}
	if (inform.getRemoveTime() != null) {
	    if (nowDate.after(inform.getRemoveTime()) && inform.getStatus() != InformStatus.IGNORED) {
		informDao.updateStatusById(inform.getId(), InformStatus.IGNORED, inform.getDisposeInfo() + "    解禁时间到自动解除",
		        inform.getRemoveTime());
	    }
	    resp.setRemove_time(CbsTimeUtils.getUtcTimeForDate(inform.getRemoveTime()));
	}
	if (inform.getUserId() != null) {
	    AccountResponse userResponse = null;
	    try {
		userResponse = LifeixUserApiUtil.getInstance().findUser(inform.getUserId());
		resp.setUser_name(userResponse.getName());
	    } catch (L99NetworkException e) {
		resp.setUser_name("用户已被屏蔽或删除");
	    }
	    resp.setUserResponse(userResponse);
	}
	if (inform.getImage() != null) {
	    resp.setImage(inform.getImage());
	} else {
	    resp.setImage("无");
	}
	return resp;
    }

}
