package com.lifeix.cbs.api.gold.dao;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lifeix.cbs.api.dao.gold.GoldOrderDao;
import com.lifeix.cbs.api.dto.gold.GoldOrder;

public class GoldOrderDaoTest {

    private ApplicationContext ctx = null;
    private GoldOrderDao goldOrderDao = null;

    @Before
    public void init() {
	ctx = new ClassPathXmlApplicationContext("classpath:conf/cbs-content-gold-spring-dao-temp.xml");
	goldOrderDao = ctx.getBean(GoldOrderDao.class);

    }

    @Test
    public void testSelectById() {
	GoldOrder goldOrder = goldOrderDao.findById(1L);
	System.out.println(goldOrder);
    }
    
    @Test
    public void testInsertAndGetPrimaryKey(){
	GoldOrder goldOrder = new GoldOrder();
	goldOrder.setUserId(4L);
	goldOrder.setTargetId(4L);
	goldOrder.setCardId(2L);
	goldOrder.setAmount(11.00);
	goldOrder.setObtain(12.98);
	goldOrder.setAddedTime(new Date());
	goldOrder.setPaidTime(new Date());
	goldOrder.setStatu(1);
	goldOrder.setPaymentId(1);
	goldOrder.setContent("测试2");
	goldOrder.setIpAddress("192.168.2.112");
	System.out.println(goldOrderDao.insertAndGetPrimaryKey(goldOrder));
	
    }
    
    @Test
    public void testFindOrderByOrderNO(){
	GoldOrder order = goldOrderDao.findOrderByOrderNO("2");
	System.out.println(order);
    }
    
    @Test
    public void testGetOrderNumber(){
	
	Long count = goldOrderDao.getOrderNumber(4L, 1);
	System.out.println("获取的订单数："+count);
    }
    
    @Test
    public void testFindOrders(){
	
	List<GoldOrder> orders = goldOrderDao.findOrders(4L, 1, null, 1);
	System.out.println(orders);
	
    }

   

}
