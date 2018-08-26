/**
 * 
 */
package com.lifeix.cbs.mall.dao.order;

import java.util.List;

import com.lifeix.cbs.mall.dto.order.MallAddress;

/**
 * 订单收货地址
 * 
 * @author lifeix
 * 
 */
public interface MallAddressDao {

    public MallAddress selectById(Long id);

    public boolean insert(MallAddress order);

    public boolean update(MallAddress order);

    public boolean delete(MallAddress order);

    /**
     * 用户的收货地址列表
     * 
     * @param userId
     * @return
     */
    public List<MallAddress> findMallAddress(Long userId);
}
