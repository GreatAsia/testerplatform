package com.okay.testcenter;


import com.alibaba.fastjson.JSONObject;
import com.okay.testcenter.domain.report.UiPadCaseList;
import com.okay.testcenter.service.ui.UiPadCaseListService;
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
public class UiPadCaseListTest {

    private  final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    UiPadCaseListService uiPadCaseListService;


    @Test
    public void testInsertUiPadCaseList() {

        UiPadCaseList uiPadCaseList = new UiPadCaseList();
        uiPadCaseList.setRunId(51);
        uiPadCaseList.setSerialno("W1706001234");
        uiPadCaseList.setCaseName("testCase");
        uiPadCaseList.setCaseDesc("test");
        uiPadCaseList.setResult("ERROE");
        uiPadCaseList.setStartTime("2019-0312");
        uiPadCaseList.setElapsedTime("1分20秒");
        uiPadCaseList.setErrorLog("test report");
        uiPadCaseList.setTestLogPath("test log");
        uiPadCaseList.setCaseModule("test");
        uiPadCaseList.setPicturePath("/xdfapp/apppics/1/testPicture/test.png");
        uiPadCaseList.setTestLogPath("/xdfapp/apppics/1/testPicture/test.log");

        logger.info("[uiPadCaseList]" + JSONObject.toJSON(uiPadCaseList));
        uiPadCaseListService.insertUiPadCaseList(uiPadCaseList);

    }


    @Test
    public void testUpdateUiPadCaseList() {

        UiPadCaseList uiPadCaseList = new UiPadCaseList();
        uiPadCaseList.setId(1);
        uiPadCaseList.setRunId(1);
        uiPadCaseList.setSerialno("W1706001239");
        uiPadCaseList.setCaseName("casename");
        uiPadCaseList.setCaseDesc("test case");
        uiPadCaseList.setResult("PASS");
        uiPadCaseList.setStartTime("2019-03-12");
        uiPadCaseList.setElapsedTime("5001ms");
        uiPadCaseList.setErrorLog("testlog pass");

        logger.info("[uiPadCaseList]" + JSONObject.toJSON(uiPadCaseList));
        uiPadCaseListService.updateUiPadCaseList(uiPadCaseList);

    }

    @Test
    public void testDeleteUiPadCaseList() {

        uiPadCaseListService.deleterUiPadCaseList(2);
    }

    @Test
    public void testFindUiPadCaseListById() {

        UiPadCaseList uiPadCaseList = uiPadCaseListService.findUiPadCaseListById(1);
        logger.info("[uiPadCaseList]" + JSONObject.toJSON(uiPadCaseList));
    }


    @Test
    public void testFindUiPadCaseListByRunId() {

        List<UiPadCaseList> uiPadCaseListList = uiPadCaseListService.findUiPadCaseListByRunId(1);


        logger.info("[uiPadCaseList]" + JSONObject.toJSONString(uiPadCaseListList));
    }

    @Test
    public void testfindUiPadCaseListBySerialno() {

        List<UiPadCaseList> uiPadCaseList = uiPadCaseListService.findUiPadCaseListBySerialno("W1706001236");
        logger.info("[uiPadCaseList]" + JSONObject.toJSON(uiPadCaseList));

    }


    @Test
    public void testFindUiPadCaseList() {

        List<UiPadCaseList> uiPadCaseLists = uiPadCaseListService.findUiPadCaseList();
        logger.info("[uiPadCaseLists]" + JSONObject.toJSON(uiPadCaseLists));
    }

}
