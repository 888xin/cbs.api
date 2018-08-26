package com.lifeix.cbs.content.impl.transform;

import com.google.gson.Gson;
import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.content.bean.contest.ContestNewsContentResponse;
import com.lifeix.cbs.content.bean.contest.ContestNewsListResponse;
import com.lifeix.cbs.content.bean.contest.ContestNewsResponse;
import com.lifeix.cbs.content.dto.contest.ContestNews;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhx on 16-4-15 上午10:34
 *
 * @Description
 */
public class ContestNewsTransformUtil {

    public static ContestNewsListResponse toList(List<ContestNews> list){
        ContestNewsListResponse contestNewsListResponse = new ContestNewsListResponse();
        List<ContestNewsResponse> contestNewsList = new ArrayList<>();
        if (list.size() > 0){
            for (ContestNews contestNews : list) {
                ContestNewsResponse contestNewsResponse = new ContestNewsResponse();
                contestNewsResponse.setId(contestNews.getId());
                contestNewsResponse.setContent_id(contestNews.getContentId());
                contestNewsResponse.setContest_id(contestNews.getContestId());
                contestNewsResponse.setContest_type(contestNews.getContestType());
                contestNewsResponse.setCreate_time(CbsTimeUtils.getUtcTimeForDate(contestNews.getCreateTime()));
                contestNewsResponse.setStatus(contestNews.getStatus());
                ContestNewsContentResponse contestNewsContentResponse = new Gson().fromJson(contestNews.getContent(), ContestNewsContentResponse.class);
                contestNewsResponse.setTitle(contestNewsContentResponse.getTitle());
                contestNewsResponse.setDesc(contestNewsContentResponse.getDesc());
                contestNewsResponse.setImage(contestNewsContentResponse.getImage());
                contestNewsList.add(contestNewsResponse);
            }
        }
        contestNewsListResponse.setContest_news(contestNewsList);
        return contestNewsListResponse ;
    }
}
