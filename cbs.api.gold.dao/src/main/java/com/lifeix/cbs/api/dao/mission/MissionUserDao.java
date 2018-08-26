package com.lifeix.cbs.api.dao.mission;

import com.lifeix.cbs.api.dto.mission.MissionUser;

import java.util.List;

/**
 * Created by lhx on 16-6-17 下午6:02
 *
 * @Description
 */
public interface MissionUserDao{

    MissionUser findByUserId(Long userId);

    public MissionUser findById(Long id);

    public boolean insert(MissionUser missionUser);

    public boolean update(MissionUser missionUser);

    /**
     * 查询列表（内部）
     */
    List<MissionUser> findByListInner(Integer skip, Integer limit);
}
