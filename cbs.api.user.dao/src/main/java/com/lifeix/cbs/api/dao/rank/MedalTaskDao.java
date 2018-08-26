package com.lifeix.cbs.api.dao.rank;

import com.lifeix.cbs.api.dto.rank.MedalTask;

public interface MedalTaskDao {
    public MedalTask findById(Long id);
    public boolean update(MedalTask entity);
}
