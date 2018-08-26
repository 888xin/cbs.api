package com.lifeix.cbs.api.conetent.test.lhx;


import com.lifeix.cbs.content.dao.game.ZodiacAnimalBetDao;
import com.lifeix.cbs.content.dao.game.ZodiacAnimalDao;
import com.lifeix.cbs.content.dto.game.ZodiacAnimal;
import com.lifeix.cbs.content.dto.game.ZodiacAnimalBet;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;
import java.util.List;

/**
 * Created by lhx on 15-12-11 上午11:51
 *
 * @Description
 */
public class ZodiacAnimalTest {

    private ApplicationContext ctx = null;
    private ZodiacAnimalDao zodiacAnimalDao = null;
    private ZodiacAnimalBetDao zodiacAnimalBetDao = null;

    @Before
    public void init() {
        ctx = new ClassPathXmlApplicationContext("classpath:conf/cbs-content-spring-dao-temp.xml");
        zodiacAnimalBetDao = (ZodiacAnimalBetDao) ctx.getBean("zodiacAnimalBetDao");
        zodiacAnimalDao = (ZodiacAnimalDao) ctx.getBean("zodiacAnimalDao");
    }


//    @Test
//    public void testInsert() {
//        ZodiacAnimal zodiacAnimal = new ZodiacAnimal();
//
//        zodiacAnimal.setLottery("1,3,12");
//        zodiacAnimal.setStartTime(new Date());
//        zodiacAnimal.setEndTime(new Date());
//        zodiacAnimal.setUpdateTime(new Date());
//        zodiacAnimalDao.insert(zodiacAnimal);
//    }
//
//    @Test
//    public void testFind() {
//        ZodiacAnimal zodiacAnimal = zodiacAnimalDao.findOne(new Date(),null,false);
//
//    }
//
//
//
//    @Test
//    public void testInsert2() {
//        ZodiacAnimalBet zodiacAnimalBet = new ZodiacAnimalBet();
//
//        zodiacAnimalBet.setGameId(2);
//        zodiacAnimalBet.setUserId(100L);
//        zodiacAnimalBet.setBet("1,2,4");
//        zodiacAnimalBet.setBetSum(10.00);
//        zodiacAnimalBet.setBackSum(0.00);
//        zodiacAnimalBet.setIsLongbi(true);
//        zodiacAnimalBet.setCreateTime(new Date());
//        zodiacAnimalBet.setUpdateTime(new Date());
//        zodiacAnimalBetDao.insert(zodiacAnimalBet);
//    }
//
//    @Test
//    public void testFinds() {
//        List<ZodiacAnimalBet> list = zodiacAnimalBetDao.findZodiacAnimalBets(100L,null,20,null);
//        System.out.println(list.size());
//    }
//
//    @Test
//    public void update() {
//        boolean flag = zodiacAnimalBetDao.update(1231,10.00,1);
//        System.out.println(flag);
//    }
}
