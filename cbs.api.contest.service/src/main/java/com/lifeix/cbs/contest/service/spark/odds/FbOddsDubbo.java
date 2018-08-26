package com.lifeix.cbs.contest.service.spark.odds;

import java.util.List;
import java.util.Map;

import com.lifeix.cbs.contest.bean.odds.OddsJcResponse;
import com.lifeix.cbs.contest.bean.odds.OddsOpResponse;
import com.lifeix.cbs.contest.bean.odds.OddsSizeResponse;

public interface FbOddsDubbo {

    /**
     * 批量保存欧赔赔率
     * 
     * @param list
     * @return
     */
    public boolean saveOpOdds(List<OddsOpResponse> list);

    /**
     * 批量保存竞彩赔率
     * 
     * @param list
     * @return
     */
    public boolean saveJcOdds(List<OddsJcResponse> list);

    /**
     * 批量保存大小球赔率
     * 
     * @param list
     * @return
     */
    public boolean saveSizeOdds(List<OddsSizeResponse> list);

    /**
     * 关闭欧赔赔率
     * 
     * @param contestId
     * @return
     */
    public boolean closeOpOdds(Long contestId);

    /**
     * 关闭竞彩赔率
     * 
     * @param contestId
     * @return
     */
    public boolean closeJcOdds(Long contestId);

    /**
     * 关闭大小球赔率
     * 
     * @param contestId
     * @return
     */
    public boolean closeSizeOdds(Long contestId);

    /**
     * 按ids查竞彩赔率
     * 
     * @param contestIds
     * @return
     */
    public Map<Long, OddsJcResponse> findJcOddsByContestIds(List<Long> contestIds);

    /**
     * 按ids查大小球赔率
     * 
     * @param contestIds
     * @return
     */
    public Map<Long, OddsSizeResponse> findSizeOddsByContestIds(List<Long> contestIds);

    /**
     * 检查包含胜平负和让球盘的赔率
     * 
     * @param contestId
     *            传空则查最近五天
     */
    public int checkWarnOdds(Long contestId);
}
