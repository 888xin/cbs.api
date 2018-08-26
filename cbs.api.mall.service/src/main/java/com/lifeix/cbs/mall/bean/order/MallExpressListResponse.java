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
public class MallExpressListResponse extends ListResponse implements Response {

    /**
     * 
     */
    private static final long serialVersionUID = 6058578072857082725L;

    private List<MallExpressResponse> expressList;

    private String startIndex;

    public List<MallExpressResponse> getExpressList() {
	return expressList;
    }

    public void setExpressList(List<MallExpressResponse> expressList) {
	this.expressList = expressList;
    }

    public String getStartIndex() {
	return startIndex;
    }

    public void setStartIndex(String startIndex) {
	this.startIndex = startIndex;
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
