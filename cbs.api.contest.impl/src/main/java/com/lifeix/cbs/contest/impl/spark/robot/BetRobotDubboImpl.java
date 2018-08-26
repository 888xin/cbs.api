package com.lifeix.cbs.contest.impl.spark.robot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;

import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.contest.bean.robot.BetRobotListResponse;
import com.lifeix.cbs.contest.bean.robot.BetRobotResponse;
import com.lifeix.cbs.contest.dao.robot.BetRobotDao;
import com.lifeix.cbs.contest.dto.robot.BetRobot;
import com.lifeix.cbs.contest.impl.ImplSupport;
import com.lifeix.cbs.contest.service.spark.robot.BetRobotDubbo;
import com.lifeix.cbs.contest.util.transform.ContestTransformUtil;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

public class BetRobotDubboImpl extends ImplSupport implements BetRobotDubbo {

    @Autowired
    private BetRobotDao betRobotDao;

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
    @Override
    public void replaceRobot(Long userId, String userInfo, int fbOdds, int bbOdds, String setting, String betTime,
	    boolean gameFlag, boolean pkFlag, boolean closeFlag) throws L99IllegalParamsException, L99IllegalDataException {

	ParamemeterAssert.assertDataNotNull(userId, userInfo, setting, betTime);

	BetRobot robot = betRobotDao.selectById(userId);
	boolean insertFlag = false;
	if (robot == null) {
	    robot = new BetRobot();
	    insertFlag = true;
	}
	robot.setUserId(userId);
	robot.setUserInfo(userInfo);
	robot.setFbOdds(fbOdds);
	robot.setBbOdds(bbOdds);
	robot.setSetting(setting);
	robot.setBetTime(betTime);
	robot.setGameFlag(gameFlag);
	robot.setPkFlag(pkFlag);
	robot.setCloseFlag(closeFlag);
	boolean flag = false;
	if (insertFlag) {
	    flag = betRobotDao.insert(robot);
	} else {
	    flag = betRobotDao.update(robot);
	}
	if (!flag) {
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}
    }

    /**
     * 机器人下单更新统计信息
     * 
     * @param userId
     * @param history
     * @throws JSONException
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    @Override
    public void betRobot(Long userId, String history, boolean gameFlag) throws L99IllegalParamsException,
	    L99IllegalDataException {

	ParamemeterAssert.assertDataNotNull(userId, history);

	BetRobot robot = betRobotDao.selectById(userId);
	if (robot == null) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}
	robot.setHistory(history);
	if (gameFlag) {
	    robot.setGameTime(new Date());
	}
	robot.setCallCount(robot.getCallCount() + 1);
	boolean flag = betRobotDao.update(robot);
	if (!flag) {
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}
    }

    /**
     * 单个机器人信息
     * 
     * @param userId
     * @return
     * @throws L99IllegalDataException
     * @throws L99IllegalParamsException
     */
    @Override
    public BetRobotResponse viewRobot(Long userId) throws L99IllegalDataException, L99IllegalParamsException {

	ParamemeterAssert.assertDataNotNull(userId);

	BetRobot robot = betRobotDao.selectById(userId);
	if (robot == null) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}

	return ContestTransformUtil.transformRobot(robot);
    }

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
    @Override
    public BetRobotListResponse findRobots(Boolean closeFlag, int nowPage, int limit) throws L99IllegalDataException,
	    L99IllegalParamsException {

	limit = Math.min(limit, 100);
	limit = Math.max(limit, 1);

	int number = betRobotDao.getRobotCount(closeFlag);
	int dataNum = (int) Math.ceil(number * 1.0D / limit);
	nowPage = dataNum == 0 ? 1 : nowPage <= dataNum ? nowPage <= 0 ? 1 : nowPage : dataNum;
	int start = (nowPage - 1) * limit;
	List<BetRobot> data = betRobotDao.findRobotList(closeFlag, start, limit);

	List<BetRobotResponse> robots = new ArrayList<BetRobotResponse>();
	for (BetRobot robot : data) {
	    robots.add(ContestTransformUtil.transformRobot(robot));
	}

	BetRobotListResponse reponse = new BetRobotListResponse();
	reponse.setRobots(robots);
	reponse.setNow_page(nowPage);
	reponse.setNumber(number);
	reponse.setPage_num(dataNum);
	reponse.setLimit(limit);
	return reponse;
    }

}
