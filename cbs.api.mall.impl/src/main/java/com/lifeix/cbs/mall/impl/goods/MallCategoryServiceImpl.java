package com.lifeix.cbs.mall.impl.goods;

import com.lifeix.cbs.api.common.exception.MsgCode;
import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.mall.bean.goods.MallCategoryListResponse;
import com.lifeix.cbs.mall.bean.goods.MallCategoryResponse;
import com.lifeix.cbs.mall.bean.goods.MallCategoryListResponse;
import com.lifeix.cbs.mall.bean.goods.MallCategoryResponse;
import com.lifeix.cbs.mall.dao.goods.MallCategoryDao;
import com.lifeix.cbs.mall.dao.goods.MallGoodsDao;
import com.lifeix.cbs.mall.dto.goods.MallCategory;
import com.lifeix.cbs.mall.dto.goods.MallCategory;
import com.lifeix.cbs.mall.impl.transform.MallCategoryTransformUtil;
import com.lifeix.cbs.mall.impl.transform.MallCategoryTransformUtil;
import com.lifeix.cbs.mall.service.goods.MallCategoryService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by lhx on 16-3-22 上午9:23
 *
 * @Description
 */
@Service("mallCategoryService")
public class MallCategoryServiceImpl extends ImplSupport implements MallCategoryService {
    
    @Autowired
    private MallCategoryDao mallCategoryDao;

    @Autowired
    private MallGoodsDao mallGoodsDao;

    @Override
    public MallCategoryResponse insert(String name, String descr, String path, Integer num, Integer sort_index) throws L99IllegalDataException {
        MallCategory mallCategory = new MallCategory();
        mallCategory.setName(name);
        mallCategory.setDescr(descr);
        mallCategory.setPath(path);
        mallCategory.setNum(num);
        mallCategory.setSortIndex(sort_index);
        mallCategory.setCreateTime(new Date());
        mallCategory = mallCategoryDao.insert(mallCategory);
        if (mallCategory.getId() == null){
            throw new L99IllegalDataException(MsgCode.BasicMsg.CODE_OPERATE_FAIL, MsgCode.BasicMsg.KEY_OPERATE_FAIL);
        }
        return MallCategoryTransformUtil.toMallCategoryResponse(mallCategory);
    }

    @Override
    public void update(Long id, String name, String descr, String path, Integer num, Integer sortIndex, Integer status) throws L99IllegalDataException, L99IllegalParamsException {
        ParamemeterAssert.assertDataNotNull(id);
        MallCategory mallCategory = new MallCategory();
        mallCategory.setId(id);
        mallCategory.setName(name);
        mallCategory.setDescr(descr);
        mallCategory.setPath(path);
        mallCategory.setNum(num);
        mallCategory.setSortIndex(sortIndex);
        mallCategory.setStatus(status);
        boolean flag = mallCategoryDao.update(mallCategory);
        if (!flag){
            throw new L99IllegalDataException(MsgCode.BasicMsg.CODE_OPERATE_FAIL, MsgCode.BasicMsg.KEY_OPERATE_FAIL);
        }
    }

    @Override
    public MallCategoryListResponse getRecommendCategoryList(Integer num) {
        MallCategoryListResponse mallCategoryListResponse = new MallCategoryListResponse();
        List<MallCategory> list = mallCategoryDao.findRecommendCategorys(num);
        List<MallCategoryResponse> mallCategoryResponseList = MallCategoryTransformUtil.toMallCategoryResponseList(list);
        mallCategoryListResponse.setCategory(mallCategoryResponseList);
        return mallCategoryListResponse;
    }

    @Override
    public MallCategoryListResponse getAllCategoryList() {
        MallCategoryListResponse mallCategoryListResponse = new MallCategoryListResponse();
        List<MallCategory> list = mallCategoryDao.findAllCategorys();
        List<MallCategoryResponse> mallCategoryResponseList = MallCategoryTransformUtil.toMallCategoryResponseList(list);
        mallCategoryListResponse.setCategory(mallCategoryResponseList);
        return mallCategoryListResponse;
    }

    @Override
    public MallCategoryListResponse getAllCategoryListInner() {
        MallCategoryListResponse mallCategoryListResponse = new MallCategoryListResponse();
        List<MallCategory> list = mallCategoryDao.findAllCategorysInner();
        List<MallCategoryResponse> mallCategoryResponseList = MallCategoryTransformUtil.toMallCategoryResponseList(list);
        mallCategoryListResponse.setCategory(mallCategoryResponseList);
        return mallCategoryListResponse;
    }

    @Override
    public MallCategoryResponse view(String name) throws L99IllegalParamsException {
        ParamemeterAssert.assertDataNotNull(name);
        MallCategory mallCategory = mallCategoryDao.findByName(name);
        return MallCategoryTransformUtil.toMallCategoryResponse(mallCategory);
    }

    @Override
    public void delete(Long id) throws L99IllegalParamsException, L99IllegalDataException {
        ParamemeterAssert.assertDataNotNull(id);
        boolean isHas = mallGoodsDao.isHasByCategory(id);
        if (isHas){
            throw new L99IllegalDataException(MsgCode.BasicMsg.CODE_OPERATE_FAIL, MsgCode.BasicMsg.KEY_OPERATE_FAIL);
        }
        boolean flag = mallCategoryDao.delete(id);
        if (!flag){
            throw new L99IllegalDataException(MsgCode.BasicMsg.CODE_OPERATE_FAIL, MsgCode.BasicMsg.KEY_OPERATE_FAIL);
        }
    }

    @Override
    public boolean incrementNum(Long id) throws L99IllegalParamsException {
        ParamemeterAssert.assertDataNotNull(id);
        return mallCategoryDao.incrementNum(id);
    }
}
