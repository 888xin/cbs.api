package com.lifeix.cbs.contest.service.spark.contest;

import com.lifeix.cbs.contest.bean.fb.ContestFullResponse;
import com.lifeix.cbs.contest.bean.fb.ContestListResponse;
import com.lifeix.cbs.contest.bean.fb.ContestResponse;
import com.lifeix.exception.service.L99IllegalParamsException;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BbContestDubbo {

    /**
     * 批量查赛事
     * 
     * @param ids
     * @return
     */
    public Map<Long, ContestResponse> findContestMapByIds(List<Long> ids);

    /**
     * 修改赛事比分和状态
     * 
     * @param homeScores
     * @param awayScores
     * @param status
     * @return
     */
    public boolean updateChanges(Long contestId, int homeScores, int awayScores, int status);

    /**
     * 获取超时未结算的比赛列表(超过开场时间3小时)
     * 
     * @param limit
     * @return
     */
    public List<ContestResponse> findTimeoutContest(Long contestId);

    /**
     * 批量保存赛事
     * 
     * @param list
     * @return
     */
    public boolean saveContest(List<ContestResponse> list);

    /**
     * 赛季管理列表
     * 
     * @param time
     * @param fullFlag
     * @param client
     * @return
     * @throws L99IllegalParamsException
     */
    public ContestListResponse findBbContestsByTime(String time, boolean fullFlag, String client)
	    throws L99IllegalParamsException;

    /**
     * 大赢家按每天来加载比赛列表(旧版接口)
     * 
     * @param startTime
     * @param endTime
     * @return
     * @throws L99IllegalParamsException
     */
    public ContestListResponse findBbContestsForCbs(Date startTime, Date endTime) throws L99IllegalParamsException;

    /**
     * 大赢家按时间范围加载比赛列表(新版接口)
     * 
     * @param startTime
     * @param endTime
     * @return
     * @throws L99IllegalParamsException
     */
    public ContestListResponse findBbContestsV2ForCbs(String startTime, String endTime) throws L99IllegalParamsException;

    /**
     * 体育篮球比赛列表 返回一级赛事
     * 
     * @param time
     * @return
     * @throws L99IllegalParamsException
     */
    public ContestListResponse findBbContestsForImportant(Date startTime, Date endTime) throws L99IllegalParamsException;

    /**
     * 篮球单篇
     * 
     * @param contestId
     * @return
     * @throws L99IllegalParamsException
     */
    public ContestFullResponse findBbContestInfo(Long contestId, Boolean newFlag) throws L99IllegalParamsException;

    /**
     * 正在进行数量
     * 
     * @param nowTime
     * @return
     */
    public List<Long> findBbContestIngNum();

    /**
     * 更新IM房间Id
     * 
     * @param contestId
     * @param roomId
     * @return
     */
    public boolean updateContestRoom(Long contestId, Long roomId);

    /**
     * 根据赛事分类加载赛事
     * 
     * @param startTime
     * @param endTime
     * @param channelId
     * @param ifFuture
     *            true 未来数据 false 历史数据
     * @param recursionFlag
     *            递归锁
     * @return
     * @throws L99IllegalParamsException
     */
    public ContestListResponse findBbContestsForImportantByChannelId(Date startTime, Date endTime, Long channelId,
	    boolean ifFuture, boolean recursionFlag) throws L99IllegalParamsException;

}
