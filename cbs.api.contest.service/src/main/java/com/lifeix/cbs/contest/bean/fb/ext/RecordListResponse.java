package com.lifeix.cbs.contest.bean.fb.ext;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class RecordListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = 4273680893879798234L;

    private List<RecordResponse> records;

    @Override
    public String getObjectName() {
	return null;
    }

    public List<RecordResponse> getRecords() {
	return records;
    }

    public void setRecords(List<RecordResponse> records) {
	this.records = records;
    }

}
