package com.lifeix.cbs.contest.service.spark.channel;

import java.util.List;

import com.lifeix.cbs.contest.bean.channel.ContestChannelResponse;

/**
 * 请求获取赛事分类列表
 * @author lifeix
 *
 */
public interface ContestChannelDubbo {
    /**
     * 获取所有的赛上类型
     * @return
     */
    public List<ContestChannelResponse> getAll();
    /**
     * 根据channelId获取contestChannel对象
     * @param channelId
     * @return
     */
    public ContestChannelResponse viewChannel(Long channelId); 
    /**
     * 修改
     * @param id
     * @param name
     * @param data
     * @param sort
     * @return
     */
    public boolean updateChannel(Long id, String name, String data, Integer sort,Integer contestType);
    /**
     * 删除
     * @param id
     * @return
     */
    public boolean dropChannel(Long id);
    /**
     * 添加分类
     * @param name
     * @param data
     * @param sort
     * @return
     */
    public boolean addChannel(String name, String data, Integer sort, Integer contestType);
    
}
