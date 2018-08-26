/**
 * 
 */
package com.lifeix.cbs.mall.impl.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.exception.MsgCode.OrderMsg;
import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.mall.bean.order.MallAddressListResponse;
import com.lifeix.cbs.mall.bean.order.MallAddressResponse;
import com.lifeix.cbs.mall.dao.order.MallAddressDao;
import com.lifeix.cbs.mall.dto.order.MallAddress;
import com.lifeix.cbs.mall.impl.transform.MallOrderTransformUtil;
import com.lifeix.cbs.mall.service.order.MallAddressService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * 订单收货地址
 * 
 * @author lifeix
 * 
 */
@Service("mallAddressService")
public class MallAddressServiceImpl extends ImplSupport implements MallAddressService {

    @Autowired
    private MallAddressDao mallAddressDao;

    @Override
    public MallAddressListResponse findMallAddressList(Long userId) throws L99IllegalParamsException,
	    L99IllegalDataException, JSONException {

	ParamemeterAssert.assertDataNotNull(userId);

	List<MallAddress> mallAddressList = mallAddressDao.findMallAddress(userId);

	List<MallAddressResponse> mallAddressResponse = new ArrayList<MallAddressResponse>();
	for (MallAddress address : mallAddressList) {
	    mallAddressResponse.add(MallOrderTransformUtil.transformMallAddress(address));
	}

	MallAddressListResponse reponse = new MallAddressListResponse();
	reponse.setAddress_list(mallAddressResponse);
	reponse.setNumber(mallAddressResponse.size());

	return reponse;

    }

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
    @Override
    public void saveAddress(Long id, Long userId, String name, String address, String mobile)
	    throws L99IllegalParamsException, L99IllegalDataException {

	ParamemeterAssert.assertDataNotNull(userId, mobile, name, address);

	if (id != null) {
	    MallAddress mallAddress = mallAddressDao.selectById(id);
	    if (mallAddress == null || !mallAddress.getUserId().equals(userId)) {
		throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	    }
	    mallAddress.setName(name);
	    mallAddress.setMobile(mobile);
	    mallAddress.setAddress(address);
	    boolean flag = mallAddressDao.update(mallAddress);
	    if (!flag) {
		throw new L99IllegalDataException(String.valueOf(BasicMsg.CODE_BASIC_SERVCER), BasicMsg.KEY_BASIC_SERVCER);
	    }
	} else {
	    List<MallAddress> addressList = mallAddressDao.findMallAddress(userId);
	    if (addressList.size() >= 9) {
		// 限制用户最多只能拥有9个收货地址
		throw new L99IllegalDataException(String.valueOf(OrderMsg.CODE_ORDER_TO_MANY_ADDRESS),
		        OrderMsg.KEY_ORDER_TO_MANY_ADDRESS);
	    }
	    MallAddress mallAddress = new MallAddress();
	    mallAddress.setUserId(userId);
	    mallAddress.setName(name);
	    mallAddress.setMobile(mobile);
	    mallAddress.setAddress(address);
	    mallAddress.setCreateTime(new Date());
	    boolean flag = mallAddressDao.insert(mallAddress);
	    if (!flag) {
		throw new L99IllegalDataException(String.valueOf(BasicMsg.CODE_BASIC_SERVCER), BasicMsg.KEY_BASIC_SERVCER);
	    }
	}
    }

    /**
     * 删除收货地址
     */
    @Override
    public void delAddress(Long id, Long userId) throws L99IllegalParamsException, L99IllegalDataException {

	ParamemeterAssert.assertDataNotNull(id, userId);

	MallAddress mallAddress = mallAddressDao.selectById(id);
	if (mallAddress == null || !mallAddress.getUserId().equals(userId)) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}

	boolean flag = mallAddressDao.delete(mallAddress);
	if (!flag) {
	    throw new L99IllegalDataException(String.valueOf(BasicMsg.CODE_BASIC_SERVCER), BasicMsg.KEY_BASIC_SERVCER);
	}
    }

    /**
     * 查找收货地址
     * 
     * @param id
     * @param userId
     * @return
     * @throws L99IllegalParamsException
     */
    @Override
    public MallAddressResponse findAddress(Long id, Long userId) throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(id, userId);

	MallAddress mallAddress = mallAddressDao.selectById(id);
	if (mallAddress == null || mallAddress.getUserId() != userId) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}
	return MallOrderTransformUtil.transformMallAddress(mallAddress);
    }

}
