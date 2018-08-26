package com.lifeix.cbs.content.dao.frontpage.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lifeix.cbs.content.dao.ContentDaoSupport;
import com.lifeix.cbs.content.dao.frontpage.FrontCardDao;
import com.lifeix.cbs.content.dto.frontpage.FrontCard;

@Repository("frontCardDao")
public class FrontCardDaoImpl extends ContentDaoSupport implements FrontCardDao {

    @Override
    public FrontCard findById(Long id) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("id", id);
	return sqlSession.selectOne("FrontCardMapper.findById", params);
    }

    @Override
    public List<FrontCard> findList() {
	return sqlSession.selectList("FrontCardMapper.findList");
    }

}
