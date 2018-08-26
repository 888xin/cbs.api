package com.lifeix.cbs.api.bean.user;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

import java.util.List;

/**
 * Created by lhx on 16-4-20 上午10:37
 *
 * @Description
 */
public class UserLoginListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = -5712844465265675764L;
    private List<UserLoginResponse> user_login ;

    public List<UserLoginResponse> getUser_login() {
        return user_login;
    }

    public void setUser_login(List<UserLoginResponse> user_login) {
        this.user_login = user_login;
    }

    @Override
    public String getObjectName() {
        return null;
    }
}
