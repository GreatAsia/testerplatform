package com.okay.testcenter.service.alarm;

import com.github.pagehelper.PageInfo;
import com.okay.testcenter.domain.AlarmHistory;

import java.util.List;

/**
 * @author zhou
 * @date 2020/12/23
 */
public interface AlarmHistoryService {

    void insert(AlarmHistory alarmHistory);

    void update(AlarmHistory alarmHistory);

    void delete(int id);

    AlarmHistory findById(int id);

    PageInfo findList(int currentPage, int pageSize);

    List<AlarmHistory> findByTaskId(int taskId);

    List<AlarmHistory> list();

    Integer getLastTaskId();


}
