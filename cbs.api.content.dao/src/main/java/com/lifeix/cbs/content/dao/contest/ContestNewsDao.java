package com.lifeix.cbs.content.dao.contest;

import com.lifeix.cbs.content.dto.contest.ContestNews;

import java.util.List;

/**
 * Created by lhx on 16-4-14 下午5:47
 *
 * @Description
 */
public interface ContestNewsDao {

    /**
     * 插入记录
     */
    public long insert(ContestNews contestNews);

    public boolean edit(ContestNews contestNews);

    /**
     * 根据赛事信息查找
     * @param contestId
     * @param contestType
     * @return
     */
    public List<ContestNews> findNews(Long contestId, Integer contestType);


    /**
     * 内部接口查询
     * @param contestId
     * @param contestType
     * @param status
     * @param startId
     * @param endId
     * @param skip
     * @param limit
     * @return
     */
    public List<ContestNews> findNewsInner(Long contestId, Integer contestType, Integer status,Long startId,
                                           Long endId, Integer skip, Integer limit);

    /**
     * 总数，用于分页
     * @param contestId
     * @param contestType
     * @param status
     * @return
     */
    public Integer count(Long contestId, Integer contestType, Integer status);

    /**
     * 查询单个
     * @param id
     * @return
     */
    public ContestNews findOne(Long id);
}
