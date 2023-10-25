package com.okay.testcenter.controller.middle;


import com.okay.testcenter.domain.RetResponse;
import com.okay.testcenter.domain.RetResult;
import com.okay.testcenter.domain.middle.BugInfo;
import com.okay.testcenter.domain.middle.RequestSampler;
import com.okay.testcenter.domain.middle.ResponseSampler;
import com.okay.testcenter.tools.zendo.EditBug;
import com.okay.testcenter.tools.zendo.Login;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.okay.testcenter.tools.GetTime.getYmdForDate;

/**
 * @author zhou
 * @date 2020/11/4
 */
@Api(description = "中间层Bug接口")
@Controller
@RequestMapping(value = "/middle/bug")
public class BugController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${zendo.url}")
    private String zendoHost;




    @ApiOperation(value = "添加中间层Bug", notes = "添加中间层Bug")
    @PostMapping(value = "/add")
    @ResponseBody
    public RetResult<Object> addBug(@Validated @RequestBody BugInfo bugInfo) {

        RequestSampler requestSampler = new RequestSampler();
        requestSampler.setUrl_header(zendoHost);
        String sessionID = new Login().getSession(requestSampler);
        Map<String,String> map = new HashMap<>();
        map.put("sessionID",sessionID);

        requestSampler.setUname(bugInfo.getUname());
        requestSampler.setPwd(bugInfo.getPwd());
        requestSampler.setHeaders(map);
        ResponseSampler loginInfo = new Login().login(requestSampler);
        if("FAIL".equals(loginInfo.getResult())){
            RetResponse.makeErrRsp("登录失败==" + loginInfo.getResponse());
        }

        StringBuffer step = new StringBuffer();
        step.append("[步骤] \t\n");
        step.append(bugInfo.getRequestInfo() + "\t\n");
        step.append("[结果] \t\n");
        step.append(bugInfo.getResponseInfo() + "\t\n");
        step.append("[期望] \t\n");
        step.append(bugInfo.getVerifyInfo() + "\t\n");
        bugInfo.setSteps(step.toString());

        String creatrUrl = "m=bug&f=create&productID="+ bugInfo.getProduct() +"&branch=0&extra=moduleID=" + bugInfo.getModule();
        requestSampler.setUrl(creatrUrl);
        StringBuffer reques = new StringBuffer();
        reques.append("product=" + bugInfo.getProduct());
        reques.append("&title=" + bugInfo.getTitle());
        reques.append("&openedBuild=" + bugInfo.getOpenedBuild());
        reques.append("&branch=" + "");
        reques.append("&module=" + bugInfo.getModule());
        reques.append("&project=" + "");
        reques.append("&assignedTo=" + bugInfo.getAssignedTo());
        reques.append("&deadline=" + getYmdForDate());
        reques.append("&type=" + "codeerror");
        reques.append("&os=" + "all");
        reques.append("&browser=" + "all");
        reques.append("&color=" + "#RGB");
        reques.append("&severity=" + "3");
        reques.append("&pri=" + "3");
        reques.append("&steps=" + bugInfo.getSteps());
        reques.append("&mailto=" + bugInfo.getMailto());
        reques.append("&keywords=" + "接口");

        requestSampler.setParams(reques.toString());


        return RetResponse.makeOKRsp();
    }

    @ApiOperation(value = "关闭中间层Bug", notes = "关闭中间层Bug")
    @GetMapping(value = "/close")
    @ResponseBody
    public RetResult<Object> closeBug(@Validated int bugId) {
        RequestSampler requestSampler = new RequestSampler();
        requestSampler.setUrl_header(zendoHost);
        requestSampler.setUname("qaauto");
        requestSampler.setPwd("123456");
        ResponseSampler loginInfo = new Login().login(requestSampler);
        if("FAIL".equals(loginInfo.getResult())){
            RetResponse.makeErrRsp("登录失败==" + loginInfo.getResponse());
        }
        requestSampler.setCookies(loginInfo.getCookies());
         ResponseSampler responseSampler = new EditBug().closeBug(bugId,requestSampler);
        if("FAIL".equals(responseSampler.getResult())){
            RetResponse.makeErrRsp("关闭失败==" + responseSampler.getResponse());
        }

        return RetResponse.makeOKRsp();
    }



    @ApiOperation(value = "获取产品列表", notes = "获取产品列表")
    @GetMapping(value = "/getProductList")
    @ResponseBody
    public String getProductList() {


        RequestSampler requestSampler = new RequestSampler();
        requestSampler.setUrl_header(zendoHost);
        requestSampler.setUname("qaauto");
        requestSampler.setPwd("123456");
        ResponseSampler responseSampler = new Login().login(requestSampler);
        logger.info("response== {}",responseSampler.getResponse());
        requestSampler.setCookies(responseSampler.getCookies());
        ResponseSampler product = new EditBug().getProductList(requestSampler);
        logger.info("product== {}",product.getResponse());
       return product.getResponse();
    }


    @ApiOperation(value = "获取模块列表", notes = "获取模块列表")
    @GetMapping(value = "/getModuleList")
    @ResponseBody
    public String getBugModuleList(String productId) {

        RequestSampler requestSampler = new RequestSampler();
        requestSampler.setUrl_header(zendoHost);
        requestSampler.setUname("qaauto");
        requestSampler.setPwd("123456");
        requestSampler.setProductId(productId);
        ResponseSampler responseSampler = new Login().login(requestSampler);
        logger.info("response== {}",responseSampler.getResponse());
        requestSampler.setCookies(responseSampler.getCookies());
        ResponseSampler moduleList = new EditBug().getModuleList(requestSampler);
        logger.info("moduleList== {}",moduleList.getResponse());
        return moduleList.getResponse();
    }
}
