package com.okay.testcenter;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.okay.testcenter.domain.middle.MiddleCase;
import com.okay.testcenter.domain.middle.RequestSampler;
import com.okay.testcenter.domain.middle.ResponseSampler;
import com.okay.testcenter.service.middle.MiddleCaseService;
import com.okay.testcenter.tools.RequestId;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.okay.testcenter.tools.GetTime.getTime;
import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MiddleCaseTest {


    private  final Logger logger = LoggerFactory.getLogger(this.getClass());
    ResponseSampler responseSampler = new ResponseSampler();

    @Resource
    MiddleCaseService middleCaseService;


    @Test
    public void testInsert(){

        MiddleCase middleCase = new MiddleCase();
        middleCase.setEnv_id(1);
        middleCase.setInterface_id(1);
        middleCase.setName("注册");
        middleCase.setRequest_data("{\"username\": \"61951377365\",\"password\": \"Okay@123\",\"validate_code\": \"\"}");
        middleCase.setCheck_data("code:0");
        middleCase.setCaseType(0);
        middleCase.setUserName("zhangyazhou");
        middleCaseService.insertMiddleCase(middleCase);

    }

    @Test
    public void testUpdate(){

        MiddleCase middleCase = new MiddleCase();
        middleCase.setId(7137);
        middleCase.setEnv_id(1);
        middleCase.setInterface_id(1);
        middleCase.setName("登录");
        middleCase.setRequest_data("{\"username\": \"61951377365\",\"password\": \"Okay@123\",\"validate_code\": \"\"}");
        middleCase.setCheck_data("code:1");
        middleCase.setCaseType(0);
        middleCase.setUserName("zhangsan");
        middleCaseService.updateMiddleCase(middleCase);

    }

    @Test
    public void testFindMiddleCaseById(){

       MiddleCase middleCase =  middleCaseService.findMiddleCaseById(1);
       logger.info("[middleCase]" + JSONObject.toJSONString(middleCase));

    }

    @Test
    public void testFindMiddleCaseByName(){

        List<MiddleCase> middleCase = middleCaseService.findMiddleCaseByName("获取习题列表");
        logger.info("[middleCase]" + JSONObject.toJSONString(middleCase));

    }



    @Test
    public void testFindMiddleCaseByPath() {

        List<MiddleCase> middleCase = middleCaseService.findMiddleCaseByPath("/home/exercise_list");
        logger.info("[middleCase]" + JSONObject.toJSONString(middleCase));

    }

    @Test
    public void testFindMiddleCaseList(){

        PageInfo middleCase =  middleCaseService.findMiddleCaseList(1,10);
        logger.info("[middleCase]" + JSONObject.toJSONString(middleCase));

    }

    @Test
    public void testDeleteMiddleCase(){

        middleCaseService.deleteMiddleCase(2);


    }

    @Test
    public void testFindMiddleCaseListByEnvIdAndInterfaceId() {

        PageInfo middleCase = middleCaseService.findMiddleCaseByEnvAndInterface(1, 1,1,10);
        logger.info("[middleCase]" + JSONObject.toJSONString(middleCase));

    }


    //教师空间登录
    @Test
    public void testLoginTeacherSpace(){
        Response response = null;
        RequestSampler requestSampler = new RequestSampler();
        //登录成功后获取cookies
        requestSampler.setUrl("https://jiaoshi.qa-hotfix.xk12.cn");
        requestSampler.setContentType("application/x-www-form-urlencoded");
        requestSampler.setParams("username=61951377365&password=Okay@123&validate_code=");
        requestSampler.setProductId("01");
        requestSampler.setType("web");
        requestSampler.setRequestId(getRequestId("01"));

            try {
                 response = post(requestSampler);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("登录失败");
            }
            Map<String,String> cookie =  response.getCookies();
        responseSampler.setCookies(cookie);
        logger.info("[cookis]==" + responseSampler.getCookies());
    }

    @Test
    public void testTeacherGet() {

        Response response = null;
        RequestSampler requestSampler = new RequestSampler();
        requestSampler.setUrl("https://jiaoshi.qa-dev.xk12.cn/home/exercise_list");
        requestSampler.setProductId("01");
        requestSampler.setType("web");
        requestSampler.setRequestId(getRequestId("01"));
        Map params = new HashMap();
        params.put("_", "1559630935209");
        params.put("custom_directory_id", "4586864");
        params.put("page", "1");
        params.put("origin", "0");
        params.put("page_size", "10");
        requestSampler.setParamsList(params);

        Map cookies = new HashMap();
        cookies.put("teacher_id", "f352c0afd3fb47fd85618fb481f84e91");
        cookies.put("org_id", "132");
        cookies.put("user_action_cookie", "user_action_9b899ee3-4141-4f94-9aa1-f441703ad8f0_61951377365");
        requestSampler.setCookies(cookies);
        response = get(requestSampler);


    }


    @Test
    public void testTeacherSsoGet() {

        RequestSampler requestSampler = new RequestSampler();
        requestSampler.setUrl("https://sso-hotfix.xk12.cn/login?service=https://jiaoshi-hotfix.xk12.cn/");
        requestSampler.setProductId("01");
        requestSampler.setType("web");
        requestSampler.setRequestId(getRequestId("01"));

        Response response = get(requestSampler);
        logger.info("[response==]" + response.asString());


    }


    //学生登录
    @Test
    public void studentLogin() {
        //登录成功后获取token
        Response response = null;
        RequestSampler requestSampler = new RequestSampler();
        requestSampler.setUrl("https://studentpad.qa-dev.xk12.cn/api/pad/user/login");
        requestSampler.setContentType("application/json");
        requestSampler.setProductId("02");
        requestSampler.setType("pad");
        requestSampler.setRequestId(getRequestId("02"));


        Map<Object, Object> params = new HashMap<>();
        params.put("iccid", "isFake");
        params.put("mac", "d0:a2:31:1e:2f:eb");
        params.put("pwd", "Ap+AxDsar9lBoBb5jZmQBRgDvJUKqkcpyD815EcWmYGzkJuiTv4hUgy5+15QxZX55DUCscQoCXPRE2v+9FuYxPTU4TfYYhIfJcaI5akRB/7xRGES9vJHgSH1Xo626vRzIK23O6KAWaa3Po+lLSD/HHIacn4M/9108k/gFLnbiGQ=");
        params.put("screen_pattern", "1");
        params.put("os", "okay3.1.0");
        params.put("vc", "117");
        params.put("contype", "3");
        params.put("imei", "isFake");
        params.put("sh", "800");
        params.put("sw", "1280");
        params.put("ua", "OKAY_EBOOK_3.1.0_OKUI_4.2.14_20180904_T");
        params.put("vs", "2.8.5.9_S4-debug");
        params.put("udid", "385C4E33B430B6ECF58BDDAF0692C78F");
        params.put("serial", "W170600662");
        params.put("channel", "pad");
        params.put("uname", "81951377539");
        logger.info("[params]" + JSONObject.toJSONString(params));
        requestSampler.setBody(params);

        response = post(requestSampler);
        logger.info("[token]==" + response.jsonPath().get("data.token"));


    }


    /**
     * POST请求方法
     *
     *
     */
    public Response post(RequestSampler requestSampler) {
        Response response = null;
        logger.info("[url]==" + requestSampler.getUrl());
        logger.info("[requestInfo]==" + JSONObject.toJSONString(requestSampler));
        Map<String, String> headers = new HashMap<>();
        headers.put("requestid", requestSampler.getRequestId());
        requestSampler.setHeaders(headers);
        //app添加token
        if(requestSampler.getType().equals("app")){
            headers.put("token", requestSampler.getToken());
        }
        try {
            response = given()
                    .headers(requestSampler.getHeaders())
                    .contentType(requestSampler.getContentType())
                    .body(requestSampler.getBody())
                    .when()
                    .post(requestSampler.getUrl());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("[requestFail]==" + e.getLocalizedMessage());
        }

        logger.info("[responseInfo]==" + response.asString());
        return response;
    }



    /**
     * GET请求方法
     *
     *
     */
    public Response get(RequestSampler requestSampler) {
        Response response = null;
        logger.info("[url]==" + requestSampler.getUrl());
        logger.info("[requestInfo]==" + JSONObject.toJSONString(requestSampler));
        Map<String, String> headers = new HashMap<>();
        headers.put("requestid", requestSampler.getRequestId());
        requestSampler.setHeaders(headers);
        //app添加token
        if(requestSampler.getType().equals("app")){
            headers.put("token", requestSampler.getToken());
        }
        try {
            response = given()
                    .cookies(requestSampler.getCookies())
                    .headers(requestSampler.getHeaders())
                    .params(requestSampler.getParamsList())
                    .when()
                    .get(requestSampler.getUrl());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("[requestFail]==" + e.getLocalizedMessage());
        }

        logger.info("[responseInfo]==" + response.asString());
        return response;
    }


    @Test
    public void testTime() {

        getTime();
        getTime(86400000L);

    }


    @Test
    public void testSyncData() {

        //同步数据
//        middleCaseService.syncMiddleCase(3,2);


    }


    //获取requestID
    public String getRequestId(String productId) {

        return new RequestId().generateRequestId(productId);
    }


    @Test
    public void testSingle() {


    }
}
