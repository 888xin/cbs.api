package com.lifeix.cbs.api.impl.mission;

import com.lifeix.cbs.api.bean.mission.MissionResponse;
import com.lifeix.cbs.api.bean.mission.MissionUserListResponse;
import com.lifeix.cbs.api.bean.mission.MissionUserResponse;
import com.lifeix.cbs.api.bean.mission.PointResponse;
import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.cbs.api.common.exception.MsgCode;
import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.mission.Mission;
import com.lifeix.cbs.api.common.mission.MissionConstants;
import com.lifeix.cbs.api.common.util.CouponConstants;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.common.util.RedisConstants;
import com.lifeix.cbs.api.dao.mission.MissionGoldDao;
import com.lifeix.cbs.api.dao.mission.MissionUserDao;
import com.lifeix.cbs.api.dto.mission.MissionGold;
import com.lifeix.cbs.api.dto.mission.MissionUser;
import com.lifeix.cbs.api.service.coupon.CouponUserService;
import com.lifeix.cbs.api.service.misson.MissionUserService;
import com.lifeix.cbs.api.service.user.CbsUserService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.framework.redis.impl.RedisDataIdentify;
import com.lifeix.framework.redis.impl.RedisHashHandler;
import com.lifeix.framework.redis.impl.RedisSetsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lhx on 16-6-20 上午11:03
 *
 * @Description
 */
@Service("missionUserService")
public class MissionUserServiceImpl extends ImplSupport implements MissionUserService {

    protected static Logger LOG = LoggerFactory.getLogger(MissionUserServiceImpl.class);

    @Autowired
    private MissionUserDao missionUserDao ;

    @Autowired
    private MissionGoldDao missionGoldDao ;

    @Autowired
    private CouponUserService couponUserService ;

    @Autowired
    private RedisHashHandler redisHashHandler ;

    @Autowired
    private RedisSetsHandler redisSetsHandler ;

    @Autowired
    private CbsUserService cbsUserService ;

    @Override
    public boolean validate(Long userId, Mission missionValue) {
        boolean flag = false ;
        if (userId == null){
            LOG.error("userId is null");
            return flag ;
        }
        //床上任务
        boolean nyxFlag = validateNyx(userId, missionValue);
        if (nyxFlag){
            return nyxFlag ;
        }
        MissionUser missionUser = missionUserDao.findByUserId(userId);
        if (missionValue.getType() == MissionConstants.Type.DAY){
            //每日任务
            //取消的每日任务
            RedisDataIdentify cancelIdentify = new RedisDataIdentify(RedisConstants.MODEL_MISSION, RedisConstants.MissionRedis.CANCEL);
            Set<byte[]> set = redisSetsHandler.sMembersByte(cancelIdentify);
            Set<Integer> cancelSet = new HashSet<Integer>();
            int calcelLength = 0 ;
            if (set.size() > 0){
                for (byte[] bytes : set) {
                    cancelSet.add(Integer.valueOf(new String(bytes)));
                }
                calcelLength = cancelSet.size();
            }
            if (calcelLength > 0){
                if (cancelSet.contains(missionValue.getValue())){
                    return flag ;
                }
            }
            //每日任务
            String day = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            RedisDataIdentify dayIdentify = new RedisDataIdentify(RedisConstants.MissionRedis.DAY, day);
            if (missionUser == null){
                //用户无任务记录
                redisHashHandler.hset(dayIdentify, userId.toString().getBytes(),
                        Integer.valueOf(missionValue.getValue()).toString().getBytes());
                //插入用户积分
                MissionUser missionUserInsert = new MissionUser();
                missionUserInsert.setUserId(userId);
                missionUserInsert.setAmount(missionValue.getPoint());
                missionUserInsert.setValue(0);
                flag = missionUserDao.insert(missionUserInsert);
            } else {
                byte[] dayByte = redisHashHandler.hget(dayIdentify, userId.toString().getBytes());
                int dayValue = 0 ;
                if (dayByte != null){
                    dayValue = Integer.valueOf(new String(dayByte));
                }
                if ((missionValue.getValue() & dayValue) == 0){
                    //该每日任务没完成过，现在完成
                    redisHashHandler.hset(dayIdentify, userId.toString().getBytes(),
                            Integer.valueOf(missionValue.getValue() ^ dayValue).toString().getBytes());
                    MissionUser missionUserUpdata = new MissionUser();
                    missionUserUpdata.setUserId(userId);
                    missionUserUpdata.setAmount(missionValue.getPoint());
                    flag = missionUserDao.update(missionUserUpdata);
                }
            }
        } else if (missionValue.getType() == MissionConstants.Type.FIRST){
            //首次任务
            if (missionUser == null){
                //插入用户积分
                MissionUser missionUserInsert = new MissionUser();
                missionUserInsert.setUserId(userId);
                missionUserInsert.setValue(missionValue.getValue());
                missionUserInsert.setAmount(missionValue.getPoint());
                flag = missionUserDao.insert(missionUserInsert);
            } else {
                if ((missionValue.getValue() & missionUser.getValue()) == 0){
                    //该首次任务没完成过，现在完成
                    MissionUser missionUserUpdata = new MissionUser();
                    missionUserUpdata.setUserId(userId);
                    missionUserUpdata.setAmount(missionValue.getPoint());
                    missionUserUpdata.setValue(missionValue.getValue() ^ missionUser.getValue());
                    flag = missionUserDao.update(missionUserUpdata);
                }
            }
        }
        return flag;
    }

    @Override
    public MissionUserResponse validate(Long userId, Integer value, Integer type) throws L99IllegalParamsException {
        boolean flag = false ;
        if (userId == null){
            throw new L99IllegalParamsException(MsgCode.UserMsg.CODE_USER_ACCOUNT_NOT_FOUND, MsgCode.UserMsg.KEY_USER_ACCOUNT_NOT_FOUND);
        }
        MissionUser missionUser = missionUserDao.findByUserId(userId);
        MissionUserResponse missionUserResponse = new MissionUserResponse();
        if (missionUser != null){
            Mission missionValue = null ;
            for (Mission mission : Mission.values()) {
                if (mission.getType() == type && mission.getValue() == value){
                    missionValue = mission ;
                    break;
                }
            }
            if (missionValue == null){
                throw new L99IllegalParamsException(MsgCode.MissionMsg.CODE_MISSION_NOT_FOUND, MsgCode.MissionMsg.KEY_MISSION_NOT_FOUND);
            }
            if (missionValue.getType() == MissionConstants.Type.DAY){
                //每日任务
                //取消的每日任务
                RedisDataIdentify cancelIdentify = new RedisDataIdentify(RedisConstants.MODEL_MISSION, RedisConstants.MissionRedis.CANCEL);
                Set<byte[]> set = redisSetsHandler.sMembersByte(cancelIdentify);
                Set<Integer> cancelSet = new HashSet<Integer>();
                int calcelLength = 0 ;
                if (set.size() > 0){
                    for (byte[] bytes : set) {
                        cancelSet.add(Integer.valueOf(new String(bytes)));
                    }
                    calcelLength = cancelSet.size();
                }
                if (calcelLength > 0){
                    if (cancelSet.contains(missionValue.getValue())){
                        throw new L99IllegalParamsException(MsgCode.MissionMsg.CODE_MISSION_CANCEL, MsgCode.MissionMsg.KEY_MISSION_CANCEL);
                    }
                }
                String day = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                RedisDataIdentify dayIdentify = new RedisDataIdentify(RedisConstants.MissionRedis.DAY, day);
                byte[] dayByte = redisHashHandler.hget(dayIdentify, userId.toString().getBytes());
                int dayValue = 0 ;
                if (dayByte != null){
                    dayValue = Integer.valueOf(new String(dayByte));
                }
                if ((missionValue.getValue() & dayValue) > 0){
                    //该每日任务已经完成，判断是否跳过窗口
                    RedisDataIdentify identify = new RedisDataIdentify(RedisConstants.MissionRedis.ROAR_DAY, day);
                    byte[] tempByte = redisHashHandler.hget(identify, userId.toString().getBytes());
                    int tempValue = (tempByte==null) ? 0 : Integer.valueOf(new String(tempByte));
                    if ((tempValue & dayValue) == 0){
                        flag = true;
                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.DAY_OF_YEAR, 1);
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                        calendar.set(Calendar.MINUTE, 9);
                        calendar.set(Calendar.SECOND, 8);
                        // 第二天的凌晨00:00:00过期删除
                        long expireTime = (calendar.getTimeInMillis() - System.currentTimeMillis()) / 1000;
                        identify.setExpireTime(expireTime);
                        tempValue = tempValue ^ missionValue.getValue();
                        redisHashHandler.hset(identify, userId.toString().getBytes(),
                                Integer.valueOf(tempValue).toString().getBytes());
                        missionUserResponse.setAmount(missionValue.getPoint());

                    } else {
                        flag = false ;
                    }
                }
            } else if (missionValue.getType() == MissionConstants.Type.FIRST){
                //首次任务
                int userValue = missionUser.getValue();
                if ((missionValue.getValue() & userValue) > 0){
                    //该首次任务已经完成，判断是否跳过窗口
                    RedisDataIdentify identify = new RedisDataIdentify(RedisConstants.MODEL_MISSION, RedisConstants.MissionRedis.ROAR_FIRST);
                    byte[] tempByte = redisHashHandler.hget(identify, userId.toString().getBytes());
                    int tempValue = (tempByte==null) ? 0 : Integer.valueOf(new String(tempByte));
                    if ((tempValue & userValue) == 0){
                        flag = true;
                        tempValue = tempValue ^ missionValue.getValue();
                        redisHashHandler.hset(identify, userId.toString().getBytes(),
                                Integer.valueOf(tempValue).toString().getBytes());
                    } else {
                        flag = false ;
                    }
                    if (flag){
                        missionUserResponse.setAmount(missionValue.getPoint());
                    }
                }
            }
        }
        if (!flag){
            throw new L99IllegalParamsException(MsgCode.MissionMsg.CODE_MISSION_FINISH, MsgCode.MissionMsg.KEY_MISSION_FINISH);
        }
        return missionUserResponse ;
    }

    @Override
    public void edit(Long userId, Integer amount, Integer value) throws L99IllegalParamsException {
        ParamemeterAssert.assertDataNotNull(userId);
        MissionUser missionUser = missionUserDao.findByUserId(userId);
        boolean flag = false ;
        if (missionUser == null){
            MissionUser missionUserInsert = new MissionUser();
            missionUserInsert.setUserId(userId);
            amount = (amount == null) ? 0 : amount ;
            missionUserInsert.setAmount(amount);
            value = (value == null) ? 0 : value ;
            missionUserInsert.setValue(value);
            flag = missionUserDao.insert(missionUserInsert);
        } else {
            MissionUser missionUserUpdata = new MissionUser();
            missionUserUpdata.setUserId(userId);
            missionUserUpdata.setAmount(amount);
            missionUserUpdata.setValue(value);
            flag = missionUserDao.update(missionUserUpdata);
        }
        if (!flag){
            throw new L99IllegalParamsException(MsgCode.BasicMsg.CODE_OPERATE_FAIL, MsgCode.BasicMsg.KEY_OPERATE_FAIL);
        }
    }

    @Override
    public void consume(Long userId, Long goldId) throws L99IllegalParamsException, L99IllegalDataException {
        ParamemeterAssert.assertDataNotNull(userId, goldId);
        MissionGold missionGold = missionGoldDao.findById(goldId);
        if (missionGold == null){
            throw new L99IllegalParamsException(MsgCode.ContentMsg.CODE_CONTENT_FOUND, MsgCode.ContentMsg.KEY_CONTENT_FOUND);
        }
        MissionUser missionUser = missionUserDao.findByUserId(userId);
        if (missionUser == null || missionUser.getAmount() < missionGold.getPoint()){
            //用户账户积分少于兑换的积分
            throw new L99IllegalParamsException(MsgCode.MissionMsg.CODE_POINT_NOT_ENOUGH, MsgCode.MissionMsg.KEY_POINT_NOT_ENOUGH);
        }
        MissionUser missionUserUpdata = new MissionUser();
        missionUserUpdata.setUserId(userId);
        missionUserUpdata.setAmount(-missionGold.getPoint());
        boolean flag = missionUserDao.update(missionUserUpdata);
        if (!flag){
            throw new L99IllegalParamsException(MsgCode.BasicMsg.CODE_OPERATE_FAIL, MsgCode.BasicMsg.KEY_OPERATE_FAIL);
        }
        //给用户添加龙筹券
        couponUserService.settleCouponByPrice(userId, missionGold.getPrice(), CouponConstants.CouponSystem.TIME_24, "积分换取筹码");

    }

    @Override
    public MissionUserResponse getUserInfo(Long userId) throws L99IllegalParamsException {
        ParamemeterAssert.assertDataNotNull(userId);
        MissionUser missionUser = missionUserDao.findByUserId(userId);

        //取消的每日任务
        RedisDataIdentify cancelIdentify = new RedisDataIdentify(RedisConstants.MODEL_MISSION, RedisConstants.MissionRedis.CANCEL);
        Set<byte[]> set = redisSetsHandler.sMembersByte(cancelIdentify);
        Set<Integer> cancelSet = new HashSet<Integer>();
        int calcelLength = 0 ;
        if (set.size() > 0){
            for (byte[] bytes : set) {
                cancelSet.add(Integer.valueOf(new String(bytes)));
            }
            calcelLength = cancelSet.size();
        }
        //装载数据
        MissionUserResponse missionUserResponse = new MissionUserResponse();
        missionUserResponse.setUserId(userId);
        List<MissionResponse> missions = new ArrayList<MissionResponse>();
        if (missionUser == null){
            //用户所有任务都没完成过
            missionUserResponse.setAmount(0);
            for (Mission mission : Mission.values()) {
                if (mission.getType() != MissionConstants.Type.NYX){
                    //去除取消的每日任务
                    if (mission.getType() == MissionConstants.Type.DAY){
                        if (calcelLength > 0){
                            if (cancelSet.contains(mission.getValue())){
                                continue;
                            }
                        }
                    }
                    MissionResponse missionResponse = new MissionResponse();
                    missionResponse.setName(mission.getName());
                    missionResponse.setPoint(mission.getPoint());
                    missionResponse.setType(mission.getType());
                    missions.add(missionResponse);
                }
            }
        } else {
            missionUserResponse.setAmount(missionUser.getAmount());
            int firstUserValue = missionUser.getValue();
            //每日任务
            String day = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            RedisDataIdentify dayIdentify = new RedisDataIdentify(RedisConstants.MissionRedis.DAY, day);
            byte[] dayByte = redisHashHandler.hget(dayIdentify, userId.toString().getBytes());
            int dayValue = 0 ;
            if (dayByte != null){
                dayValue = Integer.valueOf(new String(dayByte));
            }
            for (Mission mission : Mission.values()) {
                MissionResponse missionResponse = new MissionResponse();
                //去除取消的每日任务
                if (mission.getType() == MissionConstants.Type.DAY){
                    if (calcelLength > 0){
                        if (cancelSet.contains(mission.getValue())){
                            continue;
                        }
                    }
                    missionResponse.setName(mission.getName());
                    missionResponse.setPoint(mission.getPoint());
                    missionResponse.setType(mission.getType());
                    if ((dayValue & mission.getValue()) > 0){
                        missionResponse.setFinish(true);
                    }
                    missions.add(missionResponse);
                } else if (mission.getType() == MissionConstants.Type.FIRST){
                    missionResponse.setName(mission.getName());
                    missionResponse.setPoint(mission.getPoint());
                    missionResponse.setType(mission.getType());
                    if ((firstUserValue & mission.getValue()) > 0){
                        missionResponse.setFinish(true);
                    }
                    missions.add(missionResponse);
                }
            }
        }

        missionUserResponse.setMissions(missions);

        return missionUserResponse;
    }

    @Override
    public MissionUserResponse getPointList(Long userId) throws L99IllegalParamsException {
        ParamemeterAssert.assertDataNotNull(userId);
        MissionUserResponse missionUserResponse = new MissionUserResponse();
        MissionUser missionUser = missionUserDao.findByUserId(userId);
        if (missionUser == null){
            missionUserResponse.setAmount(0);
        } else {
            missionUserResponse.setAmount(missionUser.getAmount());
        }
        missionUserResponse.setUserId(userId);
        List<MissionGold> missionGolds = missionGoldDao.getAll();
        List<PointResponse> pointList = new ArrayList<PointResponse>();
        for (MissionGold missionGold : missionGolds) {
            PointResponse pointResponse = new PointResponse();
            pointResponse.setId(missionGold.getId());
            pointResponse.setPoint(missionGold.getPoint());
            pointResponse.setGold(missionGold.getPrice());
            pointList.add(pointResponse);
        }
        missionUserResponse.setPoint_list(pointList);
        return missionUserResponse;
    }

    @Override
    public MissionUserResponse getPointListInner() {
        MissionUserResponse missionUserResponse = new MissionUserResponse();
        List<MissionGold> missionGolds = missionGoldDao.getAll();
        List<PointResponse> pointList = new ArrayList<PointResponse>();
        for (MissionGold missionGold : missionGolds) {
            PointResponse pointResponse = new PointResponse();
            pointResponse.setId(missionGold.getId());
            pointResponse.setPoint(missionGold.getPoint());
            pointResponse.setGold(missionGold.getPrice());
            pointList.add(pointResponse);
        }
        missionUserResponse.setPoint_list(pointList);
        return missionUserResponse;
    }

    @Override
    public boolean operDayMission(Integer value) {
        //取消的每日任务
        RedisDataIdentify cancelIdentify = new RedisDataIdentify(RedisConstants.MODEL_MISSION, RedisConstants.MissionRedis.CANCEL);
        if (value > 0){
            Long result = redisSetsHandler.sAdd(cancelIdentify, value.toString());
            if (result != null && result > 0){
                return true ;
            }
        } else {
            //去除取消的每日任务
            value = -value ;
            Long result = redisSetsHandler.sRemove(cancelIdentify, value.toString());
            if (result != null && result > 0){
                return true ;
            }
        }
        return false;
    }

    @Override
    public PointResponse operReward(Long id, Integer point, Integer price) throws L99IllegalParamsException {
        PointResponse pointResponse = new PointResponse();
        boolean flag = false ;
        if (id == null){
            MissionGold missionGold = new MissionGold();
            missionGold.setPoint(point);
            missionGold.setPrice(price);
            MissionGold missionGold1 = missionGoldDao.insert(missionGold);
            pointResponse.setId(missionGold1.getId());
            pointResponse.setPoint(missionGold1.getPoint());
            pointResponse.setGold(missionGold1.getPrice());
            if (pointResponse.getId() > 0){
                flag = true ;
            }
        } else {
            MissionGold missionGold = new MissionGold();
            missionGold.setId(id);
            missionGold.setPoint(point);
            missionGold.setPrice(price);
            flag = missionGoldDao.update(missionGold);
        }
        if (!flag){
            throw new L99IllegalParamsException(MsgCode.BasicMsg.CODE_OPERATE_FAIL, MsgCode.BasicMsg.KEY_OPERATE_FAIL);
        }
        return pointResponse ;
    }

    @Override
    public void deleteReward(Long id) throws L99IllegalParamsException {
        ParamemeterAssert.assertDataNotNull(id);
        boolean flag = missionGoldDao.delete(id);
        if (!flag) {
            throw new L99IllegalParamsException(MsgCode.BasicMsg.CODE_OPERATE_FAIL, MsgCode.BasicMsg.KEY_OPERATE_FAIL);
        }
    }

    @Override
    public MissionUserListResponse getUserListInfo(Integer page, Integer limit) {
        if (page == null){
            page = 0 ;
        }
        MissionUserListResponse missionUserListResponse = new MissionUserListResponse();
        List<MissionUserResponse> mission_user = new ArrayList<MissionUserResponse>();
        List<MissionUser> list = missionUserDao.findByListInner(page*limit, limit);
        for (MissionUser missionUser : list) {
            MissionUserResponse missionUserResponse = new MissionUserResponse();
            missionUserResponse.setUserId(missionUser.getUserId());
            missionUserResponse.setAmount(missionUser.getAmount());
            missionUserResponse.setValue(missionUser.getValue());
            CbsUserResponse user = cbsUserService.getSimpleCbsUserByUserId(missionUser.getUserId());
            missionUserResponse.setUser(user);
            mission_user.add(missionUserResponse);
        }
        missionUserListResponse.setMission_user(mission_user);
        if (limit == list.size()){
            missionUserListResponse.setPage_num(++page);
        } else {
            missionUserListResponse.setPage_num(-1);
        }
        return missionUserListResponse;
    }

    @Override
    public MissionUserResponse getUserDayInfo(String day, Long userId) {
        RedisDataIdentify dayIdentify = new RedisDataIdentify(RedisConstants.MissionRedis.DAY, day);
        byte[] dayByte = redisHashHandler.hget(dayIdentify, userId.toString().getBytes());
        int dayValue = 0 ;
        if (dayByte != null){
            dayValue = Integer.valueOf(new String(dayByte));
        }
        MissionUserResponse missionUserResponse = new MissionUserResponse();
        missionUserResponse.setUserId(userId);
        missionUserResponse.setValue(dayValue);
        return missionUserResponse;
    }

    @Override
    public MissionUserResponse getUserInfoInner(Long userId) {
        MissionUser missionUser = missionUserDao.findByUserId(userId);
        CbsUserResponse user = cbsUserService.getSimpleCbsUserByUserId(userId);
        MissionUserResponse missionUserResponse = new MissionUserResponse();
        missionUserResponse.setUser(user);
        if (missionUser == null){
            missionUserResponse.setValue(0);
            missionUserResponse.setAmount(0);
        } else {
            missionUserResponse.setValue(missionUser.getValue());
            missionUserResponse.setAmount(missionUser.getAmount());
        }
        return missionUserResponse;
    }

    @Override
    public MissionUserResponse getUserInfoSimple(Long userId) {
        MissionUser missionUser = missionUserDao.findByUserId(userId);
        MissionUserResponse missionUserResponse = new MissionUserResponse();
        if (missionUser == null){
            missionUserResponse.setAmount(0);
        } else {
            missionUserResponse.setAmount(missionUser.getAmount());
        }
        return missionUserResponse;
    }

    @Override
    public String getCancelDayMission() {
        //取消的每日任务
        RedisDataIdentify cancelIdentify = new RedisDataIdentify(RedisConstants.MODEL_MISSION, RedisConstants.MissionRedis.CANCEL);
        Set<byte[]> set = redisSetsHandler.sMembersByte(cancelIdentify);
        StringBuilder stringBuilder = new StringBuilder();
        if (set.size() > 0){
            for (byte[] bytes : set) {
                stringBuilder.append(Integer.valueOf(new String(bytes))).append(",");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    private boolean validateNyx(Long userId, Mission missionValue) {
        boolean flag = false;
        //判断用户ID的范围，如果是导过来床上用户，需处理
//        if (userId > 50000000L && userId < 100000000L){
        //床上过来的用户的额外任务：每日登陆（签到） 下注 充值
        if (missionValue == Mission.EVERY_DAY_LOGIN || missionValue == Mission.FIRST_LONGBI || missionValue == Mission.NYX_BET) {
            Mission missionNyxValue = Mission.NYX_BET;
            //转换任务
            if (missionValue == Mission.EVERY_DAY_LOGIN) {
                missionNyxValue = Mission.NYX_LOGIN;
            } else if (missionValue == Mission.FIRST_LONGBI) {
                missionNyxValue = Mission.NYX_RECHARGE;
            }
            String day = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            RedisDataIdentify identify = new RedisDataIdentify(RedisConstants.MissionRedis.NYX, day);
            byte[] dayByte = redisHashHandler.hget(identify, userId.toString().getBytes());
            if (dayByte == null) {
                //用户无记录
                redisHashHandler.hset(identify, userId.toString().getBytes(), Integer.valueOf(missionNyxValue.getValue()).toString().getBytes());
            } else {
                int value = Integer.valueOf(new String(dayByte));
                if ((missionNyxValue.getValue() & value) == 0) {
                    //该任务没完成过，现在完成
                    redisHashHandler.hset(identify, userId.toString().getBytes(), Integer.valueOf(missionNyxValue.getValue() ^ value).toString().getBytes());
                }
            }
            if (missionNyxValue == Mission.NYX_BET) {
                //大赢家没有的任务，另外处理
                flag = true;
            }
            //存储做过任务的用户id
            RedisDataIdentify identifyUser = new RedisDataIdentify(RedisConstants.MODEL_MISSION, RedisConstants.MissionRedis.USER);
            redisSetsHandler.sAdd(identifyUser, userId + "");
        }
//        }
        return flag;
    }


}
