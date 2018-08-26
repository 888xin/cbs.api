/**
 * 
 */
package com.lifeix.cbs.mall.service.order;

import org.json.JSONException;

import com.lifeix.cbs.mall.bean.order.MallAddressListResponse;
import com.lifeix.cbs.mall.bean.order.MallAddressResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * 
 * @author lifeix
 * 
 */
public interface MallAddressService {

    /**
     * 用户收货地址列表
     * 
     * @param userId
     * @return
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     * @throws JSONException
     */
    public MallAddressListResponse findMallAddressList(Long userId) throws L99IllegalParamsException,
	    L99IllegalDataException, JSONException;

    /**
     * 保存收货地址
     * 
     * @param id
     * @param userId
     * @param name
     * @param address
     * @param mobile
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    public void saveAddress(Long id, Long userId, String name, String address, String mobile)
	    throws L99IllegalParamsException, L99IllegalDataException;

    /**
     * 删除收货地址
     * 
     * @param id
     * @param userId
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    public void delAddress(Long id, Long userId) throws L99IllegalParamsException, L99IllegalDataException;

    /**
     * 查找收货地址
     * 
     * @param id
     * @param userId
     * @return
     * @throws L99IllegalParamsException
     */
    public MallAddressResponse findAddress(Long id, Long userId) throws L99IllegalParamsException;
}
