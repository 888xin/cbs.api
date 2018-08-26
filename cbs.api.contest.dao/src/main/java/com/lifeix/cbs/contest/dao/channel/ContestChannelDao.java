package com.lifeix.cbs.contest.dao.channel;

import java.util.List;
import com.lifeix.cbs.contest.dto.channel.ContestChannel;


public interface ContestChannelDao {

    /**
     * 赛事分类列表
     * 
     * @return
     */
    public List<ContestChannel> selectAll();
    
    /**
     * 根据主键查询记录
     * 
     * @param id
     * @return
     */
    public ContestChannel selectById(long id);

  

    /**
     * 插入
     * 
     * @param Channel
     * @return
     */
    public boolean insert(ContestChannel contestChannel);

    /**
     * 更新
     * 
     * @param ftContent
     * @return
     */
    public boolean update(ContestChannel contestChannel);

    /**
     * 根据主键删除
     * 
     * @param id
     * @return
     */
    public boolean deleteById(long id);
}
