package com.okay.testcenter.service.middle;


import com.github.pagehelper.PageInfo;
import com.okay.testcenter.domain.middle.MiddleCase;
import com.okay.testcenter.domain.middle.MiddleCaseRule;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface MiddleCaseService {

     void insertMiddleCase(MiddleCase middleCase);

     void deleteMiddleCase(int id);

     void updateMiddleCase(MiddleCase middleCase);

     MiddleCase findMiddleCaseById(int id);

     List<MiddleCase> findMiddleCaseByName(String name);

     List<MiddleCase> findMiddleCaseByPath(String path);

     PageInfo findMiddleCaseList(int currentPage, int pageSize);

     PageInfo findMiddleCaseByEnvAndInterface(int env_id,int interface_id, int currentPage, int pageSize);

     List<MiddleCase> findMiddleCaseByEnvAndInterface(int env_id, int interface_id);

    List<MiddleCase> findMiddleCaseByEnvId(int envId);

    @Async
    void syncMiddleCase(int fromEnv, int toEnv);

    @Async
    void syncMiddleCaseByProject(int projectId,int fromEnv, int toEnv);


    List<MiddleCaseRule> getMiddleCaseRule(String params,String requestType);

    List<MiddleCase> genMiddleCase(List<MiddleCaseRule> middleCaseRules,String params,String requestMethod, MiddleCase oldMiddleCase);

}
