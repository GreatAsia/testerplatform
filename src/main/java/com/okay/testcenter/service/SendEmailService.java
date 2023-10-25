package com.okay.testcenter.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.okay.testcenter.domain.dubbo.DubboCase;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface SendEmailService {

    /**
     * 发送邮件
     * @param request 请求参数
     */
    public Boolean sendEmail(JSONObject request);


    public Boolean sendEmailWebUi(JSONObject requestJson);

}