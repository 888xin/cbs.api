package com.lifeix.cbs.mall.bean.goods;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

/**
 * Created by lhx on 15-10-19 下午3:47
 * 
 * @Description
 */
public class MallCategoryListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = -3168302887153034278L;

    private List<MallCategoryResponse> category;

    public List<MallCategoryResponse> getCategory() {
        return category;
    }

    public void setCategory(List<MallCategoryResponse> category) {
        this.category = category;
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
