package com.lifeix.cbs.content.dao.frontpage;

import com.lifeix.cbs.content.dto.frontpage.FrontPage;

import java.util.List;
import java.util.Map;

/**
 * Created by lhx on 15-11-27 下午2:59
 *
 * @Description
 */
public interface FrontPageAdDao {

    /**
     * 插入记录并返回主键
     *
     */
    public Long insert(FrontPage frontPage);

    /**
     * 根据ids查询
     *
     */
    public Map<Long, FrontPage> findByIds(List<Long> ids);

    /**
     * 根据id查找
     *
     */
    public FrontPage findById(Long id);

    /**
     * 查找
     */
    public List<FrontPage> findFrontPages(Long startId, Long endId, Integer limit, Integer type);

    /**
     * 查找（内部接口）
     */
    public List<FrontPage> findFrontPagesInner(Long startId, Long endId, Integer limit, Integer type, Integer status, Integer skip);

    /**
     * 修改状态（内部接口）
     */
    public boolean editFrontPagesInner(Long id, Integer status);

    /**
     * 总数（内部接口）
     */
    public Integer findFrontPagesCount(Integer type, Integer status);

    /**
     * 根据contentId查找
     */
    public FrontPage findByContentId(Long contentId);


}
