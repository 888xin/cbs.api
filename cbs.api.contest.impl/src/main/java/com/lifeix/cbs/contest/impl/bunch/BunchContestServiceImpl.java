package com.lifeix.cbs.contest.impl.bunch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lifeix.cbs.api.bean.gold.GoldResponse;
import com.lifeix.cbs.api.common.exception.MsgCode;
import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.api.common.util.CommerceMath;
import com.lifeix.cbs.api.common.util.ContestConstants;
import com.lifeix.cbs.api.common.util.CouponConstants;
import com.lifeix.cbs.api.common.util.GoldConstants.MoneyMissedType;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.common.util.PlayType;
import com.lifeix.cbs.api.common.util.RedisConstants;
import com.lifeix.cbs.api.service.coupon.CouponUserService;
import com.lifeix.cbs.api.service.money.MoneyService;
import com.lifeix.cbs.api.service.money.MoneyStatisticService;
import com.lifeix.cbs.contest.bean.bunch.BunchContestListResponse;
import com.lifeix.cbs.contest.bean.bunch.BunchContestResponse;
import com.lifeix.cbs.contest.bean.bunch.BunchOptionsResponse;
import com.lifeix.cbs.contest.bean.bunch.BunchPrizeResponse;
import com.lifeix.cbs.contest.bean.fb.ContestResponse;
import com.lifeix.cbs.contest.dao.bb.BbContestDao;
import com.lifeix.cbs.contest.dao.bb.BbOddsJcDao;
import com.lifeix.cbs.contest.dao.bb.BbOddsSizeDao;
import com.lifeix.cbs.contest.dao.bb.BbTeamDao;
import com.lifeix.cbs.contest.dao.bunch.BunchBetDao;
import com.lifeix.cbs.contest.dao.bunch.BunchContestDao;
import com.lifeix.cbs.contest.dao.bunch.BunchPrizeDao;
import com.lifeix.cbs.contest.dao.fb.FbContestDao;
import com.lifeix.cbs.contest.dao.fb.FbOddsJcDao;
import com.lifeix.cbs.contest.dao.fb.FbOddsSizeDao;
import com.lifeix.cbs.contest.dao.fb.FbTeamDao;
import com.lifeix.cbs.contest.dto.bb.BbContest;
import com.lifeix.cbs.contest.dto.bb.BbTeam;
import com.lifeix.cbs.contest.dto.bunch.BunchBet;
import com.lifeix.cbs.contest.dto.bunch.BunchContest;
import com.lifeix.cbs.contest.dto.bunch.BunchPrize;
import com.lifeix.cbs.contest.dto.fb.FbContest;
import com.lifeix.cbs.contest.dto.fb.FbTeam;
import com.lifeix.cbs.contest.dto.odds.OddsJc;
import com.lifeix.cbs.contest.dto.odds.OddsSize;
import com.lifeix.cbs.contest.impl.ImplSupport;
import com.lifeix.cbs.contest.service.bunch.BunchContestService;
import com.lifeix.cbs.contest.util.transform.ContestTransformUtil;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.framework.redis.impl.RedisDataIdentify;
import com.lifeix.framework.redis.impl.RedisListHandler;
import com.lifeix.framework.redis.impl.RedisStringHandler;

/**
 * Created by lhx on 16-5-17 上午11:17
 *
 * @Description
 */
@Service("bunchContestService")
public class BunchContestServiceImpl extends ImplSupport implements BunchContestService {

    @Autowired
    private BunchContestDao bunchContestDao;

    @Autowired
    private BunchBetDao bunchBetDao;

    @Autowired
    private BunchPrizeDao bunchPrizeDao;

    @Autowired
    private FbContestDao fbContestDao;

    @Autowired
    private BbContestDao bbContestDao;

    // @Autowired
    // private BbOddsOpDao bbOddsOpDao;

    @Autowired
    private BbOddsJcDao bbOddsJcDao;

    @Autowired
    private BbOddsSizeDao bbOddsSizeDao;

    @Autowired
    private BbTeamDao bbTeamDao;

    // @Autowired
    // private FbOddsOpDao fbOddsOpDao;

    @Autowired
    private FbOddsJcDao fbOddsJcDao;

    @Autowired
    private FbOddsSizeDao fbOddsSizeDao;

    @Autowired
    private FbTeamDao fbTeamDao;

    @Autowired
    private MoneyService moneyService;

    @Autowired
    private MoneyStatisticService moneyStatisticService;

    @Autowired
    private CouponUserService couponUserService;

    @Autowired
    private RedisStringHandler redisStringHandler;

    @Autowired
    private RedisListHandler redisListHandler;

    @Override
    @Transactional(rollbackFor = L99IllegalDataException.class)
    public void insert(String name, String image, String options, String prizes, int cost, boolean longbi)
            throws IOException, L99IllegalParamsException, L99IllegalDataException {
        ParamemeterAssert.assertDataNotNull(name, options, prizes);
        ObjectMapper mapper = new ObjectMapper();
        List<BunchOptionsResponse> optionsList = mapper.readValue(options, new TypeReference<List<BunchOptionsResponse>>() {
        });
        // 查询赛事，预估开始时间和结束时间
        List<Long> fbList = new ArrayList<>();
        List<Long> bbList = new ArrayList<>();
        for (BunchOptionsResponse bunchOptionsResponse : optionsList) {
            Integer type = bunchOptionsResponse.getContest_type();
            if (type == ContestConstants.ContestType.FOOTBALL) {
                fbList.add(bunchOptionsResponse.getContest_id());
            } else if (type == ContestConstants.ContestType.BASKETBALL) {
                bbList.add(bunchOptionsResponse.getContest_id());
            }
        }
        Map<Long, FbContest> fbMap = new HashMap<>();
        Map<Long, BbContest> bbMap = new HashMap<>();
        if (fbList.size() > 0) {
            fbMap = fbContestDao.findFbContestMapByIds(fbList);
        }
        if (bbList.size() > 0) {
            bbMap = bbContestDao.findBbContestMapByIds(bbList);
        }
        long startTime = Long.MAX_VALUE;
        long endTime = Long.MIN_VALUE;
        // 遍历足球map，无数据跳过
        for (Long aLong : fbMap.keySet()) {
            FbContest fbContest = fbMap.get(aLong);
            long startTime1 = fbContest.getStartTime().getTime();
            if (startTime1 < new Date().getTime()) {
                // 开始时间比现在时间还超前，抛出异常，阻止添加数据
                throw new L99IllegalParamsException(MsgCode.BetMsg.CODE_BET_CONTEST_BEGIN,
                        MsgCode.BetMsg.KEY_BET_CONTEST_BEGIN);
            }
            if (startTime1 < startTime) {
                startTime = startTime1;
            }
            // 3小时后结束
            long endTime1 = startTime1 + 1000 * 60 * 60 * 3;
            if (endTime1 > endTime) {
                endTime = endTime1;
            }
        }
        // 遍历篮球map，无数据跳过
        for (Long aLong : bbMap.keySet()) {
            BbContest bbContest = bbMap.get(aLong);
            long startTime1 = bbContest.getStartTime().getTime();
            if (startTime1 < new Date().getTime()) {
                // 开始时间比现在时间还超前，抛出异常，阻止添加数据
                throw new L99IllegalParamsException(MsgCode.BetMsg.CODE_BET_CONTEST_BEGIN,
                        MsgCode.BetMsg.KEY_BET_CONTEST_BEGIN);
            }
            if (startTime1 < startTime) {
                startTime = startTime1;
            }
            // 3小时后结束
            long endTime1 = startTime1 + 1000 * 60 * 60 * 3;
            if (endTime1 > endTime) {
                endTime = endTime1;
            }
        }

        BunchContest bunchContest = new BunchContest();
        bunchContest.setName(name);
        bunchContest.setImage(image);
        bunchContest.setOptions(options);
        bunchContest.setCost(cost);
        bunchContest.setLongbi(longbi);
        bunchContest.setStartTime(new Date(startTime));
        bunchContest.setEndTime(new Date(endTime));
        Long bunchId = bunchContestDao.insert(bunchContest);
        if (bunchId <= 0) {
            throw new L99IllegalDataException(MsgCode.BasicMsg.CODE_OPERATE_FAIL, MsgCode.BasicMsg.KEY_OPERATE_FAIL);
        }
        // 添加奖品项
        List<BunchPrize> bunchPrizes = mapper.readValue(prizes, new TypeReference<List<BunchPrize>>() {
        });
        for (BunchPrize bunchPrize : bunchPrizes) {
            bunchPrize.setBunchId(bunchId);
        }
        boolean flag = bunchPrizeDao.insertBatch(bunchPrizes);
        if (!flag) {
            throw new L99IllegalDataException(MsgCode.BasicMsg.CODE_OPERATE_FAIL, MsgCode.BasicMsg.KEY_OPERATE_FAIL);
        }

    }

    @Override
    public void update(Long id, String name, String image, String options, Integer cost, Boolean longbi, String result,
                       Integer status, Integer people) throws L99IllegalParamsException, L99IllegalDataException {
        ParamemeterAssert.assertDataNotNull(id);
        if (people != null) {
            // 参与人数
            RedisDataIdentify betIdentify = new RedisDataIdentify(RedisConstants.BunchRedis.BET_RECORDS, id + "");
            redisStringHandler.set(betIdentify, people.toString().getBytes());
        }
        BunchContest bunchContest = new BunchContest();
        bunchContest.setId(id);
        bunchContest.setName(name);
        bunchContest.setImage(image);
        if (StringUtils.isNotBlank(options)) {
            BunchContest bunchContest1 = bunchContestDao.selectById(id);
            if (!bunchContest1.getOptions().equals(options)) {
                // 赛事有修改
                List<BunchBet> list = bunchBetDao.getListByBunchId(id, null, null, 1);
                if (list.size() > 0) {
                    // 已经有下注，不可修改
                    throw new L99IllegalDataException(MsgCode.BetMsg.CODE_BET_HAS, MsgCode.BetMsg.KEY_BET_HAS);
                }
            }
        }
        bunchContest.setOptions(options);
        bunchContest.setCost(cost);
        bunchContest.setLongbi(longbi);
        bunchContest.setResult(result);
        bunchContest.setStatus(status);
        boolean flag = bunchContestDao.update(bunchContest);
        if (!flag) {
            throw new L99IllegalDataException(MsgCode.BasicMsg.CODE_OPERATE_FAIL, MsgCode.BasicMsg.KEY_OPERATE_FAIL);
        }
    }

    @Override
    public BunchContestListResponse getList(Integer status, Long startId, Integer limit) {
        BunchContestListResponse bunchContestListResponse = new BunchContestListResponse();
        List<BunchContestResponse> bunchContestResponseList = new ArrayList<>();
        List<BunchContest> bunchContestList;
        if (ContestConstants.BunchContestListStatus.NEW == status) {
            bunchContestList = bunchContestDao.getList();
        } else {
            bunchContestList = bunchContestDao.getOldList(startId, limit);
        }
        int size = bunchContestList.size();
        if (size > 0) {
            for (BunchContest bunchContest : bunchContestList) {
                Date startTime = bunchContest.getStartTime();
                if (size == ContestConstants.BunchContestListStatus.NEW && startTime.before(new Date())) {
                    // 第一场比赛开始时间比现在时间提前，则过滤掉
                    continue;
                }
                BunchContestResponse bunchContestResponse = new BunchContestResponse();
                // 列表只需要部分信息
                bunchContestResponse.setId(bunchContest.getId());
                bunchContestResponse.setName(bunchContest.getName());
                bunchContestResponse.setImage(bunchContest.getImage());
                if (bunchContest.getCost() != 0) {
                    bunchContestResponse.setCost(bunchContest.getCost());
                    bunchContestResponse.setLongbi(bunchContest.getLongbi());
                }
                bunchContestResponse.setStart_time(CbsTimeUtils.getUtcTimeForDate(startTime));
                // 参与人数
                RedisDataIdentify betIdentify = new RedisDataIdentify(RedisConstants.BunchRedis.BET_RECORDS,
                        bunchContest.getId() + "");
                byte[] betByte = redisStringHandler.get(betIdentify);
                if (betByte != null && betByte.length > 0) {
                    bunchContestResponse.setBet_num(Integer.valueOf(new String(betByte)));
                } else {
                    bunchContestResponse.setBet_num(0);
                }
                bunchContestResponseList.add(bunchContestResponse);
            }
        }
        bunchContestListResponse.setBunches(bunchContestResponseList);
        if (size > 0 && ContestConstants.BunchContestListStatus.OLD == status) {
            // 塞入start
            bunchContestListResponse.setStartId(bunchContestList.get(size - 1).getId());
        }

        return bunchContestListResponse;
    }

    @Override
    public BunchContestListResponse getInnerList(Integer status, Long startId, Integer limit) {
        BunchContestListResponse bunchContestListResponse = new BunchContestListResponse();
        List<BunchContestResponse> bunchContestResponseList = new ArrayList<>();
        List<BunchContest> bunchContestList = bunchContestDao.getInnerList(status, startId, limit);
        int size = bunchContestList.size();
        if (size > 0) {
            for (BunchContest bunchContest : bunchContestList) {
                BunchContestResponse bunchContestResponse = new BunchContestResponse();
                bunchContestResponse.setId(bunchContest.getId());
                bunchContestResponse.setName(bunchContest.getName());
                bunchContestResponse.setImage(bunchContest.getImage());
                bunchContestResponse.setCost(bunchContest.getCost());
                bunchContestResponse.setLongbi(bunchContest.getLongbi());
                bunchContestResponse.setStatus(bunchContest.getStatus());
                bunchContestResponse.setResult(bunchContest.getResult());
                bunchContestResponse.setStart_time(CbsTimeUtils.getUtcTimeForDate(bunchContest.getStartTime()));
                bunchContestResponse.setEnd_time(CbsTimeUtils.getUtcTimeForDate(bunchContest.getEndTime()));
                // 参与人数
                RedisDataIdentify betIdentify = new RedisDataIdentify(RedisConstants.BunchRedis.BET_RECORDS,
                        bunchContest.getId() + "");
                byte[] betByte = redisStringHandler.get(betIdentify);
                if (betByte != null && betByte.length > 0) {
                    bunchContestResponse.setBet_num(Integer.valueOf(new String(betByte)));
                } else {
                    bunchContestResponse.setBet_num(0);
                }
                bunchContestResponseList.add(bunchContestResponse);
            }
        }
        bunchContestListResponse.setBunches(bunchContestResponseList);
        return bunchContestListResponse;
    }

    @Override
    public BunchContestResponse view(Long id, Long userId) throws IOException, L99IllegalParamsException,
            L99IllegalDataException, JSONException {
        ParamemeterAssert.assertDataNotNull(id);
        BunchContest bunchContest = bunchContestDao.selectById(id);
        ObjectMapper mapper = new ObjectMapper();
        BunchContestResponse bunchContestResponse = toResponse(bunchContest, mapper);

        if (userId != null) {
            // 查找用户对该串的下注记录
            BunchBet bunchBet = bunchBetDao.selectByUser(id, userId);
            if (bunchBet != null) {
                Integer[] userSupport = mapper.readValue(bunchBet.getSupport(), new TypeReference<Integer[]>() {
                });
                bunchContestResponse.setUser_support(userSupport);
                bunchContestResponse.setReward_status(bunchBet.getStatus());
            }

            // 用户金额
            if (bunchContest.getCost() > 0) {
                if (bunchContest.getLongbi()) {
                    // 返回用户的龙币数量
                    GoldResponse gold = moneyService.viewUserMoney(userId, "");
                    bunchContestResponse.setGold(CommerceMath.sub(gold.getBalance(), gold.getFrozen()));
                } else {
                    // 返回该串可用的龙筹券（通用）
                    List<Integer> setPrice = new ArrayList<>();
                    setPrice.add(bunchContest.getCost());
                    List<Object[]> list = couponUserService.findUserZodiacGameBetCoupons(userId, setPrice);
                    Object[] objects = list.get(0);
                    if (objects.length > 0) {
                        bunchContestResponse.setCoupon_id((long) objects[0]);
                    }
                }
            }
        }

        // 塞入奖品信息
        getPrize(id, bunchContestResponse);
        // 参与人数
        RedisDataIdentify betIdentify = new RedisDataIdentify(RedisConstants.BunchRedis.BET_RECORDS, bunchContest.getId()
                + "");
        byte[] betByte = redisStringHandler.get(betIdentify);
        if (betByte.length > 0) {
            bunchContestResponse.setBet_num(Integer.valueOf(new String(betByte)));
        } else {
            bunchContestResponse.setBet_num(0);
        }

        return bunchContestResponse;
    }

    @Override
    public BunchContestResponse viewInner(Long id) throws L99IllegalParamsException, IOException {
        ParamemeterAssert.assertDataNotNull(id);
        BunchContest bunchContest = bunchContestDao.selectById(id);
        ObjectMapper mapper = new ObjectMapper();
        BunchContestResponse bunchContestResponse = toResponseInner(bunchContest, mapper);
        return bunchContestResponse;
    }

    @Override
    public BunchContestResponse viewPrizeInner(Long id) throws L99IllegalParamsException, IOException {
        ParamemeterAssert.assertDataNotNull(id);
        BunchContestResponse bunchContestResponse = new BunchContestResponse();
        List<BunchPrizeResponse> list = new ArrayList<>();
        for (BunchPrize prize : bunchPrizeDao.selectByBunchId(id)) {
            toPrize(list, prize);
        }
        bunchContestResponse.setPrize(list);
        return bunchContestResponse;
    }

    @Override
    public void updatePrize(String prize) throws IOException, L99IllegalDataException {
        ObjectMapper mapper = new ObjectMapper();
        BunchPrize bunchPrize = mapper.readValue(prize, BunchPrize.class);
        boolean flag = bunchPrizeDao.update(bunchPrize);
        if (!flag) {
            throw new L99IllegalDataException(MsgCode.BasicMsg.CODE_OPERATE_FAIL, MsgCode.BasicMsg.KEY_OPERATE_FAIL);
        }
    }

    @Override
    public void addPrizeImage(String image) throws L99IllegalParamsException {
        ParamemeterAssert.assertDataNotNull(image);
        RedisDataIdentify betIdentify = new RedisDataIdentify(RedisConstants.MODEL_BUNCH, RedisConstants.BunchRedis.IMAGE);
        redisListHandler.insertRedisData(betIdentify, image.getBytes());
    }

    @Override
    public void deletePrizeImage(String image) throws L99IllegalParamsException {
        ParamemeterAssert.assertDataNotNull(image);
        RedisDataIdentify betIdentify = new RedisDataIdentify(RedisConstants.MODEL_BUNCH, RedisConstants.BunchRedis.IMAGE);
        redisListHandler.delRedisData(betIdentify, 1, image.getBytes());
    }

    @Override
    public BunchPrizeResponse getPrizeImage() {
        RedisDataIdentify betIdentify = new RedisDataIdentify(RedisConstants.MODEL_BUNCH, RedisConstants.BunchRedis.IMAGE);
        List<byte[]> list = redisListHandler.sMembersByte(betIdentify);
        StringBuilder stringBuilder = new StringBuilder();
        for (byte[] bytes : list) {
            stringBuilder.append(new String(bytes)).append(",");
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        BunchPrizeResponse bunchPrizeResponse = new BunchPrizeResponse();
        bunchPrizeResponse.setImage(stringBuilder.toString());
        return bunchPrizeResponse;
    }

    @Override
    public BunchContestResponse getUserAward(Long id) throws L99IllegalParamsException {
        ParamemeterAssert.assertDataNotNull(id);
        BunchContestResponse bunchContestResponse = new BunchContestResponse();
        List<BunchBet> bunchBetList = bunchBetDao.getListByBunchId(id, ContestConstants.BunchBetStatus.MAYBE_REWARD, null,
                10000);
        if (bunchBetList.size() > 0) {
            // 奖品
            List<BunchPrize> bunchPrizeList = bunchPrizeDao.selectByBunchId(id);
            Map<Integer, Set<Long>> map = new HashMap<>();
            for (BunchBet bunchBet : bunchBetList) {
                int winNum = bunchBet.getWinNum();
                Set<Long> set = map.get(winNum);
                if (set == null) {
                    set = new HashSet<>();
                }
                set.add(bunchBet.getUserId());
                map.put(winNum, set);
            }

            List<BunchPrizeResponse> prize = new ArrayList<>();
            for (BunchPrize bunchPrize : bunchPrizeList) {
                BunchPrizeResponse bunchPrizeResponse = new BunchPrizeResponse();
                int winNum = bunchPrize.getWinNum();
                int num = bunchPrize.getNum();
                Set<Long> all = map.get(winNum);
                Set<Long> maybe = new HashSet<>();
                if (all != null && all.size() > num) {
                    List<Long> allList = new ArrayList<>(all);
                    for (int i = 0; i < num; i++) {
                        Random random = new Random();
                        int n = random.nextInt(allList.size());
                        maybe.add(allList.get(n));
                        allList.remove(n);
                    }
                    bunchPrizeResponse.setMaybe(maybe);
                } else {
                    bunchPrizeResponse.setMaybe(all);
                }
                bunchPrizeResponse.setAll(all);
                bunchPrizeResponse.setName(bunchPrize.getName());
                bunchPrizeResponse.setPrice(bunchPrize.getPrice());
                bunchPrizeResponse.setType(bunchPrize.getType());
                bunchPrizeResponse.setWin_num(bunchPrize.getWinNum());
                bunchPrizeResponse.setNum(bunchPrize.getNum());
                prize.add(bunchPrizeResponse);
            }
            bunchContestResponse.setPrize(prize);
        }
        return bunchContestResponse;
    }

    @Override
    public void sendAward(Long bunchId, String userIds) throws L99IllegalParamsException, L99IllegalDataException,
            JSONException {
        ParamemeterAssert.assertDataNotNull(bunchId);
        if (StringUtils.isNotBlank(userIds)) {
            String[] userIdStrs = userIds.split(",");
            List<Long> userlist = new ArrayList<>();
            for (String s : userIdStrs) {
                userlist.add(Long.valueOf(s));
            }
            List<BunchBet> bunchBetList = bunchBetDao.selectByUsers(bunchId, userlist);
            List<BunchPrize> bunchPrizeList;
            Map<Integer, BunchPrize> map = new HashMap<>();
            if (bunchBetList.size() > 0) {
                bunchPrizeList = bunchPrizeDao.selectByBunchId(bunchId);
                for (BunchPrize bunchPrize : bunchPrizeList) {
                    map.put(bunchPrize.getWinNum(), bunchPrize);
                }
            }
            for (BunchBet bunchBet : bunchBetList) {
                if (bunchBet.getStatus() == ContestConstants.BunchBetStatus.MAYBE_REWARD) {
                    // 先修改状态再派奖
                    BunchBet bunchBet1 = new BunchBet();
                    bunchBet1.setId(bunchBet.getId());
                    bunchBet1.setStatus(ContestConstants.BunchBetStatus.GET_REWARD);
                    bunchBetDao.update(bunchBet1);
                    // 发奖，虚拟奖品（龙币，筹码）直接发，实物修改状态
                    BunchPrize bunchPrize = map.get(bunchBet.getWinNum());
                    if (bunchPrize.getType() == ContestConstants.BunchPrizeType.GOLD) {
                        // 发送筹码
                        couponUserService.settleCouponByPrice(bunchBet.getUserId(), bunchPrize.getPrice(),
                                CouponConstants.CouponSystem.TIME_24, "赛事活动串赢筹码");
                    } else if (bunchPrize.getType() == ContestConstants.BunchPrizeType.LONGBI) {
                        // 发送龙币
                        moneyService.earnMoney(bunchBet.getUserId(), (double) bunchPrize.getPrice(), "赛事活动串赢龙币", null,
                                MoneyMissedType.BUNCH_SETTLE, bunchBet.getId().toString());
                        // 统计
                        moneyStatisticService.insertUserConsumer(bunchBet.getUserId() + "", -(double) bunchPrize.getPrice());
                    } else if (bunchPrize.getType() == ContestConstants.BunchPrizeType.KIND) {
                        // 发送实物，暂时不做处理
                    }
                }
            }
            // 再次查找可能中奖的记录，更新状态为中奖但未获得奖
            List<BunchBet> maybeList = bunchBetDao.getListByBunchId(bunchId, ContestConstants.BunchBetStatus.MAYBE_REWARD,
                    null, 10000);
            if (maybeList.size() > 0) {
                for (BunchBet bunchBet : maybeList) {
                    bunchBet.setStatus(ContestConstants.BunchBetStatus.NOT_GET_REWARD);
                }
                bunchBetDao.updateBatch(maybeList);
            }
        }
        // 修改赛事串的状态，标志为已结算，userIds为空，则无人中奖，照样更新状态
        BunchContest bunchContest = new BunchContest();
        bunchContest.setId(bunchId);
        bunchContest.setStatus(ContestConstants.BunchContestStatus.SETTELED);
        bunchContestDao.update(bunchContest);
    }

    // 填充数据
    private BunchContestResponse toResponse(BunchContest bunchContest, ObjectMapper mapper) throws IOException {
        BunchContestResponse bunchContestResponse = new BunchContestResponse();
        // 解析出赛事
        List<BunchOptionsResponse> optionsList = mapper.readValue(bunchContest.getOptions(),
                new TypeReference<List<BunchOptionsResponse>>() {
                });
        ContestResponse contestResponse = new ContestResponse();
        long contestIdTmp = 0;
        int typeTmp = 0;
        for (BunchOptionsResponse bunchOptionsResponse : optionsList) {
            long contestId = bunchOptionsResponse.getContest_id();
            int type = bunchOptionsResponse.getContest_type();
            int playType = bunchOptionsResponse.getPlay_type();
            if (type == ContestConstants.ContestType.FOOTBALL) {
                // 塞入赛事信息
                // 与上一次查找的赛事不相同，就要重新查找
                if (type != typeTmp || contestId != contestIdTmp) {
                    FbContest contest = fbContestDao.selectById(contestId);
                    List<Long> teamIds = new ArrayList<>();
                    teamIds.add(contest.getHomeTeam());
                    teamIds.add(contest.getAwayTeam());
                    Map<Long, FbTeam> teams = fbTeamDao.findFbTeamMapByIds(teamIds);
                    contestResponse = ContestTransformUtil.transformFbContest(contest, teams.get(contest.getHomeTeam()),
                            teams.get(contest.getAwayTeam()), new int[]{0, 0}, true);

                    typeTmp = type;
                    contestIdTmp = contestId;
                }
                bunchOptionsResponse.setContestResponse(contestResponse);
                // 塞入盘口
                if (playType == PlayType.FB_RQSPF.getId()) {
                    OddsJc jc = fbOddsJcDao.selectByContest(contestId);
                    bunchOptionsResponse.setHandicap(jc.getHandicap());
                } else if (playType == PlayType.FB_SIZE.getId()) {
                    OddsSize oddsSize = fbOddsSizeDao.selectByContest(contestId);
                    bunchOptionsResponse.setHandicap(oddsSize.getHandicap());
                }

                // 塞入赔率
                // 设计可能不需要显示赔率，注释掉
                // if (playType == PlayType.FB_SPF.getId()){
                // OddsOp op = fbOddsOpDao.selectByContest(contestId);
                // bunchOptionsResponse.setOddsOpResponse(OddsTransformUtil.transformOddsOp(op));
                // } else if (playType == PlayType.FB_RQSPF.getId()){
                // OddsJc jc = fbOddsJcDao.selectByContest(contestId);
                // bunchOptionsResponse.setOddsJcResponse(OddsTransformUtil.transformOddsJc(jc));
                // } else if (playType == PlayType.FB_SIZE.getId()){
                // OddsSize oddsSize = fbOddsSizeDao.selectByContest(contestId);
                // bunchOptionsResponse.setOddsSizeResponse(OddsTransformUtil.transformOddsSize(oddsSize));
                // } else if (playType == PlayType.FB_ODDEVEN.getId()){
                // OddsDssResponse oddsDssResponse =
                // FbOddsDssDateUtil.getInstance().getDssResponse();
                // bunchOptionsResponse.setOddsDssResponse(oddsDssResponse);
                // }
            } else if (type == ContestConstants.ContestType.BASKETBALL) {
                // 塞入赛事信息
                // 与上一次查找的赛事不相同，就要重新查找
                if (type != typeTmp || contestId != contestIdTmp) {
                    BbContest contest = bbContestDao.selectById(contestId);
                    List<Long> teamIds = new ArrayList<>();
                    teamIds.add(contest.getHomeTeam());
                    teamIds.add(contest.getAwayTeam());
                    Map<Long, BbTeam> teams = bbTeamDao.findBbTeamMapByIds(teamIds);
                    contestResponse = ContestTransformUtil.transformBbContest(contest, teams.get(contest.getHomeTeam()),
                            teams.get(contest.getAwayTeam()), new int[]{0, 0}, true);

                    typeTmp = type;
                    contestIdTmp = contestId;
                }
                bunchOptionsResponse.setContestResponse(contestResponse);
                // 塞入盘口
                if (playType == PlayType.BB_JC.getId()) {
                    OddsJc jc = bbOddsJcDao.selectByContest(contestId);
                    bunchOptionsResponse.setHandicap(jc.getHandicap());
                } else if (playType == PlayType.BB_SIZE.getId()) {
                    OddsSize oddsSize = bbOddsSizeDao.selectByContest(contestId);
                    bunchOptionsResponse.setHandicap(oddsSize.getHandicap());
                }
                // 塞入赔率
                // 设计可能不需要显示赔率，注释掉
                // if (playType == PlayType.BB_SPF.getId()){
                // OddsOp op = bbOddsOpDao.selectByContest(contestId);
                // bunchOptionsResponse.setOddsOpResponse(OddsTransformUtil.transformOddsOp(op));
                // } else if (playType == PlayType.BB_JC.getId()){
                // OddsJc jc = bbOddsJcDao.selectByContest(contestId);
                // bunchOptionsResponse.setOddsJcResponse(OddsTransformUtil.transformOddsJc(jc));
                // } else if (playType == PlayType.BB_SIZE.getId()){
                // OddsSize oddsSize = bbOddsSizeDao.selectByContest(contestId);
                // bunchOptionsResponse.setOddsSizeResponse(OddsTransformUtil.transformOddsSize(oddsSize));
                // } else if (playType == PlayType.BB_ODDEVEN.getId()){
                // OddsDssResponse oddsDssResponse =
                // BbOddsDssDateUtil.getInstance().getDssResponse();
                // bunchOptionsResponse.setOddsDssResponse(oddsDssResponse);
                // }
            }
        }
        bunchContestResponse.setOptions(optionsList);
        // 塞入剩下的数据
        bunchContestResponse.setId(bunchContest.getId());
        bunchContestResponse.setName(bunchContest.getName());
        bunchContestResponse.setResult(bunchContest.getResult());
        bunchContestResponse.setImage(bunchContest.getImage());
        if (bunchContest.getCost() != 0) {
            bunchContestResponse.setCost(bunchContest.getCost());
            bunchContestResponse.setLongbi(bunchContest.getLongbi());
        }
        // bunchContestResponse.setCreate_time(CbsTimeUtils.getUtcTimeForDate(bunchContest.getCreateTime()));
        bunchContestResponse.setStart_time(CbsTimeUtils.getUtcTimeForDate(bunchContest.getStartTime()));
        bunchContestResponse.setEnd_time(CbsTimeUtils.getUtcTimeForDate(bunchContest.getEndTime()));
        return bunchContestResponse;
    }

    // 填充数据
    private BunchContestResponse toResponseInner(BunchContest bunchContest, ObjectMapper mapper) throws IOException {
        BunchContestResponse bunchContestResponse = new BunchContestResponse();
        // 解析出赛事
        List<BunchOptionsResponse> optionsList = mapper.readValue(bunchContest.getOptions(),
                new TypeReference<List<BunchOptionsResponse>>() {
                });
        bunchContestResponse.setOptions(optionsList);
        // 塞入剩下的数据
        bunchContestResponse.setName(bunchContest.getName());
        bunchContestResponse.setImage(bunchContest.getImage());
        bunchContestResponse.setCost(bunchContest.getCost());
        bunchContestResponse.setLongbi(bunchContest.getLongbi());
        bunchContestResponse.setStart_time(CbsTimeUtils.getUtcTimeForDate(bunchContest.getStartTime()));
        bunchContestResponse.setEnd_time(CbsTimeUtils.getUtcTimeForDate(bunchContest.getEndTime()));
        return bunchContestResponse;
    }

    private void getPrize(Long id, BunchContestResponse bunchContestResponse) {
        List<BunchPrize> bunchPrizeList = bunchPrizeDao.selectByBunchId(id);
        List<BunchPrizeResponse> prize = new ArrayList<>();
        if (bunchPrizeList.size() > 0) {
            for (BunchPrize bunchPrize : bunchPrizeList) {
                toPrize(prize, bunchPrize);
            }
        }
        bunchContestResponse.setPrize(prize);
    }

    private void toPrize(List<BunchPrizeResponse> prize, BunchPrize bunchPrize) {
        BunchPrizeResponse bunchPrizeResponse = new BunchPrizeResponse();
        bunchPrizeResponse.setId(bunchPrize.getId());
        bunchPrizeResponse.setName(bunchPrize.getName());
        bunchPrizeResponse.setPrice(bunchPrize.getPrice());
        bunchPrizeResponse.setType(bunchPrize.getType());
        bunchPrizeResponse.setWin_num(bunchPrize.getWinNum());
        bunchPrizeResponse.setNum(bunchPrize.getNum());
        bunchPrizeResponse.setImage(bunchPrize.getImage());
        prize.add(bunchPrizeResponse);
    }
}
