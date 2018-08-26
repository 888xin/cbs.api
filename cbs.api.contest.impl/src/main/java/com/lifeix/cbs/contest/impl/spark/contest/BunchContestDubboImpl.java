package com.lifeix.cbs.contest.impl.spark.contest;

import com.lifeix.cbs.api.common.util.BetConstants;
import com.lifeix.cbs.api.common.util.ContestConstants;
import com.lifeix.cbs.api.common.util.PlayType;
import com.lifeix.cbs.contest.bean.bunch.BunchOptionsResponse;
import com.lifeix.cbs.contest.dao.bb.BbContestDao;
import com.lifeix.cbs.contest.dao.bb.BbOddsJcDao;
import com.lifeix.cbs.contest.dao.bb.BbOddsSizeDao;
import com.lifeix.cbs.contest.dao.bunch.BunchBetDao;
import com.lifeix.cbs.contest.dao.bunch.BunchContestDao;
import com.lifeix.cbs.contest.dao.bunch.BunchPrizeDao;
import com.lifeix.cbs.contest.dao.fb.FbContestDao;
import com.lifeix.cbs.contest.dao.fb.FbOddsJcDao;
import com.lifeix.cbs.contest.dao.fb.FbOddsSizeDao;
import com.lifeix.cbs.contest.dto.bb.BbContest;
import com.lifeix.cbs.contest.dto.bunch.BunchBet;
import com.lifeix.cbs.contest.dto.bunch.BunchContest;
import com.lifeix.cbs.contest.dto.bunch.BunchPrize;
import com.lifeix.cbs.contest.dto.fb.FbContest;
import com.lifeix.cbs.contest.dto.odds.OddsJc;
import com.lifeix.cbs.contest.dto.odds.OddsSize;
import com.lifeix.cbs.contest.service.spark.contest.BunchContestDubbo;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by lhx on 16-5-18 上午11:32
 *
 * @Description 串的dubbo接口
 */
@Service("bunchContestDubbo")
public class BunchContestDubboImpl implements BunchContestDubbo {

    //private final String LOCK = this.getClass().getName() + ":lock";

    @Autowired
    private BunchContestDao bunchContestDao;

    @Autowired
    private BunchBetDao bunchBetDao;

    @Autowired
    private FbContestDao fbContestDao;

    @Autowired
    private BbContestDao bbContestDao;

    @Autowired
    private FbOddsJcDao fbOddsJcDao;

    @Autowired
    private FbOddsSizeDao fbOddsSizeDao;

    @Autowired
    private BbOddsJcDao bbOddsJcDao;

    @Autowired
    private BbOddsSizeDao bbOddsSizeDao;

//    @Autowired
//    private CouponUserService couponUserService;

//    @Autowired
//    private MemcacheService memcacheService;

    @Autowired
    private BunchPrizeDao bunchPrizeDao ;

    @Override
    public void updateStatus(int status) throws IOException {

        if (status == ContestConstants.BunchContestStatus.INIT) {
            //第一场比赛已经开始，修改状态，status从0修改为1，结束下单
            List<BunchContest> list = bunchContestDao.getSettleList(status);
            if (list.size() > 0) {
                for (BunchContest bunchContest : list) {
                    BunchContest bunchContest1 = new BunchContest();
                    bunchContest1.setId(bunchContest.getId());
                    bunchContest1.setStatus(ContestConstants.BunchContestStatus.WORKING);
                    bunchContestDao.update(bunchContest1);
                }
            }
        } else if (status == ContestConstants.BunchContestStatus.WORKING) {
            //状态为1且endTime < NOW()时，说明整个串的各个比赛都已经结束，计算获取比赛结果，全部都能获取到的话，更新状态为2
            List<BunchContest> list = bunchContestDao.getSettleList(status);
            if (list.size() > 0) {
                ObjectMapper mapper = new ObjectMapper();
                for (BunchContest bunchContest : list) {
                    List<BunchOptionsResponse> optionsList = mapper.readValue(bunchContest.getOptions(), new TypeReference<List<BunchOptionsResponse>>() {
                    });
                    String result = bunchContest.getResult();
                    Integer[] results = new Integer[optionsList.size()];
                    if (StringUtils.isNotBlank(result)) {
                        results = mapper.readValue(result, new TypeReference<Integer[]>() {
                        });
                    }
                    for (BunchOptionsResponse bunchOptionsResponse : optionsList) {
                        int index = bunchOptionsResponse.getIndex();
                        if (results[index] != null) {
                            //已经处理过
                            continue;
                        }
                        //计算结果
                        getResult(results, bunchOptionsResponse, index);
                    }
                    boolean isStatistics = true;
                    for (Integer integer : results) {
                        if (integer == null) {
                            //只要有一场比赛的结果缺失，都不能修改状态为ContestConstants.BunchContestStatus.SETTELING
                            isStatistics = false;
                            break;
                        }
                    }
                    if (isStatistics) {
                        //全部结果已出，但未对用户下注数据统计，修改状态
                        status = ContestConstants.BunchContestStatus.SETTELING;
                    }
                    //比赛结果转为json字符串
                    String resultJson = mapper.writeValueAsString(results);
                    BunchContest bunchContest1 = new BunchContest();
                    bunchContest1.setId(bunchContest.getId());
                    bunchContest1.setStatus(status);
                    bunchContest1.setResult(resultJson);
                    bunchContestDao.update(bunchContest1);
                }
            }
        } else if (status == ContestConstants.BunchContestStatus.SETTELING) {
            //状态为2时，统计下注记录中的场数，统计完，更新状态为3 ContestConstants.BunchContestStatus.STATISTICS
            List<BunchContest> list = bunchContestDao.getSettleList(status);
            if (list.size() > 0) {
                ObjectMapper mapper = new ObjectMapper();
                for (BunchContest bunchContest : list) {


                    //串的结果
                    Integer[] results = mapper.readValue(bunchContest.getResult(), new TypeReference<Integer[]>() {
                    });

                    int limit = 100;
                    Long startId = null;
                    int size = limit;
                    List<BunchBet> bunchBetList;
                    while (size == limit) {
                        bunchBetList = bunchBetDao.getListByBunchId(bunchContest.getId(), ContestConstants.BunchBetStatus.INIT, startId, limit);
                        size = bunchBetList.size();
                        if (size == 0) {
                            break;
                        }

                        //设置下一页的startId
                        startId = bunchBetList.get(size - 1).getId();

                        for (BunchBet bunchBet : bunchBetList) {

                            if (bunchBet.getWinNum() != -1) {
                                //winNum = -1 为数据库初始化给的数值
                                continue;
                            }
                            Integer[] supports = mapper.readValue(bunchBet.getSupport(), new TypeReference<Integer[]>() {
                            });

                            int winNum = 0;
                            for (int i = 0; i < results.length; i++) {
                                if (results[i].equals(supports[i])) {
                                    //与结果符合，赢的场数+1
                                    winNum++;
                                }
                            }
                            BunchBet bunchBet1 = new BunchBet();
                            bunchBet1.setId(bunchBet.getId());
                            bunchBet1.setStatus(ContestConstants.BunchBetStatus.STATISTICS);
                            bunchBet1.setWinNum(winNum);
                            bunchBetDao.update(bunchBet1);
                        }
                    }
                    bunchBetList = bunchBetDao.getListByBunchId(bunchContest.getId(), ContestConstants.BunchBetStatus.INIT, null, 1);
                    if (bunchBetList.size() == 0){
                        BunchContest bunchContest1 = new BunchContest();
                        bunchContest1.setId(bunchContest.getId());
                        bunchContest1.setStatus(ContestConstants.BunchContestStatus.STATISTICS);
                        bunchContestDao.update(bunchContest1);
                    }
                }
            }
        } else if (status == ContestConstants.BunchContestStatus.STATISTICS) {
            //状态为3时，用户的下注记录有可能中奖和一定不中奖的，更新完一定不中奖的，至于中奖的，可能由运营发，也可能后台自动发
            //统计完，更新状态为4 ContestConstants.BunchContestStatus.REWARDING
            List<BunchContest> list = bunchContestDao.getSettleList(status);
            if (list.size() > 0) {
                ObjectMapper mapper = new ObjectMapper();
                for (BunchContest bunchContest : list) {

                    //查询该期的奖品
                    List<BunchPrize> bunchPrizeList = bunchPrizeDao.selectByBunchId(bunchContest.getId());
                    //中奖需要的最低赢的场数
                    int miniWinNum = 100 ;
                    for (BunchPrize bunchPrize : bunchPrizeList) {
                        int winNum = bunchPrize.getWinNum();
                        if (winNum < miniWinNum){
                            miniWinNum = winNum ;
                        }
                    }

                    int limit = 100;
                    Long startId = null;
                    int size = limit;
                    List<BunchBet> bunchBetList;
                    boolean updateFlag = true ;
                    while (size == limit) {
                        bunchBetList = bunchBetDao.getListByBunchId(bunchContest.getId(), ContestConstants.BunchBetStatus.STATISTICS, startId, limit);
                        size = bunchBetList.size();
                        if (size == 0) {
                            break;
                        }

                        //设置下一页的startId
                        startId = bunchBetList.get(size - 1).getId();

                        for (BunchBet bunchBet : bunchBetList) {

                            int winNum = bunchBet.getWinNum();
                            if (winNum < miniWinNum){
                                //该下注一定不会中奖
                                BunchBet bunchBet1 = new BunchBet();
                                bunchBet1.setId(bunchBet.getId());
                                bunchBet1.setStatus(ContestConstants.BunchBetStatus.NOT_REWARD);
                                bunchBetDao.update(bunchBet1);
                            } else {
                                //该下注可能中奖
                                BunchBet bunchBet1 = new BunchBet();
                                bunchBet1.setId(bunchBet.getId());
                                bunchBet1.setStatus(ContestConstants.BunchBetStatus.MAYBE_REWARD);
                                bunchBetDao.update(bunchBet1);
                            }
                        }
                    }
                    bunchBetList = bunchBetDao.getListByBunchId(bunchContest.getId(), ContestConstants.BunchBetStatus.STATISTICS, null, 1);
                    if (bunchBetList.size() == 0){
                        BunchContest bunchContest1 = new BunchContest();
                        bunchContest1.setId(bunchContest.getId());
                        bunchContest1.setStatus(ContestConstants.BunchContestStatus.REWARDING);
                        bunchContestDao.update(bunchContest1);
                    }
                }
            }
        }

    }

    //计算结果
    private void getResult(Integer[] results, BunchOptionsResponse bunchOptionsResponse, int index) {
        int playType = bunchOptionsResponse.getPlay_type();
        //获得赛事
        long contestId = bunchOptionsResponse.getContest_id();
        int contestType = bunchOptionsResponse.getContest_type();
        if (contestType == ContestConstants.ContestType.FOOTBALL) {
            FbContest fbContest = fbContestDao.selectById(contestId);

            if (fbContest.getStatus() == ContestConstants.ContestStatu.DONE) {
                //完场
                int homeScores = fbContest.getHomeScores();
                int awayScores = fbContest.getAwayScores();
                if (playType == PlayType.FB_SPF.getId()) {
                    //胜平负
                    if (homeScores < awayScores) {
                        results[index] = BetConstants.BetStatus.AWAY;
                    } else if (homeScores > awayScores) {
                        results[index] = BetConstants.BetStatus.HOME;
                    } else {
                        results[index] = BetConstants.BetStatus.DRAW;
                    }
                } else if (playType == PlayType.FB_RQSPF.getId()) {
                    //让球胜平负
                    //查盘口
                    OddsJc oddsJc = fbOddsJcDao.selectByContest(contestId);
                    double handicap = oddsJc.getHandicap();
                    double homeScoresDouble = homeScores + handicap;
                    if (homeScoresDouble < awayScores) {
                        results[index] = BetConstants.BetStatus.AWAY;
                    } else if (homeScoresDouble > awayScores) {
                        results[index] = BetConstants.BetStatus.HOME;
                    } else {
                        results[index] = BetConstants.BetStatus.DRAW;
                    }
                } else if (playType == PlayType.FB_SIZE.getId()) {
                    //大小球
                    //查盘口
                    OddsSize oddsSize = fbOddsSizeDao.selectByContest(contestId);
                    double handicap = oddsSize.getHandicap();
                    int totalScores = homeScores + awayScores;
                    if (totalScores < handicap) {
                        results[index] = BetConstants.BetStatus.AWAY;
                    } else {
                        results[index] = BetConstants.BetStatus.HOME;
                    }
                } else if (playType == PlayType.FB_ODDEVEN.getId()) {
                    //单双数
                    int totalScores = homeScores + awayScores;
                    if (totalScores % 2 == 0) {
                        results[index] = BetConstants.BetStatus.AWAY;
                    } else {
                        results[index] = BetConstants.BetStatus.HOME;
                    }
                }
            } else if (fbContest.getStatus() == ContestConstants.ContestStatu.CANCLE) {
                //取消作为走盘来处理
                results[index] = BetConstants.BetResultStatus.DRAW;
            }
        } else if (contestType == ContestConstants.ContestType.BASKETBALL) {
            BbContest bbContest = bbContestDao.selectById(contestId);

            if (bbContest.getStatus() == ContestConstants.ContestStatu.DONE) {
                //完场
                int homeScores = bbContest.getHomeScores();
                int awayScores = bbContest.getAwayScores();
                if (playType == PlayType.BB_SPF.getId()) {
                    //胜平负
                    if (homeScores < awayScores) {
                        results[index] = BetConstants.BetStatus.AWAY;
                    } else {
                        results[index] = BetConstants.BetStatus.HOME;
                    }
                } else if (playType == PlayType.BB_JC.getId()) {
                    //让球胜平负
                    //查盘口
                    OddsJc oddsJc = bbOddsJcDao.selectByContest(contestId);
                    double handicap = oddsJc.getHandicap();
                    double homeScoresDouble = homeScores + handicap;
                    if (homeScoresDouble < awayScores) {
                        results[index] = BetConstants.BetStatus.AWAY;
                    } else if (homeScoresDouble > awayScores) {
                        results[index] = BetConstants.BetStatus.HOME;
                    } else {
                        //走盘
                        results[index] = BetConstants.BetResultStatus.DRAW;
                    }
                } else if (playType == PlayType.BB_SIZE.getId()) {
                    //大小球
                    //查盘口
                    OddsSize oddsSize = bbOddsSizeDao.selectByContest(contestId);
                    double handicap = oddsSize.getHandicap();
                    int totalScores = homeScores + awayScores;
                    if (totalScores < handicap) {
                        results[index] = BetConstants.BetStatus.AWAY;
                    } else {
                        results[index] = BetConstants.BetStatus.HOME;
                    }
                } else if (playType == PlayType.BB_ODDEVEN.getId()) {
                    //单双数
                    int totalScores = homeScores + awayScores;
                    if (totalScores % 2 == 0) {
                        results[index] = BetConstants.BetStatus.AWAY;
                    } else {
                        results[index] = BetConstants.BetStatus.HOME;
                    }
                }
            } else if (bbContest.getStatus() == ContestConstants.ContestStatu.CANCLE) {
                //取消作为走盘来处理
                results[index] = BetConstants.BetResultStatus.DRAW;
            }
        }
    }

//    @Override
//    public void settle() throws IOException, L99IllegalDataException, L99IllegalParamsException {
//        List<BunchContest> list = bunchContestDao.getSettleList(ContestConstants.BunchContestStatus.SETTELING);
//        if (list.size() > 0) {
//            Integer lock = memcacheService.get(LOCK);
//            if (lock == null) {
//                try {
//                    //加锁
//                    memcacheService.set(LOCK, 1);
//                    for (BunchContest bunchContest : list) {
//                        //赢盘得到的筹码
//                        int back = bunchContest.getBack();
//                        int limit = 100;
//                        Long startId = null;
//                        int size = limit;
//                        List<BunchBet> bunchBetList;
//                        ObjectMapper mapper = new ObjectMapper();
//                        while (size == limit) {
//                            bunchBetList = bunchBetDao.getList(null, startId, limit);
//                            size = bunchBetList.size();
//                            if (size == 0) {
//                                break;
//                            }
//                            startId = bunchBetList.get(size - 1).getId();
//
//                            for (BunchBet bunchBet : bunchBetList) {
//                                Integer[] results = mapper.readValue(bunchContest.getResult(), new TypeReference<Integer[]>() {
//                                });
//                                List<BunchOptionsResponse> supportList = mapper.readValue(bunchBet.getSupport(), new TypeReference<List<BunchOptionsResponse>>() {
//                                });
//                                boolean win = true;
//                                for (BunchOptionsResponse bunchOptionsResponse : supportList) {
//                                    int result = results[bunchOptionsResponse.getIndex()];
//                                    if (result != bunchOptionsResponse.getSupport() && result != BetConstants.BetResultStatus.DRAW) {
//                                        //有一项选择不相等且不为走盘
//                                        BunchBet bunchBet1 = new BunchBet();
//                                        bunchBet1.setId(bunchBet.getId());
//                                        bunchBet1.setStatus(BetConstants.BetResultStatus.LOSS);
//                                        bunchBetDao.update(bunchBet1);
//                                        win = false;
//                                        break;
//                                    } else if (result == BetConstants.BetResultStatus.DRAW) {
//                                        //走盘
//                                        BunchBet bunchBet1 = new BunchBet();
//                                        bunchBet1.setId(bunchBet.getId());
//                                        bunchBet1.setStatus(BetConstants.BetResultStatus.DRAW);
//                                        bunchBetDao.update(bunchBet1);
//                                        win = false;
//                                        break;
//                                    }
//
//                                }
//                                if (win) {
//                                    //赢盘
//                                    BunchBet bunchBet1 = new BunchBet();
//                                    bunchBet1.setId(bunchBet.getId());
//                                    bunchBet1.setStatus(BetConstants.BetResultStatus.WIN);
//                                    boolean flag = bunchBetDao.update(bunchBet1);
//                                    if (flag) {
//                                        //发送筹码
//                                        couponUserService.settleCouponByPrice(bunchBet.getUserId(), back, CouponConstants.CouponSystem.TIME_24, "赛事活动串赢盘");
//                                    } else {
//                                        throw new L99IllegalDataException(MsgCode.BasicMsg.CODE_OPERATE_FAIL, MsgCode.BasicMsg.KEY_OPERATE_FAIL);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                } finally {
//                    //去锁
//                    memcacheService.delete(LOCK);
//                }
//            } else {
//                //正在结算中
//                throw new L99IllegalDataException(MsgCode.BetMsg.CODE_BET_SETTLING, MsgCode.BetMsg.KEY_BET_SETTLING);
//            }
//
//        }
//    }


}
