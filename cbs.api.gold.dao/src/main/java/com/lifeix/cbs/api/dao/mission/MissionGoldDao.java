package com.lifeix.cbs.api.dao.mission;

import com.lifeix.cbs.api.dto.mission.MissionGold;

import java.util.List;

/**
 * Created by lhx on 16-6-17 下午6:21
 *
 * @Description
 */
public interface MissionGoldDao {

    public MissionGold insert(MissionGold missionGold);

    public boolean update(MissionGold missionGold);

    public MissionGold findById(Long id);

    List<MissionGold> getAll();

    public boolean delete(Long id);
}
