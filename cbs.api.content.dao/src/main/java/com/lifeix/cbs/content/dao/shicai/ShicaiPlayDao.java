package com.lifeix.cbs.content.dao.shicai;

import java.util.List;
import java.util.Map;

import com.lifeix.cbs.content.dto.shicai.ShicaiPlay;



/**
 * @author wenhuans
 * 2015年11月3日 下午6:17:03
 * 
 */
public interface ShicaiPlayDao {

    public Long insert(ShicaiPlay shicaiPlay);
    
    public ShicaiPlay findById(Long id);
    
    public Map<Long, ShicaiPlay> findMapByIds(List<Long> ids);
    
    public ShicaiPlay findByAccountId(Long accountId);
    
    public ShicaiPlay findByAccountIdAndScId(Long accountId, Long scId);
    
    /**
     * 更新用户赚的钱数量，在个人下单记录中显示
     * @author wenhuans
     * 2015年11月5日下午3:51:53
     *
     */
    public Long updateNumAndSumNum(ShicaiPlay shicaiPlay);
    
    public List<ShicaiPlay> findList(Long accountId, Long startId, Long endId, Integer limit);
    
}

