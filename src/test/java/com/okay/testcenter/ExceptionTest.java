package com.okay.testcenter;


import com.okay.testcenter.domain.middle.ResponseSampler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.okay.testcenter.tools.GetTime.getTotalTime;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExceptionTest {


    private static final Logger logger = LoggerFactory.getLogger(ExceptionTest.class);

    @Test
    public void testException() {

        int s = 1 / 0;

    }

    @Test
    public void testTotalTime() throws ParseException {

        SimpleDateFormat df =  new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime = df.parse("2020-12-09 18:18:19");

        Date endTime = df.parse("2020-12-10 10:37:10");

        String totalTime = getTotalTime(endTime, startTime);
        logger.info("totalTime {}",totalTime);
    }


    @Test
    public void testContent() {
        String info = "{\"requestid\":\"qa_012108861348\",\"code\":0,\"msg\":\"\",\"data\":{\"user_state\":1}}";
        String code = "\"user_state\": 1";
        if (info.contains(code)) {
            logger.info("pass");
        } else {
            logger.info("fail");
        }
    }

    @Test
    public void testStatus() {

        ResponseSampler responseSampler = new ResponseSampler();
        logger.info("response=={}", responseSampler.getResponse());
        logger.info("code=={}", responseSampler.getResponseCode());
    }
}
