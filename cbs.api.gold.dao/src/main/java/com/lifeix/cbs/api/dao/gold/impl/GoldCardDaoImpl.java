package com.lifeix.cbs.api.dao.gold.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lifeix.cbs.api.dao.ContentGoldDaoSupport;
import com.lifeix.cbs.api.dao.gold.GoldCardDao;
import com.lifeix.cbs.api.dto.gold.GoldCard;

@Repository("goldCardDao")
public class GoldCardDaoImpl extends ContentGoldDaoSupport implements GoldCardDao {

    @Override
    public GoldCard findById(Long id) {
	GoldCard  goldCard = sqlSession.selectOne("GoldCardMapper.findById", id);
	return goldCard;
    }

    @Override
    public boolean insert(GoldCard goldCard) {
	int num = sqlSession.insert("GoldCardMapper.insertAndGetPrimaryKey", goldCard);
	if (num > 0) {
	    return true;
	}
	return false;
    }

    @Override
    public boolean update(GoldCard goldCard) {
	int num = sqlSession.update("GoldCardMapper.update", goldCard);
	if (num > 0) {
	    return true;
	} else {
	    return false;
	}
    }

    @Override
    public boolean delete(GoldCard goldCard) {
	int num = sqlSession.delete("GoldCardMapper.delete", goldCard);
	if(num>0){
	    return true;
	}else{
	    return false;
	}
	
    }

    @Override
    public List<GoldCard> findGoldCards(Boolean enable) {
	List<GoldCard> goldCards = sqlSession.selectList("GoldCardMapper.findGoldCards", enable);
	return goldCards;
    }

}
