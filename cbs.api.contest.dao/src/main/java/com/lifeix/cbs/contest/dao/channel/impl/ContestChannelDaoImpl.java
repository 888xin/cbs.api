package com.lifeix.cbs.contest.dao.channel.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.channel.ContestChannelDao;
import com.lifeix.cbs.contest.dto.channel.ContestChannel;

@Service("contestChannelDao")
public class ContestChannelDaoImpl extends ContestDaoSupport implements ContestChannelDao {
    
    //整个分类列表
    private static final String CONTEST_CHANNEL_LIST_KEY = "selectAll";

    @Override
    public List<ContestChannel> selectAll() {
	String cacheKey = getCustomCache(CONTEST_CHANNEL_LIST_KEY);
	List<ContestChannel> channels = memcacheService.get(cacheKey);
	if(channels==null){
	    channels = sqlSession.selectList("ContestChannelMapper.selectAll");
	    if(channels!=null){
		 memcacheService.set(cacheKey, channels);
	    }
	}
	return channels;
    }

    @Override
    public ContestChannel selectById(long id) {
	String cacheKey = getCacheId(id);
	ContestChannel contestChannel = memcacheService.get(cacheKey);
	if (contestChannel == null) {
	    contestChannel = sqlSession.selectOne("ContestChannelMapper.selectById", id);
	    if (contestChannel != null) {
		memcacheService.set(cacheKey, contestChannel);
	    }
	}
	return contestChannel;
    }

    @Override
    public boolean insert(ContestChannel contestChannel) {
	sqlSession.insert("ContestChannelMapper.insert", contestChannel);
	if (contestChannel.getId() > 0) {
	    deleteCache(contestChannel);
	    return true;
	}
	return false;
    }

    @Override
    public boolean update(ContestChannel contestChannel) {
	int res = sqlSession.update("ContestChannelMapper.update", contestChannel);
	if (res > 0) {
	    deleteCache(contestChannel);
	    return true;
	}
	return false;
    }

    @Override
    public boolean deleteById(long id) {
	ContestChannel contestChannel = selectById(id);

	int res = sqlSession.delete("ContestChannelMapper.deleteById", id);
	if (res > 0) {
	    deleteCache(contestChannel);
	    return true;
	}
	return false;
    }
    
    public void deleteCache(ContestChannel contestChannel) {

  	if (contestChannel == null) {
  	    return;
  	}
  	// 分类列表缓存
  	if (contestChannel != null) {
  	    String cacheKey1 = getCustomCache(CONTEST_CHANNEL_LIST_KEY);
  	    memcacheService.delete(cacheKey1);
  	}
  	
  	//单个分类缓存
  	if (contestChannel != null) {
  	    String cacheKey2 = getCacheId(contestChannel.getId());
  	    memcacheService.delete(cacheKey2);
  	}
      }

}
