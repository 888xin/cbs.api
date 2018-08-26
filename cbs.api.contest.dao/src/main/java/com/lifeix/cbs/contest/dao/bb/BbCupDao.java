package com.lifeix.cbs.contest.dao.bb;

import java.util.List;
import java.util.Map;

import com.lifeix.cbs.contest.dto.bb.BbCup;

public interface BbCupDao {

    /**
     * spark批量保存联赛
     * 
     * @param list
     * @return
     */
    public boolean saveCup(List<BbCup> list);

    /**
     * spark获取所有联赛(key为targetId)
     * 
     * @return
     */
    public Map<Long, BbCup> getAllCup();

    /**
     * 根据第三方ids查询联赛(key为targetId)
     * 
     * @param targetIds
     * @return
     */
    public Map<Long, BbCup> getCupByTargetIds(List<Long> targetIds);

    /**
     * 根据等级来获得
     */
    public List<BbCup> getLevelCup(int level);
    /**
     * 修改球队名
     * @param cup
     * @return
     */
    public Boolean update(BbCup cup);
    /**
     * 查找单个联赛
     * @param id
     * @return
     */
    public BbCup selectById(long id);
}
