package com.okay.testcenter.impl.dubbo;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.okay.testcenter.domain.Env;
import com.okay.testcenter.domain.dubbo.DubboCase;
import com.okay.testcenter.domain.dubbo.DubboCaseHistory;
import com.okay.testcenter.domain.dubbo.DubboTestHistory;
import com.okay.testcenter.mapper.dubbo.DubboCaseMapper;
import com.okay.testcenter.mapper.dubbo.DubboInterfaceHistoryMapper;
import com.okay.testcenter.mapper.dubbo.DubboTestHistoryMapper;
import com.okay.testcenter.mapper.middle.EnvMapper;
import com.okay.testcenter.service.dubbo.DubboCaseService;
import com.okay.testcenter.tools.DubboServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.okay.testcenter.tools.GetTime.getTime;


@Service("DubboCaseService")
public class DubboCaseServiceImpl implements DubboCaseService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    DubboCaseMapper dubboCaseMapper;
    @Resource
    EnvMapper envMapper;
    @Resource
    DubboTestHistoryMapper dubboTestHistoryMapper;
    @Resource
    DubboInterfaceHistoryMapper dubboInterfaceHistoryMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertDubboInterface(DubboCase dubboCase) {

        dubboCaseMapper.insertDubboInterface(dubboCase);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDubboInterface(DubboCase dubboCase) {
        dubboCaseMapper.updateDubboInterface(dubboCase);

    }

    @Override
    public DubboCase findDubboInterfaceById(int id) {
        return dubboCaseMapper.findDubboInterfaceById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDubboInterfaceById(int id) {
        dubboCaseMapper.deleteDubboInterfaceById(id);
    }

    @Override
    public PageInfo findDubboInterfaceList(int currentPage, int pageSize) {

        PageHelper.startPage(currentPage, pageSize);
        List<DubboCase> dubboCaseList = dubboCaseMapper.findDubboInterfaceList();
        PageInfo pageInfo = new PageInfo(dubboCaseList);
        return pageInfo;
    }

    @Override
    public List<DubboCase> findDubboInterfaceList() {

        List<DubboCase> dubboCase = dubboCaseMapper.findDubboInterfaceList();
        return dubboCase;
    }

    @Override
    public PageInfo findDubboInterfaceListByModel(int model, int currentPage, int pageSize) {

        PageHelper.startPage(currentPage, pageSize);
        List<DubboCase> dubboCaseList = dubboCaseMapper.findDubboInterfaceListByModel(model);
        PageInfo pageInfo = new PageInfo(dubboCaseList);
        return pageInfo;

    }

    @Override
    public List<DubboCase> findDubboInterfaceListByModel(int model) {

        List<DubboCase> dubboCaseList = dubboCaseMapper.findDubboInterfaceListByModel(model);
        return dubboCaseList;
    }

    @Override
    public List<DubboCase> findDubboInterfaceListByModelName(String modelName) {

        List<DubboCase> dubboCaseList = dubboCaseMapper.findDubboInterfaceListByModelName(modelName);
        return dubboCaseList;
    }

    @Override
    public PageInfo findDubboInterfaceListByModelName(String modelName, int currentPage, int pageSize) {

        PageHelper.startPage(currentPage, pageSize);
        List<DubboCase> dubboCaseList = dubboCaseMapper.findDubboInterfaceListByModelName(modelName);
        PageInfo pageInfo = new PageInfo(dubboCaseList);
        return pageInfo;
    }


    @Override
    public DubboCaseHistory runDubboById(JSONObject request) {

        int id = Integer.parseInt(request.get("id").toString());
        String address = request.get("address").toString();
        String zkAddress = getAddress(address);
        DubboCase dubboCase = dubboCaseMapper.findDubboInterfaceById(id);
        //设置开始时间
        String startTime = getTime();
        //运行case
        Map requestParams = JSON.parseObject(dubboCase.getParams().trim(), Map.class);
        Map map = new HashMap<String, Object>(2);
        map.put("ParamType", dubboCase.getRequestType());
        map.put("Object", requestParams);
        List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
        params.add(map);
        dubboCase.setParamList(params);
        dubboCase.setAddress(zkAddress);
        DubboServiceFactory dubbo = DubboServiceFactory.getInstance();
        Object result = dubbo.genericInvoke(dubboCase);
        dubbo.destory();
        logger.info("responseResult===" + JSONObject.toJSON(result));
        String historyResult = result.toString().contains(dubboCase.getCheckData()) + "";
        DubboCaseHistory dubboCaseHistory = new DubboCaseHistory();
        DubboTestHistory dubboTestHistory = new DubboTestHistory();
        dubboTestHistory.setTotalCase(1);
        if ("true".equals(historyResult)) {
            dubboTestHistory.setPassCase(1);
            dubboTestHistory.setFailCase(0);
            historyResult = "PASS";
        } else {
            dubboTestHistory.setPassCase(0);
            dubboTestHistory.setFailCase(1);
            historyResult = "FAIL";
        }
        Map requestData = new HashMap(1);
        requestData.put("requestParam", JSONObject.toJSON(dubboCase));
        //Dubbo接口历史数据
        dubboCaseHistory.setCaseName(dubboCase.getCaseName());
        dubboCaseHistory.setModelId(dubboCase.getModel());
        dubboCaseHistory.setEnv(address);
        dubboCaseHistory.setStartTime(startTime);
        dubboCaseHistory.setEndTime(getTime());
        dubboCaseHistory.setRequestData(requestData.toString());
        dubboCaseHistory.setResponseContent(result.toString());
        dubboCaseHistory.setResult(historyResult);
        dubboTestHistory.setEnv(address);
        dubboTestHistory.setResult(historyResult);
        dubboTestHistory.setStartTime(dubboCaseHistory.getStartTime());
        dubboTestHistory.setEndTime(getTime());
        dubboTestHistory.setModelId(dubboCase.getModel());

        dubboTestHistoryMapper.insertTestHistory(dubboTestHistory);
        int historyId = dubboTestHistoryMapper.getLastTestHistoryId();
        dubboCaseHistory.setHistoryId(historyId);
        dubboInterfaceHistoryMapper.insertHistory(dubboCaseHistory);

        return dubboCaseHistory;
    }


    @Override
    public Object runDubboByModule(JSONObject request) {
        String modelName = request.get("modelName").toString();
        String address = request.get("address").toString();
        String zkAddress = getAddress(address);
        DubboServiceFactory dubbo = DubboServiceFactory.getInstance();
        //Dubbo接口历史数据
        List<DubboCaseHistory> dubboInterfaceHistories = new ArrayList<>();
        List<DubboCaseHistory> dubboInterfaceError = new ArrayList<>();
        int totalCase = 0;
        int passCase = 0;
        int failCase = 0;
        String historyResult = "true";
        String startTime = "";
        List<DubboCase> dubboCaseList = dubboCaseMapper.findDubboInterfaceListByModelName(modelName);
        for (int i = 0; i < dubboCaseList.size(); i++) {
            totalCase = totalCase + 1;
            //设置开始时间
            startTime = getTime();
            //运行case
            Map requestParams = JSON.parseObject(dubboCaseList.get(i).getParams().trim(), Map.class);
            Map map = new HashMap<String, Object>(2);
            map.put("ParamType", dubboCaseList.get(i).getRequestType());
            map.put("Object", requestParams);
            List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
            params.add(map);
            dubboCaseList.get(i).setParamList(params);
            dubboCaseList.get(i).setAddress(zkAddress);
            Object result = dubbo.genericInvoke(dubboCaseList.get(i));
            logger.info("responseResult===" + result);
            historyResult = result.toString().contains(dubboCaseList.get(i).getCheckData()) + "";
            if ("true".equals(historyResult)) {
                passCase = passCase + 1;
                historyResult = "PASS";
            } else {
                failCase = failCase + 1;
                historyResult = "FAIL";
            }
            Map requestData = new HashMap(1);
            requestData.put("requestParams", JSONObject.toJSON(dubboCaseList.get(i)));
            DubboCaseHistory dubboCaseHistory = new DubboCaseHistory();
            dubboCaseHistory.setCaseName(dubboCaseList.get(i).getCaseName());
            dubboCaseHistory.setModelId(dubboCaseList.get(i).getModel());
            dubboCaseHistory.setEnv(address);
            dubboCaseHistory.setStartTime(startTime);
            dubboCaseHistory.setEndTime(getTime());
            dubboCaseHistory.setRequestData(requestData.toString());
            dubboCaseHistory.setResponseContent(result.toString());
            dubboCaseHistory.setResult(historyResult);
            dubboInterfaceHistories.add(dubboCaseHistory);
        }
        dubbo.destory();
        String testResult = "";
        if (failCase == 0) {
            testResult = "PASS";
        } else {
            testResult = "FAIL";
        }
        DubboTestHistory dubboTestHistory = new DubboTestHistory();
        dubboTestHistory.setEnv(address);
        dubboTestHistory.setResult(testResult);
        dubboTestHistory.setTotalCase(totalCase);
        dubboTestHistory.setPassCase(passCase);
        dubboTestHistory.setFailCase(failCase);
        dubboTestHistory.setStartTime(startTime);
        dubboTestHistory.setEndTime(getTime());
        dubboTestHistory.setModelId(dubboCaseList.get(0).getModel());
        dubboTestHistoryMapper.insertTestHistory(dubboTestHistory);
        int historyId = dubboTestHistoryMapper.getLastTestHistoryId();

        for (DubboCaseHistory history : dubboInterfaceHistories) {
            if ("FAIL".equals(history.getResult())) {
                dubboInterfaceError.add(history);
            }
            history.setHistoryId(historyId);
            dubboInterfaceHistoryMapper.insertHistory(history);
        }
        return failCase;
    }


    @Override
    public PageInfo findDubboInterfaceListByModelAndEnv(int model, int envId, int currentPage, int pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        List<DubboCase> dubboCaseList = dubboCaseMapper.findDubboInterfaceListByModelAndEnv(model, envId, currentPage, pageSize);
        PageInfo pageInfo = new PageInfo(dubboCaseList);
        return pageInfo;
    }


    @Override
    public List<DubboCase> findDubbocaseByName(String name) {
        return dubboCaseMapper.findDubbocaseByName(name);
    }

    public String getAddress(String address) {
        Env env = envMapper.findEnvByName(address);
        String zkAddress = "zookeeper://" + env.getAddress() + ":" + env.getPort();
        logger.info("zkAddress===" + zkAddress);
        return zkAddress;
    }
}
