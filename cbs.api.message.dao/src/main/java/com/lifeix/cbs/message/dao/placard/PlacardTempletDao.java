package com.lifeix.cbs.message.dao.placard;

import java.util.List;

import com.lifeix.cbs.message.dto.PlacardTemplet;

/**
 * Created by lhx on 15-10-19 下午1:53
 *
 * @Description
 */
public interface PlacardTempletDao {

    public PlacardTemplet findById(Long templetId);

    public PlacardTemplet findLastTemplet();

    public Integer getPlacardTempletCount(Boolean disableFlag);

    public List<PlacardTemplet> findPlacardTemplet(Boolean disableFlag, boolean endTimeFlag, int start, int showData);

    public List<PlacardTemplet> findPlacardsInner(Long startId, Integer limit);

    public PlacardTemplet insert(PlacardTemplet placardTemplet);

    public boolean update(PlacardTemplet placardTemplet);

    public boolean placardCount(Long templetId);

    public boolean delete(Long templetId);
}
