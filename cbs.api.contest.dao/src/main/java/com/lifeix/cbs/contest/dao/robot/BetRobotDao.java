package com.lifeix.cbs.contest.dao.robot;

import java.util.List;

import com.lifeix.cbs.contest.dto.robot.BetRobot;

public interface BetRobotDao {

    public BetRobot selectById(Long id);

    public boolean insert(BetRobot entity);

    public boolean update(BetRobot entity);

    public boolean delete(BetRobot entity);

    /**
     * 机器人数量
     * 
     * @param closeFlag
     * @return
     */
    public int getRobotCount(Boolean closeFlag);

    /**
     * 机器人列表
     * 
     * @param closeFlag
     * @param start
     * @param limit
     * @return
     */
    public List<BetRobot> findRobotList(Boolean closeFlag, int start, int limit);
}
