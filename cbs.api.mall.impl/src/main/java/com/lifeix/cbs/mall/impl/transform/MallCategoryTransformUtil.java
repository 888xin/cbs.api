package com.lifeix.cbs.mall.impl.transform;

import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.mall.bean.goods.MallCategoryResponse;
import com.lifeix.cbs.mall.dto.goods.MallCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhx on 16-3-22 上午9:30
 *
 * @Description
 */
public class MallCategoryTransformUtil {

    public static List<MallCategoryResponse> toMallCategoryResponseList(List<MallCategory> list) {
        List<MallCategoryResponse> MallCategoryResponseList = new ArrayList<MallCategoryResponse>();
        if (list.size() > 0) {
            MallCategoryResponse MallCategoryResponse;
            for (MallCategory MallCategory : list) {
                MallCategoryResponse = new MallCategoryResponse();
                MallCategoryResponse.setId(MallCategory.getId());
                MallCategoryResponse.setNum(MallCategory.getNum());
                MallCategoryResponse.setName(MallCategory.getName());
                MallCategoryResponse.setDescr(MallCategory.getDescr());
                MallCategoryResponse.setPath(MallCategory.getPath());
                MallCategoryResponse.setSort_index(MallCategory.getSortIndex());
                MallCategoryResponse.setStatus(MallCategory.getStatus());
                MallCategoryResponse.setCreate_time(CbsTimeUtils.getUtcTimeForDate(MallCategory.getCreateTime()));
                MallCategoryResponseList.add(MallCategoryResponse);
            }
        }
        return MallCategoryResponseList;
    }


    public static MallCategoryResponse toMallCategoryResponse(MallCategory MallCategory) {
        MallCategoryResponse MallCategoryResponse = new MallCategoryResponse();
        if (MallCategory != null) {
            MallCategoryResponse = new MallCategoryResponse();
            MallCategoryResponse.setId(MallCategory.getId());
            MallCategoryResponse.setNum(MallCategory.getNum());
            MallCategoryResponse.setName(MallCategory.getName());
            MallCategoryResponse.setDescr(MallCategory.getDescr());
            MallCategoryResponse.setPath(MallCategory.getPath());
            MallCategoryResponse.setSort_index(MallCategory.getSortIndex());
            MallCategoryResponse.setCreate_time(CbsTimeUtils.getUtcTimeForDate(MallCategory.getCreateTime()));
        }
        return MallCategoryResponse;
    }
}
