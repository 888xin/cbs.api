package com.lifeix.cbs.mall.impl.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.common.exception.MsgCode;
import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.mall.bean.order.MallRecommendListResponse;
import com.lifeix.cbs.mall.bean.order.MallRecommendResponse;
import com.lifeix.cbs.mall.dao.order.MallRecommendDao;
import com.lifeix.cbs.mall.dto.order.MallRecommend;
import com.lifeix.cbs.mall.impl.transform.MallOrderTransformUtil;
import com.lifeix.cbs.mall.service.order.MallRecommendService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * @author suny
 * @date 2016.3.22
 */
@Service("mallRecommendService")
public class MallRecommendServiceImpl extends ImplSupport implements MallRecommendService {

    @Autowired
    private MallRecommendDao mallRecommendDao;

    /**
     * 获取商品导航列表
     * 
     * @param startId
     * @param limit
     * @return
     * @throws L99IllegalParamsException
     */
    @Override
    public MallRecommendListResponse findMallRecommends() throws L99IllegalParamsException {
	List<MallRecommend> mallRecommends = mallRecommendDao.findMallRecommends();
	List<MallRecommendResponse> list = new ArrayList<MallRecommendResponse>();
	for (MallRecommend recommend : mallRecommends) {
	    list.add(MallOrderTransformUtil.transformMallRecommend(recommend));
	}
	MallRecommendListResponse mallRecommendListResponse = new MallRecommendListResponse();
	mallRecommendListResponse.setMallRecommends(list);
	return mallRecommendListResponse;
    }

    @Override
    public MallRecommendResponse insert(String image, String title, Integer type, String link, Integer sort)
	    throws L99IllegalDataException, L99IllegalParamsException, JSONException {

	ParamemeterAssert.assertDataNotNull(image, link, type);
	MallRecommend bean = new MallRecommend();
	bean.setImage(image);
	bean.setTitle(title);
	bean.setType(type);
	bean.setLink(link);
	bean.setSort(sort);
	bean.setCreateTime(new Date());
	boolean flag = mallRecommendDao.insert(bean);
	if (!flag) {
	    throw new L99IllegalDataException(MsgCode.BasicMsg.CODE_BASIC_SERVCER, MsgCode.BasicMsg.KEY_BASIC_SERVCER);
	}
	return MallOrderTransformUtil.transformMallRecommend(bean);
    }

    @Override
    public void deleteMallRecommendByid(Long id) throws L99IllegalParamsException, L99IllegalDataException {
	ParamemeterAssert.assertDataNotNull(id);
	boolean flag = mallRecommendDao.delete(id);
	if (!flag) {
	    throw new L99IllegalDataException(MsgCode.BasicMsg.CODE_BASIC_SERVCER, MsgCode.BasicMsg.KEY_BASIC_SERVCER);
	}

    }

    @Override
    public void updateRecommend(Long id, String image, String title, Integer type, String link, Integer sort)
	    throws L99IllegalDataException, L99IllegalParamsException {

	ParamemeterAssert.assertDataNotNull(id);

	MallRecommend bean = new MallRecommend();
	bean.setId(id);
	bean.setImage(image);
	bean.setTitle(title);
	bean.setType(type);
	bean.setLink(link);
	bean.setSort(sort);
	boolean flag = mallRecommendDao.update(bean);
	if (!flag) {
	    throw new L99IllegalDataException(MsgCode.BasicMsg.CODE_BASIC_SERVCER, MsgCode.BasicMsg.KEY_BASIC_SERVCER);
	}
    }

    @Override
    public MallRecommendResponse findMallRecommend(Long id) throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(id);

	MallRecommend bean = mallRecommendDao.selectById(id);
	if (bean == null) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}
	return MallOrderTransformUtil.transformMallRecommend(bean);
    }

}
