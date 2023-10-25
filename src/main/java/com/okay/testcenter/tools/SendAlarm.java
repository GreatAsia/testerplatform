package com.okay.testcenter.tools;

import com.okay.testcenter.domain.AlarmHistory;
import com.okay.testcenter.domain.middle.RequestSampler;
import com.okay.testcenter.request.PostJsonRequest;
import com.okay.testcenter.service.alarm.AlarmHistoryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhou
 * @date 2020/12/22
 */
public class SendAlarm {

    private static final String url = "https://oapi.dingtalk.com/robot/send?access_token=d629399f32f8c3701cd2ad8a1287b4834964f6d44c6ff8330771b427018a6bfc";
    @Autowired
    AlarmHistoryService alarmHistoryService;

    public static void main(String[] args) {


    }


    public void send(RequestSampler requestSampler) {

        Map<String, Object> text = new HashMap();
        text.put("content", "报警内容==" + requestSampler.getAlarmList());
        Map<String, Object> at = new HashMap();
        at.put("atMobiles", "[]");
        at.put("isAtAll", true);

        Map<Object, Object> content = new HashMap();
        content.put("msgtype", "text");
        content.put("text", text);
        content.put("at", at);

        requestSampler.setBody(content);
        requestSampler.setUrl(url);
        requestSampler.setContentType("application/json");
        new PostJsonRequest().post(requestSampler);

    }

    public void trigger() {
        RequestSampler requestSampler = new RequestSampler();
        List<Map<String, Object>> list = new ArrayList<>();
        int taskId = alarmHistoryService.getLastTaskId();
        List<AlarmHistory> historyList = alarmHistoryService.findByTaskId(taskId);
        if (historyList.size() >= 2) {

            for (AlarmHistory a : historyList) {
                Map map = new HashMap();
                map.put(a.getServiceName(), a.getErrorType());
                list.add(map);
            }
            requestSampler.setAlarmList(list);
            send(requestSampler);

        } else {

        }


    }


}
