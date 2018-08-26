package com.lifeix.cbs.contest.bean.bet;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

import java.util.List;

/**
 * Created by lhx on 16-5-5 下午3:36
 *
 * @Description 单双数
 */
public class BetOddevenListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = -2206332832250117895L;

    private List<BetOddevenResponse> oddeven_bets;

    public List<BetOddevenResponse> getOddeven_bets() {
        return oddeven_bets;
    }

    public void setOddeven_bets(List<BetOddevenResponse> oddeven_bets) {
        this.oddeven_bets = oddeven_bets;
    }

    @Override
    public String getObjectName() {
        return null;
    }
}
