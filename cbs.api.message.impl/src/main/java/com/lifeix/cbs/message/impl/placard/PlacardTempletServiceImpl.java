package com.lifeix.cbs.message.impl.placard;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.bean.coupon.CouponResponse;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.util.ContentConstants;
import com.lifeix.cbs.api.common.util.ContestConstants.ContestType;
import com.lifeix.cbs.api.common.util.CouponConstants.RangeKey;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.api.common.util.XingePushUtil;
import com.lifeix.cbs.api.service.coupon.CouponService;
import com.lifeix.cbs.api.service.coupon.CouponUserService;
import com.lifeix.cbs.message.bean.placard.PlacardTempletListResponse;
import com.lifeix.cbs.message.bean.placard.PlacardTempletResponse;
import com.lifeix.cbs.message.dao.placard.PlacardDataDao;
import com.lifeix.cbs.message.dao.placard.PlacardTempletDao;
import com.lifeix.cbs.message.dto.PlacardData;
import com.lifeix.cbs.message.dto.PlacardTemplet;
import com.lifeix.cbs.message.impl.redis.RedisMessageHandler;
import com.lifeix.cbs.message.service.placard.PlacardTempletService;
import com.lifeix.cbs.message.util.TransformDtoToVoUtil;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * Created by lhx on 15-10-19 下午4:21
 *
 * @Description
 */
@Service("placardTempletService")
public class PlacardTempletServiceImpl extends ImplSupport implements PlacardTempletService {

    @Autowired
    private PlacardTempletDao placardTempletDao;

    @Autowired
    private PlacardDataDao placardDataDao;

    @Autowired
    private RedisMessageHandler redisMessageHandler;

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponUserService couponUserService;

    /**
     * 更新系统公告
     *
     * @param templetId
     * @param title
     * @param content
     * @param endTime
     * @param disableFlag
     * @param linkType
     * @param linkData
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    @Override
    public void editPlacardTemplet(Long templetId, String title, String content, String endTime, Boolean disableFlag,
	    Integer linkType, String linkData) throws L99IllegalParamsException, L99IllegalDataException {
	Date endDate = null;
	Integer linkTypeOld = 0;
	CouponResponse couponNew = null;

	try {
	    if (endTime != null) {
		endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endTime);
	    }
	} catch (ParseException e) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}

	// 校验龙筹券是否存在
	try {
	    if (linkType == 6) {
		couponNew = couponService.findById(Long.parseLong(linkData));

		if (couponNew == null) {
		    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
		}
	    }
	} catch (Exception e) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}

	// 删除 content 中图片的宽高，适配手机显示
	if (StringUtils.isNotEmpty(content)) {
	    Document root = Jsoup.parse(content);
	    Elements imageElements = root.getElementsByTag("img");
	    if ((imageElements != null) && (imageElements.size() >= 0)) {
		for (Element imageElement : imageElements) {
		    // imageElement.attr("width", "100%");
		    imageElement.removeAttr("width");
		    imageElement.removeAttr("height");
		    imageElement.removeAttr("style");
		}
	    }
	    content = root.body().html();
	}

	boolean flag = false;
	if (templetId == null) {
	    PlacardTemplet placardTemplet = new PlacardTemplet();
	    placardTemplet.setTitle(title);
	    placardTemplet.setContent(content);
	    placardTemplet.setCreateTime(new Date());

	    placardTemplet.setEndTime(endDate);
	    placardTemplet.setDisableFlag(disableFlag);
	    placardTemplet.setPlacardCount(0);
	    placardTemplet.setLinkType(linkType);
	    placardTemplet.setLinkData(linkData);
	    placardTemplet = placardTempletDao.insert(placardTemplet);
	    templetId = placardTemplet.getTempletId();
	    if (templetId > 0) {
		flag = true;
	    }
	} else {
	    PlacardTemplet placardTemplet = placardTempletDao.findById(templetId);
	    if (placardTemplet == null) {
		throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	    }
	    linkTypeOld = linkType;
	    placardTemplet.setTitle(title);
	    placardTemplet.setContent(content);
	    placardTemplet.setEndTime(endDate);
	    placardTemplet.setDisableFlag(disableFlag);
	    placardTemplet.setLinkType(linkType);
	    placardTemplet.setLinkData(linkData);
	    flag = placardTempletDao.update(placardTemplet);
	}
	if (!flag) {
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}

	// 6发送龙筹券公告
	if (linkType == 6) {

	    int rangeKey = couponNew.getRange_key();// 关联类型
	    String rangeValue = couponNew.getRange_value();// 关联赛事

	    String[] rangeArray = rangeValue.split(",");

	    // 若修改龙筹券id，则删除之前关联
	    if (linkTypeOld == 6) {
		delRelation(linkData);
	    }

	    if (disableFlag) {
		// 添加关联
		Long validTime = (endDate.getTime() - new Date().getTime()) / 1000;

		for (int i = 0; i < rangeArray.length; i++) {
		    redisMessageHandler.addRelation(rangeArray[i], String.valueOf(templetId), validTime, rangeKey);
		}
	    }
	}
    }

    private void delRelation(String linkData) throws L99IllegalParamsException {
	// 删除之前关联
	CouponResponse couponOld = null;
	try {
	    couponOld = couponService.findById(Long.parseLong(linkData));
	} catch (Exception e) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}

	int rangeKeyOld = couponOld.getRange_key();

	String[] rangeArrayOld = couponOld.getRange_value().split(",");

	for (int i = 0; i < rangeArrayOld.length; i++) {
	    redisMessageHandler.delRelation(rangeArrayOld[i], rangeKeyOld);
	}
    }

    /**
     * push公告
     *
     * @param templetId
     * @throws L99IllegalParamsException
     * @throws JSONException
     */
    @Override
    public void pushPlacardTemplet(Long templetId) throws L99IllegalParamsException, JSONException {

	ParamemeterAssert.assertDataNotNull(templetId);

	PlacardTemplet placardTemplet = placardTempletDao.findById(templetId);
	if (placardTemplet == null || placardTemplet.getEndTime().before(new Date())) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}

	// push公告
	Map<String, Object> customContent = new HashMap<String, Object>();
	customContent.put("templetId", placardTemplet.getTempletId());
	customContent.put("title", placardTemplet.getTitle());
	customContent.put("type", placardTemplet.getLinkType());
	customContent.put("link", placardTemplet.getLinkData());
	customContent.put("push_type", ContentConstants.PushType.NOTICE_MSG);
	XingePushUtil.getInstance().pushAllDevice("", placardTemplet.getTitle(), customContent);
    }

    /**
     * 系统公告列表
     *
     * @param disableFlag
     * @param nowPage
     * @param limit
     * @return
     */
    @Override
    public PlacardTempletListResponse findPlacardTemplets(Boolean disableFlag, int nowPage, int limit) {
	limit = Math.min(limit, 100);
	limit = Math.max(limit, 1);

	int number = placardTempletDao.getPlacardTempletCount(disableFlag);
	int dataNum = (int) Math.ceil(number * 1.0D / limit);
	nowPage = dataNum == 0 ? 1 : nowPage <= dataNum ? nowPage <= 0 ? 1 : nowPage : dataNum;
	int start = (nowPage - 1) * limit;
	List<PlacardTemplet> templets = placardTempletDao.findPlacardTemplet(disableFlag, true, start, limit);

	List<PlacardTempletResponse> placard_templets = new ArrayList<PlacardTempletResponse>();
	for (PlacardTemplet templet : templets) {
	    placard_templets.add(TransformDtoToVoUtil.transformPlacardTempletResponse(templet, false));
	}

	PlacardTempletListResponse reponse = new PlacardTempletListResponse();
	reponse.setPlacard_templets(placard_templets);
	reponse.setNow_page(nowPage);
	reponse.setNumber(number);
	reponse.setPage_num(dataNum);
	reponse.setLimit(limit);
	return reponse;
    }

    /**
     * 单个公告信息
     *
     * @param templetId
     * @param userFlag
     * @return
     * @throws L99IllegalParamsException
     */
    @Override
    public PlacardTempletResponse viewPlacardTemplet(Long templetId, boolean userFlag) throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(templetId);

	PlacardTemplet placardTemplet = placardTempletDao.findById(templetId);
	if (placardTemplet == null) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}

	if (userFlag) {
	    // 用户访问+1
	    placardTempletDao.placardCount(templetId);
	}

	return TransformDtoToVoUtil.transformPlacardResponse(placardTemplet, false);
    }

    /**
     * 个人用户未读公告
     *
     * @param userId
     * @return
     */
    @Override
    public int unreadNum(Long userId) throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(userId);
	PlacardData placardData = placardDataDao.findById(userId);
	if (placardData == null) {
	    return placardTempletDao.getPlacardTempletCount(true);
	} else {
	    // 用户有浏览公告的记录，要获取所有的公告，和用户的阅读时间做比较，得出未读数量
	    List<PlacardTemplet> templets = placardTempletDao.findPlacardTemplet(true, false, 0, 100);
	    int num = 0;
	    for (PlacardTemplet templet : templets) {
		if (templet.getCreateTime().after(placardData.getReadTime())) {
		    num++;
		}
	    }
	    return num;
	}
    }

    @Override
    public void deletePlacardTemplet(Long templetId) throws L99IllegalParamsException, L99IllegalDataException {
	ParamemeterAssert.assertDataNotNull(templetId);

	PlacardTemplet placardTemplet = placardTempletDao.findById(templetId);

	// 校验公告是否存在
	if (placardTemplet == null) {
	    throw new L99IllegalDataException(BasicMsg.CODE_OPERATE_FAIL, BasicMsg.KEY_OPERATE_FAIL);
	}

	if (!placardTempletDao.delete(templetId)) {
	    throw new L99IllegalDataException(BasicMsg.CODE_OPERATE_FAIL, BasicMsg.KEY_OPERATE_FAIL);
	}

	// 删除公告相关联的赛事龙筹发放
	try {
	    if (placardTemplet.getLinkType() == 6) {
		CouponResponse coupon = couponService.findById(Long.parseLong(placardTemplet.getLinkData()));

		if (coupon != null) {
		    int rangeKeyOld = coupon.getRange_key();
		    String[] rangeArrayOld = coupon.getRange_value().split(",");

		    for (int i = 0; i < rangeArrayOld.length; i++) {
			redisMessageHandler.delRelation(rangeArrayOld[i], rangeKeyOld);
		    }
		}
	    }
	} catch (Exception e) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}
    }

    @Override
    public PlacardTempletListResponse findPlacardsInner(Long startId, Integer limit) {
	List<PlacardTemplet> templets = placardTempletDao.findPlacardsInner(startId, limit);
	List<PlacardTempletResponse> placard_templets = new ArrayList<PlacardTempletResponse>();
	for (PlacardTemplet templet : templets) {
	    placard_templets.add(TransformDtoToVoUtil.transformPlacardTempletResponse(templet, false));
	}
	PlacardTempletListResponse reponse = new PlacardTempletListResponse();
	reponse.setPlacard_templets(placard_templets);
	return reponse;
    }

    @Override
    public String findPlacardRelation(Long cupId, Long contestId, int type) {
	String ret = "";
	if (type == ContestType.FOOTBALL) {
	    ret = redisMessageHandler.getRelation(String.valueOf(contestId), RangeKey.FB_COUPON_CONTEST);// 足球赛事关联

	    if (StringUtils.isEmpty(ret)) {
		ret = redisMessageHandler.getRelation(String.valueOf(cupId), RangeKey.FB_COUPON_CUP);// 足球杯赛关联
	    }
	} else if (type == ContestType.BASKETBALL) {
	    ret = redisMessageHandler.getRelation(String.valueOf(contestId), RangeKey.BB_COUPON_CONTEST);// 篮球赛事关联

	    if (StringUtils.isEmpty(ret)) {
		ret = redisMessageHandler.getRelation(String.valueOf(cupId), RangeKey.BB_COUPON_CUP);// 篮球杯赛关联
	    }
	} else if (type == ContestType.YAYA) {
	    ret = redisMessageHandler.getRelation(String.valueOf(contestId), RangeKey.COUPON_YY);// 押押关联
	}
	return ret;
    }

    /**
     * 一键领取龙筹卷
     * 
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     * @throws NumberFormatException
     */
    @Override
    public void oneKeyCoupon(Long templetId, Long userId) throws L99IllegalParamsException, NumberFormatException,
	    L99IllegalDataException {
	ParamemeterAssert.assertDataNotNull(templetId, userId);
	PlacardTemplet placardTemplet = placardTempletDao.findById(templetId);
	if (placardTemplet == null) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}
	if (placardTemplet.getLinkType() != 6 && StringUtils.isEmpty(placardTemplet.getLinkData())) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}
	couponUserService.grantCoupon(Long.parseLong(placardTemplet.getLinkData()), userId, false);
    }
}
