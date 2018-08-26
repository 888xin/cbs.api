/**
 * 
 */
package com.lifeix.cbs.api.service.user;

import java.util.List;

import org.json.JSONException;

import com.lifeix.cbs.api.bean.user.CbsUserWxResponse;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;

/**
 * 
 */
public interface CbsUserWxService {

    /**
     * 获取用户信息
     * 
     * @param userIds
     * @return
     */
    public List<CbsUserWxResponse> selectById(List<Long> userIds);

    /**
     * 保存用戶微信信息
     * 
     * @param userId
     * @param openId
     * @param appId
     * @param source
     * @return
     * @throws L99IllegalParamsException
     * @throws L99NetworkException
     * @throws JSONException
     */
    public void saveUserWxInfo(Long userId, String openId, String appId, String source) throws L99IllegalParamsException,
	    L99NetworkException, JSONException;

    /**
     * 修改用户信息
     * 
     * @param userId
     * @param openId
     * @param appId
     * @param source
     * @return
     */
    public void updateUserWxInfo(Long userId, String openId, String appId, String source);

    /**
     * 查询用户
     * 
     * @param accountId
     * @return
     */
    public CbsUserWxResponse selectById(Long userId);

}
