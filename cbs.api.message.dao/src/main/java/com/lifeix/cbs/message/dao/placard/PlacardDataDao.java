package com.lifeix.cbs.message.dao.placard;


import com.lifeix.cbs.message.dto.PlacardData;

public interface PlacardDataDao {

    public boolean insert(PlacardData placardData);

    public PlacardData findById(Long userId);

    public boolean update(PlacardData placardData);

    public boolean delete(Long userId);

}