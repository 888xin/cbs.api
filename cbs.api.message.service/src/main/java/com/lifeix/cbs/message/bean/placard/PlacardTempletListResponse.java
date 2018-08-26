package com.lifeix.cbs.message.bean.placard;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

/**
 * Created by lhx on 15-10-19 下午3:47
 *
 * @Description
 */
public class PlacardTempletListResponse extends ListResponse implements Response {
    private List<PlacardTempletResponse> placard_templets;

    public List<PlacardTempletResponse> getPlacard_templets() {
        return placard_templets;
    }

    public void setPlacard_templets(List<PlacardTempletResponse> placard_templets) {
        this.placard_templets = placard_templets;
    }

    @Override
    public String getObjectName() {
        return null;
    }
}
