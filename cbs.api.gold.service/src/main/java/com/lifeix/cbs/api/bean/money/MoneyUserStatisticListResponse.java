package com.lifeix.cbs.api.bean.money;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

import java.util.List;

/**
 * Created by lhx on 16-4-13 上午11:24
 *
 * @Description
 */
public class MoneyUserStatisticListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = 970909368202578613L;

    private List<MoneyUserStatisticResponse> users_money;

    public List<MoneyUserStatisticResponse> getUsers_money() {
        return users_money;
    }

    public void setUsers_money(List<MoneyUserStatisticResponse> users_money) {
        this.users_money = users_money;
    }

    @Override
    public String getObjectName() {
        return null;
    }
}
