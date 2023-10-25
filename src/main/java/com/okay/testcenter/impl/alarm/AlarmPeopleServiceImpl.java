package com.okay.testcenter.impl.alarm;


import com.okay.testcenter.domain.AlarmPeople;
import com.okay.testcenter.mapper.alarm.AlarmPeopleMapper;
import com.okay.testcenter.service.alarm.AlarmPeopleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhou
 * @date 2021/3/5
 */
@Service("AlarmPeopleService")
public class AlarmPeopleServiceImpl implements AlarmPeopleService {

    @Resource
    AlarmPeopleMapper alarmPeopleMapper;


    @Override
    public void insert(AlarmPeople alarmPeople) {
        alarmPeopleMapper.insert(alarmPeople);
    }

    @Override
    public void update(AlarmPeople alarmPeople) {
        alarmPeopleMapper.update(alarmPeople);
    }

    @Override
    public void delete(int id) {
        alarmPeopleMapper.delete(id);
    }

    @Override
    public AlarmPeople findById(int id) {
        return alarmPeopleMapper.findById(id);
    }

    @Override
    public AlarmPeople findByProject(String project) {
        return alarmPeopleMapper.findByProject(project);
    }

    @Override
    public List<AlarmPeople> list() {
        return alarmPeopleMapper.list();
    }
}
