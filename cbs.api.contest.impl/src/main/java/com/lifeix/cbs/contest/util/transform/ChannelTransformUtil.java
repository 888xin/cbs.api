package com.lifeix.cbs.contest.util.transform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.lifeix.cbs.contest.bean.channel.ContestChannelResponse;
import com.lifeix.cbs.contest.dto.channel.ContestChannel;
import com.lifeix.common.utils.TimeUtil;

public class ChannelTransformUtil {

    public static ContestChannelResponse transformChannel(ContestChannel channel) {
	if (channel == null) {
	    return null;
	}
	ContestChannelResponse resp = new ContestChannelResponse();
	resp.setId(channel.getId());
	resp.setName(channel.getName());
	resp.setSort(channel.getSort());
	resp.setCreate_time(TimeUtil.getUtcTimeForDate(channel.getCreateTime()));
	//从data中获取cupId列表
	List<Long> cupId = new ArrayList<Long>();
	
	if (!StringUtils.isEmpty(channel.getData())) {
	    List<String> cupIdStr = new ArrayList<String>();
	    cupIdStr =  Arrays.asList(channel.getData().split(","));
            for(int i=0;i<cupIdStr.size();i++){
        	cupId.add(Long.parseLong(cupIdStr.get(i).trim()));
            }
	}
	resp.setContest_type(channel.getContestType());
	resp.setCup_id(cupId);
	return resp;
    }
}
