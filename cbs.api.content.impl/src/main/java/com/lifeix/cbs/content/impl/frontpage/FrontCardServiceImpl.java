package com.lifeix.cbs.content.impl.frontpage;

import java.util.ArrayList;
import java.util.List;

import com.lifeix.cbs.api.bean.gold.GoldResponse;
import com.lifeix.cbs.api.service.money.MoneyService;
import com.lifeix.cbs.content.bean.frontpage.FrontCardListResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.content.bean.frontpage.FrontCardResponse;
import com.lifeix.cbs.content.dao.frontpage.FrontCardDao;
import com.lifeix.cbs.content.dto.frontpage.FrontCard;
import com.lifeix.cbs.content.service.frontpage.FrontCardService;

@Service("frontCardService")
public class FrontCardServiceImpl extends ImplSupport implements FrontCardService {

//    @Autowired
//    private FrontCardDao frontCardDao;

    @Autowired
    private MoneyService moneyService;

    @Override
    public FrontCardListResponse findList(Long userId) throws L99IllegalDataException, L99IllegalParamsException, JSONException {
        FrontCardListResponse data = new FrontCardListResponse();
        GoldResponse money = moneyService.viewUserMoney(userId, "");
        data.setAvailableMoney(money == null ? 0 : money.getBalance());
        //List<FrontCard> list = frontCardDao.findList();
        List<FrontCardResponse> res = new ArrayList<FrontCardResponse>();
        FrontCardResponse frontCardResponse = new FrontCardResponse();
        frontCardResponse.setId(1L);
        frontCardResponse.setCard_detail("将你的内容同时推荐到头版");
        frontCardResponse.setCard_name("推荐到头版");
        frontCardResponse.setCard_type(1);
        frontCardResponse.setPrice(1D);
        res.add(frontCardResponse);
        data.setCards(res);
        return data;
    }

}
