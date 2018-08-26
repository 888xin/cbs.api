package com.lifeix.cbs.api.service.misson;

import com.lifeix.cbs.api.bean.mission.MissionUserListResponse;
import com.lifeix.cbs.api.bean.mission.MissionUserResponse;
import com.lifeix.cbs.api.bean.mission.PointResponse;
import com.lifeix.cbs.api.common.mission.Mission;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

import java.util.Set;

/**
 * Created by lhx on 16-6-20 上午9:53
 *
 * @Description 用户任务
 */
public interface MissionUserService {

    /**
     * 用户积分积累
     */
    boolean validate(Long userId, Mission missionValue) ;

    MissionUserResponse validate(Long userId, Integer value, Integer type) throws L99IllegalParamsException;

    void edit(Long userId, Integer amount, Integer value) throws L99IllegalParamsException;

    /**
     * 消耗积分
     */
    void consume(Long userId, Long goldId) throws L99IllegalParamsException, L99IllegalDataException;

    /**
     * 用户完成任务情况
     */
    MissionUserResponse getUserInfo(Long userId) throws L99IllegalParamsException;

    /**
     * 积分兑换龙筹列表
     */
    MissionUserResponse getPointList(Long userId) throws L99IllegalParamsException;

    /**
     * 积分兑换龙筹列表（内部）
     */
    MissionUserResponse getPointListInner() ;

    /**
     * 操作每日任务
     */
    boolean operDayMission(Integer value);

    /**
     * 操作积分兑换龙筹
     */
    PointResponse operReward(Long id, Integer point, Integer gold) throws L99IllegalParamsException;

    /**
     * 硬删除积分兑换龙筹
     */
    void deleteReward(Long id) throws L99IllegalParamsException;

    /**
     * 查询列表（内部）
     */
    MissionUserListResponse getUserListInfo(Integer page, Integer limit);

    /**
     * 查询用户每日任务完成情况（内部）
     */
    MissionUserResponse getUserDayInfo(String day, Long userId);
    /**
     * 查询用户任务完成情况（内部）
     */
    MissionUserResponse getUserInfoInner(Long userId);

    /**
     * 查询用户任务（简单查询）
     */
    MissionUserResponse getUserInfoSimple(Long userId);

    /**
     * 查询取消的每日任务（内部）
     */
    String getCancelDayMission();

}
