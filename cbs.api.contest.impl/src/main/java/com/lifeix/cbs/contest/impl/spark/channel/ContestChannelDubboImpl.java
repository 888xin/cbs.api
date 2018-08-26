package com.lifeix.cbs.contest.impl.spark.channel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.contest.bean.channel.ContestChannelResponse;
import com.lifeix.cbs.contest.dao.channel.ContestChannelDao;
import com.lifeix.cbs.contest.dto.channel.ContestChannel;
import com.lifeix.cbs.contest.service.spark.channel.ContestChannelDubbo;
import com.lifeix.cbs.contest.util.transform.ChannelTransformUtil;

@Service("contestChannelDubbo")
public class ContestChannelDubboImpl implements ContestChannelDubbo {
    @Autowired
    private ContestChannelDao contestChannelDao;
    
    @Override
    public List<ContestChannelResponse> getAll() {
	List<ContestChannel> channels =  contestChannelDao.selectAll();
	List<ContestChannelResponse> contestChannelResponse = new ArrayList<ContestChannelResponse>();
	for (ContestChannel contestChannel : channels) {
	    contestChannelResponse.add(ChannelTransformUtil.transformChannel(contestChannel));
	}
	return contestChannelResponse;
    }
    
    
    public ContestChannelResponse viewChannel(Long channelId) {
        ContestChannel contestChannel = contestChannelDao.selectById(channelId);
        ContestChannelResponse contestChannelResponse = null;
        if(contestChannel!=null){
              contestChannelResponse = ChannelTransformUtil.transformChannel(contestChannel);
        }
  	return contestChannelResponse;
      }


    @Override
    public boolean updateChannel(Long id, String name, String data, Integer sort, Integer contestType) {
	ContestChannel contestChannel = contestChannelDao.selectById(id);
	if(contestChannel!=null){
	    contestChannel.setName(name);
	    contestChannel.setData(data);
	    contestChannel.setSort(sort);
	    contestChannel.setContestType(contestType);
	  return contestChannelDao.update(contestChannel);
	}
	return false;
    }


    @Override
    public boolean dropChannel(Long id) {
	return contestChannelDao.deleteById(id);	
    }


    @Override
    public boolean addChannel(String name, String data, Integer sort, Integer contestType) {
	ContestChannel contestChannel = new ContestChannel();
	contestChannel.setCreateTime(new Date());
	contestChannel.setData(data);
	contestChannel.setSort(sort);
	contestChannel.setName(name);
	contestChannel.setContestType(contestType);
	return contestChannelDao.insert(contestChannel);		
    }

}
