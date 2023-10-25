package com.okay.testcenter.mapper.alarm;

import com.okay.testcenter.domain.AlarmHistory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author zhou
 * @date 2020/12/23
 */
@Mapper
public interface AlarmHistoryMapper {

    void insert(AlarmHistory alarmHistory);

    void update(AlarmHistory alarmHistory);

    void delete(int id);

    AlarmHistory findById(int id);

    List<AlarmHistory> findByTaskId(int taskId);

    List<AlarmHistory> list();

    Integer getLastTaskId();

}
