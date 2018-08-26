/**
 * 
 */
package com.lifeix.cbs.mall.service.order;

import com.lifeix.cbs.mall.bean.order.MallExpressResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * @author lifeix
 * 
 */
public interface MallExpressService {

    /**
     * 根据商品订单号查询物流信息
     * 
     * @param orderId
     * @return
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    public MallExpressResponse findExpressByOrder(Long orderId) throws L99IllegalParamsException, L99IllegalDataException;

    /**
     * 根据第三方订单号查询物流信息
     * 
     * @param expressType
     * @param expressNO
     * @return
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    public MallExpressResponse findExpressByNO(Integer expressType, String expressNO) throws L99IllegalParamsException,
	    L99IllegalDataException;

    /**
     * 更新物流信息
     * 
     * @param orderId
     *            订单id
     * @param state
     *            物流状态
     * @param expressInfo
     *            物流详情
     * @throws L99IllegalDataException
     */
    public void updateMallExpress(Long orderId, Integer state, String expressInfo) throws L99IllegalDataException;

}
