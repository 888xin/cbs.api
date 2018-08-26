package com.lifeix.cbs.api.conetent.test.lhx;

import com.lifeix.cbs.content.dao.frontpage.FrontPageDao;
import com.lifeix.cbs.content.dto.frontpage.FrontPage;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;
import java.util.List;

/**
 * Created by lhx on 15-11-27 下午3:13
 *
 * @Description
 */
public class FrontPageTest {

    private ApplicationContext ctx = null;
    private FrontPageDao frontPageDao = null;

    @Before
    public void init() {
        ctx = new ClassPathXmlApplicationContext("classpath:conf/cbs-content-spring-dao-temp.xml");
        frontPageDao = (FrontPageDao) ctx.getBean("frontPageDao");
    }

//    @Test
//    public void testInsert() {
//        FrontPage frontPage = new FrontPage();
//        frontPage.setUserId(1L);
//        frontPage.setAt("ss");
//        frontPage.setContent("sdfdsfdsfdsf");
//        frontPage.setContentId(4L);
//        frontPage.setCreateTime(new Date());
//        frontPage.setType(-2);
//        frontPageDao.insert(frontPage);
//    }
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
