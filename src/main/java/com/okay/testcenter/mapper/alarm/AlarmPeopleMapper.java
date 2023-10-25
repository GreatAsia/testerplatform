package com.okay.testcenter.mapper.alarm;

import com.okay.testcenter.domain.AlarmPeople;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author zhou
 * @date 2021/3/5
 */
@Mapper
public interface AlarmPeopleMapper {


    void insert(AlarmPeople alarmPeople);

    void update(AlarmPeople alarmPeople);

    void delete(int id);

    AlarmPeople findById(int id);

    AlarmPeople findByProject(String project);

    List<AlarmPeople> list();


}
