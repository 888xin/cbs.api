package com.lifeix.cbs.api.dao.user;

import com.lifeix.cbs.api.dto.user.LoginPath;

/**
 * Created by lhx on 16-4-19 下午2:32
 *
 * @Description 用户每月累计登陆奖励路径
 */
public interface LoginPathDao {

    /**
     * 查找用户登录获得筹码路径
     * @return
     */
    public LoginPath find();

    /**
     * 更新路径
     * @param loginPath
     * @return
     */
    public boolean update(LoginPath loginPath);

}
