package com.lifeix.cbs.contest.dao.bb;

import java.util.List;
import java.util.Map;

import com.lifeix.cbs.contest.dto.bb.BbTeam;

public interface BbTeamDao {

    public BbTeam selectById(long id);

    /**
     * 修改球队
     * 
     * @param team
     * @return
     */
    public Boolean update(BbTeam team);

    /**
     * 根据名字搜索球队
     * 
     * @param key
     * @return
     */
    public List<BbTeam> searchTeam(String key);

    /**
     * 批量查找列表
     * 
     * @param ids
     * @return
     */
    public Map<Long, BbTeam> findBbTeamMapByIds(List<Long> ids);

    /**
     * spark批量保存球队
     * 
     * @param list
     * @return
     */
    public boolean saveTeam(List<BbTeam> list);

    /**
     * spark获取所有球队(key为targetId)
     * 
     * @return
     */
    public Map<Long, BbTeam> getAllTeam();

    /**
     * spark按第三方ids查找球队(key为targetId)
     * 
     * @param targetIds
     * @return
     */
    public Map<Long, BbTeam> getTeamByTargetIds(List<Long> targetIds);

}