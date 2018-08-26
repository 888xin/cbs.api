package com.lifeix.cbs.content.bean.frontpage;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.cbs.contest.bean.fb.ContestResponse;
import com.lifeix.user.beans.Response;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by lhx on 15-11-30 上午10:48
 *
 * @Description
 */
public class FrontPageContentResponse implements JsonSerializer<FrontPageContentResponse>, Response {

    /**
     * 赛事ID
     */
    private Long contestId ;

    /**
     * 赛事类型
     */
    private Integer contestType;

    /**
     * 描述
     */
    private String desc ;

    /**
     * 链接
     */
    private String link ;

    /**
     * 照片路径
     */
    private String image ;

    /**
     * 标题
     */
    private String title ;

    /**
     * 赛事分析，赛事新闻的id
     */
    private Long contest_news_id ;

    public Long getContest_news_id() {
        return contest_news_id;
    }

    public void setContest_news_id(Long contest_news_id) {
        this.contest_news_id = contest_news_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getContestType() {
        return contestType;
    }

    public void setContestType(Integer contestType) {
        this.contestType = contestType;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Long getContestId() {
        return contestId;
    }

    public void setContestId(Long contestId) {
        this.contestId = contestId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public JsonElement serialize(FrontPageContentResponse frontPageContentResponse, Type type, JsonSerializationContext jsonSerializationContext) {
        return null;
    }

    @Override
    public String getObjectName() {
        return null;
    }


}
