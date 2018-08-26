/**
 * 
 */
package mall.test.lhx;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lifeix.cbs.mall.dao.order.MallExpressDao;
import com.lifeix.cbs.mall.dto.order.MallExpress;

/**
 * @author lifeix
 * 
 */
public class MallExpressTest {

    private MallExpressDao mallExpressDao;
    private ApplicationContext ctx = null;

    @Before
    public void init() {
	ctx = new ClassPathXmlApplicationContext("classpath:cbs-mall-spring-dao-temp.xml");
	mallExpressDao = (MallExpressDao) ctx.getBean("mallExpressDao");
	System.out.println("test start");
    }

    @Test
    public void insert() {
	MallExpress mallExpress = new MallExpress();
	mallExpress.setOrderId(1232326L);
	mallExpress.setUserId(1L);
	mallExpress.setExpressType(1);
	mallExpress.setExpressNO("adaqweqwe");
	mallExpress.setCreateTime(new Date());
	mallExpress.setState(2);
	mallExpress.setExpressInfo("qwetrtrtr");
	mallExpressDao.insert(mallExpress);
    }

    @Test
    public void findById() {
	MallExpress mallExpress = mallExpressDao.findById(1232326L);
	System.out.println(mallExpress.getExpressNO());
    }

    @After
    public void end() {
	System.out.println("test end");
    }

}
