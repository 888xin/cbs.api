package com.lifeix.cbs.content.service.contest;

import com.lifeix.cbs.content.bean.contest.ContestNewsListResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * Created by lhx on 16-4-15 上午9:49
 *
 * @Description
 */
public interface ContestNewsService {

    /**
     * 根据赛事来查找
     */
    public ContestNewsListResponse findNews(Long contestId, Integer contestType) throws L99IllegalParamsException;

    /**
     * 是否有绑定的赛事分析
     */
    public boolean hasNews(Long contestId, Integer contestType) throws L99IllegalParamsException;

    /**
     * 管理后台使用的查找接口
     * @param contestId
     * @param contestType
     * @param status
     * @param startId
     * @param endId
     * @param skip
     * @param limit
     * @return
     * @throws L99IllegalParamsException
     */
    public ContestNewsListResponse findNewsInner(Long contestId, Integer contestType, Integer status, Long startId, Long endId, Integer skip, Integer limit) throws L99IllegalParamsException;

    /**
     * 添加
     * @param title
     * @param image
     * @param contentId
     * @param contestId
     * @param contestType
     * @throws L99IllegalDataException
     */
    public long add(String title, String desc, String image, Long contentId, Long contestId, Integer contestType) throws L99IllegalDataException;

    /**
     * 编辑
     * @param id
     * @param title
     * @param image
     * @param contentId
     * @param contestId
     * @param contestType
     * @param status
     * @throws L99IllegalDataException
     * @throws L99IllegalParamsException
     */
    public void edit(Long id, String title, String desc, String image, Long contentId, Long contestId, Integer contestType,Integer status) throws L99IllegalDataException, L99IllegalParamsException;

}
