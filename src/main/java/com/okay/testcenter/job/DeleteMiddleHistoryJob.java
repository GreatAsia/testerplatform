package com.okay.testcenter.job;

import com.alibaba.fastjson.JSONObject;
import com.okay.testcenter.service.middle.MiddleTestHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhou
 * @date 2021/2/19
 */
public class DeleteMiddleHistoryJob extends BaseJob {

    private static final Long leaveSize = 40000L;
    private static Logger logger = LoggerFactory.getLogger(DeleteMiddleHistoryJob.class);
    @Autowired
    MiddleTestHistoryService middleTestHistoryService;

    @Override
    public void runMonitor(JSONObject jobParam) {

        int day = Integer.valueOf(jobParam.getString("day"));
        Long lastHistoryId = middleTestHistoryService.getLastMiddleTestHistoryId();
        if (lastHistoryId > leaveSize) {
            Long leaveId = lastHistoryId - leaveSize;
            middleTestHistoryService.deleteUseLessHistory(leaveId);
        }
        logger.info("DeleteMiddleHistoryJob 触发完成");

    }
}
