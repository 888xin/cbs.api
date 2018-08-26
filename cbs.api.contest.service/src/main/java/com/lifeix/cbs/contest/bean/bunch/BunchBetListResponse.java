package com.lifeix.cbs.contest.bean.bunch;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

import java.util.List;

/**
 * Created by lhx on 16-5-18 下午5:05
 *
 * @Description
 */
public class BunchBetListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = 7585791006009040931L;

    private List<BunchBetResponse> bets;

    /**
     * 奖品
     */
    private List<BunchPrizeResponse> prize;

    public List<BunchPrizeResponse> getPrize() {
        return prize;
    }

    public void setPrize(List<BunchPrizeResponse> prize) {
        this.prize = prize;
    }

    public List<BunchBetResponse> getBets() {
        return bets;
    }

    public void setBets(List<BunchBetResponse> bets) {
        this.bets = bets;
    }

    @Override
    public String getObjectName() {
        return null;
    }
}
