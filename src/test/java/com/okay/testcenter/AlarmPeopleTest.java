package com.okay.testcenter;

import com.alibaba.fastjson.JSONObject;
import com.okay.testcenter.domain.AlarmPeople;
import com.okay.testcenter.service.alarm.AlarmPeopleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author zhou
 * @date 2020/12/23
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AlarmPeopleTest {
    private static final Logger logger = LoggerFactory.getLogger(AlarmPeopleTest.class);

    @Resource
    AlarmPeopleService alarmPeopleService;

    @Test
    public void testInsert() {
        AlarmPeople alarmPeople = new AlarmPeople();
        alarmPeople.setProject("private-web");
        alarmPeople.setPhone("18103399772");
        alarmPeople.setQa_name("QA");
        alarmPeople.setRd_name("RD");
        alarmPeople.setCreate_time(new Date());
        alarmPeople.setUpdate_time(new Date());
        alarmPeopleService.insert(alarmPeople);

    }

    @Test
    public void testUpdate() {
        AlarmPeople alarmPeople = new AlarmPeople();
        alarmPeople.setId(5);
        alarmPeople.setProject("private-web");
        alarmPeople.setPhone("18103399772");
        alarmPeople.setQa_name("QA");
        alarmPeople.setRd_name("RD");
        alarmPeople.setUpdate_time(new Date());
        alarmPeopleService.update(alarmPeople);

    }

    @Test
    public void testList() {
        List<AlarmPeople> list = alarmPeopleService.list();
        logger.info("list=={}", JSONObject.toJSONString(list));
    }

    @Test
    public void testFindByProject() {
        AlarmPeople alarmPeople = alarmPeopleService.findByProject("es-svr");
        logger.info("alarmPeople=={}", JSONObject.toJSONString(alarmPeople));
    }


}
