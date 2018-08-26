package com.lifeix.cbs.content.bean.frontpage;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.cbs.contest.bean.fb.ContestResponse;
import com.lifeix.user.beans.Response;

import java.lang.reflect.Type;

/**
 * Created by lhx on 15-11-30 下午1:58
 *
 * @Description
 */
public class FrontPageResponse implements JsonSerializer<FrontPageResponse>, Response {

    private static final long serialVersionUID = 1318763851232606559L;
    /**
     * 用户
     */
    private CbsUserResponse user ;

    /**
     * 赛事
     */
    private ContestResponse contest ;

    /**
     * 竞猜相关
     */
    private FrontPageBetResponse bet;

    /**
     * 描述
     */
    private String desc ;

    /**
     * id
     */
    private Long f_id;


    /**
     * 类型 <0 广告区， >0 内容区
     */
    private Integer type;

    /**
     * 评论数
     */
    private int commet_num;

    /**
     * '@'用户组成的jsonarray
     */
    private String at;

    private String create_time ;

    /**
     * 客户端显示的时间
     */
    private String time ;


    /**
     * 照片路径
     */
    private String[] image ;

    /**
     * 链接
     */
    private String link ;

    /**
     * contentId猜友圈发表的文章Id
     */
    private Long content_id ;

    /**
     * 标题
     */
    private String title ;

    /**
     * 是否在队列中
     */
    private Boolean queue ;

    /**
     * 资讯内嵌赛事ID
     */
    private Long innner_contest_id ;

    /**
     * 资讯内嵌赛事类型
     */
    private Integer innner_contest_type ;

    public int getCommet_num() {
        return commet_num;
    }

    public void setCommet_num(int commet_num) {
        this.commet_num = commet_num;
    }

    public Long getInnner_contest_id() {
        return innner_contest_id;
    }

    public void setInnner_contest_id(Long innner_contest_id) {
        this.innner_contest_id = innner_contest_id;
    }

    public Integer getInnner_contest_type() {
        return innner_contest_type;
    }

    public void setInnner_contest_type(Integer innner_contest_type) {
        this.innner_contest_type = innner_contest_type;
    }

    public FrontPageBetResponse getBet() {
        return bet;
    }

    public void setBet(FrontPageBetResponse bet) {
        this.bet = bet;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getContent_id() {
        return content_id;
    }

    public void setContent_id(Long content_id) {
        this.content_id = content_id;
    }

    private Integer status ;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getF_id() {
        return f_id;
    }

    public void setF_id(Long f_id) {
        this.f_id = f_id;
    }

    public String[] getImage() {
        return image;
    }

    public void setImage(String[] image) {
        this.image = image;
    }

    public Boolean getQueue() {
        return queue;
    }

    public void setQueue(Boolean queue) {
        this.queue = queue;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public CbsUserResponse getUser() {
        return user;
    }

    public void setUser(CbsUserResponse user) {
        this.user = user;
    }

    public ContestResponse getContest() {
        return contest;
    }

    public void setContest(ContestResponse contest) {
        this.contest = contest;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAt() {
        return at;
    }

    public void setAt(String at) {
        this.at = at;
    }

    @Override
    public JsonElement serialize(FrontPageResponse frontPageResponse, Type type, JsonSerializationContext jsonSerializationContext) {
        return null;
    }

    @Override
    public String getObjectName() {
        return null;
    }
}
