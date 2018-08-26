/**
 * 
 */
package com.lifeix.cbs.mall.impl.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.exception.MsgCode.OrderMsg;
import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.mall.bean.order.MallExpressResponse;
import com.lifeix.cbs.mall.common.ExpressConstants;
import com.lifeix.cbs.mall.dao.order.MallExpressDao;
import com.lifeix.cbs.mall.dto.order.MallExpress;
import com.lifeix.cbs.mall.impl.transform.MallOrderTransformUtil;
import com.lifeix.cbs.mall.impl.util.KdniaoSearchUtil;
import com.lifeix.cbs.mall.service.order.MallExpressService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * 物流信息
 * 
 * @author lifeix
 * 
 */
@Service("mallExpressService")
public class MallExpressServiceImpl extends ImplSupport implements MallExpressService {

    @Autowired
    private MallExpressDao mallExpressDao;

    /**
     * 根据商品订单号查询物流信息
     * 
     * @param orderId
     * @return
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    public MallExpressResponse findExpressByOrder(Long orderId) throws L99IllegalParamsException, L99IllegalDataException {

	ParamemeterAssert.assertDataNotNull(orderId);

	MallExpress mallExpress = mallExpressDao.findById(orderId);

	return findExpressInfo(mallExpress);
    }

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
	    L99IllegalDataException {

	ParamemeterAssert.assertDataNotNull(expressType, expressNO);

	MallExpress mallExpress = mallExpressDao.findByExpressNO(expressType, expressNO);

	return findExpressInfo(mallExpress);
    }

    private MallExpressResponse findExpressInfo(MallExpress mallExpress) throws L99IllegalDataException {
	if (mallExpress == null) {
	    // 查询不到订单物品信息
	    throw new L99IllegalDataException(OrderMsg.CODE_ORDER_NOT_FOUND, OrderMsg.KEY_ORDER_NOT_FOUND);
	}
	long changeSpeed = 100;
	if (mallExpress.getUpdateTime() != null) {
	    changeSpeed = (System.currentTimeMillis() - mallExpress.getUpdateTime().getTime()) / 1000;
	}
	// 没有签收并且更新间隔大于1分钟，需要再获取新的物流信息
	if (mallExpress.getState() != ExpressConstants.State.QX && changeSpeed > 60) {
	    String shipperCode = ExpressConstants.shipperCodeMap.get(mallExpress.getExpressType());
	    MallExpress retExpress = KdniaoSearchUtil.getInstance().getOneOrderTracesByJson(shipperCode,
		    mallExpress.getExpressNO());
	    if (retExpress != null) {
		mallExpress.setState(retExpress.getState());
		mallExpress.setExpressInfo(retExpress.getExpressInfo());
		boolean flag = mallExpressDao.update(mallExpress);
		if (!flag) {
		    LOG.warn(String.format("update mallExpress failed - %d", mallExpress.getOrderId()));
		}
	    }

	}
	return MallOrderTransformUtil.transformMallExpress(mallExpress);
    }

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
    @Override
    public void updateMallExpress(Long orderId, Integer state, String expressInfo) throws L99IllegalDataException {
	MallExpress mallExpress = new MallExpress();
	mallExpress.setOrderId(orderId);
	mallExpress.setState(state);
	mallExpress.setExpressInfo(expressInfo);
	boolean success = mallExpressDao.update(mallExpress);
	if (!success) {
	    throw new L99IllegalDataException(BasicMsg.CODE_OPERATE_FAIL, BasicMsg.KEY_OPERATE_FAIL);
	}

    }

}
