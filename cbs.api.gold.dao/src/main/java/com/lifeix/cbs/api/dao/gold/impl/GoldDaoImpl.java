/**
 * 
 */
package com.lifeix.cbs.api.dao.gold.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lifeix.cbs.api.dao.ContentGoldDaoSupport;
import com.lifeix.cbs.api.dao.gold.GoldDao;
import com.lifeix.cbs.api.dto.gold.Gold;

/**
 * @author lifeix
 *
 */
@Repository("goldDao")
public class GoldDaoImpl extends ContentGoldDaoSupport implements GoldDao {

    @Override
    public Gold findById(Long id) {
	return sqlSession.selectOne("GoldMapper.findById", id);
    }

    @Override
    public boolean insert(Gold entity) {
	return false;
    }

    @Override
    public boolean update(Gold entity) {
	return sqlSession.update("GoldMapper.update", entity) > 0;
    }

    @Override
    public boolean delete(Gold entity) {
	return false;
    }

    @Override
    public List<Gold> findList() {
	return sqlSession.selectList("GoldMapper.findList");
    }

}
