package com.okay.testcenter.impl.middle;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.okay.testcenter.common.constant.Receiver;
import com.okay.testcenter.domain.middle.*;
import com.okay.testcenter.domain.report.MiddleRequestHistory;
import com.okay.testcenter.domain.report.MiddleTestHistory;
import com.okay.testcenter.request.RequestFactory;
import com.okay.testcenter.service.middle.*;
import com.okay.testcenter.tools.GetTime;
import com.okay.testcenter.tools.MailInfo;
import com.okay.testcenter.tools.MailUtil;
import com.okay.testcenter.tools.threadpool.RunMiddleCaseCallAble;
import org.apache.commons.mail.EmailAttachment;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.okay.testcenter.runner.RunnerFactory.prepare;
import static com.okay.testcenter.tools.ExceptionUtil.getMessage;
import static com.okay.testcenter.tools.GetTime.getTime;
import static com.okay.testcenter.tools.threadpool.MyThreadPool.submitTask;
import static com.okay.testcenter.tools.threadpool.MyThreadPool.submitTasks;


@Service("MiddleRunService")
public class MiddleRunServiceImpl implements MiddleRunService {

    private static final int TIME_OUT = 10;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${logging.path}")
    private String logPath;

    @Resource
    MiddleModuleService middleModuleService;
    @Resource
    MiddleInterfaceService middleInterfaceService;
    @Resource
    MiddleCaseService middleCaseService;
    @Resource
    MiddleTestHistoryService middleTestHistoryService;
    @Resource
    MiddleRequestHisoryService middleRequestHisoryService;
    @Resource
    MiddleProjectService middleProjectService;
    @Resource
    private RedissonClient redissonClient;

    @Override
    public Object debugInterface(JSONObject request) {

        RequestSampler requestSampler = new RequestSampler();
        ResponseSampler response;
        Map bodyContent = new HashMap();
        String mehtod = request.getString("method");
        String url = request.getString("url");
        String header = request.getString("header");
        String params = request.getString("params");
        String body = request.getString("body");
        if (!body.isEmpty()) {
            bodyContent = JSON.parseObject(body, Map.class);
        }
        String cookie = request.getString("cookie");

        requestSampler.setMethod(mehtod);
        requestSampler.setUrl(url);
        requestSampler.setParams(params);
        requestSampler.setBody(bodyContent);
        requestSampler.setHeader(header);

        //处理headers
        Map<String, String> headerMap = new HashMap<>();
        if (!header.isEmpty()) {
            String[] headers = header.split(";");
            for (int j = 0; j < headers.length; j++) {
                String[] mapHeader = headers[j].split(":");
                headerMap.put(mapHeader[0], mapHeader[1]);
            }
            requestSampler.setHeaders(headerMap);
        }
        //处理cookies
        Map<String, String> cookieMap = new HashMap<>();
        if (!cookie.isEmpty()) {
            String[] cookies = cookie.split(";");
            for (int i = 0; i < cookies.length; i++) {
                String[] mapCookie = cookies[i].split("=");
                cookieMap.put(mapCookie[0], mapCookie[1]);
            }
            requestSampler.setCookies(cookieMap);
        }
        //发送请求
        response = RequestFactory.build(requestSampler);
        return JSONObject.toJSONString(response);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object runSingle(int id) {

        MiddleCase middleCase = middleCaseService.findMiddleCaseById(id);
        MiddleInterface middleInterface = middleInterfaceService.findMiddleInterfaceById(middleCase.getInterface_id());
        MiddleModule middleModule = middleModuleService.findMiddleModuleById(middleInterface.getModule_id());
        MiddleProject middleProject = middleProjectService.findMiddleProjectById(middleModule.getProject_id());

        String key = "middle_project_lock_" + middleCase.getEnv_id() + "_" + middleProject.getId();
        RLock lock = redissonClient.getLock(key);
        ResponseSampler response = new ResponseSampler();
        try {
            lock.lock();

            MiddleRunParam middleRunParam = new MiddleRunParam();
            middleRunParam.setEnvId(middleCase.getEnv_id());
            middleRunParam.setCaseType(-1);
            middleRunParam.setId(id);
            middleRunParam.setType("single");
            //登录
            ResponseSampler loginInfo = (ResponseSampler) beforRun(middleRunParam, middleProject);
            if (false == loginInfo.getLoginResult()) {
                return loginInfo;
            }
            RequestSampler requestInfo = getRequestInfo(middleCase, middleInterface, loginInfo, middleProject);
            RunMiddleCaseCallAble runMiddleCaseCallAble = new RunMiddleCaseCallAble(requestInfo);
            String startTime = getTime();
            Future<ResponseSampler> responseResult = submitTask(runMiddleCaseCallAble);

            List<Future<ResponseSampler>> responseResultList = new ArrayList<>();
            responseResultList.add(responseResult);
            String endTime = getTime();
            //添加测试历史记录
            List<ResponseSampler> responseSamplers = (List<ResponseSampler>) insertHistory(responseResultList, middleCase.getEnv_id(), middleProject, startTime, endTime, middleRunParam);
            response = responseSamplers.get(0);
            boolean result = response.getResponse().contains(middleCase.getCheck_data());
            if (result == true) {
                response.setResult("PASS");
            } else {
                response.setResult("FAIL");
            }

        } catch (Exception e) {
            logger.error("运行单条用例异常=={}", e.getMessage());
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

        return response;
    }

    @Override
    public Object runInterface(int env_id, int interface_id, int caseType) {

        MiddleInterface middleInterface = middleInterfaceService.findMiddleInterfaceById(interface_id);
        MiddleModule middleModule = middleModuleService.findMiddleModuleById(middleInterface.getModule_id());
        int project_id = middleModule.getProject_id();
        MiddleProject middleProject = middleProjectService.findMiddleProjectById(project_id);

        String key = "middle_project_lock_" + env_id + "_" + project_id;
        RLock lock = redissonClient.getLock(key);
        MiddleTestHistory middleTestHistory = new MiddleTestHistory();

        try {
            lock.lock();
            MiddleRunParam middleRunParam = new MiddleRunParam();
            middleRunParam.setEnvId(env_id);
            middleRunParam.setCaseType(caseType);
            middleRunParam.setId(interface_id);
            middleRunParam.setType("interface");
            //登录
            ResponseSampler loginInfo = (ResponseSampler) beforRun(middleRunParam, middleProject);
            if (false == loginInfo.getLoginResult()) {
                return loginInfo;
            }
            //获取用例列表
            List<MiddleCase> middleCaseList = middleCaseService.findMiddleCaseByEnvAndInterface(env_id, middleInterface.getId());
            List<RunMiddleCaseCallAble> runMiddleCaseCallAbleList = new ArrayList<>();
            for (MiddleCase middleCase : middleCaseList) {

                if (caseType != -1) {
                    if (caseType != middleCase.getCaseType()) {
                        continue;
                    }
                }
                RequestSampler requestInfo = new RequestSampler();
                requestInfo = getRequestInfo(middleCase, middleInterface, loginInfo, middleProject);
                RunMiddleCaseCallAble runMiddleCaseCallAble = new RunMiddleCaseCallAble(requestInfo);
                runMiddleCaseCallAbleList.add(runMiddleCaseCallAble);
            }

            String startTime = getTime();
            List<Future<ResponseSampler>> responseResultList = submitTasks(runMiddleCaseCallAbleList);
            String endTime = getTime();
            //添加测试历史记录
            middleTestHistory = (MiddleTestHistory) insertHistory(responseResultList, env_id, middleProject, startTime, endTime, middleRunParam);


        } catch (Exception e) {
            logger.error("按接口运行用例异常=={}", e.getMessage());
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

        return middleTestHistory;

    }

    @Override
    public Object runModule(int env_id, int module_id,int caseType) {

        MiddleModule middleModule = middleModuleService.findMiddleModuleById(module_id);
        int project_id = middleModule.getProject_id();
        MiddleProject middleProject = middleProjectService.findMiddleProjectById(project_id);

        String key = "middle_project_lock_" + env_id + "_" + project_id;
        RLock lock = redissonClient.getLock(key);
        MiddleTestHistory middleTestHistory = new MiddleTestHistory();

        try {
            lock.lock();

            MiddleRunParam middleRunParam = new MiddleRunParam();
            middleRunParam.setEnvId(env_id);
            middleRunParam.setCaseType(caseType);
            middleRunParam.setId(module_id);
            middleRunParam.setType("module");
            //登录
            ResponseSampler loginInfo = (ResponseSampler) beforRun(middleRunParam, middleProject);
            if (false == loginInfo.getLoginResult()) {
                return loginInfo;
            }

            //获取用例列表
            List<RunMiddleCaseCallAble> runMiddleCaseCallAbleList = new ArrayList<>();
            List<MiddleInterface> middleInterfaceList = middleInterfaceService.findMiddleInterfaceByModuleId(module_id);
            for (MiddleInterface middleInterface : middleInterfaceList) {

                List<MiddleCase> middleCaseList = middleCaseService.findMiddleCaseByEnvAndInterface(env_id, middleInterface.getId());
                for (MiddleCase middleCase : middleCaseList) {

                    if (caseType != -1) {
                        if (caseType != middleCase.getCaseType()) {
                            continue;
                        }
                    }
                    RequestSampler requestInfo = new RequestSampler();
                    requestInfo = getRequestInfo(middleCase, middleInterface, loginInfo, middleProject);
                    RunMiddleCaseCallAble runMiddleCaseCallAble = new RunMiddleCaseCallAble(requestInfo);
                    runMiddleCaseCallAbleList.add(runMiddleCaseCallAble);
                }
            }
            String startTime = getTime();
            List<Future<ResponseSampler>> responseResultList = submitTasks(runMiddleCaseCallAbleList);
            String endTime = getTime();
            //添加测试历史记录
            middleTestHistory = (MiddleTestHistory) insertHistory(responseResultList, env_id, middleProject, startTime, endTime, middleRunParam);

        } catch (Exception e) {
            logger.error("按模块运行用例异常=={}", e.getMessage());
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

        return middleTestHistory;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object runProject(int env_id, int project_id, int caseType) {

        String key = "middle_project_lock_" + env_id + "_" + project_id;
        RLock lock = redissonClient.getLock(key);
        MiddleTestHistory middleTestHistory = new MiddleTestHistory();
        try {
            lock.lock();
            MiddleProject middleProject = middleProjectService.findMiddleProjectById(project_id);

            MiddleRunParam middleRunParam = new MiddleRunParam();
            middleRunParam.setEnvId(env_id);
            middleRunParam.setCaseType(caseType);
            middleRunParam.setId(project_id);
            middleRunParam.setType("project");
            //登录
            ResponseSampler loginInfo = (ResponseSampler) beforRun(middleRunParam, middleProject);
            if (false == loginInfo.getLoginResult()) {

                middleTestHistory.setResult("登录失败");
                middleTestHistory.setProjectName(middleProject.getName());
                middleTestHistory.setEndTime(getTime());
                middleTestHistory.setContent(loginInfo.getResponse());
                return middleTestHistory;
            }

            //获取用例列表
            List<RunMiddleCaseCallAble> runMiddleCaseCallAbleList = new ArrayList<>();
            List<MiddleModule> middleModuleList = middleModuleService.findMiddleModuleByProjectId(project_id);

            for (MiddleModule module : middleModuleList) {

                List<MiddleInterface> middleInterfaceList = middleInterfaceService.findMiddleInterfaceByModuleId(module.getId());
                for (MiddleInterface middleInterface : middleInterfaceList) {

                    List<MiddleCase> middleCaseList = middleCaseService.findMiddleCaseByEnvAndInterface(env_id, middleInterface.getId());
                    for (MiddleCase middleCase : middleCaseList) {

                        if (caseType != -1) {
                            if (caseType != middleCase.getCaseType()) {
                                continue;
                            }
                        }
                        RequestSampler requestInfo = new RequestSampler();
                        requestInfo = getRequestInfo(middleCase, middleInterface, loginInfo, middleProject);
                        RunMiddleCaseCallAble runMiddleCaseCallAble = new RunMiddleCaseCallAble(requestInfo);
                        runMiddleCaseCallAbleList.add(runMiddleCaseCallAble);
                    }
                }
            }
            String startTime = getTime();
            List<Future<ResponseSampler>> responseResultList = submitTasks(runMiddleCaseCallAbleList);
            String endTime = getTime();
            //添加测试历史记录
            middleTestHistory = (MiddleTestHistory) insertHistory(responseResultList, env_id, middleProject, startTime, endTime, middleRunParam);


        } catch (Exception e) {
            logger.error("按项目运行用例异常=={}", e.getMessage());
            e.printStackTrace();

        } finally {
            lock.unlock();
        }


        return middleTestHistory;
    }

    @Override
    public CompletableFuture<MiddleTestHistory> runMonitorProject(int env_id, int project_id, int caseType) {
        MiddleTestHistory middleTestHistory = new MiddleTestHistory();
        Object history = "";
        try{
             history = runProject(env_id, project_id, caseType);
             if (history == null){
                 middleTestHistory = null;
             }else {
                 middleTestHistory = (MiddleTestHistory)history;
             }

        }catch (Exception e){
            logger.error("运行监控项目失败==" + getMessage(e) );
            logger.error("报错信息==" + JSONObject.toJSONString(history));
        }

        return CompletableFuture.completedFuture(middleTestHistory);
    }



    public void loginFailSendEmail(RequestSampler requestSampler, ResponseSampler responseSampler) {
        //判断登录失败不进行运行用例，直接发送邮件
        String errorLogPath = logPath + "/server-error." + GetTime.getYmd() + ".log";
        MailInfo mailInfo = new MailInfo();
        List<String> toList = new ArrayList<>();
        String[] sendToArray = Receiver.QARECEIVER.split(";");
        if (sendToArray.length > 0) {
            for (int i = 0; i < sendToArray.length; i++) {
                toList.add(sendToArray[i]);
            }
        } else {
            logger.error("收件人错误,请检查==" + Receiver.QARECEIVER);
        }

        //添加附件
        EmailAttachment att = new EmailAttachment();
        att.setPath(errorLogPath);
        att.setName("errorlog.txt");
        List<EmailAttachment> atts = new ArrayList<EmailAttachment>();
        atts.add(att);
        mailInfo.setAttachments(atts);
        //收件人
        mailInfo.setToAddress(toList);
        mailInfo.setSubject(requestSampler.getUrl_header() + "_登录失败");
        mailInfo.setContent("登录失败，请检查登录功能是否正常 <br>" +
                            "域名:" + requestSampler.getUrl_header() + "<br>"  +
                            "用户名:" + requestSampler.getUname() +  "<br>"  +
                            "requestId:" + responseSampler.getRequestId() + "<br>"  +
                            "响应内容:" + responseSampler.getResponse());

        MailUtil.sendEmail(mailInfo);


    }


    /**
     * 设置请求参数
     *
     * @param
     * @param middleCase
     * @param middleInterface
     * @param loginInfo
     * @param middleProject
     */
    public synchronized RequestSampler getRequestInfo(MiddleCase middleCase, MiddleInterface middleInterface, ResponseSampler loginInfo, MiddleProject middleProject) {

        RequestSampler createRequest = new RequestSampler();
        if ("Post-Json".equals(middleInterface.getRequest_method())) {
            Map jsonObject = JSONObject.parseObject(middleCase.getRequest_data());
            createRequest.setBody(jsonObject);
        } else {
            createRequest.setParams(middleCase.getRequest_data());
        }
        createRequest.setUname(loginInfo.getuName());
        createRequest.setToken(loginInfo.getToken());
        createRequest.setCookies(loginInfo.getCookies());
        createRequest.setUrl_header(loginInfo.getUrlHeader());
        createRequest.setUrl(middleInterface.getUrl());
        createRequest.setMethod(middleInterface.getRequest_method());
        createRequest.setEnv_id(middleCase.getEnv_id());
        createRequest.setProject_id(middleProject.getId());
        createRequest.setProjectName(middleProject.getName());
        createRequest.setRunType(middleProject.getRunType());
        createRequest.setRequestPre(middleProject.getRequestPre());
        createRequest.setCaseName(middleCase.getName());
        createRequest.setCheckData(middleCase.getCheck_data());
        createRequest.setInterfaceId(middleCase.getInterface_id());
        return createRequest;
    }

    /**
     * 添加请求历史记录
     */
    public synchronized Object insertHistory(List<Future<ResponseSampler>> responseList, int envId, MiddleProject middleProject, String statrTime, String endTime, MiddleRunParam param) {

        List<MiddleRequestHistory> requestHistoryList = new ArrayList<>();
        List<ResponseSampler> responseSamplerList = new ArrayList<>();
        MiddleTestHistory middleTestHistory = new MiddleTestHistory();
        middleTestHistory.setProjectId(middleProject.getId());
        middleTestHistory.setProjectName(middleProject.getName());
        middleTestHistory.setEnv(envId);
        middleTestHistory.setStartTime(statrTime);
        middleTestHistory.setEndTime(endTime);
        middleTestHistory.setTotalCase(responseList.size());
        int passCase = 0;
        int failCase = 0;
        for (Future<ResponseSampler> response : responseList) {
            ResponseSampler responseSampler = new ResponseSampler();
            boolean result = false;
            try {
                responseSampler = response.get(TIME_OUT, TimeUnit.SECONDS);
                if (responseSampler.getResponse() != null && responseSampler.getRequestId() != null) {
                    result = responseSampler.getResponse().contains(responseSampler.getCheckData());
                    if (result == true) {
                        passCase++;
                        responseSampler.setResult("PASS");
                    } else {
                        failCase++;
                        responseSampler.setResult("FAIL");
                    }
                    responseSamplerList.add(responseSampler);
                } else {
                    logger.error("响应结果异常=={}", JSONObject.toJSONString(responseSampler));
                    continue;
                }

                MiddleRequestHistory middleRequestHistory = new MiddleRequestHistory();
                middleRequestHistory.setInterfaceId(responseSampler.getInterfaceId());
                middleRequestHistory.setRequestId(responseSampler.getRequestId());
                middleRequestHistory.setCaseName(responseSampler.getCaseName());
                middleRequestHistory.setUrl(responseSampler.getUrl());
                middleRequestHistory.setRequestData(responseSampler.getRequestData());
                middleRequestHistory.setEnv(envId + "");
                middleRequestHistory.setStartTime(responseSampler.getStartTime());
                middleRequestHistory.setEndTime(responseSampler.getEndTime());
                middleRequestHistory.setElapsed_time(responseSampler.getElapsedTime());
                middleRequestHistory.setResponseContent(responseSampler.getResponse());
                middleRequestHistory.setResult(result == true ? "PASS" : "FAIL");
                middleRequestHistory.setResponseCode(responseSampler.getResponseCode());
                middleRequestHistory.setExpectResult(responseSampler.getCheckData());

                requestHistoryList.add(middleRequestHistory);
            } catch (Exception e) {
                logger.error("获取响应结果报错=={}", e.getMessage());
                e.printStackTrace();
                continue;
            }

        }

        middleTestHistory.setResult(failCase == 0 ? "PASS" : "FAIL");
        middleTestHistory.setPassCase(passCase);
        middleTestHistory.setFailCase(failCase);
        logger.info("服务名称==" + middleTestHistory.getProjectName());
        logger.info("用例总数==" + middleTestHistory.getTotalCase());
        logger.info("用例PASS数==" + middleTestHistory.getPassCase());
        logger.info("用例FAIL数==" + middleTestHistory.getFailCase());

        try {
            if (requestHistoryList.size() > 0) {
                middleTestHistoryService.insertMiddleTestHistory(middleTestHistory);
                for (MiddleRequestHistory requestHistory : requestHistoryList) {
                    requestHistory.setHistoryId(middleTestHistory.getId());
                }
                middleRequestHisoryService.insertMiddleRequestHistorys(requestHistoryList);
            }

        } catch (Exception e) {
            logger.error("添加历史结果失败=={}", e.getMessage());
            logger.error("添加历史结果失败=={}", JSONObject.toJSONString(requestHistoryList));
            e.printStackTrace();
        }
        if ("single".equals(param.getType())) {
            return responseSamplerList;
        } else {
            return middleTestHistory;
        }


    }


    /**
     * 运行之前先检查账号信息和是否登录成功
     *
     * @param param
     * @return
     */
    public synchronized Object beforRun(MiddleRunParam param, MiddleProject project) {

        ResponseSampler loginResponse = new ResponseSampler();
        RequestSampler accountInfo = middleTestHistoryService.findLoginInfoByProjectAndEnv(project.getId(), param.getEnvId());
        if (accountInfo.getPwd() != null) {
            accountInfo.setProject_id(project.getId());
            accountInfo.setLoginParam(project.getLoginParam());
            accountInfo.setSecretUrl(project.getSecretUrl());
            accountInfo.setLoginType(project.getLoginType());
            accountInfo.setRunType(project.getRunType());
            accountInfo.setLoginUrl(project.getLoginUrl());
            accountInfo.setProjectName(project.getName());
            accountInfo.setRequestPre(project.getRequestPre());
        } else {
            logger.error("请添加账号信息==服务{},环境{}", project.getName(), param.getEnvId());
            loginResponse.setResponse("请添加账号信息!!!");
            if ("single".equals(param.getType())) {
                return loginResponse;
            } else {
                return new MiddleTestHistory();
            }
        }
        //登录
        loginResponse = prepare(accountInfo);
        if (false == loginResponse.getLoginResult()) {
            logger.error("登录失败，请查看邮件!!!");
            loginFailSendEmail(accountInfo, loginResponse);
            loginResponse.setResponse("登录失败，请查看邮件!!!");

        }
        loginResponse.setUrlHeader(accountInfo.getUrl_header());
        return loginResponse;


    }


}
