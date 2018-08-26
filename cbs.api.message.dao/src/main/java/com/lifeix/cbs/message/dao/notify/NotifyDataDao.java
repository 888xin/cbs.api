package com.lifeix.cbs.message.dao.notify;

import java.util.List;

import com.lifeix.cbs.message.dto.notify.NotifyData;

public interface NotifyDataDao {

    boolean insert(NotifyData nData);

    int getUnreadNotifyCount(Long userId, List<Integer> type);

    List<NotifyData> findUserNotifys(Long userId, Long page, Long endId, List<Integer> type, Integer limit);

    void updateUnreadNotify(Long userId, List<Integer> type);

}
