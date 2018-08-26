/**
 * 
 */
package com.lifeix.cbs.content.dao.inform;

import java.util.Date;
import java.util.List;

import com.lifeix.cbs.content.dto.inform.Inform;

/**
 * @author lifeix
 *
 */
public interface InformDao {

    /**
     * 举报列表
     * 
     * @param page
     * @param limit
     * @param status
     * @return
     */
    public List<Inform> selectList(Integer page, int limit, Integer status, Integer type);

    /**
     * 根据主键查找
     * 
     * @param id
     * @return
     */
    public Inform selectById(Long id);

    /**
     * 根据被举报内容id查找
     * 
     * @param commentId
     * @return
     */
    public Inform selectByContainId(Long containId, Integer type, Integer status);

    /**
     * 新增被举报评论
     * 
     * @param inForm
     * @return
     */
    public Long insertInform(Inform inForm);

    /**
     * 更新举报评论
     * 
     * @param id
     * @param total
     * @param informType
     * @param informReason
     * @return
     */
    public boolean updateInform(Long id, Integer total, Integer informType, String informReason);

    /**
     * 更新被举报评论处理状态
     * 
     * @param id
     * @param status
     * @param disposeInfo
     * @return
     */
    public boolean updateStatusById(Long id, Integer status, String disposeInfo, Date removeTime);

    /**
     * 批量更新被举报评论处理状态
     * 
     * @param ids
     * @param status
     * @param disposeInfo
     * @return
     */
    public boolean updateStatusByIds(List<Long> ids, Integer status, String disposeInfo);

    /**
     * 删除
     * 
     * @param id
     * @return
     */
    public boolean deleteById(Long id);
}
