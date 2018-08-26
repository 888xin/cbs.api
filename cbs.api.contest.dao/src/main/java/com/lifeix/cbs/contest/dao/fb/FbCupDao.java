package com.lifeix.cbs.contest.dao.fb;

import java.util.List;
import java.util.Map;

import com.lifeix.cbs.contest.dto.bb.BbCup;
import com.lifeix.cbs.contest.dto.fb.FbCup;

public interface FbCupDao {

    /**
     * spark批量保存联赛
     * 
     * @param list
     * @return
     */
    public boolean saveCup(List<FbCup> list);

    /**
     * spark获取所有联赛(key为targetId)
     * 
     * @return
     */
    public Map<Long, FbCup> getAllCup();

    /**
     * 根据第三方ids查询联赛(key为targetId)
     * 
     * @param targetIds
     * @return
     */
    public Map<Long, FbCup> getCupByTargetIds(List<Long> targetIds);

    /**
     * 根据等级来获得
     */
    public List<FbCup> getLevelCup(int level);
    
    /**
     * 修改球队名
     * @param cup
     * @return
     */
    public Boolean update(FbCup cup);
    
    /**
     * 查找单个联赛
     * @param id
     * @return
     */
    public FbCup selectById(long id);
}
