package com.okay.testcenter.service.alarm;


import com.okay.testcenter.domain.AlarmPeople;

import java.util.List;

/**
 * @author zhou
 * @date 2020/12/23
 */
public interface AlarmPeopleService {

    void insert(AlarmPeople alarmPeople);

    void update(AlarmPeople alarmPeople);

    void delete(int id);

    AlarmPeople findById(int id);

    AlarmPeople findByProject(String project);
    List<AlarmPeople> list();


}
