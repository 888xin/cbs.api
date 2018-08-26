package com.lifeix.cbs.content.dao.boot;

import java.util.List;

import com.lifeix.cbs.content.dto.boot.BootInfo;
import com.lifeix.cbs.content.dto.contest.ContestNews;


public interface BootInfoDao {

    /**
     * 插入 - 自动生成主键
     * 
     * @param contentMain
     * @return
     */
    public Boolean insert(BootInfo bootInfo);

    /**
     * 根据主键删除
     * 
     * @param id
     * @return
     */
    public Boolean deleteById(long id);

    /**
     * 根据主键禁用
     * 
     * @param id
     * @return
     */
    public Boolean disableById(long id);
    
    /**
     * 根据主键启用
     * 
     * @param id
     * @return
     */
    public Boolean ableById(long id);

    /**
     * 开机信息列表
     * 
     * @param startId
     * @param limit
     * @return
     */
    public List<BootInfo> findBootInfos(Long startId, int limit);

    /**
     * 最新一条开机信息
     * 
     * @return
     */
    public BootInfo selectLast();
    /**
     * 查找单个
     * @param id
     * @return
     */
    public BootInfo findOneById(Long id);
    /**
     * 修改
     * @param bootInfo
     * @return
     */
    public boolean edit(BootInfo bootInfo);
}