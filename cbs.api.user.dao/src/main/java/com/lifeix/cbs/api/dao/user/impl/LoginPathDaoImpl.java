package com.lifeix.cbs.api.dao.user.impl;

import com.lifeix.cbs.api.dao.ContentDaoSupport;
import com.lifeix.cbs.api.dao.user.LoginPathDao;
import com.lifeix.cbs.api.dto.user.LoginPath;
import org.springframework.stereotype.Repository;

/**
 * Created by lhx on 16-4-19 下午2:35
 *
 * @Description
 */
@Repository("loginPathDao")
public class LoginPathDaoImpl extends ContentDaoSupport implements LoginPathDao {

    private final String DATA_KEY = this.getClass().getName();

    @Override
    public LoginPath find() {
        LoginPath loginPath = memcacheService.get(DATA_KEY);
        if (loginPath == null){
            loginPath = sqlSession.selectOne("LoginPathMapper.find");
            memcacheService.set(DATA_KEY,loginPath);
        }
        return loginPath ;
    }

    @Override
    public boolean update(LoginPath loginPath) {
        boolean flag = sqlSession.update("LoginPathMapper.update",loginPath) > 0;
        if (flag){
            memcacheService.delete(DATA_KEY);
        }
        return flag ;
    }
}
