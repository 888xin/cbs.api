package com.lifeix.cbs.contest.dao.bunch;

import com.lifeix.cbs.contest.dto.bunch.BunchContest;

import java.util.List;

/**
 * Created by lhx on 16-5-16 下午5:41
 *
 * @Description
 */
public interface BunchContestDao {

    public BunchContest selectById(long id);

    public Long insert(BunchContest bunchContest);

    public boolean update(BunchContest bunchContest);

    /**
     * 获得可下注列表（客户端用）
     */
    public List<BunchContest> getList();

    /**
     * 获得往期列表（客户端用）
     */
    public List<BunchContest> getOldList(Long startId, Integer limit);

    /**
     * 根据状态获得列表，不做分页处理，因为status != -1（已处理）
     */
    public List<BunchContest> getSettleList(int status);

    /**
     * 管理后台用
     */
    public List<BunchContest> getInnerList(Integer status, Long startId, Integer limit);


}
