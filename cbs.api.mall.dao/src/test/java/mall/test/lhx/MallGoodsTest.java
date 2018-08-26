package mall.test.lhx;

import com.lifeix.cbs.mall.dao.goods.MallCategoryDao;
import com.lifeix.cbs.mall.dao.goods.MallGoodsDao;
import com.lifeix.cbs.mall.dto.goods.MallCategory;
import com.lifeix.cbs.mall.dto.goods.MallGoods;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lhx on 16-3-21 上午10:45
 *
 * @Description
 */
public class MallGoodsTest {

    private ApplicationContext ctx = null;
    private MallGoodsDao mallGoodsDao = null;
    private MallCategoryDao mallCategoryDao = null;

    @Before
    public void init() {
        ctx = new ClassPathXmlApplicationContext("classpath:cbs-mall-spring-dao-temp.xml");
        mallCategoryDao = (MallCategoryDao) ctx.getBean("mallCategoryDao");
        mallGoodsDao = (MallGoodsDao) ctx.getBean("mallGoodsDao");
    }

//    @Test
//    public void test1(){
//        MallGoods mallGoods = new MallGoods();
//        mallGoods.setCategoryId(2L);
//        mallGoods.setName("test1");
//        mallGoods.setDescr("商品描述");
//        mallGoods.setPath("商品图片");
//        mallGoods.setPathMore("所有图片");
//        mallGoods.setPrice(4.00);
//        mallGoods.setOriginal(4.01);
//        mallGoods.setSales(3);
//        mallGoods.setStock(1000);
//        mallGoods.setPostage(12.00);
//        mallGoods.setStatus(2);
//        mallGoods.setType(1);
//        mallGoods.setExProp("sdfsdfsdfsdfsd");
//        mallGoods.setSortIndex(3);
//        mallGoods.setCreateTime(new Date());
//        mallGoods.setUpdateTime(new Date());
//        mallGoodsDao.insert(mallGoods);
//
//    }
//
//    @Test
//    public void test2(){
//        MallGoods mallGoods = mallGoodsDao.findById(2);
//        System.out.println(mallGoods.getName());
//    }
//
//    @Test
//    public void test3(){
//        List<MallGoods> mallGoodsList = mallGoodsDao.findGoods(null,null,3);
//        System.out.println(mallGoodsList.size());
//    }
//
//    @Test
//    public void test4(){
//        MallCategory mallCategory = new MallCategory();
//        mallCategory.setDescr("sdfsdf");
//        mallCategory.setName("sdfsd324f");
//        mallCategory.setNum(23);
//        mallCategory.setPath("sdfsdfdfsdffsdfsd");
//        mallCategory.setSortIndex(23);
//        mallCategoryDao.insert(mallCategory);
//    }
//
//    @Test
//    public void test5(){
//        List<Long> list = new ArrayList<Long>();
//        list.add(1L);
//        list.add(2L);
//        list.add(3L);
//        Map<Long, MallGoods> mallGoodsMap = mallGoodsDao.findByIds(list);
//        for (Long aLong : mallGoodsMap.keySet()) {
//            System.out.println(aLong);
//        }
//    }

//    @Test
//    public void test6() {
//        List<MallGoods> list = mallGoodsDao.findGoodsByCategory(2L,null,2);
//        List<MallGoods> list2 = mallGoodsDao.findGoodsInner(2L,1,null,2);
//        System.out.println(list.size());
//        System.out.println(list2.size());
//
//    }

    @Test
    public void test7() {
        List<MallCategory> list = mallCategoryDao.findRecommendCategorys(2);
        System.out.println(list.size());

    }
}
