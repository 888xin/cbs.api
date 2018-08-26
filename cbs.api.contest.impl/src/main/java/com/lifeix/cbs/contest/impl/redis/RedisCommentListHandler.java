package com.lifeix.cbs.contest.impl.redis;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.util.RedisConstants;
import com.lifeix.cbs.api.common.util.RedisConstants.ContestRedis;
import com.lifeix.framework.redis.impl.RedisDataIdentify;
import com.lifeix.framework.redis.impl.RedisListHandler;

@Service("redisCommentListHandler")
public class RedisCommentListHandler extends ImplSupport {

    @Autowired
    private RedisListHandler redisListHandler;

    /**
     * 添加用户未读评论记录
     * 
     * @param userId
     */
    public void addUnreadComment(Long userId, Long commendId) {
	try {
	    RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, ContestRedis.CIRCLE_COMMENT);
	    indentify.setIdentifyId(userId.toString());
	    final byte[] commendIdKey = redisListHandler.serialize(String.valueOf(commendId));
	    redisListHandler.insertLeftRedisData(indentify, commendIdKey);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}

    }

    /**
     * 获取用户未读评论数
     * 
     * @param userId
     * @return
     */
    public Long getUnreadCommentCounts(Long userId) {
	Long counts = 0L;
	if (userId == null) {
	    return counts;
	}
	try {
	    RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, ContestRedis.CIRCLE_COMMENT);
	    indentify.setIdentifyId(userId.toString());
	    counts = redisListHandler.countListData(indentify);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
	return counts;
    }

    /**
     * 返回用户所有未读评论id列表
     * 
     * @param userId
     * @return
     */
    public List<Long> getAllUnreadCommendIds(Long userId) {
	List<Long> ids = new ArrayList<Long>();
	try {
	    RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, ContestRedis.CIRCLE_COMMENT);
	    indentify.setIdentifyId(userId.toString());
	    List<byte[]> idDatas = redisListHandler.sMembersByte(indentify, 0, -1);
	    for (byte[] id : idDatas) {
		ids.add(Long.valueOf(new String(id)));
	    }
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
	return ids;
    }

    /**
     * 返回用户未读评论id列表,带分页
     * 
     * @param userId
     * @return
     */
    public List<Long> getUnreadCommendIds(Long userId, final Integer pageNum, final Integer limit) {
	List<Long> ids = new ArrayList<Long>();
	try {
	    RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, ContestRedis.CIRCLE_COMMENT);
	    indentify.setIdentifyId(userId.toString());
	    List<byte[]> idDatas = redisListHandler.lPopList(indentify, limit);
	    for (byte[] id : idDatas) {
		ids.add(Long.valueOf(new String(id)));
	    }
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
	return ids;
    }

    /**
     * 判断是否还有未读评论
     * 
     * @param userId
     * @return
     */
    public boolean hasUnreadCommendIds(Long userId, final Integer limit) {
	try {
	    RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, ContestRedis.CIRCLE_COMMENT);
	    indentify.setIdentifyId(userId.toString());
	    List<byte[]> idDatas = redisListHandler.sMembersByte(indentify, 0, -1);
	    if (idDatas != null && idDatas.size() > 0) {
		return true;
	    } else {
		return false;
	    }
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    return false;
	}
    }

    /**
     * 清空用户所有未读评论
     * 
     * @param userId
     */
    public void resetComment(Long userId) {
	try {
	    RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, ContestRedis.CIRCLE_COMMENT);
	    indentify.setIdentifyId(userId.toString());
	    redisListHandler.delRedisData(indentify);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
    }

    /**
     * 清空一页的未读评论
     * 
     * @param userId
     */
    public void resetCommentsRange(Long userId, final Integer counts) {
	getUnreadCommendIds(userId, 0, counts);
    }

    /**
     * 将用户猜友圈的指定Ids清除
     * 
     * @param userId
     * @param ids
     */
    public void resetSingleComment(Long userId, List<Long> ids) {
	try {

	    if (userId == null) {
		return;
	    }

	    // 查出所有的评论Id
	    List<Long> allIds = getAllUnreadCommendIds(userId);

	    // 移除指定的ids
	    allIds.removeAll(ids);

	    // 清除所有的id列表
	    resetComment(userId);

	    // 插入剩余的id
	    if (allIds.isEmpty()) {
		return;
	    }
	    final byte[][] valKey = new byte[allIds.size()][];
	    int count = 0;
	    for (Long id : allIds) {
		valKey[count++] = redisListHandler.serialize(String.valueOf(id));
	    }
	    RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTEST, ContestRedis.CIRCLE_COMMENT);
	    indentify.setIdentifyId(userId.toString());
	    redisListHandler.batchLeftRedisData(indentify, valKey);
	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	}
    }
}
