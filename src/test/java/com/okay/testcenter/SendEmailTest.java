package com.okay.testcenter;

import com.alibaba.fastjson.JSONObject;
import com.okay.testcenter.domain.report.MiddleTestHistory;
import com.okay.testcenter.service.SendEmailService;
import com.okay.testcenter.service.middle.MiddleRunService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CompletableFuture;

import static com.okay.testcenter.tools.ExceptionUtil.getMessage;

/**
 * @author zhou
 * @date 2020/6/4
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SendEmailTest {


    private static final Logger logger = LoggerFactory.getLogger(SendEmailTest.class);


    @Autowired
    MiddleRunService middleRunService;
    @Autowired
    SendEmailService sendEmailService;
    MiddleTestHistory history = new MiddleTestHistory();
    private String sendTo = "zhangyazhou@okay.cn;";
    private String title = "testEmail";
    private String item = "middle";

    @Test
    public void sendEmailTest() {

        try {
            CompletableFuture<MiddleTestHistory> compHistory = middleRunService.runMonitorProject(1, 1, 0);
            CompletableFuture.allOf(compHistory).join();
            history = compHistory.get();

        } catch (Exception e) {
            logger.error("执行监控发生错误:" + getMessage(e));

        } finally {
            if (history != null) {
                if ("FAIL".equals(history.getResult())) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("sendTo", sendTo);
                    jsonObject.put("title", title);
                    jsonObject.put("item", item);
                    jsonObject.put("historyId", history.getId());
                    sendEmailService.sendEmail(jsonObject);
                }
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("sendTo", sendTo);
                jsonObject.put("title", title);
                jsonObject.put("item", item);
                jsonObject.put("historyId", 0);
                sendEmailService.sendEmail(jsonObject);
            }


        }


    }
}
