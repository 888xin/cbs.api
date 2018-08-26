package com.lifeix.cbs.contest.dao.fb;

import java.util.List;
import java.util.Map;

import com.lifeix.cbs.contest.dto.fb.FbTeam;

public interface FbTeamDao {

    public FbTeam selectById(long id);

    /**
     * 修改球队
     * 
     * @param team
     * @return
     */
    public Boolean update(FbTeam team);

    /**
     * 根据名字搜索球队
     * 
     * @param key
     * @return
     */
    public List<FbTeam> searchTeam(String key);

    /**
     * 批量查找列表
     * 
     * @param ids
     * @return
     */
    public Map<Long, FbTeam> findFbTeamMapByIds(List<Long> ids);

    /**
     * spark按第三方ids查球队(key为targetId)
     * 
     * @param targetIds
     * @return
     */
    public Map<Long, FbTeam> getTeamByTargetIds(List<Long> targetIds);

    /**
     * spark获取所有球队(key为targetId)
     * 
     * @return
     */
    public Map<Long, FbTeam> getAllTeam();

    /**
     * spark批量保存球队
     * 
     * @param list
     * @return
     */
    public boolean saveTeam(List<FbTeam> list);

}