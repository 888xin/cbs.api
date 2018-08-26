package com.lifeix.cbs.api.message.placard.dao.test;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lifeix.cbs.message.dao.placard.PlacardDataDao;
import com.lifeix.cbs.message.dao.placard.PlacardTempletDao;
import com.lifeix.cbs.message.dto.PlacardData;
import com.lifeix.cbs.message.dto.PlacardTemplet;

public class PlacardDataDaoTest {

    private ApplicationContext ctx = null;
    private PlacardDataDao placardDataDao = null;
    private PlacardTempletDao placardTempletDao = null;

    @Before
    public void init() {
	ctx = new ClassPathXmlApplicationContext("classpath:conf/cbs-content-message-spring-dao-temp.xml");
	placardDataDao = (PlacardDataDao) ctx.getBean("placardDataDao");
	placardTempletDao = (PlacardTempletDao) ctx.getBean("placardTempletDao");
    }

    @Test
    public void testInsert() {
	PlacardData placardData = new PlacardData();
	placardData.setUserId(34L);
	placardData.setCreateTime(new Date());
	placardData.setReadTime(new Date());
	boolean flag = placardDataDao.insert(placardData);
	System.out.println(flag);
    }

    @Test
    public void testSelect() {
	PlacardData placardData = placardDataDao.findById(34L);
	System.out.println(placardData.getUserId());
    }

    @Test
    public void testUpdate() {
	PlacardData placardData = new PlacardData();
	placardData.setUserId(34L);
	placardData.setReadTime(new Date());
	boolean flag = placardDataDao.update(placardData);
	System.out.println(flag);
    }

    @Test
    public void testDelete() {
	boolean flag = placardDataDao.delete(34L);
	System.out.println(flag);
    }

    @Test
    public void testInsert2() {
	// PlacardTemplet placardTemplet = new PlacardTemplet();
	// placardTemplet.setContent("sdf");
	// boolean flag = placardTempletDao.insert(placardTemplet);
	// System.out.println(flag);
    }

    @Test
    public void testGet2() {
	PlacardTemplet placardTemplet = placardTempletDao.findLastTemplet();
	System.out.println(placardTemplet.getContent());
    }

    @Test
    public void testfindPlacardTemplet() {
	List<PlacardTemplet> list = placardTempletDao.findPlacardTemplet(false, true, 0, 3);
	System.out.println(list.size());
    }

    @Test
    public void testplacardCount() {
	placardTempletDao.placardCount(22L);
    }

    @Test
    public void testgetPlacardTempletCount() {
	System.out.println(placardTempletDao.getPlacardTempletCount(false));
    }

    @Test
    public void testfindById() {
	placardTempletDao.findById(22L);
    }

}
