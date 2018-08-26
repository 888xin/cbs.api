package com.lifeix.cbs.contest.service.yy;

import org.json.JSONException;

import com.lifeix.cbs.contest.bean.yy.YyBetListResponse;
import com.lifeix.cbs.contest.bean.yy.YyContestListResponse;
import com.lifeix.cbs.contest.bean.yy.YyContestResponse;
import com.lifeix.cbs.contest.bean.yy.YyCupListResponse;
import com.lifeix.cbs.contest.bean.yy.YyOddsResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;

/**
 * 押押赛事接口
 * 
 * @author lifeix
 * 
 */
public interface YyContestService {

    /**
     * 押押分类列表
     * 
     * @return
     */
    public YyCupListResponse findYyCups();

    /**
     * 添加押押分类
     */
    public void insertYyCups(String cupName) throws L99IllegalParamsException, L99IllegalDataException;

    /**
     * 单个押押赛事
     * 
     * @param id
     * @return
     * @throws L99IllegalParamsException
     */
    public YyContestResponse viewYyContest(Long id) throws L99IllegalParamsException, L99IllegalDataException;

    /**
     * 押押赔率与下单信息
     * 
     * @param id
     * @param userId
     * @return
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     * @throws JSONException
     */
    public YyOddsResponse oddsYyContest(Long id, Long userId, String from) throws L99IllegalParamsException,
	    L99IllegalDataException, JSONException;

    /**
     * 押押可下单赛事列表
     * 
     * @param longbi
     *            是否返回龙币
     * @param cupId
     * @param userId
     * @param startId
     * @param limit
     * @return
     * @throws L99IllegalParamsException
     */
    public YyContestListResponse findBetYyContests(Boolean longbi, Long cupId, Long userId, Long startId, int limit)
	    throws L99IllegalParamsException;

    /**
     * 我的押押下单列表
     * 
     * @param userId
     * @param startId
     * @param limit
     * @return
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    public YyBetListResponse findUserYyContests(Long userId, Long startId, int limit) throws L99IllegalParamsException,
	    L99IllegalDataException;

    /**
     * 押押赛事下单记录
     * 
     * @param contestId
     * @param startId
     * @param limit
     * @return
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    public YyBetListResponse findContestBets(Long contestId, Long startId, int limit) throws L99IllegalParamsException,
	    L99IllegalDataException, L99NetworkException, JSONException;

    /**
     * 押押后台赛事列表
     * 
     * @param hideFlag
     *            是否隐藏
     * @param type
     *            null 全部 true 未结束 false 一结束
     * @param startId
     * @param limit
     * @return
     * @throws L99IllegalParamsException
     */
    public YyContestListResponse findYyContests(Boolean hideFlag, Boolean type, Long cupId, Long startId, int limit)
	    throws L99IllegalParamsException;

    /**
     * 添加押押赛事
     * 
     * @param title
     * @param images
     * @param text
     * @param options
     * @param cupId
     * @param startTime
     * @param endTime
     * @param activityFlag
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    public void insertYyContests(String title, String images, String text, String options, Long cupId, String startTime,
	    String endTime, boolean activityFlag, int showType, String listImage) throws L99IllegalParamsException, L99IllegalDataException;

    /**
     * 修改押押赛事
     * 
     * @param id
     * @param title
     * @param images
     * @param text
     * @param options
     * @param cupId
     * @param startTime
     * @param endTime
     * @param activityFlag
     * @param hideFlag
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    public void updateYyContests(Long id, String title, String images, String text, String options, Long cupId,
	    String startTime, String endTime, boolean activityFlag, Boolean hideFlag, int showType, String listImage)
	    throws L99IllegalParamsException, L99IllegalDataException;

    /**
     * 后台填入押押赛事结果
     * 
     * @param id
     *            赛事Id
     * @param winner
     *            赛事选项结果
     * @param status
     *            赛事状态 正常 or 走盘
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    public void endYyContest(Long id, Integer winner, int status) throws L99IllegalParamsException, L99IllegalDataException;

    /**
     * 可以结算的押押数量
     */
    public int YyShouldSettle();

    /**
     * 添加选项照片
     */
    void addOptionImage(String name, String path);

    /**
     * 删除选项照片
     */
    void deleteOptionImage(String name);

    /**
     * 获取所有选项照片
     */
    YyContestResponse getOptionImage();

    /**
     * 添加或删除精选押押
     */
    void editGoodYy(Long id);

}
