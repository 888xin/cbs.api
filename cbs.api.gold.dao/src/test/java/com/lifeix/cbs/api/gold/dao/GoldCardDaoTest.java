package com.lifeix.cbs.api.gold.dao;



import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lifeix.cbs.api.dao.gold.GoldCardDao;
import com.lifeix.cbs.api.dto.gold.GoldCard;

public class GoldCardDaoTest {

    private ApplicationContext ctx = null;
    private GoldCardDao goldCardDao = null;

    @Before
    public void init() {
	ctx = new ClassPathXmlApplicationContext("classpath:conf/cbs-content-gold-spring-dao-temp.xml");
	goldCardDao = ctx.getBean(GoldCardDao.class);
    }

    @Test
    public void testSelectById() {
	GoldCard goldCard = goldCardDao.findById(1L);
	System.out.println(goldCard);
    }
    
    @Test
    public void testFindGoldCards(){
	List<GoldCard> cards = goldCardDao.findGoldCards(true);
	System.out.println(cards);
    } 

  

}
