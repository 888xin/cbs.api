package com.lifeix.cbs.content.impl.spark.game;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.content.dao.game.ZodiacAnimalDao;
import com.lifeix.cbs.content.dto.game.ZodiacAnimal;
import com.lifeix.cbs.content.service.game.ZodiacAnimalIssueService;
import com.lifeix.cbs.content.service.spark.game.ZodiacAnimalDubbo;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;
import com.lifeix.framework.memcache.MemcacheService;

@Service("zodiacAnimalDubbo")
public class ZodiacAnimalDubboImpl implements ZodiacAnimalDubbo {
    protected final static Logger LOG = LoggerFactory.getLogger(ZodiacAnimalDubboImpl.class);

    public static final int ISSUEGAPSECOND = 60 * 60 * 20;
    public static final int GAP = 47;

    public static final int TIME = 47;

    public static final String CREATEZODIACANIMALISSUE = "cbs:zodiac:create";

    public static final String CREATEZODIACANIMALISSUEDAY = "cbs:zodiac:create:day";

    @Autowired
    ZodiacAnimalDao zodiacAnimalDao;

    @Autowired
    ZodiacAnimalIssueService zodiacAnimalIssueService;

    @Autowired
    private MemcacheService memcacheService;

    @Override
    public void createZodiacAnimalIssue() throws JSONException {
	Object m = memcacheService.get(CREATEZODIACANIMALISSUE);

	if (m == null) {
	    try {
		memcacheService.set(CREATEZODIACANIMALISSUE, 1, 50);
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		ZodiacAnimal animal = zodiacAnimalDao.findLast();

		if (animal != null) {
		    Date endTime = animal.getEndTime();
		    if (endTime.after(now)) {
			if (endTime.getTime() - now.getTime() > ISSUEGAPSECOND * 1000) {
			    return;
			} else {
			    Long end = endTime.getTime();
			    batchCreate(end, now, animal.getId());
			}
		    } else if (endTime.equals(now) || endTime.before(now)) {
			batchCreate(now.getTime(), now, null);
		    }

		} else {
		    batchCreate(now.getTime(), now, null);
		}
	    } finally {
		memcacheService.delete(CREATEZODIACANIMALISSUE);
	    }

	}

    }

    @Override
    public void toPrize() throws JSONException, L99IllegalParamsException, L99IllegalDataException, L99NetworkException {
	Date now = new Date();
	ZodiacAnimal animal = zodiacAnimalDao.findOne(now,null);
	if (animal != null) {
	    try {
		if(animal.getEndTime().getTime()- now.getTime() > 12*1000){
		    return;
		}
		zodiacAnimalIssueService.toPrize(animal.getId());
	    } catch (L99IllegalParamsException e) {
	    } catch (L99IllegalDataException e) {
		LOG.info("toPrize dubbo L99IllegalDataException" + e.getMessage());
	    } catch (Exception e) {
		LOG.info("toPrize dubbo Exception", e);
	    } finally {
	    }

	}
    }

    private void batchCreate(Long start, Date now, Integer beforeId) {
	List<ZodiacAnimal> list = new ArrayList<ZodiacAnimal>();
	Integer befId = beforeId;
	for (Long i = start; i < start + ISSUEGAPSECOND * 1000; i = i + GAP * 1000) {
	    ZodiacAnimal z = new ZodiacAnimal();
	    if (i.longValue() == start.longValue()) {
		z.setStartTime(new Date(i));
	    } else {
		z.setStartTime(new Date(i + GAP * 1000));
	    }
	    String next = getNextValu(z.getStartTime(), befId);

	    z.setId(Integer.valueOf(next));
	    z.setEndTime(new Date(z.getStartTime().getTime() + TIME * 1000));
	    z.setUpdateTime(now);
	    befId = z.getId();
	    list.add(z);
	}

	zodiacAnimalDao.insertBatch(list);
    }

    private String getNextValu(Date endTime, Integer beforeId) {
	String value = "";
	Calendar cal = Calendar.getInstance();
	cal.setTime(endTime);
	String year = String.valueOf(cal.get(Calendar.YEAR)).substring(2, 4);
	int month = cal.get(Calendar.MONTH) + 1;
	int day = cal.get(Calendar.DAY_OF_MONTH);
	int hour = cal.get(Calendar.HOUR_OF_DAY);
	int minute = cal.get(Calendar.MINUTE);
	int second = cal.get(Calendar.SECOND);

	if ((hour == 0 && minute == 0 && second <= GAP) || beforeId == null) {
	    value = year + lpad(2,month) + lpad(2,day) + "0001";
	} else {
	    value = String.valueOf(beforeId + 1);
	}
	return value;
    }
    
    /**
     * 补齐不足长度
     * @param length 长度
     * @param number 数字
     * @return
     */
    private static String lpad(int length, int number) {
        String f = "%0" + length + "d";
        return String.format(f, number);
    }

    public static void main(String[] args) {
	Calendar cal = Calendar.getInstance();
	int year = cal.get(Calendar.YEAR);
	int month = cal.get(Calendar.MONTH) + 1;
	int day = cal.get(Calendar.DAY_OF_MONTH);
	int sss = cal.get(Calendar.HOUR_OF_DAY);
	int ss = cal.get(Calendar.MINUTE);
	int s = cal.get(Calendar.SECOND);

	System.out.println(lpad(2,17) + "ddd" + ss + "ff" + s);
	Date d = new Date(System.currentTimeMillis() + 24 * 1000);
	System.out.println(d.getTime()- new Date().getTime() > 17*1000);
	
	
	System.out.println(new Date(System.currentTimeMillis()));
	System.out.println(new Date(System.currentTimeMillis() + 17 * 1000));
	
	System.out.println(sss + "ddd" + ss + "ff" + s);
    }

}
