package com.lifeix.cbs.api.dao.gold;

import java.util.Date;
import java.util.List;

import com.lifeix.cbs.api.dao.BasicDao;
import com.lifeix.cbs.api.dto.gold.GoldLog;

public interface GoldLogDao extends BasicDao<GoldLog, Long> {

    /**
     * 插入日志返回主键
     * 
     * @param log
     * @return
     */
    public Long insertAndGetKey(GoldLog log);

    /**
     * 查询用户的流水记录
     * 
     * @param userId
     * @param startId
     * @param limit
     * @return
     */
    public List<GoldLog> findGoldLogs(Long userId, Long startId, Integer limit);

    /**
     * 查询某时间段内的流水记录
     * 
     * @param createTime
     * @param endTime
     * @return
     */
    public List<GoldLog> countGold(Date createTime, Date endTime);

    /**
     * 查询用户在某天的流水记录
     * 
     * @param userId
     * @param startId
     * @param limit
     * @param time
     * @return
     */
    public List<GoldLog> findGoldLogs(Long userId, Long startId, Integer limit, Date createTime, Date endTime);

    /**
     * 查询系统充值接口
     * 
     * @param userId
     * @param startId
     * @param roleId
     * @param limit
     * @param logTime
     * @return
     */
    public List<GoldLog> findSystemGoldLogs(Long userId, Long roleId, Long startId, Integer limit, Date beginTime,
	    Date endTime);

    /**
     * 查询系统当天的收入记录
     * 
     * @param time
     * @return
     */
    public List<GoldLog> findSystemIncomeLog(Date time, Long startId, Integer limit);

    /**
     * 查询系统当天的支出记录
     * 
     * @param time
     * @return
     */
    public List<GoldLog> findSystemOutlayLog(Date time, Long startId, Integer limit);

    /**
     * 查看系统详细流水日志
     * 
     * @param time
     * @param startId
     * @param limit
     * @param type
     * @return
     */
    public List<GoldLog> findSystemLogDetail(Long logId, Long longNo, Date startDate, Date endDate, Long startId,
	    Integer limit, Integer[] type);
}
