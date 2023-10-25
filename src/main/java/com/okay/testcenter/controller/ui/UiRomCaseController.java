package com.okay.testcenter.controller.ui;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.okay.testcenter.domain.Page;
import com.okay.testcenter.domain.RetResponse;
import com.okay.testcenter.domain.RetResult;
import com.okay.testcenter.domain.ui.PadAutoCase;
import com.okay.testcenter.service.ui.RomAutoCaseService;
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

@Api(description = "Rom自动化用例接口")
@Controller
@RequestMapping(value = "/uiRom")
public class UiRomCaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    RomAutoCaseService romAutoCaseService;


    @GetMapping(value = "/getDeviceList")
    public String getRomDeviceList() {

        return "autoui/RomDeviceList";
    }


    @GetMapping(value = "/case/list")
    public String romAutoCaseList(Model model) {

        PageInfo padAutoCaseList = romAutoCaseService.findCaseList(1, 20);
        logger.info("[padAutoCaseList]:" + padAutoCaseList);
        model.addAttribute("padAutoCaseList", padAutoCaseList);

        return "autoui/RomCaseEdit";
    }

    @ApiOperation(value = "获取ROM自动化用例列表", notes = "获取ROM自动化用例列表")
    @GetMapping(value = "/case/getlist")
    @ResponseBody
    public String getRomlist(@Validated Page page) {

        PageInfo padAutoCaseList = romAutoCaseService.findCaseList(page.getCurrentPage(), page.getPageSize());

        return JSONObject.toJSONString(padAutoCaseList);
    }

    @ApiOperation(value = "通过名称查找ROM自动化用例", notes = "通过名称查找ROM自动化用例")
    @GetMapping(value = "/findCaseByName")
    @ResponseBody
    public String findCaseByName(String caseName) {

        List<PadAutoCase> padAutoCaseList = romAutoCaseService.findCaseByName(caseName);
        logger.info("[padAutoCaseList]==" + JSONObject.toJSONString(padAutoCaseList));
        return JSONObject.toJSONString(padAutoCaseList);
    }


    @GetMapping(value = "/case/runlist")
    public String romAutoCaseRun(Model model) {

        PageInfo padAutoCaseList = romAutoCaseService.findCaseList(1, 20);
        logger.info("[padAutoCaseList]:" + padAutoCaseList);
        model.addAttribute("padAutoCaseList", padAutoCaseList);

        return "autoui/RomRunCase";
    }

    @ApiOperation(value = "获取ROM自动化用例列表", notes = "获取ROM自动化用例列表")
    @GetMapping(value = "/case/getRunList")
    @ResponseBody
    public String getRunRomList(@Validated Page page) {

        PageInfo padAutoCaseList = romAutoCaseService.findCaseList(page.getCurrentPage(), page.getPageSize());
        return JSONObject.toJSONString(padAutoCaseList);
    }

    @ApiOperation(value = "添加ROM自动化用例", notes = "添加ROM自动化用例")
    @PostMapping(value = "/case/insert")
    @ResponseBody
    public RetResult<Object> insertRomAutoCase(@Validated @RequestBody PadAutoCase padAutoCase) {

        romAutoCaseService.insertCase(padAutoCase);
        return RetResponse.makeOKRsp();

    }

    @ApiOperation(value = "更新ROM自动化用例", notes = "更新ROM自动化用例")
    @PostMapping(value = "/case/update")
    @ResponseBody
    public RetResult<Object> updateRomAutoCase(@Validated @RequestBody PadAutoCase padAutoCase) {

        romAutoCaseService.updateCase(padAutoCase);
        return RetResponse.makeOKRsp();
    }

    @ApiOperation(value = "删除ROM自动化用例", notes = "删除ROM自动化用例")
    @PostMapping(value = "/case/delete")
    @ResponseBody
    public RetResult<Object> deleteRomCase(@Validated @RequestBody JSONObject request) {
        int id = Integer.parseInt(request.get("id").toString());
        romAutoCaseService.deleteCase(id);
        return RetResponse.makeOKRsp();
    }


}
