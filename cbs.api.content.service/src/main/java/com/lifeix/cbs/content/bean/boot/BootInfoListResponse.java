package com.lifeix.cbs.content.bean.boot;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

/**
 * 开机信息列表
 * 
 * @author lifeix-sz
 * 
 */
public class BootInfoListResponse extends ListResponse implements Response {


    /**
     * 
     */
    private static final long serialVersionUID = -5153291643665908414L;
    
    private List<BootInfoResponse> boots;

    public List<BootInfoResponse> getBoots() {
	return boots;
    }

    public void setBoots(List<BootInfoResponse> boots) {
	this.boots = boots;
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
