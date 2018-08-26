package com.lifeix.cbs.api.service.spark.mission;

import java.util.List;
import java.util.Map;

/**
 * Created by lhx on 2016/7/22 11:49
 *
 * @Description
 */
public interface NyxMissionDubbo {

    /**
     * 批量获取
     * @param users
     * @param date 日期格式：yyyy-MM-dd
     * @return Map<Long, Integer> Long 用户ID  Integer 用户值
     * ==============
     * 床上任务
     * NYX_LOGIN(0B0000_0001, "每日登录", 0, 3),NYX_BET(0B0000_0010, "每日下注比赛", 0, 3),
     * NYX_RECHARGE(0B0000_0100, "每日充值", 0, 3);
     * Integer 用户值与0B0000_0001这些值求与，等于0说明没有完成这个任务，等于1说明完成了这个任务
     */

    Map<Long, Integer> getBatch(List<Long> users, String date);
    /**
     * 批量获取当日的数据
     * @param users
     * @return
     */
    Map<Long, Integer> getBatch(List<Long> users);

    /**
     * 获取当日单个
     * @param userId
     * @return
     */
    int get(Long userId);

    /**
     * 获取单个
     * @param userId
     * @param date 日期格式：yyyy-MM-dd
     * @return
     */
    int get(Long userId, String date);
}
