package com.okay.testcenter.controller.middle;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.okay.testcenter.domain.Env;
import com.okay.testcenter.domain.Page;
import com.okay.testcenter.domain.RetResponse;
import com.okay.testcenter.domain.RetResult;
import com.okay.testcenter.domain.middle.*;
import com.okay.testcenter.service.middle.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "中间层用例接口")
@Controller
@RequestMapping(value = "/middle")
public class MiddleCaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    MiddleProjectService middleProjectService;
    @Autowired
    MiddleModuleService middleModuleService;
    @Autowired
    MiddleInterfaceService middleInterfaceService;
    @Autowired
    MiddleCaseService middleCaseService;
    @Autowired
    EnvService envService;

    @ApiOperation(value = "添加中间层用例", notes = "添加中间层用例")
    @PostMapping(value = "/addMiddleCase")
    @ResponseBody
    public RetResult<Object> addMiddleCase(@Validated @RequestBody MiddleCase middleCase) {
        String userName = "";
        if (SecurityUtils.getSubject().getPrincipal() != null && !SecurityUtils.getSubject().getPrincipal().equals("")) {
            String userMsg = SecurityUtils.getSubject().getPrincipal().toString();
            userName = userMsg.substring(0, userMsg.indexOf("-"));
        }
        if (userName.equals("")) {
            return RetResponse.makeErrRsp("请检查当前用户信息");
        }
        middleCase.setUserName(userName);
        middleCaseService.insertMiddleCase(middleCase);

        return RetResponse.makeOKRsp();
    }

    @ApiOperation(value = "更新中间层用例", notes = "更新中间层用例")
    @PostMapping(value = "/updateMiddleCase")
    @ResponseBody
    public RetResult<Object> updateMiddleCase(@Validated @RequestBody MiddleCase middleCase) {

        String userName = null;
        if (SecurityUtils.getSubject().getPrincipal() != null && !SecurityUtils.getSubject().getPrincipal().equals("")) {
            String userMsg = SecurityUtils.getSubject().getPrincipal().toString();
            userName = userMsg.substring(0, userMsg.indexOf("-"));
        }
        if (userName == null) {
            return RetResponse.makeErrRsp("请检查当前用户信息");
        }
        middleCase.setUserName(userName);
        middleCaseService.updateMiddleCase(middleCase);
        return RetResponse.makeOKRsp();
    }

    @ApiOperation(value = "删除中间层用例", notes = "删除中间层用例")
    @PostMapping(value = "/deleteMiddleCase")
    @ResponseBody
    public RetResult<Object> deleteMiddleCase(@Validated @RequestBody JSONObject request) {

        int id = request.getInteger("id");
        middleCaseService.deleteMiddleCase(id);
        return RetResponse.makeOKRsp();
    }

    @ApiOperation(value = "通过ID查找中间层用例", notes = "通过ID查找中间层用例")
    @GetMapping(value = "/queryCaseById")
    @ResponseBody
    public MiddleCase queryCaseById(@Validated Integer id) {

        MiddleCase middleCaseList = middleCaseService.findMiddleCaseById(id);
        return middleCaseList;
    }

    @ApiOperation(value = "通过名称查找中间层用例", notes = "通过名称查找中间层用例")
    @GetMapping(value = "/queryCaseByName")
    @ResponseBody
    public String queryCaseByName(@Validated String caseName) {
        List<MiddleCase> middleCaseList;

        if (caseName.contains("/")) {

            middleCaseList = middleCaseService.findMiddleCaseByPath(caseName);

        } else {

            middleCaseList = middleCaseService.findMiddleCaseByName(caseName);
        }

        return JSONObject.toJSONString(middleCaseList);
    }


    @GetMapping(value = "/case/list")
    public String caseList(Model model) {

        List<MiddleProject> middleProjectList = middleProjectService.findMiddleProjectList();
        model.addAttribute("middleProjectList", middleProjectList);

        List<Env> envList = envService.findEnvList();
        model.addAttribute("envList", envList);

        int projectId = middleProjectList.get(0).getId();
        int envId = envList.get(0).getId();

        List<MiddleModule> moduleList = middleModuleService.findMiddleModuleByProjectId(projectId);
        model.addAttribute("moduleList", moduleList);

        int moduleId = moduleList.get(0).getId();
        List<MiddleInterface> interfaceList = middleInterfaceService.findMiddleInterfaceByModuleId(moduleId);
        model.addAttribute("interfaceList", interfaceList);

        PageInfo<MiddleCase> middleCasePageInfo = middleCaseService.findMiddleCaseByEnvAndInterface(envId, projectId, 1, 10);
        model.addAttribute("middleCasePageInfo", middleCasePageInfo);


        return "middle/MiddleInterfaceCase";
    }


    @GetMapping(value = "/case/run")
    public String caseRun(Model model) {

        List<MiddleProject> middleProjectList = middleProjectService.findMiddleProjectList();
        model.addAttribute("middleProjectList", middleProjectList);

        List<Env> envList = envService.findEnvList();
        model.addAttribute("envList", envList);

        int projectId = middleProjectList.get(0).getId();
        int envId = envList.get(0).getId();

        List<MiddleModule> moduleList = middleModuleService.findMiddleModuleByProjectId(projectId);
        model.addAttribute("moduleList", moduleList);

        int moduleId = moduleList.get(0).getId();
        List<MiddleInterface> interfaceList = middleInterfaceService.findMiddleInterfaceByModuleId(moduleId);
        model.addAttribute("interfaceList", interfaceList);

        PageInfo<MiddleCase> middleCasePageInfo = middleCaseService.findMiddleCaseByEnvAndInterface(envId, projectId, 1, 10);
        model.addAttribute("middleCasePageInfo", middleCasePageInfo);


        return "middle/MiddleCaseRun";
    }


    @GetMapping(value = "/param/rule")
    public String paramRule(Integer id, Model model) {
        MiddleCase middleCase = middleCaseService.findMiddleCaseById(id);
        MiddleInterface middleInterface = middleInterfaceService.findMiddleInterfaceById(middleCase.getInterface_id());
        List<MiddleCaseRule> middleCaseRules = middleCaseService.getMiddleCaseRule(middleCase.getRequest_data(), middleInterface.getRequest_method());
        model.addAttribute("middleCase", middleCase);
        model.addAttribute("middleCaseRules", middleCaseRules);
        return "middle/MiddleParamRule";
    }

    @ApiOperation(value = "自动生成中间层用例", notes = "自动生成中间层用例")
    @ResponseBody
    @PostMapping(value = "/param/autoCaseGen")
    public String paramAnalysis(String middleCaseRules, Integer id, Model model) {
        List<MiddleCaseRule> genMiddleCaseRules = JSONObject.parseArray(middleCaseRules, MiddleCaseRule.class);
        MiddleCase oldMiddleCase = middleCaseService.findMiddleCaseById(id);
        MiddleInterface middleInterface = middleInterfaceService.findMiddleInterfaceById(oldMiddleCase.getInterface_id());
        List<MiddleCase> middleCases = middleCaseService.genMiddleCase(genMiddleCaseRules, oldMiddleCase.getRequest_data(), middleInterface.getRequest_method(), oldMiddleCase);
        System.out.println(middleCases);
        model.addAttribute("middleCases", middleCases);
        model.addAttribute("middleCaseRules", genMiddleCaseRules);
        model.addAttribute("middleCase", oldMiddleCase);
        return JSONObject.toJSONString(middleCases);
    }

    @ApiOperation(value = "通过环境ID和接口ID获取中间层用例列表", notes = "通过环境ID和接口ID获取中间层用例列表")
    @GetMapping(value = "/case/getlist")
    @ResponseBody
    public String caseGetList(Integer env_id, Integer interface_id, Integer currentPage, Integer pageSize) {

        PageInfo<MiddleCase> middleCasePageInfo = middleCaseService.findMiddleCaseByEnvAndInterface(env_id, interface_id, currentPage, pageSize);

        return JSONObject.toJSONString(middleCasePageInfo);
    }

    @ApiOperation(value = "获取中间层用例列表", notes = "获取中间层用例列表")
    @GetMapping(value = "/middleCaseList")
    @ResponseBody
    public PageInfo middleCaseList(Page page) {

        PageInfo middleCaseList = middleCaseService.findMiddleCaseList(page.getCurrentPage(), page.getPageSize());

        return middleCaseList;
    }


    @ApiOperation(value = "同步不同环境的测试用例", notes = "同步不同环境的测试用例")
    @GetMapping(value = "/syncMiddleCase")
    @ResponseBody
    public RetResult<Object> syncMiddleCase(@Validated int projectId, int fromEnvId, int toEnvId) {

        middleCaseService.syncMiddleCaseByProject(projectId, fromEnvId, toEnvId);
        return RetResponse.makeOKRsp();
    }


    @ApiOperation(value = "同步用例数据", notes = "同步用例数据")
    @GetMapping(value = "/sync/middlecase")
    public String syncData(Model model) {

        List<Env> envList = envService.findEnvList();
        model.addAttribute("envList", envList);
        List<MiddleProject> middleProjectList = middleProjectService.findMiddleProjectList();
        model.addAttribute("middleProjectList", middleProjectList);

        return "middle/MiddleInterfaceCaseSync";
    }


}
