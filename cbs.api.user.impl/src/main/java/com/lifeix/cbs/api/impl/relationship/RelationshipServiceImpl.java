package com.lifeix.cbs.api.impl.relationship;

import com.l99.common.utils.TimeUtil;
import com.lifeix.cbs.achieve.bean.bo.RelationAchieveBO;
import com.lifeix.cbs.achieve.common.constant.BehaviorConstants;
import com.lifeix.cbs.achieve.service.AchieveService;
import com.lifeix.cbs.api.bean.relationship.RelationShipListResponse;
import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.cbs.api.common.mission.Mission;
import com.lifeix.cbs.api.common.util.ContestConstants.TempletId;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.common.util.XingePushUtil;
import com.lifeix.cbs.api.dao.user.CbsUserDao;
import com.lifeix.cbs.api.dto.user.CbsUser;
import com.lifeix.cbs.api.impl.util.AccountTransformUtil;
import com.lifeix.cbs.api.service.misson.MissionUserService;
import com.lifeix.cbs.api.service.relationship.RelationshipService;
import com.lifeix.cbs.message.service.notify.NotifyService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.framework.memcache.MemcacheService;
import com.lifeix.nyx.relationship.service.CbsRelationshipService;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("relationshipService")
public class RelationshipServiceImpl implements RelationshipService {
    private static final Logger LOG = LoggerFactory.getLogger(RelationshipServiceImpl.class);
    @Autowired
    CbsRelationshipService cbsRelationshipService;
    @Autowired
    private CbsUserDao cbsUserDao;
    @Autowired
    NotifyService notifyService;

    @Autowired
    protected MemcacheService memcacheService;

    @Autowired
    protected AchieveService achieveService;

    @Autowired
    protected MissionUserService missionUserService;

    private static final String RELATIONSHIPADD = "RELATIONSHIPADD";
    private static final int EXPIRESECONDS = 60 * 60;

    @Override
    public CbsUserResponse addFollow(Long accountId, Long targetId) throws L99IllegalParamsException, L99IllegalDataException {
        CbsUserResponse cbsUserResponse = new CbsUserResponse();
        CbsUser user = cbsUserDao.selectById(targetId);
        CbsUser account = cbsUserDao.selectById(accountId);
        if (user == null || account == null) {
            throw new L99IllegalParamsException("添加关注的用户不存在");
        }
        cbsRelationshipService.addAttention(accountId, targetId);
        List<Long> targetIds = new ArrayList<Long>();
        targetIds.add(targetId);
        Map<Long, Integer> map = cbsRelationshipService.getAttentionType(accountId, targetIds);
        List<Long> meAttentionlist = cbsRelationshipService.getMeAttention(accountId, 0, 21);
        if (meAttentionlist.size() == 20){
            boolean flag = missionUserService.validate(accountId, Mission.CONCERN_20);
            cbsUserResponse.setShow_mission(flag);
        } else {
            cbsUserResponse.setShow_mission(false);
        }
        List<Long> attentionMelist = cbsRelationshipService.getMeAttention(targetId, 0, 21);
        if (attentionMelist.size() == 20){
            boolean flag = missionUserService.validate(targetId, Mission.FAN_20);
            if (flag){
                // 初始化信鸽消息map
                Map<String, Object> customContent = new HashMap<>();
                customContent.put("scheme_link", "cbs://mission/");
                String msg = "已经有20个好友关注了您";
                // 发送信鸽消息提醒
                XingePushUtil.getInstance().pushSingleAccount(targetId, "", msg, customContent);
            }
        }
        // 发送通知
        JSONObject params = new JSONObject();
        try {
            params.put("user_name", account.getUserName());
            params.put("create_time", TimeUtil.getUtcTimeForDate(new Date()));
        } catch (JSONException e) {
            LOG.error("addFollow " + e.getMessage());
        }

        Object o = memcacheService.get(RELATIONSHIPADD + accountId.toString());

        if (o == null || !targetId.toString().equals(o.toString())) {
            memcacheService.set(RELATIONSHIPADD + accountId.toString(), targetId, EXPIRESECONDS);
            notifyService.addNotify(TempletId.USER_RELATION, targetId, accountId, params.toString(), null);
        }

        // 关注成就通知
        try {
            RelationAchieveBO bo = new RelationAchieveBO();
            bo.setBehavior_type(BehaviorConstants.BehaviorType.RELATION_TYPE);
            bo.setUser_id(accountId);
            bo.setTarget_id(targetId);
            bo.setAction_type(1);
            achieveService.addAchieveData(bo.generateData(), false);
        } catch (Throwable t) {
            LOG.error("userId=" + accountId + ", relation achieve data analysis failed: " + t.getMessage(), t);
        }
        cbsUserResponse.setRelationship_type(map.get(targetId));
        return cbsUserResponse;
    }

    @Override
    public void deleteFollow(Long accountId, Long targetId) throws L99IllegalParamsException, L99IllegalDataException {
	cbsRelationshipService.delAttention(accountId, targetId);

	// 取消关注成就通知
	try {
	    RelationAchieveBO bo = new RelationAchieveBO();
	    bo.setBehavior_type(BehaviorConstants.BehaviorType.RELATION_TYPE);
	    bo.setUser_id(accountId);
	    bo.setTarget_id(targetId);
	    bo.setAction_type(0);
	    achieveService.addAchieveData(bo.generateData(), false);
	} catch (Throwable t) {
	    LOG.error("userId=" + accountId + ", relation achieve data analysis failed: " + t.getMessage(), t);
	}

    }

    @Override
    public RelationShipListResponse getRelationshipByType(Integer type, Long accountId, Long targetId, Long startId,
            Long endId, Integer limit) throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(targetId, type);
	if (limit < 40) {
	    limit = 40;
	}
	if (1 == type) {
	    // 关注的人
	    return getFollowing(accountId, targetId, startId, endId, limit);
	} else if (2 == type) {
	    // 粉丝
	    return getFollowers(accountId, targetId, startId, endId, limit);
	} /*
	   * else { // 双向关注列表 return getUserMutualFollowingV2(accountId,
	   * targetId, startId, endId, limit); }
	   */
	return new RelationShipListResponse();
    }

    /**
     * 查看我关注的人
     * 
     * @param targetId
     * @param startId
     * @param endId
     * @param limit
     * @return
     */
    private RelationShipListResponse getFollowing(Long accountId, Long targetId, Long startId, Long endId, Integer limit) {
	RelationShipListResponse resp = new RelationShipListResponse();
	Integer nowPage = 1;
	// 将startId转换成
	if (startId != null) {
	    nowPage = startId.intValue();
	}
	if (endId != null) {
	    nowPage = endId.intValue();
	}
	List<Long> accountIds = new ArrayList<Long>();
	try {

	    List<Long> list = cbsRelationshipService.getMeAttention(targetId, (nowPage - 1) * limit, limit + 1);

	    if (list != null && !list.isEmpty()) {
		if (list.size() > limit) {
		    resp.setStartId((long) nowPage + 1);
		} else {
		    resp.setStartId(-1L);
		}

		for (Long ship : list) {
		    accountIds.add(ship);
		}
	    }

	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}

	if (accountIds.size() > 0) {
	    Map<Long, CbsUser> accountMap = cbsUserDao.findCsAccountMapByIds(accountIds);
	    List<CbsUserResponse> users = new ArrayList<CbsUserResponse>();
	    Map<Long, Integer> map = null;
	    if (accountId != null) {
		map = cbsRelationshipService.getAttentionType(accountId, accountIds);
	    } else {
		map = new HashMap<Long, Integer>();
	    }

	    for (Long id : accountIds) {
		CbsUserResponse casr = AccountTransformUtil.transformUser(accountMap.get(id), false);
		if (casr != null) {
		    if (accountId == id) {
			casr.setRelationship_type(1);
		    } else {
			casr.setRelationship_type(map.get(id));
		    }
		    users.add(casr);
		}
	    }
	    resp.setUsers(users);

	}
	return resp;
    }

    /**
     * 
     * @param targetId
     * @param startId
     * @param endId
     * @param limit
     * @return
     */
    private RelationShipListResponse getFollowers(Long accountId, Long targetId, Long startId, Long endId, Integer limit) {
	RelationShipListResponse resp = new RelationShipListResponse();
	Integer nowPage = 1;
	// 将startId转换成
	if (startId != null) {
	    nowPage = startId.intValue();
	}
	if (endId != null) {
	    nowPage = endId.intValue();
	}
	List<Long> accountIds = new ArrayList<Long>();
	try {
	    List<Long> list = cbsRelationshipService.getAttentionMe(accountId, targetId, (nowPage - 1) * limit, limit + 1);
	    if (list != null && !list.isEmpty()) {
		accountIds = list;
		if (list.size() > limit) {
		    resp.setStartId((long) nowPage + 1);
		} else {
		    resp.setStartId(-1L);
		}
	    }

	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}

	if (accountIds.size() > 0) {
	    // 从nyx 用户表中获取用户信息
	    Map<Long, CbsUser> accountMap = cbsUserDao.findCsAccountMapByIds(accountIds);
	    List<CbsUserResponse> users = new ArrayList<CbsUserResponse>();
	    Map<Long, Integer> map = null;
	    if (accountId != null) {
		map = cbsRelationshipService.getAttentionType(accountId, accountIds);
	    } else {
		map = new HashMap<Long, Integer>();
	    }
	    for (Long id : accountIds) {
		CbsUserResponse casr = AccountTransformUtil.transformUser(accountMap.get(id), false);
		if (casr != null) {
		    if (accountId == id) {
			casr.setRelationship_type(1);
		    } else {
			casr.setRelationship_type(map.get(id));
		    }

		    users.add(casr);
		}
	    }
	    resp.setUsers(users);
	}
	return resp;
    }

    @Override
    public List<Long> getAllMeAttention(Long accountId) {
	return cbsRelationshipService.getMeAttention(accountId, 0, 0);
    }

    @Override
    public List<Long> getAllAttentionMe(Long accountId) {
	return cbsRelationshipService.getAttentionMe(null, accountId, 0, 0);
    }

}