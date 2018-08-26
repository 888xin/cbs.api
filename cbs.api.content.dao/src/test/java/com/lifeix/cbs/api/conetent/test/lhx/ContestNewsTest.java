package com.lifeix.cbs.api.conetent.test.lhx;

import com.lifeix.cbs.content.dao.contest.ContestNewsDao;
import com.lifeix.cbs.content.dao.frontpage.FrontPageDao;
import com.lifeix.cbs.content.dto.contest.ContestNews;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by lhx on 15-11-27 下午3:13
 *
 * @Description
 */
public class ContestNewsTest {

    private ApplicationContext ctx = null;
    private ContestNewsDao contestNewsDao = null;

    @Before
    public void init() {
        ctx = new ClassPathXmlApplicationContext("classpath:conf/cbs-content-spring-dao-temp.xml");
        contestNewsDao = (ContestNewsDao) ctx.getBean("contestNewsDao");
    }

    @Test
    public void testInsert() {
        ContestNews contestNews = new ContestNews();
        contestNews.setContent("test");
        contestNews.setContentId(23L);
        contestNews.setContestId(11L);
        contestNews.setContestType(1);
        long id = contestNewsDao.insert(contestNews);
        System.out.println(id);
    }
//
//    @Test
//    public void testget() {
//
//        List<FrontPage> list = frontPageDao.findFrontPages(null, 20, 100);
//        for (FrontPage frontPage : list) {
//            System.out.println(frontPage.getContent());
//        }
//    }

//    @Test
//    public void testCount() {
//        int count = frontPageDao.findFrontPagesCount(2, 0);
//        System.out.println(count);
//    }
}
