package com.lifeix.cbs.api.impl.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.bean.user.CbsUserStarListResponse;
import com.lifeix.cbs.api.bean.user.CbsUserStarResponse;
import com.lifeix.cbs.api.bean.user.UserContestStatisticsResponse;
import com.lifeix.cbs.api.common.exception.MsgCode;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.dao.user.CbsUserDao;
import com.lifeix.cbs.api.dao.user.CbsUserStarDao;
import com.lifeix.cbs.api.dto.user.CbsUser;
import com.lifeix.cbs.api.dto.user.CbsUserStar;
import com.lifeix.cbs.api.impl.util.AccountTransformUtil;
import com.lifeix.cbs.api.service.spark.UserContestStatisticsDubbo;
import com.lifeix.cbs.api.service.user.CbsUserStarService;
import com.lifeix.cbs.api.util.UserConstants;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.nyx.relationship.service.CbsRelationshipService;

/**
 * 用户推荐逻辑
 *
 * @author lifeix
 */
@Service("cbsUserStarService")
public class CbsUserStarServiceImpl extends ImplSupport implements CbsUserStarService {

    private final static int RECOMMEND_NUM = 2 ;

    @Autowired
    private CbsUserStarDao cbsUserStarDao;

    @Autowired
    private CbsUserDao cbsUserDao;

    @Autowired
    private UserContestStatisticsDubbo userContestStatisticsDubbo;

    @Autowired
    private CbsRelationshipService cbsRelationshipService;

    /**
     * 获取所有推荐列表
     *
     * @param hideFlag
     * @param startId
     * @param limit
     * @return
     * @throws L99IllegalParamsException
     */
    @Override
    public CbsUserStarListResponse findAllStars(Boolean hideFlag, Long startId, int limit) throws L99IllegalParamsException {

        List<CbsUserStar> userStars = cbsUserStarDao.findAllStars(hideFlag, startId, limit);

        List<Long> userIds = new ArrayList<Long>();
        for (CbsUserStar star : userStars) {
            userIds.add(star.getUserId());
        }
        Map<Long, CbsUser> userMap = cbsUserDao.findCsAccountMapByIds(userIds);

        List<CbsUserStarResponse> user_stars = new ArrayList<CbsUserStarResponse>();
        for (CbsUserStar star : userStars) {
            CbsUserStarResponse userStar = AccountTransformUtil.transformUserStar(star);
            if (userStar != null) {
                CbsUser user = userMap.get(star.getUserId());
                userStar.setUser(AccountTransformUtil.transformUser(user, true));
                user_stars.add(userStar);
                startId = star.getId();
            }
        }

        if (user_stars.size() == 0 || user_stars.size() < limit) { // 没有数据或数据不够返回-1
            startId = -1L;
        }

        CbsUserStarListResponse reponse = new CbsUserStarListResponse();
        reponse.setUser_stars(user_stars);
        reponse.setNumber(user_stars.size());
        reponse.setStartId(startId);

        return reponse;
    }

    /**
     * 更新推荐用户
     *
     * @param userId
     * @param factor
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    @Override
    public void putUserStar(Long userId, Integer factor) throws L99IllegalParamsException, L99IllegalDataException {

        ParamemeterAssert.assertDataNotNull(userId, factor);

        CbsUser user = cbsUserDao.selectById(userId);
        if (user == null) { // 用户未找到
            throw new L99IllegalDataException(MsgCode.UserMsg.CODE_USER_ACCOUNT_NOT_FOUND,
                    MsgCode.UserMsg.KEY_USER_ACCOUNT_NOT_FOUND);
        } else if (user.getStatus() == UserConstants.BLOCK) { // 用户被屏蔽
            throw new L99IllegalDataException(MsgCode.UserMsg.CODE_USER_ACCOUNT_BLOCK,
                    MsgCode.UserMsg.KEY_USER_ACCOUNT_BLOCK);
        }

        CbsUserStar userStar = cbsUserStarDao.selectByUser(userId);
        if (userStar == null) {
            userStar = new CbsUserStar();
            userStar.setUserId(userId);
            userStar.setShowNum(0);
            userStar.setHitNum(0);
            userStar.setCreateTime(new Date());
            userStar.setHideFlag(false);
        } else { // 删除旧的记录 将被修改的记录排序前置
            boolean flag = cbsUserStarDao.deleteByUser(userId);
            if (!flag) {
                throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
            }
        }

        UserContestStatisticsResponse statistics = userContestStatisticsDubbo.getUserContestStatistics(userId);
        userStar.setRank(statistics.getRank());
        userStar.setWinning(statistics.getWinning());
        userStar.setFactor(factor);

        boolean flag = cbsUserStarDao.insert(userStar);
        if (!flag) {
            throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
        }
    }

    /**
     * 显示或隐藏推荐
     *
     * @param userId
     * @param hideFlag
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    @Override
    public void onOffUserStar(Long userId, Boolean hideFlag) throws L99IllegalParamsException, L99IllegalDataException {

        ParamemeterAssert.assertDataNotNull(userId, hideFlag);

        CbsUserStar userStar = cbsUserStarDao.selectByUser(userId);
        if (userStar == null) {
            throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
        }
        if (userStar.isHideFlag() != hideFlag.booleanValue()) {
            userStar.setHideFlag(hideFlag);
            boolean flag = cbsUserStarDao.update(userStar);
            if (!flag) {
                throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
            }
        }

    }

    @Override
    public CbsUserStarListResponse findStars(Long accountId) {
        CbsUserStarListResponse reponse = new CbsUserStarListResponse();
        List<Long> userIds = cbsUserStarDao.findAllStarsIds();
        if (userIds.size() == 0){
            return reponse ;
        }
        //用户已经关注的用户id
        List<Long> attentionUserId = cbsRelationshipService.getMeAttention(accountId,0,2000);
        //去除已经关注过的用户和去除自己（如果在推荐列表中）
        userIds.remove(accountId);
        //用户有关注的用户
        if (attentionUserId.size() > 0){
            List<Long> removeUserIdList = new ArrayList<Long>();
            for (Long userId : userIds) {
                boolean flag = attentionUserId.contains(userId);
                if (flag){
                    removeUserIdList.add(userId);
                }
            }
            //有已经关注过的用户
            if (removeUserIdList.size() > 0){
                for (Long aLong : removeUserIdList) {
                    userIds.remove(aLong);
                }
            }
        }
        //智能推荐或随机选择n个推荐用户
        List<Long> recomendUserIds = new ArrayList<Long>();
        if (userIds.size() > RECOMMEND_NUM){
            Random random = new Random();
            for (int i = 0; i < RECOMMEND_NUM; i++) {
                int n = random.nextInt(userIds.size());
                recomendUserIds.add(userIds.get(n));
                userIds.remove(n);
            }
        } else {
            recomendUserIds = userIds;
        }

        Map<Long, CbsUserStar> starMap = cbsUserStarDao.findByIds(recomendUserIds);
        Map<Long, CbsUser> userMap = cbsUserDao.findCsAccountMapByIds(recomendUserIds);

        List<CbsUserStarResponse> user_stars = new ArrayList<CbsUserStarResponse>();
        for (Long userId : starMap.keySet()) {
            CbsUserStarResponse userStar = AccountTransformUtil.transformUserStar(starMap.get(userId));
            if (userStar != null) {
                CbsUser user = userMap.get(userStar.getUser_id());
                userStar.setUser(AccountTransformUtil.transformUser(user, true));
                user_stars.add(userStar);
            }
        }
        reponse.setUser_stars(user_stars);
        return reponse;
    }
}
