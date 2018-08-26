package com.lifeix.cbs.mall.impl.goods;

import java.util.*;

import javax.annotation.Resource;

import com.lifeix.cbs.mall.bean.goods.*;
import com.lifeix.cbs.mall.bean.order.MallRecommendListResponse;
import com.lifeix.cbs.mall.service.goods.MallCategoryService;
import com.lifeix.cbs.mall.service.order.MallRecommendService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.common.exception.MsgCode;
import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.mall.common.MallConstants;
import com.lifeix.cbs.mall.dao.goods.MallGoodsDao;
import com.lifeix.cbs.mall.dto.goods.MallGoods;
import com.lifeix.cbs.mall.impl.transform.MallGoodsTransformUtil;
import com.lifeix.cbs.mall.service.goods.MallGoodsService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;


/**
 * Created by lhx on 16-3-21 下午4:19
 *
 * @Description
 */
@Service("mallGoodsService")
public class MallGoodsServiceImpl extends ImplSupport implements MallGoodsService {

    private final String SPECIFICATIONS = this.getClass().getName() + ":specifications";
    private final String RECOMMENDID = this.getClass().getName() + ":categoryId:";

    @Resource(name = "srt")
    private StringRedisTemplate srt;

    @Autowired
    private MallGoodsDao mallGoodsDao;

    @Autowired
    private MallCategoryService mallCategoryService;

    @Autowired
    private MallRecommendService mallRecommendService;

    @Override
    public MallGoodsResponse insert(Long category_id, String name, String descr, String path, String path_more,
                                    Double price, Double original, Integer sales, Integer stock, Double postage, Integer status, Integer type,
                                    String ex_prop, Integer sort_index) throws L99IllegalDataException, L99IllegalParamsException {
        MallGoods mallGoods = new MallGoods();
        mallGoods.setCategoryId(category_id);
        mallGoods.setName(name);
        mallGoods.setDescr(descr);
        mallGoods.setPath(path);
        mallGoods.setPathMore(path_more);
        mallGoods.setPrice(price);
        mallGoods.setOriginal(original);
        mallGoods.setSales(1);
        mallGoods.setStock(stock);
        mallGoods.setPostage(postage);
        mallGoods.setStatus(status);
        mallGoods.setType(type);
        mallGoods.setExProp(ex_prop);
        mallGoods.setSortIndex(sort_index);
        mallGoods.setCreateTime(new Date());
        mallGoods.setUpdateTime(new Date());
        mallGoods = mallGoodsDao.insert(mallGoods);
        if (mallGoods.getId() == null) {
            throw new L99IllegalDataException(MsgCode.BasicMsg.CODE_OPERATE_FAIL, MsgCode.BasicMsg.KEY_OPERATE_FAIL);
        } else {
            //给该类别的数量加1
            mallCategoryService.incrementNum(category_id);
        }
        return MallGoodsTransformUtil.toMallGoodsResponse(mallGoods, false);
    }

    @Override
    public MallGoodsListResponse getCategoryGoodsList(Long categoryId, Long startId, Integer limit)
            throws L99IllegalParamsException {
        ParamemeterAssert.assertDataNotNull(categoryId);
        MallGoodsListResponse mallGoodsListResponse = new MallGoodsListResponse();
        List<MallGoods> list = mallGoodsDao.findGoodsByCategory(categoryId, startId, limit);
        List<MallGoodsResponse> mallGoodsResponseList = new ArrayList<MallGoodsResponse>();
        if (list.size() > 0) {
            long startIdTmp = Long.MAX_VALUE;
            for (MallGoods mallGoods : list) {
                //不需要的信息不返回，节约流量
                mallGoods.setDescr(null);
                mallGoods.setUpdateTime(null);
                mallGoods.setCreateTime(null);
                mallGoods.setPathMore(null);
                mallGoods.setExProp(null);
                //排序根据sortIndex，因此要计算最小的startId返回
                int sortIndex = mallGoods.getSortIndex();
                if (startIdTmp > sortIndex) {
                    startIdTmp = sortIndex;
                }
                mallGoodsResponseList.add(MallGoodsTransformUtil.toMallGoodsResponse(mallGoods));
            }
            mallGoodsListResponse.setGoods_list(mallGoodsResponseList);
            mallGoodsListResponse.setStartId(startIdTmp);
        }

        return mallGoodsListResponse;
    }

    @Override
    public void delete(Long id) throws L99IllegalParamsException, L99IllegalDataException {
        ParamemeterAssert.assertDataNotNull(id);
        boolean flag = mallGoodsDao.delete(id);
        if (!flag) {
            throw new L99IllegalDataException(MsgCode.BasicMsg.CODE_OPERATE_FAIL, MsgCode.BasicMsg.KEY_OPERATE_FAIL);
        }
    }

    @Override
    public MallGoodsResponse getOneMallGoods(Long id, boolean isInner) throws L99IllegalParamsException,
            L99IllegalDataException {
        ParamemeterAssert.assertDataNotNull(id);
        MallGoods mallGoods = mallGoodsDao.findById(id);
        if (!isInner) {
            // 外部调用要确定商品为上架状态
            if (mallGoods.getStatus() != MallConstants.GoodsStatus.ON) {
                throw new L99IllegalDataException(MsgCode.GoodsMsg.CODE_GOODS_NO_FOUND, MsgCode.GoodsMsg.GOODS_NO_FOUND);
            }
        }
        return MallGoodsTransformUtil.toMallGoodsResponse(mallGoods, false);
    }

    @Override
    public MallGoodsListResponse getMallGoodsInner(Long categoryId, Integer status, Long startId, Integer limit) {
        MallGoodsListResponse mallGoodsListResponse = new MallGoodsListResponse();
        List<MallGoods> list = mallGoodsDao.findGoodsInner(categoryId, status, startId, limit);
        List<MallGoodsResponse> mallGoodsResponseList = new ArrayList<>();
        if (list.size() > 0) {
            //遍历所有推荐keys，找出keys里面的推荐商品ID
            Set<String> keySets = srt.keys(RECOMMENDID + "*");
            Set<String> idSet = new HashSet<>();
            for (String keySet : keySets) {
                Set<String> ids = srt.opsForZSet().range(keySet, 0, Long.MAX_VALUE);
                idSet.addAll(ids);
            }
            for (MallGoods mallGoods : list) {
                //列表不需要的数据
                mallGoods.setDescr(null);
                mallGoods.setPathMore(null);
                MallGoodsResponse mallGoodsResponse = MallGoodsTransformUtil.toMallGoodsResponse(mallGoods);
                boolean recommend = idSet.contains(mallGoodsResponse.getId() + "");
                mallGoodsResponse.setRecommend(recommend);
                mallGoodsResponseList.add(mallGoodsResponse);
            }
        }

        mallGoodsListResponse.setGoods_list(mallGoodsResponseList);
        return mallGoodsListResponse;
    }

    @Override
    public void updateMallGoods(Long id, Long category_id, String name, String descr, String path, String path_more,
                                Double price, Double original, Integer sales, Integer stock, Double postage, Integer status, Integer type,
                                String ex_prop, Integer sort_index) throws L99IllegalDataException, L99IllegalParamsException {
        ParamemeterAssert.assertDataNotNull(id);
        MallGoods mallGoods = new MallGoods();
        mallGoods.setId(id);
        mallGoods.setCategoryId(category_id);
        mallGoods.setName(name);
        mallGoods.setDescr(descr);
        mallGoods.setPath(path);
        mallGoods.setPathMore(path_more);
        mallGoods.setPrice(price);
        mallGoods.setOriginal(original);
        mallGoods.setSales(sales);
        mallGoods.setStock(stock);
        mallGoods.setPostage(postage);
        mallGoods.setStatus(status);
        mallGoods.setType(type);
        mallGoods.setExProp(ex_prop);
        mallGoods.setSortIndex(sort_index);
        if (category_id != null){
            //删除该类别下的该商品的推荐
            String key = RECOMMENDID + category_id;
            srt.opsForZSet().remove(key, id+"");
        }
        boolean flag = mallGoodsDao.update(mallGoods);
        if (!flag) {
            throw new L99IllegalDataException(MsgCode.BasicMsg.CODE_OPERATE_FAIL, MsgCode.BasicMsg.KEY_OPERATE_FAIL);
        }
    }

    @Override
    public void saveSpecifications(String key, String value) throws L99IllegalParamsException {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            throw new L99IllegalParamsException(MsgCode.BasicMsg.CODE_PARAMEMETER, MsgCode.BasicMsg.KEY_PARAMEMETER);
        }
        srt.opsForHash().put(SPECIFICATIONS, key, value);
    }

    @Override
    public MallGoodsResponse getSpecificationsKeys() {
        Set<Object> set = srt.opsForHash().keys(SPECIFICATIONS);
        MallGoodsResponse mallGoodsResponse = new MallGoodsResponse();
        mallGoodsResponse.setSpec_keys(set.toArray());
        return mallGoodsResponse;
    }

    @Override
    public MallGoodsResponse getSpecifications(String key) {
        MallGoodsResponse mallGoodsResponse = new MallGoodsResponse();
        String value = (String) srt.opsForHash().get(SPECIFICATIONS, key);

        if (StringUtils.isNotEmpty(value)) {
            String[] strings = value.split(",");
            mallGoodsResponse.setSpec_values(strings);
        }
        return mallGoodsResponse;
    }

    @Override
    public void saveRecommendId(String id, boolean top) throws L99IllegalParamsException {
        ParamemeterAssert.assertDataNotNull(id);
        MallGoods goods = mallGoodsDao.findById(Long.valueOf(id));
        String key = RECOMMENDID + goods.getCategoryId();
        if (top) {
            Double score = srt.opsForZSet().score(key, id);
            if (score != null) {
                srt.opsForZSet().add(key, id, score + 300_0000);
            } else {
                //13位时间戳，取中间七位xx xxxxxxx xxxx，115天的时间长度
                double time = new Date().getTime() / 10000 % 10000000;
                srt.opsForZSet().add(key, id, time + 300_0000);
            }
        } else {
            //13位时间戳，取中间七位xxx xxxxxxx xxx，115天的时间长度
            double time = new Date().getTime() / 10000 % 10000000;
            srt.opsForZSet().add(key, id, time);
        }
    }

    @Override
    public void deleteRecommendId(String id) throws L99IllegalParamsException {
        MallGoods goods = mallGoodsDao.findById(Long.valueOf(id));
        String key = RECOMMENDID + goods.getCategoryId();
        srt.opsForZSet().remove(key, id);
    }

    @Override
    public MallIndexResponse getMallIndex(int recommendCategorySize) throws L99IllegalParamsException {
        MallIndexResponse mallIndexResponse = new MallIndexResponse();
        //获得推荐商品Id
//        Set<String> goodsSet = srt.opsForZSet().reverseRangeByScore(RECOMMENDID, 0, Double.MAX_VALUE);
//        List<Long> goodsList = new ArrayList<>();
//        for (String s : goodsSet) {
//            goodsList.add(Long.valueOf(s));
//        }
//        Map<Long, MallGoods> goodsMap = mallGoodsDao.findByIds(goodsList);
//        MallGoodsListResponse mallGoodsListResponse = new MallGoodsListResponse();
//        List<MallGoods> list = new ArrayList<>();
//        for (String s : goodsSet) {
//            MallGoods mallGoods = goodsMap.get(Long.valueOf(s));
//            if (mallGoods != null){
//                //主页，不需要描述信息（这个字段信息量大，浪费流量，因此设为null）
//                mallGoods.setDescr(null);
//                mallGoods.setUpdateTime(null);
//                mallGoods.setCreateTime(null);
//                mallGoods.setPathMore(null);
//                mallGoods.setExProp(null);
//                list.add(mallGoods);
//            }
//        }
//        List<MallGoodsResponse> mallGoodsResponseList = new ArrayList<>();
//        for (MallGoods mallGoods : list) {
//            mallGoodsResponseList.add(MallGoodsTransformUtil.toMallGoodsResponse(mallGoods));
//        }
//        mallGoodsListResponse.setGoods_list(mallGoodsResponseList);
//        mallIndexResponse.setMallGoodsListResponse(mallGoodsListResponse);
        //获得轮播商品条目
        MallRecommendListResponse mallRecommendListResponse = mallRecommendService.findMallRecommends();
        mallIndexResponse.setMallRecommendListResponse(mallRecommendListResponse);
        //获得推荐商品类别
        MallCategoryListResponse mallCategoryListResponse = mallCategoryService.getRecommendCategoryList(recommendCategorySize);
        List<MallCategoryResponse> categoryResponseList = mallCategoryListResponse.getCategory();
        //循环每个类别，获得推荐商品，塞入商品集合（版本4.2修改）
        for (MallCategoryResponse mallCategoryResponse : categoryResponseList) {
            Long categoryId = mallCategoryResponse.getId();
            String key = RECOMMENDID + categoryId ;
            //取每个类别的前四个
            Set<String> goodsSet = srt.opsForZSet().reverseRangeByScore(key, 0, Double.MAX_VALUE, 0, 4);
            List<Long> goodsList = new ArrayList<>();
            for (String s : goodsSet) {
                goodsList.add(Long.valueOf(s));
            }
            Map<Long, MallGoods> goodsMap = mallGoodsDao.findByIds(goodsList);
            List<MallGoods> list = new ArrayList<>();
            for (Long aLong : goodsMap.keySet()) {
                MallGoods mallGoods = goodsMap.get(aLong);
                //主页，不需要描述信息（这个字段信息量大，浪费流量，因此设为null）
                mallGoods.setDescr(null);
                mallGoods.setUpdateTime(null);
                mallGoods.setCreateTime(null);
                mallGoods.setPathMore(null);
                mallGoods.setExProp(null);
                list.add(mallGoods);
            }
            List<MallGoodsResponse> mallGoodsResponseList = new ArrayList<>();
            for (MallGoods mallGoods : list) {
                mallGoodsResponseList.add(MallGoodsTransformUtil.toMallGoodsResponse(mallGoods));
            }
            mallCategoryResponse.setGoods(mallGoodsResponseList);
        }
        mallIndexResponse.setMallCategoryListResponse(mallCategoryListResponse);
        return mallIndexResponse;
    }
}
