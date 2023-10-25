package com.okay.testcenter;



import com.alibaba.fastjson.JSONObject;
import com.okay.testcenter.domain.report.UiPadSerialnoList;
import com.okay.testcenter.service.ui.UiPadSerialnoListService;
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
public class UiPadSerialnoListTest {
    private static final Logger logger = LoggerFactory.getLogger(UiPadSerialnoListTest.class);

    @Resource
    UiPadSerialnoListService uiPadSerialnoListService;


    @Test
    public void testInsertUiPadSerialnoList() {

        UiPadSerialnoList uiPadSerialnoList = new UiPadSerialnoList();
        uiPadSerialnoList.setRunId(1);
        uiPadSerialnoList.setSerialno("W1706001235");
        uiPadSerialnoList.setVersion("V7.0");
        uiPadSerialnoList.setTotalCase(10);
        uiPadSerialnoList.setPassCase(5);
        uiPadSerialnoList.setFailCase(3);
        uiPadSerialnoList.setErrorCase(2);
        uiPadSerialnoList.setElapsedTime("30分钟30秒");
        uiPadSerialnoList.setStartTime("2019-2-28 11:05::24");
        uiPadSerialnoList.setPassRate("50%");
        uiPadSerialnoList.setApkVersion("2.0");
        uiPadSerialnoList.setRomVersion("V3.1");
        uiPadSerialnoList.setNetWork("okay");
        uiPadSerialnoList.setEnv("dev");

        logger.info("[uiPadSerialnoList]" + JSONObject.toJSON(uiPadSerialnoList));
        uiPadSerialnoListService.insertUiPadSerialnoList(uiPadSerialnoList);

    }


    @Test
    public void testUpdateUiPadSerialnoList() {

        UiPadSerialnoList uiPadSerialnoList = new UiPadSerialnoList();
        uiPadSerialnoList.setId(51);
        uiPadSerialnoList.setRunId(1);
        uiPadSerialnoList.setSerialno("W1706001234");
        uiPadSerialnoList.setVersion("V7.1");
        uiPadSerialnoList.setTotalCase(10);
        uiPadSerialnoList.setPassCase(5);
        uiPadSerialnoList.setFailCase(3);
        uiPadSerialnoList.setErrorCase(2);
        uiPadSerialnoList.setElapsedTime("5001ms");
        uiPadSerialnoList.setStartTime("2019-2-27:10:37");
        uiPadSerialnoList.setPassRate("50%");
        uiPadSerialnoList.setPassRate("50%");
        uiPadSerialnoList.setApkVersion("2.1");
        uiPadSerialnoList.setRomVersion("V3.3");
        uiPadSerialnoList.setNetWork("OKAY");
        uiPadSerialnoList.setEnv("hotfix");

        logger.info("[uiPadSerialnoList]" + JSONObject.toJSON(uiPadSerialnoList));
        uiPadSerialnoListService.updateUiPadSerialnoList(uiPadSerialnoList);

    }

    @Test
    public void testDeleteUiPadSerialnoList() {

        uiPadSerialnoListService.deleterUiPadSerialnoList(2);
    }

    @Test
    public void testFindUiPadSerialnoListById() {

        UiPadSerialnoList uiPadSerialnoList = uiPadSerialnoListService.findUiPadSerialnoListById(284);
        logger.info("[uiPadSerialnoList]" + JSONObject.toJSON(uiPadSerialnoList));
    }


    @Test
    public void testFindUiPadSerialnoListByRunId() {

        List<UiPadSerialnoList> uiPadSerialnoList = uiPadSerialnoListService.findUiPadSerialnoListByRunId(284);
        logger.info("[uiPadSerialnoList]" + JSONObject.toJSON(uiPadSerialnoList));
    }

    @Test
    public void testFindUiPadSerialnoListBySerialno() {

        UiPadSerialnoList uiPadSerialnoList = uiPadSerialnoListService.findUiPadSerialnoListBySerialno("W1706001236");
        logger.info("[uiPadSerialnoList]" + JSONObject.toJSON(uiPadSerialnoList));

    }


    @Test
    public void testFindUiPadSerialnoList() {

        List<UiPadSerialnoList> uiPadSerialnoLists = uiPadSerialnoListService.findUiPadSerialnoList();
        logger.info("[uiPadSerialnoLists]" + JSONObject.toJSON(uiPadSerialnoLists));
    }


    @Test
    public void testfindUiPadSerialnoListByRunIdAndSerialno() {

        UiPadSerialnoList uiPadSerialnoLists = uiPadSerialnoListService.findUiPadSerialnoListByRunIdAndSerialno(284, "S16100101f");
        logger.info("[uiPadSerialnoLists]" + JSONObject.toJSON(uiPadSerialnoLists));


    }

}
