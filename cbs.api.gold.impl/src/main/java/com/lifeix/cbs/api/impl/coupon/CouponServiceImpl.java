package com.lifeix.cbs.api.impl.coupon;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.achieve.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.achieve.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.bean.coupon.CouponListResponse;
import com.lifeix.cbs.api.bean.coupon.CouponResponse;
import com.lifeix.cbs.api.common.exception.MsgCode;
import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.util.CouponConstants.CouponType;
import com.lifeix.cbs.api.common.util.CouponConstants.RangeKey;
import com.lifeix.cbs.api.dao.coupon.CouponDao;
import com.lifeix.cbs.api.dto.coupon.Coupon;
import com.lifeix.cbs.api.service.coupon.CouponService;
import com.lifeix.cbs.api.transform.CouponTransformUtil;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * Created by lhx on 16-2-25 下午2:27
 * 
 * @Description
 */
@Service("couponService")
public class CouponServiceImpl extends ImplSupport implements CouponService {

    @Autowired
    private CouponDao couponDao;

    /**
     * 新增或修改龙筹券
     */
    @Override
    public void saveOrUpdate(Long id, Integer hour, String name, Integer price, Integer rangeKey, String rangeValue,
	    Integer sum, Integer num, Double proportion, String descr, Boolean valid) throws L99IllegalParamsException {

	ParamemeterAssert.assertDataNotNull(proportion, rangeKey);

	// 赛事龙筹必须制定对应Id
	if (rangeKey == RangeKey.FB_COUPON_CUP || rangeKey == RangeKey.BB_COUPON_CUP
	        || rangeKey == RangeKey.FB_COUPON_CONTEST || rangeKey == RangeKey.BB_COUPON_CONTEST
	        || rangeKey == RangeKey.COUPON_YY) {
	    ParamemeterAssert.assertDataNotNull(rangeValue);
	}

	Coupon coupon;
	if (id == null) { // 新增龙筹券
	    coupon = new Coupon();
	    coupon.setType(CouponType.COUPON_PROCEEDINGS);
	    coupon.setHour(hour);
	    coupon.setName(name);
	    coupon.setPrice(price);
	    coupon.setRangeKey(rangeKey);
	    coupon.setRangeValue(rangeValue);
	    coupon.setSum(sum);
	    coupon.setNum(num);
	    coupon.setProportion(proportion);
	    coupon.setDescr(descr);
	    coupon.setValid(valid);
	} else { // 修改龙筹券
	    coupon = couponDao.findById(id);
	    if (coupon == null || (coupon.getType() == CouponType.COUPON_SYSTEM && proportion == null)) {
		throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	    }
	    // if (!coupon.isValid()) { // 未激活的龙筹券可被修改
	    // coupon.setHour(hour);
	    // coupon.setName(name);
	    // coupon.setPrice(price);
	    // coupon.setRangeKey(rangeKey);
	    // coupon.setRangeValue(rangeValue);
	    // coupon.setSum(sum);
	    // coupon.setNum(num);
	    // coupon.setProportion(proportion);
	    // coupon.setDescr(descr);
	    // }
	    coupon.setHour(hour);
	    coupon.setName(name);
	    coupon.setPrice(price);
	    coupon.setRangeKey(rangeKey);
	    coupon.setRangeValue(rangeValue);
	    coupon.setSum(sum);
	    coupon.setNum(num);
	    coupon.setProportion(proportion);
	    coupon.setDescr(descr);
	    // coupon.setValid(valid);
	}

	boolean flag;
	if (id == null) {
	    flag = couponDao.insert(coupon);
	} else {
	    flag = couponDao.update(coupon);
	}
	if (!flag) {
	    throw new L99IllegalParamsException(MsgCode.BasicMsg.CODE_BASIC_SERVCER, MsgCode.BasicMsg.KEY_BASIC_SERVCER);
	}
    }

    /**
     * 获得龙筹卷列表
     */
    @Override
    public CouponListResponse selectCoupons(Integer type, Boolean valid, Long startId, int limit) {

	CouponListResponse response = new CouponListResponse();

	List<Coupon> coupons = couponDao.selectCoupons(type, valid, startId, limit);
	if (coupons.size() > 0) {
	    List<CouponResponse> list = new ArrayList<CouponResponse>();
	    for (Coupon coupon : coupons) {
		CouponResponse couponResponse = CouponTransformUtil.transformGold(coupon);
		list.add(couponResponse);
	    }
	    response.setConpons(list);
	}
	return response;
    }

    /**
     * 改变龙筹券的状态（激活与失效）
     */
    @Override
    public void toggerValid(Long id, Boolean valid) throws L99IllegalParamsException {

	ParamemeterAssert.assertDataNotNull(id, valid);

	Coupon coupon;
	coupon = couponDao.findById(id);
	if (coupon == null || coupon.getType() == CouponType.COUPON_SYSTEM) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}

	coupon.setValid(valid);

	boolean flag = couponDao.update(coupon);
	if (!flag) {
	    throw new L99IllegalParamsException(MsgCode.BasicMsg.CODE_BASIC_SERVCER, MsgCode.BasicMsg.KEY_BASIC_SERVCER);
	}
    }

    @Override
    public CouponResponse findById(Long id) {
	Coupon coupon = couponDao.findById(id);
	CouponResponse couponResponse = null;
	if (coupon != null) {
	    couponResponse = CouponTransformUtil.transformGold(coupon);
	}
	return couponResponse;
    }

}
