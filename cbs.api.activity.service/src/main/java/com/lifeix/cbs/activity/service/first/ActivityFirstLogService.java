package com.lifeix.cbs.activity.service.first;

import java.util.List;

import com.lifeix.cbs.activity.bean.first.ActivityFirstResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * Created by yis on 16-5-9 下午5:47
 *
 * @Description
 */
public interface ActivityFirstLogService {

    /**
     * 查询用户是否有抽奖机会
     * 
     * @param userId
     * @param flag
     *            false：不查询用户充值 true：查询用户充值
     * @return
     */
    public ActivityFirstResponse check(Long userId, Boolean flag);

    /**
     * 查询用户是否有抽奖机会
     * 
     * @param userId
     * @return
     */
    public ActivityFirstResponse check(Long userId);

    /**
     * 查询首充活动中奖列表
     */
    public List<String> getRewardLogList();

    /**
     * 添加首充日志
     * 
     * @param userId
     * @param amount
     * @param rule
     * @param reward
     * @param createTime
     * @return
     * @throws L99IllegalDataException
     */
    public ActivityFirstResponse addFirstLog(Long userId) throws L99IllegalParamsException;

    /**
     * 首充活动判断
     * 
     * @param userId
     * @param price
     *            充值金额
     * @throws L99IllegalDataException
     */
    public void addFirstRecord(Long userId, Double price) throws L99IllegalParamsException;

}
