package com.okay.testcenter.controller.ui;

import com.alibaba.fastjson.JSONObject;
import com.okay.testcenter.domain.RetResponse;
import com.okay.testcenter.domain.middle.RequestSampler;
import com.okay.testcenter.tools.singlelogin.SingleLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

import static com.okay.testcenter.tools.TeacherPublish.publishResource;


@Api(description = "老师发布Pad数据接口")
@Controller
@RequestMapping(value = "/uiPad")
public class TeacherPublishController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    String username = "";
    String password = "";
    String url_header = "";
    RequestSampler cookies = new RequestSampler();

    @ApiOperation(value = "老师发布Pad数据", notes = "老师发布Pad数据")
    @PostMapping(value = "/publish")
    @ResponseBody
    public Object publish(@Validated @RequestBody JSONObject request) {


        this.username = request.getString("username");
        this.password = request.getString("password");
        this.url_header = request.getString("url_header");


        Map<String, String> LoginCookies = new SingleLogin(url_header, username, password).login().getCookies();
        cookies.setCookies(LoginCookies);
        if (LoginCookies != null) {
            logger.info("教师空间登录完成==" + LoginCookies);
        } else {
            logger.error("教师空间登录失败==" + LoginCookies);
        }

        //发布资源
        boolean result = publishResource(request, cookies);
        if (result == true) {

            return RetResponse.makeOKRsp();
        } else {
            return RetResponse.makeErrRsp("发布资料失败");
        }


    }




}
