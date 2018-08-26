package com.lifeix.cbs.contest.bean.fb.ext;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;
/**
 * 主队历史
 * 客队历史
 * 主客交战历史
 * @author lifeix
 *
 */
public class AboutRecordListResponse extends ListResponse implements Response {

    /**
     * 
     */
    private static final long serialVersionUID = 2624919671269120725L;
    private List<RecordResponse> home_records;
    private List<RecordResponse> away_records;
    private List<RecordResponse> home_away_records;

    public List<RecordResponse> getHome_records() {
	return home_records;
    }

    public void setHome_records(List<RecordResponse> home_records) {
	this.home_records = home_records;
    }

    public List<RecordResponse> getAway_records() {
	return away_records;
    }

    public void setAway_records(List<RecordResponse> away_records) {
	this.away_records = away_records;
    }

    public List<RecordResponse> getHome_away_records() {
	return home_away_records;
    }

    public void setHome_away_records(List<RecordResponse> home_away_records) {
	this.home_away_records = home_away_records;
    }

    @Override
    public String getObjectName() {
	return null;
    }
}
