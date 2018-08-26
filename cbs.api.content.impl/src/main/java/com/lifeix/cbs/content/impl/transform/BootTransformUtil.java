package com.lifeix.cbs.content.impl.transform;

import com.lifeix.cbs.api.common.util.BootConstants;
import com.lifeix.cbs.content.bean.boot.BootInfoResponse;
import com.lifeix.cbs.content.dto.boot.BootInfo;
import com.lifeix.common.utils.TimeUtil;



/**
 * Created by lhx on 16-4-15 上午10:34
 *
 * @Description
 */
public class BootTransformUtil {
    /**
     * 转换开机信息
     * 
     * @param bootInfo
     * @return
     */
    public static BootInfoResponse transformBootInfo(BootInfo bootInfo) {
	if (bootInfo == null) {
	    return null;
	}
	BootInfoResponse resp = new BootInfoResponse();
	resp.setId(bootInfo.getId());
	resp.setInfo_key(bootInfo.getInfoKey());
	resp.setEnable_flag(bootInfo.getEnableFlag());
	resp.setType(bootInfo.getType());
	resp.setAdv_time(bootInfo.getAdvTime());
	resp.setData_link(bootInfo.getDataLink());
	resp.setCreate_time(TimeUtil.getUtcTimeForDate(bootInfo.getCreateTime()));
	int bootType = bootInfo.getType();
	//更加不同的type组合scheme_link
	String scheme_link ="";
	switch(bootType){
	case BootConstants.BootType.CONTENT :
	    scheme_link = BootConstants.typeScheme.getNews(bootInfo.getDataLink());
	    break; 
	case BootConstants.BootType.BASKETBALL :
	    scheme_link = BootConstants.typeScheme.getBbContest(bootInfo.getDataLink());
	    break;
	case BootConstants.BootType.FOOTBALL :
	    scheme_link = BootConstants.typeScheme.getFbContest(bootInfo.getDataLink());
	    break;
	case BootConstants.BootType.YAYA :
	    scheme_link = BootConstants.typeScheme.getYaya(bootInfo.getDataLink());
	    break;
	case BootConstants.BootType.TEMPLET :
	    scheme_link = BootConstants.typeScheme.getTemplet(bootInfo.getDataLink());
	    break;
	}
	resp.setScheme_link(scheme_link);
	return resp;
    }
}
