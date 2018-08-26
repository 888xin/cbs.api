/**
 * 
 */
package com.lifeix.cbs.mall.dao.order;

import java.util.List;

import com.lifeix.cbs.mall.dto.order.MallExpress;

/**
 * @author lifeix
 * 
 */
public interface MallExpressDao {
    /**
     * 根据orderId查询
     */
    public MallExpress findById(Long orderId);

    /**
     * 添加物流信息
     */
    public boolean insert(MallExpress mallExpress);

    /**
     * 更新物流信息
     */
    public boolean update(MallExpress mallExpress);

    /**
     * 根据创建时间查询物流信息
     */
    public List<MallExpress> findListByCreateTime(String createTime, Integer state);

    /**
     * 根据更新时间查询物流信息
     */
    public List<MallExpress> findListByUpdateTime(String updateTime, Integer state);

    /**
     * 根据用户Id查询物流信息
     */
    public List<MallExpress> findByUserId(Long userId, Integer state, Long startId, Integer limit);

    /**
     * 根据物流单号查询物流信息
     * 
     * @param expressType
     *            快递公司类型
     * @param expressNO
     *            快递单号
     */
    public MallExpress findByExpressNO(Integer expressType, String expressNO);

}
