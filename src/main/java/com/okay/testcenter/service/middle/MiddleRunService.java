package com.okay.testcenter.service.middle;

import com.alibaba.fastjson.JSONObject;
import com.okay.testcenter.domain.report.MiddleTestHistory;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;


public interface MiddleRunService {

    Object debugInterface(JSONObject requestInfo);

    Object runSingle(int id);

    @Async
    Object runInterface(int env_id, int interface_id, int caseType);

    @Async
    Object runModule(int env_id, int module_id,int caseType);

    @Async
    Object runProject(int env_id, int project_id, int caseType);

    @Async
    CompletableFuture<MiddleTestHistory> runMonitorProject(int env_id, int project_id, int caseType);

}
