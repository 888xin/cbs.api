package com.lifeix.cbs.content.bean.shicai;

import java.util.List;



import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;


/**
 * @author wenhuans
 * 2015年11月6日 上午9:29:58
 * 
 */
public class ShicaiRecordListResponse extends ListResponse implements Response{

    /**
     * 
     */
    private static final long serialVersionUID = -4813779069644530295L;
    
    
    private List<ShicaiRecordResponse> record;
    
    public List<ShicaiRecordResponse> getRecord() {
        return record;
    }

    public void setRecord(List<ShicaiRecordResponse> record) {
        this.record = record;
    }

    @Override
    public String getObjectName() {
	return null;
    }



}

