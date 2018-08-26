package com.lifeix.cbs.content.impl.contest;

import com.google.gson.Gson;
import com.lifeix.cbs.api.common.exception.MsgCode;
import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.content.bean.contest.ContestNewsContentResponse;
import com.lifeix.cbs.content.bean.contest.ContestNewsListResponse;
import com.lifeix.cbs.content.dao.contest.ContestNewsDao;
import com.lifeix.cbs.content.dto.contest.ContestNews;
import com.lifeix.cbs.content.impl.transform.ContestNewsTransformUtil;
import com.lifeix.cbs.content.service.contest.ContestNewsService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import lifeix.framwork.util.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Created by lhx on 16-4-15 上午10:00
 *
 * @Description
 */
@Service("contestNewsService")
public class ContestNewsServiceImpl extends ImplSupport implements ContestNewsService {

    @Autowired
    private ContestNewsDao contestNewsDao;

    @Override
    public ContestNewsListResponse findNews(Long contestId, Integer contestType) throws L99IllegalParamsException {
        ParamemeterAssert.assertDataNotNull(contestId, contestType);
        List<ContestNews> list = contestNewsDao.findNews(contestId, contestType);
        ContestNewsListResponse contestNewsListResponse = ContestNewsTransformUtil.toList(list);
        return contestNewsListResponse;
    }

    @Override
    public boolean hasNews(Long contestId, Integer contestType) throws L99IllegalParamsException {
        ParamemeterAssert.assertDataNotNull(contestId, contestType);
        List<ContestNews> list = contestNewsDao.findNews(contestId, contestType);
        return list.size() > 0;
    }

    @Override
    public ContestNewsListResponse findNewsInner(Long contestId, Integer contestType, Integer status, Long startId,
                                                 Long endId, Integer skip, Integer limit) {
        List<ContestNews> list = contestNewsDao.findNewsInner(contestId, contestType, status, startId, endId, skip, limit);
        if (endId != null) {
            Collections.reverse(list);
        }
        int count = contestNewsDao.count(contestId, contestType, status);
        ContestNewsListResponse contestNewsListResponse = ContestNewsTransformUtil.toList(list);
        contestNewsListResponse.setNumber(count);
        return contestNewsListResponse;
    }

    // @Override
    // public ContestNewsResponse view(Long id) throws L99IllegalParamsException
    // {
    // ParamemeterAssert.assertDataNotNull(id);
    // ContestNews contestNews = contestNewsDao.findOne(id);
    // return null;
    // }

    @Override
    public long add(String title, String desc, String image, Long contentId, Long contestId, Integer contestType)
            throws L99IllegalDataException {
        ContestNews contestNews = new ContestNews();
        contestNews.setContentId(contentId);
        contestNews.setContestId(contestId);
        contestNews.setContestType(contestType);
        ContestNewsContentResponse contestNewsContentResponse = new ContestNewsContentResponse();
        contestNewsContentResponse.setTitle(title);
        contestNewsContentResponse.setDesc(desc);
        contestNewsContentResponse.setImage(image);
        String content = JsonUtils.toJsonString(contestNewsContentResponse);
        contestNews.setContent(content);
        long id = contestNewsDao.insert(contestNews);
        if (id == -1) {
            throw new L99IllegalDataException(MsgCode.BasicMsg.CODE_OPERATE_FAIL, MsgCode.BasicMsg.KEY_OPERATE_FAIL);
        }
        return id ;
    }

    @Override
    public void edit(Long id, String title, String desc, String image, Long contentId, Long contestId, Integer contestType, Integer status)
            throws L99IllegalDataException, L99IllegalParamsException {
        ParamemeterAssert.assertDataNotNull(id);
        ContestNews contestNewsOld = contestNewsDao.findOne(id);
        if (contestNewsOld == null) {
            throw new L99IllegalDataException(MsgCode.ContentMsg.CODE_CONTENT_NOT_FOUND,
                    MsgCode.ContentMsg.KEY_CONTENT_NOT_FOUND);
        }
        ContestNews contestNews = new ContestNews();
        contestNews.setId(id);
        contestNews.setContentId(contentId);
        contestNews.setContestId(contestId);
        contestNews.setContestType(contestType);
        contestNews.setStatus(status);
        if (StringUtils.isNotEmpty(title) || StringUtils.isNotEmpty(desc) || StringUtils.isNotEmpty(image)) {
            ContestNewsContentResponse contestNewsContentResponse = new Gson().fromJson(contestNewsOld.getContent(),
                    ContestNewsContentResponse.class);
            if (StringUtils.isNotEmpty(title)) {
                contestNewsContentResponse.setTitle(title);
            }
            if (StringUtils.isNotEmpty(desc)) {
                contestNewsContentResponse.setDesc(desc);
            }
            if (StringUtils.isNotEmpty(image)) {
                contestNewsContentResponse.setImage(image);
            }
            contestNews.setContent(JsonUtils.toJsonString(contestNewsContentResponse));
        }
        contestNewsDao.edit(contestNews);
    }

}
