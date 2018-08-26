package com.lifeix.cbs.api.impl.user;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.achieve.service.AchieveService;
import com.lifeix.cbs.activity.bean.first.ActivityFirstResponse;
import com.lifeix.cbs.activity.service.first.ActivityFirstLogService;
import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.cbs.api.bean.user.UserLoginListResponse;
import com.lifeix.cbs.api.bean.user.UserLoginResponse;
import com.lifeix.cbs.api.common.exception.MsgCode;
import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.mission.Mission;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestType;
import com.lifeix.cbs.api.common.util.CouponConstants;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.common.util.RedisConstants;
import com.lifeix.cbs.api.common.util.RedisConstants.UserRedis;
import com.lifeix.cbs.api.dao.rank.UserContestStatisticsDao;
import com.lifeix.cbs.api.dao.user.LoginPathDao;
import com.lifeix.cbs.api.dto.rank.UserContestStatistics;
import com.lifeix.cbs.api.dto.user.LoginPath;
import com.lifeix.cbs.api.service.coupon.CouponUserService;
import com.lifeix.cbs.api.service.misson.MissionUserService;
import com.lifeix.cbs.api.service.user.CbsUserLoginService;
import com.lifeix.cbs.api.service.user.CbsUserService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.framework.redis.impl.RedisDataIdentify;
import com.lifeix.framework.redis.impl.RedisHashHandler;
import com.lifeix.framework.redis.impl.RedisStringHandler;

/**
 * Created by lhx on 16-4-19 上午10:05
 *
 * @Description 新版用户登录奖励
 */
@Service("cbsUserLoginService")
public class CbsUserLoginServiceImpl extends ImplSupport implements CbsUserLoginService {

    // 点击过领取
    private final String CLICK = "1";
    // 未点击过领取
    private final String UNCLICK = "0";

    @Autowired
    private UserContestStatisticsDao userContestStatisticsDao;

    @Autowired
    private LoginPathDao loginPathDao;

    @Autowired
    private CouponUserService couponUserService;

    @Autowired
    private CbsUserService cbsUserService;

    @Autowired
    private ActivityFirstLogService activityFirstLogService;

    @Autowired
    private RedisHashHandler redisHashHandler;

    @Autowired
    private RedisStringHandler redisStringHandler;

    @Autowired
    private MissionUserService missionUserService;

    @Autowired
    protected AchieveService achieveService;

    /**
     * 本月登录的天数
     */
    @Override
    public UserLoginResponse loginDays(Long userId) throws L99IllegalParamsException {

        ParamemeterAssert.assertDataNotNull(userId);

        UserLoginResponse userLoginResponse = new UserLoginResponse();
        userLoginResponse.setUser_id(userId);
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR)).substring(2, 4);
        int month = calendar.get(Calendar.MONTH) + 1;
        Integer dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // 用户本日是否登陆过的key
        String userValue = null;
        String userKey = String.format(UserRedis.USER_LOGIN_DAY, userId);
        RedisDataIdentify dayIdentify = new RedisDataIdentify(RedisConstants.MODEL_USER, userKey);
        byte[] userByte = redisStringHandler.get(dayIdentify);
        if (userByte != null && userByte.length > 0) {
            userValue = new String(userByte);
        }

        // 用户本月累计登陆的天数key
        String day = null;
        String monthKey = String.format(UserRedis.USER_LOGIN_MONTH, year + "" + month);
        RedisDataIdentify monethIdentify = new RedisDataIdentify(RedisConstants.MODEL_USER, monthKey);
        byte[] dayByte = redisHashHandler.hget(monethIdentify, userId.toString().getBytes());
        if (dayByte != null && dayByte.length > 0) {
            day = new String(dayByte);
        }

        //防止31天签到造成客户端崩溃
        if ("30".equals(day)){
            userLoginResponse.setDay(Long.valueOf(day));
            userLoginResponse.setReceive(true);
            LoginPath loginPath = loginPathDao.find();
            userLoginResponse.setGold_days(loginPath.getDays());
            userLoginResponse.setAmounts(loginPath.getAmounts());
            return userLoginResponse;
        }

        // 本日登陆过且点击确认
        if (CLICK.equals(userValue)) {
            if (day == null) {
                // 有本日确认登陆记录但没有历史累计天数记录，这种情况一般不会出现，以防万一，设为1
                redisHashHandler.hIncrBy(monethIdentify, userId.toString().getBytes(), 1L);
                day = "1";
            }
            userLoginResponse.setDay(Long.valueOf(day));
            userLoginResponse.setReceive(true);
        } else {
            // 本日没登陆过
            if (userValue == null) {
                if (day == null) {
                    userLoginResponse.setDay(1);
                } else {
                    long alreadyDay = Long.valueOf(day) + 1;
                    if (alreadyDay > dayOfMonth) {
                        // 防止获得的记录大于这个月的天数的情况，这种情况绝少
                        redisHashHandler
                                .hset(monethIdentify, userId.toString().getBytes(), dayOfMonth.toString().getBytes());
                    }
                    userLoginResponse.setDay(alreadyDay);
                }
                // 设置用户本日登陆记录
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                // 第二天的凌晨00:00:00过期删除
                long expireTime = (calendar.getTimeInMillis() - System.currentTimeMillis()) / 1000;
                dayIdentify.setExpireTime(expireTime);
                redisStringHandler.set(dayIdentify, UNCLICK.getBytes());
            } else {
                // 没有点击确认领取的
                if (day == null) {
                    // 本月没有登陆记录
                    userLoginResponse.setDay(1);
                } else {
                    long alreadyDay = Long.valueOf(day) + 1;
                    if (alreadyDay > dayOfMonth) {
                        // 防止获得的记录大于这个月的天数的情况，这种情况绝少
                        redisHashHandler
                                .hset(monethIdentify, userId.toString().getBytes(), dayOfMonth.toString().getBytes());
                    }
                    userLoginResponse.setDay(alreadyDay);
                }
            }
        }
        LoginPath loginPath = loginPathDao.find();
        userLoginResponse.setGold_days(loginPath.getDays());
        userLoginResponse.setAmounts(loginPath.getAmounts());

        UserContestStatistics userStatistics = userContestStatisticsDao.getUserContestStatistics(userId);

        // 查询用户结算下注统计数
        int yyBetCount = 0;
        int bbBetCount = 0;
        int fbBetCount = 0;
        try {
            if (userStatistics != null && StringUtils.isNotEmpty(userStatistics.getContestCount())) {
                JSONObject contestCount = new JSONObject(userStatistics.getContestCount());
                yyBetCount = contestCount.optInt(String.valueOf(ContestType.YAYA), 0);
                bbBetCount = contestCount.optInt(String.valueOf(ContestType.BASKETBALL), 0);
                fbBetCount = contestCount.optInt(String.valueOf(ContestType.FOOTBALL), 0);
            }
        } catch (Exception e) {
        }
        // 默认显示顺序足球、篮球、押押
        int displayType = ContestType.FOOTBALL;
        if (fbBetCount < bbBetCount) {
            if (bbBetCount < yyBetCount) {
                displayType = ContestType.YAYA;
            } else {
                displayType = ContestType.BASKETBALL;
            }
        } else if (fbBetCount < yyBetCount) {
            displayType = ContestType.YAYA;
        }

        userLoginResponse.setYyBetCount(yyBetCount);
        userLoginResponse.setBbBetCount(bbBetCount);
        userLoginResponse.setFbBetCount(fbBetCount);
        userLoginResponse.setDisplayType(displayType);

        // 查询用户是否参加首充活动
        ActivityFirstResponse actRes = activityFirstLogService.check(userId);
        userLoginResponse.setActFlag(actRes.getActFlag());
        userLoginResponse.setActStatus(actRes.getStatus());
        userLoginResponse.setActTimes(actRes.getTimes());

        // 用户获取的最后一个成就(校验是否通知过通知客户端)
        userLoginResponse.setAchieve(achieveService.findUserLastAchieve(userId));

        return userLoginResponse;

    }

    @Override
    public void receive(Long userId) throws L99IllegalParamsException, L99IllegalDataException {
        ParamemeterAssert.assertDataNotNull(userId);

        // 用户本日是否登陆过的key
        String userValue = null;
        String userKey = String.format(UserRedis.USER_LOGIN_DAY, userId);
        RedisDataIdentify dayIdentify = new RedisDataIdentify(RedisConstants.MODEL_USER, userKey);
        byte[] userByte = redisStringHandler.get(dayIdentify);
        if (userByte != null && userByte.length > 0) {
            userValue = new String(userByte);
        }
        // 未点击确认
        if (UNCLICK.equals(userValue)) {
            // 设置点击确认生效。用increment而不是set是因为前者不会让生存时间失效，后者会。“srt.opsForValue().set(userKey,
            // CLICK)”弃用
            redisStringHandler.incr(dayIdentify);
            // 获得用户累积登陆天数
            Calendar calendar = Calendar.getInstance();
            String year = String.valueOf(calendar.get(Calendar.YEAR)).substring(2, 4);
            int month = calendar.get(Calendar.MONTH) + 1;

            // 用户本月累计登陆的天数key
            String monthKey = String.format(UserRedis.USER_LOGIN_MONTH, year + "" + month);
            RedisDataIdentify monethIdentify = new RedisDataIdentify(RedisConstants.MODEL_USER, monthKey);

            // 设置用户累计天数累加1
            Long allDay = redisHashHandler.hIncrBy(monethIdentify, userId.toString().getBytes(), 1L);
            // 获得登陆路径
            LoginPath loginPath = loginPathDao.find();
            String[] goldDays = loginPath.getDays().split(",");
            for (int i = 0; i < goldDays.length; i++) {
                if (goldDays[i].equals(allDay + "")) {
                    String[] amounts = loginPath.getAmounts().split(",");
                    int gold = Integer.valueOf(amounts[i]);
                    String desc = String.format("获得累积登录%d天奖励", allDay);
                    // 累积登录发送筹码奖励
                    couponUserService.settleCouponByPrice(userId, gold, CouponConstants.CouponSystem.TIME_48, desc);
                    break;
                }
            }

            // add by lhx on 16-06-23 每日登录任务 start
            missionUserService.validate(userId, Mission.EVERY_DAY_LOGIN);
            // add by lhx on 16-06-23 每日登录任务 end
        } else {
            // 已经领取过，不能再领取，抛出异常
            throw new L99IllegalParamsException(MsgCode.CouponMsg.CODE_COUPON_LOGIN, MsgCode.CouponMsg.KEY_COUPON_LOGIN_HAS);
        }

    }

    @Override
    public UserLoginListResponse statistic(String time) throws L99IllegalParamsException {

        ParamemeterAssert.assertDataNotNull(time);

        // 用户本月累计登陆的天数key
        String monthKey = String.format(UserRedis.USER_LOGIN_MONTH, time);
        RedisDataIdentify monethIdentify = new RedisDataIdentify(RedisConstants.MODEL_USER, monthKey);

        UserLoginListResponse userLoginListResponse = new UserLoginListResponse();
        Map<byte[], byte[]> map = redisHashHandler.hGetAll(monethIdentify);
        // Map<Object, Object> map = srt.opsForHash().entries(monthKey);
        List<UserLoginResponse> logins = new ArrayList<>();
        List<Long> userList = new ArrayList<>();
        Map<Long, Long> userResultMap = new HashMap<>();
        for (byte[] o : map.keySet()) {
            long userId = Long.valueOf(new String(o));
            userList.add(userId);
            userResultMap.put(userId, Long.valueOf(new String(map.get(o))));
        }
        if (userList.size() > 0) {
            Map<Long, CbsUserResponse> userMap = cbsUserService.findCsAccountMapByIds(userList);
            for (Long aLong : userMap.keySet()) {
                UserLoginResponse userLoginResponse = new UserLoginResponse();
                // userLoginResponse.setUser_id(aLong);
                userLoginResponse.setUser(userMap.get(aLong));
                userLoginResponse.setDay(userResultMap.get(aLong));
                logins.add(userLoginResponse);
            }
        }

        userLoginListResponse.setUser_login(logins);
        return userLoginListResponse;
    }

    @Override
    public void setReward(Integer days, Long userId) throws L99IllegalParamsException {
        ParamemeterAssert.assertDataNotNull(days, userId);
        // 用户本日是否登陆过的key

        // 用户本日是否登陆过的key
        String userKey = String.format(UserRedis.USER_LOGIN_DAY, userId);
        RedisDataIdentify dayIdentify = new RedisDataIdentify(RedisConstants.MODEL_USER, userKey);
        // 用户本日登陆记录删除
        redisStringHandler.del(dayIdentify);

        // 设置用户累积登陆天数
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR)).substring(2, 4);
        int month = calendar.get(Calendar.MONTH) + 1;

        // 用户本月累计登陆的天数key
        String monthKey = String.format(UserRedis.USER_LOGIN_MONTH, year + "" + month);
        RedisDataIdentify monethIdentify = new RedisDataIdentify(RedisConstants.MODEL_USER, monthKey);

        // 用户本月累计登陆的天数key
        redisHashHandler.hset(monethIdentify, userId.toString().getBytes(), days.toString().getBytes());

    }

    @Override
    public void setPath(String days, String amounts) throws L99IllegalParamsException {
        ParamemeterAssert.assertDataNotNull(days, amounts);
        LoginPath loginPath = new LoginPath();
        loginPath.setAmounts(amounts);
        loginPath.setDays(days);
        loginPath.setUpdateTime(new Date());
        boolean flag = loginPathDao.update(loginPath);
        if (!flag) {
            throw new L99IllegalParamsException(MsgCode.BasicMsg.CODE_OPERATE_FAIL, MsgCode.BasicMsg.KEY_OPERATE_FAIL);
        }
    }

    @Override
    public UserLoginResponse getPath() {
        LoginPath loginPath = loginPathDao.find();
        UserLoginResponse userLoginResponse = new UserLoginResponse();
        userLoginResponse.setGold_days(loginPath.getDays());
        userLoginResponse.setAmounts(loginPath.getAmounts());
        return userLoginResponse;
    }

    @Override
    public void expire() {
        String userKey = String.format(UserRedis.USER_LOGIN_DAY, "*");
        RedisDataIdentify dayIdentify = new RedisDataIdentify(RedisConstants.MODEL_USER, userKey);
        // 用户登陆过的key
        Set<String> set = redisStringHandler.keys(dayIdentify);
        // 登陆记录删除
        for (String s : set) {
            redisStringHandler.del(s);
        }
    }
}
