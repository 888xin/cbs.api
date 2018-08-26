package com.lifeix.cbs.content.impl.redis;

import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.util.RedisConstants;
import com.lifeix.framework.redis.impl.RedisDataIdentify;
import com.lifeix.framework.redis.impl.RedisHashHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lhx on 16-5-5 下午6:06
 *
 * @Description 资讯评论数统计
 */
@Service("redisCommentCountHandler")
public class RedisCommentCountHandler extends ImplSupport {

    @Autowired
    private RedisHashHandler redisHashHandler;

    /**
     * 添加统计
     */
    public void addCommentCount(Long contentId, Integer index) {
        try {
            RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTENT, RedisConstants.ContentRedis.COMMENT);
            indentify.setIdentifyId(contentId.toString());
            redisHashHandler.opsIncr(indentify, index.toString());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    /**
     * 获得数量
     */
    public int getCommentCount(Long contentId) {
        int total = 0;
        try {
            RedisDataIdentify indentify = new RedisDataIdentify(RedisConstants.MODEL_CONTENT, RedisConstants.ContentRedis.COMMENT);
            byte[] dayByte = redisHashHandler.hget(indentify, contentId.toString().getBytes());
            if (dayByte != null && dayByte.length > 0) {
                total = new Integer(new String(dayByte));
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return total;
    }
}
