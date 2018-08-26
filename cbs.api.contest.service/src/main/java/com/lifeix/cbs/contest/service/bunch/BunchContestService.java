package com.lifeix.cbs.contest.service.bunch;

import com.lifeix.cbs.contest.bean.bunch.BunchContestListResponse;
import com.lifeix.cbs.contest.bean.bunch.BunchContestResponse;
import com.lifeix.cbs.contest.bean.bunch.BunchPrizeResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import org.json.JSONException;

import java.io.IOException;

/**
 * Created by lhx on 16-5-17 上午11:14
 *
 * @Description
 */
public interface BunchContestService {

    /**
     * 插入数据（管理后台用）
     */
    public void insert(String name, String image, String options, String prizes, int cost, boolean longbi) throws IOException, L99IllegalParamsException, L99IllegalDataException;

    /**
     * 更新数据
     */
    public void update(Long id, String name, String image, String options, Integer cost, Boolean longbi, String result, Integer status, Integer people) throws L99IllegalParamsException, L99IllegalDataException;

    /**
     * 获得可下注列表（客户端用）
     */
    public BunchContestListResponse getList(Integer status, Long startId, Integer limit) ;

    /**
     * 列表（内部接口用）
     */
    public BunchContestListResponse getInnerList(Integer status, Long startId, Integer limit) ;

    /**
     * 浏览单个串，包括玩法对应的赔率和用户是否下注和下注的选项
     */
    public BunchContestResponse view(Long id, Long userId) throws IOException, L99IllegalParamsException, L99IllegalDataException, JSONException;


    /**
     * 查看单个串（内部）
     */
    public BunchContestResponse viewInner(Long id) throws L99IllegalParamsException, IOException;
    /**
     * 查看奖品（内部）
     */
    public BunchContestResponse viewPrizeInner(Long id) throws L99IllegalParamsException, IOException;

    /**
     * 更改奖品
     */
    public void updatePrize(String prize) throws IOException, L99IllegalDataException;

    /**
     * 添加奖品照片
     */
    public void addPrizeImage(String image) throws L99IllegalParamsException;
    /**
     * 删除奖品照片
     */
    public void deletePrizeImage(String image) throws L99IllegalParamsException;

    /**
     * 获得奖品照片
     */
    public BunchPrizeResponse getPrizeImage();

    /**
     * 获得可能中奖的用户
     */
    public BunchContestResponse getUserAward(Long id) throws L99IllegalParamsException;

    /**
     * 派奖
     */
    public void sendAward(Long bunchId, String userIds) throws L99IllegalParamsException, L99IllegalDataException, JSONException;
}
