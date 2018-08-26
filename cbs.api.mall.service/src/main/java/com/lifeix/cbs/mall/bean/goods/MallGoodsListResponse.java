package com.lifeix.cbs.mall.bean.goods;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

/**
 * Created by lhx on 15-10-19 下午3:47
 * 
 * @Description
 */
public class MallGoodsListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = -8739964219073438329L;

    private List<MallGoodsResponse> goods_list;

    public List<MallGoodsResponse> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<MallGoodsResponse> goods_list) {
        this.goods_list = goods_list;
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
