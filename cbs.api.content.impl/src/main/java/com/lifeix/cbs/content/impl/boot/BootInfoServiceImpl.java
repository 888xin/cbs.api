package com.lifeix.cbs.content.impl.boot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.cbs.api.common.impl.ImplSupport;
import com.lifeix.cbs.api.common.util.ParamemeterAssert;
import com.lifeix.cbs.content.bean.boot.BootInfoListResponse;
import com.lifeix.cbs.content.bean.boot.BootInfoResponse;
import com.lifeix.cbs.content.dao.boot.BootInfoDao;
import com.lifeix.cbs.content.dto.boot.BootInfo;
import com.lifeix.cbs.content.impl.transform.BootTransformUtil;
import com.lifeix.cbs.content.service.boot.BootInfoService;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

@Service("bootInfoService")
public class BootInfoServiceImpl extends ImplSupport implements BootInfoService {

    @Autowired
    private BootInfoDao bootInfoDao;

    /**
     * 获取当前开机信息
     * 
     * @return
     */
    @Override
    public BootInfoResponse currBootInfo() {

	BootInfo bootInfo = bootInfoDao.selectLast();
	BootInfoResponse data = BootTransformUtil.transformBootInfo(bootInfo);

	if (data != null) {
	    // 设置是否显示搜索
	    data.setSearch_flag(true);
	}
	return data;
    }

    /**
     * 添加开机信息
     * 
     * @param infoKey
     * @param enableFlag
     * @return
     * @throws L99IllegalParamsException
     * @throws L99IllegalDataException
     */
    @Override
    public void addBootInfo(String infoKey, boolean enableFlag, Integer type, Integer advTime, String dataLink)
	    throws L99IllegalParamsException, L99IllegalDataException {

	ParamemeterAssert.assertDataNotNull(infoKey);

	BootInfo bootInfo = new BootInfo();
	bootInfo.setInfoKey(infoKey);
	bootInfo.setEnableFlag(enableFlag);
	bootInfo.setType(type);
	bootInfo.setAdvTime(advTime);
	bootInfo.setDataLink(dataLink);
	bootInfo.setCreateTime(new Date());
	Boolean flag = bootInfoDao.insert(bootInfo);
	if (!flag) {
	    throw new L99IllegalDataException(BasicMsg.CODE_BASIC_SERVCER, BasicMsg.KEY_BASIC_SERVCER);
	}
    }

    /**
     * 删除开机信息
     * 
     * @param id
     * @return
     * @throws L99IllegalParamsException
     */
    @Override
    public boolean delBootInfo(Long id) throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(id);

	Boolean flag = bootInfoDao.deleteById(id);

	return flag;

    }

    /**
     * 禁用开机信息
     * 
     * @throws L99IllegalParamsException
     */
    @Override
    public boolean disableBootInfo(Long id) throws L99IllegalParamsException {

	ParamemeterAssert.assertDataNotNull(id);

	Boolean flag = bootInfoDao.disableById(id);

	return flag;
    }

    /**
     * 启用
     */
    @Override
    public boolean ableBootInfo(Long id) throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(id);

	Boolean flag = bootInfoDao.ableById(id);

	return flag;
    }

    /**
     * 开机信息列表
     * 
     * @param startId
     * @param limit
     * @return
     */
    public BootInfoListResponse findBootInfolList(Long startId, int limit) {

	List<BootInfo> bootInfos = bootInfoDao.findBootInfos(startId, limit);
	List<BootInfoResponse> bootResponses = new ArrayList<BootInfoResponse>();
	for (BootInfo info : bootInfos) {
	    bootResponses.add(BootTransformUtil.transformBootInfo(info));
	}
	BootInfoListResponse response = new BootInfoListResponse();
	response.setBoots(bootResponses);
	response.setStartId(startId);
	response.setLimit(limit);

	return response;
    }

    @Override
    public BootInfoResponse findOneById(Long id) throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(id);
	BootInfo bootInfo = bootInfoDao.findOneById(id);
	BootInfoResponse data = BootTransformUtil.transformBootInfo(bootInfo);
	// 设置是否显示搜索
	data.setSearch_flag(true);
	return data;
    }

    public void setBootInfoDao(BootInfoDao bootInfoDao) {
	this.bootInfoDao = bootInfoDao;
    }

    @Override
    public boolean editBootInfo(Long id, String infoKey, Integer type, Integer advTime, String dataLink)
	    throws L99IllegalParamsException {
	ParamemeterAssert.assertDataNotNull(id);
	BootInfo bootInfo = new BootInfo();
	bootInfo.setId(id);
	bootInfo.setInfoKey(infoKey);
	bootInfo.setType(type);
	bootInfo.setAdvTime(advTime);
	bootInfo.setDataLink(dataLink);
	boolean result = bootInfoDao.edit(bootInfo);
	return result;
    }
}
