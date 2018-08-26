/**
 * 
 */
package com.lifeix.cbs.content.service.inform;

import org.json.JSONException;

import com.lifeix.cbs.content.bean.inform.InformListResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;

/**
 * @author lifeix
 *
 */
public interface InformService {

    /**
     * 新增或更新举报
     * 
     * @param containId
     * @param contain
     * @param image
     * @param informerId
     * @param informType
     * @param informReason
     * @param userId
     * @param type
     * @return
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     * @throws L99NetworkException
     * @throws JSONException
     */
    public Long addOrUpdateInform(Long containId, String contain, String image, Long informerId, Integer informType,
	    String informReason, Long userId, Integer type) throws L99IllegalParamsException, L99IllegalDataException,
	    L99NetworkException, JSONException;

    /**
     * 后台获取举报列表
     * 
     * @param page
     * @param limit
     * @param status
     * @param type
     * @return
     * @throws JSONException
     */
    public InformListResponse getInformList(Integer page, int limit, Integer status, Integer type) throws JSONException;

    /**
     * 处理举报
     * 
     * @param id
     * @param status
     * @param disposeInfo
     * @param lastTime
     * @param type
     * @throws L99IllegalDataException
     */
    public void updateInformStatus(Long id, Integer status, String disposeInfo, Long lastTime, Integer type)
	    throws L99IllegalDataException;
}
