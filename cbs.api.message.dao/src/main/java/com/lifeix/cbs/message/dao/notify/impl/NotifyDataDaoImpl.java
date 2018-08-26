package com.lifeix.cbs.message.dao.notify.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.message.dao.MessageDaoSupport;
import com.lifeix.cbs.message.dao.notify.NotifyDataDao;
import com.lifeix.cbs.message.dto.notify.NotifyData;

@Service("notifyDataDao")
public class NotifyDataDaoImpl extends MessageDaoSupport implements NotifyDataDao {

    @Override
    public boolean insert(NotifyData nData) {
	return sqlSession.insert("NotifyDataMapper.insert", nData) > 0;
    }

    /**
     * 查询未读消息数量
     * 
     * @param userId
     * @param type
     * @param readFlag
     * @return
     */
    @Override
    public int getUnreadNotifyCount(Long userId, List<Integer> type) {
	Map<String, Object>  map = new HashMap<String, Object>();
	map.put("userId", userId);
	map.put("type", type);
	Object o = sqlSession.selectOne("NotifyDataMapper.getUnreadNotifyCount", map);
	return o == null? 0 : (Integer)o;
    }

    /**
     * 查用户消息提醒
     * 
     * @param userId
     * @param startId
     * @param endId
     * @param type
     * @param limit
     * @return
     */
    @Override
    public List<NotifyData> findUserNotifys(Long userId, Long page, Long endId, List<Integer> type, Integer limit) {
	Map<String, Object>  map = new HashMap<String, Object>();
	map.put("userId", userId);
	map.put("startId", (page-1) * limit);
	map.put("type", type);
	map.put("limit", limit + 1);
	return sqlSession.selectList("NotifyDataMapper.findUserNotifys", map);
    }

    /**
     * 把对应类型的未读消息置为已读
     * 
     * @param userId
     * @param type
     * @return
     */
    @Override
    public void updateUnreadNotify(Long userId, List<Integer> type) {

	Map<String, Object>  map = new HashMap<String, Object>();
	map.put("userId", userId);
	map.put("type", type);
	sqlSession.update("NotifyDataMapper.updateUnreadNotify", map);
    }

}
