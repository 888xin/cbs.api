/**
 * 
 */
package com.lifeix.cbs.api.service.user;

import java.util.List;
import java.util.Map;

import org.json.JSONException;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.bean.user.CbsUserListResponse;
import com.lifeix.cbs.api.bean.user.CbsUserResponse;
import com.lifeix.cbs.api.bean.user.UserSpaceResponse;
import com.lifeix.exception.L99ExceptionBase;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;
import com.lifeix.user.beans.CustomResponse;

/**
 * @author jacky
 * 
 */
public interface CbsUserService {

    /**
     * 根据龙号获得用户
     * 
     * @param longNO
     * @return
     * @throws L99IllegalParamsException
     * @throws L99NetworkException
     * @throws JSONException
     */
    public CbsUserResponse getCbsUserByLongNo(Long longNO) throws L99IllegalParamsException, JSONException,
	    L99NetworkException;

    /**
     * 根据用户Id获取用户
     * 
     * @param userId
     * @param isFullUser
     * @return
     * @throws L99IllegalParamsException
     */
    public CbsUserResponse getCbsUserByUserId(Long userId, boolean isFullUser) throws L99IllegalParamsException,
	    JSONException, L99NetworkException;

    /**
     * 根据用户Id获取用户（获取简单信息）
     */
    public CbsUserResponse getSimpleCbsUserByUserId(Long userId);

    /**
     * 根据用户Id获取用户
     * 
     * @param userId
     * @param isFullUser
     * @return
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    public CbsUserResponse getCbsUserByUserId(Long userId, boolean isFullUser, boolean isBlock)
	    throws L99IllegalParamsException, JSONException, L99NetworkException, L99IllegalDataException;

    /**
     * 用户注册
     * 
     * @param authType
     * @param authKey
     * @param accountType
     * @param name
     * @param password
     * @param gender
     * @param ipaddress
     * @param machineCode
     * @param randCode
     * @param client
     * @param market
     * @param country
     * @return
     * @throws L99IllegalParamsException
     * @throws JSONException
     * @throws L99NetworkException
     */
    public CbsUserResponse register(int authType, String authKey, int accountType, String name, String password, int gender,
	    String machineCode, String randCode, String client, String market, String country, String ipaddress)
	    throws L99IllegalParamsException, L99NetworkException, JSONException;

    /**
     * 用户登录
     * 
     * @param username
     * @param password
     * @param type
     * @param country
     * @param version
     * @param machineCode
     * @param encryptFlag
     * @param ipaddress
     * @param accountBrowser
     * @param timezone
     * @param sourceClient
     * @param devToken
     * @return
     * @throws L99IllegalParamsException
     * @throws L99NetworkException
     * @throws JSONException
     * 
     */
    public CbsUserResponse login(String username, String password, int type, String country, String version,
	    String machineCode, boolean encryptFlag, String ipaddress, String accountBrowser, String timezone,
	    String sourceClient, String devToken) throws L99IllegalParamsException, L99NetworkException, JSONException;

    /**
     * 第三方注册登录
     * 
     * @param oauthType
     * @param usercode
     * @param username
     * @param userhead
     * @param gender
     * @param machineCode
     * @param client
     * @param market
     * @param country
     * @param sign
     * @param ipaddress
     * @return
     * @throws L99IllegalParamsException
     * @throws JSONException
     * @throws L99NetworkException
     */
    public CbsUserResponse loginByThird(String oauthType, String usercode, String username, String userhead, int gender,
	    String machineCode, String client, String market, String country, String sign, String ipaddress)
	    throws L99IllegalParamsException, L99NetworkException, JSONException;

    /**
     * 第三方注册登录
     * 
     * @param oauthType
     * @param usercode
     * @param username
     * @param userhead
     * @param gender
     * @param machineCode
     * @param client
     * @param market
     * @param country
     * @param sign
     * @param ipaddress
     * @return
     * @throws L99IllegalParamsException
     * @throws JSONException
     * @throws L99NetworkException
     */
    public CbsUserResponse loginByThird(String oauthType, String usercode, String username, String userhead, int gender,
	    String machineCode, String client, String market, String country, String sign, String ipaddress, String openId)
	    throws L99IllegalParamsException, L99NetworkException, JSONException;

    /**
     * 修改用户信息
     * 
     * @param type
     *            修改的类型
     * @param name
     *            用户姓名 1
     * @param gender
     *            用户性别 2
     * @param avatarId
     *            用户头像 Id
     * @param userPath
     *            用户头像 4
     * @param aboutme
     *            用户简介 8
     * @param back
     *            用户背景图 16
     */
    public DataResponse<Object> updateUserInfo(Long accountId, Integer type, String name, Long avatarId, String userPath,
	    Integer gender, String aboutme, String back);

    /**
     * 连续登陆奖励
     * 
     * @param userId
     * @param loginToken
     * @param machineCode
     * @param client
     * @param ipaddress
     */
    public CustomResponse continueLoginGold(Long userId, String loginToken, String machineCode, String client,
	    String ipaddress);

    /**
     * 获取用户已经连续登录天数
     * 
     * @param userId
     * @return
     * @throws L99IllegalParamsException
     */
    public CustomResponse continueLoginTimes(Long userId) throws L99IllegalParamsException;

    public UserSpaceResponse findUserSpace(Long visitor, Long userId, Long longNO) throws L99IllegalParamsException,
	    L99IllegalDataException, JSONException, L99ExceptionBase;

    public Map<Long, CbsUserResponse> findCsAccountMapByIds(List<Long> userIds);

    /**
     * 查询用户
     * 
     * @param accountId
     * @return
     */
    public CbsUserResponse selectById(Long accountId);

    /**
     * 搜索用户
     * 
     * @param userId
     * @param searchKey
     * @param startId
     * @param limit
     * @return
     * @throws L99NetworkException
     * @throws JSONException
     */
    public CbsUserListResponse select(Long userId, String searchKey, Integer startId, Integer limit)
	    throws L99NetworkException, JSONException;

    /**
     * 手机通讯录
     * 
     * @param mobilePhones
     * @return
     * @throws L99IllegalParamsException
     * @throws L99NetworkException
     * @throws JSONException
     */
    public CbsUserListResponse contacts(Long accountId, String mobilePhones) throws L99IllegalParamsException,
	    L99NetworkException, JSONException;

    /**
     * 验证用户返回账户信息
     * 
     * @param auth
     * @param token
     * @param ipaddress
     * @return
     * @throws L99IllegalParamsException
     * @throws JSONException
     * @throws L99NetworkException
     * @throws L99IllegalDataException
     */
    public CustomResponse authUser(String auth, String token, String ipaddress) throws L99IllegalParamsException,
	    JSONException, L99NetworkException, L99IllegalDataException;

    /**
     * 检查是否存在大赢家用户 不存在则导入
     * 
     * @param userId
     * @param from
     */
    public void checkCbsUser(Long userId, String from);

    /**
     * 查询用户龙币余额
     * 
     * @param userId
     * @return
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     * @throws JSONException
     */
    public CustomResponse viewUserMoney(Long userId) throws L99IllegalParamsException, L99IllegalDataException,
	    JSONException;
}
