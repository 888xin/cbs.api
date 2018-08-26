package com.lifeix.cbs.contest.dao.fb;

import com.lifeix.cbs.contest.dto.fb.FbContest;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface FbContestDao {

    public FbContest selectById(long id);

    /**
     * 根据roomId获取赛事
     * 
     * @param roomId
     * @return
     */
    public FbContest selectByRoomId(long roomId);

    public boolean update(FbContest contest);

    public boolean delete(FbContest contest);

    /**
     * 批量查找列表
     * 
     * @param ids
     * @return
     */
    public Map<Long, FbContest> findFbContestMapByIds(List<Long> ids);

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
    public List<FbContest> findFbContestsForImportant(Date time);

    /**
     * 按时间范围查找比赛
     * 
     * @param time
     * @param fullFlag
     *            true 所有赛事 false 一级赛事全部入选 ，一级赛事不够20个用二级和三级赛事补充
     *            ，一二三级赛事不够20个，4级赛事补充不能超过10个
     * 
     * @return
     */
    public List<FbContest> findFbContestsByRangeTime(Date time, boolean fullFlag);

    /**
     * 更新比赛比分和状态
     * 
     * @param contest
     * @return
     */
    public boolean updateChanges(FbContest entity);

    /**
     * 更新比赛结算状态
     * 
     * @param entity
     * @return
     */
    public boolean updateSettle(FbContest entity);

    /**
     * 更新房间Id
     * 
     * @param contestId
     * @param roomId
     * @return
     */
    public boolean updateRoom(Long contestId, Long roomId);

    /**
     * 查找未正常结束的比赛
     * 
     * @param startId
     * @param limit
     * @return
     */
    public List<FbContest> findFbContestsByAbnormal(Long startId, Integer limit);

    /**
     * 获取超时未结算的比赛列表(超过开场时间3小时)
     * 
     * @param limit
     * @return
     */
    public List<FbContest> findTimeoutContest(Long contestId, int limit);

    /**
     * 获取取消的比赛列表(开始时间剩余超过20天)
     * 
     * @param limit
     * @return
     */
    public List<FbContest> findCancleContest(int limit);

    /**
     * spark批量保存赛事
     * 
     * @param list
     * @return
     */
    public boolean saveContest(List<FbContest> list);

    /**
     * 获取正在进行的一级足球比赛id列表
     * 
     * @param nowTime
     * @return
     */
    public List<Long> findFbContestIngNum(String nowTime);

    /**
     * 更新比赛（内部调用）
     */
    public boolean updateInner(FbContest entity);

    /**
     * 根据cupIds和起始时间加载未来最近的赛事
     * 
     * @param time
     * @param cupIds
     * @return
     */
    public FbContest findNextFbContestsByCup(Date time, List<Long> cupIds);

    /**
     * 根据cupIds和结束时间加载历史最近的赛事
     * 
     * @param time
     * @param cupIds
     * @return
     */
    public FbContest findPrevFbContestsByCup(Date time, List<Long> cupIds);

    /**
     * 回滚更改
     */
    public boolean rollback(Long contestId);

    /**
     * 根据主客队获取比赛
     * 
     * @param homeTeam
     * @param awayTeam
     * @param time
     * @return
     */
    public FbContest selectByTeam(Long homeTeam, Long awayTeam, String time);

    /**
     * 更新比赛最终结果
     * 
     * @param list
     * @return
     */
    public boolean updateFinalResults(List<FbContest> list);

    /**
     * 查看指定时间范围内赛事
     * 
     * @param start
     * @param end
     * @return
     */
    public Map<Long, String> findFbContestCupByRangeTime(Date start, Date end);

    /**
     * 获取最近五天包含胜平负和让球胜平负的一级比赛来做赔率自检
     * 
     * @return
     */
    public List<FbContest> findFbContestsByCheck();

    /**
     * 未来十五天的数据，用做排除相同球赛
     * @return
     */
    List<FbContest> findFbContestsBySame();
}