package com.lifeix.cbs.message.impl.placard;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.message.bean.placard.PlacardTempletListResponse;
import com.lifeix.cbs.message.bean.placard.PlacardTempletResponse;
import com.lifeix.cbs.message.dao.placard.PlacardDataDao;
import com.lifeix.cbs.message.dao.placard.PlacardTempletDao;
import com.lifeix.cbs.message.dto.PlacardData;
import com.lifeix.cbs.message.dto.PlacardTemplet;
import com.lifeix.cbs.message.service.placard.PlacardService;
import com.lifeix.cbs.message.util.TransformDtoToVoUtil;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * Created by lhx on 15-10-19 下午4:00
 *
 * @Description
 */
@Service("placardService")
public class PlacardServiceImpl extends ImplSupport implements PlacardService {

    @Autowired
    private PlacardTempletDao placardTempletDao ;

    @Autowired
    private PlacardDataDao placardDataDao ;

    /**
     * 用户系统公告列表(最多返回limit个)
     * @param userId
     * @param limit
     * @return
     */
    @Override
    public PlacardTempletListResponse findPlacardDatas(Long userId, int limit) throws L99IllegalParamsException, L99IllegalDataException {
        ParamemeterAssert.assertDataNotNull(userId);

        limit = Math.min(limit, 100);
        limit = Math.max(limit, 1);

        List<PlacardTemplet> templets = placardTempletDao.findPlacardTemplet(true, true, 0, limit);

        PlacardData data = placardDataDao.findById(userId);

        List<PlacardTempletResponse> placard_templets = new ArrayList<PlacardTempletResponse>();
        for (PlacardTemplet templet : templets) {
            boolean placardRead = true ;
            if(data != null){
                if(templet.getCreateTime().after(data.getReadTime())){
                    placardRead = false;
                }
            }else{
                placardRead = false;
            }
            placard_templets.add(TransformDtoToVoUtil.transformPlacardTempletResponse(templet, placardRead));
        }

        // 设置系统公告已读
        readPlacardTemplet(userId);

        PlacardTempletListResponse reponse = new PlacardTempletListResponse();
        reponse.setPlacard_templets(placard_templets);
        reponse.setNumber(placard_templets.size());
        reponse.setLimit(limit);
        return reponse;
    }

    /**
     * 系统公告已读
     * @param userId
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    @Override
    public void readPlacardTemplet(Long userId) throws L99IllegalParamsException, L99IllegalDataException {

        ParamemeterAssert.assertDataNotNull(userId);

        PlacardData data = placardDataDao.findById(userId);
        if(data == null){
            data = new PlacardData();
            data.setUserId(userId);
            data.setCreateTime(new Date());
            data.setReadTime(new Date());
            boolean flag = placardDataDao.insert(data);
            if(!flag){
                throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
            }
        }else{
            data.setReadTime(new Date());
            boolean flag = placardDataDao.update(data);
            if(!flag){
                throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
            }
        }
    }

}
