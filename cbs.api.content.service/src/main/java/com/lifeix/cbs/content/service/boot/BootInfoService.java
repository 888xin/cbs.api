package com.lifeix.cbs.content.service.boot;

import com.lifeix.cbs.content.bean.boot.BootInfoListResponse;
import com.lifeix.cbs.content.bean.boot.BootInfoResponse;
import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;

public interface BootInfoService {

    /**
     * 获取当前开机信息
     * 
     * @return
     */
    public BootInfoResponse currBootInfo();

    /**
     * 添加开机信息
     * 
     * @param infoKey
     * @param enableFlag
     * @param type
     * @param devTime
     * @param dataLink
     * @return
     * @throws L99IllegalParamsException 
     * @throws L99IllegalDataException 
     */
    public void addBootInfo(String infoKey, boolean enableFlag, Integer type, Integer devTime,
	    String dataLink) throws L99IllegalParamsException, L99IllegalDataException;

    /**
     * 删除开机信息
     * 
     * @param id
     * @return
     * @throws L99IllegalParamsException 
     */
    public boolean delBootInfo(Long id) throws L99IllegalParamsException;

    /**
     * 禁用开机信息
     * 
     * @param id
     * @return
     * @throws L99IllegalParamsException 
     */
    public boolean disableBootInfo(Long id) throws L99IllegalParamsException;
    /**
     * 启用开机信息
     * @param id
     * @return
     * @throws L99IllegalParamsException
     */
    public boolean ableBootInfo(Long id) throws L99IllegalParamsException;

    /**
     * 开机信息列表
     * 
     * @param startId
     * @param limit
     * @return
     */
    public BootInfoListResponse findBootInfolList(Long startId, int limit);
    /**
     * 查找单个
     * @return
     * @throws L99IllegalParamsException 
     */
    public BootInfoResponse findOneById(Long id) throws L99IllegalParamsException;
    /**
     * 修改开机动画信息
     * @param id
     * @return
     * @throws L99IllegalParamsException
     */
    public boolean editBootInfo(Long id,String infoKey,  Integer type, Integer devTime,
	    String dataLink) throws L99IllegalParamsException;

}
