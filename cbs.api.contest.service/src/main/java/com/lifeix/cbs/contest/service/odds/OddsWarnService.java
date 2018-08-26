/**
 * 
 */
package com.lifeix.cbs.contest.service.odds;

import com.lifeix.cbs.contest.bean.odds.OddsWarnListResponse;
import com.lifeix.exception.service.L99IllegalDataException;

/**
 * 异常赔率接口
 * 
 * @author lifeix
 *
 */
public interface OddsWarnService {

    /**
     * 获取异常赔率列表
     * 
     * @param status
     * @param startId
     * @param limit
     * @return
     * @throws L99IllegalDataException
     */
    public OddsWarnListResponse getOddsWarnList(Integer status, Long startId, int limit) throws L99IllegalDataException;

    /**
     * 处理异常赔率
     * 
     * @param id
     * @param status
     */
    public void editOddsWarn(Long id, Integer status);
}
