package com.lifeix.cbs.content.service.frontpage;


import com.lifeix.cbs.content.bean.frontpage.FrontCardListResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import org.json.JSONException;


/**
 * Created by lhx on 15-11-30 上午10:45
 *
 * @Description
 */
public interface FrontCardService {


    /**
     * 查询
     */
    public FrontCardListResponse findList(Long userId) throws L99IllegalDataException, L99IllegalParamsException, JSONException;

}