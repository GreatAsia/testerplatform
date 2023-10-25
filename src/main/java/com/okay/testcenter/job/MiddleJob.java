package com.okay.testcenter.job;

import com.alibaba.fastjson.JSONObject;
import com.okay.testcenter.common.constant.Receiver;
import com.okay.testcenter.domain.AlarmHistory;
import com.okay.testcenter.domain.middle.MiddleProject;
import com.okay.testcenter.domain.report.MiddleTestHistory;
import com.okay.testcenter.service.SendEmailService;
import com.okay.testcenter.service.alarm.AlarmHistoryService;
import com.okay.testcenter.service.middle.EnvService;
import com.okay.testcenter.service.middle.MiddleProjectService;
import com.okay.testcenter.service.middle.MiddleRunService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.okay.testcenter.tools.ExceptionUtil.getMessage;

/**
 * @author zhou
 * @date 2020/7/14
 */
public class MiddleJob extends BaseJob {
    private static Logger logger = LoggerFactory.getLogger(MiddleJob.class);
    @Autowired
    MiddleRunService middleRunService;
    @Autowired
    SendEmailService sendEmailService;
    @Autowired
    MiddleProjectService middleProjectService;
    @Autowired
    EnvService envService;
    @Autowired
    AlarmHistoryService alarmHistoryService;
    @Value("${server.root.url}")
    private String platformHost;


    MiddleTestHistory history;
    private String title;
    private String item = "middle";
    private int taskId = 0;

    private static final String ENVNAME = "online";

    @Override
    public void runMonitor(JSONObject jobParam) {

        int envId = Integer.valueOf(jobParam.getString("envId"));
        int projectId = Integer.valueOf(jobParam.getString("projectId"));


        String receiveString = "";
        String mailReceivers = "";
        switch (projectId) {
            case 1:
                mailReceivers = Receiver.RECEIVER + "ailearn@okay.cn";
                break;
            case 2:
                mailReceivers = Receiver.RECEIVER + "ailearn@okay.cn;stu@okay.cn;pmm@okay.cn";
                break;
            case 3:
                mailReceivers = Receiver.RECEIVER + "ailearn@okay.cn;profession-dep@okay.cn";
                break;
            case 4:
                mailReceivers = Receiver.RECEIVER + "php-cms@okay.cn";
                break;
            case 5:
                mailReceivers = Receiver.RECEIVER + "php-cms@okay.cn";
                break;
            case 6:
                mailReceivers = Receiver.RECEIVER + "okayapp@okay.cn;zhaoliang@okay.cn";
                break;
            case 7:
                mailReceivers = Receiver.RECEIVER + "php-cms@okay.cn";
                break;
            case 8:
                mailReceivers = Receiver.RECEIVER + "shangcheng@okay.cn;zhaoliang@okay.cn";
                break;
            case 9:
                mailReceivers = Receiver.RECEIVER;
                break;
            case 10:
                mailReceivers = Receiver.RECEIVER + "ailearn@okay.cn";
                break;
            case 11:
                mailReceivers = Receiver.RECEIVER + "okayapp@okay.cn;zhaoliang@okay.cn";
                break;
            case 12:
                mailReceivers = Receiver.RECEIVER + "okayapp@okay.cn;zhaoliang@okay.cn";
                break;
            case 13:
                mailReceivers = Receiver.RECEIVER;
                break;
            case 14:
                mailReceivers = Receiver.RECEIVER + "ailearn@okay.cn;stu@okay.cn;pmm@okay.cn";
                break;
            case 18:
                mailReceivers = Receiver.RECEIVER + "sunzhangchao@okay.cn;xujian@okay.cn";
                break;
            case 20:
                mailReceivers = Receiver.AILEANRECEIVER;
                break;
            case 21:
                mailReceivers = Receiver.AILEANRECEIVER;
                break;
            default:
                mailReceivers = Receiver.RECEIVER;
                logger.info("default mailReceivers ");
                break;
        }

        if (envId == 3) {
            receiveString = mailReceivers;
            taskId = alarmHistoryService.getLastTaskId() + 1;
        } else {
            receiveString = Receiver.QARECEIVER;

        }


        if (projectId == 0) {
            List<MiddleProject> middleProjects = middleProjectService.findMiddleProjectList();
            for (MiddleProject m : middleProjects) {
                runCase(envId, m.getId(), receiveString);
            }
        } else {
            runCase(envId, projectId, receiveString);
        }



    }

    /**
     * 运行用例，发送邮件
     *
     * @param envId
     * @param projectId
     * @param receiveString
     */
    private void runCase(int envId, int projectId, String receiveString) {

        try {
            CompletableFuture<MiddleTestHistory> compHistory = middleRunService.runMonitorProject(envId, projectId, 0);
            CompletableFuture.allOf(compHistory).join();
            history = compHistory.get();

        } catch (Exception e) {
            logger.error("执行监控发生错误:" + getMessage(e));
            logger.error("history:" + JSONObject.toJSONString(history));
            e.printStackTrace();

        } finally {

            String env = envService.findEnvById(envId).getName();
            title = env + "环境自动化测试报告";

            if (history != null) {
                if ("FAIL".equals(history.getResult())) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("sendTo", receiveString);
                    jsonObject.put("title", title);
                    jsonObject.put("item", item);
                    jsonObject.put("historyId", history.getId());

                    sendEmailService.sendEmail(jsonObject);

                    if (ENVNAME.equals(env)) {
                        AlarmHistory alarmHistory = new AlarmHistory();
                        alarmHistory.setTaskId(taskId);
                        alarmHistory.setServiceName(history.getProjectName());
                        alarmHistory.setErrorType("断言失败");
                        alarmHistory.setTime(history.getEndTime());
                        alarmHistory.setContent(platformHost + "/middle/report/detail/" + history.getId());
                        alarmHistory.setAlarm("false");
                        alarmHistoryService.insert(alarmHistory);
                    }

                }
                if ("登录失败".equals(history.getResult())) {

                    if (ENVNAME.equals(env)) {
                        AlarmHistory alarmHistory = new AlarmHistory();
                        alarmHistory.setTaskId(taskId);
                        alarmHistory.setServiceName(history.getProjectName());
                        alarmHistory.setErrorType("登录失败");
                        alarmHistory.setTime(history.getEndTime());
                        alarmHistory.setContent(history.getContent());
                        alarmHistory.setAlarm("false");
                        alarmHistoryService.insert(alarmHistory);
                    }

                }
            }

        }
    }


}
