package com.lifeix.cbs.content.bean.game;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class ZodiacPlayListResponse  extends ListResponse implements Response{
    private static final long serialVersionUID = 1L;
private List<ZodiacPlayResponse> plays;

public List<ZodiacPlayResponse> getPlays() {
    return plays;
}

public void setPlays(List<ZodiacPlayResponse> plays) {
    this.plays = plays;
}

@Override
public String getObjectName() {
    // TODO Auto-generated method stub
    return null;
}

}
