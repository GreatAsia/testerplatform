package com.okay.testcenter.controller.webUi;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.okay.testcenter.domain.Page;
import com.okay.testcenter.domain.RetResult;
import com.okay.testcenter.domain.ui.WebReport;
import com.okay.testcenter.domain.ui.WebReportDesc;
import com.okay.testcenter.service.SendEmailService;
import com.okay.testcenter.service.webUi.WebReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "UI自动化报告")
@Controller()
@RequestMapping(value = "/WebUi")
public class WebUiReportReportController {

    @Autowired
    WebReportService webReportService;


    @Autowired
    SendEmailService sendEmailService;


    @GetMapping(value = "/report/list")
    public String webUiReportByPage(Model model, WebReport webReport, Page page) {
        List<WebReport> webReports = webReportService.getWebReportList(webReport);
        PageInfo<WebReport> pageInfo = new PageInfo<>(webReports);
        model.addAttribute("webReports", webReports);
        model.addAttribute("pageInfo", pageInfo);
        return "webUI/WebUiReport";
    }


    @GetMapping(value = "/reportDesc/list")
    public String webUiReportDescByPage(Model model, @RequestParam(value = "id", required = false) Integer webReportId, Page page) {
        WebReportDesc webReportDesc = new WebReportDesc();
        webReportDesc.setWebReportId(webReportId);
        WebReport webReport = new WebReport();
        webReport.setId(webReportId);
        List<WebReportDesc> webReportDescs = webReportService.getWebReportDescList(webReportDesc);
        List<WebReport> webReports = webReportService.getWebReportList(webReport);
        PageInfo<WebReportDesc> pageInfo = new PageInfo<>(webReportDescs);
        model.addAttribute("webReport", webReports.get(0));
        model.addAttribute("webReportDescs", webReportDescs);
        model.addAttribute("pageInfo", pageInfo);
        return "webUI/WebUiReportDesc";
    }

    @ApiOperation(value = "发送WEB自动化邮件", notes = "发送WEB自动化邮件")
    @ResponseBody
    @PostMapping("/sendEmail")
    public RetResult<Object> sendWebEmail(@RequestBody JSONObject requestJson) {
        boolean b=sendEmailService.sendEmailWebUi(requestJson);
        RetResult retResult = new RetResult<>();
        if (b) {
            retResult.setCode(200);
            retResult.setMsg("发送邮件成功");
        } else {
            retResult.setCode(400);
            retResult.setMsg("发送邮件失败");
        }
        return retResult;
    }
}
