package com.lifeix.cbs.contest.bean.bet;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

import java.util.List;

/**
 * Created by lhx on 16-4-28 下午2:33
 *
 * @Description 大小球下单
 */
public class BetSizeListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = 5504271814642950848L;

    private List<BetSizeResponse> size_bets;

    public List<BetSizeResponse> getSize_bets() {
        return size_bets;
    }

    public void setSize_bets(List<BetSizeResponse> size_bets) {
        this.size_bets = size_bets;
    }

    @Override
    public String getObjectName() {
        return null;
    }
}
