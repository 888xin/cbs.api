package com.lifeix.cbs.contest.impl.bunch;

import com.lifeix.cbs.api.bean.coupon.CouponUserResponse;
import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.cbs.api.common.exception.MsgCode;
import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.api.common.util.ContestConstants;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.common.util.RedisConstants;
import com.lifeix.cbs.api.service.coupon.CouponUserService;
import com.lifeix.cbs.api.service.money.MoneyService;
import com.lifeix.cbs.api.service.money.MoneyStatisticService;
import com.lifeix.cbs.api.service.user.CbsUserService;
import com.lifeix.cbs.contest.bean.bunch.BunchBetListResponse;
import com.lifeix.cbs.contest.bean.bunch.BunchBetResponse;
import com.lifeix.cbs.contest.bean.bunch.BunchPrizeResponse;
import com.lifeix.cbs.contest.dao.bunch.BunchBetDao;
import com.lifeix.cbs.contest.dao.bunch.BunchContestDao;
import com.lifeix.cbs.contest.dao.bunch.BunchPrizeDao;
import com.lifeix.cbs.contest.dto.bunch.BunchBet;
import com.lifeix.cbs.contest.dto.bunch.BunchContest;
import com.lifeix.cbs.contest.dto.bunch.BunchPrize;
import com.lifeix.cbs.contest.impl.ImplSupport;
import com.lifeix.cbs.contest.service.bunch.BunchBetService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.framework.redis.impl.RedisDataIdentify;
import com.lifeix.framework.redis.impl.RedisStringHandler;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lhx on 16-5-18 上午10:05
 *
 * @Description
 */
@Service("bunchBetService")
public class BunchBetServiceImpl extends ImplSupport implements BunchBetService {

    @Autowired
    private BunchContestDao bunchContestDao ;

    @Autowired
    private BunchBetDao bunchBetDao ;

    @Autowired
    private BunchPrizeDao bunchPrizeDao ;

    @Autowired
    private MoneyService moneyService ;

    @Autowired
    private CbsUserService cbsUserService ;

    @Autowired
    private CouponUserService couponUserService ;

    @Autowired
    private MoneyStatisticService moneyStatisticService ;

    @Autowired
    private RedisStringHandler redisStringHandler;

    @Override
    public void insert(Long userId, Long bunchContestId, String supports, Long userCouponId) throws L99IllegalParamsException, IOException, JSONException, L99IllegalDataException {
        ParamemeterAssert.assertDataNotNull(userId, bunchContestId, supports);

        //校验，用户是否下过单
        BunchBet bunchBet = bunchBetDao.selectByUser(bunchContestId, userId);
        if (bunchBet != null){
            //已下过单
            throw new L99IllegalParamsException(MsgCode.BetMsg.CODE_BET_HAS, MsgCode.BetMsg.KEY_BET_HAS);
        }
        //校验，格式
        ObjectMapper mapper = new ObjectMapper();
        Integer[] supportOptions = mapper.readValue(supports, new TypeReference<Integer[]>() {
        });
        BunchContest bunchContest = bunchContestDao.selectById(bunchContestId);
        if (bunchContest == null){
            //赛事不存在
            throw new L99IllegalParamsException(MsgCode.ContestMsg.CODE_CONTEST_NOT_EXIST, MsgCode.ContestMsg.KEY_CONTEST_NOT_EXIST);
        }
        JSONArray jsonArray = new JSONArray(bunchContest.getOptions());
        if (supportOptions.length != jsonArray.length()){
            //可允许选择的比赛数目不符合
            throw new L99IllegalParamsException(MsgCode.BetMsg.CODE_BET_NUM_NOT_SUPPORT, MsgCode.BetMsg.KEY_BET_NUM_NOT_SUPPORT);
        }
        //校验，比赛时间
        if (bunchContest.getStartTime().getTime() < new Date().getTime()){
            //停止下注
            throw new L99IllegalParamsException(MsgCode.BetMsg.CODE_BET_CONTEST_BEGIN, MsgCode.BetMsg.KEY_BET_CONTEST_BEGIN);
        }
        //校验，该串是否要钱，要钱的话，扣用户钱
        int cost = bunchContest.getCost();
        if (cost > 0){
            if (bunchContest.getLongbi()){
                //扣龙币
                moneyService.consumeMoney(userId, (double)bunchContest.getCost(), "下单活动串", null);
                moneyStatisticService.insertUserConsumer(userId + "", -(double)bunchContest.getCost());
            } else {
                //扣筹码
                if (userCouponId == null) {
                    //龙筹余额不足
                    throw new L99IllegalParamsException(MsgCode.BasicMsg.CODE_NOT_GOLD, MsgCode.BasicMsg.KEY_NOT_GOLD);
                } else {
                    // 检查龙筹券是否有效
                    CouponUserResponse couponUser = couponUserService.findCouponDetail(userCouponId, true);

                    // 判断龙筹券是否是自己拥有
                    if (!userId.equals(couponUser.getUser_id())) {
                        throw new L99IllegalDataException(MsgCode.CouponMsg.CODE_COUPON_FAIL, MsgCode.CouponMsg.KEY_COUPON_FAIL);
                    }
                    //面值不相等
                    if (couponUser.getPrice() != cost){
                        throw new L99IllegalDataException(MsgCode.CouponMsg.CODE_COUPON_FAIL_NOT_RANGE, MsgCode.CouponMsg.KEY_COUPON_FAIL_NOT_RANGE);
                    }
                }
                couponUserService.useManyCoupons(userCouponId + "", userId, "活动串消耗");
            }
        }
        bunchBet = new BunchBet();
        bunchBet.setBunchId(bunchContestId);
        bunchBet.setUserId(userId);
        bunchBet.setSupport(supports);
        boolean flag = bunchBetDao.insert(bunchBet);
        if (flag){
            //人数记录+1
            RedisDataIdentify betIdentify = new RedisDataIdentify(RedisConstants.BunchRedis.BET_RECORDS, bunchContestId+"");
            redisStringHandler.incr(betIdentify);
        }
    }

    @Override
    public BunchBetListResponse getList(Long userId, Long startId, int limit) throws L99IllegalParamsException {
        ParamemeterAssert.assertDataNotNull(userId);
        BunchBetListResponse bunchBetListResponse = new BunchBetListResponse();
        List<BunchBetResponse> bets = new ArrayList<>();
        List<BunchBet> bunchBetList = bunchBetDao.getList(userId, startId, limit);
        if (bunchBetList.size() > 0){
            for (BunchBet bunchBet : bunchBetList) {
                BunchBetResponse bunchBetResponse = getBunchBetResponse(bunchBet);
                bets.add(bunchBetResponse);
            }
        }
        bunchBetListResponse.setBets(bets);
        return bunchBetListResponse;
    }

    @Override
    public BunchBetListResponse getAwardsList(Long bunchId, Integer status, int page, int limit) throws L99IllegalParamsException {
        ParamemeterAssert.assertDataNotNull(bunchId);
        BunchBetListResponse bunchBetListResponse = new BunchBetListResponse();
        List<BunchBetResponse> bets = new ArrayList<>();
        int skip = page > 0 ? (page - 1) * limit : 0 ;
        List<BunchBet> bunchBetList = bunchBetDao.getAwardsList(bunchId, status, skip, limit);
        if (bunchBetList.size() > 0){

            //查找用户信息
            List<Long> userList = new ArrayList<>();
            for (BunchBet bunchBet : bunchBetList) {
                userList.add(bunchBet.getUserId());
            }
            Map<Long, CbsUserResponse> userResponseMap = cbsUserService.findCsAccountMapByIds(userList);

            for (BunchBet bunchBet : bunchBetList) {
                BunchBetResponse bunchBetResponse = getBunchBetResponse(bunchBet);
                bunchBetResponse.setUser(userResponseMap.get(bunchBet.getUserId()));
                bets.add(bunchBetResponse);
            }
            //设置页数
            bunchBetListResponse.setPage_num(bunchBetList.size() < limit ? -1 : (page + 1));
            //查询奖品
            List<BunchPrize> bunchPrizeList = bunchPrizeDao.selectByBunchId(bunchId);
            List<BunchPrizeResponse> prize = new ArrayList<>();
            if (bunchPrizeList.size() > 0){
                for (BunchPrize bunchPrize : bunchPrizeList) {
                    BunchPrizeResponse bunchPrizeResponse = new BunchPrizeResponse();
                    bunchPrizeResponse.setName(bunchPrize.getName());
                    bunchPrizeResponse.setPrice(bunchPrize.getPrice());
                    bunchPrizeResponse.setImage(bunchPrize.getImage());
                    bunchPrizeResponse.setWin_num(bunchPrize.getWinNum());
                    prize.add(bunchPrizeResponse);
                }
            }
            bunchBetListResponse.setPrize(prize);
        } else {
            bunchBetListResponse.setPage_num(-1);
        }


        bunchBetListResponse.setBets(bets);
        return bunchBetListResponse;
    }


    private BunchBetResponse getBunchBetResponse(BunchBet bunchBet) {
        BunchBetResponse bunchBetResponse = new BunchBetResponse();
        bunchBetResponse.setId(bunchBet.getId());
        bunchBetResponse.setBunchId(bunchBet.getBunchId());
        bunchBetResponse.setCreate_time(CbsTimeUtils.getUtcTimeForDate(bunchBet.getCreateTime()));
        bunchBetResponse.setStatus(bunchBet.getStatus());
        bunchBetResponse.setWin_num(bunchBet.getWinNum());
        return bunchBetResponse;
    }


}
