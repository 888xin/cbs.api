package com.lifeix.cbs.message.service.placard;

import com.lifeix.cbs.message.bean.placard.PlacardTempletListResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * Created by lhx on 15-10-19 下午3:54
 *
 * @Description
 */
public interface PlacardService {

    /**
     * 用户系统公告列表(最多返回limit个)
     */
    public PlacardTempletListResponse findPlacardDatas(Long userId, int limit) throws L99IllegalParamsException, L99IllegalDataException;

    /**
     * 系统公告已读
     */
    public void readPlacardTemplet(Long userId) throws L99IllegalParamsException, L99IllegalDataException;
}
