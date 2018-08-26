package com.lifeix.cbs.content.impl.game;

import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.cbs.api.common.exception.MsgCode;
import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.api.common.util.ContentConstants;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.service.coupon.CouponUserService;
import com.lifeix.cbs.api.service.user.CbsUserService;
import com.lifeix.cbs.content.bean.game.ZodiacAnimalListResponse;
import com.lifeix.cbs.content.bean.game.ZodiacAnimalResponse;
import com.lifeix.cbs.content.bean.game.ZodiacPlayListResponse;
import com.lifeix.cbs.content.bean.game.ZodiacPlayResponse;
import com.lifeix.cbs.content.dao.game.ZodiacAnimalBetDao;
import com.lifeix.cbs.content.dao.game.ZodiacAnimalDao;
import com.lifeix.cbs.content.dto.game.ZodiacAnimal;
import com.lifeix.cbs.content.dto.game.ZodiacAnimalBet;
import com.lifeix.cbs.content.service.game.ZodiacAnimalIssueService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;
import com.lifeix.framework.memcache.MemcacheService;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service("zodiacAnimalIssueService")
public class ZodiacAnimalIssueServiceImpl extends ImplSupport implements ZodiacAnimalIssueService {

    // 最近中奖的记录
    private static final String RECENT_WIN = "recent_win";

    // 最近中奖的记录最多显示的数量
    private static final long RECENT_WIN_LIMIT = 14;

    // 连续中奖和未中奖生肖统计
    private static final String ZODIAC_COUNT = "zodiac_count";

    // 最近中奖生肖
    private final String ZODIAC_RECENT = this.getClass().getName() + "zodiac_recent";

    // 过期时间（秒）
    private static final int OUT_TIME = 300;

    // 返奖的倍率
    private static final int BACK = 3;

    // 结算
    private final String SETTLE_LOCK = this.getClass().getName() + ":settle";

    // 结算
    private final String WINNER_LOCK = this.getClass().getName() + ":winner";

    // 当前期统计每个生肖下单数
    private final String ZODIAC_STATISTIC = this.getClass().getName() + ":statistics";

    @Resource(name = "srt")
    private StringRedisTemplate srt;

    @Autowired
    private MemcacheService memcacheService;

    @Autowired
    private ZodiacAnimalDao zodiacAnimalDao;

    @Autowired
    private ZodiacAnimalBetDao zodiacAnimalBetDao;
    @Autowired
    private CbsUserService cbsUserService;

    @Autowired
    private CouponUserService couponUserService;

    @Override
    public ZodiacAnimalResponse getNowIssue(Long userId, boolean isLongbi) throws L99IllegalParamsException, JSONException,
            L99NetworkException, L99IllegalDataException {
        ParamemeterAssert.assertDataNotNull(userId);
        String allZodiacKey = getCacheId(ZODIAC_COUNT);
        ZSetOperations<String, String> zso = srt.opsForZSet();

        ZodiacAnimalResponse zodiacAnimalResponse = new ZodiacAnimalResponse();
        Date now = new Date();
        ZodiacAnimal zodiacAnimal = zodiacAnimalDao.findOne(now, null);
        if (zodiacAnimal == null) {
            LOG.info("zodiacAnimal is null!!!");
            return zodiacAnimalResponse;
        }
        //离本期截止时间还有多少秒
        long interal = zodiacAnimal.getEndTime().getTime() - now.getTime();
        int second = (int) (interal / 1000);


        // 获取上一期结果
        ZodiacAnimal zodiacAnimalLast = zodiacAnimalDao.findOne(null, zodiacAnimal.getId() - 1);

        // 最近中奖的人记录
        String allRecentKey = getCacheId(RECENT_WIN);
        Set<String> sets = zso.reverseRangeByScore(allRecentKey, 0, Double.MAX_VALUE, 0, RECENT_WIN_LIMIT);

        // 塞入数据
        if (zodiacAnimalLast != null) {
            zodiacAnimalResponse.setPre_id(zodiacAnimalLast.getId());
            zodiacAnimalResponse.setPre_lottery(zodiacAnimalLast.getLottery());
        }
        if (zodiacAnimal != null) {
            zodiacAnimalResponse.setId(zodiacAnimal.getId());
            zodiacAnimalResponse.setStart_time(CbsTimeUtils.getUtcTimeForDate(zodiacAnimal.getStartTime()));
            zodiacAnimalResponse.setEnd_time(CbsTimeUtils.getUtcTimeForDate(zodiacAnimal.getEndTime()));
        }

        zodiacAnimalResponse.setWinning_ad(sets.toArray());
        Set<String> set = zso.range(allZodiacKey, Long.MIN_VALUE, Long.MAX_VALUE);
        Object[] setObject = set.toArray();
        Object[] set1 = {"1,-10", "2,-8", "3,-2"};
        Object[] set2 = {"10,2", "11,3", "12,5"};
        for (int i = 0; i < setObject.length; i++) {
            if (i < 3) {
                double score = zso.score(allZodiacKey, setObject[i]);
                set1[i] = setObject[i] + "," + score;
            } else if (i > 8) {
                double score = zso.score(allZodiacKey, setObject[i]);
                set2[i - 9] = setObject[i] + "," + score;
            }
        }
        // 获得旺气生肖
        zodiacAnimalResponse.setQuality_ad(set2);
        // 获得潜力生肖
        zodiacAnimalResponse.setPotential_ad(set1);
        // 获得最近中最多的生肖
        ValueOperations<String, String> voSrt = srt.opsForValue();
        Stack<Integer> stack = new Stack<Integer>();
        // 最近中奖次数第三多的序号
        int no3 = 0;
        // 最近中奖次数第二多的序号
        int no2 = 0;
        // 最近中奖次数最多的序号
        int no1 = 0;
        for (int i = 1; i < 13; i++) {
            String tmp = voSrt.get(ZODIAC_RECENT + i);
            switch (i) {
                case 1:
                    no1 = i;
                    if (tmp != null) {
                        stack.push(Integer.valueOf(tmp));
                    } else {
                        stack.push(1);
                    }
                    break;
                case 2:
                    no2 = i;
                    if (tmp != null) {
                        stack.push(Integer.valueOf(tmp));
                    } else {
                        stack.push(1);
                    }
                    break;
                case 3:
                    no3 = i;
                    if (tmp != null) {
                        stack.push(Integer.valueOf(tmp));
                    } else {
                        stack.push(1);
                    }
                    break;
                default:
                    if (tmp != null) {
                        int n = Integer.valueOf(tmp);
                        if (n > stack.peek()) {
                            stack.pop();
                            if (n > stack.peek()) {
                                int med = stack.pop();
                                if (n > stack.peek()) {
                                    int max = stack.pop();
                                    stack.push(n);
                                    stack.push(max);
                                    stack.push(med);
                                    no3 = no2;
                                    no2 = no1;
                                    no1 = i;
                                } else {
                                    stack.push(n);
                                    stack.push(med);
                                    no3 = no2;
                                    no2 = i;
                                }
                            } else {
                                stack.push(n);
                                no3 = i;
                            }
                        }
                    }
            }
        }

        // 获取用户可用的龙筹券信息
        List<Integer> setPrice = new ArrayList<Integer>();
        setPrice.add(5);
        setPrice.add(10);
        setPrice.add(20);
        List<Object[]> list = couponUserService.findUserZodiacGameBetCoupons(userId, setPrice);
        zodiacAnimalResponse.setUser_coupon(list);
        //查找用户信息
        CbsUserResponse cbsUserResponse = cbsUserService.getCbsUserByUserId(userId, false);
        zodiacAnimalResponse.setUser(cbsUserResponse);
        zodiacAnimalResponse.setSecond(second);
        int result3 = stack.pop();
        int result2 = stack.pop();
        int result1 = stack.pop();
        Object[] set3 = {no1 + "," + result1, no2 + "," + result2, no3 + "," + result3};
        zodiacAnimalResponse.setRecent_ad(set3);

        return zodiacAnimalResponse;
    }

    @Override
    public ZodiacPlayResponse bet(Long userId, Integer gameId, String bets, String userCouponIds, boolean longbi)
            throws L99IllegalParamsException, L99NetworkException, L99IllegalDataException, JSONException {
        // 检验参数，非空，以及合法性，用户存在且没被屏蔽等
        ParamemeterAssert.assertDataNotNull(userId);
        ParamemeterAssert.assertDataNotNull(gameId);
        ParamemeterAssert.assertDataNotNull(userCouponIds);
        CbsUserResponse cbsUserResponse = cbsUserService.getCbsUserByUserId(userId, false, true);
        // 用户被屏蔽
        if (cbsUserResponse == null) {
            throw new L99IllegalParamsException(MsgCode.UserMsg.CODE_USER_ACCOUNT_BLOCK,
                    MsgCode.UserMsg.KEY_USER_ACCOUNT_BLOCK);
        }
        ZodiacAnimal zodiacAnimal = zodiacAnimalDao.findOne(null, gameId);
        // 本期游戏已结束，停止下单
        if (new Date().getTime() >= zodiacAnimal.getEndTime().getTime()) {
            throw new L99IllegalParamsException(MsgCode.ZodiacMsg.CODE_GAME_ZODIAC_OPEN_NOT_PLAY,
                    MsgCode.ZodiacMsg.KEY_GAME_ZODIAC_OPEN_NOT_PLAY);
        }
        // 本期游戏未开始，不能下单
        if (new Date().getTime() < zodiacAnimal.getStartTime().getTime()) {
            throw new L99IllegalParamsException(MsgCode.ZodiacMsg.CODE_GAME_ZODIAC_NOT_OPEN,
                    MsgCode.ZodiacMsg.KEY_GAME_ZODIAC_NOT_OPEN);
        }

        // 消费龙筹券
        String desc = String.format("第%d期生肖抢红包下单，花费龙筹券id为：%s", gameId, userCouponIds);
        int totalGold = couponUserService.useManyCoupons(userCouponIds, userId, desc);

        ZodiacAnimalBet zodiacAnimalBet = new ZodiacAnimalBet();
        zodiacAnimalBet.setGameId(gameId);
        zodiacAnimalBet.setUserId(userId);
        zodiacAnimalBet.setBet(bets);
        zodiacAnimalBet.setBetSum(totalGold);
        zodiacAnimalBet.setBackSum(0);
        zodiacAnimalBet.setIsLongbi(longbi);
        zodiacAnimalBet.setCreateTime(new Date());
        zodiacAnimalBet.setUpdateTime(new Date());

        ZodiacAnimalBet zodiacAnimalBet1 = zodiacAnimalBetDao.insert(zodiacAnimalBet);
        ZodiacPlayResponse zodiacPlayResponse = new ZodiacPlayResponse();
        zodiacPlayResponse.setGame_id(zodiacAnimalBet1.getGameId());

        return zodiacPlayResponse;
    }

    @Override
    public ZodiacAnimalResponse getCurrentWinner(Integer gameId) throws L99IllegalParamsException, L99NetworkException,
            L99IllegalDataException, JSONException {
        // 参数检验
        ParamemeterAssert.assertDataNotNull(gameId);
        ZodiacAnimalResponse zodiacAnimalResponse = new ZodiacAnimalResponse();
        // 获取gameId期信息
        Date now = new Date();
        ZodiacAnimal zodiacAnimal = zodiacAnimalDao.findOne(null, gameId);
        Date startTime = zodiacAnimal.getStartTime();
        long inter = (now.getTime() - startTime.getTime()) / 1000;
        String lottery = zodiacAnimal.getLottery();
        if (StringUtils.isBlank(lottery) && inter > 25 && inter < 50) {
            try {
                Integer win = memcacheService.get(WINNER_LOCK);
                if (win == null) {
                    memcacheService.set(WINNER_LOCK, 1);
                    // 为空
                    String tmp = "";
                    tmp = tmp + getRandomNum() + ",";
                    tmp = tmp + getRandomNum() + ",";
                    tmp = tmp + getRandomNum();
                    zodiacAnimal.setLottery(tmp);
                    zodiacAnimalDao.update(gameId, ContentConstants.ZodiacAnimalStatus.INIT, tmp);
                    zodiacAnimalResponse.setLottery(tmp);
                    zodiacAnimalResponse.setId(zodiacAnimal.getId());
                }
            } finally {
                memcacheService.delete(WINNER_LOCK);
            }

        } else {
            zodiacAnimalResponse.setLottery(lottery);
            zodiacAnimalResponse.setId(zodiacAnimal.getId());
        }
        // 返回中奖response
        return zodiacAnimalResponse;
    }

    private int getRandomNum() {
        Random r = new Random();
        int i = r.nextInt(12);
        return i + 1;
    }

    @Override
    public void toPrize(String startTime, String endTime) throws L99IllegalParamsException, L99IllegalDataException,
            L99NetworkException, JSONException {
        List<ZodiacAnimal> list = zodiacAnimalDao.findNoLotteryList(startTime, endTime);
        for (ZodiacAnimal z : list) {
            toPrize(z.getId());
        }
    }

    public ZodiacAnimalResponse toPrize(Integer gameId) throws L99IllegalParamsException, JSONException,
            L99IllegalDataException, L99NetworkException {

        // 参数检验
        ParamemeterAssert.assertDataNotNull(gameId);
        ZodiacAnimalResponse zodiacAnimalResponse = new ZodiacAnimalResponse();
        // 查看是否派奖
        Integer gameInteger = memcacheService.get(SETTLE_LOCK);
        // 为null就派奖
        if (gameInteger == null) {
            try {
                memcacheService.set(SETTLE_LOCK, gameId);

                ZodiacAnimal zodiacAnimal = zodiacAnimalDao.findOne(null, gameId);
                if (zodiacAnimal.getStatus() == ContentConstants.ZodiacAnimalStatus.LOTTERY) {
                    // 删除锁
                    memcacheService.delete(SETTLE_LOCK);
                    // 本期游戏已结算
                    throw new L99IllegalParamsException(MsgCode.ZodiacMsg.CODE_GAME_ZODIAC_SETTLE,
                            MsgCode.ZodiacMsg.KEY_GAME_ZODIAC_SETTLE);
                } else {
                    if (StringUtils.isEmpty(zodiacAnimal.getLottery())) {
                        String tmp = "";
                        tmp = tmp + getRandomNum() + ",";
                        tmp = tmp + getRandomNum() + ",";
                        tmp = tmp + getRandomNum();
                        zodiacAnimal.setLottery(tmp);
                    }

                }

                List<ZodiacAnimalBet> zodiacAnimalBets = zodiacAnimalBetDao.findZodiacAnimalBetsByGameId(gameId);
                String[] lotteryStrs = zodiacAnimal.getLottery().split(",");
                int[] lotterys = new int[3];
                for (int i = 0; i < lotteryStrs.length; i++) {
                    lotterys[i] = Integer.valueOf(lotteryStrs[i]);
                }
                ZSetOperations<String, String> zso = srt.opsForZSet();
                String allRecentKey = getCacheId(RECENT_WIN);
                int lotteryNum = 0;
                // 给用户派奖
                for (ZodiacAnimalBet zodiacAnimalBet : zodiacAnimalBets) {
                    int status = zodiacAnimalBet.getStatus();
                    if (status == ContentConstants.ZodiacAnimalStatus.INIT) {
                        // 查询用户
                        CbsUserResponse cbsUserResponse = cbsUserService.getCbsUserByUserId(zodiacAnimalBet.getUserId(),
                                false);
                        // 下单内容
                        String betStr = zodiacAnimalBet.getBet();
                        if (betStr == null) {
                            continue;
                        }
                        String[] bets = betStr.split(";");
                        // 返奖金额
                        double backGold = 0;
                        // 状态
                        for (String bet : bets) {
                            String[] tmpStr = bet.split(",");
                            // 下单号码
                            int no = Integer.valueOf(tmpStr[0]);
                            // 中奖号码轮询
                            for (int lottery : lotterys) {
                                if (no == lottery) {
                                    // 中奖了，进行派奖
                                    // 下单金额
                                    int buyGold = Integer.valueOf(tmpStr[1]);
                                    // 返奖金额
                                    backGold += buyGold * BACK;
                                    // 中奖信息塞到redis中，只塞最新的14个
                                    long allKeyCount = zso.zCard(allRecentKey);
                                    if (lotteryNum <= RECENT_WIN_LIMIT) {
                                        lotteryNum++;
                                        if (allKeyCount > RECENT_WIN_LIMIT) {
                                            zso.removeRange(allRecentKey, 0, 0);
                                        }
                                        String name = cbsUserResponse.getName();
                                        int length = name.length();
                                        if (length > 1) {
                                            name = name.substring(0, 1) + "*" + name.substring(length - 2, length - 1);
                                        }
                                        zso.add(allRecentKey, name + "中了" + (buyGold * BACK - buyGold) + "龙筹",
                                                Double.parseDouble(zodiacAnimalBet.getId().toString()));
                                    }
                                    // 派奖
                                    if (buyGold == 20){
                                        couponUserService.sendCouponToUser(13L, zodiacAnimalBet.getUserId(), BACK, "生肖游戏中奖");
                                    } else if (buyGold == 10){
                                        couponUserService.sendCouponToUser(8L, zodiacAnimalBet.getUserId(), BACK, "生肖游戏中奖");
                                    } else if (buyGold == 5){
                                        couponUserService.sendCouponToUser(3L, zodiacAnimalBet.getUserId(), BACK, "生肖游戏中奖");
                                    }
                                }
                            }
                        }
                        if (backGold > 0) {
//                            // 派奖
//                            if (backGold % 60 == 0){
//                                int num = (backGold / 60)*3 ;
//                                couponUserService.sendCouponToUser(13L, zodiacAnimalBet.getUserId(), num, "生肖游戏中奖");
//                            } else if (backGold % 30 == 0){
//                                int num = (backGold / 30)*3 ;
//                                couponUserService.sendCouponToUser(8L, zodiacAnimalBet.getUserId(), num, "生肖游戏中奖");
//                            } else if (backGold % 15 == 0){
//                                int num = (backGold / 15)*3 ;
//                                couponUserService.sendCouponToUser(3L, zodiacAnimalBet.getUserId(), num, "生肖游戏中奖");
//                            } else {
//                                couponUserService.settleCouponByBack(zodiacAnimalBet.getUserId(), (double)backGold, "用户玩抢红包中奖");
//                            }
                            status = ContentConstants.ZodiacAnimalStatus.LOTTERY;
                        } else {
                            status = ContentConstants.ZodiacAnimalStatus.UN_LOTTERY;
                        }
                        // 更新下单记录
                        zodiacAnimalBetDao.update(zodiacAnimalBet.getId(), backGold, status);
                    }
                }

                String allZodiacKey = getCacheId(ZODIAC_COUNT);
                ValueOperations<String, String> voSrt = srt.opsForValue();
                // 统计旺气生肖/潜力生肖/最近中奖生肖
                for (int i = 1; i < 13; i++) {
                    Double tmpI = zso.score(allZodiacKey, String.valueOf(i));
                    if (tmpI == null) {
                        if (i == lotterys[0] || i == lotterys[1] || i == lotterys[2]) {
                            // 生肖抽中
                            zso.add(allZodiacKey, String.valueOf(i), 1.00);
                            // 统计最近
                            voSrt.increment(ZODIAC_RECENT + i, 1);
                            srt.expire(ZODIAC_RECENT + i, OUT_TIME, TimeUnit.SECONDS);
                        } else {
                            // 生肖不抽中
                            zso.add(allZodiacKey, String.valueOf(i), -1.00);
                        }
                    } else {
                        if (i == lotterys[0] || i == lotterys[1] || i == lotterys[2]) {
                            // 生肖抽中
                            if (tmpI < 0) {
                                zso.add(allZodiacKey, String.valueOf(i), 1.00);
                            } else {
                                zso.add(allZodiacKey, String.valueOf(i), tmpI + 1.00);
                            }
                            // 统计最近
                            voSrt.increment(ZODIAC_RECENT + i, 1);
                            srt.expire(ZODIAC_RECENT + i, OUT_TIME, TimeUnit.SECONDS);
                        } else {
                            // 生肖不抽中
                            if (tmpI < 0) {
                                zso.add(allZodiacKey, String.valueOf(i), tmpI - 1.00);
                            } else {
                                zso.add(allZodiacKey, String.valueOf(i), -1.00);
                            }
                        }
                    }
                }
                // 设置该期游戏已结算（已处理）
                String lottery = zodiacAnimal.getLottery();
                if (zodiacAnimalDao.update(gameId, ContentConstants.ZodiacAnimalStatus.LOTTERY, lottery)) {
                    zodiacAnimalResponse.setLottery(lottery);

                }
                return zodiacAnimalResponse;
            } finally {
                memcacheService.delete(SETTLE_LOCK);
            }
        }
        return zodiacAnimalResponse;
    }

    @Override
    public ZodiacPlayListResponse getUserPlayHistorys(Long userId, Integer startId, Integer limit) {
        List<ZodiacAnimalBet> zodiacAnimalBetList = zodiacAnimalBetDao.findZodiacAnimalBets(userId, startId, limit, null);
        ZodiacPlayListResponse zodiacPlayListResponse = null;
        if (zodiacAnimalBetList != null) {
            zodiacPlayListResponse = new ZodiacPlayListResponse();
            List<ZodiacPlayResponse> zodiacPlayResponseList = new ArrayList<ZodiacPlayResponse>();
            ZodiacPlayResponse zodiacPlayResponse;
            Long backStartId = null;
            for (ZodiacAnimalBet zodiacAnimalBet : zodiacAnimalBetList) {
                zodiacPlayResponse = new ZodiacPlayResponse();
                backStartId = Long.valueOf(zodiacAnimalBet.getId());
                zodiacPlayResponse.setGame_id(zodiacAnimalBet.getGameId());
                zodiacPlayResponse.setBack_sum(zodiacAnimalBet.getBackSum());
                zodiacPlayResponse.setBet(zodiacAnimalBet.getBet());
                zodiacPlayResponse.setBet_sum(zodiacAnimalBet.getBetSum());
                zodiacPlayResponse.setLongbi(zodiacAnimalBet.getIsLongbi());
                zodiacPlayResponse.setStatus(zodiacAnimalBet.getStatus());
                ZodiacAnimal zodiacAnimal = zodiacAnimalDao.findOne(null, zodiacAnimalBet.getGameId());
                if (zodiacAnimal != null) {
                    zodiacPlayResponse.setLottery(zodiacAnimal.getLottery());
                }
                zodiacPlayResponseList.add(zodiacPlayResponse);

            }
            zodiacPlayListResponse.setPlays(zodiacPlayResponseList);
            zodiacPlayListResponse.setStartId(backStartId);
        }
        return zodiacPlayListResponse;
    }

    @Override
    public ZodiacAnimalListResponse getPlayHistorys(Integer startId, Integer limit) {
        ZodiacAnimalListResponse zodiacAnimalListResponse = new ZodiacAnimalListResponse();
        List<ZodiacAnimalResponse> zodiacs = new ArrayList<ZodiacAnimalResponse>();
        List<ZodiacAnimal> zodiacAnimalList = zodiacAnimalDao.findZodiacs(startId, limit);
        ZodiacAnimalResponse zodiacAnimalResponse;
        if (zodiacAnimalList != null) {
            Long backStartId = null;
            for (ZodiacAnimal zodiacAnimal : zodiacAnimalList) {
                zodiacAnimalResponse = new ZodiacAnimalResponse();
                backStartId = Long.valueOf(zodiacAnimal.getId());
                zodiacAnimalResponse.setId(zodiacAnimal.getId());
                zodiacAnimalResponse.setLottery(zodiacAnimal.getLottery());
                zodiacs.add(zodiacAnimalResponse);
            }
            zodiacAnimalListResponse.setZodiacs(zodiacs);
            zodiacAnimalListResponse.setStartId(backStartId);
        }
        ZSetOperations<String, String> zso = srt.opsForZSet();
        String allZodiacKey = getCacheId(ZODIAC_COUNT);

        Set<String> set = zso.range(allZodiacKey, Long.MIN_VALUE, Long.MAX_VALUE);
        Object[] setObject = set.toArray();
        Object[] set1 = {"1", "2", "3"};
        Object[] set2 = {"10", "11", "12"};
        for (int i = 0; i < setObject.length; i++) {
            if (i < 3) {
                set1[i] = setObject[i];
            } else if (i > 8) {
                set2[i - 9] = setObject[i];
            }
        }
        // 获得旺气生肖
        zodiacAnimalListResponse.setQuality_ad(set2);
        // 获得潜力生肖
        zodiacAnimalListResponse.setPotential_ad(set1);
        return zodiacAnimalListResponse;
    }

    @Override
    public void changeBetNum(Integer gameId, String zodiacId, Long num) {
        String[] tmp = zodiacId.split(",");
        for (String s : tmp) {
            srt.opsForHash().increment(ZODIAC_STATISTIC + gameId, s, num);
        }
        srt.expire(ZODIAC_STATISTIC + gameId, 600, TimeUnit.SECONDS);
    }

    @Override
    public ZodiacAnimalResponse getBetNum(Integer gameId) {

        List<Object> list = new ArrayList<Object>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");
        list.add("9");
        list.add("10");
        list.add("11");
        list.add("12");

        List<Object> list1 = srt.opsForHash().multiGet(ZODIAC_STATISTIC + gameId, list);
        ZodiacAnimalResponse zodiacAnimalResponse = new ZodiacAnimalResponse();
        Integer[] statistic = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        for (int i = 0; i < list1.size(); i++) {
            Object tmp = list1.get(i);
            if (tmp != null) {
                statistic[i + 1] = Integer.valueOf(tmp.toString());
            }
        }
        zodiacAnimalResponse.setBet_num(statistic);
        zodiacAnimalResponse.setId(gameId);

        return zodiacAnimalResponse;
    }

}
