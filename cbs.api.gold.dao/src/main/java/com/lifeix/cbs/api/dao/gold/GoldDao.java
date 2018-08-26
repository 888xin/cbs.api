/**
 * 
 */
package com.lifeix.cbs.api.dao.gold;

import java.util.List;

import com.lifeix.cbs.api.dao.BasicDao;
import com.lifeix.cbs.api.dto.gold.Gold;

/**
 * @author lifeix
 *
 */
public interface GoldDao extends BasicDao<Gold,Long> {
    public List<Gold> findList();
}
