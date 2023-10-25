package com.okay.testcenter.controller.middle;


import com.alibaba.fastjson.JSONObject;
import com.okay.testcenter.domain.RetResponse;
import com.okay.testcenter.domain.RetResult;
import com.okay.testcenter.domain.middle.MiddleInterface;
import com.okay.testcenter.domain.middle.MiddleModule;
import com.okay.testcenter.domain.middle.MiddleProject;
import com.okay.testcenter.domain.report.MiddleTestHistory;
import com.okay.testcenter.service.middle.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author asia
 * @date 2019-10-22
 */
@Api(description = "中间层运行接口")
@Controller
@RequestMapping(value = "/middle")
public class MiddleInterfaceRunController {

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
    MiddleTestHistoryService middleTestHistoryService;
    @Autowired
    MiddleRequestHisoryService middleRequestHisoryService;

    @Resource
    MiddleRunService middleRunService;
    @Resource
    EnvService envService;

    @GetMapping(value = "/run")
    public String run(Model model) {

        List<MiddleProject> middleProjectList = middleProjectService.findMiddleProjectList();
        model.addAttribute("middleProjectList", middleProjectList);
        return "middle/MiddleInterfaceTest";
    }

    /**
     * 调试中间层接口的方法
     * @param request
     * @return
     */
    @ApiOperation(value = "调试中间层接口", notes = "调试中间层接口")
    @PostMapping(value = "runMiddleInterface")
    @ResponseBody
    public Object runMiddleInterface(@Validated @RequestBody JSONObject request) {
        return middleRunService.debugInterface(request);
    }

    /**
     * 运行单条用例的方法
     * @param id
     * @return
     */
    @ApiOperation(value = "通过ID运行中间层用例", notes = "通过ID运行中间层用例")
    @GetMapping(value = "/runMiddleSingle")
    @ResponseBody
    public Object runMiddleSingle(@Validated int id) {

        return middleRunService.runSingle(id);
    }

    /**
     * 运行整个项目的方法
     * @param env_id
     * @param project_id
     * @return
     */
    @ApiOperation(value = "通过项目ID和环境ID运行中间层用例", notes = "通过项目ID和环境ID运行中间层用例")
    @GetMapping(value = "/runMiddleProject")
    @ResponseBody
    public RetResult<Object> runMiddleProject(@Validated int env_id, int project_id) {
        middleRunService.runProject(env_id, project_id, -1);
        return RetResponse.makeOKRsp();

    }


    /**
     * 运行整个项目的方法
     * @param env_id
     * @param project_id
     * @return
     */
    @ApiOperation(value = "通过项目ID和环境ID和用例类型运行中间层用例", notes = "通过项目ID和环境ID和用例类型运行中间层用例")
    @GetMapping(value = "/runMiddleProjectByType")
    @ResponseBody
    public RetResult<Object> runMiddleProjectByType(@Validated int env_id, int project_id,int type) {
        middleRunService.runProject(env_id, project_id, type);
        return RetResponse.makeOKRsp();

    }



    /**
     * 运行模块的方法
     *
     * @param env_id
     * @param module_id
     * @return
     */
    @ApiOperation(value = "通过模块ID和环境ID运行中间层用例", notes = "通过模块ID和环境ID运行中间层用例")
    @GetMapping(value = "/runMiddleModule")
    @ResponseBody
    public RetResult<Object> runMiddleModule(@Validated int env_id, int module_id) {
        middleRunService.runModule(env_id, module_id,-1);
        return RetResponse.makeOKRsp();

    }

    /**
     * 运行模块的方法
     *
     * @param env_id
     * @param module_id
     * @return
     */
    @ApiOperation(value = "通过模块ID/环境ID和类型运行中间层用例", notes = "通过模块ID/环境ID和类型运行中间层用例")
    @GetMapping(value = "/runMiddleModuleByType")
    @ResponseBody
    public RetResult<Object> runMiddleModule(@Validated int env_id, int module_id,int type) {
        middleRunService.runModule(env_id, module_id,type);
        return RetResponse.makeOKRsp();

    }

    /**
     * 运行接口的方法
     *
     * @param env_id
     * @param interface_id
     * @return
     */
    @ApiOperation(value = "通过接口ID/环境ID和类型运行中间层用例", notes = "通过接口ID/环境ID和类型运行中间层用例")
    @GetMapping(value = "/runMiddleInterfaceByType")
    @ResponseBody
    public RetResult<Object> runMiddleInterfaceByType(@Validated int env_id, int interface_id,int type) {
        middleRunService.runInterface(env_id, interface_id,type);
        return RetResponse.makeOKRsp();

    }





    @ApiOperation(value = "中间层接口列表", notes = "中间层接口列表")
    @GetMapping(value = "/interfacelist")
    @ResponseBody
    public Object interfacelist(@Validated String project_name) {

       List<MiddleInterface> middleInterfaces = new ArrayList<>();

      MiddleProject middleProject = middleProjectService.findMiddleProjectByName(project_name);
      if(middleProject == null){
          return RetResponse.makeErrRsp("project_name is error");
      }
      int project_id = middleProject.getId();
      List<MiddleModule> middleModuleList = middleModuleService.findMiddleModuleByProjectId(project_id);
      for(MiddleModule middleModule : middleModuleList){

          List<MiddleInterface> middleInterfaceList = middleInterfaceService.findMiddleInterfaceByModuleId(middleModule.getId());
          middleInterfaces.addAll(middleInterfaceList);

      }


     return RetResponse.makeOKRsp(middleInterfaces);

    }


    /**
     * 给DevOps提供的接口
     * @param env_name
     * @param project_name
     * @return
     */
    @ApiOperation(value = "Devops运行中间层用例", notes = "Devops运行中间层用例")
    @GetMapping(value = "/devops/run")
    @ResponseBody
    public RetResult<Object> runMiddleProjectByType(@Validated String env_name, String project_name) throws ExecutionException, InterruptedException {

        int env_id = envService.findEnvByName(env_name).getId();
        int project_id = middleProjectService.findMiddleProjectByName(project_name).getId();

        CompletableFuture<MiddleTestHistory> compHistory = middleRunService.runMonitorProject(env_id, project_id, 0);
        CompletableFuture.allOf(compHistory).join();
        MiddleTestHistory history = compHistory.get();
        if(history.getResult().equals("FAIL") || history.getResult().equals("登录失败")){

            return RetResponse.makeErrRsp(JSONObject.toJSONString(history));
        }else {
            return RetResponse.makeOKRsp(history);
        }


    }



}
