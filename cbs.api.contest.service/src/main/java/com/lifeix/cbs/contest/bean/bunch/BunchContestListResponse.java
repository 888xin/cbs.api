package com.lifeix.cbs.contest.bean.bunch;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

import java.util.List;

/**
 * Created by lhx on 16-5-17 下午3:31
 *
 * @Description
 */
public class BunchContestListResponse extends ListResponse implements Response {

    private static final long serialVersionUID = 4471118672570008789L;

    private List<BunchContestResponse> bunches;

    public List<BunchContestResponse> getBunches() {
        return bunches;
    }

    public void setBunches(List<BunchContestResponse> bunches) {
        this.bunches = bunches;
    }

    @Override
    public String getObjectName() {
        return null;
    }
}
