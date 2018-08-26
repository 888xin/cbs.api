/**
 * 
 */
package com.lifeix.cbs.contest.bean.odds;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

/**
 * @author lifeix
 *
 */
public class OddsWarnListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = -4511123970645219645L;

    private List<OddsWarnResponse> oddsWarnList;

    public List<OddsWarnResponse> getOddsWarnList() {
	return oddsWarnList;
    }

    public void setOddsWarnList(List<OddsWarnResponse> oddsWarnList) {
	this.oddsWarnList = oddsWarnList;
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
