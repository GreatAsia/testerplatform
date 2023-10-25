package com.okay.testcenter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.okay.testcenter.service.middle.MiddleRunService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.okay.testcenter.tools.ReplaceHtml.replaceHtml;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MiddleRun {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    MiddleRunService middleRunService;

    private Map map = new HashMap();

    @Test
    public void testRunSingle() {
        //教师空间
        // middleRunService.runSingle(1858);
        //公立后台
        //middleRunService.runSingle(1825);
        //私立后台
//        middleRunService.runSingle(1826);
        //OMS
        //middleRunService.runSingle(2231);
        //商城
//        middleRunService.runSingle(2131);
        //学生空间
//        middleRunService.runSingle(2306);

        //学生PAD
//        middleRunService.runSingle(1823);
        //OK家长
//        middleRunService.runSingle(2162);
        //教师pad
//        middleRunService.runSingle(1932);
        //Ok学生
//        middleRunService.runSingle(2314);
//        //OK老師
//        middleRunService.runSingle(2321);
        //YST
//        middleRunService.runSingle(2391);
        //ES
        middleRunService.runSingle(2814);


    }


    @Test
    public void testResponse() {

        //String str = "{\"meta\":{\"ecode\":0,\"emsg\":\"登录成功\",\"debug\":null},\"data\":{\"uId\":\"89863\",\"uname\":\"81951055741\",\"token\":\"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiI4OTg2MyIsInN5c3RlbUlkIjoiODE5NTEwNTU3NDEiLCJvcmdJZCI6IjgwIiwidGltZXN0YW1wIjoiMTU5NDk1MDE5MDI2MyJ9.Z6fLd_nPZea3YylhTeNQDjOG448P5Tnm1dj5kJEr01E\",\"tickettoken\":\"63362087fb213092a841257f917ee253\",\"orgtype\":\"2\",\"name\":\"申振华线上监控\",\"sid\":\"80\",\"schoolid\":\"80\",\"schoolname\":\"\",\"classid\":\"\",\"avatarurl\":\"https://hd.okjiaoyu.cn/hd_SmZSdfg1So.jpg\",\"gender\":1,\"quintile\":\"初中\",\"grade\":\"九年级\",\"qg\":\"初中九年级\",\"classarray\":null,\"orglist\":[{\"sid\":\"80\",\"type\":1,\"name\":\"宁夏一中\",\"clist\":[{\"id\":\"12058\",\"name\":\"九年级101班\",\"review\":true,\"classcode\":\"123228\",\"studcount\":9},{\"id\":\"30872\",\"name\":\"九年级孤独1班班\",\"review\":true,\"classcode\":\"139153\",\"studcount\":6},{\"id\":\"40230\",\"name\":\"教学测试班班\",\"review\":true,\"classcode\":\"175421\",\"studcount\":6}]}],\"resetpwd\":true,\"fontsize\":24,\"graduation\":0,\"haveparents\":true}}\n";
        String str = "{\"meta\":{\"ecode\":0,\"emsg\":\"success\"},\"data\":{\"uid\":99951375466,\"parent\":{\"token\":\"5f4bcffb46394cbd8971fc8175bf1a03\",\"normal\":\"0\",\"uid\":\"99951375466\",\"emuid\":\"okay99951375466\",\"emupwd\":\"YjZkOWI1MGEzMTk0OTEzOGI4OTgzZmMxNTI4OTE5NWU=\",\"avatarurl\":\"\",\"phone\":\"17326850148\",\"gender\":\"1\",\"nickname\":\"1314520\",\"name\":\"沈测试\",\"isbind\":\"1\",\"channel\":\"1\",\"regioncode\":\"101111\",\"bindpassword\":\"1\",\"areaname\":\" \"},\"childs\":[{\"childid\":\"81951388412\",\"name\":\"沈老师的学生\",\"state\":\"1\",\"icon\":\"\",\"gender\":\"1\",\"regioncode\":\"0\",\"regionallcode\":\"\",\"orgid\":\"80\",\"classtype\":\"0\",\"classtypename\":\"\",\"stageid\":\"1\",\"stagename\":\"初中\",\"gradeid\":\"7\",\"gradename\":\"七年级\",\"password\":\"\",\"relative\":\"0\",\"areaname\":\"\",\"avatarurl\":\"\"}],\"role\":0}}\n";
        JSONObject data = JSON.parseObject(str).getJSONObject("data");
        String uid = "";
        String token = "";
        Set<String> sets = data.keySet();
        for (String key : sets) {
            if (key.equals("uId")) {
                uid = data.get("uId").toString();
                continue;
            }
            if (key.equals("uid")) {
                uid = data.get("uid").toString();
                continue;
            }
            if (key.equals("token")) {
                token = data.get("token").toString();
                continue;
            }
            if (key.equals("parent")) {
                token = data.getJSONObject("parent").get("token").toString();
                uid = data.getJSONObject("parent").get("uid").toString();
                break;
            }

        }
        System.out.println("token==" + token);
        System.out.println("uid==" + uid);


    }


    /**
     * 一、
     * 递归遍历JSON对象。
     *
     * @param objJson
     */
    public void analysisJson(Object objJson) {

        //如果obj为json数组
        if (objJson instanceof JSONArray) {
            JSONArray objArray = (JSONArray) objJson;
            for (int i = 0; i < objArray.size(); i++) {
                analysisJson(objArray.get(i));
            }
        }
        //如果为json对象
        else if (objJson instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) objJson;
            Set<String> it = jsonObject.keySet();
            for (String key : it) {

                if (key.equals("token")) {
                    map.put("token", jsonObject.get("token"));
                }
                if (key.equals("uid")) {
                    jsonObject.put("uid", jsonObject.get("uid"));
                }
                if (key.equals("uId")) {
                    jsonObject.put("uid", jsonObject.get("uId"));
                }
                Object object = jsonObject.get(key);
                //如果得到的是数组
                if (object instanceof JSONArray) {
                    JSONArray objArray = (JSONArray) object;
                    analysisJson(objArray);
                }
                //如果key中是一个json对象
                else if (object instanceof JSONObject) {
                    analysisJson((JSONObject) object);
                }
                //如果key中是其他
                else {
                    System.out.println("[" + key + "]:" + object.toString() + " ");
                }
            }
        }
    }


    @Test
    public void testHtml() {
        String html = "{\"message\":\"Server error: `POST https:\\/\\/productcenter.okjiaoyu.cn\\/productcenter\\/productInfo\\/findKsuProductListForBs` resulted in a `502 Bad Gateway` response:\\n<!DOCTYPE html>\\n<html>\\n<head>\\n<title>Error<\\/title>\\n<style>\\n    body {\\n        width: 35em;\\n        margin: 0 auto;\\n      (truncated...)\\n\",\"status_code\":500}";
        System.out.println("html==" + replaceHtml(html));
    }
}
