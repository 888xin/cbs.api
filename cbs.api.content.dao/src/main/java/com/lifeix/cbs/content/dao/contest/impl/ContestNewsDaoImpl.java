package com.lifeix.cbs.content.dao.contest.impl;

import com.lifeix.cbs.content.dao.ContentDaoSupport;
import com.lifeix.cbs.content.dao.contest.ContestNewsDao;
import com.lifeix.cbs.content.dto.contest.ContestNews;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lhx on 16-4-14 下午5:47
 *
 * @Description
 */
@Repository("contestNewsDao")
public class ContestNewsDaoImpl extends ContentDaoSupport implements ContestNewsDao {

    private final String name = this.getClass().getName() + ":add:";

    @Override
    public long insert(ContestNews contestNews) {
        boolean flag = sqlSession.insert("ContestNewsMapper.insert", contestNews) > 0;
        long id = -1 ;
        if (flag){
            String key = String.format(name + ":%d:%d:", contestNews.getContestId(), contestNews.getContestType());
            memcacheService.delete(key);
            id = contestNews.getId() ;
        }
        return id ;
    }

    @Override
    public boolean edit(ContestNews contestNews) {
        //修改，需要把旧的信息从缓存中删除
        ContestNews contestNewsOld = this.findOne(contestNews.getId());
        String keyOld = String.format(name + ":%d:%d:", contestNewsOld.getContestId(), contestNewsOld.getContestType());
        memcacheService.delete(keyOld);
        boolean flag = sqlSession.update("ContestNewsMapper.update", contestNews) > 0;
        if (flag){
            //让key值准确
            if (contestNews.getContestId() != null || contestNews.getContestType() != null){
                if (contestNews.getContestId() == null){
                    contestNews.setContestId(contestNewsOld.getContestId());
                }
                if (contestNews.getContestType() == null){
                    contestNews.setContestType(contestNewsOld.getContestType());
                }
                String key = String.format(name + ":%d:%d:", contestNews.getContestId(), contestNews.getContestType());
                memcacheService.delete(key);
            }
        }
        return flag;
    }

    @Override
    public List<ContestNews> findNews(Long contestId, Integer contestType) {
        String key = String.format(name + ":%d:%d:", contestId, contestType);
        List<ContestNews> list = memcacheService.get(key);
        if (list == null){
            ContestNews contestNews = new ContestNews();
            contestNews.setContestId(contestId);
            contestNews.setContestType(contestType);
            list = sqlSession.selectList("ContestNewsMapper.find", contestNews);
            memcacheService.set(key,list);
        }
        return list;
    }

    @Override
    public List<ContestNews> findNewsInner(Long contestId, Integer contestType, Integer status,Long startId,
                                           Long endId, Integer skip, Integer limit) {
        Map<String, Object> params = new HashMap<>();
        params.put("contestId", contestId);
        params.put("contestType", contestType);
        params.put("status", status);
        params.put("startId", startId);
        params.put("endId", endId);
        params.put("skip", skip);
        params.put("limit", limit);
        List<ContestNews> list = sqlSession.selectList("ContestNewsMapper.findInner", params);
        return list ;
    }

    @Override
    public Integer count(Long contestId, Integer contestType, Integer status) {
        ContestNews contestNews = new ContestNews();
        contestNews.setContestId(contestId);
        contestNews.setContestType(contestType);
        contestNews.setStatus(status);
        return sqlSession.selectOne("ContestNewsMapper.count", contestNews);
    }

    @Override
    public ContestNews findOne(Long id) {
        return sqlSession.selectOne("ContestNewsMapper.findOne", id);
    }
}
