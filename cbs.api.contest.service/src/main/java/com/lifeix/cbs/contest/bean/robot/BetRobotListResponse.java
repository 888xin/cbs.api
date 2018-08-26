package com.lifeix.cbs.contest.bean.robot;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

/**
 * 机器人列表
 * 
 * @author Peter
 * 
 */
public class BetRobotListResponse extends ListResponse implements Response {

    /**
     * 
     */
    private static final long serialVersionUID = -4471045967935830802L;

    private List<BetRobotResponse> robots;

    public List<BetRobotResponse> getRobots() {
	return robots;
    }

    public void setRobots(List<BetRobotResponse> robots) {
	this.robots = robots;
    }

    @Override
    public String getObjectName() {
	return "robots";
    }
}
