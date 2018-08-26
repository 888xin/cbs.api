/**
 * 
 */
package com.lifeix.cbs.mall.dao.order;

import java.util.List;

import com.lifeix.cbs.mall.dto.order.MallOrder;

/**
 * 商品订单
 * 
 * @author lifeix
 * 
 */
public interface MallOrderDao {

    public MallOrder selectById(Long id);

    public boolean insert(MallOrder order);

    public boolean update(MallOrder order);

    /**
     * 订单列表
     * 
     * @param userId
     * @param status
     * @param startId
     * @param limit
     * @return
     */
    public List<MallOrder> findMallOrders(Long userId, List<Integer> status, Long startId, int limit);

}
