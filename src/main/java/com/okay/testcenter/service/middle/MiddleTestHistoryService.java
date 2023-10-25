package com.okay.testcenter.service.middle;

import com.github.pagehelper.PageInfo;
import com.okay.testcenter.domain.middle.RequestSampler;
import com.okay.testcenter.domain.report.MiddleTestHistory;


public interface MiddleTestHistoryService {

     int insertMiddleTestHistory(MiddleTestHistory middleTestHistory);

     void updateMiddleTestHistory(MiddleTestHistory middleTestHistory);

     void deleteMiddleTestHistory(int id);

     MiddleTestHistory findMiddleTestHistoryById(int id);

     PageInfo findMiddleTestHistoryList(int currentPage, int pageSize);

     PageInfo findMiddleTestHistoryByEnvAndProjectId(int env, int projectId, int currentPage, int pageSize);

     RequestSampler findLoginInfoByProjectAndEnv(int project_id, int env_id);

     Long getLastMiddleTestHistoryId();

     void deleteUseLessHistory(Long leaveId);
}
