package com.okay.testcenter;

import com.okay.testcenter.domain.AlarmHistory;
import com.okay.testcenter.service.alarm.AlarmHistoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author zhou
 * @date 2020/12/23
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AlarmHistoryTest {
    private static final Logger logger = LoggerFactory.getLogger(AlarmHistoryTest.class);

    @Resource
    AlarmHistoryService alarmHistoryService;

    @Test
    public void testInsert() {

        AlarmHistory alarmHistory = new AlarmHistory();
        alarmHistory.setTaskId(1);
        alarmHistory.setErrorType("登录报错");
        alarmHistory.setServiceName("teacher-web");
        alarmHistory.setTime("2020-12-23 17:07:20");
        alarmHistory.setContent("http://okay-qaplatform.xk12.cn/middle/report/detail/1169062");
        alarmHistory.setAlarm("true");
        alarmHistoryService.insert(alarmHistory);

    }

    @Test
    public void testUpdate() {

        AlarmHistory alarmHistory = new AlarmHistory();
        alarmHistory.setTaskId(1);
        alarmHistory.setErrorType("登录报错");
        alarmHistory.setServiceName("teacher-web");
        alarmHistory.setTime("2020-12-23 17:07:20");
        alarmHistory.setContent("http://okay-qaplatform.xk12.cn/middle/report/detail/1169062");
        alarmHistory.setAlarm("false");
        alarmHistoryService.insert(alarmHistory);

    }



    @Test
    public void testLastTaskId() {

        int id = alarmHistoryService.getLastTaskId();
        System.out.println("id=" + id);
    }


}
