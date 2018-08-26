package com.lifeix.cbs.contest.dao.circle.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.common.util.ContestConstants.FriendType;
import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.circle.UserFriendCircleDao;
import com.lifeix.cbs.contest.dto.circle.UserFriendCircle;

@Service("userFriendCircleDao")
public class UserFriendCircleDaoImpl extends ContestDaoSupport implements UserFriendCircleDao {

    @Override
    public List<Long> getUserFriendCircleIds(Long userId, List<Long> targetIds, Integer friendType, Integer page,
	    Integer limit) {
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("targetIds", targetIds);
	map.put("start", (page - 1) * limit);
	if (friendType != null) {
	    if (friendType.intValue() == FriendType.CONTEST) {
		map.put("type", 1);
		map.put("hasContent", 0);
	    } else if (friendType.intValue() == FriendType.REASON) {
		map.put("type", 1);
		map.put("hasContent", 1);
	    } else if (friendType.intValue() == FriendType.TUCAO) {
		map.put("type", 0);
		map.put("hasContent", 1);
	    }
	}
	map.put("limit", limit + 1);
	return sqlSession.selectList("UserFriendCircleMapper.getUserFriendCircleIds", map);
    }

    @Override
    public Integer insert(List<UserFriendCircle> userCirlces) {
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("msgs", userCirlces);
	return sqlSession.insert("UserFriendCircleMapper.insert", map);
    }

    @Override
    public Integer update(UserFriendCircle ufc) {
	Long friendCircleId = ufc.getFriendCircleId();
	if (friendCircleId == null) {
	    return 0;
	}
	UserFriendCircle db = getUserFriendCircle(friendCircleId);
	if (db == null) {
	    List<UserFriendCircle> userCirlces = new ArrayList<UserFriendCircle>();
	    ufc.setDeleteFlag(false);
	    userCirlces.add(ufc);
	    return insert(userCirlces);
	} else {
	    db.setDeleteFlag(ufc.getDeleteFlag());
	    db.setHasContent(ufc.getHasContent());
	    db.setType(ufc.getType());
	    return sqlSession.update("UserFriendCircleMapper.update", ufc);
	}

    }

    @Override
    public UserFriendCircle getUserFriendCircle(Long friendCircleId) {
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("friendCircleId", friendCircleId);
	return sqlSession.selectOne("UserFriendCircleMapper.getUserFriendCircle", map);
    }

    @Override
    public Integer deleteByfriendCircleId(Long friendCircleId) {

	Map<String, Object> map = new HashMap<String, Object>();
	map.put("friendCircleId", friendCircleId);
	return sqlSession.update("UserFriendCircleMapper.deleteByfriendCircleId", map);
    
    }

}
