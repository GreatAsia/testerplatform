package com.okay.testcenter;



import com.alibaba.fastjson.JSONObject;
import com.okay.testcenter.domain.report.UiPadRunIdList;
import com.okay.testcenter.service.ui.UiPadRunIdListService;
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
public class UiPadRunIdListTest {


    private static final Logger logger = LoggerFactory.getLogger(UiPadRunIdListTest.class);

    @Resource
    UiPadRunIdListService uiPadRunIdListService;


    @Test
    public void insertUiPadRunIdList() {
        UiPadRunIdList uiPadRunIdList = new UiPadRunIdList();
        uiPadRunIdList.setTotalDevice(3);
        uiPadRunIdList.setPassDevice(1);
        uiPadRunIdList.setFailDevice(1);
        uiPadRunIdList.setErrorDevice(1);
        uiPadRunIdList.setElapsedTime("12分30秒");
        uiPadRunIdList.setStartTime("2019-02-28 11:03:12");
        uiPadRunIdList.setPassRate("0.0%");
        logger.info("report:" + JSONObject.toJSONString(uiPadRunIdList));
        uiPadRunIdListService.insertUiPadRunIdList(uiPadRunIdList);

    }

    @Test
    public void updateUiPadRunIdList() {
        UiPadRunIdList uiPadRunIdList = new UiPadRunIdList();
        uiPadRunIdList.setId(26);
        uiPadRunIdList.setTotalDevice(5);
        uiPadRunIdList.setPassDevice(3);
        uiPadRunIdList.setFailDevice(1);
        uiPadRunIdList.setErrorDevice(1);
        uiPadRunIdList.setElapsedTime("3s");
        uiPadRunIdList.setStartTime("2019-02-26");
        uiPadRunIdList.setPassRate("30%");
        logger.info("report:" + JSONObject.toJSONString(uiPadRunIdList));
        uiPadRunIdListService.updateUiPadRunIdList(uiPadRunIdList);
    }

    @Test
    public void deleteUiPadRunIdList() {

        uiPadRunIdListService.deleteUiPadById(2);
    }

    @Test
    public void findUiPadRunIdList() {

        UiPadRunIdList uiPadRunIdList = uiPadRunIdListService.findUiPadRunIdList(1);
        logger.info("[uiPadRunIdList]" + JSONObject.toJSON(uiPadRunIdList));
    }

    @Test
    public void findUiPadRunIdLists() {

        List<UiPadRunIdList> uiPadRunIdList = uiPadRunIdListService.findUiPadRunList(1, 10).getList();
        logger.info("[uiPadRunIdList]" + JSONObject.toJSON(uiPadRunIdList));
    }

    @Test
    public void testGetLastRunId() {
        int runId = uiPadRunIdListService.getLastId();
        logger.info("[runId]" + runId);
    }
}
