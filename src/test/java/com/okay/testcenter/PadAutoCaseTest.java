package com.okay.testcenter;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.okay.testcenter.domain.ui.PadAutoCase;
import com.okay.testcenter.service.ui.PadAutoCaseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PadAutoCaseTest {

    private static final Logger logger = LoggerFactory.getLogger(PadAutoCaseTest.class);

    @Resource
    PadAutoCaseService padAutoCaseService;

    @Test
    public void insertCase() {

        PadAutoCase padAutoCase = new PadAutoCase();
        padAutoCase.setCaseName("classroom");
        padAutoCase.setCaseContent("上课");
        padAutoCase.setCaseDesc("com.okay.classname#test");
        padAutoCaseService.insertCase(padAutoCase);
    }

    @Test
    public void deleteCase() {
        padAutoCaseService.deleteCase(2);
    }

    @Test
    public void updateCase() {
        PadAutoCase padAutoCase = new PadAutoCase();
        padAutoCase.setId(26);
        padAutoCase.setCaseContent("测试");
        padAutoCase.setCaseName("classupdate");
        padAutoCase.setCaseDesc("com.okay.classname#test");
        padAutoCaseService.updateCase(padAutoCase);
    }


    @Test
    public void findCaseById() {
        PadAutoCase padAutoCase = padAutoCaseService.findCaseById(1);
        logger.info("【padAutoCase】:" + JSONObject.toJSONString(padAutoCase));

    }

    @Test
    public void findCaseByName() {
        List<PadAutoCase> padAutoCase = padAutoCaseService.findCaseByName("上课");
        logger.info("[padAutoCase]:" + JSONObject.toJSONString(padAutoCase));
    }

    @Test
    public void findModelList() {
        PageInfo padAutoCaseList = padAutoCaseService.findCaseList(1,10);
        logger.info("[padAutoCaseList]:" + JSONObject.toJSONString(padAutoCaseList));
    }


}
