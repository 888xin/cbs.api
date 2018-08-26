package com.lifeix.cbs.contest.service.fb;

import com.lifeix.cbs.contest.bean.fb.ContestListResponse;
import com.lifeix.cbs.contest.bean.fb.ContestResponse;
import com.lifeix.cbs.contest.bean.fb.FbFixedResponse;
import com.lifeix.cbs.contest.bean.fb.FbStatistcsEventResponse;
import com.lifeix.cbs.contest.bean.fb.TeamListResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.user.beans.CustomResponse;

/**
 * 足球比赛接口
 * 
 * @author lifeix-sz
 * 
 */
public interface FbContestService {

    /**
     * 获取每天的赛事列表
     * 
     * @param time
     * @param fullFlag
     * @param client
     * @return
     * @throws L99IllegalParamsException
     */
    public ContestListResponse findFbContestsByTime(String time, boolean fullFlag, String client)
	    throws L99IllegalParamsException;

    /**
     * 根据赛事ID列表返回赛事分数和状态
     * 
     * @param contestIds
     * @return
     * @throws L99IllegalParamsException
     */
    public CustomResponse findFbScoreByContestIds(String contestIds) throws L99IllegalParamsException;

    /**
     * 获取赛事的最终比分(包含加时和点球)
     * 
     * @param contestIds
     * @return
     * @throws L99IllegalParamsException
     */
    public CustomResponse findFbFinalScores(String contestIds) throws L99IllegalParamsException;

    /**
     * 根据ID获得比赛
     * 
     * @param contestId
     * @param client
     * @return
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    public ContestResponse findFbContestsById(Long contestId, String client) throws L99IllegalParamsException,
	    L99IllegalDataException;

    /**
     * 删除比赛
     * 
     * @param contestId
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    public void dropContest(Long contestId) throws L99IllegalParamsException, L99IllegalDataException;

    /**
     * 未正常结束的足球比赛列表
     * 
     * @param startId
     * @param limit
     * @return
     */
    public ContestListResponse findAbnormalContests(Long startId, int limit);

    /**
     * 更改足球比赛信息
     * 
     * @param contestId
     * @param homeScore
     * @param awayScore
     * @param status
     * @param isLock
     * @return
     * @throws L99IllegalParamsException
     */
    public void updateContests(Long contestId, Integer homeScore, Integer awayScore, Integer status, Boolean isLock,
	    Integer level, String cupName, Integer homeCount, Integer awayCount) throws L99IllegalParamsException,
	    L99IllegalDataException;

    /**
     * 赛况与技术统计
     * 
     * @param contestId
     * @return
     * @throws L99IllegalParamsException
     */
    public FbStatistcsEventResponse findStatisticsAndEventByContestId(Long contestId) throws L99IllegalParamsException;

    /**
     * 查询球队
     * 
     * @param searchKey
     * @return
     * @throws L99IllegalParamsException
     */
    public TeamListResponse findFbTeams(String searchKey) throws L99IllegalParamsException;

    /**
     * 修改球队
     * 
     * @param id
     * @param name
     * @param logo
     * @return
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    public void updateTeam(Long id, String name, String logo) throws L99IllegalParamsException, L99IllegalDataException;

    /**
     * 根据roomId获取赛事信息
     * 
     * @param contestId
     * @return
     * @throws L99IllegalParamsException
     */
    public ContestResponse findFbContestsByRoomId(Long roomId) throws L99IllegalParamsException;

    /**
     * 足球不变信息
     * 
     * @throws L99IllegalParamsException
     */
    public FbFixedResponse findFbFixedDataByContestId(Long contestId) throws L99IllegalParamsException;

    /**
     * 根据主客队获取赛事
     * 
     * @param homeTeam
     * @param awayTeam
     * @return
     * @throws L99IllegalParamsException
     */
    public ContestResponse findContestsByTeam(Long homeTeam, Long awayTeam, String time) throws L99IllegalParamsException;

}
