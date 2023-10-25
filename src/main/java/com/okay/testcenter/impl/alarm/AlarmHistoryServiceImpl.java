package com.okay.testcenter.impl.alarm;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.okay.testcenter.domain.AlarmHistory;
import com.okay.testcenter.mapper.alarm.AlarmHistoryMapper;
import com.okay.testcenter.service.alarm.AlarmHistoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhou
 * @date 2020/12/23
 */
@Service("AlarmHistoryService")
public class AlarmHistoryServiceImpl implements AlarmHistoryService {

    @Resource
    AlarmHistoryMapper alarmHistoryMapper;


    @Override
    public void insert(AlarmHistory alarmHistory) {
        alarmHistoryMapper.insert(alarmHistory);
    }

    @Override
    public void update(AlarmHistory alarmHistory) {
        alarmHistoryMapper.update(alarmHistory);
    }

    @Override
    public void delete(int id) {
        alarmHistoryMapper.delete(id);
    }

    @Override
    public AlarmHistory findById(int id) {
        return alarmHistoryMapper.findById(id);
    }

    @Override
    public PageInfo findList(int currentPage, int pageSize) {

        PageHelper.startPage(currentPage, pageSize);
        List<AlarmHistory> alarmHistories = alarmHistoryMapper.list();
        PageInfo pageInfo = new PageInfo(alarmHistories);
        return pageInfo;
    }

    @Override
    public List<AlarmHistory> findByTaskId(int taskId) {
        return alarmHistoryMapper.findByTaskId(taskId);
    }

    @Override
    public List<AlarmHistory> list() {
        return alarmHistoryMapper.list();
    }

    @Override
    public Integer getLastTaskId() {
        return alarmHistoryMapper.getLastTaskId();
    }
}
