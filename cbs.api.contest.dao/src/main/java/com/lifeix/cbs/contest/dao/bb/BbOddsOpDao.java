package com.lifeix.cbs.contest.dao.bb;

import java.util.List;
import java.util.Map;

import com.lifeix.cbs.contest.dto.odds.OddsOp;

public interface BbOddsOpDao {

    public OddsOp selectById(long id);

    /**
     * 更新赔率
     * 
     * @param entity
     * @return
     */
    public boolean update(OddsOp entity);

    /**
     * 关闭指定比赛的赔率
     * 
     * @param contestId
     * @return
     */
    public boolean closeOdds(Long contestId);

    /**
     * 开启指定比赛的赔率
     */
    public boolean openOdds(Long contestId);

    /**
     * 根据比赛id查找赔率信息
     * 
     * @param contestId
     * @return
     */
    public OddsOp selectByContest(Long contestId);

    /**
     * 批量查找赔率
     * 
     * @param ids
     * @return
     */
    public Map<Long, OddsOp> findOddsOpMapByIds(List<Long> ids);

    /**
     * 获取赔率列表
     * 
     * @param startId
     * @param limit
     * @return
     */
    public List<OddsOp> findOdds(Long startId, int limit, Boolean isFive, Integer byOrder);

    /**
     * 批量保存赔率
     * 
     * @param list
     * @return
     */
    public boolean saveOdds(List<OddsOp> list);

}