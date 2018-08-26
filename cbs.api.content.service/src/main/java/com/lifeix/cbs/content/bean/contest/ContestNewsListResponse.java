package com.lifeix.cbs.content.bean.contest;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

import java.util.List;

/**
 * Created by lhx on 16-4-15 下午3:47
 * 
 * @Description
 */
public class ContestNewsListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = 1847935321624540375L;
    private List<ContestNewsResponse> contest_news;

    public List<ContestNewsResponse> getContest_news() {
        return contest_news;
    }

    public void setContest_news(List<ContestNewsResponse> contest_news) {
        this.contest_news = contest_news;
    }

    @Override
    public String getObjectName() {
        return null;
    }
}
