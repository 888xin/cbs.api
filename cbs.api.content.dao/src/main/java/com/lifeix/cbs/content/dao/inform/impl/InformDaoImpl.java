/**
 * 
 */
package com.lifeix.cbs.content.dao.inform.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lifeix.cbs.content.dao.ContentDaoSupport;
import com.lifeix.cbs.content.dao.inform.InformDao;
import com.lifeix.cbs.content.dto.inform.Inform;

/**
 * @author lifeix
 *
 */
@Service("informDao")
public class InformDaoImpl extends ContentDaoSupport implements InformDao {

    @Override
    public List<Inform> selectList(Integer page, int limit, Integer status, Integer type) {
	List<Inform> informList = null;
	Map<String, Object> params = new HashMap<String, Object>();
	if (page != null) {
	    params.put("page", page);
	}
	params.put("limit", limit);
	if (status != null) {
	    params.put("status", status);
	}
	params.put("type", type);
	informList = sqlSession.selectList("InformMapper.selectList", params);
	return informList;
    }

    @Override
    public Inform selectById(Long id) {
	Inform inform = null;
	inform = sqlSession.selectOne("InformMapper.selectById", id);
	return inform;
    }

    @Override
    public Inform selectByContainId(Long containId, Integer type, Integer status) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("containId", containId);
	params.put("type", type);
	if (status != null) {
	    params.put("status", status);
	}
	Inform inform = null;
	inform = sqlSession.selectOne("InformMapper.selectByContaintId", params);
	return inform;
    }

    @Override
    public Long insertInform(Inform inForm) {
	sqlSession.insert("InformMapper.insertInform", inForm);
	return inForm.getId();
    }

    @Override
    public boolean updateInform(Long id, Integer total, Integer informType, String informReason) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("id", id);
	params.put("total", total);
	params.put("informType", informType);
	params.put("informReason", informReason);
	int num = sqlSession.update("InformMapper.updateInform", params);
	return num > 0 ? true : false;
    }

    @Override
    public boolean updateStatusById(Long id, Integer status, String disposeInfo, Date removeTime) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("id", id);
	params.put("status", status);
	params.put("disposeInfo", disposeInfo);
	params.put("removeTime", removeTime);
	int num = sqlSession.update("InformMapper.updateStatusById", params);
	return num > 0 ? true : false;
    }

    @Override
    public boolean updateStatusByIds(List<Long> ids, Integer status, String disposeInfo) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("ids", ids);
	params.put("status", status);
	params.put("disposeInfo", disposeInfo);
	int num = sqlSession.update("InformMapper.updateStatusByIds", params);
	return num > 0 ? true : false;
    }

    @Override
    public boolean deleteById(Long id) {
	int num = sqlSession.delete("InformMapper.deleteById", id);
	return num > 0 ? true : false;
    }

}
