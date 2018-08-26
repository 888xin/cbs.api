package com.lifeix.cbs.content.bean.frontpage;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

import java.util.List;

/**
 * Created by lhx on 15-10-19 下午3:47
 * 
 * @Description
 */
public class FrontPageListResponse extends ListResponse implements Response {

    private List<FrontPageResponse> frontpages;

    //轮播消息
    private List<FrontPageResponse> messages;

    public List<FrontPageResponse> getMessages() {
        return messages;
    }

    public void setMessages(List<FrontPageResponse> messages) {
        this.messages = messages;
    }

    public List<FrontPageResponse> getFrontpages() {
        return frontpages;
    }

    public void setFrontpages(List<FrontPageResponse> frontpages) {
        this.frontpages = frontpages;
    }

    @Override
    public String getObjectName() {
	return null;
    }

}
