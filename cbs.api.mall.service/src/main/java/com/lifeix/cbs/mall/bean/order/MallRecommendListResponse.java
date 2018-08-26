/**
 * 
 */
package com.lifeix.cbs.mall.bean.order;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

/**
 * @author lifeix
 * 
 */
public class MallRecommendListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = 8454173728406821446L;

    /**
     * 商品导航列表
     * 
     * */
    private List<MallRecommendResponse> mallRecommends;

    public List<MallRecommendResponse> getMallRecommends() {
	return mallRecommends;
    }

    public void setMallRecommends(List<MallRecommendResponse> mallRecommends) {
	this.mallRecommends = mallRecommends;
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
