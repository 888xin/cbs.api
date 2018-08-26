package com.lifeix.cbs.api.gold.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lifeix.cbs.api.dao.gold.GoldLogDao;
import com.lifeix.cbs.api.dto.gold.GoldLog;

public class GoldLogDaoTest {

    private ApplicationContext ctx = null;
    private GoldLogDao goldLogDao = null;

    @Before
    public void init() {
	ctx = new ClassPathXmlApplicationContext("classpath:conf/cbs-content-gold-spring-dao-temp.xml");
	goldLogDao = ctx.getBean(GoldLogDao.class);

    }

    @Test
    public void testSelectById() {
	GoldLog goldLog = goldLogDao.findById(4L);
	System.out.println(goldLog);
    }

    @Test
    public void testInsert() {
	GoldLog goldLog = new GoldLog();
	goldLog.setUserId(5L);
	goldLog.setMoney(1000.00);
	goldLog.setContent("充值：加入大赢家即送千枚龙币筹码");
	goldLog.setType(1);
	goldLog.setLogTime(new Date());
	System.out.println(goldLogDao.insert(goldLog));

    }

    @Test
    public void testUpdate() {

	GoldLog goldLog = new GoldLog();
	goldLog.setLogId(6L);
	goldLog.setUserId(5L);
	goldLog.setMoney(2000.00);
	goldLog.setContent("充值：加入大赢家即送千枚龙币筹码");
	goldLog.setType(1);
	goldLog.setLogTime(new Date());
	System.out.println(goldLogDao.update(goldLog));

    }

    @Test
    public void testFindGoldLogs() {

	List<GoldLog> logs = goldLogDao.findGoldLogs(4L, 1L, null);
	System.out.println(logs);
    }

    @Test
    public void testCountGold() throws ParseException {
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	Date createTime = format.parse("2014-02-20");
	Date endTime = new Date();
	List<GoldLog> goldlogs = goldLogDao.countGold(createTime, endTime);
	System.out.println(goldlogs);
    }

}
