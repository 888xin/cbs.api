package com.lifeix.cbs.contest.service.spark.robot;

import org.json.JSONException;

import com.lifeix.cbs.contest.bean.robot.BetRobotListResponse;
import com.lifeix.cbs.contest.bean.robot.BetRobotResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

public interface BetRobotDubbo {

    /**
     * 添加机器人配置
     * 
     * @param userId
     * @param fbOdds
     * @param bbOdds
     * @param setting
     * @param gameFlag
     * @param pkFlag
     * @param closeFlag
     * @throws JSONException
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    public void replaceRobot(Long userId, String userInfo, int fbOdds, int bbOdds, String setting, String betTime,
	    boolean gameFlag, boolean pkFlag, boolean closeFlag) throws L99IllegalParamsException, L99IllegalDataException;

    /**
     * 机器人下单更新统计信息
     * 
     * @param userId
     * @param history
     * @throws JSONException
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    public void betRobot(Long userId, String history, boolean gameFlag) throws L99IllegalParamsException,
	    L99IllegalDataException;

    /**
     * 单个机器人信息
     * 
     * @param userId
     * @return
     * @throws L99IllegalDataException
     * @throws L99IllegalParamsException
     */
    public BetRobotResponse viewRobot(Long userId) throws L99IllegalDataException, L99IllegalParamsException;

    /**
     * 机器人列表
     * 
     * @param closeFlag
     * @param nowPage
     * @param limit
     * @return
     * @throws L99IllegalDataException
     * @throws L99IllegalParamsException
     */
    public BetRobotListResponse findRobots(Boolean closeFlag, int nowPage, int limit) throws L99IllegalDataException,
	    L99IllegalParamsException;

}
