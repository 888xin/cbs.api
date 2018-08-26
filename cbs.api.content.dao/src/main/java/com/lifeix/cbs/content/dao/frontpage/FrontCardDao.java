package com.lifeix.cbs.content.dao.frontpage;

import java.util.List;

import com.lifeix.cbs.content.dto.frontpage.FrontCard;

/**
 * Created by lhx on 15-11-27 下午2:59
 *
 * @Description
 */
public interface FrontCardDao {


    /**
     * 根据contentId查找
     */
    public FrontCard findById(Long id);
    
    public List<FrontCard> findList();


}
