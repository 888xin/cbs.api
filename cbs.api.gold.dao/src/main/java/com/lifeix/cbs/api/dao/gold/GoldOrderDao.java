package com.lifeix.cbs.api.dao.gold;

import java.util.List;

import com.lifeix.cbs.api.dao.BasicDao;
import com.lifeix.cbs.api.dto.gold.GoldOrder;

public interface GoldOrderDao extends BasicDao<GoldOrder, Long> {

    /**
     * 构建订单
     * 
     * @param goldOrder
     * @return
     */
    public Long insertAndGetPrimaryKey(GoldOrder goldOrder);

    /**
     * 通过订单号查询订单
     * 
     * @param orderNO
     * @return
     */
    public GoldOrder findOrderByOrderNO(String orderNO);

    /**
     * 查询订单数量
     * 
     * @param userId
     * @param statu
     * @return
     */
    public Long getOrderNumber(Long userId, Integer statu);

    /**
     * 查询订单列表
     * 
     * @param userId
     * @param statu
     * @param start
     * @param limit
     * @return
     */
    public List<GoldOrder> findOrders(Long userId, Integer statu, Integer start, Integer limit);

}
