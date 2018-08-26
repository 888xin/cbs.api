package com.lifeix.cbs.content.dao.shicai;

import java.util.List;

import com.lifeix.cbs.content.dto.shicai.Shicai;



/**
 * @author wenhuans
 * 2015年11月3日 下午6:10:13
 * 
 */
public interface ShicaiDao {

    public Long insert(Shicai shicai);
    
    public Shicai findById(Long scId);
    
    public Boolean update(Shicai shicai);
    
    public List<Shicai> findList(Long startId, Long endId, Integer limit);
}

