package com.lifeix.cbs.contest.service.bb;

import com.lifeix.cbs.contest.bean.bb.BbChangeDataResponse;
import com.lifeix.cbs.contest.bean.bb.BbFixedResponse;
import com.lifeix.cbs.contest.bean.bb.ext.BbPlayerListResponse;
import com.lifeix.cbs.contest.bean.fb.ContestListResponse;
import com.lifeix.cbs.contest.bean.fb.ContestResponse;
import com.lifeix.cbs.contest.bean.fb.TeamListResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.user.beans.CustomResponse;

/**
 * 篮球比赛接口
 * 
 * @author lifeix-sz
 * 
 */
public interface BbContestService {
    /**
     * 篮球比赛列表
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
     * 根据ID获得比赛
     * 
     * @param contestId
     * @param client
     * @return
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    public ContestResponse findBbContestsById(Long contestId, String client) throws L99IllegalParamsException,
	    L99IllegalDataException;

    /**
     * 根据房间id获取赛事
     * 
     * @param roomId
     * @return
     * @throws L99IllegalParamsException
     */
    public ContestResponse findBbContestsByRoomId(Long roomId) throws L99IllegalParamsException;

    /**
     * 根据IDs获得赛事比分列表
     * 
     * @param contestIds
     * @return
     * @throws L99IllegalParamsException
     */
    public CustomResponse findBbScoreByContestIds(String contestIds) throws L99IllegalParamsException;

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
     * 更改篮球比赛信息
     * 
     * @param contestId
     * @param homeScore
     * @param awayScore
     * @param status
     * @param isLock
     * @param isLock2
     * @return
     * @throws L99IllegalParamsException
     */
    public void updateContests(Long contestId, Integer homeScore, Integer awayScore, Integer status, Boolean isLock,
	    Integer level, String cupName, Integer homeCount, Integer awayCount) throws L99IllegalParamsException,
	    L99IllegalDataException;

    /**
     * 查询球队
     * 
     * @param searchKey
     * @return
     * @throws L99IllegalParamsException
     */
    public TeamListResponse findBbTeams(String searchKey) throws L99IllegalParamsException;

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
     * 获取篮球常变的信息
     * 
     * @param lineupFlag
     *            是否返回阵容
     * @param contestId
     * @return
     * @throws L99IllegalParamsException
     */
    public BbChangeDataResponse findBbChangeDataResponseByContestId(Long contestId, Boolean lineupFlag)
	    throws L99IllegalParamsException;

    /**
     * 分页加载球员列表
     * 
     * @param startId
     * @param limit
     * @return
     */
    public BbPlayerListResponse findBbPlayers(Long startId, int limit);

    /**
     * 保存球员信息
     * 
     * @param id
     * @param name
     * @param firstName
     * @param lastName
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    public void updatePlayer(Long id, String name, String firstName, String lastName) throws L99IllegalParamsException,
	    L99IllegalDataException;

    /**
     * 搜索球员
     * 
     * @param name
     * @param teamId
     * @return
     * @throws L99IllegalParamsException
     */
    public BbPlayerListResponse searchPlayer(String name, Long teamId) throws L99IllegalParamsException;

    /**
     * 获取篮球不变信息，阵容、交手记录
     * 
     * @param contestId
     * @return
     * @throws L99IllegalParamsException
     */
    public BbFixedResponse findBbFixedDataByContestId(Long contestId) throws L99IllegalParamsException;

    /**
     * 根据主客队获取赛事
     * 
     * @param homeTeam
     * @param awayTeam
     * @return
     * @throws L99IllegalParamsException
     */
    public ContestResponse findBbContestsByTeam(Long homeTeam, Long awayTeam, String time) throws L99IllegalParamsException;

}
