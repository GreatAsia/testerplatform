package com.okay.testcenter.job;

import com.alibaba.fastjson.JSONObject;
import com.okay.testcenter.domain.AlarmHistory;
import com.okay.testcenter.domain.AlarmPeople;
import com.okay.testcenter.domain.middle.RequestSampler;
import com.okay.testcenter.domain.report.MiddleRequestHistory;
import com.okay.testcenter.request.PostJsonRequest;
import com.okay.testcenter.service.alarm.AlarmHistoryService;
import com.okay.testcenter.service.alarm.AlarmPeopleService;
import com.okay.testcenter.service.middle.MiddleRequestHisoryService;
import com.okay.testcenter.service.user.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhou
 * @date 2020/12/24
 */
public class TriggerJob extends BaseJob {

    private static Logger logger = LoggerFactory.getLogger(MiddleJob.class);
    @Autowired
    AlarmHistoryService alarmHistoryService;
    @Autowired
    CacheService cacheService;
    @Autowired
    MiddleRequestHisoryService middleRequestHisoryService;
    @Autowired
    AlarmPeopleService alarmPeopleService;
    @Value("${dingding,webhook}")
    private String ddWebhook;

    private List<String> peopleList = new ArrayList<>();
    private List<String> serviceList = new ArrayList<>();


    @Override
    public void runMonitor(JSONObject jobParam) {

        int envId = Integer.valueOf(jobParam.getString("envId"));
        trigger();
        logger.info("trigger 触发完成");

    }


    public void trigger() {

        Object cacheId = cacheService.getCommonCache("taskId");
        if (cacheId == null) {
            cacheId = alarmHistoryService.getLastTaskId();
            cacheService.setCommonCache("taskId", cacheId);
        }
        int taskId = alarmHistoryService.getLastTaskId();
        if (taskId <= (int) cacheId) {
            return;
        }

        RequestSampler requestSampler = new RequestSampler();
        List<Map<String, Object>> list = new ArrayList<>();

        List<AlarmHistory> historyList = alarmHistoryService.findByTaskId(taskId);

        if (historyList.size() >= 2) {


            for (AlarmHistory a : historyList) {
                Map map = new HashMap();
                map.put(a.getServiceName(), a.getErrorType());
                list.add(map);
                //更新钉钉报警标签
                a.setAlarm("true");
                alarmHistoryService.update(a);
            }
            requestSampler.setAlarmHistoryList(historyList);
            requestSampler.setAlarmList(list);

            sendActionCard(requestSampler);


        } else if (historyList.size() == 1) {

            List<AlarmHistory> beforeHistory = alarmHistoryService.findByTaskId(taskId - 1);
            AlarmHistory a = historyList.get(0);
                for (AlarmHistory b : beforeHistory) {

                    if (a.getServiceName().equals(b.getServiceName())) {
                        //判断一下时间
                        SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                        boolean isTime = false;
                        try {
                            Date startTime = df.parse(b.getTime());
                            Date endTime = df.parse(a.getTime());
                            isTime = (endTime.getTime() - startTime.getTime()) <= 330000;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (isTime) {
                            Map map = new HashMap();
                            map.put(a.getServiceName(), a.getErrorType());
                            list.add(map);

                            //更新钉钉报警标签
                            a.setAlarm("true");
                            alarmHistoryService.update(a);

                            requestSampler.setAlarmList(list);
                            requestSampler.setAlarmHistoryList(historyList);
                            sendActionCard(requestSampler);

                        }
                        break;
                    }

                }


        }
        cacheService.setCommonCache("taskId", taskId);

    }

    public void sendText(RequestSampler requestSampler) {


        for (String serviceName : serviceList) {
            AlarmPeople phones = alarmPeopleService.findByProject(serviceName);
            if (phones != null) {
                String[] tel = phones.getPhone().split(",");
                if (tel.length >= 1) {
                    for (String phone : tel) {
                        if (!peopleList.contains(phone)) {
                            peopleList.add(phone);
                        }

                    }
                }
            }
        }

        Map<String, Object> text = new HashMap();
        text.put("content", "请关注线上报警!");
        Map<String, Object> at = new HashMap();
        at.put("atMobiles", peopleList);
        at.put("isAtAll", false);

        Map<Object, Object> content = new HashMap();
        content.put("msgtype", "text");
        content.put("text", text);
        content.put("at", at);

        requestSampler.setBody(content);
        requestSampler.setUrl(ddWebhook);
        requestSampler.setContentType("application/json");
        new PostJsonRequest().post(requestSampler);

    }

    public void sendActionCard(RequestSampler requestSampler) {

        StringBuffer totalContent = new StringBuffer();

        List<Map<String, Object>> btns = new ArrayList<>();
        for (AlarmHistory alarm : requestSampler.getAlarmHistoryList()) {
            Map<String, Object> btn = new HashMap<>();
            if ("登录失败".equals(alarm.getErrorType())) {
                totalContent.append(alarm.getServiceName() + " : " + alarm.getErrorType() + "==" + alarm.getContent()).append("\n");
                serviceList.add(alarm.getServiceName());

            } else {
                if (alarm.getContent() != null) {

                    String[] url = alarm.getContent().split("/");
                    int historyId = Integer.parseInt(url[url.length - 1]);
                    List<MiddleRequestHistory> middleRequestHistoryList = middleRequestHisoryService.findHistoryByHistoryId(historyId);
                    StringBuffer requestIds = new StringBuffer();
                    for (MiddleRequestHistory m : middleRequestHistoryList) {
                        if ("FAIL".equals(m.getResult())) {
                            requestIds.append(m.getRequestId()).append(";");
                        }
                    }

                    btn.put("title", alarm.getServiceName() + " : " + requestIds.toString());
                    serviceList.add(alarm.getServiceName());
                }

            }
            btn.put("actionURL", alarm.getContent());
            btns.add(btn);
        }

        Map<Object, Object> content = new HashMap();
        content.put("title", "线上环境报警");
        content.put("text", "报错服务" + requestSampler.getAlarmList().size() + "个 \n\t" + totalContent.toString());
        content.put("btnOrientation", 0);

        content.put("btns", btns);
        Map<Object, Object> actionCard = new HashMap();
        actionCard.put("actionCard", content);
        actionCard.put("msgtype", "actionCard");

        requestSampler.setBody(actionCard);
        requestSampler.setUrl(ddWebhook);
        requestSampler.setContentType("application/json");
        new PostJsonRequest().post(requestSampler);
        sendText(requestSampler);
    }


    public void sendFeedCard(RequestSampler requestSampler) {

        List<Map<String, Object>> links = new ArrayList<>();
        for (AlarmHistory alarm : requestSampler.getAlarmHistoryList()) {
            Map<String, Object> link = new HashMap<>();
            link.put("title", "报警=" + alarm.getServiceName() + ":" + alarm.getErrorType());
            link.put("messageURL", alarm.getContent());
            link.put("picURL", "");
            links.add(link);
        }
        Map<Object, Object> linkContent = new HashMap<Object, Object>();
        linkContent.put("links", links);

        Map<Object, Object> feedCard = new HashMap();
        feedCard.put("feedCard", linkContent);
        feedCard.put("msgtype", "feedCard");

        requestSampler.setBody(feedCard);
        requestSampler.setUrl(ddWebhook);
        requestSampler.setContentType("application/json");
        new PostJsonRequest().post(requestSampler);

    }


}
