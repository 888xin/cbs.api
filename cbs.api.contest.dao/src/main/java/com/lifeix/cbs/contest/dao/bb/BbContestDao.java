package com.lifeix.cbs.contest.dao.bb;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lifeix.cbs.contest.dto.bb.BbContest;

public interface BbContestDao {

    public BbContest selectById(long id);

    /**
     * 根据roomId获取赛事信息
     * 
     * @param roomId
     * @return
     */
    public BbContest selectByRoomId(long roomId);

    public boolean update(BbContest contest);

    public boolean delete(BbContest contest);

    /**
     * 批量查找列表
     * 
     * @param ids
     * @return
     */
    public Map<Long, BbContest> findBbContestMapByIds(List<Long> ids);

    /**
     * 查询正在下单中的比赛联赛ID
     * 
     * @return
     */
    public List<Long> findCupIdByRunOdds();

    /**
     * 体育头条赛事列表
     * 
     * @param time
     *            当天00:00 到 23:59的赛事
     * @return
     */
    public List<BbContest> findBbContestsForImportant(Date time);

    /**
     * 按时间范围查找比赛
     * 
     * @param time
     * @param fullFlag
     *            true 所有赛事
     * @return
     */
    public List<BbContest> findBbContestsByRangeTime(Date time, boolean fullFlag);

    /**
     * 更新比赛比分和状态
     * 
     * @param contest
     * @param tmgr
     * @return
     */
    public boolean updateChanges(BbContest entity);

    /**
     * 更新房间Id
     * 
     * @param contestId
     * @param roomId
     * @return
     */
    public boolean updateRoom(Long contestId, Long roomId);

    /**
     * 更新比赛结算状态
     * 
     * @param entity
     * @return
     */
    public boolean updateSettle(BbContest entity);

    /**
     * 查找未正常结束的比赛
     * 
     * @param startId
     * @param limit
     * @return
     */
    public List<BbContest> findBbContestsByAbnormal(Long startId, Integer limit);

    List<BbContest> findTimeoutContest(Long contestId, int limit);

    /**
     * spark批量保存赛事
     * 
     * @param list
     * @return
     */
    boolean saveContest(List<BbContest> list);

    /**
     * 获取正在进行的一级篮球比赛id列表
     * 
     * @param nowTime
     * @return
     */
    public List<Long> findBbContestIngNum(String nowTime);

    /**
     * 更新比赛（内部调用）
     */
    public boolean updateInner(BbContest entity);

    /**
     * 根据主客队获取比赛
     * 
     * @param homeTeam
     * @param awayTeam
     * @return
     */
    public BbContest findBbContestsByTeam(Long homeTeam, Long awayTeam, String time);

    /**
     * 根据cupIds和起始时间加载未来最近的赛事
     * 
     * @param time
     * @param cupIds
     * @return
     */
    public BbContest findNextBbContestsByCup(Date time, List<Long> cupIds);

    /**
     * 根据cupIds和结束时间加载历史最近的赛事
     * 
     * @param time
     * @param cupIds
     * @return
     */
    public BbContest findPrevBbContestsByCup(Date time, List<Long> cupIds);

    /**
     * 回滚更改
     */
    public boolean rollback(Long contestId);

    /**
     * 查看指定时间范围内赛事
     * 
     * @param start
     * @param end
     * @return
     */
    public Map<Long, String> findBbContestCupByRangeTime(Date start, Date end);

    /**
     * 获取最近五天包含胜平负和让球胜平负的一级比赛来做赔率自检
     * 
     * @return
     */
    public List<BbContest> findBbContestsByCheck();

    /**
     * 未来十五天的数据，用做排除相同球赛
     * @return
     */
    List<BbContest> findBbContestsBySame();

}