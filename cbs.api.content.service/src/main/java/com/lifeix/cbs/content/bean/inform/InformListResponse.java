/**
 * 
 */
package com.lifeix.cbs.content.bean.inform;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

/**
 * @author lifeix
 *
 */
public class InformListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = 2775485912278581962L;

    private List<InformResponse> informs;


    public List<InformResponse> getInforms() {
	return informs;
    }

    public void setInforms(List<InformResponse> informs) {
	this.informs = informs;
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
