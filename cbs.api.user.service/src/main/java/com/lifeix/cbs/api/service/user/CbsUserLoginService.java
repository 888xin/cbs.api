package com.lifeix.cbs.api.service.user;

import com.lifeix.cbs.api.bean.user.UserLoginListResponse;
import com.lifeix.cbs.api.bean.user.UserLoginResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * Created by lhx on 16-4-19 上午10:04
 *
 * @Description 新版用户登录奖励
 */
public interface CbsUserLoginService {

    /**
     * 本月登录的天数
     *
     * @param userId
     * @return
     * @throws L99IllegalParamsException
     */
    public UserLoginResponse loginDays(Long userId) throws L99IllegalParamsException;

    /**
     * 确认登陆，如果该天可以领取筹码，就领取筹码
     *
     * @param userId
     */
    public void receive(Long userId) throws L99IllegalParamsException, L99IllegalDataException;

    /**
     * 统计每月用户登录
     */
    public UserLoginListResponse statistic(String time) throws L99IllegalParamsException;

    /**
     * 设置用户累计登录天数（内部或者测试用）
     */
    public void setReward(Integer days, Long userId) throws L99IllegalParamsException;

    /**
     * 设置累计登录路径（内部用）
     */
    public void setPath(String days, String amounts) throws L99IllegalParamsException;

    /**
     * 获得登陆奖励路径
     * @return
     */
    public UserLoginResponse getPath();

    /**
     * 删除所有用户的登陆记录
     */
    public void expire();
}
