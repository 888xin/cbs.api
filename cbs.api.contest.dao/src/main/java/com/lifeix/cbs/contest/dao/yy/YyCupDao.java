package com.lifeix.cbs.contest.dao.yy;

import java.util.List;

import com.lifeix.cbs.contest.dto.yy.YyCup;

public interface YyCupDao {

    public YyCup selectById(long id);

    public YyCup selectByName(String cupName);

    public boolean insert(YyCup entity);

    public boolean update(YyCup entity);

    public boolean delete(YyCup entity);

    /**
     * 押押分类列表
     * 
     * @return
     */
    public List<YyCup> findYyCups();

}