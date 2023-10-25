package com.okay.testcenter.controller.ui;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.okay.testcenter.domain.Page;
import com.okay.testcenter.domain.RetResponse;
import com.okay.testcenter.domain.RetResult;
import com.okay.testcenter.domain.ui.PadAutoCase;
import com.okay.testcenter.service.ui.PadAutoCaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(description = "Pad自动化用例接口")
@Controller
@RequestMapping(value = "/uiPad")
public class UiPadCaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    PadAutoCaseService padAutoCaseService;


    @GetMapping(value = "/getDeviceList")
    public String getDeviceList() {

        return "autoui/DeviceList";
    }


    @GetMapping(value = "/case/list")
    public String padAutoCaseList(Model model) {

        PageInfo padAutoCaseList = padAutoCaseService.findCaseList(1,20);
        logger.info("[padAutoCaseList]:" + padAutoCaseList);
        model.addAttribute("padAutoCaseList", padAutoCaseList);

        return "autoui/CaseEdit";
    }

    @ApiOperation(value = "获取PAD自动化用例列表", notes = "获取PAD自动化用例列表")
    @GetMapping(value = "/case/getlist")
    @ResponseBody
    public String getPadlist(@Validated Page page) {

        PageInfo padAutoCaseList = padAutoCaseService.findCaseList(page.getCurrentPage(), page.getPageSize());

        return JSONObject.toJSONString(padAutoCaseList);
    }

    @ApiOperation(value = "通过名称查找PAD自动化用例", notes = "通过名称查找PAD自动化用例")
    @GetMapping(value = "/findCaseByName")
    @ResponseBody
    public String findCaseByName(String caseName) {

        List<PadAutoCase> padAutoCaseList = padAutoCaseService.findCaseByName(caseName);
        logger.info("[padAutoCaseList]==" +  JSONObject.toJSONString(padAutoCaseList));
        return JSONObject.toJSONString(padAutoCaseList);
    }


    @GetMapping(value = "/case/runlist")
    public String padAutoCaseRun(Model model) {

        PageInfo padAutoCaseList = padAutoCaseService.findCaseList(1,20);
        logger.info("[padAutoCaseList]:" + padAutoCaseList);
        model.addAttribute("padAutoCaseList", padAutoCaseList);

        return "autoui/RunCase";
    }

    @ApiOperation(value = "获取PAD自动化用例列表", notes = "获取PAD自动化用例列表")
    @GetMapping(value = "/case/getRunList")
    @ResponseBody
    public String getRunPadList(@Validated Page page) {

        PageInfo padAutoCaseList = padAutoCaseService.findCaseList(page.getCurrentPage(), page.getPageSize());
        return JSONObject.toJSONString(padAutoCaseList);
    }

    @ApiOperation(value = "添加PAD自动化用例", notes = "添加PAD自动化用例")
    @PostMapping(value = "/case/insert")
    @ResponseBody
    public RetResult<Object> insertPadAutoCase(@Validated @RequestBody PadAutoCase padAutoCase) {

        padAutoCaseService.insertCase(padAutoCase);
        return RetResponse.makeOKRsp();

    }

    @ApiOperation(value = "更新PAD自动化用例", notes = "更新PAD自动化用例")
    @PostMapping(value = "/case/update")
    @ResponseBody
    public RetResult<Object> updatePadAutoCase(@Validated @RequestBody PadAutoCase padAutoCase) {

        padAutoCaseService.updateCase(padAutoCase);
        return RetResponse.makeOKRsp();
    }

    @ApiOperation(value = "删除PAD自动化用例", notes = "删除PAD自动化用例")
    @PostMapping(value = "/case/delete")
    @ResponseBody
    public RetResult<Object> deletePadCase(@Validated @RequestBody JSONObject request) {
        int id = Integer.parseInt(request.get("id").toString());
        padAutoCaseService.deleteCase(id);
        return RetResponse.makeOKRsp();
    }


}
