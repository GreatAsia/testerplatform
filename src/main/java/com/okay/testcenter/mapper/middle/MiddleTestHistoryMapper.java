package com.okay.testcenter.mapper.middle;

import com.okay.testcenter.domain.middle.RequestSampler;
import com.okay.testcenter.domain.report.MiddleTestHistory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MiddleTestHistoryMapper {

     int insertMiddleTestHistory(MiddleTestHistory middleTestHistory);

     void updateMiddleTestHistory(MiddleTestHistory middleTestHistory);

     void deleteMiddleTestHistory(int id);

     MiddleTestHistory findMiddleTestHistoryById(int id);

     List<MiddleTestHistory> findMiddleTestHistoryList();

     List<MiddleTestHistory> findMiddleTestHistoryByEnvAndProjectId(int env, int projectId);

     RequestSampler findLoginInfoByProjectAndEnv(int project_id, int env_id);

     Integer getLastMiddleTestHistoryId();

     void deleteUseLessHistory(long leaveId);
}
